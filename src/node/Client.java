package node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import util.DistributedHashtable;

public class Client extends Thread {

	private static ArrayList<String> peerList;

	// put
	public static Boolean put(String key, String value, int pId)
			throws Exception {
		if (key.length() > 24)
			return false;
		if (value.length() > 1000)
			return false;

		String[] peerAddress = peerList.get(pId).split(":");
		Socket socket = new Socket(peerAddress[0],
				Integer.parseInt(peerAddress[1]));
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

		// put option
		dOut.writeByte(0);
		dOut.flush();

		// key, value
		dOut.writeUTF(key);
		dOut.writeUTF(value);
		dOut.flush();

		DataInputStream dIn = new DataInputStream(socket.getInputStream());
		boolean ack = dIn.readBoolean();

		socket.close();

		return ack;
	}

	// get
	public static String get(String key, int pId) throws IOException {
		if (key.length() > 24)
			return null;

		String[] peerAddress = peerList.get(pId).split(":");
		Socket socket = new Socket(peerAddress[0],
				Integer.parseInt(peerAddress[1]));
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

		// get option
		dOut.writeByte(1);
		dOut.flush();

		// key
		dOut.writeUTF(key);
		dOut.flush();

		DataInputStream dIn = new DataInputStream(socket.getInputStream());
		String value = dIn.readUTF();

		socket.close();

		return value;
	}

	// delete
	public static Boolean delete(String key, int pId) throws Exception {
		if (key.length() > 24)
			return false;

		String[] peerAddress = peerList.get(pId).split(":");
		Socket socket = new Socket(peerAddress[0],
				Integer.parseInt(peerAddress[1]));
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

		// put option
		dOut.writeByte(2);
		dOut.flush();

		// key, value
		dOut.writeUTF(key);
		dOut.flush();

		DataInputStream dIn = new DataInputStream(socket.getInputStream());
		boolean ack = dIn.readBoolean();

		socket.close();

		return ack;
	}

	public static void main(String[] args) throws IOException {
		
		if(args.length < 3){
			System.out.println("Usage: java -jar build/OpenBench.jar <PeerId> <Address> <Port>");
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

		Peer peer = new Peer(id, address, port);

		ServerSocket serverSocket = new ServerSocket(port);

		// start server
		Server server = new Server(serverSocket, peer);
		server.start();

		// start assign
		Assign assign = new Assign(peer);
		assign.start();

		int option, pId;
		String key = null, value = null;
		Boolean result = false;

		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n\nSelect the option:");
			System.out.println("\t1 - Put");
			System.out.println("\t2 - Get");
			System.out.println("\t3 - Del");

			try {
				option = scanner.nextInt();
				if (option == 1) {

					System.out.print("key: ");
					key = scanner.next();
					System.out.print("value: ");
					value = scanner.next();
					System.out.println(key + " " + value);
					pId = DistributedHashtable.hash(key, peerList.size());
					System.out.println(pId);
					try {
						result = put(key, value, pId);
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
}
