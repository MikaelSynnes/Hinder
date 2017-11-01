/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Mikael
 */

@Data @NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity

public class Location implements Serializable {
    @Id @GeneratedValue
    protected long id;
    int latitude;
    int longitude;
    
    
    public Location(int latitude, int longitude){
        this.latitude=latitude;
        this.longitude=longitude;
     
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
    
}
