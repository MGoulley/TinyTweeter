package tinytweet;

import com.google.api.client.util.Lists;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.AnnotationBoolean;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Api(name = "tinytweeter",
	apiKeyRequired = AnnotationBoolean.FALSE)
public class TinytweeterEndpoint {
	static {
        ObjectifyService.register(Utilisateur.class); 
        ObjectifyService.register(Tweet.class); 
        ObjectifyService.register(Hashtag.class); 
    }
	
	@ApiMethod(name = "create_user", httpMethod = ApiMethod.HttpMethod.POST, path="users/create")
    public Utilisateur createUtilisateur(@Named("username")String username) {
    	ofy().clear();
    	List<Utilisateur> users = ofy().load().type(Utilisateur.class).list();
		for (Utilisateur u : users) {
			if (u.username.equals(username)) {
				System.out.println(u.getUsername() + " existe deja.");
				return u;
			}
		}
    	Utilisateur user = new Utilisateur(username);
    	ofy().save().entity(user).now();
    	System.out.println("Creation de: " + user);
    	return user;
    }
	
	@ApiMethod(name = "users", httpMethod = ApiMethod.HttpMethod.GET, path="users")
	public List<Utilisateur> utilisateurs(@Named("limite")int nb) {
		ofy().clear();
		List<Utilisateur> users = ofy().load().type(Utilisateur.class).limit(nb).list();
    	return users;
    }
	
	@ApiMethod(name = "usersbyname", httpMethod = ApiMethod.HttpMethod.GET, path="usersbyname")
	public List<Utilisateur> usersbyname(@Named("user")String user) {
		ofy().clear();
		List<Utilisateur> users = ofy().load().type(Utilisateur.class).list();
		
		List<Utilisateur> toRemove = new ArrayList<Utilisateur>();
		for (Utilisateur elem : users){
			if (!elem.username.equals(user)){
				toRemove.add(elem);
			}
		}
		for (Utilisateur u : toRemove) {
			users.remove(u);
		}
		System.out.println(users);
    	return users;
    }
	
	
	@ApiMethod(name = "create_tweet", httpMethod = ApiMethod.HttpMethod.POST, path="tweets/create")
	public Tweet createTweet(@Named("authorID")Long userID, 
							@Named("authorUsername") String username, 
							@Named("message") String message) {
		ofy().clear();
		
		// Creation du tweet
		Tweet tweet = new Tweet(userID, username, message);
		ofy().save().entity(tweet).now();
		
		// Recuperation des hashtags dans le tweet
		List<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile("\\#(\\S+)"); // regex pour obtenir les hastags
        Matcher m = p.matcher(message);
        while(m.find())
        {
            result.add(m.group(1));
        }
        
        // Creation des hashtags ou ajout dans les hashtags existants
        if(!result.isEmpty()) {
        	Set<Long> hastagsID = new HashSet<Long>();
        	List<Hashtag> hashtags = ofy().load().type(Hashtag.class).list(); //  on récupère une seule fois la liste des hashtags
            for(String s : result) {
            	boolean found = false;
            	for(int i = 0; i < hashtags.size(); i++) {
            		if(s.equals(hashtags.get(i).getHashtag())) {
            			Hashtag ht = hashtags.get(i);
            			ht.addTweet(tweet.tweetID);
            			ofy().save().entity(ht).now();
            			hastagsID.add(ht.hashtagID);
            			found = true;
            		}
            	}
            	// si le hashtag n'existe pas encore
            	if(found == false) {
            		Hashtag ht = new Hashtag(s);
            		ht.addTweet(tweet.tweetID);
            		ofy().save().entity(ht).now();
        			hastagsID.add(ht.hashtagID);
            	}
            }
            // Mise � jour du tweet avec les hashtags
            tweet.setHashtags(hastagsID);
            ofy().save().entity(tweet).now();
        }  
        
		// Ajout du tweet pour l'utilisateur
		Key<Utilisateur> cleUser = Key.create(Utilisateur.class, userID);
		Utilisateur user = ofy().load().key(cleUser).now();
		System.out.println(tweet);		
		user.addTweet(tweet.tweetID);	
		System.out.println(user);
		ofy().save().entity(user).now();
    	return tweet;
    }
	
	@ApiMethod(name = "get_hashtag_tweets",  httpMethod = ApiMethod.HttpMethod.GET, path="hashtag/{hashtagID}") 
    public List<Tweet> get_hashtag_tweets(@Named("hashtagID")Long hashtagID) {
        ofy().clear();
        
        Key<Hashtag> cleHT = Key.create(Hashtag.class, hashtagID);
		Hashtag ht = ofy().load().key(cleHT).now();
        return Lists.newArrayList(ofy().load().type(Tweet.class).ids(ht.getTweets()).values());
    }
	
	
	@ApiMethod(name = "hashtags",  httpMethod = ApiMethod.HttpMethod.GET, path="hashtags") 
    public List<Hashtag> showhashtags() {
        ofy().clear();
        List<Hashtag> allhashtags = ofy().load().type(Hashtag.class).list();
        return allhashtags;
    }
	
