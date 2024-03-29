package xyz.kotlout.kotlout.model.experiment.trial;

import xyz.kotlout.kotlout.model.geolocation.Geolocation;

/**
 * A trial with no (or only a single) outcome
 */
public class CountTrial extends Trial {

  private long result;

  /**
   * Default public constructor for Firebase to use.
   */
  public CountTrial() {

  }

  /**
   * Parameterized constructor for creating new instances before storing into Firebase.
   *
   * @param result         Result to store in the trial
   * @param experimenterId Experimenter who did the trial.
   * @param location       Location of the trial
   */
  public CountTrial(long result, String experimenterId, Geolocation location) {
    super(experimenterId, location);
    this.result = result;
  }

  /**
   * Get result stored in trial.
   *
   * @return result of trial.
   */
  public long getResult() {
    return result;
  }

  /**
   * Set trial with a result. SHOULD ONLY BE USED FOR FIREBASE.
   *
   * @param result Boolean result to set.
   */
  public void setResult(long result) {
    this.result = result;
  }
}
