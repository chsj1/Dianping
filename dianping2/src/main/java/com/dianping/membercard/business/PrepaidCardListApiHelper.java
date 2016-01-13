package com.dianping.membercard.business;

import android.content.Context;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.membercard.utils.ContextWrapper;
import com.dianping.model.SimpleMsg;
import java.io.File;

public class PrepaidCardListApiHelper
{
  File cache;
  ContextWrapper context;
  String errMsg;
  public double lat;
  public double lng;
  public String membercardid;
  public int nextStartIndex;
  public int pixel;
  MApiRequest request;
  MApiService service;
  public String token;

  public PrepaidCardListApiHelper(Context paramContext)
  {
    this.context = new ContextWrapper(paramContext);
    this.cache = paramContext.getFileStreamPath("prepaidcardlist_cache_data");
    this.service = this.context.getActivity().mapiService();
  }

  public static PrepaidCardListApiResult getCacheCardList(Context paramContext)
  {
    Object localObject = new PrepaidCardListApiHelper(paramContext);
    paramContext = new PrepaidCardListApiResult();
    localObject = ((PrepaidCardListApiHelper)localObject).getCacheCardList();
    if (localObject != null)
    {
      localObject = ((DPObject)localObject).edit().putBoolean("IsEnd", true).generate();
      paramContext.isCache = true;
      paramContext.cardList = ((DPObject)localObject);
      return paramContext;
    }
    return (PrepaidCardListApiResult)null;
  }

  public void abort()
  {
    if (this.request != null);
  }

  public String errorMsg()
  {
    return this.errMsg;
  }

