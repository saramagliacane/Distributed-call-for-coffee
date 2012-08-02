package com.emoticon.controllers;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.emoticon.entities.BirthdayCall;
import com.emoticon.entities.CoffeeCall;
import com.emoticon.entities.User;

/**
 * Session Bean implementation class NotificationManager
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 */
@Stateless
public class NotificationManagerBean implements NotificationManagerRemote {

	@PersistenceContext(unitName="emoticon")
	private EntityManager manager;
	
	@Resource TimerService timerService;
	
	public void setBirthdayTimer(User u){
		
		if(u.getBirthday() == null) return;
		
		Calendar cal = Calendar.getInstance();
		Calendar rightNow = Calendar.getInstance();
		cal.setTime(u.getBirthday());
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.add(Calendar.DAY_OF_YEAR, -2);
		
		// if an useful date (two days before) has not yet passed this year
		///* commented to make the timers start as soon as they are created
		
		if (cal.get(Calendar.DAY_OF_YEAR)> rightNow.get(Calendar.DAY_OF_YEAR)){
				cal.set(Calendar.YEAR, rightNow.get(Calendar.YEAR)); 
		}
		// if the date already passed, set it for next year
		else cal.set(Calendar.YEAR, rightNow.get(Calendar.YEAR)+1); 
		//*/
		
		//open the call 5+2 days before the birthday
		cal.add(Calendar.DAY_OF_YEAR, -5);
		
		Date date = cal.getTime();		
		TimerInfo n = new TimerInfo(u.getId(), "openBirthday");
		
		System.out.println("§§§ NM : openBirthday Timer created for " + u.getUsername()+" date: "+ date);
		timerService.createTimer(date, n);
				
		cal.add(Calendar.DAY_OF_YEAR, +5);
		//cal.add(Calendar.DAY_OF_YEAR, +8); to let them be open for the debug
		date = cal.getTime();
		TimerInfo c = new TimerInfo(u.getId(), "closeBirthday");
		System.out.println("§§§ NM : closeBirthday Timer created for " + u.getUsername()+" date: "+ date);
		timerService.createTimer(date, c);				
}

	@Timeout
	public void Timeout(Timer timer) {
		
		System.out.println("*** In Timeout "+ timer.toString());
		
		TimerInfo n  = (TimerInfo) timer.getInfo();
		User u;
		
		// close Timed Calls
		if (n.info.equals("closeTimed")){
			try{
				Query q = manager.createQuery("FROM CoffeeCall c WHERE c.ID=:id and c.status=1");
				q.setParameter("id", n.userid);
				CoffeeCall c = (CoffeeCall) q.getSingleResult();
				c.setStatus(0);
				// Started now
				c.setStarted(new Date()); 
				manager.merge(c);
				manager.flush();
				System.out.println("TTT NM : Timed Call closed "+n.userid);
				
			} catch (NoResultException e){
				System.out.println("§§§ NM : No Timed Call to be closed found.");
			}	
		}
		// close Shared Calls
		else if (n.info.equals("closeShared")){
			try{
				Query q = manager.createQuery("FROM CoffeeCall c WHERE c.ID=:id and c.status=1");
				q.setParameter("id", n.userid);
				CoffeeCall c = (CoffeeCall) q.getSingleResult();
				manager.remove(c);
				manager.flush();
				System.out.println("$$$ NM : Shared Call deleted "+n.userid);
				
			} catch (NoResultException e){
				System.out.println("$$$ NM : No Shared Call to be deleted found.");
			}	
		}
		// manage Birthday Calls
		else{
			try{
				u = manager.find(User.class, n.userid);
			} catch (EntityNotFoundException e) {
				return;
			}
			System.out.println("*** NMINFO: "+ n.info + " for user "+ u.getUsername());
		
			// open Birthday Calls
			if(n.info.equals("openBirthday")){
				BirthdayCall b = new BirthdayCall();
				b.setUser(u);
				b.setStatus(1);
				manager.persist(b);	
				manager.flush();
				System.out.println("£££ NM : New Birthday call added "+n.userid);
			}
			// close Birthday Calls
			else if (n.info.equals("closeBirthday")){
			  
				try{
					Query q = manager.createQuery("FROM BirthdayCall c WHERE c.user=:user and c.status=1");
					q.setParameter("user", u);
					BirthdayCall c = (BirthdayCall) q.getSingleResult();
					c.setStatus(0);
					manager.merge(c);
					manager.flush();
					System.out.println("&&& NM : BirthdayCall closed for "+n.userid);
					
					// set timer for next year Birthday call
					setBirthdayTimer(u);
	
				} catch (NoResultException e){
					System.out.println("&&& NM : No BirthdayCall to be closed found.");
				}	
			}// else if						
			}//else
		
	}//Timeout
		
	public void setTimedCallTimer(CoffeeCall c){
		
		if(c.getBreakTime() == null) return;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(c.getBreakTime());
		Date date = cal.getTime();		
		TimerInfo n = new TimerInfo(c.getID(), "closeTimed");
		System.out.println("§§§ NM : closeTimedCall Timer created for " + c.getID()+" date: "+ date);
		timerService.createTimer(date, n);						
		
	}
	
	public void setSharedCallTimer(CoffeeCall c){
		
		if(c.getExpirationTime() == null) return;
				
		Calendar cal = Calendar.getInstance();
		cal.setTime(c.getExpirationTime());
		Date date = cal.getTime();		
		TimerInfo n = new TimerInfo(c.getID(), "closeShared");
		System.out.println("§§§ NM : closeSharedCall Timer created for " + c.getID()+" date: "+ date);
		timerService.createTimer(date, n);						
		
	}

			
}
