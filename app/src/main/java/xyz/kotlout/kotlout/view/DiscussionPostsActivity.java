package xyz.kotlout.kotlout.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.experiment.Post;
import xyz.kotlout.kotlout.model.user.User;
import xyz.kotlout.kotlout.model.adapter.PostAdapter;

public class DiscussionPostsActivity extends AppCompatActivity {

  private RecyclerView posts;
  private PostAdapter postAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_discussion_posts);

    posts = findViewById(R.id.discussion_recycler_list);
    posts.setLayoutManager(new LinearLayoutManager(this));

    ArrayList<Post> postList = new ArrayList<>();
    for (int i=0; i<100; i++){
        Post newPost = new Post();
        User user = new User();
        user.setFirstName("LOLOLOL");
        newPost.setPoster(user);
        newPost.setText("lmao");
        newPost.setTimestamp(new Date());
        postList.add(newPost);
    }

    postAdapter = new PostAdapter(postList, this);
    posts.setAdapter(postAdapter);
  }
}