package com.dianping.v1.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.base.thirdparty.wxapi.WXHelper.IWxApiListener;
import com.dianping.base.thirdparty.wxapi.WeiXinCard;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.util.Log;
import com.dianping.util.encrypt.Md5;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX.Req;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends Activity
  implements IWXAPIEventHandler
{
  private IWXAPI mWXApi;

  private void handleIntent()
  {
    this.mWXApi = WXHelper.getWXAPI(this);
    if (this.mWXApi != null)
      this.mWXApi.handleIntent(getIntent(), this);
  }

  private void onSsoFail()
  {
    Intent localIntent;
    if (!getSharedPreferences(getPackageName(), 0).getBoolean("wxssobindtag", false))
    {
      localIntent = new Intent("android.intent.action.VIEW");
      if (!ConfigHelper.disableweblogin)
        break label67;
      localIntent.setData(Uri.parse("dianping://login"));
      localIntent.setFlags(131072);
    }
    while (true)
    {
      startActivity(localIntent);
      removeWXSSOInfo();
      finish();
      return;
      label67: localIntent.setData(Uri.parse("dianping://loginweb"));
      localIntent.setFlags(268435456);
    }
  }

  private void removeWXSSOInfo()
  {
    SharedPreferences.Editor localEditor = getSharedPreferences(getPackageName(), 0).edit();
    localEditor.remove("wxssobindtag");
    localEditor.remove("wxssologincallbackurl");
    localEditor.remove("wxssologinstate");
    localEditor.commit();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.requestWindowFeature(1);
    handleIntent();
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    handleIntent();
  }

  public void onReq(BaseReq paramBaseReq)
  {
    if ((paramBaseReq instanceof ShowMessageFromWX.Req))
      try
      {
        paramBaseReq = (WXAppExtendObject)((ShowMessageFromWX.Req)paramBaseReq).message.mediaObject;
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse(paramBaseReq.extInfo));
        startActivity(localIntent);
        finish();
        return;
      }
      catch (Exception paramBaseReq)
      {
        while (true)
        {
          paramBaseReq.printStackTrace();
          paramBaseReq = new Intent("android.intent.action.VIEW");
          paramBaseReq.setData(Uri.parse("dianping://home"));
          startActivity(paramBaseReq);
        }
      }
    paramBaseReq = new Intent("android.intent.action.VIEW", Uri.parse("dianping://wxadapter"));
    paramBaseReq.putExtras(getIntent());
    startActivity(paramBaseReq);
    finish();
  }

  public void onResp(BaseResp paramBaseResp)
  {
    Object localObject1;
    if (paramBaseResp.getType() == 1)
    {
      paramBaseResp = (SendAuth.Resp)paramBaseResp;
      if (TextUtils.isEmpty(paramBaseResp.code))
      {
        Toast.makeText(this, "用户取消", 0).show();
        onSsoFail();
        return;
      }
      localObject1 = getSharedPreferences(getPackageName(), 0);
      if (!((SharedPreferences)localObject1).getString("wxssologinstate", "").equals(paramBaseResp.state))
      {
        Toast.makeText(this, "请求不合法", 0).show();
        onSsoFail();
        return;
      }
      if (((SharedPreferences)localObject1).getBoolean("wxssobindtag", false))
      {
        localObject2 = new JSONObject();
        try
        {
          ((JSONObject)localObject2).put("code", paramBaseResp.code);
          ((JSONObject)localObject2).put("state", paramBaseResp.state);
          ((JSONObject)localObject2).put("sign", Md5.md5(paramBaseResp.code + "dpwxssodpwxantiattach9527"));
          paramBaseResp = ((SharedPreferences)localObject1).edit();
          paramBaseResp.putString("wxssobindresp", ((JSONObject)localObject2).toString());
          paramBaseResp.commit();
          removeWXSSOInfo();
          finish();
          return;
        }
        catch (JSONException paramBaseResp)
        {
          while (true)
            Log.e(paramBaseResp.toString());
        }
      }
      Object localObject2 = new Intent("android.intent.action.VIEW");
      ((Intent)localObject2).setData(Uri.parse("dianping://loginweb"));
      ((Intent)localObject2).setFlags(268435456);
      String str = Md5.md5(paramBaseResp.code + "dpwxssodpwxantiattach9527");
      localObject1 = ((SharedPreferences)localObject1).getString("wxssologincallbackurl", "");
      paramBaseResp = (String)localObject1 + "&code=" + paramBaseResp.code + "&sign=" + str;
      removeWXSSOInfo();
      ((Intent)localObject2).putExtra("url", paramBaseResp);
      startActivity((Intent)localObject2);
    }
    do
      while (true)
      {
        finish();
        return;
        if (paramBaseResp.getType() != 9)
          break;
        Log.v("WXEntryActivity", "COMMAND_ADD_CARD_TO_EX_CARD_PACKAGE");
        localObject1 = new Bundle();
        paramBaseResp.toBundle((Bundle)localObject1);
        WeiXinCard.instance().addToWeiXinCardResult((Bundle)localObject1);
      }
    while ((paramBaseResp.getType() != 2) || (WXHelper.getWxApiListener() == null));
    if (paramBaseResp.errCode == 0)
      WXHelper.getWxApiListener().onSucess();
    while (true)
    {
      WXHelper.unregisterWxApiListener();
      break;
      if (paramBaseResp.errCode == -2)
      {
        WXHelper.getWxApiListener().onCancel();
        continue;
      }
      WXHelper.getWxApiListener().onError();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.v1.wxapi.WXEntryActivity
 * JD-Core Version:    0.6.0
 */