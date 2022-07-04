package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import java.awt.*;

public class server implements Runnable {
	private serverGUI GUI;// GUI管理介面
	private PairThread pairThread;// 接收加入排隊要求之執行序
	
	private int port = 8081; // port 號
	private ServerSocket serverSocket = null;
	
	private Thread selfthread = null; // 接收client Socket 的Thread
	private volatile ArrayDeque<Socket> Queue = null;
	server() throws IOException {

		// server的socket 啟動
		serverSocket = new ServerSocket(port);
		System.out.println("Server started on port " + serverSocket.getLocalPort() + "...");
		System.out.println("Waiting for client...");
		
		Queue = new ArrayDeque<Socket>(20);//排隊佇列
		
		selfthread = new Thread(this);
		selfthread.start();// 開始接收socket
		
		pairThread = new PairThread(Queue); // 開始配對執行序
		pairThread.start();

		//GUI = new serverGUI(this); // 開啟伺服器管理用GUI
	}

	public static void main(String[] args) throws IOException {
		server Server = new server();
	}

	@Override
	public void run() {
		System.out.println("socket 接收執行序開始運作...");
		while (selfthread != null) {
			try {
				// wait until client socket connecting, then add new thread
				var socket = serverSocket.accept();
				var objinputstream = new ObjectInputStream(socket.getInputStream());
				var cmd = objinputstream.readUTF();
				switch (cmd) {
				case "Pair":
					this.joinToQueue(socket);
					break;
				case "Exit":
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
	public void joinToQueue(Socket socket) {// 加入排隊
		System.out.printf("%s has join the queue%n", socket.getRemoteSocketAddress());
		Queue.add(socket);
	}

}
