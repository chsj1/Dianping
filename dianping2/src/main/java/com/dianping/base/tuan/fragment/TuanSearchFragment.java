package com.dianping.base.tuan.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class TuanSearchFragment extends AbstractSearchFragment
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

  public static TuanSearchFragment createAndAdd(FragmentActivity paramFragmentActivity, DPObject paramDPObject, String paramString, boolean paramBoolean)
  {
    return createAndAdd(paramFragmentActivity, paramDPObject, paramString, paramBoolean, null);
  }

  public static TuanSearchFragment createAndAdd(FragmentActivity paramFragmentActivity, DPObject paramDPObject, String paramString1, boolean paramBoolean, String paramString2)
  {
    TuanSearchFragment localTuanSearchFragment = new TuanSearchFragment();
    localTuanSearchFragment.channel = paramString1;
    localTuanSearchFragment.categoryForPopularSuggest = paramDPObject;
    localTuanSearchFragment.searchHint = "输入商户名、地点";
    localTuanSearchFragment.addToStack = paramBoolean;
    localTuanSearchFragment.pageName = paramString2;
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localTuanSearchFragment);
    if (paramBoolean)
      paramFragmentActivity.addToBackStack(null);
    localTuanSearchFragment.mSearchMode = 2;
    paramFragmentActivity.commitAllowingStateLoss();
    return localTuanSearchFragment;
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
    Log.v("createRequest", "keyword = " + paramString);
    if (location() != null)
    {
      this.latitude = location().latitude();
      this.longitude = location().longitude();
      this.accuracy = location().accuracy();
    }
    StringBuilder localStringBuilder;
    if (TextUtils.isEmpty(paramString))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("popularsearchgn.bin");
      localStringBuilder.append("?cityid=").append(city().id());
      localStringBuilder.append("&dpid=").append(DeviceUtils.dpid());
      if (this.categoryForPopularSuggest != null)
      {
        localStringBuilder.append("&categoryid=").append(this.categoryForPopularSuggest.getInt("ID"));
        localStringBuilder.append("&parentcategoryid=").append(this.categoryForPopularSuggest.getInt("ParentID"));
        localStringBuilder.append("&categoryenname=").append(this.categoryForPopularSuggest.getString("EnName"));
        localStringBuilder.append("&parentcategoryenname=").append(this.categoryForPopularSuggest.getString("ParentEnName"));
      }
      if (getHistoryWord(0) == null)
        paramString = "";
    }
    while (true)
    {
      if (!TextUtils.isEmpty(paramString));
      try
      {
        localStringBuilder.append("&keyword=").append(URLEncoder.encode(paramString, "utf-8"));
        label241: if (this.latitude != 0.0D)
          localStringBuilder.append("&lat=" + FMT.format(this.latitude));
        if (this.longitude != 0.0D)
          localStringBuilder.append("&lng=" + FMT.format(this.longitude));
        if (this.accuracy != 0)
          localStringBuilder.append("&accuracy=" + this.accuracy);
        if (getAccount() == null);
        while (true)
        {
          return mapiGet(this, localStringBuilder.toString(), CacheType.DISABLED);
          paramString = getHistoryWord(0).getString("Keyword");
          break;
          if (TextUtils.isEmpty(accountService().token()))
            continue;
          localStringBuilder.append("&token=").append(accountService().token());
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("http://app.t.dianping.com/");
        localStringBuilder.append("searchsuggestgn.bin");
        localStringBuilder.append("?cityid=").append(city().id());
        localStringBuilder.append("&keyword=").append(URLEncoder.encode(paramString));
        if (location() != null)
        {
          localStringBuilder.append("&lat=").append(location().latitude());
          localStringBuilder.append("&lng=").append(location().longitude());
        }
        localStringBuilder.append("&dpid=").append(DeviceUtils.dpid());
        if (!TextUtils.isEmpty(this.channel))
          localStringBuilder.append("&channel=").append(this.channel);
        return mapiGet(this, localStringBuilder.toString(), CacheType.DISABLED);
      }
      catch (java.lang.Exception paramString)
      {
        break label241;
      }
    }
  }

  public String getFileName()
  {
    return "find_tuan_search_fragment,find_main_search_fragment";
  }

  public void onClick(View paramView)
  {
    super.onClick(paramView);
    if ((paramView.getId() == R.id.back) && (!this.addToStack) && (getActivity() != null))
      getActivity().finish();
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
 * Qualified Name:     com.dianping.base.tuan.fragment.TuanSearchFragment
 * JD-Core Version:    0.6.0
 */