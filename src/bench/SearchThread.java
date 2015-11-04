package bench;

import java.util.ArrayList;
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
		
		Random rand = new Random(System.currentTimeMillis());
		
		String fileName;
		
		ArrayList<String> result;
		
		for(int i = 0; i < operations; i++){
			fileName = "file-p" + rand.nextInt(10) + "-0" + rand.nextInt(10);
			try {
				result = client.search(fileName, false);
				//System.out.println(result.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Time for doing 10K searches in peer " + client.getPeer().getPeerId() + " was " + (System.currentTimeMillis() - start) + "ms.");
		
	}


}
