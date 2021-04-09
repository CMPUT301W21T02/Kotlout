package xyz.kotlout.kotlout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.view.MainActivity;

@RunWith(AndroidJUnit4.class)
public class ExperimentListViewTest {

  @After
  public void postTest() {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    try {
      Tasks.await(firestore.terminate());
      Tasks.await(firestore.clearPersistence());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Rule
  public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

  @Test
  public void testAddCountExperiment() {

    String description = "Volkswagen in Edmonton";
    String region = "Edmonton";
    String minimumTrials = "200";
    String typeOption = "Count";

    // Open the add experiment fragment
    onView(withId(R.id.fab_main_add_experiment))
        .perform(click());

    // Type in experiment details
    onView(withId(R.id.et_experiment_new_description))
        .perform(typeText(description));
    onView(withId(R.id.et_experiment_new_region))
        .perform(typeText(region));
    onView(withId(R.id.et_experiment_new_min_trials))
        .perform(typeText(minimumTrials), closeSoftKeyboard());

    onView(withId(R.id.sp_experiment_new_type))
        .perform(click());
    onView(withText(typeOption)).perform(click());

    // Submit
    onView(withId(R.id.btn_experiment_new_add)).perform(click());

    // Open the list group
    onView(withText("Open Experiments")).perform(click());

    onView(withId(R.id.tv_experiment_list_description)).check(matches(withText(description)));
    onView(withId(R.id.tv_experiment_list_region)).check(matches(withText(region)));
    onView(withId(R.id.tv_experiment_list_type)).check(matches(withText(typeOption)));
    onView(withId(R.id.tv_experiment_list_counter))
        .check(matches(withText(containsString(minimumTrials))));
  }
}
