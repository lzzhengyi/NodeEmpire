package NodeEmpire;

public class ShipArmor {

	double armor;
	double shield;
	double rate; //rate of shield regen
	double consumption; //fuel consumption per tu during regen
	double size;
	double cost;
	double efficiency;
	
	public ShipArmor (double a, double s, double r,
			double cm, double sz, double c){
		armor = a;
		shield = s;
		rate = r;
		consumption = cm;
		size = sz;
		cost = c;
		efficiency = (armor*shield*rate)/(cost*size);
	}
	public String toString (){
		return 
		Double.toString(armor)+"a" +
		Double.toString(shield)+"s" +
		Double.toString(rate)+"r"+
		Double.toString(consumption)+"c"+
		Double.toString(size)+"sz"+
		Double.toString(cost)+"c";
	}

}
