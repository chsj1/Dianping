package com.dianping.tuan.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import java.text.DecimalFormat;

public class MallSearchFragment extends AbstractSearchFragment
{
  protected static final DecimalFormat FMT = new DecimalFormat("#.00000");
  protected int accuracy;
  protected boolean addToStack;
  protected DPObject categoryForPopularSuggest;
  protected String channel;
  protected double latitude;
  protected double longitude;
  protected String pageName;

  protected static boolean compareGaString(Context paramContext, DPObject paramDPObject, String paramString)
  {
    if ((paramDPObject == null) || (paramContext == null));
    do
    {
      do
      {
        do
        {
          do
            return false;
          while (TextUtils.isEmpty(paramString));
          paramContext = paramContext.getResources();
        }
        while (paramContext == null);
        paramContext = paramContext.getString(R.string.search_keyword_ga_suffix);
      }
      while (TextUtils.isEmpty(paramContext));
      paramContext = paramDPObject.getString(paramContext);
    }
    while ((TextUtils.isEmpty(paramContext)) || (!paramContext.equals(paramString)));
    return true;
  }

  public static MallSearchFragment createAndAdd(FragmentActivity paramFragmentActivity, DPObject paramDPObject, String paramString, boolean paramBoolean)
  {
    return createAndAdd(paramFragmentActivity, paramDPObject, paramString, paramBoolean, null);
  }

  public static MallSearchFragment createAndAdd(FragmentActivity paramFragmentActivity, DPObject paramDPObject, String paramString1, boolean paramBoolean, String paramString2)
  {
    MallSearchFragment localMallSearchFragment = new MallSearchFragment();
    localMallSearchFragment.channel = paramString1;
    localMallSearchFragment.categoryForPopularSuggest = paramDPObject;
    localMallSearchFragment.searchHint = "输入商户名、地点";
    localMallSearchFragment.addToStack = paramBoolean;
    localMallSearchFragment.pageName = paramString2;
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localMallSearchFragment);
    if (paramBoolean)
      paramFragmentActivity.addToBackStack(null);
    localMallSearchFragment.mSearchMode = 2;
    paramFragmentActivity.commitAllowingStateLoss();
    return localMallSearchFragment;
  }

  public static boolean isHistory(Context paramContext, DPObject paramDPObject)
  {
    return compareGaString(paramContext, paramDPObject, "_history");
  }

  public static boolean isHotword(Context paramContext, DPObject paramDPObject)
  {
    return compareGaString(paramContext, paramDPObject, "_hot");
  }

  public static boolean isSuggest(Context paramContext, DPObject paramDPObject)
  {
    return compareGaString(paramContext, paramDPObject, "_suggest");
  }

  public MApiRequest createRequest(String paramString)
  {
    return null;
  }

  public String getFileName()
  {
    return "find_mall_search_fragment";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  protected void setGAPageName()
  {
    if (TextUtils.isEmpty(this.pageName))
    {
      super.setGAPageName();
      return;
    }
    GAHelper.instance().setGAPageName(this.pageName);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.MallSearchFragment
 * JD-Core Version:    0.6.0
 */