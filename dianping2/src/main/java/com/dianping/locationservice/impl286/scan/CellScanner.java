package com.dianping.locationservice.impl286.scan;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.dianping.locationservice.impl286.model.CellCdmaModel;
import com.dianping.locationservice.impl286.model.CellGsmModel;
import com.dianping.locationservice.impl286.model.CellLteModel;
import com.dianping.locationservice.impl286.model.CellModel;
import com.dianping.locationservice.impl286.model.CellWcdmaModel;
import com.dianping.util.PermissionCheckHelper;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class CellScanner extends BaseScanner<CellModel>
{
  private final String GET_SIM_STATE_METHOD_GENERAL = "getSimState";
  private Context mContext;
  private final TelephonyManager mPhoneManager;

  public CellScanner(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.mPhoneManager = ((TelephonyManager)paramContext.getSystemService("phone"));
  }

  private void doCdmaScanBelowApi17(CdmaCellLocation paramCdmaCellLocation)
  {
    int j = getMccFromOperator();
    int k = paramCdmaCellLocation.getSystemId();
    int m = paramCdmaCellLocation.getBaseStationId();
    int n = paramCdmaCellLocation.getNetworkId();
    int i1 = paramCdmaCellLocation.getBaseStationLatitude();
    int i2 = paramCdmaCellLocation.getBaseStationLongitude();
    if ((isDataValid(j)) && (isDataValid(k)) && (isDataValid(m)) && (isDataValid(n)));
    for (int i = 1; i == 0; i = 0)
    {
      this.mErrorMsg = "cdma invalid value";
      return;
    }
    paramCdmaCellLocation = new CellCdmaModel(j, k, m, n, i1, i2);
    this.mResultList.add(paramCdmaCellLocation);
  }

  private void doCellScan()
  {
    if (Build.VERSION.SDK_INT >= 17)
      doCellScanAboveApi17();
    doCellScanBelowApi17();
    if (this.mResultList.isEmpty())
      this.mErrorMsg = "No cell scan result";
  }

  @TargetApi(18)
  private void doCellScanAboveApi17()
  {
    Object localObject1 = this.mPhoneManager.getAllCellInfo();
    if ((localObject1 == null) || (((List)localObject1).isEmpty()));
    while (true)
    {
      return;
      Iterator localIterator = ((List)localObject1).iterator();
      while (localIterator.hasNext())
      {
        CellInfo localCellInfo = (CellInfo)localIterator.next();
        Object localObject2 = null;
        if ((localCellInfo instanceof CellInfoGsm))
        {
          localObject1 = ((CellInfoGsm)localCellInfo).getCellIdentity();
          localObject1 = new CellGsmModel(((CellIdentityGsm)localObject1).getMcc(), ((CellIdentityGsm)localObject1).getMnc(), ((CellIdentityGsm)localObject1).getCid(), ((CellIdentityGsm)localObject1).getLac(), 0);
          label93: if (localObject1 == null)
            break label336;
          i = ((CellModel)localObject1).getMcc();
          int j = ((CellModel)localObject1).getMncSid();
          int k = ((CellModel)localObject1).getCidBid();
          int m = ((CellModel)localObject1).getLacNid();
          if ((!isDataValid(i)) || (!isDataValid(j)) || (!isDataValid(k)) || (!isDataValid(m)) || (m == 0))
            break label338;
        }
        label336: label338: for (int i = 1; ; i = 0)
        {
          if (i == 0)
            break label344;
          this.mResultList.add(localObject1);
          break;
          if ((localCellInfo instanceof CellInfoCdma))
          {
            localObject1 = ((CellInfoCdma)localCellInfo).getCellIdentity();
            localObject1 = new CellCdmaModel(getMccFromOperator(), ((CellIdentityCdma)localObject1).getSystemId(), ((CellIdentityCdma)localObject1).getBasestationId(), ((CellIdentityCdma)localObject1).getNetworkId(), ((CellIdentityCdma)localObject1).getLatitude(), ((CellIdentityCdma)localObject1).getLongitude());
            break label93;
          }
          if ((localCellInfo instanceof CellInfoLte))
          {
            localObject1 = ((CellInfoLte)localCellInfo).getCellIdentity();
            localObject1 = new CellLteModel(((CellIdentityLte)localObject1).getMcc(), ((CellIdentityLte)localObject1).getMnc(), ((CellIdentityLte)localObject1).getCi(), ((CellIdentityLte)localObject1).getTac(), 0);
            break label93;
          }
          localObject1 = localObject2;
          if (Build.VERSION.SDK_INT < 18)
            break label93;
          localObject1 = localObject2;
          if (!(localCellInfo instanceof CellInfoWcdma))
            break label93;
          localObject1 = ((CellInfoWcdma)localCellInfo).getCellIdentity();
          localObject1 = new CellWcdmaModel(((CellIdentityWcdma)localObject1).getMcc(), ((CellIdentityWcdma)localObject1).getMnc(), ((CellIdentityWcdma)localObject1).getCid(), ((CellIdentityWcdma)localObject1).getLac(), 0);
          break label93;
          break;
        }
        label344: this.mErrorMsg = "invalid value";
      }
    }
  }

  private void doCellScanBelowApi17()
  {
    CellLocation localCellLocation = this.mPhoneManager.getCellLocation();
    if (localCellLocation == null)
      this.mErrorMsg = "cellLocation is null";
    do
    {
      return;
      if (!(localCellLocation instanceof GsmCellLocation))
        continue;
      doGsmScanBelowApi17((GsmCellLocation)localCellLocation);
      return;
    }
    while (!(localCellLocation instanceof CdmaCellLocation));
    doCdmaScanBelowApi17((CdmaCellLocation)localCellLocation);
  }

  private void doGsmScanBelowApi17(GsmCellLocation paramGsmCellLocation)
  {
    int j = getMccFromOperator();
    int k = getMncFromOperator();
    int m = paramGsmCellLocation.getCid();
    int n = paramGsmCellLocation.getLac();
    int i;
    if ((isDataValid(j)) && (isDataValid(k)) && (isDataValid(m)) && (isDataValid(n)) && (n != 0))
    {
      i = 1;
      if (i != 0)
        break label83;
      this.mErrorMsg = "gsm invalid value";
    }
    while (true)
    {
      return;
      i = 0;
      break;
      label83: paramGsmCellLocation = new CellGsmModel(j, k, m, n, 0);
      this.mResultList.add(paramGsmCellLocation);
      paramGsmCellLocation = this.mPhoneManager.getNeighboringCellInfo();
      if (paramGsmCellLocation == null)
        continue;
      paramGsmCellLocation = paramGsmCellLocation.iterator();
      while (paramGsmCellLocation.hasNext())
      {
        Object localObject = (NeighboringCellInfo)paramGsmCellLocation.next();
        i = ((NeighboringCellInfo)localObject).getCid();
        m = ((NeighboringCellInfo)localObject).getLac();
        n = ((NeighboringCellInfo)localObject).getRssi();
        if ((i == -1) || (m == -1))
          continue;
        localObject = new CellGsmModel(j, k, i, m, n);
        this.mResultList.add(localObject);
      }
    }
  }

  private int getMccFromOperator()
  {
    String str = getOperator();
    if (isOperatorValid(str))
      return Integer.parseInt(str.substring(0, 3));
    return -1;
  }

  private int getMncFromOperator()
  {
    String str = getOperator();
    if (isOperatorValid(str))
      return Integer.parseInt(str.substring(3, str.length()));
    return -1;
  }

  private String getOperator()
  {
    String str2 = this.mPhoneManager.getNetworkOperator();
    String str1 = str2;
    if (!isOperatorValid(str2))
      str1 = this.mPhoneManager.getSimOperator();
    return str1;
  }

  private boolean isDataValid(int paramInt)
  {
    if (paramInt == 2147483647);
    do
      return false;
    while (paramInt < 0);
    return true;
  }

  private boolean isDualSimReady()
  {
    boolean bool3 = false;
    boolean bool1 = false;
    boolean bool4 = false;
    try
    {
      bool2 = isSimReadyBySlot("getSimState", 0);
      bool1 = bool2;
      boolean bool5 = isSimReadyBySlot("getSimState", 1);
      bool1 = bool5;
      if (!bool2)
      {
        bool2 = bool3;
        if (!bool1);
      }
      else
      {
        bool2 = true;
      }
      return bool2;
    }
    catch (Exception localException)
    {
      while (true)
      {
        boolean bool2 = bool1;
        bool1 = bool4;
      }
    }
  }

  private boolean isOperatorValid(String paramString)
  {
    int k = 0;
    int j = k;
    if (paramString != null)
    {
      int i = paramString.length();
      if (i != 5)
      {
        j = k;
        if (i != 6);
      }
      else
      {
        j = 1;
      }
    }
    return j;
  }

  private boolean isSimReadyBySlot(String paramString, int paramInt)
    throws Exception
  {
    int j = 0;
    try
    {
      Object localObject = Class.forName(this.mPhoneManager.getClass().getName()).getMethod(paramString, new Class[] { Integer.TYPE }).invoke(this.mPhoneManager, new Object[] { Integer.valueOf(paramInt) });
      int i = j;
      if (localObject != null)
      {
        paramInt = Integer.parseInt(localObject.toString());
        i = j;
        if (paramInt == 5)
          i = 1;
      }
      return i;
    }
    catch (Exception localException)
    {
    }
    throw new Exception(paramString);
  }

  protected void onStartScan()
  {
    if (!PermissionCheckHelper.isPermissionGranted(this.mContext, "android.permission.ACCESS_COARSE_LOCATION"))
    {
      this.mErrorMsg = "Has no location permission";
      return;
    }
    if ((this.mPhoneManager.getSimState() != 5) && (!isDualSimReady()))
    {
      this.mErrorMsg = "Sim state is not ready";
      CellModel localCellModel = new CellModel(-1);
      this.mResultList.add(localCellModel);
      return;
    }
    try
    {
      doCellScan();
      return;
    }
    catch (Exception localException)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.scan.CellScanner
 * JD-Core Version:    0.6.0
 */