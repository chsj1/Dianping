package com.dianping.selectdish.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.DishLikeManager;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.NewCartManager.CartChangedListener;
import com.dianping.selectdish.animation.SelectDishCartAnimationManager;
import com.dianping.selectdish.animation.SelectDishCartAnimationManager.AddDishAniListener;
import com.dianping.selectdish.entity.SelectDishMenuAdapter;
import com.dianping.selectdish.entity.SelectDishMenuAdapter.SelectDishMenuAdapterListener;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.util.GetHistoryUtil;
import com.dianping.selectdish.util.GetHistoryUtil.GetHistoryListener;
import com.dianping.selectdish.view.SelectDishGuideBannerView;
import com.dianping.selectdish.view.SelectDishMenuCartView;
import com.dianping.selectdish.view.SelectDishPagePortalPopupWindow;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.v1.R.style;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaImageView;
import com.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectDishMenuActivity extends NovaActivity
  implements NewCartManager.CartChangedListener, FullRequestHandle<MApiRequest, MApiResponse>, SelectDishMenuAdapter.SelectDishMenuAdapterListener
{
  private static final int REQUEST_CODE_RECOMMEND_DISH = 1;
  private static final String SOURCE_QRCODE = "qrcode";
  private static final String SOURCE_RECOMMAND = "recommend";
  private SelectDishMenuCartView cartMenuInfoView;
  private DPObject[] categories;
  private int currentCategory = -2147483648;
  private int currentSort = -2147483648;
  private int dealId;
  private DishLikeManager dishLikeManager = DishLikeManager.getInstance();
  private FilterBar filterBar;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private MApiRequest getGroupOnSetMealRequest;
  private GetHistoryUtil getHistoryUtil = new GetHistoryUtil(this, false);
  private int giftId;
  private int groupOnOrderId;
  private boolean isOpen = false;
  private int listStyle;
  private View mBanner;
  private NewCartManager mCartManager = NewCartManager.getInstance();
  private View mCloseView;
  private LoadingErrorView mLoadingErrorView;
  private View mLoadingView;
  private SelectDishMenuAdapter mMenuAdapter;
  private View mMenuContentView;
  private GridView mMenuView;
  private View mOrderBanner;
  private MApiRequest mRequestShopInfo;
  private BroadcastReceiver menuReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if (("com.dianping.selectdish.resetmenu".equals(paramContext)) && (SelectDishMenuActivity.this.mMenuAdapter != null))
        SelectDishMenuActivity.this.mMenuAdapter.reset();
      if (("com.dianping.selectdish.updatedishrecommend".equals(paramContext)) && (SelectDishMenuActivity.this.mMenuAdapter != null) && (SelectDishMenuActivity.this.mMenuAdapter.getDataList().size() > 0))
        SelectDishMenuActivity.this.mMenuAdapter.notifyDataSetChanged();
    }
  };
  private View morePortalView;
  private Dialog newuserGuideDialog;
  private int orderId;
  private SelectDishPagePortalPopupWindow portalPopupWindow;
  private SharedPreferences prefs;
  private int shopId;
  private String shopName;
  private DPObject[] sorts;
  private String source;
  private NovaImageView styleView;
  private TextView subtitleView;
  private TextView titleView;
  private View titlebarView;
  private MApiRequest updateDishRequest;

  private void getGroupOnSetMeal()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/getsetmeal.hbt").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    localBuilder.appendQueryParameter("dealid", String.valueOf(this.dealId));
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.groupOnOrderId));
    this.getGroupOnSetMealRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.getGroupOnSetMealRequest, this);
  }

  private DPObject getSelectFilterItem(Object paramObject, int paramInt)
  {
    Object localObject2 = null;
    Object localObject1;
    int j;
    int i;
    if ("sort".equals(paramObject))
    {
      localObject1 = localObject2;
      if (this.sorts != null)
        localObject1 = this.sorts;
      if (localObject1 != null)
      {
        j = localObject1.length;
        i = 0;
      }
    }
    else
    {
      while (true)
      {
        if (i >= j)
          break label104;
        paramObject = localObject1[i];
        if (paramObject.getInt("ID") == paramInt)
        {
          return paramObject;
          localObject1 = localObject2;
          if (!"category".equals(paramObject))
            break;
          localObject1 = localObject2;
          if (this.categories == null)
            break;
          localObject1 = this.categories;
          break;
        }
        i += 1;
      }
    }
    label104: return (DPObject)null;
  }

  private void getUserHistory()
  {
    this.getHistoryUtil.setGetHistoryListener(new GetHistoryUtil.GetHistoryListener()
    {
      public void onFail()
      {
      }

      public void onFinish()
      {
      }

      public void onProgress()
      {
      }

      public void onStart()
      {
      }
    });
    this.getHistoryUtil.getUserHistory();
  }

  private void initViews()
  {
    this.mBanner = findViewById(R.id.sd_menu_banner);
    this.mOrderBanner = findViewById(R.id.sd_menu_orderbanner);
    this.mLoadingView = findViewById(R.id.sd_menu_loading);
    this.mLoadingView.setVisibility(0);
    this.mLoadingErrorView = ((LoadingErrorView)findViewById(R.id.sd_menu_error));
    this.mLoadingErrorView.setCallBack(new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        SelectDishMenuActivity.this.requestShopInfo();
      }
    });
    this.mMenuContentView = findViewById(R.id.sd_menu_layout);
    this.mMenuContentView.setVisibility(4);
    this.filterBar = ((FilterBar)findViewById(R.id.sd_menu_filter));
    this.filterBar.setOnItemClickListener(new FilterBar.OnItemClickListener()
    {
      public void onClickItem(Object paramObject, View paramView)
      {
        if ("sort".equals(paramObject))
        {
          localListFilterDialog = new ListFilterDialog(SelectDishMenuActivity.this);
          localListFilterDialog.setItems(SelectDishMenuActivity.this.sorts);
          if (SelectDishMenuActivity.this.currentSort == -2147483648)
          {
            localDPObject = SelectDishMenuActivity.this.sorts[0];
            localListFilterDialog.setSelectedItem(localDPObject);
            localListFilterDialog.setOnFilterListener(new FilterDialog.OnFilterListener(paramObject)
            {
              public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
              {
                paramObject = (DPObject)paramObject;
                SelectDishMenuActivity.access$402(SelectDishMenuActivity.this, paramObject.getInt("ID"));
                SelectDishMenuActivity.this.mMenuAdapter.setSort(SelectDishMenuActivity.this.currentSort);
                SelectDishMenuActivity.this.updateFilter(this.val$tag, SelectDishMenuActivity.this.currentSort);
                SelectDishMenuActivity.this.mMenuAdapter.reset();
                SelectDishMenuActivity.this.gaUserInfo.title = String.valueOf(SelectDishMenuActivity.this.currentSort);
                GAHelper.instance().contextStatisticsEvent(SelectDishMenuActivity.this, "sort", SelectDishMenuActivity.this.gaUserInfo, "tap");
                paramFilterDialog.dismiss();
              }
            });
            localListFilterDialog.show(paramView);
          }
        }
        do
        {
          return;
          localDPObject = SelectDishMenuActivity.this.getSelectFilterItem(paramObject, SelectDishMenuActivity.this.currentSort);
          break;
        }
        while (!"category".equals(paramObject));
        ListFilterDialog localListFilterDialog = new ListFilterDialog(SelectDishMenuActivity.this);
        localListFilterDialog.setItems(SelectDishMenuActivity.this.categories);
        if (SelectDishMenuActivity.this.currentCategory == -2147483648);
        for (DPObject localDPObject = SelectDishMenuActivity.this.categories[0]; ; localDPObject = SelectDishMenuActivity.this.getSelectFilterItem(paramObject, SelectDishMenuActivity.this.currentCategory))
        {
          localListFilterDialog.setSelectedItem(localDPObject);
          localListFilterDialog.setOnFilterListener(new FilterDialog.OnFilterListener(paramObject)
          {
            public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
            {
              paramObject = (DPObject)paramObject;
              SelectDishMenuActivity.access$902(SelectDishMenuActivity.this, paramObject.getInt("ID"));
              SelectDishMenuActivity.this.mMenuAdapter.setCategory(SelectDishMenuActivity.this.currentCategory);
              SelectDishMenuActivity.this.updateFilter(this.val$tag, SelectDishMenuActivity.this.currentCategory);
              SelectDishMenuActivity.this.mMenuAdapter.reset();
              SelectDishMenuActivity.this.gaUserInfo.title = String.valueOf(SelectDishMenuActivity.this.currentCategory);
              GAHelper.instance().contextStatisticsEvent(SelectDishMenuActivity.this, "category", SelectDishMenuActivity.this.gaUserInfo, "tap");
              paramFilterDialog.dismiss();
            }
          });
          localListFilterDialog.show(paramView);
          return;
        }
      }
    });
    this.morePortalView = LayoutInflater.from(this).inflate(R.layout.selectdish_title_bar_more_view, null, false);
    this.morePortalView.setVisibility(4);
    this.morePortalView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectDishMenuActivity.this.gaUserInfo.title = "";
        GAHelper.instance().contextStatisticsEvent(SelectDishMenuActivity.this, "morefunction", SelectDishMenuActivity.this.gaUserInfo, "tap");
        SelectDishMenuActivity.this.showMorePagePortalPopupWindow();
      }
    });
    this.titlebarView = findViewById(R.id.sd_menu_title_bar);
    this.titlebarView.setBackgroundColor(getResources().getColor(R.color.white));
    ((LinearLayout)findViewById(R.id.title_bar_right_view_container)).addView(this.morePortalView);
    this.titleView = ((TextView)findViewById(R.id.title_bar_title));
    this.titleView.setText(R.string.sd_title_menu);
    this.subtitleView = ((TextView)findViewById(R.id.title_bar_subtitle));
    updateTitlebarContent(this.shopName);
    findViewById(R.id.left_view).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectDishMenuActivity.this.finish();
      }
    });
    this.mMenuView = ((GridView)findViewById(R.id.sd_menu_grid));
    this.mCloseView = findViewById(R.id.sd_menu_close);
    this.mCloseView.findViewById(R.id.sd_menu_empty_viewothershops).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + SelectDishMenuActivity.this.shopId));
        SelectDishMenuActivity.this.startActivity(paramView);
        SelectDishMenuActivity.this.finish();
      }
    });
    this.mMenuAdapter = new SelectDishMenuAdapter(this, this, this.shopId, this.currentSort, this.currentCategory);
    if (this.listStyle == 0)
    {
      this.mMenuView.setNumColumns(1);
      this.mMenuView.setPadding(0, 0, 0, 0);
      this.mMenuAdapter.setSmallStyle();
    }
    while (true)
    {
      this.cartMenuInfoView = ((SelectDishMenuCartView)findViewById(R.id.sd_menu_bottom_layout));
      this.cartMenuInfoView.setIsTogetherMenu(false);
      this.cartMenuInfoView.setCheckCartListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishMenuActivity.this.gaUserInfo.title = "";
          GAHelper.instance().contextStatisticsEvent(SelectDishMenuActivity.this, "cart", SelectDishMenuActivity.this.gaUserInfo, "tap");
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishcart"));
          SelectDishMenuActivity.this.startActivity(paramView);
        }
      });
      updateCartInfoViews();
      return;
      if (this.listStyle != 1)
        continue;
      this.mMenuView.setNumColumns(2);
      this.mMenuView.setPadding(ViewUtils.dip2px(this, 11.0F), ViewUtils.dip2px(this, 17.0F), ViewUtils.dip2px(this, 11.0F), 0);
      this.mMenuAdapter.setBigStyle();
    }
  }

  private void requestShopInfo()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/menuextrainfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    localBuilder.appendQueryParameter("dealid", String.valueOf(this.dealId));
    this.mRequestShopInfo = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mRequestShopInfo, this);
  }

  private void setupNewUserGuideData()
  {
    this.prefs = getSharedPreferences("selectdishuserguide", 0);
    int i = 0;
    int k = 0;
    boolean bool3 = false;
    boolean bool4 = false;
    if (this.prefs.getInt("newuser", 0) == 0)
    {
      i = 0 + 1;
      k = 1;
    }
    int j;
    boolean bool1;
    boolean bool2;
    Object localObject2;
    Object localObject1;
    View localView1;
    View localView2;
    if ((this.prefs.getInt("grouponnewuser", 0) == 0) && (this.dealId != 0))
    {
      j = i + 1;
      bool1 = true;
      bool2 = bool3;
      if (j > 0)
      {
        localObject2 = LayoutInflater.from(this).inflate(R.layout.selectdish_newuser_guide_layout, null, false);
        localObject1 = (SelectDishGuideBannerView)((View)localObject2).findViewById(R.id.base_banner_view);
        this.newuserGuideDialog = new Dialog(this, R.style.dialog_fullscreen);
        this.newuserGuideDialog.setContentView((View)localObject2);
        localObject2 = new ArrayList();
        if (k != 0)
        {
          localView1 = LayoutInflater.from(this).inflate(R.layout.selectdish_newuser_layout, null, false);
          localView1.findViewById(R.id.select_dish_btn_skip).setOnClickListener(new View.OnClickListener(bool2, bool1)
          {
            public void onClick(View paramView)
            {
              SelectDishMenuActivity.this.newuserGuideDialog.dismiss();
              if (this.val$finalHasTogetherSDGuide)
                SelectDishMenuActivity.this.updateTogetherGuidePref();
              if (this.val$finalHasGrouponGuide)
                SelectDishMenuActivity.this.updateGrouponGuidePref();
            }
          });
          localView2 = localView1.findViewById(R.id.select_dish_btn_know);
          localView2.setOnClickListener(new View.OnClickListener(bool2, bool1)
          {
            public void onClick(View paramView)
            {
              SelectDishMenuActivity.this.newuserGuideDialog.dismiss();
              if (this.val$finalHasTogetherSDGuide)
                SelectDishMenuActivity.this.updateTogetherGuidePref();
              if (this.val$finalHasGrouponGuide)
                SelectDishMenuActivity.this.updateGrouponGuidePref();
            }
          });
          if (j <= 1)
            break label471;
          localView2.setVisibility(8);
        }
      }
    }
    while (true)
    {
      ((ArrayList)localObject2).add(localView1);
      if (bool1)
      {
        localView1 = LayoutInflater.from(this).inflate(R.layout.selectdish_groupon_newuser_layout, null, false);
        localView1.findViewById(R.id.select_dish_btn_skip).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            SelectDishMenuActivity.this.newuserGuideDialog.dismiss();
            SelectDishMenuActivity.this.updateGrouponGuidePref();
          }
        });
        localView1.findViewById(R.id.select_dish_btn_know).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            SelectDishMenuActivity.this.newuserGuideDialog.dismiss();
            SelectDishMenuActivity.this.updateGrouponGuidePref();
          }
        });
        ((ArrayList)localObject2).add(localView1);
      }
      if (bool2)
      {
        localView1 = LayoutInflater.from(this).inflate(R.layout.selectdish_together_newuser_layout, null, false);
        localView1.findViewById(R.id.select_dish_btn_skip).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            SelectDishMenuActivity.this.newuserGuideDialog.dismiss();
            SelectDishMenuActivity.this.updateTogetherGuidePref();
          }
        });
        localView1.findViewById(R.id.select_dish_btn_know).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            SelectDishMenuActivity.this.newuserGuideDialog.dismiss();
            SelectDishMenuActivity.this.updateTogetherGuidePref();
          }
        });
        ((ArrayList)localObject2).add(localView1);
      }
      ((SelectDishGuideBannerView)localObject1).updateBannerView(j, (ArrayList)localObject2);
      ((SelectDishGuideBannerView)localObject1).setVisibility(0);
      localObject1 = this.newuserGuideDialog.getWindow();
      localObject2 = ((Window)localObject1).getAttributes();
      ((WindowManager.LayoutParams)localObject2).dimAmount = 0.8F;
      ((Window)localObject1).setAttributes((WindowManager.LayoutParams)localObject2);
      ((Window)localObject1).setFlags(2, 2);
      this.newuserGuideDialog.show();
      this.newuserGuideDialog.setCanceledOnTouchOutside(false);
      return;
      j = i;
      bool1 = bool4;
      bool2 = bool3;
      if (this.prefs.getInt("togethernewuser", 0) != 0)
        break;
      j = i;
      bool1 = bool4;
      bool2 = bool3;
      if (this.dealId != 0)
        break;
      j = i + 1;
      bool2 = true;
      bool1 = bool4;
      break;
      label471: localView2.setVisibility(0);
    }
  }

  private void showMorePagePortalPopupWindow()
  {
    if (this.portalPopupWindow == null)
      this.portalPopupWindow = new SelectDishPagePortalPopupWindow(this, this.mCartManager.getMenuEntranceList());
    this.portalPopupWindow.showBelowView(this.titlebarView, this.morePortalView);
  }

  private void updateCartInfoViews()
  {
    int j = 0;
    SelectDishMenuCartView localSelectDishMenuCartView;
    if ((this.mCartManager.getTotalDishCount() + this.mCartManager.getTotalSelectFreeDishCount() > 0) || (this.mCartManager.hasHistoryFreeDish()) || (this.mCartManager.groupOnDealId > 0))
    {
      i = 1;
      localSelectDishMenuCartView = this.cartMenuInfoView;
      if (i == 0)
        break label71;
    }
    label71: for (int i = j; ; i = 8)
    {
      localSelectDishMenuCartView.setVisibility(i);
      this.cartMenuInfoView.refresh();
      return;
      i = 0;
      break;
    }
  }

  private void updateFilter(Object paramObject, int paramInt)
  {
    DPObject localDPObject = getSelectFilterItem(paramObject, paramInt);
    if (localDPObject != null)
      this.filterBar.setItem(paramObject, localDPObject.getString("Name"));
  }

  private void updateMenuEntranceView()
  {
    DPObject[] arrayOfDPObject = this.mCartManager.getMenuEntranceList();
    View localView = this.morePortalView;
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length >= 1));
    for (int i = 0; ; i = 4)
    {
      localView.setVisibility(i);
      return;
    }
  }

  private void updateTitlebarContent(String paramString)
  {
    ViewUtils.setVisibilityAndContent(this.subtitleView, paramString);
  }

  private void verifyDishes()
  {
    if (this.updateDishRequest != null)
      mapiService().abort(this.updateDishRequest, this, false);
    this.mCartManager.updateShopId = 0;
    Object localObject = this.mCartManager.getAllDishes();
    if ((((ArrayList)localObject).isEmpty()) && (TextUtils.isEmpty(this.mCartManager.orderViewId)))
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/querydish.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    if (TextUtils.isEmpty(this.mCartManager.orderViewId))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localObject = ((ArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext())
        localStringBuilder.append(((CartItem)((Iterator)localObject).next()).dishInfo.dishId).append(",");
      if (localStringBuilder.length() > 0)
        localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
      localBuilder.appendQueryParameter("dishides", localStringBuilder.toString());
    }
    while (true)
    {
      this.updateDishRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      this.mCartManager.updateShopId = this.shopId;
      mapiService().exec(this.updateDishRequest, this);
      return;
      localBuilder.appendQueryParameter("orderViewId", this.mCartManager.orderViewId);
    }
  }

  public void dealData(boolean paramBoolean1, Object paramObject, boolean paramBoolean2)
  {
    this.mLoadingView.setVisibility(8);
    if (paramBoolean1)
    {
      if (!(paramObject instanceof DPObject))
        break label483;
      paramObject = (DPObject)paramObject;
      if ((this.sorts != null) && (this.categories != null))
        break label446;
      this.sorts = paramObject.getArray("MenuSortNavList");
      this.categories = paramObject.getArray("MenuCategoryNavList");
      this.currentSort = paramObject.getInt("CurrentSort");
      this.currentCategory = paramObject.getInt("CurrentCategory");
      this.mMenuAdapter.setSort(this.currentSort);
      this.mMenuAdapter.setCategory(this.currentCategory);
      if (paramBoolean2)
        this.mLoadingErrorView.setVisibility(0);
    }
    else
    {
      return;
    }
    if ((this.sorts != null) && (this.categories != null))
    {
      this.filterBar.addItem("category", "category");
      this.filterBar.addItem("sort", "sort");
      int i = this.filterBar.getHeight();
      LinearLayout localLinearLayout = new LinearLayout(this);
      localLinearLayout.setOrientation(0);
      localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, i));
      View localView = new View(this);
      localView.setBackgroundColor(getResources().getColor(R.color.inner_divider));
      this.styleView = new NovaImageView(this);
      this.styleView.setGAString("showlist");
      this.styleView.setScaleType(ImageView.ScaleType.CENTER);
      this.styleView.setImageResource(R.drawable.sd_menustyle_small);
      this.styleView.setPadding(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 15.0F), 0);
      this.styleView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishMenuActivity.this.gaUserInfo.title = "";
          GAHelper.instance().contextStatisticsEvent(SelectDishMenuActivity.this, "showlist", SelectDishMenuActivity.this.gaUserInfo, "tap");
          if (SelectDishMenuActivity.this.mMenuAdapter.isCurrentStyleSmall())
          {
            SelectDishMenuActivity.this.mMenuAdapter.setBigStyle();
            SelectDishMenuActivity.this.mMenuView.setNumColumns(2);
            SelectDishMenuActivity.this.mMenuView.setPadding(ViewUtils.dip2px(SelectDishMenuActivity.this, 11.0F), ViewUtils.dip2px(SelectDishMenuActivity.this, 17.0F), ViewUtils.dip2px(SelectDishMenuActivity.this, 11.0F), 0);
            SelectDishMenuActivity.this.styleView.setImageResource(R.drawable.sd_menustyle_small);
            return;
          }
          SelectDishMenuActivity.this.mMenuAdapter.setSmallStyle();
          SelectDishMenuActivity.this.mMenuView.setNumColumns(1);
          SelectDishMenuActivity.this.mMenuView.setPadding(0, 0, 0, 0);
          SelectDishMenuActivity.this.styleView.setImageResource(R.drawable.sd_menustyle_big);
        }
      });
      localLinearLayout.addView(localView, new LinearLayout.LayoutParams(1, i));
      localLinearLayout.addView(this.styleView, new LinearLayout.LayoutParams(-2, i));
      this.filterBar.addView(localLinearLayout, new LinearLayout.LayoutParams(-2, i));
      updateFilter("sort", this.currentSort);
      updateFilter("category", this.currentCategory);
    }
    if (this.mCartManager.average == 0.0D)
      this.mCartManager.average = paramObject.getInt("AvgPrice");
    if (this.dishLikeManager.recommendInfos != null)
    {
      this.dishLikeManager.recommendInfos.clear();
      this.dishLikeManager.addRecommendInfos(paramObject.getArray("List"));
    }
    label446: if (this.shopName == null)
    {
      this.shopName = paramObject.getString("ShopName");
      this.mCartManager.setShopName(this.shopName);
    }
    updateTitlebarContent(this.shopName);
    label483: this.mMenuContentView.setVisibility(0);
  }

  public String getPageName()
  {
    return "menuorder_list";
  }

  public void gotoDishDetail(DishInfo paramDishInfo)
  {
    if (paramDishInfo != null)
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishdetail"));
      localIntent.putExtra("detail", paramDishInfo);
      startActivity(localIntent);
      this.gaUserInfo.title = String.valueOf(paramDishInfo.dishId);
      GAHelper.instance().contextStatisticsEvent(this, "detail", this.gaUserInfo, "tap");
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1))
    {
      Bundle localBundle = new Bundle();
      localBundle.putBoolean("fromRecommend", true);
      localBundle.putString("source", "shopinfo");
      localBundle.putStringArrayList("dishes", paramIntent.getStringArrayListExtra("dishes"));
      AddReviewUtil.addReview(this, this.shopId, this.shopName, localBundle);
    }
  }

  public void onCountChanged()
  {
    updateCartInfoViews();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject = new IntentFilter("com.dianping.selectdish.resetmenu");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.menuReceiver, (IntentFilter)localObject);
    localObject = new IntentFilter("com.dianping.selectdish.updatedishrecommend");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.menuReceiver, (IntentFilter)localObject);
    setContentView(R.layout.selectdish_menu_layout);
    if (getBooleanParam("close", false))
    {
      finish();
      return;
    }
    this.shopId = getIntParam("shopid");
    this.giftId = getIntParam("giftid");
    this.orderId = getIntParam("orderid", 0);
    this.source = getStringParam("source");
    this.listStyle = getIntParam("liststyle");
    this.dealId = getIntParam("dealid");
    this.groupOnOrderId = getIntParam("grouponorderid");
    this.gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    GAHelper.instance().setGAPageName(getPageName());
    if ("recommend".equals(this.source))
    {
      this.gaUserInfo.title = "2";
      GAHelper.instance().contextStatisticsEvent(this, "menuorder_list", this.gaUserInfo, "view");
      label212: if (paramBundle != null)
        break label494;
      this.currentSort = getIntParam("sort", -2147483648);
      this.currentCategory = getIntParam("category", -2147483648);
      label242: this.mCartManager.setShopIdandDealId(this.shopId, this.dealId);
      this.mCartManager.orderViewId = getStringParam("orderviewid");
      this.mCartManager.tableId = getStringParam("tablenum");
      this.mCartManager.exchangedGiftId = this.giftId;
      localObject = this.mCartManager;
      if (this.orderId != 0)
        break label523;
    }
    label523: for (paramBundle = null; ; paramBundle = String.valueOf(this.orderId))
    {
      ((NewCartManager)localObject).setOrderId(paramBundle);
      this.mCartManager.addCartChangedListener(this);
      this.mCartManager.groupOnOrderId = this.groupOnOrderId;
      initViews();
      requestShopInfo();
      if (this.dealId != 0)
      {
        this.mCartManager.emptyCart();
        this.mCartManager.deleteCartStorage();
        getGroupOnSetMeal();
      }
      if ((accountService().token() != null) && (!this.mCartManager.hasHistoryFreeDish()))
        getUserHistory();
      verifyDishes();
      setupNewUserGuideData();
      if (TextUtils.isEmpty(this.mCartManager.orderViewId))
        break;
      showToast("所点菜品已放回购物车\n您可继续编辑菜品哦~");
      return;
      if ("qrcode".equals(this.source))
      {
        this.gaUserInfo.title = "3";
        GAHelper.instance().contextStatisticsEvent(this, "menuorder_list", this.gaUserInfo, "view");
        break label212;
      }
      this.gaUserInfo.title = "1";
      GAHelper.instance().contextStatisticsEvent(this, "menuorder_list", this.gaUserInfo, "view");
      break label212;
      label494: this.currentSort = paramBundle.getInt("sort", -2147483648);
      this.currentCategory = paramBundle.getInt("category", -2147483648);
      break label242;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.menuReceiver);
    if (this.mRequestShopInfo != null)
    {
      mapiService().abort(this.mRequestShopInfo, this, true);
      this.mRequestShopInfo = null;
    }
    if (this.getGroupOnSetMealRequest != null)
    {
      mapiService().abort(this.getGroupOnSetMealRequest, this, true);
      this.getGroupOnSetMealRequest = null;
    }
    this.getHistoryUtil.releaseHistoryRequest();
    this.getHistoryUtil.removeGetHistoryListener();
    this.mCartManager.storeCart();
    this.mCartManager.removeCartChangedListener(this);
  }

  public void onDishChanged(CartItem paramCartItem)
  {
    if (paramCartItem == null)
    {
      this.mMenuAdapter.notifyDataSetChanged();
      return;
    }
    updateSingleRow(paramCartItem.dishInfo.dishId);
  }

  public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
  {
  }

  public void onGroupOnOrSetChanged()
  {
    this.mMenuAdapter.notifyDataSetChanged();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://choosedish"));
      localIntent.putExtra("shopId", String.valueOf(this.shopId));
      localIntent.putExtra("fromRecommend", true);
      startActivityForResult(localIntent, 1);
    }
    return paramBoolean;
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    finish();
    if (!getBooleanParam("close", false))
      startActivity(paramIntent);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRequestShopInfo)
    {
      this.mRequestShopInfo = null;
      this.mLoadingView.setVisibility(8);
      this.mLoadingErrorView.setVisibility(0);
    }
    do
    {
      return;
      if (paramMApiRequest != this.getGroupOnSetMealRequest)
        continue;
      this.getGroupOnSetMealRequest = null;
      return;
    }
    while (paramMApiRequest != this.updateDishRequest);
    this.updateDishRequest = null;
    this.mCartManager.updateShopId = 0;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRequestShopInfo)
    {
      this.mRequestShopInfo = null;
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest instanceof DPObject))
      {
        this.mLoadingErrorView.setVisibility(8);
        paramMApiRequest = (DPObject)paramMApiRequest;
        this.isOpen = paramMApiRequest.getBoolean("IsOrderDish");
        if (!this.isOpen)
        {
          this.mCloseView.setVisibility(0);
          this.mLoadingView.setVisibility(8);
          updateMenuEntranceView();
        }
      }
    }
    Object localObject1;
    Object localObject2;
    Object localObject3;
    label282: label305: label364: 
    do
      while (true)
      {
        return;
        this.mCartManager.isEstimate = paramMApiRequest.getBoolean("IsEstimate");
        boolean bool1 = paramMApiRequest.getBoolean("IsPrePaySupport");
        boolean bool2 = paramMApiRequest.getBoolean("TableIdSupport");
        this.mCartManager.setSupportPrePay(bool1);
        this.mCartManager.setSupportTable(bool2);
        this.mCartManager.setMenuEntrance(paramMApiRequest.getObject("DishMenuEntrance"));
        this.mCartManager.orderButtonSubTitle = paramMApiRequest.getString("OrderButtonSubTitle");
        paramMApiResponse = paramMApiRequest.getObject("MenuBanner");
        paramMApiRequest = paramMApiRequest.getObject("OrderBanner");
        if (paramMApiResponse != null)
        {
          this.mBanner.setVisibility(0);
          localObject1 = (TextView)this.mBanner.findViewById(R.id.sd_banner_title);
          localObject2 = (TextView)this.mBanner.findViewById(R.id.sd_banner_content);
          localObject3 = paramMApiResponse.getString("Tag");
          String str = paramMApiResponse.getString("Text");
          paramMApiResponse = paramMApiResponse.getString("Url");
          if (!TextUtils.isEmpty((CharSequence)localObject3))
          {
            ((TextView)localObject1).setText((CharSequence)localObject3);
            ((TextView)localObject1).setVisibility(0);
            if (TextUtils.isEmpty(str))
              break label419;
            ((TextView)localObject2).setText(str);
            ((TextView)localObject2).setVisibility(0);
            if (!TextUtils.isEmpty(paramMApiResponse))
              this.mBanner.setOnClickListener(new View.OnClickListener(paramMApiResponse)
              {
                public void onClick(View paramView)
                {
                  try
                  {
                    SelectDishMenuActivity.this.startActivity(this.val$url);
                    return;
                  }
                  catch (Exception paramView)
                  {
                    paramView.printStackTrace();
                  }
                }
              });
            if (paramMApiRequest == null)
              break label450;
            this.mOrderBanner.setVisibility(0);
            paramMApiResponse = (TextView)this.mOrderBanner.findViewById(R.id.sd_orderbanner_content);
            localObject1 = paramMApiRequest.getString("Text");
            paramMApiRequest = paramMApiRequest.getString("Url");
            if (TextUtils.isEmpty((CharSequence)localObject1))
              break label441;
            paramMApiResponse.setText((CharSequence)localObject1);
            paramMApiResponse.setVisibility(0);
            if (!TextUtils.isEmpty(paramMApiRequest))
              this.mOrderBanner.setOnClickListener(new View.OnClickListener(paramMApiRequest)
              {
                public void onClick(View paramView)
                {
                  try
                  {
                    SelectDishMenuActivity.this.startActivity(this.val$orderurl);
                    return;
                  }
                  catch (Exception paramView)
                  {
                    paramView.printStackTrace();
                  }
                }
              });
          }
        }
        while (true)
        {
          this.mMenuView.setAdapter(this.mMenuAdapter);
          this.mMenuAdapter.reset();
          updateMenuEntranceView();
          return;
          ((TextView)localObject1).setVisibility(8);
          break;
          ((TextView)localObject2).setVisibility(8);
          break label282;
          this.mBanner.setVisibility(8);
          break label305;
          paramMApiResponse.setVisibility(8);
          break label364;
          this.mOrderBanner.setVisibility(8);
        }
        this.mLoadingErrorView.setVisibility(0);
        return;
        if (paramMApiRequest != this.getGroupOnSetMealRequest)
          break;
        this.getGroupOnSetMealRequest = null;
        paramMApiRequest = paramMApiResponse.result();
        if (!(paramMApiRequest instanceof DPObject))
          continue;
        paramMApiRequest = (DPObject)paramMApiRequest;
        this.mCartManager.setGroupOnInfo(paramMApiRequest);
        this.mMenuAdapter.notifyDataSetChanged();
        return;
      }
    while (paramMApiRequest != this.updateDishRequest);
    label419: label441: label450: this.updateDishRequest = null;
    paramMApiRequest = paramMApiResponse.result();
    if ((paramMApiRequest instanceof DPObject))
    {
      paramMApiResponse = (DPObject)paramMApiRequest;
      paramMApiRequest = new SparseArray();
      localObject1 = paramMApiResponse.getArray("MenuCountItemList");
      int i;
      if ((localObject1 != null) && (localObject1.length >= 1))
      {
        i = 0;
        while (i < localObject1.length)
        {
          paramMApiRequest.put(localObject1[i].getInt("DishId"), Integer.valueOf(localObject1[i].getInt("Count")));
          i += 1;
        }
      }
      paramMApiResponse = paramMApiResponse.getArray("MenuItemList");
      if (paramMApiResponse != null)
      {
        localObject1 = new CartItem[paramMApiResponse.length];
        i = 0;
        while (i < paramMApiResponse.length)
        {
          localObject2 = new CartItem(new DishInfo(paramMApiResponse[i]));
          localObject3 = (Integer)paramMApiRequest.get(((CartItem)localObject2).dishInfo.dishId);
          if (localObject3 != null)
            ((CartItem)localObject2).setItemCount(((Integer)localObject3).intValue());
          localObject1[i] = localObject2;
          i += 1;
        }
        if (!TextUtils.isEmpty(this.mCartManager.orderViewId))
          break label749;
        this.mCartManager.correctAll(localObject1, null);
      }
    }
    while (true)
    {
      this.mCartManager.updateShopId = 0;
      return;
      label749: this.mCartManager.setDishCartItems(localObject1);
    }
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if (paramMApiRequest == this.mRequestShopInfo)
    {
      this.mLoadingErrorView.setVisibility(8);
      this.mLoadingView.setVisibility(0);
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("sort", this.currentSort);
    paramBundle.putInt("category", this.currentCategory);
  }

  public void startAddDishAnimation(View paramView, int paramInt, DishInfo paramDishInfo)
  {
    if (this.cartMenuInfoView.getVisibility() != 0)
    {
      this.cartMenuInfoView.setVisibility(0);
      this.cartMenuInfoView.post(new Runnable(paramView, paramInt, paramDishInfo)
      {
        public void run()
        {
          SelectDishMenuActivity.this.startAddDishAnimation(this.val$view, this.val$duration, this.val$dishInfo);
        }
      });
      return;
    }
    FrameLayout localFrameLayout = (FrameLayout)findViewById(R.id.sd_menu_moving_frame);
    ImageView localImageView = new ImageView(this);
    int i = ViewUtils.dip2px(this, 17.0F);
    localImageView.setLayoutParams(new ViewGroup.LayoutParams(i, i));
    localImageView.setImageResource(R.drawable.background_selectdish_count);
    localFrameLayout.addView(localImageView);
    paramView = paramView.findViewById(R.id.sd_menuitem_count);
    View localView = this.cartMenuInfoView.getDishCountView();
    SelectDishCartAnimationManager localSelectDishCartAnimationManager = new SelectDishCartAnimationManager(this);
    localSelectDishCartAnimationManager.getClass();
    localSelectDishCartAnimationManager.setAddDishAniListener(new SelectDishCartAnimationManager.AddDishAniListener(localSelectDishCartAnimationManager, paramDishInfo, localFrameLayout, localImageView)
    {
      public void onAnimationEnd(Animator paramAnimator)
      {
        super.onAnimationEnd(paramAnimator);
        SelectDishMenuActivity.this.gaUserInfo.title = String.valueOf(this.val$dishInfo.dishId);
        GAHelper.instance().contextStatisticsEvent(SelectDishMenuActivity.this, "addcart", SelectDishMenuActivity.this.gaUserInfo, "tap");
        this.val$frameLayout.removeView(this.val$movingView);
        SelectDishMenuActivity.this.mCartManager.addDish(this.val$dishInfo);
      }
    });
    localSelectDishCartAnimationManager.startAddDishAnimation(localImageView, paramView, localView, paramInt, 0);
  }

  protected void updateGrouponGuidePref()
  {
    SharedPreferences.Editor localEditor = this.prefs.edit();
    localEditor.putInt("newuser", 1);
    localEditor.putInt("grouponnewuser", 1);
    localEditor.apply();
  }

  public void updateSingleRow(int paramInt)
  {
    if (this.mMenuView != null)
    {
      int j = this.mMenuView.getFirstVisiblePosition();
      int k = this.mMenuView.getLastVisiblePosition();
      int i = j;
      while (i <= k)
      {
        if (((this.mMenuAdapter.getItem(i) instanceof DPObject)) && (paramInt == ((DPObject)this.mMenuAdapter.getItem(i)).getInt("Id")))
        {
          View localView = this.mMenuView.getChildAt(i - j);
          this.mMenuAdapter.getView(i, localView, this.mMenuView);
        }
        i += 1;
      }
    }
  }

  protected void updateTogetherGuidePref()
  {
    SharedPreferences.Editor localEditor = this.prefs.edit();
    localEditor.putInt("newuser", 1);
    localEditor.putInt("togethernewuser", 1);
    localEditor.apply();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishMenuActivity
 * JD-Core Version:    0.6.0
 */