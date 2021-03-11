package xyz.kotlout.kotlout.model.experiment;

import java.util.List;

import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;

/**
 * A specialization of the Experiment class for experiments involving integer counts.
 */
public class CountExperiment extends Experiment {

  private List<CountTrial> trials;

  /**
   * Creates a new BinomialExperiment with basic fields passed on to the base Experiment
   * constructor.
   */
  public CountExperiment(String description, String region, int minimumTrials) {
    super(description, region, minimumTrials);
  }
}
