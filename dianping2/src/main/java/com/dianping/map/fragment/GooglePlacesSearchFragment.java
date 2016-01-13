package com.dianping.map.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.dianping.app.DPActivity;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.List<Ljava.util.Map<Ljava.lang.String;Ljava.lang.String;>;>;
import java.util.Map;

public class GooglePlacesSearchFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String ACTION_HISTORY_CHANGED = "com.dianping.ACTION_HISTORY_CHANGED";
  private static final String LOG_TAG = GooglePlacesSearchFragment.class.getSimpleName();
  private static final String SPLIT = "IAMSPLIT";
  List<Map<String, String>> data;
  private boolean doSearch;
  private String errorMsg;
  private String gaAction;
  private String keyword;
  private ListView listView;
  GooglePlacesSearchFragment.Adapter mAdapter;
  private int mMapType = -1;
  private OnResultItemClickListner mOnResultItemClickListner;
  private BroadcastReceiver receiver = new GooglePlacesSearchFragment.1(this);
  private MApiRequest req;
  private String searchLat;
  private String searchLng;

  private boolean clearSearchHistory()
  {
    this.data.clear();
    if (myAddress() != null)
      this.data.add(myAddress());
    this.data.add(noHistoryItem());
    return DPActivity.preferences().edit().remove("googlePlaceSearchHistory").commit();
  }

  private Map<String, String> creatItem(String paramString)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("title", paramString);
    return localHashMap;
  }

  private boolean doParseResult(DPObject[] paramArrayOfDPObject)
  {
    this.data.clear();
    if (paramArrayOfDPObject.length < 1)
    {
      if (myAddress() != null)
        this.data.add(myAddress());
      this.data.add(noSearchResultItem());
    }
    while (true)
    {
      return true;
      Log.i(LOG_TAG, "the json result count is " + paramArrayOfDPObject.length);
      Object localObject = new HashMap();
      ((Map)localObject).put("title", "共找到" + paramArrayOfDPObject.length + "个 “" + this.keyword + "”");
      this.data.add(localObject);
      if (myAddress() != null)
        this.data.add(myAddress());
      int i = 0;
      while (i < paramArrayOfDPObject.length)
      {
        localObject = paramArrayOfDPObject[i];
        HashMap localHashMap = new HashMap();
        localHashMap.put("title", ((DPObject)localObject).getString("Name"));
        localHashMap.put("subtitle", ((DPObject)localObject).getString("Vicinity"));
        localHashMap.put("lat", String.valueOf(((DPObject)localObject).getDouble("lat")));
        localHashMap.put("lng", String.valueOf(((DPObject)localObject).getDouble("lng")));
        this.data.add(localHashMap);
        i += 1;
      }
    }
  }

  private void doSearch(String paramString, boolean paramBoolean)
  {
    if (this.req != null)
      mapiService().abort(this.req, this, true);
    Object localObject = null;
    try
    {
      String str = "http://m.api.dianping.com/searchlocation.bin?lat=" + this.searchLat + "&lng=" + this.searchLng + "&range=50000" + "&keyword=" + URLEncoder.encode(paramString, "UTF-8");
      localObject = str;
      if (this.mMapType < 0)
        this.mMapType = 1;
      this.req = BasicMApiRequest.mapiGet(localObject + "&mtype=" + this.mMapType, CacheType.DISABLED);
      mapiService().exec(this.req, this);
      if (paramBoolean)
        saveSearchHistory(paramString);
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
        Log.e(LOG_TAG, "UnsupportedEncodingException", localUnsupportedEncodingException);
    }
  }

  private void initSearchCenter()
  {
    Object localObject1 = getActivity().getSharedPreferences(getActivity().getPackageName(), 0).getString("findconditions_region", null);
    if (localObject1 != null)
    {
      localObject1 = ((String)localObject1).split("IAMSPLIT");
      if (localObject1.length == 4)
      {
        this.searchLat = localObject1[0];
        this.searchLng = localObject1[1];
        return;
      }
    }
    Object localObject3 = null;
    localObject1 = localObject3;
    if (locationService().hasLocation());
    try
    {
      localObject1 = (Location)locationService().location().decodeToObject(Location.DECODER);
      if ((localObject1 == null) || (((Location)localObject1).city().id() != cityId()))
      {
        this.searchLat = String.valueOf(city().latitude());
        this.searchLng = String.valueOf(city().longitude());
        return;
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
      this.searchLat = String.valueOf(localObject2.latitude());
      this.searchLng = String.valueOf(localObject2.longitude());
    }
  }

  private Map<String, String> myAddress()
  {
    HashMap localHashMap2 = null;
    Object localObject = localHashMap2;
    if (locationService().hasLocation());
    try
    {
      localObject = (Location)locationService().location().decodeToObject(Location.DECODER);
      localHashMap2 = null;
      if (localObject != null)
      {
        localHashMap2 = new HashMap();
        localHashMap2.put("title", ((Location)localObject).address());
        localHashMap2.put("subtitle", "我的位置");
        localHashMap2.put("lat", String.valueOf(((Location)localObject).offsetLatitude()));
        localHashMap2.put("lng", String.valueOf(((Location)localObject).offsetLongitude()));
      }
      return localHashMap2;
    }
    catch (ArchiveException localHashMap1)
    {
      while (true)
      {
        localArchiveException.printStackTrace();
        HashMap localHashMap1 = localHashMap2;
      }
    }
  }

  private Map<String, String> noHistoryItem()
  {
    return creatItem("没有搜索记录");
  }

  private Map<String, String> noSearchResultItem()
  {
    return creatItem("没有找到任何地点");
  }

  private boolean saveSearchHistory(String paramString)
  {
    SharedPreferences localSharedPreferences = DPActivity.preferences();
    Object localObject2 = localSharedPreferences.getString("googlePlaceSearchHistory", "没有搜索记录");
    Object localObject1;
    if (((String)localObject2).contains(paramString + String.valueOf("IAMSPLIT".hashCode())))
    {
      localObject1 = ((String)localObject2).replace(paramString + String.valueOf("IAMSPLIT".hashCode()), "");
      localObject2 = new StringBuilder();
      if (!"没有搜索记录".equals(localObject1))
        break label175;
      ((StringBuilder)localObject2).append(paramString);
    }
    while (true)
    {
      boolean bool = localSharedPreferences.edit().putString("googlePlaceSearchHistory", ((StringBuilder)localObject2).toString()).commit();
      paramString = new Intent("com.dianping.ACTION_HISTORY_CHANGED");
      getActivity().sendBroadcast(paramString);
      return bool;
      localObject1 = localObject2;
      if (!((String)localObject2).contains(paramString))
        break;
      localObject1 = ((String)localObject2).replace(paramString, "");
      break;
      label175: ((StringBuilder)localObject2).append(paramString).append(String.valueOf("IAMSPLIT".hashCode())).append((String)localObject1);
    }
  }

  List<Map<String, String>> getSearchHistory(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = DPActivity.preferences().getString("googlePlaceSearchHistory", "没有搜索记录");
    String[] arrayOfString = ((String)localObject).split(String.valueOf("IAMSPLIT".hashCode()));
    if (myAddress() != null)
      localArrayList.add(myAddress());
    int i = 0;
    if ((i < arrayOfString.length) && (i < paramInt))
    {
      String str = arrayOfString[i];
      if ((str != null) && (str.equals("我的位置")));
      while (true)
      {
        i += 1;
        break;
        HashMap localHashMap = new HashMap();
        localHashMap.put("title", str);
        localArrayList.add(localHashMap);
      }
    }
    if (!((String)localObject).equals("没有搜索记录"))
    {
      localObject = new HashMap();
      ((Map)localObject).put("title", "清空搜索记录");
      localArrayList.add(localObject);
      localObject = new HashMap();
      ((Map)localObject).put("title", "历史记录");
      localArrayList.add(0, localObject);
    }
    return (List<Map<String, String>>)localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      this.keyword = paramBundle.getString("keyword");
      this.errorMsg = paramBundle.getString("errorMsg");
    }
    Uri localUri = getActivity().getIntent().getData();
    this.doSearch = false;
    if (localUri != null)
    {
      this.searchLat = localUri.getQueryParameter("lat");
      this.searchLng = localUri.getQueryParameter("lng");
      if ((this.searchLat == null) || (this.searchLng == null) || ("0.0".equals(this.searchLat)) || ("0.0".equals(this.searchLng)))
        initSearchCenter();
      if (localUri.getQueryParameter("keyword") == null)
        break label235;
      this.data = new ArrayList();
      this.doSearch = true;
      this.keyword = localUri.getQueryParameter("keyword");
    }
    while (true)
    {
      this.mMapType = getIntParam("mapType", -1);
      super.onCreate(paramBundle);
      this.mAdapter = new GooglePlacesSearchFragment.Adapter(this);
      this.listView.setAdapter(this.mAdapter);
      this.listView.setOnItemClickListener(this);
      if (this.doSearch)
        doSearch(localUri.getQueryParameter("keyword"), true);
      paramBundle = new IntentFilter("com.dianping.ACTION_HISTORY_CHANGED");
      registerReceiver(this.receiver, paramBundle);
      return;
      label235: this.data = getSearchHistory(20);
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == -1) && (this.mOnResultItemClickListner != null))
      this.mOnResultItemClickListner.onResultItemClick(paramIntent);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.list_frame, paramViewGroup, false);
    this.listView = ((ListView)paramLayoutInflater.findViewById(R.id.list));
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    if (this.req != null)
      mapiService().abort(this.req, this, true);
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.mAdapter.getItem(paramInt);
    if ("清空搜索记录".equals(paramAdapterView.get("title")))
      if (clearSearchHistory())
      {
        this.mAdapter.notifyDataSetChanged();
        statisticsEvent("localsearch5", "localsearch5_keyword_clear", "", 0);
      }
    do
    {
      return;
      Log.i(LOG_TAG, "clear search history failed because of the data removed from the pref failed");
      break;
    }
    while (("没有搜索记录".equals(paramAdapterView.get("title"))) || ("没有找到任何地点".equals(paramAdapterView.get("title"))) || (("历史记录".equals(paramAdapterView.get("title"))) && (paramInt == 0)) || ((paramAdapterView.get("title") != null) && (((String)paramAdapterView.get("title")).startsWith("共找到")) && (paramInt == 0)) || (((String)paramAdapterView.get("title")).equals(this.errorMsg)));
    if (paramAdapterView.get("lat") == null)
    {
      startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://googleplacessearch?keyword=" + (String)paramAdapterView.get("title") + "&lat=" + this.searchLat + "&lng=" + this.searchLng + "&range=50000" + "&mapType=" + this.mMapType)), 0);
      statisticsEvent("localsearch5", "localsearch5_keyword_history", (String)paramAdapterView.get("title"), 0);
      return;
    }
    if ((paramAdapterView.get("subtitle") == null) || (!((String)paramAdapterView.get("subtitle")).equals("我的位置")))
      saveSearchHistory((String)paramAdapterView.get("title"));
    paramView = new Intent();
    paramView.putExtra("title", (String)paramAdapterView.get("title"));
    paramView.putExtra("lat", (String)paramAdapterView.get("lat"));
    paramView.putExtra("lng", (String)paramAdapterView.get("lng"));
    paramView.putExtra("address", (String)paramAdapterView.get("subtitle"));
    if (this.mOnResultItemClickListner != null)
      this.mOnResultItemClickListner.onResultItemClick(paramView);
    if (this.gaAction != null)
    {
      statisticsEvent("localsearch5", this.gaAction, (String)paramAdapterView.get("title"), 0);
      return;
    }
    statisticsEvent("localsearch5", "localsearch5_keyword", (String)paramAdapterView.get("title"), 0);
  }

  public void onKeywordChanged(String paramString)
  {
    if ("".equals(paramString))
    {
      this.data = getSearchHistory(20);
      this.mAdapter.notifyDataSetChanged();
      return;
    }
    this.keyword = paramString;
    doSearch(paramString, false);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.errorMsg = paramMApiResponse.message().content();
    this.data.clear();
    this.data.add(creatItem(this.errorMsg));
    this.mAdapter.notifyDataSetChanged();
    Log.e(LOG_TAG, "requst google places api failed with status code " + paramMApiResponse.statusCode() + ", the url is " + paramMApiRequest.url());
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject[]))
    {
      if (doParseResult((DPObject[])(DPObject[])paramMApiResponse.result()))
        this.mAdapter.notifyDataSetChanged();
      return;
    }
    onRequestFailed(paramMApiRequest, paramMApiResponse);
    Log.e(LOG_TAG, "response data is not instanceof DPObject[]");
  }

  public void onResume()
  {
    super.onResume();
    this.mMapType = getIntParam("mapType", -1);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("keyword", this.keyword);
    paramBundle.putString("errorMsg", this.errorMsg);
    super.onSaveInstanceState(paramBundle);
  }

  public void setGaAction(String paramString)
  {
    this.gaAction = paramString;
  }

  public void setOnResultItemClickListner(OnResultItemClickListner paramOnResultItemClickListner)
  {
    this.mOnResultItemClickListner = paramOnResultItemClickListner;
  }

  public static abstract interface OnResultItemClickListner
  {
    public abstract void onResultItemClick(Intent paramIntent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.fragment.GooglePlacesSearchFragment
 * JD-Core Version:    0.6.0
 */