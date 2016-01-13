package com.dianping.hotel.shoplist.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hotel.shoplist.adapt.HotelRegionListAdapter;
import com.dianping.hotel.shoplist.data.model.HotelUniRegionModel;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class HotelRegionActivity extends NovaActivity
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int ADDR_RESULT = -1;
  private static final int HOTEL_REGION_LIST_COUNT = 3;
  private static final int METRO_RESULT = 4;
  private static final int PLACE_RESULT = 3;
  private static final int RANGE_RESULT = 1;
  private static final int REGION_RESULT = 2;
  private static final int REQUEST_CITY_SWITCH = 0;
  private static final int REQUEST_HOTEL_POSITION = 1;
  private int DIVIDER_LINE_SIZE;
  private LinearLayout activityContentLayout;
  private int cityId = cityId();
  private LinearLayout listContentLayout;
  private ArrayList<ListView> listViewArray;
  private Location location;
  private int[] originalSelectedIndexPath;
  private HotelUniRegionModel regionData;
  private ArrayList<HotelRegionListAdapter> regionListAdapterArray;
  private DPObject respData;
  private int screenWith;
  private int[] selectedIndexPath;
  private View titleMain;
  private MApiRequest uniregionRequest;

  private void changeRegion(HotelUniRegionModel paramHotelUniRegionModel)
  {
    Object localObject1;
    HashMap localHashMap;
    Object localObject2;
    if (paramHotelUniRegionModel.getSearchParams() != null)
    {
      localObject1 = paramHotelUniRegionModel.getSearchParams().split("&");
      localHashMap = new HashMap();
      int j = localObject1.length;
      int i = 0;
      while (i < j)
      {
        localObject2 = localObject1[i].split("=");
        if (localObject2.length == 2)
          localHashMap.put(localObject2[0], localObject2[1]);
        i += 1;
      }
      localObject1 = new Intent();
      localObject2 = new DPObject().edit();
      ((DPObject.Editor)localObject2).putString("Name", paramHotelUniRegionModel.getName());
      switch (paramHotelUniRegionModel.getType())
      {
      default:
      case 1:
      case 4:
      case 5:
      case 0:
      case 2:
      case 3:
      }
    }
    while (true)
    {
      ((Intent)localObject1).putExtra("HOTEL_REGION_SelectedIndexPath", this.selectedIndexPath);
      ((Intent)localObject1).putExtra("result", ((DPObject.Editor)localObject2).generate());
      setResult(-1, (Intent)localObject1);
      finish();
      return;
      ((Intent)localObject1).putExtra("type", 1);
      ((DPObject.Editor)localObject2).putString("ID", (String)localHashMap.get("range"));
      continue;
      ((Intent)localObject1).putExtra("type", 4);
      ((DPObject.Editor)localObject2).putInt("ID", paramHotelUniRegionModel.getUid());
      double d1 = Double.parseDouble((String)localHashMap.get("mylat"));
      double d2 = Double.parseDouble((String)localHashMap.get("mylng"));
      ((DPObject.Editor)localObject2).putDouble("Lat", d1);
      ((DPObject.Editor)localObject2).putDouble("Lng", d2);
      continue;
      ((Intent)localObject1).putExtra("type", 3);
      ((Intent)localObject1).putExtra("address", paramHotelUniRegionModel.getName());
      d1 = Double.parseDouble((String)localHashMap.get("mylat"));
      d2 = Double.parseDouble((String)localHashMap.get("mylng"));
      ((Intent)localObject1).putExtra("lat", d1);
      ((Intent)localObject1).putExtra("lng", d2);
      continue;
      ((Intent)localObject1).putExtra("type", 2);
      ((DPObject.Editor)localObject2).putInt("ID", paramHotelUniRegionModel.getUid());
      ((DPObject.Editor)localObject2).putInt("ParentID", paramHotelUniRegionModel.getParentId());
    }
  }

  private void changeSelectIndex(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((this.originalSelectedIndexPath == null) || (this.selectedIndexPath == null) || (this.originalSelectedIndexPath.length != this.selectedIndexPath.length) || (paramInt1 >= this.originalSelectedIndexPath.length))
      return;
    Object localObject1 = (HotelRegionListAdapter)this.regionListAdapterArray.get(paramInt1);
    int i;
    if (paramBoolean)
    {
      ((HotelRegionListAdapter)localObject1).setRegionData(this.regionData);
      this.selectedIndexPath[paramInt1] = paramInt2;
      boolean bool = true;
      paramInt2 = paramInt1 + 1;
      i = 0;
      label80: paramBoolean = bool;
      if (i <= paramInt1)
      {
        if (this.originalSelectedIndexPath[i] == this.selectedIndexPath[i])
          break label158;
        paramBoolean = false;
      }
      i = paramInt2;
      label111: if (i >= this.originalSelectedIndexPath.length)
        break label178;
      if (!paramBoolean)
        break label167;
      this.selectedIndexPath[i] = this.originalSelectedIndexPath[i];
    }
    while (true)
    {
      i += 1;
      break label111;
      if (((HotelRegionListAdapter)localObject1).getSelectedPosition() != paramInt2)
        break;
      return;
      label158: i += 1;
      break label80;
      label167: this.selectedIndexPath[i] = 0;
    }
    label178: Object localObject2 = this.regionListAdapterArray.iterator();
    while (((Iterator)localObject2).hasNext())
      ((HotelRegionListAdapter)((Iterator)localObject2).next()).setPressInfo(paramInt1, paramBoolean);
    ((HotelRegionListAdapter)localObject1).setSelectedPosition(this.selectedIndexPath[paramInt1], true);
    for (localObject2 = ((HotelRegionListAdapter)localObject1).getRegionData(); paramInt2 < this.regionListAdapterArray.size(); localObject2 = localObject1)
    {
      localObject1 = localObject2;
      if (localObject2 != null)
      {
        localObject1 = localObject2;
        if (((HotelUniRegionModel)localObject2).getSubNaviItemList() != null)
        {
          localObject1 = localObject2;
          if (!((HotelUniRegionModel)localObject2).getSubNaviItemList().isEmpty())
          {
            localObject1 = localObject2;
            if (this.selectedIndexPath[(paramInt2 - 1)] < ((HotelUniRegionModel)localObject2).getSubNaviItemList().size())
              localObject1 = (HotelUniRegionModel)((HotelUniRegionModel)localObject2).getSubNaviItemList().get(this.selectedIndexPath[(paramInt2 - 1)]);
          }
        }
      }
      localObject2 = (HotelRegionListAdapter)this.regionListAdapterArray.get(paramInt2);
      ((HotelRegionListAdapter)localObject2).setRegionData((HotelUniRegionModel)localObject1);
      ((HotelRegionListAdapter)localObject2).setSelectedPosition(this.selectedIndexPath[paramInt2], true);
      paramInt2 += 1;
    }
    layoutListView();
  }

  private void createListViews()
  {
    int j = this.screenWith / 4;
    int i = 0;
    if (i < 3)
    {
      ListView localListView = new ListView(this);
      localListView.setVerticalScrollBarEnabled(false);
      localListView.setOnItemClickListener(this);
      HotelRegionListAdapter localHotelRegionListAdapter = new HotelRegionListAdapter(this, i);
      localListView.setAdapter(localHotelRegionListAdapter);
      localListView.setDividerHeight(this.DIVIDER_LINE_SIZE);
      localListView.setVisibility(8);
      localListView.setDivider(new ColorDrawable(0));
      if (i == 0)
      {
        localListView.setLayoutParams(new LinearLayout.LayoutParams(j, -1));
        localListView.setBackgroundColor(0);
      }
      while (true)
      {
        this.listViewArray.add(localListView);
        this.regionListAdapterArray.add(localHotelRegionListAdapter);
        this.listContentLayout.addView(localListView);
        i += 1;
        break;
        if ((i > 0) && (i < 2))
        {
          localLayoutParams = new LinearLayout.LayoutParams(j, -1);
          localLayoutParams.weight = 1.0F;
          localListView.setLayoutParams(localLayoutParams);
          localListView.setBackgroundResource(R.drawable.hotel_region_right_border);
          continue;
        }
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(j * 2, -1);
        localLayoutParams.weight = 2.0F;
        localListView.setLayoutParams(localLayoutParams);
      }
    }
  }

  private void doGA(String paramString, View paramView, HotelUniRegionModel paramHotelUniRegionModel)
  {
    statisticsEvent("area5", "area5_hotarea", paramHotelUniRegionModel.getName(), 0);
    GAUserInfo localGAUserInfo = new GAUserInfo();
    localGAUserInfo.region_id = Integer.valueOf(paramHotelUniRegionModel.getUid());
    GAHelper.instance().contextStatisticsEvent(paramView.getContext(), paramString, localGAUserInfo, "tap");
  }

  private LinearLayout getSearchBar()
  {
    LinearLayout localLinearLayout = new LinearLayout(this);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(this, 45.0F)));
    localLinearLayout.setPadding(ViewUtils.dip2px(this, 15.0F), ViewUtils.dip2px(this, 7.0F), ViewUtils.dip2px(this, 15.0F), ViewUtils.dip2px(this, 7.0F));
    localLinearLayout.setBackgroundColor(Color.parseColor("#F0F0F0"));
    localLinearLayout.setGravity(17);
    return localLinearLayout;
  }

  private TextView getSearchBarText()
  {
    TextView localTextView = new TextView(this);
    localTextView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    float f = ViewUtils.dip2px(this, 16.0F);
    ShapeDrawable localShapeDrawable = new ShapeDrawable(new RoundRectShape(new float[] { f, f, f, f, f, f, f, f }, null, null));
    localShapeDrawable.getPaint().setColor(-1);
    localShapeDrawable.getPaint().setStyle(Paint.Style.FILL);
    localTextView.setBackgroundDrawable(localShapeDrawable);
    localTextView.setText("输入地点查找酒店");
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_13));
    localTextView.setGravity(17);
    localTextView.setTextColor(Color.parseColor("#CDCDCD"));
    localTextView.setOnClickListener(new View.OnClickListener()
    {
      private String[] initSearchCenter()
      {
        String[] arrayOfString = new String[2];
        Object localObject1 = HotelRegionActivity.this.getSharedPreferences(HotelRegionActivity.this.getPackageName(), 0).getString("findconditions_region", null);
        if (localObject1 != null)
        {
          localObject1 = ((String)localObject1).split("IAMSPLIT");
          if (localObject1.length == 4)
          {
            arrayOfString[0] = localObject1[0];
            arrayOfString[1] = localObject1[1];
            return arrayOfString;
          }
        }
        Object localObject3 = null;
        localObject1 = localObject3;
        if (HotelRegionActivity.this.locationService().hasLocation());
        try
        {
          localObject1 = (Location)HotelRegionActivity.this.locationService().location().decodeToObject(Location.DECODER);
          if ((localObject1 == null) || (((Location)localObject1).city().id() != HotelRegionActivity.this.cityId()))
          {
            arrayOfString[0] = String.valueOf(HotelRegionActivity.this.city().latitude());
            arrayOfString[1] = String.valueOf(HotelRegionActivity.this.city().longitude());
            return arrayOfString;
          }
        }
        catch (ArchiveException localObject2)
        {
          Object localObject2;
          while (true)
          {
            localArchiveException.printStackTrace();
            localObject2 = localObject3;
          }
          arrayOfString[0] = String.valueOf(localObject2.latitude());
          arrayOfString[1] = String.valueOf(localObject2.longitude());
        }
        return (String)arrayOfString;
      }

      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://customlocationlist"));
        paramView.putExtra("hint", "输入地点找酒店");
        HotelRegionActivity.this.startActivityForResult(paramView, 1);
        HotelRegionActivity.this.statisticsEvent("area5", "area5_keyword_click", "", 0);
      }
    });
    return localTextView;
  }

  private void layoutListView()
  {
    int j = 0;
    int i = 0;
    if (i < this.listViewArray.size())
    {
      localListView = (ListView)this.listViewArray.get(i);
      if (((HotelRegionListAdapter)this.regionListAdapterArray.get(i)).getCount() == 0)
        localListView.setVisibility(8);
      while (true)
      {
        i += 1;
        break;
        int k = i;
        localListView.setVisibility(0);
        localListView.setDivider(new ColorDrawable(0));
        localListView.setDividerHeight(this.DIVIDER_LINE_SIZE);
        localListView.scrollTo(0, 0);
        localListView.smoothScrollToPosition(((HotelRegionListAdapter)localListView.getAdapter()).getSelectedPosition());
        j = k;
        if (i <= 0)
          continue;
        localListView.setBackgroundResource(R.drawable.hotel_region_right_border);
        j = k;
      }
    }
    ListView localListView = (ListView)this.listViewArray.get(j);
    localListView.setDividerHeight(this.DIVIDER_LINE_SIZE);
    localListView.setBackgroundColor(-1);
  }

  private void sendUniRegionRequest()
  {
    this.location = location();
    if (this.location != null)
    {
      this.uniregionRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotelsearch/uniregion.hotel?cityid=" + cityId() + "&locatecityid=" + this.location.city().id(), CacheType.DISABLED);
      mapiService().exec(this.uniregionRequest, this);
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 3);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 == -1)
    {
      if ((paramInt1 == 0) && (this.cityId != cityId()))
      {
        this.cityId = cityId();
        super.setTitle(city().name());
        this.originalSelectedIndexPath = new int[] { 0, 0, 0 };
        this.selectedIndexPath = Arrays.copyOf(this.originalSelectedIndexPath, this.originalSelectedIndexPath.length);
        sendUniRegionRequest();
        statisticsEvent("area5", "area5_city", this.cityId + "", 0);
      }
      if (paramInt1 == 1)
      {
        Intent localIntent = new Intent();
        paramIntent = paramIntent.getExtras();
        if (paramIntent != null)
        {
          localIntent.putExtra("result", new DPObject().edit().generate());
          localIntent.putExtra("type", paramIntent.getInt("type", -1));
          localIntent.putExtra("address", paramIntent.getString("address"));
          localIntent.putExtra("lat", paramIntent.getDouble("lat"));
          localIntent.putExtra("lng", paramIntent.getDouble("lng"));
        }
        setResult(-1, localIntent);
        finish();
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle(city().name());
    this.titleMain = findViewById(R.id.title_main);
    this.titleMain.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://switchcity"));
        HotelRegionActivity.this.startActivityForResult(paramView, 0);
        HotelRegionActivity.this.statisticsEvent("area5", "area5_city_click", "", 0);
      }
    });
    this.activityContentLayout = new LinearLayout(this);
    this.activityContentLayout.setOrientation(1);
    super.setContentView(this.activityContentLayout);
    this.DIVIDER_LINE_SIZE = ViewUtils.dip2px(this, 1.0F);
    paramBundle = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(paramBundle);
    this.screenWith = paramBundle.widthPixels;
    paramBundle = getSearchBar();
    paramBundle.addView(getSearchBarText());
    this.activityContentLayout.addView(paramBundle);
    paramBundle = new LinearLayout(this);
    paramBundle.setLayoutParams(new LinearLayout.LayoutParams(-1, this.DIVIDER_LINE_SIZE));
    paramBundle.setBackgroundColor(getResources().getColor(R.color.inner_divider));
    this.activityContentLayout.addView(paramBundle);
    this.listContentLayout = new LinearLayout(this);
    this.listContentLayout.setOrientation(0);
    this.listContentLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    this.activityContentLayout.addView(this.listContentLayout);
    this.originalSelectedIndexPath = getIntent().getIntArrayExtra("HOTEL_REGION_SelectedIndexPath");
    if (this.originalSelectedIndexPath == null)
      this.originalSelectedIndexPath = new int[] { 0, 0, 0 };
    this.selectedIndexPath = Arrays.copyOf(this.originalSelectedIndexPath, this.originalSelectedIndexPath.length);
    this.listViewArray = new ArrayList();
    this.regionListAdapterArray = new ArrayList();
    createListViews();
    sendUniRegionRequest();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    ListView localListView = (ListView)paramAdapterView;
    int i = this.listViewArray.indexOf(paramAdapterView);
    changeSelectIndex(i, paramInt, false);
    paramAdapterView = (HotelUniRegionModel)((HotelRegionListAdapter)localListView.getAdapter()).getItem(paramInt);
    if (i == 0)
      doGA("hotellist_area_left", paramView, paramAdapterView);
    do
      return;
    while (paramAdapterView.getSearchParams() == null);
    doGA("hotellist_area_right", paramView, paramAdapterView);
    changeRegion(paramAdapterView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.uniregionRequest)
      this.uniregionRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.uniregionRequest)
    {
      this.uniregionRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.respData = ((DPObject)paramMApiResponse.result());
        this.regionData = HotelUniRegionModel.parse(this.respData);
        reloadData();
      }
    }
  }

  public void reloadData()
  {
    changeSelectIndex(0, this.originalSelectedIndexPath[0], true);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.activity.HotelRegionActivity
 * JD-Core Version:    0.6.0
 */