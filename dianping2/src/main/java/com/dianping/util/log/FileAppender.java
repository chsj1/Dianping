package com.dianping.util.log;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.dianping.app.DPApplication;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public final class FileAppender extends Appender
{
  protected static final File LOG_DIR_PATH = new File(DPApplication.instance().getFilesDir(), "novalogbase");
  protected static final String LOG_NAME = "novalogbase";
  protected static final String PREFERENCE_NOVA_LOG = "nova_log_latest_modified";
  private static FileAppender instance;
  private static int mBackupIndex = 0;
  private static final int mBufferSize = 1024;
  private static final long mPerSize = 1024L;
  private SharedPreferences.Editor editor;
  private BufferedWriter mBufferedWriter;
  private long mCount;
  private List<String> mErrorLogList;
  private ExecutorService mPool;
  private MApiRequest mPost;
  private MApiService mapiService;
  private SharedPreferences mySharedPreferences;

  private FileAppender()
  {
    DPApplication localDPApplication = DPApplication.instance();
    DPApplication.instance();
    this.mySharedPreferences = localDPApplication.getSharedPreferences("nova_log_latest_modified", 0);
    this.mBufferedWriter = null;
    this.mCount = 0L;
    this.mapiService = DPApplication.instance().mapiService();
    this.mPost = null;
    this.editor = this.mySharedPreferences.edit();
    this.mPool = Executors.newSingleThreadExecutor();
    this.mErrorLogList = new ArrayList();
    if ((LOG_DIR_PATH.exists()) || (LOG_DIR_PATH.mkdirs()))
      try
      {
        new File(LOG_DIR_PATH, ".nomedia").createNewFile();
        return;
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        return;
      }
    Log.e("NOVA_LOG", LOG_DIR_PATH + " create fail.");
  }

  private String formatMessage(int paramInt, String paramString1, String paramString2)
  {
    if (paramString2 == null)
      return "";
    JSONObject localJSONObject = new JSONObject();
    paramString1 = paramString1 + File.separator + paramString2;
    try
    {
      localJSONObject.put("time", System.currentTimeMillis());
      if (paramInt == 3)
        localJSONObject.put("level", "normal");
      while (true)
      {
        localJSONObject.put("log", paramString1);
        if ((paramInt == 4) && (!this.mErrorLogList.contains(paramString1)))
        {
          postLog(localJSONObject.toString(), "http://m.api.dianping.com/applog/apperrorlog.api");
          if (this.mErrorLogList.size() <= 10)
            break label219;
          this.mErrorLogList.remove(this.mErrorLogList.size() - 1);
          this.mErrorLogList.add(paramString1);
        }
        return localJSONObject.toString() + "," + '\n';
        if (paramInt != 4)
          break;
        localJSONObject.put("level", "error");
      }
    }
    catch (JSONException paramString2)
    {
      while (true)
      {
        paramString2.printStackTrace();
        continue;
        localJSONObject.put("level", "?");
        continue;
        label219: this.mErrorLogList.add(paramString1);
      }
    }
  }

  public static FileAppender getInstance()
  {
    if (instance == null)
      instance = new FileAppender();
    return instance;
  }

  private File getLatestModifiedFile(String paramString)
  {
    Object localObject1 = null;
    File[] arrayOfFile = LOG_DIR_PATH.listFiles();
    if (arrayOfFile == null)
      return null;
    int j = arrayOfFile.length;
    int i = 0;
    Object localObject2 = localObject1;
    Object localObject3;
    if (i < j)
    {
      localObject2 = arrayOfFile[i];
      localObject3 = localObject1;
      if (((File)localObject2).isFile())
      {
        if (!((File)localObject2).isHidden())
          break label72;
        localObject3 = localObject1;
      }
    }
    while (true)
    {
      i += 1;
      localObject1 = localObject3;
      break;
      label72: if ((!paramString.equalsIgnoreCase("")) && (paramString.equalsIgnoreCase(((File)localObject2).getName())))
        return localObject2;
      Object localObject4 = localObject1;
      if (localObject1 == null)
        localObject4 = localObject2;
      if (localObject4.length() > ((File)localObject2).length())
      {
        localObject3 = localObject2;
        continue;
      }
      localObject3 = localObject4;
      if (localObject4.lastModified() >= ((File)localObject2).lastModified())
        continue;
      localObject3 = localObject2;
    }
  }

  private String read(String paramString)
  {
    paramString = new File(paramString);
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      if ((!paramString.exists()) || (paramString.isDirectory()))
        throw new FileNotFoundException();
    }
    catch (IOException paramString)
    {
      paramString.printStackTrace();
    }
    while (true)
    {
      return localStringBuilder.toString();
      FileInputStream localFileInputStream = new FileInputStream(paramString);
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream, "UTF-8"));
      for (paramString = localBufferedReader.readLine(); paramString != null; paramString = localBufferedReader.readLine())
        localStringBuilder.append(paramString);
      localBufferedReader.close();
      localFileInputStream.close();
    }
  }

  private void write(String paramString)
  {
    this.mPool.execute(new Thread(paramString)
    {
      // ERROR //
      public void run()
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   4: aload_0
        //   5: getfield 19	com/dianping/util/log/FileAppender$2:val$msg	Ljava/lang/String;
        //   8: invokevirtual 32	java/lang/String:length	()I
        //   11: i2l
        //   12: invokestatic 36	com/dianping/util/log/FileAppender:access$114	(Lcom/dianping/util/log/FileAppender;J)J
        //   15: pop2
        //   16: aload_0
        //   17: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   20: invokestatic 40	com/dianping/util/log/FileAppender:access$100	(Lcom/dianping/util/log/FileAppender;)J
        //   23: ldc2_w 41
        //   26: lcmp
        //   27: ifge +18 -> 45
        //   30: aload_0
        //   31: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   34: invokestatic 46	com/dianping/util/log/FileAppender:access$200	(Lcom/dianping/util/log/FileAppender;)Ljava/io/BufferedWriter;
        //   37: aload_0
        //   38: getfield 19	com/dianping/util/log/FileAppender$2:val$msg	Ljava/lang/String;
        //   41: invokevirtual 49	java/io/BufferedWriter:write	(Ljava/lang/String;)V
        //   44: return
        //   45: aload_0
        //   46: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   49: invokestatic 46	com/dianping/util/log/FileAppender:access$200	(Lcom/dianping/util/log/FileAppender;)Ljava/io/BufferedWriter;
        //   52: ifnull +23 -> 75
        //   55: aload_0
        //   56: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   59: invokestatic 46	com/dianping/util/log/FileAppender:access$200	(Lcom/dianping/util/log/FileAppender;)Ljava/io/BufferedWriter;
        //   62: invokevirtual 52	java/io/BufferedWriter:flush	()V
        //   65: aload_0
        //   66: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   69: invokestatic 46	com/dianping/util/log/FileAppender:access$200	(Lcom/dianping/util/log/FileAppender;)Ljava/io/BufferedWriter;
        //   72: invokevirtual 55	java/io/BufferedWriter:close	()V
        //   75: invokestatic 58	com/dianping/util/log/FileAppender:access$300	()I
        //   78: ifle +171 -> 249
        //   81: invokestatic 61	com/dianping/util/log/FileAppender:access$310	()I
        //   84: pop
        //   85: aload_0
        //   86: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   89: invokestatic 65	com/dianping/util/log/FileAppender:access$400	(Lcom/dianping/util/log/FileAppender;)Landroid/content/SharedPreferences$Editor;
        //   92: ifnull +52 -> 144
        //   95: aload_0
        //   96: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   99: invokestatic 65	com/dianping/util/log/FileAppender:access$400	(Lcom/dianping/util/log/FileAppender;)Landroid/content/SharedPreferences$Editor;
        //   102: ldc 67
        //   104: new 69	java/lang/StringBuilder
        //   107: dup
        //   108: invokespecial 70	java/lang/StringBuilder:<init>	()V
        //   111: ldc 72
        //   113: invokevirtual 76	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   116: invokestatic 58	com/dianping/util/log/FileAppender:access$300	()I
        //   119: invokevirtual 79	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
        //   122: invokevirtual 83	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   125: invokeinterface 89 3 0
        //   130: pop
        //   131: aload_0
        //   132: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   135: invokestatic 65	com/dianping/util/log/FileAppender:access$400	(Lcom/dianping/util/log/FileAppender;)Landroid/content/SharedPreferences$Editor;
        //   138: invokeinterface 93 1 0
        //   143: pop
        //   144: new 95	java/io/FileOutputStream
        //   147: dup
        //   148: new 69	java/lang/StringBuilder
        //   151: dup
        //   152: invokespecial 70	java/lang/StringBuilder:<init>	()V
        //   155: getstatic 99	com/dianping/util/log/FileAppender:LOG_DIR_PATH	Ljava/io/File;
        //   158: invokevirtual 104	java/io/File:getAbsolutePath	()Ljava/lang/String;
        //   161: invokevirtual 76	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   164: getstatic 107	java/io/File:separator	Ljava/lang/String;
        //   167: invokevirtual 76	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   170: ldc 109
        //   172: invokevirtual 76	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   175: ldc 111
        //   177: invokevirtual 76	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   180: invokestatic 58	com/dianping/util/log/FileAppender:access$300	()I
        //   183: invokevirtual 79	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
        //   186: invokevirtual 83	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   189: iconst_0
        //   190: invokespecial 114	java/io/FileOutputStream:<init>	(Ljava/lang/String;Z)V
        //   193: astore_1
        //   194: aload_0
        //   195: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   198: new 48	java/io/BufferedWriter
        //   201: dup
        //   202: new 116	java/io/OutputStreamWriter
        //   205: dup
        //   206: aload_1
        //   207: ldc 118
        //   209: invokespecial 121	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/lang/String;)V
        //   212: sipush 1024
        //   215: invokespecial 124	java/io/BufferedWriter:<init>	(Ljava/io/Writer;I)V
        //   218: invokestatic 128	com/dianping/util/log/FileAppender:access$202	(Lcom/dianping/util/log/FileAppender;Ljava/io/BufferedWriter;)Ljava/io/BufferedWriter;
        //   221: pop
        //   222: aload_0
        //   223: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   226: lconst_0
        //   227: invokestatic 131	com/dianping/util/log/FileAppender:access$102	(Lcom/dianping/util/log/FileAppender;J)J
        //   230: pop2
        //   231: aload_0
        //   232: getfield 17	com/dianping/util/log/FileAppender$2:this$0	Lcom/dianping/util/log/FileAppender;
        //   235: aload_0
        //   236: getfield 19	com/dianping/util/log/FileAppender$2:val$msg	Ljava/lang/String;
        //   239: invokestatic 134	com/dianping/util/log/FileAppender:access$500	(Lcom/dianping/util/log/FileAppender;Ljava/lang/String;)V
        //   242: return
        //   243: astore_1
        //   244: aload_1
        //   245: invokevirtual 137	java/lang/Exception:printStackTrace	()V
        //   248: return
        //   249: iconst_3
        //   250: invokestatic 141	com/dianping/util/log/FileAppender:access$302	(I)I
        //   253: pop
        //   254: goto -169 -> 85
        //   257: astore_1
        //   258: aload_1
        //   259: invokevirtual 137	java/lang/Exception:printStackTrace	()V
        //   262: goto -40 -> 222
        //
        // Exception table:
        //   from	to	target	type
        //   16	44	243	java/lang/Exception
        //   45	75	243	java/lang/Exception
        //   75	85	243	java/lang/Exception
        //   85	144	243	java/lang/Exception
        //   222	242	243	java/lang/Exception
        //   249	254	243	java/lang/Exception
        //   258	262	243	java/lang/Exception
        //   144	222	257	java/lang/Exception
      }
    });
  }

  public void close()
  {
    if (this.mBufferedWriter != null);
    try
    {
      this.mBufferedWriter.flush();
      this.mBufferedWriter.close();
      if (this.editor != null)
      {
        this.editor.putString("nova_log_latest_modified", "novalogbase." + mBackupIndex);
        this.editor.commit();
      }
      this.mPost = null;
      if (this.mPool != null)
        this.mPool.shutdown();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void e(int paramInt, String paramString1, String paramString2)
  {
    write(formatMessage(paramInt, paramString1, paramString2));
  }

  public void flush()
  {
    if (this.mBufferedWriter != null);
    try
    {
      this.mBufferedWriter.flush();
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public String getCodeLog()
  {
    flush();
    Object localObject = new StringBuilder();
    int i = mBackupIndex;
    while (i < 4)
    {
      ((StringBuilder)localObject).append(read(LOG_DIR_PATH + File.separator + "novalogbase" + "." + i));
      i += 1;
    }
    String str = ((StringBuilder)localObject).toString().trim();
    if (str.length() == 0)
      return "";
    localObject = str;
    if (",".equalsIgnoreCase(str.substring(str.length() - 1, str.length())))
      localObject = "[" + str.substring(0, str.length() - 1) + "]";
    return (String)localObject;
  }

  public void i(int paramInt, String paramString1, String paramString2)
  {
    write(formatMessage(paramInt, paramString1, paramString2));
  }

  public void open()
  {
    File localFile2 = getLatestModifiedFile(this.mySharedPreferences.getString("nova_log_latest_modified", ""));
    File localFile1;
    boolean bool;
    if (localFile2 == null)
    {
      localFile1 = new File(LOG_DIR_PATH.getAbsolutePath(), "novalogbase." + mBackupIndex);
      bool = false;
    }
    try
    {
      while (true)
      {
        this.mBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localFile1, bool), "UTF-8"), 1024);
        return;
        localFile1 = localFile2;
        mBackupIndex = Integer.parseInt(String.valueOf(localFile2.getName().charAt(localFile2.getName().length() - 1)));
        this.mCount = localFile2.length();
        bool = true;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void postLog(String paramString1, String paramString2)
  {
    this.mPost = BasicMApiRequest.mapiPost(paramString2, new String[] { "log", paramString1 });
    this.mapiService.exec(this.mPost, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        Log.e("NOVA_LOG", "failed to upload, statusCode = " + paramMApiResponse.statusCode());
        FileAppender.access$002(FileAppender.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        FileAppender.access$002(FileAppender.this, null);
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.log.FileAppender
 * JD-Core Version:    0.6.0
 */