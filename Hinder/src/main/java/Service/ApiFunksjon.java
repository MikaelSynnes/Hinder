/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Domain.Close;
import Domain.CloseUser;
import Domain.Conversation;

import Domain.Location;
import Domain.Message;
import Domain.User;
import static java.lang.System.out;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
public class ApiFunksjon {

    Location loc;
    @PersistenceContext
    EntityManager em;
    Message msg;
    User usr;
    private static final String SALT = "Mikael";
    public static DistanceCalc dist;

    //Test for uthenting av Location
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

    // Test for uthenting av alle brukere.
    @GET
    @Path("user")
    public List<User> getUser() {

        List<User> result = null;
        result = (List) em.createQuery("SELECT u from User u", User.class).getResultList();
        return result;
    }

// Testfunksjon for å teste uthenting av Conversation.
    @Path("test")
    @GET
    public List<Conversation> getTest() {
        List<Conversation> result = null;

        Conversation c = new Conversation();
        em.persist(c);
        result = em.createQuery("FROM Conversation c",
                Conversation.class)
                .getResultList();

        return result != null ? result : Collections.EMPTY_LIST;
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

    //Henter ut brukere i nærheten basert på lat lon. 
    @Path("getclose")
    @GET
    public List<Close> getClose(@QueryParam("id") String i) {
        List<Close> c;
        dist = new DistanceCalc();
        ArrayList close = new ArrayList();
        Location myLoc = getUserLocation(i);

        List<User> u = getUser();

        // Henter ut alle brukere for å sjekke om de ligger innenfor 120 Km.
        // Bruker en sjekk for å hindre at bruker selv ikke blir lagt til i listen
        for (User us : u) {
            Location userLoc = getLocation(us.getLocation());
            double dis = dist.getDistance(myLoc, userLoc);
            if (dis < 120.0 && userLoc.getId() != myLoc.getId()) {
                Close cl = new Close();
                cl.setDistance(dis);
                cl.setName(us.getName());
                close.add(cl);
            }

        }
        c = close;

        return c;
    }

    //API som brukes for å logge inn bruker, oppdaterer location user når bruker logger inn.
    @Path("login")
    @GET

    public User login(@QueryParam("name") String name, @QueryParam("pass") String password) {
        if (name != null || password != null || checkUser(name)) {
            password = generateHash(password + SALT);
            User u = em.createQuery("SELECT u FROM User u"
                    + " Where u.name = :nameParam AND u.password = :passParam", User.class).setParameter("nameParam", name).setParameter("passParam", password).
                    getSingleResult();
            return u;

        } else {
            return null;
        }

    }

    //API for å bestemme location til bruker.
    @Path("setloc")
    @GET
    public Location setLocation(@QueryParam("name") String name, @QueryParam("lat") double lat, @QueryParam("long") double lon) {
        if (!checkUser(name)) {

            User u = getUser(name);
            Location l = getLocation(u.getLocation());
            l.setLatitude(lat);
            l.setLongitude(lon);

            u.setLocation(l.getId());

            //em.createQuery("UPDATE User set Location= :iparam WHERE name= :nameParam").setParameter("nameParam", name).setParameter("iParam", i);
            return l;
        } else {
            return new Location();
        }
    }

    //Henter ut location fra Id.
    @Path("loc")
    @GET
    public Location getLocation(@QueryParam("id") long i) {
        try {
            return em.createQuery("SELECT l FROM Location l WHERE l.id = :idParam", Location.class).setParameter("idParam", i).getSingleResult();
        } catch (Exception e) {
            Location l = new Location();
            em.persist(l);
            return l;

        }

    }

    //Henter ut location fra brukernavn.
    @Path("userloc")
    @GET
    public Location getUserLocation(@QueryParam("name") String i) {
        long result = (long) em.createQuery("SELECT u.location from User u Where u.name=:nameParam").setParameter("nameParam", i).getSingleResult();
        return getLocation((int) result);
    }

    //Registrer bruker i databasen og setter location til nåværende location til bruker.
    @Path("register")
    @GET
    public User addUser(@QueryParam("name") String name, @QueryParam("pass") String password, @QueryParam("lat") long lat,
            @QueryParam("long") long lon) {
        if (checkUser(name)) {
            if (name != null || password != null) {
                password = generateHash(password + SALT);
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

    // Henter ut brukere i n
    @Path("getConv")
    @GET
    public List<CloseUser> getConv(@QueryParam("name") String name) {
        List<CloseUser> users = new ArrayList<>();
        List<Conversation> convos = null;

        User user = getUser(name);
        convos = user.getConversations();
        for (Conversation convo : convos) {
            List<CloseUser> close = convo.getUsernames();
            for (CloseUser c : close) {
                if (c.getName() == null ? name != null : !c.getName().equals(name)) {
                    users.add(c);
                }
            }
        }
        return users;

    }

    //Henter ut Conversations som tilhører bruker. (Chatter allerede startet)
    public List<Conversation> getCon(@QueryParam("name") String name) {
        List<CloseUser> users = new ArrayList<>();
        List<Conversation> convos = null;

        User user = getUser(name);
        convos = user.getConversations();
        System.out.println(convos.toString());

        for (Conversation convo : convos) {
            System.out.println(convo.getId());
            List<CloseUser> close = convo.getUsernames();
            for (CloseUser c : close) {
                if (c.getName() == null ? name != null : !c.getName().equals(name)) {
                    users.add(c);
                }
            }

        }
        return convos;
    }

    //Sjekker om Samtalen allerede er startet med person.
    
    public Conversation checkIfConvoExist(String u, String i) {
        List<Conversation> convos = getCon(u);
        for (Conversation c : convos) {
            List<CloseUser> close = c.getUsernames();

            if (close.get(0).getName().equals(u) && close.get(1).getName().equals(i)) {
                return c;
            } else if (close.get(0).getName().equals(i) && close.get(1).getName().equals(u)) {
                return c;
            }

        }
        return null;

    }
    //Lager en ny samtale mellom to brukere.
    @Path("createCon")
    @GET
    public Conversation createConversation(@QueryParam("name1") String name1, @QueryParam("name2") String name2) {
        Conversation c;
        if (!checkUser(name1) || !checkUser(name2)) {
            User a = getUser(name1);
            User b = getUser(name2);
            try {
                c = checkIfConvoExist(name1, name2);
            } catch (Exception e) {
                c = new Conversation();
                em.persist(c);
            }
            if (c == null) {
                c = new Conversation();
                em.persist(c);
            }

            addConversation(a, b, c);
            addConversation(b, a, c);

            return c;
        }
        return null;

    }

    private Conversation getConversation(String i) {
        try {
            return em.createQuery("SELECT c FROM Conversation c WHERE c.id = :idParam", Conversation.class).setParameter("idParam", i).getSingleResult();
        } catch (Exception e) {
            return null;

        }
    }
    //Henter ut bruker fra String.
    private User getUser(String name) {
        return em.createQuery("SELECT u FROM User u WHERE u.name= :nameParam", User.class).setParameter("nameParam", name).getSingleResult();
    }

    private void addConversation(User u, User b, Conversation c) {
        u.addConvo(c);
        dist = new DistanceCalc();
        CloseUser user = new CloseUser();
        user.setName(u.getName());
        double distance = dist.getDistance(getLocation(u.getLocation()), getLocation(b.getLocation()));
        user.setDistance(distance);
        em.persist(user);
        c.addtoUserNames(user);
    }

    
       //Sjekker om bruker eksisterer.
    public Boolean checkUser(String name) {

        List<User> users = getUser();
        for (User u : users) {
            if (u.getName().equals(name)) {
                return false;
            }
        }
        return true;

    }
    //Hasher passord før det sendes inn i databasen.
    public static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

}
