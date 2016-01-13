package com.dianping.util.telephone;

import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import com.dianping.app.DPApplication;

public class CellUtils
{
  private static TelephonyManager sCellManager;

  public static String cellInfo()
  {
    try
    {
      String str = doCellScan();
      return str;
    }
    catch (Exception localException)
    {
    }
    return "";
  }

  public static TelephonyManager cellManager()
  {
    if (sCellManager == null)
    {
      sCellManager = (TelephonyManager)DPApplication.instance().getSystemService("phone");
      sCellManager.isNetworkRoaming();
    }
    return sCellManager;
  }

  private static String doCdmaScan(CdmaCellLocation paramCdmaCellLocation)
  {
    int i = mccFromOperator();
    int j = paramCdmaCellLocation.getSystemId();
    int k = paramCdmaCellLocation.getBaseStationId();
    int m = paramCdmaCellLocation.getNetworkId();
    int n = paramCdmaCellLocation.getBaseStationLatitude();
    int i1 = paramCdmaCellLocation.getBaseStationLongitude();
    if ((i < 0) || (j < 0) || (k < 0) || (m < 0))
      return "";
    return new CellCdmaModel(i, j, k, m, n, i1).toDPString();
  }

  private static String doCellScan()
  {
    CellLocation localCellLocation = cellManager().getCellLocation();
    if (localCellLocation == null)
      return "";
    if ((localCellLocation instanceof GsmCellLocation))
      return doGsmScan((GsmCellLocation)localCellLocation);
    if ((localCellLocation instanceof CdmaCellLocation))
      return doCdmaScan((CdmaCellLocation)localCellLocation);
    return "";
  }

  private static String doGsmScan(GsmCellLocation paramGsmCellLocation)
  {
    int i = mccFromOperator();
    int j = mncFromOperator();
    int k = paramGsmCellLocation.getCid();
    int m = paramGsmCellLocation.getLac();
    if ((i < 0) || (j < 0) || (k < 0) || (m < 0))
      return "";
    return new CellGsmModel(i, j, k, m, 0).toDPString();
  }

  private static boolean isOperatorValid(String paramString)
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

  public static boolean isRoaming()
  {
    return cellManager().isNetworkRoaming();
  }

  private static int mccFromOperator()
  {
    String str = operator();
    if (isOperatorValid(str))
      return Integer.parseInt(str.substring(0, 3));
    return -1;
  }

  private static int mncFromOperator()
  {
    String str = operator();
    if (isOperatorValid(str))
      return Integer.parseInt(str.substring(3, str.length()));
    return -1;
  }

  public static int networkType()
  {
    return cellManager().getNetworkType();
  }

  private static String operator()
  {
    String str2 = cellManager().getNetworkOperator();
    String str1 = str2;
    if (!isOperatorValid(str2))
      str1 = cellManager().getSimOperator();
    return str1;
  }

  public static String operatorName()
  {
    String str1 = null;
    String str2 = cellManager().getNetworkOperator();
    if (TextUtils.isEmpty(str2))
      str1 = "";
    do
    {
      return str1;
      if ((str2.startsWith("46000")) || (str2.startsWith("46002")))
        return "中国移动";
      if (str2.startsWith("46001"))
        return "中国联通";
    }
    while (!str2.startsWith("46003"));
    return "中国电信";
  }

  public static int simState()
  {
    return cellManager().getSimState();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.telephone.CellUtils
 * JD-Core Version:    0.6.0
 */