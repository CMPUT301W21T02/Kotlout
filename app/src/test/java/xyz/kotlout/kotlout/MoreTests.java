package xyz.kotlout.kotlout;

import com.google.common.truth.Expect;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.experiment.HistogramData;

@RunWith(JUnit4.class)
public class MoreTests {

  @Rule
  public final Expect expect = Expect.create();

  @Test
  public void testHistogramData() {

    boolean isValid = true;
    ArrayList<HistogramData> testHistograms = new ArrayList<>();
    ArrayList<HistogramData> merged = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      testHistograms.add(new HistogramData("April 2, 2021", 1));
    }

    for (HistogramData h : testHistograms) {
      int index = merged.indexOf(h);
      if (index != -1) {
        merged.set(index, merged.get(index).merge(h));
      } else {
        merged.add(h);
      }
    }
    Assert.assertEquals(1, merged.size());
    Assert.assertEquals("April 2, 2021", merged.get(0).getResult());
    Assert.assertEquals((float) merged.get(0).getCount(), (float) 5, 0);

    merged.add(new HistogramData("April 5, 2021", 2));

    Assert.assertEquals(2, merged.size());
  }


}
