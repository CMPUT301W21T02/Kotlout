package xyz.kotlout.kotlout.controller;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
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
    UserHelper.initUserHelper(applicationContext);
    UserHelper.initalizeUser();
  }


}
