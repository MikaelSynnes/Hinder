/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Domain.Conversation;
import Domain.Location;
import Domain.Message;
import Domain.User;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Mikael
 */
@Stateless
@Path("test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestFunksjon {

    Location loc;
    @PersistenceContext
    EntityManager em;

   
    /*@GET
    public Location getLocation() {
        loc = new Location(100, 12);
        em.persist(loc);
        Location result = null;
        result = (Location) em.createQuery("SELECT l FROM Location l",
                Location.class)
                .getSingleResult();
        
        return result;
    }*/
     @GET
    public List<Location> getLocation() {
        loc = new Location(100, 12);
        em.persist(loc);
        List<Location> result = null;
        result = (List) em.createQuery("SELECT l FROM Location l",
                Location.class)
                .getResultList();
        
        return result;
    }

    public void createValues() {

        loc = new Location(100, 12);
        em.persist(loc);

    }

}
