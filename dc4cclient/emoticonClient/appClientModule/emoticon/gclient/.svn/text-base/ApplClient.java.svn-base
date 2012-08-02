package emoticon.gclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.emoticon.controllers.Notification;
import com.emoticon.controllers.UserManagerRemote;
import com.emoticon.entities.BirthdayCall;
import com.emoticon.entities.CoffeeCall;
import com.emoticon.entities.User;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!------------------!!!!!!!!!!!!!!!!
// every access to this.um has to be surrounded with synchronized(um){...}
// this because concurrent threads cannot access to the same EJB! 
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!-------------------!!!!!!!!!!!!!!!!
public class ApplClient implements ActionListener {

	private JList activeCoffeeCall;
	private JList ListJoinedBirth;
	private JList ListActiveBirth;
	private JList ListNotification;
	private JList ListJoinedCoffee;
	private JComboBox comboBox;
	private JList ListWhosOnline;
	private JFrame frame;
	int activeSelected;
	int joinedSelected;
	int bactiveSelected;
	int bjoinedSelected;
	boolean goN = true;
	boolean goE = true;
	JButton bTimed;
	JButton BShared;
	JButton BJoinCoffee;
	JButton BLeaveCoffee;


	// UserManagerBean link
	UserManagerRemote um;
	
	// Events Thread
	eventsThread wst;
	
	// Notification Thread
	notificationThread nt;
	
	// For managing user list rendering
	private class UserEntry {
		  public String title;
		  public ImageIcon image;

		  public UserEntry(String title, ImageIcon image) {
		    this.title = title;
		    this.image = image;
		  }
	}
	private class UserCellRenderer extends JLabel implements ListCellRenderer {
		
