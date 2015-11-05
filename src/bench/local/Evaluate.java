package bench.local;

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
		
		client.bench(operations);
		
	}
}
