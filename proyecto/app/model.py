class DrawingData:
    def __init__(self, shape, position, color):
        self.shape = shape
        self.position = position
        self.color = color

    def __str__(self):
        return f'DrawingData({self.shape},{self.position},{self.color})'