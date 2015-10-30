package NodeEmpire;

import java.util.ArrayList;

public class Ship {
	final static double BASE_INTEGRITY = 1000;
	final static double BASE_MORALE = 100;
	final static double BASE_INT_DMG = 400;
	
	String name;
	ShipClass myShipClass;
	double armor;
	double propulsion;
	double reactor;
	double [] weapons;
	double reactorStorage;
	double shieldlvl;
	double integrity; //hull integrity
	double fuel;
	double supplies;
	int crewNum;
	double morale;
	ArrayList <NHero> officers;
	double veterancy;
	boolean hasPower;
	boolean isDestroyed;
	
	public Ship (ShipClass sc){
		name = "";
		myShipClass = sc;
		fuel = 0;
		supplies = 0;
		veterancy = 0;
		officers = new ArrayList <NHero>();
		officers.add(new NHero());
		isDestroyed = false;
		refresh();
	}
	public Ship (ShipClass sc, boolean f){
		name = "";
		myShipClass = sc;
		fuel = 0;
		supplies = 0;
		veterancy = 0;
		officers = new ArrayList <NHero>();
		isDestroyed = false;
		refresh();
	}
	public void refresh(){
		initIntegrity();
		morale = BASE_MORALE;
		crewNum = myShipClass.crewsize;
		hasPower = true;
	}
	public void initIntegrity () {
		armor = BASE_INTEGRITY;
		propulsion =BASE_INTEGRITY;
		reactor=BASE_INTEGRITY;
		integrity=BASE_INTEGRITY;
		weapons = new double [myShipClass.shipw.length];
		for (int i=0;i<weapons.length;i++){
			weapons[i]=BASE_INTEGRITY;;
		}
		reactorStorage = myShipClass.shipr.storage;
		shieldlvl = myShipClass.shipa.shield;
	}
	public double calcMaxRange (){
		double r = 0;
		for (int i=0;i<myShipClass.shipw.length;i++){
			if (myShipClass.shipw[i].range>r){
				r = myShipClass.shipw[i].range;
			}
		}
		return r;
	}
	public void takeDamage (Ship enemy){
		//all cannons fire, shields absorb damage, parts are hit
//		for (int i=0;i<enemy.myShipClass.shipw.length;i++){
//
//		}
		isDestroyed = Planet.rand.nextBoolean() || (Planet.rand.nextBoolean() && --crewNum <1);
		enemy.isDestroyed = Planet.rand.nextBoolean() || (Planet.rand.nextBoolean() && --enemy.crewNum <1);;
		if (officers.size()>0 && officers.get(0).myAttribs[NHero.WAR]>Planet.rand.nextInt(20)){
			isDestroyed = false;
		}
		if (enemy.officers.size()>0 && enemy.officers.get(0).myAttribs[NHero.WAR]>Planet.rand.nextInt(20)){
			enemy.isDestroyed = false;
		}
		if (isDestroyed){
			for (int i=0;i<officers.size();i++){
				officers.get(i).hasDied=true;
			}
		}
		if (enemy.isDestroyed){
			for (int i=0;i<officers.size();i++){
				officers.get(i).hasDied=true;
			}
		}
	}
	public void fireBarrage (Ship enemy){
		if (hasPower && !isDestroyed && !enemy.isDestroyed){
			for (int i=0;i<myShipClass.shipw.length;i++){
				if (Planet.rand.nextDouble()<weapons[i]/BASE_INTEGRITY){
					enemy.receiveBarrage(myShipClass.shipw[i]);
				}
			}
			if (calcMaxRange() <= enemy.calcMaxRange() && Planet.rand.nextBoolean() && enemy.hasPower && enemy.isDestroyed){
				enemy.fireBarrage(this);
			}
		}
	}
	public void receiveBarrage (ShipWeapon weapon){
		double count = weapon.range;
		for (int i=0;i<count;i++){
			if (Planet.rand.nextDouble()>propulsion/BASE_INTEGRITY || Planet.rand.nextDouble()<weapon.accuracy/myShipClass.shipp.power){
				double dmg = weapon.power;
				if (shieldlvl>0){
					shieldlvl = shieldlvl-dmg;
					dmg = dmg - shieldlvl;
					if (shieldlvl<0){
						shieldlvl=0;
					}
				}
				if (dmg>0){
					if (armor>0 && Planet.rand.nextDouble()<armor/BASE_INTEGRITY){
						armor = armor - dmg * BASE_INT_DMG;
					} else {
						if (crewNum>0){
							crewNum = crewNum - Planet.rand.nextInt(crewNum);
						}
						if (reactor>0 && Planet.rand.nextDouble()>armor/BASE_INTEGRITY){
							reactor = reactor - dmg * BASE_INT_DMG;
							if (Planet.rand.nextDouble()>reactor/BASE_INTEGRITY){
								hasPower = false;								
							}
							if (Planet.rand.nextDouble()>reactor/BASE_INTEGRITY){
								isDestroyed = true;								
							}
						} else {
							isDestroyed = true;
						}
						if (integrity>0 && Planet.rand.nextDouble()>armor/BASE_INTEGRITY){
							integrity = integrity - dmg * BASE_INT_DMG;
							if (Planet.rand.nextDouble()>integrity/BASE_INTEGRITY){
								isDestroyed = true;
							}
						} else {
							if (integrity <=0 || crewNum < 1 || !hasPower){
								isDestroyed = true;
							}
						}
						if (!isDestroyed){
							if (propulsion>0 && Planet.rand.nextDouble()>armor/BASE_INTEGRITY){
								propulsion = propulsion - dmg * BASE_INT_DMG;
							}
							for (int w=0;w<weapons.length;w++){
								if (weapons[w]>0 && Planet.rand.nextDouble()>armor/BASE_INTEGRITY){
									weapons[w] = weapons [w] - dmg * BASE_INT_DMG;
								}							
							}
						}
					}
				}
			} 
		}
	}
	public void exchangeFire (Ship enemy){
		
	}
	public void checkDestroyed (){
		isDestroyed = officers.size()==0 || (Planet.rand.nextBoolean() && reactor<BASE_INTEGRITY);
	}
	public void gainVeterancy (){
		veterancy = veterancy + 0.5;
	}
}
