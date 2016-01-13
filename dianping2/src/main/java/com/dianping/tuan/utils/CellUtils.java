package com.dianping.tuan.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class CellUtils
{
  public static void performClick(Context paramContext, String paramString)
  {
    String str = paramString;
    if (paramString != null)
    {
      str = paramString;
      if (!"".equals(paramString))
        str = paramString.trim();
    }
    try
    {
      paramContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
      return;
    }
    catch (java.lang.Exception paramString)
    {
      Toast.makeText(paramContext, "非法链接", 0).show();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.utils.CellUtils
 * JD-Core Version:    0.6.0
 */