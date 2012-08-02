package com.emoticon.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class represents one coffeeCall in the system
 * It is implemented as an entity bean and persisted in database.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 */

@SuppressWarnings("serial")
@Entity
@Table(name="coffeeCall")
public class CoffeeCall implements Serializable {
	
	private int ID;
	private Date breakTime;
	private Date expirationTime;
	private int quorum;
	private Location location;
	private int type;     //0 = timed, 1 = shared
	private User issuer;
	private Set<User> subscribers;
	private int status;   //0 = closed, 1 = open
	private Date datetime;
	private Date started;
	
	/** Gets the CoffeeCall's unique identifier
	 * @return the CoffeeCall's unique ID
	 */
	@Id
	@GeneratedValue
	public int getID() {
		return ID;
	}
	/**Sets the CoffeeCall's unique identifier
	 * @param id the ID to set (not used)
	 */
	public void setID(int id) {
		ID = id;
	}
	
	/**Gets a Date object representing the time a Timed Call will start
	 * @return the breakTime of a Timed Call
	 */
	public Date getBreakTime() {
		return breakTime;
	}
	/**Sets a Date object representing the time a Timed Call will start
	 * @param breakTime the breakTime of a Timed Call to set
	 */
	public void setBreakTime(Date breakTime) {
		this.breakTime = breakTime;
	}
	
	/**Gets a Date object representing the time a Shared Call will expire
	 * @return the expirationTime of a Shared Call
	 */
	public Date getExpirationTime() {
		return expirationTime;
	}
	/**Sets a Date object representing the time a Shared Call will expire
	 * @param expirationTime the expirationTime of a Shared Call to set
	 */
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	/**Gets an integer representing the number of users needed to start a Shared Call
	 * @return the quorum of a Shared Call
	 */
	public int getQuorum() {
		return quorum;
	}
	/**Sets an integer representing the number of users needed to start a Shared Call
	 * @param quorum the quorum of a Shared Call to set
	 */
	public void setQuorum(int quorum) {
		this.quorum = quorum;
	}
	
	/**Gets a Location object representing the place to which the CoffeeCall refers
	 * @return the location of the CoffeeCall
	 */
	@ManyToOne
	public Location getLocation() {
		return location;
	}
	/**Sets a Location object representing the place to which the CoffeeCall refers
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**Gets an integer representing the type of the CoffeeCall
	 * @return the type: 0 = timed, 1 = shared
	 */
	public int getType() {
		return type;
	}
	/**Sets an integer representing the type of the CoffeeCall
	 * @param type the type to set: 0 = timed, 1 = shared
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**Gets a User object representing the user who started the CoffeeCall
	 * @return the issuer of the CoffeeCall
	 */
	@ManyToOne
	public User getIssuer() {
		return issuer;
	}
	/**Sets a User object representing the user who started the CoffeeCall
	 * @param issuer the issuer to set
	 */
	public void setIssuer(User issuer) {
		this.issuer = issuer;
	}
	
	/**
	 * @return the subscribers of the CoffeeCall
	 */
	  @ManyToMany
	  @JoinTable(name="CoffeeUser", 
	    joinColumns=
	        @JoinColumn(name="CoffeeID", referencedColumnName="ID"),
	      inverseJoinColumns=
	      @JoinColumn(name="UserID", referencedColumnName="ID")
	      )
	      public Set<User> getSubscribers() {
		  	return subscribers;
	  	}
	/**
	 * @param subscribers the subscribers to set
	 */
	  	public void setSubscribers(Set<User> subscribers) {
	  		this.subscribers = subscribers;
		}
	
	/**
	 * @return the status : 0 = closed, 1 = open
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set : 0 = closed, 1 = open
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the datetime
	 */
	public Date getDatetime() {
		return datetime;
	}
	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	/**
	 * @return the starting time of the CoffeeCall
	 */
	public Date getStarted() {
		return started;
	}
	/**
	 * @param started the starting time to set
	 */
	public void setStarted(Date started) {
		this.started = started;
	}
	
}
