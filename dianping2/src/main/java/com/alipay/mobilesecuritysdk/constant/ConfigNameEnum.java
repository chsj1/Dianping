package com.alipay.mobilesecuritysdk.constant;

public enum ConfigNameEnum
{
  private String value;

  static
  {
    MAIN_SWITCH_INTERVAL = new ConfigNameEnum("MAIN_SWITCH_INTERVAL", 2, "mainSwitchInterval");
    LOCATE_LUT = new ConfigNameEnum("LOCATE_LUT", 3, "locateLUT");
    LOCATE_INTERVAL = new ConfigNameEnum("LOCATE_INTERVAL", 4, "locateInterval");
    APP_LUT = new ConfigNameEnum("APP_LUT", 5, "appLUT");
    APP_INTERVAL = new ConfigNameEnum("APP_INTERVAL", 6, "appInterval");
    PACKAGE_CHANGED = new ConfigNameEnum("PACKAGE_CHANGED", 7, "pkgchanged");
    LOCATION_MAX_LINES = new ConfigNameEnum("LOCATION_MAX_LINES", 8, "locationMaxLines");
    CONFIGS = new ConfigNameEnum("CONFIGS", 9, "configs");
    PKG_NAME = new ConfigNameEnum("PKG_NAME", 10, "n");
    PUB_KEY_HASH = new ConfigNameEnum("PUB_KEY_HASH", 11, "h");
    APP_ITEM = new ConfigNameEnum("APP_ITEM", 12, "appitem");
    START_TAG = new ConfigNameEnum("START_TAG", 13, "apps");
    ENUM$VALUES = new ConfigNameEnum[] { MAIN_SWITCH_LUT, MAIN_SWITCH_STATE, MAIN_SWITCH_INTERVAL, LOCATE_LUT, LOCATE_INTERVAL, APP_LUT, APP_INTERVAL, PACKAGE_CHANGED, LOCATION_MAX_LINES, CONFIGS, PKG_NAME, PUB_KEY_HASH, APP_ITEM, START_TAG };
  }

  private ConfigNameEnum(String arg3)
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
 * Qualified Name:     com.alipay.mobilesecuritysdk.constant.ConfigNameEnum
 * JD-Core Version:    0.6.0
 */