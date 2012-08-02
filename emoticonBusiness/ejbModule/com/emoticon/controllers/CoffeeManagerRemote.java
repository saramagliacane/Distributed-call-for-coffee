package com.emoticon.controllers;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import com.emoticon.entities.*;
/**
 * This interface is used as remote implementation interface
 * for {@link CoffeeManagerBean} session beans.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */

@Remote
public interface CoffeeManagerRemote {

	public List<CoffeeCall> listCoffeeCall ();
	public List<CoffeeCall> listJoinedCoffee(int userID);
	public CoffeeCall issueCall (Location location, Date breakTime, int quorum, Date expirationTime, int type, int issuerid);
	public CoffeeCall joinCall(int coffeeID, int userID);
	public CoffeeCall leaveCall(int coffeeID, int userID);
	public int numberOfUsersAtSameLocation(int locID);
	public List<CoffeeCall> coffeeCallsAtSameLocation(CoffeeCall c, User u);
	
}
