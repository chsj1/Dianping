package com.dianping.hotel.shopinfo.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.ExpandableHeightGridView;
import com.dianping.base.widget.ExpandableHeightListView;
import com.dianping.base.widget.ExpandableTextView;
import com.dianping.base.widget.FacilityGridView;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HotelDetailsActivity extends NovaActivity
  implements TableView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final byte HOTEL_DETAIL_DESC = 0;
  private String cityId;
  private int clickCount = 0;
  private ExpandableTextView descText;
  private FacilityGridView facilityGridView;
  private byte hotelDetailStatus = 0;
  private DPObject hotelDetails;
  private ExpandableListAdapter landmarkAdapter;
  private ExpandableHeightListView landmarkList;
  private ArrayList<DPObject> mRouteList = new ArrayList();
  private int mRouteType = 1;
  private LinearLayout phoneView;
  private MApiRequest routeRequest;
  private String shopId;
  private DPObject shopInfo;

  private void requestRoute()
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
      this.hotelDetailStatus = localIntent.getByteExtra("HotelDetailStatus", 0);
    }
    for (this.shopInfo = ((DPObject)localIntent.getParcelableExtra("shopInfo")); ; this.shopInfo = ((DPObject)paramBundle.getParcelable("shopInfo")))
    {
      setupView();
      requestRoute();
      return;
      this.hotelDetails = ((DPObject)paramBundle.getParcelable("hoteldetail"));
      this.cityId = paramBundle.getString("cityid");
      this.shopId = paramBundle.getString("shopid");
      this.hotelDetailStatus = paramBundle.getByte("HotelDetailStatus", 0).byteValue();
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
    if (paramInt == R.id.phone)
    {
      paramTableView = new ArrayList();
      paramTableView.add(new BasicNameValuePair("shopId", this.shopId));
      statisticsEvent("hoteldevice5", "hoteldevice5_tel", "", 0, paramTableView);
      paramTableView = this.hotelDetails.getStringArray("PhoneList");
      if (paramTableView != null)
      {
        if (paramTableView.length != 1)
          break label90;
        TelephoneUtils.dial(this, this.cityId, paramTableView[0]);
      }
    }
    while (true)
    {
      return;
      label90: paramView = new String[paramTableView.length];
      paramInt = 0;
      while (paramInt < paramTableView.length)
      {
        paramView[paramInt] = ("拨打电话: " + paramTableView[paramInt]);
        paramInt += 1;
      }
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      localBuilder.setTitle("联系商户").setItems(paramView, new DialogInterface.OnClickListener(paramTableView)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          TelephoneUtils.dial(HotelDetailsActivity.this, HotelDetailsActivity.this.cityId, this.val$phoneNos[paramInt]);
        }
      });
      localBuilder.create().show();
      return;
      if (paramInt == R.id.hotel_facility)
      {
        this.facilityGridView.expand();
        return;
      }
      if (paramInt == R.id.route_lay)
      {
        if (this.shopInfo == null)
          continue;
        if (this.shopInfo.getBoolean("IsForeignShop"))
          MapUtils.launchMap(this, this.shopInfo);
        while (true)
        {
          statisticsEvent("shopinfo5", "shopinfo5_info_route", "查看最优路线", 0);
          return;
          MapUtils.gotoNavi(this, this.shopInfo);
        }
      }
      if (paramInt != R.id.more_route)
        break;
      if (this.shopInfo == null)
        continue;
      if (this.shopInfo.getBoolean("IsForeignShop"))
        MapUtils.launchMap(this, this.shopInfo);
      while (true)
      {
        statisticsEvent("shopinfo5", "shopinfo5_info_route", "查看更多路线", 0);
        return;
        MapUtils.gotoNavi(this, this.shopInfo);
      }
    }
    if (this.clickCount >= 7)
    {
      this.clickCount = 0;
      paramTableView = Uri.parse("dianping://hotelbookingweb");
      paramView = Uri.parse("http://www.dianping.com/hotel/event/app/eventapp/openApp?type=appee");
      startActivity(new Intent("android.intent.action.VIEW", paramTableView.buildUpon().appendQueryParameter("url", paramView.toString()).build()));
      return;
    }
    this.clickCount += 1;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.routeRequest)
      this.routeRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
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

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("hotelDetails", this.hotelDetails);
    paramBundle.putString("cityId", this.cityId);
    paramBundle.putByte("HotelDetailStatus", this.hotelDetailStatus);
    paramBundle.putParcelable("shopInfo", this.shopInfo);
  }

  protected void setupView()
  {
    int i;
    if (this.hotelDetailStatus == 0)
    {
      i = R.layout.hotel_detail_desc;
      super.setContentView(i);
      if (this.hotelDetails != null)
        break label34;
    }
    label34: Object localObject1;
    Object localObject2;
    do
    {
      do
      {
        return;
        i = R.layout.hotel_detail_traffic;
        break;
        ((TableView)findViewById(R.id.hotel_table)).setOnItemClickListener(this);
        switch (this.hotelDetailStatus)
        {
        default:
          return;
        case 0:
        }
        if ((this.hotelDetails.getArray("FacilityList") != null) && (this.hotelDetails.getArray("FacilityList").length > 0))
        {
          findViewById(R.id.facility_header).setVisibility(0);
          localObject1 = (GridView)findViewById(R.id.facilityGridView);
          ((GridView)localObject1).setAdapter(new FuncilityAdapter(this.hotelDetails.getArray("FacilityList")));
          ((GridView)localObject1).setVisibility(0);
        }
        if (this.hotelDetails.getStringArray("FunFacilityList") != null)
        {
          localObject1 = findViewById(R.id.funfacility_header);
          ((View)localObject1).setVisibility(0);
          ((View)localObject1).setClickable(true);
          localObject1 = (TextView)findViewById(R.id.tv_hotel_fun_facility);
          localObject2 = new StringBuilder();
          String[] arrayOfString = this.hotelDetails.getStringArray("FunFacilityList");
          int j = arrayOfString.length;
          i = 0;
          while (i < j)
          {
            ((StringBuilder)localObject2).append(arrayOfString[i]).append("，");
            i += 1;
          }
          ((StringBuilder)localObject2).deleteCharAt(((StringBuilder)localObject2).length() - 1);
          ((TextView)localObject1).setText(((StringBuilder)localObject2).toString());
          ((TextView)localObject1).setVisibility(0);
        }
        if ((this.hotelDetails.getStringArray("PhoneList") != null) && (this.hotelDetails.getStringArray("PhoneList").length > 0))
        {
          findViewById(R.id.phone_header).setVisibility(0);
          this.phoneView = ((LinearLayout)findViewById(R.id.phone));
          this.phoneView.setVisibility(0);
          this.phoneView.setClickable(true);
          this.phoneView.setPadding(ViewUtils.dip2px(this, 9.0F), this.phoneView.getPaddingTop(), this.phoneView.getPaddingRight(), this.phoneView.getPaddingBottom());
          localObject2 = this.hotelDetails.getStringArray("PhoneList");
          localObject1 = "";
          i = 0;
          if (i < localObject2.length)
          {
            if (i == 0);
            for (localObject1 = (String)localObject1 + localObject2[0]; ; localObject1 = (String)localObject1 + "、" + localObject2[i])
            {
              i += 1;
              break;
            }
          }
          ((ImageView)this.phoneView.findViewById(R.id.img00)).setVisibility(8);
          ((TextView)this.phoneView.findViewById(16908308)).setText((CharSequence)localObject1);
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
        if (this.hotelDetails.getString("Introduce") == null)
          continue;
        findViewById(R.id.desc_header).setVisibility(0);
        this.descText = ((ExpandableTextView)findViewById(R.id.desc));
        localObject1 = (ImageView)findViewById(R.id.desc_arrow);
        ((ImageView)localObject1).setVisibility(0);
        this.descText.expand();
        ((ImageView)localObject1).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            HotelDetailsActivity.this.descText.expand();
          }
        });
        this.descText.setVisibility(0);
        this.descText.setTextClickEvent();
        this.descText.setText(this.hotelDetails.getString("Introduce"));
      }
      while (this.hotelDetails.getObject("Policy") == null);
      findViewById(R.id.policy_header).setVisibility(0);
      findViewById(R.id.policy).setVisibility(0);
      localObject1 = this.hotelDetails.getObject("Policy");
      ((TextView)findViewById(R.id.policy_desc)).setText(((DPObject)localObject1).getString("Content"));
      localObject1 = ((DPObject)localObject1).getStringArray("IconList");
      localObject2 = (ExpandableHeightGridView)findViewById(R.id.policy_icon);
      ((ExpandableHeightGridView)localObject2).setExpanded(true);
    }
    while ((localObject1 == null) || (localObject1.length <= 0));
    ((ExpandableHeightGridView)localObject2).setAdapter(new IconAdapter(localObject1));
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
          localView = LayoutInflater.from(HotelDetailsActivity.this).inflate(R.layout.hotel_list_child, null);
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
        paramViewGroup = LayoutInflater.from(HotelDetailsActivity.this).inflate(R.layout.hotel_list_group, null);
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

  class FuncilityAdapter extends BaseAdapter
  {
    DPObject[] keyFacilityList;

    public FuncilityAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.keyFacilityList = localObject;
    }

    public int getCount()
    {
      return this.keyFacilityList.length;
    }

    public Object getItem(int paramInt)
    {
      return this.keyFacilityList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = LayoutInflater.from(HotelDetailsActivity.this).inflate(R.layout.shopinfo_hotel_facility_item, null);
        paramViewGroup = new HotelDetailsActivity.ViewHolder(HotelDetailsActivity.this);
        paramViewGroup.hotel_info_facility_img = ((NetworkImageView)paramView.findViewById(R.id.hotel_info_facility_img));
        paramViewGroup.hotel_info_facility_text = ((TextView)paramView.findViewById(R.id.hotel_info_facility_text));
        paramView.setTag(paramViewGroup);
      }
      while (true)
      {
        paramViewGroup.hotel_info_facility_img.setImage(this.keyFacilityList[paramInt].getString("Icon"));
        paramViewGroup.hotel_info_facility_text.setText(this.keyFacilityList[paramInt].getString("Title"));
        return paramView;
        paramViewGroup = (HotelDetailsActivity.ViewHolder)paramView.getTag();
      }
    }
  }

  class IconAdapter extends BasicAdapter
  {
    private String[] iconList;

    public IconAdapter(String[] arg2)
    {
      Object localObject;
      this.iconList = localObject;
    }

    public int getCount()
    {
      return this.iconList.length;
    }

    public String getItem(int paramInt)
    {
      return this.iconList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = LayoutInflater.from(HotelDetailsActivity.this).inflate(R.layout.policy_icon, null);
      ((NetworkImageView)paramViewGroup.findViewById(R.id.icon)).setImage(getItem(paramInt));
      return paramViewGroup;
    }
  }

  class ViewHolder
  {
    NetworkImageView hotel_info_facility_img;
    TextView hotel_info_facility_text;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.activity.HotelDetailsActivity
 * JD-Core Version:    0.6.0
 */