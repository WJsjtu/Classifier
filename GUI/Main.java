package GUI;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	public static void main(String[] args) {
		JFrame jf=new JFrame("Classification");
		JPanel jp=new JPanel();
		jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
		jp.add(new TopPanel());
		jp.add(new MiddlePanel());
		jp.add(new BottomPanel());
		jf.add(jp);
		jf.setVisible(true);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
