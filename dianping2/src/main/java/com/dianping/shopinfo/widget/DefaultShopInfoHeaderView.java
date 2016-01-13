package com.dianping.shopinfo.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopPower;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaButton;

public class DefaultShopInfoHeaderView extends LinearLayout
{
  protected static final int SHOP_STATUS_DONE = 0;
  protected static final int SHOP_STATUS_ERROR = -1;
  protected static final int SHOP_STATUS_LOADING = 1;
  protected TextView businessArea;
  protected DPNetworkImageView chainShop;
  protected TextView cookStyle;
  protected NetworkImageView icon;
  protected TextView imgCount;
  protected TextView imgCountZero;
  protected View.OnClickListener onIconClickListener;
  public NovaButton payButton;
  protected TextView priceAvg;
  public View rateSource;
  protected TextView reviewCount;
  public TextView shopName;
  protected ShopPower shopPower;
  protected int shopStatus;

  public DefaultShopInfoHeaderView(Context paramContext)
  {
    super(paramContext);
  }

  public DefaultShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected int getAvailableWith()
  {
    return ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 142.0F);
  }

  protected String getFullName(DPObject paramDPObject)
  {
    Object localObject = paramDPObject.getString("Name");
    paramDPObject = paramDPObject.getString("BranchName");
    if (android.text.TextUtils.isEmpty((CharSequence)localObject))
      return "";
    localObject = new StringBuilder().append((String)localObject);
    if (android.text.TextUtils.isEmpty(paramDPObject));
    for (paramDPObject = ""; ; paramDPObject = "(" + paramDPObject + ")")
      return paramDPObject;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.icon = ((NetworkImageView)findViewById(R.id.shop_icon));
    this.shopName = ((TextView)findViewById(R.id.shop_name));
    this.reviewCount = ((TextView)findViewById(R.id.review_count));
    this.priceAvg = ((TextView)findViewById(R.id.price_avg));
    this.businessArea = ((TextView)findViewById(R.id.business_area));
    this.cookStyle = ((TextView)findViewById(R.id.cook_style));
    this.shopPower = ((ShopPower)findViewById(R.id.shop_power));
    this.imgCount = ((TextView)findViewById(R.id.img_count));
    this.payButton = ((NovaButton)findViewById(R.id.pay_button));
    this.imgCountZero = ((TextView)findViewById(R.id.imgCountZero));
    this.rateSource = findViewById(R.id.text_rate_source);
    this.chainShop = ((DPNetworkImageView)findViewById(R.id.chain_shop));
  }

  protected void setAreaStyleInfo(DPObject paramDPObject)
  {
    if ((this.businessArea == null) || (this.cookStyle == null))
      return;
    this.businessArea.setText(paramDPObject.getString("RegionName"));
    this.cookStyle.setText(paramDPObject.getString("CategoryName"));
  }

  protected void setBaseInfo(DPObject paramDPObject)
  {
    this.shopName.setText(getFullName(paramDPObject));
    setIconImage(paramDPObject);
    setPrice(paramDPObject);
    this.shopPower.setPower(paramDPObject.getInt("ShopPower"));
    setShopDesc(paramDPObject);
  }

  protected int setChainInfo(DPObject paramDPObject)
  {
    if ((this.chainShop != null) && (!com.dianping.util.TextUtils.isEmpty(paramDPObject.getString("ChainTag"))))
    {
      this.chainShop.setVisibility(0);
      this.chainShop.setImage(paramDPObject.getString("ChainTag"));
      int i = ViewUtils.dip2px(getContext(), 59.0F);
      paramDPObject = (LinearLayout.LayoutParams)this.cookStyle.getLayoutParams();
      paramDPObject.rightMargin = ViewUtils.dip2px(getContext(), 10.0F);
      this.cookStyle.setLayoutParams(paramDPObject);
      return i;
    }
    this.chainShop.setVisibility(8);
    paramDPObject = (LinearLayout.LayoutParams)this.cookStyle.getLayoutParams();
    paramDPObject.rightMargin = 0;
    this.cookStyle.setLayoutParams(paramDPObject);
    return 0;
  }

  public void setIconClickListener(View.OnClickListener paramOnClickListener)
  {
    this.onIconClickListener = paramOnClickListener;
    if (this.icon != null)
      this.icon.setOnClickListener(this.onIconClickListener);
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if ((paramDPObject.contains("PicCount")) && (paramDPObject.getInt("PicCount") == 0) && (android.text.TextUtils.isEmpty(paramDPObject.getString("DefaultPic"))))
      if (this.icon != null)
      {
        MyResources localMyResources = MyResources.getResource(ShopInfoHeaderView.class);
        this.icon.setBackgroundResource(R.color.gray_light_background);
        this.icon.setLocalBitmap(BitmapFactory.decodeResource(localMyResources.getResources(), R.drawable.placeholder_default));
        this.icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (this.imgCountZero != null)
        {
          this.imgCountZero.setVisibility(0);
          this.imgCountZero.setText("上传第1张图片");
        }
      }
    while (true)
    {
      if (this.imgCount != null)
      {
        if (paramDPObject.getInt("PicCount") == 0)
          break;
        this.imgCount.setVisibility(0);
        this.imgCount.setText("" + paramDPObject.getInt("PicCount"));
      }
      return;
      if (this.icon == null)
        continue;
      this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      this.icon.setImage(paramDPObject.getString("DefaultPic"));
    }
    this.imgCount.setVisibility(8);
  }

  protected void setPrice(DPObject paramDPObject)
  {
    if (this.priceAvg != null)
    {
      if (!android.text.TextUtils.isEmpty(paramDPObject.getString("PriceText")))
        this.priceAvg.setText(paramDPObject.getString("PriceText"));
    }
    else
    {
      if ((this.reviewCount != null) && (!paramDPObject.getBoolean("IsForeignShop")))
      {
        if (paramDPObject.getInt("VoteTotal") != 0)
          break label134;
        this.reviewCount.setVisibility(4);
      }
      return;
    }
    TextView localTextView = this.priceAvg;
    StringBuilder localStringBuilder = new StringBuilder().append("￥");
    if (paramDPObject.getInt("AvgPrice") > 0);
    for (String str = Integer.toString(paramDPObject.getInt("AvgPrice")); ; str = "-")
    {
      localTextView.setText(str);
      break;
    }
    label134: this.reviewCount.setVisibility(0);
    this.reviewCount.setText(paramDPObject.getInt("VoteTotal") + "条");
  }

  protected void setScoreSourceInfo(DPObject paramDPObject)
  {
    if ((this.shopStatus == 0) && (!paramDPObject.getBoolean("IsRateFromDP")) && (this.rateSource != null))
      this.rateSource.setVisibility(0);
  }

  public void setShop(DPObject paramDPObject, int paramInt)
  {
    this.shopStatus = paramInt;
    setBaseInfo(paramDPObject);
    setScoreSourceInfo(paramDPObject);
  }

  protected void setShopDesc(DPObject paramDPObject)
  {
    if ((this.businessArea == null) || (this.cookStyle == null) || (this.chainShop == null));
    int i;
    int j;
    do
    {
      return;
      setAreaStyleInfo(paramDPObject);
      i = setChainInfo(paramDPObject);
      j = getAvailableWith();
      if (j < ViewUtils.dip2px(getContext(), 49.0F))
      {
        this.businessArea.setVisibility(8);
        this.cookStyle.setVisibility(8);
        this.chainShop.setVisibility(8);
        return;
      }
      if (ViewUtils.measureTextView(this.cookStyle) + i <= j)
        continue;
      this.businessArea.setVisibility(8);
      this.cookStyle.setVisibility(8);
      return;
    }
    while (ViewUtils.measureTextView(this.cookStyle) + ViewUtils.measureTextView(this.businessArea) + i + ViewUtils.dip2px(getContext(), 10.0F) <= j);
    this.businessArea.setVisibility(8);
    this.cookStyle.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.DefaultShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */