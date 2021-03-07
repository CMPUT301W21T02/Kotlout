package xyz.kotlout.kotlout.model.user;

import androidx.core.app.RemoteInput.EditChoicesBeforeSending;
import com.google.firebase.firestore.Exclude;
import java.util.List;
import java.util.UUID;

import xyz.kotlout.kotlout.model.experiment.Experiment;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    // UUID doens't have a no-argument constructor and cannot be deserialized by firestore
    // Maybe we should consider using Firebase ID's instead
    @Exclude
    private UUID uuid;
    @Exclude
    private List<Experiment> subscriptions;

    // Required for Firebase Serialization
    public User() {}

    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.uuid = UUID.randomUUID();
    }

    @Exclude
    public UUID getUuid() {
        return uuid;
    }

    @Exclude
    public void setUuid(UUID uuid) {
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
