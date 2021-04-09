package xyz.kotlout.kotlout.controller;

import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Controls firebase services and connection to emulator/production
 */
public class FirebaseController {

  private static final boolean USE_EMU = false;
  private static final String EMU_HOST = "10.0.2.2";
  private static final String NON_EMU_HOST = "192.168.0.80";
  private static final int EMU_FIREBASE_PORT = 8080;
  private static final int EMU_AUTH_PORT = 9099;
  private static boolean IS_INITIALIZED = false;
  public static final String POSTS_COLLECTION = "posts";

  /**
   * Initialize an instance of firestore. The operating is idempotent and multiple calls
   * are ignored.
   *
   * TODO: Does not need to be public. Initialization checked on first getFireStore call.
   */
  public static void initFirestore() {
    if (IS_INITIALIZED) return;
    IS_INITIALIZED = true;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    if (USE_EMU) {
      firestore.useEmulator(EMU_HOST, EMU_FIREBASE_PORT);
    }
  }

  //TODO consider removing this method, since we no longer keep a static instance

  /**
   * Gets an instance of firestore and returns it
   *
   * @return Firestore instance
   */
  public static FirebaseFirestore getFirestore() {
    if (!IS_INITIALIZED) initFirestore();
    return FirebaseFirestore.getInstance();
  }

}
