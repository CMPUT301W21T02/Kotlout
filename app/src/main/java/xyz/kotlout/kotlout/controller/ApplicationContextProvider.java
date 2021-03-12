package xyz.kotlout.kotlout.controller;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;
import xyz.kotlout.kotlout.model.user.User;

public class ApplicationContextProvider extends Application {

  private static Context applicationContext;

  @Override
  public void onCreate() {
    super.onCreate();
    applicationContext = getApplicationContext();
    FirebaseFirestore firestore = FirebaseController.getFirestore();
    String curent_uuid = LocalStorageController.readUUID();
    if(curent_uuid == null) {
      User newUser = new User();
      newUser.setUuid(UUID.randomUUID().toString());
      LocalStorageController.storeUUID(newUser.getUuid());
      firestore.collection("users").document(newUser.getUuid()).set(newUser);
    }

  }

  public static Context getAppContext() {
    return applicationContext;
  }
}
