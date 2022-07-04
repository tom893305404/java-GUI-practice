package server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;

public class testserver1 implements Runnable {
	private int port = 8081; // port 號
	private ServerSocket serverSocket = null;
	private Thread selfthread = null; // 接收client Socket 的Thread
	testserver1() throws IOException {

		// server的socket 啟動
		serverSocket = new ServerSocket(port);
		System.out.println("Server started on port " + serverSocket.getLocalPort() + "...");
		System.out.println("Waiting for client...");
		selfthread = new Thread(this);
		selfthread.start();
	}

	public static void main(String[] args) throws IOException {
		testserver1 Server = new testserver1();
	}

	@Override
	public void run() {
		System.out.println("socket 接收執行序開始aaaaa運作...");
		while (selfthread != null) {
			try {
				// wait until client socket connecting, then add new thread
				var socket = serverSocket.accept();
				var objinputstream = new ObjectInputStream(socket.getInputStream());
				var cmd = objinputstream.readUTF();
				switch (cmd) {
				case "Pair":
					System.out.println("in the Pair");
					break;
				case "Text":
					System.out.println("in the Text");
					break;
					
				default:
					System.out.println("Unkown Request...");
					break;
				}
			} catch (IOException e) {
				System.out.println("Error : " + e);
			}
		}
	}

}
