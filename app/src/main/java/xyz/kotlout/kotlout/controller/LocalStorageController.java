package xyz.kotlout.kotlout.controller;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import xyz.kotlout.kotlout.model.user.User;

/**
 * Manages internal storage
 */
public class LocalStorageController {

  private static final String UUID_FILE_NAME = "uuid.ser";
  private static final String TAG = "LOCAL STORAGE";
  private static Context ctx;
  private static UUID uuid;

  public static UUID getUUID() {
    return uuid;
  }

  /**
   * Initialize local storage
   * Currently reads the UUID stored internally
   * Will create a UUID if none exists
   */
  public static void initLocalStorage(Context context) {
    ctx = context;
    uuid = readUUID();
    Log.d(TAG, "Loaded UUID: " + uuid.toString());
    if(uuid == null) {
      uuid = UUID.randomUUID();
      storeUUID(uuid);
    }
  }

  public static UUID readUUID() {
    Log.d(TAG, "LOCAL STORAGE DIR: " + ctx.getFilesDir());
    try (
        FileInputStream uuidFileStream = new FileInputStream(ctx.getFilesDir().toString() + '/' + UUID_FILE_NAME);
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

  public static void storeUUID(UUID new_uuid) {
    if (new_uuid == null) {
      Log.w(TAG, "ERROR: cannot store empty uuid");
      return;
    }
    try (
        FileOutputStream uuidFileStream = ctx.openFileOutput(UUID_FILE_NAME, Context.MODE_PRIVATE);
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
