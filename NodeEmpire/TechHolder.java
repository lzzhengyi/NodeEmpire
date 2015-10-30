package NodeEmpire;

public class TechHolder {
 /*
  * essentially a holder of the booleans and tech levels a faction has enabled
  * econo
  * agric
  * finan
  * indus
  * const
  * 
  * pTechs gain levels over time
  * bTechs are booleans
  */
	//basic stats for ship components
	final static double BASIC_COST = 100;
	final static double BASIC_SIZE = 3;
	
	//amt of tech points needed to gain a level
	final static int TP_MAX = 1000;
	
	//the corresponding techs for facility tech indices
	final static int F_COST = 0;
	final static int I_OUTPUT = 1;
	
	//rough list of corresponding techs for weapon/armor techs
	final static int W_POWER = 0;
	final static int W_RANGE = 1;
	final static int A_SHIELD = 1;
	final static int A_REGEN = 2;
	
	//counts of each kind of tech
	final static int FAC_TECH_C = 10;
	final static int MILIT_TECH_C = 6;
	final static int OTHER_TECH_C = 7;
	final static int SWEAP_C = 5;
	final static int BTECH_C = 20;
	
	//lists of tech indices
	final static int TECH_T = 0;
	final static int AGRI_T = 1;
	final static int ECON_T = 2;
	final static int SHIP_T = 3;
	final static int MINE_T = 4;
	final static int FACT_T = 5;
	final static int REFI_T = 6;
	final static int ACAD_T = 7;
	final static int BARR_T = 8;
	final static int LIBR_T = 9;
	
	final static int TWEP_T = 10;
	final static int TARM_T = 11;
	final static int SWEP_T = 12;
	final static int SARM_T = 13;
	final static int SPRP_T = 14;
	final static int SRCT_T = 15;
	
	final static int CONT_T = 16;
	final static int LOGI_T = 17;
	final static int TAXA_T = 18;
	final static int LONG_T = 19;
	final static int GNAG_T = 20;
	final static int FDEF_T = 21;
	final static int FLED_T = 22;
	
	final static int PHOT_T = 0;
	final static int PART_T = 1;
	final static int PLAS_T = 2;
	final static int ARCE_T = 3;
	final static int KINE_T = 4;
	
	int [][] pTechs; //techs that progressively increase in power
	int[][] pTechProgress; //progress tracks points added to that tech
	int [][] wTechs;
	int [][] wTechProgress;
	boolean [] bTechs;
	int[] bTechProgress;
	
