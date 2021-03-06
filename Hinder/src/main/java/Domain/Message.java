package Domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author mikael
 */
@Data @NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Message implements Serializable {
    @Id @GeneratedValue
    Long id;
    
    @Column(name = "name")
    String name;
    String text;
    
    @Version
    Timestamp version;
    
    @XmlTransient
    @ManyToOne(optional = false,cascade = CascadeType.PERSIST)
    Conversation conversation;

    public Message(String name, String text) {
        this.name = name;
        this.text = text;
    }
}
