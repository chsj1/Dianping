package com.dianping.takeaway.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.BaseBannerView;
import com.dianping.base.widget.TitleBar;
import com.dianping.takeaway.entity.TakeawayIconPagerAdapter;
import com.dianping.takeaway.entity.TakeawayMainDataSource;
import com.dianping.takeaway.entity.TakeawayMainDataSource.EntryParser;
import com.dianping.takeaway.entity.TakeawayMainDataSource.MyTakeaway;
import com.dianping.takeaway.entity.TakeawayMainDataSource.TaRecentOrderLoadListener;
import com.dianping.takeaway.entity.TakeawayNetLoadStatus;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource;
import com.dianping.takeaway.util.TakeawayPreferencesManager;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class TakeawayMainActivity extends TakeawaySampleShopListActivity
  implements TakeawayMainDataSource.TaRecentOrderLoadListener
{
  private static final int REQUEST_SEARCH_SHOP = 2;
  private TextView addressView;
  private BaseBannerView bannerLayout;
  private NovaButton centerButton;
  private View channelView;
  private View headerViews;
  private TakeawayIconPagerAdapter iconAdapter;
  private ViewPager iconsPagerView;
  private TakeawayMainDataSource mainDataSource;
  private View mainTitleView;
  private NavigationDot navDotView;
  private NovaRelativeLayout orderLayout;
  private TextView reachText;
  private TextView shopText;
  private TextView statusText;

  private View createItemView(DPObject paramDPObject)
  {
    String str1 = paramDPObject.getString("Image");
    String str2 = paramDPObject.getString("Url");
    NetworkImageView localNetworkImageView = new NetworkImageView(this);
    localNetworkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    localNetworkImageView.setImage(str1);
    if (!TextUtils.isEmpty(str2))
      localNetworkImageView.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$bannerData.getString("Url");
          TakeawayMainActivity.this.startActivity(paramView);
          GAUserInfo localGAUserInfo = TakeawayMainActivity.this.getGAUserInfo();
          localGAUserInfo.url = paramView;
          GAHelper.instance().contextStatisticsEvent(TakeawayMainActivity.this, "banner", localGAUserInfo, "tap");
        }
      });
    return localNetworkImageView;
  }

  private void exit()
  {
    if ((this.mainDataSource.isShopListPage) && (!this.mainDataSource.forceFinish))
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://home"));
      localIntent.setFlags(67108864);
      super.startActivity(localIntent);
    }
    super.finish();
  }

  private void gotoShopSearchManager(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayshopsearch"));
    if (!TextUtils.isEmpty(paramString))
      localIntent.putExtra("oldkeyword", paramString);
    localIntent.putExtra("lat", this.mainDataSource.lat);
    localIntent.putExtra("lng", this.mainDataSource.lng);
    localIntent.putExtra("geotype", this.mainDataSource.geoType);
    if (this.mainDataSource.isShopListPage)
    {
      localIntent.putExtra("source", 0);
      startActivity(localIntent);
      return;
    }
    localIntent.putExtra("source", 1);
    startActivityForResult(localIntent, 2);
    overridePendingTransition(0, 0);
  }

  private void initCenterView()
  {
    this.centerButton = ((NovaButton)findViewById(R.id.personalCenterBtn));
    this.centerButton.setGAString("my");
    this.centerButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayMainActivity.this.startActivity("dianping://mytakeawayorderlist");
      }
    });
    if (this.mainDataSource.isShopListPage)
    {
      this.orderLayout = ((NovaRelativeLayout)findViewById(R.id.orderLayout));
      this.orderLayout.setGAString("currentorder");
      this.orderLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayMainActivity.this.startActivity("dianping://takeawayorderdetail?orderviewid=" + TakeawayMainActivity.this.mainDataSource.myTakeaway.orderViewId);
        }
      });
      this.statusText = ((TextView)findViewById(R.id.orderStatusText));
      this.reachText = ((TextView)findViewById(R.id.reachTimeText));
      this.shopText = ((TextView)findViewById(R.id.shopNameText));
      this.centerButton.setVisibility(0);
      this.mainDataSource.resumeCenter();
    }
  }

  private void updateBannerView()
  {
    ArrayList localArrayList1 = this.mainDataSource.bannerList();
    if ((localArrayList1 == null) || (localArrayList1.isEmpty()))
      this.bannerLayout.setVisibility(8);
    do
      return;
    while (this.bannerLayout.isShown());
    int j = 1;
    Object localObject1 = TakeawayPreferencesManager.getTakeawayBannerPreferences(this);
    int i = j;
    if (((SharedPreferences)localObject1).getBoolean("announce_closed", false))
    {
      long l = ((SharedPreferences)localObject1).getLong("announce_closed_time", System.currentTimeMillis());
      i = j;
      if (System.currentTimeMillis() - l < 86400000L)
        i = 0;
    }
    if (i == 0)
    {
      this.bannerLayout.setVisibility(8);
      return;
    }
    ArrayList localArrayList2 = new ArrayList();
    Object localObject2 = null;
    localObject1 = null;
    i = 0;
    if (i < localArrayList1.size())
    {
      View localView = createItemView((DPObject)localArrayList1.get(i));
      Object localObject3;
      Object localObject4;
      if (i == 0)
      {
        localObject3 = createItemView((DPObject)localArrayList1.get(i));
        localObject4 = localObject1;
      }
      while (true)
      {
        localArrayList2.add(localView);
        i += 1;
        localObject2 = localObject3;
        localObject1 = localObject4;
        break;
        localObject3 = localObject2;
        localObject4 = localObject1;
        if (i != localArrayList1.size() - 1)
          continue;
        localObject3 = localObject2;
        localObject4 = localObject1;
        if (i <= 0)
          continue;
        localObject4 = createItemView((DPObject)localArrayList1.get(i));
        localObject3 = localObject2;
      }
    }
    if ((localArrayList1.size() > 1) && (localObject2 != null) && (localObject1 != null))
    {
      localArrayList2.add(0, localObject1);
      localArrayList2.add(localObject2);
    }
    this.bannerLayout.setNaviDotGravity(3);
    this.bannerLayout.updateBannerView(localArrayList1.size(), localArrayList2);
    this.bannerLayout.setVisibility(0);
    this.bannerLayout.setBtnOnCloseListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayMainActivity.this.bannerLayout.setVisibility(8);
        paramView = TakeawayPreferencesManager.getTakeawayBannerPreferences(TakeawayMainActivity.this).edit();
        paramView.putBoolean("announce_closed", true);
        paramView.putLong("announce_closed_time", System.currentTimeMillis()).commit();
      }
    });
    GAHelper.instance().contextStatisticsEvent(this, "banner", null, "view");
  }

  private void updateCenterView(TakeawayMainDataSource.MyTakeaway paramMyTakeaway)
  {
    if ((TextUtils.isEmpty(paramMyTakeaway.shopName)) || (TextUtils.isEmpty(paramMyTakeaway.orderStatusStr)) || (TextUtils.isEmpty(paramMyTakeaway.orderTime)))
      this.orderLayout.setVisibility(8);
    do
    {
      return;
      this.shopText.setText(paramMyTakeaway.shopName);
      this.statusText.setText(paramMyTakeaway.orderStatusStr);
      this.reachText.setText(paramMyTakeaway.orderTime);
    }
    while (this.orderLayout.isShown());
    this.orderLayout.setVisibility(0);
    GAHelper.instance().contextStatisticsEvent(this, "currentorder", null, "view");
  }

  private void updateChannelView()
  {
    int i = 8;
    Object localObject;
    int j;
    if (this.iconAdapter != null)
    {
      this.iconAdapter.notifyDataSetChanged();
      localObject = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(this, 75.0F) * this.mainDataSource.iconParser.lineNum + ViewUtils.dip2px(this, 20.0F));
      this.iconsPagerView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      j = this.mainDataSource.iconParser.pagerIconObjs.size();
      if (j == 1)
        this.navDotView.setVisibility(8);
    }
    else
    {
      localObject = this.channelView;
      if (this.mainDataSource.iconParser.perPageIconNum != 0)
        break label180;
    }
    while (true)
    {
      ((View)localObject).setVisibility(i);
      return;
      j -= 2;
      if (this.mainDataSource.iconParser.curPagerPosition > j)
        this.mainDataSource.iconParser.curPagerPosition = j;
      this.iconsPagerView.setCurrentItem(this.mainDataSource.iconParser.curPagerPosition, false);
      this.navDotView.setTotalDot(j);
      this.navDotView.setVisibility(0);
      break;
      label180: i = 0;
    }
  }

  protected TakeawaySampleShoplistDataSource getDataSource()
  {
    if (this.mainDataSource == null)
      this.mainDataSource = new TakeawayMainDataSource(this);
    return this.mainDataSource;
  }

  public String getPageName()
  {
    return "takeawayshoplist";
  }

  protected void initShoplistFooterView()
  {
    this.footerViewLayout = new LinearLayout(this);
    this.footerViewLayout.setOrientation(1);
    this.footerViewLayout.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(this, 60.0F)));
  }

  protected void initShoplistHeaderView()
  {
    this.headerViews = LayoutInflater.from(this).inflate(R.layout.takeaway_shoplist_header_layout, null, false);
    this.bannerLayout = ((BaseBannerView)this.headerViews.findViewById(R.id.banner_layout));
    this.channelView = this.headerViews.findViewById(R.id.icon_layout);
    this.iconsPagerView = ((ViewPager)this.headerViews.findViewById(R.id.icon_pager));
    this.iconsPagerView.setOffscreenPageLimit(5);
    this.iconAdapter = new TakeawayIconPagerAdapter(this, this.mainDataSource);
    this.iconsPagerView.setAdapter(this.iconAdapter);
    this.navDotView = ((NavigationDot)this.headerViews.findViewById(R.id.nav_dots));
    this.navDotView.setDotNormalId(R.drawable.wm_dot);
    this.navDotView.setDotPressedId(R.drawable.wm_dot_pressed);
    this.iconsPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramInt)
      {
        if (paramInt == 0)
        {
          paramInt = TakeawayMainActivity.this.mainDataSource.iconParser.pagerIconObjs.size();
          if (TakeawayMainActivity.this.iconsPagerView.getCurrentItem() != 0)
            break label51;
          TakeawayMainActivity.this.iconsPagerView.setCurrentItem(paramInt - 2, false);
        }
        label51: 
        do
          return;
        while (TakeawayMainActivity.this.iconsPagerView.getCurrentItem() != paramInt - 1);
        TakeawayMainActivity.this.iconsPagerView.setCurrentItem(1, false);
      }

      public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
      {
      }

      public void onPageSelected(int paramInt)
      {
        int i = TakeawayMainActivity.this.mainDataSource.iconParser.pagerIconObjs.size();
        if (i == 1)
          return;
        TakeawayMainActivity.this.navDotView.setCurrentIndex(paramInt - 1);
        if (paramInt == i - 1)
        {
          TakeawayMainActivity.this.navDotView.setCurrentIndex(0);
          return;
        }
        if (paramInt == 0)
        {
          TakeawayMainActivity.this.navDotView.setCurrentIndex(i - 3);
          return;
        }
        TakeawayMainActivity.this.mainDataSource.iconParser.curPagerPosition = paramInt;
      }
    });
    this.headerViewLayout = new LinearLayout(this);
    this.headerViewLayout.setOrientation(1);
    this.headerViewLayout.addView(this.headerViews, new LinearLayout.LayoutParams(-1, -2));
  }

  protected void initTitleBarView()
  {
    if (this.mainDataSource.isShopListPage);
    for (Object localObject1 = this.mainDataSource.curAddress; ; localObject1 = this.mainDataSource.searchKey)
    {
      Object localObject2 = localObject1;
      if (TextUtils.isEmpty((CharSequence)localObject1))
        localObject2 = "定位中...";
      updateTitleBarView((String)localObject2);
      if (!this.mainDataSource.isShopListPage)
        break;
      localObject1 = new ImageView(this);
      int i = ViewUtils.dip2px(this, 10.0F);
      ((ImageView)localObject1).setPadding(i, i, i, i);
      ((ImageView)localObject1).setImageDrawable(getResources().getDrawable(R.drawable.wm_shoplist_titlebar_icon_search));
      super.getTitleBar().addRightViewItem((View)localObject1, "shopkeyword", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayMainActivity.this.gotoShopSearchManager(null);
          GAHelper.instance().contextStatisticsEvent(TakeawayMainActivity.this, "search", TakeawayMainActivity.this.getGAUserInfo(), "tap");
        }
      });
      return;
    }
    super.getTitleBar().removeAllRightViewItem();
  }

  protected void initViews()
  {
    super.initViews();
    initCenterView();
  }

  public void loadCenterOrderFinish(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (8.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
      return;
    case 1:
      updateCenterView((TakeawayMainDataSource.MyTakeaway)paramObject);
      return;
    case 2:
    }
    paramTakeawayNetLoadStatus = (String)paramObject;
    if (!TextUtils.isEmpty(paramTakeawayNetLoadStatus))
      showToast(paramTakeawayNetLoadStatus);
    while (true)
    {
      this.orderLayout.setVisibility(8);
      return;
      showToast("网络不给力哦，请稍后再试");
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 2) && (paramInt2 == -1) && (paramIntent != null))
    {
      String str = paramIntent.getStringExtra("keyword");
      if (!str.equals(this.mainDataSource.searchKey))
      {
        this.mainDataSource.searchKey = str;
        this.mainDataSource.noShopReason = 1;
        this.mainDataSource.extraInfo = paramIntent.getStringExtra("extrainfo");
      }
    }
    else
    {
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mainDataSource.setTaRecentOrderLoadListener(this);
  }

  public void onDestroy()
  {
    GAHelper.instance().contextStatisticsEvent(this, "back", null, "tap");
    this.mainDataSource.onDestroy();
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      exit();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    exit();
  }

  protected void onPause()
  {
    if (this.mainDataSource.isShopListPage)
      this.mainDataSource.pauseCenter();
    super.onPause();
  }

  protected void onResume()
  {
    if (this.mainDataSource.isShopListPage)
      this.mainDataSource.resumeCenter();
    super.onResume();
  }

  protected void onSetContentView()
  {
    super.setContentView(R.layout.takeaway_main_activity);
  }

  protected void updateShoplistHeaderView()
  {
    updateBannerView();
    updateChannelView();
  }

  protected void updateTitleBarView(String paramString)
  {
    LayoutInflater localLayoutInflater;
    if (this.mainTitleView == null)
    {
      localLayoutInflater = LayoutInflater.from(this);
      if (!this.mainDataSource.isShopListPage)
        break label89;
    }
    label89: for (int i = R.layout.takeaway_main_title_address; ; i = R.layout.takeaway_main_title_keyword)
    {
      this.mainTitleView = localLayoutInflater.inflate(i, null);
      this.addressView = ((TextView)this.mainTitleView.findViewById(R.id.title_content));
      ViewUtils.setVisibilityAndContent(this.addressView, paramString);
      this.mainTitleView.setOnClickListener(new View.OnClickListener(paramString)
      {
        public void onClick(View paramView)
        {
          if (TakeawayMainActivity.this.mainDataSource.isShopListPage)
          {
            TakeawayMainActivity.this.gotoAddressManager();
            GAHelper.instance().contextStatisticsEvent(TakeawayMainActivity.this, "location", null, "tap");
            return;
          }
          TakeawayMainActivity.this.gotoShopSearchManager(this.val$titleContent);
        }
      });
      super.getTitleBar().setCustomContentView(this.mainTitleView);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayMainActivity
 * JD-Core Version:    0.6.0
 */