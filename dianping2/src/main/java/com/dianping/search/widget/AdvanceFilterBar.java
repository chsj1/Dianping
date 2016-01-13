package com.dianping.search.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.HashMap;
import java.util.Map;

public class AdvanceFilterBar extends LinearLayout
{
  private Map<String, Integer> dialogPosMap = new HashMap();
  private SparseArray<NovaLinearLayout> layoutMap = new SparseArray(4);

  public AdvanceFilterBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public AdvanceFilterBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBackgroundResource(R.drawable.filter_bar_shadow_bg);
    setOrientation(0);
  }

  public void addItem(int paramInt, String paramString, FilterDialog paramFilterDialog)
  {
    Object localObject = (NovaLinearLayout)this.layoutMap.get(paramInt);
    if (paramFilterDialog != null)
      paramFilterDialog.setTag(paramString);
    this.dialogPosMap.put(paramString, Integer.valueOf(paramInt));
    if (localObject != null)
    {
      ((NovaLinearLayout)localObject).setTag(paramFilterDialog);
      return;
    }
    if (getChildCount() > 0)
    {
      paramString = new ImageView(getContext());
      paramString.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_divider));
      localObject = new LinearLayout.LayoutParams(2, ViewUtils.dip2px(getContext(), 20.0F));
      ((LinearLayout.LayoutParams)localObject).gravity = 16;
      paramString.setScaleType(ImageView.ScaleType.FIT_XY);
      paramString.setLayoutParams((ViewGroup.LayoutParams)localObject);
      addView(paramString);
    }
    paramString = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.filter_bar_item, this, false);
    paramString.setTag(paramFilterDialog);
    paramString.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        Object localObject = paramView.getTag();
        if (localObject != null)
        {
          if ((localObject instanceof SearchTwinListFilterDialog))
            ((SearchTwinListFilterDialog)localObject).setSelectedItem(((SearchTwinListFilterDialog)localObject).getSelectedItem());
          ((FilterDialog)localObject).show(paramView);
        }
      }
    });
    addView(paramString);
    this.layoutMap.put(paramInt, paramString);
  }

  public FilterDialog getItem(int paramInt)
  {
    if (this.layoutMap.get(paramInt) == null)
      return null;
    return (FilterDialog)((NovaLinearLayout)this.layoutMap.get(paramInt)).getTag();
  }

  public void setItemEffect(Object paramObject, boolean paramBoolean)
  {
    if (paramObject == null);
    do
    {
      do
      {
        do
        {
          return;
          paramObject = paramObject.toString();
        }
        while (TextUtils.isEmpty(paramObject));
        paramObject = (Integer)this.dialogPosMap.get(paramObject);
      }
      while (paramObject == null);
      paramObject = (NovaLinearLayout)this.layoutMap.get(paramObject.intValue());
    }
    while (paramObject == null);
    Resources localResources = getContext().getResources();
    if (paramBoolean);
    for (int i = localResources.getColor(R.color.tips_text_red); ; i = localResources.getColor(R.color.wm_filterbar_item_gray))
    {
      ((TextView)paramObject.findViewById(16908308)).setTextColor(i);
      return;
    }
  }

  public void setItemTitle(int paramInt, String paramString)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.layoutMap.get(paramInt);
    if (localNovaLinearLayout != null)
    {
      ((TextView)localNovaLinearLayout.findViewById(16908308)).setText(paramString);
      localNovaLinearLayout.gaUserInfo.title = paramString;
    }
  }

  public void setItemTitle(Object paramObject, String paramString)
  {
    if (paramObject == null);
    Integer localInteger;
    do
    {
      do
      {
        return;
        paramObject = paramObject.toString();
      }
      while (TextUtils.isEmpty(paramObject));
      localInteger = (Integer)this.dialogPosMap.get(paramObject);
      Log.d("debug_filter", "tag=" + paramObject + " pos=" + localInteger + " map=" + this.dialogPosMap);
    }
    while (localInteger == null);
    setItemTitle(localInteger.intValue(), paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.AdvanceFilterBar
 * JD-Core Version:    0.6.0
 */