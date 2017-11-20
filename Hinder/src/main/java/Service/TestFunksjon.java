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
     @Path("test")
        @GET
    public  List<Conversation> getTest() {

        

     
            List<Conversation> result = null;

            Conversation c = new Conversation();
            em.persist(c);
            result = em.createQuery("FROM Conversation c",
                    Conversation.class)
                    .getResultList();

            return result != null ? result : Collections.EMPTY_LIST;
        }


    @Path("login")
    @GET

    public User login(@QueryParam("name") String name, @QueryParam("pass") String password) {
  

        if (name != null || password != null || checkUser(name)) {
 

           User u = em.createQuery("SELECT u FROM User u"
                    + " Where u.name = :nameParam AND u.password = :passParam", User.class).setParameter("nameParam", name).setParameter("passParam", password).
                    getSingleResult();
               return u;

        }
        else{
            return null;
        }

    }
    
   
        @Path("setloc")
    @GET
    public Location setLocation(@QueryParam("name")String name, @QueryParam("lat")long lat,@QueryParam("long")long lon){
        if(!checkUser(name)){
         
        User u=getUser(name);
        Location l=new Location(lat,lon);
        em.persist(l);
        long i=l.getId();
        u.setLocation(l.getId());
        
        //em.createQuery("UPDATE User set Location= :iparam WHERE name= :nameParam").setParameter("nameParam", name).setParameter("iParam", i);
               
            
        return l;
            }else{
        return new Location();
        }
    }
    
    @Path("loc")
    @GET
    public Location getLocation(@QueryParam("id")int i){
        return em.createQuery("SELECT l FROM Location l WHERE l.id = :idParam",Location.class).setParameter("idParam", i).getSingleResult();
        
    }
    @Path("userloc")
    @GET
    public Location getUserlocation(@QueryParam("name")String i){
        long result=(long)em.createQuery("SELECT u.location from User u Where u.name=:nameParam").setParameter("nameParam", i).getSingleResult();
      return getLocation((int) result);
    }
    
     @Path("register")  
    @GET
    public User addUser(@QueryParam("name") String name, @QueryParam("pass") String password, @QueryParam("lat") long lat,
            @QueryParam("long") long lon) {
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
    
    
    @Path("getConv")
    @GET
    public List<Conversation> getConv(@QueryParam("name") String name){
        
        List<Conversation> convos=null;
        try{
              User user =getUser(name);
               return user.getConversations();
        }catch(Exception e){
            return Collections.EMPTY_LIST;
        }
     
        
        
        
        
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
