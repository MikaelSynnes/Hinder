package Service;

import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
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
import Domain.Conversation;
import Domain.Message;
import Domain.User;

/**
 *
 * @author mikael
 */
@Stateless
@Path("messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ForumServices {

    @PersistenceContext
    EntityManager em;

    @GET
    public List<Message> getMessages(@QueryParam("name") String name) {
        List<Message> result = null;
        if (name != null) {
            result = em.createQuery("SELECT m FROM Message m WHERE m.conversation.id = :id",
                    Message.class)
                    .setParameter("id", name)
                    .getResultList();
        }

        return result != null ? result : Collections.EMPTY_LIST;
    }

    @POST
    @Path("add")
    public Response addMessage(@QueryParam("name") String name, Message message) {
        if (name != null) {
            Conversation c = em.find(Conversation.class, name);
            if (c == null) {
                c = new Conversation(name);
                em.persist(c);
            }
            message.setConversation(c);
            em.persist(message);

            return Response.ok(message).build();
        } else {
            return Response.noContent().build();
        }
    }

        @Path("conversation")
    @GET
    public List<Conversation> getConversation() {
        List<Conversation> result = null;

        result = em.createQuery("FROM Conversation c",
                Conversation.class)
                .getResultList();

        return result != null ? result : Collections.EMPTY_LIST;
    }

    @Path("test")
        @GET
    public  List<Conversation> getTest() {

        

     
            List<Conversation> result = null;

            Conversation c = new Conversation(new User("mikael"), new User("Mikael2"));
            em.persist(c);
            result = em.createQuery("FROM Conversation c",
                    Conversation.class)
                    .getResultList();

            return result != null ? result : Collections.EMPTY_LIST;
        }

    
}
