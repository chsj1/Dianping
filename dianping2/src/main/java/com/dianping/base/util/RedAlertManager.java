package com.dianping.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.accountservice.AccountService;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.util.TextUtils;
import com.dianping.util.encrypt.Md5;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class RedAlertManager
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static String FIND_TOPMODULE_EMPTY_FLAG = "FIND_TOPMODULE_EMPTY";
  private static RedAlertManager instance = null;
  public String ACTION_REFRESH_RED_ALERT = "com.dianping.action.RedAlerts";
  private Context mContext;
  private MApiRequest mRedAlertRequest;
  private HashMap<String, DPObject> remoteRedAlertHash = new HashMap();
  private MApiService service;
  private String userId = null;

  private RedAlertManager(Context paramContext)
  {
    if (this.service == null)
      this.service = ((MApiService)DPApplication.instance().getService("mapi"));
    this.mContext = paramContext;
    getRedAlertInfo();
  }

  public static RedAlertManager getInstance()
  {
    monitorenter;
    try
    {
      if (instance == null)
        instance = new RedAlertManager(DPApplication.instance().getApplicationContext());
      RedAlertManager localRedAlertManager = instance;
      return localRedAlertManager;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private DPObject getLocalInfo(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return null;
    return (DPObject)DPCache.getInstance().getParcelable(getUserIdMD5(), paramString, 31539600000L, DPObject.CREATOR);
  }

  private String getUserIdMD5()
  {
    if (!TextUtils.isEmpty(this.userId))
      return this.userId;
    String str2 = Md5.md5(String.valueOf(DPApplication.instance().accountService().id()));
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = "0";
    this.userId = str1;
    return this.userId;
  }

  private long getVersionInLong(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return 0L;
    try
    {
      long l = Long.parseLong(paramString);
      return l;
    }
    catch (NumberFormatException paramString)
    {
      paramString.printStackTrace();
    }
    return 0L;
  }

  private void refreshAllRedAlerts()
  {
    Intent localIntent = new Intent(this.ACTION_REFRESH_RED_ALERT);
    this.mContext.sendBroadcast(localIntent);
  }

  private void saveRedAlertInDay(String paramString, DPObject paramDPObject)
  {
    if ((TextUtils.isEmpty(paramString)) || (paramDPObject == null))
      return;
    DPCache.getInstance().put(getUserIdMD5(), paramString, paramDPObject, 86400000L);
  }

  private void saveRedAlertInPermanent(String paramString, DPObject paramDPObject)
  {
    if ((TextUtils.isEmpty(paramString)) || (paramDPObject == null))
      return;
    DPCache.getInstance().put(getUserIdMD5(), paramString, paramDPObject, 31539600000L);
  }

  public boolean checkLocalRedAlertByTag(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
      return false;
    while (DPCache.getInstance().getParcelable(getUserIdMD5(), paramString, 31539600000L, DPObject.CREATOR) != null);
    return true;
  }

  public String checkRedAlertByTag(String paramString)
  {
    DPObject localDPObject = null;
    Object localObject;
    if ((paramString.equalsIgnoreCase("find.topmodule")) && (this.remoteRedAlertHash.get(paramString) == null))
      localObject = FIND_TOPMODULE_EMPTY_FLAG;
    String[] arrayOfString;
    do
    {
      do
      {
        do
        {
          do
          {
            do
            {
              return localObject;
              localObject = localDPObject;
            }
            while (TextUtils.isEmpty(paramString));
            localObject = localDPObject;
          }
          while (this.remoteRedAlertHash == null);
          localObject = localDPObject;
        }
        while (this.remoteRedAlertHash.get(paramString) == null);
        localObject = (DPObject)this.remoteRedAlertHash.get(paramString);
        paramString = getLocalInfo(paramString);
        if ((paramString == null) || (getVersionInLong(paramString.getString("Version")) < getVersionInLong(((DPObject)localObject).getString("Version"))))
        {
          localObject = ((DPObject)localObject).getString("ShowText");
          paramString = (String)localObject;
          if (TextUtils.isEmpty((CharSequence)localObject))
            paramString = "";
          return paramString;
        }
        arrayOfString = ((DPObject)localObject).getStringArray("Depends");
        localObject = localDPObject;
      }
      while (arrayOfString == null);
      localObject = localDPObject;
    }
    while (arrayOfString.length == 0);
    paramString = null;
    int i = 0;
    while (true)
    {
      localObject = paramString;
      if (i >= arrayOfString.length)
        break;
      localObject = getLocalInfo(arrayOfString[i]);
      localDPObject = (DPObject)this.remoteRedAlertHash.get(arrayOfString[i]);
      if ((localObject == null) || (getVersionInLong(((DPObject)localObject).getString("Version")) < getVersionInLong(localDPObject.getString("Version"))))
      {
        paramString = localDPObject.getString("ShowText");
        localObject = paramString;
        if (!TextUtils.isEmpty(paramString))
          break;
        return "";
      }
      i += 1;
    }
  }

  public long getDiffOfVersionByTag(String paramString)
  {
    if ((TextUtils.isEmpty(paramString)) || (this.remoteRedAlertHash == null) || (this.remoteRedAlertHash.get(paramString) == null))
      return 0L;
    DPObject localDPObject = (DPObject)this.remoteRedAlertHash.get(paramString);
    paramString = getLocalInfo(paramString);
    if (paramString == null)
      return getVersionInLong(localDPObject.getString("Version"));
    return getVersionInLong(localDPObject.getString("Version")) - getVersionInLong(paramString.getString("Version"));
  }

  public void getRedAlertInfo()
  {
    if (this.mRedAlertRequest != null)
      this.service.abort(this.mRedAlertRequest, this, true);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/framework/getredalerts.bin?").buildUpon();
    City localCity = DPApplication.instance().cityConfig().currentCity();
    if (localCity != null)
      localBuilder.appendQueryParameter("cityid", localCity.id() + "");
    this.mRedAlertRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    this.service.exec(this.mRedAlertRequest, this);
  }

  public int getRedAlertNumByTag(String paramString)
  {
    if ((TextUtils.isEmpty(paramString)) || (this.remoteRedAlertHash == null) || (this.remoteRedAlertHash.get(paramString) == null))
      return 0;
    return ((DPObject)this.remoteRedAlertHash.get(paramString)).getInt("ShowCount");
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRedAlertRequest)
      this.mRedAlertRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRedAlertRequest)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject[])((DPObject)paramMApiResponse.result()).getArray("List");
        this.remoteRedAlertHash.clear();
        if (paramMApiRequest != null)
        {
          paramMApiRequest = new ArrayList(Arrays.asList(paramMApiRequest)).iterator();
          while (paramMApiRequest.hasNext())
          {
            paramMApiResponse = (DPObject)paramMApiRequest.next();
            this.remoteRedAlertHash.put(paramMApiResponse.getString("TagId"), paramMApiResponse);
          }
        }
      }
      refreshAllRedAlerts();
    }
    this.mRedAlertRequest = null;
  }

  public void reloadRedAlertInfo()
  {
    this.userId = "";
    getRedAlertInfo();
  }

  public void updateLocalRedAlert(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    DPCache.getInstance().put(getUserIdMD5(), paramString, new DPObject(), 31539600000L);
  }

  public void updateRedAlert(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    updateRedAlert(paramDPObject.getString("TagId"));
  }

  public void updateRedAlert(String paramString)
  {
    if ((TextUtils.isEmpty(paramString)) || (this.remoteRedAlertHash.get(paramString) == null))
      return;
    saveRedAlertInPermanent(paramString, (DPObject)this.remoteRedAlertHash.get(paramString));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.RedAlertManager
 * JD-Core Version:    0.6.0
 */