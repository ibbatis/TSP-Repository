package main;

import antcolony.AntColony;
import grasp.GRASP;
import problem.GraphGenerator;
import problem.TravelingSalesman;
import tabuSearch.TabuSearch;

public class Main {

	public static void main(String[] args) {

		GraphGenerator generator = new GraphGenerator();
		int graphSize = 2000;
		
		for(int i = 0; i < 10; i++){
			double time = System.currentTimeMillis();
			
			double[][] graph = generator.Run(graphSize);
			
			System.out.println("Graph generated in "+(System.currentTimeMillis() - time)+" milliseconds");
			/*
			TravelingSalesman TSP = new TravelingSalesman(graphSize,graph);
			
			AntColony Colony = new AntColony(TSP);
			Colony.run();
			
			GRASP GRASP = new GRASP(TSP);
			GRASP.run();
			
			TabuSearch TS = new TabuSearch(TSP);
			TS.run();
			
			System.out.println("");
			*/
		}
		
	}
	
}
