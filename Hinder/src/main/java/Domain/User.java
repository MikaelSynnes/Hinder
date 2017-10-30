/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Mikael
 */

@Entity
public class User {
     
    @Id
    String name;
    String location;
    String password;

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
