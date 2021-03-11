package xyz.kotlout.kotlout.controller;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * Manages internal storage
 */
public class LocalStorageController {

  private static final String UUID_FILE_NAME = "uuid.ser";
  private static final String TAG = "LOCAL STORAGE";

  /**
   * Read uuid from storage
   *
   * @return UUID stored in internal storage
   */
  public static UUID readUUID() {
    Context ctx = ApplicationContextProvider.getAppContext();
    Log.d(TAG, "LOCAL STORAGE DIR: " + ctx.getFilesDir());
    try (
        FileInputStream uuidFileStream = new FileInputStream(
            ctx.getFilesDir().toString() + '/' + UUID_FILE_NAME);
        ObjectInputStream uuidObjStream = new ObjectInputStream(uuidFileStream)
    ) {
      return (UUID) uuidObjStream.readObject();
    } catch (FileNotFoundException e) {
      Log.d(TAG, "UUID file not found" + e.getMessage());
    } catch (ClassNotFoundException e) {
      Log.e(TAG, "UUID file exists, but data is not a valid UUID\n\t" + e.getMessage());
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
  public static void storeUUID(UUID new_uuid) {
    if (new_uuid == null) {
      Log.w(TAG, "ERROR: cannot store empty uuid");
      return;
    }
    try (
        FileOutputStream uuidFileStream = ApplicationContextProvider.getAppContext()
            .openFileOutput(UUID_FILE_NAME, Context.MODE_PRIVATE);
        ObjectOutputStream uuidObjStream = new ObjectOutputStream(uuidFileStream)
    ) {
      uuidObjStream.writeObject(new_uuid);
      Log.w(TAG, "Wrote uuid: " + new_uuid.toString() + " to Data");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
