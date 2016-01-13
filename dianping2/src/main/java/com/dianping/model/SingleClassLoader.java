package com.dianping.model;

public class SingleClassLoader extends ClassLoader
{
  private final Class cl;
  private final String className;

  public SingleClassLoader(Class paramClass)
  {
    this.cl = paramClass;
    this.className = paramClass.getName();
  }

  public Class<?> loadClass(String paramString)
    throws ClassNotFoundException
  {
    if (this.className.equals(paramString))
      return this.cl;
    return super.loadClass(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.SingleClassLoader
 * JD-Core Version:    0.6.0
 */