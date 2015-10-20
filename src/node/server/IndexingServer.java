package node.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import util.PeerQueue;
import node.Peer;

public class IndexingServer extends Thread {

	private ServerSocket serverSocket;

	private Hashtable<String, ArrayList<Peer>> index;

	private PeerQueue<Socket> peerQueue;

	public IndexingServer(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;

		index = new Hashtable<String, ArrayList<Peer>>();

		peerQueue = new PeerQueue<Socket>();
	}
	
	//setters
	public void setPeerQueue(PeerQueue<Socket> peerQueue) {
		this.peerQueue = peerQueue;
	}
	
	//getters
	public PeerQueue<Socket> getPeerQueue() {
		return peerQueue;
	}

	// Queue methods
	public void addToPeerQueue(Socket sock) {
		peerQueue.add(sock);
	}

	public Socket peekPeerQueue() {
		return peerQueue.peek();
	}

	public Socket pollPeerQueue() {
		return peerQueue.poll();
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
			synchronized (getPeerQueue()) {
				addToPeerQueue(socket);
			}
			/*
			 * try { Thread.sleep(2); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */
		}

	}
}
