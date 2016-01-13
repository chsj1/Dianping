package com.dianping.base.basic;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import com.dianping.v1.R.layout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ParseQRUrlActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  static final String TAG = ParseQRUrlActivity.class.getSimpleName();
  private MApiRequest mMockRequest = null;
  private Uri mQrUri = null;

  private boolean handleUrl(Uri paramUri)
  {
    int j = 0;
    int i = j;
    if (paramUri != null)
    {
      if (!"parseqrurl".equals(paramUri.getHost()))
        break label546;
      paramUri = paramUri.getQueryParameter("qrurl");
      i = j;
      if (paramUri != null)
      {
        this.mQrUri = Uri.parse(paramUri);
        i = j;
        if (this.mQrUri != null)
        {
          statisticsEvent("searchtab5", "searchtab5_scan_success", paramUri, 0);
          if (!"dianping".equals(this.mQrUri.getScheme()))
            break label150;
        }
      }
    }
    label150: 
    do
    {
      try
      {
        startActivity(new Intent("android.intent.action.VIEW", this.mQrUri));
        setResult(-1);
        finish();
        i = 1;
        return i;
      }
      catch (Exception paramUri)
      {
        while (true)
        {
          Log.w(TAG, "url:" + this.mQrUri + " 有误");
          paramUri.printStackTrace();
        }
      }
      if ((!"http".equals(this.mQrUri.getScheme())) || ((!"dpurl.cn".equals(this.mQrUri.getHost())) && (!this.mQrUri.getHost().endsWith("dpurl.cn"))))
        break;
      i = j;
    }
    while (!this.mQrUri.getPath().startsWith("/m"));
    Log.i(TAG, "parse url " + this.mQrUri);
    new GetValidUri(this.mQrUri).execute(new Void[0]);
    return true;
    if ((Environment.isDebug()) && ("http".equals(this.mQrUri.getScheme())) && (this.mQrUri.getQuery() != null) && (this.mQrUri.getQuery().startsWith("_=*__*")))
    {
      onSwitch(this.mQrUri.toString());
      return false;
    }
    if ((Environment.isDebug()) && ("http".equals(this.mQrUri.getScheme())) && (this.mQrUri.getQuery() != null) && (this.mQrUri.getQuery().startsWith("_=0__0&uid=")))
    {
      showProgressDialog("正在注册请稍后");
      this.mMockRequest = BasicMApiRequest.mapiGet(Uri.parse(paramUri).toString(), CacheType.DISABLED);
      mapiService().exec(this.mMockRequest, this);
      return true;
    }
    if ("http".equals(this.mQrUri.getScheme()))
      try
      {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse(String.format("dianping://web?url=%s", new Object[] { URLEncoder.encode(paramUri) })));
        startActivity(localIntent);
        setResult(-1);
        finish();
        return true;
      }
      catch (Exception paramUri)
      {
        while (true)
          paramUri.printStackTrace();
      }
    paramUri = new AlertDialog.Builder(this).setMessage(paramUri).setTitle("是否打开此链接？").setPositiveButton("确定", new DialogInterface.OnClickListener(paramUri)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        try
        {
          paramDialogInterface = new Intent("android.intent.action.VIEW");
          paramDialogInterface.setData(Uri.parse(this.val$qrurlString));
          ParseQRUrlActivity.this.startActivity(paramDialogInterface);
          ParseQRUrlActivity.this.setResult(-1);
          ParseQRUrlActivity.this.finish();
          return;
        }
        catch (Exception paramDialogInterface)
        {
          while (true)
            paramDialogInterface.printStackTrace();
        }
      }
    }).setNegativeButton("取消 ", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        ParseQRUrlActivity.this.onBackPressed();
      }
    }).create();
    paramUri.setOnDismissListener(new DialogInterface.OnDismissListener()
    {
      public void onDismiss(DialogInterface paramDialogInterface)
      {
        ParseQRUrlActivity.this.onBackPressed();
      }
    });
    paramUri.show();
    return true;
    label546: statisticsEvent("searchtab5", "searchtab5_scan_success", paramUri.toString(), 0);
    new GetValidUri(paramUri).execute(new Void[0]);
    return true;
  }

  private void onResigerSucceed()
  {
    Object localObject = this.mQrUri.getScheme() + "://" + this.mQrUri.getEncodedAuthority() + "/";
    MApiDebugAgent localMApiDebugAgent = (MApiDebugAgent)getService("mapi_debug");
    localMApiDebugAgent.setSwitchDomain((String)localObject);
    localMApiDebugAgent.setMapiDomain((String)localObject);
    localMApiDebugAgent.setBookingDebugDomain((String)localObject);
    localMApiDebugAgent.setTDebugDomain((String)localObject);
    localMApiDebugAgent.setPayDebugDomain((String)localObject);
    localMApiDebugAgent.setMovieDebugDomain((String)localObject);
    localMApiDebugAgent.setMembercardDebugDomain((String)localObject);
    localMApiDebugAgent.setTakeawayDebugDomain((String)localObject);
    localMApiDebugAgent.setHuihuiDebugDomain((String)localObject);
    localMApiDebugAgent.setBeautyDebugDomain((String)localObject);
    localMApiDebugAgent.setConfigDebugDomain((String)localObject);
    getSharedPreferences("com.dianping.mapidebugagent", 0).edit().putString("setSwitchDomain", localMApiDebugAgent.switchDomain()).putString("setMapiDomain", localMApiDebugAgent.mapiDomain()).putString("setBookingDebugDomain", localMApiDebugAgent.bookingDebugDomain()).putString("setTDebugDomain", localMApiDebugAgent.tDebugDomain()).putString("setPayDebugDomain", localMApiDebugAgent.payDebugDomain()).putString("setMovieDebugDomain", localMApiDebugAgent.movieDebugDomain()).putString("setMembercardDebugDomain", localMApiDebugAgent.membercardDebugDomain()).putString("setTakeawayDebugDomain", localMApiDebugAgent.takeawayDebugDomain()).putString("setHuihuiDebugDomain", localMApiDebugAgent.huihuiDebugDomain()).putString("setBeautyDebugDomain", localMApiDebugAgent.beautyDebugDomain()).putString("setConfigDebugDomain", localMApiDebugAgent.configDebugDomain()).commit();
    showToast("注册成功");
    localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
    ((Intent)localObject).setFlags(67108864);
    startActivity((Intent)localObject);
    setResult(-1);
    finish();
  }

  private void onSwitch(String paramString)
  {
    paramString = paramString.substring("http://".length(), paramString.length() - "?_=*__*".length()).split("/");
    MApiDebugAgent localMApiDebugAgent = (MApiDebugAgent)getService("mapi_debug");
    localMApiDebugAgent.setTDebugDomain("http://" + paramString[0] + "/");
    localMApiDebugAgent.setPayDebugDomain("http://" + paramString[1] + "/");
    getSharedPreferences("com.dianping.mapidebugagent", 0).edit().putString("setTDebugDomain", localMApiDebugAgent.tDebugDomain()).putString("setPayDebugDomain", localMApiDebugAgent.payDebugDomain()).commit();
    showToast("切换环境成功");
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
    paramString.setFlags(67108864);
    startActivity(paramString);
    setResult(-1);
    finish();
  }

  private void syncCard(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(paramString);
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    mapiService().exec(BasicMApiRequest.mapiPost("http://mc.api.dianping.com/syncard.mc", (String[])localArrayList.toArray(new String[localArrayList.size()])), this);
  }

  boolean checkExist(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return false;
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("shoplist.bin?");
    paramString = BasicMApiRequest.mapiPost(localStringBuilder.toString(), new String[] { "ids", paramString });
    paramString = (MApiResponse)mapiService().execSync(paramString);
    return ((paramString.result() instanceof DPObject)) && (((DPObject)paramString.result()).getInt("RecordCount") > 0);
  }

  Uri getUrlFromServer(Uri paramUri)
  {
    paramUri = BasicHttpRequest.httpGet(paramUri.toString());
    paramUri = (HttpResponse)httpService().execSync(paramUri);
    if ((paramUri.result() instanceof byte[]))
    {
      paramUri = (byte[])(byte[])paramUri.result();
      try
      {
        paramUri = new String(paramUri, "UTF-8");
        int i = paramUri.indexOf("dianping://");
        if (i >= 0)
        {
          int j = paramUri.indexOf('"', i);
          if (j > i)
          {
            paramUri = Uri.parse(paramUri.substring(i, j));
            return paramUri;
          }
        }
      }
      catch (UnsupportedEncodingException paramUri)
      {
        paramUri.printStackTrace();
      }
    }
    return null;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.loading_1);
    paramBundle = getIntent();
    if (paramBundle.getBooleanExtra("landscape", false))
      setRequestedOrientation(0);
    if (!handleUrl(paramBundle.getData()))
    {
      paramBundle = (TextView)getLayoutInflater().inflate(17367043, null, false);
      paramBundle.setGravity(17);
      paramBundle.setText("此商户不存在，请扫描正确的二维码哦！");
      super.setContentView(paramBundle);
    }
  }

  protected void onPause()
  {
    super.onPause();
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    onBackPressed();
    if (paramMApiRequest == this.mMockRequest)
    {
      if (!"malformed content".equals(paramMApiResponse.error()))
        break label41;
      onResigerSucceed();
    }
    while (true)
    {
      this.mMockRequest = null;
      return;
      label41: showToast("注册失败");
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    onBackPressed();
    if (paramMApiRequest == this.mMockRequest)
    {
      onResigerSucceed();
      this.mMockRequest = null;
    }
  }

  class GetValidUri extends AsyncTask<Void, Void, Uri>
  {
    private Uri mSrcUri;

    GetValidUri(Uri arg2)
    {
      Object localObject;
      this.mSrcUri = localObject;
    }

    public Uri doInBackground(Void[] paramArrayOfVoid)
    {
      paramArrayOfVoid = this.mSrcUri.getPath().trim();
      if (paramArrayOfVoid.startsWith("/m/s"))
      {
        paramArrayOfVoid = paramArrayOfVoid.substring(4);
        boolean bool = ParseQRUrlActivity.this.checkExist(paramArrayOfVoid);
        Log.i(ParseQRUrlActivity.TAG, "get shopid = " + paramArrayOfVoid);
        if (bool)
          return Uri.parse("dianping://shopinfo?id=" + paramArrayOfVoid);
      }
      else if (paramArrayOfVoid.startsWith("/m"))
      {
        return ParseQRUrlActivity.this.getUrlFromServer(this.mSrcUri);
      }
      return null;
    }

    public void onPostExecute(Uri paramUri)
    {
      if (paramUri != null)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(paramUri);
        ParseQRUrlActivity.this.startActivity(localIntent);
        ParseQRUrlActivity.this.setResult(-1);
        ParseQRUrlActivity.this.finish();
        return;
      }
      paramUri = (TextView)ParseQRUrlActivity.this.getLayoutInflater().inflate(17367043, null, false);
      paramUri.setGravity(17);
      paramUri.setText("此商户不存在，请扫描正确的二维码哦！");
      ParseQRUrlActivity.this.setContentView(paramUri);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.ParseQRUrlActivity
 * JD-Core Version:    0.6.0
 */