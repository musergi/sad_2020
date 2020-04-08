import socket
import json
import threading


from app.model import DrawingData


IP_ADDRESS = socket.gethostname()
TCP_PORT = 6969


class DictSocket:
    def __init__(self, clientsocket=None):
        if clientsocket is None:
            clientsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            clientsocket.connect((IP_ADDRESS, TCP_PORT))
        self.socket = clientsocket
    
    def send_dict(self, data: dict):
        json_str = json.dumps(data)
        self._send_str(json_str)
        
    def listen(self, callback):
       forwarder_thread = threading.Thread(
           target=lambda: self._forward_to_func(callback),
           daemon=True)
       forwarder_thread.start()
            
    def _forward_to_func(self, func):
         while True:
            func(self.send_dictread_dict())
    
    def read_dict(self):
        string = ''
        while not string:
            string = self._read_str()
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
        self.dict_socket.send_dict({'username': self.username})
        self.dict_socket.listen(self.on_server_dict)

    def send_drawing_data(self, drawing_data: DrawingData, to: list):
        self.dict_socket.send_dict({
            'from': self.username,
            'to': to,
            **vars(drawing_data)
        })

    def on_server_dict(self, server_dict):
        print(server_dict)


class ChatServerSocket:
    def __init__(self):
        self.active_clients = {}
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind((IP_ADDRESS, TCP_PORT))
        self.socket.listen(5)
    
    def serve_forever(self):
        while True:
            (clientsocket, address) = self.socket.accept()
            dict_socket = DictSocket(clientsocket)
            username = dict_socket.read_dict()['username']
            self.active_clients[username] = dict_socket
            dict_socket.listen(self._process_recived)
    
    def _process_recived(self, data: dict):
        print(data)
        destinations = data['to']
        for dest in destinations:
            if dest in self.active_clients:
                self.active_clients[dest].send_dict(data)