package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.v1.R.id;

public class UserShopPhotoItem extends LinearLayout
  implements View.OnClickListener
{
  private UserShopPhotoView photoView;
  private DPObject shop;
  private LinearLayout shopLayout;

  public UserShopPhotoItem(Context paramContext)
  {
    super(paramContext);
  }

  public UserShopPhotoItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.shopLayout)
    {
      if (!(getContext() instanceof NovaActivity))
        break label43;
      ((NovaActivity)getContext()).statisticsEvent("profile5", "profile5_photo_item", "商户", 0);
    }
    while (this.shop == null)
    {
      return;
      label43: DPApplication.instance().statisticsEvent("profile5", "profile5_photo_item", "商户", 0);
    }
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + this.shop.getInt("ID")));
    paramView.putExtra("shop", this.shop);
    getContext().startActivity(paramView);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shopLayout = ((LinearLayout)findViewById(R.id.shoplayout));
    this.photoView = ((UserShopPhotoView)findViewById(R.id.shopphoto));
    this.shopLayout.setOnClickListener(this);
  }

  public void setPhoto(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    this.shop = paramDPObject1.getObject("Shop");
    ((TextView)findViewById(R.id.shopName)).setText(DPObjectUtils.getShopFullName(this.shop));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.UserShopPhotoItem
 * JD-Core Version:    0.6.0
 */