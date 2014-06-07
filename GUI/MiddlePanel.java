package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import file_pretreatment.MessLines;

public class MiddlePanel extends JPanel{
	private static final long serialVersionUID = 2L;
	private Thread work=null;
	private JButton start=null;
	private JTextArea jta=null;
	
	public MiddlePanel(){
		init();
	}
	
	public void init(){
		add(build_left_part());
		jta=new JTextArea(20, 40);
		jta.setEditable(false);
		add(new JScrollPane(jta));
	}
	
	public void enable_button(){
		start.setEnabled(true);
	}
	
	public void setMessage(String s){
		jta.setText(s);
	}
	
	public void appendMessage(String s){
		jta.append(s);
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
		LeftPanel.add(Box.createVerticalStrut(10));
		
		JPanel button_panel=new JPanel();
		start=new JButton("Start");
		button_panel.add(start);
		LeftPanel.add(button_panel);
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(Main.path.equals(""))
					JOptionPane.showMessageDialog(null, "Please choose the data file", 
							"No data file choosen", JOptionPane.ERROR_MESSAGE);
				else{
					work=new Thread(new MessLines());
					work.start();
					Main.bp.setStatus("Working...");
					start.setEnabled(false);
				}
			}
		});
		
		return LeftPanel;
	}

}
