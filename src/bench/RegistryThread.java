package bench;

import node.client.Client;

public class RegistryThread extends Thread{
	private Client client;

	public RegistryThread(Client client){
		this.client = client;
	}

	public void run(){
		long start = System.currentTimeMillis();
		try{
			client.registry(false);
			client.registry(true);
		} catch (Exception e){
			e.printStackTrace();
		}

		while(true){
			int id = client.getPeer().getPeerId();
			try {
				client.replicateFiles(id == Bench.peerList.size() - 1 ? 0 : id + 1);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Cannot replicate, trying again...");
				try { Thread.sleep(5000); } catch (Exception e1) { } 
			}
		}
		
		System.out.println("Time for registry peer " + client.getPeer().getPeerId() + " was " + (System.currentTimeMillis() - start) + "ms.");
	}
}
