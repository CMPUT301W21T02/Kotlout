package xyz.kotlout.kotlout.controller;

import android.util.Pair;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.experiment.Experiment;

/**
 * A controller for managing lists of Experiment objects.
 */
public class ExperimentListController {

  private static final String TAG = "EXP_LIST_CONTROLLER";
  private final String[] myExperimentsGroups = {"Open experiments", "Closed Experiments"};
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
  public List<Pair<String, List<Experiment>>> initializeMyExperiments() {
    List<Pair<String, List<Experiment>>> myExperiments = new ArrayList<>();
    for (String group : myExperimentsGroups) {
      myExperiments.add(new Pair<>(group, new ArrayList<>()));
    }
    return myExperiments;
  }

  public Query getGetUserExperiments() {
    return getUserExperiments;
  }
}
