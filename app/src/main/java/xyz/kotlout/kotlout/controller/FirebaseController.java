package xyz.kotlout.kotlout.controller;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Controls firebase services and connection to emulator/production
 */
public class FirebaseController {

  // Collection Names in Firebase, must be kept in sync.
  public static final String POSTS_COLLECTION = "posts";
  public static final String USER_COLLECTION = "users";
  public static final String EXPERIMENT_COLLECTION = "experiments";

  private static final boolean USE_EMU = false;
  private static final String EMU_HOST = "10.0.2.2";
  private static final int EMU_FIREBASE_PORT = 8080;
  private static boolean IS_INITIALIZED = false;

  /**
   * Initialize an instance of firestore. The operating is idempotent and multiple calls are ignored.
   * <p>
   * TODO: Does not need to be public. Initialization checked on every getFireStore call. Is lazy evaluation ideal?
   */
  public static void initFirestore() {
    if (IS_INITIALIZED) {
      return;
    }
    IS_INITIALIZED = true;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    if (USE_EMU) {
      firestore.useEmulator(EMU_HOST, EMU_FIREBASE_PORT);
    }
  }

  /**
   * Gets an instance of firestore and returns it.
   *
   * @return Firestore instance
   */
  public static FirebaseFirestore getFirestore() {
    if (!IS_INITIALIZED) {
      initFirestore();
    }
    return FirebaseFirestore.getInstance();
  }
}
