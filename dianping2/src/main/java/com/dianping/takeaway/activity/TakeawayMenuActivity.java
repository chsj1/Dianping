package com.dianping.takeaway.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TextView;
import com.dianping.base.basic.FragmentTabsPagerActivity;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.NumOperateButton;
import com.dianping.base.widget.TitleBar;
import com.dianping.takeaway.entity.DishOperation;
import com.dianping.takeaway.entity.TakeawayAddDishAnimations;
import com.dianping.takeaway.entity.TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo;
import com.dianping.takeaway.entity.TakeawayAddDishAnimations.TakeawayAddDishAnimationsListener;
import com.dianping.takeaway.entity.TakeawayDishInfo;
import com.dianping.takeaway.entity.TakeawayDishMenuAdapter;
import com.dianping.takeaway.entity.TakeawayDishMenuDataSource;
import com.dianping.takeaway.entity.TakeawayShopInfo;
import com.dianping.takeaway.fragment.TakeawayCommentsFragment;
import com.dianping.takeaway.fragment.TakeawayShopDetailFragment;
import com.dianping.takeaway.fragment.TakeawayShopMenuFragment;
import com.dianping.takeaway.util.TakeawayCarCacheManager;
import com.dianping.takeaway.util.TakeawayCarCacheManager.MenuCache;
import com.dianping.takeaway.util.TakeawayGAManager;
import com.dianping.takeaway.util.TakeawayUtils;
import com.dianping.takeaway.view.TAToastView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TakeawayMenuActivity extends FragmentTabsPagerActivity
{
  public static TakeawayShopMenuFragment menuFragment;
  private final int ANIM_DURATION = 300;
  private final int FROM_ORDER_DETAIL = 5;
  private Activity activity = this;
  public View.OnClickListener addClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      Object localObject = paramView.getTag();
      if ((localObject instanceof DishOperation))
      {
        localObject = (DishOperation)localObject;
        TakeawayDishInfo localTakeawayDishInfo = ((DishOperation)localObject).dishItem;
        if (localTakeawayDishInfo.order())
          if (Build.VERSION.SDK_INT >= 23)
          {
            TakeawayMenuActivity.this.mainLooperHandler.post(new Runnable((DishOperation)localObject)
            {
              public void run()
              {
                if (TakeawayMenuActivity.menuFragment != null)
                {
                  TakeawayMenuActivity.menuFragment.broadcastChange(this.val$info);
                  TakeawayMenuActivity.this.updateCartListView();
                  TakeawayMenuActivity.this.updateCartBarView(TakeawayMenuActivity.menuFragment.dishMenuDataSource);
                }
              }
            });
            Activity localActivity = TakeawayMenuActivity.this.activity;
            if (!((DishOperation)localObject).adapter.isSelectedDishList)
              break label238;
            paramView = "2";
            label79: TakeawayGAManager.statistics_takeaway6_dish_quantity(localActivity, paramView, String.valueOf(localTakeawayDishInfo.id), TakeawayMenuActivity.this.shopId, String.valueOf(localTakeawayDishInfo.selectedNum));
            paramView = TakeawayMenuActivity.this.getGAUserInfo();
            paramView.index = Integer.valueOf(localTakeawayDishInfo.selectedNum);
            GAHelper.instance().contextStatisticsEvent(TakeawayMenuActivity.this.activity, "add", paramView, "tap");
            label143: if (((DishOperation)localObject).adapter.isRecommedDishList)
              TakeawayMenuActivity.this.statisticsEvent("takeaway6", "takeaway6_dish_dpcategory_dishclk", TakeawayMenuActivity.this.shopId, 0);
          }
      }
      label238: 
      do
      {
        TakeawayMenuActivity.this.statisticsEvent("takeaway6", "takeaway6_dishdetail_quantity", "", 0);
        if (!TakeawayMenuActivity.this.shopId.equals(TakeawayCarCacheManager.readCacheMenu(TakeawayMenuActivity.this.activity).shopId))
          TakeawayCarCacheManager.clearCacheMenu(TakeawayMenuActivity.this.activity);
        return;
        TakeawayMenuActivity.this.startAddDishAnimation((DishOperation)localObject, paramView, 500);
        break;
        paramView = "1";
        break label79;
        ((NumOperateButton)paramView).subtractNum(false);
        TakeawayMenuActivity.this.showShortToast("该菜品不能购买更多了哟");
        break label143;
      }
      while (!(localObject instanceof Integer));
      int i = Integer.parseInt(localObject.toString());
      localObject = (TakeawayDishInfo)TakeawayMenuActivity.menuFragment.dishMenuDataSource.dishInfoMap.get(Integer.valueOf(i));
      if ((localObject != null) && (!((TakeawayDishInfo)localObject).isSoldout))
        if (((TakeawayDishInfo)localObject).order())
          if (Build.VERSION.SDK_INT >= 23)
            TakeawayMenuActivity.this.mainLooperHandler.post(new Runnable()
            {
              public void run()
              {
                if (TakeawayMenuActivity.menuFragment != null)
                {
                  TakeawayMenuActivity.menuFragment.broadcastChange(null);
                  TakeawayMenuActivity.this.updateCartListView();
                  TakeawayMenuActivity.this.updateCartBarView(TakeawayMenuActivity.menuFragment.dishMenuDataSource);
                }
              }
            });
      while (true)
      {
        TakeawayMenuActivity.this.statisticsEvent("takeaway6", "takeaway6_dish_addlike_clk", TakeawayMenuActivity.this.shopId, 0);
        break;
        TakeawayMenuActivity.this.startAddDishAnimation(null, paramView, 500);
        continue;
        TakeawayMenuActivity.this.showShortToast("该菜品不能购买更多了哟");
        continue;
        TakeawayMenuActivity.this.showShortToast("菜品信息发生变更，客官请选择其他美食吧！");
      }
    }
  };
  private Point addDishAniEndPoint = null;
  private TakeawayAddDishAnimations.TakeawayAddDishAnimationsListener addDishAniListener = new TakeawayAddDishAnimations.TakeawayAddDishAnimationsListener()
  {
    public void onAnimationCancel(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo)
    {
    }

    public void onAnimationEnd(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo)
    {
      if (paramTakeawayAddDishAnimationInfo == null);
      do
      {
        return;
        TakeawayMenuActivity.this.mainLooperHandler.post(new Runnable(paramTakeawayAddDishAnimationInfo)
        {
          public void run()
          {
            if (TakeawayMenuActivity.menuFragment != null)
            {
              TakeawayMenuActivity.menuFragment.broadcastChange(this.val$addDishAnimation.dishOperation);
              TakeawayMenuActivity.this.updateCartListView();
              TakeawayMenuActivity.this.updateCartBarView(TakeawayMenuActivity.menuFragment.dishMenuDataSource);
            }
          }
        });
      }
      while ((paramTakeawayAddDishAnimationInfo.movingView == null) || (paramTakeawayAddDishAnimationInfo.movingView.getParent() == null));
      TakeawayMenuActivity.this.getApplicationWindowManager().removeView(paramTakeawayAddDishAnimationInfo.movingView);
    }

    public void onAnimationPositionChanged(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo, Point paramPoint)
    {
      if ((paramTakeawayAddDishAnimationInfo != null) && (paramTakeawayAddDishAnimationInfo.movingView != null))
      {
        WindowManager.LayoutParams localLayoutParams = (WindowManager.LayoutParams)paramTakeawayAddDishAnimationInfo.movingView.getLayoutParams();
        localLayoutParams.x = paramPoint.x;
        localLayoutParams.y = paramPoint.y;
        localLayoutParams.windowAnimations = 0;
        paramTakeawayAddDishAnimationInfo.movingView.setLayoutParams(localLayoutParams);
        TakeawayMenuActivity.this.getApplicationWindowManager().updateViewLayout(paramTakeawayAddDishAnimationInfo.movingView, localLayoutParams);
      }
    }

    public void onAnimationStart(TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo paramTakeawayAddDishAnimationInfo)
    {
      if ((paramTakeawayAddDishAnimationInfo != null) && (paramTakeawayAddDishAnimationInfo.movingView != null))
      {
        if (paramTakeawayAddDishAnimationInfo.movingView.getParent() == null)
        {
          WindowManager.LayoutParams localLayoutParams = (WindowManager.LayoutParams)paramTakeawayAddDishAnimationInfo.movingView.getLayoutParams();
          TakeawayMenuActivity.this.getApplicationWindowManager().addView(paramTakeawayAddDishAnimationInfo.movingView, localLayoutParams);
        }
        paramTakeawayAddDishAnimationInfo.movingView.setVisibility(0);
      }
    }
  };
  private TakeawayAddDishAnimations addDishAnimations;
  private String address;
  private WindowManager appWindowManager;
  public TakeawayDishMenuAdapter cartAdapter;
  private TextView cartAmountView;
  public View cartBgView;
  public List<TakeawayDishInfo> cartList = new ArrayList();
  public ListView cartListView;
  public View cartView;
  public View closeCartBtn;
  private TextView deliveryFeeView;
  private ExecutorService executorService = null;
  private String lat;
  private String lng;
  private Handler mainLooperHandler = new Handler(Looper.myLooper());
  private TextView noWorkContentView;
  private LinearLayout noWorkLayout;
  private TextView noWorkTitleView;
  public LinearLayout overallStatusView;
  private String queryId;
  public View.OnClickListener removeClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      DishOperation localDishOperation;
      TakeawayDishInfo localTakeawayDishInfo;
      TakeawayDishMenuAdapter localTakeawayDishMenuAdapter;
      Activity localActivity;
      if ((paramView instanceof DishOperation))
      {
        localDishOperation = (DishOperation)paramView;
        localTakeawayDishInfo = localDishOperation.dishItem;
        localTakeawayDishMenuAdapter = localDishOperation.adapter;
        if (localTakeawayDishInfo.cancel())
        {
          localActivity = TakeawayMenuActivity.this.activity;
          if (!localTakeawayDishMenuAdapter.isSelectedDishList)
            break label203;
        }
      }
      label203: for (paramView = "2"; ; paramView = "1")
      {
        TakeawayGAManager.statistics_takeaway6_dish_quantity(localActivity, paramView, String.valueOf(localTakeawayDishInfo.id), TakeawayMenuActivity.this.shopId, String.valueOf(localTakeawayDishInfo.selectedNum));
        paramView = TakeawayMenuActivity.this.getGAUserInfo();
        paramView.index = Integer.valueOf(localTakeawayDishInfo.selectedNum);
        GAHelper.instance().contextStatisticsEvent(TakeawayMenuActivity.this.activity, "add", paramView, "tap");
        if (TakeawayMenuActivity.menuFragment != null)
          TakeawayMenuActivity.menuFragment.broadcastChange(localDishOperation);
        TakeawayMenuActivity.this.updateCartListView();
        TakeawayMenuActivity.this.adjustCartListViewSize(localTakeawayDishMenuAdapter);
        TakeawayMenuActivity.this.updateCartBarView(TakeawayMenuActivity.menuFragment.dishMenuDataSource);
        if ((localTakeawayDishMenuAdapter.getCount() == 0) && (localTakeawayDishMenuAdapter.isSelectedDishList))
          TakeawayMenuActivity.this.closeCartBtn.performClick();
        TakeawayMenuActivity.this.statisticsEvent("takeaway6", "takeaway6_dishdetail_quantity", "", 0);
        return;
      }
    }
  };
  private String shopId;
  private String shopName;
  private int source;
  private Button submitBtn;
  public TAToastView toastView;
  private RMBLabelItem totalPriceView;
  private LinearLayout workingLayout;

  private WindowManager getApplicationWindowManager()
  {
    if (this.appWindowManager == null)
      this.appWindowManager = ((WindowManager)getApplicationContext().getSystemService("window"));
    return this.appWindowManager;
  }

  private void initView()
  {
    super.getTitleBar().setTitle(this.shopName);
    this.tabPagerFragment.tabHost().findViewById(16908307).setBackgroundResource(0);
    this.tabPagerFragment.viewPager().setBackgroundResource(0);
    if ((menuFragment == null) || (this.source == 5))
      menuFragment = TakeawayShopMenuFragment.newInstance(this.source, this.shopId, this.queryId, this.address, this.lat, this.lng);
    addTab("菜单", R.layout.takeaway_tab_indicator, menuFragment, null);
    addTab("外卖点评", R.layout.takeaway_tab_indicator, TakeawayCommentsFragment.newInstance(this.shopId, this.shopName), null);
    addTab("餐厅详情", R.layout.takeaway_tab_indicator, TakeawayShopDetailFragment.newInstance(this.shopId, this.shopName, this.lat, this.lng), null);
    this.tabPagerFragment.tabHost().setCurrentTab(0);
    this.tabPagerFragment.viewPager().setOffscreenPageLimit(5);
  }

  private void startAddDishAnimation(DishOperation paramDishOperation, View paramView, int paramInt)
  {
    if (this.addDishAnimations == null)
      this.addDishAnimations = new TakeawayAddDishAnimations(this.addDishAniListener);
    int[] arrayOfInt = new int[2];
    if (this.addDishAniEndPoint == null)
    {
      this.cartAmountView.getLocationOnScreen(arrayOfInt);
      this.addDishAniEndPoint = new Point(arrayOfInt[0] + ViewUtils.dip2px(this, 10.0F), arrayOfInt[1] - ViewUtils.dip2px(this, 20.0F));
    }
    if (this.executorService == null)
      this.executorService = Executors.newCachedThreadPool();
    this.executorService.execute(new Runnable(paramView, arrayOfInt, paramInt, paramDishOperation)
    {
      public void run()
      {
        Object localObject = new WindowManager.LayoutParams(-2, -2, 2002, 40, 1);
        ((WindowManager.LayoutParams)localObject).gravity = 51;
        ((WindowManager.LayoutParams)localObject).x = 0;
        ((WindowManager.LayoutParams)localObject).y = 0;
        ImageView localImageView = new ImageView(TakeawayMenuActivity.this);
        localImageView.setImageResource(R.drawable.wm_dots_animation);
        localImageView.setLayoutParams((ViewGroup.LayoutParams)localObject);
        this.val$startView.getLocationOnScreen(this.val$location);
        localObject = new TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo(new Point(this.val$location[0] + this.val$startView.getWidth() - ViewUtils.dip2px(TakeawayMenuActivity.this, 30.0F), this.val$location[1] - ViewUtils.dip2px(TakeawayMenuActivity.this, 20.0F)), TakeawayMenuActivity.this.addDishAniEndPoint, this.val$duration);
        ((TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo)localObject).dishOperation = this.val$dishOperation;
        ((TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo)localObject).movingView = localImageView;
        TakeawayMenuActivity.this.mainLooperHandler.post(new Runnable((TakeawayAddDishAnimations.TakeawayAddDishAnimationInfo)localObject)
        {
          public void run()
          {
            TakeawayMenuActivity.this.addDishAnimations.startAddDishAnimation(this.val$addDishAniInfo);
          }
        });
      }
    });
  }

  private void updateSubmitBtn(TakeawayDishMenuDataSource paramTakeawayDishMenuDataSource, boolean paramBoolean1, double paramDouble, boolean paramBoolean2)
  {
    label48: Button localButton;
    if (paramDouble >= paramTakeawayDishMenuDataSource.mShopInfo.minAmount)
    {
      this.submitBtn.setText("选好了");
      this.submitBtn.setTextSize(2, 20.0F);
      if (!paramBoolean2)
        break label140;
      this.submitBtn.setBackgroundResource(R.drawable.common_action_btn_disable);
      localButton = this.submitBtn;
      if ((!paramBoolean1) || (paramDouble < paramTakeawayDishMenuDataSource.mShopInfo.minAmount))
        break label153;
    }
    label140: label153: for (paramBoolean1 = true; ; paramBoolean1 = false)
    {
      localButton.setEnabled(paramBoolean1);
      return;
      this.submitBtn.setText("还差¥" + TakeawayUtils.PRICE_DF.format(paramTakeawayDishMenuDataSource.mShopInfo.minAmount - paramDouble) + "起送");
      this.submitBtn.setTextSize(2, 14.0F);
      break;
      this.submitBtn.setBackgroundResource(R.drawable.btn_weight);
      break label48;
    }
  }

  protected void adjustCartListViewSize(TakeawayDishMenuAdapter paramTakeawayDishMenuAdapter)
  {
    if ((this.cartListView == null) || (!paramTakeawayDishMenuAdapter.isSelectedDishList));
    RelativeLayout.LayoutParams localLayoutParams;
    do
    {
      while (true)
      {
        return;
        localLayoutParams = (RelativeLayout.LayoutParams)this.cartListView.getLayoutParams();
        if (paramTakeawayDishMenuAdapter.getCount() >= 4)
          break;
        if (localLayoutParams.height == -2)
          continue;
        localLayoutParams.height = -2;
        this.cartListView.setLayoutParams(localLayoutParams);
        return;
      }
      paramTakeawayDishMenuAdapter = this.cartListView.getChildAt(0);
    }
    while (paramTakeawayDishMenuAdapter == null);
    localLayoutParams.height = (paramTakeawayDishMenuAdapter.getMeasuredHeight() + this.cartListView.getDividerHeight() << 2);
    this.cartListView.setLayoutParams(localLayoutParams);
  }

  protected GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    try
    {
      localGAUserInfo.shop_id = Integer.valueOf(Integer.parseInt(this.shopId));
      localGAUserInfo.query_id = this.queryId;
      return localGAUserInfo;
    }
    catch (Exception localException)
    {
      while (true)
        localGAUserInfo.shop_id = Integer.valueOf(0);
    }
  }

  public String getPageName()
  {
    return "takeawaydishlist";
  }

  protected void hideCartListView()
  {
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(1.0F, 0.0F);
    localTranslateAnimation.setDuration(300L);
    localTranslateAnimation.setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        TakeawayMenuActivity.this.cartView.setVisibility(4);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    localAlphaAnimation.setDuration(300L);
    this.cartListView.startAnimation(localTranslateAnimation);
    this.cartBgView.startAnimation(localAlphaAnimation);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.getWindow().setBackgroundDrawable(null);
    if (paramBundle == null)
    {
      this.source = super.getIntParam("source", -1);
      this.shopId = super.getStringParam("shopid");
      this.shopName = super.getStringParam("shopname");
      this.queryId = super.getStringParam("queryid");
      this.address = super.getStringParam("address");
      this.lat = super.getStringParam("lat");
    }
    for (this.lng = super.getStringParam("lng"); ; this.lng = paramBundle.getString("lng"))
    {
      if (TextUtils.isEmpty(this.shopId))
      {
        super.showShortToast("缺少必要参数");
        super.finish();
      }
      initView();
      return;
      this.source = paramBundle.getInt("source", -1);
      this.shopId = paramBundle.getString("shopid");
      this.shopName = paramBundle.getString("shopname");
      this.queryId = paramBundle.getString("queryid");
      this.address = paramBundle.getString("address");
      this.lat = paramBundle.getString("lat");
    }
  }

  protected void onDestroy()
  {
    if (this.addDishAnimations != null)
      this.addDishAnimations.cancelAllActiveAddDishAnimations();
    this.mainLooperHandler.removeCallbacksAndMessages(null);
    menuFragment = null;
    super.onDestroy();
  }

  public void onPageSelected(int paramInt)
  {
    super.onPageSelected(paramInt);
    super.statisticsEvent("takeaway6", "takeaway6_dish_changetab_clk", this.shopId, 0);
    GAUserInfo localGAUserInfo = getGAUserInfo();
    switch (paramInt)
    {
    default:
      localGAUserInfo.title = "菜单";
    case 1:
    case 2:
    }
    while (true)
    {
      GAHelper.instance().contextStatisticsEvent(this.activity, "changetab", localGAUserInfo, "tap");
      return;
      super.statisticsEvent("takeaway6", "takeaway6_dish_comment_show", this.shopId, 0);
      localGAUserInfo.title = "外卖点评";
      continue;
      super.statisticsEvent("takeaway6", "takeaway6_dish_shopdetail_show", this.shopId, 0);
      localGAUserInfo.title = "餐厅详情";
    }
  }

  protected void onPause()
  {
    if (!TextUtils.isEmpty(this.shopId))
    {
      if (this.cartList.isEmpty())
        break label49;
      TakeawayCarCacheManager.addCacheMenu(this.activity, new TakeawayCarCacheManager.MenuCache(this.shopId, this.cartList));
    }
    while (true)
    {
      super.onPause();
      return;
      label49: if (!this.shopId.equals(TakeawayCarCacheManager.readCacheMenu(this).shopId))
        continue;
      TakeawayCarCacheManager.clearCacheMenu(this.activity);
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putInt("source", this.source);
    paramBundle.putString("shopid", this.shopId);
    paramBundle.putString("shopname", this.shopName);
    paramBundle.putString("queryid", this.queryId);
    paramBundle.putString("address", this.address);
    paramBundle.putString("lat", this.lat);
    paramBundle.putString("lng", this.lng);
    super.onSaveInstanceState(paramBundle);
  }

  protected void setOnContentView()
  {
    super.setContentView(R.layout.takeaway_shop_menu);
    this.cartView = findViewById(R.id.cart_view);
    this.cartView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayMenuActivity.this.hideCartListView();
      }
    });
    this.cartBgView = findViewById(R.id.cart_bg);
    this.cartListView = ((ListView)findViewById(R.id.cart_list));
    this.submitBtn = ((Button)findViewById(R.id.takeaway_shopmenu_list_button_submit));
    this.closeCartBtn = findViewById(R.id.takeaway_shopmenu_list_layout_hotspot);
    this.cartAmountView = ((TextView)findViewById(R.id.takeaway_shopmenu_list_text_amount));
    this.workingLayout = ((LinearLayout)findViewById(R.id.working_layout));
    this.noWorkLayout = ((LinearLayout)findViewById(R.id.no_work_layout));
    this.noWorkTitleView = ((TextView)findViewById(R.id.no_work_title));
    this.noWorkContentView = ((TextView)findViewById(R.id.no_work_content));
    this.totalPriceView = ((RMBLabelItem)findViewById(R.id.takeaway_shopmenu_list_textview_total));
    this.deliveryFeeView = ((TextView)findViewById(R.id.takeaway_shopmenu_delivery_fee));
    this.toastView = ((TAToastView)findViewById(R.id.toast_view));
    this.overallStatusView = ((LinearLayout)findViewById(R.id.status_layout));
  }

  public void setupCartBarView(TakeawayDishMenuDataSource paramTakeawayDishMenuDataSource)
  {
    if (paramTakeawayDishMenuDataSource.mShopInfo.isAvailable)
    {
      this.closeCartBtn.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (TakeawayMenuActivity.this.cartView.isShown())
          {
            TakeawayMenuActivity.this.hideCartListView();
            return;
          }
          TakeawayMenuActivity.this.statisticsEvent("takeaway6", "takeaway6_dish_btm", TakeawayMenuActivity.this.shopId, 0);
          paramView = TakeawayMenuActivity.this.getGAUserInfo();
          GAHelper.instance().contextStatisticsEvent(TakeawayMenuActivity.this.activity, "cart", paramView, "tap");
          TakeawayMenuActivity.this.cartAdapter.notifyDataSetChanged();
          TakeawayMenuActivity.this.closeCartBtn.postDelayed(new Runnable()
          {
            public void run()
            {
              TakeawayMenuActivity.this.showCartListView();
            }
          }
          , 100L);
        }
      });
      this.submitBtn.setOnClickListener(new View.OnClickListener(paramTakeawayDishMenuDataSource)
      {
        public void onClick(View paramView)
        {
          this.val$dishMenuDataSource.loadSuitableAddress();
          TakeawayMenuActivity.this.toastView.showToast("加载中...", true);
        }
      });
      this.deliveryFeeView.setText(paramTakeawayDishMenuDataSource.mShopInfo.deliveryFee);
      if ((paramTakeawayDishMenuDataSource.mShopInfo.deliveryFee == null) || ("免外送费".equals(paramTakeawayDishMenuDataSource.mShopInfo.deliveryFee)))
        this.deliveryFeeView.setVisibility(8);
      while (true)
      {
        this.workingLayout.setVisibility(0);
        this.noWorkLayout.setVisibility(8);
        updateCartListView();
        updateCartBarView(paramTakeawayDishMenuDataSource);
        return;
        this.deliveryFeeView.setVisibility(0);
      }
    }
    this.cartAmountView.setText("0");
    this.cartAmountView.setEnabled(false);
    this.cartAmountView.setVisibility(0);
    ViewUtils.setVisibilityAndContent(this.noWorkTitleView, paramTakeawayDishMenuDataSource.getUnavailableMessage()[0]);
    ViewUtils.setVisibilityAndContent(this.noWorkContentView, paramTakeawayDishMenuDataSource.getUnavailableMessage()[1]);
    this.workingLayout.setVisibility(8);
    this.noWorkLayout.setVisibility(0);
    this.closeCartBtn.setClickable(false);
    updateSubmitBtn(paramTakeawayDishMenuDataSource, false, 0.0D, true);
  }

  protected void showCartListView()
  {
    adjustCartListViewSize(this.cartAdapter);
    this.cartListView.setSelection(0);
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
    localTranslateAnimation.setDuration(300L);
    localAlphaAnimation.setDuration(300L);
    this.cartListView.startAnimation(localTranslateAnimation);
    this.cartBgView.startAnimation(localAlphaAnimation);
    this.cartView.setVisibility(0);
  }

  public void showOverRangeActivity(TakeawayDishMenuDataSource paramTakeawayDishMenuDataSource)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayoverrange"));
    localIntent.putExtra("content", paramTakeawayDishMenuDataSource.errorMsg[0]);
    localIntent.putExtra("cityid", cityId());
    localIntent.putExtra("geotype", 1);
    localIntent.putExtra("shopname", this.shopName);
    localIntent.putExtra("shopid", paramTakeawayDishMenuDataSource.shopID);
    startActivity(localIntent);
    finish();
  }

  public void showUnknownAddressErrorView(TakeawayDishMenuDataSource paramTakeawayDishMenuDataSource)
  {
    this.overallStatusView.removeAllViews();
    View localView = LayoutInflater.from(this).inflate(R.layout.takeaway_empty_item, this.overallStatusView, false);
    ((TextView)localView.findViewById(16908308)).setText("定位失败");
    ((Button)localView.findViewById(R.id.btn_change)).setOnClickListener(new View.OnClickListener(paramTakeawayDishMenuDataSource)
    {
      public void onClick(View paramView)
      {
        if (TakeawayMenuActivity.menuFragment != null)
        {
          paramView = TakeawayMenuActivity.menuFragment;
          this.val$dishMenuDataSource.getClass();
          paramView.gotoAddressManager(2);
        }
      }
    });
    this.overallStatusView.addView(localView);
    this.overallStatusView.setVisibility(0);
  }

  protected void updateCartBarView(TakeawayDishMenuDataSource paramTakeawayDishMenuDataSource)
  {
    boolean bool;
    double d1;
    int i;
    double d2;
    Object localObject;
    label59: TakeawayDishInfo localTakeawayDishInfo;
    double d3;
    if (!this.cartList.isEmpty())
    {
      bool = true;
      d1 = 0.0D;
      this.totalPriceView.setRMBLabelStyle(4, 2, false, getResources().getColor(R.color.light_red));
      if (!bool)
        break label243;
      i = 0;
      d2 = 0.0D;
      localObject = this.cartList.iterator();
      if (!((Iterator)localObject).hasNext())
        break label159;
      localTakeawayDishInfo = (TakeawayDishInfo)((Iterator)localObject).next();
      d3 = d2 + localTakeawayDishInfo.curPrice * localTakeawayDishInfo.selectedNum;
      if (localTakeawayDishInfo.minFeeCalType != 1)
        break label149;
      d2 = localTakeawayDishInfo.originPrice;
    }
    while (true)
    {
      d1 += d2 * localTakeawayDishInfo.selectedNum;
      i += localTakeawayDishInfo.selectedNum;
      d2 = d3;
      break label59;
      bool = false;
      break;
      label149: d2 = localTakeawayDishInfo.curPrice;
    }
    label159: this.closeCartBtn.setClickable(true);
    this.cartAmountView.setVisibility(0);
    if (!this.cartAmountView.isEnabled())
      this.cartAmountView.setEnabled(true);
    if (i < 100)
    {
      localObject = String.valueOf(i);
      this.cartAmountView.setText((CharSequence)localObject);
      this.totalPriceView.setRMBLabelValue(d2);
    }
    while (true)
    {
      updateSubmitBtn(paramTakeawayDishMenuDataSource, bool, d1, false);
      return;
      localObject = "n+";
      break;
      label243: this.closeCartBtn.setClickable(false);
      this.cartAmountView.setVisibility(4);
      this.cartAmountView.setText("0");
      this.totalPriceView.setRMBLabelValue(0.0D);
    }
  }

  protected void updateCartListView()
  {
    if ((this.cartView != null) && (this.cartView.isShown()))
      this.cartAdapter.notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayMenuActivity
 * JD-Core Version:    0.6.0
 */