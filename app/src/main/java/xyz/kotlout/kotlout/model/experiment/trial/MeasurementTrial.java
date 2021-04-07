package xyz.kotlout.kotlout.model.experiment.trial;

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

  public double getResult() {
    return result;
  }

}
