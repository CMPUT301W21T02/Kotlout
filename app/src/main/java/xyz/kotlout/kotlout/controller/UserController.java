package xyz.kotlout.kotlout.controller;

import android.util.Log;
import androidx.core.util.Consumer;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.regex.Pattern;
import xyz.kotlout.kotlout.model.user.User;

public class UserController {
  private static final String USER_COLLECTION = "users";
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
    userDoc = FirebaseController.getFirestore().collection(USER_COLLECTION).document(userId);
    registerSnapshotListener();
  }

  /**
   * Creates a snapshot listener for the userdoc iff snapshotListenerRef does not already have a
   * listener. The new listener is stored in snapshotListenerRef Unregister this listener with
   * unregisterSnapshotListener when finished using unregisterSnapshotListener()
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
   * Unregisters snapshotListenerRef and sets it to null Should be called once listener is no longer
   * needed
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
}
