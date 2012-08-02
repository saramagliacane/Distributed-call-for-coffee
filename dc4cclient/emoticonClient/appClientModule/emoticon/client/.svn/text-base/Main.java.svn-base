package emoticon.client;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.emoticon.controllers.*;
import com.emoticon.entities.*;

import javax.naming.Context;
import javax.naming.NamingException;

public class Main {
	public static void main(String[] args) {
		
		try {
			Context jndiContext = getInitialContext();
			UserManagerRemote um = (UserManagerRemote)jndiContext.lookup("UserManagerBean/remote");
			UserManagerRemote um2 = (UserManagerRemote)jndiContext.lookup("UserManagerBean/remote");
			UserManagerRemote um3 = (UserManagerRemote)jndiContext.lookup("UserManagerBean/remote");
			
			
			LocationRemote lm = (LocationRemote) jndiContext.lookup("locationBean/remote");
			lm.putLocations();
			
			Calendar now = Calendar.getInstance();
			um.register("Sara", "Magliacane", "sara", "sara", "sara.magliacane@gmail.com", now.getTime());
			um.register("Francesco", "Pongetti", "pongiof", "pongiof", "test@gmail.com", now.getTime());
			um.register("test", "test", "test", "test", "", now.getTime());
			um.register("Prova", "Prova", "a", "a", "", now.getTime());
			um.register("Ago", "Ago", "ago", "ago", "", now.getTime());
			um.register("Rose", "Sun", "rose", "rose", "", new Date(1986,0,25,0,0,0));
			
			Calendar expire = Calendar.getInstance();
			expire.add(Calendar.SECOND, 1);
			um.login("sara", "sara");
			um.issueSharedCall(lm.getLocationById(1), 3, null);
			um.joinBirthdayCall(1);
			um.proposePresent("Marcello :p", 10, 1);
			
			um2.login("pongiof", "pongiof");
			um2.issueSharedCall(lm.getLocationById(1), 3, null);
			um2.joinBirthdayCall(1);
			um2.proposePresent("Roses", 20, 1);
			um2.votePresent(1);
						
			um3.login("test", "test");
			um3.issueSharedCall(lm.getLocationById(1), 3, null);
			//um3.leaveCoffeeCall(3);
							
			List<CoffeeCall> prova = um.listCoffeeCall();
			Iterator<CoffeeCall> it = prova.iterator();

			while (it.hasNext()){
				CoffeeCall c = (CoffeeCall)it.next();
				System.out.println("\nCoffeeCall number: "+ c.getID());
				Collection<User> sub = c.getSubscribers();
				if(sub== null) break;
				Iterator<User> i = sub.iterator();
				while (i.hasNext()) System.out.println(((User)i.next()).getUsername()+" ");
			}
			
				
			List prova_birth = um.listOpenBirthday();
			Iterator it2= prova_birth.iterator();

			while (it2.hasNext()){
				BirthdayCall c = (BirthdayCall)it2.next();
				System.out.println("\n BirthdayCall: "+ c.getID()+" user "+c.getUser().getUsername()+" date " +c.getUser().getBirthday());
				Collection<User> sub2 = c.getSubscribers();
				if(sub2== null) break;
				Iterator i2 = sub2.iterator();
				while (i2.hasNext()) System.out.println(((User)i2.next()).getUsername()+"");
				
				List<Present> presents = um.listPresent(c.getID());
				Iterator i3 = presents.iterator();
				while (i3.hasNext()) 
				{
					Present p = (Present)i3.next();
					System.out.println("present: "+ p.getDescription()+" ");
					Set<Vote>votes = p.getVotes();
					Iterator i4 = votes.iterator();
						while (i4.hasNext()){
							System.out.println(" voted by: "+((Vote)i4.next()).getUser().getUsername());
						}
				}
				
				Present p = (um.chosenPresent(c.getID()));
				if(p!= null) System.out.println("Chosen present: "+p.getDescription());
			}
			
			now.add(Calendar.SECOND,1);
			while(true){
				if (now.getTime().after(new Date())) continue;
				
				System.out.println("*** Print notifications for "+ ((User)um.getCurrentUser()).getUsername());
				
				List<Notification> not = um.checkEvents();
				Iterator<Notification> noti= not.iterator();
	
				while (noti.hasNext()){
					System.out.println("* " +now.getTime()+ ": "+ noti.next());
				}
				break;
			}
			
			
			while(true){
				if (now.getTime().after(new Date())) continue;
				
				System.out.println("*** Print notifications for "+ ((User)um2.getCurrentUser()).getUsername());
				
				List<Notification> not = um2.checkEvents();
				Iterator<Notification> noti= not.iterator();
	
				while (noti.hasNext()){
					System.out.println("* " +now.getTime()+ ": "+ noti.next());
				}
				break;
			}		
			
			while(true){
				if (now.getTime().after(new Date())) continue;
				
				System.out.println("*** Print notifications for "+ ((User)um3.getCurrentUser()).getUsername());
				
				List<Notification> not = um3.checkEvents();
				Iterator<Notification> noti= not.iterator();
	
				while (noti.hasNext()){
					System.out.println("* " +now.getTime()+ ": "+ noti.next());
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Context getInitialContext() throws javax.naming.NamingException {
		return new javax.naming.InitialContext();
	}

	public Main() {
		super();
	}

}