package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

/**
 * A specialization of the Experiment class for experiments involving binary outcomes.
 */
public class BinomialExperiment extends Experiment {

  private final List<BinomialTrial> trials = new ArrayList<>();
  /**
   * Type of experiment, can't be static due to Firestore model generation
   */
  public ExperimentType type = ExperimentType.BINOMIAL;

  /**
   * Default constructor.
   */
  public BinomialExperiment() {
  }

  /**
   * Creates a new BinomialExperiment with basic fields passed on to the base Experiment constructor.
   */
  public BinomialExperiment(String description, String region, int minimumTrials, boolean geolocationRequired) {
    super(description, region, minimumTrials, geolocationRequired);
  }

  @Override
  ExperimentType getExperimentType() {
    return type;
  }

  @Override
  public void addTrial(Trial trial) {
    trials.add((BinomialTrial) trial);
  }

  /**
   * Get list of trials done for this experiment
   *
   * @return ArrayList of Trials for this experiment
   */
  public List<BinomialTrial> getTrials() {
    return trials;
  }

}
