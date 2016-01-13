package com.alipay.mobilesecuritysdk.deviceID;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DataShare
{
  String GetDataFromSharedPre(SharedPreferences paramSharedPreferences, String paramString)
  {
    return paramSharedPreferences.getString(paramString, "");
  }

  void SetDataToSharePre(SharedPreferences paramSharedPreferences, Map<String, String> paramMap)
  {
    if ((paramSharedPreferences != null) && (paramMap != null))
    {
      paramSharedPreferences = paramSharedPreferences.edit();
      if (paramSharedPreferences != null)
      {
        paramSharedPreferences.clear();
        paramMap = paramMap.entrySet().iterator();
      }
    }
    while (true)
    {
      if (!paramMap.hasNext())
      {
        paramSharedPreferences.commit();
        return;
      }
      Object localObject = (Map.Entry)paramMap.next();
      String str = (String)((Map.Entry)localObject).getKey();
      localObject = ((Map.Entry)localObject).getValue();
      if ((localObject instanceof String))
      {
        paramSharedPreferences.putString(str, (String)localObject);
        continue;
      }
      if ((localObject instanceof Integer))
      {
        paramSharedPreferences.putInt(str, ((Integer)localObject).intValue());
        continue;
      }
      if ((localObject instanceof Long))
      {
        paramSharedPreferences.putLong(str, ((Long)localObject).longValue());
        continue;
      }
      if ((localObject instanceof Float))
      {
        paramSharedPreferences.putFloat(str, ((Float)localObject).floatValue());
        continue;
      }
      if (!(localObject instanceof Boolean))
        continue;
      paramSharedPreferences.putBoolean(str, ((Boolean)localObject).booleanValue());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.mobilesecuritysdk.deviceID.DataShare
 * JD-Core Version:    0.6.0
 */