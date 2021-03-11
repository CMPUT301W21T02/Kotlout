package xyz.kotlout.kotlout.controller;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.regex.Pattern;
import xyz.kotlout.kotlout.model.user.User;

public class UserController {

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

  public static boolean validateEmail(String email) {
    return EMAIL_REGEX.matcher(email).matches();
  }

  public static boolean validatePhoneNumber(String phoneNumber) {
    return PHONE_REGEX.matcher(phoneNumber).matches();
  }

  public User getUser() {
    return user;
  }

  public static void setInfo(User user, String firstName, String lastName, String email, String phone) {
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPhoneNumber(phone);
  }

  public static User fetchUser(String docName, FirebaseFirestore firestore) {
    final User[] user = {new User()};
    firestore.collection("users").document(docName).get().addOnCompleteListener(
        new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
              DocumentSnapshot documentSnapshot = task.getResult();
              if (documentSnapshot.exists()) {
                user[0] = documentSnapshot.toObject(User.class);
              }
            }
          }
        });
    return user[0];
  }
}