			public UserCellRenderer() {
				setIconTextGap(12);
		  }

		  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			UserEntry entry = (UserEntry) value;
		    setText(entry.title);
		    setIcon(entry.image);
		    setToolTipText(entry.title);
		    return this;
		  }
	}	
	
	
	// For managing presents list rendering
	private class PresentEntry {
		  public String inf;
		  public BirthdayCall birthday;
		  public ImageIcon image;
		  
		  public PresentEntry(String inf, BirthdayCall b, ImageIcon image) {
		    this.inf = inf;
		    this.birthday = b;
		    this.image = image;
		  }
	}	
	
	private class PresentCellRenderer extends DefaultListCellRenderer {


	  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel label =  (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
	
		PresentEntry entry = (PresentEntry) value;
	    label.setText(entry.inf);
	    label.setIcon(entry.image);
	    label.setToolTipText(entry.inf);
	    return label;
	  }
}
	
	// For managing coffee list rendering
	private class CoffeeEntry {
		  public String inf;
		  public ImageIcon image;
		  public CoffeeCall coffee;
		  
		  public CoffeeEntry(String inf, ImageIcon image, CoffeeCall c) {
		    this.inf = inf;
		    this.image = image;
		    this.coffee = c;
		  }
	}	
	
	private class CoffeeCellRenderer extends DefaultListCellRenderer {

	  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel label =  (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
	
		CoffeeEntry entry = (CoffeeEntry) value;
	    label.setText(entry.inf);
	    label.setIcon(entry.image);
	    label.setToolTipText(entry.inf);
	    return label;
	  }
}
	
	// For managing status box
	ImageIcon[] statusImages = {ImageManager.getIcon(ApplClient.class, "/icon/usrgreensmall.png"),ImageManager.getIcon(ApplClient.class, "/icon/userredsmall.png"),ImageManager.getIcon(ApplClient.class, "/icon/useryellowsmall.png")};
	String[] statusString = {"Working","Busy"};

    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
    	
		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);
		}
		
		public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {

			int selectedIndex = ((Integer)value).intValue();
			
			//Set the icon and text.  If icon was null, say so.
			ImageIcon icon = statusImages[selectedIndex];
			String status = statusString[selectedIndex];
			
			setIcon(icon);
			setText(status);
			setFont(list.getFont());	
			return this;
		}
}

    // for managing the change status
    private class changeStatus implements ActionListener  {

		public void actionPerformed(ActionEvent arg0) {
			try {
				synchronized(um) {	
					int nuovo = comboBox.getSelectedIndex()+1;
					if (nuovo == 1) {
						bTimed.setEnabled(true);
						BShared.setEnabled(true);
						BJoinCoffee.setEnabled(true);
						BLeaveCoffee.setEnabled(true);
					}
					else if (nuovo==2) {
						bTimed.setEnabled(false);
						BShared.setEnabled(false);
						BJoinCoffee.setEnabled(false);
						BLeaveCoffee.setEnabled(false);
					}
					um.setStatus(comboBox.getSelectedIndex()+1);	
				}
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(frame,"There was an error. Please try again","Error", 0);
			}
		}
    	 	
    }
        
    // for managing the operations when an user exit
	private class windowB extends WindowAdapter  {		
		public void windowClosing(WindowEvent arg0) {
			logout();	
		}

	}
	
	//synchronized(...){...} mutual exclusion code!
	// check for new notifications
	private int check() {
			int wait = 6000;
			List<Notification> notificationList;
			synchronized(um) {
				notificationList = um.checkEvents();
			}
			DefaultListModel notificationModel = new DefaultListModel(); 
			Iterator it = notificationList.iterator();
			int i = 1;
			ListNotification.setModel(notificationModel);
			while (it.hasNext()){
				Notification n = (Notification)it.next();
				if (n.birthdaycall != null) {
					if (n.present!= null) {
						notificationModel.addElement(i+". Birthday Call for "+n.birthdaycall.getUser().getName()+" " +n.birthdaycall.getUser().getSurname()+" closed. Present chosen: "+n.present.getDescription());
						ListNotification.setModel(notificationModel);
					}
					if (n.present == null) {
						// regalo nn scelto, cioè nuova chiamata
					}
				}
				if (n.coffeecall!=null) {
					String[] s = {"Back to work"};
					String[] type = {"Timed","Shared"};
					
					// add one after one element 
					notificationModel.addElement(i+". Coffee Call started. From: "+n.coffeecall.getIssuer().getName()+" "+ n.coffeecall.getIssuer().getSurname()+", Location: "+n.coffeecall.getLocation().getName()+", Type: "+type[n.coffeecall.getType()]);
					ListNotification.setModel(notificationModel);
	
					
					frame.setEnabled(false);
					JOptionPane.showOptionDialog(frame, "Coffee Call Started! Coffee Call from: "+n.coffeecall.getIssuer().getName()+" "+ n.coffeecall.getIssuer().getSurname()+", Location: "+n.coffeecall.getLocation().getName()+", Type: "+type[n.coffeecall.getType()],"Coffee Call Started",1,1,null,s,null);
					
					// when click on
					synchronized(um) {
						this.um.setStatus(1); // return from work
					}
					frame.setEnabled(true);
					// pause for more time. In this way we don't have two dialog consequetly
				}
				i++;
			}
			return wait;
	}

	
	//-----------------------THREAD--------------------
	//Control the notifications in a separate thread every 5 secs or more
	class notificationThread extends Thread {
		public void run() {
			try {
				while(goN) {
						Thread.sleep(check());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	//-----------------------THREAD--------------------
	// The icon for the shared and timed coffee call
	ImageIcon[] coffeeImages = {ImageManager.getIcon(ApplClient.class, "/icon/timer_iconsmall.gif"),ImageManager.getIcon(ApplClient.class, "/icon/icon_userssmall.png")};
	// The icon for joined closed and active birthday call
	ImageIcon[] birthdayImages = {ImageManager.getIcon(ApplClient.class, "/icon/presenticonsmllBlack.png"),ImageManager.getIcon(ApplClient.class, "/icon/presenticonsmll.png")};
	// control the events in a separate thread. Very Efficient
    class eventsThread extends Thread {
        public void run() {
	        	try {
	        		while(goE) {
	        			// First fills the who's online list
	        			List<User> userList;
	        			synchronized(um) {
	        				userList = um.whoIsOnline();
	        			}
	        			DefaultListModel listModel = new DefaultListModel(); 
	        			Iterator it = userList.iterator();
	        			while (it.hasNext()){
	        				User u = (User)it.next();
	        				synchronized(um) {
		        				if (!u.getUsername().equals(um.getCurrentUser().getUsername()))
		        					listModel.addElement(new UserEntry(u.getName()+" "+u.getSurname(),statusImages[u.getStatus()-1]));
		        			}
	        			}
	        			ListWhosOnline.setModel(listModel);
	        			
	        			// Second fills the active coffee calls
	        			List<CoffeeCall> coffeeList;
	        			List<CoffeeCall> coffeListJoined;
	        			synchronized(um) {
	        				coffeeList = um.listCoffeeCall();
	        				coffeListJoined = um.listJoinedCoffee();
	        			}
	        			DefaultListModel listModelCoffee = new DefaultListModel(); 
	        			it = coffeeList.iterator();
	        			while (it.hasNext()){
	        				boolean insert = true;
	        				CoffeeCall c = (CoffeeCall)it.next();
	        				Iterator it2 = coffeListJoined.iterator();
	        				while (it2.hasNext()) {
	        					CoffeeCall j = (CoffeeCall)it2.next();
		        				if (j.getID() == c.getID()) insert = false;
	    	        		}
	        				if (c!=null && c.getType()==0 && insert) { // timed coffee
	        					listModelCoffee.addElement(new CoffeeEntry(c.getIssuer().getSurname()+", "+c.getLocation().getName()+", \n Time: "+c.getBreakTime().getHours()+":"+c.getBreakTime().getMinutes(),coffeeImages[c.getType()],c));
	        				}
	        				if (c!=null && c.getType()==1 && insert) { // shared coffee
	        					listModelCoffee.addElement(new CoffeeEntry(c.getIssuer().getSurname()+", "+c.getLocation().getName()+", \n Quorum: "+c.getQuorum(),coffeeImages[c.getType()],c));
	        				}
	        				
	        			}
	        			// tmp store the selection index in the list of active coffee call
	        			int tmp = activeSelected;
	        			activeCoffeeCall.setModel(listModelCoffee);
	        			activeSelected = tmp;
	        			activeCoffeeCall.setSelectedIndex(activeSelected);
	        			
	        			// fills the joined coffee call
	        			synchronized(um) {
	        				coffeeList = um.listJoinedCoffee();
	        			}
	        			DefaultListModel listModelCoffeeJ = new DefaultListModel(); 
	        			it = coffeeList.iterator();
	        			while (it.hasNext()){
	        				CoffeeCall c = (CoffeeCall)it.next();
	        				if (c!=null && c.getType()==0 && c.getStatus()==1) { // timed coffee
	        					listModelCoffeeJ.addElement(new CoffeeEntry(c.getIssuer().getSurname()+", "+c.getLocation().getName()+", \n Time: "+c.getBreakTime().getHours()+":"+c.getBreakTime().getMinutes(),coffeeImages[c.getType()],c));
	        				}
	        				if (c!=null && c.getType()==1 && c.getStatus()==1) { // shared coffee
	        					listModelCoffeeJ.addElement(new CoffeeEntry(c.getIssuer().getSurname()+", "+c.getLocation().getName()+", \n Quorum: "+c.getQuorum(),coffeeImages[c.getType()],c));
	        				}
	        			}
	        			tmp = joinedSelected;
	        			ListJoinedCoffee.setModel(listModelCoffeeJ);
	        			joinedSelected = tmp;
	        			ListJoinedCoffee.setSelectedIndex(joinedSelected);
	        			
	        			// fills the active birthday call
	        			List<BirthdayCall> birthdayList;
	        			synchronized(um) {
	        				birthdayList= um.listOpenBirthday();
	        			}
	        			DefaultListModel listModelBirthday = new DefaultListModel(); 
	        			it = birthdayList.iterator();
	        			while (it.hasNext()){
	        				try {
			    				BirthdayCall b = (BirthdayCall)it.next();
			          			boolean insert = true;
			         			Collection<User> sub = b.getSubscribers();
			        			Iterator it2 = sub.iterator();
			        			while (it2.hasNext()) {
			        				User next = (User)it2.next();
				        			synchronized(um) {
				        				if (next.getUsername().equals(um.getCurrentUser().getUsername())) {
				        					insert = false;
				        				}      
				        			}
			        			}
			        			synchronized(um) {
				    				if (!b.getUser().getUsername().equals(um.getCurrentUser().getUsername())&& insert)
				    					listModelBirthday.addElement(new PresentEntry("Birthday of: "+b.getUser().getSurname()+", on: "+b.getUser().getBirthday().getDate()+"/"+b.getUser().getBirthday().getMonth()+1,b,birthdayImages[b.getStatus()]));
				        		}
			        		}
	        				catch (NullPointerException e) {
	        					e.printStackTrace();
	        				}
	        			}
	        			tmp = bactiveSelected;
	        			ListActiveBirth.setModel(listModelBirthday);
	        			bactiveSelected = tmp;
	        			ListActiveBirth.setSelectedIndex(bactiveSelected);
	        			
	        			// fills the joined birthday call
	        			String[] statusB = {"Closed","Open"};
	        			List<BirthdayCall> birthdayListJ;
						synchronized(um) {		
							birthdayListJ= um.listJoinedBirthday();
						}
	        			DefaultListModel listModelBirthdayJ = new DefaultListModel(); 
	        			it = birthdayListJ.iterator();
	        			while (it.hasNext()){
	        				BirthdayCall b = (BirthdayCall)it.next();
	        					listModelBirthdayJ.addElement(new PresentEntry("Birthday of: "+b.getUser().getSurname()+", on: "+b.getUser().getBirthday().getDate()+"/"+b.getUser().getBirthday().getMonth()+1+", status: "+statusB[b.getStatus()],b,birthdayImages[b.getStatus()]));
	        			}
	        			tmp = bjoinedSelected;
	        			ListJoinedBirth.setModel(listModelBirthdayJ);
	        			bjoinedSelected = tmp;
	        			ListJoinedBirth.setSelectedIndex(bjoinedSelected);
	
	        			// Sleep for some time
						Thread.sleep(1500);
					} 
	        	}
	        	catch (Exception e) {
						e.printStackTrace();
				}
	        }
    }
	/**
	 * Start and set the frame visible
	 */
	public ApplClient(UserManagerRemote um) {
		this.um = um;
		createContents();
		frame.setVisible(true);
		wst = new eventsThread();
		wst.start();
		nt = new notificationThread();
		nt.start();
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void createContents() {
			frame = new JFrame();
			frame.setTitle("DC4C - Emoticon");
			frame.setResizable(false);
			frame.getContentPane().setLayout(null);
			frame.setBounds(100, 100, 804, 470);
			frame.addWindowListener(new windowB());
	
			
			ListWhosOnline = new JList();
			ListWhosOnline.setCellRenderer(new UserCellRenderer());
			JScrollPane ListWhosOnlineS = new JScrollPane(ListWhosOnline);
			ListWhosOnlineS.setBorder(new LineBorder(Color.black, 1, false));
			ListWhosOnlineS.setBounds(625, 105, 166, 273);
			frame.getContentPane().add(ListWhosOnlineS);
	
		       
			Integer[] intArray = new Integer[statusString.length];
			for (int i = 0; i < statusString.length; i++) {
	            intArray[i] = new Integer(i);
			}
			
			comboBox = new JComboBox(intArray);
		    ComboBoxRenderer renderer= new ComboBoxRenderer();
		    comboBox.setRenderer(renderer);
		    comboBox.setMaximumRowCount(3);
		    comboBox.addActionListener(new changeStatus());
	
			comboBox.setBounds(625, 42, 132, 25);
			frame.getContentPane().add(comboBox);
	
			final JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.setFocusable(false);
			tabbedPane.setBounds(0, 0, 613, 378);
			frame.getContentPane().add(tabbedPane);
	
			final JPanel panel = new JPanel();
			panel.setLayout(null);
			tabbedPane.addTab("Coffee Call Manager", ImageManager.getIcon(ApplClient.class, "/icon/coffeeiconsmalpng.png"), panel, null);
	
			ListJoinedCoffee = new JList();
			ListJoinedCoffee.setFocusable(false);
			ListJoinedCoffee.addListSelectionListener(new ListSelectionListener (){
				public void valueChanged(ListSelectionEvent arg0) {
					joinedSelected = ListJoinedCoffee.getSelectedIndex();
				}
			});
			ListJoinedCoffee.setCellRenderer(new CoffeeCellRenderer());
			JScrollPane ListJoinedCoffeeS = new JScrollPane(ListJoinedCoffee);
			ListJoinedCoffeeS.setBorder(new LineBorder(Color.black, 1, false));
			ListJoinedCoffeeS.setBounds(374, 34, 225, 249);
			panel.add(ListJoinedCoffeeS);
	
			final JSeparator separator = new JSeparator();
			separator.setBounds(0, 289, 608, 2);
			panel.add(separator);
	
			bTimed = new JButton();
			bTimed.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/timer_iconsmall.gif"));
			bTimed.setFocusable(false);
			bTimed.setText("Issue Timed Call");
			bTimed.setBounds(265, 305, 164, 26);
			bTimed.addActionListener(this);
			panel.add(bTimed);
	
			BJoinCoffee = new JButton();
			BJoinCoffee.addActionListener(this);
			BJoinCoffee.setFont(new Font("", Font.BOLD, 10));
			BJoinCoffee.setHorizontalTextPosition(SwingConstants.LEADING);
			BJoinCoffee.setIconTextGap(1);
			BJoinCoffee.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/1206569942771180767pitr_green_double_arrows_set_1.svg.thumbsmall.png"));
			BJoinCoffee.setFocusable(false);
			BJoinCoffee.setText("Join");
			BJoinCoffee.setBounds(252, 80, 106, 26);
			panel.add(BJoinCoffee);
	
			BLeaveCoffee = new JButton();
			BLeaveCoffee.addActionListener(this);
			BLeaveCoffee.setFont(new Font("", Font.BOLD, 10));
			BLeaveCoffee.setBorderPainted(true);
			BLeaveCoffee.setHorizontalAlignment(SwingConstants.CENTER);
			BLeaveCoffee.setIconTextGap(1);
			BLeaveCoffee.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/1206569967984704715pitr_green_double_arrows_set_4.svg.thumbsmall.png"));
			BLeaveCoffee.setFocusable(false);
			BLeaveCoffee.setText("Leave");
			BLeaveCoffee.setBounds(252, 124, 106, 26);
			panel.add(BLeaveCoffee);
	
			BShared = new JButton();
			BShared.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/icon_userssmall.png"));
			BShared.setFocusable(false);
			BShared.setBounds(435, 305, 164, 26);
			BShared.addActionListener(this);
			panel.add(BShared);
			BShared.setText("Issue Shared Call");
	
			final JLabel LJoinedCoffee = new JLabel();
			LJoinedCoffee.setFont(new Font("", Font.BOLD, 13));
			LJoinedCoffee.setText("Joined Coffee Calls");
			LJoinedCoffee.setBounds(425, 10, 133, 16);
			panel.add(LJoinedCoffee);
	
			final JLabel LActiveCoffee = new JLabel();
			LActiveCoffee.setFont(new Font("", Font.BOLD, 13));
			LActiveCoffee.setBounds(55, 10, 124, 16);
			panel.add(LActiveCoffee);
			LActiveCoffee.setText("Active Coffee Calls");
	
			final JLabel LImageCoffee = new JLabel();
			LImageCoffee.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/120px-Applications-ristretto.svg.png"));
			LImageCoffee.setText("");
			LImageCoffee.setBounds(245, 165, 124, 114);
			panel.add(LImageCoffee);
	
			activeCoffeeCall = new JList();
			activeCoffeeCall.setFocusable(false);
			activeCoffeeCall.addListSelectionListener(new ListSelectionListener (){
				public void valueChanged(ListSelectionEvent arg0) {
					activeSelected = activeCoffeeCall.getSelectedIndex();
				}
			});
			activeCoffeeCall.setCellRenderer(new CoffeeCellRenderer());
			JScrollPane activeCoffeeCallS = new JScrollPane(activeCoffeeCall);
			activeCoffeeCallS.setBorder(new LineBorder(Color.black, 1, false));
			activeCoffeeCallS.setBounds(10, 34, 225, 249);
			panel.add(activeCoffeeCallS);
				
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("");
	
			final JPanel panel_1 = new JPanel();
			panel_1.setLayout(null);
			tabbedPane.addTab("Birthday Manager", ImageManager.getIcon(ApplClient.class, "/icon/presenticonsmll.png"), panel_1, null);
	
			ListActiveBirth = new JList();
			ListActiveBirth.setFocusable(false);
			ListActiveBirth.addListSelectionListener(new ListSelectionListener (){
				public void valueChanged(ListSelectionEvent arg0) {
					bactiveSelected = ListActiveBirth.getSelectedIndex();
				}
			});
			ListActiveBirth.setCellRenderer(new PresentCellRenderer());
			JScrollPane ListActiveBirthS  = new JScrollPane(ListActiveBirth);
			ListActiveBirthS.setBorder(new LineBorder(Color.black, 1, false));
			ListActiveBirthS.setBounds(10, 34, 225, 249);
			panel_1.add(ListActiveBirthS);
	
			ListJoinedBirth = new JList();
			ListJoinedBirth.setFocusable(false);
			ListJoinedBirth.setCellRenderer(new PresentCellRenderer());
			ListJoinedBirth.addListSelectionListener(new ListSelectionListener (){
				public void valueChanged(ListSelectionEvent arg0) {
					bjoinedSelected = ListJoinedBirth.getSelectedIndex();
				}
			});
			JScrollPane ListJoinedBirthS  = new JScrollPane(ListJoinedBirth);
			ListJoinedBirthS.setBorder(new LineBorder(Color.black, 1, false));
			ListJoinedBirthS.setBounds(374, 34,225, 249);
			panel_1.add(ListJoinedBirthS);
	
			final JLabel LActiveBirth = new JLabel();
			LActiveBirth.setFont(new Font("", Font.BOLD, 13));
			LActiveBirth.setText("Active Calls for Present");
			LActiveBirth.setBounds(50, 10, 150, 16);
			panel_1.add(LActiveBirth);
	
			final JButton BJoinBirth = new JButton();
			BJoinBirth.addActionListener(this);
			BJoinBirth.setFont(new Font("", Font.BOLD, 10));
			BJoinBirth.setHorizontalTextPosition(SwingConstants.LEADING);
			BJoinBirth.setFocusable(false);
			BJoinBirth.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/1206569942771180767pitr_green_double_arrows_set_1.svg.thumbsmall.png"));
			BJoinBirth.setText("Join ");
			BJoinBirth.setBounds(252, 80, 106, 26);
			panel_1.add(BJoinBirth);
	
			final JLabel LJoinedBirth = new JLabel();
			LJoinedBirth.setFont(new Font("", Font.BOLD, 13));
			LJoinedBirth.setText("Joined Calls For Present");
			LJoinedBirth.setBounds(410, 10, 165, 16);
			panel_1.add(LJoinedBirth);
	
			final JSeparator separator_1 = new JSeparator();
			separator_1.setBounds(0, 289, 608, 2);
			panel_1.add(separator_1);
	
			final JButton BPropose = new JButton();
			BPropose.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/presentproposesmall.png"));
			BPropose.setFocusable(false);
			BPropose.setText("Propose Present");
			BPropose.setBounds(265, 305, 164, 26);
			BPropose.addActionListener(this);
			panel_1.add(BPropose);
	
			final JButton BPresentList = new JButton();
			BPresentList.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/Regali-di-Natale.gif"));
			BPresentList.setFocusable(false);
			BPresentList.setText("Presents List");
			BPresentList.setBounds(435, 305, 164, 26);
			BPresentList.addActionListener(this);
			panel_1.add(BPresentList);
	
			final JLabel LBirthImage = new JLabel();
			LBirthImage.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/Regali-di-Natalebig.gif"));
			LBirthImage.setText("");
			LBirthImage.setBounds(245, 165, 119, 107);
			panel_1.add(LBirthImage);
	
			final JLabel LSetYourStatus = new JLabel();
			LSetYourStatus.setFont(new Font("", Font.BOLD, 14));
			LSetYourStatus.setText("Set Your Status:");
			LSetYourStatus.setBounds(625, 20, 121, 16);
			frame.getContentPane().add(LSetYourStatus);
	
			final JLabel LWhosonline = new JLabel();
			LWhosonline.setFont(new Font("", Font.BOLD, 14));
			LWhosonline.setText("Who's Online:");
			LWhosonline.setBounds(625, 85, 96, 16);
			frame.getContentPane().add(LWhosonline);
	
			ListNotification = new JList();
			JScrollPane ListNotificationS = new JScrollPane(ListNotification);
			ListNotificationS.setBorder(new LineBorder(Color.black, 1, false));
			ListNotificationS.setBounds(10, 384, 482, 45);
			frame.getContentPane().add(ListNotificationS);
	
			final JButton BLogout = new JButton();
			BLogout.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/120px-Vista-logout.png"));
			BLogout.setHorizontalAlignment(SwingConstants.CENTER);
			BLogout.setIconTextGap(4);
			BLogout.setHorizontalTextPosition(SwingConstants.RIGHT);
			BLogout.setFocusable(false);
			BLogout.setFont(new Font("", Font.BOLD, 14));
			BLogout.addActionListener(this);
			BLogout.setText("Logout");
			BLogout.setBounds(670, 395, 121, 35);
			frame.getContentPane().add(BLogout);
	
			final JButton refreshButton = new JButton();
			refreshButton.addActionListener(this);
			refreshButton.setFocusable(false);
			refreshButton.setIcon(ImageManager.getIcon(ApplClient.class, "/icon/PNG-Symbol-Refresh.png-256x256.png"));
			refreshButton.setText("Refresh");
			refreshButton.setBounds(500, 395, 106, 26);
			frame.getContentPane().add(refreshButton);

	}
	
	private void logout() {
			int res;
			synchronized(um) {		
				res = um.logout();
			}
			if (res == 1) {
				JOptionPane.showMessageDialog(frame,"There was an error on logout. Please try again","Error", 0);
			}
			else if (res==0) {
				frame.dispose();
				synchronized(um) {		
					Main main = new Main(this.um);	
				}
				this.goE = false;
				this.goN = false;
			}
	}

	public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Issue Timed Call")) {
				synchronized(um) {		
					IssueTimedCoffee itc = new IssueTimedCoffee(this.um);	
				}
			}
			else if (e.getActionCommand().equals("Issue Shared Call")) {
				synchronized(um) {		
					IssueSharedCoffee isc = new IssueSharedCoffee(this.um);	
				}
			}
			else if (e.getActionCommand().equals("Propose Present")) {
				if (ListJoinedBirth.getSelectedIndex()>-1) {
					PresentEntry b = (PresentEntry)ListJoinedBirth.getModel().getElementAt(ListJoinedBirth.getSelectedIndex());
					BirthdayCall bc = b.birthday;
					if (bc.getStatus()!=0){
						synchronized(um) {		
							ProposePresent pp = new ProposePresent(bc,this.um);
						}
					}
					else {
						JOptionPane.showMessageDialog(frame,"The birthday call is closed. You can't propose presents anymore","Information", 1);
					}
				}
			}
			else if (e.getActionCommand().equals("Presents List")) {
				if (ListJoinedBirth.getSelectedIndex()>-1) {
					PresentEntry b = (PresentEntry)ListJoinedBirth.getModel().getElementAt(ListJoinedBirth.getSelectedIndex());
					BirthdayCall bc = b.birthday;
					synchronized(um) {		
						PresentList pl = new PresentList(bc,this.um);
					}
				}
			}
			else if (e.getActionCommand().equals("Logout")) {
				logout();
			}
			else if (e.getActionCommand().equals("Join")) {
				if (activeCoffeeCall.getSelectedIndex()>-1) { 
					CoffeeEntry c = (CoffeeEntry)activeCoffeeCall.getModel().getElementAt(activeCoffeeCall.getSelectedIndex());
					int res;
					synchronized(um) {		
						res = this.um.joinCoffeeCall(c.coffee.getID());
					}
					if (res == 1) {
						JOptionPane.showMessageDialog(frame,"There was an error on join. Please try again","Error", 0);
					}
				}
			}
			else if (e.getActionCommand().equals("Leave")) {
				if (ListJoinedCoffee.getSelectedIndex()>-1) { 
					CoffeeEntry c = (CoffeeEntry)ListJoinedCoffee.getModel().getElementAt(ListJoinedCoffee.getSelectedIndex());
					System.out.println(c.coffee.getID());
					int res;
					synchronized(um) {			
						res = this.um.leaveCoffeeCall(c.coffee.getID());
					}
					if (res == 1) {
						JOptionPane.showMessageDialog(frame,"There was an error on leave. Please try again","Error", 0);
					}
				}	
			}
			// birthday join
			else if (e.getActionCommand().equals("Join ")) {
				if (ListActiveBirth.getSelectedIndex()>-1) { 
					PresentEntry b = (PresentEntry)ListActiveBirth.getModel().getElementAt(ListActiveBirth.getSelectedIndex());
					int res;
					synchronized(um) {
						res = this.um.joinBirthdayCall(b.birthday.getID());
					}
					if (res == 1) {
						JOptionPane.showMessageDialog(frame,"There was an error on join. Please try again","Error", 0);
					}
				}
			}
			// refresh notification
			else if (e.getActionCommand().equals("Refresh")) {
				check();
			}
	}
	
	public static Context getInitialContext() throws javax.naming.NamingException {
		return new javax.naming.InitialContext();
	}


}
