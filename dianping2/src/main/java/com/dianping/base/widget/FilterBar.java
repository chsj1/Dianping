package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class FilterBar extends LinearLayout
{
  protected final View.OnClickListener handler = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (FilterBar.this.listener != null)
        FilterBar.this.listener.onClickItem(paramView.getTag(), paramView);
    }
  };
  OnItemClickListener listener;

  public FilterBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public FilterBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBackgroundResource(R.drawable.filter_bar_bg);
    setOrientation(0);
  }

  public NovaLinearLayout addItem(Object paramObject, String paramString)
  {
    if (getChildCount() > 0)
    {
      localObject = new ImageView(getContext());
      ((ImageView)localObject).setImageDrawable(getResources().getDrawable(R.drawable.filter_bar_divider));
      ((ImageView)localObject).setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
      addView((View)localObject);
    }
    Object localObject = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.filter_bar_item, this, false);
    ((NovaLinearLayout)localObject).setTag(paramObject);
    ((NovaLinearLayout)localObject).setOnClickListener(this.handler);
    addView((View)localObject);
    setItem(paramObject, paramString);
    return (NovaLinearLayout)localObject;
  }

  public void changeItemVisiable(Object paramObject, boolean paramBoolean)
  {
    paramObject = findViewWithTag(paramObject);
    if (paramBoolean)
    {
      paramObject.setVisibility(0);
      return;
    }
    paramObject.setVisibility(8);
  }

  public String getItemTextAt(int paramInt)
  {
    Object localObject = getChildAt(paramInt);
    if (localObject != null)
    {
      localObject = (TextView)((View)localObject).findViewById(16908308);
      if (localObject != null)
        return ((TextView)localObject).getText().toString();
    }
    return (String)null;
  }

  public void setItem(Object paramObject, String paramString)
  {
    paramObject = findViewWithTag(paramObject);
    if (paramObject == null);
    do
    {
      return;
      ((TextView)paramObject.findViewById(16908308)).setText(paramString);
    }
    while (!(paramObject instanceof NovaLinearLayout));
    ((NovaLinearLayout)paramObject).gaUserInfo.title = paramString;
  }

  public void setItemTextAt(String paramString, int paramInt)
  {
    Object localObject = (NovaLinearLayout)getChildAt(paramInt);
    if (localObject != null)
    {
      localObject = (TextView)((NovaLinearLayout)localObject).findViewById(16908308);
      if (localObject != null)
        ((TextView)localObject).setText(paramString);
    }
  }

  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
  {
    this.listener = paramOnItemClickListener;
  }

  public static abstract interface OnItemClickListener
  {
    public abstract void onClickItem(Object paramObject, View paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.FilterBar
 * JD-Core Version:    0.6.0
 */