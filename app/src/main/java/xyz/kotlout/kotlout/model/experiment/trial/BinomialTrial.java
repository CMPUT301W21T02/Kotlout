package xyz.kotlout.kotlout.model.experiment.trial;

import xyz.kotlout.kotlout.model.geolocation.Geolocation;

/**
 * A trial with a binary (true or false) outcome
 */
public class BinomialTrial extends Trial {

  private boolean result;

  /**
   * Default public constructor for Firebase to use.
   */
  public BinomialTrial() {
  }

  /**
   * Parameterized constructor for creating new instances before storing into Firebase.
   *
   * @param result         Result to store in the trial
   * @param experimenterId Experimenter who did the trial.
   */
  public BinomialTrial(boolean result, String experimenterId, Geolocation location) {
    super(experimenterId, location);
    this.result = result;
  }

  /**
   * Get result stored in trial.
   *
   * @return result of trial.
   */
  public boolean getResult() {
    return result;
  }

  /**
   * Set trial with a result. SHOULD ONLY BE USED FOR FIREBASE.
   *
   * @param result Boolean result to set.
   */
  public void setResult(boolean result) {
    this.result = result;
  }
}
