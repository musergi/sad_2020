import argparse
from app import client, server

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--client", type=bool, required=False)
    parser.add_argument("--server", type=bool, required=False)
    args = parser.parse_args()

    if args.client:
        client.test()
    if args.server:
        server.run()