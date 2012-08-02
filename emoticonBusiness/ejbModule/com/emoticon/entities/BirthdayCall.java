package com.emoticon.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class represents one BirthdayCall in the system
 * It is implemented as an entity bean and persisted in database.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name="birthdayCall")


public class BirthdayCall implements Serializable{
	
	private int ID;
	private User user;
	private Collection<User> subscribers;
	private int status;
	
	
	/**Gets the BirthdayCall unique identifier
	 * @return the BirthdayCall's unique ID
	 */
	@Id
	@GeneratedValue
	public int getID() {
		return ID;
	}
	/**Sets the BirthdayCall unique identifier
	 * @param id the ID to set (not used)
	 */
	public void setID(int id) {
		ID = id;
	}
	
	/**Gets the User object that the BirthdayCall refers to
	 * @return the user who will celebrate his birthday 
	 */
	@ManyToOne
	public User getUser() {
		return user;
	}
	/**Sets the User object that the BirthdayCall refers to
	 * @param user the user to whom the BirthdayCall refers
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**Gets the User objects that subscribed the BirthdayCall
	 * @return the subscribers of the birthdayCall
	 */
	  @ManyToMany
	  @JoinTable(name="BithdayUser", 
	    joinColumns=
	        @JoinColumn(name="birthdayID", referencedColumnName="ID"),
	      inverseJoinColumns=
	      @JoinColumn(name="userID", referencedColumnName="ID")
	      )
	public Collection<User> getSubscribers() {
		return subscribers;
	}
	/**Sets the User objects that subscribed the BirthdayCall
	 * @param subscribers the subscribers to set
	 */
	public void setSubscribers(Collection<User> subscribers) {
	this.subscribers = subscribers;
	}
	
	/**Gets an integer representing the status of the BirthdayCall
	 * @return the status of the BirthdayCall: 0 = closed, 1 = open
	 */
	public int getStatus(){
		return this.status;
	}
	
	/**Sets an integer representing the status of the BirthdayCall
	 * @param status to set: 0 = closed, 1 = open
	 */
		public void setStatus(int status){
		this.status=status;
	}
	
}
