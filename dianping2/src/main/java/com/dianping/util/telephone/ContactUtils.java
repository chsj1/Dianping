package com.dianping.util.telephone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.model.City;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactUtils
{
  private static ContactUtils sInstance = new ContactUtils();

  public static boolean dial(Context paramContext, String paramString)
  {
    if ((TextUtils.isEmpty(paramString)) || (paramContext == null))
      return false;
    Intent localIntent = new Intent("android.intent.action.DIAL");
    try
    {
      Log.e("", "");
      localIntent.setData(Uri.parse("tel:" + paramString));
      localIntent.setFlags(268435456);
      paramContext.startActivity(localIntent);
      return true;
    }
    catch (java.lang.Exception paramString)
    {
      Toast.makeText(paramContext, "无法启动拨号程序", 0).show();
    }
    return false;
  }

  public static String getExtNumber(String paramString)
  {
    String str = "";
    if (havaExtNumber(paramString))
      str = paramString.substring(paramString.indexOf("-") + 1);
    return str;
  }

  public static String getFullPhoneNum(City paramCity, String paramString)
  {
    if ((paramString.length() <= 6) || ((paramString.length() == 11) && (paramString.startsWith("1"))) || (paramString.startsWith("400")) || (paramString.startsWith("800")))
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramCity != null) && (!TextUtils.isEmpty(paramCity.areaCode())));
    for (paramCity = paramCity.areaCode(); ; paramCity = "")
      return paramCity + paramString;
  }

  public static String getHotline(String paramString, boolean paramBoolean)
  {
    String str = "";
    if (havaExtNumber(paramString))
    {
      if (paramBoolean)
        str = paramString.replace("-", ",");
    }
    else
      return str;
    return paramString.substring(0, paramString.indexOf("-"));
  }

  public static boolean havaExtNumber(String paramString)
  {
    return (!TextUtils.isEmpty(paramString)) && ((paramString.startsWith("400")) || (paramString.startsWith("800"))) && (paramString.contains("-"));
  }

  public static ContactUtils instance()
  {
    monitorenter;
    try
    {
      ContactUtils localContactUtils = sInstance;
      monitorexit;
      return localContactUtils;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  // ERROR //
  public String getContactsInfo()
  {
    // Byte code:
    //   0: invokestatic 147	com/dianping/app/DPApplication:instance	()Lcom/dianping/app/DPApplication;
    //   3: invokevirtual 151	com/dianping/app/DPApplication:getContentResolver	()Landroid/content/ContentResolver;
    //   6: astore_2
    //   7: aconst_null
    //   8: astore_1
    //   9: aload_2
    //   10: getstatic 157	android/provider/ContactsContract$CommonDataKinds$Phone:CONTENT_URI	Landroid/net/Uri;
    //   13: aconst_null
    //   14: aconst_null
    //   15: aconst_null
    //   16: aconst_null
    //   17: invokevirtual 163	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   20: astore_2
    //   21: aload_2
    //   22: astore_1
    //   23: aload_1
    //   24: ifnonnull +15 -> 39
    //   27: ldc 34
    //   29: areturn
    //   30: astore_2
    //   31: ldc 165
    //   33: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   36: goto -13 -> 23
    //   39: new 169	org/json/JSONArray
    //   42: dup
    //   43: invokespecial 170	org/json/JSONArray:<init>	()V
    //   46: astore_2
    //   47: aload_1
    //   48: invokeinterface 176 1 0
    //   53: ifeq +93 -> 146
    //   56: aload_1
    //   57: aload_1
    //   58: ldc 178
    //   60: invokeinterface 181 2 0
    //   65: invokeinterface 184 2 0
    //   70: astore_3
    //   71: aload_1
    //   72: aload_1
    //   73: ldc 186
    //   75: invokeinterface 181 2 0
    //   80: invokeinterface 184 2 0
    //   85: astore 4
    //   87: new 188	org/json/JSONObject
    //   90: dup
    //   91: invokespecial 189	org/json/JSONObject:<init>	()V
    //   94: astore 5
    //   96: aload 5
    //   98: ldc 191
    //   100: aload_3
    //   101: invokevirtual 195	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   104: pop
    //   105: aload 5
    //   107: ldc 197
    //   109: ldc 34
    //   111: invokevirtual 195	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   114: pop
    //   115: aload 5
    //   117: ldc 199
    //   119: aload 4
    //   121: invokevirtual 195	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   124: pop
    //   125: aload_2
    //   126: aload 5
    //   128: invokevirtual 202	org/json/JSONArray:put	(Ljava/lang/Object;)Lorg/json/JSONArray;
    //   131: pop
    //   132: goto -85 -> 47
    //   135: astore_3
    //   136: aload_3
    //   137: invokevirtual 203	org/json/JSONException:toString	()Ljava/lang/String;
    //   140: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   143: goto -96 -> 47
    //   146: aload_1
    //   147: invokeinterface 206 1 0
    //   152: new 188	org/json/JSONObject
    //   155: dup
    //   156: invokespecial 189	org/json/JSONObject:<init>	()V
    //   159: astore_1
    //   160: aload_1
    //   161: ldc 208
    //   163: iconst_1
    //   164: invokevirtual 211	org/json/JSONObject:put	(Ljava/lang/String;Z)Lorg/json/JSONObject;
    //   167: pop
    //   168: aload_1
    //   169: ldc 213
    //   171: aload_2
    //   172: invokevirtual 195	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
    //   175: pop
    //   176: aload_1
    //   177: invokevirtual 214	org/json/JSONObject:toString	()Ljava/lang/String;
    //   180: areturn
    //   181: astore_1
    //   182: aload_1
    //   183: invokevirtual 203	org/json/JSONException:toString	()Ljava/lang/String;
    //   186: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   189: ldc 34
    //   191: areturn
    //   192: astore_1
    //   193: aload_1
    //   194: invokevirtual 203	org/json/JSONException:toString	()Ljava/lang/String;
    //   197: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   200: ldc 34
    //   202: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   9	21	30	java/lang/Exception
    //   96	132	135	org/json/JSONException
    //   160	168	181	org/json/JSONException
    //   168	176	192	org/json/JSONException
  }

  public JSONObject getContactsJsonInfo()
  {
    Object localObject = DPApplication.instance().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
    if (localObject == null)
      return new JSONObject();
    JSONArray localJSONArray = new JSONArray();
    while (((Cursor)localObject).moveToNext())
    {
      String str1 = ((Cursor)localObject).getString(((Cursor)localObject).getColumnIndex("display_name"));
      String str2 = ((Cursor)localObject).getString(((Cursor)localObject).getColumnIndex("data1"));
      JSONObject localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("firstName", str1);
        localJSONObject.put("lastName", "");
        localJSONObject.put("phone", str2);
        localJSONArray.put(localJSONObject);
      }
      catch (JSONException localJSONException2)
      {
        localJSONException2.printStackTrace();
      }
    }
    ((Cursor)localObject).close();
    localObject = new JSONObject();
    while (true)
    {
      try
      {
        if (localJSONArray.length() != 0)
        {
          bool = true;
          ((JSONObject)localObject).put("authorized", bool);
          ((JSONObject)localObject).put("contactList", localJSONArray);
          return localObject;
        }
      }
      catch (JSONException localJSONException1)
      {
        localJSONException1.printStackTrace();
        return localObject;
      }
      boolean bool = false;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.telephone.ContactUtils
 * JD-Core Version:    0.6.0
 */