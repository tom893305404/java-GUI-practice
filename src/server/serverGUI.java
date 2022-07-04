package server;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

class serverGUI extends JFrame{
	private JButton START;
	private JButton STOP;
	private JLabel comLabel;
	private server Server;
	serverGUI(server _server){
		Server = _server;
		this.UserInterFaceInit();
		this.actionlistener();
	}
	private void UserInterFaceInit() {
		var cp = this.getContentPane();
		cp.setLayout(null);
		//Start button
		START = new JButton("Start");
		START.setBounds(10, 10, 90, 45);
		//START Button
		START.addActionListener(new ActionListener(){  
			 public void actionPerformed(ActionEvent e){//push the button Start to Start Server  
			        comLabel.setText("Start");
			     }
			 }
			 );  
		//Stop button
		STOP  = new JButton("Stop");
		STOP.setBounds(120, 10, 90, 45);
		//Stop Button
		STOP.addActionListener(new ActionListener(){  
			 public void actionPerformed(ActionEvent e){  
			        comLabel.setText("Stop");
	         }  
	     });  
		//TestLabel
		//TODO 做好伺服器端終端介面
		comLabel = new JLabel();
		comLabel.setBounds(10,65,200,100);
		comLabel.setBackground(Color.white);
		comLabel.setOpaque(true);
		comLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		comLabel.setVerticalAlignment(JLabel.BOTTOM);
		comLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//Frame
		this.setBounds(0, 0, 240, 250);
		this.setTitle("Gobang Server");
		//add
		cp.add(START);
		cp.add(STOP);
		cp.add(comLabel);
		
		this.setVisible(true);
	}
	private void actionlistener() {


	}
	public void cPrint(String text) {
		
		comLabel.setText(text);
		
	}
}