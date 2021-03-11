package xyz.kotlout.kotlout.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.LocalStorageController;
import xyz.kotlout.kotlout.model.user.User;

/**
 * Test activity that fetches the user from storage, or generates it if one exists Used to test
 * users in firebase until the main activity UI is done
 */
public class TestUserActivity extends AppCompatActivity {

  private EditText firstNameText, lastNameText, emailText, phoneText, uuidText;
  private Button updateButton, createButton, storeButton, loadButton;
  private FirebaseFirestore firestore;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    // Used to programatically position new elements
    ConstraintLayout profileLayout = (ConstraintLayout) findViewById(R.id.profileConstraintLayout);
    ConstraintSet profileLayoutConstraints = new ConstraintSet();


    firstNameText = findViewById(R.id.profileFirstNameEditText);
    lastNameText = findViewById(R.id.profileLastNameEditText);
    emailText = findViewById(R.id.profileEmailEditText);
    phoneText = findViewById(R.id.profilePhoneEditText);
    firestore = FirebaseController.getFirestore();
    uuidText = new EditText(this);
    updateButton = new Button(this);
    createButton = new Button(this);
    storeButton = new Button(this);
    loadButton = new Button(this);

    updateButton.setText("Pull Data");
    createButton.setText("Push Data");
    storeButton.setText("Store Data");
    loadButton.setText("Load Data");
    String curent_uuid = LocalStorageController.readUUID();
    if(curent_uuid == null) {
      curent_uuid = UUID.randomUUID().toString();
      LocalStorageController.storeUUID(curent_uuid);
    }
    uuidText.setText(curent_uuid);

    updateButton.setId(View.generateViewId());
    createButton.setId(View.generateViewId());
    storeButton.setId(View.generateViewId());
    loadButton.setId(View.generateViewId());
    uuidText.setId(View.generateViewId());

    profileLayout.addView(uuidText);
    profileLayout.addView(updateButton);
    profileLayout.addView(createButton);
    profileLayout.addView(storeButton);
    profileLayout.addView(loadButton);
    profileLayoutConstraints.clone(profileLayout);

    profileLayoutConstraints
        .connect(uuidText.getId(), ConstraintSet.TOP, phoneText.getId(), ConstraintSet.BOTTOM, 20);
    profileLayoutConstraints
        .connect(uuidText.getId(), ConstraintSet.LEFT, profileLayout.getId(), ConstraintSet.LEFT,
            100);
    profileLayoutConstraints
        .connect(updateButton.getId(), ConstraintSet.LEFT, profileLayout.getId(),
            ConstraintSet.LEFT, 100);
    profileLayoutConstraints
        .connect(updateButton.getId(), ConstraintSet.TOP, uuidText.getId(), ConstraintSet.BOTTOM,
            30);
    profileLayoutConstraints.connect(createButton.getId(), ConstraintSet.LEFT, updateButton.getId(),
        ConstraintSet.RIGHT, 100);
    profileLayoutConstraints
        .connect(createButton.getId(), ConstraintSet.TOP, updateButton.getId(), ConstraintSet.TOP,
            0);
    profileLayoutConstraints
        .connect(storeButton.getId(), ConstraintSet.LEFT, createButton.getId(), ConstraintSet.RIGHT,
            100);
    profileLayoutConstraints
        .connect(storeButton.getId(), ConstraintSet.TOP, updateButton.getId(), ConstraintSet.TOP,
            0);
    profileLayoutConstraints
        .connect(loadButton.getId(), ConstraintSet.LEFT, updateButton.getId(), ConstraintSet.RIGHT,
            100);
    profileLayoutConstraints
        .connect(loadButton.getId(), ConstraintSet.TOP, updateButton.getId(), ConstraintSet.BOTTOM,
            100);

    profileLayoutConstraints.applyTo(profileLayout);

    // Store UUID in uuidText to local storage
    storeButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String currentUUID = uuidText.getText().toString();
        Log.d("ACTIVITY",
            "Current UUID " + uuidText.getText().toString() + " New UUID " + currentUUID);
        LocalStorageController.storeUUID(uuidText.getText().toString());
      }
    });

    // Pull User data from firebase for UUID in uuidText
    updateButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        fetchFirebaseData(uuidText.getText().toString());
      }
    });

    // Get uuid stored in local data
    loadButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String localUuid =  LocalStorageController.readUUID();
        if (localUuid != null) {
          Log.d("ACTIVITY", (String) localUuid);
        } else {
          Log.d("ACTIVITY", ":pensive: (null uuid)");
        }
        uuidText.setText(localUuid);
      }
    });

    // Create a new User in firebase with the information in the UI
    createButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        User createdUser = new User(
            firstNameText.getText().toString(),
            lastNameText.getText().toString(),
            emailText.getText().toString(),
            phoneText.getText().toString(),
            uuidText.getText().toString()
        );
        UpdateUser(createdUser);
//        UpdateUser(createdUser);
//        uuidText.setText(createdUser.getUuid());
      }
    });
  }

  /**
   * Get user data from firebase for a given UUID
   *
   * @param docName User document ID to read
   */
  private void fetchFirebaseData(String docName) {
    firestore.collection("users")
        .document(docName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d("FIRESTORE", "Document snapshot: " + document.getData());
            User readUser = document.toObject(User.class);
            readUser.setUuid(docName);
            updateTextFields(readUser);
          } else {
            Log.d("FIRESTORE", "Document snapshot not found");
          }
        } else {
          Log.d("FIRESTORE", "Get failed with: " + task.getException());
        }
      }
    });
  }

  /**
   * Update UI text fields to match the provided user
   *
   * @param user
   */
  private void updateTextFields(User user) {
    if (user == null) {
      Log.d("UI", "Cannot update with null user");
      return;
    }
    firstNameText.setText(user.getFirstName());
    lastNameText.setText(user.getLastName());
    emailText.setText(user.getEmail());
    phoneText.setText(user.getPhoneNumber());
  }

  /**
   * Upload user to firestore
   *
   * @param user
   */
  private void UpdateUser(User user) {
    firestore.collection("users").document(user.getUuid()).set(user);
  }

}
