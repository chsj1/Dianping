package com.dianping.base.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.base.app.NovaApplication;
import com.dianping.configservice.ConfigService;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.util.Log;
import java.io.File;

public class CssStyleManager
{
  public static final String TUAN_CSS_APP_VERSION = "tuan_css_app_version";
  public static final String TUAN_CSS_PREFIX = "dianpingtuancssprefix";
  public static final String TUAN_CSS_STYLE_FILE = "tuan.css";
  public static final String TUAN_CSS_STYLE_VERSION = "tuan_css_version";
  private static CssStyleManager instance = null;
  protected Context context = null;
  HttpRequest httpRequest;

  private CssStyleManager(Context paramContext)
  {
    this.context = paramContext.getApplicationContext();
  }

  public static CssStyleManager instance(Context paramContext)
  {
    if (instance == null)
    {
      if (paramContext == null)
      {
        Log.e("You can't init CssStyleManager with a null context!");
        return null;
      }
      instance = new CssStyleManager(paramContext);
    }
    return instance;
  }

  protected boolean checkAppVersion()
  {
    try
    {
      String str = DPActivity.preferences(this.context).getString("tuan_css_app_version", "");
      boolean bool = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName.equals(str);
      return bool;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  // ERROR //
  protected boolean checkFile(File paramFile)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 4
    //   3: aconst_null
    //   4: astore_2
    //   5: aconst_null
    //   6: astore_3
    //   7: new 104	java/io/BufferedReader
    //   10: dup
    //   11: new 106	java/io/InputStreamReader
    //   14: dup
    //   15: new 108	java/io/FileInputStream
    //   18: dup
    //   19: aload_1
    //   20: invokespecial 111	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   23: invokespecial 114	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   26: invokespecial 117	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   29: astore_1
    //   30: aload_1
    //   31: invokevirtual 120	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   34: ldc 15
    //   36: invokevirtual 124	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   39: istore 5
    //   41: iload 5
    //   43: ifeq +25 -> 68
    //   46: iconst_1
    //   47: istore 4
    //   49: aload_1
    //   50: ifnull +7 -> 57
    //   53: aload_1
    //   54: invokevirtual 127	java/io/BufferedReader:close	()V
    //   57: iload 4
    //   59: ireturn
    //   60: astore_1
    //   61: aload_1
    //   62: invokevirtual 130	java/io/IOException:printStackTrace	()V
    //   65: goto -8 -> 57
    //   68: aload_1
    //   69: ifnull +7 -> 76
    //   72: aload_1
    //   73: invokevirtual 127	java/io/BufferedReader:close	()V
    //   76: iconst_0
    //   77: ireturn
    //   78: astore_1
    //   79: aload_1
    //   80: invokevirtual 130	java/io/IOException:printStackTrace	()V
    //   83: goto -7 -> 76
    //   86: astore_2
    //   87: aload_3
    //   88: astore_1
    //   89: aload_2
    //   90: astore_3
    //   91: aload_1
    //   92: astore_2
    //   93: aload_3
    //   94: invokevirtual 131	java/lang/Exception:printStackTrace	()V
    //   97: aload_1
    //   98: ifnull -41 -> 57
    //   101: aload_1
    //   102: invokevirtual 127	java/io/BufferedReader:close	()V
    //   105: iconst_0
    //   106: ireturn
    //   107: astore_1
    //   108: aload_1
    //   109: invokevirtual 130	java/io/IOException:printStackTrace	()V
    //   112: iconst_0
    //   113: ireturn
    //   114: astore_1
    //   115: aload_2
    //   116: ifnull +7 -> 123
    //   119: aload_2
    //   120: invokevirtual 127	java/io/BufferedReader:close	()V
    //   123: aload_1
    //   124: athrow
    //   125: astore_2
    //   126: aload_2
    //   127: invokevirtual 130	java/io/IOException:printStackTrace	()V
    //   130: goto -7 -> 123
    //   133: astore_3
    //   134: aload_1
    //   135: astore_2
    //   136: aload_3
    //   137: astore_1
    //   138: goto -23 -> 115
    //   141: astore_3
    //   142: goto -51 -> 91
    //
    // Exception table:
    //   from	to	target	type
    //   53	57	60	java/io/IOException
    //   72	76	78	java/io/IOException
    //   7	30	86	java/lang/Exception
    //   101	105	107	java/io/IOException
    //   7	30	114	finally
    //   93	97	114	finally
    //   119	123	125	java/io/IOException
    //   30	41	133	finally
    //   30	41	141	java/lang/Exception
  }

  public ConfigService configService()
  {
    return (ConfigService)NovaApplication.instance().getService("config");
  }

  public void doGetCssResponse()
  {
    SharedPreferences localSharedPreferences = DPActivity.preferences(this.context);
    String str1 = localSharedPreferences.getString("tuan_css_version", "");
    String str2 = ConfigHelper.cssVersion;
    String str3 = ConfigHelper.cssLink;
    String str4 = ConfigHelper.appVersion;
    if ((!"".equals(str2)) && (!str1.equals(str2)))
    {
      fetchCssFile(str3, str2);
      if (!TextUtils.isEmpty(str4))
        localSharedPreferences.edit().putString("tuan_css_app_version", str4).commit();
    }
  }

  protected void fetchCssFile(String paramString1, String paramString2)
  {
    new AsyncTask(paramString2)
    {
      // ERROR //
      protected Void doInBackground(String[] paramArrayOfString)
      {
        // Byte code:
        //   0: invokestatic 39	com/dianping/app/DPApplication:instance	()Lcom/dianping/app/DPApplication;
        //   3: ldc 41
        //   5: invokevirtual 45	com/dianping/app/DPApplication:getService	(Ljava/lang/String;)Ljava/lang/Object;
        //   8: checkcast 47	com/dianping/dataservice/http/HttpService
        //   11: astore_2
        //   12: aload_0
        //   13: getfield 18	com/dianping/base/widget/CssStyleManager$2:this$0	Lcom/dianping/base/widget/CssStyleManager;
        //   16: aload_1
        //   17: iconst_0
        //   18: aaload
        //   19: invokestatic 53	com/dianping/dataservice/http/BasicHttpRequest:httpGet	(Ljava/lang/String;)Lcom/dianping/dataservice/http/HttpRequest;
        //   22: putfield 57	com/dianping/base/widget/CssStyleManager:httpRequest	Lcom/dianping/dataservice/http/HttpRequest;
        //   25: aload_2
        //   26: aload_0
        //   27: getfield 18	com/dianping/base/widget/CssStyleManager$2:this$0	Lcom/dianping/base/widget/CssStyleManager;
        //   30: getfield 57	com/dianping/base/widget/CssStyleManager:httpRequest	Lcom/dianping/dataservice/http/HttpRequest;
        //   33: invokeinterface 61 2 0
        //   38: checkcast 63	com/dianping/dataservice/http/HttpResponse
        //   41: astore 5
        //   43: aconst_null
        //   44: astore 4
        //   46: aconst_null
        //   47: astore_3
        //   48: aload_3
        //   49: astore_1
        //   50: aload 4
        //   52: astore_2
        //   53: aload 5
        //   55: invokeinterface 67 1 0
        //   60: checkcast 69	[B
        //   63: checkcast 69	[B
        //   66: astore 5
        //   68: aload_3
        //   69: astore_1
        //   70: aload 4
        //   72: astore_2
        //   73: aload_0
        //   74: getfield 18	com/dianping/base/widget/CssStyleManager$2:this$0	Lcom/dianping/base/widget/CssStyleManager;
        //   77: getfield 73	com/dianping/base/widget/CssStyleManager:context	Landroid/content/Context;
        //   80: ldc 75
        //   82: iconst_0
        //   83: invokevirtual 81	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
        //   86: astore_3
        //   87: aload_3
        //   88: astore_1
        //   89: aload_3
        //   90: astore_2
        //   91: aload_3
        //   92: aload 5
        //   94: invokevirtual 87	java/io/FileOutputStream:write	([B)V
        //   97: aload_3
        //   98: astore_1
        //   99: aload_3
        //   100: astore_2
        //   101: aload_0
        //   102: getfield 18	com/dianping/base/widget/CssStyleManager$2:this$0	Lcom/dianping/base/widget/CssStyleManager;
        //   105: getfield 73	com/dianping/base/widget/CssStyleManager:context	Landroid/content/Context;
        //   108: invokestatic 93	com/dianping/app/DPActivity:preferences	(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //   111: invokeinterface 99 1 0
        //   116: astore 4
        //   118: aload_3
        //   119: astore_1
        //   120: aload_3
        //   121: astore_2
        //   122: aload 4
        //   124: ldc 101
        //   126: aload_0
        //   127: getfield 20	com/dianping/base/widget/CssStyleManager$2:val$serverCssVersion	Ljava/lang/String;
        //   130: invokeinterface 107 3 0
        //   135: pop
        //   136: aload_3
        //   137: astore_1
        //   138: aload_3
        //   139: astore_2
        //   140: aload 4
        //   142: invokeinterface 111 1 0
        //   147: pop
        //   148: aload_3
        //   149: astore_1
        //   150: aload_3
        //   151: astore_2
        //   152: ldc 113
        //   154: ldc 115
        //   156: invokestatic 120	com/dianping/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
        //   159: aload_3
        //   160: ifnull +7 -> 167
        //   163: aload_3
        //   164: invokevirtual 123	java/io/FileOutputStream:close	()V
        //   167: aconst_null
        //   168: areturn
        //   169: astore_2
        //   170: aload_1
        //   171: astore_2
        //   172: ldc 113
        //   174: ldc 125
        //   176: invokestatic 128	com/dianping/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
        //   179: aload_1
        //   180: ifnull -13 -> 167
        //   183: aload_1
        //   184: invokevirtual 123	java/io/FileOutputStream:close	()V
        //   187: goto -20 -> 167
        //   190: astore_1
        //   191: goto -24 -> 167
        //   194: astore_1
        //   195: aload_2
        //   196: ifnull +7 -> 203
        //   199: aload_2
        //   200: invokevirtual 123	java/io/FileOutputStream:close	()V
        //   203: aload_1
        //   204: athrow
        //   205: astore_1
        //   206: goto -39 -> 167
        //   209: astore_2
        //   210: goto -7 -> 203
        //
        // Exception table:
        //   from	to	target	type
        //   53	68	169	java/lang/Exception
        //   73	87	169	java/lang/Exception
        //   91	97	169	java/lang/Exception
        //   101	118	169	java/lang/Exception
        //   122	136	169	java/lang/Exception
        //   140	148	169	java/lang/Exception
        //   152	159	169	java/lang/Exception
        //   183	187	190	java/lang/Exception
        //   53	68	194	finally
        //   73	87	194	finally
        //   91	97	194	finally
        //   101	118	194	finally
        //   122	136	194	finally
        //   140	148	194	finally
        //   152	159	194	finally
        //   172	179	194	finally
        //   163	167	205	java/lang/Exception
        //   199	203	209	java/lang/Exception
      }
    }
    .execute(new String[] { paramString1 });
  }

  public String getCssFilePath()
  {
    try
    {
      File localFile = new File(this.context.getFilesDir().getAbsolutePath() + "/" + "tuan.css");
      if (localFile.exists())
      {
        if ((checkAppVersion()) && (checkFile(localFile)))
        {
          Log.d("CssManager", "load css in files");
          return "file://" + localFile.getAbsolutePath();
        }
        localFile.delete();
        Log.d("CssManager", "load css in asset");
        return "file:///android_asset/tuan.css";
      }
      Log.d("CssManager", "load css in asset");
      return "file:///android_asset/tuan.css";
    }
    catch (Exception localException)
    {
    }
    return "file:///android_asset/tuan.css";
  }

  public String makeHtml(String paramString, boolean paramBoolean)
  {
    if (paramBoolean);
    for (String str = "</body>" + "<script>(function () {var imgs = document.getElementsByTagName('img');for (var i = 0; i != imgs.length; ++i) {var img = imgs[i];img.style.width = (document.body.offsetWidth - img.offsetLeft * 2) + 'px';img.addEventListener('click', function (evt) {window.open('dianping://showcheckinphoto?img=' + encodeURIComponent(evt.target.src)); }, false);}})();</script>" + "</html>"; ; str = "</body></html>")
      return "<html><head><meta http-equiv='Content-Type'content='text/html; charset=UTF-8'><title>大众点评网</title><link rel='stylesheet' href='tuan.css' type='text/css'></head><body id='top' class='deal-more-detail'>" + paramString + str;
  }

  public void syncServerCssFile()
  {
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        CssStyleManager.this.doGetCssResponse();
      }
    }
    , 3000L);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CssStyleManager
 * JD-Core Version:    0.6.0
 */