package NodeEmpire;

import javax.swing.JFrame;

public class NodeDriver {
	public static void main(String[] args) {
	NodeFrameTest ndt = new NodeFrameTest();
	ndt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	ndt.setTitle("Sphere Game Demo");

	ndt.getContentPane();
	ndt.pack();
	ndt.setVisible(true);
	
	ndt.init();
	}
}
