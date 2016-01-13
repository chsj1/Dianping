package com.dianping.wed.weddingfeast.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.basic.TabPagerFragment.TabsAdapter;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.widget.WeddingShopPhotoGalleryFragment;
import com.dianping.wed.widget.WeddingShopPhotoUserGalleryFragment;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaRelativeLayout;

public class WeddingShopPhotoGalleryActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
{
  private static final int REQUEST_DRAFT = 129;
  View bookingView;
  ImageButton btnBack;
  ImageButton btnUploadPhoto;
  String defaultTabName;
  private DPObject dpObjShop;
  private DPObject dpWeddingShop;
  private TextView emptyTV;
  private DPObject hallItem;
  ProgressDialog loadingDialog;
  private int mCurTab;
  private boolean officialCreated;
  private TabPagerFragment officialFragment;
  private View officialView;
  private int shopId;
  MApiRequest shopRequest;
  private String[] tabNames;
  ShopListTabView titleTab;
  private boolean userCreated;
  private WeddingShopPhotoUserGalleryFragment userFragment;
  private View userView;

  private void changeTab()
  {
    if (this.mCurTab == 0)
    {
      if ((this.dpWeddingShop != null) && (this.dpWeddingShop.getInt("ImgCount") == 0))
      {
        this.officialView.setVisibility(8);
        this.userView.setVisibility(8);
        showEmptyView();
        return;
      }
      this.officialView.setVisibility(0);
      this.userView.setVisibility(8);
      hideEmptyView();
      setupOfficialTagsView();
      return;
    }
    hideEmptyView();
    this.officialView.setVisibility(8);
    this.userView.setVisibility(0);
    setupUserTagsView();
  }

  private int defaultTabIndex(DPObject[] paramArrayOfDPObject)
  {
    return 0;
  }

  private void hideEmptyView()
  {
    this.emptyTV = ((TextView)findViewById(R.id.official_empty_view));
    this.emptyTV.setVisibility(8);
  }

