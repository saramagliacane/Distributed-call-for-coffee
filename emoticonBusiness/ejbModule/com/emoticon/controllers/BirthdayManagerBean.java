package com.emoticon.controllers;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.emoticon.entities.BirthdayCall;
import com.emoticon.entities.Present;
import com.emoticon.entities.User;
import com.emoticon.entities.Vote;

/**
 * This class implements the  BirthdayManager interface
 * as a stateless session bean
 * 
 * @see BirthdayManager
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 * 
 */
@Stateless
public class BirthdayManagerBean implements BirthdayManagerRemote {
	
	@PersistenceContext(unitName="emoticon")
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	public List<BirthdayCall> listOpenBirthday(){
		
		try {
			Query q = manager.createQuery("SELECT DISTINCT b FROM BirthdayCall b LEFT JOIN FETCH b.subscribers WHERE b.status=1"); //
			return q.getResultList();
		}
		catch(NoResultException e) {
			return new LinkedList<BirthdayCall>();
		}
		
	}
	
	//return also the closed joined calls
	@SuppressWarnings("unchecked")
	public List<BirthdayCall> listJoinedBirthday(int userID){
		try {
			Query q = manager.createQuery("SELECT DISTINCT c FROM BirthdayCall c LEFT JOIN FETCH c.subscribers WHERE :id MEMBER OF c.subscribers");
			q.setParameter("id", userID);
			return q.getResultList();
		}
		catch(NoResultException e) {
			return new LinkedList<BirthdayCall>();
		}
	}
	public int joinBirthdayCall(int birthdayID, int userID){
		
		try{
			BirthdayCall c = manager.find(BirthdayCall.class, birthdayID);
			User u = manager.find(User.class, userID);
			if(c.getStatus() == 0) return 1;
			if(c.getUser() == u) return 1;
			
			Collection<User> subscribers = c.getSubscribers();		
			if (subscribers.contains(u)==false) subscribers.add(u);
			c.setSubscribers(subscribers);
		
			manager.merge(c);
		}catch(Exception e){ return 1;}
		
		System.out.print("*** Birthday Call "+ birthdayID+" joined by User "+userID);
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public Present chosenPresent(int birthdayID){
		try{
			BirthdayCall b = manager.find(BirthdayCall.class, birthdayID);
				
				Query q = manager.createQuery("SELECT p FROM Present AS p WHERE p.proposedFor=:b");
				q.setParameter("b", b);
				
				//get a list of presents for the specific birthday call
				List<Present> l =  q.getResultList();
				Iterator<Present> i = l.iterator();
				
				// search for the most voted present
				int maxsize = 0;
				Present chosen = null; 
				Present temp = null;
		
				while (i.hasNext()){
					
					temp = (Present) i.next();
					Set<Vote> votes = temp.getVotes();
					// if the maximum is already reached the present was proposed earlier
					
					if (maxsize < votes.size() || chosen==null){
						maxsize = votes.size();
						chosen = temp;
					}
					// choose the cheapest present between two with the same votes
					else if (maxsize == votes.size() && chosen!=null) {
						chosen = chosen.getPrice()<temp.getPrice() ? chosen:temp;
					}
				}
				
				return chosen;

		}catch(Exception e){ return null;}
	}
}
