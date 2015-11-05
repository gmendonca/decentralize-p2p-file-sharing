package bench.local;

import node.client.Client;

public class Evaluate extends Thread {
	private Client client;
	private int operations;

	public Evaluate(Client client, int operations) {
		this.client = client;
		this.operations = operations;
	}

	public void run() {
		
		client.bench(operations);
		
	}
}
