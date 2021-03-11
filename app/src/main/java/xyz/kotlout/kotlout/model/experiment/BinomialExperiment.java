package xyz.kotlout.kotlout.model.experiment;

import java.util.List;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;

/**
 * A specialization of the Experiment class for experiments involving binary outcomes.
 */
public class BinomialExperiment extends Experiment {

  private List<BinomialTrial> trials;

  /**
   * Creates a new BinomialExperiment with basic fields passed on to the base Experiment
   * constructor.
   */
  public BinomialExperiment(String description, String region, int minimumTrials) {
    super(description, region, minimumTrials);
  }
}
