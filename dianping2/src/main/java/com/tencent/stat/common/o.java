package com.tencent.stat.common;

import java.io.File;

class o
{
  private static int a = -1;

  public static boolean a()
  {
    if (a == 1)
      return true;
    if (a == 0)
      return false;
    String[] arrayOfString = new String[6];
    arrayOfString[0] = "/bin";
    arrayOfString[1] = "/system/bin/";
    arrayOfString[2] = "/system/xbin/";
    arrayOfString[3] = "/system/sbin/";
    arrayOfString[4] = "/sbin/";
    arrayOfString[5] = "/vendor/bin/";
    int i = 0;
    while (true)
    {
      try
      {
        if (i >= arrayOfString.length)
          continue;
        File localFile = new File(arrayOfString[i] + "su");
        if ((localFile != null) && (localFile.exists()))
        {
          a = 1;
          return true;
        }
      }
      catch (Exception localException)
      {
        a = 0;
        return false;
      }
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.common.o
 * JD-Core Version:    0.6.0
 */