import socket
import pickle
import threading


BUFFER_SIZE = 20000
DEFAULT_ADDRESS = socket.gethostname()
DEFAULT_PORT = 6969


class ChatSocket:
    def __init__(self, username: str, address: str = DEFAULT_ADDRESS, port: int = DEFAULT_PORT):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect((address, port))
        self.send_object(LoginData(username=username))

    def send_object(self, data):
        serialized_data = pickle.dumps(data)
        if len(serialized_data) > BUFFER_SIZE:
            raise IOError('Send buffer overflown')
        self.socket.send(serialized_data)

    def _listen_object(self, callback):
        raw_data = self.socket.recv(BUFFER_SIZE)
        data = pickle.loads(raw_data)
        callback(data)




class ChatServerSocket:
    def __init__(self, address: str = DEFAULT_ADDRESS, port: int = DEFAULT_PORT):
        self.serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.serversocket.bind((address, port))
        self.serversocket.listen(10)

    def serve_forever(self):
        while True:
            (clientsocket, address) = self.serversocket.accept()
            threading.Thread(target=lambda: self._serve(clientsocket),
                daemon=True).start()

    def _serve(self, clientsocket):
        raw_login_data = clientsocket.recv(BUFFER_SIZE)
        print(len(raw_login_data))
        login_data = pickle.loads(raw_login_data)
        print(login_data)
        while True:
            recv_data = clientsocket.recv(BUFFER_SIZE)
            print(len(recv_data))
            data = pickle.loads(recv_data)
            print(data)
            clientsocket.send(recv_data)


class LoginData:
    def __init__(self, username):
        self.username = username

    def __str__(self):
        return f'LoginData({self.username})'


class DrawingData:
    def __init__(self):
        self.origin = 'musergi'
        self.destination = ['norma']
        self.shape = 'circle'
        self.position = (0, 0)
        self.color = 'red'

    def __str__(self):
        dest_string = ','.join(self.destination)
        return f'Draw({self.origin},[{dest_string}],{self.shape},{self.position},{self.color})'