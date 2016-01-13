package com.tencent.connect.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONObject;

public class AssistActivity extends Activity
{
  private static final String RESTART_FLAG = "RESTART_FLAG";
  private static final String TAG = "AssistActivity";
  private static BaseApi sApiObject;
  private BaseApi mAPiObject;

  public static Intent getAssistActivityIntent(Context paramContext)
  {
    return new Intent(paramContext, AssistActivity.class);
  }

  public static void setApiObject(BaseApi paramBaseApi)
  {
    sApiObject = paramBaseApi;
  }

  public static void setResultDataForLogin(Activity paramActivity, Intent paramIntent)
  {
    try
    {
      String str = paramIntent.getStringExtra("key_response");
      Log.d("AssistActivity", "AssistActivity--setResultDataForLogin-- " + str);
      if (!TextUtils.isEmpty(str))
      {
        Object localObject = new JSONObject(str);
        str = ((JSONObject)localObject).optString("openid");
        localObject = ((JSONObject)localObject).optString("access_token");
        if ((!TextUtils.isEmpty(str)) && (!TextUtils.isEmpty((CharSequence)localObject)))
        {
          paramActivity.setResult(10101, paramIntent);
          return;
        }
        paramActivity.setResult(12345, paramIntent);
        return;
      }
    }
    catch (Exception paramActivity)
    {
      paramActivity.printStackTrace();
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Log.d("AssistActivity", "AssistActivity--onActivityResult--" + paramInt2 + " data=" + paramIntent);
    Log.d("AssistActivity", "--requestCode: " + paramInt1 + " | resultCode: " + paramInt2 + " | data: " + paramIntent);
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (this.mAPiObject != null)
    {
      Log.d("AssistActivity", "AssistActivity--onActivityResult-- mAPiObject != null");
      this.mAPiObject.onActivityResult(this, paramInt1, paramInt2, paramIntent);
    }
    while (true)
    {
      finish();
      return;
      Log.d("AssistActivity", "AssistActivity--onActivityResult-- mAPiObject == null");
      setResultDataForLogin(this, paramIntent);
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    boolean bool = false;
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    Log.d("AssistActivity", "AssistActivity--onCreate--");
    if (sApiObject == null);
    int i;
    do
    {
      return;
      this.mAPiObject = sApiObject;
      sApiObject = null;
      i = this.mAPiObject.getActivityIntent().getIntExtra("key_request_code", 0);
      if (paramBundle == null)
        continue;
      bool = paramBundle.getBoolean("RESTART_FLAG");
    }
    while (bool);
    startActivityForResult(this.mAPiObject.getActivityIntent(), i);
  }

  protected void onDestroy()
  {
    Log.d("AssistActivity", "-->onDestroy");
    super.onDestroy();
  }

  protected void onPause()
  {
    Log.d("AssistActivity", "-->onPause");
    super.onPause();
  }

  protected void onResume()
  {
    Log.d("AssistActivity", "-->onResume");
    super.onResume();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    Log.d("AssistActivity", "AssistActivity--onSaveInstanceState--");
    paramBundle.putBoolean("RESTART_FLAG", true);
    super.onSaveInstanceState(paramBundle);
  }

  protected void onStart()
  {
    Log.d("AssistActivity", "-->onStart");
    super.onStart();
  }

  protected void onStop()
  {
    Log.d("AssistActivity", "-->onStop");
    super.onStop();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.connect.common.AssistActivity
 * JD-Core Version:    0.6.0
 */