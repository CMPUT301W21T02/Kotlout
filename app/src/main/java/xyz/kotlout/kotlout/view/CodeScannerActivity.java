package xyz.kotlout.kotlout.view;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import xyz.kotlout.kotlout.R;

/**
 * Activity to Scan barcodes and QR Codes
 * Code modified from example from: https://github.com/yuriy-budiyev/code-scanner
 */
public class CodeScannerActivity extends AppCompatActivity {

  public static final int SCAN_CODE_REQUEST = 1;
  public static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
  private CodeScanner codeScanner;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_code_scanner);
    CodeScannerView scannerView = findViewById(R.id.scanner_view);
    codeScanner = new CodeScanner(this, scannerView);
    codeScanner.setDecodeCallback(result -> {
      String encodedString = result.getText();
      Intent codeIntent = new Intent();
      codeIntent.putExtra("code", encodedString);
      codeIntent.putExtra("format", result.getBarcodeFormat());
      setResult(RESULT_OK, codeIntent);
      finish();
    });
    validatePermissions();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  /**
   * Asks user for camera permissions
   */
  private void validatePermissions() {
    if (ContextCompat.checkSelfPermission(this, permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    } else {
      codeScanner.startPreview();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Unable to access camera", Toast.LENGTH_SHORT).show();
        finish();
      }
    }
  }
}
