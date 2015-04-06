package fr.astek.pac.dtos;

/**
 * Created by jmejdoub on 03/04/2015.
 */
public class Organizer {

    protected String astekianId ;

    protected String firstName;

    protected String lastName;

    protected String thumbnailPath;

    public Organizer() {
    }

    /**
     *
     * @param astekianId
     * @param firstName
     * @param lastName
     * @param thumbnailPath
     */
    public Organizer(String astekianId, String firstName, String lastName, String thumbnailPath) {
        this.astekianId = astekianId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thumbnailPath = thumbnailPath;
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

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
