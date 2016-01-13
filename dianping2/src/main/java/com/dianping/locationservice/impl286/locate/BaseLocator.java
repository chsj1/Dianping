package com.dianping.locationservice.impl286.locate;

import android.content.Context;
import android.text.TextUtils;
import com.dianping.locationservice.impl286.model.CoordModel;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseLocator
  implements Locator
{
  protected final Context mContext;
  protected long mElapse;
  protected String mErrorMsg = "";
  private LocateListener mListener;
  protected final List<CoordModel> mResultList = new ArrayList();

  public BaseLocator(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private void calculateElapse()
  {
    this.mElapse = (System.currentTimeMillis() - this.mElapse);
    Log.d(getClass().getSimpleName() + " elapse: " + this.mElapse);
  }

  private boolean hasListener()
  {
    return this.mListener != null;
  }

  private void startLocate()
  {
    Log.d("");
    this.mElapse = System.currentTimeMillis();
    onStartLocate();
  }

  public void locate(LocateListener paramLocateListener)
  {
    this.mResultList.clear();
    this.mErrorMsg = "";
    this.mListener = paramLocateListener;
    startLocate();
  }

  protected void notifyLocateFinish()
  {
    calculateElapse();
    if (!TextUtils.isEmpty(this.mErrorMsg))
      Log.w(this.mErrorMsg);
    if (hasListener())
      this.mListener.onLocateFinish(this.mResultList);
  }

  protected abstract void onStartLocate();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.BaseLocator
 * JD-Core Version:    0.6.0
 */