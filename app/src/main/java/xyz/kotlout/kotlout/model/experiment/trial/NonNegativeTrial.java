package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A trial with a non-negative integer outcome
 */
public class NonNegativeTrial extends Trial {

  private long result;

  public NonNegativeTrial() {

  }

  public NonNegativeTrial(long result, String experimenterId) {
    super(experimenterId);
    this.result = result;
  }

  public long getResult() {
    return result;
  }

  public void setResult(long result) {
    this.result = result;
  }

}
