package com.dianping.shopinfo.wed.baby.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopPower;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetImageHandler;
import com.dianping.widget.NetworkImageView;

public class WeddingShopHeaderView extends ShopInfoHeaderView
{
  private static final String REVIEW_TAIL = "条点评";
  private TextView avgCostLabel;
  private TextView avgCostView;
  private DPObject headerInfo;
  private TextView reviewCount;
  private TextView verticalDivider;

  public WeddingShopHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public WeddingShopHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setBanquetTopImage(String paramString)
  {
    if (this.icon != null)
      this.icon.setImage(paramString);
  }

  public void setBanquetContent(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      throw new NullPointerException("Banquet info must be not null");
    TextView localTextView1 = (TextView)findViewById(R.id.shop_avg_cost);
    TextView localTextView2 = (TextView)findViewById(R.id.shop_review_count);
    if (localTextView1 != null)
    {
      localTextView1.setText(paramDPObject.getString("Seat"));
      localTextView1.setVisibility(0);
    }
    if ((localTextView2 != null) && (!TextUtils.isEmpty(paramDPObject.getString("SiteType"))))
    {
      localTextView2.setText(paramDPObject.getString("SiteType"));
      localTextView2.setVisibility(0);
    }
    paramDPObject = paramDPObject.getString("DefaultImg");
    if (!TextUtils.isEmpty(paramDPObject))
      setBanquetTopImage(paramDPObject);
  }

  protected void setBaseInfo(DPObject paramDPObject)
  {
    this.name.setText(getFullName(paramDPObject));
    setIconImage(paramDPObject);
    this.power.setPower(paramDPObject.getInt("ShopPower"));
    setReviewCountStr();
  }

  public void setHeaderInfo(DPObject paramDPObject)
  {
    this.headerInfo = paramDPObject;
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (this.headerInfo == null)
      return;
    paramDPObject = (ImageView)findViewById(R.id.shop_panel_cover_image);
    MyResources localMyResources = MyResources.getResource(ShopInfoHeaderView.class);
    String str = this.headerInfo.getString("DefaultImg");
    if (TextUtils.isEmpty(str))
    {
      this.icon.setLocalBitmap(BitmapFactory.decodeResource(localMyResources.getResources(), R.drawable.header_default));
      this.icon.setImageHandler(new NetImageHandler(paramDPObject)
      {
        public void onFinish()
        {
          WeddingShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
          this.val$coverImage.setVisibility(0);
        }
      });
      return;
    }
    this.icon.setImage(str);
    this.icon.setImageHandler(new NetImageHandler(paramDPObject)
    {
      public void onFinish()
      {
        WeddingShopHeaderView.this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.val$coverImage.setVisibility(0);
      }
    });
  }

  public void setReviewCountStr()
  {
    if (this.headerInfo == null);
    int i;
    while (true)
    {
      return;
      str = String.valueOf(this.headerInfo.getInt("ReviewCount"));
      this.reviewCount = ((TextView)findViewById(R.id.shop_review_count));
      if (!TextUtils.isEmpty(str))
        break;
      this.reviewCount.setVisibility(4);
      this.avgCostView = ((TextView)findViewById(R.id.shop_avg_cost));
      this.avgCostLabel = ((TextView)findViewById(R.id.shop_avg_cost_label));
      this.verticalDivider = ((TextView)findViewById(R.id.vertical_divider));
      if ((this.avgCostView == null) || (this.avgCostLabel == null) || (this.verticalDivider == null))
        continue;
      i = this.headerInfo.getInt("AvgPrice");
      if (i > 0)
        break label207;
    }
    label207: for (String str = ""; ; str = String.valueOf(i))
    {
      if (!TextUtils.isEmpty(str))
        break label215;
      this.avgCostView.setVisibility(8);
      this.verticalDivider.setVisibility(8);
      return;
      if (str.contains("条点评"));
      while (true)
      {
        this.reviewCount.setText(str);
        this.reviewCount.setVisibility(0);
        break;
        str = str + "条点评";
      }
    }
    label215: this.verticalDivider.setVisibility(0);
    this.avgCostLabel.setVisibility(0);
    this.avgCostView.setText(str);
    this.avgCostView.setVisibility(0);
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.widget.WeddingShopHeaderView
 * JD-Core Version:    0.6.0
 */