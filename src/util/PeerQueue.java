package util;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class PeerQueue<T> implements Serializable{

	private LinkedList<T> queue;

	public T peek() {
		return queue.peek();
	}

	public T poll() {
		return queue.poll();
	}

	public void add(T t) {
		queue.add(t);

	}

	public PeerQueue() {
		queue = new LinkedList<T>();
	}

	public int Size() {
		return queue.size();
	}

}
