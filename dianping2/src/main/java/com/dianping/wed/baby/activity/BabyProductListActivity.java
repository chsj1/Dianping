package com.dianping.wed.baby.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.ugc.photo.ShopPhotoGalleryFragment;
import com.dianping.base.ugc.photo.ShopPhotoGalleryFragment.PhotoGalleryShop;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.baby.fragment.WeddingShopProductPhotoGalleryFragment;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;

public class BabyProductListActivity extends NovaActivity
  implements View.OnClickListener, LoginResultListener, RequestHandler<MApiRequest, MApiResponse>, ShopPhotoGalleryFragment.PhotoGalleryShop
{
  private static final String All_PARAM = "全部";
  private ImageButton btnBack;
  private DPObject[] categories;
  private String categoryDesc;
  private boolean hideLeft = false;
  private boolean isEmptySource = false;
  private WeddingShopProductPhotoGalleryFragment leftFragment;
  private View leftPhotoView;
  ProgressDialog loadingDialog;
  int mCurTab;
  private String productCategoryId;
  private TabPagerFragment rightFragment;
  private View rightPhotoView;
  private DPObject shop;
  private int shopId;
  MApiRequest shopRequest;
  private ShopListTabView titleTab;
  private ImageButton uploadPhoto;
  private boolean userPhotoViewInit = false;

  private void sendShopRequest(int paramInt)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/shop.bin?");
    localStringBuffer.append("shopid=").append(paramInt);
    this.shopRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.shopRequest, this);
  }

  private void setTitleBar()
  {
    hideTitleBar();
    findViewById(R.id.wedding_titlebar).setVisibility(0);
    this.btnBack = ((ImageButton)findViewById(R.id.left_view));
    this.btnBack.setVisibility(0);
    this.btnBack.setOnClickListener(this);
    this.uploadPhoto = ((ImageButton)findViewById(R.id.toupload));
    this.uploadPhoto.setVisibility(8);
    this.uploadPhoto.setOnClickListener(this);
    if (TextUtils.isEmpty(this.categoryDesc))
      this.categoryDesc = "产品";
    if (!this.hideLeft)
    {
      String str = "全部" + this.categoryDesc;
      ((TextView)findViewById(R.id.title1)).setText(str);
    }
    while (true)
    {
      this.titleTab = ((ShopListTabView)findViewById(R.id.tab_view));
      this.titleTab.setCurIndex(this.mCurTab);
      this.titleTab.setTabChangeListener(new ShopListTabView.TabChangeListener()
      {
        public void onTabChanged(int paramInt)
        {
          if (BabyProductListActivity.this.mCurTab == paramInt)
            return;
          BabyProductListActivity.this.mCurTab = paramInt;
          BabyProductListActivity.this.changeTab();
        }
      });
      return;
      ((ShopListTabView)findViewById(R.id.tab_view)).setVisibility(8);
      ((LinearLayout)findViewById(R.id.weddingtitlebar)).setVisibility(0);
    }
  }

  private void setupShopPhotoView()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("shopId", this.shopId);
    localBundle.putString("cateName", "全部");
    localBundle.putInt("photoType", 1);
    localBundle.putString("productCategoryId", this.productCategoryId);
    this.shop = ((DPObject)getIntent().getParcelableExtra("shop"));
    if (this.shop != null)
      localBundle.putParcelable("shop", this.shop);
    if (TextUtils.isEmpty(this.categoryDesc))
      localBundle.putString("categoryDesc", this.categoryDesc);
    localBundle.putBoolean("isEmptySource", this.isEmptySource);
    this.leftFragment.setArgument(localBundle);
  }

  private void setupUserPhotoView()
  {
    if (this.userPhotoViewInit)
      return;
    Object localObject;
    if ((this.categories == null) || (this.categories.length <= 0))
    {
      localObject = new Bundle();
      ((Bundle)localObject).putInt("shopId", this.shopId);
      ((Bundle)localObject).putString("cateName", "全部");
      ((Bundle)localObject).putInt("photoType", 1);
      ((Bundle)localObject).putInt("type", 1);
      this.rightFragment.addTab("", R.layout.shop_photo_tab_indicator, ShopPhotoGalleryFragment.class, (Bundle)localObject);
    }
    while (true)
    {
      if ((this.categories == null) || (this.categories.length <= 1))
        findViewById(16908307).setVisibility(8);
      this.userPhotoViewInit = true;
      return;
      int i = 0;
      while (i < this.categories.length)
      {
        localObject = this.categories[i];
        Bundle localBundle = new Bundle();
        localBundle.putInt("shopId", this.shopId);
        localBundle.putString("cateName", ((DPObject)localObject).getString("Name"));
        localBundle.putInt("type", ((DPObject)localObject).getInt("Type"));
        this.rightFragment.addTab(((DPObject)localObject).getString("Name"), R.layout.shop_photo_tab_indicator, ShopPhotoGalleryFragment.class, localBundle);
        i += 1;
      }
    }
  }

  private void setupView()
  {
    if (this.shop == null)
      return;
    this.categories = this.shop.getArray("ShopPhotoCategory");
    if (this.isEmptySource)
    {
      this.mCurTab = 1;
      this.hideLeft = true;
    }
    while (true)
    {
      super.setContentView(R.layout.tab_pager_fragment_wedding_shop);
      setTitleBar();
      this.leftFragment = ((WeddingShopProductPhotoGalleryFragment)getSupportFragmentManager().findFragmentById(R.id.left_viewer));
      this.leftPhotoView = findViewById(R.id.left_photo_view);
      this.rightFragment = ((TabPagerFragment)getSupportFragmentManager().findFragmentById(R.id.right_viewer));
      this.rightPhotoView = findViewById(R.id.right_photo_view);
      changeTab();
      return;
      this.mCurTab = 0;
    }
  }

  void changeTab()
  {
    GAUserInfo localGAUserInfo = getCloneUserInfo();
    localGAUserInfo.shop_id = Integer.valueOf(this.shopId);
    if (this.shop != null)
      localGAUserInfo.category_id = Integer.valueOf(this.shop.getInt("CategoryId"));
    if (this.mCurTab == 0)
    {
      this.leftPhotoView.setVisibility(0);
      this.rightPhotoView.setVisibility(8);
      this.uploadPhoto.setVisibility(8);
      setupShopPhotoView();
      GAHelper.instance().contextStatisticsEvent(this, "producttab_product", localGAUserInfo, "tap");
      return;
    }
    this.leftPhotoView.setVisibility(8);
    this.rightPhotoView.setVisibility(0);
    this.uploadPhoto.setVisibility(0);
    setupUserPhotoView();
    GAHelper.instance().contextStatisticsEvent(this, "producttab_memalbum", localGAUserInfo, "tap");
  }

  public DPObject getShop()
  {
    return this.shop;
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnBack)
    {
      finish();
      return;
    }
    if (this.shop != null)
    {
      UploadPhotoUtil.uploadShopPhoto(this, this.shop);
      return;
    }
    UploadPhotoUtil.uploadShopPhoto(this, this.shopId);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    String str;
    if (paramBundle != null)
    {
      str = paramBundle.getQueryParameter("id");
      this.productCategoryId = paramBundle.getQueryParameter("productCategoryId");
      if (TextUtils.isEmpty(str))
        this.shopId = -1;
    }
    try
    {
      this.shopId = Integer.parseInt(str);
      if (this.shopId < 0)
      {
        finish();
        return;
      }
    }
    catch (java.lang.NumberFormatException paramBundle)
    {
      while (true)
        this.shopId = -1;
      this.shop = ((DPObject)getIntent().getParcelableExtra("shop"));
      this.categoryDesc = getIntent().getStringExtra("categoryDesc");
      this.isEmptySource = getIntent().getBooleanExtra("isEmptySource", false);
      if (this.shop == null)
      {
        sendShopRequest(this.shopId);
        this.loadingDialog = new ProgressDialog(this);
        this.loadingDialog.setMessage("正在加载...");
        this.loadingDialog.show();
        return;
      }
      setupView();
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.shopRequest != null)
      mapiService().abort(this.shopRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest != this.shopRequest)
      return;
    if (this.loadingDialog != null)
      this.loadingDialog.dismiss();
    Toast.makeText(this, "暂时无法获取商户图片数据", 1).show();
    finish();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest != this.shopRequest)
      return;
    try
    {
      this.shop = ((DPObject)paramMApiResponse.result());
      if (this.shop == null)
      {
        Toast.makeText(this, "暂时无法获取商户图片数据", 1).show();
        finish();
        return;
      }
    }
    catch (Exception paramMApiRequest)
    {
      while (true)
        paramMApiRequest.printStackTrace();
      if (this.loadingDialog != null)
        this.loadingDialog.dismiss();
      setupView();
    }
  }

  public void updateTitleByDesc(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    this.categoryDesc = paramString;
    paramString = "全部" + this.categoryDesc;
    ((TextView)findViewById(R.id.title1)).setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.BabyProductListActivity
 * JD-Core Version:    0.6.0
 */