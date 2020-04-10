import argparse
from app import client, server

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--client", type=bool, required=False)
    parser.add_argument("--server", type=bool, required=False)
    parser.add_argument("--username", type=str, required=False)
    parser.add_argument("--peer", type=str, required=False)
    args = parser.parse_args()

    if args.client:
        client.run(args.username, args.peer)
    if args.server:
        server.run()