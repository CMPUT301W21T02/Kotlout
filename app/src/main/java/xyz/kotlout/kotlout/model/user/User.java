package xyz.kotlout.kotlout.model.user;

import com.google.firebase.firestore.Exclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a single user of the app. As there is
 * no way to make accounts, this also corresponds directly to
 * a device on which the app is installed.
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

  /**
   * Empty user constructor for Firebase
   */
  public User() {
  }

  /**
   * Instantiates a new User with information about that user.
   * All fields can optionally be set to null except uuid.
   *
   * @param firstName   the user's first name
   * @param lastName    the user's last name
   * @param email       the user's email
   * @param phoneNumber the user's phone number
   * @param uuid        the uuid to assign to the user
   */
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

  /**
   * Gets the uuid of the user.
   *
   * @return the user uuid
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * Sets the uuid of the user.
   *
   * @param uuid the new user uuid
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Gets the first name of the user.
   *
   * @return the users first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of the user.
   *
   * @param firstName the new first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name of the user.
   *
   * @return the current last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of the user.
   *
   * @param lastName the new last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the email of the user.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of the user.
   *
   * @param email the new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets phone number of the user.
   *
   * @return the phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets phone number of the user.
   *
   * @param phoneNumber the new phone number
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Gets the user display name.
   *
   * @return the display name
   */
  @Exclude
  public String getDisplayName() {
    String displayName;
    if (getFirstName() != null) {
      if (getLastName() != null) {
        displayName = String.format("%s %s", getFirstName(), getLastName());
      } else {
        displayName = getFirstName();
      }
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
