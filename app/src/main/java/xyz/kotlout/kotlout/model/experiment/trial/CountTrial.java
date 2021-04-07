package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A trial with no (or only a single) outcome
 */
public class CountTrial extends Trial {

  private int result;

  public CountTrial() {

  }

  public CountTrial(int result, String experimenterId) {
    super(experimenterId);
    this.result = result;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }
}
