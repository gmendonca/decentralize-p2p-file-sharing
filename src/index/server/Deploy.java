package index.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import util.DistributedHashtable;

public class Deploy {

	public static ArrayList<String> serverList;
	public static int numPeers;

	public static void main(String[] args) throws IOException {
		// Creating servers

		if(args.length < 1){
			System.out.println("Usage: java -jar build/Deploy.jar <ServerId>");
			return;
		}

		int id = Integer.parseInt(args[0]);

		serverList = DistributedHashtable.readConfigFile("servers");

		String[] peerAddress;
		int port;

		peerAddress = serverList.get(id).split(":");
		port = Integer.parseInt(peerAddress[1]);

		ServerSocket serverSocket = new ServerSocket(port);

		// start server
		IndexingServer server = new IndexingServer(serverSocket);
		server.start();

		//start assign server
		Assign assign = new Assign(server);
		assign.start();
	}


}
