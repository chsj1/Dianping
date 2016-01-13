package com.dianping.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;
import com.dianping.widget.NetworkImageView;

public class MyOrderItem extends LinearLayout
{
  private int itemImage;
  private NetworkImageView itemImageView;
  private boolean mIsRedDotVisible;
  private String mMarkText = "";
  private TextView mMarkTextView;
  private String subTitle;
  private TextView subTitleView;
  private String title;
  private TextView titleView;

  public MyOrderItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public MyOrderItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.UserProfileItem);
    this.title = paramAttributeSet.getString(R.styleable.UserProfileItem_itemTitle);
    this.subTitle = paramAttributeSet.getString(R.styleable.UserProfileItem_itemSubTitle);
    this.itemImage = paramAttributeSet.getResourceId(R.styleable.UserProfileItem_itemImage, 0);
    paramAttributeSet.recycle();
    paramContext = LayoutInflater.from(paramContext).inflate(R.layout.myorder_item, this, true);
    this.itemImageView = ((NetworkImageView)paramContext.findViewById(R.id.item_img));
    this.titleView = ((TextView)paramContext.findViewById(R.id.item_title_text));
    this.subTitleView = ((TextView)paramContext.findViewById(R.id.item_sub_title_text));
    this.mMarkTextView = ((TextView)findViewById(R.id.mark_text));
    this.subTitleView.setText(this.subTitle);
    this.titleView.setText(this.title);
    this.itemImageView.setImageResource(this.itemImage);
    setOrientation(0);
  }

  public String getTitle()
  {
    return this.title;
  }

  public boolean isRedMarkVisible()
  {
    return (this.mIsRedDotVisible) || (!TextUtils.isEmpty(this.mMarkText));
  }

  public void setItemImage(int paramInt)
  {
    this.itemImage = paramInt;
    this.itemImageView.setImageResource(paramInt);
  }

  public void setItemImage(String paramString)
  {
    this.itemImageView.setImage(paramString);
  }

  public void setItemImage(String paramString, int paramInt1, int paramInt2)
  {
    ViewGroup.LayoutParams localLayoutParams = this.itemImageView.getLayoutParams();
    localLayoutParams.height = paramInt2;
    localLayoutParams.width = paramInt1;
    this.itemImageView.setImage(paramString);
  }

  public void setRedMark(boolean paramBoolean, String paramString)
  {
    int i = ViewUtils.dip2px(getContext(), 5.0F);
    int j = ViewUtils.dip2px(getContext(), 5.0F);
    if (paramBoolean)
    {
      this.mMarkTextView.setBackgroundResource(R.drawable.red_mark_text_bg);
      this.mMarkTextView.setPadding(i, 0, j, 0);
    }
    for (this.mIsRedDotVisible = true; TextUtils.isEmpty(paramString); this.mIsRedDotVisible = false)
    {
      this.mMarkText = "";
      this.mMarkTextView.setVisibility(8);
      this.subTitleView.setVisibility(0);
      return;
      this.mMarkTextView.setBackgroundResource(R.drawable.mark_text_bg);
      this.mMarkTextView.setPadding(i, 0, j, 0);
    }
    this.mMarkText = paramString;
    this.mMarkTextView.setVisibility(0);
    this.mMarkTextView.setText(paramString);
    this.subTitleView.setVisibility(4);
  }

  public void setSubtitle(String paramString)
  {
    this.subTitle = paramString;
    this.subTitleView.setTag(paramString);
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
    this.titleView.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.MyOrderItem
 * JD-Core Version:    0.6.0
 */