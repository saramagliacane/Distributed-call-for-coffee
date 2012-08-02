package com.emoticon.controllers;

import javax.ejb.Remote;

import com.emoticon.entities.Location;

/**
 * This interface is used as remote implementation interface
 * for {@link LocationBean} session beans.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */
@Remote

public interface LocationRemote {

	/** This method insert into the DB some default locations
	 *  It has to be lunched before starting the application for the first time
	 *  @return error code or 0
	 */
	public int putLocations();
	public Location getLocationById(int id);
}
