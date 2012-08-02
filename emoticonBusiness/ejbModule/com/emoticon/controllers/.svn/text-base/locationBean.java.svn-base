package com.emoticon.controllers;

import com.emoticon.entities.Location;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * This class implements the LocationRemote interface
 * as a stateless session bean
 * 
 * @see LocationRemote
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */

public @Stateless(name="locationBean") class locationBean implements LocationRemote {

	@PersistenceContext(unitName="emoticon")
	private EntityManager manager;
	
	public int putLocations() {
		try {
			Location loc = new Location();
			loc.setName("Macchinetta 1");
			manager.persist(loc);
			
			loc = new Location();
			loc.setName("Macchinetta 2");
			manager.persist(loc);
			
			loc = new Location();
			loc.setName("Macchinetta ingresso");
			manager.persist(loc);
			
			loc = new Location();
			loc.setName("Macchinetta vicino ai bagni");
			manager.persist(loc);
			
			loc = new Location();
			loc.setName("Macchinetta terzo piano");
			manager.persist(loc);
			
			
			return 0;
		}
		catch (Exception e) {
			return 1;
		}
	}

	public Location getLocationById(int id){
		return manager.find(Location.class, id);
	}
}
