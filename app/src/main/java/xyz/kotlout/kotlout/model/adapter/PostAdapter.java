package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
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
  private final List<Post> posts;
  private final Context context;
  private final OnPostClickListener postClickListener;

  private final CollectionReference postsCollection;
  private final CollectionReference userCollection; // For names

  public PostAdapter(Context context, String experimentId, List<Post> posts, OnPostClickListener postClickListener) {
    this.context = context;
    this.posts = posts;
    this.postClickListener = postClickListener;

    FirebaseFirestore firebase = FirebaseController.getFirestore();
    postsCollection = firebase
        .collection(ExperimentController.EXPERIMENT_COLLECTION)
        .document(experimentId).collection(FirebaseController.POSTS_COLLECTION);

    userCollection = firebase
        .collection(UserController.USER_COLLECTION);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_post_comment,
        parent, false);

    ViewHolder viewHolder = new ViewHolder(view);

    viewHolder.getText().setOnClickListener(v -> postClickListener.onPostTextClick(
        posts.get(viewHolder.getAdapterPosition()).getPostId()
    ));
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Post post = posts.get(position);
    holder.getText().setText(post.getText());

    //holder.getName().setText(context.getString(R.string.default_author_name));
    userCollection.document(post.getPoster()).get().addOnSuccessListener(documentSnapshot -> {
      holder.getName().setText(context.getString(R.string.default_author_name));
      User user = documentSnapshot.toObject(User.class);
      if (user != null) {
        String displayName = user.getDisplayName();
        if (displayName != null) {
          holder.getName().setText(displayName);
        }
      }
    });

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        context.getString(R.string.comment_date_format),
        Locale.CANADA
    );

    holder.getDate().setText(simpleDateFormat.format(post.getTimestamp()));

    if (post.getParent() == null) {
      holder.getReplies().setVisibility(View.GONE);
    } else {
      holder.getReplies().setVisibility(View.VISIBLE);
      // We need to find the thing somehow.

      holder.getReplies().setText(context.getString(R.string.discussion_deleted_message));
      postsCollection.document(post.getParent()).get().addOnSuccessListener(documentSnapshot -> {
            Post parent = documentSnapshot.toObject(Post.class);
            if (parent != null) {

              holder.getReplies().setOnClickListener(v -> {
                postClickListener.onPostReplyClick(parent.getPostId());
              });

              userCollection.document(post.getPoster()).get().addOnSuccessListener(documentSnapshot1 -> {
                User user = documentSnapshot1.toObject(User.class);
                if (user != null) {
                  String displayName = user.getDisplayName();
                  if (displayName == null) {
                    displayName = context.getString(R.string.default_author_name);
                  }

                  holder.getReplies().setText(context.getString(R.string.discussion_reply_format,
                      displayName, parent.getText()));
                }
              }).addOnFailureListener(e -> holder.getReplies().setText(context.getString(R.string.discussion_reply_format,
                  context.getString(R.string.default_author_name),
                  parent.getText())));
            } else {
              holder.getReplies().setText(context.getString(R.string.discussion_deleted_message));
            }
          }
      ).addOnFailureListener(e -> holder.getReplies().setText(context.getString(R.string.discussion_deleted_message)));
    }
  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

  public interface OnPostClickListener {

    void onPostTextClick(String postUUID);

    void onPostReplyClick(String parentUUID);
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

}
