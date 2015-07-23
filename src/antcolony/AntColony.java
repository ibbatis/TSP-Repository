package antcolony;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import problem.TravelingSalesman;

public class AntColony {

	private double a; // Preference coefficient for the pheromone route
	private double b; // Preference coefficient for the shortest route
	private double c; // Initial ammount of pheromones in the routes
	
	private double evaporation; // Evaporation coefficient for each update
	private double pheromoneDrop; // Coefficient for leaving pheromones in the routes
	private double colonySize; // Got the idea from another code. This scales the colony size based on the number of cities
	
	private int maxIterations; // Maximum number of iterations the program will make without finding a better solution
	private Random R;
	
	private ArrayList<Ant> Colony;
	private TravelingSalesman TSP;
	private double[][] pheromoneTrail; // Stores the pheromones on the trails
	private int currentCity;
	
	private Ant bestAnt;
	
	public int selectCity(Ant a){
		// Ant can move randomly on a whim
		if(R.nextDouble() < a.Whim()){
			ArrayList<Integer> choices = new ArrayList<Integer>();
			for(int i = 0; i < a.Path().length; i++)
				if(!a.visited(i))
					choices.add(i);
			return choices.get(R.nextInt(choices.size()));
		}
		
		updateProbs(a);
		double Winning = R.nextDouble();
		ArrayList<Double> Competitors = new ArrayList<Double>();
		for(double d: a.Probabilities())
			if(d != 0.0)
				Competitors.add(new Double(d));
		Collections.sort(Competitors);
		int ChanceModifier = Competitors.size() - 1;
		
		for(int i = 0; i < Competitors.size(); i++){
			if(Winning <= 0.7*(Math.pow((1 - 0.7), ChanceModifier))){
				for(int j = 0; j < a.Probabilities().length; j++)
					if(Competitors.get(i).equals(a.Probabilities()[j]))
						return j;
			}
			ChanceModifier--;
		}
		for(int j = 0; j < a.Probabilities().length; j++)
			if(Competitors.get(Competitors.size()-1).equals(a.Probabilities()[j]))
				return j;
		throw new RuntimeException("Something went wrong");
	}
	
	private void moveAnt(Ant a){
		int city = selectCity(a);
		a.visit(city,currentCity,TSP.Distances()[a.Path()[currentCity]][city]);
	}
	
	private void moveAllAnts(){
		for(int i = 0; i < TSP.Size(); i++){
			for(Ant a: Colony){
				moveAnt(a);
			}
			currentCity++;
		}
	}
	
	private void resetAllAnts() {
		for(Ant a: Colony)
			a.reset();
		currentCity = 0;
	}
	
	private void updateProbs(Ant ant){
		int origin = ant.Path()[currentCity];
		double[] probs = new double[TSP.Size()];
		
		double den = 0.0;
		for(int i = 0; i < TSP.Size(); i++)
			if(!ant.visited(i))
				den += Math.pow(pheromoneTrail[origin][i],a)
						* Math.pow(1.0/TSP.Distances()[origin][i],b);
		
		for(int i = 0; i < TSP.Size(); i++){
			double num = 0.0;
			if(ant.visited(i))
				probs[i] = 0.0;
			else {
				num = Math.pow(pheromoneTrail[origin][i],a)
						* Math.pow(1.0/TSP.Distances()[origin][i],b);
				probs[i] = num/den;
			}
		}
		ant.Probabilities(probs.clone());
	}
	
	private void updatePheromones(){
		for(int i = 0; i < TSP.Size(); i++)
			for(int j = 0; j < TSP.Size(); j++)
				pheromoneTrail[i][j] *= evaporation;
		
		for(Ant a: Colony){
			double droppedPheromones = pheromoneDrop/a.pathLength();
			for(int i = 0; i < TSP.Size()-1; i++)
				pheromoneTrail[ a.Path()[i] ][ a.Path()[i+1] ] += droppedPheromones;
			pheromoneTrail[ a.Path()[TSP.Size()-1] ][ a.Path()[0] ] += droppedPheromones;
		}
	}
	
	private boolean updateBest(){
		if(bestAnt == null)
			bestAnt = Colony.get(0);
		for(Ant a: Colony){
			if(a.pathLength() < bestAnt.pathLength()){
				bestAnt = new Ant(a);
				return true;
			}		
		}
		return false;
	}
	
	private ArrayList<Ant> newColony(int numCities, double colonyFactor){
		ArrayList<Ant> newColony = new ArrayList<Ant>();
		for(int i = 0; i < Math.floor(numCities*colonyFactor); i++)
			newColony.add(new Ant(numCities));
		for(Ant a: newColony)
			a.visit(R.nextInt(TSP.Size()),currentCity, 0);
		currentCity++;
		return newColony;
	}
	
	public AntColony(TravelingSalesman TSP){
		this.TSP = TSP;
		this.R = new Random();
		
		this.a = 1;
		this.b = 5;
		this.c = 1;
		
		this.evaporation = 0.5;
		this.pheromoneDrop = 500;
		this.colonySize = 0.5;
		
		this.maxIterations = 1000;
		
		this.Colony = newColony(TSP.Size(),colonySize);
		this.currentCity = 0;
		this.pheromoneTrail = new double[TSP.Size()][TSP.Size()];
		for(double[] v: pheromoneTrail)
			Arrays.fill(v,c);
		this.bestAnt = null;
	}
	
	public void run(){
		int currentIter = 0;
		int bestIter = 0;
		
		do{
			resetAllAnts();
			moveAllAnts();
			updatePheromones();
			if(updateBest())
				bestIter = currentIter;
			currentIter++;
		} while(currentIter - bestIter < maxIterations);
		System.out.println("The best solution found by the Ant Colony was: "+bestAnt.toString());
	}
	
}
