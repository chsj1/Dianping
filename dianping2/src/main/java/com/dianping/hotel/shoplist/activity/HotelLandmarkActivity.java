package com.dianping.hotel.shoplist.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hotel.shoplist.fragement.HotelAllRegionFragment;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.DPBasicItem;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelLandmarkActivity extends NovaActivity
  implements View.OnClickListener, TableView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int COLUMN_COUNT = 3;
  private static final int METRO_RESULT = 4;
  private static final int PLACE_RESULT = 3;
  private static final int RANGE_RESULT = 1;
  private static final int REGION_RESULT = 2;
  private static final int REQUEST_CITY_SWITCH = 0;
  private static final int REQUEST_HOTEL_METRO = 2;
  private static final int REQUEST_HOTEL_POSITION = 1;
  private static final int REQUEST_MORE_REGION = 3;
  private static final String TAG = HotelLandmarkActivity.class.getSimpleName();
  private TextView allRegion;
  HotelAllRegionFragment allRegionFragment;
  private DPObject[] allRegionList;
  private MApiRequest allRegionRequest;
  private MApiRequest aroundcityRegionRecomRequest;
  public int cityId = cityId();
  private DPObject curRange;
  private ImageView fiveHundred;
  private ImageView fiveKilometer;
  private FrameLayout fragmentContainer;
  FragmentManager fragmentManager;
  FragmentTransaction fragmentTransaction;
  private DPObject[] hotRegionList;
  private int hotRegionListEnd;
  private int hotRegionListLength;
  private MApiRequest hotRegionRequest;
  private DPObject hotRegionResult;
  private LinearLayout hotelRegionContainer;
  private Drawable imgPosition;
  private Drawable imgRailway;
  private boolean isMetroCity;
  private View.OnClickListener listener;
  private Location location;
  private TextView nearbyme;
  private ImageView oneKilometer;
  private DPBasicItem positionSearch;
  private TextView positionSearchItemTitle;
  private DPBasicItem railway;
  private TextView railwayItemTitle;
  private LocalListener rangeListener = new LocalListener(null);
  private ArrayList<DPObject> rangeNavs;
  private FrameLayout range_layout;
  private boolean searchAroundCities;
  private TextView searchbyregion;
  private View titleMain;
  private ImageView twoKilometer;
  private String url;

  private TextView createRecommendItem(DPObject paramDPObject)
  {
    TextView localTextView = createTextView();
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -2);
    localLayoutParams.weight = 1.0F;
    localLayoutParams.leftMargin = ViewUtils.dip2px(this, 4.0F);
    localLayoutParams.rightMargin = ViewUtils.dip2px(this, 4.0F);
    localLayoutParams.topMargin = ViewUtils.dip2px(this, 4.0F);
    localLayoutParams.bottomMargin = ViewUtils.dip2px(this, 4.0F);
    localLayoutParams.gravity = 17;
    localTextView.setLayoutParams(localLayoutParams);
    localTextView.setText(paramDPObject.getString("Name"));
    if (paramDPObject.getBoolean("Highlight"))
      localTextView.setTextColor(getResources().getColor(R.color.orange_red));
    while (true)
    {
      localTextView.setTag(paramDPObject);
      localTextView.setOnClickListener(this.listener);
      return localTextView;
      localTextView.setTextColor(getResources().getColor(R.color.deep_gray));
    }
  }

  private LinearLayout createRecommendRow(List<DPObject> paramList)
  {
    LinearLayout localLinearLayout = new LinearLayout(this);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setOrientation(0);
    localLinearLayout.setBackgroundResource(R.color.transparent);
    int i = 0;
    while (i < paramList.size())
    {
      localLinearLayout.addView(createRecommendItem((DPObject)paramList.get(i)));
      i += 1;
    }
    return localLinearLayout;
  }

  private TextView createTextView()
  {
    TextView localTextView = new TextView(this);
    localTextView.setClickable(true);
    localTextView.setEllipsize(TextUtils.TruncateAt.END);
    localTextView.setSingleLine(true);
    localTextView.setPadding(0, ViewUtils.dip2px(this, 12.0F), 0, ViewUtils.dip2px(this, 12.0F));
    localTextView.setBackgroundResource(R.drawable.category_item_background);
    localTextView.setGravity(17);
    localTextView.setTextSize(0, getResources().getDimension(R.dimen.text_medium_1));
    return localTextView;
  }

  private void sendAllRegionRequest()
  {
    this.allRegionRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotel/regionrecom.hotel?cityid=" + cityId(), CacheType.DISABLED);
    mapiService().exec(this.allRegionRequest, this);
  }

  private void sendAroundcityRegionRecomRequest()
  {
    this.aroundcityRegionRecomRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotelsearch/aroundcityregionrecom.hotel?cityid=" + cityId(), CacheType.DISABLED);
    mapiService().exec(this.aroundcityRegionRecomRequest, this);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 3);
  }

  public void isShowLocal()
  {
    if (this.searchAroundCities)
    {
      this.nearbyme.setVisibility(8);
      this.range_layout.setVisibility(8);
      return;
    }
    this.location = location();
    if ((this.location != null) && (this.location.city() != null) && (this.location.city().id() == cityId()))
    {
      if (this.curRange != null)
      {
        if (!((DPObject)this.fiveHundred.getTag()).getString("ID").equals(this.curRange.getString("ID")))
          break label132;
        this.fiveHundred.setSelected(true);
      }
      while (true)
      {
        this.nearbyme.setVisibility(0);
        this.range_layout.setVisibility(0);
        return;
        label132: if (((DPObject)this.oneKilometer.getTag()).getString("ID").equals(this.curRange.getString("ID")))
        {
          this.oneKilometer.setSelected(true);
          continue;
        }
        if (((DPObject)this.twoKilometer.getTag()).getString("ID").equals(this.curRange.getString("ID")))
        {
          this.twoKilometer.setSelected(true);
          continue;
        }
        if (!((DPObject)this.fiveKilometer.getTag()).getString("ID").equals(this.curRange.getString("ID")))
          continue;
        this.fiveKilometer.setSelected(true);
      }
    }
    this.nearbyme.setVisibility(8);
    this.range_layout.setVisibility(8);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Log.d(TAG, "requestCode = " + paramInt1);
    if (paramInt2 == -1)
    {
      if (paramInt1 != 0)
        break label160;
      if (this.cityId != cityId())
      {
        this.cityId = cityId();
        super.setTitle(city().name());
        this.nearbyme.setVisibility(8);
        this.range_layout.setVisibility(8);
        this.hotelRegionContainer.setVisibility(8);
        this.searchbyregion.setVisibility(8);
        this.railway.setVisibility(8);
        sendHotRegionRequest();
        sendAllRegionRequest();
        statisticsEvent("area5", "area5_city", this.cityId + "", 0);
      }
    }
    label160: 
    do
    {
      return;
      if (paramInt1 == 1)
      {
        paramIntent.putExtra("type", 3);
        setResult(-1, paramIntent);
        finish();
        return;
      }
      if (paramInt1 != 3)
        continue;
      paramIntent.putExtra("type", 2);
      setResult(-1, paramIntent);
      finish();
      return;
    }
    while (paramInt1 != 2);
    paramIntent.putExtra("type", 4);
    setResult(-1, paramIntent);
    finish();
  }

  public void onClick(View paramView)
  {
    DPObject localDPObject = (DPObject)paramView.getTag();
    Object localObject = new Intent();
    ((Intent)localObject).putExtra("result", localDPObject);
    ((Intent)localObject).putExtra("type", 2);
    setResult(-1, (Intent)localObject);
    statisticsEvent("area5", "area5_hotarea", localDPObject.getString("Name"), 0);
    localObject = new GAUserInfo();
    ((GAUserInfo)localObject).region_id = Integer.valueOf(localDPObject.getInt("ID"));
    GAHelper.instance().contextStatisticsEvent(paramView.getContext(), "hotellist_area", (GAUserInfo)localObject, "tap");
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.hotel_landmark_layout);
    super.setTitle(city().name());
    this.titleMain = findViewById(R.id.title_main);
    this.rangeNavs = getIntent().getParcelableArrayListExtra("rangeNavs");
    this.curRange = ((DPObject)getIntent().getParcelableExtra("curRange"));
    this.nearbyme = ((TextView)findViewById(R.id.nearbyme));
    this.searchbyregion = ((TextView)findViewById(R.id.searchbyregion));
    this.range_layout = ((FrameLayout)findViewById(R.id.range_layout));
    this.hotelRegionContainer = ((LinearLayout)findViewById(R.id.recommend_hotelRegion_container));
    this.fiveHundred = ((ImageView)findViewById(R.id.fiveHundred));
    this.oneKilometer = ((ImageView)findViewById(R.id.oneKilo));
    this.twoKilometer = ((ImageView)findViewById(R.id.twoKilo));
    this.fiveKilometer = ((ImageView)findViewById(R.id.fiveKilo));
    this.fiveHundred.setTag(this.rangeNavs.get(0));
    this.oneKilometer.setTag(this.rangeNavs.get(1));
    this.twoKilometer.setTag(this.rangeNavs.get(2));
    this.fiveKilometer.setTag(this.rangeNavs.get(3));
    this.fiveHundred.setOnClickListener(this.rangeListener);
    this.oneKilometer.setOnClickListener(this.rangeListener);
    this.twoKilometer.setOnClickListener(this.rangeListener);
    this.fiveKilometer.setOnClickListener(this.rangeListener);
    this.allRegion = ((TextView)findViewById(R.id.allregion));
    this.fragmentContainer = ((FrameLayout)findViewById(R.id.fragment_container));
    isShowLocal();
    this.positionSearch = ((DPBasicItem)findViewById(R.id.positionSearch));
    this.railway = ((DPBasicItem)findViewById(R.id.railway));
    this.positionSearchItemTitle = this.positionSearch.getItemTitle();
    this.positionSearchItemTitle.setCompoundDrawablePadding(ViewUtils.dip2px(this, 13.0F));
    this.positionSearchItemTitle.setGravity(17);
    this.railwayItemTitle = this.railway.getItemTitle();
    this.railwayItemTitle.setCompoundDrawablePadding(ViewUtils.dip2px(this, 13.0F));
    this.railwayItemTitle.setGravity(17);
    this.imgPosition = getResources().getDrawable(R.drawable.nearby_list_icon_disable_search);
    this.imgRailway = getResources().getDrawable(R.drawable.nearby_list_icon_disable_subway);
    this.imgPosition.setBounds(0, 0, this.imgPosition.getMinimumWidth(), this.imgPosition.getMinimumHeight());
    this.imgRailway.setBounds(0, 0, this.imgRailway.getMinimumWidth(), this.imgRailway.getMinimumHeight());
    this.positionSearchItemTitle.setCompoundDrawables(this.imgPosition, null, null, null);
    this.railwayItemTitle.setCompoundDrawables(this.imgRailway, null, null, null);
    this.titleMain.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://switchcity"));
        HotelLandmarkActivity.this.startActivityForResult(paramView, 0);
        HotelLandmarkActivity.this.statisticsEvent("area5", "area5_city_click", "", 0);
      }
    });
    this.searchAroundCities = false;
    try
    {
      paramBundle = getIntent().getData().getQueryParameter("searcharoundcities");
      if (!TextUtils.isEmpty(paramBundle))
        this.searchAroundCities = Boolean.parseBoolean(paramBundle);
      label560: paramBundle = (TableView)findViewById(R.id.hotel_search_container);
      if (paramBundle != null)
      {
        if (this.searchAroundCities)
          break label651;
        paramBundle.setOnItemClickListener(this);
      }
      while (true)
      {
        this.fragmentManager = getSupportFragmentManager();
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        this.allRegionFragment = new HotelAllRegionFragment();
        this.fragmentTransaction.add(R.id.fragment_container, this.allRegionFragment).commit();
        if (this.searchAroundCities)
          break;
        sendHotRegionRequest();
        sendAllRegionRequest();
        return;
        label651: paramBundle.setVisibility(8);
      }
      sendAroundcityRegionRecomRequest();
      this.titleMain.setVisibility(8);
      return;
    }
    catch (java.lang.Exception paramBundle)
    {
      break label560;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.hotRegionRequest != null)
    {
      mapiService().abort(this.hotRegionRequest, this, true);
      this.hotRegionRequest = null;
    }
    if (this.allRegionRequest != null)
    {
      mapiService().abort(this.allRegionRequest, this, true);
      this.allRegionRequest = null;
    }
    if (this.aroundcityRegionRecomRequest != null)
    {
      mapiService().abort(this.aroundcityRegionRecomRequest, this, true);
      this.aroundcityRegionRecomRequest = null;
    }
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    if (paramView.getId() == R.id.positionSearch)
    {
      paramTableView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://customlocationlist"));
      paramTableView.putExtra("hint", "输入地点找酒店");
      startActivityForResult(paramTableView, 1);
      statisticsEvent("area5", "area5_keyword_click", "", 0);
      return;
    }
    this.url = "dianping://metrofilter";
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(this.url)), 2);
    statisticsEvent("area5", "area5_subway_click", "", 0);
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    super.onLocationChanged(paramLocationService);
    isShowLocal();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.hotRegionRequest)
    {
      this.hotRegionRequest = null;
      isShowLocal();
      this.hotelRegionContainer.setVisibility(8);
      this.searchbyregion.setVisibility(8);
      this.railway.setVisibility(8);
      Toast.makeText(this, paramMApiResponse.message().content(), 0).show();
    }
    do
    {
      return;
      if (paramMApiRequest != this.allRegionRequest)
        continue;
      this.allRegionRequest = null;
      this.fragmentContainer.setVisibility(8);
      this.allRegion.setVisibility(8);
      return;
    }
    while (paramMApiRequest != this.aroundcityRegionRecomRequest);
    this.aroundcityRegionRecomRequest = null;
    this.fragmentContainer.setVisibility(8);
    this.hotelRegionContainer.setVisibility(8);
    this.searchbyregion.setVisibility(8);
    this.allRegion.setVisibility(8);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.hotRegionRequest)
    {
      this.hotRegionRequest = null;
      isShowLocal();
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.hotelRegionContainer.removeAllViews();
        this.hotRegionResult = ((DPObject)paramMApiResponse.result());
        this.hotRegionList = this.hotRegionResult.getArray("RegionList");
        if ((this.hotRegionList == null) || (this.hotRegionList.length <= 0))
          break label149;
        this.hotelRegionContainer.setVisibility(0);
        this.searchbyregion.setVisibility(0);
        this.isMetroCity = this.hotRegionResult.getBoolean("IsMetroCity");
        if (!this.isMetroCity)
          break label137;
        this.railway.setVisibility(0);
        setItems(this.hotRegionList, this);
      }
    }
    label137: label149: 
    do
    {
      do
      {
        return;
        this.railway.setVisibility(8);
        break;
        this.hotelRegionContainer.setVisibility(8);
        this.searchbyregion.setVisibility(8);
        this.railway.setVisibility(8);
        return;
        if (paramMApiRequest != this.allRegionRequest)
          continue;
        this.allRegionRequest = null;
        this.allRegionList = ((DPObject)paramMApiResponse.result()).getArray("RegionList");
        this.allRegionFragment.setData(this.allRegionList);
        this.fragmentContainer.setVisibility(0);
        this.allRegion.setVisibility(0);
        return;
      }
      while (paramMApiRequest != this.aroundcityRegionRecomRequest);
      this.aroundcityRegionRecomRequest = null;
      this.allRegionList = ((DPObject)paramMApiResponse.result()).getArray("RegionList");
      if ((this.allRegionList != null) && (this.allRegionList.length > 0))
      {
        this.allRegionFragment.setData(this.allRegionList);
        this.fragmentContainer.setVisibility(0);
        this.allRegion.setVisibility(0);
      }
      this.hotRegionList = ((DPObject)paramMApiResponse.result()).getArray("RegionRecomList");
    }
    while ((this.hotRegionList == null) || (this.hotRegionList.length <= 0));
    this.hotelRegionContainer.setVisibility(0);
    this.searchbyregion.setVisibility(0);
    setItems(this.hotRegionList, this);
  }

  public void sendHotRegionRequest()
  {
    this.hotRegionRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotregion.hotel?cityid=" + cityId() + "&limit=15", CacheType.DISABLED);
    mapiService().exec(this.hotRegionRequest, this);
  }

  public void setItems(DPObject[] paramArrayOfDPObject, View.OnClickListener paramOnClickListener)
  {
    if (paramArrayOfDPObject == null)
      return;
    this.listener = paramOnClickListener;
    paramOnClickListener = new ArrayList();
    paramOnClickListener.addAll(Arrays.asList(paramArrayOfDPObject));
    this.hotRegionListLength = paramOnClickListener.size();
    int i = 0;
    label37: if (i < paramOnClickListener.size())
      if (i + 3 <= paramOnClickListener.size())
        break label96;
    label96: for (int j = paramOnClickListener.size(); ; j = i + 3)
    {
      this.hotRegionListEnd = j;
      paramArrayOfDPObject = paramOnClickListener.subList(i, this.hotRegionListEnd);
      this.hotelRegionContainer.addView(createRecommendRow(paramArrayOfDPObject));
      i += 3;
      break label37;
      break;
    }
  }

  private class LocalListener
    implements View.OnClickListener
  {
    private LocalListener()
    {
    }

    public void onClick(View paramView)
    {
      paramView = (DPObject)paramView.getTag();
      Intent localIntent = new Intent();
      localIntent.putExtra("result", paramView);
      localIntent.putExtra("type", 1);
      HotelLandmarkActivity.this.setResult(-1, localIntent);
      HotelLandmarkActivity.this.statisticsEvent("area5", "area5_distance", "", 0);
      HotelLandmarkActivity.this.finish();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.activity.HotelLandmarkActivity
 * JD-Core Version:    0.6.0
 */