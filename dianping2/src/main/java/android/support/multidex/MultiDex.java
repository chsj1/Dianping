package android.support.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.util.Log;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

public final class MultiDex
{
  private static final boolean IS_VM_MULTIDEX_CAPABLE;
  private static final int MAX_SUPPORTED_SDK_VERSION = 20;
  private static final int MIN_SDK_VERSION = 4;
  private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
  private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
  static final String TAG = "MultiDex";
  private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
  private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
  private static final Set<String> installedApk = new HashSet();

  static
  {
    IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));
  }

  private static boolean checkValidZipFiles(List<File> paramList)
  {
    paramList = paramList.iterator();
    while (paramList.hasNext())
      if (!MultiDexExtractor.verifyZipFile((File)paramList.next()))
        return false;
    return true;
  }

  private static void clearOldDexDir(Context paramContext)
    throws Exception
  {
    paramContext = new File(paramContext.getFilesDir(), "secondary-dexes");
    File[] arrayOfFile;
    if (paramContext.isDirectory())
    {
      Log.i("MultiDex", "Clearing old secondary dex dir (" + paramContext.getPath() + ").");
      arrayOfFile = paramContext.listFiles();
      if (arrayOfFile == null)
        Log.w("MultiDex", "Failed to list secondary dex dir content (" + paramContext.getPath() + ").");
    }
    else
    {
      return;
    }
    int j = arrayOfFile.length;
    int i = 0;
    if (i < j)
    {
      File localFile = arrayOfFile[i];
      Log.i("MultiDex", "Trying to delete old file " + localFile.getPath() + " of size " + localFile.length());
      if (!localFile.delete())
        Log.w("MultiDex", "Failed to delete old file " + localFile.getPath());
      while (true)
      {
        i += 1;
        break;
        Log.i("MultiDex", "Deleted old file " + localFile.getPath());
      }
    }
    if (!paramContext.delete())
    {
      Log.w("MultiDex", "Failed to delete secondary dex dir " + paramContext.getPath());
      return;
    }
    Log.i("MultiDex", "Deleted old secondary dex dir " + paramContext.getPath());
  }

  private static void expandFieldArray(Object paramObject, String paramString, Object[] paramArrayOfObject)
    throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    paramString = findField(paramObject, paramString);
    Object[] arrayOfObject1 = (Object[])(Object[])paramString.get(paramObject);
    Object[] arrayOfObject2 = (Object[])(Object[])Array.newInstance(arrayOfObject1.getClass().getComponentType(), arrayOfObject1.length + paramArrayOfObject.length);
    System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, arrayOfObject1.length);
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject2, arrayOfObject1.length, paramArrayOfObject.length);
    paramString.set(paramObject, arrayOfObject2);
  }

  private static Field findField(Object paramObject, String paramString)
    throws NoSuchFieldException
  {
    Class localClass = paramObject.getClass();
    while (localClass != null)
      try
      {
        Field localField = localClass.getDeclaredField(paramString);
        if (!localField.isAccessible())
          localField.setAccessible(true);
        return localField;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        localClass = localClass.getSuperclass();
      }
    throw new NoSuchFieldException("Field " + paramString + " not found in " + paramObject.getClass());
  }

  private static Method findMethod(Object paramObject, String paramString, Class<?>[] paramArrayOfClass)
    throws NoSuchMethodException
  {
    Class localClass = paramObject.getClass();
    while (localClass != null)
      try
      {
        Method localMethod = localClass.getDeclaredMethod(paramString, paramArrayOfClass);
        if (!localMethod.isAccessible())
          localMethod.setAccessible(true);
        return localMethod;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        localClass = localClass.getSuperclass();
      }
    throw new NoSuchMethodException("Method " + paramString + " with parameters " + Arrays.asList(paramArrayOfClass) + " not found in " + paramObject.getClass());
  }

  private static ApplicationInfo getApplicationInfo(Context paramContext)
    throws PackageManager.NameNotFoundException
  {
    PackageManager localPackageManager;
    try
    {
      localPackageManager = paramContext.getPackageManager();
      paramContext = paramContext.getPackageName();
      if ((localPackageManager == null) || (paramContext == null))
        return null;
    }
    catch (RuntimeException paramContext)
    {
      Log.w("MultiDex", "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", paramContext);
      return null;
    }
    return localPackageManager.getApplicationInfo(paramContext, 128);
  }

  public static void install(Context paramContext)
  {
    Log.i("MultiDex", "install");
    if (IS_VM_MULTIDEX_CAPABLE)
      Log.i("MultiDex", "VM has multidex support, MultiDex support library is disabled.");
    ApplicationInfo localApplicationInfo;
    Object localObject;
    while (true)
    {
      return;
      if (Build.VERSION.SDK_INT < 4)
        throw new RuntimeException("Multi dex installation failed. SDK " + Build.VERSION.SDK_INT + " is unsupported. Min SDK version is " + 4 + ".");
      try
      {
        localApplicationInfo = getApplicationInfo(paramContext);
        if (localApplicationInfo == null)
          continue;
        synchronized (installedApk)
        {
          localObject = localApplicationInfo.sourceDir;
          if (!installedApk.contains(localObject))
            break;
          return;
        }
      }
      catch (Exception paramContext)
      {
        Log.e("MultiDex", "Multidex installation failure", paramContext);
        throw new RuntimeException("Multi dex installation failed (" + paramContext.getMessage() + ").");
      }
    }
    installedApk.add(localObject);
    if (Build.VERSION.SDK_INT > 20)
      Log.w("MultiDex", "MultiDex is not guaranteed to work in SDK version " + Build.VERSION.SDK_INT + ": SDK version higher than " + 20 + " should be backed by " + "runtime with built-in multidex capabilty but it's not the " + "case here: java.vm.version=\"" + System.getProperty("java.vm.version") + "\"");
    try
    {
      localObject = paramContext.getClassLoader();
      if (localObject == null)
      {
        Log.e("MultiDex", "Context class loader is null. Must be running in test mode. Skip patching.");
        monitorexit;
        return;
      }
    }
    catch (RuntimeException paramContext)
    {
      Log.w("MultiDex", "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", paramContext);
      monitorexit;
      return;
    }
    try
    {
      clearOldDexDir(paramContext);
      File localFile = new File(localApplicationInfo.dataDir, SECONDARY_FOLDER_NAME);
      List localList = MultiDexExtractor.load(paramContext, localApplicationInfo, localFile, false);
      if (checkValidZipFiles(localList))
      {
        installSecondaryDexes((ClassLoader)localObject, localFile, localList);
        monitorexit;
        Log.i("MultiDex", "install done");
        return;
      }
    }
    catch (Throwable localThrowable)
    {
      while (true)
      {
        Log.w("MultiDex", "Something went wrong when trying to clear old MultiDex extraction, continuing without cleaning.", localThrowable);
        continue;
        Log.w("MultiDex", "Files were not valid zip files.  Forcing a reload.");
        paramContext = MultiDexExtractor.load(paramContext, localApplicationInfo, localThrowable, true);
        if (!checkValidZipFiles(paramContext))
          break;
        installSecondaryDexes((ClassLoader)localObject, localThrowable, paramContext);
      }
    }
    throw new RuntimeException("Zip files were not valid.");
  }

  private static void installSecondaryDexes(ClassLoader paramClassLoader, File paramFile, List<File> paramList)
    throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException
  {
    if (!paramList.isEmpty())
    {
      if (Build.VERSION.SDK_INT >= 19)
        V19.access$000(paramClassLoader, paramList, paramFile);
    }
    else
      return;
    if (Build.VERSION.SDK_INT >= 14)
    {
      V14.access$100(paramClassLoader, paramList, paramFile);
      return;
    }
    V4.access$200(paramClassLoader, paramList);
  }

  static boolean isVMMultidexCapable(String paramString)
  {
    int m = 0;
    int k = m;
    Object localObject;
    if (paramString != null)
    {
      localObject = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(paramString);
      k = m;
      if (!((Matcher)localObject).matches());
    }
    try
    {
      int i = Integer.parseInt(((Matcher)localObject).group(1));
      int j = Integer.parseInt(((Matcher)localObject).group(2));
      if ((i > 2) || ((i == 2) && (j >= 1)))
      {
        k = 1;
        localObject = new StringBuilder().append("VM with version ").append(paramString);
        if (k == 0)
          break label119;
      }
      label119: for (paramString = " has multidex support"; ; paramString = " does not have multidex support")
      {
        Log.i("MultiDex", paramString);
        return k;
        k = 0;
        break;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        k = m;
    }
  }

  private static final class V14
  {
    private static void install(ClassLoader paramClassLoader, List<File> paramList, File paramFile)
      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException
    {
      paramClassLoader = MultiDex.access$300(paramClassLoader, "pathList").get(paramClassLoader);
      MultiDex.access$400(paramClassLoader, "dexElements", makeDexElements(paramClassLoader, new ArrayList(paramList), paramFile));
    }

    private static Object[] makeDexElements(Object paramObject, ArrayList<File> paramArrayList, File paramFile)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
      return (Object[])(Object[])MultiDex.access$500(paramObject, "makeDexElements", new Class[] { ArrayList.class, File.class }).invoke(paramObject, new Object[] { paramArrayList, paramFile });
    }
  }

  private static final class V19
  {
    private static void install(ClassLoader paramClassLoader, List<File> paramList, File paramFile)
      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException
    {
      Object localObject = MultiDex.access$300(paramClassLoader, "pathList").get(paramClassLoader);
      ArrayList localArrayList = new ArrayList();
      MultiDex.access$400(localObject, "dexElements", makeDexElements(localObject, new ArrayList(paramList), paramFile, localArrayList));
      if (localArrayList.size() > 0)
      {
        paramList = localArrayList.iterator();
        while (paramList.hasNext())
          Log.w("MultiDex", "Exception in makeDexElement", (IOException)paramList.next());
        paramFile = MultiDex.access$300(paramClassLoader, "dexElementsSuppressedExceptions");
        localObject = (IOException[])(IOException[])paramFile.get(paramClassLoader);
        if (localObject != null)
          break label130;
        paramList = (IOException[])localArrayList.toArray(new IOException[localArrayList.size()]);
      }
      while (true)
      {
        paramFile.set(paramClassLoader, paramList);
        return;
        label130: paramList = new IOException[localArrayList.size() + localObject.length];
        localArrayList.toArray(paramList);
        System.arraycopy(localObject, 0, paramList, localArrayList.size(), localObject.length);
      }
    }

    private static Object[] makeDexElements(Object paramObject, ArrayList<File> paramArrayList, File paramFile, ArrayList<IOException> paramArrayList1)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
      return (Object[])(Object[])MultiDex.access$500(paramObject, "makeDexElements", new Class[] { ArrayList.class, File.class, ArrayList.class }).invoke(paramObject, new Object[] { paramArrayList, paramFile, paramArrayList1 });
    }
  }

  private static final class V4
  {
    private static void install(ClassLoader paramClassLoader, List<File> paramList)
      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException
    {
      int i = paramList.size();
      Field localField = MultiDex.access$300(paramClassLoader, "path");
      StringBuilder localStringBuilder = new StringBuilder((String)localField.get(paramClassLoader));
      String[] arrayOfString = new String[i];
      File[] arrayOfFile = new File[i];
      ZipFile[] arrayOfZipFile = new ZipFile[i];
      DexFile[] arrayOfDexFile = new DexFile[i];
      paramList = paramList.listIterator();
      while (paramList.hasNext())
      {
        File localFile = (File)paramList.next();
        String str = localFile.getAbsolutePath();
        localStringBuilder.append(':').append(str);
        i = paramList.previousIndex();
        arrayOfString[i] = str;
        arrayOfFile[i] = localFile;
        arrayOfZipFile[i] = new ZipFile(localFile);
        arrayOfDexFile[i] = DexFile.loadDex(str, str + ".dex", 0);
      }
      localField.set(paramClassLoader, localStringBuilder.toString());
      MultiDex.access$400(paramClassLoader, "mPaths", arrayOfString);
      MultiDex.access$400(paramClassLoader, "mFiles", arrayOfFile);
      MultiDex.access$400(paramClassLoader, "mZips", arrayOfZipFile);
      MultiDex.access$400(paramClassLoader, "mDexs", arrayOfDexFile);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.multidex.MultiDex
 * JD-Core Version:    0.6.0
 */