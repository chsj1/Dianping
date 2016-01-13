package com.dianping.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.dianping.permission.PermissionRequestInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionCheckHelper
{
  private static final String TAG = "PermissionCheckHelper";
  private static PermissionCheckHelper instance;
  private boolean isRequesting = false;
  private final Object lock = new Object();
  private List<PermissionRequestInfo> requestList = new ArrayList();

  private int[] createGrantedResults(int paramInt)
  {
    int[] arrayOfInt = new int[paramInt];
    int i = 0;
    while (i < paramInt)
    {
      arrayOfInt[i] = 0;
      i += 1;
    }
    return arrayOfInt;
  }

  private boolean hasSinglePermission(PermissionRequestInfo paramPermissionRequestInfo)
  {
    int i = 0;
    String[] arrayOfString = paramPermissionRequestInfo.getPermissionArray();
    if (ContextCompat.checkSelfPermission(paramPermissionRequestInfo.getContext(), arrayOfString[0]) == 0)
    {
      paramPermissionRequestInfo.getListener().onPermissionCheckCallback(paramPermissionRequestInfo.getRequestCode(), paramPermissionRequestInfo.getRequestPermissions(), createGrantedResults(paramPermissionRequestInfo.getPermissionCount()));
      i = 1;
    }
    return i;
  }

  public static PermissionCheckHelper instance()
  {
    if (instance == null)
      instance = new PermissionCheckHelper();
    return instance;
  }

  public static boolean isPermissionGranted(Context paramContext, String paramString)
  {
    int j = 0;
    monitorenter;
    if ((paramContext != null) && (paramString != null));
    try
    {
      if ("".equals(paramString))
        Log.d("PermissionCheckHelper", "权限授予检查参数错误!");
      while (true)
      {
        return j;
        int i = ContextCompat.checkSelfPermission(paramContext, paramString);
        if (i != 0)
          continue;
        j = 1;
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramContext;
  }

  private void requestIntoQueue(Context paramContext, PermissionRequestInfo paramPermissionRequestInfo)
  {
    synchronized (this.lock)
    {
      this.requestList.add(paramPermissionRequestInfo);
      if (!this.isRequesting)
      {
        paramPermissionRequestInfo = new Intent("android.intent.action.VIEW", Uri.parse("dianping://handlepermission"));
        paramPermissionRequestInfo.setFlags(268435456);
        paramContext.startActivity(paramPermissionRequestInfo);
        this.isRequesting = true;
      }
      return;
    }
  }

  public PermissionRequestInfo findShouldCheckPermission(PermissionRequestInfo paramPermissionRequestInfo)
  {
    Object localObject = paramPermissionRequestInfo.getPermissionArray();
    int j = localObject.length;
    if (j == 1)
    {
      localObject = paramPermissionRequestInfo;
      if (hasSinglePermission(paramPermissionRequestInfo))
        localObject = null;
      return localObject;
    }
    ArrayList localArrayList1 = new ArrayList(j);
    ArrayList localArrayList2 = new ArrayList(j);
    int i = 0;
    if (i < j)
    {
      String str = localObject[i];
      if (ContextCompat.checkSelfPermission(paramPermissionRequestInfo.getContext(), str) == 0)
        paramPermissionRequestInfo.getPermissionResultMap().put(str, Integer.valueOf(0));
      while (true)
      {
        i += 1;
        break;
        localArrayList1.add(str);
        localArrayList2.add(paramPermissionRequestInfo.getMessageArray()[i]);
      }
    }
    if (localArrayList1.size() == 0)
    {
      paramPermissionRequestInfo.getListener().onPermissionCheckCallback(paramPermissionRequestInfo.getRequestCode(), paramPermissionRequestInfo.getRequestPermissions(), createGrantedResults(paramPermissionRequestInfo.getPermissionCount()));
      return null;
    }
    paramPermissionRequestInfo.setPermissionArray((String[])localArrayList1.toArray(new String[localArrayList1.size()]));
    paramPermissionRequestInfo.setMessageArray((String[])localArrayList2.toArray(new String[localArrayList2.size()]));
    return (PermissionRequestInfo)paramPermissionRequestInfo;
  }

  public PermissionRequestInfo getNextRequest()
  {
    Object localObject4 = this.lock;
    monitorenter;
    Object localObject1 = null;
    while (true)
    {
      Object localObject3 = localObject1;
      try
      {
        if (this.requestList != null)
        {
          localObject3 = localObject1;
          if (this.requestList.size() > 0)
          {
            localObject1 = (PermissionRequestInfo)this.requestList.get(0);
            this.requestList.remove(0);
            localObject3 = findShouldCheckPermission((PermissionRequestInfo)localObject1);
            localObject1 = localObject3;
            if (localObject3 == null)
              continue;
          }
        }
        if (localObject3 == null)
          this.isRequesting = false;
        return localObject3;
      }
      finally
      {
        monitorexit;
      }
    }
    throw localObject2;
  }

  public boolean isRequesting()
  {
    return this.isRequesting;
  }

  public void requestPermissions(Context paramContext, int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, PermissionCallbackListener paramPermissionCallbackListener)
  {
    if ((paramContext == null) || (paramArrayOfString1 == null) || (paramArrayOfString1.length == 0) || (paramArrayOfString2 == null) || (paramArrayOfString2.length == 0) || (paramPermissionCallbackListener == null))
      Log.d("PermissionCheckHelper", "权限检查参数错误!");
    do
    {
      return;
      paramArrayOfString1 = findShouldCheckPermission(new PermissionRequestInfo(paramContext, paramPermissionCallbackListener, paramInt, paramArrayOfString1, paramArrayOfString2));
    }
    while (paramArrayOfString1 == null);
    requestIntoQueue(paramContext, paramArrayOfString1);
  }

  public void setRequestFinish()
  {
    synchronized (this.lock)
    {
      this.isRequesting = false;
      return;
    }
  }

  public static abstract interface PermissionCallbackListener
  {
    public abstract void onPermissionCheckCallback(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.PermissionCheckHelper
 * JD-Core Version:    0.6.0
 */