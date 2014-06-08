package GUI;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * @author zy
 * @function build the middle part of the GUI (the TextArea)
 */
public class MiddlePanel extends JPanel{
	private static final long serialVersionUID = 2L;
	private JTextArea jta=null;
	
	public MiddlePanel(){
		init();
	}
	
	public void init(){
		jta=new JTextArea(20, 100);
		jta.setEditable(false);
		setLayout(new BorderLayout());
		add(new JScrollPane(jta));
	}
	
	
	public void setMessage(String s){
		jta.setText(s);
	}
	
	public void appendMessage(String s){
		jta.append(s);
	}

}
