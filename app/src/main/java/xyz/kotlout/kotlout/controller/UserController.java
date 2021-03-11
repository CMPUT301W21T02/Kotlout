package xyz.kotlout.kotlout.controller;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.regex.Pattern;
import xyz.kotlout.kotlout.model.user.User;

public class UserController {
  private static final String USER_COLLECTION = "users";
  /**
   * Pattern to validate email addresses </br> Regex from: https://emailregex.com, Accessed on
   * Friday March 5th 2021
   */
  private static final Pattern EMAIL_REGEX = Pattern.compile(
      "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
  );

  /**
   * Pattern to validate email addresses </br> Notation taken from Figma diagram: 000-111-2222
   */
  private static final Pattern PHONE_REGEX = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");

  private User user;

  private void syncUser() {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    firestore.collection(USER_COLLECTION).document(this.user.getUuid()).set(this.user);
  }

  public static boolean validateEmail(String email) {
    return EMAIL_REGEX.matcher(email).matches();
  }

  public static boolean validatePhoneNumber(String phoneNumber) {
    return PHONE_REGEX.matcher(phoneNumber).matches();
  }

  public static void registerUser(String userUUID) {
    if (fetchUser(userUUID) == null) {
      User newUser = new User();
      newUser.setUuid(userUUID);
      FirebaseFirestore firestore = FirebaseController.getFirestore();
      firestore.collection(USER_COLLECTION).document(newUser.getUuid()).set(newUser);
    } else {
      throw new RuntimeException("User already registered!");
    }
  }

  public static User fetchUser(String userUUID) {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    final CollectionReference collection = firestore.collection(USER_COLLECTION);
    final User[] readUser = {null};
    collection.document(userUUID).get().addOnCompleteListener(snapshot -> {
      if (snapshot.isSuccessful()) {
        DocumentSnapshot document = snapshot.getResult();
        if (document.exists()) {
          Log.d("FIRESTORE", "Document snapshot: " + document.getData());
          readUser[0] = (User) document.toObject(User.class);
        } else {
          Log.d("FIRESTORE", "Document snapshot not found");
        }
      } else {
        Log.d("FIRESTORE", "Get failed with: " + snapshot.getException());
      }
    });
    return readUser[0];
  }

  public void updateUser(User user) {
    if (this.user.getUuid() == user.getUuid()) {
      this.user = user; //TODO: This probably needs changed for only updating say user info
      syncUser();
    } else {
      throw new RuntimeException("UUIDs don't match to update user");
    }
  }

  public User getUser() {
    return user;
  }
}
