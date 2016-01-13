package com.dianping.movie.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ShopPower;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.HashMap;
import java.util.Map;

public class PurchasePromoteShopView extends NovaLinearLayout
  implements View.OnClickListener
{
  private Context context;
  private DPObject[] dpShops;

  public PurchasePromoteShopView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PurchasePromoteShopView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  private View createPromoteShopItem(DPObject paramDPObject, int paramInt)
  {
    View localView1 = LayoutInflater.from(getContext()).inflate(R.layout.promote_shop_item, null, false);
    View localView2 = localView1.findViewById(R.id.promote_shop_item);
    NetworkThumbView localNetworkThumbView = (NetworkThumbView)localView1.findViewById(R.id.promote_shop_img);
    TextView localTextView1 = (TextView)localView1.findViewById(R.id.text_name);
    ImageView localImageView1 = (ImageView)localView1.findViewById(R.id.ic_tuan);
    ImageView localImageView2 = (ImageView)localView1.findViewById(R.id.ic_promo);
    ImageView localImageView3 = (ImageView)localView1.findViewById(R.id.ic_book);
    ShopPower localShopPower = (ShopPower)localView1.findViewById(R.id.shop_power);
    TextView localTextView2 = (TextView)localView1.findViewById(R.id.text_detail_name);
    localView2.setTag(paramDPObject);
    localView2.setTag(R.id.movie_editionflag, Integer.valueOf(paramInt));
    localView2.setOnClickListener(this);
    localNetworkThumbView.setImage(paramDPObject.getString("ImgUrl"));
    localTextView1.setText(paramDPObject.getString("ShopName"));
    localTextView2.setText(paramDPObject.getString("RegionName") + "  " + paramDPObject.getString("CategoryName"));
    if (paramDPObject.getBoolean("HasDeals"))
      localImageView1.setVisibility(0);
    if (paramDPObject.getBoolean("Bookable"))
      localImageView3.setVisibility(0);
    if (paramDPObject.getBoolean("HasPromos"))
      localImageView2.setVisibility(0);
    if (paramDPObject.getInt("Score") > 50);
    for (paramInt = 50; ; paramInt = paramDPObject.getInt("Score"))
    {
      localShopPower.setPower(paramInt);
      return localView1;
    }
  }

  public void onClick(View paramView)
  {
    if (DPObjectUtils.isDPObjectof(paramView.getTag(), "PromotionShop"))
    {
      DPObject localDPObject = (DPObject)paramView.getTag();
      Object localObject = new Intent("android.intent.action.VIEW", Uri.parse(localDPObject.getString("ClickUrl")));
      getContext().startActivity((Intent)localObject);
      localObject = new HashMap();
      ((Map)localObject).put("act", String.valueOf(2));
      ((Map)localObject).put("adidx", String.valueOf(((Integer)paramView.getTag(R.id.movie_editionflag)).intValue() + 1));
      AdClientUtils.report(localDPObject.getString("Feedback"), (Map)localObject);
    }
  }

  public void setShopList(DPObject[] paramArrayOfDPObject)
  {
    this.dpShops = paramArrayOfDPObject;
    if ((this.dpShops == null) || (this.dpShops.length == 0));
    while (true)
    {
      return;
      int i = 0;
      while (i < this.dpShops.length)
      {
        addView(createPromoteShopItem(this.dpShops[i], i));
        i += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.PurchasePromoteShopView
 * JD-Core Version:    0.6.0
 */