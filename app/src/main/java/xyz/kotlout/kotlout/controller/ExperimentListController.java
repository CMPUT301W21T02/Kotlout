package xyz.kotlout.kotlout.controller;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A controller for managing lists of Experiment objects.
 */
public class ExperimentListController {

  private static final String TAG = "EXP_LIST_CONTROLLER";
  private final Query getUserExperiments;

  public ExperimentListController(String userUuid) {
    FirebaseFirestore db = FirebaseController.getFirestore();
    CollectionReference experimentsRef = db.collection(ExperimentController.EXPERIMENT_COLLECTION);
    getUserExperiments = experimentsRef.whereEqualTo("ownerUuid", userUuid);
  }

  /**
   * Initialize the list for My Experiments
   *
   * @return An empty list for user experiments grouped by Open and Closed.
   */
  public Map<MyExperimentGroup, List<ExperimentController>> initializeMyExperiments() {

    Map<MyExperimentGroup, List<ExperimentController>> myExperiments = new HashMap<>();

    for (MyExperimentGroup group : MyExperimentGroup.values()) {
      myExperiments.put(group, new ArrayList<>());
    }
    return myExperiments;
  }

  public Query getGetUserExperiments() {
    return getUserExperiments;
  }
}
