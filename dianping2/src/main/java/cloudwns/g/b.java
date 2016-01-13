package cloudwns.g;

import android.util.SparseArray;
import com.tencent.base.util.g;
import com.tencent.wns.data.A2Ticket;
import com.tencent.wns.data.B2Ticket;
import java.util.HashMap;
import java.util.Map;

public final class b extends a
{
  private static final SparseArray a = new SparseArray();
  private static final SparseArray b = new SparseArray();

  public static int a(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length < 4))
      return 0;
    int i = paramArrayOfByte.length;
    return paramArrayOfByte[0] & 0xFF | (paramArrayOfByte[(i / 4)] & 0xFF) << 8 | (paramArrayOfByte[(i / 2)] & 0xFF) << 16 | (paramArrayOfByte[(i * 3 / 4)] & 0xFF) << 24;
  }

  @Deprecated
  public static long a(String paramString)
  {
    if (g.b(paramString))
      return 0L;
    if ("999".equals(paramString))
      return 999L;
    return a(paramString, 0);
  }

  public static long a(String paramString, int paramInt)
  {
    if (g.b(paramString))
      return 0L;
    try
    {
      long l = Long.parseLong(paramString);
      return l;
    }
    catch (NumberFormatException paramString)
    {
      paramString.printStackTrace();
    }
    return 0L;
  }

  public static A2Ticket a(long paramLong)
  {
    return b(String.valueOf(paramLong));
  }

  // ERROR //
  public static B2Ticket a(long paramLong, int paramInt)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: lload_0
    //   4: lconst_1
    //   5: lcmp
    //   6: ifge +8 -> 14
    //   9: ldc 2
    //   11: monitorexit
    //   12: aconst_null
    //   13: areturn
    //   14: getstatic 18	cloudwns/g/b:b	Landroid/util/SparseArray;
    //   17: astore_3
    //   18: aload_3
    //   19: monitorenter
    //   20: getstatic 18	cloudwns/g/b:b	Landroid/util/SparseArray;
    //   23: iload_2
    //   24: lload_0
    //   25: invokestatic 64	cloudwns/g/b:a	(Landroid/util/SparseArray;IJ)Lcom/tencent/wns/data/B2Ticket;
    //   28: astore 4
    //   30: aload_3
    //   31: monitorexit
    //   32: aload 4
    //   34: astore_3
    //   35: aload 4
    //   37: ifnonnull +15 -> 52
    //   40: invokestatic 67	cloudwns/g/b:a	()Lcloudwns/h/a;
    //   43: lload_0
    //   44: invokestatic 57	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   47: iload_2
    //   48: invokevirtual 72	cloudwns/h/a:a	(Ljava/lang/String;I)Lcom/tencent/wns/data/B2Ticket;
    //   51: astore_3
    //   52: ldc 2
    //   54: monitorexit
    //   55: aload_3
    //   56: areturn
    //   57: astore_3
    //   58: ldc 2
    //   60: monitorexit
    //   61: aload_3
    //   62: athrow
    //   63: astore 4
    //   65: aload_3
    //   66: monitorexit
    //   67: aload 4
    //   69: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   9	12	57	finally
    //   14	20	57	finally
    //   40	52	57	finally
    //   52	55	57	finally
    //   58	61	57	finally
    //   67	70	57	finally
    //   20	32	63	finally
    //   65	67	63	finally
  }

  private static B2Ticket a(SparseArray paramSparseArray, int paramInt, long paramLong)
  {
    paramSparseArray = (Map)paramSparseArray.get(paramInt);
    if (paramSparseArray != null)
      return (B2Ticket)paramSparseArray.get(Long.valueOf(paramLong));
    return null;
  }

  public static void a(long paramLong, B2Ticket paramB2Ticket)
  {
    a(paramLong, paramB2Ticket, 0);
  }

  public static void a(long paramLong, B2Ticket paramB2Ticket, int paramInt)
  {
    boolean bool2 = true;
    monitorenter;
    while (true)
    {
      try
      {
        B2Ticket localB2Ticket = a(paramLong, paramInt);
        if (paramB2Ticket == null)
          continue;
        bool1 = a().a(String.valueOf(paramLong), paramB2Ticket, paramInt);
        StringBuilder localStringBuilder = new StringBuilder().append("B2Ticket Database Saved = ").append(bool1).append(", b2==null is ");
        if (paramB2Ticket.b() == null)
        {
          bool1 = true;
          localStringBuilder = localStringBuilder.append(bool1).append(", b2_gtkey==null is ");
          if (paramB2Ticket.c() != null)
            break label177;
          bool1 = bool2;
          cloudwns.l.a.b("Ticket/Account", bool1);
          a(paramLong, paramB2Ticket, paramInt, localB2Ticket);
          return;
          a().b(String.valueOf(paramLong), paramInt);
          cloudwns.l.a.b("Ticket/Account", "b2 deleted uin=" + paramLong + ", loginType=" + paramInt);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      boolean bool1 = false;
      continue;
      label177: bool1 = false;
    }
  }

  private static void a(long paramLong, B2Ticket paramB2Ticket1, int paramInt, B2Ticket paramB2Ticket2)
  {
    Object localObject1;
    if (paramB2Ticket2 != null)
      synchronized (a)
      {
        ??? = (Map)a.get(paramInt);
        localObject1 = ???;
        if (??? == null)
        {
          localObject1 = new HashMap();
          a.put(paramInt, localObject1);
        }
        ((Map)localObject1).put(Long.valueOf(paramLong), paramB2Ticket2);
      }
    synchronized (b)
    {
      localObject1 = (Map)b.get(paramInt);
      paramB2Ticket2 = (B2Ticket)localObject1;
      if (localObject1 == null)
      {
        paramB2Ticket2 = new HashMap();
        b.put(paramInt, paramB2Ticket2);
      }
      paramB2Ticket2.put(Long.valueOf(paramLong), paramB2Ticket1);
      cloudwns.l.a.b("Ticket/Account", "b2 cache updated, uin=" + paramLong + ", loginType=" + paramInt);
      return;
      paramB2Ticket1 = finally;
      monitorexit;
      throw paramB2Ticket1;
    }
  }

  public static void a(long paramLong, B2Ticket paramB2Ticket, boolean paramBoolean)
  {
    monitorenter;
    if (!paramBoolean);
    try
    {
      A2Ticket localA2Ticket = a(paramLong);
      if (localA2Ticket == null)
      {
        cloudwns.l.a.e("WnsMain", "WTF ?! No A2 But Savin' B2 ? UIN = " + paramLong);
        paramB2Ticket.b(0);
      }
      while (true)
      {
        a(paramLong, paramB2Ticket);
        return;
        paramB2Ticket.b(a(localA2Ticket.a()));
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramB2Ticket;
  }

  public static boolean a(String paramString, long paramLong)
  {
    paramString = b(paramString, paramLong);
    if (paramString != null)
      cloudwns.l.a.d("Ticket/Account", "Check B2Ticket Failed, reason = " + paramString);
    return paramString == null;
  }

  public static A2Ticket b(String paramString)
  {
    return null;
  }

  public static B2Ticket b(long paramLong)
  {
    return a(paramLong, 0);
  }

  private static String b(String paramString, long paramLong)
  {
    if (paramString == null)
      return "ACCOUNT_NULL";
    if (paramLong < 1L)
      return "UIN_IS_ZERO";
    paramString = b(paramLong);
    if (paramString == null)
      return "B2_ALL_NULL";
    if (paramString.b() == null)
      return "B2_NULL";
    if (paramString.c() == null)
      return "B2_GT_NULL";
    return null;
  }

  public static B2Ticket c(long paramLong)
  {
    if (paramLong < 1L)
      return null;
    synchronized (a)
    {
      B2Ticket localB2Ticket = a(a, 0, paramLong);
      return localB2Ticket;
    }
  }

  public static void c(String paramString)
  {
  }

  public static void d(long paramLong)
  {
    a(paramLong, null);
  }

  public static String e(long paramLong)
  {
    if (paramLong != 999L)
      return "";
    return "";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.g.b
 * JD-Core Version:    0.6.0
 */