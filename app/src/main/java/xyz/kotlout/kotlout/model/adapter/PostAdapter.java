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

/**
 * This class adapts a list of posts such that it appears correctly in
 * a recyclerview.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

  private static final String TAG = "PostAdaptor";
  private final List<Post> posts;
  private final Context context;
  private final OnPostClickListener postClickListener;
  private final CollectionReference postsCollection; // Used to fetch user posts
  private final CollectionReference userCollection; // Used to fetch user names

  /**
   * Constructs a Post adaptor
   * @param context Context, needed for referencing resources.
   * @param experimentId UUID of an experiment, needed to fetch collection information.
   * @param posts A list of Posts to display.
   * @param postClickListener Callback for views being clicked.
   */
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

    // Set the comment body in a post
    holder.getText().setText(post.getText());

    //
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

      holder.getReplies().setText(context.getString(R.string.discussion_fetching));
      postsCollection.document(post.getParent()).get().addOnSuccessListener(documentSnapshot -> {
            Post parent = documentSnapshot.toObject(Post.class);
            if (parent != null) {

              holder.getReplies().setOnClickListener(v -> postClickListener.onPostReplyClick(parent.getPostId()));

              holder.getReplies().setText(context.getString(R.string.discussion_reply_format,
                  context.getString(R.string.default_author_name),
                  parent.getText()));

              userCollection.document(parent.getPoster()).get().addOnSuccessListener(documentSnapshotParentUser -> {
                User parentUser = documentSnapshotParentUser.toObject(User.class);
                if (parentUser != null) {
                  String parentDisplayName = parentUser.getDisplayName();
                  if (parentDisplayName == null) {
                    parentDisplayName = context.getString(R.string.default_author_name);
                  }

                  holder.getReplies().setText(context.getString(R.string.discussion_reply_format,
                      parentDisplayName, parent.getText()));
                }
              });
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
