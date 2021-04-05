package xyz.kotlout.kotlout.controller;

import android.util.Log;
import androidx.core.util.Consumer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.List;
import xyz.kotlout.kotlout.model.user.User;

/**
 * A Controller class used to interface with a user given their userId.
 */
public class UserController {

  public static final String USER_COLLECTION = "users";
  private static final String TAG = "USER CONTROLLER";

  private final DocumentReference userDoc;
  private ListenerRegistration snapshotListenerRef;
  private Consumer<User> updateCallback;
  private User user;

  /**
   * Creates a UserController with a Firestore Document Reference a given user doc
   *
   * @param userId Id of the userdoc to reference with this controller
   */
  public UserController(String userId) {
    if (userId == null) {
      Log.e(TAG, "Error no uuid provided, using local uuid");
      userId = UserHelper.readUuid();
    }
    // Set user to garuntee UUID is valid if firebase is unable to fetch data
    user = new User();
    user.setUuid(userId);
    userDoc = FirebaseController.getFirestore().collection(USER_COLLECTION).document(userId);
    registerSnapshotListener();
  }

  /**
   * Creates a snapshot listener for the userdoc iff snapshotListenerRef does not already have a listener. The new listener is
   * stored in snapshotListenerRef Unregister this listener with unregisterSnapshotListener when finished using
   * unregisterSnapshotListener()
   */
  public void registerSnapshotListener() {
    if (snapshotListenerRef == null) {
      snapshotListenerRef =
          userDoc.addSnapshotListener((value, error) -> {
            Log.d(TAG, "Firebase event");
            if (error != null) {
              Log.d(TAG, "Firebase Error: " + error.getMessage());
              return;
            }
            if (value != null && value.exists()) {
              Log.d(TAG, "FOUND VALID UPDATE");
              User newUser = value.toObject(User.class);
              updateCallback.accept(newUser);
              onUserUpdate(newUser);
            } else {
              Log.d(TAG, "no update");
            }
          });
    }
  }

  /**
   * Unregisters snapshotListenerRef and sets it to null Should be called once listener is no longer needed
   */
  public void unregisterSnapshotListener() {
    if (snapshotListenerRef != null) {
      snapshotListenerRef.remove();
    }
    snapshotListenerRef = null;
  }

  /**
   * Local callback for user updates Currently just updates the locally stored user
   *
   * @param newUser userData from firestore update
   */
  private void onUserUpdate(User newUser) {
    this.user = newUser;
  }

  private void syncUser() {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    firestore.collection(USER_COLLECTION).document(this.user.getUuid()).set(this.user);
  }

  public void updateUser(User user) {
    if (this.user.getUuid().equals(user.getUuid())) {
      this.user = user; //TODO: This probably needs changed for only updating say user info
      syncUser();
    } else {
      throw new RuntimeException("UUIDs don't match to update user");
    }
  }

  /**
   * Sets the callback to be run when user document changes
   *
   * @param updateCallback Callback function that will run on every User data update
   */
  public void setUpdateCallback(Consumer<User> updateCallback) {
    this.updateCallback = updateCallback;
  }

  public void updateUserData(User newData) {
    userDoc.set(newData);
  }

  public User getUser() {
    return user;
  }

  /**
   * Checks if the controlled user is the same as the device user
   *
   * @return True if the given user is the same as the device user, False otherwise
   */
  public boolean isCurrentUser() {
    return this.user.getUuid().equals(UserHelper.readUuid());
  }

  public DocumentReference getUserDoc() {
    return userDoc;
  }

  /**
   * Subscribe to an experiment. Does nothing if the user is already subscribed to the given experiment.
   *
   * @param experimentId A string ID for the experiment.
   */
  public void addSubscription(String experimentId) {
    userDoc.get().addOnSuccessListener(documentSnapshot -> {
          User currentUser = documentSnapshot.toObject(User.class);
          List<String> subscriptions = currentUser.getSubscriptions();

          // Only subscribe to experiments the user isn't already subscribed to
          if (subscriptions.contains(experimentId)) {
            return;
          }
          subscriptions.add(experimentId);
          this.user.setSubscriptions(subscriptions);
          syncUser();
        }
    ).addOnFailureListener(e -> Log.e(TAG, "addSubscription: Could not subscribe to experiment with ID " + experimentId));
  }

  public void fetchSubscriptions() {


  }
}
