package com.dianping.search.shoplist.agent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.basic.MainSearchFragment;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.agent.ShopListAgent;
import com.dianping.base.shoplist.agentconfig.ShopListAgentConfig;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.ShopListConst;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.loader.MyResources;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.NearByTopShopListAgentConfig;
import com.dianping.search.widget.SearchCustomHorizontalScrollView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;

public class ShopListSearchAgent extends ShopListAgent
  implements View.OnClickListener, AbstractSearchFragment.OnSearchFragmentListener
{
  private static final String CELL_SEARCH_BAR = "000TitleBar";
  private static final Handler mHandler = new Handler();
  NewShopListDataSource dataSource;
  NovaImageView mClearKeywordBtn;
  NovaLinearLayout mGuideKeywordContent;
  private String mHintText;
  View mKeywordLayout;
  NovaTextView mKeywordView;
  CustomImageButton mLeftButton;
  String mPrevSearchKeyword = "";
  String mSearchKeyword;
  View mSearchLayoutCover;
  View mSearchlayoutTab;
  SearchCustomHorizontalScrollView mSrcollContent;
  View mTitleSearchTab;
  protected View.OnClickListener onSearchKeywordListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (ShopListSearchAgent.this.mGuideKeywordContent.getChildCount() == 1)
      {
        paramView = ShopListSearchAgent.this.createSearchFragment();
        paramView.setKeyword("");
        paramView.setOnSearchFragmentListener(ShopListSearchAgent.this);
      }
      int j;
      String str;
      while (true)
      {
        return;
        ShopListSearchAgent.this.mGuideKeywordContent.removeView(paramView);
        j = ShopListSearchAgent.this.mGuideKeywordContent.getChildCount();
        paramView = new StringBuilder();
        if (j != 1)
          break;
        str = ShopListSearchAgent.this.mGuideKeywordContent.getChildAt(0).getTag().toString();
        if ((str == null) || (TextUtils.isEmpty(str)))
          continue;
        paramView.append(str);
        ShopListSearchAgent.this.dataSource.setSuggestKeyword(paramView.toString());
        ShopListSearchAgent.this.dataSource.reset(true);
        ShopListSearchAgent.this.dataSource.reload(false);
        ShopListSearchAgent.this.mSearchKeyword = paramView.toString();
        ShopListSearchAgent.this.mPrevSearchKeyword = ShopListSearchAgent.this.mSearchKeyword;
        return;
      }
      int i = 0;
      label173: if (i < j)
      {
        str = ShopListSearchAgent.this.mGuideKeywordContent.getChildAt(i).getTag().toString();
        if ((str != null) && (!TextUtils.isEmpty(str)))
          break label215;
      }
      while (true)
      {
        i += 1;
        break label173;
        break;
        label215: paramView.append(str);
        if (i == j - 1)
          continue;
        paramView.append(" ");
      }
    }
  };
  int type;

  public ShopListSearchAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setSearchKeywordBackground(View paramView, TextView paramTextView, ImageView paramImageView)
  {
    paramView.setVisibility(0);
    paramTextView.setVisibility(0);
    paramImageView.setVisibility(0);
    if (this.mClearKeywordBtn != null)
      this.mClearKeywordBtn.setVisibility(8);
    if (this.mKeywordView != null)
      this.mKeywordView.setVisibility(8);
    if (this.type == 1)
    {
      paramView.setBackgroundResource(R.drawable.guide_keyword_bar_orange_background);
      paramTextView.setTextColor(getResources().getColor(R.color.white));
      paramImageView.setImageResource(R.drawable.search_ic_close_orange);
    }
    while (true)
    {
      if (this.type == 2)
      {
        this.mKeywordView.setText(this.mSearchKeyword);
        this.mKeywordView.setVisibility(0);
        this.mClearKeywordBtn.setVisibility(0);
        paramView.setVisibility(8);
        paramTextView.setVisibility(8);
        paramImageView.setVisibility(8);
      }
      return;
      if (this.type != 0)
        continue;
      paramView.setBackgroundResource(R.drawable.guide_keyword_bar_background);
      paramTextView.setTextColor(getResources().getColor(R.color.text_gray));
      paramImageView.setImageResource(R.drawable.search_ic_close);
    }
  }

  protected void createGuideKeywordView(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.search_guide_keyword_text, null);
    NovaTextView localNovaTextView = (NovaTextView)localView.findViewById(R.id.search_bar_guide_text);
    ImageView localImageView = (ImageView)localView.findViewById(R.id.search_iv_close);
    LinearLayout localLinearLayout = (LinearLayout)localView.findViewById(R.id.search_layout_guide_bg);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -1);
    localLayoutParams.setMargins(getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_text_margin), 0, 0, 0);
    localLinearLayout.setLayoutParams(localLayoutParams);
    localNovaTextView.setText(paramString.toString());
    localView.setOnClickListener(this.onSearchKeywordListener);
    localView.setTag(paramString);
    if (this.dataSource != null)
    {
      paramString = DPActivity.preferences(getContext().getApplicationContext());
      this.type = paramString.getInt("SearchBarType", 2);
      if (this.dataSource.status() == 2)
      {
        this.type = this.dataSource.guideKeywordType;
        paramString.edit().putInt("SearchBarType", this.type).apply();
      }
      setSearchKeywordBackground(localLinearLayout, localNovaTextView, localImageView);
    }
    localNovaTextView.setGravity(19);
    this.mGuideKeywordContent.addView(localView);
  }

  protected AbstractSearchFragment createSearchFragment()
  {
    if ((getFragment() instanceof ShopListAgentFragment))
      return ((ShopListAgentFragment)getFragment()).getCurrentAgentListConfig().createSuggestFragment(getActivity(), getDataSource());
    return MainSearchFragment.newInstance(getActivity(), ShopListUtils.getCategoryIdFromDataSource(getDataSource()));
  }

  protected View createSearchKeywordBar()
  {
    View localView = inflater().inflate(R.layout.search_keyword_title_bar, getParentView(), false);
    this.mTitleSearchTab = localView.findViewById(R.id.title_search_tab);
    this.mKeywordLayout = localView.findViewById(R.id.search_bar);
    this.mKeywordView = ((NovaTextView)localView.findViewById(R.id.keyword));
    this.mKeywordView.setOnClickListener(this);
    this.mTitleSearchTab.setOnClickListener(this);
    this.mClearKeywordBtn = ((NovaImageView)localView.findViewById(R.id.search_bar_clear));
    this.mClearKeywordBtn.setOnClickListener(this);
    this.mLeftButton = ((CustomImageButton)localView.findViewById(R.id.left_btn));
    this.mLeftButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (ShopListSearchAgent.this.getActivity() != null)
          ShopListSearchAgent.this.getActivity().onBackPressed();
      }
    });
    this.mSearchlayoutTab = localView.findViewById(R.id.title_search_tab);
    this.mGuideKeywordContent = ((NovaLinearLayout)localView.findViewById(R.id.search_guide_keyword_content));
    this.mSrcollContent = ((SearchCustomHorizontalScrollView)localView.findViewById(R.id.search_srcoll_content));
    if ((getCurrentAgentConfig() instanceof NearByTopShopListAgentConfig))
    {
      this.mKeywordView.setGAString("headlinesearch");
      this.mClearKeywordBtn.setGAString("headlinesearch");
    }
    while (true)
    {
      this.mGuideKeywordContent = ((NovaLinearLayout)localView.findViewById(R.id.search_guide_keyword_content));
      this.mSrcollContent = ((SearchCustomHorizontalScrollView)localView.findViewById(R.id.search_srcoll_content));
      this.mSearchLayoutCover = localView.findViewById(R.id.search_layout_cover);
      return localView;
      this.mKeywordView.setGAString("search_box");
      this.mClearKeywordBtn.setGAString("search_box");
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.dataSource = ((NewShopListDataSource)getDataSource());
    if (getActivity() == null);
    do
      return;
    while (getDataSource() == null);
    if ((paramBundle != null) && (paramBundle.getBoolean("needSearch")))
    {
      createSearchFragment().setOnSearchFragmentListener(this);
      return;
    }
    if ((getFragment() instanceof ShopListAgentFragment))
      ((ShopListAgentFragment)getFragment()).updateWedTab();
    this.mSearchKeyword = getDataSource().suggestKeyword();
    if ((paramBundle != null) && (paramBundle.getBoolean("onFocus")))
    {
      paramBundle = null;
      if ((getActivity() instanceof AbstractTabListActivity))
        paramBundle = ((AbstractTabListActivity)getActivity()).getKeyword();
      if ((TextUtils.isEmpty(this.mSearchKeyword)) && (TextUtils.isEmpty(paramBundle)))
      {
        refreshSearchResult(this.mSearchKeyword, false);
        return;
      }
      if ((!TextUtils.isEmpty(this.mSearchKeyword)) && (this.mSearchKeyword.equals(paramBundle)))
      {
        refreshSearchResult(this.mSearchKeyword, false);
        return;
      }
      if (TextUtils.isEmpty(paramBundle))
      {
        refreshSearchResult(paramBundle, false);
        return;
      }
      refreshSearchResult(paramBundle, true);
      return;
    }
    if ((!TextUtils.isEmpty(this.mSearchKeyword)) && ((getActivity() instanceof AbstractTabListActivity)))
      ((AbstractTabListActivity)getActivity()).setKeyword(this.mSearchKeyword);
    setKeyword(this.mSearchKeyword);
    if ((this.dataSource.isGuideKeyword) && (this.dataSource.status() == 2))
      this.dataSource.setGuideKeywrodFlag(false);
    this.mPrevSearchKeyword = this.mSearchKeyword;
  }

  public void onClick(View paramView)
  {
    AbstractSearchFragment localAbstractSearchFragment = createSearchFragment();
    if (paramView.getId() == R.id.search_bar_clear)
      localAbstractSearchFragment.setKeyword("");
    while (((getCurrentAgentConfig() instanceof NearByTopShopListAgentConfig)) && ((getFragment() instanceof AbstractSearchFragment.OnSearchFragmentListener)))
    {
      localAbstractSearchFragment.setOnSearchFragmentListener((AbstractSearchFragment.OnSearchFragmentListener)getFragment());
      return;
      if ((paramView.getId() != R.id.keyword) && (paramView.getId() != R.id.title_search_tab))
        continue;
      localAbstractSearchFragment.setKeyword(this.mSearchKeyword);
    }
    if (((getCurrentAgentConfig() instanceof NearByTopShopListAgentConfig)) && ((getFragment() instanceof AbstractSearchFragment.OnSearchFragmentListener)))
    {
      localAbstractSearchFragment.setOnSearchFragmentListener((AbstractSearchFragment.OnSearchFragmentListener)getFragment());
      return;
    }
    localAbstractSearchFragment.setOnSearchFragmentListener(this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((getCurrentAgentConfig() instanceof NearByTopShopListAgentConfig))
      this.mHintText = getResources().getString(R.string.search_nearby_hint);
  }

  public void onSearchFragmentDetach()
  {
  }

  protected void refreshSearchResult(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      getDataSource().setCurRange(null);
      getDataSource().setCurCategory(ShopListConst.ALL_CATEGORY);
      getDataSource().setCurRegion(ShopListConst.ALL_REGION);
      getDataSource().setCurMetro(null);
      getDataSource().setIsMetro(false);
      getDataSource().setCurSort(null);
      getDataSource().setCurSelectNav(null);
      getDataSource().setMaxPrice(-1);
      getDataSource().setMinPrice(-1);
      getDataSource().setCityId(0);
      getDataSource().setFilterNavi(null);
    }
    if (paramString != null)
    {
      if ((paramString.equals("清除搜索记录")) || (paramString.equals("清空搜索记录")))
      {
        new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3).clearHistory();
        return;
      }
      if ((getActivity() != null) && (getActivity().getIntent() != null))
      {
        getActivity().getIntent().putExtra("keyword", paramString);
        new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3).saveRecentQuery(paramString, null);
      }
    }
    if ((getActivity() instanceof AbstractTabListActivity))
      ((AbstractTabListActivity)getActivity()).setKeyword(paramString);
    if (TextUtils.isEmpty(paramString))
    {
      getDataSource().setSuggestKeyword("");
      getDataSource().setSuggestValue("");
      if (((getFragment() instanceof ShopListAgentFragment)) && (((ShopListAgentFragment)getFragment()).isCurrentCity()) && ((getDataSource().curRegion() == null) || (getDataSource().curRegion() == ShopListConst.ALL_REGION)))
        getDataSource().setCurRegion(ShopListConst.NEARBY);
      if ((getFragment() instanceof ShopListAgentFragment))
        ((ShopListAgentFragment)getFragment()).GATag = "nearby";
    }
    while (true)
    {
      setKeyword(paramString);
      getDataSource().reset(true);
      getDataSource().reload(false);
      return;
      getDataSource().setSuggestKeyword(paramString);
      if (!(getFragment() instanceof ShopListAgentFragment))
        continue;
      ((ShopListAgentFragment)getFragment()).GATag = "search";
    }
  }

  protected void setBackBtnGone()
  {
    if ((this.mLeftButton == null) || (this.mTitleSearchTab == null))
      return;
    this.mLeftButton.setVisibility(8);
    this.mTitleSearchTab.setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
    this.mSearchLayoutCover.setVisibility(8);
  }

  protected void setGuideKeyword(String paramString)
  {
    paramString = paramString.split(" ");
    if (this.mGuideKeywordContent != null)
      this.mGuideKeywordContent.removeAllViews();
    if (paramString != null)
    {
      int j = paramString.length;
      int i = 0;
      while (i < j)
      {
        createGuideKeywordView(paramString[i]);
        i += 1;
      }
    }
    if ((getActivity() instanceof AbstractTabListActivity))
      ((AbstractTabListActivity)getActivity()).showTabTitle(false);
    if ((getFragment() instanceof ShopListAgentFragment))
      ((ShopListAgentFragment)getFragment()).setKeyWordsTitle(true);
  }

  protected void setKeyword(String paramString)
  {
    if ((getCurrentAgentConfig() instanceof NearByTopShopListAgentConfig))
      this.mHintText = getResources().getString(R.string.default_search_hint);
    SharedPreferences localSharedPreferences;
    if ((!TextUtils.isEmpty(paramString)) || (!TextUtils.isEmpty(this.mHintText)))
    {
      if (this.mKeywordLayout == null)
        addCell("000TitleBar", createSearchKeywordBar());
      if (!TextUtils.isEmpty(this.mHintText))
      {
        this.mKeywordView.setHint(this.mHintText);
        this.mClearKeywordBtn.setVisibility(8);
      }
      localSharedPreferences = DPActivity.preferences(getContext().getApplicationContext());
      this.type = localSharedPreferences.getInt("SearchBarType", 2);
      if (!TextUtils.isEmpty(paramString))
        if (!this.dataSource.isGuideKeyword)
          if (!this.mSearchKeyword.equals(this.mPrevSearchKeyword))
          {
            if (this.mGuideKeywordContent != null)
              this.mGuideKeywordContent.removeAllViews();
            if (this.dataSource.status() == 2)
            {
              if (this.dataSource.guideKeywordResult != null)
                this.type = this.dataSource.guideKeywordType;
              localSharedPreferences.edit().putInt("SearchBarType", this.type).apply();
            }
            setGuideKeyword(this.mSearchKeyword);
          }
    }
    do
    {
      do
      {
        while (true)
        {
          if (this.mGuideKeywordContent != null)
            mHandler.post(new Runnable()
            {
              public void run()
              {
                ShopListSearchAgent.this.mSrcollContent.scrollTo(ViewUtils.getViewWidth(ShopListSearchAgent.this.mGuideKeywordContent), 0);
              }
            });
          return;
          if (this.dataSource.status() != 2)
            continue;
          if (this.dataSource.guideKeywordResult != null)
            this.type = this.dataSource.guideKeywordType;
          localSharedPreferences.edit().putInt("SearchBarType", this.type).apply();
          if (this.mGuideKeywordContent == null)
            continue;
          int j = this.mGuideKeywordContent.getChildCount();
          int i = 0;
          while (i < j)
          {
            paramString = this.mGuideKeywordContent.getChildAt(i);
            setSearchKeywordBackground(paramString.findViewById(R.id.search_layout_guide_bg), (NovaTextView)paramString.findViewById(R.id.search_bar_guide_text), (ImageView)paramString.findViewById(R.id.search_iv_close));
            i += 1;
          }
          continue;
          if (this.dataSource.status() == 2)
          {
            if (this.dataSource.guideKeywordResult != null)
              this.type = this.dataSource.guideKeywordType;
            localSharedPreferences.edit().putInt("SearchBarType", this.type).apply();
          }
          if (this.mSearchKeyword.equals(this.mPrevSearchKeyword))
            continue;
          paramString = paramString.split(" ");
          createGuideKeywordView(paramString[(paramString.length - 1)]);
        }
        if (!(getActivity() instanceof AbstractTabListActivity))
          continue;
        ((AbstractTabListActivity)getActivity()).showTabTitle(false);
      }
      while (!(getFragment() instanceof ShopListAgentFragment));
      ((ShopListAgentFragment)getFragment()).setKeyWordsTitle(true);
      return;
      if (this.mKeywordLayout != null)
        this.mKeywordLayout.setVisibility(8);
      if (!(getActivity() instanceof AbstractTabListActivity))
        continue;
      ((AbstractTabListActivity)getActivity()).showTabTitle(true);
    }
    while (!(getFragment() instanceof ShopListAgentFragment));
    ((ShopListAgentFragment)getFragment()).setKeyWordsTitle(false);
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    String str = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty(str))
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
      return;
    }
    str = paramDPObject.getString("Value");
    getDataSource().setSuggestValue(str);
    if (!TextUtils.isEmpty(str))
    {
      getDataSource().setCurRegion(ShopListConst.ALL_REGION);
      getDataSource().setPlaceType(2);
    }
    if ((getDataSource() instanceof NewShopListDataSource))
      ((NewShopListDataSource)getDataSource()).disableRewrite = 0;
    paramDPObject = paramDPObject.getString("Keyword");
    if ((getActivity() != null) && (getActivity().getIntent() != null) && (getActivity().getIntent().getData() != null))
      getActivity().getIntent().getData().buildUpon().appendQueryParameter("keyword", paramDPObject);
    refreshSearchResult(paramDPObject, true);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListSearchAgent
 * JD-Core Version:    0.6.0
 */