package com.tencent.a.a;

import java.io.File;
import java.io.FileFilter;

class i
  implements FileFilter
{
  i(g paramg)
  {
  }

  public boolean accept(File paramFile)
  {
    if (!paramFile.getName().endsWith(this.a.j()));
    do
      return false;
    while (g.d(paramFile) == -1);
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.a.i
 * JD-Core Version:    0.6.0
 */