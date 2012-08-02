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


public class IssueTimedCoffee implements ActionListener {

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
	public IssueTimedCoffee(UserManagerRemote um) {
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
		frame.setTitle("DC4C - New Timed Coffee Call");
		frame.setResizable(false);
		frame.setBounds(100, 100, 361, 199);
		frame.addWindowListener(new windowB());

		final JLabel locationLabel = new JLabel();
		locationLabel.setFont(new Font("", Font.BOLD, 13));
		locationLabel.setText("Location");
		locationLabel.setBounds(4, 104, 55, 16);
		frame.getContentPane().add(locationLabel);

		final JLabel hourLabel = new JLabel();
		hourLabel.setText("Hour");
		hourLabel.setBounds(65, 50, 31, 16);
		frame.getContentPane().add(hourLabel);

		final JLabel newTimedCoffeeLabel = new JLabel();
		newTimedCoffeeLabel.setForeground(new Color(0, 0, 255));
		newTimedCoffeeLabel.setFont(new Font("", Font.BOLD, 16));
		newTimedCoffeeLabel.setText("New Timed Coffee Call");
		newTimedCoffeeLabel.setBounds(95, 10, 178, 31);
		frame.getContentPane().add(newTimedCoffeeLabel);

		comboBox = new JComboBox();
		
		comboBox.setBounds(65, 100, 166, 25);
		
		Iterator t = this.locations.iterator();
		while(t.hasNext()){
			Location l = (Location)t.next();
			comboBox.addItem(l);
		}
		
		frame.getContentPane().add(comboBox);

		final JLabel minutesLabel = new JLabel();
		minutesLabel.setText("Minutes");
		minutesLabel.setBounds(105, 50, 46, 16);
		frame.getContentPane().add(minutesLabel);

		final JLabel timeLabel = new JLabel();
		timeLabel.setFont(new Font("", Font.BOLD, 13));
		timeLabel.setText("Time");
		timeLabel.setBounds(26, 72, 33, 16);
		frame.getContentPane().add(timeLabel);

		hourT = new JTextField();
		hourT.setBounds(65, 70, 31, 20);
		hourT.setText(hours+"");
		frame.getContentPane().add(hourT);

		minuteText = new JTextField();
		minuteText.setBounds(110, 70, 31, 20);
		minuteText.setText(minutes+"");
		frame.getContentPane().add(minuteText);

		final JButton cancelButton = new JButton();
		cancelButton.setFocusable(false);
		cancelButton.setIcon(ImageManager.getIcon(IssueTimedCoffee.class, "/icon/cancelsmall3.png"));
		cancelButton.setText("Cancel");
		cancelButton.setBounds(125, 135, 106, 26);
		cancelButton.addActionListener(this);
		frame.getContentPane().add(cancelButton);

		final JButton createButton = new JButton();
		createButton.setFocusable(false);
		createButton.setIcon(ImageManager.getIcon(IssueTimedCoffee.class, "/icon/timer_iconsmall.gif"));
		createButton.setText("Create");
		createButton.setBounds(237, 135, 106, 26);
		createButton.addActionListener(this);
		frame.getContentPane().add(createButton);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			frame.dispose();
		}
		else if(e.getActionCommand().equals("Create")) {
			try {
				int hourV = Integer.parseInt(hourT.getText());
				int minutesV = Integer.parseInt(minuteText.getText());

				if (hourV<0 || hourV>23 || minutesV<0 || minutesV>59) {
					JOptionPane.showMessageDialog(frame,"Please insert a valid time","Error", 0);
				}
				else {
					int today = new Date().getDate();
					int month = new Date().getMonth();
					int year = new Date().getYear();
					Date time = new Date(year,month,today,hourV,minutesV,0);
					System.out.println(time);
        			int res;
					synchronized(um) {
        				res = this.um.issueTimedCall((Location)comboBox.getItemAt(comboBox.getSelectedIndex()), time);
        			}
					if (res == 0) {
						JOptionPane.showMessageDialog(frame,"Timed Call Created","DC4C - Timed Call Created", 1);
						frame.dispose();		
					} else if (res==1) {
						JOptionPane.showMessageDialog(frame,"There was an error. Please try again","DC4C - Error", 0);	
					}
				}
			}
			catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(frame,"Please insert a valid time","Error", 0);
			}			
		}
	}

}
