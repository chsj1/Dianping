package com.dianping.takeaway.entity;

import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.takeaway.util.TakeawayHistoryAddressManager;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.List;

public class TakeawayAddressDataSource
{
  private static final int MAX_SEARCHED_HISTORY_SIZE = 10;
  public final String LOCATE_FAILED_TIP = "无法获取您当前的位置";
  public final int TYPE_INPUT = 1;
  public final int TYPE_SELECT = 2;
  private NovaActivity activity;
  public TakeawayHistoryAddressManager addressManager;
  public boolean canClick = true;
  private DataLoadListener dataLoadListener;
  public List<TakeawayAddress> historyList = new ArrayList();
  public TakeawayAddress locateAddress;
  private MApiRequest locateRequest;
  public boolean permitSuggest = true;
  public String queryId;
  public String shopId;
  public String source;
  public List<TakeawayAddress> suggestList = new ArrayList();
  private MApiRequest suggestRequest;
  private MApiRequest validateRequest;

  public TakeawayAddressDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    this.source = paramNovaActivity.getStringParam("source");
    this.shopId = paramNovaActivity.getStringParam("shopid");
    this.queryId = paramNovaActivity.getStringParam("queryid");
    this.addressManager = new TakeawayHistoryAddressManager("takeaway_searched_address");
    this.historyList = this.addressManager.parseAddressFile();
  }

  public void addAddress(TakeawayAddress paramTakeawayAddress)
  {
    if ((paramTakeawayAddress.lat < 0.0D) && (paramTakeawayAddress.lng < 0.0D))
      return;
    int i = 0;
    while (true)
    {
      if (i < this.historyList.size())
      {
        if (((TakeawayAddress)this.historyList.get(i)).address.equals(paramTakeawayAddress.address))
          this.historyList.remove(i);
      }
      else
        while (this.historyList.size() >= 10)
          this.historyList.remove(0);
      i += 1;
    }
    this.historyList.add(paramTakeawayAddress);
    this.addressManager.writeAddressFile(this.historyList);
  }

  public NovaActivity getActivity()
  {
    return this.activity;
  }

  public GAUserInfo getGAUserInfo()
  {
    return new GAUserInfo();
  }

  public boolean isLoacting()
  {
    return this.locateRequest != null;
  }

  public void onDestroy()
  {
    this.addressManager.writeAddressFile(this.historyList);
    if (this.locateRequest != null)
    {
      this.activity.mapiService().abort(this.locateRequest, null, true);
      this.locateRequest = null;
    }
    if (this.suggestRequest != null)
    {
      this.activity.mapiService().abort(this.suggestRequest, null, true);
      this.suggestRequest = null;
    }
    if (this.validateRequest != null)
    {
      this.activity.mapiService().abort(this.validateRequest, null, true);
      this.validateRequest = null;
    }
  }

  public void sendLocateRequest()
  {
    if (this.locateRequest != null)
      this.canClick = true;
    Location localLocation;
    while (true)
    {
      return;
      localLocation = this.activity.location();
      if (localLocation != null)
        break;
      this.canClick = true;
      if (this.dataLoadListener == null)
        continue;
      this.dataLoadListener.loadFinish(DataStatus.ERROR_LOCATE, null);
      return;
    }
    this.locateRequest = BasicMApiRequest.mapiGet(String.format("%slat=%s&lng=%s&source=%s", new Object[] { "http://waimai.api.dianping.com/getlocation.ta?", String.valueOf(localLocation.latitude()), String.valueOf(localLocation.longitude()), this.source }), CacheType.DISABLED);
    this.activity.mapiService().exec(this.locateRequest, new FullRequestHandle()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (TakeawayAddressDataSource.this.dataLoadListener != null)
          TakeawayAddressDataSource.this.dataLoadListener.locateFinsh(TakeawayNetLoadStatus.STATUS_FAILED, null);
        TakeawayAddressDataSource.this.canClick = true;
        TakeawayAddressDataSource.access$102(TakeawayAddressDataSource.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiResponse != null)
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (TakeawayAddressDataSource.this.dataLoadListener != null)
            TakeawayAddressDataSource.this.dataLoadListener.locateFinsh(TakeawayNetLoadStatus.STATUS_SUCCESS, paramMApiRequest);
        }
        TakeawayAddressDataSource.access$102(TakeawayAddressDataSource.this, null);
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        if (TakeawayAddressDataSource.this.dataLoadListener != null)
          TakeawayAddressDataSource.this.dataLoadListener.locateFinsh(TakeawayNetLoadStatus.STATUS_START, null);
      }
    });
  }

  public void sendSuggestRequest(String paramString)
  {
    if (this.suggestRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("address");
    localArrayList.add(paramString);
    paramString = this.activity.location();
    if (paramString != null)
    {
      localArrayList.add("locatecityid");
      localArrayList.add(String.valueOf(paramString.city().id()));
    }
    this.suggestRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/addresssuggest.ta?", (String[])localArrayList.toArray(new String[0]));
    this.activity.mapiService().exec(this.suggestRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (TakeawayAddressDataSource.this.dataLoadListener != null)
          TakeawayAddressDataSource.this.dataLoadListener.loadSuggestFinsh(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse);
        TakeawayAddressDataSource.access$302(TakeawayAddressDataSource.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiResponse != null)
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if ((paramMApiRequest != null) && (TakeawayAddressDataSource.this.dataLoadListener != null))
            TakeawayAddressDataSource.this.dataLoadListener.loadSuggestFinsh(TakeawayNetLoadStatus.STATUS_SUCCESS, paramMApiRequest);
        }
        TakeawayAddressDataSource.access$302(TakeawayAddressDataSource.this, null);
      }
    });
  }

  public void sendValidateRequest(int paramInt, TakeawayAddress paramTakeawayAddress)
  {
    if (this.validateRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("source");
    localArrayList.add(this.source);
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("type");
    localArrayList.add(String.valueOf(paramInt));
    if (!TextUtils.isEmpty(this.shopId))
    {
      localArrayList.add("shopid");
      localArrayList.add(this.shopId);
    }
    if (paramInt == 2)
    {
      localArrayList.add("lat");
      localArrayList.add(String.valueOf(paramTakeawayAddress.lat));
      localArrayList.add("lng");
      localArrayList.add(String.valueOf(paramTakeawayAddress.lng));
    }
    Location localLocation = this.activity.location();
    if (localLocation != null)
    {
      localArrayList.add("locatecityid");
      localArrayList.add(String.valueOf(localLocation.city().id()));
    }
    localArrayList.add("address");
    localArrayList.add(paramTakeawayAddress.address);
    this.validateRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/parseaddress.ta?", (String[])localArrayList.toArray(new String[0]));
    this.activity.mapiService().exec(this.validateRequest, new FullRequestHandle()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (TakeawayAddressDataSource.this.dataLoadListener != null)
        {
          TakeawayAddressDataSource.this.dataLoadListener.validateAddressFinsh(TakeawayNetLoadStatus.STATUS_FINISH_BEFORE, null);
          TakeawayAddressDataSource.this.dataLoadListener.validateAddressFinsh(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse);
        }
        TakeawayAddressDataSource.this.permitSuggest = true;
        TakeawayAddressDataSource.access$202(TakeawayAddressDataSource.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (TakeawayAddressDataSource.this.dataLoadListener != null)
          TakeawayAddressDataSource.this.dataLoadListener.validateAddressFinsh(TakeawayNetLoadStatus.STATUS_FINISH_BEFORE, null);
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (TakeawayAddressDataSource.this.dataLoadListener != null)
            TakeawayAddressDataSource.this.dataLoadListener.validateAddressFinsh(TakeawayNetLoadStatus.STATUS_SUCCESS, paramMApiRequest);
        }
        TakeawayAddressDataSource.this.permitSuggest = true;
        TakeawayAddressDataSource.access$202(TakeawayAddressDataSource.this, null);
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        if (TakeawayAddressDataSource.this.dataLoadListener != null)
          TakeawayAddressDataSource.this.dataLoadListener.validateAddressFinsh(TakeawayNetLoadStatus.STATUS_START, null);
      }
    });
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadListener = paramDataLoadListener;
  }

  public static abstract interface DataLoadListener
  {
    public abstract void loadFinish(TakeawayAddressDataSource.DataStatus paramDataStatus, Object paramObject);

    public abstract void loadSuggestFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);

    public abstract void locateFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);

    public abstract void validateAddressFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);
  }

  public static enum DataStatus
  {
    static
    {
      ERROR_LOCATE = new DataStatus("ERROR_LOCATE", 1);
      $VALUES = new DataStatus[] { ERROR_NETWORK, ERROR_LOCATE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayAddressDataSource
 * JD-Core Version:    0.6.0
 */