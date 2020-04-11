import socket
import json
import threading
from functools import partial

from app.model import DrawingData


IP_ADDRESS = socket.gethostname()
TCP_PORT = 6969


class DictSocket:
    """Generic class for sending and receiving python dictionaries througth
    a TCP socket"""
    def __init__(self, clientsocket=None):
        """Constructor, it can recieve an already connected socket as a
        parameter, if no socket is passed, it creates and connects a socket"""
        if clientsocket is None:
            clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            clientsocket.connect((IP_ADDRESS, TCP_PORT))
        self.socket = clientsocket
        self.is_alive = True
    
    def send_dict(self, data: dict):
        """Sends a dict throught the network"""
        json_str = json.dumps(data)
        self._send_str(json_str)
        
    def listen(self, callback):
        """Listens for the TCP input stream when a json is received parses it
        to a dict and passes it to the callback passed in the parameters"""
        forwarder_thread = threading.Thread(
           target=lambda: self._forward_to_func(callback),
           daemon=True)
        forwarder_thread.start()
            
    def _forward_to_func(self, func):
        while self.is_alive:
            func(self.read_dict())
    
    def read_dict(self):
        """Reads a string from the TCP input stream if its not empty parses the
        json and return it parsed as Python dict"""
        string = ''
        empty_count = -1
        while not string:
            empty_count += 1
            string = self._read_str()
            if empty_count == 5:
                self.is_alive = False
        data = json.loads(string)
        return data

    def _send_str(self, string: str):
        f = self.socket.makefile('w')
        f.write(f'{string}\n')
        f.close()
        
    def _read_str(self):
        f = self.socket.makefile('r')
        string = f.readline()
        f.close()
        return string.strip()


class ChatClientSocket:
    """Comunication class used by the client"""
    def __init__(self, username):
        self.username = username
        self.dict_socket = DictSocket()
        self.dict_socket.send_dict({'type':'login', 'username': self.username})
        response = self.dict_socket.read_dict()
        if response['type'] == 'login_deny':
            raise ConnectionError(response['message'])
        self.dict_socket.listen(self.on_server_dict)

    def send_drawing_data(self, drawing_data: DrawingData, to: list):
        self.dict_socket.send_dict({
            'from': self.username,
            'to': to,
            **vars(drawing_data)
        })

    def create_room(self, peers):
        self.dict_socket.send_dict({
            'type':'create_room',
            'username': self.username,
            'peers': peers,
        })

    def on_server_dict(self, server_dict):
        del server_dict['to']
        del server_dict['from']
        drawing_data = DrawingData(**server_dict)
        for callback in self.callbacks:
            callback(drawing_data)



class ChatServerSocket:
    def __init__(self):
        self.active_clients = {}
        self.rooms = {}

        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind((IP_ADDRESS, TCP_PORT))
        self.socket.listen(5)
    
    def serve_forever(self):
        while True:
            (clientsocket, address) = self.socket.accept() # Accept connection
            dict_socket = DictSocket(clientsocket) # Create dict socket

            # Listens for all packages from socket
            dict_socket.listen(partial(self._process_recived, socket=dict_socket))
    
    def _process_recived(self, data: dict, socket: DictSocket):
        print(data)

        paquet_type = data['type']
        if paquet_type == 'login':
            username = data['username']
            if username in self.active_clients and self.active_clients[username].is_alive:
                socket.send_dict({'type': 'login_deny', 'message': 'Username already in use'})
                return
            self.active_clients[username] = socket
            socket.send_dict({'type': 'login_accept'})
            return

        if paquet_type == 'create_room':
            print('Creating room')
            return

        destinations = data['to']
        for dest in destinations:
            if dest in self.active_clients:
                self.active_clients[dest].send_dict(data)