package com.dianping.locationservice.impl286.scan;

import android.content.Context;
import android.text.TextUtils;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class BaseScanner<T>
  implements Callable<List<T>>, Scanner
{
  protected final Context mContext;
  protected long mElapse;
  protected String mErrorMsg = "";
  protected final List<T> mResultList = new ArrayList();

  public BaseScanner(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private void calculateElapse()
  {
    this.mElapse = (System.currentTimeMillis() - this.mElapse);
    Log.d(getClass().getSimpleName() + " elapse: " + this.mElapse);
  }

  private void startScan()
  {
    Log.d("");
    this.mElapse = System.currentTimeMillis();
    onStartScan();
  }

  public List<T> call()
    throws Exception
  {
    scan();
    return this.mResultList;
  }

  protected abstract void onStartScan();

  public void scan()
  {
    this.mResultList.clear();
    this.mErrorMsg = "";
    startScan();
    calculateElapse();
    if (!TextUtils.isEmpty(this.mErrorMsg))
      Log.w(this.mErrorMsg);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.scan.BaseScanner
 * JD-Core Version:    0.6.0
 */