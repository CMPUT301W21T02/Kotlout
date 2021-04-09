package xyz.kotlout.kotlout.controller;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Locale;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.BinomialExperiment;
import xyz.kotlout.kotlout.model.experiment.CountExperiment;
import xyz.kotlout.kotlout.model.experiment.Experiment;
import xyz.kotlout.kotlout.model.experiment.MeasurementExperiment;
import xyz.kotlout.kotlout.model.experiment.NonNegativeExperiment;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

/**
 * Controller for handling interactions with Experiment objects.
 */
public class ExperimentController {

  private static final String TAG = "EXPERIMENT_CONTROLLER";

  private Experiment experimentContext;
  private String experimentId;
  private ExperimentType type;

  /**
   * Default constructor. Disabled, because an empty controller is useless.
   */
  public ExperimentController() {
  }

  /**
   * Initializes the controller and sets the context to a string that refers to the experiment's document ID in Firestore.
   *
   * @param experimentId    Document ID of experiment in Firebase
   * @param loadedObserver  Callback to use when experiment has been loaded. (Only called once)
   * @param updatedObserver Callback to use for whenever the snapshot changes.
   */
  public ExperimentController(String experimentId, @Nullable ExperimentLoadedObserver loadedObserver,
      @Nullable ExperimentUpdatedObserver updatedObserver) {
    FirebaseFirestore db = FirebaseController.getFirestore();
    this.experimentId = experimentId;

    db.collection(FirebaseController.EXPERIMENT_COLLECTION).document(experimentId).get()
        .addOnSuccessListener(documentSnapshot -> {
          if (documentSnapshot != null) {
            type = ExperimentType.valueOf((String) documentSnapshot.get("type"));
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
            if (loadedObserver != null) {
              loadedObserver.onExperimentLoaded();
            }
          }
        });

    if (updatedObserver != null) {
      db.collection(FirebaseController.EXPERIMENT_COLLECTION).document(experimentId).addSnapshotListener((documentSnapshot, error) -> {
        if (documentSnapshot != null && type != null) {
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
          updatedObserver.onExperimentUpdated();
        }
      });
    }
  }

  /**
   * Initializes the controller and sets the context to an existing Experiment object.
   *
   * @param experiment An instance of Experiment.
   */
  public ExperimentController(Experiment experiment) {
    this.experimentContext = experiment;
    this.type = ExperimentType.UNKNOWN;
  }

  /**
   * Initializes the controller and sets the context to an existing Experiment object of known type.
   *
   * @param experiment An instance of Experiment.
   * @param type       Type of experiment
   */
  public ExperimentController(Experiment experiment, ExperimentType type) {
    this.experimentContext = experiment;
    this.type = type;
  }

