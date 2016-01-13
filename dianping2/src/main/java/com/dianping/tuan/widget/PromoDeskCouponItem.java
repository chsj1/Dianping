package com.dianping.tuan.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import java.util.ArrayList;

public class PromoDeskCouponItem extends LinearLayout
{
  protected DPObject dpPromoDeskCoupon;
  protected ImageView ivPromoDeskCouponIcon;
  protected RMBLabelItem rmbPromoDeskRMBLabel;
  protected TextView tvPromoDeskCouponTitle;
  protected TextView tvPromoDeskCouponValidTime;

  public PromoDeskCouponItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public PromoDeskCouponItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.tvPromoDeskCouponTitle = ((TextView)findViewById(R.id.tv_promodeskcoupon_title));
    this.tvPromoDeskCouponValidTime = ((TextView)findViewById(R.id.tv_promodeskcoupon_valid_time));
    this.rmbPromoDeskRMBLabel = ((RMBLabelItem)findViewById(R.id.rmb_promodeskcoupon));
    this.ivPromoDeskCouponIcon = ((ImageView)findViewById(R.id.iv_promodeskcoupon_icon));
    if (!isInEditMode())
    {
      this.tvPromoDeskCouponTitle.setText("");
      this.tvPromoDeskCouponValidTime.setText("");
      this.rmbPromoDeskRMBLabel.setRMBLabelValue(1.7976931348623157E+308D);
      this.ivPromoDeskCouponIcon.setImageResource(R.drawable.rad_disable);
    }
  }

  public boolean setPromoDeskCoupon(DPObject paramDPObject1, DPObject paramDPObject2, ArrayList<DPObject> paramArrayList)
  {
    if (paramDPObject1 == null)
      return false;
    if ((paramArrayList == null) || (paramArrayList.size() == 0))
      return false;
    this.dpPromoDeskCoupon = paramDPObject1;
    this.tvPromoDeskCouponTitle.setText(this.dpPromoDeskCoupon.getString("Title"));
    if (!TextUtils.isEmpty(this.dpPromoDeskCoupon.getString("ExpireDate")))
      this.tvPromoDeskCouponValidTime.setText("有效期至" + this.dpPromoDeskCoupon.getString("ExpireDate"));
    double d2 = Double.valueOf(this.dpPromoDeskCoupon.getString("Price")).doubleValue();
    double d3 = this.dpPromoDeskCoupon.getDouble("OrderPriceLimit");
    int j = this.dpPromoDeskCoupon.getInt("ProductCode");
    boolean bool = this.dpPromoDeskCoupon.getBoolean("CanUse");
    paramDPObject1 = null;
    int i = 0;
    while (i < paramArrayList.size())
    {
      DPObject localDPObject2 = (DPObject)paramArrayList.get(i);
      DPObject localDPObject1 = paramDPObject1;
      if (localDPObject2.getBoolean("Selected"))
      {
        localDPObject1 = paramDPObject1;
        if (localDPObject2.getInt("ProductCode") == j)
          localDPObject1 = localDPObject2;
      }
      i += 1;
      paramDPObject1 = localDPObject1;
    }
    double d1 = 0.0D;
    if (paramDPObject1 != null)
    {
      if (paramDPObject1.getBoolean("CalculateNoDiscountAmount"))
        d1 = paramDPObject1.getInt("Quantity") * paramDPObject1.getDouble("Price");
    }
    else
    {
      if ((!bool) || (d1 < Double.valueOf(d3).doubleValue()))
        break label433;
      if (paramDPObject2 != null)
        break label330;
      this.tvPromoDeskCouponTitle.setTextColor(getResources().getColor(R.color.text_color_black));
      this.rmbPromoDeskRMBLabel.setRMBLabelValue(Math.abs(d2));
      this.ivPromoDeskCouponIcon.setImageResource(R.drawable.rad_normal);
    }
    while (true)
    {
      return true;
      d1 = paramDPObject1.getInt("Quantity") * paramDPObject1.getDouble("Price") - paramDPObject1.getDouble("NoDiscountAmount");
      break;
      label330: if (this.dpPromoDeskCoupon.getInt("ID") == paramDPObject2.getInt("ID"))
      {
        this.tvPromoDeskCouponTitle.setTextColor(getResources().getColor(R.color.text_color_black));
        this.rmbPromoDeskRMBLabel.setRMBLabelValue(-Math.abs(d2));
        this.ivPromoDeskCouponIcon.setImageResource(R.drawable.rad_pressed);
        continue;
      }
      this.tvPromoDeskCouponTitle.setTextColor(getResources().getColor(R.color.text_color_black));
      this.rmbPromoDeskRMBLabel.setRMBLabelValue(Math.abs(d2));
      this.ivPromoDeskCouponIcon.setImageResource(R.drawable.rad_normal);
    }
    label433: this.tvPromoDeskCouponTitle.setTextColor(getResources().getColor(R.color.text_color_gray));
    this.rmbPromoDeskRMBLabel.setRMBLabelValue(Math.abs(d2));
    this.ivPromoDeskCouponIcon.setImageResource(R.drawable.rad_disable);
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.widget.PromoDeskCouponItem
 * JD-Core Version:    0.6.0
 */