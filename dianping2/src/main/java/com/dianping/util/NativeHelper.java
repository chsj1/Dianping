package com.dianping.util;

public class NativeHelper
{
  public static final boolean A;
  public static final int VERSION = 1;

  static
  {
    boolean bool1 = false;
    try
    {
      System.loadLibrary("nh");
      boolean bool2 = a();
      bool1 = bool2;
      A = bool1;
      return;
    }
    catch (Throwable localThrowable)
    {
      while (true)
        Log.w("failed to load native helper");
    }
  }

  private static native boolean a();

  public static native boolean nd(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4);

  public static native byte[] ndug(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

  public static native boolean ne(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4);

  public static native byte[] nug(byte[] paramArrayOfByte);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.NativeHelper
 * JD-Core Version:    0.6.0
 */