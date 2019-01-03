package tinytweet;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;

@Entity
@Cache(expirationSeconds = 600)
public class Utilisateur {
	public class LoadUtilisateur{}
	
	@Id
	Long utilisateurID;
	String username;
	@Ignore
	private List<Utilisateur> abonements;
	@Ignore
	private List<Utilisateur> followers;
	@Ignore
	private List<Tweet> mytweets;

	@Load(LoadUtilisateur.class)
	@Index
	private transient List<Ref<Utilisateur>> abonementsRefs =  new ArrayList<Ref<Utilisateur>>();
	@Load(LoadUtilisateur.class)
	@Index
	private transient List<Ref<Utilisateur>> followersRefs = new ArrayList<Ref<Utilisateur>>();
	@Load(Tweet.LoadTweet.class)
	@Index
	private transient List<Ref<Tweet>> mytweetsRefs = new ArrayList<Ref<Tweet>>();
	/*
	 * private Set<Long> abonements = new HashSet<Long>(); private Set<Long>
	 * followers = new HashSet<Long>(); private Set<Long> mytweets = new
	 * HashSet<Long>();
	 */

	public Utilisateur() {}

	public Utilisateur(String username) {
		this.username = username;
		this.utilisateurID = null;
	}
	

	@OnLoad
	public void deRef() {
		System.out.println("CHARGEMENTTTTT");
		System.out.println(mytweetsRefs);
		if (abonementsRefs != null) {
			abonements = new ArrayList<Utilisateur>();
			for (Ref<Utilisateur> abonne : abonementsRefs) {
				if (abonne.isLoaded()) {
					abonements.add(abonne.get());
				}
			}
		}
		if (followersRefs != null) {
			followers = new ArrayList<Utilisateur>();
			for (Ref<Utilisateur> follower : followersRefs) {
				if (follower.isLoaded()) {
					followers.add(follower.get());
				}
			}
		}
		if (mytweetsRefs != null) {
			mytweets = new ArrayList<Tweet>();
			for (Ref<Tweet> tweet : mytweetsRefs) {
				if (tweet.isLoaded()) {
					mytweets.add(tweet.get());
				}
			}
		}
	}

	/*
	 * Getters / Setters / Adders / Removers
	 */

	public void addTweet(Key<Tweet> t) {
		System.out.println(t);
		System.out.println(mytweetsRefs);
		mytweetsRefs.add(Ref.create(t));
	}

	public void addAbonnement(Key<Utilisateur> u) {
		abonementsRefs.add(Ref.create(u));
	}

	public void addFollower(Key<Utilisateur> u) {
		followersRefs.add(Ref.create(u));
	}

	/**
	 * @return the utilisateurID
	 */
	public Long getUtilisateurID() {
		return utilisateurID;
	}

	/**
	 * @param utilisateurID
	 *            the utilisateurID to set
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
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the abonements
	 */
	public List<Utilisateur> getAbonements() {
		return abonements;
	}

	/**
	 * @param abonements
	 *            the abonements to set
	 */
	public void setAbonements(List<Utilisateur> abonements) {
		this.abonements = abonements;
	}

	/**
	 * @return the followers
	 */
	public List<Utilisateur> getFollowers() {
		return followers;
	}

	/**
	 * @param followers
	 *            the followers to set
	 */
	public void setFollowers(List<Utilisateur> followers) {
		this.followers = followers;
	}

	/**
	 * @return the mytweets
	 */
	public List<Tweet> getMytweets() {
		return mytweets;
	}

	/**
	 * @param mytweets
	 *            the mytweets to set
	 */
	public void setMytweets(List<Tweet> mytweets) {
		this.mytweets = mytweets;
	}

	/**
	 * @return the abonementsRefs
	 */
	public List<Ref<Utilisateur>> getAbonementsRefs() {
		return abonementsRefs;
	}

	/**
	 * @param abonementsRefs
	 *            the abonementsRefs to set
	 */
	public void setAbonementsRefs(List<Ref<Utilisateur>> abonementsRefs) {
		this.abonementsRefs = abonementsRefs;
	}

	/**
	 * @return the followersRefs
	 */
	public List<Ref<Utilisateur>> getFollowersRefs() {
		return followersRefs;
	}

	/**
	 * @param followersRefs
	 *            the followersRefs to set
	 */
	public void setFollowersRefs(List<Ref<Utilisateur>> followersRefs) {
		this.followersRefs = followersRefs;
	}

	/**
	 * @return the mytweetsRefs
	 */
	public List<Ref<Tweet>> getMytweetsRefs() {
		return mytweetsRefs;
	}

	/**
	 * @param mytweetsRefs
	 *            the mytweetsRefs to set
	 */
	public void setMytweetsRefs(List<Ref<Tweet>> mytweetsRefs) {
		this.mytweetsRefs = mytweetsRefs;
	}
}
