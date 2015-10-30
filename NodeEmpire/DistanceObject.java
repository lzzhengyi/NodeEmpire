package NodeEmpire;

public class DistanceObject implements Comparable {
	double distance;
	Node node;
	
	public DistanceObject (Node n, double d){
		node = n;
		distance = d;
	}


	public int compareTo(Object oj) {
		DistanceObject n = (DistanceObject) oj;
		if (n.distance<distance){
			return 1;
		} else if (n.distance>distance){
			return -1;
		} else {
			return 0;
		}
	}
	
}
