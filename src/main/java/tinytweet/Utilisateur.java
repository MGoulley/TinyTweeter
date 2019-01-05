package tinytweet;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache(expirationSeconds = 600)
public class Utilisateur {	
	@Id
	Long utilisateurID;
	String username;
	private Set<Long> abonements = new HashSet<Long>(); 
	private Set<Long> followers = new HashSet<Long>(); 
	private Set<Long> mytweets = new HashSet<Long>();

	public Utilisateur() {}

	public Utilisateur(String username) {
		this.username = username;
		this.utilisateurID = null;
	}
	


	/*
	 * Getters / Setters / Adders / Removers
	 */

	public void addTweet(Long l) {
		mytweets.add(l);
	}

	public void addAbonnement(Long l) {
		abonements.add(l);
	}

	public void addFollower(Long l) {
		followers.add(l);
	}

	/**
	 * @return the utilisateurID
	 */
	public Long getUtilisateurID() {
		return utilisateurID;
	}

	/**
	 * @param utilisateurID the utilisateurID to set
	 */
	public void setUtilisateurID(Long utilisateurID) {
		this.utilisateurID = utilisateurID;
	}

	/**
	 * @return the username
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
	 * @return the abonements
	 */
	public Set<Long> getAbonements() {
		return abonements;
	}

	/**
	 * @param abonements the abonements to set
	 */
	public void setAbonements(Set<Long> abonements) {
		this.abonements = abonements;
	}

	/**
	 * @return the followers
	 */
	public Set<Long> getFollowers() {
		return followers;
	}

	/**
	 * @param followers the followers to set
	 */
	public void setFollowers(Set<Long> followers) {
		this.followers = followers;
	}

	/**
	 * @return the mytweets
	 */
	public Set<Long> getMytweets() {
		return mytweets;
	}

	/**
	 * @param mytweets the mytweets to set
	 */
	public void setMytweets(Set<Long> mytweets) {
		this.mytweets = mytweets;
	}


}
