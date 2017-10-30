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

/**
 *
 * @author mikael
 */
@Data
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Conversation implements Serializable {


    @Id
    String id;
    String userA;
    String userB;
    @XmlTransient
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Message> messages;

    @Version
    Timestamp version;

    public Conversation(User A, User B) {
        userA = A.getName();
        userB = B.getName();
        id=userA+userB;
    }

    public Conversation(String name) {
       id=name;
    }
}
