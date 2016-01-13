package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;
import com.dianping.widget.NetworkImageView;

public class UserProfileVerticalItem extends UserProfileBaseItem
{
  private int itemImage;
  private String itemTag;
  private Context mContext;
  private boolean mIsRedDotVisible = false;
  private NetworkImageView mItemImg;
  private String mTitle;
  private View reddotLayout;
  private TextView titleView;

  public UserProfileVerticalItem(Context paramContext)
  {
    super(paramContext, null);
  }

  public UserProfileVerticalItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    paramContext = this.mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.UserProfileItem);
    this.mTitle = paramContext.getString(R.styleable.UserProfileItem_itemTitle);
    this.itemImage = paramContext.getResourceId(R.styleable.UserProfileItem_itemImage, 0);
    this.itemTag = paramContext.getString(R.styleable.UserProfileItem_itemTag);
    paramContext.recycle();
    paramContext = LayoutInflater.from(this.mContext).inflate(R.layout.userprofile_vertical_item, this, true);
    this.titleView = ((TextView)paramContext.findViewById(R.id.vertical_item_title));
    this.mItemImg = ((NetworkImageView)paramContext.findViewById(R.id.vertical_item_img));
    this.reddotLayout = paramContext.findViewById(R.id.vertical_item_flayout);
    this.titleView.setText(this.mTitle);
    try
    {
      this.mItemImg.setLocalDrawable(getResources().getDrawable(this.itemImage));
      return;
    }
    catch (Exception paramContext)
    {
      paramContext.printStackTrace();
    }
  }

  public String getTag()
  {
    return this.itemTag;
  }

  public String getTitle()
  {
    return this.mTitle;
  }

  public boolean isRedMarkVisible()
  {
    return this.mIsRedDotVisible;
  }

  public void setRedAlert(boolean paramBoolean)
  {
    this.mIsRedDotVisible = paramBoolean;
    if (paramBoolean)
    {
      this.reddotLayout.setVisibility(0);
      return;
    }
    this.reddotLayout.setVisibility(8);
  }

  public void setRedAlert(boolean paramBoolean, String paramString)
  {
    setRedAlert(paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.UserProfileVerticalItem
 * JD-Core Version:    0.6.0
 */