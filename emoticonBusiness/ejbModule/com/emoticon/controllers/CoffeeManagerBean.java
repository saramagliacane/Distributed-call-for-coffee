package com.emoticon.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.emoticon.entities.CoffeeCall;
import com.emoticon.entities.Location;
import com.emoticon.entities.User;

/**
 * This class implements the CoffeeManager interface
 * as a stateless session bean
 * 
 * @see CoffeeManager
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 *
 */

@Stateless(name = "CoffeeManagerBean")
public class CoffeeManagerBean implements CoffeeManagerRemote {

	@PersistenceContext(unitName="emoticon")
	private EntityManager manager;
	
	@EJB private NotificationManagerRemote nm;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CoffeeCall> listCoffeeCall(){
		
		try {
			Query q = manager.createQuery("SELECT DISTINCT c FROM CoffeeCall c LEFT JOIN FETCH c.subscribers WHERE c.status = 1 ");
			// no debug c.status = 1 (only the open ones)
			return q.getResultList();
		}
		catch(NoResultException e) {
			return new LinkedList<CoffeeCall>();
		}
					
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CoffeeCall> listJoinedCoffee(int userID){
		try {
			Query q = manager.createQuery("SELECT DISTINCT c FROM CoffeeCall c LEFT JOIN FETCH c.subscribers WHERE :id MEMBER OF c.subscribers AND c.status = 1");
			q.setParameter("id", userID);
			return q.getResultList();
		}
		catch(NoResultException e) {
			return new LinkedList<CoffeeCall>();
		}
	}

	@Override
	public CoffeeCall issueCall(Location location, Date breakTime, int quorum,
			Date expirationTime, int type, int issuerid) {
		
		CoffeeCall c = new CoffeeCall();
		
		User issuer;
		
		try {
			issuer = manager.find(User.class, issuerid);
		} catch (EntityNotFoundException e) {
			return null;
		}
				
		c.setLocation(location);
		c.setIssuer(issuer);
		c.setType(type);
		c.setDatetime(new Date());
		c.setStatus(1);
		c.setStarted(null);
		
		Set<User> sub = new HashSet<User>();
		if (sub.contains(issuer)==false) sub.add(issuer);
		c.setSubscribers(sub);
								
		if (type == 0){
			//timed call
			c.setBreakTime(breakTime);			
		}
		else {
			//shared call
			c.setExpirationTime(expirationTime);
			c.setQuorum(quorum);
		}
		
		manager.persist(c);
		manager.flush();
		
		if (type == 0) nm.setTimedCallTimer(c);	
		else nm.setSharedCallTimer(c);
		
		System.out.print("*** Call " + c.getID()+ " issued by User "+issuerid);
		return c;
	}

	@Override
	public CoffeeCall joinCall(int coffeeID, int userID) {
			
		CoffeeCall c = null;
		User u = new User();
		
		try{
			c = manager.find(CoffeeCall.class,coffeeID);
			u = manager.find(User.class, userID);
			
		}catch(NoResultException e){ 
			System.out.print("***CM Call "+ coffeeID+" could not be joined by User "+userID);
			return null;
		}
			
		try{		
			if(c.getStatus() == 0) return null;
				
			Set<User> subscribers = c.getSubscribers();		
			if (subscribers.contains(u)==false) subscribers.add(u);
			c.setSubscribers(subscribers);
			
			manager.merge(c);
			manager.flush();
			System.out.print("***CM Call "+ coffeeID+" joined by User "+userID);
			return c;
		}catch(Exception e){ 
			return null;
		}
								
	
	}

	@Override
	public CoffeeCall leaveCall(int coffeeID, int userID) {
		
		CoffeeCall c = null;
		try{
			c = manager.find(CoffeeCall.class,coffeeID);
			//if removeSubscriber returns 1, the user was not removed from the call				
			
		}catch(NoResultException e){ 
			e.printStackTrace();
			System.out.println("*** CM: The User "+ userID+" could not leave call "+coffeeID +": call not found");
			return null;
		}

		try{
			Set<User> subscribers = c.getSubscribers();		
			if (subscribers.contains(manager.find(User.class,userID))){
		    	subscribers.remove(manager.find(User.class,userID));}
			else {
				System.out.println("*** CM: The User "+ userID+" could not leave call again "+coffeeID);
				return null;
			}
			c.setSubscribers(subscribers);
			
			manager.merge(c);
			manager.flush();
			System.out.print("*** CM: Call "+ coffeeID+" left by User "+userID);
			return c;	
		}catch(Exception e){ 
			e.printStackTrace();
			System.out.println("*** CM: The User "+ userID+" could not leave call "+coffeeID);
			return null;
		}
			
	
	}
	

	@SuppressWarnings("unchecked")
	public int numberOfUsersAtSameLocation(int locID){
		try{
			Query q = manager.createQuery("SELECT DISTINCT u FROM User u WHERE u.location.id =:l");
			q.setParameter("l", locID);
			List<User> drinkingUsers = q.getResultList();
			return drinkingUsers.size();
			
		}catch(Exception e){ 
			return 0;
		}
	}
	
	public List<CoffeeCall> coffeeCallsAtSameLocation(CoffeeCall c, User u){
		
		List<CoffeeCall> coffeeCalls = new LinkedList<CoffeeCall>();
		List<CoffeeCall> temp = new LinkedList<CoffeeCall>();
		
		try{
			Location l = c.getLocation();
			Query q = manager.createQuery("SELECT DISTINCT c FROM CoffeeCall c WHERE c.location.id =:l AND c.status = 1 AND c.type = 1");
			q.setParameter("l", l.getId());
			coffeeCalls = q.getResultList();
		
		}catch(NoResultException e){ 
			return new LinkedList<CoffeeCall>();
		}
					
		//debug			
		Iterator<CoffeeCall> i = coffeeCalls.iterator();
		while(i.hasNext()){
			CoffeeCall b = i.next();
			if(b.getID()!=c.getID())  
				if(!b.getSubscribers().contains(u))
					temp.add(b);
			System.out.println("### CM: Coffeecalls at same loc :" + b.getID());
		}
		//		
			
		return temp;			
		
	}
	
}
