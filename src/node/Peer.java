package node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.PeerQueue;
import node.client.Connection;
import node.server.Server;

public class Peer {

	private int peerId;
	private String address;
	private int port;
	private String directory;
	private ArrayList<String> fileNames;
	private int numFiles;
	private int numThreads = 4;
	public ServerSocket serverSocket;

	private PeerQueue<Connection> peerQueue;

	private Hashtable<String, String> hashtable;

	public Peer(int peerId, String address, int port, String directory)
			throws IOException {
		this.peerId = peerId;
		this.address = address;
		this.port = port;

		hashtable = new Hashtable<String, String>();
		peerQueue = new PeerQueue<Connection>();
	}

	public Peer(int peerId, String address, int port, String directory,
			ArrayList<String> fileNames, int numFiles) throws IOException {
		this.peerId = peerId;
		this.directory = directory;
		this.fileNames = fileNames;
		this.numFiles = numFiles;
		this.address = address;
		this.port = port;

		hashtable = new Hashtable<String, String>();
		peerQueue = new PeerQueue<Connection>();
	}

	// getters
	public int getPeerId() {
		return peerId;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public Hashtable<String, String> getHashtable() {
		return hashtable;
	}

	public int getNumFiles() {
		return numFiles;
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public String getDirectory() {
		return directory;
	}

	// setters
	public void setPeerId(int peerId) {
		this.peerId = peerId;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHashtable(Hashtable<String, String> hashtable) {
		this.hashtable = hashtable;
	}

	public void setNumFiles(int numFiles) {
		this.numFiles = numFiles;
	}

	public void setFileNames(ArrayList<String> fileNames) {
		this.fileNames.addAll(fileNames);
	}

	public void addFileName(String fileName) {
		this.fileNames.add(fileName);
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	// Override
	public String toString() {
		return peerId + ":" + address + ":" + port + ":" + directory;
	}

	public void server() throws IOException {

		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			return;
		}

		while (true) {
			Socket socket = serverSocket.accept();
			synchronized (peerQueue) {
				peerQueue.add(new Connection(socket, directory));
			}
			/*
			 * try { Thread.sleep(2); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */
		}

	}

	public void assign() throws IOException {

		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		while (true) {
			if (peerQueue.peek() == null) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			synchronized (peerQueue) {
				// System.out.println("Added to executor");
				Connection c = peerQueue.poll();
				Server s = new Server(c.getSocket(), c.getDirectory());
				executor.execute(s);
			}
		}

	}

}
