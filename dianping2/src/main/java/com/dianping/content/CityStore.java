package com.dianping.content;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.model.City;
import com.dianping.util.Daemon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

abstract class CityStore
{
  private static final String BINARY_CITIES = "PL4JF98GHJSLSNF0IK";
  private static final int LOAD_LOCAL_CITY = 1;
  private static final String TAG = CityStore.class.getSimpleName();
  private static LoadLocalCitiesListener mListener;
  private HashMap<String, ArrayList<City>> m1stCharCityMap = new HashMap();
  private City[] mCities = new City[0];
  private Context mContext;
  Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == 1) && (CityStore.mListener != null))
        CityStore.mListener.onAfterLoad();
    }
  };
  private HashMap<Integer, City> mIdCityMap = new HashMap();

  public CityStore(Context paramContext)
  {
    this.mContext = paramContext;
    if (mListener != null)
      mListener.onPreLoad();
    new Thread(new Runnable()
    {
      public void run()
      {
        CityStore.this.loadFromFile();
      }
    }).start();
  }

  public static void addLoadLocalCityListener(LoadLocalCitiesListener paramLoadLocalCitiesListener)
  {
    mListener = paramLoadLocalCitiesListener;
  }

  // ERROR //
  private void loadFromFile()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 5
    //   3: aconst_null
    //   4: astore_1
    //   5: aconst_null
    //   6: astore_3
    //   7: aload 5
    //   9: astore_2
    //   10: aload_0
    //   11: getfield 67	com/dianping/content/CityStore:mContext	Landroid/content/Context;
    //   14: ldc 17
    //   16: invokevirtual 101	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   19: astore 4
    //   21: aload 5
    //   23: astore_2
    //   24: aload 4
    //   26: astore_3
    //   27: aload 4
    //   29: astore_1
    //   30: aload 4
    //   32: invokevirtual 107	java/io/FileInputStream:available	()I
    //   35: newarray byte
    //   37: astore 5
    //   39: aload 5
    //   41: astore_2
    //   42: aload 4
    //   44: astore_3
    //   45: aload 4
    //   47: astore_1
    //   48: aload 4
    //   50: aload 5
    //   52: invokevirtual 111	java/io/FileInputStream:read	([B)I
    //   55: pop
    //   56: aload 5
    //   58: astore_2
    //   59: aload 4
    //   61: astore_3
    //   62: aload 4
    //   64: astore_1
    //   65: getstatic 44	com/dianping/content/CityStore:TAG	Ljava/lang/String;
    //   68: ldc 113
    //   70: invokestatic 119	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   73: aload 5
    //   75: astore_1
    //   76: aload 4
    //   78: ifnull +11 -> 89
    //   81: aload 4
    //   83: invokevirtual 122	java/io/FileInputStream:close	()V
    //   86: aload 5
    //   88: astore_1
    //   89: aload_1
    //   90: astore_2
    //   91: aload_1
    //   92: ifnonnull +38 -> 130
    //   95: aload_0
    //   96: getfield 67	com/dianping/content/CityStore:mContext	Landroid/content/Context;
    //   99: invokevirtual 126	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   102: getstatic 131	com/dianping/v1/R$raw:cities	I
    //   105: invokevirtual 137	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   108: astore_3
    //   109: aload_3
    //   110: invokevirtual 140	java/io/InputStream:available	()I
    //   113: newarray byte
    //   115: astore_2
    //   116: aload_2
    //   117: astore_1
    //   118: aload_3
    //   119: aload_2
    //   120: invokevirtual 141	java/io/InputStream:read	([B)I
    //   123: pop
    //   124: aload_2
    //   125: astore_1
    //   126: aload_3
    //   127: invokevirtual 142	java/io/InputStream:close	()V
    //   130: aload_2
    //   131: ifnonnull +92 -> 223
    //   134: aload_0
    //   135: getfield 65	com/dianping/content/CityStore:mHandler	Landroid/os/Handler;
    //   138: iconst_1
    //   139: invokevirtual 148	android/os/Handler:sendEmptyMessage	(I)Z
    //   142: pop
    //   143: getstatic 44	com/dianping/content/CityStore:TAG	Ljava/lang/String;
    //   146: ldc 150
    //   148: invokestatic 153	com/dianping/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   151: return
    //   152: astore_1
    //   153: aload_1
    //   154: invokevirtual 156	java/io/IOException:printStackTrace	()V
    //   157: aload 5
    //   159: astore_1
    //   160: goto -71 -> 89
    //   163: astore_1
    //   164: aload_2
    //   165: astore_1
    //   166: aload_3
    //   167: ifnull -78 -> 89
    //   170: aload_3
    //   171: invokevirtual 122	java/io/FileInputStream:close	()V
    //   174: aload_2
    //   175: astore_1
    //   176: goto -87 -> 89
    //   179: astore_1
    //   180: aload_1
    //   181: invokevirtual 156	java/io/IOException:printStackTrace	()V
    //   184: aload_2
    //   185: astore_1
    //   186: goto -97 -> 89
    //   189: astore_2
    //   190: aload_1
    //   191: ifnull +7 -> 198
    //   194: aload_1
    //   195: invokevirtual 122	java/io/FileInputStream:close	()V
    //   198: aload_2
    //   199: athrow
    //   200: astore_1
    //   201: aload_1
    //   202: invokevirtual 156	java/io/IOException:printStackTrace	()V
    //   205: goto -7 -> 198
    //   208: astore_2
    //   209: getstatic 44	com/dianping/content/CityStore:TAG	Ljava/lang/String;
    //   212: ldc 158
    //   214: aload_2
    //   215: invokestatic 161	com/dianping/util/Log:i	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   218: aload_1
    //   219: astore_2
    //   220: goto -90 -> 130
    //   223: new 163	com/dianping/archive/Unarchiver
    //   226: dup
    //   227: aload_2
    //   228: invokespecial 166	com/dianping/archive/Unarchiver:<init>	([B)V
    //   231: astore_2
    //   232: aconst_null
    //   233: astore_1
    //   234: aload_2
    //   235: invokevirtual 170	com/dianping/archive/Unarchiver:readDPObject	()Lcom/dianping/archive/DPObject;
    //   238: astore_2
    //   239: aload_2
    //   240: astore_1
    //   241: aload_1
    //   242: ifnull +126 -> 368
    //   245: aload_1
    //   246: ldc 172
    //   248: invokevirtual 178	com/dianping/archive/DPObject:getArray	(Ljava/lang/String;)[Lcom/dianping/archive/DPObject;
    //   251: astore_2
    //   252: new 180	java/util/ArrayList
    //   255: dup
    //   256: invokespecial 181	java/util/ArrayList:<init>	()V
    //   259: astore_1
    //   260: iconst_0
    //   261: istore 6
    //   263: iload 6
    //   265: aload_2
    //   266: arraylength
    //   267: if_icmpge +67 -> 334
    //   270: aload_2
    //   271: iload 6
    //   273: aaload
    //   274: ldc 183
    //   276: invokevirtual 187	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   279: invokestatic 193	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   282: ifeq +37 -> 319
    //   285: aload_1
    //   286: aload_2
    //   287: iload 6
    //   289: aaload
    //   290: invokestatic 197	com/dianping/model/City:fromDPObject	(Lcom/dianping/archive/DPObject;)Lcom/dianping/model/City;
    //   293: invokevirtual 201	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   296: pop
    //   297: iload 6
    //   299: iconst_1
    //   300: iadd
    //   301: istore 6
    //   303: goto -40 -> 263
    //   306: astore_2
    //   307: getstatic 44	com/dianping/content/CityStore:TAG	Ljava/lang/String;
    //   310: ldc 158
    //   312: aload_2
    //   313: invokestatic 203	com/dianping/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   316: goto -75 -> 241
    //   319: aload_1
    //   320: aload_2
    //   321: iload 6
    //   323: aaload
    //   324: invokestatic 206	com/dianping/model/City:fromDPObjectWithUrl	(Lcom/dianping/archive/DPObject;)Lcom/dianping/model/City;
    //   327: invokevirtual 201	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   330: pop
    //   331: goto -34 -> 297
    //   334: aload_1
    //   335: invokevirtual 209	java/util/ArrayList:size	()I
    //   338: anewarray 51	com/dianping/model/City
    //   341: astore_2
    //   342: aload_1
    //   343: aload_2
    //   344: invokevirtual 213	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   347: pop
    //   348: aload_2
    //   349: arraylength
    //   350: ifle +8 -> 358
    //   353: aload_0
    //   354: aload_2
    //   355: invokespecial 217	com/dianping/content/CityStore:setCities	([Lcom/dianping/model/City;)V
    //   358: aload_0
    //   359: getfield 65	com/dianping/content/CityStore:mHandler	Landroid/os/Handler;
    //   362: iconst_1
    //   363: invokevirtual 148	android/os/Handler:sendEmptyMessage	(I)Z
    //   366: pop
    //   367: return
    //   368: getstatic 44	com/dianping/content/CityStore:TAG	Ljava/lang/String;
    //   371: ldc 219
    //   373: invokestatic 153	com/dianping/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   376: goto -18 -> 358
    //
    // Exception table:
    //   from	to	target	type
    //   81	86	152	java/io/IOException
    //   10	21	163	java/lang/Exception
    //   30	39	163	java/lang/Exception
    //   48	56	163	java/lang/Exception
    //   65	73	163	java/lang/Exception
    //   170	174	179	java/io/IOException
    //   10	21	189	finally
    //   30	39	189	finally
    //   48	56	189	finally
    //   65	73	189	finally
    //   194	198	200	java/io/IOException
    //   109	116	208	java/lang/Exception
    //   118	124	208	java/lang/Exception
    //   126	130	208	java/lang/Exception
    //   234	239	306	java/lang/Exception
  }

  public static void removeLoadLocalCityListener()
  {
    mListener = null;
  }

  private void setCities(City[] paramArrayOfCity)
  {
    this.mIdCityMap.clear();
    this.m1stCharCityMap.clear();
    this.mCities = getSpecialCities(paramArrayOfCity);
    City[] arrayOfCity = this.mCities;
    int j = arrayOfCity.length;
    int i = 0;
    while (i < j)
    {
      City localCity = arrayOfCity[i];
      this.mIdCityMap.put(Integer.valueOf(localCity.id()), localCity);
      ArrayList localArrayList = (ArrayList)this.m1stCharCityMap.get(localCity.firstChar());
      paramArrayOfCity = localArrayList;
      if (localArrayList == null)
      {
        paramArrayOfCity = new ArrayList();
        this.m1stCharCityMap.put(localCity.firstChar(), paramArrayOfCity);
      }
      paramArrayOfCity.add(localCity);
      i += 1;
    }
  }

  public City[] getCities()
  {
    return this.mCities;
  }

  public City getCityById(int paramInt)
  {
    if (this.mIdCityMap == null)
      return null;
    return (City)this.mIdCityMap.get(Integer.valueOf(paramInt));
  }

  public final City[] getSortBy1stChar()
  {
    ArrayList localArrayList = new ArrayList();
    Object[] arrayOfObject = this.m1stCharCityMap.keySet().toArray();
    Arrays.sort(arrayOfObject);
    int j = arrayOfObject.length;
    int i = 0;
    while (i < j)
    {
      Object localObject = arrayOfObject[i];
      localArrayList.addAll((Collection)this.m1stCharCityMap.get(localObject));
      i += 1;
    }
    return (City[])localArrayList.toArray(new City[localArrayList.size()]);
  }

  protected abstract City[] getSpecialCities(City[] paramArrayOfCity);

  public void setCities(DPObject[] paramArrayOfDPObject, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null);
    do
    {
      return;
      City[] arrayOfCity = new City[paramArrayOfDPObject.length];
      int i = 0;
      if (i < paramArrayOfDPObject.length)
      {
        if (TextUtils.isEmpty(paramArrayOfDPObject[i].getString("Url")))
          arrayOfCity[i] = City.fromDPObject(paramArrayOfDPObject[i]);
        while (true)
        {
          i += 1;
          break;
          arrayOfCity[i] = City.fromDPObjectWithUrl(paramArrayOfDPObject[i]);
        }
      }
      setCities(arrayOfCity);
      paramArrayOfDPObject = new Handler(Daemon.looper());
    }
    while (paramArrayOfDPObject == null);
    paramArrayOfDPObject.post(new Runnable(paramArrayOfByte)
    {
      // ERROR //
      public void run()
      {
        // Byte code:
        //   0: aconst_null
        //   1: astore_2
        //   2: aconst_null
        //   3: astore_1
        //   4: aload_0
        //   5: getfield 19	com/dianping/content/CityStore$3:this$0	Lcom/dianping/content/CityStore;
        //   8: invokestatic 34	com/dianping/content/CityStore:access$200	(Lcom/dianping/content/CityStore;)Landroid/content/Context;
        //   11: ldc 36
        //   13: iconst_0
        //   14: invokevirtual 42	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
        //   17: astore_3
        //   18: aload_3
        //   19: astore_1
        //   20: aload_3
        //   21: astore_2
        //   22: aload_3
        //   23: aload_0
        //   24: getfield 21	com/dianping/content/CityStore$3:val$bytes	[B
        //   27: invokevirtual 48	java/io/FileOutputStream:write	([B)V
        //   30: aload_3
        //   31: ifnull +7 -> 38
        //   34: aload_3
        //   35: invokevirtual 51	java/io/FileOutputStream:close	()V
        //   38: return
        //   39: astore_2
        //   40: aload_1
        //   41: ifnull -3 -> 38
        //   44: aload_1
        //   45: invokevirtual 51	java/io/FileOutputStream:close	()V
        //   48: return
        //   49: astore_1
        //   50: return
        //   51: astore_1
        //   52: aload_2
        //   53: ifnull +7 -> 60
        //   56: aload_2
        //   57: invokevirtual 51	java/io/FileOutputStream:close	()V
        //   60: aload_1
        //   61: athrow
        //   62: astore_1
        //   63: return
        //   64: astore_2
        //   65: goto -5 -> 60
        //
        // Exception table:
        //   from	to	target	type
        //   4	18	39	java/lang/Exception
        //   22	30	39	java/lang/Exception
        //   44	48	49	java/io/IOException
        //   4	18	51	finally
        //   22	30	51	finally
        //   34	38	62	java/io/IOException
        //   56	60	64	java/io/IOException
      }
    });
  }

  public static abstract interface LoadLocalCitiesListener
  {
    public abstract void onAfterLoad();

    public abstract void onPreLoad();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.content.CityStore
 * JD-Core Version:    0.6.0
 */