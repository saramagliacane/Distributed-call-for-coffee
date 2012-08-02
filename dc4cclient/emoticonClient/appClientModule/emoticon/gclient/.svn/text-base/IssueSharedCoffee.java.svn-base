package emoticon.gclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.emoticon.controllers.UserManagerRemote;
import com.emoticon.entities.Location;


public class IssueSharedCoffee implements ActionListener {

	private JTextField quorum;
	private JTextField minuteText;
	private JTextField hourT;
	private JComboBox comboBox;
	private JFrame frame;
	
	private UserManagerRemote um;
	private List<Location> locations;

	private class windowB extends WindowAdapter {
		public void windowClosing(WindowEvent arg0) {
		}

	}
	
	/**
	 * Start the frame and set it visible
	 */
	public IssueSharedCoffee(UserManagerRemote um) {
		this.um = um;
		this.locations = um.listLocation();
		createContents();
		frame.setVisible(true);
		
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void createContents() {
		
		Date date = new Date();
		int hours = date.getHours();
		int minutes = date.getMinutes();
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setTitle("DC4C - New Shared Coffee Call");
		frame.setResizable(false);
		frame.setBounds(100, 100, 361, 251);
		frame.addWindowListener(new windowB());

		final JLabel locationLabel = new JLabel();
		locationLabel.setFont(new Font("", Font.BOLD, 13));
		locationLabel.setText("Location");
		locationLabel.setBounds(56, 147, 55, 16);
		frame.getContentPane().add(locationLabel);

		final JLabel hourLabel = new JLabel();
		hourLabel.setText("Hour");
		hourLabel.setBounds(117, 55, 31, 16);
		frame.getContentPane().add(hourLabel);

		final JLabel newTimedCoffeeLabel = new JLabel();
		newTimedCoffeeLabel.setForeground(new Color(0, 0, 255));
		newTimedCoffeeLabel.setFont(new Font("", Font.BOLD, 16));
		newTimedCoffeeLabel.setText("New Shared Coffee Call");
		newTimedCoffeeLabel.setBounds(87, 10, 186, 31);
		frame.getContentPane().add(newTimedCoffeeLabel);

		comboBox = new JComboBox();
		comboBox.setBounds(117, 143, 166, 25);
		Iterator t = this.locations.iterator();
		while(t.hasNext()){
			Location l = (Location)t.next();
			comboBox.addItem(l);
		}
		frame.getContentPane().add(comboBox);

		final JLabel minutesLabel = new JLabel();
		minutesLabel.setText("Minutes");
		minutesLabel.setBounds(154, 55, 46, 16);
		frame.getContentPane().add(minutesLabel);

		final JLabel timeLabel = new JLabel();
		timeLabel.setFont(new Font("", Font.BOLD, 13));
		timeLabel.setText("Expiration Time");
		timeLabel.setBounds(10, 80, 101, 16);
		frame.getContentPane().add(timeLabel);

		hourT = new JTextField();
		hourT.setBounds(117, 78, 31, 20);
		hourT.setText(hours+"");
		frame.getContentPane().add(hourT);

		minuteText = new JTextField();
		minuteText.setBounds(161, 78, 31, 20);
		minuteText.setText(minutes+"");
		frame.getContentPane().add(minuteText);

		final JButton cancelButton = new JButton();
		cancelButton.setFocusable(false);
		cancelButton.setIcon(ImageManager.getIcon(IssueSharedCoffee.class, "/icon/cancelsmall3.png"));
		cancelButton.setText("Cancel");
		cancelButton.setBounds(125, 185, 106, 26);
		cancelButton.addActionListener(this);
		frame.getContentPane().add(cancelButton);

		final JButton createButton = new JButton();
		createButton.setFocusable(false);
		createButton.setIcon(ImageManager.getIcon(IssueSharedCoffee.class, "/icon/icon_userssmall.png"));
		createButton.setText("Create");
		createButton.setBounds(237, 185, 106, 26);
		createButton.addActionListener(this);
		frame.getContentPane().add(createButton);

		final JLabel quorumLabel = new JLabel();
		quorumLabel.setFont(new Font("", Font.BOLD, 13));
		quorumLabel.setText("Quorum");
		quorumLabel.setBounds(56, 112, 55, 16);
		frame.getContentPane().add(quorumLabel);

		quorum = new JTextField();
		quorum.setBounds(117, 110, 87, 20);
		frame.getContentPane().add(quorum);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			frame.dispose();
		}
		else if(e.getActionCommand().equals("Create")) {
			
			int hourV=1;
			int minutesV=1;
			int quorumV =1;
			boolean go = true;
			
			try {
				hourV = Integer.parseInt(hourT.getText());
				minutesV = Integer.parseInt(minuteText.getText());
			}
			catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(frame,"Please insert valid time ","Error", 0);
				go = false;
			}	
			try {
				quorumV = Integer.parseInt(quorum.getText());	
			}
			catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(frame,"Please insert valid quorum ","Error", 0);
				go = false;
			}
			if (hourV<0 || hourV>23 || minutesV<0 || minutesV>59) {
				JOptionPane.showMessageDialog(frame,"Please insert a valid time","Error", 0);
				go = false;
			}
			if (quorumV<=1) {
				JOptionPane.showMessageDialog(frame,"Please insert valid quorum ","Error", 0);
				go = false;
			}
			if (go == true) {
				int today = new Date().getDate();
				int month = new Date().getMonth();
				int year = new Date().getYear();
				Date time = new Date(year,month,today,hourV,minutesV,0);
				System.out.println(time);
    			int res;
				synchronized(um) {
					res = this.um.issueSharedCall((Location)comboBox.getItemAt(comboBox.getSelectedIndex()), quorumV, time);
    			}
				if (res == 0) {
					JOptionPane.showMessageDialog(frame,"Shared Call Created","DC4C - Shared Call Created", 1);
					frame.dispose();		
				} else if (res==1) {
					JOptionPane.showMessageDialog(frame,"There was an error. Please try again","DC4C - Error", 0);	
				}
			}	
			
		}
	}

}
