package NodeEmpire;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Connector implements MapLocation {

	Node n1, n2;
	Faction allegiance;
	ArrayList <Fleet> fleet;
	
	public Connector (Node one, Node two){
		n1 = one;
		n2 = two;
		allegiance = null;
		fleet = null;
	}
	public void draw (Graphics page){
		if (allegiance !=null){
			page.setColor(allegiance.factionColor);
		} else {
			page.setColor(Color.WHITE);
		}
		page.drawLine(n1.myloc.x, n1.myloc.y, n2.myloc.x, n2.myloc.y);
	}
}
