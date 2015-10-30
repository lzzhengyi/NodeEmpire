package NodeEmpire;

import java.util.ArrayList;

public class Government {

	/*
	 * stores the leaders and administrators of a faction
	 * their stats are used to influence faction actions
	 */
	final static int MIN_OFFICIALS = 10;
	final static int MAX_OFFICIALS = 100;
	final static int BASE_AGE_DIE = 58;
	final static double CHANCE_DECLINE = 0.98;
	
	//any government positions will be listed here
	//other governments could be implemented as subobjects of this one
	/*
	 * Leaders  track ruler and his successors
	 * fleetCommanders track generals and admirals in charge of fleets and armies
	 * governors track node rulers
	 * nobles track powerful elites
	 * officials track generic officers who can be assigned to ships and governing positions
	 * 
	 * Current noble philosophy: add them to nobles, remove them when governors are needed
	 * and when ships are produced to act as governors or commanders,
	 * but promote them when tapped
	 */
	ArrayList <NHero> leaders,fleetCommanders,governors,nobles,officials,members;
	Faction faction;
	
	public Government (Faction f){
		faction = f;
		leaders = new ArrayList <NHero> ();
		fleetCommanders = new ArrayList <NHero> ();
		governors = new ArrayList <NHero> ();
		nobles = new ArrayList <NHero> ();
		officials = new ArrayList <NHero> ();
		members = new ArrayList <NHero> ();
	}
	public void tickCycle (){
		conscriptOfficials();
		checkMembers();
		updateAges();
		pruneDead();
		checkSuccession();
		affirmSuccession();
//		checkCoup();
	}
	/**
	 * Refill the pool of assignable officers
	 */
	public void conscriptOfficials (){
		for (int i=0;i<faction.connectedNodes.size();i++){
			officials.addAll(faction.connectedNodes.get(i).planet.trainOfficers());
		}
		//rewrite this if necessary
		//not sure whether nobles are added to nobles, officials, or both
		for (int i=0;i<faction.noblefamilies.size();i++){
			faction.noblefamilies.get(i).tickCycle();
//			officials.addAll(faction.noblefamilies.get(i).assignable);
			nobles.addAll(faction.noblefamilies.get(i).assignable);
			faction.noblefamilies.get(i).assignable.clear();
		}
		if (officials.size()<MIN_OFFICIALS){
			while (officials.size()<MIN_OFFICIALS){
				officials.add(new NHero(NodeFrameTest.yearCount-20,faction.capital.planet));
			}
		}
	}
	/**
	 * Ensure that the position of ruler is filled
	 */
	public void checkSuccession (){
		while (faction.ruler.hasDied){
			if (leaders.size()>0){
				if (!leaders.get(0).hasDied){
					faction.ruler=leaders.get(0);
				} else {
					while (leaders.size()>0 && leaders.get(0).hasDied){
						leaders.remove(0);
					}
				}
			} else {
				if (officials.size()>0){
					faction.ruler=officials.remove(0);
				} else {
					faction.ruler=new NHero (NodeFrameTest.yearCount-20,faction.capital.planet);
				}
				//implement unrest
			}

		}
	}
	/**
	 * Select a loyal official, governor, general, or noble, and add to leaders list
	 */
	public void affirmSuccession (){

	}
	/**
	 * Check to see if any fleet commander or nobles launch coups
	 */
	public void checkCoup(){
		if (leaders.size()>1){
			for (int i=leaders.size()-1;i>0;i--){
				if (leaders.get(i).loyalty<Planet.REBELLION_THRESHOLD){		
					simulateCoup(leaders.remove(i));
					
				}
			}
		}
		//repeat the check for governors, nobles, and commanders
	}
	/**
	 * Coup success depends upon comparison of the tactics and strategy of the ruler and rebel
	 * along with the ratio of loyal to disloyal leaders
	 */
	public void simulateCoup (NHero rebel){
		leaders.trimToSize();
		
	}
	/**
	 * update the members list
	 */
	public void checkMembers (){
		members.clear();
		members.addAll(leaders);
		members.addAll(fleetCommanders);
		members.addAll(governors);
		members.addAll(nobles);
		members.addAll(officials);
	}
	public void updateAges(){
		for (int i=0;i<members.size();i++){
			members.get(i).age = NodeFrameTest.yearCount-members.get(i).birthdate;
		}
		checkOldAge();
	}
	public void checkOldAge (){
		for (int i=0;i<members.size();i++){
			if (!members.get(i).immortal && members.get(i).age>=BASE_AGE_DIE){
				double chance = 0.98;
				for (int j=BASE_AGE_DIE;j<members.get(i).age;j++){
					chance = chance*CHANCE_DECLINE;
				}
				members.get(i).hasDied = chance>Planet.rand.nextDouble();				
			}
		}
	}
	public void pruneDead (){
		for (int i=leaders.size()-1;i>=0;i--){
			if (leaders.get(i).hasDied){
				leaders.remove(i);
			}
		}
		for (int i=nobles.size()-1;i>=0;i--){
			if (nobles.get(i).hasDied){
				nobles.remove(i);
			}
		}
		for (int i=fleetCommanders.size()-1;i>=0;i--){
			if (fleetCommanders.get(i).hasDied){
				fleetCommanders.remove(i);
			}
		}
		for (int i=governors.size()-1;i>=0;i--){
			if (governors.get(i).hasDied){
				governors.remove(i);
			}
		}
		for (int i=officials.size()-1;i>=0;i--){
			if (officials.get(i).hasDied){
				officials.remove(i);
			}
		}
	}
}
