package com.dianping.locationservice.impl286.scan;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.dianping.locationservice.impl286.model.WifiModel;
import com.dianping.util.PermissionCheckHelper;
import java.util.Iterator;
import java.util.List;

public class WifiScanner extends BaseScanner<WifiModel>
{
  private Context mContext;
  private final WifiManager mWifiManager;

  public WifiScanner(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.mWifiManager = ((WifiManager)paramContext.getSystemService("wifi"));
  }

  private void doWifiScan()
  {
    Object localObject1 = this.mWifiManager.getConnectionInfo();
    if ((localObject1 != null) && (!TextUtils.isEmpty(((WifiInfo)localObject1).getSSID())))
    {
      localObject1 = new WifiModel(((WifiInfo)localObject1).getSSID(), ((WifiInfo)localObject1).getBSSID(), ((WifiInfo)localObject1).getRssi());
      this.mResultList.add(localObject1);
      if (PermissionCheckHelper.isPermissionGranted(this.mContext, "android.permission.ACCESS_COARSE_LOCATION"))
        break label82;
    }
    while (true)
    {
      return;
      localObject1 = new WifiModel("", "", 0);
      break;
      label82: Object localObject2 = this.mWifiManager.getScanResults();
      if (localObject2 == null)
        continue;
      localObject2 = ((List)localObject2).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        Object localObject3 = (ScanResult)((Iterator)localObject2).next();
        localObject3 = new WifiModel(((ScanResult)localObject3).SSID, ((ScanResult)localObject3).BSSID, ((ScanResult)localObject3).level);
        if ((localObject1 != null) && (((WifiModel)localObject3).equals(localObject1)))
          continue;
        this.mResultList.add(localObject3);
      }
    }
  }

  protected void onStartScan()
  {
    if (!this.mWifiManager.isWifiEnabled())
    {
      this.mErrorMsg = "Wifi is not enabled";
      return;
    }
    doWifiScan();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.scan.WifiScanner
 * JD-Core Version:    0.6.0
 */