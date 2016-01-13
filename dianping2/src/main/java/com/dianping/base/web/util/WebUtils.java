package com.dianping.base.web.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.web.cache.UploadImageResourceHandler;
import com.dianping.base.web.js.AlertDialogJsHandler;
import com.dianping.base.web.js.BindJsHandler;
import com.dianping.base.web.js.ChooseImageJsHandler;
import com.dianping.base.web.js.DownloadImageJsHandler;
import com.dianping.base.web.js.GetCXJsHandler;
import com.dianping.base.web.js.GetCityIdJsHandler;
import com.dianping.base.web.js.GetDeviceJsHandler;
import com.dianping.base.web.js.GetLocationJsHandler;
import com.dianping.base.web.js.GetRequestIdJsHandler;
import com.dianping.base.web.js.GetUAJsHandler;
import com.dianping.base.web.js.GetUserInfoJsHandler;
import com.dianping.base.web.js.JumpToSchemeJsHandler;
import com.dianping.base.web.js.LoginSuccessJsHandler;
import com.dianping.base.web.js.LogoutJsHandler;
import com.dianping.base.web.js.MapiJsHandler;
import com.dianping.base.web.js.NetworkStatusJsHandler;
import com.dianping.base.web.js.PlayVoiceJsHandler;
import com.dianping.base.web.js.PreviewImageJsHandler;
import com.dianping.base.web.js.SetPullDownJsHandler;
import com.dianping.base.web.js.SetScrollEnabledJsHandler;
import com.dianping.base.web.js.ShareJsHandler;
import com.dianping.base.web.js.StopPullDownJsHandler;
import com.dianping.base.web.js.UpdateAccountJsHandler;
import com.dianping.base.web.js.UploadContactListJsHandler;
import com.dianping.base.web.js.UploadImageJsHandler;
import com.dianping.base.web.js.UploadPhotoJsHandler;
import com.dianping.base.web.js.VersionJsHandler;
import com.dianping.zeus.cache.CachedResourceManager;
import com.dianping.zeus.cache.CommonFilesResourceHandler;
import com.dianping.zeus.js.JsHandlerFactory;
import java.util.Locale;

public class WebUtils
{
  private static final String[] DPHOSTS = { ".dianping.com", ".51ping.com", ".dpfile.com", ".alpha.dp" };

  public static String getJsLocalStorage(Context paramContext, String paramString1, String paramString2)
  {
    return paramContext.getSharedPreferences("jsbridge_storage", 0).getString(paramString1, paramString2);
  }

  public static void initWebEnvironment()
  {
    CachedResourceManager.addResourceHandler("DPCommonFiles", new CommonFilesResourceHandler());
    CachedResourceManager.addResourceHandler("UploadImageResource", new UploadImageResourceHandler());
    CachedResourceManager.init(NovaApplication.instance().getApplicationContext());
    JsHandlerFactory.registerJsHandler("share", ShareJsHandler.class);
    JsHandlerFactory.registerJsHandler("bind", BindJsHandler.class);
    JsHandlerFactory.registerJsHandler("loginsuccess", LoginSuccessJsHandler.class);
    JsHandlerFactory.registerJsHandler("show_alert", AlertDialogJsHandler.class);
    JsHandlerFactory.registerJsHandler("getdevice", GetDeviceJsHandler.class);
    JsHandlerFactory.registerJsHandler("version", VersionJsHandler.class);
    JsHandlerFactory.registerJsHandler("getNetworkStatus", NetworkStatusJsHandler.class);
    JsHandlerFactory.registerJsHandler("getUserInfo", GetUserInfoJsHandler.class);
    JsHandlerFactory.registerJsHandler("getLocation", GetLocationJsHandler.class);
    JsHandlerFactory.registerJsHandler("uploadImage", UploadImageJsHandler.class);
    JsHandlerFactory.registerJsHandler("getCX", GetCXJsHandler.class);
    JsHandlerFactory.registerJsHandler("getUA", GetUAJsHandler.class);
    JsHandlerFactory.registerJsHandler("getRequestId", GetRequestIdJsHandler.class);
    JsHandlerFactory.registerJsHandler("downloadImage", DownloadImageJsHandler.class);
    JsHandlerFactory.registerJsHandler("mapi", MapiJsHandler.class);
    JsHandlerFactory.registerJsHandler("getCityId", GetCityIdJsHandler.class);
    JsHandlerFactory.registerJsHandler("jumpToScheme", JumpToSchemeJsHandler.class);
    JsHandlerFactory.registerJsHandler("updateAccount", UpdateAccountJsHandler.class);
    JsHandlerFactory.registerJsHandler("setPullDown", SetPullDownJsHandler.class);
    JsHandlerFactory.registerJsHandler("stopPullDown", StopPullDownJsHandler.class);
    JsHandlerFactory.registerJsHandler("uploadContactList", UploadContactListJsHandler.class);
    JsHandlerFactory.registerJsHandler("chooseImage", ChooseImageJsHandler.class);
    JsHandlerFactory.registerJsHandler("uploadPhoto", UploadPhotoJsHandler.class);
    JsHandlerFactory.registerJsHandler("playVoice", PlayVoiceJsHandler.class);
    JsHandlerFactory.registerJsHandler("previewImage", PreviewImageJsHandler.class);
    JsHandlerFactory.registerJsHandler("logout", LogoutJsHandler.class);
    JsHandlerFactory.registerJsHandler("setScrollEnabled", SetScrollEnabledJsHandler.class);
  }

  public static boolean isFromDP(String paramString)
  {
    int m = 1;
    try
    {
      if ((paramString.startsWith("js://_")) || (paramString.startsWith("javascript:")))
        break label106;
      Object localObject = Uri.parse(paramString).getHost();
      if (TextUtils.isEmpty((CharSequence)localObject))
      {
        k = m;
        if (paramString.contains("com.dianping.v1"))
          break label109;
      }
      paramString = ((String)localObject).toLowerCase(Locale.getDefault());
      localObject = DPHOSTS;
      int j = localObject.length;
      int i = 0;
      while (i < j)
      {
        boolean bool = paramString.endsWith(localObject[i]);
        k = m;
        if (bool)
          break label109;
        i += 1;
      }
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
    return false;
    label106: int k = 0;
    label109: return k;
  }

  public static void setJsLocalStorage(Context paramContext, String paramString1, String paramString2)
  {
    paramContext.getSharedPreferences("jsbridge_storage", 0).edit().putString(paramString1, paramString2).commit();
  }

  public static String ua()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("dp");
    localStringBuilder.append("/");
    localStringBuilder.append(DPApplication.instance().getPackageName());
    localStringBuilder.append("/");
    localStringBuilder.append(Environment.versionName());
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.web.util.WebUtils
 * JD-Core Version:    0.6.0
 */