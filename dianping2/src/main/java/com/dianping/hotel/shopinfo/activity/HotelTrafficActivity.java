package com.dianping.hotel.shopinfo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.ExpandableHeightListView;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.map.utils.MapUtils;
import com.dianping.model.Location;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class HotelTrafficActivity extends NovaActivity
  implements TableView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private String cityId;
  private DPObject hotelDetails;
  private ExpandableListAdapter landmarkAdapter;
  private ExpandableHeightListView landmarkList;
  private ArrayList<DPObject> mRouteList = new ArrayList();
  private int mRouteType = 1;
  private MApiRequest routeRequest;
  private String shopId;
  private DPObject shopInfo;
  private ExpandableListAdapter trafficAdapter;
  private ExpandableHeightListView trafficList;

  public void expandGroup(ExpandableListView paramExpandableListView, View paramView, int paramInt)
  {
    if (paramExpandableListView.isGroupExpanded(paramInt))
    {
      paramExpandableListView.collapseGroup(paramInt);
      ((ImageView)paramView.findViewById(R.id.arrow)).setImageResource(R.drawable.ic_arrow_down_black);
      return;
    }
    int i = 0;
    while (i < paramExpandableListView.getChildCount())
    {
      paramExpandableListView.collapseGroup(i);
      i += 1;
    }
    paramExpandableListView.expandGroup(paramInt);
    ((ImageView)paramView.findViewById(R.id.arrow)).setImageResource(R.drawable.ic_arrow_up_black);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    Uri localUri = localIntent.getData();
    if (paramBundle == null)
    {
      this.hotelDetails = ((DPObject)localIntent.getParcelableExtra("hoteldetail"));
      this.cityId = localUri.getQueryParameter("cityId");
      this.shopId = localUri.getQueryParameter("shopid");
    }
    for (this.shopInfo = ((DPObject)localIntent.getParcelableExtra("shopInfo")); ; this.shopInfo = ((DPObject)paramBundle.getParcelable("shopInfo")))
    {
      setupView();
      requestRoute();
      return;
      this.hotelDetails = ((DPObject)paramBundle.getParcelable("hoteldetail"));
      this.cityId = paramBundle.getString("cityid");
      this.shopId = paramBundle.getString("shopid");
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.routeRequest != null)
    {
      mapiService().abort(this.routeRequest, this, true);
      this.routeRequest = null;
    }
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    paramInt = paramView.getId();
    if (paramInt == R.id.route_lay)
      if (this.shopInfo != null)
      {
        MapUtils.gotoNavi(this, this.shopInfo);
        statisticsEvent("shopinfo5", "shopinfo5_info_route", "查看最优路线", 0);
      }
    do
      return;
    while ((paramInt != R.id.more_route) || (this.shopInfo == null));
    MapUtils.gotoNavi(this, this.shopInfo);
    statisticsEvent("shopinfo5", "shopinfo5_info_route", "查看更多路线", 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.routeRequest)
    {
      this.routeRequest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if ((paramMApiRequest != null) && (paramMApiRequest.getArray("Paths") != null))
      {
        this.mRouteList.clear();
        this.mRouteList.addAll(Arrays.asList(paramMApiRequest.getArray("Paths")));
      }
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("hotelDetails", this.hotelDetails);
    paramBundle.putString("cityId", this.cityId);
    paramBundle.putParcelable("shopInfo", this.shopInfo);
  }

  protected void requestRoute()
  {
    if (this.shopInfo == null);
    do
    {
      return;
      localObject = locationService().location();
    }
    while (localObject == null);
    String str = Location.FMT.format(((DPObject)localObject).getDouble("OffsetLat"));
    Object localObject = Location.FMT.format(((DPObject)localObject).getDouble("OffsetLng"));
    this.routeRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/direction.bin?srclat=" + str + "&srclng=" + (String)localObject + "&destlat=" + this.shopInfo.getDouble("Latitude") + "&destlng=" + this.shopInfo.getDouble("Longitude") + "&maptype=" + 1 + "&navitype=" + this.mRouteType, CacheType.NORMAL);
    mapiService().exec(this.routeRequest, this);
  }

  protected void setupView()
  {
    super.setContentView(R.layout.hotel_detail_traffic);
    if (this.hotelDetails == null);
    while (true)
    {
      return;
      ((TableView)findViewById(R.id.hotel_table)).setOnItemClickListener(this);
      super.setTitle("位置交通");
      TextView localTextView1 = (TextView)findViewById(R.id.route_header);
      Object localObject2 = (LinearLayout)findViewById(R.id.route_lay);
      TextView localTextView2 = (TextView)findViewById(R.id.more_route);
      Object localObject1 = this.shopInfo.getString("ExtraJson");
      if (!TextUtils.isEmpty((CharSequence)localObject1));
      try
      {
        localObject1 = new JSONObject((String)localObject1);
        String str = ((JSONObject)localObject1).optString("path");
        if (!TextUtils.isEmpty(((JSONObject)localObject1).optString("pathtype")))
          this.mRouteType = Integer.valueOf(((JSONObject)localObject1).optString("pathtype")).intValue();
        if (!TextUtils.isEmpty(str))
        {
          localTextView1.setVisibility(0);
          ((LinearLayout)localObject2).setVisibility(0);
          localTextView2.setVisibility(0);
          localTextView1 = (TextView)findViewById(R.id.route_view);
          localObject2 = (TextView)findViewById(R.id.route_time);
          localTextView1.setText(str);
          ((TextView)localObject2).setText(((JSONObject)localObject1).optString("pathtime"));
        }
        if ((this.hotelDetails.getArray("TrafficList") != null) && (this.hotelDetails.getArray("TrafficList").length > 0))
        {
          findViewById(R.id.traffic_header).setVisibility(0);
          this.trafficList = ((ExpandableHeightListView)findViewById(R.id.traffic));
          this.trafficList.setVisibility(0);
          this.trafficAdapter = new ExpandableListAdapter(this.hotelDetails.getArray("TrafficList"), true);
          this.trafficList.setAdapter(this.trafficAdapter);
          this.trafficList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
          {
            public boolean onGroupClick(ExpandableListView paramExpandableListView, View paramView, int paramInt, long paramLong)
            {
              HotelTrafficActivity.this.expandGroup(paramExpandableListView, paramView, paramInt);
              paramExpandableListView = new ArrayList();
              paramExpandableListView.add(new BasicNameValuePair("shopId", HotelTrafficActivity.this.shopId));
              HotelTrafficActivity.this.statisticsEvent("hoteldevice5", "hoteldevice5_transport", "", 0, paramExpandableListView);
              return true;
            }
          });
          this.trafficList.setFocusable(false);
        }
        if ((this.hotelDetails.getArray("LandmarkList") != null) && (this.hotelDetails.getArray("LandmarkList").length > 0))
        {
          findViewById(R.id.landmark_header).setVisibility(0);
          this.landmarkList = ((ExpandableHeightListView)findViewById(R.id.landmark));
          this.landmarkList.setVisibility(0);
          this.landmarkAdapter = new ExpandableListAdapter(this.hotelDetails.getArray("LandmarkList"), false);
          this.landmarkList.setAdapter(this.landmarkAdapter);
          this.landmarkList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
          {
            public boolean onGroupClick(ExpandableListView paramExpandableListView, View paramView, int paramInt, long paramLong)
            {
              return true;
            }
          });
          this.landmarkList.setFocusable(false);
        }
        if (this.shopInfo == null)
          continue;
        localObject1 = this.shopInfo.getString("ExtraJson");
        if (localObject1 == null);
      }
      catch (JSONException localJSONException2)
      {
        try
        {
          new JSONObject((String)localObject1);
          localObject1 = "酒店位于" + this.shopInfo.getString("DistrictName") + this.shopInfo.getString("RegionName") + "商区";
          findViewById(R.id.traffic_desc).setVisibility(0);
          findViewById(R.id.tv_traffic_desc).setVisibility(0);
          ((TextView)findViewById(R.id.tv_traffic_desc)).setText((CharSequence)localObject1);
          return;
          localJSONException1 = localJSONException1;
          localJSONException1.printStackTrace();
        }
        catch (JSONException localJSONException2)
        {
          while (true)
            localJSONException2.printStackTrace();
        }
      }
    }
  }

  class ExpandableListAdapter extends BaseExpandableListAdapter
  {
    private boolean expandable = true;
    private DPObject[] items;

    public ExpandableListAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.items = localObject;
    }

    public ExpandableListAdapter(DPObject[] paramBoolean, boolean arg3)
    {
      this.items = paramBoolean;
      boolean bool;
      this.expandable = bool;
    }

    public String getChild(int paramInt1, int paramInt2)
    {
      return this.items[paramInt1].getString("Content");
    }

    public long getChildId(int paramInt1, int paramInt2)
    {
      return paramInt2;
    }

    public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
    {
      if (getChild(paramInt1, paramInt2) != null);
      for (paramViewGroup = getChild(paramInt1, paramInt2); ; paramViewGroup = "暂无相关信息")
      {
        View localView = paramView;
        if (paramView == null)
          localView = LayoutInflater.from(HotelTrafficActivity.this).inflate(R.layout.hotel_list_child, null);
        ((TextView)localView.findViewById(R.id.detail)).setText(paramViewGroup);
        return localView;
      }
    }

    public int getChildrenCount(int paramInt)
    {
      return 1;
    }

    public DPObject getGroup(int paramInt)
    {
      return this.items[paramInt];
    }

    public int getGroupCount()
    {
      return this.items.length;
    }

    public long getGroupId(int paramInt)
    {
      return paramInt;
    }

    public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = getGroup(paramInt);
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = LayoutInflater.from(HotelTrafficActivity.this).inflate(R.layout.hotel_list_group, null);
      if (!this.expandable)
        paramViewGroup.findViewById(R.id.arrow).setVisibility(8);
      ((TextView)paramViewGroup.findViewById(R.id.title)).setText(localDPObject.getString("Title"));
      ((TextView)paramViewGroup.findViewById(R.id.distance)).setText(localDPObject.getString("Distance"));
      return paramViewGroup;
    }

    public boolean hasStableIds()
    {
      return true;
    }

    public boolean isChildSelectable(int paramInt1, int paramInt2)
    {
      return false;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.activity.HotelTrafficActivity
 * JD-Core Version:    0.6.0
 */