package index.server;

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

	public boolean registry(String fileName, String peer) throws Exception{
		String [] peerItems = peer.split(":");
		try{
			if(index.containsKey(fileName)){
				index.get(fileName).add(new Peer(Integer.parseInt(peerItems[0]), peerItems[1], Integer.parseInt(peerItems[2]), peerItems[3]));
				return true;
			}else {
				index.put(fileName, new ArrayList<Peer>());
				index.get(fileName).add(new Peer(Integer.parseInt(peerItems[0]), peerItems[1], Integer.parseInt(peerItems[2]), peerItems[3]));
				return true;
			}
		} catch (Exception e){
			return false;
		}
	}

	public ArrayList<Peer> lookup(String fileName){
		if(index.containsKey(fileName)){
			return index.get(fileName);
		}
		return new ArrayList<Peer>();
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
