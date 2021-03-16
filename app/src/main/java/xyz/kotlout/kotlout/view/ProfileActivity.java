package xyz.kotlout.kotlout.view;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.util.Consumer;
import com.google.firebase.firestore.FirebaseFirestore;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.LocalStorageController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.user.User;

public class ProfileActivity extends AppCompatActivity {

  // Getting the user
  String uuid = LocalStorageController.readUUID();

  // Declaration of Objects, instantiation of Firestore
  private EditText firstNameText, lastNameText, emailText, phoneText;
  private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
  private Menu optionsMenu;
  private UserController controller;

  /**
   * Callback function that runs whenever an update to the Firebase user is detected
   */
  private final Consumer<User> updateCallback = (user) -> {
    if(user == null) {
      user = new User();
    }
    firstNameText.setText(user.getFirstName() == null ? "": user.getFirstName());
    lastNameText.setText(user.getLastName() == null ? "": user.getLastName());
    emailText.setText(user.getEmail() == null ? "": user.getEmail());
    phoneText.setText(user.getPhoneNumber() == null ? "": user.getPhoneNumber());
  };

  /**
   * When the user launches the activity, their information should be displayed if it exists
   *
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    controller = new UserController(uuid);
    controller.setUpdateCallback(updateCallback);

    firstNameText = findViewById(R.id.profileFirstNameEditText);
    lastNameText = findViewById(R.id.profileLastNameEditText);
    emailText = findViewById(R.id.profileEmailEditText);
    phoneText = findViewById(R.id.profilePhoneEditText);

    changeEditable();
  }

  /**
   * An options relevant to the user profile will appear
   *
   * @param menu
   * @return true
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.user_profile_menu, menu);
    optionsMenu = menu;
    return true;
  }

  /**
   * If the EditTexts are editable, then they will be changed to be uneditable
   * If the EditTexts are uneditable, then they will be changed to be editable with proper input
   *
   */
  public void changeEditable() {
    firstNameText = findViewById(R.id.profileFirstNameEditText);
    lastNameText = findViewById(R.id.profileLastNameEditText);
    emailText = findViewById(R.id.profileEmailEditText);
    phoneText = findViewById(R.id.profilePhoneEditText);

    if (firstNameText.getInputType() == InputType.TYPE_NULL) {
      // Setting proper input type
      firstNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
      lastNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
      emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
      phoneText.setInputType(InputType.TYPE_CLASS_PHONE);

      // Enabling the EditTexts
      firstNameText.setEnabled(true);
      lastNameText.setEnabled(true);
      emailText.setEnabled(true);
      phoneText.setEnabled(true);

    } else {
      // Setting input to null
      firstNameText.setInputType(InputType.TYPE_NULL);
      lastNameText.setInputType(InputType.TYPE_NULL);
      emailText.setInputType(InputType.TYPE_NULL);
      phoneText.setInputType(InputType.TYPE_NULL);

      // Enabling the EditTexts
      firstNameText.setEnabled(false);
      lastNameText.setEnabled(false);
      emailText.setEnabled(false);
      phoneText.setEnabled(false);
    }
  }

  /**
   * If the editing button is visible, it will be changed to the confirmation button, and vice versa
   * @param menu
   * @return true
   */
  public boolean changeVisibility(Menu menu) {
    MenuItem edit = menu.findItem(R.id.edit_profile_button);
    MenuItem confirm = menu.findItem(R.id.edit_confirm_button);
    if (edit.isVisible()) {
      confirm.setVisible(true);
      edit.setVisible(false);
    } else {
      confirm.setVisible(false);
      edit.setVisible(true);
    }
    return true;
  }

  /**
   * Allows for the selection of items in the options menu
   *
   * @param item The menu item which was clicked
   * @return Whether the item was successfully clicked
   */
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    firstNameText = findViewById(R.id.profileFirstNameEditText);
    lastNameText = findViewById(R.id.profileLastNameEditText);
    emailText = findViewById(R.id.profileEmailEditText);
    phoneText = findViewById(R.id.profilePhoneEditText);

    switch (item.getItemId()) {

      // Makes the EditTexts editable
      case R.id.edit_profile_button:
        changeVisibility(optionsMenu);
        changeEditable();
        return true;

      // Checks for valid inputs in the EditTexts
      case R.id.edit_confirm_button:
        String newFirstName, newLastName, newEmail, newPhone;
        newFirstName = firstNameText.getText().toString();
        newLastName = lastNameText.getText().toString();
        newEmail = emailText.getText().toString();
        newPhone = phoneText.getText().toString();
        changeVisibility(optionsMenu);
        changeEditable();

        if (UserHelper.validateEmail(newEmail) && UserHelper.validatePhoneNumber(newPhone)) {
          controller.updateUserData(new User(newFirstName, newLastName, newEmail, newPhone, uuid));
        } else {
          // If there are invalid fields, reset them to the firebase value
          updateCallback.accept(controller.getUser());
          Toast.makeText(this, "Invalid Entries Detected", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
