package NodeEmpire;

public class NHero {

	
	final static int ATR_CNT = 5;
	final static int GEN_CNT = 4;
	final static int PRE_CNT = 4;
	
	final static int WAR = 0;
	final static int TACTICS = 1;
	final static int STRATEGY = 2;
	final static int LOGISTICS = 3;
	final static int ADMIN = 4;
	
	final static int STR = 0;
	final static int CHR = 1;
	final static int INT = 2;
	final static int POT = 3;
	
	final static int RESEARCH = 0;
	final static int DEVELOP = 1;
	final static int CONQUEST = 2;
	final static int CULTURE = 3;
	
	final static int GENE_MAX = 21;
	final static int ATR_BASE_MAX = 11;
	final static int PRE_MAX = 11;
	final static int LOWEST_RANK = 1000;
	final static int NO_RANK = -1;
	final static int RULER = 0;
	final static int FLEET_COM = 10;
	final static int ARMY_COM = 11;
	final static int GOVERNOR = 50;
	final static int SHIP_COM = 100;
	final static int LEGION_COM = 101;
	
	
	String myName, surname;
	int [] myAttribs; //war, tactics, strategy, logistics, admin
	int [] myGenes; //str, chr, int, pot
	int [] myPreferences; //research, development, conquest, art
	//note: preferences are added and percentages of each are calculated
	boolean myGender;
	NHero [] myParents;
	int birthdate;
	int achievements; //used for government promotion, increase during victories, etc
	int age;
	Planet location; //homeworld, changed when assigned as governor
	int rank;
	double loyalty;
	Faction allegiance;
	boolean immortal;
	boolean hasDied;
	boolean isAssigned;
	NHero spouse;
	
