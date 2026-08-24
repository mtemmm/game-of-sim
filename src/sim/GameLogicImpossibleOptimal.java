package sim;

import java.util.LinkedList;
import java.util.PriorityQueue;

import sim.GameOfSim.MinMaxRes;

public class GameLogicImpossibleOptimal  extends GameOfSim{

	private class EdgeEval implements Comparable<EdgeEval>{
		Edge e;
		double eval;
		boolean asc;
		
		public EdgeEval(Edge e, double eval, boolean asc) {
			super();
			this.e = e;
			this.eval = eval;
			this.asc = asc;
		}

		@Override
		public int compareTo(EdgeEval arg0) {
			if(asc)
                return Double.compare(this.eval, arg0.eval); //getMin
            else
                return Double.compare(arg0.eval, this.eval); 
            
		}
	}
	
	@Override
	protected MinMaxRes getMax(float alpha, float beta) {
		MinMaxRes res = new MinMaxRes(-1, -1,-2);
		PriorityQueue<EdgeEval> pq = new PriorityQueue<EdgeEval>();
		for(int i=0;i<SIZE;i++) {
			for(int j=i+1;j<SIZE;j++) {
				if(canPlay(i, j)) {
					pq.add(new EdgeEval(new Edge(i,j), evaluate(i,j, Color.blue), false));
				}
			}
		}
		while(!pq.isEmpty()) {
			EdgeEval ee = pq.poll();
			int i = ee.e.v1;
			int j = ee.e.v2;
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
		return res;
	}

	private float evaluate(int i, int j, Color c) {
		colorTheEdge(i, j, c);
		int redThreats = threats.get(Color.red);
		int blueThreats = threats.get(Color.blue);
		colorTheEdge(i, j, null);
		return (float)(redThreats - blueThreats) / NUM_OF_TRIANGLES * 0.9f;
	}

	@Override
	protected MinMaxRes getMin(float alpha, float beta) {
		MinMaxRes res = new MinMaxRes(-1, -1, 2);
		PriorityQueue<EdgeEval> pq = new PriorityQueue<EdgeEval>();
		for(int i=0;i<SIZE;i++) {
			for(int j=i+1;j<SIZE;j++) {
				if(canPlay(i, j)) {
					pq.add(new EdgeEval(new Edge(i,j), evaluate(i,j, Color.red), true));
				}
			}
		}
		while(!pq.isEmpty()) {
			EdgeEval ee = pq.poll();
			int i = ee.e.v1;
			int j = ee.e.v2;
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
		
		return res;
	}
}
