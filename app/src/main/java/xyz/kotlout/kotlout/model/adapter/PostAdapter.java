package xyz.kotlout.kotlout.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.experiment.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

  private final List<Post> postData;
  Context appContext;

  public PostAdapter(List<Post> posts, Context context) {
    this.postData = posts;
    this.appContext = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_post_comment,
        parent, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Post post = postData.get(position);
    holder.getText().setText(post.getText());
    holder.getName().setText(post.getPoster().getDisplayName());

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        appContext.getString(R.string.comment_date_format),
        Locale.CANADA
    );
    holder.getDate().setText(simpleDateFormat.format(post.getTimestamp()));

    if (post.getChildren().isEmpty()) {
      holder.getReplies().setVisibility(View.GONE);
    } else {
      if (post.getChildren().size() > 1) {
        holder.getReplies().setText(String.format(Locale.CANADA,
            "%d Replies", post.getChildren().size()));
      } else {
        holder.getReplies().setText("1 Reply");
      }
      holder.getReplies().setVisibility(View.VISIBLE);
    }
  }

  @Override
  public int getItemCount() {
    return postData.size();
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
      replies = itemView.findViewById(R.id.comment_reply_count);
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
