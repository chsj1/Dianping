package com.dianping.debug;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaApplication;
import com.dianping.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class UrlschemeService extends Service
{
  private static final String TAG = "testservice";
  private AccountService accountService;
  String device = "";
  String domain = "";
  boolean isRunning = true;
  Runnable r = new Runnable()
  {
    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: getstatic 29	java/lang/System:out	Ljava/io/PrintStream;
      //   3: ldc 31
      //   5: invokevirtual 37	java/io/PrintStream:println	(Ljava/lang/String;)V
      //   8: aload_0
      //   9: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   12: getfield 41	com/dianping/debug/UrlschemeService:isRunning	Z
      //   15: ifeq +525 -> 540
      //   18: ldc 43
      //   20: ldc 45
      //   22: invokestatic 51	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
      //   25: aconst_null
      //   26: astore 8
      //   28: aconst_null
      //   29: astore 7
      //   31: aconst_null
      //   32: astore 4
      //   34: aconst_null
      //   35: astore_1
      //   36: aconst_null
      //   37: astore 9
      //   39: aconst_null
      //   40: astore 6
      //   42: aconst_null
      //   43: astore 5
      //   45: aload 8
      //   47: astore_2
      //   48: aload 9
      //   50: astore_3
      //   51: ldc 43
      //   53: new 53	java/lang/StringBuilder
      //   56: dup
      //   57: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   60: ldc 56
      //   62: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   65: aload_0
      //   66: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   69: getfield 64	com/dianping/debug/UrlschemeService:domain	Ljava/lang/String;
      //   72: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   75: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   78: invokestatic 51	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
      //   81: aload 8
      //   83: astore_2
      //   84: aload 9
      //   86: astore_3
      //   87: new 70	java/net/URL
      //   90: dup
      //   91: new 53	java/lang/StringBuilder
      //   94: dup
      //   95: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   98: ldc 72
      //   100: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   103: aload_0
      //   104: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   107: getfield 64	com/dianping/debug/UrlschemeService:domain	Ljava/lang/String;
      //   110: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   113: ldc 74
      //   115: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   118: aload_0
      //   119: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   122: getfield 77	com/dianping/debug/UrlschemeService:device	Ljava/lang/String;
      //   125: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   128: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   131: invokespecial 79	java/net/URL:<init>	(Ljava/lang/String;)V
      //   134: astore 8
      //   136: aload 7
      //   138: astore_2
      //   139: aload 4
      //   141: astore_3
      //   142: aload 8
      //   144: invokevirtual 83	java/net/URL:openConnection	()Ljava/net/URLConnection;
      //   147: checkcast 85	java/net/HttpURLConnection
      //   150: astore_1
      //   151: aload_1
      //   152: astore_2
      //   153: aload_1
      //   154: astore_3
      //   155: new 87	java/io/InputStreamReader
      //   158: dup
      //   159: aload_1
      //   160: invokevirtual 91	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
      //   163: invokespecial 94	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
      //   166: astore 4
      //   168: new 96	java/io/BufferedReader
      //   171: dup
      //   172: aload 4
      //   174: invokespecial 99	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
      //   177: astore_2
      //   178: new 101	java/lang/StringBuffer
      //   181: dup
      //   182: invokespecial 102	java/lang/StringBuffer:<init>	()V
      //   185: astore_3
      //   186: aload_2
      //   187: invokevirtual 105	java/io/BufferedReader:readLine	()Ljava/lang/String;
      //   190: astore 5
      //   192: aload 5
      //   194: ifnull +60 -> 254
      //   197: aload_3
      //   198: aload 5
      //   200: invokevirtual 108	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   203: pop
      //   204: goto -18 -> 186
      //   207: astore_2
      //   208: aload 4
      //   210: astore 5
      //   212: aload_2
      //   213: astore 4
      //   215: aload_1
      //   216: astore_2
      //   217: aload 5
      //   219: astore_3
      //   220: aload 4
      //   222: invokevirtual 111	java/lang/Exception:printStackTrace	()V
      //   225: aload_1
      //   226: ifnull +7 -> 233
      //   229: aload_1
      //   230: invokevirtual 114	java/net/HttpURLConnection:disconnect	()V
      //   233: aload 5
      //   235: ifnull -227 -> 8
      //   238: aload 5
      //   240: invokevirtual 117	java/io/InputStreamReader:close	()V
      //   243: goto -235 -> 8
      //   246: astore_1
      //   247: aload_1
      //   248: invokevirtual 118	java/io/IOException:printStackTrace	()V
      //   251: goto -243 -> 8
      //   254: aload_3
      //   255: invokevirtual 119	java/lang/StringBuffer:toString	()Ljava/lang/String;
      //   258: astore_2
      //   259: ldc 43
      //   261: aload_2
      //   262: invokestatic 51	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
      //   265: new 121	org/json/JSONObject
      //   268: dup
      //   269: aload_2
      //   270: invokespecial 122	org/json/JSONObject:<init>	(Ljava/lang/String;)V
      //   273: astore_2
      //   274: aload_0
      //   275: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   278: aload_2
      //   279: invokevirtual 126	com/dianping/debug/UrlschemeService:handle	(Lorg/json/JSONObject;)V
      //   282: aload_0
      //   283: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   286: getfield 129	com/dianping/debug/UrlschemeService:tempTime	Ljava/lang/String;
      //   289: aload_2
      //   290: ldc 131
      //   292: invokevirtual 135	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   295: invokevirtual 141	java/lang/String:contentEquals	(Ljava/lang/CharSequence;)Z
      //   298: ifne +137 -> 435
      //   301: ldc 43
      //   303: new 53	java/lang/StringBuilder
      //   306: dup
      //   307: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   310: ldc 143
      //   312: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   315: aload_0
      //   316: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   319: getfield 146	com/dianping/debug/UrlschemeService:tempScheme	Ljava/lang/String;
      //   322: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   325: ldc 148
      //   327: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   330: aload_2
      //   331: ldc 150
      //   333: invokevirtual 135	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   336: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   339: ldc 152
      //   341: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   344: aload_2
      //   345: ldc 131
      //   347: invokevirtual 135	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   350: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   353: ldc 154
      //   355: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   358: aload_0
      //   359: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   362: getfield 129	com/dianping/debug/UrlschemeService:tempTime	Ljava/lang/String;
      //   365: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   368: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   371: invokestatic 51	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
      //   374: aload_0
      //   375: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   378: aload_2
      //   379: ldc 150
      //   381: invokevirtual 135	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   384: putfield 146	com/dianping/debug/UrlschemeService:tempScheme	Ljava/lang/String;
      //   387: aload_0
      //   388: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   391: aload_2
      //   392: ldc 131
      //   394: invokevirtual 135	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   397: putfield 129	com/dianping/debug/UrlschemeService:tempTime	Ljava/lang/String;
      //   400: new 156	android/content/Intent
      //   403: dup
      //   404: ldc 158
      //   406: aload_0
      //   407: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   410: getfield 146	com/dianping/debug/UrlschemeService:tempScheme	Ljava/lang/String;
      //   413: invokestatic 164	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
      //   416: invokespecial 167	android/content/Intent:<init>	(Ljava/lang/String;Landroid/net/Uri;)V
      //   419: astore_2
      //   420: aload_2
      //   421: ldc 168
      //   423: invokevirtual 172	android/content/Intent:setFlags	(I)Landroid/content/Intent;
      //   426: pop
      //   427: aload_0
      //   428: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   431: aload_2
      //   432: invokevirtual 176	com/dianping/debug/UrlschemeService:startActivity	(Landroid/content/Intent;)V
      //   435: ldc 43
      //   437: new 53	java/lang/StringBuilder
      //   440: dup
      //   441: invokespecial 54	java/lang/StringBuilder:<init>	()V
      //   444: ldc 178
      //   446: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   449: aload_0
      //   450: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   453: getfield 146	com/dianping/debug/UrlschemeService:tempScheme	Ljava/lang/String;
      //   456: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   459: ldc 154
      //   461: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   464: aload_0
      //   465: getfield 14	com/dianping/debug/UrlschemeService$1:this$0	Lcom/dianping/debug/UrlschemeService;
      //   468: getfield 129	com/dianping/debug/UrlschemeService:tempTime	Ljava/lang/String;
      //   471: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   474: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   477: invokestatic 51	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
      //   480: aload_1
      //   481: ifnull +7 -> 488
      //   484: aload_1
      //   485: invokevirtual 114	java/net/HttpURLConnection:disconnect	()V
      //   488: aload 4
      //   490: ifnull +83 -> 573
      //   493: aload 4
      //   495: invokevirtual 117	java/io/InputStreamReader:close	()V
      //   498: goto -490 -> 8
      //   501: astore_1
      //   502: aload_1
      //   503: invokevirtual 118	java/io/IOException:printStackTrace	()V
      //   506: goto -498 -> 8
      //   509: astore 4
      //   511: aload_2
      //   512: astore_1
      //   513: aload_1
      //   514: ifnull +7 -> 521
      //   517: aload_1
      //   518: invokevirtual 114	java/net/HttpURLConnection:disconnect	()V
      //   521: aload_3
      //   522: ifnull +7 -> 529
      //   525: aload_3
      //   526: invokevirtual 117	java/io/InputStreamReader:close	()V
      //   529: aload 4
      //   531: athrow
      //   532: astore_1
      //   533: aload_1
      //   534: invokevirtual 118	java/io/IOException:printStackTrace	()V
      //   537: goto -8 -> 529
      //   540: return
      //   541: astore 4
      //   543: aload_2
      //   544: astore_1
      //   545: aload 6
      //   547: astore_3
      //   548: goto -35 -> 513
      //   551: astore_2
      //   552: aload 4
      //   554: astore_3
      //   555: aload_2
      //   556: astore 4
      //   558: goto -45 -> 513
      //   561: astore 4
      //   563: goto -348 -> 215
      //   566: astore 4
      //   568: aload_3
      //   569: astore_1
      //   570: goto -355 -> 215
      //   573: goto -565 -> 8
      //
      // Exception table:
      //   from	to	target	type
      //   168	186	207	java/lang/Exception
      //   186	192	207	java/lang/Exception
      //   197	204	207	java/lang/Exception
      //   254	435	207	java/lang/Exception
      //   435	480	207	java/lang/Exception
      //   238	243	246	java/io/IOException
      //   493	498	501	java/io/IOException
      //   51	81	509	finally
      //   87	136	509	finally
      //   220	225	509	finally
      //   525	529	532	java/io/IOException
      //   142	151	541	finally
      //   155	168	541	finally
      //   168	186	551	finally
      //   186	192	551	finally
      //   197	204	551	finally
      //   254	435	551	finally
      //   435	480	551	finally
      //   51	81	561	java/lang/Exception
      //   87	136	561	java/lang/Exception
      //   142	151	566	java/lang/Exception
      //   155	168	566	java/lang/Exception
    }
  };
  String tempScheme = "";
  String tempTime = "";

  private void clearAppUserData(String paramString)
  {
    if (execRuntimeProcess("pm clear " + paramString) == null)
    {
      Log.i("testservice", "Clear app data packageName:" + paramString + ", FAILED !");
      return;
    }
    Log.i("testservice", "Clear app data packageName:" + paramString + ", SUCCESS !");
  }

  private java.lang.Process execRuntimeProcess(String paramString)
  {
    try
    {
      paramString = Runtime.getRuntime().exec(paramString);
      return paramString;
    }
    catch (IOException paramString)
    {
      paramString.printStackTrace();
    }
    return null;
  }

  private void login()
  {
    Object localObject = new ArrayList();
    ((ArrayList)localObject).add(new DPObject("UserProfile").edit().putInt("UserID", 7602590).putString("NickName", "li小la").putString("Avatar", "http://i2.dpfile.com/pc/21f328e7da240e0c9087358b33297836(96c96)/thumb.jpg").putInt("BadgeCount", 10).putInt("CheckInCount", 45).putInt("CityId", 1).putString("Email", "lixiaola@gmail.com").putString("Token", "cc5a2a6ffdec74923138334b724bf48c633efdaa3c5ac4e57a0bfc44781b84a4").putString("PhoneNo", "12345678901").generate());
    localObject = (DPObject)((ArrayList)localObject).get(0);
    if (this.accountService == null)
      this.accountService = ((AccountService)NovaApplication.instance().getService("account"));
    this.accountService.update((DPObject)localObject);
    try
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://me"));
      ((Intent)localObject).setFlags(268435456);
      startActivity((Intent)localObject);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  private void logout()
  {
    if (this.accountService == null)
      this.accountService = ((AccountService)NovaApplication.instance().getService("account"));
    this.accountService.logout();
    try
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://me"));
      localIntent.setFlags(268435456);
      startActivity(localIntent);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  void handle(JSONObject paramJSONObject)
    throws JSONException
  {
    if (paramJSONObject.getString("type").contentEquals("command"))
      if (paramJSONObject.getString("content").contentEquals("login"))
      {
        login();
        Log.i("testservice", "login");
      }
    do
    {
      return;
      if (paramJSONObject.getString("content").contentEquals("kill"))
      {
        Log.i("testservice", "kill");
        android.os.Process.killProcess(android.os.Process.myPid());
        return;
      }
      if (paramJSONObject.getString("content").contentEquals("delogin"))
      {
        Log.i("testservice", "delogin");
        logout();
        return;
      }
      if (!paramJSONObject.getString("content").contentEquals("clean-cache"))
        continue;
      Log.i("testservice", "clean-cache");
      clearAppUserData("com.dianping.v1");
      return;
    }
    while ((!paramJSONObject.getString("type").contentEquals("scheme")) || (this.tempScheme.contentEquals(paramJSONObject.getString("urlscheme"))) || (this.tempTime.contentEquals(paramJSONObject.getString("time"))));
    Log.i("testservice", "inside====tempscheme=" + this.tempScheme + "jsonObj.concat(urlscheme)=" + paramJSONObject.getString("urlscheme") + "jsonObj.concat(time)=" + paramJSONObject.getString("time") + "temptime=" + this.tempTime);
    this.tempScheme = paramJSONObject.getString("urlscheme");
    this.tempTime = paramJSONObject.getString("time");
    paramJSONObject = new Intent("android.intent.action.VIEW", Uri.parse(this.tempScheme));
    paramJSONObject.setFlags(268435456);
    startActivity(paramJSONObject);
  }

  public IBinder onBind(Intent paramIntent)
  {
    Log.i("testservice", "bind");
    return null;
  }

  public void onCreate()
  {
    super.onCreate();
    Log.i("testservice", "service created");
    Log.i("testservice", "再次得到的domain=" + this.domain);
    Toast.makeText(this, "开始测试", 0).show();
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.isRunning = false;
    Log.i("testservice", "destroyed");
    Toast.makeText(this, "结束测试", 0).show();
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if (paramIntent != null)
    {
      String str = paramIntent.getStringExtra("Command");
      if ((str != null) && ("Exit".equals(str)))
        stopSelf();
      this.device = paramIntent.getStringExtra("device");
      if (this.device != null)
        this.device = this.device.replaceAll(" +", "+");
      this.domain = paramIntent.getStringExtra("domain");
      if (this.domain != null)
      {
        if (this.domain.startsWith("http://"))
          this.domain = this.domain.substring(7);
        if (this.domain.endsWith("/"))
          this.domain = this.domain.substring(0, this.domain.length() - 1);
        Log.i("testservice", "获取到的domain=" + this.domain);
        new Thread(this.r).start();
      }
    }
    return 2;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.UrlschemeService
 * JD-Core Version:    0.6.0
 */