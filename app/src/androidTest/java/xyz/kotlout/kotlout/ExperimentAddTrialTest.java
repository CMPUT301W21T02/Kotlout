package xyz.kotlout.kotlout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.view.MainActivity;

@RunWith(AndroidJUnit4.class)
public class ExperimentAddTrialTest {

  @Rule
  public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
      MainActivity.class);

  @Test
  public void testAddCountTrial() {

    String description = "Birds Seen Today";
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

    onView((withId(R.id.et_trial_entry_number))).perform(typeText("1"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withText("Me")).perform(click());

    onView(withText("1")).perform(click());

    // Bad submission -> good submission test
    onView(withId(R.id.fab_view_add_trial)).perform(click());

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView((withId(R.id.et_trial_entry_number))).perform(typeText("1"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

  }

  @Test
  public void testAddBinomialTrial() {

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

    onView(withText("Me")).perform(click());

    onView(withText("Pass")).perform(click());

  }

  @Test
  public void testAddMeasurementTrial() {

    String description = "How far (km) did you walk today?";
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

    onView((withId(R.id.et_trial_entry_number))).perform(typeText("10"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withText("Me")).perform(click());

    onView(withText("10.0")).perform(click());

  }

  @Test
  public void testAddNonNegTrial() {

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

    onView((withId(R.id.et_trial_entry_number))).perform(typeText("20"));

    onView(withId(R.id.btn_new_trial_submit)).perform(click());

    onView(withText("Me")).perform(click());

    onView(withText("20")).perform(click());

  }
}
