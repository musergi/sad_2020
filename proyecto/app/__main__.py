import argparse
from app import client, server

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--client", default=False, required=False, action='store_true')
    parser.add_argument("--server", default=False, required=False, action='store_true')
    args = parser.parse_args()

    if args.client:
        client.run()
    if args.server:
        server.run()