package node;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Assign extends Thread {

	private int numThreads = 4;
	private Peer peer;

	public Assign(Peer peer) {
		this.peer = peer;
	}

	public void run() {
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		while (true) {
			if (peer.peekPeerQueue() == null) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			synchronized (peer.getPeerQueue()) {
				Socket socket = peer.pollPeerQueue();
				Task t = new Task(socket, peer);
				executor.execute(t);
			}
		}
	}
}
