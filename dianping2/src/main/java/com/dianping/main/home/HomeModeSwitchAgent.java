package com.dianping.main.home;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;

public class HomeModeSwitchAgent extends HomeAgent
  implements RequestHandler<MApiRequest, MApiResponse>, LocationListener
{
  private static final String MODE_SWITCH_TAG = "05ModeSwitchAgent";
  private static final int QUERY_ACTION = 0;
  private static final int SET_ACTION = 1;
  private static final int STANDARD_MODE = 0;
  private static final int TRIP_MODE = 1;
  private Adapter mAdapter;
  private Location mLocation;
  private int mMode = 0;
  private MApiRequest mModeQueryRequest;
  private MApiRequest mModeSwitchRequest;
  private boolean mShouleShow = false;

  public HomeModeSwitchAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendModeQueryRequest()
  {
    if (this.mModeQueryRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mindex/tripmode.bin").buildUpon();
    localBuilder.appendQueryParameter("action", "0");
    localBuilder.appendQueryParameter("cityid", "" + cityId());
    this.mLocation = location();
    if (this.mLocation != null)
    {
      double d1 = this.mLocation.latitude();
      double d2 = this.mLocation.longitude();
      localBuilder.appendQueryParameter("lng", Location.FMT.format(d2) + "");
      localBuilder.appendQueryParameter("lat", Location.FMT.format(d1) + "");
      if (this.mLocation.city() != null)
        localBuilder.appendQueryParameter("loccityid", String.valueOf(this.mLocation.city().id()));
    }
    this.mModeQueryRequest = getFragment().mapiGet(this, localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mModeQueryRequest, this);
  }

  private void sendModeSwitchRequest()
  {
    if (this.mModeSwitchRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mindex/tripmode.bin").buildUpon();
    localBuilder.appendQueryParameter("action", "1");
    StringBuilder localStringBuilder = new StringBuilder().append("");
    if (this.mMode == 0);
    for (int i = 1; ; i = 0)
    {
      localBuilder.appendQueryParameter("mode", i);
      localBuilder.appendQueryParameter("cityid", "" + cityId());
      this.mModeSwitchRequest = getFragment().mapiGet(this, localBuilder.build().toString(), CacheType.DISABLED);
      getFragment().mapiService().exec(this.mModeSwitchRequest, this);
      return;
    }
  }

  private void stopModeQueryRequest()
  {
    if (this.mModeQueryRequest != null)
    {
      mapiService().abort(this.mModeQueryRequest, this, true);
      this.mModeQueryRequest = null;
    }
  }

  private void stopModeSwitchRequest()
  {
    if (this.mModeSwitchRequest != null)
    {
      mapiService().abort(this.mModeSwitchRequest, this, true);
      this.mModeSwitchRequest = null;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.mAdapter.notifyMergeItemRangeChanged();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    super.onCitySwitched(paramCity1, paramCity2);
    stopModeQueryRequest();
    sendModeQueryRequest();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().cityConfig().addListener(this);
    getFragment().locationService().addListener(this);
    this.mAdapter = new Adapter(null);
    addCell("05ModeSwitchAgent", this.mAdapter);
    sendModeQueryRequest();
  }

  public void onDestroy()
  {
    DPApplication.instance().cityConfig().removeListener(this);
    getFragment().locationService().removeListener(this);
    stopModeSwitchRequest();
    stopModeQueryRequest();
    super.onDestroy();
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    paramLocationService = location();
    if ((paramLocationService == null) || (paramLocationService.city() == null));
    do
      return;
    while ((this.mLocation != null) && (paramLocationService.city().equals(this.mLocation.city())));
    stopModeQueryRequest();
    sendModeQueryRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mModeQueryRequest)
    {
      this.mModeQueryRequest = null;
      this.mShouleShow = false;
      dispatchAgentChanged(false);
    }
    do
      return;
    while (paramMApiRequest != this.mModeSwitchRequest);
    this.mModeSwitchRequest = null;
    showToast("切换失败");
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    int i = 0;
    if (paramMApiRequest == this.mModeQueryRequest)
    {
      this.mModeQueryRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest.getInt("Status") != 0)
          break label75;
        this.mMode = paramMApiRequest.getInt("Mode");
        this.mShouleShow = paramMApiRequest.getBoolean("IsTour");
        dispatchAgentChanged(false);
      }
    }
    while (true)
    {
      return;
      label75: this.mShouleShow = false;
      return;
      if (paramMApiRequest != this.mModeSwitchRequest)
        continue;
      this.mModeSwitchRequest = null;
      if (!(paramMApiResponse.result() instanceof DPObject))
        continue;
      if (((DPObject)paramMApiResponse.result()).getInt("Status") != 0)
        break;
      if (this.mMode == 0)
        i = 1;
      this.mMode = i;
      if (!(getFragment() instanceof MainHomeFragment))
        continue;
      ((MainHomeFragment)getFragment()).onRefresh();
      return;
    }
    showToast("切换失败");
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if (HomeModeSwitchAgent.this.mShouleShow)
        return 1;
      return 0;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      ModelSwitchHolder localModelSwitchHolder = (ModelSwitchHolder)paramViewHolder;
      Object localObject1;
      if (HomeModeSwitchAgent.this.mMode == 0)
      {
        paramViewHolder = "标准版";
        Object localObject2 = localModelSwitchHolder.switchBtn;
        if (HomeModeSwitchAgent.this.mMode != 0)
          break label236;
        localObject1 = "查看旅游版";
        label39: ((Button)localObject2).setText((CharSequence)localObject1);
        localObject2 = HomeModeSwitchAgent.this.getCity().name();
        TextView localTextView = localModelSwitchHolder.msgText;
        StringBuilder localStringBuilder = new StringBuilder().append("您现在在");
        localObject1 = localObject2;
        if (((String)localObject2).length() > 8)
          localObject1 = ((String)localObject2).substring(0, 8) + "...";
        localTextView.setText((String)localObject1 + "-" + paramViewHolder + ", 点击");
        localModelSwitchHolder.switchBtn.setOnClickListener(new View.OnClickListener(localModelSwitchHolder)
        {
          public void onClick(View paramView)
          {
            this.val$modelSwitchHolder.convertView.performClick();
          }
        });
        localModelSwitchHolder.convertView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            HomeModeSwitchAgent.this.sendModeSwitchRequest();
          }
        });
        localObject1 = ((NovaRelativeLayout)localModelSwitchHolder.convertView).gaUserInfo;
        if (HomeModeSwitchAgent.this.mMode != 0)
          break label242;
      }
      label236: label242: for (paramViewHolder = "查看标准版"; ; paramViewHolder = "查看旅游版")
      {
        ((GAUserInfo)localObject1).title = paramViewHolder;
        ((DPActivity)HomeModeSwitchAgent.this.getContext()).addGAView(localModelSwitchHolder.convertView, 0);
        return;
        paramViewHolder = "旅游版";
        break;
        localObject1 = "切回标准版";
        break label39;
      }
    }

    public ModelSwitchHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new ModelSwitchHolder(HomeModeSwitchAgent.this.res.inflate(HomeModeSwitchAgent.this.getContext(), R.layout.home_modeswitch_item, paramViewGroup, false));
    }

    class ModelSwitchHolder extends BasicRecyclerAdapter.BasicHolder
    {
      View convertView;
      TextView msgText;
      Button switchBtn;

      public ModelSwitchHolder(View arg2)
      {
        super(localView);
        this.convertView = localView;
        this.msgText = ((TextView)localView.findViewById(R.id.mode_info));
        this.switchBtn = ((Button)localView.findViewById(R.id.mode_button));
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeModeSwitchAgent
 * JD-Core Version:    0.6.0
 */