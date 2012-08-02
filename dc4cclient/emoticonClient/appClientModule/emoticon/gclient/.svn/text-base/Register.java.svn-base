package emoticon.gclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.naming.Context;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.emoticon.controllers.UserManagerRemote;



public class Register implements ActionListener {

	private JTextField year;
	private JTextField month;
	private JTextField day;
	private JPasswordField pass;
	private JTextField username;
	private JTextField email;
	private JTextField surname;
	private JTextField name;
	private JFrame frame;
	private JButton registerButton;
	UserManagerRemote um;
	
	/**
	 * Create the application
	 */
	public Register(UserManagerRemote um) {
		this.um = um;
		createContents();
		frame.setVisible(true);
	}
	private class windowB extends WindowAdapter {
		
		public void windowClosing(WindowEvent arg0) {
			Main main = new Main(um);		
		}

	}

	/**
	 * Initialize the contents of the frame
	 */
	private void createContents() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.setTitle("DC4C - Register");
		frame.setBounds(100, 100, 452, 267);
		frame.addWindowListener(new windowB());

		final JLabel LName = new JLabel();
		LName.setFont(new Font("", Font.BOLD, 13));
		LName.setText("Name*");
		LName.setBounds(22, 66, 42, 16);
		frame.getContentPane().add(LName);

		final JLabel LSur = new JLabel();
		LSur.setFont(new Font("", Font.BOLD, 13));
		LSur.setText("Surname*");
		LSur.setBounds(5, 100, 66, 16);
		frame.getContentPane().add(LSur);

		final JLabel LEmail = new JLabel();
		LEmail.setFont(new Font("", Font.BOLD, 13));
		LEmail.setText("E-Mail");
		LEmail.setBounds(22, 134, 42, 16);
		frame.getContentPane().add(LEmail);

		final JLabel LBirth = new JLabel();
		LBirth.setFont(new Font("", Font.BOLD, 13));
		LBirth.setText("Birthday");
		LBirth.setBounds(244, 145, 66, 16);
		frame.getContentPane().add(LBirth);

		name = new JTextField();
		name.setBounds(70, 64, 108, 20);
		frame.getContentPane().add(name);

		surname = new JTextField();
		surname.setBounds(70, 97, 108, 20);
		frame.getContentPane().add(surname);

		email = new JTextField();
		email.setBounds(70, 132, 108, 20);
		frame.getContentPane().add(email);

		final JLabel LUser = new JLabel();
		LUser.setFont(new Font("", Font.BOLD, 13));
		LUser.setText("Username*");
		LUser.setBounds(241, 66, 69, 16);
		frame.getContentPane().add(LUser);

		final JLabel LPass = new JLabel();
		LPass.setFont(new Font("", Font.BOLD, 13));
		LPass.setText("Password*");
		LPass.setBounds(244, 99, 66, 16);
		frame.getContentPane().add(LPass);

		username = new JTextField();
		username.setBounds(316, 64, 108, 20);
		frame.getContentPane().add(username);

		final JLabel registerNewUserLabel = new JLabel();
		registerNewUserLabel.setForeground(new Color(0, 0, 255));
		registerNewUserLabel.setFont(new Font("", Font.BOLD, 18));
		registerNewUserLabel.setText("Register New User");
		registerNewUserLabel.setBounds(140, 10, 164, 29);
		frame.getContentPane().add(registerNewUserLabel);

		pass = new JPasswordField();
		pass.setBounds(316, 98, 109, 20);
		frame.getContentPane().add(pass);

		final JLabel dayLabel = new JLabel();
		dayLabel.setText("Day");
		dayLabel.setBounds(310, 121, 25, 16);
		frame.getContentPane().add(dayLabel);

		final JLabel monthLabel = new JLabel();
		monthLabel.setText("Month");
		monthLabel.setBounds(345, 121, 35, 16);
		frame.getContentPane().add(monthLabel);

		final JLabel yearLabel = new JLabel();
		yearLabel.setText("Year");
		yearLabel.setBounds(390, 121, 35, 16);
		frame.getContentPane().add(yearLabel);

		day = new JTextField();
		day.setText("22");
		day.setBounds(310, 143, 30, 20);
		frame.getContentPane().add(day);

		month = new JTextField();
		month.setText("05");
		month.setBounds(350, 143, 30, 20);
		frame.getContentPane().add(month);

		year = new JTextField();
		year.setText("1986");
		year.setBounds(390, 143, 34, 20);
		frame.getContentPane().add(year);

		final JLabel theFieldsLabel = new JLabel();
		theFieldsLabel.setText("(The * fields are mandatory)");
		theFieldsLabel.setBounds(10, 174, 157, 16);
		frame.getContentPane().add(theFieldsLabel);


		
		registerButton = new JButton();
		registerButton.setFocusable(false);
		registerButton.setIcon(ImageManager.getIcon(Register.class, "/icon/registersmall.png"));
		registerButton.setText("Register");
		registerButton.setBounds(310, 198, 114, 26);
		registerButton.addActionListener(this);
		frame.getContentPane().add(registerButton);
	}
	
	public static Context getInitialContext() throws javax.naming.NamingException {
		return new javax.naming.InitialContext();
	}

	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Register")) {
			// control the input fields
			try {
				int dayV = Integer.parseInt(day.getText());
				int monthV = Integer.parseInt(month.getText()) - 1;
				int yearV = Integer.parseInt(year.getText());	

				if (username.getText().equals("") || pass.getText().equals("") || surname.getText().equals("") || name.getText().equals("")){
					JOptionPane.showMessageDialog(frame,"Please fill mandatory fields","Error", 0);
				}
				else if (dayV<1 || dayV>31 || monthV<0 || monthV >12 || yearV<1900) {
					JOptionPane.showMessageDialog(frame,"Please insert a valid birth date","Error", 0);
				}
				else {
					Date birthdate = new Date(yearV-1900,monthV,dayV,0,0,0);
					int res = this.um.register(name.getText(), surname.getText(), username.getText(), pass.getText(),email.getText(), birthdate);
					if (res==0) {
						JOptionPane.showMessageDialog(frame,"User Created","DC4C - User Created", 1);
						frame.dispose();
						Main main = new Main(this.um);		
					}
					else if (res==1) {
						JOptionPane.showMessageDialog(frame,"Sorry but the username you provided is already used","Error", 0);
					}
				}
			}
			catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(frame,"Please insert a valid birth date","Error", 0);
			}			
		}
	}

}
