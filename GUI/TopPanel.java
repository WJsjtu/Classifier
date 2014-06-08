package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import file_pretreatment.Cross_validation;

/**
 * 
 * @author zy
 * @function build the top part of the GUI
 */
public class TopPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private Thread work=null;
	private JButton start=null;

	public TopPanel(){
		init();
	}
	
	public void init(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel jp1=new JPanel();
		JButton jb=new JButton("Choose a file");
		final JTextField jtf=new JTextField(90);
		jtf.setEditable(false);
		jp1.add(jb);
		jp1.add(Box.createHorizontalStrut(10));
		jp1.add(jtf);
		
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser data=new JFileChooser(".");
				data.showOpenDialog(null);
				File f=data.getSelectedFile();
				
				if(f!=null){
					try {
						Main.path=f.getCanonicalPath();
						jtf.setText(f.getCanonicalPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		JPanel jp2=new JPanel();
		
		JPanel prompt_pane=new JPanel();
		prompt_pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel prompt=new JLabel("Select an algorithm :  ");
		prompt.setLayout(new FlowLayout(FlowLayout.LEFT));
		prompt.setHorizontalAlignment(JLabel.LEFT);
		prompt_pane.add(prompt);
		jp2.add(prompt_pane);
		
		String[] algorithms={"Logstic Regression","Decision Tree C4.5","Decision Tree ID3"};
		final JComboBox<String> algorithm=new JComboBox<String> (algorithms);
		jp2.add(algorithm);
		
		jp2.add(Box.createVerticalStrut(10));
		
		JPanel button_panel=new JPanel();
		start=new JButton("Start");
		button_panel.add(start);
		jp2.add(Box.createHorizontalStrut(20));
		jp2.add(button_panel);
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				/**
				 * if haven't choose the file
				 */
				if(Main.path.equals(""))
					JOptionPane.showMessageDialog(null, "Please choose the data file", 
							"No data file choosen", JOptionPane.ERROR_MESSAGE);
				else{
					work=new Thread(new Cross_validation());
					work.start();
					Main.bp.setStatus("Working...");
					Main.algorithm=algorithm.getSelectedIndex();
					Main.mp.setMessage("=== Start working ===\nPlease wait serveral seconds(about 40s)\n\n");
					start.setEnabled(false);
				}
			}
		});
		
		add(jp1);
		add(jp2);
	}
	
	public void enable_button(){
		start.setEnabled(true);
	}
}
