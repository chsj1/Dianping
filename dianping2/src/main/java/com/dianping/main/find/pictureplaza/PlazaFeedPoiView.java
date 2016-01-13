package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaRelativeLayout;

public class PlazaFeedPoiView extends NovaRelativeLayout
{
  private final int REVIEW_TYPE_MOVIE = 1;
  private ImageView poiImageView;
  private TextView shopDistance;
  private TextView shopNameView;

  public PlazaFeedPoiView(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaFeedPoiView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shopNameView = ((TextView)findViewById(R.id.shop_name));
    this.shopDistance = ((TextView)findViewById(R.id.shop_distance));
    this.poiImageView = ((ImageView)findViewById(R.id.icon_locate));
  }

  public void setPoiInfo(DPObject paramDPObject, int paramInt)
  {
    this.shopNameView.setText(paramDPObject.getString("ShopName"));
    this.shopDistance.setText(paramDPObject.getString("Distance"));
    this.poiImageView.setImageResource(0);
    if (paramInt == 1)
    {
      this.poiImageView.setImageResource(R.drawable.icon_poi_movie);
      return;
    }
    this.poiImageView.setImageResource(R.drawable.icon_locate);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaFeedPoiView
 * JD-Core Version:    0.6.0
 */