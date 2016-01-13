package com.dianping.main.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dianping.adapter.BasicRecyclerAdapter;
import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.City;
import com.dianping.model.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class VerticalChannelAgent extends HomeAgent
  implements RequestHandler<MApiRequest, MApiResponse>, HomeAgent.OnCellRefreshListener, HomeAgent.OnCellRetryListener
{
  private static final String VERTICAL_CHANNEL_TAG = "43channel.";
  private Adapter adapter;
  ArrayList<DPObject> channelList = new ArrayList();
  private MApiRequest channelRequest;
  private CityConfig cityConfig;
  DPObject dpObject;

  public VerticalChannelAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void cleanCateCache(int paramInt)
  {
    ((DPActivity)getContext()).mapiCacheService().remove(getChannelRequest(paramInt));
  }

  private MApiRequest getChannelRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mindex/indexhotlist.bin").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(paramInt));
    Location localLocation = location();
    double d1;
    double d2;
    if (localLocation != null)
    {
      d1 = localLocation.latitude();
      d2 = localLocation.longitude();
      if ((d1 != 0.0D) || (d2 != 0.0D))
        break label133;
      localBuilder.appendQueryParameter("lng", "0");
      localBuilder.appendQueryParameter("lat", "0");
    }
    while (true)
    {
      if (localLocation.city() != null)
        localBuilder.appendQueryParameter("loccityid", localLocation.city().id() + "");
      return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DAILY);
      label133: localBuilder.appendQueryParameter("lng", Location.FMT.format(d1));
      localBuilder.appendQueryParameter("lat", Location.FMT.format(d2));
    }
  }

  private void stopRequest()
  {
    if (this.channelRequest != null)
    {
      mapiService().abort(this.channelRequest, this, true);
      this.channelRequest = null;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.adapter.notifyMergeItemRangeChanged();
    onRefreshComplete();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    super.onCitySwitched(paramCity1, paramCity2);
    this.channelList.clear();
    stopRequest();
    reqChannel();
    dispatchAgentChanged(false);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.cityConfig = getFragment().cityConfig();
    this.cityConfig.addListener(this);
    this.adapter = new Adapter(null);
    addCell("43channel.", this.adapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    stopRequest();
    this.cityConfig.removeListener(this);
  }

  public void onRefresh()
  {
    this.isRefresh = true;
    cleanCateCache(cityId());
    stopRequest();
    reqChannel();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.channelRequest == paramMApiRequest)
    {
      this.channelRequest = null;
      i = 1;
      paramMApiRequest = (DPObject)DPCache.getInstance().getParcelable(String.valueOf(cityId()) + Environment.versionCode(), "verticalagent", 31539600000L, DPObject.CREATOR);
      if (paramMApiRequest != null)
      {
        int j = 0;
        this.channelList.clear();
        this.dpObject = paramMApiRequest;
        paramMApiRequest = this.dpObject.getArray("IndexHotList");
        i = j;
        if (paramMApiRequest != null)
        {
          i = j;
          if (paramMApiRequest.length > 0)
          {
            this.channelList.addAll(Arrays.asList(paramMApiRequest));
            i = j;
          }
        }
      }
      dispatchAgentChanged(false);
      if (i == 0)
        break label141;
    }
    label141: for (int i = 3; ; i = 2)
    {
      this.isRequestStatus = i;
      sendRefreshShowMessage();
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.channelRequest)
    {
      this.channelRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.channelList.clear();
        this.dpObject = ((DPObject)paramMApiResponse.result());
        paramMApiRequest = this.dpObject.getArray("IndexHotList");
        if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
          this.channelList.addAll(Arrays.asList(paramMApiRequest));
        DPCache.getInstance().put(String.valueOf(cityId()) + Environment.versionCode(), "verticalagent", this.dpObject, 31539600000L);
      }
      dispatchAgentChanged(false);
      this.isRequestStatus = 2;
      sendRefreshShowMessage();
    }
  }

  public void onResume()
  {
    super.onResume();
    reqChannel();
  }

  public void onRetry()
  {
    stopRequest();
    reqChannel();
  }

  void reqChannel()
  {
    if (this.channelRequest != null)
      return;
    this.channelRequest = getChannelRequest(cityId());
    getFragment().mapiService().exec(this.channelRequest, this);
    dispatchAgentChanged("home/HomeRetrySection", null);
    onRefreshRequest();
  }

  public boolean showRetry()
  {
    return true;
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      int k = 0;
      if (VerticalChannelAgent.this.channelList == null);
      for (int i = 0; ; i = VerticalChannelAgent.this.channelList.size())
      {
        int j = k;
        if (i > 2)
        {
          j = k;
          if (i < 8)
            j = 1;
        }
        return j;
      }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      paramViewHolder = (BasicRecyclerAdapter.BasicHolder)paramViewHolder;
      ((VerticalChannelTemplate)paramViewHolder.view).setData(VerticalChannelAgent.this.channelList);
      ((NovaActivity)VerticalChannelAgent.this.getContext()).addGAView(paramViewHolder.view, -1, "home", "home".equals(((NovaActivity)VerticalChannelAgent.this.getContext()).getPageName()));
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(this, new VerticalChannelTemplate(VerticalChannelAgent.this.getContext()));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.VerticalChannelAgent
 * JD-Core Version:    0.6.0
 */