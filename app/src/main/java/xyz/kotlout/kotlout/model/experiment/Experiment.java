package xyz.kotlout.kotlout.model.experiment;


import java.io.Serializable;
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
  }
}
