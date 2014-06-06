package GUI;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BottomPanel extends JPanel{
	private static final long serialVersionUID = 3L;
	private JLabel status=new JLabel("hello");

	public BottomPanel(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(status);
	}
	
	public void setStatus(String s){
		status.setText(s);
	}
}
