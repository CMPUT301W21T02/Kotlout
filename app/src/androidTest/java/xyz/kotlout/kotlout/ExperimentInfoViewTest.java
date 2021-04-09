package xyz.kotlout.kotlout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.view.ExperimentViewActivity;
import xyz.kotlout.kotlout.view.MainActivity;

@RunWith(AndroidJUnit4.class)
public class ExperimentInfoViewTest {

  @Rule
  public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
      MainActivity.class);

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

  @Test
  public void testCountTrialInfo() {

    String description = "People Seen Today";
    String region = "Edmonton";
    String minimumTrials = "10";
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
    onView(withText("Open Experiments")).perform(click());

    onView(withText(description)).perform(click());

    onView(withText("TRIALS")).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("1"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("4"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(isRoot()).perform(ViewActions.pressBack());

    onView(withText(description)).perform(click());

    onView(withText("2 Trials (10 minimum)")).perform(click());

    onView(Matchers.allOf(withId(R.id.histogram), withText("1")));

    onView(Matchers.allOf(withId(R.id.histogram), withText("4")));

    onView(Matchers.allOf(withId(R.id.histogram), withText("Answers")));

    onView(ViewMatchers.withId(R.id.histogram)).perform(ViewActions.swipeUp());

    onView(Matchers.allOf(withId(R.id.mean_text_view), withText("2.5")));

    onView(Matchers.allOf(withId(R.id.median_text_view), withText("4")));

    onView(Matchers.allOf(withId(R.id.std_dev_text_view), withText("1.5")));

    onView(Matchers.allOf(withId(R.id.q1_text_view), withText("1")));

    onView(Matchers.allOf(withId(R.id.q3_text_view), withText("4")));

    onView(withText("Cumulative Count")).perform(click());

  }

  @Test
  public void testBinomialTrialInfo() {

    String description = "Did you see me today?";
    String region = "Edmonton";
    String minimumTrials = "10";
    String typeOption = "Binomial";

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
    onView(withText("Open Experiments")).perform(click());

    onView(withText(description)).perform(click());

    onView(withText("TRIALS")).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.radio_success))).perform(click());

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.radio_fail))).perform(click());

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(isRoot()).perform(ViewActions.pressBack());

    onView(withText(description)).perform(click());

    onView(withText("2 Trials (10 minimum)")).perform(click());

    onView(Matchers.allOf(withId(R.id.histogram), withText("false")));

    onView(Matchers.allOf(withId(R.id.histogram), withText("true")));

    onView(ViewMatchers.withId(R.id.histogram)).perform(ViewActions.swipeUp());

    onView(Matchers.allOf(withId(R.id.mean_text_view), withText("0.5")));

    onView(Matchers.allOf(withId(R.id.median_text_view), withText("1")));

    onView(Matchers.allOf(withId(R.id.std_dev_text_view), withText("0.5")));

    onView(Matchers.allOf(withId(R.id.q1_text_view), withText("0")));

    onView(Matchers.allOf(withId(R.id.q3_text_view), withText("1")));

    onView(Matchers.allOf(withId(R.id.trialInfo), withText("Success Per Day")));

  }

  @Test
  public void testMeasurementTrialInfo() {

    String description = "How much water (in L) did you drink today?";
    String region = "Edmonton";
    String minimumTrials = "10";
    String typeOption = "Measurement";

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
    onView(withText("Open Experiments")).perform(click());

    onView(withText(description)).perform(click());

    onView(withText("TRIALS")).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("1.5"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("1.0"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("1.00"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(isRoot()).perform(ViewActions.pressBack());

    onView(withText(description)).perform(click());

    onView(withText("3 Trials (10 minimum)")).perform(click());

    onView(ViewMatchers.withText("3 Trials (10 minimum)")).perform(ViewActions.swipeUp());

    onView(Matchers.allOf(withId(R.id.histogram), withText("1.5")));

    onView(Matchers.allOf(withId(R.id.histogram), withText("2.75")));

    onView(Matchers.allOf(withId(R.id.histogram), withText("2.0")));

    onView(Matchers.allOf(withId(R.id.mean_text_view), withText("1.1667")));

    onView(Matchers.allOf(withId(R.id.median_text_view), withText("1")));

    onView(Matchers.allOf(withId(R.id.std_dev_text_view), withText("0.2357")));

    onView(Matchers.allOf(withId(R.id.q1_text_view), withText("1")));

    onView(Matchers.allOf(withId(R.id.q3_text_view), withText("1.5")));

    onView(Matchers.allOf(withId(R.id.trialInfo), withText("Mean Per Day")));

  }

  @Test
  public void testNonNegTrialInfo() {

    String description = "How old are you?";
    String region = "Edmonton";
    String minimumTrials = "10";
    String typeOption = "Non-Negative Integer";

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
    onView(withText("Open Experiments")).perform(click());

    onView(withText(description)).perform(click());

    onView(withText("TRIALS")).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("20"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("19"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView((withId(R.id.editTextNumber))).perform(typeText("22"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    // Fail to add a trial
    onView(withId(R.id.fab_view_add_trial)).perform(click());
    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(isRoot()).perform(ViewActions.pressBack());
    onView(isRoot()).perform(ViewActions.pressBack());

    onView(withText(description)).perform(click());

    onView(withText("3 Trials (10 minimum)")).perform(click());

    onView(Matchers.allOf(withId(R.id.histogram), withText("19")));
    onView(Matchers.allOf(withId(R.id.histogram), withText("20")));
    onView(Matchers.allOf(withId(R.id.histogram), withText("19")));

    onView(ViewMatchers.withId(R.id.histogram)).perform(ViewActions.swipeUp());

    onView(Matchers.allOf(withId(R.id.mean_text_view), withText("20.3333"))).perform(click());

    onView(Matchers.allOf(withId(R.id.median_text_view), withText("20")));

    onView(Matchers.allOf(withId(R.id.std_dev_text_view), withText("1.2472")));

    onView(Matchers.allOf(withId(R.id.q1_text_view), withText("19")));

    onView(Matchers.allOf(withId(R.id.q3_text_view), withText("22")));

    onView(Matchers.allOf(withId(R.id.trialInfo), withText("Mean Per Day")));
  }

  @Test
  public void testOpenExperiment() {
    String description = "Volkswagen in Edmonton";
    String region = "Edmonton";
    String minimumTrials = "200";
    String typeOption = "Count";

    Intents.init();

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

    onView(withText(description)).perform(click());

    intended(hasComponent(ExperimentViewActivity.class.getName()));

    Intents.release();
  }
}
