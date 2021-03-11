package xyz.kotlout.kotlout.model.experiment;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.user.User;

/**
 * Base class for modelling experiments. Child classes should model a particular type of
 * experiment.
 */
public abstract class Experiment implements Serializable {

  private User owner;
  private String description;
  private String region;
  private int minimumTrials;
  private boolean isOngoing;
  private boolean geolocationRequired;
  private List<User> ignoredUsers;
  private List<Post> posts;

  /**
   * Default constructor.
   */
  public Experiment() {
  }

  /**
   * Creates a new Experiment with some basic details including description, region, and minimum
   * number of trials.
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
   * Sets for the experiment owner.
   *
   * @param owner The owner of the experiment.
   */
  public void setOwner(User owner) {
    this.owner = owner;
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
   * Gets whether not not a geolocation is required for trials conducted as part of the experiment.
   *
   * @return Returns true if trial geolocation is required, or false otherwise.
   */
  public boolean isGeolocationRequired() {
    return geolocationRequired;
  }

  public List<Post> getPosts() {
    return posts;
  }
}
