package com.dianping.base.shoplist.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.model.City;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;

public abstract class AbstractTabListActivity extends AgentActivity
{
  View fragment1layout;
  View fragment2layout;
  CustomImageButton leftButton;
  NovaFragment mCurrentFragment;
  NovaFragment mFragment1;
  NovaFragment mFragment2;
  TextView mTitle;
  public boolean needTitleBarShadow = true;
  CustomImageButton rightButton;
  private Drawable searchIconDrawable = null;
  protected Bundle shareBundle = new Bundle();
  View tabTitleLayout;
  String[] tabTitles = null;
  ShopListTabView tabView;

  void changeTab(int paramInt)
  {
    if ((this.fragment1layout == null) || (this.fragment2layout == null))
      return;
    if (paramInt == 0)
    {
      if ((this.mFragment1 != null) && (!this.mFragment1.isAdded()))
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, this.mFragment1).commitAllowingStateLoss();
      this.mCurrentFragment = this.mFragment1;
      this.fragment1layout.setVisibility(0);
      this.fragment2layout.setVisibility(8);
      if ((this.mFragment2 != null) && ((this.mFragment2 instanceof TitleListener)))
        ((TitleListener)this.mFragment2).onBlur();
      if ((this.mFragment1 != null) && ((this.mFragment1 instanceof TitleListener)))
        ((TitleListener)this.mFragment1).onFocus();
    }
    while (true)
    {
      if (paramInt != 2)
      {
        if ((this.searchIconDrawable == null) && (getResources() != null))
          this.searchIconDrawable = getResources().getDrawable(R.drawable.navibar_icon_search);
        if (this.searchIconDrawable != null)
          this.rightButton.setImageDrawable(this.searchIconDrawable);
      }
      this.shareBundle.putInt("tabIndex", paramInt);
      if ((this.mCurrentFragment == null) || (!(this.mCurrentFragment instanceof TitleTabListener)))
        break;
      ((TitleTabListener)this.mCurrentFragment).onTabChange(paramInt);
      return;
      if (paramInt == 1)
      {
        if ((this.mFragment2 != null) && (!this.mFragment2.isAdded()))
          getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, this.mFragment2).commitAllowingStateLoss();
        this.mCurrentFragment = this.mFragment2;
        this.fragment1layout.setVisibility(8);
        this.fragment2layout.setVisibility(0);
        if ((this.mFragment1 != null) && ((this.mFragment1 instanceof TitleListener)))
          ((TitleListener)this.mFragment1).onBlur();
        if ((this.mFragment2 == null) || (!(this.mFragment2 instanceof TitleListener)))
          continue;
        ((TitleListener)this.mFragment2).onFocus();
        continue;
      }
      if (paramInt != 2)
        continue;
      if ((this.mFragment1 != null) && (!this.mFragment1.isAdded()))
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, this.mFragment1).commitAllowingStateLoss();
      this.fragment1layout.setVisibility(0);
      this.fragment2layout.setVisibility(8);
      if (this.mCurrentFragment == this.mFragment2)
      {
        if ((this.mFragment2 != null) && ((this.mFragment2 instanceof TitleListener)))
          ((TitleListener)this.mFragment2).onBlur();
        if ((this.mFragment1 != null) && ((this.mFragment1 instanceof TitleListener)))
          ((TitleListener)this.mFragment1).onFocus();
      }
      this.mCurrentFragment = this.mFragment1;
    }
  }

  protected AgentFragment getAgentFragment()
  {
    return null;
  }

  public NovaFragment getCurrentFragment()
  {
    return this.mCurrentFragment;
  }

  public abstract NovaFragment[] getFragments()
    throws ClassNotFoundException, IllegalAccessException, InstantiationException;

  public String getKeyword()
  {
    if ((this.shareBundle == null) || (TextUtils.isEmpty(this.shareBundle.getString("keyword"))))
      return "";
    Log.d("debug_keyword", "get=" + this.shareBundle.getString("keyword"));
    return this.shareBundle.getString("keyword");
  }

  public String getPageName()
  {
    if ((this.mCurrentFragment instanceof GaPager))
      return ((GaPager)this.mCurrentFragment).getPageName();
    return super.getPageName();
  }

  public int getShopCategoryId()
  {
    Log.d("debug_category", "get id=" + this.shareBundle.getInt("categoryId", -1));
    return this.shareBundle.getInt("categoryId", -1);
  }

  public int getShopRangeId()
  {
    return this.shareBundle.getInt("rangeId", -2);
  }

  public int getShopRegionId()
  {
    Log.d("debug_region", "get id=" + this.shareBundle.getInt("regionId", 0));
    return this.shareBundle.getInt("regionId", 0);
  }

  public String getShopSortId()
  {
    String str2 = this.shareBundle.getString("sortId");
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = "0";
    return str1;
  }

  public String[] getTabTitles()
  {
    if (city().isTuan())
      return new String[] { "商户", "团购" };
    return new String[] { "全部商户" };
  }

  public ShopListTabView getTabView()
  {
    return this.tabView;
  }

  protected void initViewAgentView(Bundle paramBundle)
  {
    hideTitleBar();
    setContentView(R.layout.tab_list_layout);
    this.tabTitleLayout = findViewById(R.id.tab_title_layout);
    this.leftButton = ((CustomImageButton)findViewById(R.id.left_btn));
    this.leftButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AbstractTabListActivity.this.onBackPressed();
      }
    });
    this.rightButton = ((CustomImageButton)findViewById(R.id.right_btn));
    this.rightButton.setGAString("searchbtn");
    this.rightButton.setVisibility(0);
    this.rightButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if ((AbstractTabListActivity.this.mCurrentFragment instanceof AbstractTabListActivity.TitleListener))
          ((AbstractTabListActivity.TitleListener)AbstractTabListActivity.this.mCurrentFragment).onSearchClick();
      }
    });
    this.tabView = ((ShopListTabView)findViewById(R.id.title_bar_tab));
    paramBundle = (NovaLinearLayout)this.tabView.findViewById(R.id.tab1);
    Object localObject = (NovaLinearLayout)this.tabView.findViewById(R.id.tab2);
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.tabView.findViewById(R.id.tab3);
    paramBundle.setGAString("shoptab");
    ((NovaLinearLayout)localObject).setGAString("tuangoutab");
    localNovaLinearLayout.setGAString("producttab");
    this.mTitle = ((TextView)findViewById(R.id.title_bar_text));
    this.fragment1layout = findViewById(R.id.fragment1);
    this.fragment2layout = findViewById(R.id.fragment2);
    this.fragment1layout.setVisibility(0);
    this.fragment2layout.setVisibility(8);
    this.tabTitles = getTabTitles();
    if (this.tabTitles != null)
    {
      if (this.tabTitles.length != 1)
        break label339;
      this.mTitle.setText(this.tabTitles[0]);
      this.mTitle.setVisibility(0);
      this.tabView.setVisibility(8);
    }
    while (true)
    {
      paramBundle = new NovaFragment[0];
      try
      {
        localObject = getFragments();
        paramBundle = (Bundle)localObject;
        if (paramBundle != null)
        {
          if (paramBundle.length > 0)
            this.mFragment1 = paramBundle[0];
          if (paramBundle.length > 1)
            this.mFragment2 = paramBundle[1];
        }
        this.tabView.setTabChangeListener(new ShopListTabView.TabChangeListener()
        {
          public void onTabChanged(int paramInt)
          {
            AbstractTabListActivity.this.changeTab(paramInt);
          }
        });
        return;
        label339: if (this.tabTitles.length != 2)
          continue;
        this.tabView.setVisibility(0);
        this.mTitle.setVisibility(8);
        this.tabView.setLeftTitleText(this.tabTitles[0]);
        this.tabView.setRightTitleText(this.tabTitles[1]);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        while (true)
          localClassNotFoundException.printStackTrace();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        while (true)
          localIllegalAccessException.printStackTrace();
      }
      catch (InstantiationException localInstantiationException)
      {
        while (true)
          localInstantiationException.printStackTrace();
      }
    }
  }

  protected boolean isNeedCity()
  {
    return city().id() <= 0;
  }

  protected boolean needTitleBarShadow()
  {
    return this.needTitleBarShadow;
  }

  public void onAttachFragment(Fragment paramFragment)
  {
    super.onAttachFragment(paramFragment);
    if ((paramFragment instanceof TitleListener))
      ((TitleListener)paramFragment).onFocus();
  }

  public void onBackPressed()
  {
    if ((this.mCurrentFragment != null) && ((this.mCurrentFragment instanceof OnBackPressedOverrid)) && (((OnBackPressedOverrid)this.mCurrentFragment).onBackPressed()))
      return;
    super.onBackPressed();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.shareBundle = paramBundle.getBundle("shareBundle");
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBundle("shareBundle", this.shareBundle);
  }

  protected void onStart()
  {
    super.onStart();
    parseUrl();
    int i = this.shareBundle.getInt("tabIndex", 0);
    if (!this.tabView.setCurIndex(i))
      changeTab(i);
  }

  protected void parseUrl()
  {
    Object localObject = getIntent().getData();
    String str = ((Uri)localObject).getQueryParameter("keyword");
    if (!TextUtils.isEmpty(str))
      setKeyword(str);
    localObject = ((Uri)localObject).getQueryParameter("tab");
    if ((!TextUtils.isEmpty((CharSequence)localObject)) && (TextUtils.isDigitsOnly((CharSequence)localObject)))
      this.shareBundle.putInt("tabIndex", Integer.valueOf((String)localObject).intValue());
  }

  public void setKeyword(String paramString)
  {
    Log.d("debug_keyword", "set=" + paramString);
    this.shareBundle.putString("keyword", paramString);
    if (this.rightButton != null)
    {
      if (TextUtils.isEmpty(paramString))
        break label106;
      this.rightButton.setVisibility(8);
    }
    while (true)
    {
      if ((this.mFragment1 instanceof FilterChangeListener))
        ((FilterChangeListener)this.mFragment1).onKeywordChange(paramString);
      if ((this.mFragment2 instanceof FilterChangeListener))
        ((FilterChangeListener)this.mFragment2).onKeywordChange(paramString);
      return;
      label106: this.rightButton.setVisibility(0);
    }
  }

  public void setShopCategoryId(int paramInt)
  {
    Log.d("debug_category", "set id=" + paramInt);
    this.shareBundle.putInt("categoryId", paramInt);
    if ((this.mFragment1 instanceof FilterChangeListener))
      ((FilterChangeListener)this.mFragment1).onCategoryChange(paramInt);
    if ((this.mFragment2 instanceof FilterChangeListener))
      ((FilterChangeListener)this.mFragment2).onCategoryChange(paramInt);
  }

  public void setShopRangeId(int paramInt)
  {
    this.shareBundle.putInt("rangeId", paramInt);
    this.shareBundle.putInt("regionId", -2);
  }

  public void setShopRegionId(int paramInt)
  {
    this.shareBundle.putInt("regionId", paramInt);
    this.shareBundle.putInt("rangeId", -2);
  }

  public void setShopSordId(String paramString)
  {
    this.shareBundle.putString("sortId", paramString);
  }

  public void showSearchButton(boolean paramBoolean)
  {
    if (this.rightButton == null)
      return;
    if (paramBoolean)
    {
      this.rightButton.setVisibility(0);
      return;
    }
    this.rightButton.setVisibility(8);
  }

  public void showTabTitle(boolean paramBoolean)
  {
    if (this.tabTitleLayout == null)
      return;
    if (paramBoolean)
    {
      this.tabTitleLayout.setVisibility(0);
      return;
    }
    this.tabTitleLayout.setVisibility(8);
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    Object localObject = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      startActivity((String)localObject);
      return;
    }
    localObject = new StringBuilder("dianping://searchshoplist");
    ((StringBuilder)localObject).append("?tab=").append(this.shareBundle.getInt("tabIndex", 0));
    String str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str))
      ((StringBuilder)localObject).append("&keyword=").append(str);
    localObject = new Intent("android.intent.action.VIEW", Uri.parse(((StringBuilder)localObject).toString()));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject).putExtra("value", paramDPObject);
    startActivity((Intent)localObject);
  }

  public static abstract interface FilterChangeListener
  {
    public abstract void onCategoryChange(int paramInt);

    public abstract void onKeywordChange(String paramString);
  }

  public static abstract interface GaPager
  {
    public abstract String getPageName();
  }

  public static abstract interface OnBackPressedOverrid
  {
    public abstract boolean onBackPressed();
  }

  public static abstract interface TitleListener
  {
    public abstract void onBlur();

    public abstract void onFocus();

    public abstract void onSearchClick();
  }

  public static abstract interface TitleTabListener
  {
    public abstract void onTabChange(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.activity.AbstractTabListActivity
 * JD-Core Version:    0.6.0
 */