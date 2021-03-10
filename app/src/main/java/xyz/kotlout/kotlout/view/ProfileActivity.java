package xyz.kotlout.kotlout.view;

import android.content.ClipData.Item;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NavUtils;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.user.User;

public class ProfileActivity extends AppCompatActivity {
    View confirm = findViewById(R.id.edit_confirm_button);
    View edit = findViewById(R.id.edit_profile_button);
    EditText firstName = findViewById(R.id.profileFirstNameEditText);
    EditText lastName = findViewById(R.id.profileLastNameEditText);
    EditText email = findViewById(R.id.profileEmailEditText);
    EditText phone = findViewById(R.id.profilePhoneEditText);
    //TODO: get user info from firebase
    User user;
    UserController userController = new UserController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (user.getFirstName() != null && user.getFirstName() != "") {
            firstName.setText(user.getFirstName());
        }
        if (user.getLastName() != null && user.getFirstName() != "") {
            lastName.setText(user.getFirstName());
        }
        if (user.getEmail() != null && user.getFirstName() != "") {
            email.setText(user.getFirstName());
        }
        if (user.getPhoneNumber() != null && user.getFirstName() != "") {
            phone.setText(user.getFirstName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.edit_profile_button:
                confirm.setVisibility(View.VISIBLE);
                edit.setVisibility(View.INVISIBLE);
                firstName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                lastName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                phone.setInputType(InputType.TYPE_CLASS_PHONE);
                return true;

            case R.id.edit_confirm_button:
                confirm.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.VISIBLE);
                String newFirstName, newLastName, newEmail, newPhone;
                newFirstName = firstName.getText().toString();
                newLastName = lastName.getText().toString();
                newEmail = email.getText().toString();
                newPhone = phone.getText().toString();

                // I think the setting should be done through the controller, but I'm not 100% sure
                user.setFirstName(newFirstName);
                user.setLastName(newLastName);
                user.setEmail(newEmail);
                user.setPhoneNumber(newPhone);

                firstName.setInputType(InputType.TYPE_NULL);
                lastName.setInputType(InputType.TYPE_NULL);
                email.setInputType(InputType.TYPE_NULL);
                phone.setInputType(InputType.TYPE_NULL);
                //TODO: Add logic to push user profile changes to db
        }
        return super.onOptionsItemSelected(item);
    }
}