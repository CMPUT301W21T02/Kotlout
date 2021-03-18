package xyz.kotlout.kotlout.controller;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Locale;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.BinomialExperiment;
import xyz.kotlout.kotlout.model.experiment.CountExperiment;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.experiment.MeasurementExperiment;
import xyz.kotlout.kotlout.model.experiment.NonNegativeExperiment;

/**
 * Controller for handling interactions with Experiment objects.
 */
public class ExperimentController {

  public static final String EXPERIMENT_COLLECTION = "experiments";
  private static final String TAG = "EXPERIMENT_CONTROLLER";

  Experiment experimentContext;
  String experimentId;

  /**
   * Default constructor. Disabled, because an empty controller is useless.
   */
  private ExperimentController() {
  }

  /**
   * Initializes the controller and sets the context to a string that refers to the experiment's document ID in Firestore.
   *
   * @param documentId Firestore DocumentID that refers to the experiment.
   */
  public ExperimentController(String documentId) {

    FirebaseFirestore db = FirebaseController.getFirestore();

    db.collection(EXPERIMENT_COLLECTION).document(documentId).get()
        .addOnSuccessListener(documentSnapshot -> {
          //TODO: This feels jank. There must be a better way. Generics?
          ExperimentType type = ExperimentType.valueOf((String) documentSnapshot.get("type"));
          switch (type) {
            case BINOMIAL:
              experimentContext = documentSnapshot.toObject(BinomialExperiment.class);
              break;
            case NON_NEGATIVE_INTEGER:
              experimentContext = documentSnapshot.toObject(NonNegativeExperiment.class);
              break;
            case COUNT:
              experimentContext = documentSnapshot.toObject(CountExperiment.class);
              break;
            case MEASUREMENT:
              experimentContext = documentSnapshot.toObject(MeasurementExperiment.class);
              break;
          }
        });
  }

  /**
   * Initializes the controller and sets the context to an existing Experiment object.
   *
   * @param experiment An instance of Experiment.
   */
  public ExperimentController(Experiment experiment) {
    this.experimentContext = experiment;
  }

  public ExperimentController(DocumentSnapshot experimentDoc) {
    ExperimentType type = ExperimentType.valueOf((String) experimentDoc.get("type"));
    switch (type) {
      case BINOMIAL:
        experimentContext = experimentDoc.toObject(BinomialExperiment.class);
        break;
      case NON_NEGATIVE_INTEGER:
        experimentContext = experimentDoc.toObject(NonNegativeExperiment.class);
        break;
      case COUNT:
        experimentContext = experimentDoc.toObject(CountExperiment.class);
        break;
      case MEASUREMENT:
        experimentContext = experimentDoc.toObject(MeasurementExperiment.class);
        break;
    }
  }

  /**
   * Sets the experiment context with the basic fields needed for a new Experiment.
   *
   * @param description The experiment description.
   * @param region      The region where the experiment is conducted.
   * @param minTrials   The minimum number of trials required for the experiment.
   * @param type        The type of experiment.
   */
  @NonNull
  public static ExperimentController newInstance(@NonNull String description, String region,
      int minTrials, @NonNull ExperimentType type) {

    switch (type) {
      case BINOMIAL:
        return new ExperimentController(new BinomialExperiment(description, region, minTrials));
      case NON_NEGATIVE_INTEGER:
        return new ExperimentController(new NonNegativeExperiment(description, region, minTrials));
      case COUNT:
        return new ExperimentController(new CountExperiment(description, region, minTrials));
      case MEASUREMENT:
        return new ExperimentController(new MeasurementExperiment(description, region, minTrials));
    }
    return null;
  }

  /**
   * Gets the document ID of the current experiment object owned by the controller.
   *
   * @return A String stating the ID
   */
  public String getExperimentId() {
    return experimentId;
  }

  /**
   * Gets the current Experiment object owned by the controller.
   *
   * @return An Experiment object.
   */
  public Experiment getExperimentContext() {
    return experimentContext;
  }

  /**
   * Adds a new experiment to the firestore database.
   */
  public void publish() {
    if (experimentContext == null) {
      return;
    }

    FirebaseFirestore db = FirebaseController.getFirestore();
    db.collection(EXPERIMENT_COLLECTION)
        .add(experimentContext)
        .addOnSuccessListener(
            documentReference -> {
              experimentId = documentReference.getId();
              Log.d(TAG, "Experiment published with id: " + experimentId);
            }
        )
        .addOnFailureListener(e -> Log.w(TAG, "Experiment not published", e));
  }

  /**
   * Returns the number of trials that this experiment has completed TODO: This returns 0 all the time at the moment.
   *
   * @return integer value representing the count of experiments
   */
  public int getTrialsCompleted() {
    return 0;
  }

  /**
   * Generates a standardized string stating how many trials have been completed for this experiment.
   * <p>
   * When more than the minimum number of trials have been completed, it will print just the trial count. If less than the
   * minimum number of trials have been completed, it will print both the minimum and current count.
   *
   * @return Formatted string formed from experiment information.
   */
  public String generateCountText() {

    int minTrials = experimentContext.getMinimumTrials();
    int currentTrials = getTrialsCompleted();

    if (minTrials > currentTrials) {
      return String.format(Locale.CANADA, "%d Trials (%d minimum)", currentTrials, minTrials);
    } else {
      return String.format(Locale.CANADA, "%d Trials", currentTrials);
    }
  }
}

