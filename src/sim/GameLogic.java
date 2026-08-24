package sim;

public class GameLogic extends GameOfSim{

	@Override
	protected MinMaxRes getMax(float alpha, float beta) {
		MinMaxRes res = new MinMaxRes(-1, -1,-2);
		for(int i=0; i<SIZE; i++) {
			for(int j=i+1;j<SIZE;j++) {
				if(canPlay(i, j)) {
					colorTheEdge(i, j, Color.blue);
					float val;
					if(gameEnded(i, j, Color.blue)) {
						val = -1;
					}
					else if(gameDone()) {
						val = 1;
					}
					else {
						val = getMin(alpha, beta).val;
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
		MinMaxRes res = new MinMaxRes(-1, -1, 2);
		for(int i=0; i<SIZE; i++) {
			for(int j=i+1;j<SIZE;j++) {
				if(canPlay(i, j)) {
					colorTheEdge(i, j, Color.red);
					float val;
					if(gameEnded(i, j, Color.red)) {
						val = 1;
					}
					else if(gameDone()) {
						val = -1;
					}
					else {
						val = getMax(alpha, beta).val;
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
	
}
