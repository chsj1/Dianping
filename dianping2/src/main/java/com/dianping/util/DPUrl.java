package com.dianping.util;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.dianping.app.Environment;
import com.dianping.util.log.NovaLog;
import org.json.JSONArray;
import org.json.JSONObject;

public class DPUrl
{
  private static final String TAG = "DPUrl";
  private Uri.Builder builder;
  private Intent mIntent;

  public DPUrl()
  {
    this("");
  }

  public DPUrl(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      this.builder = Uri.parse(paramString).buildUpon();
      checkValidChar(this.builder.build().getHost());
    }
  }

  private void checkValidChar(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      onError("DPUrl checkValidChar failed");
    while (true)
    {
      return;
      int i = 0;
      while (i < paramString.length())
      {
        int j = paramString.charAt(i);
        if ((j >= 65) && (j <= 90))
          onError("DPUrl checkValidChar failed: " + paramString);
        i += 1;
      }
    }
  }

  private void onError(String paramString)
  {
    if (Environment.isDebug())
      throw new RuntimeException(paramString);
    NovaLog.e("DPUrl", paramString);
  }

  public Intent getIntent()
  {
    if (this.mIntent == null)
      this.mIntent = new Intent();
    this.mIntent.setData(getUri());
    this.mIntent.setAction("android.intent.action.VIEW");
    return this.mIntent;
  }

  public Uri getUri()
  {
    if (this.builder != null)
      return this.builder.build();
    Log.e("DPUrl", "getUri = null");
    return Uri.EMPTY;
  }

  public DPUrl putParam(String paramString, double paramDouble)
  {
    return putParam(paramString, String.valueOf(paramDouble));
  }

  public DPUrl putParam(String paramString, int paramInt)
  {
    return putParam(paramString, String.valueOf(paramInt));
  }

  public DPUrl putParam(String paramString, Long paramLong)
  {
    return putParam(paramString, String.valueOf(paramLong));
  }

  public DPUrl putParam(String paramString1, String paramString2)
  {
    checkValidChar(paramString1);
    if (this.builder != null)
    {
      this.builder.appendQueryParameter(paramString1, paramString2);
      return this;
    }
    onError("not give url now");
    return this;
  }

  public DPUrl putParam(String paramString, JSONArray paramJSONArray)
  {
    return putParam(paramString, paramJSONArray.toString());
  }

  public DPUrl putParam(String paramString, JSONObject paramJSONObject)
  {
    return putParam(paramString, paramJSONObject.toString());
  }

  public DPUrl putParam(String paramString, boolean paramBoolean)
  {
    return putParam(paramString, String.valueOf(paramBoolean));
  }

  public DPUrl setData(Uri paramUri)
  {
    if (paramUri == null)
    {
      Log.e("DPUrl", "setData data = null");
      return this;
    }
    checkValidChar(paramUri.getHost());
    this.builder = paramUri.buildUpon();
    return this;
  }

  public DPUrl setUrl(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      this.builder = Uri.parse(paramString).buildUpon();
      checkValidChar(this.builder.build().getHost());
    }
    return this;
  }

  public String toString()
  {
    return getUri().toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.DPUrl
 * JD-Core Version:    0.6.0
 */