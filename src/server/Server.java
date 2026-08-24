package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{

	private static final int PORT = 1234;

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		try(ServerSocket ss = new ServerSocket(PORT)){
			System.out.println("Server running...");
			while(true) {
				Socket client = ss.accept();
				System.out.println("Client connected: " + client.getInetAddress().getHostAddress());
				pool.execute(new ClientHandler(client));
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			pool.shutdown();
		}
	}
	
}
