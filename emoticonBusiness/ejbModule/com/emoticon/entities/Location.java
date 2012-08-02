package com.emoticon.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents one location where an user can go to take a coffee
 * It is implemented as an entity bean and persisted in database.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name="location")
public class Location implements Serializable {
	
	private int id;
	private String name;
	
	/**Gets the Location unique identifier
	 * @return the Location's unique ID
	 */
	@Id
	@GeneratedValue
	public int getId()
	{
		return id;
	}
	/**Sets the Location unique identifier
	 * @param id unique identifier to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**Gets a String with the Location name.
	 * @return Location name
	 */
	public String getName() {
		return name;
	}
	/**Sets a String with the Location name.
	 * @param name name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public Location(){}
	
	public Location(String name){
		this.id =this.getId();
		this.name = name;
	}
		
	/**
	 * Override the default to string
	 */
	public String toString() {
		return this.name;
	}
}
