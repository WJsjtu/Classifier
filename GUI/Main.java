package GUI;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	public static String path="";
	public static boolean algorithm=true;  //true for Logistic Regression, false for Decision Tree C4.5
	public static BottomPanel bp=null;
	
	public static void main(String[] args) {
		JFrame jf=new JFrame("Classification");
		JPanel jp=new JPanel();
		jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
		jp.add(new TopPanel());
		jp.add(new MiddlePanel());
		
		BottomPanel bp=new BottomPanel();
		jp.add(bp);
		jf.add(jp);
		jf.setVisible(true);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
