package xyz.kotlout.kotlout.model.experiment;

import com.google.firebase.firestore.DocumentId;
import java.util.Date;

/**
 * A Post is a class representing a question or reply posted to an experiment's discussion forum. is a simple POJO class so that
 * it may be serialized onto firebase.
 */
public class Post {

  @DocumentId
  private String postId;
  private String poster;
  private String parent;
  private String text;
  private Date timestamp;

  // Empty constructor required for firebase...
  public Post() {
  }

  public Post(String postId, String poster, String parent, String text, Date timestamp) {
    this.postId = postId;
    this.poster = poster;
    this.parent = parent;
    this.text = text;
    this.timestamp = timestamp;
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(String postId) {
    this.postId = postId;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
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
