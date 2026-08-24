package sim;


public class GameLogicFullSearch extends GameOfSim{

	@Override
	protected MinMaxRes getMax(float alpha, float beta) {
		return getMax();
	}

	private MinMaxRes getMax() {
		MinMaxRes res = new MinMaxRes(-1, -1, -1);
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
					else {
						val = getMin().val;
					}
					if(val > res.val) {
						res.val = val;
						res.v1 = i;
						res.v2 = j;
					}
					colorTheEdge(i, j, null);
					
				}
			}
		}
		return res;
	}

	@Override
	protected MinMaxRes getMin(float alpha, float beta) {
		return getMin();
	}

	private MinMaxRes getMin() {
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
					else {
						val = getMax().val;
					}
					if(val < res.val) {
						res.val = val;
						res.v1 = i;
						res.v2 = j;
					}
					colorTheEdge(i, j, null);
				}
			}
		}
		return res;
	}

}
