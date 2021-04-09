package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.model.user.User;
import xyz.kotlout.kotlout.view.fragment.ProfileSheetFragment;

public class NameView extends androidx.appcompat.widget.AppCompatTextView implements OnClickListener {

  private User user;
  private String name;

  public NameView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NameView);
    int count = typedArray.getIndexCount();
    try {
      for (int i = 0; i < count; ++i) {
        int attr = typedArray.getIndex(i);
        if (attr == R.styleable.NameView_name) {
          name = typedArray.getString(attr);
          initNameView();
        }
      }
    } finally {
      typedArray.recycle();
    }
    this.setOnClickListener(this);
  }

  public NameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initNameView();
  }

  public void setUser(User user) {
    this.user = user;
    this.name = user.getDisplayName();
    initNameView();
  }

  private void initNameView() {
    setText(this.name);
  }

  public void onClick(View v) {
    ProfileSheetFragment profile = ProfileSheetFragment.newInstance(user);
    profile.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "NameView");
  }

}
