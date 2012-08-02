package com.emoticon.controllers;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.emoticon.entities.BirthdayCall;
import com.emoticon.entities.Present;
import com.emoticon.entities.User;
import com.emoticon.entities.Vote;


/**
 * This class implements the PresentManager interface
 * as a stateless session bean
 * 
 * @see PresentManager
 * @author Francesco Pongetti
 * @author Sara Magliacane
 * 
 */
@Stateless
public class PresentManagerBean implements PresentManagerRemote {

	@PersistenceContext(unitName="emoticon")
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Present> listPresent(int birthdayID) {
		BirthdayCall b;
		try {
			b = manager.find(BirthdayCall.class, birthdayID);
			System.out.println("*** BirthdayClass found: " + birthdayID);
		}catch(EntityNotFoundException e){
			System.out.println("*** BirthdayClass not found: " + birthdayID);
			return new LinkedList<Present>();
		}
		try{
			Query q = manager.createQuery("SELECT DISTINCT p FROM Present AS p WHERE p.proposedFor=:b ORDER by p.proposedFor");
			q.setParameter("b", b);
			System.out.println("*** There is a list of presents somewhere for "+ b.getID());
			List <Present> l =  q.getResultList();
			Iterator<Present> i = l.iterator();
			while (i.hasNext()) 
			{
				Present p = (Present)i.next();
				System.out.println("*** present: "+ p.getDescription()+" ");
			}
			return l;
		}catch(NoResultException f) {
			System.out.println("*** BirthdayClass found, but couldn't get the presents' list ");
			return null;
		}
	}

	@Override
	public int proposePresent(String description, float price, int birthdayID,
			int userID) {
		Present p = new Present();
		p.setDatetime(new Date());
		p.setDescription(description);
		p.setPrice(price);
				
		BirthdayCall b; User u;		
		try{
			b = (BirthdayCall)manager.find(BirthdayCall.class,birthdayID);
			u = (User)manager.find(User.class,userID); 
		}catch(EntityNotFoundException e){
			System.out.println("*** proposePresent: BirthdayClass ot User not found");
			return 1;
		}
		//if the call is closed
		if(b == null) return 1;
		if(b.getStatus()==0) return 1;
		if(b.getSubscribers()== null) return 1;
		
		//or the user is not a subscriber of the call return error
		if(b.getSubscribers().contains(u)){
		
			p.setProposedBy(u);
			p.setProposedFor(b);
			manager.persist(p);
			manager.flush();
			System.out.println("*** Proposed a present "+ description);
			//propose = vote for the proposed present
			return votePresent(p.getID(), userID);
		}
		else return 1;
	}

	@Override
	public int votePresent(int presentID, int userID) {
		
		Present p; User u;
		
		try{
			p = manager.find(Present.class,presentID);
			u = manager.find(User.class,userID);
		}catch(EntityNotFoundException e){
			System.out.println("*** Could not vote for the present. Not found.");
			return 1;
		}
		
		if( p == null) return 1;
		BirthdayCall c = p.getProposedFor();
		//if the call is closed
		if(c.getStatus()==0) return 1;
		//or the user is not a subscriber of the call return error
		if(!c.getSubscribers().contains(u)) return 1;
		
		//update the old vote or insert a new one
		try{
			Query q = manager.createQuery("SELECT DISTINCT v FROM Vote AS v WHERE v.user=:us AND v.birthdayCall=:bd");
			q.setParameter("us", u);
			q.setParameter("bd", c);
			Vote oldvote = (Vote) q.getSingleResult();
			oldvote.setPresent(p);
			manager.merge(oldvote);
			System.out.println("*** Old vote with in this birthdaycall " + c.getUser().getUsername() +" updated.");
					
		} catch (NoResultException e){
			
			System.out.println("*** No old vote for this user "+ u.getUsername()+ " in the birtdhacall for " + c.getUser().getUsername() +" found.");
			Vote v = new Vote();
			v.setUser(u);
			v.setPresent(p);
			v.setBirthdayCall(c);
			manager.persist(v);
		}
		
		manager.flush();
		
		System.out.println("*** Voted for a present "+ p.getDescription());
		
		// if this vote has reached the absolute majority of votes (X - 1)/2 the call is closed
		Query count = manager.createQuery("SELECT count(u) FROM User AS u");
		long num_users = (Long) count.getSingleResult();
		
		count = manager.createQuery("SELECT count(v) FROM Vote AS v WHERE v.present=:p");
		count.setParameter("p", p);
		long num_votes =  (Long) count.getSingleResult();
		
		if (num_votes == (num_users)/2){
			c.setStatus(0);
			manager.merge(c);
			System.out.println("*** BirthdayCall is " + c.getID() + " closed.");
			
		}
		return 0;
	}
	
}
