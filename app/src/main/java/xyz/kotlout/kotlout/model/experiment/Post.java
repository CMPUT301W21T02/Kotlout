package xyz.kotlout.kotlout.model.experiment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import xyz.kotlout.kotlout.model.user.User;

/**
 * A Post is a class representing a question or reply posted to an experiment's discussion forum.
 */
public class Post {

  private final List<Post> children = new ArrayList<>();
  private User poster;
  private Post parent;
  private String text;
  private Date timestamp;


  public User getPoster() {
    return poster;
  }

  public void setPoster(User poster) {
    this.poster = poster;
  }

  public List<Post> getChildren() {
    return children;
  }

  public Post getParent() {
    return parent;
  }

  public void setParent(Post parent) {
    this.parent = parent;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
}
