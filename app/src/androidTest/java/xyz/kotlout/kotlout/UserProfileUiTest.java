package xyz.kotlout.kotlout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import android.os.SystemClock;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.user.User;
import xyz.kotlout.kotlout.view.MainActivity;
import xyz.kotlout.kotlout.view.ProfileActivity;

@RunWith(AndroidJUnit4.class)
public class UserProfileUiTest {

  @Rule
  public ActivityScenarioRule<ProfileActivity> rule = new ActivityScenarioRule<>(
      ProfileActivity.class);
  String VALID_FIRST_NAME_STRING = "Morty";
  String VALID_LAST_NAME_STRING = "Rick";
  String VALID_PHONE_NUM_STRING = "000-111-2222";
  String VALID_EMAIL_ADDR_STRING = "high@iq.gov";
  String INVALID_PHONE_NUM_STRING = "0-0-1-1-2-2-";
  String INVALID_EMAIL_ADDR_STRING = "high@iq.gov@";

  private static User oldUser ;


  @BeforeClass
  public static void setupProfileTest() {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    AtomicBoolean initalized = new AtomicBoolean();
    User newUser = new User();
    newUser.setUuid(UUID.randomUUID().toString());
    firestore.collection(UserHelper.USER_COLLECTION).document(newUser.getUuid()).get().addOnCompleteListener( task -> {
      if(task.isSuccessful() && task.getResult() != null) {
        oldUser = task.getResult().toObject(User.class);
        initalized.set(true);
      }
    });
    while(!initalized.get()) {
      SystemClock.sleep(100);
    }
    firestore.collection(UserHelper.USER_COLLECTION).document(newUser.getUuid()).set(newUser);
  }

  @AfterClass
  public static void tearDownProfileTest() {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    firestore.collection(UserHelper.USER_COLLECTION).document(oldUser.getUuid()).set(oldUser);
  }

