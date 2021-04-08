package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.UserHelper;

public class NameView extends androidx.appcompat.widget.AppCompatTextView implements OnClickListener {

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

  public void setUserName(String name) {
    this.name = name;
    initNameView();
  }

  private void initNameView() {
    setText(this.name);
  }

  public void onClick(@NonNull View v) {
    Intent startActivity = new Intent(v.getContext(), ProfileActivity.class).putExtra(UserHelper.UUID_INTENT, name);
    v.getContext().startActivity(startActivity);
  }

}