  /**
   * Initializes the controller from a known DocumentSnapshot object. This does not require a callback as the document can be
   * automatically processed into an object.
   *
   * @param experimentDoc DocumentSnapshot object to build experiment from.
   */
  public ExperimentController(@NonNull DocumentSnapshot experimentDoc) {
    this.experimentId = experimentDoc.getId();
    type = ExperimentType.valueOf((String) experimentDoc.get("type"));

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
   *  @param description The experiment description.
   * @param region      The region where the experiment is conducted.
   * @param minTrials   The minimum number of trials required for the experiment.
   * @param geolocationRequired
   * @param type        Type of experiment
   */
  @NonNull
  public static ExperimentController newInstance(@NonNull String description, String region,
      int minTrials, boolean geolocationRequired, @NonNull ExperimentType type) {

    switch (type) {
      case BINOMIAL:
        return new ExperimentController(new BinomialExperiment(description, region, minTrials, geolocationRequired),
            type);
      case NON_NEGATIVE_INTEGER:
        return new ExperimentController(new NonNegativeExperiment(description, region, minTrials, geolocationRequired),
            type);
      case COUNT:
        return new ExperimentController(new CountExperiment(description, region, minTrials, geolocationRequired), type);
      case MEASUREMENT:
        return new ExperimentController(new MeasurementExperiment(description, region, minTrials, geolocationRequired),
            type);
      default:
        throw new UnsupportedOperationException();
    }
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
   * Get the type of the experiment handled by this controller.
   *
   * @return Type of experiment.
   */
  public ExperimentType getType() {
    return type;
  }

  /**
   * Publishes a new experiment and adds it to firestore.
   */
  public void publishNewExperiment() {
    if (experimentContext == null) {
      return;
    }
    experimentContext.setPublished(true);

    FirebaseFirestore db = FirebaseController.getFirestore();
    db.collection(FirebaseController.EXPERIMENT_COLLECTION)
        .add(experimentContext)
        .addOnSuccessListener(
            documentReference -> {
              experimentId = documentReference.getId();
              Log.d(TAG, "Experiment published with id: " + experimentId);

              // add published experiment to user subscriptions
              UserController userController = new UserController(UserHelper.readUuid());
              userController.setUpdateCallback(user -> userController.addSubscription(experimentId));
            }
        )
        .addOnFailureListener(e -> Log.w(TAG, "Experiment not published", e));
  }

  /**
   * Returns the number of trials that this experiment has completed.
   *
   * @return integer value representing the count of experiments.
   */
  public int getTrialsCompleted() {
    switch (type) {
      case BINOMIAL:
        BinomialExperiment binomialExperiment = (BinomialExperiment) experimentContext;
        return binomialExperiment.getTrials().size();
      case NON_NEGATIVE_INTEGER:
        NonNegativeExperiment nonNegativeExperiment = (NonNegativeExperiment) experimentContext;
        return nonNegativeExperiment.getTrials().size();
      case COUNT:
        CountExperiment countExperiment = (CountExperiment) experimentContext;
        return countExperiment.getTrials().size();
      case MEASUREMENT:
        MeasurementExperiment measurementExperiment = (MeasurementExperiment) experimentContext;
        return measurementExperiment.getTrials().size();
    }

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

  /**
   * Publishes an experiment that already exists in Firebase.
   */
  public void publish() {
    if (experimentContext == null) {
      return;
    }
    if (experimentContext.isPublished()) {
      return;
    }

    setExperimentPublished(true);
  }

  /**
   * Unpublishes an experiment that already exists in Firebase.
   */
  public void unpublish() {
    if (experimentContext == null) {
      return;
    }
    if (!experimentContext.isPublished()) {
      return;
    }

    setExperimentPublished(false);
  }

  /**
   * Updates an experiment's publishing status.
   *
   * @param published True for published. False for unpublished.
   */
  private void setExperimentPublished(boolean published) {
    experimentContext.setPublished(published);
    FirebaseFirestore db = FirebaseController.getFirestore();
    db.collection(FirebaseController.EXPERIMENT_COLLECTION).document(experimentContext.getId())
        .update("published", experimentContext.isPublished())
        .addOnSuccessListener(aVoid ->
            Log.d(TAG,
                "setExperimentPublished: Published set to " + published + " for experiment " + experimentContext.getId()))
        .addOnFailureListener(e ->
            Log.d(TAG,
                "setExperimentPublished: Unable to set published to " + published + " for experiment " +
                    experimentContext.getId()));
  }

  /**
   * Ends an experiment.
   */
  public void end() {
    if (experimentContext == null) {
      return;
    }
    if (!experimentContext.isOngoing()) {
      return;
    }

    setExperimentOngoing(false);
  }

  /**
   * Resumes an experiment that was previously ended.
   */
  public void resume() {
    if (experimentContext == null) {
      return;
    }
    if (experimentContext.isOngoing()) {
      return;
    }

    setExperimentOngoing(true);
  }

  /**
   * Updates an experiment's ongoing status.
   *
   * @param ongoing True for ongoing. False for ended.
   */
  private void setExperimentOngoing(boolean ongoing) {
    experimentContext.setOngoing(ongoing);
    FirebaseFirestore db = FirebaseController.getFirestore();
    db.collection(FirebaseController.EXPERIMENT_COLLECTION).document(experimentContext.getId())
        .update("ongoing", experimentContext.isOngoing())
        .addOnSuccessListener(aVoid ->
            Log.d(TAG,
                "setExperimentPublished: Ongoing set to " + ongoing + " for experiment " + experimentContext.getId()))
        .addOnFailureListener(e ->
            Log.d(TAG,
                "setExperimentPublished: Unable to set ongoing to " + ongoing + " for experiment " +
                    experimentContext.getId()));
  }

  /**
   * Adds a new trial to the experiment context handled by this controller
   *
   * @param trial The new trial to add.
   */
  public void addTrial(Trial trial) {
    FirebaseFirestore db = FirebaseController.getFirestore();
    db.collection(FirebaseController.EXPERIMENT_COLLECTION).document(experimentId)
        .update("trials", FieldValue.arrayUnion(trial));
  }

  /**
   * Get a list of trials linked to this experiment.
   *
   * @return List of trials of the correct type.
   */
  public List<? extends Trial> getListTrials() {
    switch (type) {
      case BINOMIAL:
        BinomialExperiment binomialExperiment = (BinomialExperiment) experimentContext;
        return binomialExperiment.getTrials();
      case NON_NEGATIVE_INTEGER:
        NonNegativeExperiment nonNegativeExperiment = (NonNegativeExperiment) experimentContext;
        return nonNegativeExperiment.getTrials();
      case COUNT:
        CountExperiment countExperiment = (CountExperiment) experimentContext;
        return countExperiment.getTrials();
      case MEASUREMENT:
        MeasurementExperiment measurementExperiment = (MeasurementExperiment) experimentContext;
        return measurementExperiment.getTrials();
    }

    return null;
  }

  /**
   * Callback interface for when the experiment is loaded.
   */
  public interface ExperimentLoadedObserver {

    void onExperimentLoaded();
  }

  /**
   * Callback interface for when the experiment is updated.
   */
  public interface ExperimentUpdatedObserver {

    void onExperimentUpdated();
  }

}

