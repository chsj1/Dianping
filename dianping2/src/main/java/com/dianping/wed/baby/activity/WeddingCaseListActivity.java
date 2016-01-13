package com.dianping.wed.baby.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.ugc.photo.ShopPhotoGalleryFragment;
import com.dianping.base.ugc.photo.ShopPhotoGalleryFragment.PhotoGalleryShop;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.baby.fragment.WeddingCaseListFragment;
import com.dianping.wed.baby.fragment.WeddingTravelCaseListFragment;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaTextView;

public class WeddingCaseListActivity extends WeddingNovaActivity
  implements ShopListTabView.TabChangeListener, View.OnClickListener, ShopPhotoGalleryFragment.PhotoGalleryShop
{
  NovaFragment currentFragment;
  FragmentManager fragmentManager;
  boolean isBeautyShop = false;
  boolean isInitTabPager;
  boolean isInitial = false;
  CustomImageButton leftImageButton;
  int officalTypeId;
  int productCategoryId;
  int shopId;
  ShopListTabView shopListTabView;
  DPObject shopObject;
  SparseArray<NovaFragment> sparseFragments;
  TabPagerFragment tabPagerFragment;
  WeddingCaseListFragment weddingCaseListFragment;
  WeddingTravelCaseListFragment weddingTravelCaseListFragment;

  private void setTabIndex(int paramInt)
  {
    if (this.currentFragment != null)
      this.fragmentManager.beginTransaction().hide(this.currentFragment).commit();
    if (paramInt == 0)
      if (this.sparseFragments.get(0) != null)
      {
        this.fragmentManager.beginTransaction().show((Fragment)this.sparseFragments.get(0)).commit();
        this.currentFragment = ((NovaFragment)this.sparseFragments.get(0));
      }
    label208: 
    do
    {
      return;
      if (paramInt != 1)
        continue;
      Object localObject1;
      if (!this.isInitTabPager)
      {
        localObject1 = null;
        if (this.shopObject != null)
          localObject1 = this.shopObject.getArray("ShopPhotoCategory");
        if ((localObject1 != null) && (localObject1.length > 0))
          break label208;
        localObject1 = new Bundle();
        ((Bundle)localObject1).putInt("shopId", this.shopId);
        ((Bundle)localObject1).putString("cateName", "全部");
        ((Bundle)localObject1).putInt("photoType", 1);
        ((Bundle)localObject1).putInt("type", 1);
        this.tabPagerFragment.addTab("", R.layout.shop_photo_tab_indicator, ShopPhotoGalleryFragment.class, (Bundle)localObject1);
      }
      while (true)
      {
        this.isInitTabPager = true;
        this.fragmentManager.beginTransaction().show(this.tabPagerFragment).commit();
        this.currentFragment = this.tabPagerFragment;
        return;
        paramInt = 0;
        while (paramInt < localObject1.length)
        {
          Object localObject2 = localObject1[paramInt];
          Bundle localBundle = new Bundle();
          localBundle.putInt("shopId", this.shopId);
          localBundle.putString("cateName", localObject2.getString("Name"));
          localBundle.putInt("type", localObject2.getInt("Type"));
          this.tabPagerFragment.addTab(localObject2.getString("Name"), R.layout.shop_photo_tab_indicator, ShopPhotoGalleryFragment.class, localBundle);
          paramInt += 1;
        }
      }
    }
    while ((paramInt != 2) || (this.sparseFragments.get(1) == null));
    this.fragmentManager.beginTransaction().show((Fragment)this.sparseFragments.get(1)).commit();
    this.currentFragment = ((NovaFragment)this.sparseFragments.get(1));
  }

  public DPObject getShop()
  {
    if (this.shopObject == null)
      return this.weddingCaseListFragment.getShop();
    return this.shopObject;
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.left_view)
      onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.wed_caselist_activity);
    hideTitleBar();
    if (getIntent() != null)
    {
      this.shopId = getIntParam("shopid");
      this.shopObject = getObjectParam("shop");
      this.productCategoryId = getIntParam("productcategoryid");
    }
    if (paramBundle != null)
    {
      this.shopId = paramBundle.getInt("shopid");
      this.shopObject = ((DPObject)paramBundle.getParcelable("shop"));
      this.productCategoryId = getIntParam("productcategoryid");
      this.isBeautyShop = paramBundle.getBoolean("isBeautyShop", false);
      this.officalTypeId = paramBundle.getInt("officalTypeId", 0);
    }
    if ((this.shopObject != null) && (paramBundle == null))
    {
      if (1 == getIntParam("beautykey"))
      {
        this.isBeautyShop = true;
        this.shopId = getIntParam("id");
      }
      this.officalTypeId = getIntParam("officialtypeid", 0);
    }
    if (this.officalTypeId != -1)
    {
      this.fragmentManager = getSupportFragmentManager();
      this.sparseFragments = new SparseArray();
      this.tabPagerFragment = ((TabPagerFragment)this.fragmentManager.findFragmentByTag("tabpager"));
      this.fragmentManager.beginTransaction().hide(this.tabPagerFragment).commit();
      this.weddingCaseListFragment = ((WeddingCaseListFragment)this.fragmentManager.findFragmentByTag("caselist"));
      this.weddingCaseListFragment.setProductCategoryId(this.productCategoryId);
      this.weddingCaseListFragment.setIsBeautyShop(this.isBeautyShop);
      this.weddingCaseListFragment.setOfficalTypeId(this.officalTypeId);
      this.fragmentManager.beginTransaction().show(this.weddingCaseListFragment).commit();
      this.sparseFragments.put(0, this.weddingCaseListFragment);
      this.shopListTabView = ((ShopListTabView)findViewById(R.id.tab_view));
      this.shopListTabView.setTabChangeListener(this);
      this.currentFragment = this.weddingCaseListFragment;
      this.leftImageButton = ((CustomImageButton)findViewById(R.id.left_view));
      this.leftImageButton.setOnClickListener(this);
      return;
    }
    this.fragmentManager = getSupportFragmentManager();
    this.sparseFragments = new SparseArray();
    this.weddingCaseListFragment = ((WeddingCaseListFragment)this.fragmentManager.findFragmentByTag("caselist"));
    this.weddingCaseListFragment.setIsBeautyShop(this.isBeautyShop);
    this.weddingCaseListFragment.setOfficalTypeId(this.officalTypeId);
    this.fragmentManager.beginTransaction().hide(this.weddingCaseListFragment).commit();
    this.sparseFragments.put(0, this.weddingCaseListFragment);
    this.shopListTabView = ((ShopListTabView)findViewById(R.id.tab_view));
    this.shopListTabView.setTabChangeListener(this);
    this.shopListTabView.setCurIndex(1);
    this.leftImageButton = ((CustomImageButton)findViewById(R.id.left_view));
    this.leftImageButton.setOnClickListener(this);
    this.tabPagerFragment = ((TabPagerFragment)this.fragmentManager.findFragmentByTag("tabpager"));
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopid", this.shopId);
    paramBundle.putParcelable("shopid", this.shopObject);
    paramBundle.putInt("productcategoryid", this.productCategoryId);
    paramBundle.putBoolean("isBeautyShop", this.isBeautyShop);
    paramBundle.putInt("officalTypeId", this.weddingCaseListFragment.getOfficalTypeId());
  }

  public void onTabChanged(int paramInt)
  {
    setTabIndex(paramInt);
    GAUserInfo localGAUserInfo = getCloneUserInfo();
    localGAUserInfo.index = Integer.valueOf(paramInt);
    localGAUserInfo.shop_id = Integer.valueOf(this.shopId);
    GAHelper.instance().contextStatisticsEvent(this, "casetab", localGAUserInfo, "tap");
    if (this.isBeautyShop)
    {
      if (paramInt != 0)
        break label71;
      ((NovaTextView)findViewById(R.id.title1)).setGAString("shop_photo_tab");
    }
    label71: 
    do
      return;
    while (1 != paramInt);
    ((NovaTextView)findViewById(R.id.title2)).setGAString("user_photo_tab");
  }

  public void resetTitleBar(DPObject[] paramArrayOfDPObject)
  {
    if (this.isInitial);
    label58: 
    do
    {
      return;
      this.isInitial = true;
      if (paramArrayOfDPObject.length != 2)
        continue;
      this.shopListTabView.setLeftTitleText(paramArrayOfDPObject[0].getString("Name"));
      this.shopListTabView.setMidTitleText(paramArrayOfDPObject[1].getString("Name"));
      this.sparseFragments.clear();
      int i = 0;
      Object localObject;
      int j;
      if (i < paramArrayOfDPObject.length)
      {
        if (paramArrayOfDPObject[i].getInt("Type") != 1)
          break label147;
        localObject = paramArrayOfDPObject[i].getString("ID");
        j = 0;
        if (TextUtils.isDigitsOnly((CharSequence)localObject))
          j = Integer.parseInt((String)localObject);
        if (j == this.productCategoryId)
          this.sparseFragments.put(i, this.weddingCaseListFragment);
        if (i != 0)
          break label139;
        setTabIndex(0);
      }
      while (true)
      {
        i += 1;
        break label58;
        break;
        setTabIndex(2);
        continue;
        localObject = paramArrayOfDPObject[i].getString("ID");
        j = 0;
        if (TextUtils.isDigitsOnly((CharSequence)localObject))
          j = Integer.parseInt((String)localObject);
        if (this.weddingTravelCaseListFragment == null)
        {
          this.weddingTravelCaseListFragment = new WeddingTravelCaseListFragment();
          localObject = new Bundle();
          ((Bundle)localObject).putInt("productcategoryid", j);
          this.weddingTravelCaseListFragment.setArguments((Bundle)localObject);
          this.fragmentManager.beginTransaction().add(R.id.content, this.weddingTravelCaseListFragment, "packagelist").commit();
          this.fragmentManager.beginTransaction().hide(this.weddingTravelCaseListFragment).commit();
        }
        this.sparseFragments.put(i, this.weddingTravelCaseListFragment);
      }
    }
    while (paramArrayOfDPObject.length != 1);
    label139: label147: this.shopListTabView.setLeftTitleText(paramArrayOfDPObject[0].getString("Name"));
    this.shopListTabView.setMidTitleText("");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WeddingCaseListActivity
 * JD-Core Version:    0.6.0
 */