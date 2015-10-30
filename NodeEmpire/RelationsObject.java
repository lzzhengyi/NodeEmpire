package NodeEmpire;

public class RelationsObject {
	final static double BASE_RELATIONS_VAL = 50;
	final static double ALLIANCE_VAL = 1000;
	final static double FRIENDLY_VAL = 300;
	final static double ENEMY_VAL = 0;
	double relation;
	Faction faction;
	boolean noAttackTreaty;
	boolean noAttackTurns;
	boolean alliance;
	boolean tribute;
	boolean tributeAmt;
	
	public RelationsObject (double r, Faction f){
		faction = f;
		relation = r;
	}
	public RelationsObject (Faction f){
		faction = f;
		relation = BASE_RELATIONS_VAL;
	}
}
