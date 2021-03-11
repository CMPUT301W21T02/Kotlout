package xyz.kotlout.kotlout;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class UserControllerTest {

  @Test
  void testValidateEmail() {
    boolean success = true;
    String[] testCases = {
        "danny@phantom.ca",
        "too+Many+Filters+1+2+3@gmail.gov",
        "jeffBezos@octopus.ca"
    };
  }

  @Test
  void testValidatePhoneNumber() {

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
