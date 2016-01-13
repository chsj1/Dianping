package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;
import com.dianping.widget.NetworkImageView;

public class UserProfileItem extends UserProfileBaseItem
{
  private int itemImage;
  private NetworkImageView itemImageView;
  private Context mContext;
  private ImageView mIconNew;
  private boolean mIsRedDotVisible;
  private TextView mMShowTextView;
  private String mMarkText = "";
  private MeasuredTextView mMarkTextView;
  private String subTitle;
  private TextView subTitleView;
  private String title;
  private TextView titleView;

  public UserProfileItem(Context paramContext)
  {
    this(paramContext, null);
    this.mContext = paramContext;
  }

  public UserProfileItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.UserProfileItem);
    this.title = paramContext.getString(R.styleable.UserProfileItem_itemTitle);
    this.subTitle = paramContext.getString(R.styleable.UserProfileItem_itemSubTitle);
    this.itemImage = paramContext.getResourceId(R.styleable.UserProfileItem_itemImage, 0);
    paramContext.recycle();
    paramContext = setupView(this.mContext);
    this.itemImageView = ((NetworkImageView)paramContext.findViewById(R.id.item_img));
    this.titleView = ((TextView)paramContext.findViewById(R.id.item_title_text));
    this.subTitleView = ((TextView)paramContext.findViewById(R.id.item_sub_title_text));
    this.mMShowTextView = ((TextView)paramContext.findViewById(R.id.mark_showtext));
    this.mMarkTextView = ((MeasuredTextView)findViewById(R.id.mark_text));
    this.mIconNew = ((ImageView)findViewById(R.id.ic_new));
    this.subTitleView.setText(this.subTitle);
    this.titleView.setText(this.title);
    try
    {
      this.itemImageView.setLocalDrawable(getResources().getDrawable(this.itemImage));
      setOrientation(0);
      setBackgroundResource(R.drawable.cell_item);
      return;
    }
    catch (Exception paramContext)
    {
      while (true)
        paramContext.printStackTrace();
    }
  }

  public NetworkImageView getItemImageView()
  {
    return this.itemImageView;
  }

  public String getTitle()
  {
    return this.title;
  }

  public boolean isRedMarkVisible()
  {
    return (this.mIsRedDotVisible) || (!TextUtils.isEmpty(this.mMarkText));
  }

  public void setIconNewVisibility(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mIconNew.setVisibility(0);
      return;
    }
    this.mIconNew.setVisibility(8);
  }

  public void setImageSize(int paramInt1, int paramInt2)
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(ViewUtils.dip2px(this.mContext, paramInt1), ViewUtils.dip2px(this.mContext, paramInt2));
    this.itemImageView.setLayoutParams(localLayoutParams);
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

  public void setObject(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    if (!TextUtils.isEmpty(paramDPObject.getString("Title")))
      setTitle(paramDPObject.getString("Title"));
    if (!TextUtils.isEmpty(paramDPObject.getString("SubTitle")))
      setSubtitle(paramDPObject.getString("SubTitle"));
    setImageSize(26, 26);
    this.itemImageView.setImage(paramDPObject.getString("PicUrl"));
  }

  public void setRedAlert(boolean paramBoolean, String paramString)
  {
    if (paramBoolean)
    {
      this.subTitleView.setVisibility(8);
      String str = paramString;
      if (TextUtils.isEmpty(paramString))
        str = "";
      this.mMarkText = str;
      this.mMShowTextView.setText(this.mMarkText);
      this.mMShowTextView.setVisibility(0);
      int i = ViewUtils.dip2px(getContext(), 8.0F);
      int j = ViewUtils.dip2px(getContext(), 8.0F);
      this.mMarkTextView.setBackgroundResource(R.drawable.red_dot);
      this.mMarkTextView.setWidth(i);
      this.mMarkTextView.setHeight(j);
      this.mMarkTextView.setVisibility(0);
      this.mIsRedDotVisible = true;
      return;
    }
    if (ViewUtils.isShow(this.mIconNew))
      this.mIconNew.setVisibility(8);
    if (ViewUtils.isShow(this.mMarkTextView))
    {
      this.mMarkText = "";
      this.mMarkTextView.setVisibility(8);
      this.mMShowTextView.setText("");
      this.mMShowTextView.setVisibility(8);
    }
    this.mIsRedDotVisible = false;
    this.subTitleView.setVisibility(0);
  }

  public void setSpecialRedAlert(boolean paramBoolean, String paramString)
  {
    this.mMShowTextView.setVisibility(8);
    if ((paramBoolean) && (!TextUtils.isEmpty(paramString)))
    {
      this.subTitleView.setVisibility(4);
      this.mMarkText = paramString;
      if ("NEW".equals(paramString.toUpperCase()))
      {
        setIconNewVisibility(true);
        this.mMarkTextView.setVisibility(8);
      }
      while (true)
      {
        this.mIsRedDotVisible = true;
        return;
        if (ViewUtils.isShow(this.mIconNew))
          this.mIconNew.setVisibility(8);
        this.mMarkTextView.setFlag(false);
        this.mMarkTextView.setText("");
        this.mMarkTextView.setBackgroundResource(0);
        int i = ViewUtils.dip2px(getContext(), 5.0F);
        int j = ViewUtils.dip2px(getContext(), 3.0F);
        this.mMarkTextView.setBackgroundResource(R.drawable.red_round_corner_rectangle);
        this.mMarkTextView.setPadding(i, j, i, j);
        this.mMarkTextView.setText(this.mMarkText);
        this.mMarkTextView.setVisibility(0);
      }
    }
    if (ViewUtils.isShow(this.mIconNew))
      this.mIconNew.setVisibility(8);
    this.mMarkText = "";
    this.mMarkTextView.setVisibility(8);
    this.mIsRedDotVisible = false;
    this.subTitleView.setVisibility(0);
  }

  public void setSubtitle(SpannableStringBuilder paramSpannableStringBuilder)
  {
    if (TextUtils.isEmpty(paramSpannableStringBuilder.toString()))
      return;
    this.subTitle = paramSpannableStringBuilder.toString();
    this.subTitleView.setText(paramSpannableStringBuilder);
    this.subTitleView.setVisibility(0);
  }

  public void setSubtitle(String paramString)
  {
    this.subTitle = paramString;
    this.subTitleView.setText(paramString);
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
    this.titleView.setText(paramString);
  }

  protected View setupView(Context paramContext)
  {
    return LayoutInflater.from(paramContext).inflate(R.layout.userprofile_item, this, true);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.UserProfileItem
 * JD-Core Version:    0.6.0
 */