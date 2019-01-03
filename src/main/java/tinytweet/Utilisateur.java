package tinytweet;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
@Cache(expirationSeconds=600)
public class Utilisateur {
	@Id Long utilisateurID;
	String username;	
	private Set<Long> abonements = new HashSet<Long>();
	private Set<Long> followers = new HashSet<Long>();
	private Set<Long> mytweets = new HashSet<Long>();
	
	public Utilisateur() {}

	public Utilisateur(String username) {
		this.username = username;
		this.utilisateurID = null;
	}
	
	@Override
	public String toString() {
		return "Utilisateur [utilisateurID=" + utilisateurID + ", username=" + username + ", abonements=" + abonements
				+ ", followers=" + followers + ", mytweets=" + mytweets + "]";
	}
	
	/*
	 *  Getters / Setters / Adders / Removers
	 */
	public void addFollower(Long followerID) { this.followers.add(followerID);}	
	public void removeFollower(Long followerID) { this.followers.remove(followerID);}	
	public void addTweet(Long tweetID) { this.mytweets.add(tweetID);}	
	public void removeTweet(Long tweetID) { this.mytweets.remove(tweetID);}	
	public void addAbonnement(Long userID) {this.abonements.add(userID);}	
	public void removeAbonnement(Long userID) {this.abonements.remove(userID);}
	public Long getUtilisateurID() {return utilisateurID;}
	public void setUtilisateurID(Long utilisateurID) {this.utilisateurID = utilisateurID;}
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	public Set<Long> getAbonements() {return abonements;}
	public void setAbonements(Set<Long> abonements) {this.abonements = abonements;}
	public Set<Long> getFollowers() {return followers;}
	public void setFollowers(Set<Long> followers) {this.followers = followers;}
	public Set<Long> getMytweets() {return mytweets;}
	public void setMytweets(Set<Long> mytweets) {this.mytweets = mytweets;}
}

