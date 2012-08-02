package com.emoticon.controllers;
import java.io.Serializable;

import com.emoticon.entities.*;

/**
 * Notification Class of objects sent to the user by the checkEvents function 
 * in the UserManager
 * @author Francesco Pongetti
 * @author Sara Magliacane
 */
@SuppressWarnings("serial")
public class Notification implements Serializable{
	
	//0... Birthday, 1...Coffee
	public int type;
	public Present present;
	public BirthdayCall birthdaycall;
	public CoffeeCall coffeecall;
	
	public Notification(int type, Present present, BirthdayCall birthdaycall,
			CoffeeCall coffeecall) {
		this.type = type;
		this.present = present;
		this.birthdaycall = birthdaycall;
		this.coffeecall = coffeecall;
	}
	
	public String toString(){
		if (type == 0) 
			return "Birthday Notification: chosen " + present.getDescription() + " for Birthday of user " + birthdaycall.getUser().getUsername(); 
		else
			return "Coffee Notification: for " +(coffeecall.getType()==0? "timed":"shared") + " " +coffeecall.getID()+ " @ " +coffeecall.getLocation().getName(); 
	}
}
	

