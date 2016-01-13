package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class MallInfoAgent extends ShopCellAgent
{
  private static final String CELL_MALL_INFO = "7700shopinfo.";
  private static final String SHOP_EXTRA_INFO = "shopExtraInfo";
  private final View.OnClickListener contentListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (MallInfoAgent.this.shopMallUrl != null);
      try
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(MallInfoAgent.this.shopMallUrl));
        MallInfoAgent.this.startActivity(paramView);
        return;
      }
      catch (java.lang.Exception paramView)
      {
      }
    }
  };
  private DPObject shopExtra;
  private String shopMallUrl;

  public MallInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createMallInfoCell(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("ShopMallId");
    this.shopMallUrl = paramDPObject.getString("ShopMallUrl");
    Object localObject2 = paramDPObject.getString("MallPic");
    String str = paramDPObject.getString("ShopMallName");
    Object localObject1 = paramDPObject.getString("PromoInfoInMall");
    paramDPObject = paramDPObject.getString("MallPreferential");
    if ((i <= 0) || (TextUtils.isEmpty(str)))
      return null;
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shop_mall_layout, getParentView(), false);
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)localNovaLinearLayout.findViewById(R.id.content);
    ((NetworkImageView)localNovaRelativeLayout.findViewById(R.id.mall_pic)).setImage((String)localObject2);
    ((TextView)localNovaRelativeLayout.findViewById(R.id.mall_name)).setText(str);
    localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.promo_info_1);
    if (!TextUtils.isEmpty((CharSequence)localObject1))
    {
      ((TextView)localObject2).setVisibility(0);
      ((TextView)localObject2).setText((CharSequence)localObject1);
      localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.mall_discount);
      if (TextUtils.isEmpty(paramDPObject))
        break label238;
      ((TextView)localObject1).setVisibility(0);
      ((TextView)localObject1).setText(paramDPObject);
    }
    while (true)
    {
      localNovaRelativeLayout.setGAString("belongtomall", getGAExtra());
      localNovaRelativeLayout.setOnClickListener(this.contentListener);
      ((TextView)localNovaLinearLayout.findViewById(R.id.title)).setText("所在商场");
      return localNovaLinearLayout;
      ((TextView)localObject2).setVisibility(8);
      break;
      label238: ((TextView)localObject1).setVisibility(8);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
      {
        return;
        this.shopExtra = ((DPObject)super.getSharedObject("shopExtraInfo"));
        if ((this.shopExtra != null) || (paramBundle == null))
          continue;
        this.shopExtra = ((DPObject)paramBundle.getParcelable("shopExtraInfo"));
      }
      while (this.shopExtra == null);
      paramBundle = createMallInfoCell(this.shopExtra);
    }
    while (paramBundle == null);
    addCell("7700shopinfo.", paramBundle, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.MallInfoAgent
 * JD-Core Version:    0.6.0
 */