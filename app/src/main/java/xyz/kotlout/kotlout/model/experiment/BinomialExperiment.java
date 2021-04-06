package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

/**
 * A specialization of the Experiment class for experiments involving binary outcomes.
 */
public class BinomialExperiment extends Experiment {

  public ExperimentType type = ExperimentType.BINOMIAL;
  private final List<BinomialTrial> trials = new ArrayList<>();

  /**
   * Default constructor.
   */
  public BinomialExperiment() {
  }

  /**
   * Creates a new BinomialExperiment with basic fields passed on to the base Experiment constructor.
   */
  public BinomialExperiment(String description, String region, int minimumTrials) {
    super(description, region, minimumTrials);
  }

  @Override
  ExperimentType getExperimentType() {
    return type;
  }

  @Override
  public void addTrial(Trial trial) {
    trials.add((BinomialTrial) trial);
  }

  public List<BinomialTrial> getTrials() {
    return trials;
  }

}
