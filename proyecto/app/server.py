from app import coms

def run():
    server = coms.ChatServerSocket()
    server.serve_forever()