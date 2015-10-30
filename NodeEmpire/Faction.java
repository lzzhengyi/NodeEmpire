package NodeEmpire;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;

public class Faction {
	final static double BASE_TAX_RATE = 0.2;
	final static int SHIPS_PER_PLANET = 5000;
	Integer number;
	String name;
	NHero ruler;
	NobleFamily firstFamily;
	ArrayList <NobleFamily> noblefamilies;
	ArrayList <Node> nodes;
	ArrayList <Node> connectedNodes;
	Node capital;
	Government government;
	double treasury;
	double lastIncome;
	TechHolder techholder;
	Color factionColor;
	double taxrate;
	Hashtable <Integer,RelationsObject> relations;
	ArrayList <Fleet> fleets;
	ArrayList <Army> armies;
	ArrayList <NHero> heroes;
	Fleet combinedFleet;
	int ship_count;
	int fleet_count;
	static ArrayList <Faction> factions;
	int foundingYear;
	
	Faction (Node n){
		government = new Government(this);
		foundingYear = NodeFrameTest.yearCount;
		name = Planet.genRandName();
		treasury = 0;
		lastIncome = 0;
		techholder = new TechHolder();
		capital = n;
		capital.planet.isCapital=true;
		nodes = new ArrayList <Node>();
		connectedNodes = nodes;
		noblefamilies = new ArrayList <NobleFamily>();
		nodes.add(capital);
		factionColor = new Color (Planet.rand.nextInt(256),Planet.rand.nextInt(256),Planet.rand.nextInt(256));
		taxrate = BASE_TAX_RATE;
		number = null;
		relations = null;
		ruler = capital.planet.myFamily.founder;
		firstFamily = capital.planet.myFamily;
		capital.planet.governor=ruler;
		capital.allegiance=this;
		capital.planet.planetTech.combineTech(techholder);
		ship_count = 0;
		fleet_count = 0;
		heroes = new ArrayList <NHero>();
		fleets = new ArrayList <Fleet>();
		armies = new ArrayList <Army>();
//		capital.planet.facilityCount[Planet.ACADEMY]=5;
	}
	public void updateRelations (Hashtable<Integer, RelationsObject> ht){
		if (relations==null){
			relations = ht;
		}
		while (ht.elements().hasMoreElements()){
			RelationsObject ro = ht.elements().nextElement();
			if (!relations.containsKey(ro.faction.number)){
				relations.put(ro.faction.number, ro);
			}
		}
	}
	public void removeFaction (Integer facno){
		if (relations!=null){
			relations.remove(facno);
		}
	}
	public void tickCycle (){
//		if (ruler!=null){
//			
//		}
//		System.out.println(ruler.myName+" "+ruler.surname+" moves");
		if (capital==null && nodes.size()>0){
			capital = nodes.get(Planet.rand.nextInt(nodes.size()));
		}
		if (nodes.size()==0){
			invadeRandomAny();
		} else {
			if (nodes.size()>10){
				capital.planet.printStats();
			}
			updateConnectedNodes();
			collectTaxes();
			if (treasury>Planet.INDEX_MAX)
				treasury = Planet.INDEX_MAX;
			shipMinerals();
			shipSupplies();
			supplyFleet();
			conscriptHeroes();
			pruneDead();
			colonizeEmpty();
			formFleet();
			buildShips();
			inflictShipAttrition();
			if (ruler.myPreferences[NHero.CONQUEST]>3){
				treasury = treasury-100;
				capital.planet.facilityCount[Planet.ACADEMY]++;
			}
			if (ruler.myPreferences[NHero.CONQUEST]>6){
				treasury = treasury-100;
				capital.planet.facilityCount[Planet.ACADEMY]++;
			}
			if (ruler.myPreferences[NHero.CONQUEST]>3 && NodeFrameTest.turnCount>10){
				invadeRandom();
			}
			nodes.trimToSize();
		}
	}
	public void pruneDead (){
		for (int i=0;i<heroes.size();i++){
			if (heroes.get(i).hasDied){
				heroes.remove(i);
			}
		}
	}
	public double calcTotalSupplies (int rid){
		double total = 0;
		for(int i=0;i<nodes.size();i++){
			total = total + nodes.get(i).planet.stockpiles[rid];
		}
		return total;
	}
	public static double calcTotalSupplies (ArrayList <Node> nlist,int rid){
		double total = 0;
		for(int i=0;i<nlist.size();i++){
			total = total + nlist.get(i).planet.stockpiles[rid];
		}
		return total;
	}
	//should return all nodes connected to the capital in an arraylist
	public void updateConnectedNodes (){
		if (capital!=null && nodes.size()>0){
			//placeholder
			connectedNodes = nodes;
		}
	}
	//need a method to gather resources of the empire
	//get taxes from all connected nodes
	//if this time is not high enough, raise tax rate
	public void collectTaxes (){
		double og = treasury+0;
		if (connectedNodes.size()>0){
			for (int i=0;i<connectedNodes.size();i++){
				double tax =connectedNodes.get(i).planet.collectTax(taxrate);
				//this is strangely written if you follow the code, I don't know why
				if (nodes.contains(connectedNodes.get(i))){
					treasury = treasury + tax;
				} else {
					connectedNodes.get(i).planet.grantIncome(tax);
				}
			}
		}
		lastIncome = treasury-og;
	}
	//add minerals of all connected nodes to the capital
	public void shipMinerals (){
		if (connectedNodes.size()>0 && capital !=null){
			for (int i=0;i<connectedNodes.size();i++){
				if (nodes.contains(connectedNodes.get(i))){
					capital.planet.receiveMinerals(nodes.get(i).planet.shipMinerals());
				} 
			}
		}
	}
	//calculate the amount needed to support fleets,
	//divide that by
	//nodes control, and requisition from nodes
	//supply cost = hullsize
	public void shipSupplies (){
		
	}
	//gather up supplies/fuel and then issue them to the fleet
	//make sure that the fleet and troops can fight for 3 turns
	public void supplyFleet (){
		//
	}
	public void inflictShipAttrition(){
		double supply = 0;
		for (int i=0;i<nodes.size();i++){
			supply = supply + nodes.get(i).planet.stockpiles[Planet.SUPPLIES];
		}
		supply = Math.max(100, supply/10000);
		int supplyAmt = (int) Math.min(SHIPS_PER_PLANET, supply);
		while (fleets.get(0).ships.size()>supplyAmt){
			for (int i=fleets.get(0).ships.size()-1;i>=0;i--){
				fleets.get(0).ships.get(i).isDestroyed=Planet.rand.nextBoolean();
			}
			fleets.get(0).removeDead();
		}
	}
	/*
	 * need to resolve this by incorporating it into the Government object
	 */
	public void conscriptHeroes(){
		for (int i=0;i<connectedNodes.size();i++){
			heroes.addAll(connectedNodes.get(i).planet.trainOfficers());
		}
		for (int i=0;i<noblefamilies.size();i++){
			noblefamilies.get(i).tickCycle();
			heroes.addAll(noblefamilies.get(i).assignable);
			noblefamilies.get(i).assignable.clear();
		}
		if (heroes.size()<10){
			while (heroes.size()<10){
				heroes.add(new NHero(NodeFrameTest.yearCount-20,capital.planet));
			}
		}
	}
	//colonize one empty province per turn from capital
	public void colonizeEmpty(){
		ArrayList <Node> empty = new ArrayList <Node>();
		for (int i=0;i<nodes.size();i++){
			empty.addAll(nodes.get(i).checkAdjAbandoned());
		}
		if (empty.size()>0 && capital.planet.population>Node.COLONY_SIZE*2){
			capital.planet.population=capital.planet.population-Node.COLONY_SIZE;
			Node claimed = empty.get(Planet.rand.nextInt(empty.size()));
			claimed.allegiance.nodes.remove(claimed);
			nodes.add(claimed);
			claimed.resettle(this);
		}
	}
	public Node findInvasionTarget (){
		ArrayList <Node> enemy = new ArrayList <Node>();
		for (int i=0;i<nodes.size();i++){
			enemy.addAll(nodes.get(i).checkAdjEnemy(this));
		}
		if (enemy.size()>0 && fleets.get(0).ships.size()>0){
			return enemy.get(Planet.rand.nextInt(enemy.size()));
		} else {
			return null;
		}
	}
	public boolean isFactionValid (){
		return (fleets.size()>0 && fleets.get(0).ships.size()>0) || nodes.size()>0;
	}
	//determine fleet limit
	//buy most efficient ship
	//currently placeholder
	/*
	 * This needs to be rewritten to take officers from the government
	 * This will also spawn local rather than global fleets
	 */
	public void formFleet (){
		//remove dead fleets
		if (fleets.size()>0&&fleets.get(0).ships.size()==0){
			fleets.remove(0);
			fleets.trimToSize();
		}
		//form a new fleet if no fleets exist
		if (fleets.size()<1){
			fleets.add(new Fleet());
			fleets.get(0).name=name+" Fleet "+Integer.toString(++fleet_count);
		}
		//initialize fleet officer roster
		for (int i=0;i<fleets.size();i++){
			if (fleets.get(i).officers.size()<1 && fleets.get(i).ships.size()>0){
				fleets.get(i).initializeOfficers();
			}
		}
	}
	public void invadeRandomAny (){
		if (factions!=null){
			Faction enemy=this;
			while (enemy==this && factions.size()>1){
				enemy = factions.get(Planet.rand.nextInt(factions.size()));				
			}
			if (enemy.nodes.size()>0){
				Node target = enemy.nodes.get(Planet.rand.nextInt(enemy.nodes.size()));
				Fleet f1 = fleets.get(0);
				target.planet.sufferInvasion(f1.ships.size());
//				System.out.println(target.allegiance.name+" is raided!");
				if (f1 == resolveBattleRandom(f1, target.allegiance.fleets.get(0))){
					NodeFrameTest.np.blf.updateText(name+" seizes "+target.planet.myName+" from "+enemy.name);
					System.out.println(name+" seizes "+target.planet.myName+" from "+enemy.name);
					target.allegiance.nodes.remove(target);
					if (target ==target.allegiance.capital){
						target.allegiance.capital=null;
						target.planet.isCapital=false;
					}
					target.allegiance=this;
					nodes.add(target);
					capital = target;
					target.planet.isCapital=true;
				}
			}
		}
	}
	public void invadeRandom (){
		Node target = findInvasionTarget();
		if (target!=null){
			Fleet f1 = fleets.get(0);
			target.planet.sufferInvasion(f1.ships.size());
			if (f1 == resolveBattleRandom(f1, target.allegiance.fleets.get(0))){
				NodeFrameTest.np.blf.updateText(name+" captures "+target.planet.myName+" from "+target.allegiance.name);
				target.allegiance.nodes.remove(target);
				if (target ==target.allegiance.capital){
					target.allegiance.capital=null;
					target.planet.isCapital=false;
				}
				target.planet.sufferCapture(f1.ships.size());
				target.allegiance=this;
				nodes.add(target);
			}
		}
	}
	public Fleet resolveBattleRandom(Fleet f1, Fleet f2){
		int turnCount = Planet.rand.nextInt(10)+10; //the constant should be the propulsion level of the attacker
		int f1losses = 0;
		int f2losses = 0;
		while (turnCount>0 && f1.ships.size()>0 && f2.ships.size()>0){
			turnCount--;
//			for (int i=0;i<f1.ships.size();i++){
//				if (i<f2.ships.size()){
//					f1.ships.get(i).takeDamage(f2.ships.get(i));
//				}
//			}
			for (int i=0;i<f1.ships.size();i++){
				f1.ships.get(i).fireBarrage(f2.ships.get(Planet.rand.nextInt(f2.ships.size())));
			}
			f1losses = f1losses + f1.removeDead();
			f2losses = f2losses + f2.removeDead();
			if (f1.ships.size()<=3){
				for (int i=0;i<f2.ships.size();i++){
					int target = Planet.rand.nextInt(f1.ships.size());
//					System.out.println("Attacking ship "+target+" "+f1.ships.get(target).name+" "+f1.ships.get(target).armor+" "+f1.ships.get(target).integrity);
					f2.ships.get(i).fireBarrage(f1.ships.get(target));
				}				
			} else{
				for (int i=0;i<f2.ships.size();i++){
					f2.ships.get(i).fireBarrage(f1.ships.get(Planet.rand.nextInt(f1.ships.size())));
				}				
			}
			f1losses = f1losses + f1.removeDead();
			f2losses = f2losses + f2.removeDead();
		}
		if (f1losses>0){
			NodeFrameTest.np.blf.updateText(f1losses+" ships destroyed of the "+f1.name);
		}
		if (f2losses>0){
			NodeFrameTest.np.blf.updateText(f2losses+" ships destroyed of the "+f2.name);
		}
		if (f1.ships.size()>f2.ships.size()){
			return f1;
		} else if (f1.ships.size()<f2.ships.size()){
			return f2;
		} else {
			return null;
		}
	}
	public void buildShips (){
		//calculate how many ships exist and how many must be built
		int shipc=0;
		for (int i=0;i<fleets.size();i++){
			shipc=shipc+fleets.get(i).ships.size();
		}
		//rewrite this code to spawn new ships over each planet
		ArrayList <Ship> slist =new ArrayList <Ship>();
		while (shipc<lastIncome/100 && heroes.size()>0){
			Ship n = techholder.purchaseDefaultShip(ShipClass.DESTROYER);
			n.name="IFS"+name+Integer.toString(ship_count);
//			System.out.println(n.name+" purchased");
			heroes.get(0).rank=NHero.SHIP_COM;
			n.officers.add(heroes.remove(0));
			ship_count++;
			shipc=shipc+100;
			slist.add(n);
		}
		//adding all spaceships, one by one to each fleet
		while (slist.size()>0){
			for (int i=0;i<fleets.size()&&slist.size()>0;i++){
				fleets.get(i).ships.add(slist.remove(0));
			}
		}
	}
}
