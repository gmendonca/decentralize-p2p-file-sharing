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

			/*while(true){
				int id = client.getPeer().getPeerId();
				try {
					client.replicateFiles(id == Bench.peerList.size() - 1 ? 0 : id + 1);
					break;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Cannot replicate, trying again...");
					try { Thread.sleep(5000); } catch (Exception e1) { } 
				}
			}*/
		}

		System.out.println("Time for registry peer " + client.getPeer().getPeerId() + " was " + (System.currentTimeMillis() - start) + "ms.");
	}
}
