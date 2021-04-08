package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A trial with a real (approximated as a Double) valued outcome
 */
public class MeasurementTrial extends Trial {

  private double result;

  /**
   * Default public constructor for Firebase to use.
   */
  public MeasurementTrial() {

  }

  /**
   * Parameterized constructor for creating new instances before storing into Firebase.
   *
   * @param result         Result to store in the trial
   * @param experimenterId Experimenter who did the trial.
   */
  public MeasurementTrial(double result, String experimenterId) {
    super(experimenterId);
    this.result = result;
  }

  /**
   * Get result stored in trial.
   *
   * @return result of trial.
   */
  public double getResult() {
    return result;
  }

  /**
   * Set trial with a result. SHOULD ONLY BE USED FOR FIREBASE.
   *
   * @param result Boolean result to set.
   */
  public void setResult(double result) {
    this.result = result;
  }

}
