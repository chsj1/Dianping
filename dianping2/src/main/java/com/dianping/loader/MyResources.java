package com.dianping.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.app.DPApplication;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.SiteSpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MyResources
{
  static final HashMap<String, MyResources> loaders = new HashMap();
  AssetManager asset;
  MyResources[] deps;
  FileSpec file;
  String packageName;
  Resources res;

  MyResources(FileSpec paramFileSpec, String paramString, Resources paramResources, AssetManager paramAssetManager, MyResources[] paramArrayOfMyResources)
  {
    this.file = paramFileSpec;
    this.packageName = paramString;
    this.res = paramResources;
    this.asset = paramAssetManager;
    this.deps = paramArrayOfMyResources;
  }

  static MyResources getResource(MyClassLoader paramMyClassLoader)
  {
    FileSpec localFileSpec = paramMyClassLoader.file;
    Object localObject1 = (MyResources)loaders.get(localFileSpec.id());
    if (localObject1 != null)
      return localObject1;
    localObject1 = null;
    int i;
    if (paramMyClassLoader.deps != null)
    {
      localObject2 = new MyResources[paramMyClassLoader.deps.length];
      i = 0;
      while (true)
      {
        localObject1 = localObject2;
        if (i >= localObject2.length)
          break;
        localObject2[i] = getResource(paramMyClassLoader.deps[i]);
        i += 1;
      }
    }
    paramMyClassLoader = new File(DPApplication.instance().getFilesDir(), "repo");
    if (!paramMyClassLoader.isDirectory())
      throw new RuntimeException(paramMyClassLoader + " not exists");
    Object localObject2 = new File(paramMyClassLoader, localFileSpec.id());
    if (TextUtils.isEmpty(localFileSpec.md5()));
    File localFile;
    for (paramMyClassLoader = "1.apk"; ; paramMyClassLoader = localFileSpec.md5() + ".apk")
    {
      localFile = new File((File)localObject2, paramMyClassLoader);
      if (localFile.isFile())
        break;
      throw new RuntimeException(localFile + " not exists");
    }
    while (true)
    {
      try
      {
        localAssetManager = (AssetManager)AssetManager.class.newInstance();
        localAssetManager.getClass().getMethod("addAssetPath", new Class[] { String.class }).invoke(localAssetManager, new Object[] { localFile.getAbsolutePath() });
        paramMyClassLoader = DPApplication.instance().getResources();
        localResources = new Resources(localAssetManager, paramMyClassLoader.getDisplayMetrics(), paramMyClassLoader.getConfiguration());
        localObject2 = null;
        XmlResourceParser localXmlResourceParser = localAssetManager.openXmlResourceParser("AndroidManifest.xml");
        i = localXmlResourceParser.getEventType();
        break label456;
        i = localXmlResourceParser.nextToken();
        break label456;
        if (!"manifest".equals(localXmlResourceParser.getName()))
          continue;
        paramMyClassLoader = localXmlResourceParser.getAttributeValue(null, "package");
        localXmlResourceParser.close();
        if (paramMyClassLoader != null)
          continue;
        throw new RuntimeException("package not found in AndroidManifest.xml [" + localFile + "]");
      }
      catch (Exception paramMyClassLoader)
      {
        AssetManager localAssetManager;
        Resources localResources;
        if (!(paramMyClassLoader instanceof RuntimeException))
          continue;
        throw ((RuntimeException)paramMyClassLoader);
        paramMyClassLoader = new MyResources(localFileSpec, paramMyClassLoader, localResources, localAssetManager, localObject1);
        loaders.put(localFileSpec.id(), paramMyClassLoader);
        return paramMyClassLoader;
        throw new RuntimeException(paramMyClassLoader);
      }
      label456: paramMyClassLoader = (MyClassLoader)localObject2;
      if (i == 1)
        continue;
      switch (i)
      {
      case 2:
      }
    }
  }

  public static MyResources getResource(SiteSpec paramSiteSpec, FileSpec paramFileSpec)
  {
    Object localObject1 = (MyResources)loaders.get(paramFileSpec.id());
    if (localObject1 != null)
      return localObject1;
    Object localObject3 = paramFileSpec.deps();
    localObject1 = null;
    int i;
    if (localObject3 != null)
    {
      localObject2 = new MyResources[localObject3.length];
      i = 0;
      while (true)
      {
        localObject1 = localObject2;
        if (i >= localObject3.length)
          break;
        localObject1 = paramSiteSpec.getFile(localObject3[i]);
        if (localObject1 == null)
          return null;
        localObject1 = getResource(paramSiteSpec, (FileSpec)localObject1);
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
    File localFile;
    for (paramSiteSpec = "1.apk"; ; paramSiteSpec = paramFileSpec.md5() + ".apk")
    {
      localFile = new File((File)localObject2, paramSiteSpec);
      if (localFile.isFile())
        break;
      return null;
    }
    while (true)
    {
      try
      {
        localObject3 = (AssetManager)AssetManager.class.newInstance();
        localObject3.getClass().getMethod("addAssetPath", new Class[] { String.class }).invoke(localObject3, new Object[] { localFile.getAbsolutePath() });
        localObject2 = null;
        XmlResourceParser localXmlResourceParser = ((AssetManager)localObject3).openXmlResourceParser("AndroidManifest.xml");
        i = localXmlResourceParser.getEventType();
        break label419;
        i = localXmlResourceParser.nextToken();
        break label419;
        if (!"manifest".equals(localXmlResourceParser.getName()))
          continue;
        paramSiteSpec = localXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "package");
        localXmlResourceParser.close();
        if (paramSiteSpec != null)
          continue;
        throw new RuntimeException("package not found in AndroidManifest.xml [" + localFile + "]");
      }
      catch (Exception paramSiteSpec)
      {
        if (!(paramSiteSpec instanceof RuntimeException))
          continue;
        throw ((RuntimeException)paramSiteSpec);
        localObject2 = DPApplication.instance().getResources();
        paramSiteSpec = new MyResources(paramFileSpec, paramSiteSpec, new Resources((AssetManager)localObject3, ((Resources)localObject2).getDisplayMetrics(), ((Resources)localObject2).getConfiguration()), (AssetManager)localObject3, localObject1);
        loaders.put(paramFileSpec.id(), paramSiteSpec);
        return paramSiteSpec;
        throw new RuntimeException(paramSiteSpec);
      }
      label419: paramSiteSpec = (SiteSpec)localObject2;
      if (i == 1)
        continue;
      switch (i)
      {
      case 2:
      }
    }
  }

  public static MyResources getResource(Class<?> paramClass)
  {
    if ((paramClass.getClassLoader() instanceof MyClassLoader))
      return getResource((MyClassLoader)paramClass.getClassLoader());
    return getRootResource();
  }

  static MyResources getRootResource()
  {
    Object localObject = (MyResources)loaders.get("root");
    if (localObject != null)
      return localObject;
    localObject = DPApplication.instance();
    localObject = new RootResources(new FileSpec("root", "", null, 0, 0, null), ((Context)localObject).getPackageName(), ((Context)localObject).getResources(), ((Context)localObject).getAssets(), new MyResources[0]);
    loaders.put("root", localObject);
    return (MyResources)localObject;
  }

  private View inf(Context paramContext, String paramString, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    int i = this.res.getIdentifier(paramString, "layout", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label89;
        View localView = arrayOfMyResources[i].inf(paramContext, paramString, paramViewGroup, paramBoolean);
        localObject = localView;
        if (localView != null)
          break;
        i += 1;
      }
      label89: return null;
    }
    return inflate(paramContext, i, paramViewGroup, paramBoolean);
  }

  private Integer searchColor(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "color", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        Integer localInteger = arrayOfMyResources[i].searchColor(paramString);
        localObject = localInteger;
        if (localInteger != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return Integer.valueOf(this.res.getColor(i));
  }

  private ColorStateList searchColorStateList(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "color", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        ColorStateList localColorStateList = arrayOfMyResources[i].searchColorStateList(paramString);
        localObject = localColorStateList;
        if (localColorStateList != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return this.res.getColorStateList(i);
  }

  private Float searchDimension(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "dimen", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        Float localFloat = arrayOfMyResources[i].searchDimension(paramString);
        localObject = localFloat;
        if (localFloat != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return Float.valueOf(this.res.getDimension(i));
  }

  private Integer searchDimensionPixelOffset(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "dimen", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        Integer localInteger = arrayOfMyResources[i].searchDimensionPixelOffset(paramString);
        localObject = localInteger;
        if (localInteger != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return Integer.valueOf(this.res.getDimensionPixelOffset(i));
  }

  private Integer searchDimensionPixelSize(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "dimen", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        Integer localInteger = arrayOfMyResources[i].searchDimensionPixelSize(paramString);
        localObject = localInteger;
        if (localInteger != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return Integer.valueOf(this.res.getDimensionPixelSize(i));
  }

  private Drawable searchDrawable(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "drawable", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        Drawable localDrawable = arrayOfMyResources[i].searchDrawable(paramString);
        localObject = localDrawable;
        if (localDrawable != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return this.res.getDrawable(i);
  }

  private InputStream searchRawResource(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "raw", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        InputStream localInputStream = arrayOfMyResources[i].searchRawResource(paramString);
        localObject = localInputStream;
        if (localInputStream != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return this.res.openRawResource(i);
  }

  private String searchString(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "string", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        String str = arrayOfMyResources[i].searchString(paramString);
        localObject = str;
        if (str != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return this.res.getString(i);
  }

  private String[] searchStringArray(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "array", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        String[] arrayOfString = arrayOfMyResources[i].searchStringArray(paramString);
        localObject = arrayOfString;
        if (arrayOfString != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return this.res.getStringArray(i);
  }

  private CharSequence searchText(String paramString)
  {
    int i = this.res.getIdentifier(paramString, "string", this.packageName);
    if (i == 0)
    {
      Object localObject;
      if (this.deps == null)
      {
        localObject = null;
        return localObject;
      }
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      i = 0;
      while (true)
      {
        if (i >= j)
          break label79;
        CharSequence localCharSequence = arrayOfMyResources[i].searchText(paramString);
        localObject = localCharSequence;
        if (localCharSequence != null)
          break;
        i += 1;
      }
      label79: return null;
    }
    return this.res.getText(i);
  }

  public byte[] getAsset(String paramString)
    throws IOException
  {
    paramString = openAsset(paramString);
    int i = paramString.available();
    if (i > 0);
    ByteArrayOutputStream localByteArrayOutputStream;
    while (true)
    {
      localByteArrayOutputStream = new ByteArrayOutputStream(i);
      byte[] arrayOfByte = new byte[4096];
      while (true)
      {
        i = paramString.read(arrayOfByte);
        if (i == -1)
          break;
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
      }
      i = 4096;
    }
    paramString.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public AssetManager getAssets()
  {
    return this.asset;
  }

  public int getColor(int paramInt)
  {
    return this.res.getColor(paramInt);
  }

  public int getColor(String paramString)
  {
    Object localObject = searchColor(paramString);
    if (localObject == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "color", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":color/" + paramString + " not found in " + this.file);
      return ((DPApplication)localObject).getResources().getColor(i);
    }
    return ((Integer)localObject).intValue();
  }

  public ColorStateList getColorStateList(int paramInt)
  {
    return this.res.getColorStateList(paramInt);
  }

  public ColorStateList getColorStateList(String paramString)
  {
    ColorStateList localColorStateList = searchColorStateList(paramString);
    Object localObject = localColorStateList;
    if (localColorStateList == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "color", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":color/" + paramString + " not found in " + this.file);
      localObject = ((DPApplication)localObject).getResources().getColorStateList(i);
    }
    return (ColorStateList)localObject;
  }

  public float getDimension(int paramInt)
  {
    return this.res.getDimension(paramInt);
  }

  public float getDimension(String paramString)
  {
    Object localObject = searchDimension(paramString);
    if (localObject == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "dimen", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":dimen/" + paramString + " not found in " + this.file);
      return ((DPApplication)localObject).getResources().getDimension(i);
    }
    return ((Float)localObject).floatValue();
  }

  public int getDimensionPixelOffset(int paramInt)
  {
    return this.res.getDimensionPixelOffset(paramInt);
  }

  public int getDimensionPixelOffset(String paramString)
  {
    Object localObject = searchDimensionPixelOffset(paramString);
    if (localObject == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "dimen", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":dimen/" + paramString + " not found in " + this.file);
      return ((DPApplication)localObject).getResources().getDimensionPixelOffset(i);
    }
    return ((Integer)localObject).intValue();
  }

  public int getDimensionPixelSize(int paramInt)
  {
    return this.res.getDimensionPixelSize(paramInt);
  }

  public int getDimensionPixelSize(String paramString)
  {
    Object localObject = searchDimensionPixelSize(paramString);
    if (localObject == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "dimen", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":dimen/" + paramString + " not found in " + this.file);
      return ((DPApplication)localObject).getResources().getDimensionPixelSize(i);
    }
    return ((Integer)localObject).intValue();
  }

  public Drawable getDrawable(int paramInt)
  {
    return this.res.getDrawable(paramInt);
  }

  public Drawable getDrawable(String paramString)
  {
    Drawable localDrawable = searchDrawable(paramString);
    Object localObject = localDrawable;
    if (localDrawable == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "drawable", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":drawable/" + paramString + " not found in " + this.file);
      localObject = ((DPApplication)localObject).getResources().getDrawable(i);
    }
    return (Drawable)localObject;
  }

  public byte[] getRawResource(int paramInt)
  {
    InputStream localInputStream = openRawResource(paramInt);
    ByteArrayOutputStream localByteArrayOutputStream;
    while (true)
    {
      try
      {
        paramInt = localInputStream.available();
        if (paramInt > 0)
        {
          localByteArrayOutputStream = new ByteArrayOutputStream(paramInt);
          byte[] arrayOfByte2 = new byte[4096];
          paramInt = localInputStream.read(arrayOfByte2);
          if (paramInt == -1)
            break;
          localByteArrayOutputStream.write(arrayOfByte2, 0, paramInt);
          continue;
        }
      }
      catch (Exception localException)
      {
        return new byte[0];
      }
      paramInt = 4096;
    }
    localException.close();
    byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
    return arrayOfByte1;
  }

  public byte[] getRawResource(String paramString)
  {
    paramString = openRawResource(paramString);
    ByteArrayOutputStream localByteArrayOutputStream;
    while (true)
    {
      try
      {
        i = paramString.available();
        if (i > 0)
        {
          localByteArrayOutputStream = new ByteArrayOutputStream(i);
          byte[] arrayOfByte = new byte[4096];
          i = paramString.read(arrayOfByte);
          if (i == -1)
            break;
          localByteArrayOutputStream.write(arrayOfByte, 0, i);
          continue;
        }
      }
      catch (Exception paramString)
      {
        return new byte[0];
      }
      int i = 4096;
    }
    paramString.close();
    paramString = localByteArrayOutputStream.toByteArray();
    return paramString;
  }

  public Resources getResources()
  {
    return this.res;
  }

  public String getString(int paramInt)
  {
    return this.res.getString(paramInt);
  }

  public String getString(String paramString)
  {
    String str = searchString(paramString);
    Object localObject = str;
    if (str == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "string", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":string/" + paramString + " not found in " + this.file);
      localObject = ((DPApplication)localObject).getString(i);
    }
    return (String)localObject;
  }

  public String[] getStringArray(int paramInt)
  {
    return this.res.getStringArray(paramInt);
  }

  public String[] getStringArray(String paramString)
  {
    String[] arrayOfString = searchStringArray(paramString);
    Object localObject = arrayOfString;
    if (arrayOfString == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "array", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":array/" + paramString + " not found in " + this.file);
      localObject = ((DPApplication)localObject).getResources().getStringArray(i);
    }
    return (String)localObject;
  }

  public CharSequence getText(int paramInt)
  {
    return this.res.getText(paramInt);
  }

  public CharSequence getText(String paramString)
  {
    CharSequence localCharSequence = searchText(paramString);
    Object localObject = localCharSequence;
    if (localCharSequence == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "string", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":string/" + paramString + " not found in " + this.file);
      localObject = ((DPApplication)localObject).getText(i);
    }
    return (CharSequence)localObject;
  }

  public View inflate(Context paramContext, int paramInt, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    if (!(paramContext instanceof ResourceOverrideable))
      throw new RuntimeException("unable to inflate without ResourceOverrideable context");
    ResourceOverrideable localResourceOverrideable = (ResourceOverrideable)paramContext;
    MyResources localMyResources = localResourceOverrideable.getOverrideResources();
    localResourceOverrideable.setOverrideResources(this);
    try
    {
      paramContext = LayoutInflater.from(paramContext).inflate(paramInt, paramViewGroup, paramBoolean);
      return paramContext;
    }
    finally
    {
      localResourceOverrideable.setOverrideResources(localMyResources);
    }
    throw paramContext;
  }

  public View inflate(Context paramContext, String paramString, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    View localView = inf(paramContext, paramString, paramViewGroup, paramBoolean);
    Object localObject = localView;
    if (localView == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "layout", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":layout/" + paramString + " not found in " + this.file);
      localObject = LayoutInflater.from(paramContext).inflate(i, paramViewGroup, paramBoolean);
    }
    return (View)localObject;
  }

  public InputStream openAsset(String paramString)
    throws IOException
  {
    return openAsset(paramString, 2);
  }

  public InputStream openAsset(String paramString, int paramInt)
    throws IOException
  {
    try
    {
      InputStream localInputStream = this.asset.open(paramString, paramInt);
      return localInputStream;
    }
    catch (IOException localIOException1)
    {
      if (this.deps == null)
        throw localIOException1;
      MyResources[] arrayOfMyResources = this.deps;
      int j = arrayOfMyResources.length;
      int i = 0;
      while (i < j)
      {
        Object localObject = arrayOfMyResources[i];
        try
        {
          localObject = ((MyResources)localObject).openAsset(paramString, paramInt);
          return localObject;
        }
        catch (IOException localIOException2)
        {
          i += 1;
        }
      }
      try
      {
        paramString = DPApplication.instance().getAssets().open(paramString, paramInt);
        return paramString;
      }
      catch (IOException paramString)
      {
      }
    }
    throw localIOException1;
  }

  public InputStream openRawResource(int paramInt)
  {
    return this.res.openRawResource(paramInt);
  }

  public InputStream openRawResource(String paramString)
  {
    InputStream localInputStream = searchRawResource(paramString);
    Object localObject = localInputStream;
    if (localInputStream == null)
    {
      localObject = DPApplication.instance();
      int i = ((DPApplication)localObject).getResources().getIdentifier(paramString, "raw", ((DPApplication)localObject).getPackageName());
      if (i == 0)
        throw new Resources.NotFoundException("@" + this.packageName + ":raw/" + paramString + " not found in " + this.file);
      localObject = ((DPApplication)localObject).getResources().openRawResource(i);
    }
    return (InputStream)localObject;
  }

  public static abstract interface ResourceOverrideable
  {
    public abstract MyResources getOverrideResources();

    public abstract void setOverrideResources(MyResources paramMyResources);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.MyResources
 * JD-Core Version:    0.6.0
 */