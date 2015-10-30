package NodeEmpire;

import java.util.ArrayList;

public class Legion {
	final static double BASE_MORALE_MAX = 100;
	final static double BASE_VETERANCY = 30;
	final static double CONSCRIPT_VETERANCY = 10;
	String name;
	int number;
	int troopCount;
	double supplies;
	double fuel;
	double morale;
	double veterancy;
	boolean hasPower;
	Troop myTroop;
	ArrayList <NHero> myOfficers;
	
	public Legion (Troop t, String n, int num, ArrayList <NHero> o){
		myOfficers = o;
		myTroop = t;
		number = num;
		name = n;
		supplies = 0;
		fuel = 0;
		morale = 0;
		veterancy = 0;
		hasPower = true;
	}
	public void initializeMV (boolean isRegular){
		morale = BASE_MORALE_MAX + myTroop.getMoraleBonus();
		if (isRegular){
			veterancy = BASE_VETERANCY;
		} else {
			veterancy = CONSCRIPT_VETERANCY;
		}
	}
	public void changeNumbers (int change){
		number = number + change;
		if (number<0){
			number = 0;
		}
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
	public void estimateDamage (Legion other){
		
	}
	public void calculateDamage (Legion other){
		
	}
}
