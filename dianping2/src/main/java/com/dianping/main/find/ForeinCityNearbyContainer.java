package com.dianping.main.find;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class ForeinCityNearbyContainer extends LinearLayout
{
  private LinearLayout leftContainer;
  private LinearLayout rightContainer;

  public ForeinCityNearbyContainer(Context paramContext)
  {
    super(paramContext);
    initViews(paramContext);
  }

  public ForeinCityNearbyContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViews(paramContext);
  }

  private void initViews(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(R.layout.find_forein_city_container, this, true);
    this.leftContainer = ((LinearLayout)findViewById(R.id.left_container));
    this.rightContainer = ((LinearLayout)findViewById(R.id.right_container));
  }

  public void addItem(LinearLayout paramLinearLayout, DPObject paramDPObject, View.OnClickListener paramOnClickListener, int paramInt)
  {
    if (getContext() == null);
    do
      return;
    while ((paramLinearLayout == null) || (paramDPObject == null));
    String str1 = paramDPObject.getString("Title");
    String str2 = paramDPObject.getString("PicUrl");
    String str3 = paramDPObject.getString("Content");
    FrameLayout localFrameLayout = (FrameLayout)LayoutInflater.from(getContext()).inflate(R.layout.find_forein_city_item, null);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 10.0F);
    NetworkImageView localNetworkImageView = (NetworkImageView)localFrameLayout.findViewById(R.id.image);
    TextView localTextView1 = (TextView)localFrameLayout.findViewById(R.id.title);
    TextView localTextView2 = (TextView)localFrameLayout.findViewById(R.id.content);
    if ((str1 != null) && (!TextUtils.isEmpty(str1)))
    {
      localTextView1.setVisibility(0);
      localTextView1.setText(str1);
      if ((str3 == null) || (TextUtils.isEmpty(str3)))
        break label311;
      localTextView2.setVisibility(0);
      localTextView2.setText(str3);
      label176: if ((str2 == null) || (TextUtils.isEmpty(str2)))
        break label321;
      localNetworkImageView.setVisibility(0);
      localNetworkImageView.setImage(str2);
    }
    while (true)
    {
      localFrameLayout.setLayoutParams(localLayoutParams);
      paramDPObject = paramDPObject.edit().putInt("position", paramInt).generate();
      localFrameLayout.setTag(paramDPObject);
      localFrameLayout.findViewById(R.id.click_container).setOnClickListener(new View.OnClickListener(paramDPObject, paramOnClickListener)
      {
        public void onClick(View paramView)
        {
          paramView.setTag(this.val$tagDPObject);
          this.val$clickListener.onClick(paramView);
        }
      });
      localFrameLayout.findViewById(R.id.item_container).setOnClickListener(new View.OnClickListener(paramDPObject, paramOnClickListener)
      {
        public void onClick(View paramView)
        {
          paramView.setTag(this.val$tagDPObject);
          this.val$clickListener.onClick(paramView);
        }
      });
      paramLinearLayout.addView(localFrameLayout);
      postDelayed(new Runnable(localFrameLayout)
      {
        public void run()
        {
          this.val$itemLayout.findViewById(R.id.click_container).setLayoutParams(new FrameLayout.LayoutParams(this.val$itemLayout.getWidth(), this.val$itemLayout.getHeight()));
        }
      }
      , 500L);
      return;
      localTextView1.setVisibility(8);
      break;
      label311: localTextView2.setVisibility(8);
      break label176;
      label321: localNetworkImageView.setVisibility(8);
    }
  }

  public void setData(DPObject[] paramArrayOfDPObject, View.OnClickListener paramOnClickListener)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return;
    int i = 0;
    int j = paramArrayOfDPObject.length;
    label17: if (i < j)
      if (i % 2 != 0)
        break label57;
    label57: for (LinearLayout localLinearLayout = this.leftContainer; ; localLinearLayout = this.rightContainer)
    {
      addItem(localLinearLayout, paramArrayOfDPObject[i], paramOnClickListener, i);
      i += 1;
      break label17;
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.ForeinCityNearbyContainer
 * JD-Core Version:    0.6.0
 */