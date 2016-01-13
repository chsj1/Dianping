package cloudwns.b;

import android.util.Log;
import com.tencent.base.Global;

public class i
{
  private static final i a = new i();

  public static void a(String paramString)
  {
    a("wnsPerf", paramString, null);
  }

  public static void a(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (Global.isDebug())
      Log.w(paramString1, paramString2, paramThrowable);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.b.i
 * JD-Core Version:    0.6.0
 */