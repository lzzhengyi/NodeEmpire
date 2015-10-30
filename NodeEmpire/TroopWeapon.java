package NodeEmpire;

public class TroopWeapon implements Comparable {

	double power; //dmg vs armor -> dmg vs troop numbers
	double size; //capacity penalty
	double accuracy; //chance to hit
	double rate; //amount of shots per turn
	double penetration; //bonus dmg vs armor
	double moralebonus; //increases max morale
	double cost;
	double efficiency; //I'd like to use this value to rank weapons in a priority list
	boolean powered; //can be disabled by disabling weapons
	boolean disabling; //if true, has a chance to disable powered weapons and armor when hits enemy
	
	
	public TroopWeapon (double p, double s, double a, double r, double pen, double c,boolean pwd, boolean d){
		power = p;
		size = s;
		accuracy = a;
		rate = r;
		penetration = pen;
		cost = c;
		powered = pwd;
		disabling = d;
		efficiency = ((power+penetration)*accuracy*rate)/(cost*size);
	}
	public double offensiveValue (){
		return ((power+penetration)*accuracy*rate);
	}
	public int compareTo(Object arg0) {
		TroopWeapon tw = (TroopWeapon) arg0;
		if (efficiency>tw.efficiency){
			return 1;
		} else if (efficiency<tw.efficiency){
			return -1;
		} else {
			return 0;
		}
	}
	public String toString (){
		String s = "";
		if (powered){
			s = "P";
		}
		if (disabling){
			s = s+ "D";
		}

		return 
		Double.toString(power)+"p" +
		Double.toString(accuracy)+"a" +
		Double.toString(rate)+"r"+
		Double.toString(penetration)+"pen"+
		Double.toString(size)+"sz"+
		Double.toString(cost)+"c"
		+s;
	}
}
