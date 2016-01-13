package com.dianping.shopinfo.wed.home.market;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeMarketShopBriefAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_SPECIAL_ITEM = "7000ShopInfo.";
  private static final String HOME_MARKET_BRIEF = "homeMarketBrief";
  private String[] businessRange;
  private DPObject homeMarketBrief;
  private MApiRequest mHomeMarketBriefRequest;
  private final View.OnClickListener shopBriefListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", HomeMarketShopBriefAgent.this.shopId() + ""));
      HomeMarketShopBriefAgent.this.statisticsEvent("shopinfoh", "shopinfoh_mainfo", "2", 0, paramView);
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", HomeMarketShopBriefAgent.this.shopId() + ""));
      HomeMarketShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_info", "", 0, paramView);
      paramView = new StringBuffer("http://m.dianping.com/wed/mobile/homemarketshopbrief/shopId=");
      paramView.append(HomeMarketShopBriefAgent.this.shopId());
      paramView.append("?dpshare=0");
      try
      {
        paramView = URLEncoder.encode(paramView.toString(), "UTF-8");
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
        HomeMarketShopBriefAgent.this.startActivity(localIntent);
        return;
      }
      catch (java.io.UnsupportedEncodingException paramView)
      {
      }
    }
  };
  private String[] shopCharacteristics;
  private boolean showBrief;

  public HomeMarketShopBriefAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createShopBriefAgent()
  {
    DPObject localDPObject = getShop();
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject1;
    Object localObject2;
    int i;
    if (this.showBrief)
    {
      localShopinfoCommonCell.setTitle("商户信息", this.shopBriefListener);
      if (((this.businessRange != null) && (this.businessRange.length > 0)) || ((this.shopCharacteristics != null) && (this.shopCharacteristics.length > 0)))
      {
        localObject1 = (LinearLayout)this.res.inflate(getContext(), R.layout.home_market_shop_brief_layout, getParentView(), false);
        localObject2 = (TextView)((LinearLayout)localObject1).findViewById(R.id.businessRangeView);
        if ((this.businessRange == null) || (this.businessRange.length <= 0))
          break label543;
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("经营范围：");
        i = 0;
        while (i < this.businessRange.length)
        {
          localStringBuffer.append(this.businessRange[i]);
          if (i < this.businessRange.length - 1)
            localStringBuffer.append(" ");
          i += 1;
        }
        ((TextView)localObject2).setText(localStringBuffer.toString());
        localObject2 = (MeasuredGridView)((LinearLayout)localObject1).findViewById(R.id.characteristicsView);
        if ((this.shopCharacteristics == null) || (this.shopCharacteristics.length <= 0))
          break label553;
        ((MeasuredGridView)localObject2).setAdapter(new GridViewAdapter(getContext()));
        label251: ((LinearLayout)localObject1).setOnClickListener(this.shopBriefListener);
        localShopinfoCommonCell.addContent((View)localObject1, false, this.shopBriefListener);
      }
    }
    while (true)
    {
      if ((!localDPObject.getBoolean("IsForeignShop")) && ((localDPObject.getObject("ClientShopStyle") == null) || (!"car_carpark".equals(localDPObject.getString("ShopView")))))
      {
        localObject1 = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
        ((TextView)localObject1).setText("附近停车场");
        statisticsEvent("shopinfo5", "shopinfo5_nearby_parking_show", "", 0);
        ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject1, true, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = new StringBuilder("dianping://nearbyshoplist");
            paramView.append("?shopid=").append(HomeMarketShopBriefAgent.this.shopId());
            paramView.append("&categoryid=").append(180);
            HomeMarketShopBriefAgent.this.getFragment().startActivity(paramView.toString());
            HomeMarketShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_nearby_parking", "", 0);
          }
        })).setGAString("parking", getGAExtra());
      }
      if (localDPObject.getInt("BranchCounts") > 0)
      {
        localObject1 = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
        i = localDPObject.getInt("BranchCounts");
        ((TextView)localObject1).setText("其他" + i + "家分店");
        ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject1, true, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            HomeMarketShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_branch", "", 0);
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", HomeMarketShopBriefAgent.this.shopId() + ""));
            HomeMarketShopBriefAgent.this.statisticsEvent("shopinfoh", "shopinfoh_otherrecommendshop", "", 0, paramView);
            paramView = HomeMarketShopBriefAgent.this.getShop();
            if (paramView == null)
              return;
            Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + HomeMarketShopBriefAgent.this.shopId()));
            localIntent.putExtra("showAddBranchShop", true);
            localIntent.putExtra("shop", paramView);
            HomeMarketShopBriefAgent.this.getFragment().startActivity(localIntent);
          }
        })).setGAString("branch", getGAExtra());
      }
      if ((localDPObject.getDouble("Latitude") != 0.0D) && (localDPObject.getDouble("Longitude") != 0.0D))
      {
        localObject1 = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
        ((TextView)localObject1).setText("附近的店");
        ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject1, true, new View.OnClickListener(localDPObject)
        {
          public void onClick(View paramView)
          {
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", HomeMarketShopBriefAgent.this.shopId() + ""));
            HomeMarketShopBriefAgent.this.statisticsEvent("shopinfoh", "shopinfoh_recommendshop", "", 0, paramView);
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", HomeMarketShopBriefAgent.this.shopId() + ""));
            HomeMarketShopBriefAgent.this.statisticsEvent("shopinfo5", "shopinfo5_nearby", "全部", 0, paramView);
            paramView = new StringBuilder("dianping://nearbyshoplist");
            paramView.append("?shopid=").append(HomeMarketShopBriefAgent.this.shopId());
            paramView.append("&shopname=").append(this.val$shop.getString("Name"));
            paramView.append("&cityid=").append(this.val$shop.getInt("CityID"));
            paramView.append("&shoplatitude=").append(this.val$shop.getDouble("Latitude"));
            paramView.append("&shoplongitude=").append(this.val$shop.getDouble("Longitude"));
            paramView.append("&categoryid=").append(0);
            paramView.append("&category=").append("全部");
            HomeMarketShopBriefAgent.this.getFragment().startActivity(paramView.toString());
          }
        })).setGAString("nearby", getGAExtra());
      }
      return localShopinfoCommonCell;
      label543: ((TextView)localObject2).setVisibility(8);
      break;
      label553: ((MeasuredGridView)localObject2).setVisibility(8);
      break label251;
      localShopinfoCommonCell.hideTitle();
    }
  }

  public static String getHomeMarketBrief()
  {
    return "homeMarketBrief";
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getShop() == null) || (this.homeMarketBrief == null));
    do
    {
      return;
      paramBundle = createShopBriefAgent();
    }
    while (paramBundle == null);
    addCell("7000ShopInfo.", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (shopId() <= 0);
    do
    {
      return;
      if (paramBundle == null)
        continue;
      this.homeMarketBrief = ((DPObject)paramBundle.getParcelable("homeMarketBrief"));
    }
    while (this.homeMarketBrief != null);
    this.mHomeMarketBriefRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/wedding/homemarketbrief.bin?shopid=" + shopId(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mHomeMarketBriefRequest, this);
  }

  public void onDestroy()
  {
    if ((getFragment() != null) && (getFragment().mapiService() != null) && (this.mHomeMarketBriefRequest != null))
      getFragment().mapiService().abort(this.mHomeMarketBriefRequest, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mHomeMarketBriefRequest == paramMApiRequest)
      this.mHomeMarketBriefRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mHomeMarketBriefRequest == paramMApiRequest)
    {
      this.mHomeMarketBriefRequest = null;
      if ((paramMApiResponse != null) && (paramMApiResponse.result() != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.homeMarketBrief = ((DPObject)paramMApiResponse.result());
        this.showBrief = this.homeMarketBrief.getBoolean("ShowBrief");
        if (this.showBrief)
        {
          this.businessRange = this.homeMarketBrief.getStringArray("BusinessRange");
          this.shopCharacteristics = this.homeMarketBrief.getStringArray("ShopCharacteristics");
        }
        setSharedObject("homeMarketBrief", this.homeMarketBrief);
        dispatchAgentChanged(false);
        dispatchAgentChanged("shopinfo/wedhome_market_top", null);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("homeMarketBrief", this.homeMarketBrief);
    return localBundle;
  }

  private class GridViewAdapter extends BaseAdapter
  {
    private Context context;

    public GridViewAdapter(Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    public int getCount()
    {
      return HomeMarketShopBriefAgent.this.shopCharacteristics.length;
    }

    public Object getItem(int paramInt)
    {
      return HomeMarketShopBriefAgent.this.shopCharacteristics[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (LinearLayout)HomeMarketShopBriefAgent.this.getResources().inflate(this.context, R.layout.home_brief_item, null, false);
      if (paramInt % 3 == 0)
        paramView.setGravity(3);
      while (true)
      {
        ((TextView)paramView.findViewById(R.id.home_brief)).setText(HomeMarketShopBriefAgent.this.shopCharacteristics[paramInt]);
        paramView.setOnClickListener(HomeMarketShopBriefAgent.this.shopBriefListener);
        return paramView;
        if (paramInt % 3 == 1)
        {
          paramView.setGravity(17);
          continue;
        }
        if (paramInt % 3 != 2)
          continue;
        paramView.setGravity(5);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.market.HomeMarketShopBriefAgent
 * JD-Core Version:    0.6.0
 */