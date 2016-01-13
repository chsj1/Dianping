package com.tencent.stat.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class d
{
  public static File a(String paramString)
  {
    paramString = new File(paramString);
    if (paramString.exists())
      return paramString;
    if (!paramString.getParentFile().exists())
      a(paramString.getParentFile().getAbsolutePath());
    paramString.mkdir();
    return paramString;
  }

  public static List<String> a(File paramFile)
  {
    paramFile = new BufferedReader(new FileReader(paramFile));
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      String str = paramFile.readLine();
      if (str == null)
        return localArrayList;
      localArrayList.add(str.trim());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.common.d
 * JD-Core Version:    0.6.0
 */