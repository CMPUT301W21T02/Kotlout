package xyz.kotlout.kotlout.controller;

import android.app.Application;
import android.content.Context;

/**
 * The Application context provider, this provides context for the application
 * When the app is created this handles making sure prerequisite requirements
 * are fulfilled.
 */
public class ApplicationContextProvider extends Application {

  private static Context applicationContext;

  /**
   * Get the app's context
   *
   * @return the app context
   */
  public static Context getAppContext() {
    return applicationContext;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    applicationContext = getApplicationContext();
    FirebaseController.initFirestore();
    UserHelper.initUserHelper(applicationContext);
    UserHelper.initializeUser();
  }
}
