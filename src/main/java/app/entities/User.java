package app.entities;

/**
 * Purpose:
 *
 * @author: Jeppe Koch
 */
public class User {
    private int userId;
    private String email;
    private String password;
    private String role;
    private int contactIdFk;

    public User(int userId, String email, String password, String role, int contactIdFk) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contactIdFk = contactIdFk;
    }

    public User(int userId, String email, String password, String role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getContactIdFk() {
        return contactIdFk;
    }


}
