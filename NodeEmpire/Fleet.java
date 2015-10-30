package NodeEmpire;

import java.util.ArrayList;

public class Fleet {
	String name;
	MapLocation location;
	ArrayList <Ship> ships;
	ArrayList <NHero> officers;
	ArrayList <Army> army;
	double fuel;
	double supplies;
	double fuelCost;
	double supplyCost;
	boolean isAggressor;
	//stores ships, officers, fuel, other supplies, 
	//carried troops
	public Fleet (){
		name = "";
		ships = new ArrayList <Ship>();
		officers = new ArrayList <NHero>();
		army = new ArrayList <Army>();
		fuel = 0;
		supplies = 0;
		fuelCost = 0;
	}
	public void changeSupplies (double change){
		supplies = supplies + change;
		if (supplies<0){
			supplies = 0;
		}
	}
	public void changeFuel (double change){
		fuel = fuel + change;
		if (fuel<0){
			fuel = 0;
		}
	}
	public void depleteSupplies (){
		supplies = supplies - supplyCost;
		if (supplies<0){
			supplies=0;
		}
		//insert starvation here
	}
	public void depleteFuel (){
		fuel = fuel - fuelCost;
		if (fuel<0){
			fuel=0;
		}
	}	
	public void calculateFuelCost(){
		fuelCost = 0;
		for (int i=0;i<ships.size();i++){
			fuelCost = fuelCost + ships.get(i).myShipClass.capacity;
		}
	}
	public void calculateSupplyCost(){
		supplyCost = 0;
		for (int i=0;i<ships.size();i++){
			supplyCost = supplyCost + ships.get(i).crewNum;
		}
	}
	public int removeDead (){
		String cnt = "";
		int count = 0;
		boolean removed=false;
		for (int i=ships.size()-1;i>=0;i--){
			if (ships.get(i).isDestroyed){
				count++;
				removed = true;
				cnt = ships.remove(i).name;	
//				cnt = cnt+ships.remove(i).name+",";	
			} else {
				ships.get(i).veterancy++;
			}
		}
		ships.trimToSize();
//		if (removed){
//			NodeFrameTest.np.blf.updateText(count+" ships destroyed, including "+cnt);
//			System.out.println(count+" ships destroyed, including "+cnt);	
//		}
		return count;
	}
	public void combineFleet (Fleet other){
		ships.addAll(other.ships);
		officers.addAll(other.officers);
		army.addAll(other.army);
		fuel=fuel+other.fuel;
		supplies=supplies+other.supplies;
		calculateFuelCost();
		calculateSupplyCost();
	}
	public void initializeOfficers (){
		NHero commander = null;
		officers.clear();
		for (int i=0;i<ships.size();i++){
			if (ships.get(i).officers.size()>0){
				for (int j=0;j<ships.get(i).officers.size();j++){
					NHero candidate = ships.get(i).officers.get(j);
					if (candidate.rank>NHero.FLEET_COM){
						officers.add(candidate);
					} else if (commander ==null && candidate.rank<=NHero.FLEET_COM){
						commander = candidate;
					} else if (candidate.rank<=NHero.FLEET_COM && candidate.rank<commander.rank){
						officers.add(0, commander);
						commander = candidate;
					} else if (candidate.rank>=NHero.FLEET_COM) {
						officers.add(0, candidate);
					}
				}
			}
		}
		if (commander != null){
			officers.add(0, commander);
		} else {
			if (officers.size()>0){
				officers.get(0).rank=NHero.FLEET_COM;
				System.out.println(officers.get(0).myName+" promoted to Fleet Admiral");
			}
		}
	}
}
