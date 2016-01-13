package com.dianping.permission;

import android.content.Context;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PermissionRequestInfo
{
  private Context context;
  private int count = 0;
  private PermissionCheckHelper.PermissionCallbackListener listener;
  private String[] messageArray;
  private String[] permissionArray;
  private Map<String, Integer> permissionResultMap;
  private int requestCode;

  public PermissionRequestInfo(Context paramContext, PermissionCheckHelper.PermissionCallbackListener paramPermissionCallbackListener, int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    this.context = paramContext;
    this.listener = paramPermissionCallbackListener;
    this.requestCode = paramInt;
    this.permissionArray = paramArrayOfString1;
    this.messageArray = paramArrayOfString2;
    initMap();
  }

  private void initMap()
  {
    this.permissionResultMap = new HashMap();
    if ((this.permissionArray == null) || (this.permissionArray.length == 0));
    while (true)
    {
      return;
      String[] arrayOfString = this.permissionArray;
      int j = arrayOfString.length;
      int i = 0;
      while (i < j)
      {
        String str = arrayOfString[i];
        this.permissionResultMap.put(str, Integer.valueOf(-1));
        this.count += 1;
        i += 1;
      }
    }
  }

  public Context getContext()
  {
    return this.context;
  }

  public PermissionCheckHelper.PermissionCallbackListener getListener()
  {
    return this.listener;
  }

  public String[] getMessageArray()
  {
    return this.messageArray;
  }

  public String[] getPermissionArray()
  {
    return this.permissionArray;
  }

  public int getPermissionCount()
  {
    return this.count;
  }

  public Map<String, Integer> getPermissionResultMap()
  {
    return this.permissionResultMap;
  }

  public int getRequestCode()
  {
    return this.requestCode;
  }

  public String[] getRequestPermissions()
  {
    String[] arrayOfString2 = new String[this.count];
    String[] arrayOfString1 = arrayOfString2;
    if (this.permissionResultMap != null)
    {
      arrayOfString1 = arrayOfString2;
      if (this.count > 0)
        arrayOfString1 = (String[])this.permissionResultMap.keySet().toArray(new String[this.count]);
    }
    return arrayOfString1;
  }

  public int[] getRequestResults()
  {
    int[] arrayOfInt = new int[this.count];
    if ((this.permissionResultMap != null) && (this.count > 0))
    {
      Integer[] arrayOfInteger = (Integer[])this.permissionResultMap.values().toArray(new Integer[this.count]);
      int i = 0;
      while (i < this.count)
      {
        arrayOfInt[i] = arrayOfInteger[i].intValue();
        i += 1;
      }
    }
    return arrayOfInt;
  }

  public void setContext(Context paramContext)
  {
    this.context = paramContext;
  }

  public void setListener(PermissionCheckHelper.PermissionCallbackListener paramPermissionCallbackListener)
  {
    this.listener = paramPermissionCallbackListener;
  }

  public void setMessageArray(String[] paramArrayOfString)
  {
    this.messageArray = paramArrayOfString;
  }

  public void setPermissionArray(String[] paramArrayOfString)
  {
    this.permissionArray = paramArrayOfString;
  }

  public void setRequestCode(int paramInt)
  {
    this.requestCode = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.permission.PermissionRequestInfo
 * JD-Core Version:    0.6.0
 */