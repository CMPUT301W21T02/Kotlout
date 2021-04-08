package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.ExperimentController;
import xyz.kotlout.kotlout.controller.FirebaseController;
import xyz.kotlout.kotlout.controller.UserController;
import xyz.kotlout.kotlout.model.experiment.Post;
import xyz.kotlout.kotlout.model.user.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

  private static final String TAG = "PostAdaptor";
  private final List<Post> posts = new ArrayList<>();
  Context appContext;
  private final CollectionReference postsCollection;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_post_comment,
        parent, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Post post = posts.get(position);
    holder.getText().setText(post.getText());

    // Hopefully using a query to fill out a field doesn't cause problems.
    FirebaseController.getFirestore().collection(UserController.USER_COLLECTION)
        .document(post.getPoster()).get().addOnSuccessListener(documentSnapshot -> {
          holder.getName().setText(documentSnapshot.toObject(User.class).getDisplayName());
    });

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        appContext.getString(R.string.comment_date_format),
        Locale.CANADA
    );
    holder.getDate().setText(simpleDateFormat.format(post.getTimestamp()));

    if (post.getParent() == null) {
      holder.getReplies().setVisibility(View.GONE);
    } else {
      // We need to find the thing somehow.
      holder.getReplies().setText("fetching...");
      postsCollection.document(post.getParent()).get().addOnSuccessListener( documentSnapshot -> {
        holder.getReplies().setText(documentSnapshot.toObject(Post.class).getText());
      }
      ).addOnFailureListener(e -> {
        holder.getReplies().setText("[deleted]");
      });
    }
  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView date;
    TextView text;
    TextView replies;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      name = itemView.findViewById(R.id.comment_name);
      date = itemView.findViewById(R.id.comment_date);
      text = itemView.findViewById(R.id.comment_text);
      replies = itemView.findViewById(R.id.comment_reply_string);
    }

    public TextView getName() {
      return name;
    }

    public TextView getDate() {
      return date;
    }

    public TextView getText() {
      return text;
    }

    public TextView getReplies() {
      return replies;
    }
  }

  public PostAdapter(String experimentId, Context context) {
    appContext = context;

    postsCollection = FirebaseController.getFirestore()
        .collection(ExperimentController.EXPERIMENT_COLLECTION)
        .document(experimentId).collection(FirebaseController.POSTS_COLLECTION);

    Query sortedPosts = postsCollection.orderBy("timestamp", Direction.DESCENDING);

    Toast.makeText(appContext, "Loading Comments...", Toast.LENGTH_SHORT).show();

    // Initial Fetch of all Comments
    sortedPosts.get().addOnSuccessListener(queryDocumentSnapshots -> {
      Toast.makeText(appContext, "Loaded Comments", Toast.LENGTH_SHORT).show();
        posts.addAll(queryDocumentSnapshots.toObjects(Post.class));
        notifyItemRangeInserted(0, posts.size());
    }).addOnFailureListener((e -> {
      Toast.makeText(appContext, "Failed to Fetch Comments", Toast.LENGTH_SHORT).show();
    }));

    // This listener keeps things synced
    sortedPosts.addSnapshotListener((value, error) -> {
      if (error != null){
        Log.w(TAG, "Listen Failed", error);
        return;
      }

      // There are 3 Types of changes, Additions, removals, and modifications.
      for (DocumentChange doc: value.getDocumentChanges()){
        if (doc.getType() == Type.ADDED) {
            posts.add(doc.getDocument().toObject(Post.class));
            notifyItemInserted(posts.size()-1);
        } else if (doc.getType() == Type.REMOVED) {
            posts.remove(doc.getOldIndex());
            notifyItemRemoved(doc.getOldIndex());
        } else if (doc.getType() == Type.MODIFIED) {
            Post newPost = doc.getDocument().toObject(Post.class);
            if (doc.getOldIndex() == doc.getNewIndex()){
              posts.set(doc.getNewIndex(), newPost);
            } else {
              posts.remove(doc.getOldIndex());
              posts.add(doc.getNewIndex(), newPost);
              notifyItemMoved(doc.getOldIndex(), doc.getNewIndex());
            }
          notifyItemChanged(doc.getNewIndex());
        }
      }
    });
  }

}
