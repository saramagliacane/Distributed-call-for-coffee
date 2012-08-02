package com.emoticon.controllers;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import com.emoticon.entities.BirthdayCall;
import com.emoticon.entities.CoffeeCall;
import com.emoticon.entities.Location;
import com.emoticon.entities.Present;
import com.emoticon.entities.User;

/**
 * This interface is used as remote implementation interface
 * for {@link UserManagerBean} session beans.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */
@Remote

public interface UserManagerRemote {

	/** This method insert into the DB a new registred user
	 * error code 1 for already exist user name
	 *  @return error code or 0
	 */
	public int register(String name, String surname, String user, String pass, String email, Date birthday);
	public int login(String user, String pass);
	public int logout();
	public List<User> whoIsOnline();
	public void setStatus(int status);
	public List<CoffeeCall> listCoffeeCall ();
	public List<CoffeeCall> listJoinedCoffee();
	public int issueSharedCall(Location location, int quorum, Date expirationTime);
	public int issueTimedCall(Location location, Date breakTime);
	public int joinCoffeeCall(int coffeeID);
	public int leaveCoffeeCall(int coffeeID);
	public List<BirthdayCall> listOpenBirthday();
	public List<BirthdayCall> listJoinedBirthday();
	public int joinBirthdayCall(int birthdayID);
	public int proposePresent(String description, float price, int birthdayID);
	public int votePresent(int presentID);
	public List<Present> listPresent(int birthdayID);
	public Present chosenPresent(int birthdayID);
	
	public List<Location> listLocation();
	
	public List<Notification> checkEvents();
	
	public User getCurrentUser();
}
