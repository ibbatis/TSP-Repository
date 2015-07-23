package tabuSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import problem.TravelingSalesman;

public class TabuSearch {
	
	private int MaxTabu = 5;
	
	private TravelingSalesman TSP;
	private HashSet<int[]> Neighborhood;
	private ArrayList<int[]> TabuList;
	private Random R;

	private int[] randomInitial(){
		int length = TSP.Size();
		int cursor = 0;
		int[] InitialSolution = new int[length];
		ArrayList<Integer> visitedNodes = new ArrayList<Integer>();
		
		while(cursor < length){
			int randomMove = R.nextInt(length);
			if(!visitedNodes.contains(randomMove)){
				InitialSolution[cursor] = randomMove;
				visitedNodes.add(randomMove);
				cursor++;
			}
		}
		return InitialSolution;
	}
	
	private double distance(int[] Sol){ 
		// Checks to see if the solution doesn't violate any constaints
		double totDistance = 0;
		for(int i = 0; i < Sol.length-1; i++){
			totDistance += TSP.Distances()[Sol[i]][Sol[i+1]];
		}
		return totDistance;
	}
	
	private void makeNeighborhood(int[] x){
		// Inserts all Neighbors of the given solution into the Neighborhood vector
		int[] newSol = new int[TSP.Size()];
		System.arraycopy(x, 0, newSol, 0, TSP.Size());		
		for(int i = 0; i < x.length; i++){
			for(int j = 0; j < x.length; j++){
				int aux = x[i];
				x[i] = x[j];
				x[j] = aux;
			}
			Neighborhood.add(newSol.clone());
			System.arraycopy(x, 0, newSol, 0, TSP.Size());
		}
	}
	
	public TabuSearch(TravelingSalesman Problem){
		this.TSP = Problem;
		this.Neighborhood = new HashSet<int[]>();
		this.TabuList = new ArrayList<int[]>();
		this.R = new Random();
	}

	public int[] run(){
		int[] s = new int[TSP.Size()];
		int[] bestS = new int[TSP.Size()];
		s = randomInitial();
		System.arraycopy(s, 0, bestS, 0, TSP.Size());

		int currIter = 0;
		int bestIter = 0;
		
		do {
			Neighborhood.clear();
			makeNeighborhood(s);

			Iterator<int[]> Neighbors = Neighborhood.iterator();
			
			while(Neighbors.hasNext()) {
				int[] candidate = Neighbors.next().clone();
				if(!TabuList.contains(candidate) && distance(candidate) < distance(s)){
					System.arraycopy(candidate, 0, s, 0, TSP.Size());
				}
			}
			
			if(distance(s) < distance(bestS)){
				System.arraycopy(s, 0, bestS, 0, TSP.Size());
				bestIter = currIter;
			}
			
			TabuList.add(s);
			if(TabuList.size() > MaxTabu)
				TabuList.remove(0);
			
			currIter++;
		} while(currIter - bestIter < 1000);
		
		String res = "";
		/*
		res += "The best solution found by Tabu Search was: ";
		for(int i: bestS)
			res += "|"+i+"|";
		*/
		res += "Tabu Length: "+distance(bestS);
		System.out.println(res);
		
		
		return bestS;
	}
	

}
