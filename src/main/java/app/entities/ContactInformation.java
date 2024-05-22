package app.entities;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class ContactInformation {
    private int contactId;
    private String name;
    private String streetName;
    private String houseNumber;
    private int floor;
    private String email;
    private int phoneNumber;
    private int zipCodeFk;

    public ContactInformation(int contactId, String name, String streetName, String houseNumber, int floor, String email, int phoneNumber, int zipCodeFk) {
        this.contactId = contactId;
        this.name = name;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.floor = floor;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.zipCodeFk = zipCodeFk;
    }

    public ContactInformation(String name, int zipCodeFk) {
        this.name = name;
        this.zipCodeFk = zipCodeFk;
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public int getFloor() {
        return floor;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public int getZipCodeFk() {
        return zipCodeFk;
    }
}
