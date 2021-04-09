package xyz.kotlout.kotlout.controller;

import android.content.Context;
import android.provider.Settings.Secure;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentReference;
import java.util.regex.Pattern;
import xyz.kotlout.kotlout.model.user.User;

/**
 * The user helper function class
 */
public final class UserHelper {

  /**
   * Firestore collection name
   */
  public static final String UUID_INTENT = "uuid";
  /**
   * Pattern to validate email addresses </br> Regex from: https://emailregex.com, Accessed on * Friday March 5th 2021
   */
  private static final Pattern EMAIL_REGEX = Pattern.compile(
      "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
  );
  /**
   * Pattern to validate phone numbers </br> Notation taken from Figma diagram: 000-111-2222
   */
  private static final Pattern PHONE_REGEX = Pattern.compile("^[\\d\\-\\+\\(\\)]*$");
  private static String LOCAL_USER_UUID;

  /**
   * Validate email boolean.
   *
   * @param email the email
   * @return the boolean
   */
  public static boolean validateEmail(String email) {
    return email == null || email.isEmpty() || EMAIL_REGEX.matcher(email).matches();
  }

  /**
   * Validate phone number boolean.
   *
   * @param phoneNumber the phone number
   * @return the boolean
   */
  public static boolean validatePhoneNumber(String phoneNumber) {
    return phoneNumber == null || phoneNumber.isEmpty() || PHONE_REGEX.matcher(phoneNumber).matches();
  }

  /**
   * Sets info.
   *
   * @param user      the user
   * @param firstName the first name
   * @param lastName  the last name
   * @param email     the email
   * @param phone     the phone
   */
  public static void setInfo(User user, String firstName, String lastName, String email, String phone) {
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPhoneNumber(phone);
  }

  public static void initUserHelper(@NonNull Context context) {
    LOCAL_USER_UUID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
  }

  /**
   * Read uuid from storage
   *
   * @return UUID stored in internal storage
   */
  public static String readUuid() {
    return LOCAL_USER_UUID;
  }

  /**
   * Initialize user id locally and add user to firebase if it does not exist
   */
  public static void initializeUser() {
    User user = new User();
    user.setUuid(readUuid());
    DocumentReference ref = FirebaseController.getFirestore().collection(FirebaseController.USER_COLLECTION)
        .document(UserHelper.readUuid());
    ref.get().addOnCompleteListener(task -> {
      if (!task.isSuccessful() || task.getResult().get("uuid") == null) {
        ref.set(user);
      }
    });
  }

}
