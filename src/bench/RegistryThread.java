package bench;

import node.client.Client;

public class RegistryThread extends Thread{
	private Client client;
	private int operations;

	public RegistryThread(Client client, int operations){
		this.client = client;
		this.operations = operations;
	}

	public void run(){
		long start = System.currentTimeMillis();

		for(int i = 0; i < operations; i++){
			try{
				client.registry(false);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		System.out.println("Time for registry peer " + client.getPeer().getPeerId() + " "+operations+" times was " + (System.currentTimeMillis() - start) + "ms.");
	}
}
