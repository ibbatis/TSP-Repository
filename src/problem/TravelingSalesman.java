package problem;

public class TravelingSalesman {
	
	protected int Size;
	protected double[][] Distances;
	
	public TravelingSalesman(int size, double[][] weights) {
		this.Size = size;
		this.Distances = weights;
	}

	public int Size() {
		return Size;
	}

	public double[][] Distances() {
		return Distances;
	}
	
}
