package bench;

import node.client.Client;

public class SearchThread extends Thread{

	private Client client;
	private int operations;
	private int numClients;

	public SearchThread(Client client, int operations, int numClients) {
		this.client = client;
		this.operations = operations;
		this.numClients = numClients;
	}


	public void run(){


		String fileName;
		int j = 0;

		for(int i = 0; i < operations; i++){
			fileName = "file-p" + (j++) + "-0" + i;
			if(j == numClients) j = 0;
			try {
				client.search(fileName, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