  @Before
  public void clearUserFields() {
    String uuid = UserHelper.readUuid();
    User newUser = new User();
    newUser.setUuid(uuid);
    FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION).document(uuid).set(newUser);
  }

  @Test
  public void testOpenProfileFromMainActivity() {
    ActivityScenario.launch(MainActivity.class);
    onView(withId(R.id.show_profile)).perform(click());
    onView(withId(R.id.profilePhoneEditText)).check(matches(isDisplayed()));
  }

  @Test
  public void testEditImmutableFields() {
    onView(withId(R.id.profileFirstNameEditText)).perform(click()).check(matches(not(isEnabled())));
    onView(withId(R.id.profileLastNameEditText)).perform(click()).check(matches(not(isEnabled())));
    onView(withId(R.id.profileEmailEditText)).perform(click()).check(matches(not(isEnabled())));
    onView(withId(R.id.profilePhoneEditText)).perform(click()).check(matches(not(isEnabled())));
    onView(withId(R.id.edit_confirm_button)).check(doesNotExist());

    onView(withId(R.id.profileFirstNameEditText))
        .check(matches(not(withText(containsString(VALID_FIRST_NAME_STRING)))));
    onView(withId(R.id.profileLastNameEditText))
        .check(matches(not(withText(containsString(VALID_LAST_NAME_STRING)))));
    onView(withId(R.id.profileEmailEditText))
        .check(matches(not(withText(containsString(VALID_EMAIL_ADDR_STRING)))));
    onView(withId(R.id.profilePhoneEditText))
        .check(matches(not(withText(containsString(VALID_PHONE_NUM_STRING)))));
  }

  @Test
  public void testEditMutableFields() {
    onView(withId(R.id.edit_profile_button)).perform(click());
    onView(withId(R.id.profileFirstNameEditText))
        .perform(click(), typeText(VALID_FIRST_NAME_STRING), closeSoftKeyboard());
    onView(withId(R.id.profileLastNameEditText))
        .perform(click(), typeText(VALID_LAST_NAME_STRING), closeSoftKeyboard());
    onView(withId(R.id.profileEmailEditText))
        .perform(click(), typeText(VALID_EMAIL_ADDR_STRING), closeSoftKeyboard());
    onView(withId(R.id.profilePhoneEditText))
        .perform(click(), typeText(VALID_PHONE_NUM_STRING), closeSoftKeyboard());
    onView(withId(R.id.edit_confirm_button)).perform(click());

    onView(withId(R.id.profileFirstNameEditText))
        .check(matches(withText(containsString(VALID_FIRST_NAME_STRING))));
    onView(withId(R.id.profileLastNameEditText))
        .check(matches(withText(containsString(VALID_LAST_NAME_STRING))));
    onView(withId(R.id.profileEmailEditText))
        .check(matches(withText(containsString(VALID_EMAIL_ADDR_STRING))));
    onView(withId(R.id.profilePhoneEditText))
        .check(matches(withText(containsString(VALID_PHONE_NUM_STRING))));
  }

  @Test
  public void testInvalidEditMutableFields() {
    onView(withId(R.id.edit_profile_button)).perform(click());
    onView(withId(R.id.profileFirstNameEditText))
        .perform(click(), typeText(VALID_FIRST_NAME_STRING), closeSoftKeyboard());
    onView(withId(R.id.profileLastNameEditText))
        .perform(click(), typeText(VALID_LAST_NAME_STRING), closeSoftKeyboard());
    onView(withId(R.id.profileEmailEditText))
        .perform(click(), typeText(INVALID_EMAIL_ADDR_STRING), closeSoftKeyboard());
    onView(withId(R.id.profilePhoneEditText))
        .perform(click(), typeText(INVALID_PHONE_NUM_STRING), closeSoftKeyboard());
    onView(withId(R.id.edit_confirm_button)).perform(click());

    onView(withId(R.id.profileFirstNameEditText))
        .check(matches(withHint(R.string.profile_first_name_text)));
    onView(withId(R.id.profileLastNameEditText))
        .check(matches(withHint(R.string.profile_last_name_text)));
    onView(withId(R.id.profileEmailEditText)).check(matches(withHint(R.string.profile_email_text)));
    onView(withId(R.id.profilePhoneEditText)).check(matches(withHint(R.string.profile_phone_text)));
  }

  @Test
  public void testNoEdit() {
    onView(withId(R.id.edit_profile_button)).perform(click());
    onView(withId(R.id.edit_confirm_button)).perform(click());

    onView(withId(R.id.profileFirstNameEditText))
        .check(matches(withHint(R.string.profile_first_name_text)));
    onView(withId(R.id.profileLastNameEditText))
        .check(matches(withHint(R.string.profile_last_name_text)));
    onView(withId(R.id.profileEmailEditText)).check(matches(withHint(R.string.profile_email_text)));
    onView(withId(R.id.profilePhoneEditText)).check(matches(withHint(R.string.profile_phone_text)));
  }


  @Test
  public void testRecieveRemoteChange() {
    onView(withId(R.id.profileFirstNameEditText))
        .check(matches(withHint(R.string.profile_first_name_text)));
    onView(withId(R.id.profileLastNameEditText))
        .check(matches(withHint(R.string.profile_last_name_text)));
    onView(withId(R.id.profileEmailEditText)).check(matches(withHint(R.string.profile_email_text)));
    onView(withId(R.id.profilePhoneEditText)).check(matches(withHint(R.string.profile_phone_text)));

    String uuid = UserHelper.readUuid();
    FirebaseController.getFirestore().collection(UserHelper.USER_COLLECTION).document(uuid).set(
        new User(VALID_FIRST_NAME_STRING, VALID_LAST_NAME_STRING, VALID_EMAIL_ADDR_STRING,
            VALID_PHONE_NUM_STRING, uuid));
    // Pause to make sure update happens in time
    SystemClock.sleep(2000);

    onView(withId(R.id.profileFirstNameEditText))
        .check(matches(withText(containsString(VALID_FIRST_NAME_STRING))));
    onView(withId(R.id.profileLastNameEditText))
        .check(matches(withText(containsString(VALID_LAST_NAME_STRING))));
    onView(withId(R.id.profileEmailEditText))
        .check(matches(withText(containsString(VALID_EMAIL_ADDR_STRING))));
    onView(withId(R.id.profilePhoneEditText))
        .check(matches(withText(containsString(VALID_PHONE_NUM_STRING))));

  }

}