  // ERROR //
  public DPObject getCacheCardList()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: getfield 47	com/dianping/membercard/business/PrepaidCardListApiHelper:cache	Ljava/io/File;
    //   6: invokevirtual 110	java/io/File:exists	()Z
    //   9: ifne +5 -> 14
    //   12: aconst_null
    //   13: areturn
    //   14: aconst_null
    //   15: astore_2
    //   16: aconst_null
    //   17: astore 4
    //   19: new 112	java/io/FileInputStream
    //   22: dup
    //   23: aload_0
    //   24: getfield 47	com/dianping/membercard/business/PrepaidCardListApiHelper:cache	Ljava/io/File;
    //   27: invokespecial 115	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   30: astore_1
    //   31: aload_1
    //   32: invokevirtual 119	java/io/FileInputStream:available	()I
    //   35: newarray byte
    //   37: astore_2
    //   38: aload_1
    //   39: aload_2
    //   40: invokevirtual 123	java/io/FileInputStream:read	([B)I
    //   43: pop
    //   44: new 70	com/dianping/archive/DPObject
    //   47: dup
    //   48: aload_2
    //   49: iconst_0
    //   50: aload_2
    //   51: arraylength
    //   52: invokespecial 126	com/dianping/archive/DPObject:<init>	([BII)V
    //   55: astore_2
    //   56: aload_1
    //   57: invokevirtual 129	java/io/FileInputStream:close	()V
    //   60: iconst_0
    //   61: ifeq +105 -> 166
    //   64: new 131	java/lang/NullPointerException
    //   67: dup
    //   68: invokespecial 132	java/lang/NullPointerException:<init>	()V
    //   71: athrow
    //   72: aload_2
    //   73: ifnonnull +11 -> 84
    //   76: aload_0
    //   77: getfield 47	com/dianping/membercard/business/PrepaidCardListApiHelper:cache	Ljava/io/File;
    //   80: invokevirtual 135	java/io/File:delete	()Z
    //   83: pop
    //   84: aload_2
    //   85: areturn
    //   86: astore_1
    //   87: goto -15 -> 72
    //   90: astore_1
    //   91: aload 4
    //   93: astore_1
    //   94: aload_1
    //   95: astore_2
    //   96: aload_0
    //   97: getfield 47	com/dianping/membercard/business/PrepaidCardListApiHelper:cache	Ljava/io/File;
    //   100: invokevirtual 135	java/io/File:delete	()Z
    //   103: pop
    //   104: aload_3
    //   105: astore_2
    //   106: aload_1
    //   107: ifnull -35 -> 72
    //   110: aload_1
    //   111: invokevirtual 129	java/io/FileInputStream:close	()V
    //   114: aload_3
    //   115: astore_2
    //   116: goto -44 -> 72
    //   119: astore_1
    //   120: aload_2
    //   121: ifnull +7 -> 128
    //   124: aload_2
    //   125: invokevirtual 129	java/io/FileInputStream:close	()V
    //   128: aload_1
    //   129: athrow
    //   130: astore_1
    //   131: aload_3
    //   132: astore_2
    //   133: goto -61 -> 72
    //   136: astore_2
    //   137: goto -9 -> 128
    //   140: astore_3
    //   141: aload_1
    //   142: astore_2
    //   143: aload_3
    //   144: astore_1
    //   145: goto -25 -> 120
    //   148: astore_3
    //   149: aload_1
    //   150: astore_2
    //   151: aload_3
    //   152: astore_1
    //   153: goto -33 -> 120
    //   156: astore_2
    //   157: goto -63 -> 94
    //   160: astore_3
    //   161: aload_2
    //   162: astore_3
    //   163: goto -69 -> 94
    //   166: goto -94 -> 72
    //
    // Exception table:
    //   from	to	target	type
    //   64	72	86	java/io/IOException
    //   19	31	90	java/lang/Exception
    //   19	31	119	finally
    //   96	104	119	finally
    //   110	114	130	java/io/IOException
    //   124	128	136	java/io/IOException
    //   31	56	140	finally
    //   56	60	148	finally
    //   31	56	156	java/lang/Exception
    //   56	60	160	java/lang/Exception
  }

  public PrepaidCardListApiResult getMyCards(String paramString)
  {
    PrepaidCardListApiResult localPrepaidCardListApiResult = new PrepaidCardListApiResult();
    this.request = BasicMApiRequest.mapiGet(paramString, CacheType.DISABLED);
    paramString = (MApiResponse)this.service.execSync(this.request);
    this.request = null;
    if ((paramString.result() instanceof DPObject))
    {
      paramString = (DPObject)paramString.result();
      if (paramString != null)
      {
        writeCache(paramString.toByteArray());
        localPrepaidCardListApiResult.isCache = false;
        localPrepaidCardListApiResult.cardList = paramString;
        return localPrepaidCardListApiResult;
      }
    }
    else
    {
      this.errMsg = paramString.message().content();
    }
    paramString = getCacheCardList();
    if (paramString != null)
    {
      paramString = paramString.edit().putBoolean("IsEnd", true).generate();
      localPrepaidCardListApiResult.isCache = true;
      localPrepaidCardListApiResult.cardList = paramString;
      return localPrepaidCardListApiResult;
    }
    return null;
  }

  public void rmCache()
  {
    writeCache(null);
  }

  // ERROR //
  public void writeCache(byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +8 -> 9
    //   4: aload_1
    //   5: arraylength
    //   6: ifne +12 -> 18
    //   9: aload_0
    //   10: getfield 47	com/dianping/membercard/business/PrepaidCardListApiHelper:cache	Ljava/io/File;
    //   13: invokevirtual 135	java/io/File:delete	()Z
    //   16: pop
    //   17: return
    //   18: aconst_null
    //   19: astore_2
    //   20: aconst_null
    //   21: astore 4
    //   23: new 181	java/io/FileOutputStream
    //   26: dup
    //   27: aload_0
    //   28: getfield 47	com/dianping/membercard/business/PrepaidCardListApiHelper:cache	Ljava/io/File;
    //   31: invokespecial 182	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   34: astore_3
    //   35: aload_3
    //   36: aload_1
    //   37: invokevirtual 185	java/io/FileOutputStream:write	([B)V
    //   40: aload_3
    //   41: ifnull +74 -> 115
    //   44: aload_3
    //   45: invokevirtual 186	java/io/FileOutputStream:close	()V
    //   48: return
    //   49: astore_1
    //   50: aload_1
    //   51: invokevirtual 189	java/io/IOException:printStackTrace	()V
    //   54: return
    //   55: astore_1
    //   56: aload 4
    //   58: astore_1
    //   59: aload_1
    //   60: astore_2
    //   61: aload_0
    //   62: getfield 47	com/dianping/membercard/business/PrepaidCardListApiHelper:cache	Ljava/io/File;
    //   65: invokevirtual 135	java/io/File:delete	()Z
    //   68: pop
    //   69: aload_1
    //   70: ifnull -53 -> 17
    //   73: aload_1
    //   74: invokevirtual 186	java/io/FileOutputStream:close	()V
    //   77: return
    //   78: astore_1
    //   79: aload_1
    //   80: invokevirtual 189	java/io/IOException:printStackTrace	()V
    //   83: return
    //   84: astore_1
    //   85: aload_2
    //   86: ifnull +7 -> 93
    //   89: aload_2
    //   90: invokevirtual 186	java/io/FileOutputStream:close	()V
    //   93: aload_1
    //   94: athrow
    //   95: astore_2
    //   96: aload_2
    //   97: invokevirtual 189	java/io/IOException:printStackTrace	()V
    //   100: goto -7 -> 93
    //   103: astore_1
    //   104: aload_3
    //   105: astore_2
    //   106: goto -21 -> 85
    //   109: astore_1
    //   110: aload_3
    //   111: astore_1
    //   112: goto -53 -> 59
    //   115: return
    //
    // Exception table:
    //   from	to	target	type
    //   44	48	49	java/io/IOException
    //   23	35	55	java/lang/Exception
    //   73	77	78	java/io/IOException
    //   23	35	84	finally
    //   61	69	84	finally
    //   89	93	95	java/io/IOException
    //   35	40	103	finally
    //   35	40	109	java/lang/Exception
  }

  public static class PrepaidCardListApiResult
  {
    public DPObject cardList;
    public boolean isCache;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.business.PrepaidCardListApiHelper
 * JD-Core Version:    0.6.0
 */