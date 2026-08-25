package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{

	private final int port;
	private final NetworkListener listener;
	private final String host;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean running;
	
	public Client(int port, NetworkListener listener, String host) {
		super();
		this.port = port;
		this.listener = listener;
		this.host = host;
	}

	public void connect() throws UnknownHostException, IOException {
		this.socket = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
		this.running = true;
		
		Thread listenerThread = new Thread(this, "NetworkListenerThread");
		listenerThread.setDaemon(true);
		listenerThread.start();
	
	}
	
	public void disconnect(){
        running = false;
        try{
            if(socket != null) {
                socket.close();
            }
        } 
        catch(IOException e) {
            e.printStackTrace();
        }
    }

	public void sendMove(int v1, int v2) {
        if(out != null) {
            out.println("MOVE|" + v1 + "|" + v2);
        }
    }
	
	public void sendStart(String level) {
        if(out != null) {
            out.println("START|" + level);
        }
    }
	
	
	@Override
	public void run() {
		try{
            String input;
            while(running && (input = in.readLine()) != null){
                parse(input.trim());
            }
        } 
		catch(IOException e){
            if(running){
                listener.error("Connection lost: " + e.getMessage());
            }
        } 
		finally {
            disconnect();
        }
	}

	private void parse(String input) {
		String[] components = input.split("\\|");
		String command = components[0];
		switch(command){
			case "STARTED":
				String level = components[1];
				listener.gameStarted(level);
				break;
			case "COMP_MOVE":
				int v1 = Integer.parseInt(components[1]);
				int v2 = Integer.parseInt(components[2]);
				listener.computerMove(v1, v2);
				break;
			case "OVER":
				boolean won = false;
				if(components[1].equals("WON"))
					won = true;
				v1 = Integer.parseInt(components[2]);
				v2 = Integer.parseInt(components[3]);
				listener.gameOver(won, v1, v2);
				running = false;
				break;
			case "INVALID":
				listener.invalidMove();
				break;
			case "ERROR": default:
				listener.error("There was an error.");
		}
	}

}
