package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import node.Peer;

public class Task extends Thread {

	private Socket socket;
	private Peer peer;

	public Task(Socket socket, Peer peer) {
		this.socket = socket;
		this.peer = peer;
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
					key = dIn.readUTF();
					//System.out.println(key);
					value = dIn.readUTF();
					dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeBoolean(peer.put(key, value));
					dOut.flush();
					break;
				case 1:
					// get
					key = dIn.readUTF();
					value = peer.get(key);
					dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeUTF((value != null) ? value : "");
					dOut.flush();
					break;
				case 2:
					// delete
					key = dIn.readUTF();
					dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeBoolean(peer.delete(key));
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
