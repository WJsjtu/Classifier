package GUI;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MiddlePanel extends JPanel{
	private static final long serialVersionUID = 2L;
	
	public MiddlePanel(){
		init();
	}
	
	public void init(){
		add(build_left_part());
		JTextArea jta=new JTextArea(20, 20);
		add(new JScrollPane(jta));
	}
	
	public JPanel build_left_part(){
		JPanel LeftPanel=new JPanel();
		LeftPanel.setLayout(new BoxLayout(LeftPanel,BoxLayout.Y_AXIS));
		
		JPanel prompt_pane=new JPanel();
		prompt_pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel prompt=new JLabel("Choose the algorithm you want :  ");
		prompt.setLayout(new FlowLayout(FlowLayout.LEFT));
		prompt.setHorizontalAlignment(JLabel.LEFT);
		prompt_pane.add(prompt);
		LeftPanel.add(prompt_pane);
		
		String[] algorithms={"Logstic Regression","Decision Tree C4.5"};
		JComboBox<String> algorithm=new JComboBox<String> (algorithms);
		LeftPanel.add(algorithm);
		
//		JPanel choose_algorithm=new JPanel();
//		choose_algorithm.add(new JLabel("algorithm"));
//		String[] algorithms={"Logstic Regression","Decision Tree C4.5"};
//		JComboBox<String> algorithm=new JComboBox<String> (algorithms);
//		choose_algorithm.add(algorithm);
//		LeftPanel.add(choose_algorithm);
		
		JPanel button_panel=new JPanel();
		JButton start=new JButton("Start");
		JButton pause=new JButton("Pause");
		button_panel.add(start);
		button_panel.add(pause);
		LeftPanel.add(button_panel);
		
		return LeftPanel;
	}

}
