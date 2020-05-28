import tkinter as tk
import pickle
import logging
from urllib import request, parse
from threading import Thread
from functools import partial

from app import coms
from app.interface import LoginFrame, ChatSelectionFrame, CanvasFrame

COLORS = ['red', 'blue', 'cyan', 'green', 'yellow', 'orange', 'black', 'white']
DEFAULT_FONT = ('Monospace', 20)

logging.basicConfig(level=logging.INFO)


class App:
    def __init__(self):
        self.root = tk.Tk()
        self.root.title('Drawbook')
        self.root.geometry('800x600')
        self.main_container = tk.Frame(self.root)
        self.main_container.pack(fill='both', expand=True)
        self.main_container.grid_rowconfigure(0, weight=1)
        self.main_container.grid_columnconfigure(0, weight=1)
        self.frames = {}
        for Frame in [LoginFrame, ChatSelectionFrame, CanvasFrame]:
            self.frames[Frame.__name__] = Frame(master=self.main_container, controller=self)
        self.frames['LoginFrame'].tkraise()

        self.socket = None
        self.username = ''

    def login(self, address:str, port:int, username:str):
        self.socket = coms.DrawBookSocket()
        self.socket.connect(address, port)
        self.socket.send({'type':'login', 'username':username})
        login_response = self.socket.recv()
        if login_response['type'] == 'login_accept':
            self.username = username
            self.frames['ChatSelectionFrame'].tkraise()

    def create_room(self):
        self.socket.send({'type':'room_create', 'username':self.username})
        creation_response = self.socket.recv()
        if creation_response['type'] == 'room_accept':
            self.frames['CanvasFrame'].room = self.username
            self.frames['CanvasFrame'].tkraise()
            self.frames['CanvasFrame'].listen()

    def join_room(self, room):
        self.socket.send({'type':'room_join', 'room':room, 'username':self.username})
        join_response = self.socket.recv()
        if join_response['type'] == 'room_accept':
            self.frames['CanvasFrame'].room = room
            self.frames['CanvasFrame'].tkraise()
            self.frames['CanvasFrame'].listen()

def run():
    app = App()
    app.root.mainloop()