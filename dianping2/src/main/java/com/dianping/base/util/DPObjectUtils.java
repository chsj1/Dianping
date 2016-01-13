package com.dianping.base.util;

import android.text.TextUtils;
import com.dianping.archive.DPObject;

public class DPObjectUtils
{
  public static String getShopFullName(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return "";
    Object localObject = paramDPObject.getString("Name");
    paramDPObject = paramDPObject.getString("BranchName");
    localObject = new StringBuilder().append((String)localObject);
    if ((paramDPObject == null) || (paramDPObject.length() == 0));
    for (paramDPObject = ""; ; paramDPObject = "(" + paramDPObject + ")")
      return paramDPObject;
  }

  public static boolean isArrayEmpty(DPObject[] paramArrayOfDPObject)
  {
    return (paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0);
  }

  public static boolean isDPObjectof(Object paramObject)
  {
    return paramObject instanceof DPObject;
  }

  public static boolean isDPObjectof(Object paramObject, String paramString)
  {
    if (!isDPObjectof(paramObject))
      return false;
    return ((DPObject)paramObject).isClass(paramString);
  }

  public static boolean isResultListEmpty(DPObject paramDPObject)
  {
    return isResultListEmpty(paramDPObject, null);
  }

  public static boolean isResultListEmpty(DPObject paramDPObject, String paramString)
  {
    if (paramDPObject == null);
    String str;
    do
    {
      return true;
      str = paramString;
      if (!TextUtils.isEmpty(paramString))
        continue;
      str = "List";
    }
    while ((paramDPObject.getArray(str) == null) || (paramDPObject.getArray(str).length == 0));
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.DPObjectUtils
 * JD-Core Version:    0.6.0
 */