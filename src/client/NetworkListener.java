package client;

public interface NetworkListener {
	
	void gameStarted(String level);
	void computerMove(int v1, int v2);
	void gameOver(boolean won, int v1, int v2);
	void invalidMove();
	void error(String message);
}
