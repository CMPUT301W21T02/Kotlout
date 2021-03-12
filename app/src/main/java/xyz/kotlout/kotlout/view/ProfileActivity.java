package xyz.kotlout.kotlout.view;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import xyz.kotlout.kotlout.model.user.User;

public class ProfileActivity extends AppCompatActivity {

  String uuid = LocalStorageController.readUUID();
  // Declaration and instantiation of Objects
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

  public void changeEditable() {

    firstNameText = findViewById(R.id.profileFirstNameEditText);
    lastNameText = findViewById(R.id.profileLastNameEditText);
    emailText = findViewById(R.id.profileEmailEditText);
    phoneText = findViewById(R.id.profilePhoneEditText);

    if (firstNameText.getInputType() == InputType.TYPE_NULL) {

      firstNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
      lastNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
      emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
      phoneText.setInputType(InputType.TYPE_CLASS_PHONE);

      firstNameText.setEnabled(true);
      lastNameText.setEnabled(true);
      emailText.setEnabled(true);
      phoneText.setEnabled(true);

    } else {

      firstNameText.setInputType(InputType.TYPE_NULL);
      lastNameText.setInputType(InputType.TYPE_NULL);
      emailText.setInputType(InputType.TYPE_NULL);
      phoneText.setInputType(InputType.TYPE_NULL);

      firstNameText.setEnabled(false);
      lastNameText.setEnabled(false);
      emailText.setEnabled(false);
      phoneText.setEnabled(false);
    }
  }

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

      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;

      case R.id.edit_profile_button:
        changeVisibility(optionsMenu);
        changeEditable();
        firstNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        lastNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        phoneText.setInputType(InputType.TYPE_CLASS_PHONE);
        return true;

      case R.id.edit_confirm_button:
        String newFirstName, newLastName, newEmail, newPhone;
        newFirstName = firstNameText.getText().toString();
        newLastName = lastNameText.getText().toString();
        newEmail = emailText.getText().toString();
        newPhone = phoneText.getText().toString();
        changeVisibility(optionsMenu);
        changeEditable();

        if (UserController.validateEmail(newEmail) == true
            && UserController.validatePhoneNumber(newPhone) == true) {

          firstNameText.setInputType(InputType.TYPE_NULL);
          lastNameText.setInputType(InputType.TYPE_NULL);
          emailText.setInputType(InputType.TYPE_NULL);
          phoneText.setInputType(InputType.TYPE_NULL);

          controller.updateUserData(new User(newFirstName, newLastName, newEmail, newPhone, uuid));
        } else {
          // If there are invalid fields, reset them to the firebase value
          updateCallback.accept(controller.getUser());
          Toast.makeText(this, "Invalid Entry", Toast.LENGTH_SHORT);
        }
    }
    return super.onOptionsItemSelected(item);
  }
}