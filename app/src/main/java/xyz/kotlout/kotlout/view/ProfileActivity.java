package xyz.kotlout.kotlout.view;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NavUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.LocalStorageController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.user.User;

public class ProfileActivity extends AppCompatActivity {
    // Declaration and instantiation of Objects
    private EditText firstNameText, lastNameText, emailText, phoneText;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Menu optionsMenu;
    String uuid = LocalStorageController.readUUID();
    User user = UserController.fetchUser(uuid, firestore);

    /**
     * When the user launches the activity, their information should be displayed if it exists
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstNameText = findViewById(R.id.profileFirstNameEditText);
        lastNameText = findViewById(R.id.profileLastNameEditText);
        emailText = findViewById(R.id.profileEmailEditText);
        phoneText = findViewById(R.id.profilePhoneEditText);

        changeEditable();

        if (user.getFirstName() != null && user.getFirstName() != "") {
            firstNameText.setText(user.getFirstName());
        }
        if (user.getLastName() != null && user.getFirstName() != "") {
            lastNameText.setText(user.getFirstName());
        }
        if (user.getEmail() != null && user.getFirstName() != "") {
            emailText.setText(user.getFirstName());
        }
        if (user.getPhoneNumber() != null && user.getFirstName() != "") {
            phoneText.setText(user.getFirstName());
        }
    }

    /**
     * An options relevant to the user profile will appear
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

        if (firstNameText.getInputType() == InputType.TYPE_NULL){

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
        if(edit.isVisible()) {
            confirm.setVisible(true);
            edit.setVisible(false);
        }
        else {
            confirm.setVisible(false);
            edit.setVisible(true);
        }
        return true;
    }

    /**
     * Allows for the selection of items in the options menu
     * @param item
     *  The menu item which was clicked
     * @return
     *  Whether the item was successfully clicked
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

                if (UserController.validateEmail(newEmail) == true && UserController.validatePhoneNumber(newPhone) == true) {

                    UserController.setInfo(user, newFirstName, newLastName, newEmail, newPhone);

                    firstNameText.setInputType(InputType.TYPE_NULL);
                    lastNameText.setInputType(InputType.TYPE_NULL);
                    emailText.setInputType(InputType.TYPE_NULL);
                    phoneText.setInputType(InputType.TYPE_NULL);

                    firestore.collection("users").document(user.getUuid()).set(user);

                } else {
                    Toast.makeText(this, "Invalid Entry", Toast.LENGTH_SHORT);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}