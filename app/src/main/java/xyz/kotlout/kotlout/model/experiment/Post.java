package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import xyz.kotlout.kotlout.model.user.User;

/**
 * A Post is a class representing a question or reply posted to an experiment's discussion forum.
 */
public class Post {

  private User poster;
  private final List<Post> children = new ArrayList<>();
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

  public void setPoster(User poster) {
    this.poster = poster;
  }

  public void setParent(Post parent) {
    this.parent = parent;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
}
