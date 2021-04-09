package xyz.kotlout.kotlout;

import static com.google.common.truth.Truth.assertThat;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import xyz.kotlout.kotlout.controller.ScannableController;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;
import xyz.kotlout.kotlout.view.TrialNewActivity;

@RunWith(RobolectricTestRunner.class)
public class ScannableTests {

  private final static int NUM_TRIALS = 10;

  public static Uri generateRandomUri() {
    String result = "";
    Random rand = new Random();
    int randTrialType = rand.nextInt(4);
    ExperimentType[] trialTypes = {ExperimentType.BINOMIAL, ExperimentType.COUNT, ExperimentType.NON_NEGATIVE_INTEGER,
        ExperimentType.NON_NEGATIVE_INTEGER};
    switch (trialTypes[randTrialType]) {
      case BINOMIAL:
        result = Boolean.valueOf(rand.nextBoolean()).toString();
        break;
      case COUNT:
        result = Long.valueOf(rand.nextLong()).toString();
        break;
      case MEASUREMENT:
        result = Double.valueOf(rand.nextDouble()).toString();
        break;
      case NON_NEGATIVE_INTEGER:
        result = Long.valueOf(rand.nextLong()).toString();
        break;
    }
    return ScannableController
        .createUri(result, Long.valueOf(rand.nextLong()).toString(), trialTypes[randTrialType].toString(), null, null);
  }

  public static boolean compareTrials(ExperimentType type, Trial a, Trial b) {
    try {
      switch (type) {
        case BINOMIAL:
          return ((BinomialTrial) a).getResult() == ((BinomialTrial) b).getResult();
        case NON_NEGATIVE_INTEGER:
          return ((NonNegativeTrial) a).getResult() == ((NonNegativeTrial) b).getResult();
        case COUNT:
          return ((CountTrial) a).getResult() == ((CountTrial) b).getResult();
        case MEASUREMENT:
          return ((MeasurementTrial) a).getResult() == ((MeasurementTrial) b).getResult();
        case UNKNOWN:
          return false;
      }
    } catch (NullPointerException e) {
      return false;
    }
    return false;
  }

  @Test
  public void testEncodeBinomialTrials() {
    Random rand = new Random();
    // Create random binomial trials and experiment Id's
    List<BinomialTrial> originalTrials = new ArrayList<>();
    List<String> experimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      originalTrials.add(new BinomialTrial(rand.nextBoolean(), Long.valueOf(rand.nextLong()).toString(), null));
      experimentIds.add(Long.valueOf(rand.nextLong()).toString());
    }

