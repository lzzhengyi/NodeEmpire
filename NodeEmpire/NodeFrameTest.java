package NodeEmpire;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class NodeFrameTest extends JFrame {

	final static int START_DATE = -1000;
	static NodePanel np;
	static int turnCount;
	static int yearCount;//year is advanced every four turns

	public NodeFrameTest (){
		turnCount = 0;
		yearCount = START_DATE;
		np = new NodePanel();
		np.setPreferredSize (new Dimension (1050,850));
		add (np);
	}
	public void init (){

		/*
		 * plan: 
		 * generate 20 nodes
		 * generate 30 connections
		 * draw nodes
		 * draw connections
		 */
		np.genNodes();
	}
	public class NodePanel extends JPanel implements ActionListener {
		final static int TIMER_DEFAULT = 1000;
		final static int CON_DIST = 100;
		final static int NUM_NODES = 70;
		final static int REBELLION_LIMIT = 50;
		final static int MAP_SIZE = 800;

		Node [] nlist;
		Connector [] clist;
		ArrayList <Connector> conl;
		ArrayList <Faction> facl;
		Hashtable <Integer,RelationsObject> rom;
		int fac_cnt;
		boolean fac_init;
		Timer timer;
		MapPanel mapDis;
		BattleLogFrame blf;
		FactionDisplayFrame fdf;

		public NodePanel (){
			nlist = new Node [NUM_NODES];
			clist = new Connector [30];
			conl = new ArrayList <Connector> ();
			facl = new ArrayList <Faction> ();
			fac_cnt = 0;
			fac_init = false;
			setLayout(new BorderLayout());
			mapDis = new MapPanel();
			add (mapDis, BorderLayout.CENTER);
			timer = new Timer(TIMER_DEFAULT, this);
			blf = new BattleLogFrame();
			fdf = new FactionDisplayFrame(facl);
			timer.start();
		}
		public void genNodes (){
			ArrayList <Node> checkl = new ArrayList <Node> ();
//			facl = new ArrayList <Faction> ();
			Random rand = new Random ();

			for (int i=0;i<nlist.length;i++){
				nlist[i]=new Node (new Point (rand.nextInt(MAP_SIZE),rand.nextInt(MAP_SIZE)));
				checkl.add(nlist[i]);
				facl.add(new Faction(nlist[i]));
				nlist[i].allegiance=facl.get(i);
			}
			Faction.factions=facl;
			createFactionMap();
//			for (int i=0;i<facl.size();i++){
//				facl.get(i).updateRelations(rom);
//			}
			for (int i=0;i<nlist.length;i++){
				//				ArrayList <DistanceObject> dlist = new ArrayList <DistanceObject>();
				PriorityQueue<DistanceObject> dlist = new PriorityQueue <DistanceObject>();
				for (int j=0;j<checkl.size();j++){
					if (!checkl.get(j).equals(nlist[i]))
						dlist.add(new DistanceObject(checkl.get(j),calculateDistance(checkl.get(j),nlist[i])));
				}
				do{
					if (dlist.peek()!=null){
						Node no1 = nlist[i];
						Node no2 = dlist.poll().node;
						Connector cn = new Connector(no1,no2);
						no1.connectors.add(cn);
						no2.connectors.add(cn);
						conl.add(cn);						
					}
				}while (dlist.peek()!=null && dlist.peek().distance<CON_DIST);
				checkl.remove(0);
			}
			//			for (int i=0;i<clist.length;i++){
			//				Node n1,n2;
			//				do{
			//					n1 = nlist[rand.nextInt(nlist.length)];
			//					n2 = nlist[rand.nextInt(nlist.length)];
			//				}while(n1==n2);
			//
			//				clist[i]=new Connector (n1,n2);
			//			}
		}
		public void processTurn(){
			NodeFrameTest.turnCount++;
			if (NodeFrameTest.turnCount%4==0){
				NodeFrameTest.yearCount++;
			}
			for (int i=0;i<nlist.length;i++){
				if (nlist[i].planet.population>0)
					nlist[i].tickCycle();
			}
			for (int i=0;i<facl.size();i++){
				if (facl.get(i).isFactionValid()){
					facl.get(i).tickCycle();
					if (facl.get(i).nodes.size()>REBELLION_LIMIT){
						Faction fr = facl.get(i);
						incitePopularRebellions(fr);
					}
				} else {
					facl.remove(facl.get(i));
					facl.trimToSize();
//					eliminateFaction(facl.remove(i).number);
//					Faction.factions=facl;
				}
				repaint();
			}
//			for (int i=0;i<facl.size();i++){
//				facl.get(i).updateRelations(rom);
//			}
		}
		public void eliminateFaction (Integer facno){
			rom.remove(facno);
			for (int i=0;i<facl.size();i++){
				facl.get(i).removeFaction(facno);
			}
		}
		public void incitePopularRebellions (Faction fr){
			for (int i=0;i<fr.nodes.size();i++){
				Node rebel = fr.nodes.get(i);
				if (rebel.planet.calculateRebellion()){
					Faction rebelf = new Faction(rebel);
					facl.add(rebelf);
					fr.nodes.remove(rebel);
					rebelf.formFleet();
					rebelf.fleets.get(0).combineFleet(rebel.planet.defFleet);
					rebel.planet.defFleet=new Fleet();
				}
			}
		}
		public double calculateDistance (Node n1, Node n2){
			return Math.sqrt(Math.pow((n1.myloc.x-n2.myloc.x), 2)+Math.pow(n2.myloc.y-n1.myloc.y, 2));
		}
		public void createFactionMap (){
			rom = new Hashtable <Integer,RelationsObject> ();
			if (fac_init){
				for (int i=0;i<facl.size();i++){
					facl.get(i).number=new Integer(i);
					rom.put(facl.get(i).number, new RelationsObject(facl.get(i)));
				}
			} else {
				fac_init = true;
				for (int i=0;i<facl.size();i++){
					fac_cnt++;
					facl.get(i).number=new Integer(i);
					rom.put(new Integer(i), new RelationsObject(facl.get(i)));
				}
			}
		}
		public void paintComponent(Graphics page){
			super.paintComponent(page);

//			for (int i=0;i<conl.size();i++){
//				if (i<nlist.length && nlist[i]!=null){
//					nlist[i].draw(page);
//				}
//				//				if (clist[i]!=null){
//				//					clist[i].draw(page);
//				//				}
//				if (conl.get(i)!=null){
//					conl.get(i).draw(page);
//				}
//			}
		}
		private class MapPanel extends JPanel {
			public void paintComponent(Graphics page){
				super.paintComponent(page);
				setBackground(Color.black);
				for (int i=0;i<conl.size();i++){
					if (conl.get(i)!=null){
						conl.get(i).draw(page);
					}
					if (i<nlist.length && nlist[i]!=null){
						nlist[i].draw(page);
					}
				}
				page.drawString(Integer.toString(yearCount), MAP_SIZE, MAP_SIZE);
			}
		}
		class BattleLogFrame extends JFrame {
			JTextArea frameLog;
			JScrollPane jsp;
			JPanel jp;

			public BattleLogFrame (){	
				jp = new JPanel ();
				frameLog = new JTextArea ("");
				frameLog.setLineWrap(true);
				jsp = new JScrollPane (frameLog,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jsp.setPreferredSize (new Dimension (300,400));
				jp.add (jsp);
				add (jp);
				
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				setTitle("Battle Log");

				getContentPane();
				pack();
				setVisible(true);
			}
			public void showMe (){
				//			getContentPane();
				//			pack();
				setVisible(true);
			}
//			public void initializeFrame (){
	//
//			}
			public void clearText (){
				frameLog.setText("");
			}
			public void updateText (String update) {
				frameLog.append(update+"\n");
			}
		}
		private class FactionDisplayFrame extends JFrame implements ActionListener {
			FactionPanel jp;
			JButton pauseToggle, clearLog;
			
			public FactionDisplayFrame (ArrayList <Faction> facl){
				jp = new FactionPanel (facl);
				jp.setLayout(new BorderLayout());
				JPanel jp1 = new JPanel();
				jp1.setLayout (new BoxLayout(jp1, BoxLayout.PAGE_AXIS));
				
				pauseToggle = new JButton("Toggle Pause");
				clearLog = new JButton ("Clear Battle Log");
				pauseToggle.addActionListener (this);
				clearLog.addActionListener (this);
				add (jp, BorderLayout.CENTER);
				jp1.add (pauseToggle);
				jp1.add (clearLog);
				add (jp1, BorderLayout.SOUTH);
				
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				setTitle("Faction Log");
				setPreferredSize (new Dimension (300,700));
				
				getContentPane();
				pack();
				setVisible(true);
			}
			private class FactionPanel extends JPanel {
				
				ArrayList <Faction> facl;
				
				public FactionPanel (ArrayList <Faction> fac){
					facl = fac;
				}
				public void paintComponent(Graphics page){
					super.paintComponent(page);
					setBackground(Color.black);
					page.setColor(Color.WHITE);
					page.drawString("Factions: "+Integer.toString(facl.size()), 500, 100);
					for (int i=0;i<facl.size();i++){
						page.setColor(facl.get(i).factionColor);
						if (facl.get(i).fleets.size()>0){
							page.drawString(facl.get(i).name+" Ships: "+Integer.toString(facl.get(i).fleets.get(0).ships.size())
									+" Nodes: "+Integer.toString(facl.get(i).nodes.size())+" Founding Year: "+Integer.toString(facl.get(i).foundingYear), 5, 10+i*10);		
						} else {
							page.drawString(facl.get(i).name+" Ships: 0 Nodes: "+Integer.toString(facl.get(i).nodes.size())+" Founding Year: "+Integer.toString(facl.get(i).foundingYear), 5, 10+i*10);		
						}
				
					}

				}
			}
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==pauseToggle){
					if (timer.isRunning()){
						timer.stop();
					} else {
						timer.start();
					}
				}
				if (e.getSource()==clearLog){
					NodeFrameTest.np.blf.frameLog.setText("");
				}
			}
		}
		public void actionPerformed(ActionEvent e) {
			
			processTurn();
			repaint();
			mapDis.repaint();
			fdf.jp.repaint();
		}
	}

}

