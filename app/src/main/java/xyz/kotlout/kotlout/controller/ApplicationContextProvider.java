package xyz.kotlout.kotlout.controller;

import android.app.Application;
import android.content.Context;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;
import xyz.kotlout.kotlout.model.user.User;

public class ApplicationContextProvider extends Application {

  private static Context applicationContext;

  public static Context getAppContext() {
    return applicationContext;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    applicationContext = getApplicationContext();
    FirebaseController.initFirestore();
  }
}
