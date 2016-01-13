package android.support.v4.os;

import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;
import java.io.File;

public class EnvironmentCompat
{
  public static final String MEDIA_UNKNOWN = "unknown";
  private static final String TAG = "EnvironmentCompat";

  public static String getStorageState(File paramFile)
  {
    if (Build.VERSION.SDK_INT >= 19)
      return EnvironmentCompatKitKat.getStorageState(paramFile);
    try
    {
      if (paramFile.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath()))
      {
        paramFile = Environment.getExternalStorageState();
        return paramFile;
      }
    }
    catch (java.io.IOException paramFile)
    {
      Log.w("EnvironmentCompat", "Failed to resolve canonical path: " + paramFile);
    }
    return "unknown";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.os.EnvironmentCompat
 * JD-Core Version:    0.6.0
 */