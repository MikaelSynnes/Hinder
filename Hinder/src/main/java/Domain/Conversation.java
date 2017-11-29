package Domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.NoArgsConstructor;
import Domain.Message;
import java.util.ArrayList;
import javax.persistence.GeneratedValue;

/**
 *
 * @author mikael
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Conversation implements Serializable {

    @Id
    @GeneratedValue
    String id;

    @XmlTransient
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Message> messages;
    
   
    @OneToMany
            
    List<CloseUser> usernames;

    @Version
    Timestamp version;

    public Conversation() {
       
       
    }



    public String getId() {
        return id;
    }
    public void addtoUserNames(CloseUser u){
        usernames.add(u);
    }
    
    public void setUsernames(List<CloseUser> i)
    {
        usernames=i;
    }
    public List<CloseUser> getUsernames(){
        return usernames;
    }

}
