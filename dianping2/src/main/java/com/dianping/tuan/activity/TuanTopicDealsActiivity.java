package com.dianping.tuan.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.tuan.adapter.ModuleBasicLoadAdapter;
import com.dianping.tuan.adapter.TuanBasicLoadAdapter.RequestInfoInterface;
import com.dianping.tuan.adapter.decorator.ModuleDividerAdapter;
import com.dianping.tuan.widget.NaviTagFilterBar;
import com.dianping.tuan.widget.NaviTagFilterBar.OnSelectionChangedListener;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class TuanTopicDealsActiivity extends BaseTuanActivity
  implements NaviTagFilterBar.OnSelectionChangedListener, TuanBasicLoadAdapter.RequestInfoInterface
{
  public static final int FILTER_BAR_HEIGHT_DIP = 45;
  public static final String MAPI_TOPIC_DEALS = "findrichdealsgn.bin";
  protected String categoryId;
  protected HashMap<String, String> currentFilterData = new HashMap();
  protected String currentScreening;
  protected HashMap<String, String> intentParamMap = new HashMap();
  protected boolean intentParsed = false;
  protected boolean isFirstPage = true;
  protected String keyword;
  protected ModuleBasicLoadAdapter listAdapter;
  protected ListView listView;
  protected DPObject[] naviFilterData;
  protected NovaLinearLayout rootView;
  protected DPObject[] screeningData;
  protected boolean screeningTaped;
  protected DPObject[] tagCellData;
  protected String title;
  protected NovaFrameLayout topCellContainer;
  protected NaviTagFilterBar topFilterBar;
  protected String topic;

  public NaviTagFilterBar createFilterBar()
  {
    NaviTagFilterBar localNaviTagFilterBar = new NaviTagFilterBar(this);
    localNaviTagFilterBar.setHasScreening(true);
    return localNaviTagFilterBar;
  }

  public MApiRequest createRequest(int paramInt)
  {
    if (paramInt == 0);
    UrlBuilder localUrlBuilder;
    for (boolean bool = true; ; bool = false)
    {
      this.isFirstPage = bool;
      localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
      localUrlBuilder.appendPath("findrichdealsgn.bin");
      localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
      if (getAccount() != null)
        localUrlBuilder.addParam("token", getAccount().token());
      if (location() != null)
      {
        localUrlBuilder.addParam("lat", Double.valueOf(location().latitude()));
        localUrlBuilder.addParam("lng", Double.valueOf(location().longitude()));
        this.listAdapter.setLocation(location().latitude(), location().longitude());
      }
      localUrlBuilder.addParam("start", Integer.valueOf(paramInt));
      Object localObject = mergeParameters();
      if (((HashMap)localObject).size() <= 0)
        break;
      localObject = ((HashMap)localObject).entrySet().iterator();
      while (((Iterator)localObject).hasNext())
      {
        Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
        localUrlBuilder.addParam((String)localEntry.getKey(), localEntry.getValue());
      }
    }
    localUrlBuilder.addParam("screening", this.currentScreening);
    localUrlBuilder.addParam("topic", this.topic);
    return (MApiRequest)mapiGet(this.listAdapter, localUrlBuilder.buildUrl(), CacheType.DISABLED);
  }

  public String filterType2ParamEnNameKey(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 1:
      return "categoryenname";
    case 2:
    }
    return "regionenname";
  }

  public String filterType2ParamKey(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 1:
      return "categoryid";
    case 2:
      return "regionid";
    case 3:
      return "distance";
    case 4:
    }
    return "filter";
  }

  public String getPageName()
  {
    return "drlist";
  }

  protected HashMap<String, String> mergeParameters()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.putAll(this.intentParamMap);
    localHashMap.putAll(this.currentFilterData);
    return localHashMap;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (this.rootView == null)
      setupView();
    setContentView(this.rootView);
    parseIntent();
    updateView();
    this.listAdapter.reset();
  }

  public void onFilterSelectionChanged(HashMap<Integer, DPObject> paramHashMap, DPObject paramDPObject)
  {
    refreshSelectedFilterData(paramHashMap);
    if (DPObjectUtils.isDPObjectof(paramDPObject, "Navi"))
    {
      paramHashMap = filterType2ParamKey(paramDPObject.getInt("Type"));
      if ((!"regionid".equals(paramHashMap)) && (!"range".equals(paramHashMap)))
        break label71;
      GAHelper.instance().contextStatisticsEvent(getBaseContext(), "district", null, 0, "tap");
    }
    while (true)
    {
      this.listAdapter.reset();
      return;
      label71: if (!"filter".equals(paramHashMap))
        continue;
      GAHelper.instance().contextStatisticsEvent(getBaseContext(), "sort", null, 0, "tap");
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (this.isFirstPage)
      {
        this.naviFilterData = paramMApiRequest.getArray("NaviBars");
        this.tagCellData = paramMApiRequest.getArray("TagNavis");
        if (!this.screeningTaped)
        {
          this.screeningData = paramMApiRequest.getArray("Screenings");
          this.currentScreening = "";
        }
        this.screeningTaped = false;
        updateView();
      }
    }
  }

  public void onScreeningSelectionChanged(HashMap<Integer, DPObject> paramHashMap, String paramString)
  {
    Log.v("debug_test", "tuan rec shop activity screening changed");
    refreshSelectedFilterData(paramHashMap);
    this.currentScreening = paramString;
    GAHelper.instance().contextStatisticsEvent(getBaseContext(), "select", null, 0, "tap");
    this.screeningTaped = true;
    this.listAdapter.reset();
  }

  public void onTagCellSelectionChanged(int paramInt, HashMap<Integer, DPObject> paramHashMap, DPObject paramDPObject)
  {
    refreshSelectedFilterData(paramHashMap);
    if ((DPObjectUtils.isDPObjectof(paramDPObject, "Navi")) && ("categoryid".equals(filterType2ParamKey(paramDPObject.getInt("Type")))))
      GAHelper.instance().contextStatisticsEvent(getBaseContext(), "selectcategory", paramDPObject.getString("Title"), paramInt, "tap");
    this.listAdapter.reset();
  }

  protected void parseIntent()
  {
    if (this.intentParsed)
      return;
    String str2 = getStringParam("category");
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = getStringParam("categoryid");
    if (!TextUtils.isEmpty(str1))
    {
      this.intentParamMap.put("categoryid", str1);
      this.categoryId = str1;
    }
    this.intentParamMap.put("categoryenname", getStringParam("categoryenname"));
    str2 = getStringParam("region");
    str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = getStringParam("regionid");
    if (!TextUtils.isEmpty(str1))
      this.intentParamMap.put("regionid", str1);
    this.intentParamMap.put("regionenname", getStringParam("regionenname"));
    str1 = getStringParam("sort");
    if (!TextUtils.isEmpty(str1))
      this.intentParamMap.put("filter", str1);
    this.intentParamMap.put("keyword", getStringParam("keyword"));
    this.topic = getStringParam("topic");
    this.title = getStringParam("title");
    this.intentParsed = true;
  }

  protected void refreshSelectedFilterData(HashMap<Integer, DPObject> paramHashMap)
  {
    this.currentFilterData.clear();
    if ((paramHashMap != null) && (paramHashMap.size() > 0))
    {
      paramHashMap = paramHashMap.entrySet().iterator();
      while (paramHashMap.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)paramHashMap.next();
        String str1 = filterType2ParamKey(((Integer)localEntry.getKey()).intValue());
        String str2 = filterType2ParamEnNameKey(((Integer)localEntry.getKey()).intValue());
        if (TextUtils.isEmpty(str1))
          continue;
        if (DPObjectUtils.isDPObjectof(localEntry.getValue(), "Navi"))
          this.currentFilterData.put(str1, "" + ((DPObject)localEntry.getValue()).getInt("ID"));
        if ((TextUtils.isEmpty(str2)) || (!DPObjectUtils.isDPObjectof(localEntry.getValue(), "Navi")))
          continue;
        this.currentFilterData.put(str2, "" + ((DPObject)localEntry.getValue()).getString("EnName"));
      }
    }
  }

  protected void setHeight()
  {
    this.topFilterBar.setLineHegiht(ViewUtils.dip2px(getBaseContext(), 45.0F));
  }

  protected void setupView()
  {
    this.rootView = new NovaLinearLayout(getBaseContext());
    this.rootView.setOrientation(1);
    Object localObject = new ViewGroup.LayoutParams(-1, -1);
    this.rootView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.topCellContainer = new NovaFrameLayout(getBaseContext());
    localObject = new ViewGroup.LayoutParams(-1, -2);
    this.topCellContainer.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.rootView.addView(this.topCellContainer);
    this.listView = new ListView(getBaseContext());
    this.listView.setDivider(null);
    localObject = new ViewGroup.LayoutParams(-1, -1);
    this.listView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.rootView.addView(this.listView);
    this.topFilterBar = createFilterBar();
    this.topFilterBar.setOnSelectionChangedListener(this);
    this.topFilterBar.setNeedCount(true);
    this.listAdapter = new ModuleBasicLoadAdapter(this);
    this.listAdapter.setRequestInfoInterface(this);
    this.listView.setAdapter(new ModuleDividerAdapter(getBaseContext(), this.listAdapter));
    this.topCellContainer.addView(this.topFilterBar);
    localObject = new FrameLayout.LayoutParams(-1, -2);
    this.topFilterBar.setLayoutParams((ViewGroup.LayoutParams)localObject);
  }

  protected void updateFilterBar()
  {
    if (((this.naviFilterData == null) || (this.naviFilterData.length < 1)) && ((this.screeningData == null) || (this.screeningData.length < 1)) && ((this.tagCellData == null) || (this.tagCellData.length < 1)))
    {
      this.topFilterBar.setVisibility(8);
      return;
    }
    this.topFilterBar.setVisibility(0);
    this.topFilterBar.setFilterData(this.naviFilterData, false);
    this.topFilterBar.setScreeningData(this.screeningData, false);
    this.topFilterBar.setTagCellData(this.tagCellData, false);
    this.topFilterBar.setScreening(this.currentScreening, false);
    this.topFilterBar.updateView();
    setHeight();
  }

  protected void updateView()
  {
    if (!TextUtils.isEmpty(this.title))
      setTitle(this.title);
    updateFilterBar();
    this.gaExtra.title = this.title;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanTopicDealsActiivity
 * JD-Core Version:    0.6.0
 */