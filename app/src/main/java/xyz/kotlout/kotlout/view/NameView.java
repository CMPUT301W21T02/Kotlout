package xyz.kotlout.kotlout.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.util.jar.Attributes.Name;
import xyz.kotlout.kotlout.R;
import xyz.kotlout.kotlout.controller.UserHelper;

public class NameView extends androidx.appcompat.widget.AppCompatTextView  implements OnClickListener {

  private String name;

  public NameView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NameView);
    int count = typedArray.getIndexCount();
    try {
      for(int i = 0; i < count; ++i) {
        int attr = typedArray.getIndex(i);
        if(attr == R.styleable.NameView_name) {
          name = typedArray.getString(attr);
          initNameView();
        }
      }
    } finally {
      typedArray.recycle();
    }
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

  @Override
  public void onClick(View v) {
    Intent startActivity = new Intent(v.getContext(), ProfileActivity.class).putExtra("uuid", name);
    this.getContext().startActivity(startActivity);
  }

}
