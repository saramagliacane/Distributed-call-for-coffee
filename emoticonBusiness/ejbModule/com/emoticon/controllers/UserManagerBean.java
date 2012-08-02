package com.emoticon.controllers;
import com.emoticon.entities.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This class implements the UserManager interface as a stateful session bean.
 * It is the only class the client has to see
 * 
 * @see UserManager
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 *
 */


public @Stateful(name="UserManagerBean") class UserManagerBean implements UserManagerRemote {

	@PersistenceContext(unitName="emoticon")
	private EntityManager manager;

	@EJB private NotificationManagerRemote nm;
	@EJB private CoffeeManagerRemote cm;
	@EJB private BirthdayManagerRemote bm;
	@EJB private PresentManagerRemote pm;
	
	private User currentUser;
	
	//the calls he subscribed automatically with each associated coffee call
	private HashMap <Integer,Set<Integer>> autoCoffee;
	//the calls he actually subscribed
	private Set<Integer> userjoined;
		
	public int register(String name, String surname, String user, String pass,
			String email, Date birthday) {
			
		try {
			Query q = manager.createQuery("FROM User u WHERE u.username=:us");
			q.setParameter("us", user);
			q.getSingleResult();
		}
		catch(NoResultException e) {
				User newUser = new User();
				newUser.setName(name);
				newUser.setSurname(surname);
				newUser.setUsername(user);
				newUser.setPassword(pass);
				newUser.setBirthday(birthday);
				newUser.setLocation(null);
				newUser.setStatus(0);
										
				manager.persist(newUser);
				manager.flush();
				nm.setBirthdayTimer(newUser);
				return 0;
		}
				System.out.println("*** Could not register, username already present.");
				return 1;
	}

	@Override
	public int login(String user, String pass) {
		
		User olduser = null;
		try {
			Query q = manager.createQuery("FROM User u WHERE u.username=:us");
			q.setParameter("us", user);
			olduser = (User)q.getSingleResult();
		}catch(NoResultException e) {
				System.out.println("*** No Such Username "+ user);
				return 1;
		}
		if(olduser == null) return 1;
	
		try{
		
		if((olduser.getPassword()).equals(pass)){
			System.out.println("*** Logged into the system: "+ olduser.getUsername());
			olduser.setStatus(1);
			olduser.setLocation(null);
			manager.merge(olduser);
			manager.flush();
			this.currentUser = olduser;
			this.autoCoffee =  new HashMap<Integer,Set<Integer>>();
			this.userjoined = new HashSet<Integer>();
					
			List<CoffeeCall> list = cm.listJoinedCoffee(currentUser.getId());
			//recreate the automatically joined calls
			Iterator<CoffeeCall> it = list.iterator();
			while (it.hasNext()){
				CoffeeCall c = it.next();
				//if it is closed continue
				if(c.getStatus()== 0) continue;
				this.userjoined.add(c.getID());
				this.autogenerateCoffee(c);
			}
			return 0;
			}
		else{
				System.out.println("*** Not logged in.");
				return 1;
		}
		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("*** No Such Username "+ user);
			return 1;
		}
		
	}

	@Override
	public int logout() {
		try{
			if(currentUser.getStatus()!= 0){
				currentUser.setStatus(0);
				currentUser.setLocation(null);
				manager.merge(currentUser);
				System.out.println("*** Logged out of the system: "+ currentUser.getUsername());
				
				//leave the automatically joined
				Iterator<Set<Integer>> i1 = this.autoCoffee.values().iterator();
				while (i1.hasNext()){
					
					Set<Integer> list= i1.next();
					Iterator<Integer> i2 = list.iterator();
					
					while (i2.hasNext()){
						int c2 = i2.next();
						cm.leaveCall(c2, currentUser.getId());
					}
				}
				
				//save the user joined calls, maybe they were in the just left
				Iterator<Integer> save = this.userjoined.iterator();
				while(save.hasNext()){
					cm.joinCall(save.next(), currentUser.getId());
				}
				
				this.autoCoffee = null;
				this.userjoined = null;
				return 0;
			}
			else {
				System.out.println("*** Could not log out of the system: "+ currentUser.getUsername());
				return 1;
			}
		}catch(Exception e){
			System.out.println("*** Could not log out of the system: "+ (currentUser==null?null:currentUser.getUsername()));
			return 1;
		}			
	}

	// sets the status of the user to working, drinking coffee or busy
	// and manages the frozen calls
	@Override
	public void setStatus(int status) {
		if((currentUser.getStatus()!=0)&&(status == 1||status == 2)){
			
			if (currentUser.getStatus()== status) return;
			
			// the user is logged in the system
			// the user comes back to work
			if (status == 1) { 
				currentUser.setStatus(status); 
				currentUser.setLocation(null);
				
				//retrieve the frozen calls
				Set<Integer> list = this.userjoined;
				Iterator<Integer> frozC = list.iterator();
				while (frozC.hasNext()){
					int id = frozC.next();
					CoffeeCall c = manager.find(CoffeeCall.class, id);
					System.out.println("*** CoffeeCall de-frozen "+ id);
					if (c.getStatus()== 1) this.joinCoffeeCall(c.getID());										
				}
				manager.merge(currentUser);
				manager.flush();
											
			}
			// if the user is busy we froze his calls
			else{
				currentUser.setStatus(status);
				currentUser.setLocation(null);
				
				List<CoffeeCall> jC = this.listJoinedCoffee();
				Iterator<CoffeeCall> joinedC = jC.iterator();
				
				//the UM leaveCoffeeCall erases the userjoined
				//but we have to use it because it deletes also the
				//autogenerated calls
				Set<Integer> save = new HashSet<Integer>();
				Iterator<Integer> savei = this.userjoined.iterator();
				while(savei.hasNext()) save.add(savei.next());
				
				
				while (joinedC.hasNext()){
					CoffeeCall c = (CoffeeCall) joinedC.next();
					this.leaveCoffeeCall(c.getID());
				}
				
				Iterator<Integer> restorei = save.iterator();
				while(restorei.hasNext())
					this.userjoined.add(restorei.next());
				
				
				manager.merge(currentUser);
				manager.flush();				
			}
			
			System.out.println("*** Status changed for user: "+ currentUser.getUsername()+" to status "+currentUser.getStatus());
			
		}//if valid integer
		
	}

	@Override
	public List<CoffeeCall> listCoffeeCall() {
		if(currentUser.getStatus()!= 0){
			return cm.listCoffeeCall();
		}
		else return new LinkedList<CoffeeCall>();
	}
	
	// except autoCoffee all of them
	@Override
	public List<CoffeeCall> listJoinedCoffee() {
		if(currentUser.getStatus()!= 0){
			
			//the list of user joined calls
			List<CoffeeCall> temp = cm.listJoinedCoffee(currentUser.getId());
			List<CoffeeCall> temp2 = new LinkedList<CoffeeCall>();
			Iterator<CoffeeCall> i = temp.iterator();
			while(i.hasNext()){
				CoffeeCall t = i.next();
				if (this.userjoined.contains(t.getID())) temp2.add(t);
			}
				
			return temp2;
		}
		else return new LinkedList<CoffeeCall>();
	}

	// join automatically all the other shared calls
	@Override
	public int issueSharedCall(Location location, int quorum,
			Date expirationTime) {
		
		System.out.println (">>> ISSUED SHARED CALL user " + currentUser.getUsername());
		if(currentUser.getStatus()== 1){
			
			CoffeeCall c = cm.issueCall(location, null, quorum, expirationTime, 1, currentUser.getId());
			if (c==null) return 1;
			System.out.println("### Call added to userjoined " + c.getID());
			if (!this.userjoined.contains(c.getID()))this.userjoined.add(c.getID());
						
			//join also the other compatible calls
			this.autogenerateCoffee(c);
			
			return 0;
		}
		else return 1;
	}
	
	@Override
	public int issueTimedCall(Location location, Date breakTime){
		
		System.out.println (">>> ISSUED TIMED CALL user " + currentUser.getUsername());
		
		if(currentUser.getStatus()== 1){
			
			CoffeeCall c = cm.issueCall(location, breakTime, 0, new Date(), 0, currentUser.getId());
			if (c==null) return 1;
			if (!this.userjoined.contains(c.getID())) this.userjoined.add(c.getID());
			System.out.println("### Call added to userjoined " + c.getID());
			
			return 0;
			
		}
		else return 1;
	}

	// join automatically all the other shared calls
	@Override
	public int joinCoffeeCall(int coffeeID) {
		
		System.out.println (">>> USER WANTED TO JOIN CALL user " + currentUser.getUsername());
			
		if(currentUser.getStatus()== 1){
			
			CoffeeCall c = cm.joinCall(coffeeID, currentUser.getId());
			if (c==null) return 1;
			if (!this.userjoined.contains(coffeeID))this.userjoined.add(coffeeID);
			
			//if it is timed we do not add compatible calls
			if (c.getType()== 0) return 0;
				
			//join automatically the other calls
			autogenerateCoffee(c);
			
			return 0;
		}
		else return 1;

	}
	
	//leave automatically all related calls
	@Override
	public int leaveCoffeeCall(int coffeeID) {
		
		System.out.println (">>> USER LEFT CALL user " + currentUser.getUsername());
		
		
		if(currentUser.getStatus()!= 0){
			
			CoffeeCall c = cm.leaveCall(coffeeID, currentUser.getId());
			if (c==null) return 1;
			
			if (this.userjoined.contains(coffeeID)) this.userjoined.remove(coffeeID);
			//if it is timed we do not have compatible calls
			if (c.getType()== 0)return 0;
			
			System.out.println (">>> Coffee call deleted from userjoined "+coffeeID);
						
			//when leave also the automatically joined calls
			//associated with this call
			
			if (this.autoCoffee.isEmpty()) return 0; 
			if(!this.autoCoffee.containsKey(coffeeID)) return 0;
			
			Set<Integer> a = this.autoCoffee.get((Integer)coffeeID);
			Iterator<Integer> autoc = a.iterator();
			while(autoc.hasNext()){
				int auto = autoc.next();
				System.out.println("!!! autoCoffee deleted " + auto);
				cm.leaveCall(auto, currentUser.getId());
			}	
			
			if(this.autoCoffee.containsKey(coffeeID))			
				this.autoCoffee.remove(coffeeID);				
			
			
			//but restore the joined calls that were maybe removed
			List<CoffeeCall> list = this.listJoinedCoffee();
			Iterator<CoffeeCall> listit = list.iterator();
			while(listit.hasNext()){
				CoffeeCall f = listit.next();
				if(f.getStatus()== 1)
						cm.joinCall(f.getID(), currentUser.getId());
			}
			
			return 0;	
		}
		else return 1;
	}

	@Override
	public List<BirthdayCall> listOpenBirthday() {
		if(currentUser.getStatus()!= 0){
			return bm.listOpenBirthday();
		}
		else return new LinkedList<BirthdayCall>();
	}

	@Override
	public List<BirthdayCall> listJoinedBirthday(){
		if(currentUser.getStatus()!= 0){
			return bm.listJoinedBirthday(currentUser.getId());
		}
		else return new LinkedList<BirthdayCall>();
	}

	@Override
	public int joinBirthdayCall(int birthdayID) {
		if(currentUser.getStatus()!= 0){
			return bm.joinBirthdayCall(birthdayID, currentUser.getId());
		}
		else return 1;
	}

	@Override
	public int proposePresent(String description, float price, int birthdayID) {
		if(currentUser.getStatus()!= 0){
			return pm.proposePresent(description, price, birthdayID, currentUser.getId());
		}
		else return 1;
	}

	@Override
	public int votePresent(int presentID) {
		if(currentUser.getStatus()!= 0){
			return pm.votePresent(presentID, currentUser.getId());
		}
		else return 1;
	}
	
	@Override
	public List<Present> listPresent(int birthdayID){
		if(currentUser.getStatus()!= 0){
			return pm.listPresent(birthdayID);
		}
		else return new LinkedList<Present>();
	}
	
	@Override
	public Present chosenPresent(int birthdayID){
		if(currentUser.getStatus()!= 0){
			return bm.chosenPresent(birthdayID);
		}
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> whoIsOnline(){
		if(currentUser.getStatus()!= 0){
			
			try{
				Query q = manager.createQuery("SELECT u FROM User u WHERE u.status!=0");
				return q.getResultList();
			}catch (NoResultException e){
				return new LinkedList<User>();
			}
		}
		else return new LinkedList<User>();
	}
	
	@Override
	public User getCurrentUser(){
		if(currentUser.getStatus()!= 0){
			return currentUser;
		}
		else return null;
	}

	// manages the most important functions of the system
	// the notifications of the chosen presents for the closed birthday calls
	// and the notifications for the closed coffee calls
	
	@Override
	public List<Notification> checkEvents() {
		if(currentUser.getStatus()!= 0){
		
			List<Notification> not = new LinkedList<Notification>();
			not.clear();
			Notification n;
			Calendar now = Calendar.getInstance();		
			
			//Birthday Notifications
			try {
			
			List<BirthdayCall> jB = this.listJoinedBirthday();
			Iterator<BirthdayCall> joinedB = jB.iterator();
			while (joinedB.hasNext()){
				BirthdayCall b = (BirthdayCall) joinedB.next();
				if(b.getStatus()==1) continue;
				
				//closed joined calls
				//recent?????
				n = new Notification(0, this.chosenPresent(b.getID()),b,null);
				not.add(n);
			}
			} catch (Exception e) {
				System.out.println("+++ BirthdayCall not notified ");
			}
			
			//CoffeeCalls Notifications
			try {
			
			if(currentUser.getStatus()==2) return not;
			
			Set<Integer> jC = this.userjoined;
			Iterator<Integer> joinedC = jC.iterator();
			
				
			while (joinedC.hasNext()){
				CoffeeCall c = null;
				try{
					c = manager.find(CoffeeCall.class, joinedC.next());
				}catch(NoResultException e){continue;}
				if(c==null) continue;
				autogenerateCoffee(c);
				
				System.out.println(">>> checkEvents: User "+ currentUser.getUsername() + " joined call " + c.getID());
				
				// if the user is already away we froze all his calls until he sets his status back to working
				if((currentUser.getStatus()==3)||(currentUser.getStatus()==2)){
					System.out.println(">>> User "+ currentUser.getUsername() + " has frozen call " + c.getID()+" because he's busy.");
					this.leaveCoffeeCall(c.getID());
					continue;
				}	
				// recent enough? started no more than 5 seconds ago
				// must leave some shared not started yet
				now.add(Calendar.MILLISECOND, -5500);
				if(c.getStarted()!= null)
					if(c.getStarted().before(now.getTime())) {
						this.leaveCoffeeCall(c.getID());
						System.out.println(">>> User "+ currentUser.getUsername() + " left call " + c.getID()+" because it's too old.");
						continue;
					}
						 	
				//timed calls
				if(c.getType()==0) {
					System.out.println(">>> Timed call " + c.getID());
					// if it is not closed yet, continue
					if(c.getStatus()==1) {
						System.out.println(">>> Timed call not closed yet " + c.getID());
						continue;
					}
				}
				
				//shared calls
				else {
					System.out.println(">>> Shared call " + c.getID());
					
					// if it is not closed yet, maybe we should close it
					if(c.getStatus()==1){						
						
						int closed = this.closeSharedCalls(c);
						//if not closed yet
						if(closed == 0) continue;
					}						
				}
				
				n = new Notification(1, null,null,c);
				not.add(n);
				currentUser.setLocation(c.getLocation());
				currentUser.setStatus(3);
				manager.merge(currentUser);
				manager.flush();				
				System.out.print("<<< Gone to take coffee "+currentUser.getUsername()+" @ "+currentUser.getLocation().getName());
				
			}
		} catch (Exception e) {
			System.out.println("+++ CoffeeCall not notified ");
		}
		
		return not;
		
	}//user not logged in
	else return new LinkedList<Notification>();
		
}
	
	private void autogenerateCoffee(CoffeeCall c){
		
		try{
			if(c==null)return;
			if(c.getType()==0) return;
		
			//join compatible calls at the same location
			List<CoffeeCall> list = cm.coffeeCallsAtSameLocation(c, this.currentUser);
			Iterator<CoffeeCall> listit = list.iterator();
			Set<Integer> templ = new HashSet<Integer>();
			
			while(listit.hasNext()){
				
				CoffeeCall temp = (CoffeeCall) listit.next();
				if (temp.getQuorum()>= c.getQuorum()){
					templ.add(temp.getID());
					cm.joinCall(temp.getID(), currentUser.getId());
					System.out.println(">>> Autogenerated call " + temp.getID());
					
				}
				
			}
			this.autoCoffee.put((Integer)c.getID(), templ);
			
			//debug
			Set<Integer> a = this.autoCoffee.get((Integer)c.getID());
			Iterator<Integer> autoc = a.iterator();
			while(autoc.hasNext()){
				int auto = autoc.next();
				System.out.println("!!! autoCoffee " + auto);
			}
			//
		
		}catch (Exception e){
			e.printStackTrace();
			System.out.println(">>> Could not autogenerate calls");
		}
		
		//*/
	}
	
	// procedure to close shared calls, called from check();
	// 1 closes the call, 0 still open
	private int closeSharedCalls(CoffeeCall c){
		
		if(c==null) return 0;
		// in case the call is deleted while we are processing this
		int coffeeID = c.getID();
		manager.refresh(c);
		manager.flush();
		
		try{			
			System.out.println(">>> Shared call not closed yet " + c.getID());
			int drinkingUsers = cm.numberOfUsersAtSameLocation(c.getLocation().getId());
			System.out.println("### Number of drinking users "+ drinkingUsers);
			
			int quorum = c.getQuorum();
			System.out.println("### Quorum "+ quorum);
			
			Set<User> sub = (Set<User>) c.getSubscribers();
			Iterator<User> subit = sub.iterator();
			int subno = sub.size();
			System.out.println("### Number of subscribers "+ subno);
			
			// some of them might be off-line
			while (subit.hasNext()){
				User u  = subit.next();
				if (u.getStatus()!=1) 
					if(u.getLocation().getId()!=c.getLocation().getId())
						subno--;
			}		
			
			System.out.println("### Number of active subscribers "+ subno);		
			
			// close if the call has reached the quorum
			if(subno+drinkingUsers>= quorum){
				c.setStatus(0);
				c.setStarted(new Date());
				manager.merge(c);
				manager.flush();
				System.out.println(">>> Shared call closed now " + coffeeID);
				return 1;
			}
			return 0;
		}catch(Exception e){
			System.out.println(">>> Could not close call " + coffeeID);
			return 0;
		}
	}

	// Return a list of location where to take coffee
	@SuppressWarnings("unchecked")
	public List<Location> listLocation() {
		if(currentUser.getStatus()!= 0){
			try{
				Query q = manager.createQuery("SELECT l FROM Location l");
				return q.getResultList();
			}catch (NoResultException e){
				return new LinkedList<Location>();
			}
		}
		else return new LinkedList<Location>();
	}	
	
	
}
