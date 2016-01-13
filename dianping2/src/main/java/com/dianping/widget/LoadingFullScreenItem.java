package com.dianping.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;

public class LoadingFullScreenItem extends LinearLayout
{
  public LoadingFullScreenItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public LoadingFullScreenItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if (paramInt == 0)
    {
      post(new Runnable()
      {
        public void run()
        {
          ((AnimationDrawable)((ImageView)LoadingFullScreenItem.this.findViewById(R.id.anim_icon)).getDrawable()).start();
        }
      });
      return;
    }
    ((AnimationDrawable)((ImageView)findViewById(R.id.anim_icon)).getDrawable()).stop();
  }

  public void setTips(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
      ((TextView)findViewById(R.id.tips)).setText(paramCharSequence);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.LoadingFullScreenItem
 * JD-Core Version:    0.6.0
 */