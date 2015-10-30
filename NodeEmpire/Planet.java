package NodeEmpire;

import java.util.ArrayList;
import java.util.Random;

public class Planet {

	final static String [] consonants = {"b","c","ch","d","f","g","h",
		"j","k","l","m","n","p","ph","q","r","rh","s","sh",
		"sc","st","sn","t","th","tr",
		"v","w","x","y","z"};
	final static String [] vowels = {"a","e","i","o","u","y"};
	final static int BASE_AGRI = 300;
	final static int BASE_ECON = 100;
	final static int CASH_TO_INDEX = 100;
	final static double BASE_POP = 1000000;
	final static double BASE_MINE_AMT = 100;
	final static double BASE_PROCESS_AMT = 100;
	final static double BASE_CONST_AMT = 100;
	final static double BASE_PROD_AMT = 100;
	final static double BASE_FOOD_AMT = 8000;
	
	final static double MAX_LOYALTY = 1000;
	final static double REBELLION_THRESHOLD = 200;
	final static double REBELLION_CHANCE = 0.3;
	final static double START_SUPPLIES = 10000;
	final static double BASE_HOME_TAX = 0.3;
	final static double SUPPLY_MAX = 100000000;
	final static double SHIP_MAX = 2000;
	final static double ACADEMY_MAX = 50;
	final static double INDEX_MAX = 1000000000;
	
	final static int MINE = 0;
	final static int REFINERY = 1;
	final static int FACTORY = 2;
	final static int SHIPYARD = 3;
	final static int BARRACKS = 4;
	final static int ACADEMY = 5;
	final static int LIBRARY = 6;
	final static int R_CENTER = 7;
	final static int F_CENTER = 8;
	final static int A_CENTER = 9;
	
	final static int ATLANTIUM = 0;
	final static int LEMURIUM = 1;
	final static int STELLARIUM = 2;
	final static int PROTONIUM = 3;
	
	final static int SUPPLIES = 4;
	final static int FUEL = 5;
	
	final static int STOCK_COUNT = 6;
	final static int MINERAL_COUNT = PROTONIUM+1;
	final static int FACIL_COUNT = 20;
	
	String myName;
	Node node;
	boolean isCapital;
	double agriID;
	double econID;
	double population; //if this is zero, planet is disabled and faction removed
	double loyalty;
	double home_tax;
	double treasury;
	double [] minerals;
	double [] mineralrefreshrate;
	double [] stockpiles;
	double diseaseChance;
	int [] facilityCount;
	ArrayList <NHero> pNheros;
	ArrayList <Army> garrisons;
	ArrayList <Fleet> fleets;
	Fleet defFleet;
	Army defArmy;
	NHero governor;
	NobleFamily myFamily;
	TechHolder planetTech;
	Ship buildingShip;
	Legion trainingLegion;
	int shipbuildTurns;
	int legiontrainTurns;
	final static Random rand = new Random();
	int shipCount, legionCount;
	
	//all specified, may take a while to complete due to uncomplete other aspects
	Planet (String name,double agri, double econ,double population, double [] min, double [] rr,
			int [] fc) {
		
	}
	
