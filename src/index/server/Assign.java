package index.server;

import java.net.Socket;

public class Assign extends Thread {

	private IndexingServer indexingServer;

	public Assign(IndexingServer indexingServer) {
		this.indexingServer = indexingServer;
	}

	public void run() {

		while (true) {
			if (indexingServer.peekPeerQueue() == null) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			synchronized (indexingServer.getPeerQueue()) {
				Socket socket = indexingServer.pollPeerQueue();
				Task t = new Task(socket, indexingServer);
				t.start();
			}
		}
	}
}
