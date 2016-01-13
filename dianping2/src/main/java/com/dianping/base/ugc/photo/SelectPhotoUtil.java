package com.dianping.base.ugc.photo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;

public class SelectPhotoUtil
{
  public static final int REQUEST_CODE_SELECT_PHOTO = 1000;

  public static void selectPhoto(Activity paramActivity, int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("dianping://photoselect").buildUpon();
    localBuilder.appendQueryParameter("maxNum", String.valueOf(paramInt));
    paramActivity.startActivityForResult(new Intent("android.intent.action.VIEW", localBuilder.build()), 1000);
  }

  public static void selectPhoto(Activity paramActivity, int paramInt1, int paramInt2)
  {
    Uri.Builder localBuilder = Uri.parse("dianping://photoselect").buildUpon();
    localBuilder.appendQueryParameter("maxNum", String.valueOf(paramInt1));
    paramActivity.startActivityForResult(new Intent("android.intent.action.VIEW", localBuilder.build()), paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.ugc.photo.SelectPhotoUtil
 * JD-Core Version:    0.6.0
 */