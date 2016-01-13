package com.dianping.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.NovaLinearLayout;

public class LoadingErrorView extends NovaLinearLayout
  implements View.OnClickListener
{
  static final String DEFAULT_ERROR_MESSAGE = "网络连接失败 点击重新加载";
  LoadRetry callback;
  TextView errorText;
  int type = 1;

  public LoadingErrorView(Context paramContext)
  {
    super(paramContext);
  }

  public LoadingErrorView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (this.callback != null)
      this.callback.loadRetry(paramView);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.errorText = ((TextView)findViewById(16908308));
    this.errorText.setText("网络连接失败 点击重新加载");
    Drawable localDrawable;
    if (this.type == 2)
    {
      localDrawable = getResources().getDrawable(R.drawable.icon_loading_big);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.errorText.setCompoundDrawables(null, localDrawable, null, null);
    }
    while (true)
    {
      setOnClickListener(this);
      return;
      localDrawable = getResources().getDrawable(R.drawable.icon_loading_small);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.errorText.setCompoundDrawables(localDrawable, null, null, null);
    }
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }

  public void setCallBack(LoadRetry paramLoadRetry)
  {
    this.callback = paramLoadRetry;
  }

  public void setErrorMessage(String paramString)
  {
    if (this.errorText != null)
      this.errorText.setText(paramString);
  }

  public void setType(int paramInt)
  {
    Drawable localDrawable;
    if (paramInt == 2)
    {
      localDrawable = getResources().getDrawable(R.drawable.icon_loading_big);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.errorText.setCompoundDrawables(null, localDrawable, null, null);
    }
    while (true)
    {
      this.type = 2;
      return;
      localDrawable = getResources().getDrawable(R.drawable.icon_loading_small);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.errorText.setCompoundDrawables(localDrawable, null, null, null);
    }
  }

  public static abstract interface LoadRetry
  {
    public abstract void loadRetry(View paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.LoadingErrorView
 * JD-Core Version:    0.6.0
 */