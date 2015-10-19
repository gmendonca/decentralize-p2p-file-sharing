package run.env;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import node.Peer;
import node.server.Server;
import util.DistributedHashtable;

public class Deploy {
	
	public static ArrayList<String> peerList;
	public static int numPeers;
	
	public static void main(String[] args) throws IOException {
		// Creating servers
		
		peerList = DistributedHashtable.readConfigFile();

		numPeers = peerList.size();
		
		int id;
		String[] peerAddress;
		String address;
		int port;
		
		for (id = 0; id < peerList.size(); id++) {
			peerAddress = peerList.get(id).split(":");
			address =  peerAddress[0];
			port = Integer.parseInt(peerAddress[1]);
			
			Peer peer = new Peer(id, address, port);

			ServerSocket serverSocket = new ServerSocket(port);

			// start server
			Server server = new Server(serverSocket, peer);
			server.start();

		}
	}
			

}