	Planet (Node n){
		node = n;
		Random rand = new Random ();
		myName = genRandName();
		agriID = rand.nextInt(BASE_AGRI)+rand.nextDouble();
		econID = rand.nextInt(BASE_ECON)+rand.nextDouble();
		population =rand.nextInt((int)BASE_POP*3/9)+BASE_POP*6/9;
		initFacilities();
		initMinerals();
		initStockpile();
		planetTech = new TechHolder();
		pNheros = new ArrayList <NHero> ();
		isCapital = false;
		loyalty = MAX_LOYALTY;
		myFamily = new NobleFamily(new NHero(0,this));
		diseaseChance = rand.nextDouble()/2;
		shipbuildTurns = 0;
		legiontrainTurns = 0;
		buildingShip = null;
		trainingLegion = null;
		defArmy = new Army();
		defFleet = new Fleet();
		governor = null;
	}
	public static String genRandName (){
		String s = "";
		if (rand.nextDouble()>.75){
			s = s+consonants[rand.nextInt(consonants.length)];
		}
		s=s+vowels[rand.nextInt(vowels.length)];
		if (rand.nextDouble()>.66){
			s = s+consonants[rand.nextInt(consonants.length)];
		}
		s=s+vowels[rand.nextInt(vowels.length)];
		s = s+consonants[rand.nextInt(consonants.length)];
		if (rand.nextDouble()>.8){
			s = s+consonants[rand.nextInt(consonants.length)];
		}
		if (rand.nextDouble()>.2){
			s = s+consonants[rand.nextInt(consonants.length)];
			s=s+vowels[rand.nextInt(vowels.length)];
		}
		if (rand.nextDouble()>.7){
			s=s+vowels[rand.nextInt(vowels.length)];
			s = s+consonants[rand.nextInt(consonants.length)];
		}
		if (rand.nextDouble()>0.9){
			s = s+consonants[rand.nextInt(consonants.length)];
			s=s+vowels[rand.nextInt(vowels.length)];
			s=s+vowels[rand.nextInt(vowels.length)];
			s = s+consonants[rand.nextInt(consonants.length)];
		}
		return s;
	}
	public void initFacilities (){
		facilityCount = new int [FACIL_COUNT];
		for (int i=0;i<facilityCount.length;i++){
			facilityCount[i]=0;
		}
	}
	public void initMinerals (){
		minerals = new double [MINERAL_COUNT];
		mineralrefreshrate = new double [MINERAL_COUNT];
		for (int i=0;i<minerals.length;i++){
			if (rand.nextDouble()>0.4){
				minerals[i]=rand.nextInt(10000000)+rand.nextDouble()*1000000;
				mineralrefreshrate[i]=rand.nextDouble();
			} else {
				minerals[i]=0;
				mineralrefreshrate[i]=0;
				if (rand.nextDouble()>0.2){
					mineralrefreshrate[i]=rand.nextDouble();
				}
			}
		}
	}
	public void initStockpile(){
		stockpiles = new double [STOCK_COUNT];
		for (int i=0;i<stockpiles.length;i++){
			stockpiles[i]=0;
		}
		stockpiles[SUPPLIES]=START_SUPPLIES;
		stockpiles[FUEL]=START_SUPPLIES;
	}
	public void tickCycle (){
		if (myFamily.members.size()==0){
			myFamily = new NobleFamily(new NHero (NodeFrameTest.yearCount-(Planet.rand.nextInt(30)+15),this));
		}
		if (loyalty<MAX_LOYALTY){
			loyalty++;
		}
		if (legiontrainTurns>0 && --legiontrainTurns==0){
			defArmy.legions.add(trainingLegion);
			trainingLegion.myOfficers.add(trainOfficer());
			trainingLegion = null;
		}
		if (shipbuildTurns>0 && --shipbuildTurns==0){
			defFleet.ships.add(buildingShip);
			buildingShip.officers.add(trainOfficer());
			buildingShip = null;
		}
		treasury = treasury + calcIncome()*BASE_HOME_TAX;
		if (treasury>INDEX_MAX)
			treasury = INDEX_MAX;
		produceFood();
		consumeFood();
		investInfrastructure();
		if (NodeFrameTest.turnCount%4==0){
			processYear();
		}
	}
	public void processYear(){
		mineTerritory();
		refineMinerals();
		growPopulation();
		purchaseMilitary();
	}
	/*
	 * production methods processed each year
	 */
	public void mineTerritory (){
		for (int i=0;i<minerals.length;i++){
			minerals[i]=minerals[i]+mineralrefreshrate[i];
			if (minerals[i]>0){
				double amt = facilityCount[MINE] *BASE_MINE_AMT* (1+0.1 * planetTech.pTechs[TechHolder.MINE_T][TechHolder.I_OUTPUT]);
				if (amt>minerals[i]){
					amt = minerals[i];
				}
				stockpiles[i]=stockpiles[i]+amt;
				minerals[i]=minerals[i]-amt;
			}
		}
	}
	public double[] shipMinerals (){
		stockpiles[ATLANTIUM]=stockpiles[ATLANTIUM]/2;
		stockpiles[LEMURIUM]=stockpiles[LEMURIUM]/2;
		stockpiles[STELLARIUM]=stockpiles[STELLARIUM]/2;
		stockpiles[PROTONIUM]=stockpiles[PROTONIUM]/2;
		return new double [] {
				stockpiles[ATLANTIUM],
				stockpiles[LEMURIUM],
				stockpiles[STELLARIUM],
				stockpiles[PROTONIUM],
				};
	}
	public void receiveMinerals(double[] shipment){
		stockpiles[ATLANTIUM]=stockpiles[ATLANTIUM]+shipment[ATLANTIUM];
		stockpiles[LEMURIUM]=stockpiles[LEMURIUM]+shipment[LEMURIUM];
		stockpiles[STELLARIUM]=stockpiles[STELLARIUM]+shipment[STELLARIUM];
		stockpiles[PROTONIUM]=stockpiles[PROTONIUM]+shipment[PROTONIUM];
	}
	public void refineMinerals (){
		if (stockpiles[STELLARIUM]>0){
			double amt = facilityCount[REFINERY] * BASE_PROCESS_AMT* (1+0.1 * planetTech.pTechs[TechHolder.REFI_T][TechHolder.I_OUTPUT]);
			if (amt>stockpiles[STELLARIUM]){
				amt = stockpiles[STELLARIUM];
			}
			stockpiles[STELLARIUM]=stockpiles[STELLARIUM]-amt;
			stockpiles[FUEL]=stockpiles[FUEL]+amt;
		}
	}
	public void consumeFood (){
		stockpiles[SUPPLIES] = stockpiles[SUPPLIES]-(population);
		if (stockpiles[SUPPLIES]<0){
			System.out.println("Starvation on "+myName);
			population = population - rand.nextInt((int)population/2);
			stockpiles[SUPPLIES]=0;
		}
	}
	public void produceFood (){
		stockpiles[SUPPLIES] = stockpiles[SUPPLIES]+agriID*BASE_FOOD_AMT;
		if (stockpiles[SUPPLIES]>SUPPLY_MAX){
			stockpiles[SUPPLIES]=SUPPLY_MAX;
		}
	}
	public void growPopulation (){
		if (population<agriID*BASE_FOOD_AMT && population > 4){
			population = population + rand.nextInt((int)(Math.max(2, (population/4))));	
		}
	}
	public double calcIncome (){
		return (agriID+econID)*population/1000;
	}
	public double collectTax (double rate){
		return calcIncome() * rate * Math.min(500.0, loyalty)/500;
	}
	public void grantIncome (double income){
		treasury = treasury + income;
	}
	public boolean checkFacilitiesBuilt(){
		boolean pre = true;
		for (int i=0;i<facilityCount.length;i++){
			if (facilityCount[i]<1)
				pre = false;
			if (facilityCount[i]>ACADEMY_MAX)
				facilityCount[i]=(int) ACADEMY_MAX;
		}
		return pre;
	}
	public void investInfrastructure (){		
		while (agriID*BASE_FOOD_AMT<population && treasury>0){
			treasury = treasury - CASH_TO_INDEX*100;
			agriID = agriID + 100;
		}
		while (treasury<800 && treasury > 0){
			treasury = treasury - CASH_TO_INDEX*100;
			econID = econID + 100;
		}
		for (int i=0;i<facilityCount.length && treasury>0;i++){
			if (facilityCount[i]<1){
				treasury = treasury-CASH_TO_INDEX*100;
				facilityCount[i]++;
			}
		}
		if (econID>INDEX_MAX)
			econID = INDEX_MAX;
		if (agriID>INDEX_MAX)
			agriID = INDEX_MAX;
		if (econID<INDEX_MAX && agriID < INDEX_MAX && treasury>10){
			econID = econID + (treasury/10)/CASH_TO_INDEX;
			agriID = agriID + (treasury/10)/CASH_TO_INDEX;
			treasury = treasury *8/10;
		}

		purchaseMilitary();
		//check if starvation and increase infrastructure accordingly
		//then invest remainder of tax in econ until econ 500
		//after this, continue investing 0.1 in econ
		//then buy 1 of each facility
		//then, can invest 0.1 of remainder in random tech
		//then buy a legion using basic funds when minerals are sufficient
		//then, buy a defense fleet
	}
	public void sufferInvasion(int invaders){
		loyalty = loyalty-10;
		sufferRaid(invaders);
	}
	public void sufferCapture (int invaders){
		loyalty = loyalty/2;
		sufferRaid(invaders);
	}
	public void sufferRaid (int invaders){
		for (int i=0;i<facilityCount.length;i++){
			if(rand.nextBoolean()){
				facilityCount[i]=facilityCount[i]*3/4;
			}
		}
		if (population>2){
			population=population-(100*invaders+Math.max(rand.nextInt((int)population/2), rand.nextInt((int)population/2)));
			if (population<0)
				population=0;
		}else {
			population = 0;
		}
	}
	public boolean calculateRebellion (){
		return !isCapital && (population > 0 || defFleet.ships.size()>0) && loyalty<Planet.REBELLION_THRESHOLD && rand.nextDouble()>loyalty/MAX_LOYALTY;
	}
	public void purchaseMilitary(){
		if (defFleet.supplyCost<econID && defFleet.ships.size()<SHIP_MAX){
			Ship s = planetTech.purchaseDefaultShip(ShipClass.DESTROYER);
			s.name=myName+"-class"+Integer.toString(shipCount);
			shipCount++;
			s.officers.add(trainOfficer());
			defFleet.ships.add(s);
//			System.out.println(s.name+" purchased");
		}
	}
	public NHero trainOfficer (){
		NHero recruit = new NHero(NodeFrameTest.yearCount-17,this);
		if (facilityCount[ACADEMY]>0)
			recruit.rerollAttribs();
		return recruit;
	}
	public ArrayList <NHero> trainOfficers (){
		ArrayList <NHero> recruits = new ArrayList <NHero>();
		for (int i=0;i<facilityCount[ACADEMY];i++){
			NHero nx = new NHero(NodeFrameTest.yearCount-17,this);
			nx.rerollAttribs();
			recruits.add(nx);
		}
		return recruits;
	}
	public void printStats (){
		String title = myName;
		title = title + 
		"Econ:"+econID+
		"Agri:"+agriID+
		"Pop:"+population+
		"Treasury:"+treasury+
		"Income:"+calcIncome();
		;
		System.out.println(title);
	}
}
