package run.env;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import node.Peer;
import node.client.Client;
import node.server.Assign;
import node.server.IndexingServer;
import util.DistributedHashtable;

public class Deploy {

	public static ArrayList<String> peerList;
	public static int numPeers;

	public static void main(String[] args) throws IOException {
		// Creating servers

		peerList = DistributedHashtable.readConfigFile();

		ArrayList<Peer> peerClassList = new ArrayList<Peer>();
		
		ArrayList<Socket> socketList = new ArrayList<Socket>();

		int id;
		String[] peerAddress;
		String address;
		int port;

		for (id = 0; id < peerList.size(); id++) {
			peerAddress = peerList.get(id).split(":");
			address =  peerAddress[0];
			port = Integer.parseInt(peerAddress[1]);
			
			Peer peer = new Peer(id, address, port);
			//peer.setDirectory(peerAddress[2]);
			peerClassList.add(peer);

			ServerSocket serverSocket = new ServerSocket(port);

			// start server
			IndexingServer server = new IndexingServer(serverSocket, peer);
			server.start();

			//start assign server
			Assign assign = new Assign(peer);
			assign.start();
		}

		// checking if all are open
		for (id = 0; id < peerList.size(); id++) {
			peerAddress = peerList.get(id).split(":");
			address =  peerAddress[0];
			port = Integer.parseInt(peerAddress[1]);

			try {
				System.out.println("Testing connection to server " + address + ":"
						+ port);
				Socket s = new Socket(address, port);
				socketList.add(s);
				System.out.println("Server " + address + ":"
						+ port + " is running.");
			} catch (Exception e) {
				// System.out.println("Not connected to server " + address + ":"
				// + port);
				// System.out.println("Trying again");
				id--;
				port--;
			}
		}

		//Client c = new Client(peerClassList.get(0), socketList);
		//c.userInterface();
	}


}
