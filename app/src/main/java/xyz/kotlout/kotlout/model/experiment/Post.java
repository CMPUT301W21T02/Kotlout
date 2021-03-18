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


  public User getPoster() {
    return poster;
  }

  public List<Post> getChildren() {
    return children;
  }

  public Post getParent() {
    return parent;
  }

  public String getText() {
    return text;
  }

  public Date getTimestamp() {
    return timestamp;
  }
}
