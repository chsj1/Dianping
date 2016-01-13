package com.dianping.mall.nearby;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.DataSource.DataLoader;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MallNearByDataSource extends BaseShopListDataSource
  implements DataSource.DataLoader
{
  private static final DPObject DEFAULT_FLOOR;
  private static final DPObject DEFAULT_FOOD_CATEGORY = new DPObject("Pair").edit().putInt("ID", 10).putString("Name", "全部美食").generate();
  private static final DPObject DEFAULT_SHOPPING_CATEGORY = new DPObject("Pair").edit().putInt("ID", 20).putString("Name", "全部购物").generate();
  private static final DPObject DEFAULT_SORT;
  protected NovaActivity activity;
  private String branchName;
  private int categoryType;
  private DPObject curFloor;
  DataLoadListener dataLoadListener;
  public DataStatus dataStatus = DataStatus.NORMAL;
  private String defaultPic;
  private String distance;
  private DPObject[] floorNavs;
  public int geoType = 1;
  public Handler handler;
  public String lat;
  public String lng;
  private int mallId;
  protected RequestHandler<MApiRequest, MApiResponse> mapiHandler = new MallNearByDataSource.2(this);
  public String noShopNotiDetail;
  public String noShopNotiTitle;
  public Handler retryHandler = new MallNearByDataSource.1(this);
  private String shopName;
  protected MApiRequest shopRequest;

  static
  {
    DEFAULT_FLOOR = new DPObject("Pair").edit().putString("ID", "-1").putString("Name", "全部楼层").generate();
    DEFAULT_SORT = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "推荐排序").generate();
  }

  public MallNearByDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    setDataLoader(this);
  }

  private void dealEmptyMsg(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (!paramString.contains("|"))
        break label44;
      paramString = paramString.split("\\|");
      if (paramString.length == 2)
      {
        this.noShopNotiTitle = paramString[0];
        this.noShopNotiDetail = paramString[1];
      }
    }
    return;
    label44: this.noShopNotiTitle = paramString;
    this.noShopNotiDetail = "";
  }

  protected MApiRequest createRequest(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    Object localObject = this.activity.location();
    if (localObject != null)
    {
      localArrayList.add("locatecityid");
      localArrayList.add(String.valueOf(((Location)localObject).city().id()));
    }
    if (this.mallId > 0)
    {
      localArrayList.add("mallid");
      localArrayList.add(this.mallId + "");
    }
    int i;
    if ((!TextUtils.isEmpty(this.lat)) && (!TextUtils.isEmpty(this.lng)))
    {
      localArrayList.add("latitude");
      localArrayList.add(this.lat);
      localArrayList.add("longitude");
      localArrayList.add(this.lng);
      if (curCategory() != null)
        break label404;
      i = 0;
      label167: if (i > 0)
      {
        localArrayList.add("categoryid");
        localArrayList.add(i + "");
      }
      if (curFloor() != null)
        break label418;
      localObject = null;
      label213: if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localArrayList.add("floor");
        localArrayList.add(localObject);
      }
      if (curSort() != null)
        break label431;
    }
    label404: label418: label431: for (localObject = null; ; localObject = curSort().getString("ID"))
    {
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localArrayList.add("sortid");
        localArrayList.add(localObject);
      }
      localArrayList.add("startindex");
      localArrayList.add(String.valueOf(paramInt));
      localArrayList.add("geotype");
      localArrayList.add(String.valueOf(this.geoType));
      localObject = this.activity.accountService().token();
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localArrayList.add("token");
        localArrayList.add(localObject);
      }
      return BasicMApiRequest.mapiPost("http://mapi.dianping.com/shopping/getshoplist.bin?", (String[])localArrayList.toArray(new String[0]));
      if (localObject == null)
        break;
      DecimalFormat localDecimalFormat = Location.FMT;
      localArrayList.add("latitude");
      localArrayList.add(localDecimalFormat.format(((Location)localObject).latitude()));
      localArrayList.add("longitude");
      localArrayList.add(localDecimalFormat.format(((Location)localObject).longitude()));
      break;
      i = curCategory().getInt("ID");
      break label167;
      localObject = curFloor().getString("Name");
      break label213;
    }
  }

  public DPObject curFloor()
  {
    return this.curFloor;
  }

  public DPObject[] floorNavs()
  {
    return this.floorNavs;
  }

  public DPObject generateFilter()
  {
    return new DPObject().edit().putObject("curCategory", curCategory()).putObject("curSort", curSort()).putObject("curFloor", curFloor()).generate();
  }

  public String getBranchName()
  {
    return this.branchName;
  }

  public String getDefaultPic()
  {
    return this.defaultPic;
  }

  public String getDistance()
  {
    return this.distance;
  }

  public String getShopName()
  {
    return this.shopName;
  }

  public void initFilter(DPObject paramDPObject, String paramString1, String paramString2, String paramString3)
  {
    if (paramDPObject != null)
    {
      setCurCategory(paramDPObject.getObject("curCategory"));
      setCurSort(paramDPObject.getObject("curSort"));
      setCurFloor(paramDPObject.getObject("curFloor"));
    }
    do
    {
      return;
      if (!TextUtils.isEmpty(paramString1))
        setCurCategory(new DPObject("Pair").edit().putString("ID", paramString1).putString("Name", "").generate());
      if (TextUtils.isEmpty(paramString2))
        continue;
      setCurSort(new DPObject("Pair").edit().putString("ID", paramString2).putString("Name", "").generate());
    }
    while (TextUtils.isEmpty(paramString3));
    setCurFloor(new DPObject("Pair").edit().putString("ID", paramString2).putString("Name", "").generate());
  }

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.shopRequest != null)
      return;
    this.shopRequest = createRequest(paramInt);
    if (paramBoolean)
      this.activity.mapiCacheService().remove(this.shopRequest);
    this.activity.mapiService().exec(this.shopRequest, this.mapiHandler);
    changeStatus(1);
  }

  public void onDestroy()
  {
    if (this.shopRequest != null)
    {
      this.activity.mapiService().abort(this.shopRequest, null, true);
      this.shopRequest = null;
    }
    this.retryHandler.removeMessages(1);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    initFilter((DPObject)paramBundle.getParcelable("shopfilter"), "", "", "");
    this.lat = paramBundle.getString("lat");
    this.lng = paramBundle.getString("lng");
    this.geoType = paramBundle.getInt("geotype");
    this.mallId = paramBundle.getInt("mallId");
    this.categoryType = paramBundle.getInt("categorytype");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("shopfilter", generateFilter());
    paramBundle.putString("lat", this.lat);
    paramBundle.putString("lng", this.lng);
    paramBundle.putInt("geotype", this.geoType);
    paramBundle.putInt("mallId", this.mallId);
    paramBundle.putInt("categorytype", this.categoryType);
  }

  public void setCategoryType(int paramInt)
  {
    this.categoryType = paramInt;
  }

  public boolean setCurFloor(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (this.curFloor == null))
    {
      this.curFloor = paramDPObject;
      return true;
    }
    if (paramDPObject.equals(this.curFloor))
      return false;
    this.curFloor = paramDPObject;
    return true;
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadListener = paramDataLoadListener;
  }

  public void setDefaultFilter()
  {
    if (this.categoryType == 20)
      setCurCategory(DEFAULT_SHOPPING_CATEGORY);
    while (true)
    {
      setCurFloor(DEFAULT_FLOOR);
      setCurSort(DEFAULT_SORT);
      return;
      setCurCategory(DEFAULT_FOOD_CATEGORY);
    }
  }

  public void setLoadingDataStatus()
  {
    changeStatus(1);
  }

  public void setMallId(int paramInt)
  {
    this.mallId = paramInt;
  }

  public void updateNavs(DPObject paramDPObject)
  {
    String str = paramDPObject.getString("CurrentFloor");
    if (paramDPObject.getStringArray("FloorNavs") != null)
    {
      String[] arrayOfString = paramDPObject.getStringArray("FloorNavs");
      this.floorNavs = new DPObject[arrayOfString.length];
      int i = 0;
      while (i < arrayOfString.length)
      {
        if (TextUtils.isEmpty(arrayOfString[i]))
        {
          i += 1;
          continue;
        }
        if ((!TextUtils.isEmpty(str)) && (str.equals("全部楼层")))
        {
          this.curFloor = new DPObject("Pair").edit().putString("ID", "-1").putString("Name", paramDPObject.getString("CurrentFloor")).generate();
          label128: if (!arrayOfString[i].equals("全部楼层"))
            break label275;
        }
        label275: for (DPObject localDPObject = new DPObject("Pair").edit().putString("ID", "-1").putString("Name", arrayOfString[i]).generate(); ; localDPObject = new DPObject("Pair").edit().putString("ID", i + "").putString("Name", arrayOfString[i]).generate())
        {
          this.floorNavs[i] = localDPObject;
          break;
          if ((TextUtils.isEmpty(str)) || (!str.equals(arrayOfString[i])))
            break label128;
          this.curFloor = new DPObject("Pair").edit().putString("ID", i + "").putString("Name", paramDPObject.getString("CurrentFloor")).generate();
          break label128;
        }
      }
    }
    super.updateNavs(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.mall.nearby.MallNearByDataSource
 * JD-Core Version:    0.6.0
 */