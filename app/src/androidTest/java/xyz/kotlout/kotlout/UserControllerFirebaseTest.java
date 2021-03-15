package xyz.kotlout.kotlout;

import com.google.common.truth.Expect;
import static com.google.common.truth.Truth.assertWithMessage;

import androidx.core.util.Consumer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.firebase.firestore.DocumentReference;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.user.User;


/**
 * Firebase needs context, so the testcases are under instrumentation There might be a better way to
 * do this
 */
@RunWith(AndroidJUnit4.class)
public class UserControllerFirebaseTest {

  @Test
  public void testFirebase() {
    AtomicInteger updateCount = new AtomicInteger();
    String testUuid = UUID.randomUUID().toString();
    User testUser = new User();
    testUser.setUuid(testUuid);
    long timeoutMillis = 2000;
    Consumer<User> callback = user -> {
      if (user != null && user.getUuid().equals(testUuid)) {
        updateCount.incrementAndGet();
      } else {
        assertWithMessage("I don't know how you did it, but the UUID is wrong").fail();
      }
    };

    UserController controller = new UserController(testUuid);
    controller.setUpdateCallback(callback);
    DocumentReference userDoc = FirebaseController.getFirestore().collection("users")
        .document(testUuid);

    long startTime = System.currentTimeMillis();
    triggerDocUpdates(userDoc, testUser, 10);

    while (updateCount.get() < 10) {
      if (System.currentTimeMillis() - startTime > timeoutMillis) {
        assertWithMessage("Timeout, updates did not occur in time").fail();
      }
    }
  }

  private void triggerDocUpdates(DocumentReference docRef, User testUser, int count) {
    for (int i = 0; i < count; ++i) {
      testUser.setFirstName(String.format("%d", i));
      docRef.set(testUser);
    }
  }

}
