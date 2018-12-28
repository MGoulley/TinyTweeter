package tinytweet;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.ObjectifyService;

import tinytweet.Utilisateur;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

@Api(name = "tinytweeter")
public class TinytweeterEndpoint {
	static {
        ObjectifyService.register(Utilisateur.class); // Fait connaître votre classe-entité à Objectify
    }
	
	@ApiMethod(name = "create_user", httpMethod = ApiMethod.HttpMethod.GET, path="users/create")
    public Utilisateur createUtilisateur(@Named("username")String username) {
    	Utilisateur user = new Utilisateur(username);
    	ofy().save().entity(user).now();
    	return user;
    }
	
	@ApiMethod(name = "users", httpMethod = ApiMethod.HttpMethod.GET, path="users/")
	public List<Utilisateur> utilisateurs() {
    	return ofy().load().type(Utilisateur.class).list();
    }
}
