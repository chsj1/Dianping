package com.dianping.main.city;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.base.basic.HistorySearchSuggestionHelper;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.model.City;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class CitySearchFragment extends AbstractSearchFragment
{
  private static int searchType = 0;

  public static CitySearchFragment newInstance(FragmentActivity paramFragmentActivity, int paramInt)
  {
    CitySearchFragment localCitySearchFragment = new CitySearchFragment();
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localCitySearchFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commitAllowingStateLoss();
    searchType = paramInt;
    return localCitySearchFragment;
  }

  public MApiRequest createRequest(String paramString)
  {
    return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/common/searchcity.bin").buildUpon().appendQueryParameter("suggest", paramString).build().toString(), CacheType.DISABLED);
  }

  protected View createSuggestionItem(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView instanceof NovaLinearLayout))
    {
      paramView = (NovaLinearLayout)paramView;
      paramViewGroup = paramDPObject.getObject("City");
      if (paramViewGroup != null)
        ((TextView)paramView.findViewById(R.id.area)).setText(paramViewGroup.getString("Name"));
      paramDPObject = paramDPObject.getString("Country");
      if (TextUtils.isEmpty(paramDPObject))
        break label134;
      ((TextView)paramView.findViewById(R.id.country)).setText("(" + paramDPObject + ")");
    }
    while (true)
    {
      paramView.setGAString("select_city_search", paramViewGroup.getString("Name"));
      return paramView;
      paramView = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.city_search_list_item, paramViewGroup, false);
      break;
      label134: ((TextView)paramView.findViewById(R.id.country)).setText("");
    }
  }

  public String getFileName()
  {
    return "city_search_fragment";
  }

  protected ArrayList<DPObject> getSuggestListFromResponse(Object paramObject)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramObject instanceof DPObject[]))
      localArrayList.addAll(Arrays.asList((DPObject[])(DPObject[])paramObject));
    return localArrayList;
  }

  protected View getSuggestionEmptyView(String paramString, ViewGroup paramViewGroup)
  {
    return getActivity().getLayoutInflater().inflate(R.layout.empty_search_city_item, paramViewGroup, false);
  }

  public void onViewCreated(View paramView, @Nullable Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    if (paramView != null)
    {
      paramView.findViewById(R.id.searchBtn).setVisibility(8);
      paramView.findViewById(R.id.search_lay).setPadding(0, 0, ViewUtils.dip2px(getActivity(), 15.0F), 0);
    }
    this.searchEditText.setImeOptions(4);
    this.searchEditText.setHint("城市名,拼音,首字母...");
  }

  protected void search(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    String str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str))
    {
      searchSuggest(str);
      paramDPObject = str;
    }
    while (true)
    {
      new Thread(new Runnable(paramDPObject)
      {
        public void run()
        {
          String str = CitySearchFragment.this.getFileName();
          int i;
          if (!TextUtils.isEmpty(str))
          {
            i = str.indexOf(",");
            if (i != -1)
              break label45;
          }
          while (true)
          {
            HistorySearchSuggestionHelper.insert(CitySearchFragment.this.mContentResolver, this.val$finalKeyword, "", str);
            return;
            label45: str = str.substring(0, i);
          }
        }
      }).start();
      return;
      if (!(paramDPObject.getObject("City") instanceof DPObject))
        break;
      DPObject localDPObject = paramDPObject.getObject("City");
      str = localDPObject.getString("Name");
      paramDPObject = str;
      if (!(getActivity() instanceof CityListPickerActivity))
        continue;
      ((CityListPickerActivity)getActivity()).doSwitch(City.fromDPObject(localDPObject));
      KeyboardUtils.hideKeyboard(this.searchEditText);
      this.listView.setVisibility(8);
      getFragmentManager().popBackStackImmediate();
      paramDPObject = str;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.city.CitySearchFragment
 * JD-Core Version:    0.6.0
 */