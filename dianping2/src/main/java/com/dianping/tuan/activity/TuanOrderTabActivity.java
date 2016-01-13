package com.dianping.tuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.tuan.fragment.TuanMeiOrderListFragment;
import com.dianping.tuan.fragment.TuanOrderListFragment;
import com.dianping.tuan.fragment.UnusedCouponListFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class TuanOrderTabActivity extends BaseTuanActivity
{
  protected static final String HOST_COUPON = "mycoupon";
  protected static final String HOST_MEIORDER = "mymeituanorder";
  protected static final String HOST_ORDER = "myorder";
  String host;
  boolean isEdit;
  protected TitleBar mTitleBar;
  TuanMeiOrderListFragment meituanOrderListFragment;
  TuanOrderListFragment orderFragment;
  String tab;
  ShopListTabView tabBarView;
  UnusedCouponListFragment unusedCouponListFragment;

  private void addFragments()
  {
    this.unusedCouponListFragment = new UnusedCouponListFragment();
    this.orderFragment = new TuanOrderListFragment();
    this.meituanOrderListFragment = new TuanMeiOrderListFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("coupon_fragment", getIntent().getData().getQueryParameter("tab"));
    this.unusedCouponListFragment.setArguments(localBundle);
    localBundle = new Bundle();
    localBundle.putString("order_fragment", getIntent().getData().getQueryParameter("tab"));
    this.orderFragment.setArguments(localBundle);
    localBundle = new Bundle();
    localBundle.putString("meituan_fragment", getIntent().getData().getQueryParameter("tab"));
    this.meituanOrderListFragment.setArguments(localBundle);
    getSupportFragmentManager().beginTransaction().add(16908300, this.orderFragment, "order_fragment").commit();
    getSupportFragmentManager().beginTransaction().add(16908300, this.unusedCouponListFragment, "coupon_fragment").commit();
    getSupportFragmentManager().beginTransaction().add(16908300, this.meituanOrderListFragment, "meituan_fragment").commit();
  }

  private void tabChanged(int paramInt)
  {
    if (isFinishing())
      return;
    if (paramInt == 0)
      showCouponFragment();
    while (true)
    {
      toggleRemoveBtn();
      return;
      if (paramInt == 2)
      {
        showOrderFragment();
        continue;
      }
      showMeiTuanOrderFragment();
    }
  }

  public void changeTab(String paramString1, String paramString2)
  {
    if ("myorder".equals(paramString1))
    {
      this.tabBarView.setCurIndex(2);
      this.orderFragment.setCurrentTab(paramString2);
    }
    do
    {
      return;
      if (!"mycoupon".equals(paramString1))
        continue;
      this.tabBarView.setCurIndex(0);
      return;
    }
    while (!"mymeituanorder".equals(paramString1));
    this.tabBarView.setCurIndex(1);
  }

  protected ShopListTabView createTabView()
  {
    ShopListTabView localShopListTabView = (ShopListTabView)LayoutInflater.from(this).inflate(R.layout.shoplist_tab_layout, null, false);
    localShopListTabView.setLeftTitleText("团购券");
    localShopListTabView.setMidTitleText("团购订单");
    localShopListTabView.setRightTitleText("美团订单");
    localShopListTabView.setTabChangeListener(new ShopListTabView.TabChangeListener()
    {
      public void onTabChanged(int paramInt)
      {
        TuanOrderTabActivity.this.tabChanged(paramInt);
      }
    });
    return localShopListTabView;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onBackPressed()
  {
    if ((this.tabBarView.getCurrentIndex() == 1) && (this.meituanOrderListFragment.onGoBack()))
    {
      this.meituanOrderListFragment.onBackPressed();
      return;
    }
    super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isLogined())
      setupView();
  }

  public boolean onGoBack()
  {
    if (this.isEdit)
      setIsEdit(false);
    do
      return false;
    while ((this.tabBarView.getCurrentIndex() == 1) && (this.meituanOrderListFragment.onGoBack()));
    return super.onGoBack();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((paramBoolean) && (isNeedLogin()))
      setupView();
    return super.onLogin(paramBoolean);
  }

  protected void onResume()
  {
    if ("myorder".equals(this.host))
    {
      this.tabBarView.setCurIndex(2);
      this.orderFragment.setCurrentTab(this.tab);
      tabChanged(2);
    }
    while (true)
    {
      this.host = null;
      toggleRemoveBtn();
      super.onResume();
      return;
      if ("mycoupon".equals(this.host))
      {
        this.tabBarView.setCurIndex(0);
        tabChanged(0);
        continue;
      }
      if (!"mymeituanorder".equals(this.host))
        continue;
      this.tabBarView.setCurIndex(1);
      tabChanged(1);
    }
  }

  public void setIsEdit(boolean paramBoolean)
  {
    this.isEdit = paramBoolean;
    if (paramBoolean)
      if (this.tabBarView.getCurrentIndex() == 2)
      {
        this.orderFragment.showEditView(true);
        showEditTitleBar();
      }
    while (true)
    {
      toggleRemoveBtn();
      return;
      if (this.tabBarView.getCurrentIndex() != 2)
        continue;
      this.orderFragment.showEditView(false);
      showDefaultTitleBar();
    }
  }

  protected void setupView()
  {
    Object localObject = new FrameLayout(this);
    ((FrameLayout)localObject).setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    ((FrameLayout)localObject).setId(16908300);
    setContentView((View)localObject);
    localObject = getIntent().getData();
    if (localObject != null)
    {
      this.host = ((Uri)localObject).getHost();
      this.tab = ((Uri)localObject).getQueryParameter("tab");
    }
    this.mTitleBar = getTitleBar();
    addFragments();
    this.unusedCouponListFragment.hideFragmentTitleBar();
    this.orderFragment.hideFragmentTitleBar();
    showDefaultTitleBar();
  }

  protected void showCouponFragment()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.show(this.unusedCouponListFragment);
    localFragmentTransaction.hide(this.orderFragment);
    localFragmentTransaction.hide(this.meituanOrderListFragment);
    localFragmentTransaction.commitAllowingStateLoss();
  }

  protected void showDefaultTitleBar()
  {
    if (this.tabBarView == null)
      this.tabBarView = createTabView();
    this.mTitleBar.setLeftView(0, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TuanOrderTabActivity.this.onLeftTitleButtonClicked();
      }
    });
    this.mTitleBar.setCustomContentView(this.tabBarView);
    this.mTitleBar.addRightViewItem("remove", R.drawable.history_remove, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TuanOrderTabActivity.this.setIsEdit(true);
      }
    });
  }

  protected void showEditTitleBar()
  {
    this.mTitleBar.setLeftView(-1, null);
    View localView = LayoutInflater.from(this).inflate(R.layout.tuan_order_edit_title, null, false);
    this.mTitleBar.setCustomContentView(localView);
    localView = LayoutInflater.from(this).inflate(R.layout.tuan_order_right_btn, null, false);
    this.mTitleBar.addRightViewItem(localView, "remove", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TuanOrderTabActivity.this.setIsEdit(false);
      }
    });
  }

  protected void showMeiTuanOrderFragment()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.show(this.meituanOrderListFragment);
    localFragmentTransaction.hide(this.unusedCouponListFragment);
    localFragmentTransaction.hide(this.orderFragment);
    localFragmentTransaction.commitAllowingStateLoss();
  }

  protected void showOrderFragment()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.show(this.orderFragment);
    localFragmentTransaction.hide(this.unusedCouponListFragment);
    localFragmentTransaction.hide(this.meituanOrderListFragment);
    localFragmentTransaction.commitAllowingStateLoss();
  }

  protected void toggleRemoveBtn()
  {
    int k = 1;
    int j = 0;
    if ((this.mTitleBar != null) && (this.mTitleBar.findRightViewItemByTag("remove") != null))
    {
      i = k;
      if (this.tabBarView.getCurrentIndex() != 0)
        if (this.tabBarView.getCurrentIndex() != 1)
          break label76;
    }
    label76: for (int i = k; ; i = 0)
    {
      View localView = this.mTitleBar.findRightViewItemByTag("remove");
      if (i != 0)
        j = 8;
      localView.setVisibility(j);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanOrderTabActivity
 * JD-Core Version:    0.6.0
 */