package xyz.kotlout.kotlout.controller;

import android.util.Log;

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

  private static final String TAG = "EXPERIMENT_CONTROLLER";
  private static final String EXPERIMENT_COLLECTION = "experiment";
  Experiment experiment;

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
    this.experiment = experiment;
  }

  /**
   * Initializes the controller and sets the context to an existing Experiment object using the
   * experiment's UUID
   *
   * @param uuid UUID of an existing experiment.
   */
  public ExperimentController(String uuid) {
    //this.experiment = ;
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
        experiment = new BinomialExperiment(description, region, minTrials);
        break;
      case nonNegativeInteger:
        experiment = new NonNegativeExperiment(description, region, minTrials);
        break;
      case count:
        experiment = new CountExperiment(description, region, minTrials);
        break;
      case measurement:
        experiment = new MeasurementExperiment(description, region, minTrials);
        break;
    }
  }

  /**
   * Gets the current Experiment object owned by the controller.
   *
   * @return An Experiment object.
   */
  public Experiment getExperimentContext() {
    return experiment;
  }

  /**
   * Adds a new experiment to the firestore database.
   */
  public void publishExperiment() {
    if (experiment == null) {
      return;
    }

    FirebaseFirestore db = FirebaseController
        .getFirestore(); // TODO: disable emulation for prod release
    db.collection(EXPERIMENT_COLLECTION)
        .add(experiment)
        .addOnSuccessListener(
            documentReference -> Log
                .d(TAG, "Experiment published with id: " + documentReference.getId()))
        .addOnFailureListener(e -> Log.w(TAG, "Experiment not published", e));
  }

  /**
   * Returns the number of trials already completed in the current experiment.
   * <p>
   * TODO: Actually fill this out
   *
   * @return Number of trials completed.
   */
  public int getTrials() {
    // return experiment.getTrials().size();
    return 1;
  }

  /**
   * Generates a standardized string stating how many trials have been completed for this
   * experiment.
   * <p>
   * When more than the minimum number of trials have been completed, it will print just the trial
   * count. If less than the minimum number of trials have been completed, it will print both the
   * minimum and current count.
   *
   * @return Formatted string formed from experiment information.
   */
  public String generateCountText() {

    int minTrials = experiment.getMinimumTrials();
    int currentTrials = getTrials();

    if (minTrials > currentTrials) {
      return String.format(Locale.CANADA, "%d Trials (%d minimum)", currentTrials, minTrials);
    } else {
      return String.format(Locale.CANADA, "%d Trials", currentTrials);
    }
  }
}

