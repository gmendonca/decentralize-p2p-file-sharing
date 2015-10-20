package node;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import util.PeerQueue;

public class Peer {

	private int peerId;
	private String address;
	private int port;
	private String directory;
	private ArrayList<String> fileNames;
	private int numFiles;

	private Hashtable<String, String> hashtable;

	public Peer(int peerId, String address, int port) throws IOException {
		this.peerId = peerId;
		this.address = address;
		this.port = port;

		
		hashtable = new Hashtable<String, String>();
	}

	public Peer(int peerId, String address, int port, String directory, ArrayList<String> fileNames, int numFiles) throws IOException{
		this.directory = directory;
		this.fileNames = fileNames;
		this.numFiles = numFiles;
		this.address = address;
		this.port = port;

		hashtable = new Hashtable<String, String>();
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

	public int getNumFiles(){
		return numFiles;
	}

	public ArrayList<String> getFileNames(){
		return fileNames;
	}

	public String getDirectory(){
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

	public void setNumFiles(int numFiles){
		this.numFiles = numFiles;
	}

	public void setFileNames(ArrayList<String> fileNames){
		this.fileNames.addAll(fileNames);
	}

	public void addFileName(String fileName){
		this.fileNames.add(fileName);
	}

	public void setDirectory(String directory){
		this.directory = directory;
	}

	

	//Override
	public String toString(){
		return peerId + ":" +address + ":" + port + ":" + directory;

	}

	// put
	public Boolean put(String key, String value) throws Exception {
		try {
			hashtable.put(key, value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// get
	public String get(String key) throws IOException {
		return hashtable.get(key);
	}

	// delete
	public Boolean delete(String key) {
		if (!hashtable.containsKey(key))
			return false;

		try {
			hashtable.remove(key);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

}
