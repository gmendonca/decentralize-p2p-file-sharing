package node.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import node.Peer;
import util.DistributedHashtable;

public class Client extends Thread {

	private Peer peer;
	private static ArrayList<String> peerList;

	// put
	public static Boolean registry(String key, String value, int pId)
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
	
	public void userInterface(){
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
	
	public Client(Peer peer){
		this.peer = peer;
		
		System.out.println("Runnning as Peer " + peer.getPeerId());
		try {
			peerList = DistributedHashtable.readConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
