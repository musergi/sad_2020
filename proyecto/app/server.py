from app import coms

def run():
    server = coms.DrawBookServer()
    server.serve_forever()