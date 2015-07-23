package grasp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import problem.TravelingSalesman;

public class GRASP {

	private TravelingSalesman TSP;
	
	private Random R;
	private int K = 5;
	private double selectionRate = 0.8;
	private HashSet<Integer[]> Neighborhood;
	
	private Integer SelectMove(int origin, ArrayList<Integer> Competitors, double rate){
		Collections.sort(Competitors, new GRASPComparator(origin, TSP.Distances()));

		double Winning = R.nextDouble();
		int ChanceModifier = K-1;
		
		for(int i = 0; i < Competitors.size(); i++) {
			if(Winning <= rate*(Math.pow((1 - rate), ChanceModifier))){
				return Competitors.get(i);
			}
			ChanceModifier--;
		}
		return Competitors.get(0);
	}
	
	private Integer[] Construct(){
		ArrayList<Integer> cities = new ArrayList<Integer>();
		for(int i = 0; i < TSP.Size(); i++)
			cities.add(i);
		
		Integer[] InitialSolution = new Integer[TSP.Size()];
		
		InitialSolution[0] = cities.remove(R.nextInt(cities.size()));
		for(int i = 1; i < InitialSolution.length; i++){
			InitialSolution[i] = SelectMove(InitialSolution[i-1],cities,selectionRate);
			cities.remove(InitialSolution[i]);
		}
		
		return InitialSolution;
	}
	
	private void makeNeighborhood(Integer[] x){
		// Inserts all Neighbors of the given solution into the Neighborhood vector
		Integer[] newSol = new Integer[TSP.Size()];
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
	
	private double distance(Integer[] Sol){ 
		// Checks to see if the solution doesn't violate any constaints
		double totDistance = 0;
		for(int i = 0; i < Sol.length-1; i++){
			totDistance += TSP.Distances()[Sol[i]][Sol[i+1]];
		}
		return totDistance;
	}
	
	public GRASP(TravelingSalesman TSP){
		this.R = new Random();
		this.TSP = TSP;
		this.Neighborhood = new HashSet<Integer[]>();
	}
	
	public void run(){
		Integer[] initialSolution = Construct();
		Integer[] bestSolution = new Integer[initialSolution.length];
		System.arraycopy(initialSolution, 0, bestSolution, 0, TSP.Size());
		boolean proceed;
		do {
			proceed = false;
			makeNeighborhood(bestSolution.clone());
			for(Integer[] candidate: Neighborhood){
				if(distance(candidate) < distance(bestSolution)){
					System.arraycopy(candidate, 0, bestSolution, 0, TSP.Size());
					proceed = true;
				}
			}
		} while(proceed);
		String res = "";
		/*
		res = "The best solution found by GRASP was Route: [";
		for(int i: bestSolution)
			res += "|"+i+"|";
			*/
		res += "GRASP Length: "+distance(bestSolution);
		System.out.println(res);
	}
	
}
