package com.dianping.membercard.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaFrameLayout;

public class AvailableCardListItem extends NovaFrameLayout
{
  public RelativeLayout cardLoc;
  public TextView cardProduct;
  public TextView category;
  public CheckBox checkBox;
  public TextView distance;
  public View dividerView;
  public ImageView loc;
  public ImageView newImage;
  public TextView region;
  public TextView shopName;
  public ImageView status;

  public AvailableCardListItem(Context paramContext)
  {
    super(paramContext);
  }

  public AvailableCardListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean isChecked()
  {
    return this.checkBox.isChecked();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shopName = ((TextView)findViewById(R.id.shop_name));
    this.cardProduct = ((TextView)findViewById(R.id.card_product));
    this.dividerView = findViewById(R.id.list_divider);
    this.distance = ((TextView)findViewById(R.id.shop_distance));
    this.region = ((TextView)findViewById(R.id.shop_region));
    this.category = ((TextView)findViewById(R.id.shop_category));
    this.status = ((ImageView)findViewById(R.id.card_status));
    this.newImage = ((ImageView)findViewById(R.id.ic_new));
    this.checkBox = ((CheckBox)findViewById(R.id.check_box));
    this.cardLoc = ((RelativeLayout)findViewById(R.id.card_loc));
  }

  public void setAvailableCard(DPObject paramDPObject, int paramInt)
  {
    String str1 = paramDPObject.getString("Title");
    String str2 = paramDPObject.getString("SubTitle");
    Object localObject;
    if ((paramInt == 2) || (paramInt == 1) || (paramInt == 3))
    {
      this.shopName.setText(str2);
      localObject = paramDPObject.getArray("ProductList");
      if ((localObject != null) && (localObject.length > 0))
      {
        str1 = localObject[0].getString("ProductName");
        if (str1 != null)
        {
          localObject = str1;
          if (str1.contains("|"))
          {
            int i = str1.indexOf("|");
            localObject = str1.substring(0, i);
            str1 = str1.substring(i + 1);
            localObject = (String)localObject + str1;
          }
          this.cardProduct.setText((CharSequence)localObject);
        }
      }
      if (TextUtils.isEmpty(paramDPObject.getString("Region")))
        break label390;
      this.region.setText(paramDPObject.getString("Region"));
      this.region.setVisibility(0);
      label174: if (TextUtils.isEmpty(paramDPObject.getString("Category")))
        break label402;
      this.category.setText(paramDPObject.getString("Category"));
      this.category.setVisibility(0);
      label207: if (TextUtils.isEmpty(paramDPObject.getString("DistanceDesc")))
        break label414;
      this.distance.setText(paramDPObject.getString("DistanceDesc"));
      this.distance.setVisibility(0);
      label240: if (paramInt != 2)
        break label426;
      this.status.setVisibility(4);
      label253: if (!paramDPObject.getBoolean("IsNew"))
        break label469;
      this.newImage.setVisibility(0);
      label270: if (paramInt != 2)
        break label481;
      this.checkBox.setVisibility(0);
      this.checkBox.setChecked(false);
    }
    while (true)
    {
      if (paramInt == 3)
      {
        this.cardProduct.setVisibility(8);
        this.checkBox.setVisibility(8);
        this.newImage.setVisibility(8);
        this.cardLoc.setVisibility(8);
      }
      return;
      localObject = str1;
      if (!TextUtils.isEmpty(paramDPObject.getString("SubTitle")))
        localObject = str1 + "(" + str2 + ")";
      this.shopName.setText((CharSequence)localObject);
      break;
      label390: this.region.setVisibility(8);
      break label174;
      label402: this.category.setVisibility(8);
      break label207;
      label414: this.distance.setVisibility(8);
      break label240;
      label426: this.status.setVisibility(0);
      if (paramDPObject.getBoolean("Joined"))
      {
        this.status.setBackgroundResource(R.drawable.mc_icon_joined);
        break label253;
      }
      this.status.setBackgroundResource(R.drawable.mc_addvip);
      break label253;
      label469: this.newImage.setVisibility(8);
      break label270;
      label481: this.checkBox.setVisibility(8);
    }
  }

  public void setChecked()
  {
    if (isChecked())
    {
      this.checkBox.setChecked(false);
      return;
    }
    this.checkBox.setChecked(true);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.AvailableCardListItem
 * JD-Core Version:    0.6.0
 */