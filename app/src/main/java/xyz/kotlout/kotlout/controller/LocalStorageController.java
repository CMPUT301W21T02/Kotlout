package xyz.kotlout.kotlout.controller;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Manages internal storage
 */
public final class LocalStorageController {

  private static final String UUID_FILE_NAME = "uuid.dat";
  private static final String TAG = "LOCAL STORAGE";

  /**
   * Read uuid from storage
   *
   * @return UUID stored in internal storage
   */
  public static String readUUID() {
    Context ctx = ApplicationContextProvider.getAppContext();
    Log.d(TAG, "LOCAL STORAGE DIR: " + ctx.getFilesDir());
    try (
        FileInputStream uuidFileStream = new FileInputStream(
            ctx.getFilesDir().toString() + '/' + UUID_FILE_NAME);
        DataInputStream uuidInputStream = new DataInputStream(uuidFileStream);
        BufferedReader uuidBuffer = new BufferedReader(new InputStreamReader(uuidInputStream))
    ) {
      String uuidString = uuidBuffer.readLine();
      if (uuidBuffer.readLine() == null) {
        return (String) uuidString;
      } else {
        throw new RuntimeException("File contains extra lines");
      }

    } catch (FileNotFoundException e) {
      Log.d(TAG, "UUID file not found" + e.getMessage());
    } catch (RuntimeException e) {
      Log.e(TAG, "UUID file exists, but data is not a valid UUID\n\t" + e.getMessage());
      return null;
      //return "Please don't edit the uuid file :'(..." + UUID.randomUUID().toString();
    } catch (IOException e) {
      Log.e(TAG, "General IO exception:\n\t" + e.getMessage());
    }
    return null;
  }

  /**
   * Store a given uuid into local storage
   *
   * @param new_uuid UUID to store
   */
  public static void storeUUID(String new_uuid) {
    if (new_uuid == null || new_uuid.isEmpty()) {
      Log.w(TAG, "ERROR: cannot store empty uuid");
      return;
    }
    try (
        FileOutputStream uuidFileStream = ApplicationContextProvider.getAppContext()
            .openFileOutput(UUID_FILE_NAME, Context.MODE_PRIVATE)
    ) {
      uuidFileStream.write(new_uuid.getBytes(Charset.defaultCharset()));
      Log.w(TAG, "Wrote uuid: " + new_uuid + " to Data");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
