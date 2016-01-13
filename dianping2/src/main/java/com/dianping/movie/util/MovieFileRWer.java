package com.dianping.movie.util;

public class MovieFileRWer<T>
{
  public static final String HISTORY_FILE_NAME = "movie_search_history";
  private String fileName;

  public MovieFileRWer(String paramString)
  {
    this.fileName = paramString;
  }

  // ERROR //
  public java.util.ArrayList<T> readFileToList(FileLineParser<T> paramFileLineParser)
  {
    // Byte code:
    //   0: new 27	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 28	java/util/ArrayList:<init>	()V
    //   7: astore 5
    //   9: invokestatic 34	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   12: invokevirtual 40	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   15: aload_0
    //   16: getfield 20	com/dianping/movie/util/MovieFileRWer:fileName	Ljava/lang/String;
    //   19: invokevirtual 46	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   22: astore_3
    //   23: aconst_null
    //   24: astore_2
    //   25: aconst_null
    //   26: astore 4
    //   28: new 48	java/io/BufferedReader
    //   31: dup
    //   32: new 50	java/io/FileReader
    //   35: dup
    //   36: aload_3
    //   37: invokespecial 53	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   40: invokespecial 56	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   43: astore_3
    //   44: aload_3
    //   45: invokevirtual 60	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   48: astore_2
    //   49: aload_2
    //   50: ifnull +41 -> 91
    //   53: aload 5
    //   55: aload_1
    //   56: aload_2
    //   57: invokeinterface 64 2 0
    //   62: invokevirtual 68	java/util/ArrayList:add	(Ljava/lang/Object;)Z
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
    //   77: invokevirtual 71	java/lang/Exception:printStackTrace	()V
    //   80: aload_1
    //   81: ifnull +7 -> 88
    //   84: aload_1
    //   85: invokevirtual 74	java/io/BufferedReader:close	()V
    //   88: aload 5
    //   90: areturn
    //   91: aload_3
    //   92: ifnull +58 -> 150
    //   95: aload_3
    //   96: invokevirtual 74	java/io/BufferedReader:close	()V
    //   99: aload 5
    //   101: areturn
    //   102: astore_1
    //   103: aload_1
    //   104: invokevirtual 71	java/lang/Exception:printStackTrace	()V
    //   107: aload 5
    //   109: areturn
    //   110: astore_1
    //   111: aload_1
    //   112: invokevirtual 71	java/lang/Exception:printStackTrace	()V
    //   115: aload 5
    //   117: areturn
    //   118: astore_1
    //   119: aload_2
    //   120: ifnull +7 -> 127
    //   123: aload_2
    //   124: invokevirtual 74	java/io/BufferedReader:close	()V
    //   127: aload_1
    //   128: athrow
    //   129: astore_2
    //   130: aload_2
    //   131: invokevirtual 71	java/lang/Exception:printStackTrace	()V
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
    //   0: invokestatic 34	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   3: invokevirtual 40	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   6: aload_0
    //   7: getfield 20	com/dianping/movie/util/MovieFileRWer:fileName	Ljava/lang/String;
    //   10: invokevirtual 46	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   13: astore_3
    //   14: aload_3
    //   15: invokevirtual 88	java/io/File:exists	()Z
    //   18: ifne +8 -> 26
    //   21: aload_3
    //   22: invokevirtual 91	java/io/File:createNewFile	()Z
    //   25: pop
    //   26: aconst_null
    //   27: astore 5
    //   29: aconst_null
    //   30: astore_2
    //   31: aconst_null
    //   32: astore 4
    //   34: new 93	java/io/BufferedWriter
    //   37: dup
    //   38: new 95	java/io/FileWriter
    //   41: dup
    //   42: aload_3
    //   43: invokespecial 96	java/io/FileWriter:<init>	(Ljava/io/File;)V
    //   46: invokespecial 99	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
    //   49: astore_3
    //   50: aload_1
    //   51: invokeinterface 105 1 0
    //   56: astore_1
    //   57: aload_1
    //   58: invokeinterface 110 1 0
    //   63: ifeq +55 -> 118
    //   66: aload_3
    //   67: aload_1
    //   68: invokeinterface 114 1 0
    //   73: invokevirtual 117	java/lang/Object:toString	()Ljava/lang/String;
    //   76: invokevirtual 120	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   79: aload_3
    //   80: invokevirtual 123	java/io/BufferedWriter:newLine	()V
    //   83: aload_3
    //   84: invokevirtual 126	java/io/BufferedWriter:flush	()V
    //   87: goto -30 -> 57
    //   90: astore_2
    //   91: aload_3
    //   92: astore_1
    //   93: aload_2
    //   94: astore_3
    //   95: aload_1
    //   96: astore_2
    //   97: aload_3
    //   98: invokevirtual 127	java/io/FileNotFoundException:printStackTrace	()V
    //   101: aload_1
    //   102: ifnull +7 -> 109
    //   105: aload_1
    //   106: invokevirtual 128	java/io/BufferedWriter:close	()V
    //   109: return
    //   110: astore_2
    //   111: aload_2
    //   112: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   115: goto -89 -> 26
    //   118: aload_3
    //   119: ifnull +85 -> 204
    //   122: aload_3
    //   123: invokevirtual 128	java/io/BufferedWriter:close	()V
    //   126: return
    //   127: astore_1
    //   128: aload_1
    //   129: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   132: return
    //   133: astore_1
    //   134: aload_1
    //   135: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   138: return
    //   139: astore_3
    //   140: aload 5
    //   142: astore_1
    //   143: aload_1
    //   144: astore_2
    //   145: aload_3
    //   146: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   149: aload_1
    //   150: ifnull -41 -> 109
    //   153: aload_1
    //   154: invokevirtual 128	java/io/BufferedWriter:close	()V
    //   157: return
    //   158: astore_1
    //   159: aload_1
    //   160: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   163: return
    //   164: astore_1
    //   165: aload_2
    //   166: ifnull +7 -> 173
    //   169: aload_2
    //   170: invokevirtual 128	java/io/BufferedWriter:close	()V
    //   173: aload_1
    //   174: athrow
    //   175: astore_2
    //   176: aload_2
    //   177: invokevirtual 129	java/io/IOException:printStackTrace	()V
    //   180: goto -7 -> 173
    //   183: astore_1
    //   184: aload_3
    //   185: astore_2
    //   186: goto -21 -> 165
    //   189: astore_2
    //   190: aload_3
    //   191: astore_1
    //   192: aload_2
    //   193: astore_3
    //   194: goto -51 -> 143
    //   197: astore_3
    //   198: aload 4
    //   200: astore_1
    //   201: goto -106 -> 95
    //   204: return
    //
    // Exception table:
    //   from	to	target	type
    //   50	57	90	java/io/FileNotFoundException
    //   57	87	90	java/io/FileNotFoundException
    //   21	26	110	java/io/IOException
    //   122	126	127	java/io/IOException
    //   105	109	133	java/io/IOException
    //   34	50	139	java/io/IOException
    //   153	157	158	java/io/IOException
    //   34	50	164	finally
    //   97	101	164	finally
    //   145	149	164	finally
    //   169	173	175	java/io/IOException
    //   50	57	183	finally
    //   57	87	183	finally
    //   50	57	189	java/io/IOException
    //   57	87	189	java/io/IOException
    //   34	50	197	java/io/FileNotFoundException
  }

  public static abstract interface FileLineParser<T>
  {
    public abstract T parse(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.util.MovieFileRWer
 * JD-Core Version:    0.6.0
 */