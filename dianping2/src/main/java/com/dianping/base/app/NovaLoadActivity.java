package com.dianping.base.app;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public abstract class NovaLoadActivity extends NovaActivity
{
  private ViewGroup mContentContainer;
  private ViewGroup mEmptyContainer;
  private TextView mEmptyTV;
  private View mErrorView;
  private View mLoadingView;
  private View mLocatingView;
  private boolean retrieved;

  protected View getEmptyView(String paramString)
  {
    if (this.mEmptyTV == null)
    {
      this.mEmptyTV = ((TextView)getLayoutInflater().inflate(R.layout.simple_list_item_18, null, false));
      this.mEmptyTV.setMovementMethod(LinkMovementMethod.getInstance());
      Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.mEmptyTV.setCompoundDrawablePadding(8);
      this.mEmptyTV.setCompoundDrawables(localDrawable, null, null, null);
    }
    if (!TextUtils.isEmpty(paramString))
      this.mEmptyTV.setText(Html.fromHtml(paramString));
    while (true)
    {
      return this.mEmptyTV;
      this.mEmptyTV.setText("暂无数据");
    }
  }

  protected View getErrorView()
  {
    return getLayoutInflater().inflate(R.layout.error_item, null);
  }

  protected View getLoadingView()
  {
    return getLayoutInflater().inflate(R.layout.loading_item, null);
  }

  protected View getLocatingView()
  {
    return getLayoutInflater().inflate(R.layout.locating_item, null);
  }

  protected boolean isRetrieved()
  {
    return this.retrieved;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setupView();
    this.mEmptyContainer = ((ViewGroup)findViewById(R.id.empty));
    this.mContentContainer = ((ViewGroup)findViewById(R.id.content));
    if ((this.mEmptyContainer == null) || (this.mContentContainer == null))
      throw new IllegalStateException("layout must include ViewGroup with id content & empty");
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.retrieved = paramBundle.getBoolean("retrieved", false);
  }

  protected void onResume()
  {
    super.onResume();
    if (!this.retrieved)
    {
      reloadContent();
      return;
    }
    showContent();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("retrieved", this.retrieved);
  }

  protected abstract void reloadContent();

  protected abstract void setupView();

  protected void showContent()
  {
    this.retrieved = true;
    this.mEmptyContainer.setVisibility(8);
    this.mContentContainer.setVisibility(0);
  }

  protected void showEmpty(String paramString)
  {
    this.mContentContainer.setVisibility(8);
    paramString = getEmptyView(paramString);
    if (this.mEmptyContainer.getChildAt(0) != paramString)
    {
      this.mEmptyContainer.removeAllViews();
      this.mEmptyContainer.addView(paramString);
    }
    this.mEmptyContainer.setVisibility(0);
  }

  protected void showError(String paramString)
  {
    this.retrieved = true;
    this.mContentContainer.setVisibility(8);
    if (this.mErrorView == null)
    {
      this.mErrorView = getErrorView();
      if ((this.mErrorView instanceof LoadingErrorView))
      {
        ((LoadingErrorView)this.mErrorView).setType(2);
        ((LoadingErrorView)this.mErrorView).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            NovaLoadActivity.this.startReload();
          }
        });
      }
    }
    if (!TextUtils.isEmpty(paramString))
    {
      TextView localTextView = (TextView)this.mErrorView.findViewById(16908308);
      if (localTextView != null)
        localTextView.setText(paramString);
    }
    if (this.mEmptyContainer.getChildAt(0) != this.mErrorView)
    {
      this.mEmptyContainer.removeAllViews();
      this.mEmptyContainer.addView(this.mErrorView);
    }
    this.mEmptyContainer.setVisibility(0);
  }

  protected void showLoading(String paramString)
  {
    this.mContentContainer.setVisibility(8);
    if (this.mLoadingView == null)
      this.mLoadingView = getLoadingView();
    if (!TextUtils.isEmpty(paramString))
    {
      TextView localTextView = (TextView)this.mLoadingView.findViewById(16908308);
      if (localTextView != null)
        localTextView.setText(paramString);
    }
    if (this.mEmptyContainer.getChildAt(0) != this.mLoadingView)
    {
      this.mEmptyContainer.removeAllViews();
      this.mEmptyContainer.addView(this.mLoadingView);
    }
    this.mEmptyContainer.setVisibility(0);
  }

  protected void showLocating(String paramString)
  {
    this.mContentContainer.setVisibility(8);
    if (this.mLocatingView == null)
      this.mLocatingView = getLocatingView();
    if (!TextUtils.isEmpty(paramString))
    {
      TextView localTextView = (TextView)this.mLocatingView.findViewById(16908308);
      if (localTextView != null)
        localTextView.setText(paramString);
    }
    if (this.mEmptyContainer.getChildAt(0) != this.mLocatingView)
    {
      this.mEmptyContainer.removeAllViews();
      this.mEmptyContainer.addView(this.mLocatingView);
    }
    this.mEmptyContainer.setVisibility(0);
  }

  public void startReload()
  {
    this.retrieved = false;
    reloadContent();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.NovaLoadActivity
 * JD-Core Version:    0.6.0
 */