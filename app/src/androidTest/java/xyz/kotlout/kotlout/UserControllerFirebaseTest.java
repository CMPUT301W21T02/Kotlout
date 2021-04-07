package xyz.kotlout.kotlout;

import static com.google.common.truth.Truth.assertWithMessage;

import android.os.SystemClock;
import androidx.core.util.Consumer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.firebase.firestore.DocumentReference;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.user.User;


/**
 * Firebase needs context, so the testcases are under instrumentation There might be a better way to
 * do this
 *
 * Can move to Java tests using robotium, but then it won't write to remote firestore
 */
@RunWith(AndroidJUnit4.class)
public class UserControllerFirebaseTest {

  private final static long TIMEOUT = 1000;
  private final static int NUM_DOC_WRITES = 10;
  private static User oldUser;

  @BeforeClass
  public static void storeOldUser() {
    AtomicBoolean done = new AtomicBoolean();
    FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION).document(UserHelper.readUuid()).get().addOnCompleteListener(task -> oldUser = task.getResult().toObject(User.class));
  }

  @After
  public void restoreOldUser() {
    DocumentReference userDoc = FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION).document(UserHelper.readUuid());
    if(oldUser != null) {
      userDoc.set(oldUser);
    } else {
      userDoc.delete();
    }
  }

  @Test
  public void testInitUser() {
    AtomicBoolean done = new AtomicBoolean();
    DocumentReference userDoc = FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION).document(UserHelper.readUuid());
    User newUser = new User();
    newUser.setUuid(UserHelper.readUuid());
    userDoc.delete();
    UserHelper.initializeUser();
    SystemClock.sleep(100);
    userDoc.get().addOnCompleteListener(task -> {
      Assert.assertEquals(UserHelper.readUuid(), task.getResult().getString("uuid"));
      done.set(true);
    });
    waitFor(done);
  }

  @Test
  public void testInitPreexistingUser() {
    AtomicBoolean done = new AtomicBoolean();
    DocumentReference userDoc = FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION).document(UserHelper.readUuid());
    User newUser = new User();
    newUser.setUuid(UserHelper.readUuid());
    User testUser = new User("FIRST", "LAST", "EMAIL", "PHONE", UserHelper.readUuid());
    userDoc.set(testUser).addOnSuccessListener(task -> done.set(true));
    waitFor(done);
    done.set(false);
    UserHelper.initializeUser();
    userDoc.get().addOnCompleteListener(task -> {
      Assert.assertEquals(testUser.getFirstName(), task.getResult().toObject(User.class).getFirstName());
      done.set(true);
    });
    waitFor(done);
  }

  @Test
  public void testFirebase() {
    AtomicInteger updateCount = new AtomicInteger();
    String testUuid = UUID.randomUUID().toString();
    User testUser = new User();
    testUser.setUuid(testUuid);
    Consumer<User> callback = user -> {
      if (user != null && user.getUuid().equals(testUuid)) {
        updateCount.incrementAndGet();
      } else {
        assertWithMessage("I don't know how you did it, but the UUID is wrong").fail();
      }
    };

    UserController controller = new UserController(testUuid);
    controller.setUpdateCallback(callback);
    DocumentReference userDoc = FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION)
        .document(testUuid);

    long startTime = System.currentTimeMillis();
    triggerDocUpdates(userDoc, testUser, NUM_DOC_WRITES);

    while (updateCount.get() < NUM_DOC_WRITES) {
      if (System.currentTimeMillis() - startTime > TIMEOUT) {
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

  private void waitFor(AtomicBoolean condition) {
    while(!condition.get()) {
      SystemClock.sleep(100);
    }
  }

}
