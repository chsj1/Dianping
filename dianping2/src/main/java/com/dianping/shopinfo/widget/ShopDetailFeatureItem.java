package com.dianping.shopinfo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import java.util.Date;

public class ShopDetailFeatureItem extends LinearLayout
  implements View.OnClickListener
{
  static final String DATE_FORMAT_STRING = "yy-MM-dd";
  private ImageView mExpandBtn;
  private NetworkImageView mIcon;
  private boolean mIsExpanded = false;
  private TextView mReview;
  private TextView mTitle;

  public ShopDetailFeatureItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopDetailFeatureItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.container_lay)
    {
      if (this.mIsExpanded)
      {
        this.mExpandBtn.setImageResource(R.drawable.ic_arrow_down_black);
        this.mIsExpanded = false;
        this.mReview.setVisibility(8);
      }
    }
    else
      return;
    ((NovaActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_info_feature", "", 0);
    this.mExpandBtn.setImageResource(R.drawable.ic_arrow_up_black);
    this.mIsExpanded = true;
    this.mReview.setVisibility(0);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mExpandBtn = ((ImageView)findViewById(R.id.expand_btn));
    this.mIcon = ((NetworkImageView)findViewById(R.id.icon));
    this.mTitle = ((TextView)findViewById(R.id.name));
    this.mReview = ((TextView)findViewById(R.id.content));
    ((LinearLayout)findViewById(R.id.container_lay)).setOnClickListener(this);
  }

  public void setFeatureContent(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    Object localObject1 = paramDPObject.getObject("FeatureTag");
    this.mIcon.setImage(((DPObject)localObject1).getString("PictureUrl"));
    this.mTitle.setText(((DPObject)localObject1).getString("Title"));
    localObject1 = paramDPObject.getArray("TaggedReviews");
    if ((localObject1 != null) && (localObject1.length > 0))
    {
      this.mExpandBtn.setVisibility(0);
      Object localObject2 = localObject1[0];
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(localObject2.getString("UserName"));
      ((StringBuilder)localObject1).append(":");
      ((StringBuilder)localObject1).append(localObject2.getString("ReviewBody"));
      if (paramDPObject.getTime("AddTime") > 0L)
      {
        paramDPObject = new Date(paramDPObject.getTime("AddTime"));
        ((StringBuilder)localObject1).append(" (" + DateUtils.formatDate2TimeZone(paramDPObject, "yy-MM-dd", "GMT+8") + ")");
      }
      this.mReview.setText(((StringBuilder)localObject1).toString());
      return;
    }
    this.mExpandBtn.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ShopDetailFeatureItem
 * JD-Core Version:    0.6.0
 */