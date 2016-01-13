package com.dianping.widget.view;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.dianping.app.DPActivity;
import com.dianping.util.ViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class ViewStatistics
{
  private String getGAHashKey(DPActivity paramDPActivity, View paramView, int paramInt, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("_");
    String str = paramString;
    if (paramString == null)
      str = paramDPActivity.getPageName();
    return str + "_" + GAHelper.instance().getElementIdByView(paramView) + "_" + paramInt;
  }

  public static ViewStatistics instance()
  {
    return ViewStatisticsInner.instance;
  }

  private boolean isViewShowedBefore(String paramString, DPActivity paramDPActivity)
  {
    if ((paramDPActivity.gaViewMarked.size() == 0) || (!GAHelper.requestId.equals(paramDPActivity.gaViewMarked.get(0))))
    {
      paramDPActivity.gaViewMarked.clear();
      paramDPActivity.gaViewMarked.add(0, GAHelper.requestId);
      paramDPActivity.gaViewMarked.add(paramString);
      return false;
    }
    if (paramDPActivity.gaViewMarked.contains(paramString))
      return true;
    paramDPActivity.gaViewMarked.add(paramString);
    return false;
  }

  private void showGAViewInternal(DPActivity paramDPActivity, View paramView, String paramString)
  {
    if ((paramString.contains(paramDPActivity.getPageName())) && (isViewOnScreen(paramDPActivity, paramView)) && (!isViewShowedBefore(paramString, paramDPActivity)))
      GAHelper.instance().statisticsEvent(paramView, Integer.valueOf(paramString.substring(paramString.lastIndexOf("_") + 1)).intValue(), "view");
  }

  public void addGAView(DPActivity paramDPActivity, View paramView, int paramInt, String paramString, boolean paramBoolean)
  {
    if (paramView == null)
      return;
    if (paramInt == -1)
      paramInt = 2147483647;
    while (true)
    {
      paramView.postDelayed(new Runnable(paramDPActivity, paramView, paramInt, paramString, paramBoolean)
      {
        public void run()
        {
          if ((!this.val$dpActivity.isResumed) || (this.val$dpActivity.gAViews == null))
            return;
          String str = ViewStatistics.this.getGAHashKey(this.val$dpActivity, this.val$view, this.val$indexTmp, this.val$pagename);
          if (this.val$show)
            ViewStatistics.this.showGAViewInternal(this.val$dpActivity, this.val$view, str);
          Iterator localIterator = this.val$dpActivity.gAViews.entrySet().iterator();
          while (localIterator.hasNext())
          {
            if (((Map.Entry)localIterator.next()).getValue() != this.val$view)
              continue;
            localIterator.remove();
          }
          this.val$dpActivity.gAViews.put(str, this.val$view);
        }
      }
      , 500L);
      return;
    }
  }

  public boolean getUtmAndMarketingSource(GAUserInfo paramGAUserInfo, Uri paramUri)
  {
    if (paramGAUserInfo == null)
      return false;
    int j = 0;
    if ((paramUri != null) && ("dianping".equals(paramUri.getScheme())))
    {
      Object localObject1 = paramUri.getQueryParameter("utm_");
      Object localObject2;
      int i;
      if (TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject2 = paramUri.getQueryParameter("_utm");
        i = j;
        localObject1 = localObject2;
        if (TextUtils.isEmpty((CharSequence)localObject2))
        {
          localObject1 = paramUri.getQueryParameter("utm");
          i = j;
        }
        localObject2 = paramUri.getQueryParameter("marketingsource_");
        if (!TextUtils.isEmpty((CharSequence)localObject2))
          break label145;
        String str = paramUri.getQueryParameter("_marketingsource");
        j = i;
        localObject2 = str;
        if (TextUtils.isEmpty(str))
          localObject2 = paramUri.getQueryParameter("marketingsource");
      }
      label145: for (j = i; ; j = 1)
      {
        paramGAUserInfo.utm = ((String)localObject1);
        paramGAUserInfo.marketing_source = ((String)localObject2);
        return j;
        i = 1;
        break;
      }
    }
    paramGAUserInfo.utm = null;
    paramGAUserInfo.marketing_source = null;
    return false;
  }

  public boolean isViewOnScreen(DPActivity paramDPActivity, View paramView)
  {
    int i = 1;
    if (paramView == null);
    int[] arrayOfInt;
    do
    {
      return false;
      arrayOfInt = new int[2];
      arrayOfInt[1] = 0;
      paramView.getLocationOnScreen(arrayOfInt);
    }
    while (arrayOfInt[1] <= 0);
    if (arrayOfInt[1] < ViewUtils.getScreenHeightPixels(paramDPActivity));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public void onNewGAPager(GAUserInfo paramGAUserInfo, DPActivity paramDPActivity, boolean paramBoolean)
  {
    GAUserInfo localGAUserInfo1 = paramGAUserInfo;
    GAUserInfo localGAUserInfo2 = localGAUserInfo1;
    if (localGAUserInfo1 == null)
      localGAUserInfo2 = new GAUserInfo();
    GAHelper.instance().setGAPageName(paramDPActivity.getPageName());
    GAHelper.instance().setRequestId(paramDPActivity, UUID.randomUUID().toString(), localGAUserInfo2, paramBoolean);
    paramGAUserInfo.utm = null;
    paramGAUserInfo.marketing_source = null;
  }

  public void removeGAView(DPActivity paramDPActivity, String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return;
      paramString = "_" + paramString + "_";
      Iterator localIterator = paramDPActivity.gAViews.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if ((localEntry.getValue() != null) && (!((String)localEntry.getKey()).contains(paramString)))
          continue;
        localIterator.remove();
      }
      paramDPActivity = paramDPActivity.gaViewMarked.iterator();
      while (paramDPActivity.hasNext())
      {
        if (!((String)paramDPActivity.next()).contains(paramString))
          continue;
        paramDPActivity.remove();
      }
    }
  }

  public void showGAView(DPActivity paramDPActivity, String paramString)
  {
    Iterator localIterator = paramDPActivity.gAViews.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (localEntry.getValue() != null)
      {
        if (!TextUtils.isEmpty(paramString))
        {
          String str = "_" + paramString + "_";
          if (!((String)localEntry.getKey()).contains(str))
            continue;
          showGAViewInternal(paramDPActivity, (View)localEntry.getValue(), (String)localEntry.getKey());
          continue;
        }
        showGAViewInternal(paramDPActivity, (View)localEntry.getValue(), (String)localEntry.getKey());
        continue;
      }
      localIterator.remove();
    }
  }

  private static class ViewStatisticsInner
  {
    static final ViewStatistics instance = new ViewStatistics(null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.view.ViewStatistics
 * JD-Core Version:    0.6.0
 */