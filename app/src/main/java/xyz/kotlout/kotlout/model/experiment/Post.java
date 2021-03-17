package xyz.kotlout.kotlout.model.experiment;

import java.util.Date;
import java.util.List;
import xyz.kotlout.kotlout.model.user.User;

public class Post {

  private User poster;
  private List<Post> children;
  private Post parent;
  private String text;
  private Date timestamp;
}
