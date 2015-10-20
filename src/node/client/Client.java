package node.client;

import index.server.Assign;
import index.server.IndexingServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import node.Peer;
import util.DistributedHashtable;
import util.Util;

public class Client extends Thread {

	private Peer peer;
	private static ArrayList<String> peerList;
	private ArrayList<Socket> socketList;

	public Client(Peer peer){
		this.peer = peer;
	}

	//getters
	public Peer getPeer(){
		return this.peer;
	}

	public ArrayList<String> getPeerList(){
		return peerList;
	}

	public ArrayList<Socket> getSocketList(){
		return this.socketList;
	}

	//setters
	public void setPeer(Peer peer){
		this.peer = peer;
	}

	public void setPeerlist(){
		try {
			peerList = DistributedHashtable.readConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPeerList(ArrayList<String> peerList2){
		peerList = peerList2;
	}

	public void setSocketList(ArrayList<Socket> socketList){
		this.socketList = socketList;
	}

	// put
	public Boolean registry()
			throws Exception {

		int pId;
		Socket socket;
		boolean ack = false;
		for(String fileName : peer.getFileNames()){
			pId = DistributedHashtable.hash(fileName, peerList.size());
			socket = socketList.get(pId);

			//synchronized(socket){
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			// put option
			dOut.writeByte(0);
			dOut.flush();

			// fileName, peer
			dOut.writeUTF(fileName);
			dOut.flush();
			dOut.writeUTF(peer.toString());
			dOut.flush();

			DataInputStream dIn = new DataInputStream(socket.getInputStream());
			ack = dIn.readBoolean();
			if(ack == false) break;
			//}
		}

		return ack;
	}

	// get
	public ArrayList<String> search(String fileName) throws IOException {

		int pId = DistributedHashtable.hash(fileName, peerList.size());
		Socket socket = socketList.get(pId);

		ArrayList<String> resultList = new ArrayList<String>();
		int numFiles;
		//synchronized(socket){
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

		// lookup option
		dOut.writeByte(1);
		dOut.flush();

		// key
		dOut.writeUTF(fileName);
		dOut.flush();

		DataInputStream dIn = new DataInputStream(socket.getInputStream());
		numFiles = dIn.readInt();
		for(int i = 0; i < numFiles; i++){
			resultList.add(dIn.readUTF());
		}
		//}

		return resultList;
	}

	// delete
	public Boolean delete(String key, int pId) throws Exception {
		if (key.length() > 24)
			return false;

		Socket socket = socketList.get(pId);
		boolean ack;
		synchronized(socket){
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

			// put option
			dOut.writeByte(2);
			dOut.flush();

			// key
			dOut.writeUTF(key);
			dOut.flush();

			DataInputStream dIn = new DataInputStream(socket.getInputStream());
			ack = dIn.readBoolean();
		}

		return ack;
	}

	public boolean startServer(int port){
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			// start server
			IndexingServer server = new IndexingServer(serverSocket);
			server.start();

			// start assign
			Assign assign = new Assign(server);
			assign.start();
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	public void userInterface(){
		int option, pId;
		String key = null;
		Boolean result = false;
		ArrayList<String> value;

		System.out.println("Runnning as Peer " + peer.getPeerId());

		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n\nSelect the option:");
			System.out.println("\t1 - Registry");
			System.out.println("\t2 - Get");
			System.out.println("\t3 - Del");

			try {
				option = scanner.nextInt();
				if (option == 1) {
					try {
						result = registry();
					} catch (Exception e) {
						System.out
						.println("Couldn't registry in the system.");
					}

					System.out
					.println(result ? "Registered!"
							: "Not registered.");

				} else if (option == 2) {

					System.out.print("key: ");
					key = scanner.next();

					try {
						value = search(key);
					} catch (Exception e) {
						System.out
						.println("Something went wrong and it couldn'd find the value.");
						continue;
					}
					for(String s : value){
						System.out.println(s);
					}

				} else if (option == 3) {

					System.out.print("key: ");
					key = scanner.next();

					pId = DistributedHashtable.hash(key, peerList.size());

					try {
						result = delete(key, pId);
					} catch (Exception e) {
						System.out
						.println("Something went wrong and it couldn'd delete the value.");
						continue;
					}

					System.out.println(result ? "Key " + key + " was at Peer "
							+ pId + "(" + peerList.get(pId)
							+ ") and now is deleted." : "Key not deleted.");

				} else {
					System.out.println("Option not valid");
					continue;
				}
			} catch (Exception e) {
				System.out.println("Oops, something went wrong. Closing it!");
				break;
			}
		}
		scanner.close();
	}

	public static void main(String[] args) throws IOException {

		peerList = DistributedHashtable.readConfigFile();

		if(args.length < 4){
			System.out.println("Usage: java -jar build/Client.jar <PeerId> <Address> <Port> <Folder>");
			return;
		}

		int id = 0;
		try {
			id = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("Put a valid Id");
			return;
		}

		if(id > peerList.size()){
			System.out.println("Peer Id shouldn't be greater than the number provided in the config file!");
			return;
		}

		String address = args[1];

		int port = 0;
		try {
			port = Integer.parseInt(args[2]);
		} catch (Exception e) {
			System.out.println("Put a valid port number");
			return;
		}

		String dir = args[3];
		File folder = new File(dir);

		if(!folder.isDirectory()){
			System.out.println("Put a valid directory name");
			return;
		}

		ArrayList<String> fileNames = Util.listFilesForFolder(folder);
		Peer peer = new Peer(id, address, port, dir, fileNames, fileNames.size());

		String[] peerAddress;

		ArrayList<Socket> socketList = new ArrayList<Socket>();

		Client c = new Client(peer);
		if(c.startServer(port))
			System.out.println("Server running.");
		else
			System.out.println("It wasn't possible to start the Server.");

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
		c.setSocketList(socketList);
		c.userInterface();
	}

}
