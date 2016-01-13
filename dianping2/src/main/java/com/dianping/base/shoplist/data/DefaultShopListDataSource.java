package com.dianping.base.shoplist.data;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.model.ShopListDataSource;
import java.util.ArrayList;
import java.util.Arrays;

@Deprecated
public class DefaultShopListDataSource extends AbstractDataSource
  implements ShopListDataSource
{
  Context context;
  private String emptyMsg;
  private String errorMsg;
  private boolean hasSearchDate;
  private long hotelCheckinTimeMills;
  private long hotelCheckoutTimeMills;
  private boolean isEnd;
  private boolean isRank;
  private View lastExtraView;
  View.OnClickListener lastListener;
  int lastResource;
  private int loadMoreCount;
  private ArrayList<DPObject> mBannerList = new ArrayList();
  private ArrayList<DPObject> mHotelTuanInfoList = new ArrayList();
  protected DataSource.DataLoader mLoader;
  private int nextStartIndex;
  private double offsetLatitude;
  private double offsetLongitude;
  private int recordCount;
  private ArrayList<DPObject> shops = new ArrayList();
  private boolean shouldShowDistance;
  private int startIndex;
  private String takeawayTips;
  private String takeawayUrl;

  public DefaultShopListDataSource(DataSource.DataLoader paramDataLoader)
  {
    this.mLoader = paramDataLoader;
  }

  public void appendShops(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("StartIndex") == this.nextStartIndex)
    {
      if (this.nextStartIndex == 0)
        this.shops.clear();
      if (paramDPObject.getArray("List") != null)
        this.shops.addAll(Arrays.asList(paramDPObject.getArray("List")));
      this.recordCount = paramDPObject.getInt("RecordCount");
      this.startIndex = paramDPObject.getInt("StartIndex");
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.isEnd = paramDPObject.getBoolean("IsEnd");
      setEmptyMsg(paramDPObject.getString("EmptyMsg"));
      this.errorMsg = null;
      this.hasSearchDate = paramDPObject.getBoolean("HasSearchDate");
      this.mBannerList.clear();
      if ((paramDPObject.getArray("BannerList") != null) && (paramDPObject.getArray("BannerList").length > 0))
        this.mBannerList.addAll(Arrays.asList(paramDPObject.getArray("BannerList")));
      this.mHotelTuanInfoList.clear();
      if ((paramDPObject.getArray("HotelTuanInfoList") != null) && (paramDPObject.getArray("HotelTuanInfoList").length > 0))
        this.mHotelTuanInfoList.addAll(Arrays.asList(paramDPObject.getArray("HotelTuanInfoList")));
      if (paramDPObject.getString("KeywordExtendTips") != null)
        this.takeawayTips = paramDPObject.getString("KeywordExtendTips");
      if (paramDPObject.getString("KeywordExtendUrl") != null)
        this.takeawayUrl = paramDPObject.getString("KeywordExtendUrl");
      publishDataChange(10);
      changeStatus(2);
    }
  }

  public ArrayList<DPObject> bannerList()
  {
    return this.mBannerList;
  }

  public String emptyMsg()
  {
    return this.emptyMsg;
  }

  public String errorMsg()
  {
    return this.errorMsg;
  }

  public String getTakeawayTips()
  {
    return this.takeawayTips;
  }

  public String getTakeawayUrl()
  {
    return this.takeawayUrl;
  }

  public boolean hasSearchDate()
  {
    return this.hasSearchDate;
  }

  public ArrayList<DPObject> hotelTuanInfoList()
  {
    return this.mHotelTuanInfoList;
  }

  public void incLoadMoreCount()
  {
    this.loadMoreCount += 1;
  }

  public boolean isEnd()
  {
    return this.isEnd;
  }

  public boolean isRank()
  {
    return this.isRank;
  }

  public View lastExtraView()
  {
    if ((this.lastResource > 0) && (this.lastListener != null))
    {
      View localView = LayoutInflater.from(this.context).inflate(this.lastResource, null, false);
      localView.setOnClickListener(this.lastListener);
      return localView;
    }
    return this.lastExtraView;
  }

  public int nextStartIndex()
  {
    return this.nextStartIndex;
  }

  public double offsetLatitude()
  {
    return this.offsetLatitude;
  }

  public double offsetLongitude()
  {
    return this.offsetLongitude;
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    this.shops = paramBundle.getParcelableArrayList("shops");
    if (this.shops == null)
      this.shops = new ArrayList();
    ArrayList localArrayList = paramBundle.getParcelableArrayList("banner");
    if ((localArrayList != null) && (localArrayList.size() > 0))
    {
      this.mBannerList.clear();
      this.mBannerList.addAll(localArrayList);
    }
    localArrayList = paramBundle.getParcelableArrayList("hotelTuan");
    if ((localArrayList != null) && (localArrayList.size() > 0))
    {
      this.mHotelTuanInfoList.clear();
      this.mHotelTuanInfoList.addAll(localArrayList);
    }
    this.recordCount = paramBundle.getInt("recordCount");
    this.nextStartIndex = paramBundle.getInt("nextStartIndex");
    this.isEnd = paramBundle.getBoolean("isEnd");
    this.emptyMsg = paramBundle.getString("emptyMsg");
    this.errorMsg = paramBundle.getString("errorMsg");
    this.isRank = paramBundle.getBoolean("isRank");
    this.offsetLatitude = paramBundle.getDouble("offsetLatitude");
    this.offsetLongitude = paramBundle.getDouble("offsetLongitude");
    this.hasSearchDate = paramBundle.getBoolean("hasSearchDate");
    this.takeawayTips = paramBundle.getString("takeawayTips");
    this.takeawayUrl = paramBundle.getString("takeawayUrl");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelableArrayList("shops", this.shops);
    paramBundle.putParcelableArrayList("banner", this.mBannerList);
    paramBundle.putInt("recordCount", this.recordCount);
    paramBundle.putInt("nextStartIndex", this.nextStartIndex);
    paramBundle.putBoolean("isEnd", this.isEnd);
    paramBundle.putString("emptyMsg", this.emptyMsg);
    paramBundle.putString("errorMsg", this.errorMsg);
    paramBundle.putBoolean("isRank", this.isRank);
    paramBundle.putDouble("offsetLatitude", this.offsetLatitude);
    paramBundle.putDouble("offsetLongitude", this.offsetLongitude);
    paramBundle.putBoolean("hasSearchDate", this.hasSearchDate);
    paramBundle.putParcelableArrayList("hotelTuan", this.mHotelTuanInfoList);
    paramBundle.putString("takeawayTips", this.takeawayTips);
    paramBundle.putString("takeawayUrl", this.takeawayUrl);
  }

  public String queryId()
  {
    return "";
  }

  public int recordCount()
  {
    return this.recordCount;
  }

  public void reload(boolean paramBoolean)
  {
    changeStatus(1);
    this.mLoader.loadData(this.nextStartIndex, paramBoolean);
  }

  public void reset(boolean paramBoolean)
  {
    this.nextStartIndex = 0;
    if (paramBoolean)
    {
      this.shops.clear();
      this.isEnd = false;
      this.errorMsg = null;
      this.emptyMsg = null;
    }
  }

  public void setDataLoader(DataSource.DataLoader paramDataLoader)
  {
    this.mLoader = paramDataLoader;
  }

  public void setEmptyMsg(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    this.emptyMsg = paramString;
  }

  public void setError(String paramString)
  {
    this.errorMsg = paramString;
    changeStatus(3);
  }

  public void setHotelCheckinTime(long paramLong)
  {
    this.hotelCheckinTimeMills = paramLong;
  }

  public void setHotelCheckoutTime(long paramLong)
  {
    this.hotelCheckoutTimeMills = paramLong;
  }

  public void setIsRank(boolean paramBoolean)
  {
    this.isRank = paramBoolean;
  }

  public void setLastExtraView(int paramInt, View.OnClickListener paramOnClickListener, Context paramContext)
  {
    this.lastResource = paramInt;
    this.lastListener = paramOnClickListener;
    this.context = paramContext;
  }

  public void setLastExtraView(View paramView)
  {
    this.lastExtraView = paramView;
  }

  public void setOffsetGPS(double paramDouble1, double paramDouble2)
  {
    this.offsetLatitude = paramDouble1;
    this.offsetLongitude = paramDouble2;
    publishDataChange(11);
  }

  public void setShowDistance(boolean paramBoolean)
  {
    this.shouldShowDistance = paramBoolean;
  }

  public ArrayList<DPObject> shops()
  {
    return this.shops;
  }

  public boolean showDistance()
  {
    return this.shouldShowDistance;
  }

  public int startIndex()
  {
    return this.startIndex;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.DefaultShopListDataSource
 * JD-Core Version:    0.6.0
 */