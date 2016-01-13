package com.dianping.zeus.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.zeus.ui.ComplexButton;
import com.dianping.zeus.ui.ITitleBar;
import com.dianping.zeus.utils.ViewUtils;
import org.json.JSONObject;

public abstract class BaseTitleBar extends FrameLayout
  implements ITitleBar
{
  public ComplexButton mButtonLL;
  public ComplexButton mButtonLR;
  public ComplexButton mButtonRL;
  public ComplexButton mButtonRR;
  private int mCurProgress = 0;
  protected View mLyL;
  protected View mLyR;
  protected ProgressBar mPb;
  private Runnable mPbUpdateRunnable = new BaseTitleBar.1(this);
  private Handler mProgressHandler = new Handler();
  private int mRealProgress = 0;
  protected FrameLayout mTextContainer;
  public BaseTitleBar.ITitleContent mTitleContent;
  protected BaseTitleBar.OnTitleBarEventListener onTitleBarEventListener;

  public BaseTitleBar(Context paramContext)
  {
    super(paramContext);
    initViews();
  }

  public BaseTitleBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViews();
  }

  public abstract BaseTitleBar.ITitleContent createTitleContentView();

  public int getLayoutId()
  {
    return R.layout.web_title_bar;
  }

  public long getProgressDelay()
  {
    return 5L;
  }

  public Drawable getProgressDrawable()
  {
    return getContext().getResources().getDrawable(R.drawable.webview_progress_bg);
  }

  public int getProgressHeight()
  {
    return ViewUtils.dip2px(getContext(), 3.0F);
  }

  public String getWebTitle()
  {
    return this.mTitleContent.getTitleText().toString();
  }

  public void initViews()
  {
    LayoutInflater.from(getContext()).inflate(getLayoutId(), this);
    this.mTitleContent = createTitleContentView();
    this.mTextContainer = ((FrameLayout)findViewById(R.id.text_container));
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -2);
    this.mTextContainer.addView((View)this.mTitleContent, localLayoutParams);
    this.mButtonLL = ((ComplexButton)findViewById(R.id.button_ll));
    this.mButtonLR = ((ComplexButton)findViewById(R.id.button_lr));
    this.mButtonRL = ((ComplexButton)findViewById(R.id.button_rl));
    this.mButtonRR = ((ComplexButton)findViewById(R.id.button_rr));
    this.mPb = ((ProgressBar)findViewById(R.id.pb_progress));
    this.mPb.getLayoutParams().height = getProgressHeight();
    this.mPb.setProgressDrawable(getProgressDrawable());
    this.mLyL = findViewById(R.id.title_bar_left_view_container);
    this.mLyR = findViewById(R.id.title_bar_right_view_container);
    getViewTreeObserver().addOnGlobalLayoutListener(new BaseTitleBar.2(this));
  }

  public void performLLClick()
  {
    if (this.mButtonLL.isSoundEffectsEnabled())
    {
      this.mButtonLL.setSoundEffectsEnabled(false);
      this.mButtonLL.performClick();
      this.mButtonLL.setSoundEffectsEnabled(true);
      return;
    }
    this.mButtonLL.performClick();
  }

  public void setLLButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener)
  {
    this.mButtonLL.setBitmap(paramBitmap, paramOnClickListener);
    updateTitleMargins();
  }

  public void setLLButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    setLLButton(paramString, "android.resource://" + getContext().getApplicationContext().getPackageName() + "/" + paramInt, paramBoolean, paramOnClickListener);
  }

  public void setLLButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    this.mButtonLL.setIconText(paramString1, paramString2, paramBoolean, paramOnClickListener);
  }

  public void setLRButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener)
  {
    this.mButtonLR.setBitmap(paramBitmap, paramOnClickListener);
    updateTitleMargins();
  }

  public void setLRButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    setLRButton(paramString, "android.resource://" + getContext().getApplicationContext().getPackageName() + "/" + paramInt, paramBoolean, paramOnClickListener);
  }

  public void setLRButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    this.mButtonLR.setIconText(paramString1, paramString2, paramBoolean, paramOnClickListener);
  }

  public void setOnTitleBarEventListener(BaseTitleBar.OnTitleBarEventListener paramOnTitleBarEventListener)
  {
    this.onTitleBarEventListener = paramOnTitleBarEventListener;
  }

  public void setProgress(int paramInt)
  {
    if (this.mCurProgress > paramInt)
      this.mCurProgress = paramInt;
    this.mRealProgress = paramInt;
    this.mProgressHandler.removeCallbacks(this.mPbUpdateRunnable);
    this.mProgressHandler.post(this.mPbUpdateRunnable);
  }

  public void setRLButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener)
  {
    this.mButtonRL.setBitmap(paramBitmap, paramOnClickListener);
    updateTitleMargins();
  }

  public void setRLButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    setRLButton(paramString, "android.resource://" + getContext().getApplicationContext().getPackageName() + "/" + paramInt, paramBoolean, paramOnClickListener);
  }

  public void setRLButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    this.mButtonRL.setIconText(paramString1, paramString2, paramBoolean, paramOnClickListener);
  }

  public void setRRButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener)
  {
    this.mButtonRR.setBitmap(paramBitmap, paramOnClickListener);
    updateTitleMargins();
  }

  public void setRRButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    setRRButton(paramString, "android.resource://" + getContext().getApplicationContext().getPackageName() + "/" + paramInt, paramBoolean, paramOnClickListener);
  }

  public void setRRButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    this.mButtonRR.setIconText(paramString1, paramString2, paramBoolean, paramOnClickListener);
  }

  public void setTitleContentParams(JSONObject paramJSONObject)
  {
    this.mTitleContent.setTitleContentParams(paramJSONObject);
  }

  public void setWebTitle(String paramString)
  {
    this.mTitleContent.setTitleText(paramString);
  }

  public void showProgressBar(boolean paramBoolean)
  {
    ProgressBar localProgressBar = this.mPb;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localProgressBar.setVisibility(i);
      return;
    }
  }

  public void showTitleBar(boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      setVisibility(i);
      return;
    }
  }

  public void updateTitleMargins()
  {
    int i;
    if (this.mLyL.getMeasuredWidth() > this.mLyR.getMeasuredWidth())
      i = this.mLyL.getMeasuredWidth();
    while (true)
    {
      int j = this.mTitleContent.getCalculatedWidth();
      try
      {
        if (getMeasuredWidth() - i * 2 < j)
          this.mTextContainer.setPadding(this.mLyL.getMeasuredWidth() + this.mLyL.getLeft(), 0, this.mLyR.getMeasuredWidth() + getMeasuredWidth() - this.mLyR.getRight(), 0);
        while (true)
        {
          ((View)this.mTitleContent).requestLayout();
          return;
          i = this.mLyR.getMeasuredWidth() + this.mLyL.getLeft();
          break;
          this.mTextContainer.setPadding(i, 0, i, 0);
        }
      }
      catch (Exception localException)
      {
        while (true)
          localException.printStackTrace();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.zeus.widget.BaseTitleBar
 * JD-Core Version:    0.6.0
 */