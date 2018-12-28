package tinytweet;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import tinytweet.Utilisateur;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Api(name = "tinytweeter")
public class TinytweeterEndpoint {
	static {
        ObjectifyService.register(Utilisateur.class); 
        ObjectifyService.register(Tweet.class); 
        ObjectifyService.register(Hashtag.class); 
    }
	
	@ApiMethod(name = "create_user", httpMethod = ApiMethod.HttpMethod.POST, path="users/create")
    public Utilisateur createUtilisateur(@Named("username")String username) {
    	Utilisateur user = new Utilisateur(username);
    	ofy().clear();
    	ofy().save().entity(user).now();
    	return user;
    }
	
	@ApiMethod(name = "users", httpMethod = ApiMethod.HttpMethod.GET, path="users")
	public List<Utilisateur> utilisateurs() {
		ofy().clear();
		List<Utilisateur> users = ofy().load().type(Utilisateur.class).list();
    	return users;
    }
	
	@ApiMethod(name = "create_tweet", httpMethod = ApiMethod.HttpMethod.POST, path="tweets/create")
	public Tweet createTweet(@Named("authorID")Long userID, 
							@Named("authorUsername") String username, 
							@Named("message") String message) {
		Tweet tweet = new Tweet(userID, username, message);
		
		
		List<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile("\\#(\\S+)");
        Matcher m = p.matcher(message);
        while(m.find())
        {
            result.add(m.group(1));
        }
        
        
		System.out.println(result);
		ofy().clear();
		ofy().save().entity(tweet).now();
    	return tweet;
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
		System.out.println(user);
		Key<Utilisateur> cleFollower = Key.create(Utilisateur.class, followID);
		Utilisateur follower = ofy().load().key(cleFollower).now();
		follower.addFollower(userID);
		System.out.println(user);
		ofy().save().entity(user).now();
		ofy().save().entity(follower).now();
    	return user;
    }
	
	@ApiMethod(name = "timeline", httpMethod = ApiMethod.HttpMethod.POST, path="users/{userID}/timeline")
	public List<Tweet> timeline(@Named("userID")Long userID) {
		ofy().clear();		
		Key<Utilisateur> cleUser = Key.create(Utilisateur.class, userID);
		Utilisateur user = ofy().load().key(cleUser).now();
		Set<Long> abonnes = user.getAbonements();
		Set<Long> tweets = new HashSet<Long>();
		for(Long l : abonnes) {
			Key<Utilisateur> cle = Key.create(Utilisateur.class, l);
			Utilisateur u = ofy().load().key(cle).now();
			tweets.addAll(u.getMytweets());
		}
		tweets.addAll(user.getMytweets());
		
		List<Tweet> lst = new ArrayList<Tweet>();
		for(Long l : tweets) {
			lst.add(ofy().load().key(Key.create(Tweet.class, l)).now());
		}
    	return lst;
    }
}
