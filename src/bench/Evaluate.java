package bench;

import java.util.ArrayList;
import java.util.Random;

import node.Peer;
import node.client.Client;

public class Evaluate extends Thread {
	private Client client;
	private int operations;

	public Evaluate(Client client, int operations) {
		this.client = client;
		this.operations = operations;
	}

	public void run() {
		long start = System.currentTimeMillis();
		long startTime = start;

		for (int i = 0; i < operations; i++) {
			try {
				client.registry(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Time for registry peer "
				+ client.getPeer().getPeerId() + " " + operations
				+ " times was " + (System.currentTimeMillis() - start) + "ms.");

		start = System.currentTimeMillis();

		Random rand = new Random(System.currentTimeMillis());

		String fileName;

		for (int i = 0; i < operations; i++) {
			fileName = "file-p" + rand.nextInt(8) + "-0" + rand.nextInt(8);
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

		rand = new Random();
		
		ArrayList<Peer> result;

		for (int i = 0; i < operations; i++) {
			rand.setSeed(System.currentTimeMillis() * client.getPeer().getPeerId());
			fileName = "file-p" + rand.nextInt(8)+ "-0" + rand.nextInt(8);
			try {
				result = client.search(fileName, false);
				//System.out.println(client.getPeer().getPeerId() + " " + fileName + " " + result.size());
				client.download(fileName, client.getPeer().getPeerId(), result.get(0).toString());
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
