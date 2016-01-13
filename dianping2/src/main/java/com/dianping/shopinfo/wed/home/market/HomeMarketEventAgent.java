package com.dianping.shopinfo.wed.home.market;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeMarketEventAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_HOME_MARKET_EVENT = "0400HomeMarket.10Event";
  private static final String HOME_MARKET_EVENT_LIST = "homeMarketEventList";
  private ImageView expandIndicator;
  private TextView expandTextView;
  private LinearLayout expandView;
  private DPObject[] homeMarketEventList;
  private boolean isExpand;
  private MApiRequest mHomeMarketEventsRequest;
  private LinearLayout moreEventsView;

  public HomeMarketEventAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getShop() == null) || (this.homeMarketEventList == null) || (this.homeMarketEventList.length <= 0))
      return;
    paramBundle = this.res.inflate(getContext(), R.layout.home_market_event_layout, getParentView(), false);
    Object localObject1 = paramBundle.findViewById(R.id.title_layout);
    LinearLayout localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.first_event);
    this.moreEventsView = ((LinearLayout)paramBundle.findViewById(R.id.more_events));
    this.expandView = ((LinearLayout)paramBundle.findViewById(R.id.expand));
    ((TextView)paramBundle.findViewById(R.id.title)).setText("卖场活动");
    ((View)localObject1).setOnClickListener(new TitleListener(null));
    int i;
    label263: Object localObject2;
    if (this.homeMarketEventList.length > 1)
    {
      this.expandTextView = ((TextView)this.expandView.findViewById(R.id.expand_text));
      this.expandIndicator = ((ImageView)this.expandView.findViewById(R.id.expand_indicator));
      this.expandView.setVisibility(0);
      this.expandView.setOnClickListener(new ExpandListener(null));
      if (!this.isExpand)
      {
        this.moreEventsView.setVisibility(8);
        this.expandTextView.setText("更多" + (this.homeMarketEventList.length - 1) + "个活动");
        this.expandIndicator.setImageDrawable(this.res.getDrawable(R.drawable.arrow_down_shop));
        i = 0;
        if (i >= this.homeMarketEventList.length)
          break label616;
        localObject2 = this.homeMarketEventList[i];
        localObject1 = ((DPObject)localObject2).getString("EventLogo");
        String str1 = ((DPObject)localObject2).getString("EventName");
        String str2 = ((DPObject)localObject2).getString("EventInfo");
        String str3 = ((DPObject)localObject2).getString("EventTime");
        int j = ((DPObject)localObject2).getInt("Visit");
        int k = ((DPObject)localObject2).getInt("EventID");
        localObject2 = this.res.inflate(getContext(), R.layout.home_market_event_item, getParentView(), false);
        NetworkThumbView localNetworkThumbView = (NetworkThumbView)((View)localObject2).findViewById(R.id.logo);
        TextView localTextView1 = (TextView)((View)localObject2).findViewById(R.id.name);
        TextView localTextView2 = (TextView)((View)localObject2).findViewById(R.id.info);
        TextView localTextView3 = (TextView)((View)localObject2).findViewById(R.id.time);
        TextView localTextView4 = (TextView)((View)localObject2).findViewById(R.id.visit);
        View localView = ((View)localObject2).findViewById(R.id.mc_dotted_line);
        if (Build.VERSION.SDK_INT >= 11)
          localView.setLayerType(1, null);
        localNetworkThumbView.setImage((String)localObject1);
        localTextView1.setText(str1);
        localTextView2.setText(str2);
        localTextView3.setText("" + str3);
        localTextView4.setText(j + "");
        ((View)localObject2).setOnClickListener(new EventListener(k, i + 1));
        if (i != 0)
          break label604;
        localLinearLayout.addView((View)localObject2);
      }
    }
    while (true)
    {
      i += 1;
      break label263;
      this.moreEventsView.setVisibility(0);
      this.expandTextView.setText("收起");
      this.expandIndicator.setImageDrawable(this.res.getDrawable(R.drawable.arrow_up_shop));
      break;
      this.expandView.setVisibility(8);
      break;
      label604: this.moreEventsView.addView((View)localObject2);
    }
    label616: addCell("0400HomeMarket.10Event", paramBundle);
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
      this.isExpand = paramBundle.getBoolean("isExpand");
      this.homeMarketEventList = ((DPObject[])(DPObject[])paramBundle.getParcelableArray("homeMarketEventList"));
    }
    while (this.homeMarketEventList != null);
    this.mHomeMarketEventsRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/wedding/homemarketevents.bin?shopid=" + shopId(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mHomeMarketEventsRequest, this);
  }

  public void onDestroy()
  {
    if ((this.mHomeMarketEventsRequest != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.mHomeMarketEventsRequest, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeMarketEventsRequest)
      this.mHomeMarketEventsRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeMarketEventsRequest)
    {
      this.mHomeMarketEventsRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject[])))
      {
        this.homeMarketEventList = ((DPObject[])(DPObject[])paramMApiResponse.result());
        if ((this.homeMarketEventList != null) && (this.homeMarketEventList.length > 0))
          dispatchAgentChanged(false);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("isExpand", this.isExpand);
    localBundle.putParcelableArray("homeMarketEventList", this.homeMarketEventList);
    return localBundle;
  }

  private class EventListener
    implements View.OnClickListener
  {
    private int eventId;
    private int id;

    public EventListener(int paramInt1, int arg3)
    {
      this.eventId = paramInt1;
      int i;
      this.id = i;
    }

    public void onClick(View paramView)
    {
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", HomeMarketEventAgent.this.shopId() + ""));
      HomeMarketEventAgent.this.statisticsEvent("shopinfoh", "shopinfoh_maeve", "", this.id, paramView);
      paramView = new StringBuffer("http://m.dianping.com/wed/mobile/home/market/event/detail/");
      paramView.append(this.eventId + "");
      try
      {
        paramView = URLEncoder.encode(paramView.toString(), "UTF-8");
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
        HomeMarketEventAgent.this.startActivity(localIntent);
        return;
      }
      catch (java.io.UnsupportedEncodingException paramView)
      {
      }
    }
  }

  private class ExpandListener
    implements View.OnClickListener
  {
    private ExpandListener()
    {
    }

    public void onClick(View paramView)
    {
      if (HomeMarketEventAgent.this.isExpand)
      {
        HomeMarketEventAgent.access$202(HomeMarketEventAgent.this, false);
        HomeMarketEventAgent.this.moreEventsView.setVisibility(8);
        HomeMarketEventAgent.this.expandTextView.setText("更多" + (HomeMarketEventAgent.this.homeMarketEventList.length - 1) + "个活动");
        HomeMarketEventAgent.this.expandIndicator.setImageDrawable(HomeMarketEventAgent.this.res.getDrawable(R.drawable.arrow_down_shop));
        return;
      }
      HomeMarketEventAgent.access$202(HomeMarketEventAgent.this, true);
      HomeMarketEventAgent.this.moreEventsView.setVisibility(0);
      HomeMarketEventAgent.this.expandTextView.setText("收起");
      HomeMarketEventAgent.this.expandIndicator.setImageDrawable(HomeMarketEventAgent.this.res.getDrawable(R.drawable.arrow_up_shop));
    }
  }

  private class TitleListener
    implements View.OnClickListener
  {
    private TitleListener()
    {
    }

    public void onClick(View paramView)
    {
      paramView = new ArrayList();
      paramView.add(new BasicNameValuePair("shopid", HomeMarketEventAgent.this.shopId() + ""));
      HomeMarketEventAgent.this.statisticsEvent("shopinfoh", "shopinfoh_maeve_more", "", 0, paramView);
      paramView = new StringBuffer("http://m.dianping.com/wed/mobile/home/market/event/shop/");
      paramView.append(HomeMarketEventAgent.this.shopId());
      try
      {
        paramView = URLEncoder.encode(paramView.toString(), "UTF-8");
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
        HomeMarketEventAgent.this.startActivity(localIntent);
        return;
      }
      catch (java.io.UnsupportedEncodingException paramView)
      {
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.market.HomeMarketEventAgent
 * JD-Core Version:    0.6.0
 */