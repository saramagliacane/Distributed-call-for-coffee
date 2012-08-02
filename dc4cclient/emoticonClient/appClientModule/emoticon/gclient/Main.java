package emoticon.gclient;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.naming.Context;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.emoticon.controllers.LocationRemote;
import com.emoticon.controllers.UserManagerRemote;

public class Main implements ActionListener{

	private JPasswordField Tpassword;
	private JTextField TUsername;
	private JFrame frame;
	private JButton loginButton;
	private JLabel Luser;
	private JLabel passwordL;
	private JButton LRegister;
	private JLabel title1L;
	UserManagerRemote um;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Context jndiContext = getInitialContext();
					Main window = new Main((UserManagerRemote)jndiContext.lookup("UserManagerBean/remote"));
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Start
	 */
	public Main(UserManagerRemote um) {
		this.um = um;
		
/*	Adds the default locations. To be started only one time
 * 
 *
*/  	try {
			Context jndiContext = getInitialContext();
			LocationRemote lr = (LocationRemote)jndiContext.lookup("locationBean/remote");
			lr.putLocations();
	
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
		
			}
			 catch (Exception e) {
				 e.printStackTrace();
			 }
			createContents();
			frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void createContents() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setTitle("DC4C - Login");
		frame.setResizable(false);
		frame.setBounds(100, 100, 234, 327);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loginButton = new JButton();
		loginButton.addActionListener(this);
		loginButton.setVerticalTextPosition(SwingConstants.CENTER);
		loginButton.setVerticalAlignment(SwingConstants.CENTER);
		loginButton.setHorizontalTextPosition(SwingConstants.LEADING);
		loginButton.setIcon(ImageManager.getIcon(Main.class, "/icon/120px-Vista-forwardsmall2.png"));
		loginButton.setFocusable(false);
		loginButton.setText("Login");
		loginButton.setBounds(70, 185, 95, 26);
		frame.getContentPane().add(loginButton);

		TUsername = new JTextField();
		TUsername.setBounds(55, 105, 121, 20);
		frame.getContentPane().add(TUsername);

		Luser = new JLabel();
		Luser.setFont(new Font("", Font.BOLD, 14));
		Luser.setText("username");
		Luser.setBounds(75, 80, 78, 16);
		frame.getContentPane().add(Luser);

		passwordL = new JLabel();
		passwordL.setFont(new Font("", Font.BOLD, 14));
		passwordL.setText("password");
		passwordL.setBounds(75, 130, 78, 16);
		frame.getContentPane().add(passwordL);

		LRegister = new JButton();
		LRegister.setIcon(ImageManager.getIcon(Main.class, "/icon/registersmall.png"));
		LRegister.setFocusable(false);
		LRegister.setText("Register");
		LRegister.setBounds(110, 265, 111, 26);
		LRegister.addActionListener(this);
		frame.getContentPane().add(LRegister);

		title1L = new JLabel();
		title1L.setForeground(Color.RED);
		title1L.setFont(new Font("Arial", Font.BOLD, 48));
		title1L.setText("DC4C");
		title1L.setBounds(46, 10, 138, 56);
		frame.getContentPane().add(title1L);

		Tpassword = new JPasswordField();
		Tpassword.setBounds(55, 152, 121, 20);
		frame.getContentPane().add(Tpassword);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Login")) {
			if (TUsername.getText().equals("") || Tpassword.getText().equals("")){
				JOptionPane.showMessageDialog(loginButton,"Please fill username and password fields","Error", 0);
			} else {
				int res = this.um.login(TUsername.getText(), Tpassword.getText());
				if (res==0) {
					frame.dispose();
					ApplClient app = new ApplClient(this.um);
				}
				else if (res==1) {
					JOptionPane.showMessageDialog(frame,"Username and password not valid","Error", 0);
				}
			}
		}
		else if (e.getActionCommand().equals("Register")) {
			frame.dispose();
			Register register = new Register(this.um);
		}
	}
	
	public static Context getInitialContext() throws javax.naming.NamingException {
		return new javax.naming.InitialContext();
	}
}
