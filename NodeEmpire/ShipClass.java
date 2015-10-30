package NodeEmpire;

public class ShipClass {
	final static int CREW_PAY = 10;
	static int crew_pay;
	final static int FIGHTER = 0;
	final static int CARRIER = 1;
	final static int STARFIGHTER = 2;
	final static int DESTROYER = 3;
	final static int CRUISER = 4;
	final static int BATTLESHIP = 5;
	final static int DREADNAUGHT = 6; 
	
	final static int LONG = 2;
	final static int MEDIUM = 1;
	final static int SHORT = 0;
	
	final static String [] SHIPNAMES = {"Fighter","Carrier",
			"Starfighter","Destroyer","Cruiser",
			"Battleship","Dreadnaught"};
	final static int [] HULLCAPS = {10,100,20,40,60,80,100};
	final static int [] B_CREW_SIZE = {1,800,2,50,100,300,500};
	
	int shipType;
	int range;
	int crewsize;
	ShipArmor shipa;
	ShipPropulsion shipp;
	ShipReactor shipr;
	ShipWeapon [] shipw;
	double capacity; //this is essentially hull-type
	//size is also the value from which component capacity is subtracted
	double empty_capacity; //leftover space
	double cost; //cost of the ship
	
	//generates a specific ship
	public ShipClass (int cn,int st, int r, int sz,
			ShipArmor sa, ShipPropulsion sp,
			ShipReactor sr, ShipWeapon [] sw){
		crewsize = cn;
		shipType = st;
		range = r;
		capacity = sz;
		shipa = sa;
		shipp = sp;
		shipr = sr;
		shipw = sw;
		
		calcUsedCapacity();
		calcCost();
	}
	//auto-assembles a ship type given faction specs
	public ShipClass (int st, int range, int size, Faction f){
		
	}
	//returns the ship type, 
	public String toString () {
		String s = "";
		for (int i=0;i<shipw.length;i++){
			s = s + shipw[i].power+"w"+i+"p";
		}
		return 
		SHIPNAMES[shipType]+s+
		Double.toString(shipa.armor)+"a"+
		Double.toString(shipa.shield)+"s"+
		Double.toString(shipr.power)+"r"+
		Double.toString(shipp.power)+"p";
	}
	public double calcUsedCapacity (){
		double cap = 0;
		cap = cap + shipp.size;
		cap = cap + shipr.size;
		cap = cap + shipa.size;
		for (int i=0;i<shipw.length;i++){
			cap = cap + shipw[i].size;
		}
		empty_capacity = capacity-cap;
		return cap;
	}
	public double calcCost (){
		double cap = 0;
		cap = cap + shipp.cost;
		cap = cap + shipr.cost;
		cap = cap + shipa.cost;
		cap = cap + crew_pay*crewsize;
		for (int i=0;i<shipw.length;i++){
			cap = cap + shipw[i].cost;
		}
		cost = cap;
		return cap;
	}
	public void setCrewPay (int val){
		crew_pay = val;
	}
	
}
