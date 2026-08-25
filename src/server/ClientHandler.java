package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import sim.GameLogicEasy;
import sim.GameLogicImpossible;
import sim.GameLogicMedium;
import sim.GameLogicVeryEasy;
import sim.GameOfSim;
import sim.GameOfSim.Edge;

public class ClientHandler implements Runnable{

	private Socket socket;
	private GameOfSim game;

	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try ( BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	          PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
	        ){
			String input;
			while ((input = in.readLine()) != null) {
				String[] components = input.split("\\|");
				if(input.equals("QUIT")) {
					break;
				}
				else if(components[0].equals("START")) {
					handleStart(components, out);
				}
				else if(components[0].equals("MOVE")) {
					handleMove(components, out);
				}
				else {
					out.println("ERROR");
				}
			}
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				socket.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	//input = "START|LEVEL"
	private void handleStart(String[] components, PrintWriter out) {
		String level = components[1];
		switch(level) {
			case "VERY_EASY":
				game = new GameLogicVeryEasy();
				break;
			case "EASY":
				game = new GameLogicEasy();
				break;
			case "IMPOSSIBLE":
				game = new GameLogicImpossible();
				break;
			case "MEDIUM": default:
				game = new GameLogicMedium();
				break;
		}
		out.println("STARTED|" + level);
	}
	//input = "MOVE|v1|v2"
	private void handleMove(String[] components, PrintWriter out) {
		int v1 = Integer.parseInt(components[1]);
		int v2 = Integer.parseInt(components[2]);
		if(!game.canPlay(v1, v2)) {
			out.println("INVALID");
			return;
		}
		game.playerPlays(v1, v2);
		if(game.isGameEnded()) {
			/*String message = "OVER|LOST|" + game.getGameEndedTriangle()[0].v1 + "|" + game.getGameEndedTriangle()[1].v2 + "|" + game.getGameEndedTriangle()[2].v2;
			System.out.println(message);*/
			out.println("OVER|LOST|" + game.getGameEndedTriangle()[0].v1 + "|" + game.getGameEndedTriangle()[1].v2 + "|" + game.getGameEndedTriangle()[2].v2);
			return;
		}
		Edge e = game.computerPlays();
		if(game.isGameEnded()) {
			out.println("OVER|WON|" + game.getGameEndedTriangle()[0].v1 + "|" + game.getGameEndedTriangle()[1].v2 + "|" + game.getGameEndedTriangle()[2].v2);
			return;
		}
		out.println("COMP_MOVE|" + e.v1 + "|" + e.v2);
	}
	
}
