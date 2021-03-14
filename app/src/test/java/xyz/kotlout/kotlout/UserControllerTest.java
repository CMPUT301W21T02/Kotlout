package xyz.kotlout.kotlout;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.kotlout.kotlout.controller.UserController;

public class UserControllerTest {

  @Test
  void testValidateEmail() {
    boolean success = true;
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
        "",
        null,
        "don'tAtMe",
        "UwU",
        "too+Many+Filters+1+2+3@gmail.gov",
        bigBoi.toString(),
        "@@@@@"
    };

    for (String testCase : positiveTestCases) {
      success = UserController.validateEmail(testCase);
      if (!success) {
        Assertions.fail("Failed positive test case: " + testCase);
      }
    }

    for (String testCase : negativeTestCases) {
      success = !UserController.validateEmail(testCase);
      if (!success) {
        Assertions.fail("Failed negative test case: " + testCase);
      }
    }
  }

  @Test
  void testValidatePhoneNumber() {
    boolean success = true;
    String[] positiveTestCases = {
        "123-456-7890",
        "000-000-0000",
    };

    StringBuilder bigBoi = new StringBuilder(1024);
    bigBoi.append("a");
    while (bigBoi.length() < bigBoi.capacity()) {
      bigBoi.append(bigBoi.toString());
    }
    String[] negativeTestCases = {
        null,
        "",
        "phone_number",
        "000000000"
    };

    for (String testCase : positiveTestCases) {
      success = UserController.validatePhoneNumber(testCase);
      if (!success) {
        Assertions.fail("Failed positive test case: " + testCase);
      }
    }

    for (String testCase : negativeTestCases) {
      success = !UserController.validatePhoneNumber(testCase);
      if (!success) {
        Assertions.fail("Failed negative test case: " + testCase);
      }
    }
  }


  @Test
  void testSyncUser() {
  }

  @Test
  void testRegisterUser() {

  }

  @Test
  void testUpdateUser() {

  }
}
