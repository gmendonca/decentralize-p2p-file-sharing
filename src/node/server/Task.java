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
				String key, value;

				switch (option) {
				case 0:
					//registry
					key = dIn.readUTF();
					//System.out.println(key);
					value = dIn.readUTF();
					dOut = new DataOutputStream(socket.getOutputStream());
					//dOut.writeBoolean(peer.put(key, value));
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
