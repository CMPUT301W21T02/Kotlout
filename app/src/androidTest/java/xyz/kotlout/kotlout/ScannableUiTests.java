package xyz.kotlout.kotlout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.OngoingStubbing;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.model.experiment.CountExperiment;
import xyz.kotlout.kotlout.view.CodeScannerActivity;
import xyz.kotlout.kotlout.view.ExperimentViewActivity;
import xyz.kotlout.kotlout.view.MainActivity;
import xyz.kotlout.kotlout.view.TrialNewActivity;

@RunWith(AndroidJUnit4.class)
public class ScannableUiTests {

  private static final int MINIMUM_TRIALS = 10;
  private static final int MAX_TIMEOUT = 1000;
  private static final String USER_ID = "eI2h8gciZtTPfjzlyMwB";
  private static final String DOC_ID = "uwuwuwuwuwuwuwuwuwuw";
  private static final String EXPERIMENT_COLLECTION = "experiments";
  private static final int TRIAL_TAB_POS = 2;

  @BeforeClass
  public static void setupScannableTest() {
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    AtomicBoolean initalized = new AtomicBoolean();
    CountExperiment experiment = new CountExperiment("UI Test Experiment", "Test Region", MINIMUM_TRIALS, false);
    experiment.setOngoing(true);
    experiment.setOwnerUuid(USER_ID);
    experiment.setPublished(false);
    firestore.collection(EXPERIMENT_COLLECTION).document(DOC_ID).set(experiment).addOnSuccessListener(task -> initalized.set(true));
    waitFor(initalized);
  }

  @AfterClass
  public static void tearDownScannableTest() {
    DocumentReference experimentDoc = FirebaseController.getFirestore().collection(EXPERIMENT_COLLECTION).document(DOC_ID);
    AtomicBoolean initalized = new AtomicBoolean();
    experimentDoc.delete().addOnSuccessListener(task -> initalized.set(true));
    waitFor(initalized);
  }

  @Test
  public void testCreateQrCode() {
    Random rand = new Random();
    Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ExperimentViewActivity.class).putExtra(TrialNewActivity.EXPERIMENT_ID, DOC_ID);
    ActivityScenario scenario = ActivityScenario.launch(intent);
    onView(withId(R.id.tl_experiment_view)).perform(openTab(TRIAL_TAB_POS));
    onView(withId(R.id.fab_view_add_trial)).perform(click());
    onView(withId(R.id.btn_new_trial_create_qrcode)).perform(click());

    onView(withId(R.id.et_trial_entry_number)).perform(click(), typeText(Long.valueOf(Math.abs(rand.nextLong())).toString()), closeSoftKeyboard());
    onView(withId(R.id.btn_new_trial_create_qrcode)).perform(click());
  }

  @Test
  public void testBarCode() {
    Random rand = new Random();
    String barcode = Long.valueOf(Math.abs(rand.nextLong())).toString();
    Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ExperimentViewActivity.class).putExtra(TrialNewActivity.EXPERIMENT_ID, DOC_ID);
    //Intent resultIntent = new Intent();
    //resultIntent.putExtra("code", barcode);
    //resultIntent.putExtra("format", BarcodeFormat.CODE_39.toString());
    //OngoingStubbing a = intending(hasComponent(hasShortClassName(".CodeScannerActivity")));
    //a.respondWith(new ActivityResult(Activity.RESULT_OK, resultIntent));
    ActivityScenario scenario = ActivityScenario.launch(intent);
    onView(withId(R.id.tl_experiment_view)).perform(openTab(TRIAL_TAB_POS));
    onView(withId(R.id.fab_view_add_trial)).perform(click());
    onView(withId(R.id.btn_new_trial_reg_barcode)).perform(click());

    onView(withId(R.id.et_trial_entry_number)).perform(click(), typeText(Long.valueOf(Math.abs(rand.nextLong())).toString()), closeSoftKeyboard());
    onView(withId(R.id.btn_new_trial_reg_barcode)).perform(click());
  }

  @Test
  public void testScannerView() {
    Random rand = new Random();
    Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), MainActivity.class);
    ActivityScenario scenario = ActivityScenario.launch(intent);
    onView(withId(R.id.scan_code_menu_button)).perform(click());
    onView(withId(R.id.scanner_view)).perform(click());
  }

  private static void waitFor(AtomicBoolean initalized) {
    int count = 0;
    final int maxIterations = 10;
    if (!initalized.get()) {
      SystemClock.sleep(MAX_TIMEOUT / maxIterations);
      Assert.assertTrue(count < maxIterations);
    }
  }
  @NonNull
  private static ViewAction openTab(final int position) {
    return new ViewAction() {
      @Override
      public Matcher<View> getConstraints() {
        return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
      }

      @Override
      public String getDescription() {
        return "Tab View with tab at index: " + position;
      }

      @Override
      public void perform(UiController uiController, View view) {
        if(view instanceof TabLayout) {
          TabLayout tabLayout = (TabLayout) view;
          TabLayout.Tab tab = tabLayout.getTabAt(position);
          if(tab != null) {
            tab.select();
          }
        }
      }
    };
  }

}
