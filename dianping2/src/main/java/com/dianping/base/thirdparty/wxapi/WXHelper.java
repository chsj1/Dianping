package com.dianping.base.thirdparty.wxapi;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.util.BitmapUtils;
import com.dianping.util.Log;
import com.tencent.mm.sdk.modelmsg.GetMessageFromWX.Req;
import com.tencent.mm.sdk.modelmsg.GetMessageFromWX.Resp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.lang.reflect.Method;

public class WXHelper
{
  public static final String ACTION_WX_PAY = "com.dianping.base.thirdparty.wxapi.WXPAY";
  public static final String APP_ID = "wx8e251222d6836a60";
  public static final String TAG = WXHelper.class.getSimpleName();
  public static final String WX_SSO_BIND_RESPONSE = "wxssobindresp";
  public static final String WX_SSO_BIND_TAG = "wxssobindtag";
  public static final String WX_SSO_LOGIN_CALLBACK_URL = "wxssologincallbackurl";
  public static final String WX_SSO_LOGIN_STATE = "wxssologinstate";
  public static IWxApiListener mWxApiListener = null;
  private static IWXAPI sApi = null;

  private static String buildTransaction(String paramString)
  {
    String str = String.valueOf(System.currentTimeMillis());
    if (paramString == null)
      return str;
    return paramString + str;
  }

