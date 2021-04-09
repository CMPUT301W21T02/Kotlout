package xyz.kotlout.kotlout.model.experiment;

import com.google.firebase.firestore.DocumentId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import xyz.kotlout.kotlout.model.user.User;

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

  /**
   * Empty firebase constructor
   */
  public Post() {
  }

  /**
   * Full assignment constructor
   *
   * @param postId    Document ID in firebase
   * @param poster    UUID of User who created this post
   * @param parent    UUID of the post this is a reply to, must be in the same collection,if null this is top-level.
   * @param text      Body of the post
   * @param timestamp the timestamp
   */
  public Post(String postId, String poster, String parent, String text, Date timestamp) {
    this.postId = postId;
    this.poster = poster;
    this.parent = parent;
    this.text = text;
    this.timestamp = timestamp;
  }

  /**
   * Gets post id.
   *
   * @return the post id
   */
  public String getPostId() {
    return postId;
  }

  /**
   * Sets post id.
   *
   * @param postId the post id
   */
  public void setPostId(String postId) {
    this.postId = postId;
  }

  /**
   * Gets poster.
   *
   * @return the poster
   */
  public String getPoster() {
    return poster;
  }

  /**
   * Sets poster.
   *
   * @param poster the poster
   */
  public void setPoster(String poster) {
    this.poster = poster;
  }

  /**
   * Gets parent.
   *
   * @return the parent
   */
  public String getParent() {
    return parent;
  }

  /**
   * Sets parent.
   *
   * @param parent the parent
   */
  public void setParent(String parent) {
    this.parent = parent;
  }

  /**
   * Gets text.
   *
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Sets text.
   *
   * @param text the text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * Sets timestamp.
   *
   * @param timestamp the timestamp
   */
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
}
