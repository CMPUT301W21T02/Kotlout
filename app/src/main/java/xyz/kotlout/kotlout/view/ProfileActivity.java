package xyz.kotlout.kotlout.view;

import android.content.ClipData.Item;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NavUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.UUID;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.LocalStorageController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.user.User;

public class ProfileActivity extends AppCompatActivity {
    // Declaration and instantiation of Objects
    View confirm = findViewById(R.id.edit_confirm_button);
    View edit = findViewById(R.id.edit_profile_button);
    EditText firstName = findViewById(R.id.profileFirstNameEditText);
    EditText lastName = findViewById(R.id.profileLastNameEditText);
    EditText email = findViewById(R.id.profileEmailEditText);
    EditText phone = findViewById(R.id.profilePhoneEditText);
    private FirebaseFirestore firestore;
    String uuid = LocalStorageController.readUUID().toString();
    // I think Amir/Tharidu are gonna add a feature to UserController after they change the UUID class
    User user = UserController.fetchUser(uuid, firestore);


    /**
     * When the user launches the activity, their information should be displayed if it exists
     * @param savedInstanceState
     */
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

    /**
     * An options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    /**
     * Allows for the selection of items in the options menu
     * @param item
     * @return
     */
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
                String newFirstName, newLastName, newEmail, newPhone;
                newFirstName = firstName.getText().toString();
                newLastName = lastName.getText().toString();
                newEmail = email.getText().toString();
                newPhone = phone.getText().toString();

                if (UserController.validateEmail(newEmail) == true && UserController.validatePhoneNumber(newPhone) == true) {
                    // I think the setting should be done through the controller, but I'm not 100% sure
                    confirm.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.VISIBLE);

                    UserController.setInfo(user, newFirstName, newLastName, newEmail, newPhone);

                    firstName.setInputType(InputType.TYPE_NULL);
                    lastName.setInputType(InputType.TYPE_NULL);
                    email.setInputType(InputType.TYPE_NULL);
                    phone.setInputType(InputType.TYPE_NULL);

                    firestore.collection("users").document(user.getUuid().toString()).set(user);
                } else {
                    Toast.makeText(this, "Invalid Entry", Toast.LENGTH_SHORT);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}