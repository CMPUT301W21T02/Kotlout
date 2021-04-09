package xyz.kotlout.kotlout.model.experiment.trial;

import xyz.kotlout.kotlout.model.geolocation.Geolocation;

/**
 * A trial with no (or only a single) outcome
 */
public class CountTrial extends Trial {

  private long result;

  public CountTrial() {

  }

  public CountTrial(long result, String experimenterId) {
    super(experimenterId);
    this.result = result;
  }

  public CountTrial(long result, String experimenterId, Geolocation location) {
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