  public static boolean checkSignature(Context paramContext)
  {
    try
    {
      paramContext = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 64).signatures;
      if (paramContext != null)
      {
        if (paramContext.length == 0)
          return false;
        int j = paramContext.length;
        int i = 0;
        while (i < j)
        {
          boolean bool = "ac6fc3fe".equalsIgnoreCase(Integer.toHexString(paramContext[i].toCharsString().hashCode()));
          if (bool)
            return true;
          i += 1;
        }
      }
    }
    catch (Exception paramContext)
    {
    }
    return false;
  }

  private static String getTransaction(Context paramContext)
  {
    Object localObject = null;
    try
    {
      paramContext = paramContext.getApplicationContext();
      paramContext = (Bundle)paramContext.getClass().getDeclaredMethod("getWXBundle", new Class[0]).invoke(paramContext, new Object[0]);
      if (paramContext == null)
        return "";
    }
    catch (Throwable paramContext)
    {
      while (true)
      {
        Log.e(TAG, paramContext.toString());
        paramContext = localObject;
      }
    }
    return new GetMessageFromWX.Req(paramContext).transaction;
  }

  public static IWXAPI getWXAPI(Context paramContext)
  {
    if (sApi == null)
    {
      if (paramContext == null)
        return null;
      sApi = WXAPIFactory.createWXAPI(paramContext.getApplicationContext(), "wx8e251222d6836a60", true);
      if (!sApi.registerApp("wx8e251222d6836a60"))
        Log.e(TAG, "register wxapi fail");
    }
    return sApi;
  }

  public static IWxApiListener getWxApiListener()
  {
    return mWxApiListener;
  }

  private static boolean isSupportAPI(Context paramContext, int paramInt, boolean paramBoolean)
  {
    int i = 1;
    int j;
    if (!isWXAppInstalled(paramContext, paramBoolean))
    {
      j = 0;
      return j;
    }
    if (!checkSignature(paramContext))
    {
      Toast.makeText(paramContext, "请安装正式版本使用该功能", 1).show();
      return false;
    }
    if (getWXAPI(paramContext).getWXAppSupportAPI() >= paramInt);
    while (true)
    {
      j = i;
      if (i != 0)
        break;
      j = i;
      if (!paramBoolean)
        break;
      Toast.makeText(paramContext, "您安装的微信版本过低", 0).show();
      return i;
      i = 0;
    }
  }

  public static boolean isSupportPay(Context paramContext)
  {
    return isSupportAPI(paramContext, 570425345, true);
  }

  public static boolean isSupportPay(Context paramContext, boolean paramBoolean)
  {
    return isSupportAPI(paramContext, 570425345, paramBoolean);
  }

  public static boolean isSupportSSO(Context paramContext)
  {
    return isSupportShare(paramContext);
  }

  public static boolean isSupportSSO(Context paramContext, boolean paramBoolean)
  {
    return isSupportShare(paramContext, paramBoolean);
  }

  public static boolean isSupportShare(Context paramContext)
  {
    return isSupportAPI(paramContext, 570425345, true);
  }

  public static boolean isSupportShare(Context paramContext, boolean paramBoolean)
  {
    return isSupportAPI(paramContext, 570425345, paramBoolean);
  }

  public static boolean isSupportShareFriends(Context paramContext)
  {
    return isSupportAPI(paramContext, 553779201, true);
  }

  public static boolean isSupportShareFriends(Context paramContext, boolean paramBoolean)
  {
    return isSupportAPI(paramContext, 553779201, paramBoolean);
  }

  public static boolean isWXAppInstalled(Context paramContext)
  {
    return isWXAppInstalled(paramContext, true);
  }

  public static boolean isWXAppInstalled(Context paramContext, boolean paramBoolean)
  {
    boolean bool = getWXAPI(paramContext).isWXAppInstalled();
    if ((!bool) && (paramBoolean))
      Toast.makeText(paramContext, "您尚未安装微信", 0).show();
    return bool;
  }

  public static void registerApp(Context paramContext)
  {
    getWXAPI(paramContext);
  }

  public static void registerWxApiListener(IWxApiListener paramIWxApiListener)
  {
    mWxApiListener = paramIWxApiListener;
  }

  public static boolean sharePicture(Context paramContext, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean, String paramString)
  {
    try
    {
      WXMediaMessage localWXMediaMessage = new WXMediaMessage();
      WXImageObject localWXImageObject = new WXImageObject();
      localWXImageObject.imageData = paramArrayOfByte;
      localWXMediaMessage.mediaObject = localWXImageObject;
      if (paramBoolean)
      {
        paramArrayOfByte = new GetMessageFromWX.Resp();
        paramArrayOfByte.transaction = getTransaction(paramContext);
        paramArrayOfByte.message = localWXMediaMessage;
        return getWXAPI(paramContext).sendResp(paramArrayOfByte);
      }
      paramArrayOfByte = new SendMessageToWX.Req();
      paramArrayOfByte.scene = paramInt;
      if (TextUtils.isEmpty(paramString));
      for (paramArrayOfByte.transaction = buildTransaction("image"); ; paramArrayOfByte.transaction = paramString)
      {
        paramArrayOfByte.message = localWXMediaMessage;
        return getWXAPI(paramContext).sendReq(paramArrayOfByte);
      }
    }
    catch (Exception paramArrayOfByte)
    {
      Log.e(paramArrayOfByte.toString());
      ShareUtil.showToast(paramContext, "分享微信失败，请稍后再试");
    }
    return false;
  }

  public static boolean sharePictureWithFriend(Context paramContext, byte[] paramArrayOfByte)
  {
    if (!isSupportShare(paramContext, true))
      return false;
    return sharePicture(paramContext, paramArrayOfByte, 0, false, null);
  }

  public static boolean sharePictureWithFriends(Context paramContext, byte[] paramArrayOfByte)
  {
    if (!isSupportShareFriends(paramContext, true))
      return false;
    return sharePicture(paramContext, paramArrayOfByte, 1, false, null);
  }

  public static boolean shareScheme(Context paramContext, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3, String paramString4)
  {
    try
    {
      WXMediaMessage localWXMediaMessage = new WXMediaMessage();
      localWXMediaMessage.title = paramString1;
      localWXMediaMessage.description = paramString2;
      if (paramBitmap != null)
        localWXMediaMessage.thumbData = BitmapUtils.bmpToByteArray(Bitmap.createScaledBitmap(paramBitmap, 110, 110, true), true);
      paramString1 = new WXAppExtendObject();
      paramString1.extInfo = paramString3;
      localWXMediaMessage.mediaObject = paramString1;
      paramString1 = new SendMessageToWX.Req();
      paramString1.scene = 0;
      if (TextUtils.isEmpty(paramString4));
      for (paramString1.transaction = buildTransaction("text"); ; paramString1.transaction = paramString4)
      {
        paramString1.message = localWXMediaMessage;
        return getWXAPI(paramContext).sendReq(paramString1);
      }
    }
    catch (Exception paramString1)
    {
      Log.e(TAG, paramString1.toString());
      Toast.makeText(paramContext, "分享微信失败，请稍后再试", 0).show();
    }
    return false;
  }

  public static boolean shareWithFriend(Context paramContext, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3)
  {
    return shareWithFriend(paramContext, paramString1, paramString2, paramBitmap, paramString3, false);
  }

  public static boolean shareWithFriend(Context paramContext, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3, boolean paramBoolean)
  {
    return shareWithFriend(paramContext, paramString1, paramString2, paramBitmap, paramString3, paramBoolean, true, null);
  }

  public static boolean shareWithFriend(Context paramContext, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3, boolean paramBoolean1, boolean paramBoolean2, String paramString4)
  {
    if (!isSupportShare(paramContext, paramBoolean2))
      return false;
    return shareWithSomeBody(paramContext, paramString1, paramString2, paramBitmap, paramString3, 0, paramBoolean1, paramString4);
  }

  public static boolean shareWithFriends(Context paramContext, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3)
  {
    return shareWithFriends(paramContext, paramString1, paramString2, paramBitmap, paramString3, true, null);
  }

  public static boolean shareWithFriends(Context paramContext, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3, boolean paramBoolean, String paramString4)
  {
    if (!isSupportShareFriends(paramContext, paramBoolean))
      return false;
    return shareWithSomeBody(paramContext, paramString1, paramString2, paramBitmap, paramString3, 1, false, paramString4);
  }

  private static boolean shareWithSomeBody(Context paramContext, String paramString1, String paramString2, Bitmap paramBitmap, String paramString3, int paramInt, boolean paramBoolean, String paramString4)
  {
    try
    {
      WXMediaMessage localWXMediaMessage = new WXMediaMessage();
      localWXMediaMessage.title = paramString1;
      localWXMediaMessage.description = paramString2;
      if (paramBitmap != null)
        localWXMediaMessage.thumbData = BitmapUtils.bmpToByteArray(Bitmap.createScaledBitmap(paramBitmap, 110, 110, true), true);
      paramString1 = new WXWebpageObject();
      paramString1.webpageUrl = paramString3;
      localWXMediaMessage.mediaObject = paramString1;
      if (paramBoolean)
      {
        paramString1 = new GetMessageFromWX.Resp();
        paramString1.transaction = getTransaction(paramContext);
        paramString1.message = localWXMediaMessage;
        return getWXAPI(paramContext).sendResp(paramString1);
      }
      paramString1 = new SendMessageToWX.Req();
      paramString1.scene = paramInt;
      if (TextUtils.isEmpty(paramString4));
      for (paramString1.transaction = buildTransaction("text"); ; paramString1.transaction = paramString4)
      {
        paramString1.message = localWXMediaMessage;
        return getWXAPI(paramContext).sendReq(paramString1);
      }
    }
    catch (Exception paramString1)
    {
      Log.e(paramString1.toString());
      ShareUtil.showToast(paramContext, "分享微信失败，请稍后再试");
    }
    return false;
  }

  public static void unregisterWxApiListener()
  {
    mWxApiListener = null;
  }

  public static abstract interface IWxApiListener
  {
    public abstract void onCancel();

    public abstract void onError();

    public abstract void onSucess();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.thirdparty.wxapi.WXHelper
 * JD-Core Version:    0.6.0
 */