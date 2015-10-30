package NodeEmpire;

public class ShipPropulsion {

	//all values are per 10 units of fuel
	double power; //speed value per fuel
	double consumption; //fuel consumed per unit time
	double size;
	double cost;
	double efficiency;
	
	public ShipPropulsion (double p, 
			double cm, double sz, double c) {
		power = p;
		consumption = cm;
		size = sz;
		cost = c;
		efficiency = (power*consumption)/(size*cost);
	}
	public String toString () {
		return 
		Double.toString(power)+"p" +
		Double.toString(consumption)+"c"+
		Double.toString(size)+"sz"+
		Double.toString(cost)+"c";
	}
}
