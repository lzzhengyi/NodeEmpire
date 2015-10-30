package NodeEmpire;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Node implements MapLocation{
	final static int SIZE = 10;
	final static int COLONY_SIZE = 50000;
	public ArrayList <Connector> connectors;
	Point myloc;
	Planet planet;
	Faction allegiance;
	ArrayList <Fleet> fleet;
	int priority;
	
	public Node (Point c){
		priority = 1000;
		myloc = c;
		planet = new Planet(this);
		allegiance = null;
		fleet = new ArrayList <Fleet> ();
		connectors = new ArrayList <Connector>();
	}
	public ArrayList <Node> checkAdjAbandoned (){
		ArrayList <Node> emptyList = new ArrayList <Node>();
		for (int i=0;i<connectors.size();i++){
			if (connectors.get(i).n1.planet.population<1 && !emptyList.contains(connectors.get(i).n1)){
				emptyList.add(connectors.get(i).n1);
			}
			if (connectors.get(i).n2.planet.population<1 && !emptyList.contains(connectors.get(i).n2)){
				emptyList.add(connectors.get(i).n2);
			}
		}
		return emptyList;
	}
	public ArrayList <Node> checkAdjEnemy(Faction f){
		ArrayList <Node> emptyList = new ArrayList <Node>();
		for (int i=0;i<connectors.size();i++){
			if (connectors.get(i).n1.allegiance!=f && !emptyList.contains(connectors.get(i).n1)){
				emptyList.add(connectors.get(i).n1);
			}
			if (connectors.get(i).n2.allegiance!=f && !emptyList.contains(connectors.get(i).n2)){
				emptyList.add(connectors.get(i).n2);
			}
		}
		return emptyList;
	}
	public void tickCycle (){
		planet.tickCycle();
		dispatchSettlers();
		if (NodeFrameTest.turnCount%4==0){
			processYear();
		}
	}
	public void processYear(){
		
	}
	public void dispatchSettlers (){
		if (planet.population>COLONY_SIZE*2){
			ArrayList <Node>empty = checkAdjAbandoned();
			if (empty.size()>0){
				Node claimed = empty.get(Planet.rand.nextInt(empty.size()));
				if (claimed.allegiance == allegiance || (claimed.planet.defFleet.ships.size()==0 && claimed.fleet.size()==0)){
					planet.population=planet.population-COLONY_SIZE;
					claimed.allegiance.nodes.remove(claimed);
					allegiance.nodes.add(claimed);
					claimed.resettle(allegiance);
				}
			}
		}
	}
	public void draw (Graphics page){
		if (allegiance !=null){
			page.setColor(allegiance.factionColor);
		} else {
			page.setColor(Color.BLACK);
		}
		if (myloc != null){
			if (planet.population>0){
				page.fillOval(myloc.x, myloc.y, SIZE, SIZE);
			} else {
				page.drawOval(myloc.x, myloc.y, SIZE, SIZE);	
			}
		}
		if (planet!=null){
			page.drawString(planet.myName, myloc.x, myloc.y);
		}
//		if (planet.defFleet.ships.size()>0){
//			page.drawString(Integer.toString(planet.defFleet.ships.size()), myloc.x+SIZE, myloc.y+SIZE);
//		}
		if (allegiance.fleets.size()>0 && allegiance.fleets.get(0).ships.size()>0){
			page.drawString(Integer.toString(allegiance.fleets.get(0).ships.size()), myloc.x+SIZE, myloc.y+SIZE);
		}
	}
	public void resettle (Faction f){
		if (planet.population<1){
			planet.isCapital=false;
			allegiance = f;
			planet.population=COLONY_SIZE;
			if (planet.facilityCount[Planet.LIBRARY]>0){
				planet.planetTech.combineTech(f.techholder);
				f.techholder.combineTech(planet.planetTech);
			} else {
				planet.planetTech=new TechHolder();
			}
			
		}
	}
}
