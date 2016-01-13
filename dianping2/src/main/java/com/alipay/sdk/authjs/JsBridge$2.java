package com.alipay.sdk.authjs;

import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

class JsBridge$2 extends TimerTask
{
  public void run()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("toastCallBack", "true");
      CallInfo localCallInfo = new CallInfo("callback");
      localCallInfo.a(this.a.a());
      localCallInfo.a(localJSONObject);
      JsBridge.a(this.b).a(localCallInfo);
      return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localJSONException.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.authjs.JsBridge.2
 * JD-Core Version:    0.6.0
 */