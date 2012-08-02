package com.emoticon.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class represents an user
 * It is implemented as an entity bean and persisted in database.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="user")

public class User implements Serializable {
	
	private int id;
	private String name;
	private String surname;
	private String username;
	private String password;
	private String email;
	private Date birthday;
	//status = 0... off-line, 1... working, 2... busy, 3... drinking
	private int status;
	

	private Location location;
	
	/**
	 * @return the user's unique id
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the user's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the user's surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
	/**
	 * @return the username to login
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the password to login
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the user's email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the user's birthday
	 */
	public Date getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 * 0... off-line, 1... working, 2... busy, 3... drinking
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the location where the user is getting a coffee
	 */
	@ManyToOne
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}	
	
	
}