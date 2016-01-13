package com.dianping.takeaway.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DeviceUtils;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class TakeawayMainDataSource extends TakeawaySampleShoplistDataSource
{
  private boolean centerRefresh = false;
  public EntryParser iconParser = new EntryParser();
  public MyTakeaway myTakeaway = new MyTakeaway();
  private TaRecentOrderLoadListener recentOrderLoadListener;
  private Handler refreshHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (TakeawayMainDataSource.this.centerRefresh)
        TakeawayMainDataSource.this.loadRecentOrder(false);
    }
  };
  private MApiRequest taOrderRequest;

  public TakeawayMainDataSource(NovaActivity paramNovaActivity)
  {
    super(paramNovaActivity);
  }

  private void loadRecentOrder(boolean paramBoolean)
  {
    if ((!this.isShopListPage) || (!this.centerRefresh));
    while (true)
    {
      return;
      if (this.taOrderRequest == null)
        break;
      if (!paramBoolean)
        continue;
      this.activity.mapiService().abort(this.taOrderRequest, this, true);
      this.taOrderRequest = null;
    }
    if ((TextUtils.isEmpty(this.activity.accountService().token())) && (TextUtils.isEmpty(DeviceUtils.dpid())))
    {
      this.refreshHandler.sendMessageDelayed(this.refreshHandler.obtainMessage(), 30000L);
      return;
    }
    this.taOrderRequest = BasicMApiRequest.mapiGet(Uri.parse("http://waimai.api.dianping.com/myuncompleteorder.ta").buildUpon().appendQueryParameter("token", this.activity.accountService().token()).appendQueryParameter("uuid", Environment.uuid()).toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.taOrderRequest, this);
    this.refreshHandler.sendMessageDelayed(this.refreshHandler.obtainMessage(), 30000L);
  }

  protected MApiRequest createShopRequest(int paramInt)
  {
    Object localObject = null;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    Location localLocation = this.activity.location();
    int i;
    if ((!TextUtils.isEmpty(this.lat)) && (!TextUtils.isEmpty(this.lng)))
    {
      localArrayList.add("lat");
      localArrayList.add(this.lat);
      localArrayList.add("lng");
      localArrayList.add(this.lng);
      if (localLocation != null)
      {
        localArrayList.add("locatecityid");
        localArrayList.add(String.valueOf(localLocation.city().id()));
      }
      if (!TextUtils.isEmpty(this.curAddress))
      {
        localArrayList.add("address");
        localArrayList.add(this.curAddress);
      }
      if (!TextUtils.isEmpty(this.searchKey))
      {
        localArrayList.add("keyword");
        localArrayList.add(this.searchKey);
      }
      if (curCategory() != null)
        break label421;
      i = 0;
      label183: if (i != 0)
      {
        localArrayList.add("categoryid");
        localArrayList.add(String.valueOf(i));
      }
      if (curSort() != null)
        break label436;
      localObject = null;
      label214: if (localObject != null)
      {
        localArrayList.add("sortid");
        localArrayList.add(localObject);
      }
      if (curMultiFilterIds() != null)
        break label450;
    }
    label421: label436: label450: for (localObject = ""; ; localObject = curMultiFilterIds())
    {
      localArrayList.add("multifilterids");
      localArrayList.add(localObject);
      localArrayList.add("start");
      localArrayList.add(String.valueOf(paramInt));
      localArrayList.add("geotype");
      localArrayList.add(String.valueOf(this.geoType));
      localObject = this.activity.accountService().token();
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localArrayList.add("token");
        localArrayList.add(localObject);
      }
      if (!TextUtils.isEmpty(this.extraInfo))
      {
        localArrayList.add("shopSuggestInfo");
        localArrayList.add(this.extraInfo);
      }
      localObject = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/shoplist.ta?", (String[])localArrayList.toArray(new String[0]));
      do
        return localObject;
      while (localLocation == null);
      localObject = Location.FMT;
      localArrayList.add("lat");
      localArrayList.add(((NumberFormat)localObject).format(localLocation.latitude()));
      localArrayList.add("lng");
      localArrayList.add(((NumberFormat)localObject).format(localLocation.longitude()));
      break;
      i = curCategory().getInt("ID");
      break label183;
      localObject = curSort().getString("ID");
      break label214;
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.taOrderRequest != null)
    {
      this.activity.mapiService().abort(this.taOrderRequest, null, true);
      this.taOrderRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFailed(paramMApiRequest, paramMApiResponse);
    if (paramMApiRequest == this.taOrderRequest)
    {
      this.taOrderRequest = null;
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (this.recentOrderLoadListener != null))
        this.recentOrderLoadListener.loadCenterOrderFinish(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse.message().content());
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    if (paramMApiRequest == this.taOrderRequest)
    {
      this.taOrderRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest != null)
        {
          this.myTakeaway.orderStatusStr = paramMApiRequest.getString("OrderStatusStr");
          this.myTakeaway.shopName = paramMApiRequest.getString("ShopName");
          this.myTakeaway.orderTime = paramMApiRequest.getString("OrderTime");
          this.myTakeaway.orderViewId = paramMApiRequest.getString("OrderViewId");
          if (this.recentOrderLoadListener != null)
            this.recentOrderLoadListener.loadCenterOrderFinish(TakeawayNetLoadStatus.STATUS_SUCCESS, this.myTakeaway);
        }
      }
    }
  }

  protected void parseRestObj(DPObject paramDPObject)
  {
    DPObject[] arrayOfDPObject = paramDPObject.getArray("TaEntranceList");
    paramDPObject = arrayOfDPObject;
    if (arrayOfDPObject == null)
      paramDPObject = new DPObject[0];
    int i = paramDPObject.length;
    if (i < 4)
    {
      this.iconParser.perLineIconNum = 0;
      this.iconParser.lineNum = 0;
    }
    while (true)
    {
      this.iconParser.perPageIconNum = (this.iconParser.perLineIconNum * this.iconParser.lineNum);
      this.iconParser.parseIconObjs(paramDPObject);
      return;
      if (i == 4)
      {
        this.iconParser.perLineIconNum = 4;
        this.iconParser.lineNum = 1;
        continue;
      }
      this.iconParser.perLineIconNum = 4;
      this.iconParser.lineNum = 2;
    }
  }

  public void pauseCenter()
  {
    this.centerRefresh = false;
    this.refreshHandler.removeCallbacksAndMessages(null);
  }

  public void resumeCenter()
  {
    this.centerRefresh = true;
    loadRecentOrder(true);
  }

  public void setTaRecentOrderLoadListener(TaRecentOrderLoadListener paramTaRecentOrderLoadListener)
  {
    this.recentOrderLoadListener = paramTaRecentOrderLoadListener;
  }

  public static class EntryParser
  {
    public static final DPObject EMPTY_ITEM = new DPObject();
    public int curPagerPosition = 1;
    private List<DPObject[]> iconObjs = new ArrayList();
    public int lineNum;
    public List<DPObject[]> pagerIconObjs = new ArrayList();
    public int perLineIconNum;
    public int perPageIconNum;

    private DPObject[] getSubArray(DPObject[] paramArrayOfDPObject, int paramInt1, int paramInt2, int paramInt3)
    {
      Object localObject = null;
      if (paramArrayOfDPObject == null);
      int i;
      DPObject[] arrayOfDPObject;
      do
      {
        do
          return localObject;
        while (paramInt1 > paramInt2);
        i = paramInt2;
        if (paramInt2 > paramArrayOfDPObject.length - 1)
          i = paramArrayOfDPObject.length - 1;
        arrayOfDPObject = new DPObject[paramInt3];
        paramInt2 = paramInt1;
        while (paramInt2 <= i)
        {
          arrayOfDPObject[(paramInt2 - paramInt1)] = paramArrayOfDPObject[paramInt2];
          paramInt2 += 1;
        }
        paramInt3 -= i - paramInt1 + 1;
        localObject = arrayOfDPObject;
      }
      while (paramInt3 <= 0);
      paramInt2 = 1;
      while (true)
      {
        localObject = arrayOfDPObject;
        if (paramInt2 > paramInt3)
          break;
        arrayOfDPObject[(i - paramInt1 + paramInt2)] = EMPTY_ITEM;
        paramInt2 += 1;
      }
    }

    private void reParseIconObjs()
    {
      if (this.iconObjs.size() == 1)
      {
        this.pagerIconObjs.addAll(this.iconObjs);
        return;
      }
      this.pagerIconObjs.add(this.iconObjs.get(this.iconObjs.size() - 1));
      this.pagerIconObjs.addAll(this.iconObjs);
      this.pagerIconObjs.add(this.iconObjs.get(0));
    }

    protected void parseIconObjs(DPObject[] paramArrayOfDPObject)
    {
      this.iconObjs.clear();
      this.pagerIconObjs.clear();
      if (this.perPageIconNum == 0);
      do
      {
        return;
        int j = (int)Math.ceil(paramArrayOfDPObject.length / this.perPageIconNum);
        int i = 0;
        while (i < j)
        {
          DPObject[] arrayOfDPObject = getSubArray(paramArrayOfDPObject, this.perPageIconNum * i, (i + 1) * this.perPageIconNum - 1, this.perPageIconNum);
          if (arrayOfDPObject != null)
            this.iconObjs.add(arrayOfDPObject);
          i += 1;
        }
      }
      while (this.iconObjs.size() <= 0);
      reParseIconObjs();
    }
  }

  public static class MyTakeaway
  {
    public String orderStatusStr;
    public String orderTime;
    public String orderViewId;
    public String shopName;
  }

  public static abstract interface TaRecentOrderLoadListener
  {
    public abstract void loadCenterOrderFinish(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayMainDataSource
 * JD-Core Version:    0.6.0
 */