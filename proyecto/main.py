import tkinter as tk
from functools import partial

COLORS = ['red', 'blue', 'cyan', 'green', 'yellow', 'orange', 'black']


class Canvas:
    def __init__(self, root):
        # Create canvas to draw on
        self.canvas = tk.Canvas(root, width=800, height=600)
        self.canvas.grid(row=0, column=0, rowspan=len(COLORS))
        
        # Draw color attribute
        self.draw_color = COLORS[0]

        # Add color buttons
        for i, color in enumerate(COLORS):
            button = tk.Button(root, background=color, command=partial(self.change_color, color=color))
            button.grid(row=i, column=1)

        # Set drawing callback
        self.canvas.bind('<B1-Motion>', self.press_handler) # Set mouse movement and pressed callback
        self.canvas.bind('<Button-1>', self.press_handler) # Set mouse press

    def clear(self, event):
        self.canvas.delete(tk.ALL)

    def press_handler(self, event):
        self.canvas.create_oval(event.x - 10, event.y - 10, event.x + 10, event.y + 10, fill=self.draw_color, outline='')

    def change_color(self, color):
        self.draw_color = color

# Create window object
root = tk.Tk()

canvas = Canvas(root)
root.bind('<Key>', canvas.clear)

# Enter event loop
root.mainloop()