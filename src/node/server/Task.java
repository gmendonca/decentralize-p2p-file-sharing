package node.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import node.Peer;

public class Task extends Thread {

	private Socket socket;
	private IndexingServer indexingServer;

	public Task(Socket socket, IndexingServer indexingServer) {
		this.socket = socket;
		this.indexingServer = indexingServer;
	}

	public void run() {

		try {

			while(true){
				DataInputStream dIn = new DataInputStream(socket.getInputStream());
				DataOutputStream dOut = null;

				byte option = dIn.readByte();
				//System.out.println(option);
				String fileName, peer, key, value;

				switch (option) {
				case 0:
					//registry
					fileName = dIn.readUTF();
					//System.out.println(key);
					peer = dIn.readUTF();
					dOut = new DataOutputStream(socket.getOutputStream());
					//dOut.writeBoolean(peer.put(key, value));
					dOut.writeBoolean(indexingServer.registry(fileName, peer));
					dOut.flush();
					break;
				case 1:
					// get
					key = dIn.readUTF();
					//value = peer.get(key);
					dOut = new DataOutputStream(socket.getOutputStream());
					//dOut.writeUTF((value != null) ? value : "");
					dOut.flush();
					break;
				case 2:
					// delete
					key = dIn.readUTF();
					dOut = new DataOutputStream(socket.getOutputStream());
					//dOut.writeBoolean(peer.delete(key));
					dOut.flush();
					break;
				default:
					System.out.println("Not an option");
				}
			}
		} catch (Exception e) {
			//System.out.println("Nothing happened");
			//e.printStackTrace();

		}

	}

}
