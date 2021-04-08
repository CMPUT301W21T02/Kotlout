package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.adapter.PostAdapter;
import xyz.kotlout.kotlout.model.experiment.Post;

public class DiscussionPostsActivity extends AppCompatActivity {

  public static final String ON_EXPERIMENT_INTENT = "ON_EXPERIMENT";

  private RecyclerView postsView;
  private PostAdapter postAdapter;
  private String experimentUUID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_discussion_posts);

    postsView = findViewById(R.id.discussion_recycler_list);
    postsView.setLayoutManager(new LinearLayoutManager(this));

    Intent intent = getIntent();
    experimentUUID = intent.getStringExtra(ON_EXPERIMENT_INTENT);

    postAdapter = new PostAdapter(experimentUUID, this);
    postsView.setAdapter(postAdapter);

    TextInputLayout commentBox = findViewById(R.id.discussion_enter_question);
    EditText text = commentBox.getEditText();

    commentBox.setEndIconOnClickListener((v -> {
      addComment(text.getText().toString());
      text.setText("");
    }));
  }


  void addComment(String commentText) {
    Post newPost = new Post();
    newPost.setTimestamp(new Date());
    newPost.setText(commentText);
    newPost.setPoster(UserHelper.readUuid());

    // TODO: No empty comments!

    CollectionReference posts = FirebaseController.getFirestore()
        .collection(ExperimentController.EXPERIMENT_COLLECTION)
        .document(experimentUUID)
        .collection(FirebaseController.POSTS_COLLECTION);

    Toast.makeText(this, "Posting comment...", Toast.LENGTH_LONG).show();
    posts.add(newPost).addOnSuccessListener( documentReference -> {
        Toast.makeText(this, "Posted!", Toast.LENGTH_SHORT).show();
      }
    );
  }
}