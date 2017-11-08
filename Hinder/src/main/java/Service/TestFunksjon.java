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
import static java.lang.System.out;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    Message msg;
    User usr;

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
    @GET
    @Path("user")
    public List<User> getUser() {

        List<User> result = null;
        result = (List) em.createQuery("SELECT u from User u", User.class).getResultList();
        return result;
    }

    @Path("message")
    @GET
    public List<Message> getMessage() {
        msg = new Message();
        em.persist(msg);
        List<Message> result = null;
        result = (List) em.createQuery("SELECT m FROM Message m", Message.class).getResultList();
        return result;

    }

    @Path("login")
    @GET

    public List<User> login(@QueryParam("name") String name, @QueryParam("pass") String password) {
        List<User> result = null;

        if (name != null || password != null || checkUser(name)) {
            List<User> users = getUser();
            for (User u : users) {
                if (u.getName().equals(name)) {
                    return Collections.EMPTY_LIST;
                }
            }

            result = (List) em.createQuery("SELECT u FROM User u"
                    + " Where u.name = :nameParam AND u.password = :passParam", Message.class).setParameter("nameParam", name).setParameter("passParam", password).
                    getResultList();

        }
        return result;

    }
     @Path("register")
    @GET
    public User addUser(@QueryParam("name") String name, @QueryParam("pass") String password, @QueryParam("lat") int lat,
            @QueryParam("long") int lon) {
        if (checkUser(name)) {
            if (name != null || password != null) {

                User u = new User();
                u.setName(name);
                u.setPassword(password);
                Location l = new Location(lat, lon);
                em.persist(l);
                u.setLocation(l.getId());
                em.persist(u);
                return u;
            }
        }

        return null;
    }

   

    @Path("createCon")
    @GET
    public Conversation createConversation(@QueryParam("name1") String name1, @QueryParam("name2") String name2) {
        
        if(!checkUser(name1)||!checkUser(name2)){
            User a=getUser(name1);
            User b=getUser(name2);
            Conversation c=new Conversation();
            addConversation(a,c);
            addConversation(b,c);
            em.persist(c);
            
            
            return c;
        }
        return null;
            
    }
    
    
    private User getUser(String name){
         return em.createQuery("SELECT u FROM User u WHERE u.name= :nameParam", User.class).setParameter("nameParam", name).getSingleResult();
    }
    
    private void addConversation(User u,Conversation c){
                u.addConvo(c);
    }

    public Boolean checkUser(String name) {

        List<User> users = getUser();
        for (User u : users) {
            if (u.getName().equals(name)) {
                return false;
            }
        }
        return true;

    }

}
