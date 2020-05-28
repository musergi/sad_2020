import socket
import json
import threading
import logging
from functools import partial


class DrawBookSocket:
    def __init__(self, clientsocket=None):
        self.socket = clientsocket
        if clientsocket is None:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.recv_buffer = b''

    def connect(self, host:str, port:int):
        self.socket.connect((host, port))

    def send(self, data:dict):
        json_str = json.dumps(data, ensure_ascii=True) + '\n'
        self.socket.sendall(json_str.encode(encoding='ascii'))

    def recv(self) -> dict:
        while True:
            self.recv_buffer += self.socket.recv(1024)
            if b'\n' in self.recv_buffer:
                json_data = self.recv_buffer.splitlines()[0]
                self.recv_buffer = self.recv_buffer[len(json_data) + 1:]
                return json.loads(json_data.decode(encoding='ascii'))


class DrawBookServer:
    def __init__(self):
        self.rooms = DrawingRooms()
        self.sockets = {}
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind((socket.gethostname(), 4444))
        self.socket.listen(5)

    def serve_forever(self):
        while True:
            clientsocket, _ = self.socket.accept()
            threading.Thread(
                target=lambda:self.serve_client(clientsocket),
                daemon=True).start()

    def serve_client(self, clientsocket):
        s = DrawBookSocket(clientsocket)
        login_info = s.recv()
        self.sockets[login_info['username']] = s
        s.send({'type': 'login_accept'})
        while True:
            data = s.recv()
            logging.info(data)
            if data['type'] == 'room_create':
                if self.rooms.attempt_add(data['username']):
                    s.send({'type':'room_accept'})
                else:
                    s.send({'type':'room_deny'})
            if data['type'] == 'room_join':
                if self.rooms.attempt_join(data['room'], data['username']):
                    s.send({'type':'room_accept'})
                else:
                    s.send({'type':'room_deny'})
            if data['type'] == 'draw_data':
                for other in self.rooms.get_others(data['room'], data['username']):
                    self.sockets[other].send(data)
            

class DrawingRooms:
    def __init__(self):
        self.rooms = {}
        self.lock = threading.RLock()

    def attempt_add(self, owner):
        with self.lock:
            if owner in self.rooms:
                return False
            self.rooms[owner] = {owner}
            return True

    def attempt_join(self, room, client):
        with self.lock:
            if room not in self.rooms:
                return False
            self.rooms[room].add(client)
            return True

    def get_others(self, room, client):
        with self.lock:
            return self.rooms[room] - {client}