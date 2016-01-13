package com.dianping.membercard.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import com.dianping.accountservice.AccountService;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AvailableCardSearchFragment extends AbstractSearchFragment
{
  public static AvailableCardSearchFragment newInstance(FragmentActivity paramFragmentActivity)
  {
    AvailableCardSearchFragment localAvailableCardSearchFragment = new AvailableCardSearchFragment();
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localAvailableCardSearchFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commit();
    return localAvailableCardSearchFragment;
  }

  public MApiRequest createRequest(String paramString)
  {
    statisticsEvent("availablecard5", "availablecard5_keyword_success", paramString, 0);
    StringBuilder localStringBuilder = new StringBuilder("http://mc.api.dianping.com/suggestcardlist.mc?");
    localStringBuilder.append("cityid=").append(cityId());
    if (!TextUtils.isEmpty(paramString));
    try
    {
      localStringBuilder.append("&kw=" + URLEncoder.encode(paramString, "utf-8"));
      if (accountService().token() != null)
        localStringBuilder.append("&token=").append(accountService().token());
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    }
    catch (UnsupportedEncodingException paramString)
    {
      while (true)
        paramString.printStackTrace();
    }
  }

  public String getFileName()
  {
    return "available_card_fragment";
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    super.onItemClick(paramAdapterView, paramView, paramInt, paramLong);
    statisticsEvent("availablecard5", "availablecard5_keyword_suggest", this.queryid, paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.AvailableCardSearchFragment
 * JD-Core Version:    0.6.0
 */