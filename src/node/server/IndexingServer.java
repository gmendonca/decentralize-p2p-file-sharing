package node.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import node.Peer;

public class IndexingServer extends Thread {

	private ServerSocket serverSocket;
	private Peer peer;
	
	public Hashtable<String, ArrayList<Peer>> index;

	public IndexingServer(ServerSocket serverSocket, Peer peer) {
		this.serverSocket = serverSocket;
		this.peer = peer;
		
		index = new Hashtable<String, ArrayList<Peer>>();
	}
	
	public boolean registry(){
		return false;
		
	}

	public void run() {

		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			synchronized (peer.getPeerQueue()) {
				peer.addToPeerQueue(socket);
			}
			/*
			 * try { Thread.sleep(2); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */
		}

	}
}
