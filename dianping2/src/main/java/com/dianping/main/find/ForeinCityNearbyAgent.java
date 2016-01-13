package com.dianping.main.find;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class ForeinCityNearbyAgent extends AdapterCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, CityConfig.SwitchListener
{
  public static final String FIND_FOREINCITY_NEARBY_LIST_TAG = "42FindForeinCityNearBy.";
  private static final int LIMIT = 10;
  private static final int MAX_RETRY_COUNT = 3;
  private Adapter adapter;
  DPObject error;
  private DPObject exploreDPObject;
  private DPObject[] exploreListDPObject;
  private ForeinCityNearbyContainer foreinCityNearbyContainer;
  private MApiRequest mApiRequest;
  private int mRetryCount = 0;
  private MApiService mapiService;
  final LoadingErrorView.LoadRetry retryListener = new LoadingErrorView.LoadRetry()
  {
    public void loadRetry(View paramView)
    {
      ForeinCityNearbyAgent.this.error = null;
      ForeinCityNearbyAgent.this.sendRequest();
      ForeinCityNearbyAgent.this.dispatchAgentChanged(false);
    }
  };
  private int startIndex;

  public ForeinCityNearbyAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentHeaderCell()
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.find_forein_city_header, null, false);
    TextView localTextView = (TextView)localView.findViewById(R.id.distance_overview);
    ImageView localImageView = (ImageView)localView.findViewById(R.id.distance_icon);
    int i;
    if ((this.exploreListDPObject != null) && (this.exploreListDPObject.length > 0) && (this.exploreListDPObject.length < 0))
    {
      i = this.exploreListDPObject[0].getInt("Distance");
      if (i >= 100)
        break label108;
      localTextView.setText("<100m");
      localImageView.setImageResource(R.drawable.find_walking);
    }
    while (true)
    {
      localView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          ForeinCityNearbyAgent.this.statisticsEvent("overseas", "oversea_findnearmore_click", "顶部", 0);
          ForeinCityNearbyAgent.this.startActivity("dianping://foreincitynearby");
        }
      });
      return localView;
      label108: if (i < 500)
      {
        localTextView.setText("<500m");
        localImageView.setImageResource(R.drawable.find_walking);
        continue;
      }
      if (i < 1000)
      {
        localTextView.setText("<1km");
        localImageView.setImageResource(R.drawable.find_walking);
        continue;
      }
      if (i < 2000)
      {
        localTextView.setText("<2km");
        localImageView.setImageResource(R.drawable.find_walking);
        continue;
      }
      if (i < 3000)
      {
        localTextView.setText("<3km");
        localImageView.setImageResource(R.drawable.find_bus);
        continue;
      }
      if (i < 5000)
      {
        localTextView.setText("<5km");
        localImageView.setImageResource(R.drawable.find_bus);
        continue;
      }
      if (i < 10000)
      {
        localTextView.setText("<10km");
        localImageView.setImageResource(R.drawable.find_bus);
        continue;
      }
      if (i < 20000)
      {
        localTextView.setText("<20km");
        localImageView.setImageResource(R.drawable.find_train);
        continue;
      }
      if (i < 100000)
      {
        localTextView.setText("<100km");
        localImageView.setImageResource(R.drawable.find_train);
        continue;
      }
      if (i < 500000)
      {
        localTextView.setText("<500km");
        localImageView.setImageResource(R.drawable.find_train);
        continue;
      }
      localTextView.setText(">500km");
      localImageView.setImageResource(R.drawable.find_airplane);
    }
  }

  private View createContentHeaderDivider()
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.horizontal_divider_nopadding, null);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 10.0F);
    localView.setLayoutParams(localLayoutParams);
    return localView;
  }

  private View createFooterView()
  {
    Button localButton = new Button(getContext());
    localButton.setBackgroundResource(R.drawable.list_item);
    localButton.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 50.0F)));
    localButton.setTextColor(-13421773);
    localButton.setText("发现更多");
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ForeinCityNearbyAgent.this.statisticsEvent("overseas", "oversea_findnearmore_click", "底部", 0);
        ForeinCityNearbyAgent.this.startActivity("dianping://foreincitynearby");
      }
    });
    return localButton;
  }

  private void sendRequest()
  {
    if ((this.mApiRequest != null) || (location() == null))
      return;
    this.mApiRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/explore.overseas?cityid=" + cityId() + "&lat=" + location().latitude() + "&lng=" + location().longitude() + "&start=" + this.startIndex + "&limit=" + 10, CacheType.DISABLED);
    this.mapiService.exec(this.mApiRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.adapter != null)
    {
      this.adapter.setItems(this.exploreListDPObject);
      this.adapter.notifyDataSetChanged();
    }
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    if (paramCity2.id() != paramCity1.id())
    {
      this.exploreDPObject = null;
      this.exploreListDPObject = null;
      this.error = null;
      if (getCity().isForeignCity())
        sendRequest();
      dispatchAgentChanged(false);
    }
  }

  public void onClick(View paramView)
  {
    if (paramView != null)
    {
      paramView = (DPObject)paramView.getTag();
      if (paramView != null)
      {
        String str = paramView.getString("Url");
        if ((str != null) && (!TextUtils.isEmpty(str)))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(str));
          statisticsEvent("overseas", "oversea_findnear_click", str, paramView.getInt("position"));
          startActivity(localIntent);
        }
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getFragment().cityConfig().addListener(this);
    this.mapiService = getFragment().mapiService();
    if (paramBundle != null)
    {
      this.exploreDPObject = ((DPObject)paramBundle.getParcelable("ExploreDPObject"));
      if (this.exploreDPObject != null)
      {
        this.exploreListDPObject = this.exploreDPObject.getArray("List");
        dispatchAgentChanged(false);
      }
    }
    if (getCity().isForeignCity())
      sendRequest();
    this.adapter = new Adapter(null);
    addCell("42FindForeinCityNearBy.", this.adapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    getFragment().cityConfig().removeListener(this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mApiRequest == paramMApiRequest)
    {
      this.mRetryCount += 1;
      if (!(paramMApiResponse.error() instanceof DPObject))
        break label62;
    }
    label62: for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      this.mApiRequest = null;
      if (this.mRetryCount < 3)
        break;
      dispatchAgentChanged(false);
      return;
    }
    this.error = null;
    sendRequest();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.mApiRequest == paramMApiRequest))
    {
      this.mApiRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.exploreDPObject = ((DPObject)paramMApiResponse.result());
        this.startIndex = this.exploreDPObject.getInt("StartIndex");
        this.exploreListDPObject = this.exploreDPObject.getArray("List");
        dispatchAgentChanged(false);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("ExploreDPObject", this.exploreDPObject);
    return localBundle;
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private DPObject[] dpObj;

    private Adapter()
    {
      super();
    }

    private void setItems(DPObject[] paramArrayOfDPObject)
    {
      this.dpObj = paramArrayOfDPObject;
    }

    public int getCount()
    {
      int j = 0;
      if (ForeinCityNearbyAgent.this.getFragment() == null);
      for (boolean bool = false; ; bool = ForeinCityNearbyAgent.this.getFragment().city().isForeignCity())
      {
        int i = j;
        if (bool)
          if (((ForeinCityNearbyAgent.this.exploreListDPObject != null) && (ForeinCityNearbyAgent.this.exploreListDPObject.length != 0)) || (ForeinCityNearbyAgent.this.error == null))
          {
            i = j;
            if (ForeinCityNearbyAgent.this.exploreListDPObject != null)
            {
              i = j;
              if (ForeinCityNearbyAgent.this.exploreListDPObject.length <= 0);
            }
          }
          else
          {
            i = 1;
          }
        return i;
      }
    }

    public Object getItem(int paramInt)
    {
      return this.dpObj;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      LinearLayout localLinearLayout = new LinearLayout(ForeinCityNearbyAgent.this.getContext());
      localLinearLayout.setBackgroundResource(R.color.white);
      localLinearLayout.setOrientation(1);
      localLinearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
      View localView = new View(ForeinCityNearbyAgent.this.getContext());
      localView.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(ForeinCityNearbyAgent.this.getContext(), 15.0F)));
      localView.setBackgroundResource(R.drawable.home_cell_divider);
      localLinearLayout.addView(localView);
      if (this.dpObj != null)
      {
        localLinearLayout.addView(ForeinCityNearbyAgent.this.createContentHeaderCell());
        ForeinCityNearbyAgent.access$402(ForeinCityNearbyAgent.this, new ForeinCityNearbyContainer(ForeinCityNearbyAgent.this.getContext()));
        ForeinCityNearbyAgent.this.foreinCityNearbyContainer.setData(this.dpObj, ForeinCityNearbyAgent.this);
        localLinearLayout.addView(ForeinCityNearbyAgent.this.foreinCityNearbyContainer);
        localLinearLayout.addView(ForeinCityNearbyAgent.this.createContentHeaderDivider());
        localLinearLayout.addView(ForeinCityNearbyAgent.this.createFooterView());
      }
      do
        return localLinearLayout;
      while ((ForeinCityNearbyAgent.this.error == null) || ((this.dpObj != null) && (this.dpObj.length != 0)) || (ForeinCityNearbyAgent.this.error == null));
      localLinearLayout.addView(getFailedView("网络连接失败 点击重新加载", ForeinCityNearbyAgent.this.retryListener, paramViewGroup, paramView, -1));
      return localLinearLayout;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.ForeinCityNearbyAgent
 * JD-Core Version:    0.6.0
 */