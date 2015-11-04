package bench;

import java.util.Random;

import node.client.Client;

public class SearchThread extends Thread{
	
	private Client client;
	private int operations;
	
	public SearchThread(Client client, int operations){
		this.client = client;
		this.operations = operations;
	}
	
	public void run(){
		
		long start = System.currentTimeMillis();
		
		Random rand = new Random();
		
		String fileName;
		
		for(int i = 0; i < operations; i++){
			rand.setSeed(System.currentTimeMillis() * client.getPeer().getPeerId());
			fileName = "file-p" + rand.nextInt(8) + "-0" + rand.nextInt(8);
			try {
				client.search(fileName, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Time for doing "+operations+" searches in peer " + client.getPeer().getPeerId() + " was " + (System.currentTimeMillis() - start) + "ms.");
		
	}


}
