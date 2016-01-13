package com.dianping.base.ugc.photo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.archive.DPObject;

public class UploadPhotoUtil
{
  public static void uploadShopPhoto(Context paramContext, int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("dianping://addshopphoto").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(paramInt));
    localBuilder.appendQueryParameter("maxnum", "9");
    localBuilder.appendQueryParameter("checkdraft", String.valueOf(true));
    paramContext.startActivity(new Intent("android.intent.action.VIEW", localBuilder.build()));
  }

  public static void uploadShopPhoto(Context paramContext, DPObject paramDPObject)
  {
    Uri.Builder localBuilder = Uri.parse("dianping://addshopphoto").buildUpon();
    if (paramDPObject != null)
    {
      localBuilder.appendQueryParameter("shopid", String.valueOf(paramDPObject.getInt("ID")));
      localBuilder.appendQueryParameter("shopname", paramDPObject.getString("Name"));
      localBuilder.appendQueryParameter("maxnum", "9");
      localBuilder.appendQueryParameter("checkdraft", String.valueOf(true));
      paramDPObject = paramDPObject.getArray("ShopPhotoCategory");
      StringBuilder localStringBuilder = new StringBuilder();
      if (paramDPObject != null)
      {
        int i = 0;
        while (i < paramDPObject.length)
        {
          localStringBuilder.append(paramDPObject[i].getString("Name")).append(",");
          i += 1;
        }
        if (localStringBuilder.length() > 0)
          localBuilder.appendQueryParameter("category", localStringBuilder.deleteCharAt(localStringBuilder.length() - 1).toString());
      }
    }
    paramContext.startActivity(new Intent("android.intent.action.VIEW", localBuilder.build()));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.ugc.photo.UploadPhotoUtil
 * JD-Core Version:    0.6.0
 */