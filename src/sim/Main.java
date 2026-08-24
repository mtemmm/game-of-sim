package sim;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sim.GameOfSim.Edge;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException {
		GameOfSim gos = new GameLogicMedium();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		int v1, v2;
//		System.out.println("Unesite dva broja: ");
//		v1 = Integer.parseInt(in.readLine());
//		v2 = Integer.parseInt(in.readLine());
		int v1=5, v2=0;
//		gos.colorTheEdge(v1, v2, Color.red);
		long startTime = System.nanoTime();
        Edge e = gos.computerPlays();
        long endTime = System.nanoTime();
//		System.out.println("Blue: " + e.v1 + " " + e.v2);
		double durationInMilliseconds = (endTime - startTime) / 1000000.0;
        System.out.println("Bot took " + durationInMilliseconds + "ms");
        
//		do {
//			System.out.println("Unesite dva broja: ");
//			v1 = Integer.parseInt(in.readLine());
//			v2 = Integer.parseInt(in.readLine());
//			gos.colorTheEdge(v1, v2, Color.red);
//			System.out.println("Red: " + v1 + " " + v2);
//			System.out.println("------------");
//			gos.computerPlays();
//			//ispisi(gos.graph);
//		}while(!gos.gameEnded(v1, v2, Color.red));
//		System.out.println("Izgubio si!!!");
	}

	private static void ispisi(Color[][] matrica) {
		for(int i=0;i<matrica.length;i++) {
			for(int j=0;j<matrica.length;j++) {
				System.out.print(matrica[i][j] + " ");
			}
			System.out.println();
		}
	}
	
}
