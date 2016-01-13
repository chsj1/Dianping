package com.dianping.shopinfo.education.widget;

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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class EducationGridProductAdapter extends WedBaseAdapter
{
  public EducationGridProductAdapter(Context paramContext, DPObject[] paramArrayOfDPObject, int paramInt)
  {
    this.context = paramContext;
    this.adapterData = paramArrayOfDPObject;
    this.coverStyleType = paramInt;
    this.albumFrameWidth = (ViewUtils.getScreenWidthPixels(paramContext) * 43 / 100);
    this.albumFrameHeight = (this.albumFrameWidth * 210 / 280);
    this.verticalAlbumFrameWidth = this.albumFrameWidth;
    this.verticalAlbumFrameHeight = (this.verticalAlbumFrameWidth * 374 / 280);
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    if (paramView == null)
      localView = LayoutInflater.from(this.context).inflate(R.layout.item_of_education_product_photo, paramViewGroup, false);
    paramView = (DPObject)getItem(paramInt);
    ((NovaLinearLayout)localView).setGAString("edu_product", paramView.getString("Name"), paramInt);
    if (paramView == null)
      return localView;
    paramViewGroup = paramView.getString("DefaultPic");
    Object localObject = (NetworkImageView)getAdapterView(localView, R.id.img_shop_photo);
    if (this.coverStyleType == 2)
      ((NetworkImageView)localObject).getLayoutParams().width = this.verticalAlbumFrameWidth;
    int i;
    for (((NetworkImageView)localObject).getLayoutParams().height = this.verticalAlbumFrameHeight; ; ((NetworkImageView)localObject).getLayoutParams().height = this.albumFrameHeight)
    {
      ((NetworkImageView)localObject).setImage(paramViewGroup);
      ((TextView)getAdapterView(localView, R.id.lay_img_desc_title)).setText(paramView.getString("Name"));
      localObject = (TextView)getAdapterView(localView, R.id.lay_img_desc_price);
      paramViewGroup = (TextView)getAdapterView(localView, R.id.lay_img_desc_origprice);
      paramInt = paramView.getInt("Price");
      i = paramView.getInt("OriginPrice");
      if (paramInt > 0)
        break;
      ((TextView)localObject).setVisibility(4);
      paramViewGroup.setVisibility(4);
      return localView;
      ((NetworkImageView)localObject).getLayoutParams().width = this.albumFrameWidth;
    }
    getAdapterView(localView, R.id.lay_img_desc_no_price).setVisibility(8);
    ((TextView)localObject).setVisibility(0);
    paramViewGroup.setVisibility(0);
    ((TextView)localObject).setText("¥ " + PriceFormatUtils.formatPrice(paramInt));
    if (i > 0)
    {
      paramView = new SpannableString("¥" + i);
      paramView.setSpan(new StrikethroughSpan(), 0, paramView.length(), 33);
      paramViewGroup.setText(paramView);
      paramViewGroup.getPaint().setFlags(16);
      return localView;
    }
    paramViewGroup.setText("");
    paramViewGroup.setVisibility(4);
    return (View)localView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.widget.EducationGridProductAdapter
 * JD-Core Version:    0.6.0
 */