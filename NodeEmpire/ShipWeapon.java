package NodeEmpire;

public class ShipWeapon {

	final static int W_SHORT = 1;
	final static int W_MEDIUM = 2;
	final static int W_LONG = 3;
	
	double power;
	double accuracy;
	double range;
	double armorPen;
	double shieldPen;
	double size;
	boolean canDisable;
	double cost;
	double efficiency;
	
	public ShipWeapon (double p, double acc, double r,
			double ap, double sp, double sz, boolean cd,
			double c){
		power = p;
		accuracy = acc;
		range = r;
		armorPen = ap;
		shieldPen = sp;
		size = sz;
		canDisable = cd;
		cost = c;
		efficiency = ((power+armorPen+shieldPen)*accuracy*range)/(size*cost);
	}
	public String toString (){
		String s = "";
		if (canDisable){
			s = "D";
		}
		return 
		Double.toString(power)+"p" +
		Double.toString(accuracy)+"a" +
		Double.toString(range)+"r"+
		Double.toString(armorPen)+"ap"+
		Double.toString(shieldPen)+"sp"+
		Double.toString(size)+"sz"+
		Double.toString(cost)+"c"
		+s;
	}
}
