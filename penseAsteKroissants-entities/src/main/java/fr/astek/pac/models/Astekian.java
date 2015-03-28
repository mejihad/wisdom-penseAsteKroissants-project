/**
 * 
 */
package fr.astek.pac.models;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;


public class Astekian {

    @Id
    @ObjectId
    protected String id;

    protected String firstName;

    protected String lastName;

    protected String email;

    protected boolean isInMission;

    protected String password;

    protected long order;

    public Astekian() {
    }

    /**
     * @param firstName
     * @param lastName
     * @param email
     * @param isInMission
     * @param password
     */
    public Astekian(String firstName, String lastName, String email, boolean isInMission,
            String password, long order) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isInMission = isInMission;
        this.password = password;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public boolean isInMission() {

        return isInMission;
    }

    public void setInMission(boolean isInMission) {

        this.isInMission = isInMission;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order) {

        this.order = order;
    }


}
