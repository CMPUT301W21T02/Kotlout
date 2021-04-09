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
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.UserHelper;
import xyz.kotlout.kotlout.model.adapter.PostAdapter;
import xyz.kotlout.kotlout.model.adapter.PostAdapter.OnPostClickListener;
import xyz.kotlout.kotlout.model.experiment.Post;

public class DiscussionPostsActivity extends AppCompatActivity implements OnPostClickListener {

  public static final String ON_EXPERIMENT_INTENT = "ON_EXPERIMENT";
  private static final String TAG = "DISCUSSION";
  private final Pattern commentReplyPattern = Pattern.compile("(@([\\S]+))?(.*)", Pattern.DOTALL);
  private final Pattern whiteSpacePattern = Pattern.compile("\\s*");
  private PostAdapter postAdapter;
  private final List<Post> postList = new ArrayList<>();
  private String experimentUUID;
  private CollectionReference postsCollection;
  private RecyclerView postsView;
  private LinearLayoutManager layoutManager;
  private EditText commentText;
  private Query sortedPosts;

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

    postsView = findViewById(R.id.discussion_recycler_list);
    layoutManager = new LinearLayoutManager(this);

    layoutManager.setStackFromEnd(true);

    postsView.setLayoutManager(layoutManager);

    postAdapter = new PostAdapter(this, experimentUUID, postList, this);
    postsView.setAdapter(postAdapter);

    TextInputLayout commentBox = findViewById(R.id.discussion_enter_question);
    commentText = commentBox.getEditText();

    commentBox.setEndIconOnClickListener((v -> {
      addComment(this.commentText.getText().toString());
    }));

    sortedPosts = postsCollection.orderBy("timestamp", Direction.ASCENDING);

//    postsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
//      postList.addAll(queryDocumentSnapshots.toObjects(Post.class));
//      postAdapter.notifyDataSetChanged();
//    });

    sortedPosts.addSnapshotListener(this, this::snapShotListener);
  }


  void addComment(String commentText) {
    Matcher matcher = commentReplyPattern.matcher(commentText);

    String parentUUID = null;
    String commentBody = "";

    if (matcher.matches()) {
      parentUUID = matcher.group(2);
      commentBody = matcher.group(3);
    }

    if (commentBody == null) {
      commentBody = "";
    }

    if (whiteSpacePattern.matcher(commentBody).matches()){
      Toast.makeText(this, "Can't send empty comment", Toast.LENGTH_SHORT).show();
      return;
    }

    Post newPost = new Post();
    newPost.setTimestamp(new Date());
    newPost.setText(commentBody);
    newPost.setPoster(UserHelper.readUuid());
    newPost.setParent(parentUUID);

    CollectionReference posts = FirebaseController.getFirestore()
        .collection(ExperimentController.EXPERIMENT_COLLECTION)
        .document(experimentUUID)
        .collection(FirebaseController.POSTS_COLLECTION);

    // Toast.makeText(this, "Posting comment...", Toast.LENGTH_SHORT).show();
    posts.add(newPost).addOnSuccessListener(documentReference -> {
          // Toast.makeText(this, "Posted!", Toast.LENGTH_SHORT).show();
        }
    );

    this.commentText.setText("");
  }

  void snapShotListener(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
    if (error != null) {
      Log.w(TAG, "Listen Failed", error);
      return;
    }

//    boolean wasBottomed = isLastItemDisplaying();
//    postList.clear();
//    postList.addAll(
//    value.getDocuments().parallelStream()
//        .map(documentSnapshot -> {return documentSnapshot.toObject(Post.class);}).collect(Collectors.toList()));
//    postAdapter.notifyDataSetChanged();
//    if (wasBottomed)
//      postsView.smoothScrollToPosition(postList.size()-1);

    // Initial
    if (postList.isEmpty()){
      for (DocumentSnapshot doc: value.getDocuments()) {
        postList.add(doc.toObject(Post.class));
      }
      postAdapter.notifyDataSetChanged();
    } else {
      // Typical
      // Being smart doesnt work at all, TODO: Why is this completely borked?
      for (DocumentChange doc : value.getDocumentChanges()) {
        switch (doc.getType()) {
          case ADDED:
            boolean wasBottomed = layoutManager.findLastVisibleItemPosition() == postAdapter.getItemCount() - 1;
            postList.add(doc.getNewIndex(), doc.getDocument().toObject(Post.class));
            postAdapter.notifyItemInserted(doc.getNewIndex());
            if (wasBottomed) {
              layoutManager.smoothScrollToPosition(postsView, null, postAdapter.getItemCount() - 1);
            }
            break;
          case REMOVED:
            postList.remove(doc.getOldIndex());
            postAdapter.notifyItemRemoved(doc.getOldIndex());
            break;
          case MODIFIED:
            Post post = doc.getDocument().toObject(Post.class);
            if (doc.getOldIndex() == doc.getNewIndex()) {
              postList.set(doc.getNewIndex(), post);
            } else {
              postList.remove(doc.getOldIndex());
              postList.add(doc.getNewIndex(), post);
              postAdapter.notifyItemMoved(doc.getOldIndex(), doc.getNewIndex());
            }
            postAdapter.notifyItemChanged(doc.getNewIndex());
            break;
        }
      }
    }
  }

  @Override
  public void onPostTextClick(String postUUID) {
    Matcher matcher = commentReplyPattern.matcher(commentText.getText().toString());
    String commentBody = null;

    if (matcher.matches()){
      commentBody = matcher.group(3);
    }
    if (commentBody == null || whiteSpacePattern.matcher(commentBody).matches()) commentBody = "";

    commentText.setText(this.getString(R.string.discussion_reply_message, postUUID, commentBody));
    commentText.setSelection(commentText.getText().length());
  }

  @Override
  public void onPostReplyClick(String parentUUID) {
    for (int i=0; i<postList.size(); i++){
      if (postList.get(i).getPostId().equals(parentUUID)) {
        layoutManager.smoothScrollToPosition(postsView, null, i);
        break;
      }
    }
  }

}