  private void setupView()
  {
    super.setContentView(R.layout.tab_pager_fragment_wedding);
    setTitleBar();
    hideEmptyView();
    this.officialFragment = ((TabPagerFragment)getSupportFragmentManager().findFragmentById(R.id.official_viewer));
    this.officialFragment.setOnTabChangeListener(this);
    this.userFragment = ((WeddingShopPhotoUserGalleryFragment)getSupportFragmentManager().findFragmentById(R.id.user_viewer));
    this.officialView = findViewById(R.id.official_layout);
    this.userView = findViewById(R.id.user_layout);
    this.bookingView = findViewById(R.id.booking_layout);
    View localView = findViewById(R.id.phone_view);
    ((NovaRelativeLayout)localView).setGAString("tel");
    TextView localTextView = (TextView)findViewById(R.id.phone_text);
    NovaButton localNovaButton = (NovaButton)findViewById(R.id.booking_btn);
    localNovaButton.setGAString("wedbooking");
    if ((this.dpObjShop != null) && (localView != null) && (localTextView != null))
    {
      String[] arrayOfString = this.dpObjShop.getStringArray("PhoneNos");
      if ((arrayOfString != null) && (arrayOfString.length > 0))
      {
        localTextView.setText(arrayOfString[0]);
        localView.setOnClickListener(new View.OnClickListener(arrayOfString)
        {
          public void onClick(View paramView)
          {
            GAHelper.instance().contextStatisticsEvent(WeddingShopPhotoGalleryActivity.this.getApplicationContext(), "phone", WeddingShopPhotoGalleryActivity.this.gaExtra, "tap");
            TelephoneUtils.dial(WeddingShopPhotoGalleryActivity.this.getApplicationContext(), WeddingShopPhotoGalleryActivity.this.dpObjShop, this.val$phoneNos[0]);
          }
        });
      }
    }
    if ((localNovaButton != null) && (this.dpObjShop != null))
      localNovaButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddingfeastbooking").buildUpon().appendQueryParameter("shopid", String.valueOf(WeddingShopPhotoGalleryActivity.this.dpObjShop().getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(WeddingShopPhotoGalleryActivity.this.dpObjShop)).build().toString()));
          WeddingShopPhotoGalleryActivity.this.startActivity(paramView);
        }
      });
  }

  private void showEmptyView()
  {
    this.emptyTV = ((TextView)findViewById(R.id.official_empty_view));
    Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
    this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    this.emptyTV.setText(Html.fromHtml("这家商户太懒了，什么图片都没传..."));
    this.emptyTV.setVisibility(0);
  }

  public DPObject dpObjShop()
  {
    return this.dpObjShop;
  }

  public int getShopId()
  {
    return this.shopId;
  }

  protected Class getWeddingPhotoGalleryFragment()
  {
    return WeddingShopPhotoGalleryFragment.class;
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.toupload)
    {
      if (this.dpObjShop == null)
        break label38;
      UploadPhotoUtil.uploadShopPhoto(this, this.dpObjShop);
    }
    while (true)
    {
      if (paramView == this.btnBack)
        finish();
      return;
      label38: UploadPhotoUtil.uploadShopPhoto(this, this.shopId);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.dpObjShop = ((DPObject)paramBundle.getParcelable("dpObjShop"));
      this.dpWeddingShop = ((DPObject)paramBundle.getParcelable("dpWeddingShop"));
      this.shopId = paramBundle.getInt("shopId");
      this.defaultTabName = paramBundle.getString("defaultTabName");
      this.hallItem = ((DPObject)paramBundle.getParcelable("hallItem"));
      setupView();
      if (this.dpObjShop != null)
        this.shopId = this.dpObjShop.getInt("ID");
      if ((this.dpWeddingShop != null) && (this.dpWeddingShop.getInt("CooperateType") == 2))
        this.bookingView.setVisibility(8);
      if (this.hallItem == null)
        break label405;
      paramBundle = null;
      if (this.dpWeddingShop == null);
    }
    while (true)
    {
      int i;
      try
      {
        paramBundle = this.dpWeddingShop.getArray("OfficialTags");
        i = 0;
        if (i >= paramBundle.length)
          break label405;
        Object localObject = paramBundle[i];
        if (this.hallItem.getInt("ID") == localObject.getInt("TagID"))
        {
          this.mCurTab = 0;
          changeTab();
          this.officialFragment.tabHost().setCurrentTab(i);
          this.officialFragment.tabsAdapter().notifyDataSetChanged();
          return;
          paramBundle = getIntent();
          this.defaultTabName = paramBundle.getData().getQueryParameter("tabname");
          this.dpObjShop = ((DPObject)paramBundle.getParcelableExtra("objShop"));
          this.dpWeddingShop = ((DPObject)paramBundle.getParcelableExtra("extraWeddingShop"));
          this.hallItem = ((DPObject)paramBundle.getParcelableExtra("hallItem"));
          if (this.dpObjShop != null)
            break;
          if (paramBundle.getData() == null)
            continue;
          this.shopId = paramBundle.getIntExtra("shopId", 0);
          if (this.shopId <= 0)
            continue;
          sendShopRequest(this.shopId);
          this.loadingDialog = new ProgressDialog(this);
          this.loadingDialog.setMessage("正在加载...");
          this.loadingDialog.show();
          return;
          paramBundle = paramBundle.getData().getQueryParameter("id");
          try
          {
            this.shopId = Integer.parseInt(paramBundle);
            if (this.shopId <= 0)
              continue;
            sendShopRequest(this.shopId);
            return;
          }
          catch (java.lang.NumberFormatException paramBundle)
          {
            finish();
            return;
          }
          finish();
          return;
        }
      }
      catch (Exception paramBundle)
      {
        return;
      }
      i += 1;
    }
    label405: if ((this.dpWeddingShop != null) && (this.dpWeddingShop.getInt("ImgCount") == 0) && (this.dpObjShop != null) && (this.dpObjShop.getInt("PicCount") > 0))
    {
      this.mCurTab = 1;
      this.titleTab.setCurIndex(this.mCurTab);
    }
    while (true)
    {
      changeTab();
      return;
      this.mCurTab = 0;
    }
  }

  public void onPageScrollStateChanged(int paramInt)
  {
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      if (this.loadingDialog != null)
        this.loadingDialog.dismiss();
      Toast.makeText(this, "暂时无法获取商户图片数据", 1).show();
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest);
    try
    {
      this.dpObjShop = ((DPObject)paramMApiResponse.result());
      if (this.dpObjShop == null)
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

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("dpObjShop", this.dpObjShop);
    paramBundle.putParcelable("dpWeddingShop", this.dpWeddingShop);
    paramBundle.putParcelable("hallItem", this.hallItem);
    paramBundle.putInt("shopId", this.shopId);
    paramBundle.putString("defaultTabName", this.defaultTabName);
  }

  public void onTabChanged(String paramString)
  {
    if ((this.tabNames != null) && (this.tabNames.length > 0))
    {
      int i = 0;
      while (i < this.tabNames.length)
      {
        if ((this.tabNames[i] != null) && (this.tabNames[i].equals(paramString)))
        {
          GAUserInfo localGAUserInfo = new GAUserInfo();
          localGAUserInfo.shop_id = Integer.valueOf(this.shopId);
          localGAUserInfo.index = Integer.valueOf(i);
          GAHelper.instance().contextStatisticsEvent(getApplicationContext(), "tag", localGAUserInfo, "tap");
        }
        i += 1;
      }
    }
  }

  public void sendShopRequest(int paramInt)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/shop.bin?");
    localStringBuffer.append("shopid=").append(paramInt);
    this.shopRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.shopRequest, this);
  }

  public void setTitleBar()
  {
    super.hideTitleBar();
    findViewById(R.id.wedding_titlebar).setVisibility(0);
    this.btnBack = ((ImageButton)findViewById(R.id.left_view));
    this.btnBack.setVisibility(0);
    this.btnBack.setOnClickListener(this);
    this.btnUploadPhoto = ((ImageButton)findViewById(R.id.toupload));
    this.btnUploadPhoto.setImageResource(R.drawable.navibar_icon_addpic);
    this.btnUploadPhoto.setVisibility(0);
    this.btnUploadPhoto.setOnClickListener(this);
    this.titleText = ((TextView)findViewById(R.id.weddingtitle));
    this.titleTab = ((ShopListTabView)findViewById(R.id.tab_view));
    this.titleTab.setTabChangeListener(new ShopListTabView.TabChangeListener()
    {
      public void onTabChanged(int paramInt)
      {
        if (WeddingShopPhotoGalleryActivity.this.mCurTab != paramInt)
        {
          WeddingShopPhotoGalleryActivity.access$002(WeddingShopPhotoGalleryActivity.this, paramInt);
          WeddingShopPhotoGalleryActivity.this.changeTab();
          if (WeddingShopPhotoGalleryActivity.this.gaExtra.shop_id == null)
            WeddingShopPhotoGalleryActivity.this.gaExtra.shop_id = Integer.valueOf(WeddingShopPhotoGalleryActivity.this.shopId);
          GAHelper.instance().contextStatisticsEvent(WeddingShopPhotoGalleryActivity.this.getApplicationContext(), "type", WeddingShopPhotoGalleryActivity.this.gaExtra, "tap");
        }
      }
    });
  }

  public void setupOfficialTagsView()
  {
    if (this.officialCreated)
      return;
    if (this.dpWeddingShop == null)
    {
      Log.e("the wedding extra info is null ! ");
      return;
    }
    DPObject[] arrayOfDPObject1 = null;
    if (this.dpWeddingShop != null);
    try
    {
      arrayOfDPObject1 = this.dpWeddingShop.getArray("OfficialTags");
      DPObject[] arrayOfDPObject2 = arrayOfDPObject1;
      if (arrayOfDPObject1 == null)
        arrayOfDPObject2 = new DPObject[0];
      this.tabNames = new String[arrayOfDPObject2.length];
      int i = 0;
      while (true)
      {
        if (i < arrayOfDPObject2.length)
        {
          arrayOfDPObject1 = arrayOfDPObject2[i];
          if (arrayOfDPObject1 != null)
            try
            {
              Bundle localBundle = new Bundle();
              localBundle.putInt("shopId", this.shopId);
              localBundle.putString("Title", arrayOfDPObject1.getString("Title"));
              localBundle.putInt("Type", arrayOfDPObject1.getInt("Type"));
              localBundle.putInt("TagID", arrayOfDPObject1.getInt("TagID"));
              this.officialFragment.addTab(arrayOfDPObject1.getString("Title"), R.layout.shop_photo_tab_indicator, getWeddingPhotoGalleryFragment(), localBundle);
              this.tabNames[i] = arrayOfDPObject1.getString("Title");
            }
            catch (Exception localException1)
            {
              localException1.printStackTrace();
            }
        }
        else
        {
          if (arrayOfDPObject2.length > 0)
            this.officialFragment.tabHost().setCurrentTab(defaultTabIndex(arrayOfDPObject2));
          if (arrayOfDPObject2.length <= 0)
            this.officialFragment.tabHost().findViewById(16908307).setVisibility(8);
          while (true)
          {
            this.officialCreated = true;
            this.officialFragment.tabsAdapter().setTabViewWrapContent(true);
            this.officialFragment.tabsAdapter().notifyDataSetChanged();
            return;
            this.officialFragment.tabHost().findViewById(16908307).setVisibility(0);
          }
        }
        i += 1;
      }
    }
    catch (Exception localException2)
    {
    }
  }

  public void setupUserTagsView()
  {
    if (this.userCreated);
    while (true)
    {
      return;
      if (this.dpObjShop == null)
      {
        Log.e("the shop info is null ! ");
        return;
      }
      try
      {
        DPObject[] arrayOfDPObject = this.dpObjShop.getArray("ShopPhotoCategory");
        if ((arrayOfDPObject == null) || (arrayOfDPObject.length <= 0))
          continue;
        int i = 0;
        while (i < arrayOfDPObject.length)
        {
          DPObject localDPObject = arrayOfDPObject[i];
          Bundle localBundle = new Bundle();
          localBundle.putInt("shopId", this.shopId);
          localBundle.putString("cateName", localDPObject.getString("Name"));
          localBundle.putInt("type", localDPObject.getInt("Type"));
          localBundle.putInt("filter", 0);
          localBundle.putInt("shopType", this.dpObjShop.getInt("ShopType"));
          this.userFragment.setArgument(localBundle);
          i += 1;
        }
      }
      catch (Exception localException)
      {
        return;
      }
    }
    this.userCreated = true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.activity.WeddingShopPhotoGalleryActivity
 * JD-Core Version:    0.6.0
 */