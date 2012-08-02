package com.emoticon.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class represents a vote of an user for a present
 * It is implemented as an entity bean and persisted in database.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 */

@SuppressWarnings("serial")
@Entity
@Table(name="vote")
public class Vote implements Serializable {
	
	private int voteid;
	private User user;
	private BirthdayCall birthdayCall;
	private Present present;

	
	
	@Id
	@GeneratedValue
	public int getVoteid(){
		return voteid;
	}
	
	public void setVoteid(int voteid){
		this.voteid = voteid;
	}
	/**
	 * @return the user
	 */
	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the birthdayCall
	 */
	@ManyToOne
	public BirthdayCall getBirthdayCall() {
		return birthdayCall;
	}

	public void setBirthdayCall(BirthdayCall birthdayCall) {
		this.birthdayCall = birthdayCall;
	}

	/**
	 * @return the present
	 */
	@ManyToOne
	public Present getPresent() {
		return present;
	}

	public void setPresent(Present present) {
		this.present = present;
	}

}