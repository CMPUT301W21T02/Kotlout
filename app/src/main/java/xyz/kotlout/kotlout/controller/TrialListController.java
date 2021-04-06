package xyz.kotlout.kotlout.controller;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A controller for managing lists of Experiment objects.
 */
public class TrialListController {

  private static final String TAG = "TRIAL_LIST_CONTROLLER";
  private final String experimentId;

  /**
   * Initializes the controller with the given experiment ID.
   * @param experimentId The experiment ID.
   */
  public TrialListController(String experimentId) {
    this.experimentId = experimentId;
  }
}
