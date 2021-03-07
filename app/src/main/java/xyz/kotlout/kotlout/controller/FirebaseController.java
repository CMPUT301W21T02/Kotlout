package xyz.kotlout.kotlout.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Controls firebase services and connection to emulator/production
 */
public class FirebaseController {

  private static final boolean USE_EMU = true;

  private static final String EMU_HOST = "10.0.2.2";
  private static final int EMU_FIREBASE_PORT = 8080;
  private static final int EMU_AUTH_PORT = 9099;

  private static FirebaseFirestore firestore;
  private static FirebaseAuth firebaseAuth;

  /**
   * Gets an instance of firestore and returns it
   * @return An instance of firestore
   */
  public static FirebaseFirestore getFirestore() {
    if (firestore == null) {
      firestore = FirebaseFirestore.getInstance();

      if (USE_EMU) {
        firestore.useEmulator(EMU_HOST, EMU_FIREBASE_PORT);
      }
    }
    return firestore;
  }

  /**
   * Gets an instance of firebase Auth
   * @return An instance of Girebase Auth
   */
  public static FirebaseAuth getAuth() {
    if (firebaseAuth == null) {
      firebaseAuth = FirebaseAuth.getInstance();

      if (USE_EMU) {
        firebaseAuth.useEmulator(EMU_HOST, EMU_AUTH_PORT);
      }
    }
    return firebaseAuth;
  }

}
