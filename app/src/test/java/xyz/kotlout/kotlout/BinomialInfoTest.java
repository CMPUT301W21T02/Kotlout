package xyz.kotlout.kotlout;

import com.google.common.truth.Expect;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import xyz.kotlout.kotlout.model.experiment.HistogramData;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialInfo;

@RunWith(JUnit4.class)
public class BinomialInfoTest {

  @Rule
  public final Expect expect = Expect.create();

  @Test
  public void testBinomialInfoEquality() {

    BinomialInfo binomialInfo1 = new BinomialInfo("April 3, 2021");
    BinomialInfo binomialInfo2 = new BinomialInfo("April 3, 2021");
    Assert.assertEquals(binomialInfo1, binomialInfo2);
  }

  @Test
  public void testBinomialInfoIncrement() {

    BinomialInfo binomialInfo = new BinomialInfo("April 3, 2021");
    binomialInfo.incrementSuccess();
    Assert.assertEquals(100, binomialInfo.successProportion(), 0);
    binomialInfo.incrementFailure();
    Assert.assertEquals(50, binomialInfo.successProportion(), 0);
  }


}
