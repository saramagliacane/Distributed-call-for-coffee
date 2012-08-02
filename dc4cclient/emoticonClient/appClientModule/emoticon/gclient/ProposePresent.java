package emoticon.gclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.emoticon.controllers.UserManagerRemote;
import com.emoticon.entities.BirthdayCall;


public class ProposePresent implements ActionListener {

	private JTextArea descriptionT;
	private JTextField price;
	private JFrame frame;
	private BirthdayCall call;
	private UserManagerRemote um;

	private class windowB extends WindowAdapter {
		public void windowClosing(WindowEvent arg0) {
		}

	}
	
	/**
	 * Start the frame and set it visible
	 */
	public ProposePresent(BirthdayCall b, UserManagerRemote um) {
		this.um = um;
		this.call = b;
		System.out.println(b.getUser().getUsername());
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
		frame.setTitle("DC4C - Propose Present");
		frame.setResizable(false);
		frame.setBounds(100, 100, 361, 251);
		frame.addWindowListener(new windowB());

		final JLabel proposeBirthL = new JLabel();
		proposeBirthL.setForeground(new Color(0, 0, 255));
		proposeBirthL.setFont(new Font("", Font.BOLD, 16));
		proposeBirthL.setText("Propose Birthday Present");
		proposeBirthL.setBounds(76, 10, 197, 31);
		frame.getContentPane().add(proposeBirthL);

		final JLabel timeLabel = new JLabel();
		timeLabel.setFont(new Font("", Font.BOLD, 13));
		timeLabel.setText("Proposed For");
		timeLabel.setBounds(27, 58, 87, 16);
		frame.getContentPane().add(timeLabel);

		final JButton cancelButton = new JButton();
		cancelButton.setFocusable(false);
		cancelButton.setIcon(ImageManager.getIcon(ProposePresent.class, "/icon/cancelsmall3.png"));
		cancelButton.setText("Cancel");
		cancelButton.setBounds(117, 185, 106, 26);
		cancelButton.addActionListener(this);
		frame.getContentPane().add(cancelButton);

		final JButton createButton = new JButton();
		createButton.setFocusable(false);
		createButton.setIcon(ImageManager.getIcon(ProposePresent.class, "/icon/presenticonsmll.png"));
		createButton.setText("Propose");
		createButton.setBounds(229, 185, 114, 26);
		createButton.addActionListener(this);
		frame.getContentPane().add(createButton);

		final JLabel priceLabel = new JLabel();
		priceLabel.setFont(new Font("", Font.BOLD, 13));
		priceLabel.setText("Price");
		priceLabel.setBounds(80, 82, 33, 16);
		frame.getContentPane().add(priceLabel);

		price = new JTextField();
		price.setBounds(117, 80, 43, 20);
		frame.getContentPane().add(price);

		final JLabel EuroLabel = new JLabel();
		EuroLabel.setText("€");
		EuroLabel.setBounds(165, 80, 7, 16);
		frame.getContentPane().add(EuroLabel);

		final JLabel descriptionLabel = new JLabel();
		descriptionLabel.setFont(new Font("", Font.BOLD, 13));
		descriptionLabel.setText("Description");
		descriptionLabel.setBounds(43, 111, 71, 16);
		frame.getContentPane().add(descriptionLabel);

		descriptionT = new JTextArea();
		JScrollPane description = new JScrollPane(descriptionT);
		description.setBounds(117, 110, 228, 57);
		description.setBorder(new LineBorder(Color.black, 1, false));
		frame.getContentPane().add(description);

		final JLabel forLabel = new JLabel();
		forLabel.setText(this.call.getUser().getName()+ " " + this.call.getUser().getSurname());
		forLabel.setFont(new Font("", Font.BOLD, 13));
		forLabel.setBounds(120, 58, 177, 16);
		frame.getContentPane().add(forLabel);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			frame.dispose();
		}
		else if(e.getActionCommand().equals("Propose")) {
			
			float priceV = 0;
			try {
				priceV = Float.parseFloat(price.getText());
				System.out.println(priceV);
			}
			catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(frame,"Please insert valid price","Error", 0);
			}	
			if (priceV < 0) {
				JOptionPane.showMessageDialog(frame,"Please insert a valid price","Error", 0);
			}
			else if (descriptionT.getText().equals("")) {
				JOptionPane.showMessageDialog(frame,"Please insert valid description","Error", 0);
			}
			else {
				synchronized(um) {
					this.um.proposePresent(descriptionT.getText(), priceV, this.call.getID());
				}
				frame.dispose();
			}		
		}
	}

}
