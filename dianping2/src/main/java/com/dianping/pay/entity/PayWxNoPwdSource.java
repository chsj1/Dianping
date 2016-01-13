package com.dianping.pay.entity;

import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DeviceUtils;
import java.util.ArrayList;

public class PayWxNoPwdSource
{
  static final String IS_BIND_WX_NO_PWD_URL = "http://api.p.dianping.com/wxnopwduserdetails.pay";
  static final String TAG = PayWxNoPwdSource.class.getSimpleName();
  static final String UNBIND_WX_NO_PWD_URL = "http://api.p.dianping.com/rebindwxnopwd.pay";
  NovaActivity activity;
  MApiRequest isWxNoPwdReq;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new PayWxNoPwdSource.1(this);
  MApiRequest unBindWxNoPwdReq;
  private WxNoPwdResultListener wxNoPwdResultListener;

  public PayWxNoPwdSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
  }

  public void releaseRequests()
  {
    if (this.isWxNoPwdReq != null)
    {
      this.activity.mapiService().abort(this.isWxNoPwdReq, this.mapiHandler, true);
      this.isWxNoPwdReq = null;
    }
    if (this.unBindWxNoPwdReq != null)
    {
      this.activity.mapiService().abort(this.unBindWxNoPwdReq, this.mapiHandler, true);
      this.unBindWxNoPwdReq = null;
    }
  }

  public void reqIsWxNoPwd()
  {
    if (this.isWxNoPwdReq != null)
      this.activity.mapiService().abort(this.isWxNoPwdReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("payorder"));
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    this.isWxNoPwdReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/wxnopwduserdetails.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.isWxNoPwdReq, this.mapiHandler);
  }

  public void reqUnBindWxNoPwd()
  {
    if (this.unBindWxNoPwdReq != null)
      this.activity.mapiService().abort(this.unBindWxNoPwdReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("payorder"));
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    localArrayList.add("token");
    localArrayList.add(this.activity.accountService().token());
    localArrayList.add("source");
    localArrayList.add("1");
    this.unBindWxNoPwdReq = BasicMApiRequest.mapiPost("http://api.p.dianping.com/rebindwxnopwd.pay", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.unBindWxNoPwdReq, this.mapiHandler);
  }

  public void setWxNoPwdResultListener(WxNoPwdResultListener paramWxNoPwdResultListener)
  {
    this.wxNoPwdResultListener = paramWxNoPwdResultListener;
  }

  public static abstract interface WxNoPwdResultListener
  {
    public abstract void notOpen(DPObject paramDPObject);

    public abstract void opened(DPObject paramDPObject);

    public abstract void resendReq();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.entity.PayWxNoPwdSource
 * JD-Core Version:    0.6.0
 */