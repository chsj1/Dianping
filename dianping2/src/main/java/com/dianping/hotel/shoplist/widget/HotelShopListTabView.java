package com.dianping.hotel.shoplist.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class HotelShopListTabView extends LinearLayout
  implements View.OnClickListener
{
  int curIndex = -1;
  public LinearLayout layout1;
  public LinearLayout layout2;
  public LinearLayout layout3;
  TabChangeListener listener;
  TextView title1;
  TextView title2;
  TextView title3;

  public HotelShopListTabView(Context paramContext)
  {
    super(paramContext);
  }

  public HotelShopListTabView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public int getCurrentIndex()
  {
    return this.curIndex;
  }

  public void onClick(View paramView)
  {
    if (paramView == this.layout1)
      setCurIndex(0, false);
    do
    {
      return;
      if (paramView != this.layout2)
        continue;
      setCurIndex(1, false);
      return;
    }
    while (paramView != this.layout3);
    setCurIndex(2, false);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.layout1 = ((LinearLayout)findViewById(R.id.tab1));
    this.layout2 = ((LinearLayout)findViewById(R.id.tab2));
    this.layout3 = ((LinearLayout)findViewById(R.id.tab3));
    this.title1 = ((TextView)findViewById(R.id.title1));
    this.title2 = ((TextView)findViewById(R.id.title2));
    this.title3 = ((TextView)findViewById(R.id.title3));
    setCurIndex(0, false);
    this.layout1.setOnClickListener(this);
    this.layout2.setOnClickListener(this);
    if (this.layout3 != null)
      this.layout3.setOnClickListener(this);
  }

  public boolean setCurIndex(int paramInt, boolean paramBoolean)
  {
    if (this.curIndex == paramInt)
      return false;
    if (paramInt == 0)
    {
      this.curIndex = paramInt;
      this.layout1.setBackgroundResource(R.drawable.tab_left_press);
      this.title1.setTextColor(-1);
      this.layout2.setBackgroundResource(R.drawable.tab_right_normal);
      this.title2.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
      if ((this.layout3 != null) && (this.title3 != null))
      {
        this.layout3.setBackgroundResource(R.drawable.tab_mid_normal);
        this.title3.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
      }
    }
    while (true)
    {
      if (this.listener != null)
        this.listener.onTabChanged(this.curIndex);
      return true;
      if (paramInt == 1)
      {
        this.curIndex = paramInt;
        this.layout1.setBackgroundResource(R.drawable.tab_left_normal);
        this.title1.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
        this.layout2.setBackgroundResource(R.drawable.tab_right_press);
        this.title2.setTextColor(-1);
        if ((this.layout3 == null) || (this.title3 == null))
          continue;
        this.layout3.setBackgroundResource(R.drawable.tab_mid_normal);
        this.title3.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
        continue;
      }
      if (paramInt != 2)
        continue;
      if ((this.layout3 == null) || (this.title3 == null))
      {
        setCurIndex(0, false);
        return true;
      }
      this.curIndex = paramInt;
      this.layout1.setBackgroundResource(R.drawable.tab_left_normal);
      this.title1.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
      this.layout2.setBackgroundResource(R.drawable.tab_right_normal);
      this.title2.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
      this.layout3.setBackgroundResource(R.drawable.tab_mid_press);
      this.title3.setTextColor(-1);
    }
  }

  public void setLeftTitleText(String paramString)
  {
    this.title1.setText(paramString);
  }

  public void setMidTitleText(String paramString)
  {
    if ((this.layout3 == null) || (this.title3 == null))
      return;
    if (TextUtils.isEmpty(paramString))
    {
      this.title3.setText("");
      if (this.curIndex == 2)
        setCurIndex(0, false);
      this.layout3.setVisibility(8);
      paramString = this.layout1.getLayoutParams();
      paramString.width = ViewUtils.dip2px(getContext(), 80.0F);
      this.layout1.setLayoutParams(paramString);
      paramString = this.layout2.getLayoutParams();
      paramString.width = ViewUtils.dip2px(getContext(), 80.0F);
      this.layout2.setLayoutParams(paramString);
      return;
    }
    this.title3.setText(paramString);
    this.layout3.setVisibility(0);
    paramString = this.layout1.getLayoutParams();
    paramString.width = ViewUtils.dip2px(getContext(), 68.0F);
    this.layout1.setLayoutParams(paramString);
    paramString = this.layout2.getLayoutParams();
    paramString.width = ViewUtils.dip2px(getContext(), 68.0F);
    this.layout2.setLayoutParams(paramString);
  }

  public void setRightTitleText(String paramString)
  {
    this.title2.setText(paramString);
  }

  public void setTabChangeListener(TabChangeListener paramTabChangeListener)
  {
    this.listener = paramTabChangeListener;
  }

  public static abstract interface TabChangeListener
  {
    public abstract void onTabChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.widget.HotelShopListTabView
 * JD-Core Version:    0.6.0
 */