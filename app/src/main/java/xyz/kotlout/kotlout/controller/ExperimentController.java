package xyz.kotlout.kotlout.controller;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
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

  private static final String TAG = "EXPERIMENT_CONTROLLER";
  private static final String EXPERIMENT_COLLECTION = "experiment";
  Experiment experimentContext;

  /**
   * Default constructor.
   */
  public ExperimentController() {
  }

  /**
   * Initializes the controller and sets the context to an existing Experiment object.
   *
   * @param experiment An instance of Experiment.
   */
  public ExperimentController(Experiment experiment) {
    this.experimentContext = experiment;
  }

  /**
   * Sets the experiment context with the basic fields needed for a new Experiment.
   *
   * @param description The experiment description.
   * @param region      The region where the experiment is conducted.
   * @param minTrials   The minimum number of trials required for the experiment.
   * @param type        The type of experiment.
   */
  public void setExperimentContext(String description, String region, int minTrials,
      ExperimentType type) {
    switch (type) {

      case binomial:
        experimentContext = new BinomialExperiment(description, region, minTrials);
        break;
      case nonNegativeInteger:
        experimentContext = new NonNegativeExperiment(description, region, minTrials);
        break;
      case count:
        experimentContext = new CountExperiment(description, region, minTrials);
        break;
      case measurement:
        experimentContext = new MeasurementExperiment(description, region, minTrials);
        break;
    }
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

    FirebaseFirestore db = FirebaseController
        .getFirestore(); // TODO: disable emulation for prod release
    db.collection(EXPERIMENT_COLLECTION)
        .add(experimentContext)
        .addOnSuccessListener(
            documentReference -> Log
                .d(TAG, "Experiment published with id: " + documentReference.getId()))
        .addOnFailureListener(e -> Log.w(TAG, "Experiment not published", e));
  }
}

