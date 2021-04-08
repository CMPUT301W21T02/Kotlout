package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A trial with a non-negative integer outcome
 */
public class NonNegativeTrial extends Trial {

  private int result;

  public NonNegativeTrial() {

  }

  public NonNegativeTrial(int result, String experimenterId) {
    super(experimenterId);
    this.result = result;
  }
  public int getResult() {
    return result;
  }

}
