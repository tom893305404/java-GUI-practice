package server;

import java.net.Socket;
import java.util.ArrayDeque;

public class PairThread extends Thread {
	private volatile ArrayDeque<Socket> Queue = null;

	// private server server;
	PairThread(ArrayDeque<Socket> _queue) {
		Queue = _queue;
	}

	@Override
	public void run() {
		System.out.println("佇列執行緒開始執行...");
		while (true) {
			if (Queue.size() >= 2) {
				var socket1 = Queue.poll();
				var socket2 = Queue.poll();
				var thread = new Thread(new gameThread(socket1, socket2));
				thread.start();
			}
		}
	}

	

}
