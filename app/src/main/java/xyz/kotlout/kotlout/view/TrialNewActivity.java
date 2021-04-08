package xyz.kotlout.kotlout.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import org.osmdroid.util.GeoPoint;
import org.w3c.dom.Text;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.adapter.LocationAdapter;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;
import xyz.kotlout.kotlout.view.fragment.SelectLocationFragment;

public class TrialNewActivity extends AppCompatActivity implements SelectLocationFragment.OnFragmentInteractionListener {

  public static final int NEW_TRIAL_REQUEST = 0;
  public static final String EXPERIMENT_ID = "EXPERIMENT";
  public static final String EXPERIMENT_TYPE = "TYPE";
  public static final String TRIAL_EXTRA = "TRIAL";

  ExperimentType type;

  private RadioGroup radioButtons;
  private EditText textEntry;
  private CheckBox geolocationCheck;

  private Geolocation location;
  private TextView locationText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_trial);

    Intent intent = getIntent();
    type = (ExperimentType) intent.getSerializableExtra(EXPERIMENT_TYPE);

    radioButtons = findViewById(R.id.rg_new_trial);
    textEntry = findViewById(R.id.editTextNumber);
    geolocationCheck = findViewById(R.id.cb_new_trial_location);
    locationText = findViewById(R.id.text_new_trial_location);
    // TODO check geolocation required flag
    switch (type) {
      case BINOMIAL:
        textEntry.setVisibility(View.GONE);
        radioButtons.setVisibility(View.VISIBLE);
        break;
      case MEASUREMENT:
        textEntry.setHint("Decimal Number");
        textEntry.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        textEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        textEntry.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        radioButtons.setVisibility(View.GONE);
        textEntry.setVisibility(View.VISIBLE);
        break;
      case NON_NEGATIVE_INTEGER:
        textEntry.setHint("Non Negative Integer");
        textEntry.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        textEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        radioButtons.setVisibility(View.GONE);
        textEntry.setVisibility(View.VISIBLE);
        break;
      case COUNT:
        textEntry.setHint("Any Integer");
        textEntry.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
        textEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        textEntry.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        radioButtons.setVisibility(View.GONE);
        textEntry.setVisibility(View.VISIBLE);
        break;
      case UNKNOWN:
        break;
    }

    Button submitTrial = findViewById(R.id.btn_new_trial_submit);
    Button setLocation = findViewById(R.id.btn_set_geolocation);
    submitTrial.setOnClickListener(this::submitTrial);
    setLocation.setOnClickListener(this::selectGeolocation);

    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 225);
      return;
    }
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
  }

  private void submitTrial(View v) {

    String uuid = UserHelper.readUuid();
    String userInput = textEntry.getText().toString();
    Intent intent = new Intent();
    boolean useGeolocation = geolocationCheck.isChecked();
    if (location == null) {
      location = new Geolocation(53.52290725708008, -113.5255355834961);
    }
    location = (useGeolocation)? location : null;
    try {
      switch (type) {
        case BINOMIAL:
          boolean result = radioButtons.getCheckedRadioButtonId() == R.id.radio_success;
          BinomialTrial newBinomialTrial = new BinomialTrial(result, uuid, location);
          intent.putExtra(TRIAL_EXTRA, newBinomialTrial);
          break;
        case NON_NEGATIVE_INTEGER:

          NonNegativeTrial newNonNegativeTrial = new NonNegativeTrial(Long.parseLong(userInput), uuid, location);
          intent.putExtra(TRIAL_EXTRA, newNonNegativeTrial);
          break;
        case COUNT:
          CountTrial newCountTrial = new CountTrial(Long.parseLong(userInput), uuid, location);
          intent.putExtra(TRIAL_EXTRA, newCountTrial);
          break;
        case MEASUREMENT:
          MeasurementTrial measurementTrial = new MeasurementTrial(Double.parseDouble(userInput), uuid, location);
          intent.putExtra(TRIAL_EXTRA, measurementTrial);
          break;
        case UNKNOWN:
          break;
      }

      setResult(RESULT_OK, intent);
      finish();

    } catch (NumberFormatException exception) {
      textEntry.setError("Number too large");
    }
  }

  private void selectGeolocation (View v) {
    SelectLocationFragment selectLocationFragment = new SelectLocationFragment();
    if (location != null) {
      Bundle bundle = new Bundle();
      bundle.putSerializable("location", location);
      selectLocationFragment.setArguments(bundle);
    }
    selectLocationFragment.show(getSupportFragmentManager(), "SELECT_GEOLOCATION");
  }

  @Override
  public void onOkPressed(GeoPoint newPoint) {
    location = LocationAdapter.toGeolocation(newPoint);
    Log.d("onOkPressed", newPoint.toDoubleString());
    locationText.setText(newPoint.toDoubleString());
  }

  private final LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location deviceLocation) {
      if(location == null) {
        location = new Geolocation(deviceLocation.getLatitude(), deviceLocation.getLongitude());
        locationText.setText(LocationAdapter.toGeoPoint(location).toDoubleString());
      }
    }
  };
}
