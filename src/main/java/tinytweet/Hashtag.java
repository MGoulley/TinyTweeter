package tinytweet;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache(expirationSeconds=600)
public class Hashtag {
	@Id Long hashtagID;
	private String hashtag;
	private Set<Long> tweets = new HashSet<Long>();
	
	public Hashtag() {}
	
	public Hashtag(String hashtag) {
		this.hashtagID = null;
		this.hashtag = hashtag;
	}

	@Override
	public String toString() {
		return "Hashtag [hashtagID=" + hashtagID + ", hashtag=" + hashtag + "]";
	}

	public void addTweet(Long tweetID) {
		this.tweets.add(tweetID);
	}
	
	public void removeTweet(Long tweetID) {
		this.tweets.remove(tweetID);
	}

	public Long getHashtagID() {
		return hashtagID;
	}

	public void setHashtagID(Long hashtagID) {
		this.hashtagID = hashtagID;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public Set<Long> getTweets() {
		return tweets;
	}

	public void setTweets(Set<Long> tweets) {
		this.tweets = tweets;
	}
	
	
}