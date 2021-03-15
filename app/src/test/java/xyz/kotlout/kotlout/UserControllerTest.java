package xyz.kotlout.kotlout;

import com.google.common.truth.Expect;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import xyz.kotlout.kotlout.controller.UserController;

@RunWith(JUnit4.class)
public class UserControllerTest {

  @Rule
  public final Expect expect = Expect.create();

  @Test
  public void testValidateEmail() {
    boolean isValid = true;
    String[] positiveTestCases = {
        "danny@phantom.ca",
        "one+filter@gmail.gov",
        "jeffbezos@octopus.ca"
    };

    StringBuilder bigBoi = new StringBuilder(1024);
    bigBoi.append("a");
    while (bigBoi.length() < bigBoi.capacity()) {
      bigBoi.append(bigBoi.toString());
    }
    String[] negativeTestCases = {
        "don'tAtMe",
        "UwU",
        "too+Many+Filters+1+2+3@gmail.gov",
        bigBoi.toString(),
        "@@@@@"
    };

    for (String testCase : positiveTestCases) {
      isValid = UserController.validateEmail(testCase);
      expect.withMessage("Failed positive test case: " + testCase).that(isValid).isTrue();
    }

    for (String testCase : negativeTestCases) {
      isValid = UserController.validateEmail(testCase);
      expect.withMessage("Failed negative test case: " + testCase).that(isValid).isFalse();
    }
  }

  @Test
  public void testValidatePhoneNumber() {
    boolean isValid = true;
    String[] positiveTestCases = {
        "123-456-7890",
        "000-000-0000",
        "(123)345-5678",
        "+84(123)345-5678"
    };

    StringBuilder bigBoi = new StringBuilder(1024);
    bigBoi.append("a");
    while (bigBoi.length() < bigBoi.capacity()) {
      bigBoi.append(bigBoi.toString());
    }
    String[] negativeTestCases = {
        "123-456-duck",
        "+123-432?",
        "[789]123-4567",
        "phone_number",
    };

    for (String testCase : positiveTestCases) {
      isValid = UserController.validatePhoneNumber(testCase);
      expect.withMessage("Failed positive test case: " + testCase).that(isValid).isTrue();
    }

    for (String testCase : negativeTestCases) {
      isValid = UserController.validatePhoneNumber(testCase);
      expect.withMessage("Failed negative test case: " + testCase).that(isValid).isFalse();
    }
  }


  @Test
  public void testSyncUser() {
  }

  @Test
  public void testRegisterUser() {

  }

  @Test
  public void testUpdateUser() {

  }
}
