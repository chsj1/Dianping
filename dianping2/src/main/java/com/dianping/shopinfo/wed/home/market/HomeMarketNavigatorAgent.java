package com.dianping.shopinfo.wed.home.market;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeMarketNavigatorAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_NAVIGATOR = "0600HomeMarket.10Navigator";
  private static final String HOME_MARKET_NAVIGATORS = "homeMarketNavigators";
  private DPObject[] homeMarketNavigators;
  private MApiRequest mHomeMarketNavigator;

  public HomeMarketNavigatorAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getShop() == null) || (this.homeMarketNavigators == null) || (this.homeMarketNavigators.length <= 0))
      return;
    paramBundle = this.res.inflate(getContext(), R.layout.home_market_horizontal_layout, getParentView(), false);
    paramBundle.findViewById(R.id.title_layout).setOnClickListener(new ClickNavigatorListener("", -1));
    ((TextView)paramBundle.findViewById(R.id.title)).setText("卖场导航");
    LinearLayout localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.item_list_gallery);
    int j = 0;
    if (j < this.homeMarketNavigators.length)
    {
      localObject1 = this.res.inflate(getContext(), R.layout.home_market_navigator_item, getParentView(), false);
      Object localObject4 = (TextView)((View)localObject1).findViewById(R.id.category_name);
      Object localObject5 = (LinearLayout)((View)localObject1).findViewById(R.id.middle_line);
      Object localObject6 = (TextView)((View)localObject1).findViewById(R.id.floor_num);
      Object localObject3 = (TextView)((View)localObject1).findViewById(R.id.first_line);
      localObject2 = (TextView)((View)localObject1).findViewById(R.id.second_line);
      String str1 = this.homeMarketNavigators[j].getString("FloorNum");
      Object localObject7 = this.homeMarketNavigators[j].getObject("MainCategory");
      DPObject[] arrayOfDPObject = this.homeMarketNavigators[j].getArray("SecondCategoryList");
      String str2 = ((DPObject)localObject7).getString("CategoryName");
      int n = ((DPObject)localObject7).getInt("CategoryID");
      int k = ViewUtils.dip2px(getContext(), 28.0F) / 2;
      int i = 0;
      while (i < k + 1)
      {
        localObject7 = new LinearLayout.LayoutParams(1, 2);
        ImageView localImageView = new ImageView(getContext());
        localImageView.setLayoutParams((ViewGroup.LayoutParams)localObject7);
        localImageView.setImageDrawable(this.res.getDrawable(R.drawable.home_market_vertical_dottedline));
        ((LinearLayout)localObject5).addView(localImageView);
        i += 1;
      }
      ((TextView)localObject4).setText(str2);
      ((TextView)localObject6).setText(str1);
      float f1;
      int m;
      label388: float f2;
      if (arrayOfDPObject.length > 0)
      {
        f1 = 0.0F;
        localObject5 = new StringBuffer();
        localObject4 = new StringBuffer();
        i = 0;
        m = 0;
        k = m;
        if (i < arrayOfDPObject.length)
        {
          localObject6 = arrayOfDPObject[i].getString("CategoryName");
          f2 = f1 + ((String)localObject6).length();
          if (f2 > 7.0F)
            break label594;
          ((StringBuffer)localObject5).append((String)localObject6);
          if (i != arrayOfDPObject.length - 1)
            break label611;
          ((TextView)localObject3).setText(((StringBuffer)localObject5).toString());
          k = 1;
        }
        label459: f1 = 0.0F;
        if (k != 0)
          break label716;
        ((TextView)localObject2).setVisibility(0);
        label472: if (i < arrayOfDPObject.length)
        {
          localObject3 = arrayOfDPObject[i].getString("CategoryName");
          f2 = f1 + ((String)localObject3).length();
          if (f2 > 7.0F)
            break label657;
          ((StringBuffer)localObject4).append((String)localObject3);
          if (i != arrayOfDPObject.length - 1)
            break label670;
          ((TextView)localObject2).setText(((StringBuffer)localObject4).toString());
        }
      }
      while (true)
      {
        ((View)localObject1).setOnClickListener(new ClickNavigatorListener(str1 + "" + n, j + 1));
        localLinearLayout.addView((View)localObject1);
        j += 1;
        break;
        label594: ((TextView)localObject3).setText(((StringBuffer)localObject5).toString());
        k = m;
        break label459;
        label611: f1 = f2;
        if (f2 < 7.0F)
        {
          f1 = f2;
          if (i < arrayOfDPObject.length - 1)
          {
            f1 = (float)(f2 + 0.5D);
            ((StringBuffer)localObject5).append("  ");
          }
        }
        i += 1;
        break label388;
        label657: ((TextView)localObject2).setText(((StringBuffer)localObject4).toString());
        continue;
        label670: f1 = f2;
        if (f2 < 7.0F)
        {
          f1 = f2;
          if (i < arrayOfDPObject.length - 1)
          {
            f1 = (float)(f2 + 0.5D);
            ((StringBuffer)localObject4).append("  ");
          }
        }
        i += 1;
        break label472;
        label716: ((TextView)localObject2).setVisibility(8);
      }
    }
    Object localObject1 = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 126.0F), ViewUtils.dip2px(getContext(), 92.0F));
    ((LinearLayout.LayoutParams)localObject1).setMargins(0, 0, ViewUtils.dip2px(getContext(), 10.0F), 0);
    Object localObject2 = new ImageView(getContext());
    ((ImageView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject1);
    ((ImageView)localObject2).setScaleType(ImageView.ScaleType.FIT_XY);
    ((ImageView)localObject2).setImageDrawable(this.res.getDrawable(R.drawable.home_market_floor_more));
    ((ImageView)localObject2).setOnClickListener(new ClickNavigatorListener("", -2));
    localLinearLayout.addView((View)localObject2);
    addCell("0600HomeMarket.10Navigator", paramBundle);
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
      this.homeMarketNavigators = ((DPObject[])(DPObject[])paramBundle.getParcelableArray("homeMarketNavigators"));
    }
    while (this.homeMarketNavigators != null);
    this.mHomeMarketNavigator = BasicMApiRequest.mapiGet("http://m.api.dianping.com/wedding/homemarketnavigators.bin?shopid=" + shopId(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mHomeMarketNavigator, this);
  }

  public void onDestroy()
  {
    if ((this.mHomeMarketNavigator != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.mHomeMarketNavigator, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeMarketNavigator)
      this.mHomeMarketNavigator = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeMarketNavigator)
    {
      this.mHomeMarketNavigator = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject[])))
      {
        this.homeMarketNavigators = ((DPObject[])(DPObject[])paramMApiResponse.result());
        if ((this.homeMarketNavigators != null) && (this.homeMarketNavigators.length > 0))
          dispatchAgentChanged(false);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelableArray("homeMarketNavigators", this.homeMarketNavigators);
    return localBundle;
  }

  private class ClickNavigatorListener
    implements View.OnClickListener
  {
    private String floorCategory;
    private int id;

    public ClickNavigatorListener(String paramInt, int arg3)
    {
      this.floorCategory = paramInt;
      int i;
      this.id = i;
    }

    public void onClick(View paramView)
    {
      paramView = new StringBuffer("http://m.dianping.com/wed/mobile/home/market/navigator/");
      paramView.append(HomeMarketNavigatorAgent.this.shopId() + "?");
      Object localObject;
      if (!TextUtils.isEmpty(this.floorCategory))
      {
        paramView.append("shop=");
        paramView.append(this.floorCategory);
        paramView.append("&");
        localObject = new ArrayList();
        ((List)localObject).add(new BasicNameValuePair("shopid", HomeMarketNavigatorAgent.this.shopId() + ""));
        HomeMarketNavigatorAgent.this.statisticsEvent("shopinfoh", "shopinfoh_manav", "", this.id, (List)localObject);
      }
      while (true)
      {
        paramView.append("dpshare=0");
        try
        {
          paramView = URLEncoder.encode(paramView.toString(), "UTF-8");
          localObject = new Intent("android.intent.action.VIEW");
          ((Intent)localObject).setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
          HomeMarketNavigatorAgent.this.startActivity((Intent)localObject);
          return;
          if (this.id == -1)
          {
            localObject = new ArrayList();
            ((List)localObject).add(new BasicNameValuePair("shopid", HomeMarketNavigatorAgent.this.shopId() + ""));
            HomeMarketNavigatorAgent.this.statisticsEvent("shopinfoh", "shopinfoh_manav_more1", "", 0, (List)localObject);
            continue;
          }
          if (this.id != -2)
            continue;
          localObject = new ArrayList();
          ((List)localObject).add(new BasicNameValuePair("shopid", HomeMarketNavigatorAgent.this.shopId() + ""));
          HomeMarketNavigatorAgent.this.statisticsEvent("shopinfoh", "shopinfoh_manav_more2", "", 0, (List)localObject);
        }
        catch (java.io.UnsupportedEncodingException paramView)
        {
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.market.HomeMarketNavigatorAgent
 * JD-Core Version:    0.6.0
 */