package xyz.kotlout.kotlout.model.experiment.trial;

import xyz.kotlout.kotlout.model.geolocation.Geolocation;

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

  public BinomialTrial(boolean result, String experimenterId, Geolocation location) {
    super(experimenterId, location);
    this.result = result;
  }

  public boolean getResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }
}
