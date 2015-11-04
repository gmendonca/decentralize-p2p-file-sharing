package bench;

import java.util.ArrayList;

import node.Peer;
import node.client.Client;

public class DownloadThread extends Thread{

	private Client client;
	private int operations;
	private int numClients;

	public DownloadThread(Client client, int operations, int numClients) {
		this.client = client;
		this.operations = operations;
		this.numClients = numClients;
	}

	public void run(){

		String fileName;

		ArrayList<Peer> result;
		int j = 0;

		for(int i = 0; i < operations; i++){
			fileName = "file-p" + (j++) + "-0" + i;
			if(j == numClients) j = 0;
			try {
				result = client.search(fileName, false);
				//System.out.println(client.getPeer().toString() + " " + fileName + " " + result.size());
				client.download(fileName, result.get(0).getPeerId(), result.get(0).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
