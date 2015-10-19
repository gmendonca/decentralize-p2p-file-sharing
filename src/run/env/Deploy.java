package run.env;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import node.Peer;
import node.server.IndexingServer;
import util.DistributedHashtable;
import util.Util;

public class Deploy {
	
	public static ArrayList<String> peerList;
	public static int numPeers;
	
	public static void main(String[] args) throws IOException {
		// Creating servers
		
		peerList = DistributedHashtable.readConfigFile();

		numPeers = peerList.size();
		
		int id;
		String[] peerAddress;
		String address, dir;
		int port;
		ArrayList<String> fileNames;
		File folder;
		
		for (id = 0; id < peerList.size(); id++) {
			peerAddress = peerList.get(id).split(":");
			address =  peerAddress[0];
			port = Integer.parseInt(peerAddress[1]);
			dir = peerAddress[2];
			
			folder = new File(dir);

	    	fileNames = Util.listFilesForFolder(folder);
			Peer peer = new Peer(id, address, port, dir, fileNames, fileNames.size());

			ServerSocket serverSocket = new ServerSocket(port);

			// start server
			IndexingServer server = new IndexingServer(serverSocket, peer);
			server.start();

		}
	}
			

}
