package sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameLogicVeryEasy extends GameOfSim{

	//uzima random granu
	
	@Override
	protected MinMaxRes getMax(float alpha, float beta) {
		MinMaxRes res = null;
		List<Edge> list = new ArrayList<Edge>();
		for(int i=0;i<SIZE;i++) {
			for(int j=i+1;j<SIZE;j++) {
				if(graph[i][j] == null) {
					list.add(new Edge(i,j));
				}
			}
		}
		int index = (int)(Math.random() * list.size());
		if(list.size()>0) {
			res = new MinMaxRes(list.get(index).v1, list.get(index).v2, -1);
		}
		return res;
	}

	@Override
	protected MinMaxRes getMin(float alpha, float beta) {
		return null;
	}

}
