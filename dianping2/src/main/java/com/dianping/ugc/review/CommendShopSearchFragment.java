package com.dianping.ugc.review;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class CommendShopSearchFragment extends AbstractSearchFragment
{
  private static final String URL = "http://m.api.dianping.com/";

  public static CommendShopSearchFragment newInstance(FragmentActivity paramFragmentActivity)
  {
    CommendShopSearchFragment localCommendShopSearchFragment = new CommendShopSearchFragment();
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localCommendShopSearchFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commit();
    return localCommendShopSearchFragment;
  }

  public MApiRequest createRequest(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/advancedsuggest.bin?");
    localStringBuilder.append("cityid=").append(cityId());
    if (!TextUtils.isEmpty(paramString));
    try
    {
      localStringBuilder.append("&keyword=" + URLEncoder.encode(paramString, "utf-8"));
      paramString = locationService().location();
      if ((paramString != null) && (paramString.getDouble("Lat") != 0.0D) && (paramString.getDouble("Lng") != 0.0D))
      {
        localStringBuilder.append("&").append("mylat=").append(Location.FMT.format(paramString.getDouble("Lat")));
        localStringBuilder.append("&").append("mylng=").append(Location.FMT.format(paramString.getDouble("Lng")));
      }
      if ((paramString != null) && (paramString.getInt("Accuracy") > 0))
        localStringBuilder.append("&").append("myacc=").append(paramString.getInt("Accuracy"));
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
    return "com_dianping_review_CommendShopSearchFragment";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.CommendShopSearchFragment
 * JD-Core Version:    0.6.0
 */