    // Encode these trials as Uri's and revert them back to Binomial Trials
    List<BinomialTrial> parsedTrials = new ArrayList<>();
    List<String> parsedExperimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      Uri convertedTrial = ScannableController
          .createUri(Boolean.valueOf(originalTrials.get(i).getResult()).toString(), experimentIds.get(i),
              ExperimentType.BINOMIAL.toString(), null, null);
      parsedTrials.add((BinomialTrial) ScannableController.getTrialFromUri(convertedTrial));
      parsedExperimentIds.add(convertedTrial.getQueryParameter(TrialNewActivity.EXPERIMENT_ID));
    }

    // Compare the data in both lists of trials to make sure they are identical
    for (int i = 0; i < NUM_TRIALS; ++i) {
      assertThat(originalTrials.get(i).getResult() == parsedTrials.get(i).getResult()).isTrue();
      assertThat(experimentIds.get(i).equals(parsedExperimentIds.get(i))).isTrue();
    }
  }

  @Test
  public void testEncodeCountTrials() {
    Random rand = new Random();
    // Create random binomial trials and experiment Id's
    List<CountTrial> originalTrials = new ArrayList<>();
    List<String> experimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      originalTrials.add(new CountTrial(rand.nextLong(), Long.valueOf(rand.nextLong()).toString()));
      experimentIds.add(Long.valueOf(rand.nextLong()).toString());
    }

    // Encode these trials as Uri's and revert them back to Count Trials
    List<CountTrial> parsedTrials = new ArrayList<>();
    List<String> parsedExperimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      Uri convertedTrial = ScannableController
          .createUri(Long.valueOf(originalTrials.get(i).getResult()).toString(), experimentIds.get(i),
              ExperimentType.COUNT.toString(), null, null);
      parsedTrials.add((CountTrial) ScannableController.getTrialFromUri(convertedTrial));
      parsedExperimentIds.add(convertedTrial.getQueryParameter(TrialNewActivity.EXPERIMENT_ID));
    }

    // Compare the data in both lists of trials to make sure they are identical
    for (int i = 0; i < NUM_TRIALS; ++i) {
      assertThat(originalTrials.get(i).getResult() == parsedTrials.get(i).getResult()).isTrue();
      assertThat(experimentIds.get(i).equals(parsedExperimentIds.get(i))).isTrue();
    }
  }

  @Test
  public void testEncodeNonNegativeTrials() {
    Random rand = new Random();
    // Create random binomial trials and experiment Id's
    List<NonNegativeTrial> originalTrials = new ArrayList<>();
    List<String> experimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      originalTrials.add(new NonNegativeTrial(rand.nextLong(), Long.valueOf(rand.nextLong()).toString()));
      experimentIds.add(Long.valueOf(rand.nextLong()).toString());
    }

    // Encode these trials as Uri's and revert them back to Non-negative Trials
    List<NonNegativeTrial> parsedTrials = new ArrayList<>();
    List<String> parsedExperimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      Uri convertedTrial = ScannableController
          .createUri(Long.valueOf(originalTrials.get(i).getResult()).toString(), experimentIds.get(i),
              ExperimentType.NON_NEGATIVE_INTEGER.toString(), null, null);
      parsedTrials.add((NonNegativeTrial) ScannableController.getTrialFromUri(convertedTrial));
      parsedExperimentIds.add(convertedTrial.getQueryParameter(TrialNewActivity.EXPERIMENT_ID));
    }

    // Compare the data in both lists of trials to make sure they are identical
    for (int i = 0; i < NUM_TRIALS; ++i) {
      assertThat(originalTrials.get(i).getResult() == parsedTrials.get(i).getResult()).isTrue();
      assertThat(experimentIds.get(i).equals(parsedExperimentIds.get(i))).isTrue();
    }
  }

  @Test
  public void testEncodeMeasurementTrials() {
    Random rand = new Random();
    // Create random binomial trials and experiment Id's
    List<MeasurementTrial> originalTrials = new ArrayList<>();
    List<String> experimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      originalTrials.add(new MeasurementTrial(rand.nextDouble(), Long.valueOf(rand.nextLong()).toString(), null));
      experimentIds.add(Long.valueOf(rand.nextLong()).toString());
    }

    // Encode these trials as Uri's and revert them back to Measurement Trials
    List<MeasurementTrial> parsedTrials = new ArrayList<>();
    List<String> parsedExperimentIds = new ArrayList<>();
    for (int i = 0; i < NUM_TRIALS; ++i) {
      Uri convertedTrial = ScannableController
          .createUri(Double.valueOf(originalTrials.get(i).getResult()).toString(), experimentIds.get(i),
              ExperimentType.MEASUREMENT.toString(), null, null);
      parsedTrials.add((MeasurementTrial) ScannableController.getTrialFromUri(convertedTrial));
      parsedExperimentIds.add(convertedTrial.getQueryParameter(TrialNewActivity.EXPERIMENT_ID));
    }

    // Compare the data in both lists of trials to make sure they are identical
    for (int i = 0; i < NUM_TRIALS; ++i) {
      assertThat(originalTrials.get(i).getResult() == parsedTrials.get(i).getResult()).isTrue();
      assertThat(experimentIds.get(i).equals(parsedExperimentIds.get(i))).isTrue();
    }
  }
}
