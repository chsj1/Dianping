package com.dianping.shopinfo.wed.baby;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.wed.baby.widget.BabyFeatureAdapter;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import com.dianping.widget.view.NovaTextView;

public class CommercialTenantInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, AdapterView.OnItemClickListener
{
  private static final String COMMERCIAL_TENANT_SPECIFIC_ORDER_BABY = "2800BabyShopBrief.";
  private final String REQUEST_URL = "http://m.api.dianping.com/wedding/shopbriefinfo.bin?";
  StringBuilder mSb = new StringBuilder();
  private DPObject mShop;
  DPObject mShopInfo;
  private MApiRequest mShopInfoRequest;

  public CommercialTenantInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    this.mSb.setLength(0);
    this.mSb.append("http://m.api.dianping.com/wedding/shopbriefinfo.bin?").append("shopid=").append(shopId());
    this.mShopInfoRequest = BasicMApiRequest.mapiGet(Uri.parse(this.mSb.toString()).buildUpon().build().toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mShopInfoRequest, this);
  }

  private View setupCommercialTenantInfoView()
  {
    if (this.mShopInfo.getInt("UiFlag") == 1)
    {
      localObject1 = LayoutInflater.from(getContext()).inflate(R.layout.wed_shopinfo_layout, getParentView(), false);
      this.mShopInfo.getInt("Available");
      localObject2 = this.mShopInfo.getString("DetailLink");
      localObject3 = (NovaRelativeLayout)((View)localObject1).findViewById(R.id.wed_shopinfo_detail);
      if (android.text.TextUtils.isEmpty((CharSequence)localObject2))
      {
        ((NovaRelativeLayout)localObject3).setVisibility(8);
        ((View)localObject1).findViewById(R.id.view_wed_line_detail).setVisibility(8);
        localObject2 = (MeasuredGridView)((View)localObject1).findViewById(R.id.wed_gridview_features);
        localObject3 = this.mShopInfo.getStringArray("Characteristics");
        if ((localObject3 != null) && (localObject3.length != 0))
          break label474;
        ((MeasuredGridView)localObject2).setVisibility(8);
        label129: localObject2 = (LinearLayout)((View)localObject1).findViewById(R.id.businessTimeLinearLayout);
        localObject4 = (TextView)((View)localObject1).findViewById(R.id.businessTime);
        if (!android.text.TextUtils.isEmpty(this.mShopInfo.getString("BusinessHours")))
        {
          ((LinearLayout)localObject2).setVisibility(0);
          ((LinearLayout)localObject2).setOnClickListener(this);
          ((TextView)localObject4).setText(this.mShopInfo.getString("BusinessHours"));
        }
        localObject4 = (LinearLayout)((View)localObject1).findViewById(R.id.starttipsLinearLayout);
        localObject5 = (TextView)((View)localObject1).findViewById(R.id.starttipsTextView);
        if ((getShop() != null) && (!com.dianping.util.TextUtils.isEmpty(getShop().getString("StarTips"))))
        {
          localObject6 = getShop().getString("StarTips");
          ((LinearLayout)localObject4).setVisibility(0);
          ((LinearLayout)localObject4).setOnClickListener(this);
          ((TextView)localObject5).setText((CharSequence)localObject6);
        }
        if (((localObject3 == null) || (localObject3.length == 0)) && (android.text.TextUtils.isEmpty(this.mShopInfo.getString("BusinessHours"))) && (com.dianping.util.TextUtils.isEmpty(getShop().getString("StarTips"))))
          ((View)localObject1).findViewById(R.id.view_wed_line_features).setVisibility(8);
        localObject3 = (NovaLinearLayout)((View)localObject1).findViewById(R.id.wed_linear_amuse);
        localObject4 = this.mShopInfo.getArray("BabyProject");
        ((NovaLinearLayout)localObject3).setGAString("shopprofile_item");
        if ((localObject4 != null) && (localObject4.length != 0))
          break label499;
        ((View)localObject1).findViewById(R.id.wed_textview_amu).setVisibility(8);
        ((NovaLinearLayout)localObject3).setVisibility(8);
        ((View)localObject1).findViewById(R.id.view_wed_line_amuse).setVisibility(8);
      }
      while (true)
      {
        localObject3 = (LinearLayout)((View)localObject1).findViewById(R.id.wed_linear_environment);
        localObject4 = this.mShopInfo.getStringArray("BabyEnvPics");
        if ((localObject4 != null) && (localObject4.length != 0))
          break label799;
        ((View)localObject1).findViewById(R.id.wed_textview_envs).setVisibility(8);
        ((LinearLayout)localObject3).setVisibility(8);
        ((LinearLayout)localObject2).setPadding(((LinearLayout)localObject2).getPaddingLeft(), ((LinearLayout)localObject2).getPaddingTop(), ((LinearLayout)localObject2).getPaddingRight(), 0);
        return localObject1;
        ((NovaRelativeLayout)localObject3).setOnClickListener(this);
        break;
        label474: ((MeasuredGridView)localObject2).setAdapter(new BabyFeatureAdapter(getContext(), localObject3));
        ((MeasuredGridView)localObject2).setOnItemClickListener(this);
        break label129;
        label499: j = (getContext().getResources().getDisplayMetrics().widthPixels - ViewUtils.dip2px(getContext(), 50.0F)) / 3;
        i = this.mShopInfo.getInt("BabyEnvPicWidth");
        f = this.mShopInfo.getInt("BabyEnvPicHeight") * 1.0F / i;
        k = (int)(j * f);
        localObject5 = (NovaTextView)((View)localObject1).findViewById(R.id.wed_textview_amu);
        ((NovaTextView)localObject5).setGAString("shopprofile_item");
        ((NovaTextView)localObject5).setText(this.mShopInfo.getString("BabyTitle"));
        i = 0;
        while ((i < localObject4.length) && (i <= 2))
        {
          localObject5 = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.wed_shopinfo_amusement, getParentView(), false);
          ((TextView)((NovaLinearLayout)localObject5).findViewById(R.id.wed_shop_amusement)).setText(localObject4[i].getString("Name"));
          localObject6 = (NetworkImageView)((NovaLinearLayout)localObject5).findViewById(R.id.wed_shop_amusement_image);
          localObject7 = localObject4[i].getString("ID");
          ((NetworkImageView)localObject6).setIsCorner(true);
          ((NetworkImageView)localObject6).setImage((String)localObject7);
          ((NetworkImageView)localObject6).getLayoutParams().height = k;
          ((NetworkImageView)localObject6).getLayoutParams().width = j;
          localObject6 = new LinearLayout.LayoutParams(0, -2);
          ((LinearLayout.LayoutParams)localObject6).weight = 1.0F;
          if (i != 0)
            ((LinearLayout.LayoutParams)localObject6).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
          ((NovaLinearLayout)localObject3).addView((View)localObject5, (ViewGroup.LayoutParams)localObject6);
          i += 1;
        }
        ((NovaLinearLayout)localObject3).setOnClickListener(this);
        ((View)localObject1).findViewById(R.id.wed_textview_amu).setOnClickListener(this);
      }
      label799: j = (getContext().getResources().getDisplayMetrics().widthPixels - ViewUtils.dip2px(getContext(), 50.0F)) / 3;
      i = this.mShopInfo.getInt("BabyEnvPicWidth");
      float f = this.mShopInfo.getInt("BabyEnvPicHeight") * 1.0F / i;
      k = (int)(j * f);
      i = 0;
      while ((i < localObject4.length) && (i <= 2))
      {
        localObject2 = (NetworkImageView)this.res.inflate(getContext(), R.layout.wed_shopinfo_environment, (ViewGroup)localObject3, false);
        ((NetworkImageView)localObject2).setImage(localObject4[i]);
        localObject5 = new LinearLayout.LayoutParams(j, k);
        if (i != 0)
          ((LinearLayout.LayoutParams)localObject5).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
        ((LinearLayout)localObject3).addView((View)localObject2, (ViewGroup.LayoutParams)localObject5);
        i += 1;
      }
      ((LinearLayout)localObject3).setOnClickListener(this);
      ((View)localObject1).findViewById(R.id.wed_textview_envs).setOnClickListener(this);
      return localObject1;
    }
    Object localObject1 = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    int i = 0;
    Object localObject2 = (LinearLayout)this.res.inflate(getContext(), R.layout.item_commercial_tenant_shopinfo_item, getParentView(), false);
    Object localObject7 = (LinearLayout)((LinearLayout)localObject2).findViewById(R.id.businessTimeLinearLayout);
    TextView localTextView = (TextView)((LinearLayout)localObject2).findViewById(R.id.businessTime);
    Object localObject6 = (RelativeLayout)((LinearLayout)localObject2).findViewById(R.id.characteristicFrameLayout);
    Object localObject5 = (TextView)((LinearLayout)localObject2).findViewById(R.id.characteristic);
    Object localObject3 = (LinearLayout)((LinearLayout)localObject2).findViewById(R.id.starttipsLinearLayout);
    Object localObject4 = (TextView)((LinearLayout)localObject2).findViewById(R.id.starttipsTextView);
    int k = this.mShopInfo.getInt("Available");
    if (!android.text.TextUtils.isEmpty(this.mShopInfo.getString("BusinessHours")))
    {
      ((LinearLayout)localObject7).setVisibility(0);
      localTextView.setText(this.mShopInfo.getString("BusinessHours"));
      i = 1;
    }
    localObject7 = this.mShopInfo.getStringArray("Characteristics");
    int j = i;
    if (localObject7 != null)
    {
      int m = localObject7.length;
      j = i;
      if (m != 0)
      {
        ((RelativeLayout)localObject6).setVisibility(0);
        localObject6 = new StringBuilder();
        i = 0;
        while (i < m)
        {
          ((StringBuilder)localObject6).append(localObject7[i]);
          if (i != m - 1)
            ((StringBuilder)localObject6).append(" ");
          i += 1;
        }
        ((TextView)localObject5).setText(((StringBuilder)localObject6).toString());
        j = 1;
      }
    }
    if ((getShop() != null) && (!com.dianping.util.TextUtils.isEmpty(getShop().getString("StarTips"))))
    {
      localObject5 = getShop().getString("StarTips");
      ((LinearLayout)localObject3).setVisibility(0);
      ((TextView)localObject4).setText((CharSequence)localObject5);
    }
    if (j != 0)
      ((ShopinfoCommonCell)localObject1).addContent((View)localObject2, false, this);
    if ((!this.mShop.getBoolean("IsForeignShop")) && ((this.mShop.getObject("ClientShopStyle") == null) || (!"car_carpark".equals(this.mShop.getString("ShopView")))))
    {
      localObject2 = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
      ((TextView)localObject2).setText("附近停车场");
      ((ShopinfoCommonCell)localObject1).addContent((View)localObject2, true, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          CommercialTenantInfoAgent.this.mSb.setLength(0);
          CommercialTenantInfoAgent.this.mSb.append("dianping://nearbyshoplist");
          CommercialTenantInfoAgent.this.mSb.append("?shopid=").append(CommercialTenantInfoAgent.this.shopId());
          CommercialTenantInfoAgent.this.mSb.append("&categoryid=").append(180);
          CommercialTenantInfoAgent.this.getFragment().startActivity(CommercialTenantInfoAgent.this.mSb.toString());
          GAHelper.instance().contextStatisticsEvent(CommercialTenantInfoAgent.this.getContext(), "shopprofile_parking", CommercialTenantInfoAgent.this.getGAExtra(), "tap");
        }
      });
    }
    if (k == 1)
    {
      ((ShopinfoCommonCell)localObject1).setTitle("商户信息", this);
      if (k != 1)
        break label1439;
    }
    while (true)
    {
      return localObject1;
      ((ShopinfoCommonCell)localObject1).hideTitle();
      break;
      label1439: localObject1 = null;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    this.mShop = getShop();
    if (this.mShop == null);
    do
    {
      do
        return;
      while ((this.mShopInfo == null) || (getShopStatus() != 0));
      paramBundle = setupCommercialTenantInfoView();
    }
    while (paramBundle == null);
    addCell("2800BabyShopBrief.", paramBundle);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if ((i == R.id.wed_shopinfo_detail) || (i == R.id.title_layout) || (i == R.id.wed_linear_environment) || (i == R.id.wed_textview_envs) || (i == R.id.starttipsLinearLayout) || (i == R.id.businessTimeLinearLayout) || (i == R.id.wed_linear_amuse) || (i == R.id.wed_textview_amu))
    {
      if (!android.text.TextUtils.isEmpty(this.mShopInfo.getString("DetailLink")))
      {
        this.mSb.setLength(0);
        startActivity("dianping://complexweb?url=" + this.mShopInfo.getString("DetailLink"));
      }
      GAHelper.instance().contextStatisticsEvent(getContext(), "shopprofile_more", getGAExtra(), "tap");
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    if (this.mShopInfoRequest != null)
    {
      getFragment().mapiService().abort(this.mShopInfoRequest, this, true);
      this.mShopInfoRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (!android.text.TextUtils.isEmpty(this.mShopInfo.getString("DetailLink")))
    {
      this.mSb.setLength(0);
      startActivity("dianping://complexweb?url=" + this.mShopInfo.getString("DetailLink"));
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopInfoRequest)
      this.mShopInfoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopInfoRequest)
    {
      this.mShopInfoRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.mShopInfo = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.CommercialTenantInfoAgent
 * JD-Core Version:    0.6.0
 */