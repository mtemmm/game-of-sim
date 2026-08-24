package server;

import java.net.Socket;

public class ClientHandler implements Runnable{

	private Socket client;

	public ClientHandler(Socket client) {
		super();
		this.client = client;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
