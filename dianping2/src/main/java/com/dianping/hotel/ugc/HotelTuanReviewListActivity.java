package com.dianping.hotel.ugc;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;

public class HotelTuanReviewListActivity extends NovaActivity
{
  private HotelTuanReviewDetailFragment dealFragment;
  private ReviewListFragment shopFragment;
  private int shopId;
  private String shopName;
  private int tabIndex;

  private ReviewListFragment getShopReviewListFragment()
  {
    if (this.shopFragment == null)
    {
      this.shopFragment = new ReviewListFragment();
      this.shopFragment.setShopId(this.shopId);
    }
    return this.shopFragment;
  }

  private HotelTuanReviewDetailFragment getTuanReviewListFragment()
  {
    if (this.dealFragment == null)
      this.dealFragment = new HotelTuanReviewDetailFragment();
    return this.dealFragment;
  }

  private void initViews(Bundle paramBundle)
  {
    super.setContentView(R.layout.hotel_temp_hoteltuan_reviewlist_layout);
    Object localObject = (ShopListTabView)LayoutInflater.from(this).inflate(R.layout.shoplist_tab_layout, null);
    ((ShopListTabView)localObject).setTabChangeListener(new ShopListTabView.TabChangeListener()
    {
      public void onTabChanged(int paramInt)
      {
        FragmentTransaction localFragmentTransaction = HotelTuanReviewListActivity.this.getSupportFragmentManager().beginTransaction();
        if (paramInt == 0)
        {
          ReviewListFragment localReviewListFragment = HotelTuanReviewListActivity.this.getShopReviewListFragment();
          localReviewListFragment.setNeedFilter(true);
          localFragmentTransaction.replace(R.id.reviewlist_content, localReviewListFragment, "shop");
          localFragmentTransaction.commit();
        }
        do
          return;
        while (paramInt != 1);
        localFragmentTransaction.replace(R.id.reviewlist_content, HotelTuanReviewListActivity.this.getTuanReviewListFragment(), "deal");
        localFragmentTransaction.commit();
      }
    });
    ((ShopListTabView)localObject).setLeftTitleText(getString(R.string.ugc_shop));
    ((ShopListTabView)localObject).setRightTitleText(getString(R.string.ugc_tuan));
    ((ShopListTabView)localObject).setCurIndex(this.tabIndex - 1);
    getTitleBar().setCustomContentView((View)localObject);
    localObject = getSupportFragmentManager();
    if (paramBundle == null)
    {
      this.shopFragment = getShopReviewListFragment();
      this.dealFragment = getTuanReviewListFragment();
      paramBundle = ((FragmentManager)localObject).beginTransaction();
      if (this.tabIndex == 1)
        paramBundle.add(R.id.reviewlist_content, getShopReviewListFragment(), "shop");
      while (true)
      {
        paramBundle.commit();
        return;
        if (this.tabIndex != 2)
          continue;
        paramBundle.add(R.id.reviewlist_content, getTuanReviewListFragment(), "deal");
      }
    }
    this.shopFragment = ((ReviewListFragment)((FragmentManager)localObject).findFragmentByTag("shop"));
    this.dealFragment = ((HotelTuanReviewDetailFragment)((FragmentManager)localObject).findFragmentByTag("deal"));
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null);
    for (int i = getIntParam("tab", 1); ; i = paramBundle.getInt("tabIndex"))
    {
      this.tabIndex = i;
      this.shopId = getIntParam("shopid", 0);
      this.shopName = getStringParam("shopName");
      if (TextUtils.isEmpty(this.shopName))
        this.shopName = "default";
      initViews(paramBundle);
      return;
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("tabIndex", this.tabIndex);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.HotelTuanReviewListActivity
 * JD-Core Version:    0.6.0
 */