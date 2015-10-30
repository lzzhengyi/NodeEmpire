package NodeEmpire;

import java.util.ArrayList;

public class Army {

	ArrayList <NHero> officers;
	ArrayList <Legion> legions;
	double supplies;
	double fuel;
	double fuelCost;
	double supplyCost;
	
	public Army () {
		officers = new ArrayList <NHero>();
		legions = new ArrayList <Legion>();
		supplies = 0;
		fuel = 0;
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
	public void payMaintenance(){
		
	}
	public void payTurnMaintenance(){
		
	}
//	public void calculateFuelCost(){
//		fuelCost = 0;
//		for (int i=0;i<ships.size();i++){
//			fuelCost = fuelCost + ships.get(i).myShipClass.capacity;
//		}
//	}
//	public void calculateSupplyCost(){
//		supplyCost = 0;
//		for (int i=0;i<ships.size();i++){
//			supplyCost = supplyCost + ships.get(i).crewNum;
//		}
//	}
}
