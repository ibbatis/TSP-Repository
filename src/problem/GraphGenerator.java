package problem;

import java.util.Random;

public class GraphGenerator {

	Random R;
	
	public GraphGenerator(){
		this.R = new Random();
	}
	
	private double[][] assertTriangleInequality(double[][] Graph, int Size){
		for(int i = 0; i < Size; i++){
			for(int j = 0; j <= i; j++){
				if(i != j)
					for(int k = 0; k < Size; k++){
						if(!(Graph[i][j] <= Graph[i][k] + Graph[k][j]))
							while(Graph[i][j] > Graph[i][k] + Graph[k][j]){
								Graph[i][j] = Graph[j][i] = R.nextDouble()*10;
								if(Graph[i][j] < 2)
									Graph[i][j] = Graph[j][i] += 2;
							}
					}
			}
		}
		return Graph;
	}
	
	public double[][] Run(int Size){
		double[][] Graph = new double[Size][Size];
		for(int i = 0; i < Size; i++){
			for(int j = 0; j < Size; j++){
				if(i == j)
					Graph[i][j] = 0.0;
				else {
					Double Distance = R.nextDouble()*10;
					if(Distance < 2)
						Distance += 2;
					Graph[i][j] = Graph[j][i] = Distance;
				}
			}			
		}
		return assertTriangleInequality(Graph,Size);
	}

}
