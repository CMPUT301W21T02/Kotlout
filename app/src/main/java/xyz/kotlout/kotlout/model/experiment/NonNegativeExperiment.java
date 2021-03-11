package xyz.kotlout.kotlout.model.experiment;

import java.util.List;

import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;

/**
 * A specialization of the Experiment class for experiments involving integers greater than or equal
 * to zero.
 */
public class NonNegativeExperiment extends Experiment {

  private List<NonNegativeTrial> trials;

  /**
   * Creates a new BinomialExperiment with basic fields passed on to the base Experiment
   * constructor.
   */
  public NonNegativeExperiment(String description, String region, int minimumTrials) {
    super(description, region, minimumTrials);
  }
}