	@ApiMethod(name = "tweets", httpMethod = ApiMethod.HttpMethod.GET, path="tweets")
	public List<Tweet> tweets() {
		ofy().clear();
		List<Tweet> tweets = ofy().load().type(Tweet.class).list();
    	return tweets;
    }
	
	@ApiMethod(name = "add_follow", httpMethod = ApiMethod.HttpMethod.POST, path="users/addFollow")
	public Utilisateur addFollow(@Named("userID")Long userID, @Named("followID") Long followID) {
		ofy().clear();
		
		Key<Utilisateur> cleUser = Key.create(Utilisateur.class, userID);
		Utilisateur user = ofy().load().key(cleUser).now();
		user.addAbonnement(followID);

		Key<Utilisateur> cleFollower = Key.create(Utilisateur.class, followID);
		Utilisateur follower = ofy().load().key(cleFollower).now();
		follower.addFollower(userID);

		ofy().save().entity(user).now();
		ofy().save().entity(follower).now();
    	return user;
    }
	
	@ApiMethod(name = "timeline", httpMethod = ApiMethod.HttpMethod.POST, path = "users/{userID}/timeline")
	public List<Tweet> timeline(@Named("userID") Long userID, @Named("nb_messages") int nb) {
		ofy().clear();
		Key<Utilisateur> cleUser = Key.create(Utilisateur.class, userID);
		Utilisateur user = ofy().load().key(cleUser).now();
		List<Long> tweetsID = new ArrayList<Long>();
		tweetsID.addAll(user.getMytweets()); // Ajoute les tweets de user
		for (Long l : user.getAbonements()) {
			Key<Utilisateur> cleU = Key.create(Utilisateur.class, l);
			Utilisateur u = ofy().load().key(cleU).now();
			tweetsID.addAll(u.getMytweets());
		}
		System.out.println(user);
		System.out.println(user.getMytweets());
		
		List<Tweet> tweets = new ArrayList<Tweet>();
		for(Long l : tweetsID)
		{
			tweets.add(ofy().load().key(Key.create(Tweet.class, l)).now());
		}	
		
		Collections.sort(tweets, new Comparator<Tweet>() {
			@Override
			public int compare(Tweet t0, Tweet t1) {
				return t0.getDate().compareTo(t1.getDate());
			}
		});
		
		
        if (nb == 0 || tweets.size() < nb) {
            return tweets;
        } else {
            return tweets.subList(0, nb-1);
        }
	}
	
	@ApiMethod(name = "resetALL", httpMethod = ApiMethod.HttpMethod.POST, path="resetall")
	public void resetALL() {
		ofy().clear();	
		Iterable<Key<Utilisateur>> clesUsers = ofy().load().type(Utilisateur.class).keys().list();
		ofy().delete().keys(clesUsers).now();
		
		Iterable<Key<Tweet>> clesTweets = ofy().load().type(Tweet.class).keys().list();
		ofy().delete().keys(clesTweets).now();
		
		Iterable<Key<Hashtag>> cleshtag = ofy().load().type(Hashtag.class).keys().list();
		ofy().delete().keys(cleshtag).now();
    }
	
	@ApiMethod(name = "create_problem", httpMethod = ApiMethod.HttpMethod.POST, path = "createproblem")
	public void createproblem() {
		resetALL(); // reset avant de recreer
		/*Utilisateur brig = createUtilisateur("Brigitte");
		for (int i = 1; i <= 100; i++) {
			Utilisateur temp = createUtilisateur("Brigitte_follower_" + i);
			addFollow(brig.utilisateurID,temp.utilisateurID);
		}*/
		Utilisateur pdc = createUtilisateur("Pas-de-Calais");
		for (int i = 1; i <= 1000; i++) {
			Utilisateur temp = createUtilisateur("Pas-de-Calais_follower_" + i);
			addFollow(pdc.utilisateurID,temp.utilisateurID);
		}/*
		 Utilisateur pascal = createUtilisateur("Pascal");
		for (int i = 1; i <= 5000; i++) {
			Utilisateur temp = createUtilisateur("Pascal_follower_" + i);
			addFollow(pascal.utilisateurID,temp.utilisateurID);
		}*/
		
		/*for (int j=0;j<1;j++) {
			createTweet(brig.utilisateurID, brig.username, "Il pleut #pluie");
		}*/
		long res[] = new long[30];
		for (int i =0 ; i<30;i++) {
			long startTime = System.currentTimeMillis();
			//timeline(brig.utilisateurID);
			createTweet(pdc.utilisateurID, pdc.username, "Il pleut #pluie");
			long endTime = System.currentTimeMillis();
			res[i]=endTime - startTime;
		}
		for(long i : res) {
		System.out.println(i);
		}
		/*
		// Hashtag contenant 1000 tweets
		for (int i = 0; i < 1000; i++) {
			createTweet(pdc.utilisateurID, pdc.username, "Il pleut #pluie");
		}
		// Hashtag contenant 5000 tweets
		for (int i = 0; i < 5000; i++) {
			createTweet(pascal.utilisateurID, pascal.username, "HTTP est #statefull");
		}*/
	}
	
}