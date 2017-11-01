/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Mikael
 */

@Entity
@Table(name = "HUSER")
public class User implements Serializable {
     
    @Id
    String name;
    String location;
    String password;

    public User() {
    }

    
    public User(String n) {
        name=n;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

  
            
    
}
