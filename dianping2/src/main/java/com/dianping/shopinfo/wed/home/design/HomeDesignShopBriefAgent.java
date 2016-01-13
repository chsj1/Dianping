package com.dianping.shopinfo.wed.home.design;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeDesignShopBriefAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  static final String CELL_SPECIAL_ITEM = "0900ShopInfo.";
  static final String SHOP_EXTRA_INFO = "shopExtraInfoMall";
  DPObject[] briefInfoPairs;
  final View.OnClickListener contentListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopdetails"));
      paramView.putExtra("shop", HomeDesignShopBriefAgent.this.getShop());
      paramView.putExtra("shopid", HomeDesignShopBriefAgent.this.shopId());
      paramView.putExtra("shopextra", HomeDesignShopBriefAgent.this.shopExtra);
      paramView.putParcelableArrayListExtra("featurelist", HomeDesignShopBriefAgent.this.mCharacteristicList);
      HomeDesignShopBriefAgent.this.getFragment().startActivity(paramView);
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", HomeDesignShopBriefAgent.this.shopId() + ""));
      HomeDesignShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_info", "", 0, paramView);
    }
  };
  final View.OnClickListener designShopListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", HomeDesignShopBriefAgent.this.shopId() + ""));
      HomeDesignShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_info", "", 0, paramView);
      paramView = HomeDesignShopBriefAgent.this.getGAExtra();
      paramView.shop_id = Integer.valueOf(HomeDesignShopBriefAgent.this.shopId());
      GAHelper.instance().contextStatisticsEvent(HomeDesignShopBriefAgent.this.getContext(), "shopprofile_info", paramView, "tap");
      paramView = new StringBuffer("http://m.dianping.com/wed/mobile/homeshopbrief/");
      paramView.append("shopId=").append(HomeDesignShopBriefAgent.this.shopId()).append("?dpshare=0");
      try
      {
        paramView = URLEncoder.encode(paramView.toString(), "UTF-8");
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
        HomeDesignShopBriefAgent.this.startActivity(localIntent);
        return;
      }
      catch (java.io.UnsupportedEncodingException paramView)
      {
      }
    }
  };
  String homeCategoryType;
  ArrayList<DPObject> mCharacteristicList = new ArrayList();
  MApiRequest mHomeShopExtraRequest;
  MApiRequest mShopBriefRequest;
  MApiRequest mShopExtraRequest;
  MApiRequest mShopFeatureRequest;
  DPObject shopBrief;
  DPObject shopExtra;

  public HomeDesignShopBriefAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    DPObject localDPObject = getShop();
    if (localDPObject == null)
      return;
    int m = 1;
    int i = 1;
    int k = 1;
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject1;
    Object localObject2;
    Object localObject3;
    if ((this.homeCategoryType != null) && ("home_design".equals(this.homeCategoryType)))
      if ((this.briefInfoPairs != null) && (this.briefInfoPairs.length > 0))
      {
        paramBundle = (LinearLayout)this.res.inflate(getContext(), R.layout.home_shop_brief_layout, getParentView(), false);
        localObject1 = (TextView)paramBundle.findViewById(R.id.shopBusinessTimeKey);
        localObject2 = (TextView)paramBundle.findViewById(R.id.shopBusinessTimeValue);
        localObject3 = this.briefInfoPairs[0];
        ((TextView)localObject1).setText(((DPObject)localObject3).getString("ID"));
        ((TextView)localObject2).setText(((DPObject)localObject3).getString("Name"));
        paramBundle.findViewById(R.id.shopBusinessTimeLay).setVisibility(0);
        if (this.briefInfoPairs.length > 1)
        {
          localObject1 = (TextView)paramBundle.findViewById(R.id.shopScaleKey);
          localObject2 = (TextView)paramBundle.findViewById(R.id.shopScalevalue);
          localObject3 = this.briefInfoPairs[1];
          ((TextView)localObject1).setText(((DPObject)localObject3).getString("ID"));
          ((TextView)localObject2).setText(((DPObject)localObject3).getString("Name"));
          paramBundle.findViewById(R.id.shopScaleLay).setVisibility(0);
        }
        if (this.briefInfoPairs.length > 2)
        {
          localObject1 = (TextView)paramBundle.findViewById(R.id.shopQualityGuaranteeKey);
          localObject2 = (TextView)paramBundle.findViewById(R.id.shopQualityGuaranteeValue);
          localObject3 = this.briefInfoPairs[2];
          ((TextView)localObject1).setText(((DPObject)localObject3).getString("ID"));
          ((TextView)localObject2).setText(((DPObject)localObject3).getString("Name"));
          paramBundle.findViewById(R.id.shopQualityGuaranteeLay).setVisibility(0);
        }
        paramBundle.setOnClickListener(this.designShopListener);
        localShopinfoCommonCell.addContent(paramBundle, false, this.designShopListener);
        localShopinfoCommonCell.setTitle("商户信息", this.designShopListener);
      }
    while (true)
    {
      if (localDPObject.getInt("BranchCounts") > 0)
      {
        paramBundle = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
        i = localDPObject.getInt("BranchCounts");
        paramBundle.setText("其他" + i + "家分店");
        ((NovaRelativeLayout)localShopinfoCommonCell.addContent(paramBundle, true, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = HomeDesignShopBriefAgent.this.getShop();
            if (paramView == null);
            do
            {
              return;
              Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + HomeDesignShopBriefAgent.this.shopId()));
              localIntent.putExtra("showAddBranchShop", true);
              localIntent.putExtra("shop", paramView);
              HomeDesignShopBriefAgent.this.getFragment().startActivity(localIntent);
              HomeDesignShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_branch", "", 0);
            }
            while (!HomeDesignShopBriefAgent.this.isWeddingShopType());
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", HomeDesignShopBriefAgent.this.shopId() + ""));
            HomeDesignShopBriefAgent.this.statisticsEvent("shopinfoq", "shopinfoq_otherrecommendshop", "", 0, paramView);
          }
        })).setGAString("branch", getGAExtra());
      }
      if ((localDPObject.getDouble("Latitude") != 0.0D) && (localDPObject.getDouble("Longitude") != 0.0D))
      {
        paramBundle = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
        paramBundle.setText("附近的店");
        ((NovaRelativeLayout)localShopinfoCommonCell.addContent(paramBundle, true, new View.OnClickListener(localDPObject)
        {
          public void onClick(View paramView)
          {
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", HomeDesignShopBriefAgent.this.shopId() + ""));
            HomeDesignShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_nearby", "全部", 0, paramView);
            if (HomeDesignShopBriefAgent.this.isWeddingShopType())
            {
              paramView = new ArrayList();
              paramView.add(new BasicNameValuePair("shopid", HomeDesignShopBriefAgent.this.shopId() + ""));
              HomeDesignShopBriefAgent.this.statisticsEvent("shopinfoq", "shopinfoq_recommendshop", "", 0, paramView);
            }
            paramView = new StringBuilder("dianping://nearbyshoplist");
            paramView.append("?shopid=").append(HomeDesignShopBriefAgent.this.shopId());
            paramView.append("&shopname=").append(this.val$shop.getString("Name"));
            paramView.append("&cityid=").append(this.val$shop.getInt("CityID"));
            paramView.append("&shoplatitude=").append(this.val$shop.getDouble("Latitude"));
            paramView.append("&shoplongitude=").append(this.val$shop.getDouble("Longitude"));
            paramView.append("&categoryid=").append(0);
            paramView.append("&category=").append("全部");
            HomeDesignShopBriefAgent.this.getFragment().startActivity(paramView.toString());
          }
        })).setGAString("nearby", getGAExtra());
      }
      if (localShopinfoCommonCell == null)
        break;
      addCell("0900ShopInfo.", localShopinfoCommonCell, 0);
      return;
      localShopinfoCommonCell.hideTitle();
      continue;
      localObject2 = (LinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_feature_layout, getParentView(), false);
      int j = m;
      Object localObject4;
      if (this.mCharacteristicList != null)
      {
        j = m;
        if (this.mCharacteristicList.size() > 0)
        {
          paramBundle = (LinearLayout)((LinearLayout)localObject2).findViewById(R.id.shop_characteristic_lay);
          paramBundle.setVisibility(0);
          paramBundle.removeAllViews();
          localObject1 = this.mCharacteristicList.iterator();
          while (((Iterator)localObject1).hasNext())
          {
            localObject4 = (DPObject)((Iterator)localObject1).next();
            localObject3 = (NetworkImageView)this.res.inflate(getContext(), R.layout.characteristic_item, null, false);
            localObject4 = ((DPObject)localObject4).getObject("FeatureTag");
            ((NetworkImageView)localObject3).setImage(((DPObject)localObject4).getString("PictureUrl"));
            Log.i("PictureUrl", ((DPObject)localObject4).getString("PictureUrl"));
            paramBundle.addView((View)localObject3);
          }
          j = 0;
        }
      }
      paramBundle = (TextView)((LinearLayout)localObject2).findViewById(R.id.service_period);
      localObject3 = (TextView)((LinearLayout)localObject2).findViewById(R.id.navigation);
      ((TextView)localObject3).setVisibility(8);
      if (this.shopExtra != null)
      {
        i = k;
        if (!TextUtils.isEmpty(this.shopExtra.getString("Time")))
        {
          paramBundle.setVisibility(0);
          paramBundle.setText("营业时间： " + this.shopExtra.getString("Time"));
          i = 0;
        }
        localObject1 = this.shopExtra.getString("Path");
        localObject4 = this.shopExtra.getString("Pathtime");
        if (!TextUtils.isEmpty((CharSequence)localObject1))
        {
          ((TextView)localObject3).setVisibility(0);
          paramBundle = (Bundle)localObject1;
          if (!TextUtils.isEmpty((CharSequence)localObject4))
            paramBundle = (String)localObject1 + " " + (String)localObject4;
          ((TextView)localObject3).setText("距离位置： " + paramBundle);
          i = 0;
        }
      }
      if ((j != 0) && (i != 0))
      {
        localShopinfoCommonCell.hideTitle();
        continue;
      }
      localShopinfoCommonCell.setTitle("商户信息", this.contentListener);
      localShopinfoCommonCell.titleLay.setGAString("info", getGAExtra());
      ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject2, false, this.contentListener)).setGAString("info", getGAExtra());
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mHomeShopExtraRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/wedding/homeshopextra.bin?shopid=" + shopId(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mHomeShopExtraRequest, this);
  }

  public void onDestroy()
  {
    if ((getFragment() != null) && (getFragment().mapiService() != null))
    {
      if (this.mHomeShopExtraRequest != null)
        getFragment().mapiService().abort(this.mHomeShopExtraRequest, this, true);
      if (this.mShopBriefRequest != null)
        getFragment().mapiService().abort(this.mShopBriefRequest, this, true);
      if (this.mShopFeatureRequest != null)
        getFragment().mapiService().abort(this.mShopFeatureRequest, this, true);
      if (this.mShopExtraRequest != null)
        getFragment().mapiService().abort(this.mShopExtraRequest, this, true);
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopFeatureRequest)
      this.mShopFeatureRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mHomeShopExtraRequest == paramMApiRequest)
    {
      this.mHomeShopExtraRequest = null;
      if ((paramMApiResponse != null) && (paramMApiResponse.result() != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.homeCategoryType = ((DPObject)paramMApiResponse.result()).getString("HomeCategoryType");
        if ((this.homeCategoryType == null) || (!"home_design".equals(this.homeCategoryType)))
          break label127;
        this.mShopBriefRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/wedding/homeshopbrief.bin?shopid=" + shopId(), CacheType.DISABLED);
        getFragment().mapiService().exec(this.mShopBriefRequest, this);
      }
    }
    label127: 
    do
    {
      do
      {
        while (true)
        {
          return;
          this.mShopFeatureRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/shopfeaturetags.bin?shopid=" + shopId(), CacheType.DISABLED);
          new Handler().postDelayed(new Runnable()
          {
            public void run()
            {
              if (HomeDesignShopBriefAgent.this.mShopFeatureRequest != null)
                HomeDesignShopBriefAgent.this.getFragment().mapiService().exec(HomeDesignShopBriefAgent.this.mShopFeatureRequest, HomeDesignShopBriefAgent.this);
            }
          }
          , 100L);
          paramMApiRequest = Uri.parse("http://m.api.dianping.com/mshop/shopextra.bin?shopid=" + shopId()).buildUpon();
          if (location() != null)
            paramMApiRequest.appendQueryParameter("lng", String.valueOf(location().longitude())).appendQueryParameter("lat", String.valueOf(location().latitude()));
          this.mShopExtraRequest = BasicMApiRequest.mapiGet(paramMApiRequest.build().toString(), CacheType.NORMAL);
          getFragment().mapiService().exec(this.mShopExtraRequest, this);
          return;
          if (this.mShopBriefRequest != paramMApiRequest)
            break;
          this.mShopBriefRequest = null;
          if ((paramMApiResponse == null) || (paramMApiResponse.result() == null) || (!(paramMApiResponse.result() instanceof DPObject)))
            continue;
          this.shopBrief = ((DPObject)paramMApiResponse.result());
          this.briefInfoPairs = this.shopBrief.getArray("BriefInfoPairs");
          dispatchAgentChanged(false);
          return;
        }
        if (this.mShopFeatureRequest != paramMApiRequest)
          continue;
        this.mShopFeatureRequest = null;
        this.mCharacteristicList.addAll(Arrays.asList((DPObject[])(DPObject[])paramMApiResponse.result()));
        dispatchAgentChanged(false);
        return;
      }
      while (this.mShopExtraRequest != paramMApiRequest);
      this.mShopExtraRequest = null;
    }
    while ((paramMApiResponse == null) || (!(paramMApiResponse.result() instanceof DPObject)));
    this.shopExtra = ((DPObject)paramMApiResponse.result());
    setSharedObject("shopExtraInfoMall", this.shopExtra);
    paramMApiRequest = new Bundle();
    paramMApiRequest.putParcelable("shopExtraInfoMall", this.shopExtra);
    dispatchAgentChanged(false);
    dispatchAgentChanged("shopinfo/common_mallinfo", paramMApiRequest);
  }

  public Bundle saveInstanceState()
  {
    return super.saveInstanceState();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.design.HomeDesignShopBriefAgent
 * JD-Core Version:    0.6.0
 */