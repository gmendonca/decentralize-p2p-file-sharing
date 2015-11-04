package bench;

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

		RegistryThread rt = new RegistryThread(client, operations);
		rt.start();
		
		try {
			rt.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("Time for registry peer "
				+ client.getPeer().getPeerId() + " " + operations
				+ " times was " + (System.currentTimeMillis() - start) + "ms.");

		start = System.currentTimeMillis();

		SearchThread st = new SearchThread(client, operations, numClients);
		st.start();
		
		try {
			st.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Time for doing " + operations
				+ " searches in peer " + client.getPeer().getPeerId() + " was "
				+ (System.currentTimeMillis() - start) + "ms.");

		start = System.currentTimeMillis();

		DownloadThread dt = new DownloadThread(client, operations, numClients);
		dt.start();
		
		try {
			dt.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Time for doing " + operations
				+ " downloads in peer " + client.getPeer().getPeerId() + " was "
				+ (System.currentTimeMillis() - start) + "ms.");
		
		System.out.println("Overall Time for doing " + operations
				+ " operations with in peer " + client.getPeer().getPeerId() + " was "
				+ (System.currentTimeMillis() - startTime) + "ms.");
	}
}
