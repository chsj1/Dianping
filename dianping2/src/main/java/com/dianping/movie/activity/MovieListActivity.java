package com.dianping.movie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.movie.fragment.MovieListFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class MovieListActivity extends MovieBaseActivity
{
  private static final String MOVIE_FRAGMENT_FUTURE = "FUTURE";
  private static final String MOVIE_FRAGMENT_HOT = "HOT";
  public static final int SEARCH_MOVIE_ONLY = 1;
  private Context context;
  private String currentTab;
  private FragmentManager fragmentManager;
  private MovieListFragment movieOnListFragment;
  private MovieListFragment movieUpComingListFragment;
  ShopListTabView tabBarView;

  private void switchFragment(String paramString)
  {
    FragmentTransaction localFragmentTransaction = this.fragmentManager.beginTransaction();
    try
    {
      this.movieOnListFragment = ((MovieListFragment)this.fragmentManager.findFragmentByTag("HOT"));
      this.movieUpComingListFragment = ((MovieListFragment)this.fragmentManager.findFragmentByTag("FUTURE"));
      label40: if (paramString == "HOT")
        if (this.movieOnListFragment == null)
        {
          this.movieOnListFragment = new MovieListFragment();
          paramString = new Bundle();
          paramString.putInt("filter", 1);
          paramString.putString("from", this.from);
          this.movieOnListFragment.setArguments(paramString);
          localFragmentTransaction.add(R.id.movie_list, this.movieOnListFragment, "HOT");
          if (this.movieUpComingListFragment != null)
            localFragmentTransaction.hide(this.movieUpComingListFragment);
        }
      label239: 
      while (true)
      {
        localFragmentTransaction.commit();
        return;
        localFragmentTransaction.show(this.movieOnListFragment);
        break;
        if (this.movieUpComingListFragment == null)
        {
          this.movieUpComingListFragment = new MovieListFragment();
          paramString = new Bundle();
          paramString.putInt("filter", 2);
          paramString.putString("from", this.from);
          this.movieUpComingListFragment.setArguments(paramString);
          localFragmentTransaction.add(R.id.movie_list, this.movieUpComingListFragment, "FUTURE");
        }
        while (true)
        {
          if (this.movieOnListFragment == null)
            break label239;
          localFragmentTransaction.hide(this.movieOnListFragment);
          break;
          localFragmentTransaction.show(this.movieUpComingListFragment);
        }
      }
    }
    catch (Exception localException)
    {
      break label40;
    }
  }

  protected ShopListTabView createTabView()
  {
    ShopListTabView localShopListTabView = (ShopListTabView)LayoutInflater.from(this).inflate(R.layout.shoplist_tab_layout, null, false);
    localShopListTabView.setLeftTitleText("正在热映");
    localShopListTabView.setRightTitleText("即将上映");
    localShopListTabView.setTabChangeListener(new ShopListTabView.TabChangeListener()
    {
      public void onTabChanged(int paramInt)
      {
        if (paramInt == 0)
        {
          MovieListActivity.this.switchFragment("HOT");
          GAHelper.instance().contextStatisticsEvent(MovieListActivity.this, "hotmovie", null, 0, "tap");
          return;
        }
        MovieListActivity.this.switchFragment("FUTURE");
        GAHelper.instance().contextStatisticsEvent(MovieListActivity.this, "futuremovie", null, 0, "tap");
      }
    });
    return localShopListTabView;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    label19: int i;
    if (paramBundle != null)
    {
      this.currentTab = paramBundle.getString("currentTab");
      setPageId("9040004");
      this.context = this;
      setContentView(R.layout.movie_list_activity);
      paramBundle = new ImageView(this.context);
      paramBundle.setImageDrawable(getResources().getDrawable(R.drawable.movie_titlebar_icon_search));
      getTitleBar().addRightViewItem(paramBundle, "moviekeyword", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent(MovieListActivity.this, MovieSearchActivity.class);
          paramView.putExtra("scope", 1);
          MovieListActivity.this.startActivity(paramView);
          GAHelper.instance().contextStatisticsEvent(MovieListActivity.this, "search", null, 0, "tap");
        }
      });
      this.fragmentManager = getSupportFragmentManager();
      this.tabBarView = createTabView();
      getTitleBar().setCustomContentView(this.tabBarView);
      this.tabBarView.setCurIndex(0);
      if (TextUtils.isEmpty(this.currentTab))
        break label235;
      paramBundle = this.currentTab;
      i = -1;
      switch (paramBundle.hashCode())
      {
      default:
      case -1263170109:
      }
    }
    while (true)
      switch (i)
      {
      default:
        this.tabBarView.setCurIndex(0);
        switchFragment("HOT");
        return;
        this.currentTab = getStringParam("tab");
        break label19;
        if (!paramBundle.equals("future"))
          continue;
        i = 0;
      case 0:
      }
    this.tabBarView.setCurIndex(1);
    switchFragment("FUTURE");
    return;
    label235: this.tabBarView.setCurIndex(0);
    switchFragment("HOT");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("currentTab", this.currentTab);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieListActivity
 * JD-Core Version:    0.6.0
 */