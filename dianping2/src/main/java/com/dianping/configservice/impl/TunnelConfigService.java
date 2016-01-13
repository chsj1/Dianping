package com.dianping.configservice.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.app.DPApplication;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import java.io.File;

public class TunnelConfigService extends DefaultConfigService
{
  private Context context;

  public TunnelConfigService(Context paramContext, MApiService paramMApiService)
  {
    super(paramContext, paramMApiService);
    this.context = paramContext;
  }

  protected MApiRequest createRequest()
  {
    Object localObject = "http://m.api.dianping.com/framework/tunnelconfig.bin";
    if (this.context.getSharedPreferences("com.dianping.mapidebugagent", 0).getBoolean("tunnelConfig", false))
      localObject = "http://m.api.dianping.com/framework/tunnelconfig.bin".replace("dianping", "51ping");
    localObject = Uri.parse((String)localObject).buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("cityid", String.valueOf(DPApplication.instance().cityId())).appendQueryParameter("appid", String.valueOf(DPApplication.instance().wnsAppId()));
    return (MApiRequest)new BasicMApiRequest(((Uri.Builder)localObject).toString(), "GET", null, CacheType.DISABLED, false, null);
  }

  protected File getConfigDir()
  {
    File localFile = new File(this.context.getFilesDir(), "tunnel_config");
    if (!localFile.isDirectory())
    {
      localFile.delete();
      localFile.mkdir();
    }
    return localFile;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.configservice.impl.TunnelConfigService
 * JD-Core Version:    0.6.0
 */