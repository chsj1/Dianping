package com.dianping.wed.weddingfeast.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.basic.TabPagerFragment.TabsAdapter;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.weddingfeast.fragment.WeddingFeastMenuGalleryFragment;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaRelativeLayout;

public class WeddingFeastMenuGalleryActivity extends NovaActivity
  implements View.OnClickListener, LoginResultListener, RequestHandler<MApiRequest, MApiResponse>, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
{
  private static final int REQUEST_DRAFT = 129;
  View bookingView;
  ImageButton btnBack;
  private DPObject dpObjShop;
  private DPObject extra;
  MApiRequest extraRequest;
  ProgressDialog loadingDialog;
  private int mCurTab;
  private boolean menuCreated;
  private TabPagerFragment menuFragment;
  private DPObject[] menuTags;
  private View menuView;
  private int shopId = 16990551;
  MApiRequest shopRequest;
  private String[] tabNames;

  private void changeTab()
  {
    if ((this.extra == null) || (this.dpObjShop == null))
      return;
    this.menuView.setVisibility(0);
    setupMenuTagsView();
  }

  private void getExtra()
  {
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelextra.bin?");
    localStringBuffer.append("shopid=").append(this.shopId);
    this.extraRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    mapiService().exec(this.extraRequest, this);
  }

  private void sendShopRequest(int paramInt)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/shop.bin?");
    localStringBuffer.append("shopid=").append(paramInt);
    this.shopRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.shopRequest, this);
  }

  private void setupView()
  {
    if ((this.extra == null) || (this.dpObjShop == null));
    NovaButton localNovaButton;
    do
    {
      return;
      super.setContentView(R.layout.tab_menu_fragment_wedding);
      this.menuView = findViewById(R.id.menu_layout);
      this.menuFragment = ((TabPagerFragment)getSupportFragmentManager().findFragmentById(R.id.menu_viewer));
      this.menuFragment.setOnTabChangeListener(this);
      this.menuFragment.tabHost().findViewById(16908307).setBackgroundDrawable(null);
      this.bookingView = findViewById(R.id.booking_layout);
      if (this.extra.getInt("CooperateType") != 1)
      {
        this.bookingView.setVisibility(8);
        return;
      }
      View localView = findViewById(R.id.phone_view);
      ((NovaRelativeLayout)localView).setGAString("tel");
      ((NovaRelativeLayout)localView).setGAString("phone");
      TextView localTextView = (TextView)findViewById(R.id.phone_text);
      localNovaButton = (NovaButton)findViewById(R.id.booking_btn);
      localNovaButton.setGAString("wedbooking");
      if ((this.dpObjShop == null) || (localView == null) || (localTextView == null))
        continue;
      String[] arrayOfString = this.dpObjShop.getStringArray("PhoneNos");
      if ((arrayOfString == null) || (arrayOfString.length <= 0))
        continue;
      localTextView.setText(arrayOfString[0]);
      localView.setOnClickListener(new View.OnClickListener(arrayOfString)
      {
        public void onClick(View paramView)
        {
          TelephoneUtils.dial(WeddingFeastMenuGalleryActivity.this.getApplicationContext(), WeddingFeastMenuGalleryActivity.this.dpObjShop, this.val$phoneNos[0]);
        }
      });
    }
    while ((localNovaButton == null) || (this.dpObjShop == null));
    localNovaButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddinghotelbooking").buildUpon().appendQueryParameter("shopid", String.valueOf(WeddingFeastMenuGalleryActivity.this.dpObjShop.getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(WeddingFeastMenuGalleryActivity.this.dpObjShop)).build().toString()));
        WeddingFeastMenuGalleryActivity.this.startActivity(paramView);
      }
    });
  }

  protected boolean isActionBarEnabled()
  {
    return false;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 129) && (paramInt2 == -1))
      startActivity(paramIntent);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnBack)
      finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.dpObjShop = ((DPObject)paramBundle.getParcelable("dpObjShop"));
      this.shopId = paramBundle.getInt("shopId");
      this.extra = ((DPObject)paramBundle.getParcelable("extra"));
    }
    label306: label348: for (this.menuTags = this.extra.getArray("MenuTags"); ; this.menuTags = this.extra.getArray("MenuTags"))
    {
      setupView();
      changeTab();
      return;
      paramBundle = getIntent();
      this.dpObjShop = ((DPObject)paramBundle.getParcelableExtra("objShop"));
      this.extra = ((DPObject)paramBundle.getParcelableExtra("extra"));
      if (this.dpObjShop == null)
        if (paramBundle.getData() != null)
        {
          this.shopId = paramBundle.getIntExtra("shopId", 0);
          if (this.shopId > 0)
            sendShopRequest(this.shopId);
        }
        else
        {
          this.loadingDialog = new ProgressDialog(this);
          this.loadingDialog.setMessage("正在加载...");
          this.loadingDialog.show();
        }
      while (true)
      {
        while (true)
        {
          if (this.extra != null)
            break label348;
          if (paramBundle.getData() != null)
          {
            this.shopId = paramBundle.getIntExtra("shopId", 0);
            if (this.shopId <= 0)
              break label306;
            getExtra();
          }
          this.loadingDialog = new ProgressDialog(this);
          this.loadingDialog.setMessage("正在加载...");
          this.loadingDialog.show();
          return;
          String str = paramBundle.getData().getQueryParameter("shopid");
          try
          {
            this.shopId = Integer.parseInt(str);
            if (this.shopId <= 0)
              break;
            sendShopRequest(this.shopId);
          }
          catch (java.lang.NumberFormatException paramBundle)
          {
            finish();
            return;
          }
        }
        finish();
        return;
        this.shopId = this.dpObjShop.getInt("ID");
      }
      paramBundle = paramBundle.getData().getQueryParameter("shopid");
      try
      {
        this.shopId = Integer.parseInt(paramBundle);
        if (this.shopId > 0)
        {
          getExtra();
          return;
        }
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
      Toast.makeText(this, "暂时无法获取商户菜单数据", 1).show();
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest);
    do
      try
      {
        this.dpObjShop = ((DPObject)paramMApiResponse.result());
        if (this.dpObjShop == null)
        {
          Toast.makeText(this, "暂时无法获取商户菜单数据", 1).show();
          finish();
          return;
        }
      }
      catch (Exception localException)
      {
        while (true)
          localException.printStackTrace();
        if (this.loadingDialog != null)
          this.loadingDialog.dismiss();
        setupView();
        changeTab();
      }
    while (paramMApiRequest != this.extraRequest);
    try
    {
      this.extra = ((DPObject)paramMApiResponse.result());
      if (this.extra == null)
      {
        Toast.makeText(this, "暂时无法获取商户菜单数据", 1).show();
        finish();
        return;
      }
    }
    catch (Exception paramMApiRequest)
    {
      while (true)
        paramMApiRequest.printStackTrace();
      this.menuTags = this.extra.getArray("MenuTags");
      if (this.loadingDialog != null)
        this.loadingDialog.dismiss();
      setupView();
      changeTab();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("dpObjShop", this.dpObjShop);
    paramBundle.putInt("shopId", this.shopId);
    paramBundle.putParcelableArray("menuTags", this.menuTags);
    paramBundle.putParcelable("extra", this.extra);
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
          if (this.gaExtra.shop_id == null)
            this.gaExtra.shop_id = Integer.valueOf(this.shopId);
          GAHelper.instance().contextStatisticsEvent(getApplicationContext(), "price", this.gaExtra, "tap");
        }
        i += 1;
      }
    }
  }

  public void setupMenuTagsView()
  {
    if (this.menuCreated)
      return;
    this.tabNames = new String[this.menuTags.length];
    int i = 0;
    while (true)
    {
      if (i < this.menuTags.length)
      {
        try
        {
          DPObject localDPObject = this.menuTags[i];
          if (localDPObject == null)
            break label180;
          this.tabNames[i] = localDPObject.getString("TagName");
          Bundle localBundle = new Bundle();
          localBundle.putInt("shopId", this.shopId);
          localBundle.putString("TagName", localDPObject.getString("TagName"));
          localBundle.putInt("MenuID", localDPObject.getInt("MenuID"));
          this.menuFragment.addTab(localDPObject.getString("TagName"), R.layout.shop_photo_tab_indicator, WeddingFeastMenuGalleryFragment.class, localBundle);
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
      else
      {
        this.menuFragment.tabHost().findViewById(16908307).setVisibility(0);
        this.menuCreated = true;
        this.menuFragment.tabsAdapter().setTabViewWrapContent(true);
        this.menuFragment.tabsAdapter().notifyDataSetChanged();
        return;
      }
      label180: i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.activity.WeddingFeastMenuGalleryActivity
 * JD-Core Version:    0.6.0
 */