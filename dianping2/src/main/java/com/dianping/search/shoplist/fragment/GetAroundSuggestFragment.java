package com.dianping.search.shoplist.fragment;

import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.archive.DPObject;>;
import java.util.Arrays;

public class GetAroundSuggestFragment extends AbstractSearchFragment
{
  protected static final int ADVANCED_SUGGEST = 1;
  protected static final int HOT_WORD = 2;
  protected static final int NORMAL_SUGGEST = 0;
  private static final String URL = "http://m.api.dianping.com/";
  protected String endPoint = "gettingaroundsuggest.bin";
  protected int mCategoryId = 0;

  public static GetAroundSuggestFragment newInstance(FragmentActivity paramFragmentActivity)
  {
    return newInstance(paramFragmentActivity, 0);
  }

  public static GetAroundSuggestFragment newInstance(FragmentActivity paramFragmentActivity, int paramInt)
  {
    GetAroundSuggestFragment localGetAroundSuggestFragment = new GetAroundSuggestFragment();
    localGetAroundSuggestFragment.searchHint = "搜索景点、酒店、目的地";
    localGetAroundSuggestFragment.mCategoryId = paramInt;
    localGetAroundSuggestFragment.mSearchMode = 2;
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localGetAroundSuggestFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commitAllowingStateLoss();
    return localGetAroundSuggestFragment;
  }

  public Uri buildUri(DPObject paramDPObject)
  {
    Uri.Builder localBuilder = Uri.parse("dianping://shoplist").buildUpon();
    localBuilder.appendQueryParameter("tab", String.valueOf(0));
    paramDPObject = paramDPObject.getString("Keyword");
    if (!android.text.TextUtils.isEmpty(paramDPObject))
      localBuilder.appendQueryParameter("keyword", paramDPObject);
    localBuilder.appendQueryParameter("target", "gettingaround");
    return Uri.parse(localBuilder.toString());
  }

  public MApiRequest createRequest(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/" + this.endPoint + "?");
    localStringBuilder.append("cityid=").append(cityId());
    if (!android.text.TextUtils.isEmpty(paramString));
    try
    {
      localStringBuilder.append("&keyword=").append(URLEncoder.encode(paramString, "utf-8"));
      if (this.mCategoryId != 0)
        localStringBuilder.append("&categoryid=").append(String.valueOf(this.mCategoryId));
      DPObject localDPObject = locationService().location();
      if ((localDPObject != null) && (localDPObject.getDouble("Lat") != 0.0D) && (localDPObject.getDouble("Lng") != 0.0D))
      {
        localStringBuilder.append("&").append("mylat=").append(Location.FMT.format(localDPObject.getDouble("Lat")));
        localStringBuilder.append("&").append("mylng=").append(Location.FMT.format(localDPObject.getDouble("Lng")));
      }
      if ((localDPObject != null) && (localDPObject.getInt("Accuracy") > 0))
        localStringBuilder.append("&").append("myacc=").append(localDPObject.getInt("Accuracy"));
      if (android.text.TextUtils.isEmpty(paramString))
        return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.CRITICAL);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
        localUnsupportedEncodingException.printStackTrace();
    }
    return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
  }

  protected View createSuggestionItem(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (!android.text.TextUtils.isEmpty(paramDPObject.getString("Keyword")))
    {
      paramView = (NovaLinearLayout)super.createSuggestionItem(paramDPObject, paramInt, paramView, paramViewGroup);
      if (paramDPObject.getInt("SuggestType") == 1)
      {
        ((TextView)paramView.findViewById(16908308)).setTextColor(getResources().getColor(R.color.advance_suggest_blue));
        paramView.setGAString("suggest_otherplace", paramDPObject.getString("Keyword"));
        ((NovaActivity)getActivity()).addGAView(paramView, paramInt);
        return paramView;
      }
      ((TextView)paramView.findViewById(16908308)).setTextColor(getResources().getColor(R.color.deep_gray));
      paramView.setGAString("suggest", paramDPObject.getString("Keyword"));
      return paramView;
    }
    if ((paramView instanceof NovaRelativeLayout));
    for (paramView = (NovaRelativeLayout)paramView; ; paramView = (NovaRelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.suggest_list_direct_zone_item, paramViewGroup, false))
    {
      ((NetworkThumbView)paramView.findViewById(R.id.thumb)).setImage(paramDPObject.getString("PicUrl"));
      ((TextView)paramView.findViewById(R.id.title)).setText(paramDPObject.getString("Title"));
      ((TextView)paramView.findViewById(R.id.abstract_text)).setText(com.dianping.util.TextUtils.highLightShow(getActivity(), paramDPObject.getString("Abstract"), R.color.tuan_common_orange));
      ((TextView)paramView.findViewById(R.id.sub_title)).setText(paramDPObject.getString("Subtitle"));
      ((TextView)paramView.findViewById(R.id.high_light)).setText(com.dianping.util.TextUtils.highLightShow(getActivity(), paramDPObject.getString("ClickTips"), R.color.tuan_common_orange));
      paramView.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          GetAroundSuggestFragment.this.startActivity(this.val$suggestion.getString("ClickUrl"));
          GetAroundSuggestFragment.this.getFragmentManager().popBackStackImmediate();
        }
      });
      paramView.setGAString("suggest_direct");
      paramView.gaUserInfo.keyword = this.searchEditText.getText().toString().trim();
      paramView.gaUserInfo.index = Integer.valueOf(paramInt);
      paramView.gaUserInfo.query_id = this.queryid;
      GAHelper.instance().contextStatisticsEvent(getActivity(), "suggest_direct", paramView.gaUserInfo, "view");
      return paramView;
    }
  }

  public String getFileName()
  {
    return "find_around_search_fragment";
  }

  protected ArrayList<DPObject> getSuggestListFromResponse(Object paramObject)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramObject instanceof DPObject))
    {
      Object localObject = ((DPObject)paramObject).getObject("DirectZoneResult");
      if (localObject != null)
      {
        localObject = ((DPObject)localObject).getArray("List");
        if (localObject != null)
          localArrayList.addAll(Arrays.asList(localObject));
      }
    }
    localArrayList.addAll(super.getSuggestListFromResponse(paramObject));
    return (ArrayList<DPObject>)localArrayList;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.GetAroundSuggestFragment
 * JD-Core Version:    0.6.0
 */