package bench;

import java.util.ArrayList;
import java.util.Random;

import node.Peer;
import node.client.Client;

public class Evaluate extends Thread {
	private Client client;
	private int operations;
	private int numClients;

	public Evaluate(Client client, int operations, int numClients) {
		this.client = client;
		this.operations = operations;
		this.numClients = numClients;
	}

	public void run() {
		long start = System.currentTimeMillis();
		long startTime = start;

		for(int i = 0; i < operations; i++){
			try{
				client.registry(false);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		System.out.println("Time for registry peer "
				+ client.getPeer().getPeerId() + " " + operations
				+ " times was " + (System.currentTimeMillis() - start) + "ms.");
		
		String fileName;

		ArrayList<Peer> result;
		Random rand = new Random();
		int j = 0;

		start = System.currentTimeMillis();

		for(int i = 0; i < operations; i++){
			//fileName = "file-p" + (j++) + "-0" + i;
			rand.setSeed(System.currentTimeMillis() + j++);
			rand.nextInt(j);
			fileName = "file-p" + rand.nextInt(numClients) + "-0" + rand.nextInt(client.getPeer().getNumFiles());
			try {
				client.search(fileName, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Time for doing " + operations
				+ " searches in peer " + client.getPeer().getPeerId() + " was "
				+ (System.currentTimeMillis() - start) + "ms.");

		start = System.currentTimeMillis();

		for(int i = 0; i < operations; i++){
			//fileName = "file-p" + (j++) + "-0" + i;
			rand.setSeed(System.currentTimeMillis() + j++);
			rand.nextInt(j);
			fileName = "file-p" + rand.nextInt(numClients) + "-0" + rand.nextInt(client.getPeer().getNumFiles());

			try {
				result = client.search(fileName, false);
				//System.out.println(client.getPeer().toString() + " " + fileName + " " + result.size());
				client.download(fileName, result.get(0).getPeerId(), result.get(0).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Time for doing " + operations
				+ " downloads in peer " + client.getPeer().getPeerId() + " was "
				+ (System.currentTimeMillis() - start) + "ms.");

		System.out.println("Overall Time for doing " + operations
				+ " operations with in peer " + client.getPeer().getPeerId() + " was "
				+ (System.currentTimeMillis() - startTime) + "ms.");
		
	}
}
