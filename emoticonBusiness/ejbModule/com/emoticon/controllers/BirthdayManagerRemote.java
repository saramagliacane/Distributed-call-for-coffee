package com.emoticon.controllers;
import java.util.List;
import javax.ejb.Remote;
import com.emoticon.entities.BirthdayCall;
import com.emoticon.entities.Present;
/**
 * This interface is used as remote implementation interface
 * for {@link BirthdayManagerBean} session beans.
 * 
 * @author Francesco Pongetti
 * @author Sara Magliacane
 *
 */
@Remote
public interface BirthdayManagerRemote {
	
	public List<BirthdayCall> listOpenBirthday();
	public List<BirthdayCall> listJoinedBirthday(int userID);
	public int joinBirthdayCall(int birthdayID, int userID);
	public Present chosenPresent(int birthdayID);
		
}
