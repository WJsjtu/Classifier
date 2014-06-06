package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TopPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	public TopPanel(){
		init();
	}
	
	public void init(){
		JButton jb=new JButton("Choose a file");
		final JTextField jtf=new JTextField(30);
		jtf.setEditable(false);
		add(jb);
		add(jtf);
		
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser data=new JFileChooser();
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
	}
}
