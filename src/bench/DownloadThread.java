package bench;

import java.util.ArrayList;
import java.util.Random;

import node.Peer;
import node.client.Client;

public class DownloadThread extends Thread{
	
	private Client client;
	private int operations;
	
	public DownloadThread(Client client, int operations){
		this.client = client;
		this.operations = operations;
	}
	
	public void run(){
		
		long start = System.currentTimeMillis();
		
		Random rand = new Random();
		
		String fileName;
		
		ArrayList<Peer> result;
		
		for(int i = 0; i < operations; i++){
			rand.setSeed(System.currentTimeMillis() * client.getPeer().getPeerId());
			fileName = "file-p" + rand.nextInt(8) + "-0" + rand.nextInt(8);
			try {
				result = client.search(fileName, false);
				client.download(fileName, client.getPeer().getPeerId(), result.get(0).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Time for doing " + operations + " downloads in peer " + client.getPeer().getPeerId() + " was " + (System.currentTimeMillis() - start) + "ms.");
		
	}


}
