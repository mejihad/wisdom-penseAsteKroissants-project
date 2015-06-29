package fr.astek.pac.dtos;

/**
 * Created by jmejdoub on 03/04/2015.
 */
public class Organizer {

    protected String astekianId ;

    protected String firstName;

    protected String lastName;

    protected String email;

    public Organizer() {
    }

    /**
     *
     * @param astekianId
     * @param firstName
     * @param lastName
     * @param email
     */
    public Organizer(String astekianId, String firstName, String lastName, String email) {
        this.astekianId = astekianId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getAstekianId() {
        return astekianId;
    }

    public void setAstekianId(String astekianId) {
        this.astekianId = astekianId;
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
}
