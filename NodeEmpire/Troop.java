package NodeEmpire;

public class Troop {
	final static int INFANTRY = 0;
	final static int ARMOR = 1;
	final static int ROBOTIC = 2;
	
	final static int [] BASE_CAP = {100,150,300}; //base capacities per troop type
	final static double [] SUPPLY_CON = {1,2,0}; //supply consumption per time unit
	final static double [] BASE_FUEL_CON = {0,1,2}; //fuel consumption per time unit
	
	//might need a troop factory class to initialize troops
	int troopType;
	int capacity;//this might be necessary in determining speed; it is the base capacity+higher capacity determined by tech levels
	TroopWeapon [] troopw;
	TroopArmor troopa;
	
	public Troop (int tt, int capbonus, TroopWeapon [] tw, TroopArmor ta){
		troopType = tt;
		capacity = capbonus + BASE_CAP[tt];
		troopw = tw;
		troopa = ta;	
	}
	public Troop (int tt, int capbonus, Faction f){
		troopType = tt;
		capacity = capbonus + BASE_CAP[tt];
		//generate armor and weapons based on faction tech
//		troopw = tw;
//		troopa = ta;	
	}
	public double getMoraleBonus (){
		double d=0;
		for (int i=0;i<troopw.length;i++){
			d = d+troopw[i].moralebonus;
		}
		return d;
	}
}
