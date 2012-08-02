package emoticon.gclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.emoticon.controllers.UserManagerRemote;
import com.emoticon.entities.BirthdayCall;
import com.emoticon.entities.Present;


public class PresentList implements ActionListener {

	private JList presentList;
	private JFrame frame;
	private BirthdayCall call;
	private UserManagerRemote um;
	private JLabel by; 
	private JLabel price; 
	private JTextArea description; 
	private JLabel votes; 
	
	private class windowB extends WindowAdapter {
		public void windowClosing(WindowEvent arg0) {
		}

	}
	
	/**
	 * Start the frame and set it visible
	 */
	public PresentList(BirthdayCall b, UserManagerRemote um) {
		this.call = b;
		this.um = um;
		createContents();
		frame.setVisible(true);
	}

	
	// For managing list rendering
	private class PresentEntry {
		  public String title;
		  public ImageIcon image;
		  public Present p;

		  public PresentEntry(String title, ImageIcon image, Present p) {
		    this.title = title;
		    this.image = image;
		    this.p = p;
		  }
	}
	private class PresentCellRenderer extends DefaultListCellRenderer {
		
		  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			PresentEntry entry = (PresentEntry) value;
		    JLabel label = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			label.setText(entry.title);
			label.setIcon(entry.image);
			label.setToolTipText(entry.title);
		    return label;
		  }
	}	
	
	private void updateDes(int pos)
	{
		PresentEntry pe = (PresentEntry)presentList.getModel().getElementAt(presentList.getSelectedIndex());
		Present p = pe.p;
		price.setText(p.getPrice()+" €");
		by.setText(p.getProposedBy().getName()+ " " + p.getProposedBy().getSurname());
		description.setText(p.getDescription());
		votes.setText(p.getVotes().size()+"");
	}
	
	private void updateList() {
		// load the presents in the list
		ImageIcon[] presentImages = {new ImageIcon(),ImageManager.getIcon(ApplClient.class, "/icon/star-256x256.png")};
		List<Present> List;
		synchronized(um) {
			List = um.listPresent(this.call.getID());
		}
		DefaultListModel listModel = new DefaultListModel(); 
		Iterator it = List.iterator();
		int i = 1;
		int icon = 0;
		Present chosen;
		synchronized(um) {
			chosen = um.chosenPresent(this.call.getID());
		}
		int chosenid = (chosen!=null)? chosen.getID():-1;
		while (it.hasNext()){
			Present p = (Present)it.next();
			icon = chosenid == p.getID() ? 1:0;
			listModel.addElement(new PresentEntry(i+". From "+p.getProposedBy().getName()+", votes: "+p.getVotes().size(),presentImages[icon],p));
			i++;
		}
		presentList.setModel(listModel);
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
		frame.setTitle("DC4C - Present List");
		frame.setResizable(false);
		frame.setBounds(100, 100, 501, 328);
		frame.addWindowListener(new windowB());

		final JLabel proposeBirthL = new JLabel();
		proposeBirthL.setForeground(new Color(0, 0, 255));
		proposeBirthL.setFont(new Font("", Font.BOLD, 16));
		proposeBirthL.setText("Birthday Present List");
		proposeBirthL.setBounds(160, 10, 166, 31);
		frame.getContentPane().add(proposeBirthL);

		final JButton cancelButton = new JButton();
		cancelButton.setFocusable(false);
		cancelButton.setIcon(ImageManager.getIcon(PresentList.class, "/icon/cancelsmall3.png"));
		cancelButton.setText("Cancel");
		cancelButton.setBounds(385, 261, 106, 26);
		cancelButton.addActionListener(this);
		frame.getContentPane().add(cancelButton);


		presentList = new JList();
		presentList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (presentList.getSelectedIndex()>-1) {
					updateDes(presentList.getSelectedIndex());
				}
			}
			
		});
		updateList();
		presentList.setFocusable(false);
		presentList.setCellRenderer(new PresentCellRenderer());
		JScrollPane scroll = new JScrollPane(presentList);
		scroll.setBorder(new LineBorder(Color.black, 1, false));
		scroll.setBounds(10, 93, 250, 194);

		frame.getContentPane().add(scroll);

		final JLabel proposedByLabel = new JLabel();
		proposedByLabel.setFont(new Font("", Font.BOLD, 13));
		proposedByLabel.setText("Proposed by");
		proposedByLabel.setBounds(271, 93, 84, 16);
		frame.getContentPane().add(proposedByLabel);

		final JLabel priceLabel = new JLabel();
		priceLabel.setFont(new Font("", Font.BOLD, 13));
		priceLabel.setText("Price");
		priceLabel.setBounds(271, 115, 38, 16);
		frame.getContentPane().add(priceLabel);

		final JLabel votesLabel = new JLabel();
		votesLabel.setFont(new Font("", Font.BOLD, 13));
		votesLabel.setText("Votes");
		votesLabel.setBounds(271, 137, 71, 16);
		frame.getContentPane().add(votesLabel);

		final JLabel presentForLabel = new JLabel();
		presentForLabel.setFont(new Font("", Font.BOLD, 13));
		presentForLabel.setText("Present for");
		presentForLabel.setBounds(10, 47, 69, 16);
		frame.getContentPane().add(presentForLabel);

		final JLabel statusLabel = new JLabel();
		statusLabel.setFont(new Font("", Font.BOLD, 13));
		statusLabel.setText("Status");
		statusLabel.setBounds(10, 70, 39, 16);
		frame.getContentPane().add(statusLabel);

		final JSeparator separator = new JSeparator();
		separator.setBounds(255, 242, 240, 9);
		frame.getContentPane().add(separator);

		final JButton voteButton = new JButton();
		if (this.call.getStatus()==0){
			voteButton.setEnabled(false);
		}
		voteButton.addActionListener(this);
		voteButton.setFocusable(false);
		voteButton.setIcon(ImageManager.getIcon(PresentList.class, "/icon/ip_icon_02_Oksmall.png"));
		voteButton.setText("Vote");
		voteButton.setBounds(266, 261, 106, 26);
		frame.getContentPane().add(voteButton);

		final JLabel forLabel = new JLabel();
		forLabel.setText(this.call.getUser().getName()+" "+this.call.getUser().getSurname());
		forLabel.setFont(new Font("", Font.BOLD, 13));
		forLabel.setBounds(94, 47, 261, 16);
		frame.getContentPane().add(forLabel);

		final JLabel descriptionLabel_1 = new JLabel();
		descriptionLabel_1.setFont(new Font("", Font.BOLD, 13));
		descriptionLabel_1.setText("Description");
		descriptionLabel_1.setBounds(271, 158, 84, 16);
		frame.getContentPane().add(descriptionLabel_1);

		by = new JLabel();
		by.setText("");
		by.setBounds(361, 93, 124, 16);
		frame.getContentPane().add(by);

		price = new JLabel();
		price.setText("");
		price.setBounds(316, 115, 98, 16);
		frame.getContentPane().add(price);

		votes = new JLabel();
		votes.setText("");
		votes.setBounds(316, 137, 98, 16);
		frame.getContentPane().add(votes);

		description = new JTextArea();
		JScrollPane descriptionT = new JScrollPane(description);
		description.setEditable(false);
		description.setText("");
		descriptionT.setBounds(289, 180, 186, 60);
		frame.getContentPane().add(descriptionT);

		final JLabel stat = new JLabel();
		stat.setFont(new Font("", Font.BOLD, 13));
		if (this.call.getStatus()==0) {
			stat.setText("The birthday call is closed");
		} else {
			stat.setText("The birthday call is open. Closing date: "+(this.call.getUser().getBirthday().getDate()-2)+"/"+this.call.getUser().getBirthday().getMonth()+1);
		}
		stat.setBounds(55, 70, 377, 16);
		frame.getContentPane().add(stat);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			frame.dispose();
		}
		if (e.getActionCommand().equals("Vote")) {
			if (presentList.getSelectedIndex()>-1) {
				int tmp = presentList.getSelectedIndex();
				PresentEntry pe = (PresentEntry)presentList.getModel().getElementAt(presentList.getSelectedIndex());
				int res;
				synchronized(um) {
					res = this.um.votePresent(pe.p.getID());
				}
				if (res==1) {
					JOptionPane.showMessageDialog(frame,"There was an error on voting. Please try again","Error", 0);
				} else {
					frame.dispose();
				}
			}
		}
	}

}
