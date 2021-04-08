package xyz.kotlout.kotlout.model.experiment;


import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;
import xyz.kotlout.kotlout.model.user.User;

/**
 * Base class for modelling experiments. Child classes should model a particular type of experiment. Which likely contain a list
 * of a type of trial.
 */
public abstract class Experiment implements Serializable {

  @DocumentId
  private String id;
  private String ownerUuid;
  private String description;
  private String region;
  private int minimumTrials;
  private boolean isPublished; // True if the experiment has been published (i.e. visible to others). False otherwise.
  private boolean isOngoing; // True if the experiment hasn't been ended (i.e. is still accepting trials). False otherwise.
  private boolean geolocationRequired;
  private List<String> ignoredUsers;
  private List<Post> posts;

  /**
   * Default constructor.
   */
  public Experiment() {
  }

  /**
   * Creates a new Experiment with some basic details including description, region, and minimum number of trials.
   *
   * @param description   Experiment description.
   * @param region        The region the experiment is conducted in.
   * @param minimumTrials The minimum number of trials required for the experiment.
   */
  public Experiment(String description, String region, int minimumTrials) {
    this.description = description;
    this.region = region;
    this.minimumTrials = minimumTrials;
    this.geolocationRequired = false; // TODO: implement as part of https://github.com/CMPUT301W21T02/Kotlout/issues/5
    this.isOngoing = true;
    ignoredUsers = new ArrayList<>();
    posts = new ArrayList<>();
  }

  /**
   * Gets a string describing the type of experiment.
   *
   * @param experiment An instance of Experiment
   * @return A string with the experiment type.
   */
  public static String getExperimentType(Experiment experiment) {
    String experimentType = "unknown";
    if (experiment instanceof BinomialExperiment) {
      experimentType = "Binomial";
    }
    if (experiment instanceof NonNegativeExperiment) {
      experimentType = "Non-negative Integer";
    }
    if (experiment instanceof CountExperiment) {
      experimentType = "Count";
    }
    if (experiment instanceof MeasurementExperiment) {
      experimentType = "Measurement";
    }
    return experimentType;
  }

  /**
   * Gets the ID for the experiment set by Firestore.
   *
   * @return The ID for the experiment set by Firestore.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the ID for the experiment. Should only be used by Firestore.
   *
   * @param id The new ID of the experiment.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the UUID of the experiment owner.
   *
   * @return The UUID of the experiment owner.
   */
  public String getOwnerUuid() {
    return ownerUuid;
  }

  /**
   * Sets the UUID of the experiment owner.
   *
   * @param ownerUuid The owner of the experiment.
   */
  public void setOwnerUuid(String ownerUuid) {
    this.ownerUuid = ownerUuid;
  }

  /**
   * Gets the description of the experiment.
   *
   * @return The experiment description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the region that the experiment was started in.
   *
   * @return The experiment region.
   */
  public String getRegion() {
    return region;
  }

  /**
   * Gets the minimum number of trials required for the experiment.
   *
   * @return The minimum number of trials required for the experiment.
   */
  public int getMinimumTrials() {
    return minimumTrials;
  }

  /**
   * Gets whether or not the experiment is still ongoing.
   *
   * @return Returns true if the experiment is ongoing, or false otherwise.
   */
  public boolean isOngoing() {
    return isOngoing;
  }

  /**
   * Opens or closes the experiment. If an experiment is closed, it no longer accepts trials from anyone.
   *
   * @param ongoing True if experiment is not closed. False otherwise.
   */
  public void setOngoing(boolean ongoing) {
    isOngoing = ongoing;
  }

  /**
   * Checks if the experiment is published, making it visible to others in the app.
   *
   * @return True if published. False otherwise.
   */
  public boolean isPublished() {
    return isPublished;
  }

  /**
   * Sets the publishing status of the experiment.
   *
   * @param published True if the experiment is published. False otherwise.
   */
  public void setPublished(boolean published) {
    isPublished = published;
  }

  /**
   * Gets whether not not a geolocation is required for trials conducted as part of the experiment.
   *
   * @return Returns true if trial geolocation is required, or false otherwise.
   */
  public boolean isGeolocationRequired() {
    return geolocationRequired;
  }

  /**
   * Gets the posts associated with the experiment.
   *
   * @return A list of posts associated with the experiment.
   */
  public List<Post> getPosts() {
    return posts;
  }

  public void ignoreUser(@NonNull User user) {
    ignoredUsers.add(user.getUuid());
  }

  public List<String> getIgnoredUser() {
    return ignoredUsers;
  }

  abstract ExperimentType getExperimentType();

  abstract void addTrial(Trial trial);

}
