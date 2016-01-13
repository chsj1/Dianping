package com.dianping.search.shoplist.data;

import android.text.TextUtils;
import android.util.Log;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.data.model.ShopDisplayTagModel;
import com.dianping.search.shoplist.data.model.AdShopDataModel;
import com.dianping.search.shoplist.data.model.ExtSearchModel;
import com.dianping.search.shoplist.data.model.GlobalSearchResult;
import com.dianping.search.shoplist.data.model.KtvShopDataModel;
import com.dianping.search.shoplist.data.model.SearchDirectZoneModel;
import com.dianping.search.util.ShopListUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class NewShopListDataSource extends BaseShopListDataSource
{
  public static final int TYPE_NORMAL = 2;
  public static final int TYPE_NO_PIC = 1;
  public static final int TYPE_URL_PIC = 0;
  public AdShopDataModel[] adShopModels;
  public String cityName;
  public DPObject curFloor;
  public int disableRewrite = 0;
  public ArrayList<ExtSearchModel> extSearchModels = new ArrayList();
  public DPObject[] floorNavList;
  public GlobalSearchResult globalSearchResult;
  public DPObject guideKeywordResult;
  public int guideKeywordType;
  public String guideKeywords;
  public ArrayList<ShopDisplayTagModel> guideKeywordsModels = new ArrayList();
  public String hospitalEntryTitle;
  public String hospitalEntryUrl;
  public boolean isCurrentCity;
  public boolean isGuideKeyword;
  public boolean isShowAdShop;
  public boolean isShowMoreDirectZone;
  public boolean isShowMoreExtSearch;
  public boolean isShowMoreMall;
  public ArrayList<ShopDataModel> nearbyHeadlineShops = new ArrayList();
  public boolean needAdvFilter = false;
  public String pageModule;
  public String[] recAdGuideKeywords;
  public int searchDirectZoneExpandType;
  public ArrayList<SearchDirectZoneModel> searchDirectZoneModels = new ArrayList();
  public double shopLat;
  public double shopLng;
  public ArrayList<ShopDataModel> shopModels = new ArrayList(25);
  public String title;
  private int topMallCount;
  public int topWeddingShopCount = 0;
  public String weddingBanquetTitle;
  public String weddingBanquetUrl;

  private void appGuideKeywordResult(DPObject paramDPObject)
  {
    this.guideKeywordsModels = new ArrayList();
    this.guideKeywordResult = paramDPObject.getObject("GuideKeywordResult");
    if (this.guideKeywordResult == null);
    while (true)
    {
      return;
      this.guideKeywordType = this.guideKeywordResult.getInt("Type");
      paramDPObject = this.guideKeywordResult.getArray("List");
      if (paramDPObject == null)
        continue;
      int j = paramDPObject.length;
      int i = 0;
      while (i < j)
      {
        this.guideKeywordsModels.add(ShopDisplayTagModel.fromDPObject(paramDPObject[i]));
        i += 1;
      }
    }
  }

  private void appendBrandAdResult(DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getArray("AdShops");
    if (paramDPObject != null)
    {
      int j = paramDPObject.length;
      this.adShopModels = new AdShopDataModel[j];
      int i = 0;
      while (i < j)
      {
        paramDPObject[i] = paramDPObject[i].edit().remove("ListPosition").putInt("ListPosition", i).generate();
        this.adShopModels[i] = new AdShopDataModel(paramDPObject[i]);
        i += 1;
      }
    }
    this.adShopModels = new AdShopDataModel[0];
  }

  private void appendDirectZoneResult(DPObject paramDPObject)
  {
    this.searchDirectZoneModels = new ArrayList();
    paramDPObject = paramDPObject.getObject("SearchDirectZoneResult");
    if (paramDPObject == null);
    while (true)
    {
      return;
      this.searchDirectZoneExpandType = paramDPObject.getInt("ExpandType");
      paramDPObject = paramDPObject.getArray("List");
      if (paramDPObject == null)
        continue;
      int j = paramDPObject.length;
      int i = 0;
      while (i < j)
      {
        SearchDirectZoneModel localSearchDirectZoneModel = SearchDirectZoneModel.fromDPObject(paramDPObject[i], i, suggestKeyword(), queryId());
        if (!TextUtils.isEmpty(localSearchDirectZoneModel.mFeedback))
        {
          HashMap localHashMap = new HashMap();
          localHashMap.put("act", "1");
          localHashMap.put("adidx", String.valueOf(i + 1));
          AdClientUtils.report(localSearchDirectZoneModel.mFeedback, localHashMap);
          Log.d("debug_AdGA", "PV-GA-DirectZone:" + String.valueOf(i + 1));
        }
        this.searchDirectZoneModels.add(localSearchDirectZoneModel);
        i += 1;
      }
    }
  }

  private void appendExtraSearchResult(DPObject paramDPObject)
  {
    this.extSearchModels = new ArrayList();
    paramDPObject = paramDPObject.getArray("ExtSearchResult");
    if (paramDPObject != null)
    {
      int j = paramDPObject.length;
      int i = 0;
      while (i < j)
      {
        ExtSearchModel localExtSearchModel = ExtSearchModel.fromDPObject(paramDPObject[i], i, suggestKeyword(), queryId());
        if (!TextUtils.isEmpty(localExtSearchModel.feedback))
        {
          HashMap localHashMap = new HashMap();
          localHashMap.put("act", "1");
          localHashMap.put("adidx", String.valueOf(i + 1));
          AdClientUtils.report(localExtSearchModel.feedback, localHashMap);
          Log.d("debug_AdGA", "PV-GA-Ext:" + String.valueOf(i + 1));
        }
        this.extSearchModels.add(localExtSearchModel);
        i += 1;
      }
    }
  }

  private void appendGlobalSearchResult(DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getObject("GlobalSearchResult");
    if (paramDPObject != null)
      this.globalSearchResult = new GlobalSearchResult(paramDPObject);
  }

  private void appendSearchResultExtraInfo(DPObject paramDPObject)
  {
    CharSequence localCharSequence = null;
    Object localObject = paramDPObject.getString("SearchResultExtraInfo");
    if (!TextUtils.isEmpty((CharSequence)localObject));
    while (true)
    {
      int i;
      int k;
      try
      {
        localObject = new JSONObject((String)localObject);
        if (paramDPObject.getInt("StartIndex") != 0)
          continue;
        this.topWeddingShopCount = ((JSONObject)localObject).optInt("TopWeddingShopCount", 0);
        this.weddingBanquetTitle = ((JSONObject)localObject).optString("WeddingBanquetTitle", "");
        this.weddingBanquetUrl = ((JSONObject)localObject).optString("WeddingBanquetUrl", "");
        this.hospitalEntryTitle = ((JSONObject)localObject).optString("HospitalEntryTitle", "");
        this.hospitalEntryUrl = ((JSONObject)localObject).optString("HospitalEntryUrl", "");
        this.guideKeywords = ((JSONObject)localObject).optString("GuideKeywords", "");
        if (!TextUtils.isEmpty(((JSONObject)localObject).optString("RecAdGuildKeywords", "")))
          continue;
        paramDPObject = localCharSequence;
        j = 0;
        if (paramDPObject == null)
          continue;
        int m = paramDPObject.length;
        i = 0;
        if (i >= m)
          continue;
        k = j;
        if (TextUtils.isEmpty(paramDPObject[i]))
          break label316;
        k = j + 1;
        break label316;
        paramDPObject = ((JSONObject)localObject).optString("RecAdGuildKeywords", "").split("\\|");
        continue;
        this.recAdGuideKeywords = new String[j];
        m = paramDPObject.length;
        j = 0;
        i = 0;
        if (j >= m)
          continue;
        localCharSequence = paramDPObject[j];
        if (TextUtils.isEmpty(localCharSequence))
          break label313;
        localObject = this.recAdGuideKeywords;
        k = i + 1;
        localObject[i] = localCharSequence;
        i = k;
        break label329;
        this.recAdGuideKeywords = null;
        return;
      }
      catch (Exception paramDPObject)
      {
        paramDPObject.printStackTrace();
        return;
      }
      this.weddingBanquetTitle = "";
      this.weddingBanquetUrl = "";
      this.hospitalEntryTitle = "";
      this.hospitalEntryUrl = "";
      this.guideKeywords = "";
      this.recAdGuideKeywords = null;
      return;
      label313: break label329;
      label316: i += 1;
      int j = k;
      continue;
      label329: j += 1;
    }
  }

  public void appendResult(DPObject paramDPObject)
  {
    super.appendResult(paramDPObject);
    appendSearchResultExtraInfo(paramDPObject);
    appGuideKeywordResult(paramDPObject);
    if (!TextUtils.isEmpty(this.guideKeywords))
      if (this.guideKeywords.equals("null"))
      {
        appendDirectZoneResult(paramDPObject);
        if (this.searchDirectZoneModels.size() == 0)
          appendExtraSearchResult(paramDPObject);
      }
    while (true)
    {
      if (startIndex() == 0)
        appendBrandAdResult(paramDPObject);
      appendGlobalSearchResult(paramDPObject);
      return;
      appendDirectZoneResult(paramDPObject);
      if (this.searchDirectZoneModels.size() != 0)
        continue;
      appendExtraSearchResult(paramDPObject);
    }
  }

  protected void clearShops(DPObject paramDPObject)
  {
    super.clearShops(paramDPObject);
    this.shopModels.clear();
    this.nearbyHeadlineShops.clear();
    this.topMallCount = paramDPObject.getInt("TopMallCount");
    this.isShowMoreMall = false;
  }

  protected int getDeviation()
  {
    return this.nearbyHeadlineShops.size();
  }

  public void pullToRefresh(boolean paramBoolean)
  {
    this.isShowMoreMall = false;
    this.isShowAdShop = false;
    this.isShowMoreDirectZone = false;
    super.pullToRefresh(paramBoolean);
  }

  public void reset(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.extSearchModels = new ArrayList();
      this.searchDirectZoneModels = new ArrayList();
      this.globalSearchResult = null;
      this.shopModels.clear();
      this.adShopModels = null;
      this.isShowMoreMall = false;
      this.isShowAdShop = false;
      this.isShowMoreDirectZone = false;
      this.topWeddingShopCount = 0;
      this.guideKeywords = null;
      this.guideKeywordsModels = new ArrayList();
      this.guideKeywordResult = null;
      this.guideKeywordType = 2;
    }
    super.reset(paramBoolean);
  }

  public void setGuideKeywrodFlag(boolean paramBoolean)
  {
    this.isGuideKeyword = paramBoolean;
  }

  protected void storeNearByShop(DPObject paramDPObject)
  {
    super.storeNearByShop(paramDPObject);
    this.nearbyHeadlineShops.add(new ShopDataModel(paramDPObject));
  }

  protected void storeOneShop(DPObject paramDPObject)
  {
    super.storeOneShop(paramDPObject);
    if (ShopListUtils.shouldShowDeal(paramDPObject))
    {
      this.shopModels.add(new KtvShopDataModel(paramDPObject));
      return;
    }
    this.shopModels.add(new ShopDataModel(paramDPObject));
  }

  public int topMallCount()
  {
    return this.topMallCount;
  }

  protected void updateEventText(int paramInt, DPObject paramDPObject)
  {
    super.updateEventText(paramInt, paramDPObject);
    ShopDataModel localShopDataModel = (ShopDataModel)this.shopModels.get(paramInt);
    localShopDataModel.shopObj = paramDPObject;
    localShopDataModel.eventText = paramDPObject.getString("EventText");
  }

  protected void updateNavData(DPObject paramDPObject)
  {
    super.updateNavData(paramDPObject);
    Object localObject = paramDPObject.getArray("FloorNavs");
    if ((localObject != null) || (startIndex() == 0))
      this.floorNavList = ((DPObject)localObject);
    localObject = paramDPObject.getObject("CurrentFloor");
    if (localObject != null)
      this.curFloor = ((DPObject)localObject);
    paramDPObject = paramDPObject.getObject("CurrentCategory");
    if (paramDPObject != null)
      setCurCategory(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.NewShopListDataSource
 * JD-Core Version:    0.6.0
 */