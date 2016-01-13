package com.dianping.booking.util;

import java.util.Iterator;
import java.util.List;

public class BookingHistorySearchManager
{
  private final int MAX_HISTORY_SIZE;
  private String fileName;
  private List<BookingSearch> searchList;

  public BookingHistorySearchManager(String paramString, int paramInt)
  {
    this.fileName = paramString;
    this.MAX_HISTORY_SIZE = paramInt;
    parseAddressFile();
  }

  // ERROR //
  private void parseAddressFile()
  {
    // Byte code:
    //   0: aload_0
    //   1: new 31	java/util/LinkedList
    //   4: dup
    //   5: invokespecial 32	java/util/LinkedList:<init>	()V
    //   8: putfield 34	com/dianping/booking/util/BookingHistorySearchManager:searchList	Ljava/util/List;
    //   11: invokestatic 40	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   14: invokevirtual 46	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   17: aload_0
    //   18: getfield 21	com/dianping/booking/util/BookingHistorySearchManager:fileName	Ljava/lang/String;
    //   21: invokevirtual 52	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   24: astore_2
    //   25: aconst_null
    //   26: astore_1
    //   27: aconst_null
    //   28: astore 4
    //   30: new 54	java/io/BufferedReader
    //   33: dup
    //   34: new 56	java/io/FileReader
    //   37: dup
    //   38: aload_2
    //   39: invokespecial 59	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   42: invokespecial 62	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   45: astore_2
    //   46: aload_2
    //   47: invokevirtual 66	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   50: astore_1
    //   51: aload_1
    //   52: ifnull +47 -> 99
    //   55: new 6	com/dianping/booking/util/BookingHistorySearchManager$MyBookingSearch
    //   58: dup
    //   59: invokespecial 67	com/dianping/booking/util/BookingHistorySearchManager$MyBookingSearch:<init>	()V
    //   62: astore_3
    //   63: aload_0
    //   64: getfield 34	com/dianping/booking/util/BookingHistorySearchManager:searchList	Ljava/util/List;
    //   67: aload_3
    //   68: aload_1
    //   69: invokeinterface 73 2 0
    //   74: invokeinterface 79 2 0
    //   79: pop
    //   80: goto -34 -> 46
    //   83: astore_3
    //   84: aload_2
    //   85: astore_1
    //   86: aload_3
    //   87: invokevirtual 82	java/lang/Exception:printStackTrace	()V
    //   90: aload_2
    //   91: ifnull +7 -> 98
    //   94: aload_2
    //   95: invokevirtual 85	java/io/BufferedReader:close	()V
    //   98: return
    //   99: aload_2
    //   100: ifnull +54 -> 154
    //   103: aload_2
    //   104: invokevirtual 85	java/io/BufferedReader:close	()V
    //   107: return
    //   108: astore_1
    //   109: aload_1
    //   110: invokevirtual 82	java/lang/Exception:printStackTrace	()V
    //   113: return
    //   114: astore_1
    //   115: aload_1
    //   116: invokevirtual 82	java/lang/Exception:printStackTrace	()V
    //   119: return
    //   120: astore_2
    //   121: aload_1
    //   122: ifnull +7 -> 129
    //   125: aload_1
    //   126: invokevirtual 85	java/io/BufferedReader:close	()V
    //   129: aload_2
    //   130: athrow
    //   131: astore_1
    //   132: aload_1
    //   133: invokevirtual 82	java/lang/Exception:printStackTrace	()V
    //   136: goto -7 -> 129
    //   139: astore_3
    //   140: aload_2
    //   141: astore_1
    //   142: aload_3
    //   143: astore_2
    //   144: goto -23 -> 121
    //   147: astore_3
    //   148: aload 4
    //   150: astore_2
    //   151: goto -67 -> 84
    //   154: return
    //
    // Exception table:
    //   from	to	target	type
    //   46	51	83	java/lang/Exception
    //   55	80	83	java/lang/Exception
    //   103	107	108	java/lang/Exception
    //   94	98	114	java/lang/Exception
    //   30	46	120	finally
    //   86	90	120	finally
    //   125	129	131	java/lang/Exception
    //   46	51	139	finally
    //   55	80	139	finally
    //   30	46	147	java/lang/Exception
  }

  private void removeDuplicateAddress(BookingSearch paramBookingSearch)
  {
    Iterator localIterator = this.searchList.iterator();
    while (localIterator.hasNext())
    {
      BookingSearch localBookingSearch = (BookingSearch)localIterator.next();
      if (!paramBookingSearch.equals(localBookingSearch))
        continue;
      this.searchList.remove(localBookingSearch);
    }
  }

