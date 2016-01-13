package com.alipay.mobilesecuritysdk.constant;

public enum LocationNameEnum
{
  private String value;

  static
  {
    LOCATE_CELL_ID = new LocationNameEnum("LOCATE_CELL_ID", 2, "c");
    LOCATE_LAC = new LocationNameEnum("LOCATE_LAC", 3, "l");
    TIME_STAMP = new LocationNameEnum("TIME_STAMP", 4, "s");
    LOCATE_WIFI = new LocationNameEnum("LOCATE_WIFI", 5, "w");
    LOCATION_ITEM = new LocationNameEnum("LOCATION_ITEM", 6, "locationitem");
    START_TAG = new LocationNameEnum("START_TAG", 7, "locations");
    VERSION = new LocationNameEnum("VERSION", 8, "ver");
    MCC = new LocationNameEnum("MCC", 9, "mcc");
    MNC = new LocationNameEnum("MNC", 10, "mnc");
    PHONETYPE = new LocationNameEnum("PHONETYPE", 11, "phoneType");
    CDMA = new LocationNameEnum("CDMA", 12, "cdma");
    BSSID = new LocationNameEnum("BSSID", 13, "bssid");
    SSID = new LocationNameEnum("SSID", 14, "ssid");
    LEVEL = new LocationNameEnum("LEVEL", 15, "level");
    CURRENT = new LocationNameEnum("CURRENT", 16, "isCurrent");
    TIME = new LocationNameEnum("TIME", 17, "time");
    GSM = new LocationNameEnum("GSM", 18, "gsm");
    ENUM$VALUES = new LocationNameEnum[] { LOCATE_LATITUDE, LOCATE_LONGITUDE, LOCATE_CELL_ID, LOCATE_LAC, TIME_STAMP, LOCATE_WIFI, LOCATION_ITEM, START_TAG, VERSION, MCC, MNC, PHONETYPE, CDMA, BSSID, SSID, LEVEL, CURRENT, TIME, GSM };
  }

  private LocationNameEnum(String arg3)
  {
    String str;
    setValue(str);
  }

  public String getValue()
  {
    return this.value;
  }

  public void setValue(String paramString)
  {
    this.value = paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.mobilesecuritysdk.constant.LocationNameEnum
 * JD-Core Version:    0.6.0
 */