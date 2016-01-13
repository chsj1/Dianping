package com.dianping.locationservice.impl286.locate;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.dianping.locationservice.impl286.model.CellModel;
import com.dianping.locationservice.impl286.model.WifiModel;
import com.dianping.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

public class BMapLocator extends AssistLocator
{
  private static final String B_LOC_URL = "http://loc.map.baidu.com/sdk.php";

  public BMapLocator(Context paramContext, List<CellModel> paramList, List<WifiModel> paramList1)
  {
    super(paramContext, paramList, paramList1);
  }

  protected String formatRequest()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("&cl=");
    if (this.mCellModelList.isEmpty())
      ((StringBuilder)localObject1).append("0|0|-1|-1");
    Object localObject2;
    while (!this.mWifiModelList.isEmpty())
    {
      ((StringBuilder)localObject1).append("&wf=");
      localObject2 = this.mWifiModelList.iterator();
      while (true)
        if (((Iterator)localObject2).hasNext())
        {
          Object localObject3 = (WifiModel)((Iterator)localObject2).next();
          ((StringBuilder)localObject1).append(((WifiModel)localObject3).getBssid().replaceAll(":", ""));
          ((StringBuilder)localObject1).append(";");
          ((StringBuilder)localObject1).append(Math.abs(((WifiModel)localObject3).getLevel()));
          ((StringBuilder)localObject1).append(";");
          ((StringBuilder)localObject1).append("|");
          continue;
          localObject2 = (CellModel)this.mCellModelList.get(0);
          ((StringBuilder)localObject1).append(((CellModel)localObject2).getMcc());
          ((StringBuilder)localObject1).append("|");
          ((StringBuilder)localObject1).append(((CellModel)localObject2).getMncSid());
          ((StringBuilder)localObject1).append("|");
          ((StringBuilder)localObject1).append(((CellModel)localObject2).getLacNid());
          ((StringBuilder)localObject1).append("|");
          ((StringBuilder)localObject1).append(((CellModel)localObject2).getCidBid());
          ((StringBuilder)localObject1).append("&clt=");
          localObject2 = this.mCellModelList.iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (CellModel)((Iterator)localObject2).next();
            ((StringBuilder)localObject1).append(((CellModel)localObject3).getMcc());
            ((StringBuilder)localObject1).append("|");
            ((StringBuilder)localObject1).append(((CellModel)localObject3).getMncSid());
            ((StringBuilder)localObject1).append("|");
            ((StringBuilder)localObject1).append(((CellModel)localObject3).getLacNid());
            ((StringBuilder)localObject1).append("|");
            ((StringBuilder)localObject1).append(((CellModel)localObject3).getCidBid());
            ((StringBuilder)localObject1).append("|");
            ((StringBuilder)localObject1).append("1");
            ((StringBuilder)localObject1).append(";");
          }
          ((StringBuilder)localObject1).append("10");
          break;
        }
      ((StringBuilder)localObject1).setLength(((StringBuilder)localObject1).length() - 1);
    }
    ((StringBuilder)localObject1).append("&addr=detail&coor=gcj02&os=android&prod=default");
    try
    {
      localObject2 = this.mTelephonyManager.getDeviceId();
      ((StringBuilder)localObject1).append("&im=");
      ((StringBuilder)localObject1).append((String)localObject2);
      localObject1 = ((StringBuilder)localObject1).toString();
      localObject1 = BMapDigester.digest((String)localObject1) + "|tp=2";
    }
    catch (Exception localUnsupportedEncodingException)
    {
      try
      {
        localObject2 = URLEncoder.encode("bloc", "UTF-8") + "=" + URLEncoder.encode((String)localObject1, "UTF-8");
        return localObject2;
        localException = localException;
        Log.e(localException.toString());
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Log.e(localUnsupportedEncodingException.toString());
      }
    }
    return (String)(String)(String)localObject1;
  }

  protected String getHttpPostUrl()
  {
    return "http://loc.map.baidu.com/sdk.php";
  }

  // ERROR //
  protected void handleResponse(java.io.InputStream paramInputStream)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +4 -> 5
    //   4: return
    //   5: aconst_null
    //   6: astore_3
    //   7: aconst_null
    //   8: astore 5
    //   10: aconst_null
    //   11: astore 4
    //   13: new 177	java/io/BufferedReader
    //   16: dup
    //   17: new 179	java/io/InputStreamReader
    //   20: dup
    //   21: aload_1
    //   22: ldc 153
    //   24: invokespecial 182	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   27: invokespecial 185	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   30: astore_2
    //   31: new 23	java/lang/StringBuilder
    //   34: dup
    //   35: invokespecial 26	java/lang/StringBuilder:<init>	()V
    //   38: astore_3
    //   39: aload_2
    //   40: invokevirtual 188	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   43: astore 4
    //   45: aload 4
    //   47: ifnull +54 -> 101
    //   50: aload_3
    //   51: aload 4
    //   53: invokevirtual 32	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: ldc 190
    //   58: invokevirtual 32	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: goto -23 -> 39
    //   65: astore_3
    //   66: aload_3
    //   67: astore 4
    //   69: aload_2
    //   70: astore_3
    //   71: aload 4
    //   73: invokevirtual 161	java/lang/Exception:toString	()Ljava/lang/String;
    //   76: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   79: aload_2
    //   80: ifnull +7 -> 87
    //   83: aload_2
    //   84: invokevirtual 193	java/io/BufferedReader:close	()V
    //   87: aload_1
    //   88: invokevirtual 196	java/io/InputStream:close	()V
    //   91: return
    //   92: astore_1
    //   93: aload_1
    //   94: invokevirtual 197	java/io/IOException:toString	()Ljava/lang/String;
    //   97: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   100: return
    //   101: new 199	org/json/JSONObject
    //   104: dup
    //   105: aload_3
    //   106: invokevirtual 141	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   109: invokespecial 201	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   112: astore_3
    //   113: aload_3
    //   114: ldc 203
    //   116: invokevirtual 207	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   119: ldc 209
    //   121: invokevirtual 213	org/json/JSONObject:getInt	(Ljava/lang/String;)I
    //   124: istore 6
    //   126: iload 6
    //   128: sipush 167
    //   131: if_icmpne +25 -> 156
    //   134: aload_2
    //   135: ifnull +7 -> 142
    //   138: aload_2
    //   139: invokevirtual 193	java/io/BufferedReader:close	()V
    //   142: aload_1
    //   143: invokevirtual 196	java/io/InputStream:close	()V
    //   146: return
    //   147: astore_1
    //   148: aload_1
    //   149: invokevirtual 197	java/io/IOException:toString	()Ljava/lang/String;
    //   152: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   155: return
    //   156: new 215	com/dianping/locationservice/impl286/model/CoordBMapModel
    //   159: dup
    //   160: aload_3
    //   161: ldc 217
    //   163: invokevirtual 207	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   166: ldc 219
    //   168: invokevirtual 207	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   171: ldc 221
    //   173: invokevirtual 225	org/json/JSONObject:getDouble	(Ljava/lang/String;)D
    //   176: iconst_5
    //   177: invokestatic 231	com/dianping/locationservice/impl286/util/CommonUtil:format	(DI)D
    //   180: aload_3
    //   181: ldc 217
    //   183: invokevirtual 207	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   186: ldc 219
    //   188: invokevirtual 207	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   191: ldc 233
    //   193: invokevirtual 225	org/json/JSONObject:getDouble	(Ljava/lang/String;)D
    //   196: iconst_5
    //   197: invokestatic 231	com/dianping/locationservice/impl286/util/CommonUtil:format	(DI)D
    //   200: aload_3
    //   201: ldc 217
    //   203: invokevirtual 207	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   206: ldc 235
    //   208: invokevirtual 225	org/json/JSONObject:getDouble	(Ljava/lang/String;)D
    //   211: d2i
    //   212: invokestatic 241	java/lang/System:currentTimeMillis	()J
    //   215: invokespecial 244	com/dianping/locationservice/impl286/model/CoordBMapModel:<init>	(DDIJ)V
    //   218: astore_3
    //   219: aload_0
    //   220: getfield 247	com/dianping/locationservice/impl286/locate/BMapLocator:mResultList	Ljava/util/List;
    //   223: aload_3
    //   224: invokeinterface 251 2 0
    //   229: pop
    //   230: aload_2
    //   231: ifnull +7 -> 238
    //   234: aload_2
    //   235: invokevirtual 193	java/io/BufferedReader:close	()V
    //   238: aload_1
    //   239: invokevirtual 196	java/io/InputStream:close	()V
    //   242: return
    //   243: astore_1
    //   244: aload_1
    //   245: invokevirtual 197	java/io/IOException:toString	()Ljava/lang/String;
    //   248: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   251: return
    //   252: astore_2
    //   253: aload_3
    //   254: ifnull +7 -> 261
    //   257: aload_3
    //   258: invokevirtual 193	java/io/BufferedReader:close	()V
    //   261: aload_1
    //   262: invokevirtual 196	java/io/InputStream:close	()V
    //   265: aload_2
    //   266: athrow
    //   267: astore_1
    //   268: aload_1
    //   269: invokevirtual 197	java/io/IOException:toString	()Ljava/lang/String;
    //   272: invokestatic 167	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   275: goto -10 -> 265
    //   278: astore_3
    //   279: aload 5
    //   281: astore_2
    //   282: aload_3
    //   283: astore 4
    //   285: goto -216 -> 69
    //   288: astore 4
    //   290: aload_2
    //   291: astore_3
    //   292: aload 4
    //   294: astore_2
    //   295: goto -42 -> 253
    //   298: astore_3
    //   299: goto -17 -> 282
    //   302: astore_3
    //   303: aload 4
    //   305: astore_2
    //   306: goto -240 -> 66
    //
    // Exception table:
    //   from	to	target	type
    //   31	39	65	java/io/IOException
    //   39	45	65	java/io/IOException
    //   50	62	65	java/io/IOException
    //   101	126	65	java/io/IOException
    //   156	230	65	java/io/IOException
    //   83	87	92	java/io/IOException
    //   87	91	92	java/io/IOException
    //   138	142	147	java/io/IOException
    //   142	146	147	java/io/IOException
    //   234	238	243	java/io/IOException
    //   238	242	243	java/io/IOException
    //   13	31	252	finally
    //   71	79	252	finally
    //   257	261	267	java/io/IOException
    //   261	265	267	java/io/IOException
    //   13	31	278	org/json/JSONException
    //   31	39	288	finally
    //   39	45	288	finally
    //   50	62	288	finally
    //   101	126	288	finally
    //   156	230	288	finally
    //   31	39	298	org/json/JSONException
    //   39	45	298	org/json/JSONException
    //   50	62	298	org/json/JSONException
    //   101	126	298	org/json/JSONException
    //   156	230	298	org/json/JSONException
    //   13	31	302	java/io/IOException
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.BMapLocator
 * JD-Core Version:    0.6.0
 */