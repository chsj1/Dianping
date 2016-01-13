package com.dianping.util;

import android.content.Context;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.dataservice.StringInputStream;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import org.apache.http.message.BasicNameValuePair;

public class PhotoUploadStore
{
  private static PhotoUploadStore instance;
  private MApiService mapiService = (MApiService)DPApplication.instance().getService("mapi");

  private PhotoUploadStore(Context paramContext)
  {
  }

  public static PhotoUploadStore instance()
  {
    if (instance == null)
      instance = new PhotoUploadStore(DPApplication.instance());
    return instance;
  }

  public MApiResponse doUploadPhoto(String paramString, HashMap<String, String> paramHashMap, InputStream paramInputStream)
  {
    if ((paramInputStream == null) || (paramHashMap == null))
    {
      Log.e("doUploadPhoto error ");
      return null;
    }
    Object localObject1 = new Random(System.currentTimeMillis());
    String str = "----------ANDRIOD_" + Long.toString(((Random)localObject1).nextLong());
    localObject1 = new ArrayList();
    ((ArrayList)localObject1).add(new BasicNameValuePair("connection", "keep-alive"));
    ((ArrayList)localObject1).add(new BasicNameValuePair("Charsert", "UTF-8"));
    ((ArrayList)localObject1).add(new BasicNameValuePair("Content-Type", "multipart/form-data; boundary=" + str));
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("--").append(str).append("\r\n");
    ((StringBuilder)localObject2).append("Content-Disposition: form-data; name=\"UploadFile2\"; filename=\"dianping_upload.jpg\"").append("\r\n");
    ((StringBuilder)localObject2).append("Content-Type: image/jpeg\r\n");
    ((StringBuilder)localObject2).append("\r\n");
    localObject2 = new StringInputStream(((StringBuilder)localObject2).toString());
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("\r\n");
    ((StringBuilder)localObject3).append("--").append(str).append("\r\n");
    ((StringBuilder)localObject3).append("Content-Disposition: form-data; name=\"be_data\"; filename=\"be_data\"").append("\r\n");
    ((StringBuilder)localObject3).append("Content-Type: application/octet-stream\r\n");
    ((StringBuilder)localObject3).append("Content-Transfer-Encoding: binary\r\n");
    ((StringBuilder)localObject3).append("\r\n");
    localObject3 = new StringInputStream(((StringBuilder)localObject3).toString());
    Object localObject4 = new ArrayList();
    paramHashMap = paramHashMap.entrySet().iterator();
    while (paramHashMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramHashMap.next();
      if (TextUtils.isEmpty((CharSequence)localEntry.getValue()))
        continue;
      ((ArrayList)localObject4).add(new BasicNameValuePair((String)localEntry.getKey(), (String)localEntry.getValue()));
    }
    paramHashMap = new MApiFormInputStream((List)localObject4);
    localObject4 = new StringBuilder();
    ((StringBuilder)localObject4).append("\r\n");
    ((StringBuilder)localObject4).append("--").append(str).append("--").append("\r\n");
    paramHashMap = new ChainInputStream(new InputStream[] { localObject2, paramInputStream, localObject3, paramHashMap, new StringInputStream(((StringBuilder)localObject4).toString()) });
    if (TextUtils.isEmpty(paramString))
      paramString = "http://m.api.dianping.com/addshopphoto.bin";
    while (true)
    {
      paramString = new BasicMApiRequest(paramString, "POST", paramHashMap, CacheType.DISABLED, false, (List)localObject1);
      paramString = (MApiResponse)this.mapiService.execSync(paramString);
      try
      {
        paramHashMap.close();
        return paramString;
      }
      catch (IOException paramHashMap)
      {
        paramHashMap.printStackTrace();
        return paramString;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.PhotoUploadStore
 * JD-Core Version:    0.6.0
 */