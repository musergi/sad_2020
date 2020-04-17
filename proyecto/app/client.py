import tkinter as tk
from tkinter import messagebox
import pickle
from urllib import request, parse
from threading import Thread
from functools import partial

from app import coms


COLORS = ['red', 'blue', 'cyan', 'green', 'yellow', 'orange', 'black', 'white']
DEFAULT_FONT = ('Monospace', 20)


class App:
    def __init__(self):
        # Create user interface
        self.root = tk.Tk()
        self.root.title('Draw Chat')
        self.main_container = tk.Frame(self.root)
        self.frames = {}
        self._build_ui()

        self.socket = None

    def _build_ui(self):
        self.main_container.pack(fill='both', expand=True)

        frameTypes = [LoginFrame, StartingFrame, CanvasFrame]
        for FrameType in frameTypes:
            self.frames[FrameType] = FrameType(self.main_container, self)

        self.show_frame(LoginFrame)

    def show_frame(self, frame_class):
        frame = self.frames[frame_class]
        frame.tkraise()

    def attempt_login(self, username, success_callback, error_callback):
        print(username)
        if not username.strip():
            error_callback('Username can not be empty')
            return
        
        try:
            self.socket = coms.ChatClientSocket(username=username)
        except ConnectionRefusedError:
            error_callback('Server unavailable or unreachable')
        except ConnectionError as e:
            error_callback(e)
        self.root.after(0, success_callback)

    def attempt_chat_creation(self):
        self.socket.create_room()
        self.frames[CanvasFrame].room = self.socket.username
        self.socket.callbacks.append(self.frames[CanvasFrame].on_draw_data)
        self.root.after(0, lambda: self.show_frame(CanvasFrame))

    def attempt_chat_join(self, chat_name):
        self.socket.join_room(chat_name, self.on_join_success)

    def on_join_success(self, chat_name):
        self.frames[CanvasFrame].room = chat_name
        self.socket.callbacks.append(self.frames[CanvasFrame].on_draw_data)
        self.root.after(0, lambda: self.show_frame(CanvasFrame))

    def run(self):
        # Enter event loop
        self.root.mainloop()


class LoginFrame(tk.Frame):
    def __init__(self, master, controller: App, *args, **kw):
        super().__init__(master=master, *args, **kw)
        self.grid(row=0, column=0, sticky='nsew')
        
        self.controller = controller
        self.username_entry_var = tk.StringVar()
        self.error_label_content = tk.StringVar()

        self.container = tk.Frame(self)
        self.container.place(relx=0.5, rely=0.5, anchor=tk.CENTER)
        self.username_label = tk.Label(self.container, text='Username', font=DEFAULT_FONT)
        self.username_entry = tk.Entry(self.container, font=DEFAULT_FONT, textvariable=self.username_entry_var)
        self.button = tk.Button(self.container, text='Log in', font=DEFAULT_FONT, command=self.on_button_press)
        self.error_label = tk.Label(self.container, text='', font=DEFAULT_FONT, fg='red', textvariable=self.error_label_content)

        self.username_label.grid(row=0, column=0, pady=20)
        self.username_entry.grid(row=1, column=0, pady=10)
        self.button.grid(row=2, column=0)
        self.error_label.grid(row=3, column=0, pady=40)

    def on_button_press(self):
        Thread(target=lambda: self.controller.attempt_login(
            username=self.username_entry_var.get(),
            success_callback=self.on_login_succes,
            error_callback=self.on_login_fail
        ), daemon=True).start()

    def on_login_succes(self):
        self.controller.show_frame(StartingFrame)

    def on_login_fail(self, error):
        print(error)
        self.error_label_content.set(error)


class StartingFrame(tk.Frame):
    def __init__(self, master, controller, *args, **kw):
        super().__init__(master=master, *args, **kw)
        self.grid(row=0, column=0, sticky='nsew')
        
        self.controller = controller
        self.join_entry_content = tk.StringVar()

        self.container = tk.Frame(self)
        self.container.place(relx=0.5, rely=0.5, anchor=tk.CENTER)

        self.label = tk.Label(self.container, text="Create chat or wait for one to be created", font=DEFAULT_FONT)
        self.create_container = tk.Frame(self.container)
        self.create_button = tk.Button(self.create_container, text='Create chat', font=DEFAULT_FONT, command=self.on_create_chat)
        self.join_container = tk.Frame(self.container)
        self.join_entry = tk.Entry(self.join_container, font=DEFAULT_FONT, textvariable=self.join_entry_content)
        self.join_button = tk.Button(self.join_container, text='Join', font=DEFAULT_FONT, command=self.on_join_chat)

        self.label.grid(row=0, column=0, pady=40)
        self.create_container.grid(row=1, column=0, pady=40)
        self.join_container.grid(row=2, column=0, pady=40)

        self.create_button.pack()

        self.join_entry.grid(row=0, column=0)
        self.join_button.grid(row=1, column=0, pady=10)

    def on_create_chat(self):
        Thread(target=self.controller.attempt_chat_creation, daemon=True).start()

    def on_join_chat(self):
        Thread(target=lambda: self.controller.attempt_chat_join(
            chat_name=self.join_entry_content.get()
        ), daemon=True).start()


class CanvasFrame(tk.Frame):
    def __init__(self, master, controller, *args, **kw):
        super().__init__(master=master, *args, **kw)
        self.grid(row=0, column=0, sticky='nsew')

        self.room = None
        self.controller = controller

        self.canvas = tk.Canvas(self, width=800, height=600)
        self.canvas.bind('<B1-Motion>', self.on_mouse_press) # Set mouse movement and pressed callback
        self.canvas.bind('<Button-1>', self.on_mouse_press) # Set mouse press

        self.canvas.grid(row=0, column=0, rowspan=len(COLORS))

        # Draw color attribute
        self.draw_color = COLORS[0]

        # Add color buttons
        for i, color in enumerate(COLORS):
            button = tk.Button(self, background=color, command=partial(self.change_color, color))
            button.grid(row=i, column=1)

    def change_color(self, new_color):
        self.draw_color = new_color

    def on_draw_data(self, data):
        if data['type'] == 'drawing_data':
            x, y = data['position']
            color = data['color']
            self.controller.root.after(0, lambda: self.canvas.create_oval(x - 10, y - 10, x + 10, y + 10, fill=color, outline=''))

    def on_mouse_press(self, event):
        self.canvas.create_oval(event.x - 10, event.y - 10, event.x + 10, event.y + 10, fill=self.draw_color, outline='')
        Thread(target=self.controller.socket.send_drawing_data(drawing_data={
            'position': [event.x, event.y],
            'color': self.draw_color}, room=self.room), daemon=True).start()


def run():
    app = App()
    app.run()
    
    #canvas = Canvas(root, socket, ['norma'])
    #root.bind('<Key>', canvas.clear)