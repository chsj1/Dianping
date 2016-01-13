package com.tencent.share;

public final class Constants
{
  public class ParamValue
  {
    public static final String APPID = "params_appid";
    public static final String RET_ACTION_URL = "detail_url";
    public static final String RET_PIC_SRC = "image_url_remote";
    public static final String RET_SHARE_REQ_ID = "req_share_id";
    public static final String RET_SUMMARY = "desc";
    public static final String RET_TITLE = "title";
    public static final String SRC_ACTIVITY_CLASS_NAME = "srcClassName";
    public static final String SRC_PACKAGE_NAME = "srcPackageName";

    public ParamValue()
    {
    }
  }

  public class RetCode
  {
    public static final int CODE_NOT_INIT = 4;
    public static final int CODE_PARAM_ERROR = 3;
    public static final int CODE_REQ_DATA_NULL = 1;
    public static final int CODE_REQ_PARAM_ERROR = 2;
    public static final int CODE_SUCCESS = 0;

    public RetCode()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.share.Constants
 * JD-Core Version:    0.6.0
 */