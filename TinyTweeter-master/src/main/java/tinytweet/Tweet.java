package tinytweet;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache(expirationSeconds=600)
public class Tweet {
	@Id Long tweetID;
	private String auteur; // Accès plus facile !
	private Long auteurID;
	private String tweet;
	private Date date;
	private Set<Long> hashtags = new HashSet<Long>();

	public Tweet() {}
	
	public Tweet(Long auteurID, String auteur, String tweet) {
		this.tweetID = null;
		this.auteurID = auteurID;
		this.auteur = auteur;
		this.tweet = tweet;
		this.date = new Date();
	}
	
	public Tweet(Long auteurID, String auteur, String tweet, Date date, Set<Long> hashtags) {
		this.tweetID = null;
		this.auteurID = auteurID;
		this.auteur = auteur;
		this.tweet = tweet;
		this.date = date;
		this.hashtags = hashtags;
	}	

	@Override
	public String toString() {
		return "Tweet [tweetID=" + tweetID + ", auteur=" + auteur + ", auteurID=" + auteurID + ", tweet=" + tweet
				+ ", date=" + date + ", hashtags=" + hashtags + "]";
	}

	public Set<Long> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<Long> hashtags) {
		this.hashtags = hashtags;
	}

	public Long getTweetID() {
		return tweetID;
	}

	public void setTweetID(Long tweetID) {
		this.tweetID = tweetID;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public Long getAuteurID() {
		return auteurID;
	}

	public void setAuteurID(Long auteurID) {
		this.auteurID = auteurID;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}