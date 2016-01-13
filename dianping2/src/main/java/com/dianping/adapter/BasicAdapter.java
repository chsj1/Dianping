package com.dianping.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public abstract class BasicAdapter extends BaseAdapter
{
  public static final Object EMPTY;
  public static final Object ERROR;
  public static final Object FOOT;
  public static final Object HEAD;
  public static final Object LAST_EXTRA;
  public static final Object LOADING = new Object();
  public static final Object LOCATION_ERROR;

  static
  {
    ERROR = new Object();
    HEAD = new Object();
    FOOT = new Object();
    EMPTY = new Object();
    LAST_EXTRA = new Object();
    LOCATION_ERROR = new Object();
  }

  private View initImageAndTextView(String paramString, int paramInt, View.OnClickListener paramOnClickListener, ViewGroup paramViewGroup, View paramView)
  {
    if (paramView == null)
      return null;
    paramView.setMinimumHeight(paramViewGroup.getMeasuredHeight());
    paramView.setMinimumWidth(paramViewGroup.getMeasuredWidth());
    ((ImageView)paramView.findViewById(R.id.error_bad)).setImageResource(paramInt);
    ((TextView)paramView.findViewById(R.id.error_text_bad)).setText(paramString);
    paramString = paramView.findViewById(R.id.btn_retry);
    if (paramOnClickListener == null);
    for (paramInt = 8; ; paramInt = 0)
    {
      paramString.setVisibility(paramInt);
      paramString.setOnClickListener(paramOnClickListener);
      paramString.requestFocus();
      return paramView;
    }
  }

  protected View getEmptyCryFaceView(String paramString, ViewGroup paramViewGroup, View paramView)
  {
    return getEmptyImageAndTextView(paramString, R.drawable.bad_face_lib, paramViewGroup, paramView);
  }

  protected View getEmptyImageAndTextView(String paramString, int paramInt, View.OnClickListener paramOnClickListener, ViewGroup paramViewGroup, View paramView)
  {
    View localView = null;
    if (paramView == null);
    while (true)
    {
      paramView = localView;
      if (localView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.emtpy_view_image_and_text, paramViewGroup, false);
        paramView.setTag(EMPTY);
        initImageAndTextView(paramString, paramInt, paramOnClickListener, paramViewGroup, paramView);
      }
      return paramView;
      if (paramView.getTag() != EMPTY)
        continue;
      localView = paramView;
    }
  }

  protected View getEmptyImageAndTextView(String paramString, int paramInt, ViewGroup paramViewGroup, View paramView)
  {
    return getEmptyImageAndTextView(paramString, paramInt, null, paramViewGroup, paramView);
  }

  protected TextView getEmptyView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
  {
    if (paramView == null)
      paramView = null;
    Object localObject;
    while (true)
    {
      localObject = paramView;
      if (paramView == null)
      {
        localObject = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.simple_list_item_18, paramViewGroup, false);
        ((TextView)localObject).setTag(EMPTY);
        paramViewGroup = paramViewGroup.getResources().getDrawable(R.drawable.empty_page_nothing);
        paramViewGroup.setBounds(0, 0, paramViewGroup.getIntrinsicWidth(), paramViewGroup.getIntrinsicHeight());
        ((TextView)localObject).setCompoundDrawablePadding(8);
        ((TextView)localObject).setCompoundDrawables(paramViewGroup, null, null, null);
        ((TextView)localObject).setMovementMethod(LinkMovementMethod.getInstance());
      }
      if (!TextUtils.isEmpty(paramString1))
        break;
      if (!TextUtils.isEmpty(paramString2))
        ((TextView)localObject).setText(Html.fromHtml(paramString2));
      return localObject;
      if (paramView.getTag() == EMPTY)
      {
        paramView = (TextView)paramView;
        continue;
      }
      paramView = null;
    }
    ((TextView)localObject).setText(Html.fromHtml(paramString1));
    return (TextView)localObject;
  }

  protected View getFailedView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry, ViewGroup paramViewGroup, View paramView)
  {
    if (paramView == null)
      paramView = null;
    View localView;
    while (true)
    {
      localView = paramView;
      if (paramView == null)
      {
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.error_item, paramViewGroup, false);
        localView.setTag(ERROR);
      }
      ((TextView)localView.findViewById(16908308)).setText(paramString);
      if ((localView instanceof LoadingErrorView))
        break;
      return null;
      if (paramView.getTag() == ERROR)
        continue;
      paramView = null;
    }
    ((LoadingErrorView)localView).setCallBack(paramLoadRetry);
    return localView;
  }

  protected View getLoadingView(ViewGroup paramViewGroup, View paramView)
  {
    View localView = null;
    if (paramView == null);
    while (true)
    {
      paramView = localView;
      if (localView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.loading_item, paramViewGroup, false);
        paramView.setTag(LOADING);
      }
      return paramView;
      if (paramView.getTag() != LOADING)
        continue;
      localView = paramView;
    }
  }

  protected View getLocationErrorView(String paramString, View.OnClickListener paramOnClickListener, ViewGroup paramViewGroup, View paramView)
  {
    View localView = null;
    if (paramView == null);
    while (true)
    {
      paramView = localView;
      if (localView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.emtpy_view_image_and_text, paramViewGroup, false);
        paramView.setTag(LOCATION_ERROR);
        initImageAndTextView(paramString, R.drawable.bad_face_lib, paramOnClickListener, paramViewGroup, paramView);
      }
      return paramView;
      if (paramView.getTag() != LOCATION_ERROR)
        continue;
      localView = paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.adapter.BasicAdapter
 * JD-Core Version:    0.6.0
 */