package sim;

import sim.GameOfSim.MinMaxRes;

public class GameLogicEasy extends GameOfSim{

	private static final int DEPTH = 1;
	
	@Override
	protected MinMaxRes getMax(float alpha, float beta) {
		return getMax(alpha, beta, 0);
	}

	private MinMaxRes getMax(float alpha, float beta, int depth) {
		MinMaxRes res = new MinMaxRes(-1, -1, -2);
		for(int i=0; i<SIZE; i++) {
			for(int j=i+1;j<SIZE;j++) {
				if(canPlay(i, j)) {
					colorTheEdge(i, j, Color.blue);
					float val;
					if(gameEnded(i, j, Color.blue)) {
						val = -1.0f;
					}
					else if(gameDone()) {
						val = 1.0f;
					}
					else if(depth+1 >= DEPTH) {
						val = evaluate();
					}
					else {
						val = getMin(alpha, beta, depth + 1).val;
					}
					if(val > res.val) {
						res.val = val;
						res.v1 = i;
						res.v2 = j;
					}
					colorTheEdge(i, j, null);
					if (val > alpha) {
						alpha = val;
					}
					if (alpha >= beta) {
						return res;
					}
				}
			}
		}
		return res;
	}

	@Override
	protected MinMaxRes getMin(float alpha, float beta) {
		return getMin(alpha, beta, 0);
	}

	private MinMaxRes getMin(float alpha, float beta, int depth) {
		MinMaxRes res = new MinMaxRes(-1, -1, 2);
		for(int i=0; i<SIZE; i++) {
			for(int j=i+1;j<SIZE;j++) {
				if(canPlay(i, j)) {
					colorTheEdge(i, j, Color.red);
					float val;
					if(gameEnded(i, j, Color.red)) {
						val = 1.0f;
					}
					else if(gameDone()) {
						val = -1.0f;
					}
					else if(depth+1 >= DEPTH) {
						val = evaluate();
					}
					else {
						val = getMax(alpha, beta, depth + 1).val;
					}
					if(val < res.val) {
						res.val = val;
						res.v1 = i;
						res.v2 = j;
					}
					colorTheEdge(i, j, null);
					if (val < beta) {
						beta = val;
					}
					if (alpha >= beta) {
						return res;
					}
				}
			}
		}
		return res;
	}
	
	private float evaluate() {
		int redThreats = threats.get(Color.red);
		int blueThreats = threats.get(Color.blue);
		
		return (float)(redThreats - blueThreats) / NUM_OF_TRIANGLES * 0.9f;
	}

}
