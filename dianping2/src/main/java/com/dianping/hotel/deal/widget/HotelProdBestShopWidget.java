package com.dianping.hotel.deal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.HotelShopPower;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class HotelProdBestShopWidget extends NovaLinearLayout
{
  private static final int TYPE_FOOD = 3;
  private static final int TYPE_HOTEL = 1;
  private static final int TYPE_OTHER = 4;
  private static final int TYPE_SCENIC = 2;
  private TextView mDescTextView;
  private TextView mFullNameTextView;
  private TextView mLocDescTextView;
  private TextView mLocationTextView;
  private ImageView mPreLocationImageView;
  private HotelShopPower mShopPower;
  private NetworkImageView mThumbImageView;
  private TextView mThumbTagTextView;
  private DPObject shop;

  public HotelProdBestShopWidget(Context paramContext)
  {
    super(paramContext);
  }

  public HotelProdBestShopWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mThumbImageView = ((NetworkImageView)findViewById(R.id.thumb));
    this.mThumbTagTextView = ((TextView)findViewById(R.id.thumb_tag));
    this.mFullNameTextView = ((TextView)findViewById(R.id.fullName));
    this.mShopPower = ((HotelShopPower)findViewById(R.id.power));
    this.mLocDescTextView = ((TextView)findViewById(R.id.location_desc));
    this.mDescTextView = ((TextView)findViewById(R.id.desc));
    this.mLocationTextView = ((TextView)findViewById(R.id.location));
    this.mPreLocationImageView = ((ImageView)findViewById(R.id.pre_location));
  }

  public void setShop(DPObject paramDPObject, int paramInt, String paramString)
  {
    this.shop = paramDPObject;
    this.mThumbImageView.setImage(paramDPObject.getString("DefaultPic"));
    this.mFullNameTextView.setText(paramDPObject.getString("Name"));
    this.mShopPower.setPower(paramDPObject.getInt("ShopPower"));
    if ((paramString == null) || (paramString.length() == 0))
    {
      this.mThumbTagTextView.setVisibility(8);
      if ((paramInt != 1) && (paramInt != 4))
        break label190;
      this.mLocDescTextView.setVisibility(8);
      this.mDescTextView.setVisibility(0);
      this.mDescTextView.setText(paramDPObject.getString("CategoryName"));
      paramString = paramDPObject.getString("DistanceText");
      if ((paramString != null) && (paramString.length() != 0))
        break label160;
      this.mPreLocationImageView.setVisibility(8);
      this.mLocationTextView.setVisibility(8);
    }
    label160: label190: 
    do
    {
      return;
      this.mThumbTagTextView.setVisibility(0);
      this.mThumbTagTextView.setText(paramString);
      break;
      this.mPreLocationImageView.setVisibility(0);
      this.mLocationTextView.setVisibility(0);
      this.mLocationTextView.setText(paramDPObject.getString("DistanceText"));
      return;
    }
    while ((paramInt != 2) && (paramInt != 3));
    this.mDescTextView.setVisibility(8);
    this.mPreLocationImageView.setVisibility(8);
    this.mLocationTextView.setVisibility(8);
    this.mLocDescTextView.setVisibility(0);
    this.mLocDescTextView.setText(paramDPObject.getString("DistanceText"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.widget.HotelProdBestShopWidget
 * JD-Core Version:    0.6.0
 */