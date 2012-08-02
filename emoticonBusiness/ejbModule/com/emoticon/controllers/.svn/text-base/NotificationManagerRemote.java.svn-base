package com.emoticon.controllers;
import javax.ejb.Remote;
import javax.ejb.Timer;

import com.emoticon.entities.CoffeeCall;
import com.emoticon.entities.User;

/**
* @author Francesco Pongetti
* @author Sara Magliacane
*/
@Remote
public interface NotificationManagerRemote {
	
	public void setBirthdayTimer(User u);
	public void Timeout(Timer timer);
	public void setTimedCallTimer(CoffeeCall c);
	public void setSharedCallTimer(CoffeeCall c);
}
