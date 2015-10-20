package node.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
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

	public Client(Peer peer, ArrayList<Socket> socketList){
		this.peer = peer;

		try {
			peerList = DistributedHashtable.readConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// put
	public Boolean registry(String key, String value, int pId)
			throws Exception {
		if (key.length() > 24)
			return false;
		if (value.length() > 1000)
			return false;

		Socket socket = socketList.get(pId);
		boolean ack;
		synchronized(socket){
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			// put option
			dOut.writeByte(0);
			dOut.flush();

			// key, value
			dOut.writeUTF(key);
			dOut.flush();
			dOut.writeUTF(value);
			dOut.flush();

			DataInputStream dIn = new DataInputStream(socket.getInputStream());
			ack = dIn.readBoolean();
		}

		return ack;
	}

	// get
	public String get(String key, int pId) throws IOException {
		if (key.length() > 24)
			return null;

		Socket socket = socketList.get(pId);

		String value;
		synchronized(socket){
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

			// get option
			dOut.writeByte(1);
			dOut.flush();

			// key
			dOut.writeUTF(key);
			dOut.flush();

			DataInputStream dIn = new DataInputStream(socket.getInputStream());
			value = dIn.readUTF();
		}

		return value;
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

	public void userInterface(){
		int option, pId;
		String key = null, value = null;
		Boolean result = false;

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

					pId = DistributedHashtable.hash(key, peerList.size());
					System.out.println(pId);
					try {
						result = registry(key, value, pId);
					} catch (Exception e) {
						System.out
						.println("Couldn't put the key-value pair in the system.");
					}

					System.out
					.println(result ? "Key " + key
							+ " inserted at Peer " + pId + "("
							+ peerList.get(pId) + ")."
							: "Something went wrong and it couldn'd insert the key-value pair.");

				} else if (option == 2) {

					System.out.print("key: ");
					key = scanner.next();

					pId = DistributedHashtable.hash(key, peerList.size());

					try {
						value = get(key, pId);
					} catch (Exception e) {
						System.out
						.println("Something went wrong and it couldn'd find the value.");
						continue;
					}

					System.out.println(!value.equals("") ? "Key " + key
							+ " is at Peer " + pId + "(" + peerList.get(pId)
							+ ") and has value " + value + "."
							: "Value not found.");

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
		if(args.length < 4){
			System.out.println("Usage: java -jar build/Client.jar <PeerId> <Address> <Port>");
			return;
		}

		peerList = DistributedHashtable.readConfigFile();

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

		Client c = new Client(peer, socketList);
		c.userInterface();
	}

}
