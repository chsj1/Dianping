package android.support.multidex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class MultiDexExtractor
{
  private static final int BUFFER_SIZE = 16384;
  private static final String DEX_PREFIX = "classes";
  private static final String DEX_SUFFIX = ".dex";
  private static final String EXTRACTED_NAME_EXT = ".classes";
  private static final String EXTRACTED_SUFFIX = ".zip";
  private static final String KEY_CRC = "crc";
  private static final String KEY_DEX_NUMBER = "dex.number";
  private static final String KEY_TIME_STAMP = "timestamp";
  private static final int MAX_EXTRACT_ATTEMPTS = 3;
  private static final long NO_VALUE = -1L;
  private static final String PREFS_FILE = "multidex.version";
  private static final String TAG = "MultiDex";
  private static Method sApplyMethod;

  static
  {
    try
    {
      sApplyMethod = SharedPreferences.Editor.class.getMethod("apply", new Class[0]);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      sApplyMethod = null;
    }
  }

  private static void apply(SharedPreferences.Editor paramEditor)
  {
    if (sApplyMethod != null);
    try
    {
      sApplyMethod.invoke(paramEditor, new Object[0]);
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      paramEditor.commit();
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      label20: break label20;
    }
  }

  private static void closeQuietly(Closeable paramCloseable)
  {
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException paramCloseable)
    {
      Log.w("MultiDex", "Failed to close resource", paramCloseable);
    }
  }

  private static void extract(ZipFile paramZipFile, ZipEntry paramZipEntry, File paramFile, String paramString)
    throws IOException, FileNotFoundException
  {
    InputStream localInputStream = paramZipFile.getInputStream(paramZipEntry);
    paramString = File.createTempFile(paramString, ".zip", paramFile.getParentFile());
    Log.i("MultiDex", "Extracting " + paramString.getPath());
    try
    {
      paramZipFile = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(paramString)));
      try
      {
        ZipEntry localZipEntry = new ZipEntry("classes.dex");
        localZipEntry.setTime(paramZipEntry.getTime());
        paramZipFile.putNextEntry(localZipEntry);
        paramZipEntry = new byte[16384];
        for (int i = localInputStream.read(paramZipEntry); i != -1; i = localInputStream.read(paramZipEntry))
          paramZipFile.write(paramZipEntry, 0, i);
        paramZipFile.closeEntry();
        try
        {
          paramZipFile.close();
          Log.i("MultiDex", "Renaming to " + paramFile.getPath());
          if (paramString.renameTo(paramFile))
            break label245;
          throw new IOException("Failed to rename \"" + paramString.getAbsolutePath() + "\" to \"" + paramFile.getAbsolutePath() + "\"");
        }
        finally
        {
        }
        label226: closeQuietly(localInputStream);
        paramString.delete();
        throw paramZipFile;
      }
      finally
      {
        paramZipFile.close();
      }
      label245: closeQuietly(localInputStream);
      paramString.delete();
      return;
    }
    finally
    {
      break label226;
    }
  }

  private static SharedPreferences getMultiDexPreferences(Context paramContext)
  {
    if (Build.VERSION.SDK_INT < 11);
    for (int i = 0; ; i = 4)
      return paramContext.getSharedPreferences("multidex.version", i);
  }

  private static long getTimeStamp(File paramFile)
  {
    long l2 = paramFile.lastModified();
    long l1 = l2;
    if (l2 == -1L)
      l1 = l2 - 1L;
    return l1;
  }

  private static long getZipCrc(File paramFile)
    throws IOException
  {
    long l2 = ZipUtil.getZipCrc(paramFile);
    long l1 = l2;
    if (l2 == -1L)
      l1 = l2 - 1L;
    return l1;
  }

  private static boolean isModified(Context paramContext, File paramFile, long paramLong)
  {
    paramContext = getMultiDexPreferences(paramContext);
    return (paramContext.getLong("timestamp", -1L) != getTimeStamp(paramFile)) || (paramContext.getLong("crc", -1L) != paramLong);
  }

  static List<File> load(Context paramContext, ApplicationInfo paramApplicationInfo, File paramFile, boolean paramBoolean)
    throws IOException
  {
    Log.i("MultiDex", "MultiDexExtractor.load(" + paramApplicationInfo.sourceDir + ", " + paramBoolean + ")");
    File localFile = new File(paramApplicationInfo.sourceDir);
    long l = getZipCrc(localFile);
    if ((!paramBoolean) && (!isModified(paramContext, localFile, l)));
    while (true)
    {
      try
      {
        paramApplicationInfo = loadExistingExtractions(paramContext, localFile, paramFile);
        paramContext = paramApplicationInfo;
        Log.i("MultiDex", "load found " + paramContext.size() + " secondary dex files");
        return paramContext;
      }
      catch (IOException paramApplicationInfo)
      {
        Log.w("MultiDex", "Failed to reload existing extracted secondary dex files, falling back to fresh extraction", paramApplicationInfo);
        paramApplicationInfo = performExtractions(localFile, paramFile);
        putStoredApkInfo(paramContext, getTimeStamp(localFile), l, paramApplicationInfo.size() + 1);
        paramContext = paramApplicationInfo;
        continue;
      }
      Log.i("MultiDex", "Detected that extraction must be performed.");
      paramApplicationInfo = performExtractions(localFile, paramFile);
      putStoredApkInfo(paramContext, getTimeStamp(localFile), l, paramApplicationInfo.size() + 1);
      paramContext = paramApplicationInfo;
    }
  }

  private static List<File> loadExistingExtractions(Context paramContext, File paramFile1, File paramFile2)
    throws IOException
  {
    Log.i("MultiDex", "loading existing secondary dex files");
    paramFile1 = paramFile1.getName() + ".classes";
    int j = getMultiDexPreferences(paramContext).getInt("dex.number", 1);
    paramContext = new ArrayList(j);
    int i = 2;
    while (i <= j)
    {
      File localFile = new File(paramFile2, paramFile1 + i + ".zip");
      if (localFile.isFile())
      {
        paramContext.add(localFile);
        if (!verifyZipFile(localFile))
        {
          Log.i("MultiDex", "Invalid zip file: " + localFile);
          throw new IOException("Invalid ZIP file.");
        }
      }
      else
      {
        throw new IOException("Missing extracted secondary dex file '" + localFile.getPath() + "'");
      }
      i += 1;
    }
    return paramContext;
  }

  private static void mkdirChecked(File paramFile)
    throws IOException
  {
    paramFile.mkdir();
    if (!paramFile.isDirectory())
    {
      File localFile = paramFile.getParentFile();
      if (localFile == null)
        Log.e("MultiDex", "Failed to create dir " + paramFile.getPath() + ". Parent file is null.");
      while (true)
      {
        throw new IOException("Failed to create cache directory " + paramFile.getPath());
        Log.e("MultiDex", "Failed to create dir " + paramFile.getPath() + ". parent file is a dir " + localFile.isDirectory() + ", a file " + localFile.isFile() + ", exists " + localFile.exists() + ", readable " + localFile.canRead() + ", writable " + localFile.canWrite());
      }
    }
  }

  private static List<File> performExtractions(File paramFile1, File paramFile2)
    throws IOException
  {
    String str2 = paramFile1.getName() + ".classes";
    prepareDexDir(paramFile2, str2);
    ArrayList localArrayList = new ArrayList();
    ZipFile localZipFile = new ZipFile(paramFile1);
    int i = 2;
    while (true)
    {
      File localFile;
      int m;
      String str1;
      try
      {
        paramFile1 = localZipFile.getEntry("classes" + 2 + ".dex");
        if (paramFile1 != null)
        {
          localFile = new File(paramFile2, str2 + i + ".zip");
          localArrayList.add(localFile);
          Log.i("MultiDex", "Extraction is needed for file " + localFile);
          int j = 0;
          m = 0;
          if ((j < 3) && (m == 0))
          {
            int k = j + 1;
            extract(localZipFile, paramFile1, localFile, str2);
            boolean bool = verifyZipFile(localFile);
            StringBuilder localStringBuilder = new StringBuilder().append("Extraction ");
            if (bool)
            {
              str1 = "success";
              Log.i("MultiDex", str1 + " - length " + localFile.getAbsolutePath() + ": " + localFile.length());
              m = bool;
              j = k;
              if (bool)
                continue;
              localFile.delete();
              m = bool;
              j = k;
              if (!localFile.exists())
                continue;
              Log.w("MultiDex", "Failed to delete corrupted secondary dex '" + localFile.getPath() + "'");
              m = bool;
              j = k;
              continue;
            }
          }
        }
      }
      finally
      {
      }
      try
      {
        localZipFile.close();
        throw paramFile1;
        str1 = "failed";
        continue;
        if (m == 0)
          throw new IOException("Could not create zip file " + localFile.getAbsolutePath() + " for secondary dex (" + i + ")");
        i += 1;
        paramFile1 = localZipFile.getEntry("classes" + i + ".dex");
        continue;
        try
        {
          localZipFile.close();
          return localArrayList;
        }
        catch (IOException paramFile1)
        {
          Log.w("MultiDex", "Failed to close resource", paramFile1);
          return localArrayList;
        }
      }
      catch (IOException paramFile2)
      {
        while (true)
          Log.w("MultiDex", "Failed to close resource", paramFile2);
      }
    }
  }

  private static void prepareDexDir(File paramFile, String paramString)
    throws IOException
  {
    mkdirChecked(paramFile.getParentFile());
    mkdirChecked(paramFile);
    paramString = paramFile.listFiles(new FileFilter(paramString)
    {
      public boolean accept(File paramFile)
      {
        return !paramFile.getName().startsWith(this.val$extractedFilePrefix);
      }
    });
    if (paramString == null)
    {
      Log.w("MultiDex", "Failed to list secondary dex dir content (" + paramFile.getPath() + ").");
      return;
    }
    int j = paramString.length;
    int i = 0;
    label69: if (i < j)
    {
      paramFile = paramString[i];
      Log.i("MultiDex", "Trying to delete old file " + paramFile.getPath() + " of size " + paramFile.length());
      if (paramFile.delete())
        break label163;
      Log.w("MultiDex", "Failed to delete old file " + paramFile.getPath());
    }
    while (true)
    {
      i += 1;
      break label69;
      break;
      label163: Log.i("MultiDex", "Deleted old file " + paramFile.getPath());
    }
  }

  private static void putStoredApkInfo(Context paramContext, long paramLong1, long paramLong2, int paramInt)
  {
    paramContext = getMultiDexPreferences(paramContext).edit();
    paramContext.putLong("timestamp", paramLong1);
    paramContext.putLong("crc", paramLong2);
    paramContext.putInt("dex.number", paramInt);
    apply(paramContext);
  }

  static boolean verifyZipFile(File paramFile)
  {
    try
    {
      ZipFile localZipFile = new ZipFile(paramFile);
      try
      {
        localZipFile.close();
        return true;
      }
      catch (IOException localIOException1)
      {
        Log.w("MultiDex", "Failed to close zip file: " + paramFile.getAbsolutePath());
      }
      return false;
    }
    catch (ZipException localZipException)
    {
      while (true)
        Log.w("MultiDex", "File " + paramFile.getAbsolutePath() + " is not a valid zip file.", localZipException);
    }
    catch (IOException localIOException2)
    {
      while (true)
        Log.w("MultiDex", "Got an IOException trying to open zip file: " + paramFile.getAbsolutePath(), localIOException2);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.multidex.MultiDexExtractor
 * JD-Core Version:    0.6.0
 */