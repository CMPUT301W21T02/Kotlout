package xyz.kotlout.kotlout.model.experiment.trial;

import xyz.kotlout.kotlout.model.geolocation.Geolocation;

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

  public NonNegativeTrial(long result, String experimenterId, Geolocation location) {
    super(experimenterId, location);
    this.result = result;
  }

  public long getResult() {
    return result;
  }

  public void setResult(long result) {
    this.result = result;
  }

}
