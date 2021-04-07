package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A trial with a binary (true or false) outcome
 */
public class BinomialTrial extends Trial {

  private boolean result;

  public BinomialTrial() {
  }

  public BinomialTrial(boolean result, String experimenterId) {
    super(experimenterId);
    this.result = result;
  }

  public boolean getResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }
}
