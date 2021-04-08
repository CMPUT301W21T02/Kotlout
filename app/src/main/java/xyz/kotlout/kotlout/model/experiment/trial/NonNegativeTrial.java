package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A trial with a non-negative integer outcome
 */
public class NonNegativeTrial extends Trial {

  private long result;

  /**
   * Default public constructor for Firebase to use.
   */
  public NonNegativeTrial() {

  }

  /**
   * Parameterized constructor for creating new instances before storing into Firebase.
   *
   * @param result         Result to store in the trial
   * @param experimenterId Experimenter who did the trial.
   */
  public NonNegativeTrial(long result, String experimenterId) {
    super(experimenterId);
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
