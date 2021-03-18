package xyz.kotlout.kotlout.controller;

import android.app.Application;
import android.content.Context;

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
