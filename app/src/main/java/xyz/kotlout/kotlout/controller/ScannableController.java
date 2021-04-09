package xyz.kotlout.kotlout.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.HashMap;
import java.util.Hashtable;
import xyz.kotlout.kotlout.model.ExperimentType;
import xyz.kotlout.kotlout.model.experiment.trial.BinomialTrial;
import xyz.kotlout.kotlout.model.experiment.trial.CountTrial;
import xyz.kotlout.kotlout.model.experiment.trial.MeasurementTrial;
import xyz.kotlout.kotlout.model.experiment.trial.NonNegativeTrial;
import xyz.kotlout.kotlout.model.experiment.trial.Trial;
import xyz.kotlout.kotlout.model.geolocation.Geolocation;
import xyz.kotlout.kotlout.view.ExperimentViewActivity;
import xyz.kotlout.kotlout.view.TrialNewActivity;

public class ScannableController {

  public static final String BARCODE_COLLECTION = "barcodes";
  private static final String TAG = "Scannable Controller";
  private static final int QR_WIDTH = 500;
  private static final int QR_HEIGHT = 500;


  /**
   * Creates a URI describing a trial result
   *
   * @param result         trial result as a string
   * @param experimentId   Experiment Id as a string
   * @param experimentType Experiment Type as a string
   * @return Custom Uri that will add the specified trial when opened
   */
  public static Uri createUri(String result, String experimentId, String experimentType, @Nullable String latitude,
      @Nullable String longitude) {
    Builder qrUriBuilder = new Builder().scheme("kotlout").authority("experiments");
    qrUriBuilder.appendQueryParameter(TrialNewActivity.EXPERIMENT_ID, experimentId);
    qrUriBuilder.appendQueryParameter(TrialNewActivity.EXPERIMENT_TYPE, experimentType);
    qrUriBuilder.appendQueryParameter(TrialNewActivity.LONGITUDE, longitude != null ? longitude : "");
    qrUriBuilder.appendQueryParameter(TrialNewActivity.LATITUDE, latitude != null ? latitude : "");

    qrUriBuilder.appendQueryParameter("result", result);
    return qrUriBuilder.build();
  }

  /**
   * Creates a Qr Code bitmap from a Uri
   *
   * @return Bitmap of the Uri as a QRCode
   */
  public static Bitmap createQrBitmap(Uri qrUri) {
    Log.d(TAG, qrUri.toString());
    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap;
    hintMap = new Hashtable<>();
    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix;
    try {
      bitMatrix = qrCodeWriter.encode(qrUri.toString(), BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hintMap);
    } catch (WriterException e) {
      e.printStackTrace();
      return null;
    }
    Bitmap qrBitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Config.ARGB_8888);
    //https://www.journaldev.com/470/java-qr-code-generator-zxing-example
    for (int y = 0; y < bitMatrix.getHeight(); y++) {
      for (int x = 0; x < bitMatrix.getWidth(); x++) {
        qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
      }
    }
    return qrBitmap;
  }

  /**
   * Updates trial referenced by the barcode in firebase
   *
   * @param data    Trial data as a map
   * @param barcode Barcode that references the trial data
   */
  public static void storeResultAsBarcode(HashMap<String, Object> data, String barcode) {
    DocumentReference barcodeDoc = FirebaseController.getFirestore().collection(BARCODE_COLLECTION).document(barcode);
    barcodeDoc.set(data).addOnCompleteListener(task -> Log.i(TAG, "update barcode"));
  }

  /**
   * Adds a trial using its barcode and the data stored in firebase
   *
   * @param ctx     Parent context
   * @param barcode barcode used to create new trial
   */
  public static void addTrialFromBarcode(Context ctx, String barcode) {
    Intent addTrialIntent = new Intent(ctx, ExperimentViewActivity.class);
    FirebaseController.getFirestore().collection(BARCODE_COLLECTION).document(barcode).get().addOnSuccessListener(result -> {
      Trial trial = null;
      ExperimentType type = ExperimentType.valueOf(result.getString("type"));
      String experimentId = result.getString("experimentId");
      Double latitude, longitude;
      try {
        latitude = result.getDouble("latitude");
        longitude = result.getDouble("longitude");
      } catch (NumberFormatException e) {
        latitude = null;
        longitude = null;
      }
      String stringResult = "";
      //assert type != null;
      switch (type) {
        case BINOMIAL:
          stringResult = result.getBoolean("result") ? "true" : "false";
          break;
        case NON_NEGATIVE_INTEGER:
          stringResult = result.getLong("result").toString();
          break;
        case COUNT:
          stringResult = result.getLong("result").toString();
          break;
        case MEASUREMENT:
          stringResult = result.getDouble("result").toString();
          break;
        case UNKNOWN:
          return;
      }
      addTrialIntent.setData(ScannableController
          .createUri(stringResult, experimentId, type.toString(), latitude != null ? latitude.toString() : null,
              longitude != null ? latitude.toString() : null));
      ctx.startActivity(addTrialIntent);
    });
  }

  public static Trial getTrialFromUri(Uri trialUri) {
    Trial newTrial;
    String resultString = trialUri.getQueryParameter("result");
    ExperimentType type = ExperimentType.valueOf(trialUri.getQueryParameter("TYPE"));
    if (resultString == null) {
      // Not all parameters are defined
      Log.e(TAG, "Parameters in uri are missing, URI: " + trialUri.toString());
      return null;
    }
    try {
      switch (type) {
        case COUNT:
          newTrial = new CountTrial(Long.parseLong(resultString), UserHelper.readUuid());
          break;
        case BINOMIAL:
          newTrial = new BinomialTrial(trialUri.getBooleanQueryParameter("result", false), UserHelper.readUuid());
          break;
        case MEASUREMENT:
          newTrial = new MeasurementTrial(Double.parseDouble(resultString), UserHelper.readUuid());
          break;
        case NON_NEGATIVE_INTEGER:
          newTrial = new NonNegativeTrial(Long.parseLong(resultString), UserHelper.readUuid());
          break;
        default:
          // Error
          Log.e(TAG, "Error: unknown experiment type");
          return null;
      }
    } catch (NumberFormatException e) {
      Log.e(TAG, "Error: URI trial result is invalid. Result String: " + resultString);
      return null;
    }
    Geolocation location;
    try {
      Double latitude = Double.valueOf(trialUri.getQueryParameter(TrialNewActivity.LATITUDE));
      Double longitude = Double.valueOf(trialUri.getQueryParameter(TrialNewActivity.LONGITUDE));
      location = new Geolocation(latitude, longitude);
    } catch (NumberFormatException  | NullPointerException e) {
      location = null;
    }
    newTrial.setLocation(location);
    return newTrial;
  }
}
