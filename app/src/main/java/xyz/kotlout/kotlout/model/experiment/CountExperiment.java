package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.List;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;

/**
 * A specialization of the Experiment class for experiments involving integer counts.
 */
public class CountExperiment extends Experiment {

  /**
   * Type of experiment, can't be static due to Firestore model generation
   */
  public ExperimentType type = ExperimentType.COUNT;

  private List<CountTrial> trials = new ArrayList<>();

  /**
   * Default constructor to satisfy Firebase
   */
  public CountExperiment() {
  }

  /**
   * Creates a new CountExperiment with basic fields passed on to the base Experiment constructor.
   */
  public CountExperiment(String description, String region, int minimumTrials) {
    super(description, region, minimumTrials);
  }

  @Override
  ExperimentType getExperimentType() {
    return type;
  }

  @Override
  public void addTrial(Trial trial) {
    trials.add((CountTrial) trial);
  }

  /**
   * Get list of trials done for this experiment
   *
   * @return ArrayList of Trials for this experiment
   */
  public List<CountTrial> getTrials() {
    return trials;
  }

}
