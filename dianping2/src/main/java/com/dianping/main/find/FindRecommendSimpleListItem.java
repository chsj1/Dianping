package com.dianping.main.find;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;

public class FindRecommendSimpleListItem extends NovaRelativeLayout
{
  private ImageView goodNumImage;
  private TextView goodNumView;
  private ImageView reviewImage;
  private TextView reviewView;
  private TextView subTitleView;
  private NetworkImageView thumbnail;
  private TextView titleView;

  public FindRecommendSimpleListItem(Context paramContext)
  {
    super(paramContext);
  }

  public FindRecommendSimpleListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public FindRecommendSimpleListItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.titleView = ((TextView)findViewById(R.id.tv_title));
    this.subTitleView = ((TextView)findViewById(R.id.tv_subtile));
    this.goodNumImage = ((ImageView)findViewById(R.id.img_good_num));
    this.goodNumView = ((TextView)findViewById(R.id.tv_good_num));
    this.reviewImage = ((ImageView)findViewById(R.id.img_review));
    this.reviewView = ((TextView)findViewById(R.id.tv_review));
    this.thumbnail = ((NetworkImageView)findViewById(R.id.thumbnail));
  }

  public void setDeal(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    String str1 = paramDPObject.getString("Title");
    long l = paramDPObject.getLong("Up");
    int i = paramDPObject.getInt("ReviewCount");
    String str2 = paramDPObject.getString("GroupName");
    this.titleView.setText(str1);
    this.subTitleView.setText(str2);
    this.thumbnail.setImage(paramDPObject.getString("ThumbPicUrl"));
    if (l == 0L)
    {
      this.goodNumImage.setVisibility(8);
      this.goodNumView.setVisibility(8);
    }
    while (i == 0)
    {
      this.reviewImage.setVisibility(8);
      this.reviewView.setVisibility(8);
      return;
      this.goodNumImage.setVisibility(0);
      this.goodNumView.setVisibility(0);
      this.goodNumView.setText(Long.toString(l));
    }
    this.reviewImage.setVisibility(0);
    this.reviewView.setVisibility(0);
    this.reviewView.setText(Integer.toString(i));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.FindRecommendSimpleListItem
 * JD-Core Version:    0.6.0
 */