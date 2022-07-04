package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class JoinQueueTest {
 public static void main(String[] args) {
	 	Socket socket;
		try {
			//socket = new Socket("192.168.0.11", 8081, null, 35556);
			socket = new Socket("192.168.0.11", 8081);
			System.out.println("Connected to server " + socket.getRemoteSocketAddress());
			var stream = new ObjectOutputStream(socket.getOutputStream());
			stream.writeUTF("Pair");
			stream.flush();
			
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}



 }
}