	//hero with random stats
	public NHero (String name){
		
	}
	//all specified method
	public NHero (String name, int [] attribs){
		
	}
	//totally random
	public NHero (){
		myGender = Planet.rand.nextBoolean();
		myName = synthesizeNames(myGender);
		surname = Planet.genRandName();
		myParents = null;
		birthdate = NodeFrameTest.yearCount-(Planet.rand.nextInt(30)+15);
		location = null;
		allegiance = null;
		spouse = null;
		age = 0;
		achievements = 0;
		rank = NO_RANK;
		immortal = false;
		hasDied = false;
		isAssigned = false;
		loyalty = 1000;
		initializeAttribs();
		initializeGenes ();
		initializePrefs ();
	}
	public NHero (int bd, Planet loc){
		myGender = Planet.rand.nextBoolean();
		myName = synthesizeNames(myGender);
		surname = Planet.genRandName();
		myParents = null;
		birthdate = bd;
		location = loc;
		allegiance = null;
		spouse = null;
		age = 0;
		achievements = 0;
		rank = NO_RANK;
		immortal = false;
		hasDied = false;
		isAssigned = false;
		loyalty = location.loyalty+0;
		initializeAttribs();
		initializeGenes ();
		initializePrefs ();
	}
	//generate a mate to an already extent NHero
	public NHero (boolean gender, int bd, Planet loc){
		myGender = gender;
		myName = synthesizeNames(myGender);
		surname = Planet.genRandName();
		myParents = null;
		birthdate = bd;
		location = loc;
		allegiance = null;
		spouse = null;
		age = 0;
		achievements = 0;
		rank = NO_RANK;
		immortal = false;
		hasDied = false;
		isAssigned = false;
		loyalty = location.loyalty+0;
		initializeAttribs();
		initializeGenes ();
		initializePrefs ();
	}
	//progeny of two NHeros
	public NHero (NHero n1, NHero n2, int bd, Planet loc){
		myGender = Planet.rand.nextBoolean();
		myName = synthesizeNames(myGender);
		if (Planet.rand.nextBoolean()){
			surname = n2.surname;
		} else {
			surname = n1.surname;
		}
		myParents = new NHero []{n1,n2};
		birthdate = bd;
		location = loc;
		allegiance = null;
		spouse = null;
		age = 0;
		achievements = 0;
		rank = NO_RANK;
		immortal = false;
		hasDied = false;
		isAssigned = false;
		loyalty = location.loyalty+0;
		initializeAttribs(n1, n2);
		initializeGenes (n1,n2);
		initializePrefs ();
	}
	public void marry (NHero other){
		other = spouse;
		other.spouse=this;
	}
	public void marryRandomSame (){
		NHero other = new NHero (!myGender,NodeFrameTest.yearCount-(Planet.rand.nextInt(30)+15),location);
		spouse = other;
		other.spouse=this;
	}
	//totally random
	public void initializeAttribs(){
		myAttribs = new int [ATR_CNT];
		for (int i=0;i<myAttribs.length;i++){
			myAttribs [i] = Planet.rand.nextInt(ATR_BASE_MAX);
		}
		if (location!=null && location.facilityCount[Planet.ACADEMY]>0){
			rerollAttribs();
		}
	}
	//random with input from parents
	public void initializeAttribs(NHero n1, NHero n2){
		myAttribs = new int [ATR_CNT];
		for (int i=0;i<myAttribs.length;i++){
			myAttribs [i] = Planet.rand.nextInt(ATR_BASE_MAX);
			if (Planet.rand.nextDouble()<0.4){
				myAttribs[i]=n1.myAttribs[i];
			}
			if (Planet.rand.nextDouble()<0.4){
				myAttribs[i]=n2.myAttribs[i];
			}
		}
		if (location.facilityCount[Planet.ACADEMY]>0){
			rerollAttribs();
		}
	}
	public void rerollAttribs (){
		for (int i=0;i<myAttribs.length;i++){
			int reroll = Planet.rand.nextInt(ATR_BASE_MAX);
			if (reroll>myAttribs[i]){
				myAttribs [i] = reroll;				
			}
		}
	}
	//totally random
	public void initializeGenes(){
		myGenes = new int [GEN_CNT];
		for (int i=0;i<myGenes.length;i++){
			myGenes [i] = Planet.rand.nextInt(GENE_MAX);
		}
	}
	//random with input from parents
	public void initializeGenes(NHero n1, NHero n2){
		myGenes = new int [GEN_CNT];
		for (int i=0;i<myGenes.length;i++){
			myGenes [i] = Planet.rand.nextInt(GENE_MAX);
			if (Planet.rand.nextDouble()<0.4){
				myGenes[i]=n1.myGenes[i];
			}
			if (Planet.rand.nextDouble()<0.4){
				myGenes[i]=n2.myGenes[i];
			}
		}
	}
	public void initializePrefs(){
		myPreferences = new int [PRE_CNT];
		for (int i=0;i<myPreferences.length;i++){
			myPreferences [i] = Planet.rand.nextInt(PRE_MAX);
		}
	}
	public void initializeAge(int turn){
		age = turn-birthdate;
	}
	public static String synthesizeNames(boolean gender){
		String s = "";
		if (Planet.rand.nextBoolean()){
			s = s + Planet.consonants[Planet.rand.nextInt(Planet.consonants.length)];
		}
		s = s + Planet.vowels[Planet.rand.nextInt(Planet.vowels.length)];
		if (Planet.rand.nextBoolean()){
			s = s + Planet.consonants[Planet.rand.nextInt(Planet.consonants.length)];
		}
		s = s + Planet.vowels[Planet.rand.nextInt(Planet.vowels.length)];
		s = s + Planet.consonants[Planet.rand.nextInt(Planet.consonants.length)];
		if (Planet.rand.nextBoolean()){
			s = s + Planet.vowels[Planet.rand.nextInt(Planet.vowels.length)];
		}
		if (!gender){
			s = s + Planet.vowels[Planet.rand.nextInt(Planet.vowels.length)];
		}
		return s;
	}
}
