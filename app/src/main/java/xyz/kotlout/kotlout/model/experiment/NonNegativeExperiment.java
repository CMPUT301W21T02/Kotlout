package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

/**
 * A specialization of the Experiment class for experiments involving integers greater than or equal to zero.
 */
public class NonNegativeExperiment extends Experiment {

  private final List<NonNegativeTrial> trials = new ArrayList<>();
  /**
   * Type of experiment, can't be static due to Firestore model generation
   */
  public ExperimentType type = ExperimentType.NON_NEGATIVE_INTEGER;

  public NonNegativeExperiment() {
  }

  /**
   * Creates a new NonNegativeExperiment with basic fields passed on to the base Experiment constructor.
   */
  public NonNegativeExperiment(String description, String region, int minimumTrials, boolean geolocationRequired) {
    super(description, region, minimumTrials, geolocationRequired);
  }

  @Override
  ExperimentType getExperimentType() {
    return type;
  }

  @Override
  public void addTrial(Trial trial) {
    trials.add((NonNegativeTrial) trial);
  }

  /**
   * Get list of trials done for this experiment
   *
   * @return ArrayList of Trials for this experiment
   */
  public List<NonNegativeTrial> getTrials() {
    return trials;
  }
}
