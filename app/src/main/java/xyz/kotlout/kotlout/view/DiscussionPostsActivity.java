package xyz.kotlout.kotlout.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.Distribution.BucketOptions.Linear;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.adapter.PostAdapter;
import xyz.kotlout.kotlout.model.experiment.Post;

public class DiscussionPostsActivity extends AppCompatActivity {

  public static final String ON_EXPERIMENT_INTENT = "ON_EXPERIMENT";
  private static final String TAG = "DISCUSSION";

  private PostAdapter postAdapter;
  private List<Post> postList = new ArrayList<>();
  private String experimentUUID;
  private CollectionReference postsCollection;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_discussion_posts);

    Intent intent = getIntent();
    experimentUUID = intent.getStringExtra(ON_EXPERIMENT_INTENT);

    postsCollection = FirebaseController.getFirestore()
        .collection(ExperimentController.EXPERIMENT_COLLECTION)
        .document(experimentUUID)
        .collection(FirebaseController.POSTS_COLLECTION);

    RecyclerView postsView = findViewById(R.id.discussion_recycler_list);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);

    layoutManager.setStackFromEnd(true);
    postsView.setLayoutManager(layoutManager);

    postAdapter = new PostAdapter(this, experimentUUID, postList);
    postsView.setAdapter(postAdapter);

    TextInputLayout commentBox = findViewById(R.id.discussion_enter_question);
    EditText text = commentBox.getEditText();

    commentBox.setEndIconOnClickListener((v -> {
      addComment(text.getText().toString());
      text.setText("");
    }));

    Query sortedPosts = postsCollection.orderBy("timestamp", Direction.ASCENDING);

//    postsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
//      postList.addAll(queryDocumentSnapshots.toObjects(Post.class));
//      postAdapter.notifyDataSetChanged();
//    });

    postsCollection.addSnapshotListener(this, this::snapShotListener);

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

  void snapShotListener(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error){
    if (error != null){
        Log.w(TAG, "Listen Failed", error);
        return;
    }

    postList.clear();
    postList.addAll(
    value.getDocuments().parallelStream()
        .map(documentSnapshot -> {return documentSnapshot.toObject(Post.class);}).collect(Collectors.toList()));
    postAdapter.notifyDataSetChanged();

    // Being smart doesnt work at all, TODO: Why is this completely borked?
//    for (DocumentChange doc: value.getDocumentChanges()){
//      switch (doc.getType()){
//        case ADDED:
//          postList.add(doc.getDocument().toObject(Post.class));
//          postAdapter.notifyItemInserted(postList.size()-1);
//          break;
//        case REMOVED:
//          postList.remove(doc.getOldIndex());
//          postAdapter.notifyItemRemoved(doc.getOldIndex());
//          break;
//        case MODIFIED:
//          Post post = doc.getDocument().toObject(Post.class);
//          if(doc.getOldIndex() == doc.getNewIndex()){
//            postList.set(doc.getNewIndex(), post);
//          } else {
//            postList.remove(doc.getOldIndex());
//            postList.add(doc.getNewIndex(), post);
//            postAdapter.notifyItemMoved(doc.getOldIndex(), doc.getNewIndex());
//          }
//          postAdapter.notifyItemChanged(doc.getNewIndex());
//          break;
//      }
//    }
  }


}