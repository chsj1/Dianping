package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.widget.TextView;
import com.google.zxing.client.android.R.string;

final class BookResultInfoRetriever extends SupplementalInfoRetriever
{
  private final Context context;
  private final String isbn;
  private final String source;

  BookResultInfoRetriever(TextView paramTextView, String paramString, Context paramContext)
  {
    super(paramTextView);
    this.isbn = paramString;
    this.source = paramContext.getString(R.string.msg_google_books);
    this.context = paramContext;
  }

  // ERROR //
  void retrieveSupplementalInfo()
    throws java.io.IOException
  {
    // Byte code:
    //   0: new 41	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 43	java/lang/StringBuilder:<init>	()V
    //   7: ldc 45
    //   9: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   12: aload_0
    //   13: getfield 16	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:isbn	Ljava/lang/String;
    //   16: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   19: invokevirtual 53	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   22: getstatic 59	com/google/zxing/client/android/HttpHelper$ContentType:JSON	Lcom/google/zxing/client/android/HttpHelper$ContentType;
    //   25: invokestatic 65	com/google/zxing/client/android/HttpHelper:downloadViaHttp	(Ljava/lang/String;Lcom/google/zxing/client/android/HttpHelper$ContentType;)Ljava/lang/CharSequence;
    //   28: astore_1
    //   29: aload_1
    //   30: invokeinterface 71 1 0
    //   35: ifne +4 -> 39
    //   38: return
    //   39: aconst_null
    //   40: astore_2
    //   41: new 73	org/json/JSONTokener
    //   44: dup
    //   45: aload_1
    //   46: invokeinterface 74 1 0
    //   51: invokespecial 77	org/json/JSONTokener:<init>	(Ljava/lang/String;)V
    //   54: invokevirtual 81	org/json/JSONTokener:nextValue	()Ljava/lang/Object;
    //   57: checkcast 83	org/json/JSONObject
    //   60: ldc 85
    //   62: invokevirtual 89	org/json/JSONObject:optJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   65: astore_1
    //   66: aload_1
    //   67: ifnull -29 -> 38
    //   70: aload_1
    //   71: iconst_0
    //   72: invokevirtual 95	org/json/JSONArray:isNull	(I)Z
    //   75: ifne -37 -> 38
    //   78: aload_1
    //   79: iconst_0
    //   80: invokevirtual 99	org/json/JSONArray:get	(I)Ljava/lang/Object;
    //   83: checkcast 83	org/json/JSONObject
    //   86: ldc 101
    //   88: invokevirtual 105	org/json/JSONObject:getJSONObject	(Ljava/lang/String;)Lorg/json/JSONObject;
    //   91: astore_1
    //   92: aload_1
    //   93: ifnull -55 -> 38
    //   96: aload_1
    //   97: ldc 107
    //   99: invokevirtual 111	org/json/JSONObject:optString	(Ljava/lang/String;)Ljava/lang/String;
    //   102: astore_3
    //   103: aload_1
    //   104: ldc 113
    //   106: invokevirtual 111	org/json/JSONObject:optString	(Ljava/lang/String;)Ljava/lang/String;
    //   109: astore 4
    //   111: aload_1
    //   112: ldc 115
    //   114: invokevirtual 89	org/json/JSONObject:optJSONArray	(Ljava/lang/String;)Lorg/json/JSONArray;
    //   117: astore 5
    //   119: aload_2
    //   120: astore_1
    //   121: aload 5
    //   123: ifnull +73 -> 196
    //   126: aload_2
    //   127: astore_1
    //   128: aload 5
    //   130: iconst_0
    //   131: invokevirtual 95	org/json/JSONArray:isNull	(I)Z
    //   134: ifne +62 -> 196
    //   137: new 117	java/util/ArrayList
    //   140: dup
    //   141: aload 5
    //   143: invokevirtual 118	org/json/JSONArray:length	()I
    //   146: invokespecial 121	java/util/ArrayList:<init>	(I)V
    //   149: astore_1
    //   150: iconst_0
    //   151: istore 6
    //   153: iload 6
    //   155: aload 5
    //   157: invokevirtual 118	org/json/JSONArray:length	()I
    //   160: if_icmpge +36 -> 196
    //   163: aload_1
    //   164: aload 5
    //   166: iload 6
    //   168: invokevirtual 122	org/json/JSONArray:getString	(I)Ljava/lang/String;
    //   171: invokeinterface 128 2 0
    //   176: pop
    //   177: iload 6
    //   179: iconst_1
    //   180: iadd
    //   181: istore 6
    //   183: goto -30 -> 153
    //   186: astore_1
    //   187: new 37	java/io/IOException
    //   190: dup
    //   191: aload_1
    //   192: invokespecial 131	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
    //   195: athrow
    //   196: new 117	java/util/ArrayList
    //   199: dup
    //   200: invokespecial 132	java/util/ArrayList:<init>	()V
    //   203: astore_2
    //   204: aload_3
    //   205: aload_2
    //   206: invokestatic 136	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:maybeAddText	(Ljava/lang/String;Ljava/util/Collection;)V
    //   209: aload_1
    //   210: aload_2
    //   211: invokestatic 140	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:maybeAddTextSeries	(Ljava/util/Collection;Ljava/util/Collection;)V
    //   214: aload 4
    //   216: ifnull +11 -> 227
    //   219: aload 4
    //   221: invokevirtual 146	java/lang/String:isEmpty	()Z
    //   224: ifeq +93 -> 317
    //   227: aconst_null
    //   228: astore_1
    //   229: aload_1
    //   230: aload_2
    //   231: invokestatic 136	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:maybeAddText	(Ljava/lang/String;Ljava/util/Collection;)V
    //   234: new 41	java/lang/StringBuilder
    //   237: dup
    //   238: invokespecial 43	java/lang/StringBuilder:<init>	()V
    //   241: ldc 148
    //   243: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   246: aload_0
    //   247: getfield 32	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:context	Landroid/content/Context;
    //   250: invokestatic 154	com/google/zxing/client/android/LocaleManager:getBookSearchCountryTLD	(Landroid/content/Context;)Ljava/lang/String;
    //   253: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   256: ldc 156
    //   258: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: invokevirtual 53	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   264: astore_1
    //   265: aload_0
    //   266: aload_0
    //   267: getfield 16	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:isbn	Ljava/lang/String;
    //   270: aload_0
    //   271: getfield 30	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:source	Ljava/lang/String;
    //   274: aload_2
    //   275: aload_2
    //   276: invokeinterface 159 1 0
    //   281: anewarray 142	java/lang/String
    //   284: invokeinterface 163 2 0
    //   289: checkcast 165	[Ljava/lang/String;
    //   292: new 41	java/lang/StringBuilder
    //   295: dup
    //   296: invokespecial 43	java/lang/StringBuilder:<init>	()V
    //   299: aload_1
    //   300: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   303: aload_0
    //   304: getfield 16	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:isbn	Ljava/lang/String;
    //   307: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: invokevirtual 53	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   313: invokevirtual 168	com/google/zxing/client/android/result/supplement/BookResultInfoRetriever:append	(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)V
    //   316: return
    //   317: new 41	java/lang/StringBuilder
    //   320: dup
    //   321: invokespecial 43	java/lang/StringBuilder:<init>	()V
    //   324: aload 4
    //   326: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   329: ldc 170
    //   331: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   334: invokevirtual 53	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   337: astore_1
    //   338: goto -109 -> 229
    //   341: astore_1
    //   342: goto -155 -> 187
    //
    // Exception table:
    //   from	to	target	type
    //   41	66	186	org/json/JSONException
    //   70	92	186	org/json/JSONException
    //   96	119	186	org/json/JSONException
    //   128	150	186	org/json/JSONException
    //   153	177	341	org/json/JSONException
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.supplement.BookResultInfoRetriever
 * JD-Core Version:    0.6.0
 */