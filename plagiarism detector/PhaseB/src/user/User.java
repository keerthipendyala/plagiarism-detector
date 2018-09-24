package user;

import java.util.Date;

/**
 * This interface has the methods and properties that defines and maintains a person's basic details
 * and his/her role in this CopyCatch project.
 *
 * @author harip
 */
public class User {
    public String firstName;  // represents the firstName of the User
    public String lastName;   // represents the LastName of the User
    public String userName;      // represents the userName that he/she created while registering/ signing up in the Copycatch website
    public String password;   // represents the password that he/she created while registering/ signing up in the Copycatch website
    public Role role;         // represents the role of this User, which takes one of the value from the Role Enum Class
    public Date DateOfBirth;  // represents the birth date of this User
    public Address address;   // represents the updated address details of this User, referred from address class
    public Phone phone;       // represents all the phone number details of this User, referred from Phone class
}
