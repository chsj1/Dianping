package com.dianping.search.shoplist.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.basic.MainSearchFragment;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.loader.MyResources;
import com.dianping.model.City;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;

public class ShopListQQTitleAgent extends ShopListSearchTitleAgent
  implements ShopListTabView.TabChangeListener, CityConfig.SwitchListener
{
  private static final String CELL_TITLE_BAR = "002QQTitleBar";
  private ButtonSearchBar mBtnSearchBar;
  private View mBtnTitleLeft;
  private TextView mBtnTitleRight;
  String mGATag = "qqaio6";
  private LinearLayout mSearchBar;
  private String mSearchKeyword;
  private ShopListTabView mTabBar;
  private boolean mTabStatOn = false;
  private LinearLayout mTitleBar;
  private String mValue;

  public ShopListQQTitleAgent(Object paramObject)
  {
    super(paramObject);
    if (getHost() != null)
    {
      if (!getHost().equals("qqshoplist"))
        break label42;
      this.mGATag = "qqaio6";
    }
    label42: 
    do
      return;
    while (!getHost().equals("wxshoplist"));
    this.mGATag = "weixinplus6";
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mTitleBar == null)
    {
      this.mTitleBar = ((LinearLayout)inflater().inflate(R.layout.shop_list_qq_title_bar, getParentView(), false));
      this.mSearchBar = ((LinearLayout)this.mTitleBar.findViewById(R.id.search_bar));
      this.mBtnTitleLeft = this.mSearchBar.findViewById(R.id.left_title_button);
      this.mBtnTitleLeft.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          ShopListQQTitleAgent.this.getActivity().finish();
        }
      });
      this.mBtnTitleRight = ((TextView)this.mSearchBar.findViewById(R.id.right_title_button));
      this.mBtnTitleRight.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          ShopListQQTitleAgent.this.startActivity("dianping://switchcity");
        }
      });
      this.mBtnTitleRight.setText(city().name());
      this.mBtnSearchBar = ((ButtonSearchBar)this.mSearchBar.findViewById(R.id.button_search_bar));
      if (this.mBtnSearchBar != null)
      {
        this.mBtnSearchBar.setHint(getResources().getString(R.string.default_search_hint));
        Object localObject = getActivity().getIntent().getStringExtra("keyword");
        paramBundle = (Bundle)localObject;
        if (localObject == null)
          paramBundle = getActivity().getIntent().getData().getQueryParameter("keyword");
        localObject = paramBundle;
        if (paramBundle == null)
          localObject = getActivity().getIntent().getData().getQueryParameter("q");
        this.mSearchKeyword = ((String)localObject);
        this.mBtnSearchBar.setKeyword((String)localObject);
        this.mBtnSearchBar.setButtonSearchBarListener(this);
      }
      this.mTabBar = ((ShopListTabView)this.mTitleBar.findViewById(R.id.tab_bar));
      this.mTabBar.setLeftTitleText("商户");
      this.mTabBar.setRightTitleText("团购");
      this.mTabBar.setTabChangeListener(this);
      this.mTabBar.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 45.0F)));
      this.mTabBar.setGravity(17);
      addCell("002QQTitleBar", this.mTitleBar);
    }
    if (city().isTuan())
    {
      this.mTabBar.setVisibility(0);
      return;
    }
    this.mTabBar.setVisibility(8);
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    if (paramCity1.id() != paramCity2.id())
    {
      this.mBtnTitleRight.setText(paramCity2.name());
      getDataSource().setCityId(paramCity2.id());
      if (!isCurrentCity())
        break label75;
      getDataSource().setShowDistance(true);
    }
    while (TextUtils.isEmpty(this.mSearchKeyword))
    {
      getDataSource().reset(true);
      getDataSource().reload(true);
      return;
      label75: getDataSource().setShowDistance(false);
    }
    refreshSearchResult(this.mSearchKeyword);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().cityConfig().addListener(this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    DPApplication.instance().cityConfig().removeListener(this);
  }

  public void onSearchRequested()
  {
    MainSearchFragment localMainSearchFragment = MainSearchFragment.newInstance(getActivity(), ShopListUtils.getCategoryIdFromDataSource(getDataSource()));
    localMainSearchFragment.setKeyword(this.mSearchKeyword);
    localMainSearchFragment.setOnSearchFragmentListener(this);
  }

  public void onTabChanged(int paramInt)
  {
    getDataSource().setCurrentTabIndex(paramInt);
    getDataSource().reset(true);
    getDataSource().reload(true);
    this.mTabStatOn = true;
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.mSearchKeyword = paramDPObject.getString("Keyword");
    this.mValue = paramDPObject.getString("Value");
    if (this.mSearchKeyword != null)
      this.mBtnSearchBar.setKeyword(this.mSearchKeyword);
    getDataSource().setSuggestKeyword(this.mSearchKeyword);
    getDataSource().setSuggestValue(this.mValue);
    refreshSearchResult(paramDPObject.getString("Keyword"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListQQTitleAgent
 * JD-Core Version:    0.6.0
 */