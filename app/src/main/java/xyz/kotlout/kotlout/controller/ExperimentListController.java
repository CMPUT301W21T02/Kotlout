package xyz.kotlout.kotlout.controller;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A controller for managing lists of Experiment objects.
 */
public class ExperimentListController {

  private static final String TAG = "EXP_LIST_CONTROLLER";
  private final String userId;

  /**
   * Initializes the controller with the given user ID.
   *
   * @param userId The user ID.
   */
  public ExperimentListController(String userId) {
    this.userId = userId;
  }

  /**
   * Gets a query for getting all experiments for the given user.
   * <p>
   * Firestore does not support where-in queries with more than 10 clauses (i.e. only 10 subscriptions would be supported) So,
   * get all experiments and let the application filter them for subscribed experiments
   *
   * @return A instance of Query for getting all experiments for the given user.
   */
  public static Query getAllExperiments() {
    FirebaseFirestore db = FirebaseController.getFirestore();

    return db.collection(FirebaseController.EXPERIMENT_COLLECTION);
  }

  /**
   * Gets a query for getting all experiments owned by the given user.
   *
   * @return A instance of Query for getting all experiments for the given user.
   */
  public Query getUserExperiments() {
    FirebaseFirestore db = FirebaseController.getFirestore();
    CollectionReference experimentsRef = db.collection(FirebaseController.EXPERIMENT_COLLECTION);
    return experimentsRef.whereEqualTo("ownerUuid", this.userId);
  }
}
