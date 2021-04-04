package xyz.kotlout.kotlout.model.experiment;


import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
  private boolean isOngoing;
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
   * Gets the ID for the experiment set by Firestore.
   *
   * @return The ID for the experiment set by Firestore.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the ID for the experiment. Should only be used by Firestore.
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
  public boolean getIsOngoing() {
    return isOngoing;
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
}
