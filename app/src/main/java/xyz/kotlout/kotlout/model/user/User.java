package xyz.kotlout.kotlout.model.user;

import com.google.firebase.firestore.Exclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a single user of the app. As there is no way to make accounts, this also corresponds directly to a
 * device on which the app is installed.
 */
public class User implements Serializable {

  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private List<String> subscriptions;

  // UUID doesn't have a no-argument constructor and cannot be deserialized by firestore
  // Maybe we should consider using Firebase ID's instead
  private String uuid;

  // Required for Firebase Serialization
  public User() {
  }

  public User(String firstName, String lastName, String email, String phoneNumber, String uuid) {
    // If the User is initialized with empty Strings, those fields should be set to null
    if (firstName.isEmpty()) {
      firstName = null;
    }
    if (lastName.isEmpty()) {
      lastName = null;
    }
    if (email.isEmpty()) {
      email = null;
    }
    if (phoneNumber.isEmpty()) {
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
  public String getDisplayName() {
    String displayName;
    if (getFirstName() != null) {
      displayName = getFirstName();
    } else {
      displayName = getUuid();
    }
    return displayName;
  }

  /**
   * Gets a list of IDs for experiments the user has subscribed to.
   *
   * @return A list of experiment IDs.
   */
  public List<String> getSubscriptions() {
    if (subscriptions == null) {
      subscriptions = new ArrayList<>();
    }
    return subscriptions;
  }

  /**
   * Sets the user subscriptions to a new list.
   *
   * @param subscriptions A list of experiment IDs.
   */
  public void setSubscriptions(List<String> subscriptions) {
    this.subscriptions = subscriptions;
  }
}
