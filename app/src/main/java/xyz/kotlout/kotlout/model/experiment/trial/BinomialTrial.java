package xyz.kotlout.kotlout.model.experiment.trial;

/**
 * A trial with a binary (true or false) outcome
 */
public class BinomialTrial extends Trial {

  private boolean result;
  private String experimenter;

  public BinomialTrial() {

  }

  public BinomialTrial(boolean result, String experimenter) {
    this.result = result;
    this.experimenter = experimenter;
  }

  public boolean getResult() {
    return result;
  }

  public String getExperimenter() {
    return experimenter;
  }
}
