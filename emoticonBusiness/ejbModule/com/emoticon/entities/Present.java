package com.emoticon.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This class represents one present in the system
 * It is implemented as an entity bean and persisted in database.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name="present")
public class Present implements Serializable {
	
	private int ID;
	private String description;
	private float price;

	private User proposedBy;

	private BirthdayCall proposedFor;
	private Date datetime;
	private Set<Vote> votes;
	
	
	/**
	 * @return the present's unique ID
	 */
	@Id
	@GeneratedValue
	public int getID() {
		return ID;
	}
	/**
	 * @param id the ID to set
	 */
	public void setID(int id) {
		ID = id;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	
	/**
	 * @return the proposedBy
	 */
	@ManyToOne
	public User getProposedBy() {
		return proposedBy;
	}
	/**
	 * @param proposedBy the proposedBy to set
	 */
	public void setProposedBy(User proposedBy) {
		this.proposedBy = proposedBy;
	}
	
	/**
	 * @return the proposedFor
	 */
	@ManyToOne
	public BirthdayCall getProposedFor() {
		return proposedFor;
	}
	/**
	 * @param proposedFor the proposedFor to set
	 */
	public void setProposedFor(BirthdayCall proposedFor) {
		this.proposedFor = proposedFor;
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
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="present_ID") 
	public Set<Vote> getVotes(){
		return votes; 
	}
	
	public void setVotes(Set<Vote> votes){
		this.votes = votes;
	}
}