package com.dianping.base.share.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.IShareHandler;

public class ShareUtil
{
  public static final String KEY_SHARE_CHANNEL = "shareChannel";
  public static final String KEY_SHARE_RESULT = "shareResult";
  public static final int REQUEST_SHARETO = 111;
  public static final String RESULT_CANCEL = "cancel";
  public static final String RESULT_FAIL = "fail";
  public static final String RESULT_SUCCESS = "success";
  public static boolean sShareForeground;
  private static IShareHandler sShareHandler = null;

  static
  {
    sShareForeground = false;
  }

  public static IShareHandler getShareHandler()
  {
    return sShareHandler;
  }

  public static void gotoShareTo(Context paramContext, Parcelable paramParcelable, int paramInt1, String paramString1, String paramString2, int paramInt2, IShareHandler paramIShareHandler)
  {
    gotoShareTo(paramContext, ShareType.MultiShare, paramParcelable, paramInt1, paramString1, paramString2, paramInt2, paramIShareHandler);
  }

  public static void gotoShareTo(Context paramContext, ShareType paramShareType, Parcelable paramParcelable)
  {
    gotoShareTo(paramContext, paramShareType, paramParcelable, -1, null, null, 0, null);
  }

  public static void gotoShareTo(Context paramContext, ShareType paramShareType, Parcelable paramParcelable, int paramInt1, int paramInt2, IShareHandler paramIShareHandler)
  {
    gotoShareTo(paramContext, paramShareType, paramParcelable, paramInt1, null, null, paramInt2, paramIShareHandler);
  }

  public static void gotoShareTo(Context paramContext, ShareType paramShareType, Parcelable paramParcelable, int paramInt, String paramString1, String paramString2)
  {
    gotoShareTo(paramContext, paramShareType, paramParcelable, paramInt, paramString1, paramString2, 0);
  }

  public static void gotoShareTo(Context paramContext, ShareType paramShareType, Parcelable paramParcelable, int paramInt1, String paramString1, String paramString2, int paramInt2)
  {
    gotoShareTo(paramContext, paramShareType, paramParcelable, paramInt1, paramString1, paramString2, paramInt2, null);
  }

  public static void gotoShareTo(Context paramContext, ShareType paramShareType, Parcelable paramParcelable, int paramInt1, String paramString1, String paramString2, int paramInt2, IShareHandler paramIShareHandler)
  {
    if (sShareForeground)
      return;
    sShareForeground = true;
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://shareto"));
    if (paramInt1 > 0)
      localIntent.putExtra("shareItemId", paramInt1);
    localIntent.putExtra("shareType", paramShareType);
    localIntent.putExtra("shareObj", paramParcelable);
    if (!TextUtils.isEmpty(paramString1))
      localIntent.putExtra("gaCategory", paramString1);
    if (!TextUtils.isEmpty(paramString2))
      localIntent.putExtra("gaAction", paramString2);
    localIntent.putExtra("feed", paramInt2);
    if ((paramContext instanceof Activity))
      ((Activity)paramContext).startActivityForResult(localIntent, 111);
    while (true)
    {
      setShareHandler(paramIShareHandler);
      return;
      paramContext.startActivity(localIntent);
    }
  }

  public static void gotoShareTo(Context paramContext, ShareType paramShareType, Parcelable paramParcelable, String paramString1, String paramString2)
  {
    gotoShareTo(paramContext, paramShareType, paramParcelable, -1, paramString1, paramString2, 0);
  }

  public static void gotoShareTo(Context paramContext, ShareType paramShareType, Parcelable paramParcelable, String paramString1, String paramString2, int paramInt)
  {
    gotoShareTo(paramContext, paramShareType, paramParcelable, -1, paramString1, paramString2, paramInt);
  }

  public static void setShareHandler(IShareHandler paramIShareHandler)
  {
    sShareHandler = paramIShareHandler;
  }

  public static void showToast(Context paramContext, String paramString)
  {
    Toast.makeText(paramContext, paramString, 0).show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.util.ShareUtil
 * JD-Core Version:    0.6.0
 */