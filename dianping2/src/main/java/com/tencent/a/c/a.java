package com.tencent.a.c;

import java.io.File;

public class a
{
  public static boolean a(File paramFile)
  {
    int i = 0;
    if (paramFile != null)
    {
      if (!paramFile.isFile())
        break label28;
      if (paramFile.delete())
        break label26;
      paramFile.deleteOnExit();
    }
    label26: label28: 
    do
    {
      return false;
      return true;
    }
    while (!paramFile.isDirectory());
    File[] arrayOfFile = paramFile.listFiles();
    int j = arrayOfFile.length;
    while (i < j)
    {
      a(arrayOfFile[i]);
      i += 1;
    }
    return paramFile.delete();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.c.a
 * JD-Core Version:    0.6.0
 */