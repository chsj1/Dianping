package com.dianping.hui.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;

public class HuiPreProcessDataSource
  implements IHuiDataSource
{
  private static final String URL_PRE_PROCESS = "http://hui.api.dianping.com/huipreprocess.hui";
  private NovaActivity mActivity;
  public MApiRequest mPreProcessReq;
  public HuiPreProcessRequestListener mPreProcessRequestListener;
  private FullRequestHandle<MApiRequest, MApiResponse> mapiHandler = new HuiPreProcessDataSource.1(this);
  public String requestParams;
  public String shopName;

  public HuiPreProcessDataSource(NovaActivity paramNovaActivity)
  {
    this.mActivity = paramNovaActivity;
  }

  public void releaseRequests()
  {
    if (this.mPreProcessReq != null)
    {
      this.mActivity.mapiService().abort(this.mPreProcessReq, this.mapiHandler, true);
      this.mPreProcessReq = null;
    }
  }

  public void restoreData(Bundle paramBundle)
  {
    this.shopName = paramBundle.getString("shopname");
    this.requestParams = paramBundle.getString("requestParams");
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("shopname", this.shopName);
    paramBundle.putString("requestParams", this.requestParams);
  }

  public void sendPreProcessRequest(String paramString)
  {
    this.mPreProcessReq = BasicMApiRequest.mapiGet(Uri.parse("http://hui.api.dianping.com/huipreprocess.hui" + paramString).buildUpon().toString(), CacheType.DISABLED);
    this.mActivity.mapiService().exec(this.mPreProcessReq, this.mapiHandler);
  }

  public void setHuiPreProcessRequestListener(HuiPreProcessRequestListener paramHuiPreProcessRequestListener)
  {
    this.mPreProcessRequestListener = paramHuiPreProcessRequestListener;
  }

  public static abstract interface HuiPreProcessRequestListener
  {
    public abstract void requestPreProcessComplete(HuiMapiStatus paramHuiMapiStatus, Message paramMessage);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiPreProcessDataSource
 * JD-Core Version:    0.6.0
 */