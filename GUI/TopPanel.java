package GUI;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TopPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	public TopPanel(){
		init();
	}
	
	public void init(){
		JButton jb=new JButton("Choose a file");
		JTextField jtf=new JTextField(30);
		add(jb);
		add(jtf);
	}
}
