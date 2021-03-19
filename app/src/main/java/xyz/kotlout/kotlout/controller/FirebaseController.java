package xyz.kotlout.kotlout.controller;

import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Controls firebase services and connection to emulator/production
 */
public class FirebaseController {

  private static final boolean USE_EMU = true;

  private static final String EMU_HOST = "10.0.2.2";
  private static final int EMU_FIREBASE_PORT = 8080;
  private static final int EMU_AUTH_PORT = 9099;

  /**
   * Initialize an instance of firestore
   */
  public static void initFirestore() {
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
    return FirebaseFirestore.getInstance();
  }

}
