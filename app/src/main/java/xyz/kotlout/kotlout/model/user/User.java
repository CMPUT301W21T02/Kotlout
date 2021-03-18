package xyz.kotlout.kotlout.model.user;

import com.google.firebase.firestore.Exclude;
import java.util.List;
import xyz.kotlout.kotlout.model.experiment.Experiment;

public class User {

  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  // UUID doesn't have a no-argument constructor and cannot be deserialized by firestore
  // Maybe we should consider using Firebase ID's instead
  private String uuid;
  @Exclude
  private List<Experiment> subscriptions;

  // Required for Firebase Serialization
  public User() {
  }

  public User(String firstName, String lastName, String email, String phoneNumber, String uuid) {
    // If the User is initialized with empty Strings, those fields should be set to null
    if (firstName.equals("")) {
      firstName = null;
    }
    if (lastName.equals("")) {
      lastName = null;
    }
    if (email.equals("")) {
      email = null;
    }
    if (phoneNumber.equals("")) {
      phoneNumber = null;
    }
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.uuid = uuid;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Exclude
  public List<Experiment> getSubscriptions() {
    return subscriptions;
  }

}
