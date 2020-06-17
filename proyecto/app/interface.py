import threading
import socket
import tkinter as tk

from functools import partial

D_FONT = ('Monospace', 18)
COLORS = ['red', 'black', 'blue', 'green', 'yellow', 'orange', 'lightblue']


class BaseFrame(tk.Frame):
    def __init__(self, master, controller, **kw):
        tk.Frame.__init__(self, master, **kw)
        self.grid(row=0, column=0, sticky=tk.NSEW)
        #self.pack(fill='both', expand=True)
        self.container = tk.Frame(self)
        self.container.place(relx=0.5, rely=0.5, anchor=tk.CENTER)
        self.controller = controller


class Field(tk.Frame):
    def __init__(self, master, name='Field', default_val='', **kw):
        tk.Frame.__init__(self, master, **kw)
        self.var = tk.StringVar()
        self.var.set(default_val)
        tk.Label(self, font=D_FONT, text=name).grid(row=0, column=0)
        tk.Entry(self, font=D_FONT, textvariable=self.var).grid(row=1, column=0)


class Button(tk.Button):
    def __init__(self, master, text='Button', command=lambda:print('Pressed'), **kw):
        tk.Button.__init__(self, master, text=text, font=D_FONT, command=command, **kw)
        self.bind('<Return>', lambda e: command()) # Added return event to support tab navigation


class LoginFrame(BaseFrame):
    def __init__(self, master, controller, **kw):
        BaseFrame.__init__(self, master, controller, **kw)
        self.server = Field(self.container, name='Server Address', default_val=socket.gethostname())
        self.port = Field(self.container, name='Port', default_val='4444')
        self.username = Field(self.container, name='Username')
        self.server.pack(side='top', pady=10)
        self.port.pack(side='top', pady=10)
        self.username.pack(side='top', pady=40)
        Button(self.container, text='Login', command=self.on_click).pack(side='top')

    def on_click(self):
        threading.Thread(target=lambda: self.controller.login(
            address=self.server.var.get(),
            port=int(self.port.var.get()),
            username=self.username.var.get()
        ), daemon=True).start()


class ChatSelectionFrame(BaseFrame):
    def __init__(self, master, controller, **kw):
        BaseFrame.__init__(self, master, controller, **kw)
        Button(self.container, text='Create room', command=self.on_create_room).pack(side='top', pady=30)
        self.joined_room = Field(self.container, name='Join room')
        self.joined_room.pack(side='top')
        Button(self.container, text='Join', command=self.on_join_room).pack(side='top')

    def on_create_room(self):
        threading.Thread(target=self.controller.create_room, daemon=True).start()

    def on_join_room(self):
        threading.Thread(target=lambda: self.controller.join_room(self.joined_room.var.get()), daemon=True).start()


class CanvasFrame(BaseFrame):
    def __init__(self, master, controller, **kw):
        BaseFrame.__init__(self, master, controller, **kw)
        self.canvas = tk.Canvas(self.container, width=600, height=600, bg='white')
        self.canvas.bind('<Button-1>', self.on_canvas_press)
        self.canvas.bind('<B1-Motion>', self.on_canvas_press)
        self.canvas.grid(row=0, column=0, rowspan=len(COLORS))
        for i, color in enumerate(COLORS):
            tk.Button(self.container, bg=color, width=1, height=1, command=partial(self.set_color, color)).grid(row=i, column=1)
        self.room = None
        self.color = COLORS[0]

    def on_canvas_press(self, event):
        self.canvas.create_oval(event.x - 10, event.y - 10, event.x + 10, event.y + 10, fill=self.color, outline='')
        threading.Thread(target=lambda: self.controller.socket.send({'type':'draw_data', 'x': event.x, 'y':event.y, 'c':self.color, 'room':self.room, 'username':self.controller.username}), daemon=True).start()

    def set_color(self, color):
        self.color = color

    def listen(self):
        while True:
            data = self.controller.socket.recv()
            self.after_idle(lambda: self.canvas.create_oval(data['x'] - 10, data['y'] - 10, data['x'] + 10, data['y'] + 10, fill=data['c'], outline=''))
