package com.dianping.main.find;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.basic.MainSearchFragment;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.main.guide.MainActivity;
import com.dianping.main.home.RecommendListAgent;
import com.dianping.model.City;
import com.dianping.util.SearchUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFindFragment extends AdapterAgentFragment
  implements AbstractSearchFragment.OnSearchFragmentListener, CityConfig.SwitchListener
{
  private static final int TITLE_CITY_MAX_LENGTH = 4;
  private TextView btnCity;

  private void updateTitle()
  {
    String str2 = city().name();
    if ((str2 == null) || (this.btnCity == null))
      return;
    String str1 = str2;
    if (str2.length() > 4)
      str1 = str2.substring(0, 3) + "...";
    if (str1.length() >= 4)
      this.btnCity.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_15));
    while (true)
    {
      this.btnCity.setText(str1);
      return;
      this.btnCity.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_16));
    }
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("find/friends", FindFriendsGoWhereAgent.class);
        localHashMap.put("find/findAroundAgent", FindAroundAgent.class);
        localHashMap.put("find/checkintopic", RecommendListAgent.class);
        localHashMap.put("find/foreincityneaby", ForeinCityNearbyAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    cityConfig().addListener(this);
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    updateTitle();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHost("find");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.home_find_layout, paramViewGroup, false);
    SearchUtils.getSearchableInfo(getActivity(), getActivity().getComponentName());
    paramViewGroup = (ButtonSearchBar)paramLayoutInflater.findViewById(R.id.button_search_bar);
    paramViewGroup.setGAString("keyword");
    paramViewGroup.setHint(getResources().getString(R.string.default_search_hint));
    paramViewGroup.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener()
    {
      public void onSearchRequested()
      {
        MainSearchFragment localMainSearchFragment = MainSearchFragment.newInstance(MainFindFragment.this.getActivity());
        try
        {
          ((MainActivity)MainFindFragment.this.getActivity()).registerSearchFragment();
          label24: localMainSearchFragment.setOnSearchFragmentListener(MainFindFragment.this);
          MainFindFragment.this.statisticsEvent("searchtab5", "searchtab5_keyword", "", 0);
          MainFindFragment.this.statisticsEvent("index5", "index5_keyword_click", "", 0);
          return;
        }
        catch (Exception localException)
        {
          break label24;
        }
      }
    });
    this.btnCity = ((TextView)paramLayoutInflater.findViewById(R.id.find_button_city));
    this.btnCity.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://switchcity"));
        if (MainFindFragment.this.city().isForeign());
        for (int i = 1; ; i = 0)
        {
          paramView.putExtra("area", i);
          MainFindFragment.this.startActivity(paramView);
          return;
        }
      }
    });
    updateTitle();
    paramViewGroup = (PullToRefreshListView)paramLayoutInflater.findViewById(R.id.find_listview);
    paramViewGroup.setMode(PullToRefreshBase.Mode.DISABLED);
    setAgentContainerListView(paramViewGroup);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    cityConfig().removeListener(this);
    super.onDestroy();
  }

  public void onSearchFragmentDetach()
  {
    try
    {
      ((MainActivity)getActivity()).unregisterSearchFragment();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    statisticsEvent("index5", "index5_keyword", paramDPObject.getString("Keyword"), 0);
    Object localObject = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
      return;
    }
    localObject = new Bundle();
    ((Bundle)localObject).putString("source", "com.dianping.action.FIND");
    localObject = ButtonSearchBar.getResultIntent((Bundle)localObject, paramDPObject.getString("Keyword"), String.valueOf(paramDPObject.getInt("Count")));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject).putExtra("value", paramDPObject);
    startActivity((Intent)localObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.MainFindFragment
 * JD-Core Version:    0.6.0
 */