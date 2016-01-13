package com.dianping.base.widget;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.Environment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaTextView;
import java.lang.reflect.Method;

public class TitleBar
{

  @Deprecated
  public static final int TITLE_TYPE_ARROW = 3;

  @Deprecated
  public static final int TITLE_TYPE_BUTTON_SEARCH = 5;

  @Deprecated
  public static final int TITLE_TYPE_DOUBLE_LINE = 6;

  @Deprecated
  public static final int TITLE_TYPE_DOUBLE_LINE_PROGRESS = 7;

  @Deprecated
  public static final int TITLE_TYPE_DOUBLE_TEXT_BUTTON = 9;

  @Deprecated
  public static final int TITLE_TYPE_EDIT_SEARCH = 4;

  @Deprecated
  public static final int TITLE_TYPE_FILTER = 8;

  @Deprecated
  public static final int TITLE_TYPE_NONE = 2;
  public static final int TITLE_TYPE_STANDARD = 100;

  @Deprecated
  public static final int TITLE_TYPE_WIDE = 1;
  private Style mStyle;

  private TitleBar(Style paramStyle)
  {
    this.mStyle = paramStyle;
  }

  private View addRightViewItem(String paramString, Drawable paramDrawable, View.OnClickListener paramOnClickListener)
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    if (paramDrawable == null)
      return null;
    NovaImageView localNovaImageView = new NovaImageView(this.mStyle.mActivity);
    int i = ViewUtils.dip2px(this.mStyle.mActivity, 20.0F);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(i, 0, 0, 0);
    localNovaImageView.setBackgroundResource(17170445);
    localNovaImageView.setImageDrawable(paramDrawable);
    localNovaImageView.setLayoutParams(localLayoutParams);
    localNovaImageView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
          ((ImageView)paramView).setAlpha(123);
        if ((paramMotionEvent.getAction() == 1) || (paramMotionEvent.getAction() == 3))
          ((ImageView)paramView).setAlpha(255);
        return false;
      }
    });
    return addRightViewItem(localNovaImageView, paramString, paramOnClickListener);
  }

  public static TitleBar build(Activity paramActivity, int paramInt)
  {
    switch (paramInt)
    {
    default:
      return new Style(paramActivity, paramInt)
      {
        public TitleBar build()
        {
          TitleBar localTitleBar = new TitleBar(this, null);
          this.mActivity.getWindow().setFeatureInt(7, this.val$titleTypeOrResId);
          return localTitleBar;
        }

        public void setSubTitle(CharSequence paramCharSequence)
        {
        }

        public void setTitle(CharSequence paramCharSequence)
        {
        }
      }
      .build();
    case 100:
      return new StandardStyle(paramActivity).build();
    case 1:
      return new WideStyle(paramActivity).build();
    case 3:
      return new ArrowStyle(paramActivity).build();
    case 2:
      return new NoTitleStyle(paramActivity).build();
    case 4:
      return new EditSearchStyle(paramActivity).build();
    case 5:
      return new ButtonSearchStyle(paramActivity).build();
    case 6:
      return new DoubleLineStyle(paramActivity).build();
    case 7:
      return new DoubleLineProgressStyle(paramActivity).build();
    case 9:
      return new DoubleTextStyle(paramActivity).build();
    case 8:
    }
    return new FilterStyle(paramActivity).build();
  }

  public View addRightViewItem(View paramView, String paramString, View.OnClickListener paramOnClickListener)
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    if (paramView == null)
      return null;
    paramView.setOnClickListener(paramOnClickListener);
    if (TextUtils.isEmpty(paramString))
    {
      ((StandardStyle)this.mStyle).rightViewContainer.addView(paramView, ((StandardStyle)this.mStyle).rightViewContainer.getChildCount());
      return paramView;
    }
    try
    {
      paramView.getClass().getMethod("setGAString", new Class[] { String.class }).invoke(paramView, new Object[] { paramString });
      label98: paramView.setTag(2147483647, paramString);
      paramOnClickListener = findRightViewItemByTag(paramString);
      if (paramOnClickListener != null)
      {
        i = ((StandardStyle)this.mStyle).rightViewContainer.indexOfChild(paramOnClickListener);
        ((StandardStyle)this.mStyle).rightViewContainer.removeView(paramOnClickListener);
        ((StandardStyle)this.mStyle).rightViewContainer.addView(paramView, i);
        return paramView;
      }
      int k = 0;
      int i = 0;
      while (true)
      {
        int j = k;
        if (i < ((StandardStyle)this.mStyle).rightViewContainer.getChildCount())
        {
          if (paramString.compareTo((String)((StandardStyle)this.mStyle).rightViewContainer.getChildAt(i).getTag(2147483647)) > 0)
            j = i + 1;
        }
        else
        {
          ((StandardStyle)this.mStyle).rightViewContainer.addView(paramView, j);
          return paramView;
        }
        i += 1;
      }
    }
    catch (java.lang.Exception paramOnClickListener)
    {
      break label98;
    }
  }

  public View addRightViewItem(String paramString, int paramInt, View.OnClickListener paramOnClickListener)
  {
    return addRightViewItem(paramString, this.mStyle.mActivity.getResources().getDrawable(paramInt), paramOnClickListener);
  }

  public View addRightViewItem(String paramString1, String paramString2, View.OnClickListener paramOnClickListener)
  {
    if (TextUtils.isEmpty(paramString1))
      return null;
    NovaTextView localNovaTextView = (NovaTextView)LayoutInflater.from(this.mStyle.mActivity).inflate(R.layout.title_bar_text, null, false);
    localNovaTextView.setText(paramString1);
    return addRightViewItem(localNovaTextView, paramString2, paramOnClickListener);
  }

  public View findRightViewItemByTag(String paramString)
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    Object localObject;
    if (TextUtils.isEmpty(paramString))
    {
      localObject = null;
      return localObject;
    }
    int i = 0;
    while (true)
    {
      if (i >= ((StandardStyle)this.mStyle).rightViewContainer.getChildCount())
        break label92;
      View localView = ((StandardStyle)this.mStyle).rightViewContainer.getChildAt(i);
      localObject = localView;
      if (paramString.equals(localView.getTag(2147483647)))
        break;
      i += 1;
    }
    label92: return null;
  }

  public View findViewById(int paramInt)
  {
    return this.mStyle.findViewById(paramInt);
  }

  public void hide()
  {
    if (this.mStyle.findViewById(R.id.title_bar) != null)
      ((View)this.mStyle.findViewById(R.id.title_bar).getParent()).setVisibility(8);
  }

  public void removeAllRightViewItem()
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    ((StandardStyle)this.mStyle).rightViewContainer.removeAllViews();
  }

  public void removeRightViewItem(String paramString)
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    paramString = findRightViewItemByTag(paramString);
    if (paramString != null)
      ((StandardStyle)this.mStyle).rightViewContainer.removeView(paramString);
  }

  public void setBackgournd(Drawable paramDrawable)
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    ((StandardStyle)this.mStyle).rootView.setBackgroundDrawable(paramDrawable);
  }

  public void setCustomContentView(View paramView)
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    ((StandardStyle)this.mStyle).contentViewContainer.removeAllViews();
    ((StandardStyle)this.mStyle).contentViewContainer.addView(paramView);
  }

  public void setCustomLeftView(View paramView)
  {
    if (!(this.mStyle instanceof StandardStyle))
      throw new RuntimeException("只支持TitleBar类型为TITLE_TYPE_STANDORD");
    ((StandardStyle)this.mStyle).leftViewContainer.removeAllViews();
    ((StandardStyle)this.mStyle).leftViewContainer.addView(paramView);
  }

  public void setLeftView(int paramInt, View.OnClickListener paramOnClickListener)
  {
    if ((this.mStyle instanceof StandardStyle))
      ((StandardStyle)this.mStyle).setLeftView(paramInt, paramOnClickListener);
    do
      return;
    while (this.mStyle.findViewById(R.id.left_title_button) == null);
    if (paramInt == -1)
    {
      this.mStyle.findViewById(R.id.left_title_button).setVisibility(8);
      return;
    }
    if (paramInt > 0)
      ((ImageView)this.mStyle.findViewById(R.id.left_title_button)).setImageResource(paramInt);
    this.mStyle.findViewById(R.id.left_title_button).setOnClickListener(paramOnClickListener);
  }

  public void setLeftView(View.OnClickListener paramOnClickListener)
  {
    setLeftView(0, paramOnClickListener);
  }

  public void setOnDoubleClickListener(OnDoubleClickListener paramOnDoubleClickListener)
  {
    this.mStyle.setOnDoubleClickListener(paramOnDoubleClickListener);
  }

  public void setSubTitle(CharSequence paramCharSequence)
  {
    this.mStyle.setSubTitle(paramCharSequence);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mStyle.setTitle(paramCharSequence);
  }

  public void show()
  {
    if (this.mStyle.findViewById(R.id.title_bar) != null)
      ((View)this.mStyle.findViewById(R.id.title_bar).getParent()).setVisibility(0);
  }

  @Deprecated
  private static class ArrowStyle extends TitleBar.Style
  {
    public ArrowStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.arrow_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }

  @Deprecated
  private static class ButtonSearchStyle extends TitleBar.Style
  {
    public ButtonSearchStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.button_search_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }

  @Deprecated
  private static class DoubleLineProgressStyle extends TitleBar.Style
  {
    public DoubleLineProgressStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.double_line_title_progress_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }

  @Deprecated
  private static class DoubleLineStyle extends TitleBar.Style
  {
    public DoubleLineStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.double_line_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      if ("Nexus_7".equals(Environment.source2()))
        this.rootView.findViewById(R.id.subtitle).setVisibility(8);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }

  @Deprecated
  private static class DoubleTextStyle extends TitleBar.Style
  {
    public DoubleTextStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.double_text_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }

  @Deprecated
  private static class EditSearchStyle extends TitleBar.Style
  {
    public EditSearchStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.edit_search_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }

  @Deprecated
  private static class FilterStyle extends TitleBar.Style
  {
    public FilterStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.filter_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }

  private static class NoTitleStyle extends TitleBar.Style
  {
    public NoTitleStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(1);
    }

    public TitleBar build()
    {
      return new TitleBar(this, null);
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
    }

    public void setTitle(CharSequence paramCharSequence)
    {
    }
  }

  public static abstract interface OnDoubleClickListener
  {
    public abstract void onDoubleClick();
  }

  private static class StandardStyle extends TitleBar.Style
  {
    ViewGroup contentViewContainer;
    private ImageButton leftView;
    ViewGroup leftViewContainer;
    ViewGroup rightViewContainer;
    private TextView subTitleView;
    private TextView titleView;

    public StandardStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.standard_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      this.leftViewContainer = ((ViewGroup)this.rootView.findViewById(R.id.title_bar_left_view_container));
      this.leftView = ((ImageButton)this.leftViewContainer.findViewById(R.id.left_view));
      this.contentViewContainer = ((ViewGroup)this.rootView.findViewById(R.id.title_bar_content_container));
      this.titleView = ((TextView)this.contentViewContainer.findViewById(R.id.title_bar_title));
      this.subTitleView = ((TextView)this.contentViewContainer.findViewById(R.id.title_bar_subtitle));
      this.rightViewContainer = ((ViewGroup)this.rootView.findViewById(R.id.title_bar_right_view_container));
      return localTitleBar;
    }

    public void setLeftView(int paramInt, View.OnClickListener paramOnClickListener)
    {
      if (this.leftView != null)
      {
        if (paramInt != -1)
          break label30;
        this.leftView.setVisibility(8);
      }
      while (true)
      {
        this.leftView.setOnClickListener(paramOnClickListener);
        return;
        label30: if (paramInt > 0)
        {
          this.leftView.setImageResource(paramInt);
          this.leftView.setVisibility(0);
          continue;
        }
        if (paramInt != 0)
          continue;
        this.leftView.setImageResource(R.drawable.ic_back_u);
        this.leftView.setVisibility(0);
      }
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.subTitleView != null)
      {
        this.subTitleView.setVisibility(0);
        this.subTitleView.setText(paramCharSequence);
      }
      paramCharSequence = (FrameLayout.LayoutParams)this.titleView.getLayoutParams();
      paramCharSequence.gravity = 49;
      this.titleView.setLayoutParams(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.titleView != null)
        this.titleView.setText(paramCharSequence);
    }
  }

  private static abstract class Style
  {
    TitleBar.OnDoubleClickListener doubleClickListener;
    GestureDetector gestureDetector;
    protected Activity mActivity;
    protected View rootView;

    public Style(Activity paramActivity)
    {
      this.mActivity = paramActivity;
    }

    public abstract TitleBar build();

    public View findViewById(int paramInt)
    {
      if (this.rootView == null)
        return null;
      return this.rootView.findViewById(paramInt);
    }

    public void setOnDoubleClickListener(TitleBar.OnDoubleClickListener paramOnDoubleClickListener)
    {
      if ((this.rootView == null) || (this.mActivity == null))
        return;
      this.gestureDetector = new GestureDetector(this.mActivity, new GestureListener());
      this.rootView.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          return TitleBar.Style.this.gestureDetector.onTouchEvent(paramMotionEvent);
        }
      });
      this.doubleClickListener = paramOnDoubleClickListener;
    }

    public abstract void setSubTitle(CharSequence paramCharSequence);

    public abstract void setTitle(CharSequence paramCharSequence);

    public class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
      public GestureListener()
      {
      }

      public boolean onDoubleTap(MotionEvent paramMotionEvent)
      {
        if (TitleBar.Style.this.doubleClickListener != null)
          TitleBar.Style.this.doubleClickListener.onDoubleClick();
        return true;
      }

      public boolean onDown(MotionEvent paramMotionEvent)
      {
        return true;
      }
    }
  }

  @Deprecated
  private static class WideStyle extends TitleBar.Style
  {
    public WideStyle(Activity paramActivity)
    {
      super();
      this.mActivity.getWindow().requestFeature(7);
      this.mActivity.setContentView(new ViewStub(this.mActivity));
    }

    public TitleBar build()
    {
      TitleBar localTitleBar = new TitleBar(this, null);
      this.mActivity.getWindow().setFeatureInt(7, R.layout.wide_title_bar);
      this.rootView = this.mActivity.findViewById(R.id.title_bar);
      return localTitleBar;
    }

    public void setSubTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(R.id.subtitle) == null)
        return;
      ((TextView)this.rootView.findViewById(R.id.subtitle)).setText(paramCharSequence);
    }

    public void setTitle(CharSequence paramCharSequence)
    {
      if (this.rootView.findViewById(16908310) == null)
        return;
      ((TextView)this.rootView.findViewById(16908310)).setText(paramCharSequence);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TitleBar
 * JD-Core Version:    0.6.0
 */