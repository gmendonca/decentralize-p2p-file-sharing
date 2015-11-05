package bench.remote;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import util.DistributedHashtable;
import util.Util;
import node.Peer;
import node.client.Client;

public class Launch {
	
	public static ArrayList<String> peerList;
	public static ArrayList<String> serverList;

	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out
					.println("Usage: java -jar build/RemoteBench.jar <PeerId> <Number of operations>");
			return;
		}
		
		int id = Integer.parseInt(args[0]);
		if (id < 0 && id > peerList.size()) {
			System.out
					.println("Id should be positive and smaller than the number of peers allowed!");
			return;
		}

		int operations = Integer.parseInt(args[1]);
		if (operations < 0) {
			System.out
					.println("Number of operations should be a positive number!");
			return;
		}
		
		try {
			peerList = DistributedHashtable.readConfigFile("peers");
			serverList = DistributedHashtable.readConfigFile("servers");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] serverAddress, peerAddress;
		
		peerAddress = peerList.get(id).split(":");
		String address = peerAddress[0];
		int port = Integer.parseInt(peerAddress[1]);

		String dir = peerAddress[2];
		File folder = new File(dir);

		if (!folder.isDirectory()) {
			System.out.println("Put a valid directory name");
			return;
		}

		ArrayList<String> fileNames = Util.listFilesForFolder(folder);
		Peer peer = null;
		try {
			peer = new Peer(id, address, port, dir, fileNames,
					fileNames.size());
		} catch (IOException e) {
			e.printStackTrace();
		}

		Client c = new Client(peer);
		c.startOpenServer();

		ArrayList<Socket> serverSocketList = new ArrayList<Socket>();

		for (int i = 0; i < serverList.size(); i++) {
			serverAddress = serverList.get(i).split(":");
			address = serverAddress[0];
			port = Integer.parseInt(serverAddress[1]);

			try {
				System.out.println("Testing connection to server "
						+ address + ":" + port);
				Socket s = new Socket(address, port);
				serverSocketList.add(s);
				System.out.println("Server " + address + ":" + port
						+ " is running.");
			} catch (Exception e) {
				i--;
				port--;
			}
		}
		c.setServerSocketList(serverSocketList);

		c.setPeerSocketList(new Socket[peerList.size()]);
		
		c.bench(operations);

	}

}
