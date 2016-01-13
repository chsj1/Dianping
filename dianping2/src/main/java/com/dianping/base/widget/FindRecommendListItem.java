package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;

public class FindRecommendListItem extends NovaFrameLayout
{
  private static final String TAG = FindRecommendListItem.class.getSimpleName();
  private TextView goodNumView;
  private NetworkImageView networkImageView;
  private TextView subTitleView;
  private TextView titleView;

  public FindRecommendListItem(Context paramContext)
  {
    super(paramContext);
  }

  public FindRecommendListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public FindRecommendListItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.networkImageView = ((NetworkImageView)findViewById(16908294));
    this.titleView = ((TextView)findViewById(R.id.title));
    this.subTitleView = ((TextView)findViewById(R.id.sub_title));
    this.goodNumView = ((TextView)findViewById(R.id.tv_goodnum));
  }

  public void setDeal(DPObject paramDPObject)
  {
    String str1 = paramDPObject.getString("Title");
    long l = paramDPObject.getLong("Up");
    String str2 = paramDPObject.getString("SubTitle");
    String str3 = paramDPObject.getString("PicUrl");
    if (this.titleView != null)
      this.titleView.setText(str1);
    if (this.subTitleView != null)
      this.subTitleView.setText(str2);
    if (this.networkImageView != null)
      this.networkImageView.setImage(str3);
    if (this.goodNumView != null)
      this.goodNumView.setText(Long.toString(l));
    setGAString("explore_topic", str1);
    setTag(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.FindRecommendListItem
 * JD-Core Version:    0.6.0
 */