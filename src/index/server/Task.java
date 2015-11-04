package index.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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

			while (true) {
				DataInputStream dIn = new DataInputStream(
						socket.getInputStream());
				DataOutputStream dOut = null;

				byte option = dIn.readByte();
				// System.out.println(option);
				String fileName, peer;
				ArrayList<Peer> peerList;

				switch (option) {
				case 0:
					// registry
					fileName = dIn.readUTF();
					peer = dIn.readUTF();
					dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeBoolean(indexingServer.registry(fileName, peer));
					dOut.flush();
					break;
				case 1:
					// lookup
					fileName = dIn.readUTF();
					peerList = indexingServer.lookup(fileName);
					dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeInt(peerList.size());
					dOut.flush();
					if (peerList.size() != 0) {
						for (Peer p : peerList)
							dOut.writeUTF(p.toString());
						dOut.flush();
					}
					break;
				}
			}
		} catch (Exception e) {
			//System.out.println("Nothing happened");
			// e.printStackTrace();

		}

	}

}
