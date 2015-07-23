package antcolony;

import java.util.Arrays;

public class Ant {
	
	private int[] Path;
	private double pathLength;
	private boolean[] citiesVisited;
	
	private double whim;
	private double[] probabilities;
	
	public void visit(int city,int index, double distance){
		this.Path[index] = city;
		this.pathLength += distance;
		this.citiesVisited[city] = true;
	}
	
	public boolean visited(int city){
		return this.citiesVisited[city];
	}
	
	public Ant(int numOfCities){
		this.Path = new int[numOfCities];
		this.citiesVisited = new boolean[numOfCities];
		this.probabilities = new double[numOfCities];
		
		this.whim = 0.01;
		this.pathLength = 0.0;
	}
	
	public Ant(Ant a){
		this.Path = a.Path.clone();
		this.citiesVisited = a.citiesVisited.clone();
		this.probabilities = a.probabilities.clone();
		
		this.whim = a.whim;
		this.pathLength = a.pathLength;
	}
	
	public String toString(){
		String res = "";
		/*
		res = "Route: [";
		for(int i: Path)
			res += "|"+i+"|";
		*/
		res += "ANT Length: "+Double.toString(pathLength);
		return res;
	}
	
	public void reset(){
		Arrays.fill(this.Path, 0);
		Arrays.fill(this.citiesVisited, false);
		Arrays.fill(this.probabilities, 0.0);
		this.pathLength = 0.0;
	}

	public int[] Path() {
		return this.Path;
	}

	public double pathLength() {
		return this.pathLength;
	}

	public double[] Probabilities() {
		return probabilities;
	}

	public void Probabilities(double[] probabilities) {
		this.probabilities = probabilities;
	}

	public double Whim() {
		return whim;
	}
	
	
	
}