	final static ShipWeapon W_LASER = new ShipWeapon (1,2,3,0,0,BASIC_SIZE,false,BASIC_COST);
	final static ShipReactor R_BASIC = new ShipReactor (1,5,1,0.02,BASIC_SIZE,BASIC_COST);
	final static ShipReactor R_PLUS = new ShipReactor (R_BASIC.power*20,R_BASIC.storage*20,
				R_BASIC.consumption*20,R_BASIC.explosionChance,BASIC_SIZE*20,BASIC_COST*20);
	final static ShipPropulsion P_BASIC = new ShipPropulsion(1,1,BASIC_SIZE,BASIC_COST);
	final static ShipArmor A_BASIC = new ShipArmor (1,1,0,0,BASIC_SIZE,BASIC_COST);
	final static ShipClass SHIP_DESTROYER_BASIC= new ShipClass (ShipClass.B_CREW_SIZE[ShipClass.DESTROYER],
			ShipClass.DESTROYER,ShipWeapon.W_LONG,ShipClass.HULLCAPS[ShipClass.DESTROYER],
			A_BASIC,multiplyPropulsion(ShipClass.DESTROYER,P_BASIC),R_BASIC,new ShipWeapon[]{W_LASER,W_LASER,W_LASER,W_LASER,W_LASER,W_LASER});

//	method that initializes weapons of the four trees
//	method that initializes ships of each size
	public TechHolder (){
		initializeTechs();
		
	}
	public void initializeTechs (){
		pTechs = new int [FAC_TECH_C+MILIT_TECH_C+OTHER_TECH_C][];
		for (int i=0;i<FAC_TECH_C;i++){
			pTechs[i]=new int []{0,0};
		}
		pTechs[TWEP_T]=new int[]{0,0,0,0,0,0};
		pTechs[TARM_T]=new int[]{0,0,0,0,0};
		pTechs[SWEP_T]=new int[]{0,0,0,0,0,0,0};
		pTechs[SARM_T]=new int[]{0,0,0,0,0};
		pTechs[SPRP_T]=new int[]{0,0,0,0};
		pTechs[SRCT_T]=new int[]{0,0,0,0,0,0};
		for (int i=0;i<OTHER_TECH_C;i++){
			pTechs[i+MILIT_TECH_C]=new int []{0,0};
		}
		pTechProgress = pTechs.clone();
		wTechs = new int [SWEAP_C][];
		for (int i=0;i<SWEAP_C;i++){
			wTechs[i]=new int[]{0};
		}
		wTechProgress = wTechs.clone();
		bTechs = new boolean [BTECH_C];
		bTechProgress = new int [BTECH_C];
		for (int i=0;i<bTechs.length;i++){
			bTechs[i]=false;
			bTechProgress[i]=0;
		}
	}
	public void gainTech (int[][]update, int i1, int i2,int amt){
		
	}
	public void gainBTech (int [] update,int index, int amt){
		update[index]=update[index]+amt;
		
	}
	//doesn't count bonus techs
	public int calculateTotalTechPoints (){
		int cnt = 0;
		for (int i=0;i<FAC_TECH_C;i++){
			for (int j=0;j<pTechs[i].length;j++){
				cnt = cnt + pTechProgress[i][j];
				cnt = calculateInvestTP (pTechs [i][j]);
			}
		}
		for (int i=FAC_TECH_C;i<FAC_TECH_C+MILIT_TECH_C;i++){
			for (int j=0;j<pTechs[i].length;j++){
				cnt = cnt + pTechProgress[i][j];
				cnt = calculateInvestTP (pTechs [i][j]);
			}
		}
		for (int i=FAC_TECH_C+MILIT_TECH_C;i<OTHER_TECH_C+FAC_TECH_C+MILIT_TECH_C;i++){
			for (int j=0;j<pTechs[i].length;j++){
				cnt = cnt + pTechProgress[i][j];
				cnt = calculateInvestTP (pTechs [i][j]);
			}
		}
		for (int i=0;i<SWEAP_C;i++){
			for (int j=0;j<wTechs[i].length;j++){
				cnt = cnt + wTechProgress[i][j];
				cnt = calculateInvestTP (wTechs [i][j]);
			}
		}
		
		return cnt;
	}
	public void combineTech (TechHolder other){
		
	}
	public static int calculateInvestTP (int lvl){
		int cnt = 0;
		for (int i=lvl;i>0;i--){
			cnt = i*TP_MAX+cnt;
		}
		return cnt;
	}
	/*
	 * begin with the reactor, then propulsion
	 * Reactor = most efficient
	 * Propulsion = one unit per 10 capacity
	 * then weapons for smaller than cruisers,
	 * or armor from bigger than cruisers
	 * 
	 * max weapon-armor value for cruisers
	 */
	public void designShip (int stype, double maxcost){
		
	}
	public Ship purchaseDefaultShip (int stype){
		return new Ship (SHIP_DESTROYER_BASIC, false);
	}
	public static ShipPropulsion multiplyPropulsion (int stype, ShipPropulsion sp){
		double mult = ShipClass.HULLCAPS[stype]/ShipClass.HULLCAPS[ShipClass.FIGHTER];
		return new ShipPropulsion (sp.power*mult,sp.consumption*mult,
				sp.size*mult,sp.cost*mult);
	}

}
