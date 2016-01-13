package com.dianping.shopinfo.mall.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaRelativeLayout;

public class MallHotPromoItemView extends NovaRelativeLayout
{
  protected ImageView coverImage;
  protected TextView desc;
  protected NetworkThumbView thumbImage;

  public MallHotPromoItemView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MallHotPromoItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.thumbImage = ((NetworkThumbView)findViewById(R.id.promo_item_icon));
    this.coverImage = ((ImageView)findViewById(R.id.promo_panel_cover_image));
    this.desc = ((TextView)findViewById(R.id.promo_item_desc));
  }

  public void setPromo(DPObject paramDPObject)
  {
    if (!TextUtils.isEmpty(paramDPObject.getString("Pic")))
    {
      this.thumbImage.setImage(paramDPObject.getString("Pic"));
      this.thumbImage.setVisibility(0);
      this.coverImage.setVisibility(0);
    }
    while (!TextUtils.isEmpty(paramDPObject.getString("Desc")))
    {
      this.desc.setText(paramDPObject.getString("Desc"));
      this.desc.setVisibility(0);
      return;
      this.thumbImage.setVisibility(8);
      this.coverImage.setVisibility(8);
    }
    this.desc.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.view.MallHotPromoItemView
 * JD-Core Version:    0.6.0
 */