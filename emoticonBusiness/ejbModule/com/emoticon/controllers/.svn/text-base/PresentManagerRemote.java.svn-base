package com.emoticon.controllers;
import java.util.List;
import javax.ejb.Remote;
import com.emoticon.entities.Present;
/**
 * This interface is used as remote implementation interface
 * for {@link PresentManagerBean} session beans.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */
@Remote
public interface PresentManagerRemote {
	
	public int proposePresent(String description, float price, int birthdayID, int userID);
	public int votePresent(int presentID, int userID);
	public List<Present> listPresent(int birthdayID);
	
}