  // ERROR //
  private void saveAddressFile()
  {
    // Byte code:
    //   0: invokestatic 40	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   3: invokevirtual 46	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   6: aload_0
    //   7: getfield 21	com/dianping/booking/util/BookingHistorySearchManager:fileName	Ljava/lang/String;
    //   10: invokevirtual 52	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   13: astore_2
    //   14: aload_2
    //   15: invokevirtual 117	java/io/File:exists	()Z
    //   18: ifne +8 -> 26
    //   21: aload_2
    //   22: invokevirtual 120	java/io/File:createNewFile	()Z
    //   25: pop
    //   26: aconst_null
    //   27: astore_3
    //   28: aconst_null
    //   29: astore_1
    //   30: aconst_null
    //   31: astore 4
    //   33: new 122	java/io/BufferedWriter
    //   36: dup
    //   37: new 124	java/io/FileWriter
    //   40: dup
    //   41: aload_2
    //   42: invokespecial 125	java/io/FileWriter:<init>	(Ljava/io/File;)V
    //   45: invokespecial 128	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
    //   48: astore_2
    //   49: aload_0
    //   50: getfield 34	com/dianping/booking/util/BookingHistorySearchManager:searchList	Ljava/util/List;
    //   53: invokeinterface 91 1 0
    //   58: astore_1
    //   59: aload_1
    //   60: invokeinterface 97 1 0
    //   65: ifeq +56 -> 121
    //   68: aload_2
    //   69: aload_1
    //   70: invokeinterface 101 1 0
    //   75: checkcast 69	com/dianping/booking/util/BookingSearch
    //   78: invokeinterface 131 1 0
    //   83: invokevirtual 135	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   86: aload_2
    //   87: invokevirtual 138	java/io/BufferedWriter:newLine	()V
    //   90: aload_2
    //   91: invokevirtual 141	java/io/BufferedWriter:flush	()V
    //   94: goto -35 -> 59
    //   97: astore_3
    //   98: aload_2
    //   99: astore_1
    //   100: aload_3
    //   101: invokevirtual 142	java/io/FileNotFoundException:printStackTrace	()V
    //   104: aload_2
    //   105: ifnull +7 -> 112
    //   108: aload_2
    //   109: invokevirtual 143	java/io/BufferedWriter:close	()V
    //   112: return
    //   113: astore_1
    //   114: aload_1
    //   115: invokevirtual 144	java/io/IOException:printStackTrace	()V
    //   118: goto -92 -> 26
    //   121: aload_2
    //   122: ifnull +84 -> 206
    //   125: aload_2
    //   126: invokevirtual 143	java/io/BufferedWriter:close	()V
    //   129: return
    //   130: astore_1
    //   131: aload_1
    //   132: invokevirtual 144	java/io/IOException:printStackTrace	()V
    //   135: return
    //   136: astore_1
    //   137: aload_1
    //   138: invokevirtual 144	java/io/IOException:printStackTrace	()V
    //   141: return
    //   142: astore_1
    //   143: aload_3
    //   144: astore_2
    //   145: aload_1
    //   146: astore_3
    //   147: aload_2
    //   148: astore_1
    //   149: aload_3
    //   150: invokevirtual 144	java/io/IOException:printStackTrace	()V
    //   153: aload_2
    //   154: ifnull -42 -> 112
    //   157: aload_2
    //   158: invokevirtual 143	java/io/BufferedWriter:close	()V
    //   161: return
    //   162: astore_1
    //   163: aload_1
    //   164: invokevirtual 144	java/io/IOException:printStackTrace	()V
    //   167: return
    //   168: astore_2
    //   169: aload_1
    //   170: ifnull +7 -> 177
    //   173: aload_1
    //   174: invokevirtual 143	java/io/BufferedWriter:close	()V
    //   177: aload_2
    //   178: athrow
    //   179: astore_1
    //   180: aload_1
    //   181: invokevirtual 144	java/io/IOException:printStackTrace	()V
    //   184: goto -7 -> 177
    //   187: astore_3
    //   188: aload_2
    //   189: astore_1
    //   190: aload_3
    //   191: astore_2
    //   192: goto -23 -> 169
    //   195: astore_3
    //   196: goto -49 -> 147
    //   199: astore_3
    //   200: aload 4
    //   202: astore_2
    //   203: goto -105 -> 98
    //   206: return
    //
    // Exception table:
    //   from	to	target	type
    //   49	59	97	java/io/FileNotFoundException
    //   59	94	97	java/io/FileNotFoundException
    //   21	26	113	java/io/IOException
    //   125	129	130	java/io/IOException
    //   108	112	136	java/io/IOException
    //   33	49	142	java/io/IOException
    //   157	161	162	java/io/IOException
    //   33	49	168	finally
    //   100	104	168	finally
    //   149	153	168	finally
    //   173	177	179	java/io/IOException
    //   49	59	187	finally
    //   59	94	187	finally
    //   49	59	195	java/io/IOException
    //   59	94	195	java/io/IOException
    //   33	49	199	java/io/FileNotFoundException
  }

  public void addAddress(BookingSearch paramBookingSearch)
  {
    removeDuplicateAddress(paramBookingSearch);
    while (this.searchList.size() >= this.MAX_HISTORY_SIZE)
      this.searchList.remove(this.searchList.size() - 1);
    this.searchList.add(0, paramBookingSearch);
    saveAddressFile();
  }

  public void clearHistory()
  {
    this.searchList.clear();
    saveAddressFile();
  }

  public List<BookingSearch> getAddressList()
  {
    return this.searchList;
  }

  public int getHistorySize()
  {
    return this.searchList.size();
  }

  public void setAddressList(List<BookingSearch> paramList)
  {
    this.searchList = paramList;
  }

  public static class MyBookingSearch
    implements BookingSearch
  {
    private String search = "";

    public MyBookingSearch()
    {
    }

    public MyBookingSearch(String paramString)
    {
      this.search = paramString;
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == null);
      do
        return false;
      while ((!(paramObject instanceof MyBookingSearch)) || (!((MyBookingSearch)paramObject).getSearchKey().equals(this.search)));
      return true;
    }

    public String getSearchKey()
    {
      return this.search;
    }

    public BookingSearch parseAddress(String paramString)
    {
      this.search = paramString;
      return this;
    }

    public String toInfoString()
    {
      return this.search;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.util.BookingHistorySearchManager
 * JD-Core Version:    0.6.0
 */