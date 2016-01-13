package com.dianping.shopinfo.wed.baby.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.wed.WedBaseAdapter;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class GridTuanAdapter extends WedBaseAdapter
{
  boolean isExpand = false;

  public GridTuanAdapter(Context paramContext, DPObject[] paramArrayOfDPObject)
  {
    this.context = paramContext;
    this.adapterData = paramArrayOfDPObject;
    this.albumFrameWidth = (ViewUtils.getScreenWidthPixels(paramContext) * 43 / 100);
    this.albumFrameHeight = (this.albumFrameWidth * 210 / 280);
    if ((this.adapterData.length == 2) || (this.adapterData.length == 4))
      this.isExpand = true;
  }

  public int getCount()
  {
    int i = 1;
    if (this.isExpand)
      i = this.adapterData.length;
    do
      return i;
    while (this.adapterData.length == 1);
    if (this.adapterData.length <= 3)
      return 2;
    return 4;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    if (paramView == null)
      localView = LayoutInflater.from(this.context).inflate(R.layout.baby_shopinfo_tuan, paramViewGroup, false);
    paramView = (DPObject)getItem(paramInt);
    if (paramView == null)
      return localView;
    paramViewGroup = paramView.getString("Photo");
    Object localObject = (NetworkImageView)getAdapterView(localView, R.id.img_shop_photo);
    ((NetworkImageView)localObject).getLayoutParams().width = this.albumFrameWidth;
    ((NetworkImageView)localObject).getLayoutParams().height = this.albumFrameHeight;
    ((NetworkImageView)localObject).setImage(paramViewGroup);
    ((TextView)getAdapterView(localView, R.id.lay_img_desc_title)).setText(paramView.getString("ContentTitle"));
    localObject = (TextView)getAdapterView(localView, R.id.lay_img_desc_price);
    paramViewGroup = (TextView)getAdapterView(localView, R.id.lay_img_desc_origprice);
    double d1 = paramView.getDouble("Price");
    double d2 = paramView.getDouble("OriginalPrice");
    getAdapterView(localView, R.id.lay_img_desc_no_price).setVisibility(8);
    ((TextView)localObject).setVisibility(0);
    paramViewGroup.setVisibility(0);
    ((TextView)localObject).setText("¥ " + PriceFormatUtils.formatPrice(d1));
    if (d2 > 0.0D)
    {
      localObject = new SpannableString("¥" + PriceFormatUtils.formatPrice(d2));
      ((SpannableString)localObject).setSpan(new StrikethroughSpan(), 0, ((SpannableString)localObject).length(), 33);
      paramViewGroup.setText((CharSequence)localObject);
      paramViewGroup.getPaint().setFlags(16);
    }
    while (true)
    {
      paramViewGroup = (TextView)getAdapterView(localView, R.id.text3);
      paramViewGroup.setVisibility(8);
      if (TextUtils.isEmpty(paramView.getString("DiscountDesc")))
        break;
      paramViewGroup.setVisibility(0);
      paramViewGroup.setText(paramView.getString("DiscountDesc"));
      return localView;
      paramViewGroup.setText("");
      paramViewGroup.setVisibility(4);
    }
  }

  public boolean isExpand()
  {
    return this.isExpand;
  }

  public void setExpand(boolean paramBoolean)
  {
    this.isExpand = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.widget.GridTuanAdapter
 * JD-Core Version:    0.6.0
 */