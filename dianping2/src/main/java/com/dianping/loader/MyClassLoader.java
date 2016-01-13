package com.dianping.loader;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.SiteSpec;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.util.HashMap;

public class MyClassLoader extends DexClassLoader
{
  static final HashMap<String, MyClassLoader> loaders = new HashMap();
  MyClassLoader[] deps;
  FileSpec file;

  MyClassLoader(FileSpec paramFileSpec, String paramString1, String paramString2, String paramString3, ClassLoader paramClassLoader, MyClassLoader[] paramArrayOfMyClassLoader)
  {
    super(paramString1, paramString2, paramString3, paramClassLoader);
    this.file = paramFileSpec;
    this.deps = paramArrayOfMyClassLoader;
  }

  public static MyClassLoader getClassLoader(SiteSpec paramSiteSpec, FileSpec paramFileSpec)
  {
    Object localObject1 = (MyClassLoader)loaders.get(paramFileSpec.id());
    if (localObject1 != null)
      return localObject1;
    String[] arrayOfString = paramFileSpec.deps();
    localObject1 = null;
    if (arrayOfString != null)
    {
      localObject2 = new MyClassLoader[arrayOfString.length];
      int i = 0;
      while (true)
      {
        localObject1 = localObject2;
        if (i >= arrayOfString.length)
          break;
        localObject1 = paramSiteSpec.getFile(arrayOfString[i]);
        if (localObject1 == null)
          return null;
        localObject1 = getClassLoader(paramSiteSpec, (FileSpec)localObject1);
        if (localObject1 == null)
          return null;
        localObject2[i] = localObject1;
        i += 1;
      }
    }
    paramSiteSpec = new File(DPApplication.instance().getFilesDir(), "repo");
    if (!paramSiteSpec.isDirectory())
      return null;
    Object localObject2 = new File(paramSiteSpec, paramFileSpec.id());
    if (TextUtils.isEmpty(paramFileSpec.md5()));
    for (paramSiteSpec = "1.apk"; ; paramSiteSpec = paramFileSpec.md5() + ".apk")
    {
      paramSiteSpec = new File((File)localObject2, paramSiteSpec);
      if (paramSiteSpec.isFile())
        break;
      return null;
    }
    localObject2 = new File((File)localObject2, "dexout");
    ((File)localObject2).mkdir();
    paramSiteSpec = new MyClassLoader(paramFileSpec, paramSiteSpec.getAbsolutePath(), ((File)localObject2).getAbsolutePath(), null, DPApplication.instance().getClassLoader(), localObject1);
    loaders.put(paramFileSpec.id(), paramSiteSpec);
    return (MyClassLoader)(MyClassLoader)paramSiteSpec;
  }

  @SuppressLint({"NewApi"})
  public Class<?> loadClass(String paramString, boolean paramBoolean)
    throws ClassNotFoundException
  {
    Object localObject1 = findLoadedClass(paramString);
    if (localObject1 != null)
      return localObject1;
    try
    {
      Object localObject2 = getParent().loadClass(paramString);
      localObject1 = localObject2;
      label25: if (localObject1 != null)
        return localObject1;
      localObject2 = localObject1;
      MyClassLoader[] arrayOfMyClassLoader;
      int j;
      int i;
      if (this.deps != null)
      {
        arrayOfMyClassLoader = this.deps;
        j = arrayOfMyClassLoader.length;
        i = 0;
      }
      while (true)
      {
        localObject2 = localObject1;
        if (i < j)
          localObject2 = arrayOfMyClassLoader[i];
        try
        {
          localObject2 = ((MyClassLoader)localObject2).findClass(paramString);
          if (localObject2 == null)
            break;
          return localObject2;
        }
        catch (ClassNotFoundException localClassNotFoundException1)
        {
          i += 1;
        }
      }
      return findClass(paramString);
    }
    catch (ClassNotFoundException localClassNotFoundException2)
    {
      break label25;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.MyClassLoader
 * JD-Core Version:    0.6.0
 */