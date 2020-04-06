import re
import http.server
from urllib import parse
from threading import Lock


class RequestHandler(http.server.BaseHTTPRequestHandler):
    DRAWING_PATTERN = re.compile('^\/drawing')

    def __init__(self, request, client_address, server):
        super().__init__(request, client_address, server)

    def do_GET(self):
        self.send_response(404)
        self.end_headers()

    def do_POST(self):
        if RequestHandler.DRAWING_PATTERN.match(self.path):
            content_length = int(self.headers['Content-Length'])
            post_data = self.rfile.read(content_length)
            data = parse.parse_qs(post_data.decode('utf-8'))
            Model.add_drawing_data(data)
            self.send_response(200)
            self.end_headers()
            return

        self.send_response(404)
        self.end_headers()


class Model:
    lock = Lock()
    drawings = dict()

    @staticmethod
    def add_drawing_data(data: dict):
        group_set = frozenset(data['from'] + data['to'])
        drawing_data = DrawingData(data)
        with Model.lock:
            drawing_data_points = Model.drawings.get(group_set, [])
            drawing_data_points.append(drawing_data)
            print(drawing_data_points)
            Model.drawings[group_set] = drawing_data_points


class DrawingData:
    def __init__(self, data_dict: dict):
        self.shape = data_dict['shape'].pop()
        self.x = data_dict['x'].pop()
        self.y = data_dict['y'].pop()
        self.color = data_dict['color'].pop()


httpd = http.server.HTTPServer(server_address=('localhost', 6969), RequestHandlerClass=RequestHandler)
httpd.serve_forever()