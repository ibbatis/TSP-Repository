package grasp;

import java.util.Comparator;

public class GRASPComparator implements Comparator<Integer> {
    
	
	int origin;
	double[][] distances;
	
	@Override
    public int compare(Integer o1, Integer o2) {
    	Double o1Distance = distances[origin][o1];
    	Double o2Distance = distances[origin][o2];
        return o1Distance.compareTo(o2Distance);
    }

	public GRASPComparator(int origin, double[][] distances) {
		this.origin = origin;
		this.distances = distances;
	}
    
}