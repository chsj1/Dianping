package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class MultiFixedHeaderView extends DefaultShopInfoHeaderView
{
  private static final int IMAGE_COUNT = 3;
  protected LinearLayout shop_images;

  public MultiFixedHeaderView(Context paramContext)
  {
    super(paramContext);
  }

  public MultiFixedHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shop_images = ((LinearLayout)findViewById(R.id.shop_images));
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (this.shop_images == null);
    label222: 
    while (true)
    {
      return;
      this.shop_images.removeAllViews();
      DPObject[] arrayOfDPObject = paramDPObject.getArray("AdvancedPics");
      if ((arrayOfDPObject == null) || (arrayOfDPObject.length <= 0))
        continue;
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -2);
      localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 3.0F);
      localLayoutParams.weight = 1.0F;
      localLayoutParams.height = ((ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 40.0F)) / 3 + (ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 40.0F)) % 3);
      int j = 0;
      int k = arrayOfDPObject.length;
      int i = 0;
      while (true)
      {
        if (i >= k)
          break label222;
        DPObject localDPObject = arrayOfDPObject[i];
        j += 1;
        if (j > 3)
          break;
        NetworkImageView localNetworkImageView = (NetworkImageView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_networkimageview_item, null, false);
        localNetworkImageView.setLayoutParams(localLayoutParams);
        localNetworkImageView.setImage(localDPObject.getString("ThumbUrl"));
        localNetworkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        localNetworkImageView.setClickable(true);
        localNetworkImageView.setOnClickListener(new View.OnClickListener(localDPObject, paramDPObject)
        {
          public void onClick(View paramView)
          {
            try
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$advancedPic.getString("Scheme")));
              paramView.putExtra("shop", this.val$shop);
              MultiFixedHeaderView.this.getContext().startActivity(paramView);
              return;
            }
            catch (Exception paramView)
            {
              paramView.printStackTrace();
            }
          }
        });
        this.shop_images.addView(localNetworkImageView);
        i += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.MultiFixedHeaderView
 * JD-Core Version:    0.6.0
 */