package com.dianping.takeaway.util;

public class TakeawayFileRWer<T>
{
  public static final String HISTORY_FILE_NAME = "takeaway_shop_history";
  public static final String SEARCHED_ADRESS_FILE_NAME = "takeaway_searched_address";
  public static final String TA_MENU_CACHE_FILE = "ta_menu_cache";
  private String fileName;

  public TakeawayFileRWer(String paramString)
  {
    this.fileName = paramString;
  }

  // ERROR //
  public java.util.ArrayList<T> readFileToList(FileLineParser<T> paramFileLineParser)
  {
    // Byte code:
    //   0: new 33	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 34	java/util/ArrayList:<init>	()V
    //   7: astore 5
    //   9: invokestatic 40	com/dianping/app/DPApplication:instance	()Lcom/dianping/app/DPApplication;
    //   12: invokevirtual 44	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   15: aload_0
    //   16: getfield 26	com/dianping/takeaway/util/TakeawayFileRWer:fileName	Ljava/lang/String;
    //   19: invokevirtual 50	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   22: astore_3
    //   23: aconst_null
    //   24: astore_2
    //   25: aconst_null
    //   26: astore 4
    //   28: new 52	java/io/BufferedReader
    //   31: dup
    //   32: new 54	java/io/FileReader
    //   35: dup
    //   36: aload_3
    //   37: invokespecial 57	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   40: invokespecial 60	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   43: astore_3
    //   44: aload_3
    //   45: invokevirtual 64	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   48: astore_2
    //   49: aload_2
    //   50: ifnull +41 -> 91
    //   53: aload 5
    //   55: aload_1
    //   56: aload_2
    //   57: invokeinterface 68 2 0
    //   62: invokevirtual 72	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   65: pop
    //   66: goto -22 -> 44
    //   69: astore_2
    //   70: aload_3
    //   71: astore_1
    //   72: aload_2
    //   73: astore_3
    //   74: aload_1
    //   75: astore_2
    //   76: aload_3
    //   77: invokevirtual 75	java/lang/Exception:printStackTrace	()V
    //   80: aload_1
    //   81: ifnull +7 -> 88
    //   84: aload_1
    //   85: invokevirtual 78	java/io/BufferedReader:close	()V
    //   88: aload 5
    //   90: areturn
    //   91: aload_3
    //   92: ifnull +58 -> 150
    //   95: aload_3
    //   96: invokevirtual 78	java/io/BufferedReader:close	()V
    //   99: aload 5
    //   101: areturn
    //   102: astore_1
    //   103: aload_1
    //   104: invokevirtual 75	java/lang/Exception:printStackTrace	()V
    //   107: aload 5
    //   109: areturn
    //   110: astore_1
    //   111: aload_1
    //   112: invokevirtual 75	java/lang/Exception:printStackTrace	()V
    //   115: aload 5
    //   117: areturn
    //   118: astore_1
    //   119: aload_2
    //   120: ifnull +7 -> 127
    //   123: aload_2
    //   124: invokevirtual 78	java/io/BufferedReader:close	()V
    //   127: aload_1
    //   128: athrow
    //   129: astore_2
    //   130: aload_2
    //   131: invokevirtual 75	java/lang/Exception:printStackTrace	()V
    //   134: goto -7 -> 127
    //   137: astore_1
    //   138: aload_3
    //   139: astore_2
    //   140: goto -21 -> 119
    //   143: astore_3
    //   144: aload 4
    //   146: astore_1
    //   147: goto -73 -> 74
    //   150: aload 5
    //   152: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   44	49	69	java/lang/Exception
    //   53	66	69	java/lang/Exception
    //   95	99	102	java/lang/Exception
    //   84	88	110	java/lang/Exception
    //   28	44	118	finally
    //   76	80	118	finally
    //   123	127	129	java/lang/Exception
    //   44	49	137	finally
    //   53	66	137	finally
    //   28	44	143	java/lang/Exception
  }

