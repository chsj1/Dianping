package com.dianping.takeaway.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import java.util.ArrayList;
import java.util.List;

public class TakeawayPoiDataSource
{
  private NovaActivity activity;
  private DataLoadListener dataLoadListener;
  private MApiRequest getSuggestPoiRequest;
  public boolean isLocal = true;
  public String keyword = "";
  public List<DPObject> poiList = new ArrayList();

  public TakeawayPoiDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
  }

  public void getSuggestPoiTask(String paramString)
  {
    if (this.getSuggestPoiRequest != null)
      this.activity.mapiService().abort(this.getSuggestPoiRequest, null, true);
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/getsuggestpoilist.ta").buildUpon();
    Location localLocation = this.activity.location();
    int i;
    if (localLocation == null)
      i = 1;
    while ((paramString == null) || (TextUtils.isEmpty(paramString.trim())))
    {
      if (i != 0)
      {
        return;
        i = 0;
        continue;
      }
      localBuilder.appendQueryParameter("lat", String.valueOf(localLocation.latitude()));
      localBuilder.appendQueryParameter("lng", String.valueOf(localLocation.longitude()));
    }
    for (this.isLocal = true; ; this.isLocal = false)
    {
      this.getSuggestPoiRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      this.activity.mapiService().exec(this.getSuggestPoiRequest, new RequestHandler()
      {
        public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          if (TakeawayPoiDataSource.this.dataLoadListener != null)
            TakeawayPoiDataSource.this.dataLoadListener.loadPoiFinsh(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse);
          TakeawayPoiDataSource.access$102(TakeawayPoiDataSource.this, null);
        }

        public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          if ((paramMApiResponse != null) && (TakeawayPoiDataSource.this.dataLoadListener != null))
            TakeawayPoiDataSource.this.dataLoadListener.loadPoiFinsh(TakeawayNetLoadStatus.STATUS_SUCCESS, paramMApiResponse.result());
          TakeawayPoiDataSource.access$102(TakeawayPoiDataSource.this, null);
        }
      });
      return;
      localBuilder.appendQueryParameter("keyword", paramString);
      localBuilder.appendQueryParameter("cityid", String.valueOf(this.activity.cityId()));
      if (i != 0)
        continue;
      localBuilder.appendQueryParameter("locatecityid", String.valueOf(localLocation.city().id()));
    }
  }

  public void onDestroy()
  {
    if (this.getSuggestPoiRequest != null)
    {
      this.activity.mapiService().abort(this.getSuggestPoiRequest, null, true);
      this.getSuggestPoiRequest = null;
    }
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadListener = paramDataLoadListener;
  }

  public static abstract interface DataLoadListener
  {
    public abstract void loadPoiFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayPoiDataSource
 * JD-Core Version:    0.6.0
 */