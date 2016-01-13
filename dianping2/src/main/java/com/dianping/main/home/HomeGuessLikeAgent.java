package com.dianping.main.home;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianping.adapter.BasicRecyclerAdapter;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.GuessLikeItem;
import com.dianping.model.Location;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R;
import com.dianping.widget.LoadingErrorView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeGuessLikeAgent extends HomeAgent
  implements HomeAgent.OnCellRefreshListener, HomeAgent.OnCellRetryListener
{
  public static final String GUESS_TAG = "60Guess.";
  public static int adapterTypeCount = 5;
  Adapter adapter;
  boolean isShowRefresh;
  double lat;
  LocationListener listener;
  double lng;
  int localCityId = 0;
  Location location;
  protected MApiRequest mReq;
  List<Boolean> markFlagList;
  List<Boolean> markTuiGuangList;
  String moreUrlSchema = "";
  String networkType;
  String queryId;
  String requestId;
  String sessionId = "";

  public HomeGuessLikeAgent(Object paramObject)
  {
    super(paramObject);
  }

  public MApiRequest createRequest(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/operating/guesslike.bin");
    String str2 = getFragment().accountService().token();
    if (paramInt == 0)
      this.sessionId = "";
    Object localObject3 = "";
    Object localObject4 = "";
    String str1 = "0";
    this.location = location();
    Object localObject2;
    Object localObject1;
    if (this.location != null)
    {
      this.lat = this.location.latitude();
      this.lng = this.location.longitude();
      localObject2 = localObject3;
      localObject1 = localObject4;
      if (this.lat != 0.0D)
      {
        localObject2 = localObject3;
        localObject1 = localObject4;
        if (this.lng != 0.0D)
        {
          localObject2 = localObject3;
          localObject1 = localObject4;
          if (this.lat != (-1.0D / 0.0D))
          {
            localObject2 = localObject3;
            localObject1 = localObject4;
            if (this.lat != (1.0D / 0.0D))
            {
              localObject2 = localObject3;
              localObject1 = localObject4;
              if (this.lng != (-1.0D / 0.0D))
              {
                localObject2 = localObject3;
                localObject1 = localObject4;
                if (this.lng != (1.0D / 0.0D))
                {
                  localObject2 = Location.FMT.format(this.lat) + "";
                  localObject1 = Location.FMT.format(this.lng) + "";
                }
              }
            }
          }
        }
      }
      localObject3 = localObject2;
      localObject4 = localObject1;
      if (this.location.city() != null)
      {
        str1 = "" + this.location.city().id();
        this.localCityId = this.location.city().id();
        localObject4 = localObject1;
        localObject3 = localObject2;
      }
    }
    while (true)
    {
      localObject2 = ((ConnectivityManager)DPApplication.instance().getApplicationContext().getSystemService("connectivity")).getNetworkInfo(1);
      this.requestId = UUID.randomUUID().toString();
      localObject1 = null;
      this.networkType = NetworkUtils.getNetworkType(getContext());
      if (((NetworkInfo)localObject2).isAvailable())
      {
        localObject1 = NetworkUtils.wifiInfo();
        localObject1 = ((String)localObject1).substring(((String)localObject1).indexOf("|") + 1);
      }
      return BasicMApiRequest.mapiPost(localStringBuilder.toString(), new String[] { "choosecityid", cityId() + "", "token", str2, "latitude", localObject3, "longitude", localObject4, "haswifi", "" + ((NetworkInfo)localObject2).isConnected(), "wifiinfo", localObject1, "loccityid", str1, "start", "" + paramInt, "SessionId", this.sessionId });
      localObject3 = Location.FMT.format(0L) + "";
      localObject4 = Location.FMT.format(0L) + "";
    }
  }

  GuessLikeListItem createTuanListItem(ViewGroup paramViewGroup)
  {
    return (GuessLikeListItem)this.res.inflate(getContext(), R.layout.guesslike_list_item, paramViewGroup, false);
  }

  public boolean equalsLocation(Location paramLocation1, Location paramLocation2)
  {
    if (paramLocation1 == paramLocation2);
    do
    {
      return true;
      if ((paramLocation1 == null) || (paramLocation2 == null))
        return false;
    }
    while ((Location.FMT.format(paramLocation1.latitude()).equals(Location.FMT.format(paramLocation2.latitude()))) && (Location.FMT.format(paramLocation1.longitude()).equals(Location.FMT.format(paramLocation2.longitude()))));
    return false;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((paramBundle != null) && ("refreshflag".equals(paramBundle.getString("flag"))) && (this.isShowRefresh != ((MainHomeFragment)getFragment()).getShowRefresh()))
    {
      this.isShowRefresh = ((MainHomeFragment)getFragment()).getShowRefresh();
      this.adapter.notifyMergeItemRangeChanged();
    }
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    this.adapter.reset();
    if (!paramCity1.equals(paramCity2))
    {
      this.markFlagList.clear();
      this.markTuiGuangList.clear();
      this.markFlagList = new ArrayList();
      this.markTuiGuangList = new ArrayList();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().cityConfig().addListener(this);
    this.adapter = new Adapter();
    addCell("60Guess.", this.adapter);
    this.markFlagList = new ArrayList();
    this.markTuiGuangList = new ArrayList();
    this.listener = new LocationListener()
    {
      public void onLocationChanged(LocationService paramLocationService)
      {
        Object localObject = null;
        try
        {
          paramLocationService = (Location)paramLocationService.location().decodeToObject(Location.DECODER);
          if (!HomeGuessLikeAgent.this.equalsLocation(paramLocationService, HomeGuessLikeAgent.this.location))
            HomeGuessLikeAgent.this.adapter.reset();
          return;
        }
        catch (java.lang.Exception paramLocationService)
        {
          while (true)
            paramLocationService = localObject;
        }
      }
    };
    getFragment().locationService().addListener(this.listener);
  }

  public void onDestroy()
  {
    super.onDestroy();
    DPApplication.instance().cityConfig().removeListener(this);
    if (this.listener != null)
      getFragment().locationService().removeListener(this.listener);
    if (this.adapter != null)
      this.adapter.destory();
  }

  public void onRefresh()
  {
    this.adapter.reset();
    this.markTuiGuangList.clear();
    this.markFlagList.clear();
    this.markFlagList = new ArrayList();
    this.markTuiGuangList = new ArrayList();
    dispatchAgentChanged(false);
  }

  public void onRetry()
  {
    this.adapter.reset();
    this.adapter.loadNewPage();
  }

  public void setItemClickListener(LinearLayout paramLinearLayout, HomeGuessLikeItem paramHomeGuessLikeItem, int paramInt)
  {
    paramLinearLayout.setOnClickListener(new View.OnClickListener(paramHomeGuessLikeItem, paramInt)
    {
      // ERROR //
      public void onClick(View paramView)
      {

      }
    });
  }

  public boolean showRetry()
  {
    return true;
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
    implements RequestHandler<MApiRequest, MApiResponse>
  {
    final Object MORE = new Object();
    ArrayList<HomeGuessLikeItem> itemList = new ArrayList();
    protected String mErrorMsg;
    protected boolean mIsEnd;
    protected int mNextStartIndex;
    protected MApiService mapiService;
    int type_error = 3;
    int type_item = 0;
    int type_loading = 1;
    int type_more = 4;
    int type_title = 2;

    Adapter()
    {
      super();
      this.type_item += getDefaultType();
      this.type_loading += getDefaultType();
      this.type_title += getDefaultType();
      this.type_error += getDefaultType();
      this.type_more += getDefaultType();
    }

    protected void appendData(HomeGuessLikeList paramHomeGuessLikeList)
    {
      boolean bool2 = true;
      if (paramHomeGuessLikeList == null)
      {
        this.mErrorMsg = "服务器开了个小差，请稍后再试";
        notifyMergeItemRangeChanged();
        return;
      }
      boolean bool1 = bool2;
      if (paramHomeGuessLikeList.homeItemList != null)
      {
        if (paramHomeGuessLikeList.homeItemList.length != 0)
          break label198;
        bool1 = bool2;
      }
      while (true)
      {
        this.mIsEnd = bool1;
        this.mNextStartIndex = paramHomeGuessLikeList.nextStartIndex;
        if (paramHomeGuessLikeList.homeItemList == null)
          break;
        int i = 0;
        label64: if (i >= paramHomeGuessLikeList.homeItemList.length)
          break;
        HomeGuessLikeItem localHomeGuessLikeItem = paramHomeGuessLikeList.homeItemList[i];
        this.itemList.add(localHomeGuessLikeItem);
        HomeGuessLikeAgent.this.markFlagList.add(Boolean.valueOf(false));
        HomeGuessLikeAgent.this.markTuiGuangList.add(Boolean.valueOf(false));
        if ((localHomeGuessLikeItem.extContentJson != null) && (!TextUtils.isEmpty(localHomeGuessLikeItem.extContentJson.toString())));
        try
        {
          if ((!TextUtils.isEmpty(localHomeGuessLikeItem.extContentJson.optString("isad", null))) && (localHomeGuessLikeItem.extContentJson.getString("isad").equals("1")))
            HomeAgent.record(1, localHomeGuessLikeItem, i, localHomeGuessLikeItem.extContentJson.getString("feedback"));
          i += 1;
          break label64;
          label198: bool1 = paramHomeGuessLikeList.isEnd;
        }
        catch (JSONException localJSONException)
        {
          while (true)
            localJSONException.printStackTrace();
        }
      }
    }

    public void destory()
    {
      if (HomeGuessLikeAgent.this.mReq != null)
      {
        HomeGuessLikeAgent.this.getFragment().mapiService().abort(HomeGuessLikeAgent.this.mReq, this, true);
        HomeGuessLikeAgent.this.mReq = null;
      }
    }

    public int getCount()
    {
      if (HomeGuessLikeAgent.this.isShowRefresh);
      do
        return 0;
      while ((this.mIsEnd) && (this.itemList.size() == 0));
      return this.itemList.size() + 2;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt == 0)
        return HEAD;
      if (paramInt - 1 < this.itemList.size())
        return this.itemList.get(paramInt - 1);
      if (this.mIsEnd)
        return this.MORE;
      if (this.mErrorMsg == null)
        return LOADING;
      return ERROR;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof GuessLikeItem))
        return this.type_item;
      if (localObject == LOADING)
        return this.type_loading;
      if (localObject == this.MORE)
        return this.type_more;
      if (localObject == HEAD)
        return this.type_title;
      return this.type_error;
    }

    public int getViewTypeCount()
    {
      return HomeGuessLikeAgent.adapterTypeCount;
    }

    public void initViewWithData(GuessLikeListItem paramGuessLikeListItem, HomeGuessLikeItem paramHomeGuessLikeItem, int paramInt)
    {
      Object localObject1;
      Object localObject3;
      if (HomeGuessLikeAgent.this.cityId() == HomeGuessLikeAgent.this.localCityId)
      {
        paramGuessLikeListItem.setDeal(paramHomeGuessLikeItem, HomeGuessLikeAgent.this.lat, HomeGuessLikeAgent.this.lng);
        localObject1 = null;
        localObject3 = null;
        if ((paramHomeGuessLikeItem.extContentJson == null) || (TextUtils.isEmpty(paramHomeGuessLikeItem.extContentJson.toString())))
          break label377;
        localObject3 = localObject1;
      }
      while (true)
      {
        try
        {
          String str = paramHomeGuessLikeItem.extContentJson.optString("adid");
          localObject3 = str;
          if (!paramHomeGuessLikeItem.extContentJson.getString("isad").equals("1"))
            continue;
          localObject3 = str;
          if (((Boolean)HomeGuessLikeAgent.this.markFlagList.get(paramInt)).booleanValue())
            continue;
          localObject3 = str;
          HomeAgent.record(3, paramHomeGuessLikeItem, paramInt, paramHomeGuessLikeItem.extContentJson.getString("feedback"));
          localObject3 = str;
          HomeGuessLikeAgent.this.markFlagList.set(paramInt, Boolean.valueOf(true));
          localObject3 = str;
          HomeGuessLikeAgent.this.markTuiGuangList.set(paramInt, Boolean.valueOf(true));
          localObject1 = str;
          paramGuessLikeListItem.gaUserInfo.dealgroup_id = Integer.valueOf(paramHomeGuessLikeItem.id);
          paramGuessLikeListItem.gaUserInfo.index = Integer.valueOf(paramInt);
          paramGuessLikeListItem.gaUserInfo.ad_id = localObject1;
          paramGuessLikeListItem.gaUserInfo.query_id = HomeGuessLikeAgent.this.queryId;
          paramGuessLikeListItem.setGAString("reculike");
          HomeGuessLikeAgent.this.setItemClickListener(paramGuessLikeListItem, paramHomeGuessLikeItem, paramInt);
          ((NovaActivity)HomeGuessLikeAgent.this.getContext()).addGAView(paramGuessLikeListItem, paramInt, "home", "home".equals(((NovaActivity)HomeGuessLikeAgent.this.getContext()).getPageName()));
          return;
          paramGuessLikeListItem.setDeal(paramHomeGuessLikeItem, 0.0D, 0.0D);
          break;
          localObject1 = str;
          localObject3 = str;
          if (((Boolean)HomeGuessLikeAgent.this.markTuiGuangList.get(paramInt)).booleanValue())
            continue;
          localObject3 = str;
          HomeGuessLikeAgent.this.markTuiGuangList.set(paramInt, Boolean.valueOf(true));
          localObject1 = str;
          continue;
        }
        catch (JSONException localObject2)
        {
          localJSONException.printStackTrace();
          localObject2 = localObject3;
          continue;
        }
        label377: Object localObject2 = localObject3;
        if (((Boolean)HomeGuessLikeAgent.this.markTuiGuangList.get(paramInt)).booleanValue())
          continue;
        HomeGuessLikeAgent.this.markTuiGuangList.set(paramInt, Boolean.valueOf(true));
        localObject2 = localObject3;
      }
    }

    protected boolean loadNewPage()
    {
      if (this.mIsEnd);
      do
      {
        return false;
        if ((HomeGuessLikeAgent.this.mReq != null) || (DPApplication.instance().cityConfig().currentCity() == null) || (HomeGuessLikeAgent.this.getFragment() == null) || (HomeGuessLikeAgent.this.cityId() <= 0))
          continue;
        this.mErrorMsg = null;
        HomeGuessLikeAgent.this.mReq = HomeGuessLikeAgent.this.createRequest(this.mNextStartIndex);
        if (HomeGuessLikeAgent.this.mReq != null)
        {
          if ((this.mapiService == null) && (HomeGuessLikeAgent.this.getFragment() != null))
            this.mapiService = HomeGuessLikeAgent.this.getFragment().mapiService();
          if (this.mapiService != null)
            this.mapiService.exec(HomeGuessLikeAgent.this.mReq, this);
        }
        return true;
      }
      while (HomeGuessLikeAgent.this.mReq != null);
      this.mIsEnd = true;
      return false;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      int i = getItemViewType(paramInt);
      if (i == this.type_loading)
        if (this.mErrorMsg == null)
          loadNewPage();
      do
      {
        return;
        if (i == this.type_item)
        {
          initViewWithData((GuessLikeListItem)((BasicRecyclerAdapter.BasicHolder)paramViewHolder).view, (HomeGuessLikeItem)getItem(paramInt), paramInt - 1);
          return;
        }
        if (i != this.type_error)
          continue;
        paramViewHolder = (BasicRecyclerAdapter.FailHolder)paramViewHolder;
        paramViewHolder.text.setText(this.mErrorMsg);
        paramViewHolder.errorView.setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            HomeGuessLikeAgent.Adapter.this.loadNewPage();
            HomeGuessLikeAgent.Adapter.this.notifyMergeItemRangeChanged();
          }
        });
        return;
      }
      while (i != this.type_more);
      ((BasicRecyclerAdapter.BasicHolder)paramViewHolder).view.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(HomeGuessLikeAgent.this.moreUrlSchema))
          {
            HomeGuessLikeAgent.this.getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(HomeGuessLikeAgent.this.moreUrlSchema)));
            HomeGuessLikeAgent.this.statisticsEvent("index5", "index5_recommend", "更多", 0);
          }
        }
      });
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      1 local1 = null;
      if (paramInt == this.type_title)
        local1 = new BasicRecyclerAdapter.BasicHolder(HomeGuessLikeAgent.this.res.inflate(HomeGuessLikeAgent.this.getContext(), R.layout.home_competitive_title, paramViewGroup, false))
        {
          public void init(View paramView)
          {
            ((TextView)paramView.findViewById(R.id.title)).setText("猜你喜欢");
          }
        };
      do
      {
        return local1;
        if (paramInt == this.type_loading)
          return new BasicRecyclerAdapter.BasicHolder(this, getLoadingView(paramViewGroup));
        if (paramInt == this.type_item)
          return new BasicRecyclerAdapter.BasicHolder(this, HomeGuessLikeAgent.this.createTuanListItem(paramViewGroup));
        if (paramInt == this.type_error)
          return new BasicRecyclerAdapter.FailHolder(this, getFailedView(paramViewGroup));
      }
      while (paramInt != this.type_more);
      return new BasicRecyclerAdapter.BasicHolder(this, HomeGuessLikeAgent.this.res.inflate(HomeGuessLikeAgent.this.getContext(), R.layout.home_foot_view, paramViewGroup, false));
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (HomeGuessLikeAgent.this.mReq == paramMApiRequest)
      {
        HomeGuessLikeAgent.this.mReq = null;
        if (paramMApiResponse.message() != null)
          break label60;
      }
      label60: for (paramMApiRequest = "请求失败，请稍后再试"; ; paramMApiRequest = paramMApiResponse.message().content())
      {
        setErrorMsg(paramMApiRequest);
        if (this.mNextStartIndex == 0)
        {
          HomeGuessLikeAgent.this.isRequestStatus = 3;
          HomeGuessLikeAgent.this.sendRefreshShowMessage();
        }
        return;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (HomeGuessLikeAgent.this.mReq == paramMApiRequest)
      {
        HomeGuessLikeAgent.this.mReq = null;
        if (this.mNextStartIndex == 0)
        {
          HomeGuessLikeAgent.this.isRequestStatus = 2;
          HomeGuessLikeAgent.this.sendRefreshShowMessage();
        }
        paramMApiRequest = null;
      }
      try
      {
        paramMApiResponse = (HomeGuessLikeList)((DPObject)paramMApiResponse.result()).decodeToObject(HomeGuessLikeList.DECODER);
        paramMApiRequest = paramMApiResponse;
        HomeGuessLikeAgent.this.moreUrlSchema = paramMApiResponse.moreUrlSchema;
        paramMApiRequest = paramMApiResponse;
        HomeGuessLikeAgent.this.queryId = paramMApiResponse.queryID;
        paramMApiRequest = paramMApiResponse;
        HomeGuessLikeAgent.this.sessionId = paramMApiResponse.sessionId;
        paramMApiRequest = paramMApiResponse;
        appendData(paramMApiRequest);
        return;
      }
      catch (ArchiveException paramMApiResponse)
      {
        while (true)
          paramMApiResponse.printStackTrace();
      }
    }

    public void reset()
    {
      this.itemList.clear();
      this.mNextStartIndex = 0;
      this.mIsEnd = false;
      this.mErrorMsg = null;
      notifyMergeItemRangeChanged();
    }

    public void setErrorMsg(String paramString)
    {
      this.mErrorMsg = paramString;
      notifyMergeItemRangeChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeGuessLikeAgent
 * JD-Core Version:    0.6.0
 */