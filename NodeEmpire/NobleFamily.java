package NodeEmpire;

import java.util.ArrayList;

public class NobleFamily {
	final static int FEMALE_DESCENT = 0;
	final static int MALE_DESCENT = 1;
	final static int OLDEST_DESCENT = 2;
	
	final static int BIRTHPERIOD = 30;
	final static int MAX_MEMBERS = 200; 
	
	final static int BASE_AGE_MAT = 15;
	final static int BASE_AGE_OLD = 50;
	final static int BASE_AGE_DIE = 58;
	final static double CHANCE_DECLINE = 0.98;
	int birthperiod;
	int inherit_type;
	ArrayList <NHero> members;
	ArrayList <NHero> assignable; //contains members not yet assigned; these may be underage
	NHero founder;
	NHero leader;
	String surname;
	
	public NobleFamily(NHero f){
		birthperiod = 0;
		inherit_type = 2;
		founder = f;
		leader = f;
		surname = f.surname;
		members = new ArrayList <NHero>();
		members.add(founder);
		assignable = new ArrayList <NHero>();
		assignable.add(founder);
	}
	//this method will check for tech later
	//check at: base age die, chance decline, or if immortal
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
		for (int i=members.size()-1;i>=0;i--){
			if (members.get(i).hasDied){
				members.remove(i);
			}
		}
		for (int i=assignable.size()-1;i>=0;i--){
			if (assignable.get(i).hasDied){
				assignable.remove(i);
			}
		}
	}
	public void populate (){
		if (members.size()<4){
			ArrayList <NHero> addMe = new ArrayList <NHero>();
			for (int i=0;i<members.size();i++){
				NHero spouse = new NHero(!members.get(i).myGender,members.get(i).birthdate,members.get(i).location);
				addMe.add(spouse);
				int cnt = Planet.rand.nextInt(4);
				for (int j=0;j<cnt;j++){
					NHero neo = new NHero(members.get(i),spouse,NodeFrameTest.yearCount,members.get(i).location);
					addMe.add(neo);
					neo.surname=surname;
				}
			}
			members.addAll(addMe);
		}
	}
	//will be different 
	public void reproduce(){
		if (members.size()<MAX_MEMBERS){
			ArrayList <NHero> addMe = new ArrayList <NHero>();
			for (int i=0;i<members.size();i++){
				if (members.get(i).spouse==null){
					if (members.get(i).age>BASE_AGE_MAT){
						members.get(i).marryRandomSame();
						addMe.add(members.get(i).spouse);
						assignable.add(members.get(i).spouse);
					}
				} else if (members.get(i).spouse.hasDied) {
					members.get(i).marryRandomSame();
					addMe.add(members.get(i).spouse);
					assignable.add(members.get(i).spouse);
				}
				//generate a random amount of children
				int cnt = Planet.rand.nextInt(4);
				for (int j=0;j<cnt;j++){
					if (members.get(i).spouse!=null){
						NHero neo = new NHero(members.get(i),members.get(i).spouse,NodeFrameTest.yearCount,members.get(i).location);
						addMe.add(neo);
						assignable.add(neo);
						neo.surname=surname;
					}
				}
			}
			members.addAll(addMe);
		}
	}
	public void adopt(){
		if (members.size()==1){
			int cnt = Planet.rand.nextInt(4);
			for (int j=0;j<cnt;j++){
				NHero neo = new NHero(NodeFrameTest.yearCount-(Planet.rand.nextInt(20)),members.get(0).location);
				members.add(neo);
				assignable.add(neo);
				neo.surname=surname;
			}
		}
	}
	public void updateAges(){
		for (int i=0;i<members.size();i++){
			members.get(i).age = NodeFrameTest.yearCount-members.get(i).birthdate;
		}
		checkOldAge();
	}
	public void tickCycle(){
		if (NodeFrameTest.turnCount%4==0){
			updateAges();
		}
		pruneDead();
		if (NodeFrameTest.turnCount%4==0){
			birthperiod++;
			if (birthperiod == BIRTHPERIOD){
				adopt();
				birthperiod =0;
				reproduce();
			}
		}
	}
}
