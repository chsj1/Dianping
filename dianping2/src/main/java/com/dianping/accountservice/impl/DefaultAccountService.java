package com.dianping.accountservice.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.util.LoginUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.List<Lorg.apache.http.NameValuePair;>;
import org.apache.http.NameValuePair;

public class DefaultAccountService extends BaseAccountService
{
  private final ArrayList<AccountListener> listeners = new ArrayList();
  private LoginResultListener loginResultListener;

  public DefaultAccountService(Context paramContext)
  {
    super(paramContext);
  }

  private SharedPreferences preferences(Context paramContext)
  {
    return paramContext.getSharedPreferences(paramContext.getPackageName(), 3);
  }

  public void addListener(AccountListener paramAccountListener)
  {
    if (paramAccountListener != null)
      this.listeners.add(paramAccountListener);
  }

  public void cancelLogin()
  {
    if (this.loginResultListener != null)
    {
      this.loginResultListener.onLoginCancel(this);
      this.loginResultListener = null;
    }
  }

  protected void dispatchAccountChanged()
  {
    if ((this.loginResultListener != null) && (token() != null))
    {
      this.loginResultListener.onLoginSuccess(this);
      this.loginResultListener = null;
    }
    Object localObject1 = this.listeners.iterator();
    while (((Iterator)localObject1).hasNext())
      ((AccountListener)((Iterator)localObject1).next()).onAccountChanged(this);
    localObject1 = getWiWideSharedPreferences(this.context);
    Object localObject2 = profile();
    if (localObject2 != null)
    {
      localObject2 = ((DPObject)localObject2).getString("PhoneNo");
      ((SharedPreferences)localObject1).edit().putString("phoneNo", (String)localObject2).commit();
      return;
    }
    ((SharedPreferences)localObject1).edit().remove("phoneNo").commit();
  }

  protected void dispatchProfileChanged()
  {
    Iterator localIterator = this.listeners.iterator();
    while (localIterator.hasNext())
      ((AccountListener)localIterator.next()).onProfileChanged(this);
  }

  public SharedPreferences getWiWideSharedPreferences(Context paramContext)
  {
    return paramContext.getSharedPreferences("WiWide", 0);
  }

  public void login(LoginResultListener paramLoginResultListener)
  {
    login(paramLoginResultListener, null);
  }

  public void login(LoginResultListener paramLoginResultListener, List<NameValuePair> paramList)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    Object localObject1;
    Object localObject2;
    if (ConfigHelper.disableweblogin)
    {
      localObject1 = Uri.parse("dianping://login").buildUpon();
      if (paramList != null)
      {
        paramList = paramList.iterator();
        while (paramList.hasNext())
        {
          localObject2 = (NameValuePair)paramList.next();
          if (TextUtils.isEmpty(((NameValuePair)localObject2).getValue()))
            continue;
          ((Uri.Builder)localObject1).appendQueryParameter(((NameValuePair)localObject2).getName(), ((NameValuePair)localObject2).getValue());
        }
      }
      paramList = ((Uri.Builder)localObject1).build();
      if ("m".equals(paramList.getQueryParameter("logintype")))
        localIntent.setData(Uri.parse(paramList.toString().replaceFirst("dianping://login", "dianping://fastlogin")));
    }
    while (true)
    {
      localIntent.setFlags(335544320);
      this.context.startActivity(localIntent);
      this.loginResultListener = paramLoginResultListener;
      return;
      localIntent.setData(paramList);
      continue;
      Object localObject3 = LoginUtils.getLoginGASource(this.context);
      localIntent.setData(Uri.parse("dianping://loginweb"));
      String str1 = Environment.versionName();
      String str2 = Environment.mapiUserAgent();
      String str3 = preferences(DPApplication.instance()).getString("dpid", "");
      localObject2 = DPApplication.instance().locationService().location();
      if (localObject2 != null)
        localObject1 = Location.FMT.format(((DPObject)localObject2).getDouble("Lat"));
      for (localObject2 = Location.FMT.format(((DPObject)localObject2).getDouble("Lng")); ; localObject2 = "0")
      {
        localObject2 = "http://m.dianping.com/login/app?version=" + str1 + "&agent=" + str2 + "&dpid=" + str3 + "&gasource=" + (String)localObject3 + "&lat=" + (String)localObject1 + "&lng=" + (String)localObject2;
        localObject1 = localObject2;
        if (paramList == null)
          break;
        localObject3 = paramList.iterator();
        paramList = (List<NameValuePair>)localObject2;
        while (true)
        {
          localObject1 = paramList;
          if (!((Iterator)localObject3).hasNext())
            break;
          localObject1 = (NameValuePair)((Iterator)localObject3).next();
          if (TextUtils.isEmpty(((NameValuePair)localObject1).getValue()))
            continue;
          paramList = paramList + "&" + ((NameValuePair)localObject1).getName() + "=" + ((NameValuePair)localObject1).getValue();
        }
        localObject1 = "0";
      }
      localIntent.putExtra("url", (String)localObject1);
    }
  }

  public void removeListener(AccountListener paramAccountListener)
  {
    if (paramAccountListener != null)
      this.listeners.remove(paramAccountListener);
  }

  public void removeLoginResultListener()
  {
    this.loginResultListener = null;
  }

  public void signup(LoginResultListener paramLoginResultListener)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    if (ConfigHelper.disableweblogin)
    {
      localIntent.setData(Uri.parse("dianping://signup"));
      localIntent.setFlags(335544320);
      this.context.startActivity(localIntent);
      this.loginResultListener = paramLoginResultListener;
      return;
    }
    String str2 = LoginUtils.getLoginGASource(this.context);
    String str3 = Environment.versionName();
    String str4 = Environment.mapiUserAgent();
    String str5 = preferences(DPApplication.instance()).getString("dpid", "");
    Object localObject = DPApplication.instance().locationService().location();
    String str1;
    if (localObject != null)
      str1 = Location.FMT.format(((DPObject)localObject).getDouble("Lat"));
    for (localObject = Location.FMT.format(((DPObject)localObject).getDouble("Lng")); ; localObject = "0")
    {
      localIntent.setData(Uri.parse("dianping://loginweb?url=http://m.dianping.com/reg/mobile/app&version=" + str3 + "&agent=" + str4 + "&dpid=" + str5 + "&gasource=" + str2 + "&lat=" + str1 + "&lng=" + (String)localObject));
      break;
      str1 = "0";
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.accountservice.impl.DefaultAccountService
 * JD-Core Version:    0.6.0
 */