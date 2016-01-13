package com.dianping.hotel.mainsearch.fragment;

import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HotelSearchFragment extends AbstractHotelSearchFragment
{
  private static final int HEADER_VIEW_HOT_SEARCH = 0;
  private static final int HEADER_VIEW_HOT_SEARCH_KEYWORDS = 1;
  private static final int HEADER_VIEW_SEARCH_HISTORY = 2;
  private static final int SEARCH_SUGGEST_TYPE_ADVANCE = 1;
  private static final int SEARCH_SUGGEST_TYPE_HOTWORD = 2;
  private static final int SEARCH_SUGGEST_TYPE_NORMAL = 0;
  private static final int TEXT_MARGIN = 8;
  protected static final String URL = "http://m.api.dianping.com/";
  private List<View> mHeaderViews = new ArrayList();
  LinearLayout.LayoutParams mTextViewlayoutParams;

  private View getHotWordView(ArrayList<DPObject> paramArrayList)
  {
    LinearLayout localLinearLayout = new LinearLayout(getActivity());
    localLinearLayout.setOrientation(0);
    int i = ViewUtils.dip2px(getActivity(), 15.0F);
    localLinearLayout.setPadding(i, 0, i, 0);
    i = calcHotWordsCount(paramArrayList, 14, 10, localLinearLayout);
    paramArrayList = paramArrayList.iterator();
    while (true)
    {
      if (paramArrayList.hasNext())
      {
        localObject = (DPObject)paramArrayList.next();
        if (i != 0);
      }
      else
      {
        localLinearLayout.setTag(Integer.valueOf(1));
        this.mHeaderViews.add(localLinearLayout);
        return localLinearLayout;
      }
      Object localObject = getTextView((DPObject)localObject, 2);
      ((TextView)localObject).setGravity(17);
      ((TextView)localObject).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      ((TextView)localObject).setBackgroundDrawable(getResources().getDrawable(R.drawable.background_round_textview));
      LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)((TextView)localObject).getLayoutParams();
      localLayoutParams.height = ViewUtils.dip2px(getActivity(), 38.0F);
      if (i > 1)
        localLayoutParams.rightMargin = ViewUtils.dip2px(getActivity(), 10.0F);
      localLinearLayout.addView((View)localObject);
      i -= 1;
    }
  }

  private TextView getTextView(DPObject paramDPObject, int paramInt)
  {
    TextView localTextView = new TextView(getActivity());
    if (paramInt == 1)
    {
      localTextView.setLayoutParams(this.mTextViewlayoutParams);
      localTextView.setBackgroundResource(R.drawable.simple_item);
    }
    while (true)
    {
      localTextView.setText(paramDPObject.getString("Keyword"));
      localTextView.setTextColor(getResources().getColor(R.color.clickable_deep_black));
      localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      localTextView.setOnClickListener(new View.OnClickListener(paramInt, paramDPObject)
      {
        public void onClick(View paramView)
        {
          if ((HotelSearchFragment.this.getActivity() instanceof DPActivity))
          {
            if (1 != this.val$suggestType)
              break label60;
            ((DPActivity)HotelSearchFragment.this.getActivity()).statisticsEvent("index5", "index_search_adsuggest", this.val$suggest.getString("Keyword"), 0);
          }
          while (true)
          {
            HotelSearchFragment.this.search(this.val$suggest);
            return;
            label60: if (2 != this.val$suggestType)
              continue;
            ((DPActivity)HotelSearchFragment.this.getActivity()).statisticsEvent("index5", "index_search_hotsuggest", "", 0);
          }
        }
      });
      return localTextView;
      localTextView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0F));
      localTextView.setBackgroundResource(R.drawable.background_round_textview);
    }
  }

  public static HotelSearchFragment newInstance(FragmentActivity paramFragmentActivity)
  {
    HotelSearchFragment localHotelSearchFragment = new HotelSearchFragment();
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localHotelSearchFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commitAllowingStateLoss();
    return localHotelSearchFragment;
  }

  private void resetListView()
  {
    Iterator localIterator = this.mHeaderViews.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      this.listView.removeHeaderView(localView);
    }
    this.listView.setAdapter(null);
  }

  public int calcHotWordsCount(ArrayList<DPObject> paramArrayList, int paramInt1, int paramInt2, LinearLayout paramLinearLayout)
  {
    if (!isAdded())
    {
      if (paramArrayList.size() > 3)
        return 3;
      return paramArrayList.size();
    }
    int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
    int j = i;
    if (paramLinearLayout.getLayoutParams() != null)
      j = i - ((LinearLayout.LayoutParams)paramLinearLayout.getLayoutParams()).leftMargin - ((LinearLayout.LayoutParams)paramLinearLayout.getLayoutParams()).rightMargin;
    int i1 = paramLinearLayout.getPaddingLeft();
    int i2 = paramLinearLayout.getPaddingRight();
    int k = 0;
    paramLinearLayout = new int[3];
    i = 0;
    while ((i < paramArrayList.size()) && (i < 3))
    {
      int n = getTextWidth(((DPObject)paramArrayList.get(i)).getString("Keyword"), paramInt1);
      int m = k;
      if (n > k)
        m = n;
      paramLinearLayout[i] = m;
      i += 1;
      k = m;
    }
    paramInt1 = ViewUtils.dip2px(getActivity(), paramInt2);
    while (true)
    {
      if ((i <= 1) || (paramLinearLayout[(i - 1)] * i + (i - 1) * paramInt1 < j - i1 - i2))
        return i;
      i -= 1;
    }
  }

  public MApiRequest createRequest(String paramString)
  {
    CacheType localCacheType = CacheType.HOURLY;
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
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), localCacheType);
    }
    catch (UnsupportedEncodingException paramString)
    {
      while (true)
        paramString.printStackTrace();
    }
  }

  public String getFileName()
  {
    return "find_main_search_fragment";
  }

  public View getHeaderView(String paramString, int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(getActivity());
    TextView localTextView = new TextView(getActivity());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2, 1.0F);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getActivity(), 15.0F);
    localLayoutParams.height = ViewUtils.dip2px(getActivity(), 50.0F);
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    localTextView.setLayoutParams(localLayoutParams);
    localTextView.setText(paramString);
    localTextView.setTextColor(getResources().getColor(R.color.light_gray));
    localTextView.setGravity(19);
    localLinearLayout.addView(localTextView);
    localLinearLayout.setTag(Integer.valueOf(paramInt));
    this.mHeaderViews.add(localLinearLayout);
    return localLinearLayout;
  }

  public int getTextWidth(String paramString, int paramInt)
  {
    Paint localPaint = new Paint();
    localPaint.setTextSize(ViewUtils.sp2px(getActivity(), paramInt));
    return (int)localPaint.measureText(paramString);
  }

  protected void initButtonLayoutParams()
  {
    this.mTextViewlayoutParams = new LinearLayout.LayoutParams(-1, -2, 1.0F);
    this.mTextViewlayoutParams.setMargins(8, 8, 8, 8);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    sendHotWordRequest();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Iterator localIterator = this.mHeaderViews.iterator();
    while (localIterator.hasNext())
      if (paramView == (View)localIterator.next())
        return;
    super.onItemClick(paramAdapterView, paramView, paramInt, paramLong);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
      if (((paramMApiResponse.result() instanceof DPObject)) && (getActivity() != null))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        paramMApiResponse = new ArrayList();
        this.queryid = paramMApiRequest.getString("QueryID");
        if (paramMApiRequest.getArray("List") != null)
          paramMApiResponse.addAll(Arrays.asList(paramMApiRequest.getArray("List")));
        resetListView();
        int i = 0;
        if (paramMApiResponse.size() != 0)
          i = ((DPObject)paramMApiResponse.get(0)).getInt("SuggestType");
        this.listView.setHeaderDividersEnabled(false);
        switch (i)
        {
        default:
          this.mHeaderViews.clear();
          this.listView.setAdapter(getSuggestListAdapter(paramMApiResponse));
          this.mSearchMode = 2;
        case 2:
        case 1:
        }
      }
    while (true)
    {
      this.request = null;
      return;
      this.listView.addHeaderView(getHeaderView("热门搜索", 0));
      this.listView.addHeaderView(getHotWordView(paramMApiResponse));
      if (this.searchHistoryList.size() > 0)
        this.listView.addHeaderView(getHeaderView("搜索历史", 2));
      this.listView.setAdapter(this.historyListAdapter);
      this.mSearchMode = 1;
      continue;
      this.listView.setAdapter(getSuggestListAdapter(paramMApiResponse));
      this.mSearchMode = 2;
    }
  }

  public void removeSearchListHeaderView()
  {
    Iterator localIterator = this.mHeaderViews.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      if ((localView.getTag() == null) || (!(localView.getTag() instanceof Integer)) || (Integer.parseInt(localView.getTag().toString()) != 2))
        continue;
      this.listView.removeHeaderView(localView);
    }
  }

  protected void sendHotWordRequest()
  {
    Message localMessage = this.searchSuggestDelayHandler.obtainMessage(SEARCH_SUGGEST_MESSAGE, "");
    this.searchSuggestDelayHandler.sendMessage(localMessage);
    initButtonLayoutParams();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.mainsearch.fragment.HotelSearchFragment
 * JD-Core Version:    0.6.0
 */