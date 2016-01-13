package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaRelativeLayout;

public class ShopInfoServiceItemView extends NovaRelativeLayout
{
  Context context;
  TextView extendText;
  ImageView icon;
  TextView promoText;
  TextView titleTV;

  public ShopInfoServiceItemView(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
  }

  public ShopInfoServiceItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.icon = ((ImageView)findViewById(R.id.business_icon));
    this.titleTV = ((TextView)findViewById(R.id.business_text));
    this.promoText = ((TextView)findViewById(R.id.ic_off));
    this.extendText = ((TextView)findViewById(R.id.promo_text));
  }

  public void setClickActionByScheme(String paramString)
  {
    if (!TextUtils.isEmpty(paramString));
    try
    {
      this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramString)));
      return;
    }
    catch (Exception paramString)
    {
      Log.d(paramString.toString());
    }
  }

  public void setExtendInfo(String paramString)
  {
    if (this.extendText != null)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        this.extendText.setText(paramString);
        this.extendText.setVisibility(0);
      }
    }
    else
      return;
    this.extendText.setVisibility(8);
  }

  public void setGAString(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      setGAString(paramString);
  }

  public void setImageView(int paramInt)
  {
    if (this.icon != null)
      this.icon.setImageResource(paramInt);
  }

  public void setItemInfo(ShopInfoServiceView.ShopServiceItemInfo paramShopServiceItemInfo)
  {
    if (paramShopServiceItemInfo != null)
    {
      setTitle(paramShopServiceItemInfo.title);
      setImageView(paramShopServiceItemInfo.picResId);
      setPromoInfo(paramShopServiceItemInfo.promoInfo);
      setExtendInfo(paramShopServiceItemInfo.extraInfo);
      setGAString(paramShopServiceItemInfo.gaString);
      setOnClickListener(new View.OnClickListener(paramShopServiceItemInfo)
      {
        public void onClick(View paramView)
        {
          ShopInfoServiceItemView.this.setClickActionByScheme(this.val$itemInfo.scheme);
        }
      });
    }
  }

  public void setPromoInfo(String paramString)
  {
    if (this.promoText != null)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        this.promoText.setText(paramString);
        this.promoText.setVisibility(0);
      }
    }
    else
      return;
    this.promoText.setVisibility(8);
  }

  public void setTitle(String paramString)
  {
    if (this.titleTV != null)
      this.titleTV.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ShopInfoServiceItemView
 * JD-Core Version:    0.6.0
 */