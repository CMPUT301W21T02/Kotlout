package xyz.kotlout.kotlout.model.experiment.trial;

import xyz.kotlout.kotlout.model.geolocation.Geolocation;

/**
 * A trial with a real (approximated as a Double) valued outcome
 */
public class MeasurementTrial extends Trial {

  private double result;

  public MeasurementTrial() {

  }

  public MeasurementTrial(double result, String experimenterId) {
    super(experimenterId);
    this.result = result;
  }

  public MeasurementTrial(double result, String experimenterId, Geolocation location) {
    super(experimenterId, location);
    this.result = result;
  }

  public double getResult() {
    return result;
  }

  public void setResult(double result) {
    this.result = result;
  }

}
