package NodeEmpire;

public class TroopArmor {

	double armor; //does not regen during battle
	double shield; //max/starting shield value
	double rate; //rate of shield regen
	double consumption; //fuel consumed per time unit to power shields
	double size; //capacity penalty - this can be negative for powered armor
	boolean powered; //can be disabled by disabling weapons
	double cost;
	double efficiency;
	
	public TroopArmor (double a, double s, double r, double cm, double sze, boolean pwd, double cost){
		armor = a;
		shield = s;
		rate = r;
		consumption = cm;
		size = sze;
		powered = pwd;
		cost = s;
		efficiency = (a+s*r)/(cost*size);
	}
	public String toString (){
		String s = "";
		if (powered){
			s = "P";
		}
		return 
		Double.toString(armor)+"a" +
		Double.toString(shield)+"s" +
		Double.toString(rate)+"r"+
		Double.toString(consumption)+"c"+
		Double.toString(size)+"sz"+
		Double.toString(cost)+"c"
		+s;
	}
}
