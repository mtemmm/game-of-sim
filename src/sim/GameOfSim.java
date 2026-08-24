package sim;
import java.util.HashMap;
import java.util.Map;

enum Color{
	red, blue;
}

//player red, computer blue

public abstract class GameOfSim {
	
	protected static class MinMaxRes{
		public int v1;
		public int v2;
		public float val;
		
		public MinMaxRes(int v1, int v2, float val) {
			super();
			this.v1 = v1;
			this.v2 = v2;
			this.val = val;
		}
	}
	
	public static class Edge{
		public int v1;
		public int v2;

		public Edge(int v1, int v2) {
			this.v1 = v1;
			this.v2 = v2;
		}
	
	}
	
	Color[][] graph;
	protected static final int SIZE = 6;
	protected static final int NUM_EDGES = SIZE*(SIZE-1)/2;
	protected static final int NUM_OF_TRIANGLES = SIZE*(SIZE-1)*(SIZE-2)/6;
	protected int coloredEdges;
	protected Map<Color, Integer> threats;
	private boolean gameEnded = false;
	private Edge[] gameEndedTriangle;
	
	public GameOfSim() {
		this.graph = new Color[SIZE][SIZE];
		this.coloredEdges = 0;
		this.threats = new HashMap<Color, Integer>();
		threats.put(Color.red, 0);
		threats.put(Color.blue, 0);
	}
	
	public boolean gameEnded(int v1, int v2, Color c) {
		for(int j=0; j<SIZE; j++) {
			if(v1 == j || v2 == j)
				continue;
			if(graph[v1][j] == c && graph[v1][j] == graph[v2][j]) {
				gameEndedTriangle = new Edge[3];
				gameEndedTriangle[0] = new Edge(v1, j);
				gameEndedTriangle[1] = new Edge(v1, v2);
				gameEndedTriangle[2] = new Edge(v2, j);
				return true;
			}
				
		}
		return false;
	}
	
	public void colorTheEdge(int v1, int v2, Color c) {
		Color col;
		if(c == null)
			col = graph[v1][v2];
		else
			col = c;
		graph[v1][v2] = c;
		graph[v2][v1] = c;
		int numOfThreats = threateningWith(v1, v2, col);
		if(c == null) {
			coloredEdges --;
			threats.put(col, threats.get(col) - numOfThreats);
		}
		else {
			coloredEdges ++;
			threats.put(col, threats.get(col) + numOfThreats);
		}
	}
	
	private int threateningWith(int v1, int v2, Color c) {
		int num = 0;
		for(int i=0;i<SIZE;i++) {
			if(i == v1 || i == v2)
				continue;
			if(graph[v1][i] == c && graph[v2][i] == null)
				num++;
			if(graph[v1][i] == null && graph[v2][i] == c)
				num++;
		}
		return num;
	}

	public Edge computerPlays() {
		MinMaxRes mmr = getMax(-2,2);
		int a = mmr.v1;
		int b = mmr.v2;
		colorTheEdge(a, b, Color.blue);
		if(gameEnded(a, b, Color.blue))
			this.gameEnded = true;
		return new Edge(a, b);
	}
	
	public void playerPlays(int v1, int v2) {
		colorTheEdge(v1, v2, Color.red);
		if(gameEnded(v1, v2, Color.red)) {
			this.gameEnded = true;
		}
	}
	
	public boolean gameDone() {
		return coloredEdges == NUM_EDGES;
	}
	
	public boolean canPlay(int v1, int v2) {
		return graph[v1][v2] == null;
	}

	public boolean isGameEnded() {
		return gameEnded;
	}

	public Edge[] getGameEndedTriangle() {
		return gameEndedTriangle;
	}

	protected abstract MinMaxRes getMax(float alpha, float beta);
	protected abstract MinMaxRes getMin(float alpha, float beta);
	
}
