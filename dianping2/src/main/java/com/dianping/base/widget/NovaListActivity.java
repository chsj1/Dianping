package com.dianping.base.widget;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.lang.ref.WeakReference;

public class NovaListActivity extends NovaActivity
{
  protected String defEmptyMsg;
  protected TextView emptyTV;
  protected FrameLayout emptyView;
  private WeakReference<View> errorView;
  protected ListView listView;
  private WeakReference<View> loadingView;

  protected View getFailedView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry)
  {
    Object localObject1 = this.errorView;
    if (localObject1 == null);
    Object localObject2;
    for (localObject1 = null; ; localObject1 = (View)((WeakReference)localObject1).get())
    {
      localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = getLayoutInflater().inflate(R.layout.error_item, this.emptyView, false);
        this.errorView = new WeakReference(localObject2);
      }
      ((TextView)((View)localObject2).findViewById(16908308)).setText(paramString);
      if ((localObject2 instanceof LoadingErrorView))
        break;
      return null;
    }
    ((LoadingErrorView)localObject2).setCallBack(paramLoadRetry);
    return (View)(View)localObject2;
  }

  protected View getLoadingView()
  {
    Object localObject1 = this.loadingView;
    if (localObject1 == null);
    for (localObject1 = null; ; localObject1 = (View)((WeakReference)localObject1).get())
    {
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = getLayoutInflater().inflate(R.layout.loading_item, this.emptyView, false);
        this.loadingView = new WeakReference(localObject2);
      }
      return localObject2;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
      this.defEmptyMsg = paramBundle.getString("defEmptyMsg");
    setupView();
    setEmptyView();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("defEmptyMsg", this.defEmptyMsg);
    super.onSaveInstanceState(paramBundle);
  }

  protected void setEmptyMsg(String paramString, boolean paramBoolean)
  {
    if (this.emptyTV == null)
    {
      this.emptyTV = ((TextView)getLayoutInflater().inflate(R.layout.simple_list_item_18, this.emptyView, false));
      this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    }
    Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
    if ((paramBoolean) && (!TextUtils.isEmpty(paramString)))
      this.defEmptyMsg = paramString;
    while (true)
    {
      if (!TextUtils.isEmpty(this.defEmptyMsg))
        this.emptyTV.setText(Html.fromHtml(this.defEmptyMsg));
      if (this.emptyView.getChildAt(0) != this.emptyTV)
      {
        this.emptyView.removeAllViews();
        this.emptyView.addView(this.emptyTV);
      }
      return;
      if (!TextUtils.isEmpty(this.defEmptyMsg))
        continue;
      this.defEmptyMsg = paramString;
    }
  }

  protected void setEmptyView()
  {
    this.listView = ((ListView)findViewById(R.id.list));
    this.emptyView = ((FrameLayout)findViewById(R.id.empty));
    this.listView.setEmptyView(this.emptyView);
  }

  protected void setupView()
  {
    super.setContentView(R.layout.list_frame);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.NovaListActivity
 * JD-Core Version:    0.6.0
 */