package NodeEmpire;

public class ShipReactor {

	/*
	 * a reactor charges power and stores it, then sends it out
	 * to the weapons and shields
	 */
	double power; //amount of power produced per unit time
	double storage; //amount of power that can be stored
	double consumption; //fuel consumption per unit time
	double explosionChance;
	double cost;
	double size;
	double efficiency;
	
	public ShipReactor(double p, double st, double cm,
			double ex, double sz, double c){
		power = p;
		storage = st;
		consumption = cm;
		explosionChance = ex;
		cost = c;
		size = sz;
		efficiency = (power*storage)/(consumption*cost*size);
	}
	public String toString (){
		return 
		Double.toString(power)+"p" +
		Double.toString(storage)+"s" +
		Double.toString(consumption)+"c"+
		Double.toString(explosionChance)+"ex"+
		Double.toString(size)+"sz"+
		Double.toString(cost)+"c";
	}
}
