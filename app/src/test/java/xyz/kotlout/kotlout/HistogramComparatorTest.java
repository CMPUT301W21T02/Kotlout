package xyz.kotlout.kotlout;

import com.google.common.truth.Expect;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import xyz.kotlout.kotlout.model.experiment.HistogramComparator;
import xyz.kotlout.kotlout.model.experiment.HistogramData;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialInfo;

@RunWith(JUnit4.class)
public class HistogramComparatorTest {

  @Rule
  public final Expect expect = Expect.create();

  @Test
  public void HistogramComparator() {
    HistogramComparator comparator = new HistogramComparator();
    HistogramData histogramData1 = new HistogramData("Success", 1);
    HistogramData histogramData2 = new HistogramData("Failure", 1);
    HistogramData histogramData3 = new HistogramData("Success", 1);
    Assert.assertNotEquals(0, comparator.compare(histogramData1, histogramData2));
    Assert.assertEquals(0, comparator.compare(histogramData1, histogramData3));
  }


}
