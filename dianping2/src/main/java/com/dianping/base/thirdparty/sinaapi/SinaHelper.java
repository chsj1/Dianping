package com.dianping.base.thirdparty.sinaapi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import com.dianping.util.Log;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

public class SinaHelper
{
  public static final String APP_ID = "844890293";
  private static final String TAG = "SinaHelper";
  private static IWeiboShareAPI sWeiboAPI;

  public static IWeiboShareAPI getWeiboAPI(Context paramContext)
  {
    if (sWeiboAPI == null)
    {
      if (paramContext == null)
        return null;
      sWeiboAPI = WeiboShareSDK.createWeiboAPI(paramContext, "844890293");
      if (!sWeiboAPI.registerApp())
        Log.e("SinaHelper", "register weibo API fail");
    }
    return sWeiboAPI;
  }

  private static boolean isSupportAPI(Context paramContext, int paramInt, boolean paramBoolean)
  {
    int j;
    if (!isWeiboAppInstalled(paramContext, paramBoolean))
    {
      j = 0;
      return j;
    }
    if (getWeiboAPI(paramContext).getWeiboAppSupportAPI() >= paramInt);
    for (int i = 1; ; i = 0)
    {
      j = i;
      if (i != 0)
        break;
      j = i;
      if (!paramBoolean)
        break;
      Toast.makeText(paramContext, "您安装的微博版本过低", 0).show();
      return i;
    }
  }

  public static boolean isWeiboAppInstalled(Context paramContext)
  {
    return isWeiboAppInstalled(paramContext, true);
  }

  public static boolean isWeiboAppInstalled(Context paramContext, boolean paramBoolean)
  {
    boolean bool = getWeiboAPI(paramContext).isWeiboAppInstalled();
    if ((!bool) && (paramBoolean))
      Toast.makeText(paramContext, "您尚未安装微博", 0).show();
    return bool;
  }

  public static boolean share(Activity paramActivity, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3)
  {
    WebpageObject localWebpageObject = new WebpageObject();
    localWebpageObject.setThumbImage(paramBitmap);
    localWebpageObject.description = paramString2;
    localWebpageObject.actionUrl = paramString3;
    localWebpageObject.title = paramString1;
    localWebpageObject.identify = "大众点评identify";
    paramString1 = new WeiboMultiMessage();
    paramString3 = new TextObject();
    paramString3.text = paramString2;
    paramString2 = new ImageObject();
    paramString2.setImageObject(paramBitmap);
    paramString1.textObject = paramString3;
    paramString1.imageObject = paramString2;
    paramString1.mediaObject = localWebpageObject;
    paramString2 = new SendMultiMessageToWeiboRequest();
    paramString2.multiMessage = paramString1;
    paramString2.transaction = String.valueOf(System.currentTimeMillis());
    return getWeiboAPI(paramActivity).sendRequest(paramActivity, paramString2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.thirdparty.sinaapi.SinaHelper
 * JD-Core Version:    0.6.0
 */