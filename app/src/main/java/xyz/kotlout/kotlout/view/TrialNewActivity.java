package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import java.util.HashMap;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ScannableController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;

public class TrialNewActivity extends AppCompatActivity {

  public static final String TAG = "Trial Activity";
  public static final int NEW_TRIAL_REQUEST = 0;
  public static final String EXPERIMENT_ID = "EXPERIMENT";
  public static final String EXPERIMENT_TYPE = "TYPE";
  public static final String TRIAL_EXTRA = "TRIAL";


  ExperimentType type;

  RadioGroup radioButtons;
  EditText textEntry;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_trial);

    Intent intent = getIntent();
    type = (ExperimentType) intent.getSerializableExtra(EXPERIMENT_TYPE);

    radioButtons = findViewById(R.id.rg_new_trial);
    textEntry = findViewById(R.id.editTextNumber);

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
    Button registerBarcode = findViewById(R.id.btn_new_trial_reg_barcode);
    Button createQrCode = findViewById(R.id.btn_new_trial_create_qrcode);
    submitTrial.setOnClickListener(this::submitTrial);
    registerBarcode.setOnClickListener(this::registerBarcode);
    createQrCode.setOnClickListener(this::createQrCode);
  }

  /**
   * Gets the current trial result as a string regardless of the experiment type Will return null if the data is invalid
   *
   * @return Trial result
   */
  private String getResult() {
    String result;
    if (textEntry.isEnabled() && textEntry.getVisibility() == View.VISIBLE && !textEntry.getText().toString().isEmpty()
        && textEntry.getText().toString().matches("\\d+")) {
      result = textEntry.getText().toString();
    } else if (radioButtons.getCheckedRadioButtonId() != -1) {
      result = radioButtons.getCheckedRadioButtonId() == R.id.radio_success ? "true" : "false";
    } else {
      // Result is not valid
      result = null;
    }
    return result;
  }

  /**
   * Creates a QrCode and opens it with a 'Send' intent to share it
   *
   * @param view the current view
   */
  private void createQrCode(View view) {
    String result = getResult();
    Bitmap qrBitmap = ScannableController.createQrBitmap(ScannableController
        .createUri(result, getIntent().getStringExtra(EXPERIMENT_ID),
            ((ExperimentType) getIntent().getSerializableExtra(EXPERIMENT_TYPE)).toString()));
    if (qrBitmap == null) {
      Toast.makeText(this, "Trial result is invalid, cannot make QR code", Toast.LENGTH_SHORT).show();
      return;
    }
    String path = MediaStore.Images.Media
        .insertImage(view.getContext().getContentResolver(), qrBitmap, "Kotlout Experiment QR Code", null);
    Uri uri = Uri.parse(path);
    Intent shareImageIntent = new Intent(Intent.ACTION_SEND);
    shareImageIntent.setType("image/jpeg");
    shareImageIntent.putExtra(Intent.EXTRA_STREAM, uri);
    view.getContext().startActivity(Intent.createChooser(shareImageIntent, "Share QR Code"));
  }

  /**
   * Opens the code scanner to read a barcode This will be used to represent the current trial
   *
   * @param view parent view
   */
  private void registerBarcode(View view) {
    if (getResult() != null) {
      Intent codeScannerIntent = new Intent(this, CodeScannerActivity.class);
      startActivityForResult(codeScannerIntent, CodeScannerActivity.SCAN_CODE_REQUEST);
    } else {
      Toast.makeText(this, "Trial result is invalid, cannot register bar code", Toast.LENGTH_SHORT).show();
    }
  }

  private void submitTrial(View v) {

    String uuid = UserHelper.readUuid();
    String userInput = textEntry.getText().toString();
    Intent intent = new Intent();
    try {
      switch (type) {

        case BINOMIAL:
          boolean result = radioButtons.getCheckedRadioButtonId() == R.id.radio_success;
          BinomialTrial newBinomialTrial = new BinomialTrial(result, uuid);
          intent.putExtra(TRIAL_EXTRA, newBinomialTrial);
          break;
        case NON_NEGATIVE_INTEGER:

          NonNegativeTrial newNonNegativeTrial = new NonNegativeTrial(Long.parseLong(userInput), uuid);
          intent.putExtra(TRIAL_EXTRA, newNonNegativeTrial);
          break;
        case COUNT:
          CountTrial newCountTrial = new CountTrial(Long.parseLong(userInput), uuid);
          intent.putExtra(TRIAL_EXTRA, newCountTrial);
          break;
        case MEASUREMENT:
          MeasurementTrial measurementTrial = new MeasurementTrial(Double.parseDouble(userInput), uuid);
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CodeScannerActivity.SCAN_CODE_REQUEST) {
      if (resultCode == RESULT_OK) {
        assert data != null;
        String codeResult = data.getStringExtra("code");
        BarcodeFormat format = (BarcodeFormat) data.getSerializableExtra("format");
        if (format != null && format != BarcodeFormat.QR_CODE) {
          storeResultAsBarcode(
              (ExperimentType) getIntent().getSerializableExtra("type"),
              getIntent().getStringExtra("experimentId"),
              codeResult);
        } else {
          Toast.makeText(this, "Invalid barcode", Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  /**
   * Creates a map of the trial data and stores it in firebase
   *
   * @param type         Experiment type
   * @param experimentId Experiment Id
   * @param barcode      barcode that will reference this experiment
   */
  private void storeResultAsBarcode(ExperimentType type, String experimentId, String barcode) {
    HashMap<String, Object> data = new HashMap<>();
    data.put("experimentId", experimentId);
    data.put("type", type);
    data.put("result", textEntry.getVisibility() == View.VISIBLE ? Long.valueOf(textEntry.getText().toString())
        : radioButtons.getCheckedRadioButtonId() == R.id.radio_success);
    ScannableController.storeResultAsBarcode(data, barcode);
  }
}