  // ERROR //
  public void writeListToFile(java.util.List<T> paramList)
  {
    // Byte code:
    //   0: invokestatic 40	com/dianping/app/DPApplication:instance	()Lcom/dianping/app/DPApplication;
    //   3: invokevirtual 44	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   6: aload_0
    //   7: getfield 26	com/dianping/takeaway/util/TakeawayFileRWer:fileName	Ljava/lang/String;
    //   10: invokevirtual 50	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   13: astore_3
    //   14: aload_3
    //   15: invokevirtual 90	java/io/File:exists	()Z
    //   18: ifne +8 -> 26
    //   21: aload_3
    //   22: invokevirtual 93	java/io/File:createNewFile	()Z
    //   25: pop
    //   26: aconst_null
    //   27: astore_2
    //   28: aconst_null
    //   29: astore 4
    //   31: new 95	java/io/BufferedWriter
    //   34: dup
    //   35: new 97	java/io/FileWriter
    //   38: dup
    //   39: aload_3
    //   40: invokespecial 98	java/io/FileWriter:<init>	(Ljava/io/File;)V
    //   43: invokespecial 101	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
    //   46: astore_3
    //   47: aload_1
    //   48: invokeinterface 107 1 0
    //   53: astore_1
    //   54: aload_1
    //   55: invokeinterface 112 1 0
    //   60: ifeq +55 -> 115
    //   63: aload_3
    //   64: aload_1
    //   65: invokeinterface 116 1 0
    //   70: invokevirtual 119	java/lang/Object:toString	()Ljava/lang/String;
    //   73: invokevirtual 122	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   76: aload_3
    //   77: invokevirtual 125	java/io/BufferedWriter:newLine	()V
    //   80: aload_3
    //   81: invokevirtual 128	java/io/BufferedWriter:flush	()V
    //   84: goto -30 -> 54
    //   87: astore_2
    //   88: aload_3
    //   89: astore_1
    //   90: aload_2
    //   91: astore_3
    //   92: aload_1
    //   93: astore_2
    //   94: aload_3
    //   95: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   98: aload_1
    //   99: ifnull +7 -> 106
    //   102: aload_1
    //   103: invokevirtual 130	java/io/BufferedWriter:close	()V
    //   106: return
    //   107: astore_2
    //   108: aload_2
    //   109: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   112: goto -86 -> 26
    //   115: aload_3
    //   116: ifnull +52 -> 168
    //   119: aload_3
    //   120: invokevirtual 130	java/io/BufferedWriter:close	()V
    //   123: return
    //   124: astore_1
    //   125: aload_1
    //   126: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   129: return
    //   130: astore_1
    //   131: aload_1
    //   132: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   135: return
    //   136: astore_1
    //   137: aload_2
    //   138: ifnull +7 -> 145
    //   141: aload_2
    //   142: invokevirtual 130	java/io/BufferedWriter:close	()V
    //   145: aload_1
    //   146: athrow
    //   147: astore_2
    //   148: aload_2
    //   149: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   152: goto -7 -> 145
    //   155: astore_1
    //   156: aload_3
    //   157: astore_2
    //   158: goto -21 -> 137
    //   161: astore_3
    //   162: aload 4
    //   164: astore_1
    //   165: goto -73 -> 92
    //   168: return
    //
    // Exception table:
    //   from	to	target	type
    //   47	54	87	java/io/IOException
    //   54	84	87	java/io/IOException
    //   21	26	107	java/io/IOException
    //   119	123	124	java/io/IOException
    //   102	106	130	java/io/IOException
    //   31	47	136	finally
    //   94	98	136	finally
    //   141	145	147	java/io/IOException
    //   47	54	155	finally
    //   54	84	155	finally
    //   31	47	161	java/io/IOException
  }

  public static abstract interface FileLineParser<T>
  {
    public abstract T parse(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.util.TakeawayFileRWer
 * JD-Core Version:    0.6.0
 */