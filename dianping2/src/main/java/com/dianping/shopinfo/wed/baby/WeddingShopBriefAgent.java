package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class WeddingShopBriefAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String CELL_SHOPBRIEF = "7000WeddingBrief.";
  private DPObject dpShopBrief;
  private MApiRequest shopBriefRequest;

  public WeddingShopBriefAgent(Object paramObject)
  {
    super(paramObject);
    sendBriefRequest();
  }

  private void sendBriefRequest()
  {
    if (this.shopBriefRequest != null);
    do
      return;
    while (shopId() <= 0);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/shopbriefinfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.shopBriefRequest = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.shopBriefRequest, this);
  }

  private View setupCommercialTenantInfoView()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    int i = 0;
    Object localObject1 = (LinearLayout)this.res.inflate(getContext(), R.layout.item_commercial_tenant_shopinfo_item, getParentView(), false);
    Object localObject4 = (LinearLayout)((LinearLayout)localObject1).findViewById(R.id.businessTimeLinearLayout);
    TextView localTextView2 = (TextView)((LinearLayout)localObject1).findViewById(R.id.businessTime);
    Object localObject3 = (RelativeLayout)((LinearLayout)localObject1).findViewById(R.id.characteristicFrameLayout);
    Object localObject2 = (TextView)((LinearLayout)localObject1).findViewById(R.id.characteristic);
    LinearLayout localLinearLayout = (LinearLayout)((LinearLayout)localObject1).findViewById(R.id.starttipsLinearLayout);
    TextView localTextView1 = (TextView)((LinearLayout)localObject1).findViewById(R.id.starttipsTextView);
    int k = this.dpShopBrief.getInt("Available");
    if (!android.text.TextUtils.isEmpty(this.dpShopBrief.getString("BusinessHours")))
    {
      ((LinearLayout)localObject4).setVisibility(0);
      localTextView2.setText(this.dpShopBrief.getString("BusinessHours"));
      i = 1;
    }
    localObject4 = this.dpShopBrief.getStringArray("Characteristics");
    int j = i;
    if (localObject4 != null)
    {
      int m = localObject4.length;
      j = i;
      if (m != 0)
      {
        ((RelativeLayout)localObject3).setVisibility(0);
        localObject3 = new StringBuilder();
        i = 0;
        while (i < m)
        {
          ((StringBuilder)localObject3).append(localObject4[i]);
          if (i != m - 1)
            ((StringBuilder)localObject3).append(" ");
          i += 1;
        }
        ((TextView)localObject2).setText(((StringBuilder)localObject3).toString());
        j = 1;
      }
    }
    if ((getShop() != null) && (!com.dianping.util.TextUtils.isEmpty(getShop().getString("StarTips"))))
    {
      localObject2 = getShop().getString("StarTips");
      localLinearLayout.setVisibility(0);
      localTextView1.setText((CharSequence)localObject2);
    }
    if (j != 0)
      localShopinfoCommonCell.addContent((View)localObject1, false, this);
    if ((!getShop().getBoolean("IsForeignShop")) && ((getShop().getObject("ClientShopStyle") == null) || (!"car_carpark".equals(getShop().getString("ShopView")))))
    {
      localObject1 = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
      ((TextView)localObject1).setText("附近停车场");
      localShopinfoCommonCell.addContent((View)localObject1, true, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = Uri.parse("dianping://nearbyshoplist").buildUpon();
          paramView.appendQueryParameter("shopid", WeddingShopBriefAgent.this.shopId() + "");
          paramView.appendQueryParameter("categoryid", "180");
          paramView = new Intent("android.intent.action.VIEW", paramView.build());
          WeddingShopBriefAgent.this.getFragment().startActivity(paramView);
          GAHelper.instance().contextStatisticsEvent(WeddingShopBriefAgent.this.getContext(), "shopprofile_parking", WeddingShopBriefAgent.this.getGAExtra(), "tap");
        }
      });
    }
    if (k == 1)
      localShopinfoCommonCell.setTitle("商户信息", this);
    while (k == 1)
    {
      return localShopinfoCommonCell;
      localShopinfoCommonCell.hideTitle();
    }
    return (View)(View)(View)(View)null;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (this.dpShopBrief == null);
    do
    {
      do
        return;
      while ((getShop() == null) || (getShopStatus() != 0));
      paramBundle = setupCommercialTenantInfoView();
    }
    while (paramBundle == null);
    addCell("7000WeddingBrief.", paramBundle);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (((i == R.id.wed_shopinfo_detail) || (i == R.id.title_layout) || (i == R.id.wed_linear_environment) || (i == R.id.wed_textview_envs)) && (!android.text.TextUtils.isEmpty(this.dpShopBrief.getString("DetailLink"))))
    {
      paramView = Uri.parse("dianping://complexweb").buildUpon();
      paramView.appendQueryParameter("url", this.dpShopBrief.getString("DetailLink"));
      startActivity(new Intent("android.intent.action.VIEW", paramView.build()));
      GAHelper.instance().contextStatisticsEvent(getContext(), "shopprofile_more", getGAExtra(), "tap");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopBriefRequest)
      this.shopBriefRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopBriefRequest)
    {
      this.shopBriefRequest = null;
      this.dpShopBrief = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingShopBriefAgent
 * JD-Core Version:    0.6.0
 */