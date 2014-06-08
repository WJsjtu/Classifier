package GUI;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static String path="";
	
	/**
	 * algorithm: 0 for Logistic Regression, 1 for Decision Tree C4.5, 2 for Decision Tree ID3
	 */
	public static int algorithm=0;  
	public static TopPanel tp=null;
	public static MiddlePanel mp=null;
	public static BottomPanel bp=null;
	
	/**
	 * 
	 * @author zy
	 * @function Build GUI
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFrame jf=new JFrame("Classification");
		JPanel jp=new JPanel();
		jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
		
		tp=new TopPanel();
		jp.add(tp);
		
		mp=new MiddlePanel();
		jp.add(mp);
		
		bp=new BottomPanel();
		jp.add(bp);
		
		jf.add(jp);
		jf.setVisible(true);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
