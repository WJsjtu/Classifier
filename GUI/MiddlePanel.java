package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MiddlePanel extends JPanel{
	private static final long serialVersionUID = 2L;
	private Thread work=null;
	
	public MiddlePanel(){
		init();
	}
	
	public void init(){
		add(build_left_part());
		JTextArea jta=new JTextArea(20, 30);
		jta.setEditable(false);
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
		final JButton start=new JButton("Start");
		final JButton pause=new JButton("Pause");
		pause.setEnabled(false);
		button_panel.add(start);
		button_panel.add(pause);
		LeftPanel.add(button_panel);
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(Main.path.equals(""))
					JOptionPane.showMessageDialog(null, "请选择数据文件", "未选择数据文件", JOptionPane.ERROR_MESSAGE);
				else{
					work=new Thread();
					work.start();
					Main.bp.setStatus("Starting...");
					pause.setEnabled(true);
					start.setEnabled(false);
				}
			}
		});
		
		pause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(work!=null && !work.isInterrupted()){
					try {
						work.wait();
						pause.setText("Resume");
						start.setEnabled(true);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				else if(work!=null && work.isInterrupted()){
					work.notify();
					pause.setText("Pause");
				}
				
			}
		});
		
		return LeftPanel;
	}

}
