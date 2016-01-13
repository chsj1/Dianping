package com.dianping.wed.baby.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.baby.fragment.WeddingPackageListFragment;
import com.dianping.wed.baby.fragment.WeddingProductBaseFragment;
import com.dianping.wed.baby.fragment.WeddingTravelPkgListFragment;

public class WeddingPackageListActivity extends WeddingNovaActivity
  implements View.OnClickListener, ShopListTabView.TabChangeListener
{
  WeddingProductBaseFragment currentFragment;
  FragmentManager fragmentManager;
  boolean isInitial = false;
  int productCategoryId;
  ShopListTabView shopListTabView;
  SparseArray<WeddingProductBaseFragment> sparseFragments;
  String tab;
  RelativeLayout titleBar;
  WeddingPackageListFragment weddingPackageListFragment;
  WeddingTravelPkgListFragment weddingTravelPkgListFragment;

  void initFragment(Bundle paramBundle)
  {
    this.fragmentManager = getSupportFragmentManager();
    this.productCategoryId = getIntParam("productcategoryid");
    this.tab = getStringParam("tab");
    if (this.weddingPackageListFragment == null)
    {
      this.weddingPackageListFragment = new WeddingPackageListFragment();
      paramBundle = new Bundle();
      paramBundle.putInt("productcategoryid", this.productCategoryId);
      this.weddingPackageListFragment.setArguments(paramBundle);
      this.fragmentManager.beginTransaction().add(R.id.content, this.weddingPackageListFragment, "packagelist").commit();
      this.sparseFragments.put(0, this.weddingPackageListFragment);
      this.currentFragment = this.weddingPackageListFragment;
    }
    this.shopListTabView = ((ShopListTabView)findViewById(R.id.tab_view));
    this.shopListTabView.setTabChangeListener(this);
    this.titleBar = ((RelativeLayout)findViewById(R.id.wedding_titlebar));
    this.titleBar.findViewById(R.id.left_view).setOnClickListener(this);
    showTitleBar();
    this.titleBar.setVisibility(8);
    setTitle("精选套餐");
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.left_view)
      onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.wed_packagelist_activity);
    this.sparseFragments = new SparseArray();
    initFragment(paramBundle);
  }

  public void onTabChanged(int paramInt)
  {
    if (this.currentFragment == this.sparseFragments.get(paramInt))
      return;
    if (this.currentFragment != null)
      this.fragmentManager.beginTransaction().hide(this.currentFragment).commit();
    this.fragmentManager.beginTransaction().show((Fragment)this.sparseFragments.get(paramInt)).commit();
    this.currentFragment = ((WeddingProductBaseFragment)this.sparseFragments.get(paramInt));
  }

  public void resetTitleBar(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null);
    do
      return;
    while ((paramArrayOfDPObject.length > 2) || (this.isInitial));
    this.isInitial = true;
    if (paramArrayOfDPObject.length == 2)
    {
      hideTitleBar();
      this.titleBar.setVisibility(0);
      this.shopListTabView.setLeftTitleText(paramArrayOfDPObject[0].getString("Name"));
      this.shopListTabView.setRightTitleText(paramArrayOfDPObject[1].getString("Name"));
      this.sparseFragments.clear();
      int i = 0;
      label80: Object localObject;
      int j;
      if (i < paramArrayOfDPObject.length)
      {
        if (paramArrayOfDPObject[i].getInt("Type") != 1)
          break label160;
        localObject = paramArrayOfDPObject[i].getString("ID");
        j = 0;
        if (TextUtils.isDigitsOnly((CharSequence)localObject))
          j = Integer.parseInt((String)localObject);
        if (j == this.productCategoryId)
          this.sparseFragments.put(i, this.weddingPackageListFragment);
        this.shopListTabView.setCurIndex(i);
      }
      while (true)
      {
        i += 1;
        break label80;
        break;
        label160: localObject = paramArrayOfDPObject[i].getString("ID");
        j = 0;
        if (TextUtils.isDigitsOnly((CharSequence)localObject))
          j = Integer.parseInt((String)localObject);
        if (this.weddingTravelPkgListFragment == null)
        {
          this.weddingTravelPkgListFragment = new WeddingTravelPkgListFragment();
          localObject = new Bundle();
          ((Bundle)localObject).putInt("productcategoryid", j);
          this.weddingTravelPkgListFragment.setArguments((Bundle)localObject);
          this.fragmentManager.beginTransaction().add(R.id.content, this.weddingTravelPkgListFragment, "packagelist").commit();
          this.fragmentManager.beginTransaction().hide(this.weddingTravelPkgListFragment).commit();
        }
        this.sparseFragments.put(i, this.weddingTravelPkgListFragment);
      }
    }
    if (paramArrayOfDPObject.length == 1)
    {
      showTitleBar();
      this.titleBar.setVisibility(8);
      setTitle(paramArrayOfDPObject[0].getString("Name"));
      return;
    }
    showTitleBar();
    this.titleBar.setVisibility(8);
    setTitle("");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WeddingPackageListActivity
 * JD-Core Version:    0.6.0
 */