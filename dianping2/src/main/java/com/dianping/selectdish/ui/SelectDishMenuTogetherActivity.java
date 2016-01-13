package com.dianping.selectdish.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.DishLikeManager;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.TogetherCartManager;
import com.dianping.selectdish.TogetherCartManager.TogetherCartChangedListener;
import com.dianping.selectdish.animation.SelectDishCartAnimationManager;
import com.dianping.selectdish.animation.SelectDishCartAnimationManager.AddDishAniListener;
import com.dianping.selectdish.entity.SelectDishMenuAdapter.SelectDishMenuAdapterListener;
import com.dianping.selectdish.entity.SelectDishMenuTogetherAdapter;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.util.GetHistoryUtil;
import com.dianping.selectdish.view.SelectDishAlertDialog;
import com.dianping.selectdish.view.SelectDishAlertDialog.OnDialogClickListener;
import com.dianping.selectdish.view.SelectDishMenuCartView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaTextView;
import com.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectDishMenuTogetherActivity extends NovaActivity
  implements SelectDishMenuAdapter.SelectDishMenuAdapterListener
{
  private static final int ACTIVITY_STATE_LOADING = 2;
  private static final int ACTIVITY_STATE_LOAD_ERROR = 1;
  private static final int ACTIVITY_STATE_MENU = 4;
  private static final int ACTIVITY_STATE_ORDER_CLOSED = 3;
  private static final String FILTER_TAG_CATEGORY = "category";
  private static final String FILTER_TAG_SORT = "sort";
  private static final String INTENT_ACTION_SELECTDISH_RESETMENU = "com.dianping.selectdish.resetmenu";
  private static final String INTENT_ACTION_SELECTDISH_UPDATEDISHRECOMMEND = "com.dianping.selectdish.updatedishrecommend";
  private static final int LIST_STYLE_BIG = 1;
  private static final int LIST_STYLE_SMALL = 0;
  public static final String MESSAGE_HAD_QUIT = "已退出";
  private static final String MESSAGE_QUIT_ROOM_REQUESTING = "请稍候...";
  private static final int RESULT_OK = 1;
  private final TogetherCartManager.TogetherCartChangedListener cartChangedListener = new TogetherCartManager.TogetherCartChangedListener()
  {
    private final StringBuilder stringBuilder = new StringBuilder();

    public void onCountChanged()
    {
      SelectDishMenuTogetherActivity.this.updateCartInfoViews();
    }

    public void onDishChanged(CartItem paramCartItem)
    {
      if (paramCartItem == null)
      {
        SelectDishMenuTogetherActivity.this.mMenuAdapter.notifyDataSetChanged();
        return;
      }
      SelectDishMenuTogetherActivity.this.updateSingleRow(paramCartItem.dishInfo.dishId);
    }

    public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
    {
    }

    public void onGroupOnOrSetChanged()
    {
      SelectDishMenuTogetherActivity.this.mMenuAdapter.notifyDataSetChanged();
    }

    public void onManulRefreshComplete()
    {
    }

    public void onMistakeRecieved(int paramInt, String paramString)
    {
      if ((SelectDishMenuTogetherActivity.this.isResumed) && (!TextUtils.isEmpty(paramString)))
        SelectDishMenuTogetherActivity.this.showShortToast(paramString);
      if (paramInt == 100)
        SelectDishMenuTogetherActivity.this.finishWithResult();
    }

    public void onSyncComplete()
    {
      if (SelectDishMenuTogetherActivity.this.peopleNum != SelectDishMenuTogetherActivity.this.mCartManager.peopleNum)
      {
        SelectDishMenuTogetherActivity.access$2002(SelectDishMenuTogetherActivity.this, SelectDishMenuTogetherActivity.this.mCartManager.peopleNum);
        this.stringBuilder.setLength(0);
        this.stringBuilder.append(SelectDishMenuTogetherActivity.this.roomName).append("(").append(SelectDishMenuTogetherActivity.this.peopleNum).append("人)");
        SelectDishMenuTogetherActivity.this.roomNameTextView.setText(this.stringBuilder.toString());
      }
      SelectDishMenuTogetherActivity.this.mMenuAdapter.notifyDataSetChanged();
      SelectDishMenuTogetherActivity.this.updateCartInfoViews();
    }
  };
  private SelectDishMenuCartView cartMenuInfoView;
  private DPObject[] categories;
  private int currentCategory = -2147483648;
  private int currentSort = -2147483648;
  private FilterBar filterBar;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private GetHistoryUtil getHistoryUtil;
  private int listStyle;
  private final TogetherCartManager mCartManager = TogetherCartManager.getInstance();
  private LoadingErrorView mLoadingErrorView;
  private View mLoadingView;
  private SelectDishMenuTogetherAdapter mMenuAdapter;
  private View mMenuContentView;
  private GridView mMenuView;
  private MApiRequest mRequestShopInfo;
  private final BroadcastReceiver menuReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if (("com.dianping.selectdish.resetmenu".equals(paramContext)) && (SelectDishMenuTogetherActivity.this.mMenuAdapter != null))
        SelectDishMenuTogetherActivity.this.mMenuAdapter.reset();
      if (("com.dianping.selectdish.updatedishrecommend".equals(paramContext)) && (SelectDishMenuTogetherActivity.this.mMenuAdapter != null) && (SelectDishMenuTogetherActivity.this.mMenuAdapter.getDataList().size() > 0))
        SelectDishMenuTogetherActivity.this.mMenuAdapter.notifyDataSetChanged();
    }
  };
  private DPObject orderDishRoomInfo;
  private int peopleNum;
  private final RequestHandler<MApiRequest, MApiResponse> requestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == SelectDishMenuTogetherActivity.this.mRequestShopInfo)
      {
        SelectDishMenuTogetherActivity.access$1102(SelectDishMenuTogetherActivity.this, null);
        SelectDishMenuTogetherActivity.this.setLayoutsVisibleWithDifferentState(1);
      }
      do
        return;
      while (paramMApiRequest != SelectDishMenuTogetherActivity.this.requestQuitRoom);
      SelectDishMenuTogetherActivity.this.dismissDialog();
      SelectDishMenuTogetherActivity.access$1302(SelectDishMenuTogetherActivity.this, null);
      SelectDishMenuTogetherActivity.this.onRequestQuitOrderDishRoomFailed();
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == SelectDishMenuTogetherActivity.this.mRequestShopInfo)
      {
        SelectDishMenuTogetherActivity.access$1102(SelectDishMenuTogetherActivity.this, null);
        SelectDishMenuTogetherActivity.this.onRequestShopInfoFinish(paramMApiResponse);
      }
      do
        return;
      while (paramMApiRequest != SelectDishMenuTogetherActivity.this.requestQuitRoom);
      SelectDishMenuTogetherActivity.this.dismissDialog();
      SelectDishMenuTogetherActivity.access$1302(SelectDishMenuTogetherActivity.this, null);
      SelectDishMenuTogetherActivity.this.onRequestQuitOrderDishRoomFinish(paramMApiResponse);
    }
  };
  private MApiRequest requestQuitRoom;
  private int roomId;
  private String roomName;
  private TextView roomNameTextView;
  private int shopId = 0;
  private String shopName;
  private DPObject[] sorts;

  private void clearNormalDishCart()
  {
    NewCartManager localNewCartManager = NewCartManager.getInstance();
    Iterator localIterator;
    try
    {
      localIterator = new ArrayList(localNewCartManager.getAllDishes()).iterator();
      while (localIterator.hasNext())
        localNewCartManager.deleteDish(((CartItem)localIterator.next()).dishInfo);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    while (true)
    {
      return;
      localIterator = new ArrayList(localException.getAllUsedFreeDished()).iterator();
      while (localIterator.hasNext())
      {
        CartFreeItem localCartFreeItem = (CartFreeItem)localIterator.next();
        if (!localCartFreeItem.use)
          continue;
        localException.operateFreeDish(localCartFreeItem);
      }
    }
  }

  private void finishWithResult()
  {
    setResult(1);
    finish();
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
          break label102;
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
    label102: return (DPObject)null;
  }

  private void getUserHistory()
  {
    if (this.getHistoryUtil == null)
      this.getHistoryUtil = new GetHistoryUtil(this, true);
    this.getHistoryUtil.getUserHistory();
  }

  private void initDatas(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      this.orderDishRoomInfo = ((DPObject)paramBundle.getParcelable("orderdishroominfo"));
      this.currentSort = paramBundle.getInt("sort", -2147483648);
      this.currentCategory = paramBundle.getInt("category", -2147483648);
      this.shopId = paramBundle.getInt("shopid", 0);
      this.listStyle = paramBundle.getInt("liststyle", 0);
      this.mMenuAdapter = new SelectDishMenuTogetherAdapter(this, this, this.shopId, this.currentSort, this.currentCategory);
      if (this.listStyle != 0)
        break label282;
      this.mMenuAdapter.setSmallStyle();
    }
    while (true)
    {
      this.peopleNum = this.orderDishRoomInfo.getInt("PeopleNum");
      this.roomId = this.orderDishRoomInfo.getInt("RoomId");
      this.roomName = this.orderDishRoomInfo.getString("RoomName");
      this.mCartManager.interval = this.orderDishRoomInfo.getInt("Interval");
      this.mCartManager.isOwner = this.orderDishRoomInfo.getInt("IsOwner");
      this.mCartManager.setShopIdandDealId(this.shopId, this.roomId);
      this.mCartManager.tableId = getStringParam("tablenum");
      this.mCartManager.addCartChangedListener(this.cartChangedListener);
      return;
      this.orderDishRoomInfo = getObjectParam("orderdishroominfo");
      this.currentSort = getIntParam("sort", -2147483648);
      this.currentCategory = getIntParam("category", -2147483648);
      this.shopId = getIntParam("shopid");
      this.listStyle = getIntParam("liststyle");
      break;
      label282: if (this.listStyle != 1)
        continue;
      this.mMenuAdapter.setBigStyle();
    }
  }

  private void initViews()
  {
    findViewById(R.id.sd_menu_title_bar).setBackgroundColor(getResources().getColor(R.color.white));
    ((RelativeLayout)findViewById(R.id.title_bar_content_container)).setPadding(ViewUtils.dip2px(this, 15.0F), 0, 0, 0);
    ((TextView)findViewById(R.id.title_bar_title)).setText("一起点");
    findViewById(R.id.left_view).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectDishMenuTogetherActivity.this.showQuitOrderDishRoomConfirmAlert();
      }
    });
    Object localObject = (NovaTextView)LayoutInflater.from(this).inflate(R.layout.title_bar_text, null, false);
    ((NovaTextView)localObject).setText("退出");
    LinearLayout localLinearLayout = (LinearLayout)findViewById(R.id.title_bar_left_view_container);
    localLinearLayout.addView((View)localObject);
    localLinearLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectDishMenuTogetherActivity.this.showQuitOrderDishRoomConfirmAlert();
        SelectDishMenuTogetherActivity.this.gaUserInfo.title = "";
        GAHelper.instance().contextStatisticsEvent(SelectDishMenuTogetherActivity.this, "quit", SelectDishMenuTogetherActivity.this.gaUserInfo, "tap");
      }
    });
    localObject = (NovaTextView)LayoutInflater.from(this).inflate(R.layout.title_bar_text, null, false);
    ((NovaTextView)localObject).setText("退出");
    localLinearLayout = (LinearLayout)findViewById(R.id.title_bar_right_view_container);
    localLinearLayout.addView((View)localObject);
    localLinearLayout.setPadding(ViewUtils.dip2px(this, 42.0F), 0, 0, 0);
    localLinearLayout.setVisibility(4);
    this.roomNameTextView = ((TextView)findViewById(R.id.sd_room_name_textview));
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(this.roomName).append("(").append(this.peopleNum).append("人)");
    this.roomNameTextView.setText(((StringBuilder)localObject).toString());
    ((TextView)findViewById(R.id.sd_room_password_textview)).setText(this.orderDishRoomInfo.getString("Password"));
    this.mLoadingView = findViewById(R.id.sd_menu_loading);
    this.mLoadingView.setVisibility(0);
    this.mLoadingErrorView = ((LoadingErrorView)findViewById(R.id.sd_menu_error));
    this.mLoadingErrorView.setCallBack(new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        SelectDishMenuTogetherActivity.this.requestShopInfo();
      }
    });
    this.mMenuContentView = findViewById(R.id.sd_menu_layout);
    this.mMenuContentView.setVisibility(4);
    this.mMenuView = ((GridView)findViewById(R.id.sd_menu_grid));
    if (this.listStyle == 0)
    {
      this.mMenuView.setNumColumns(1);
      this.mMenuView.setPadding(0, 0, 0, 0);
    }
    while (true)
    {
      this.filterBar = ((FilterBar)findViewById(R.id.sd_menu_filter));
      this.filterBar.setOnItemClickListener(new FilterBar.OnItemClickListener()
      {
        public void onClickItem(Object paramObject, View paramView)
        {
          if ("sort".equals(paramObject))
          {
            localListFilterDialog = new ListFilterDialog(SelectDishMenuTogetherActivity.this);
            localListFilterDialog.setItems(SelectDishMenuTogetherActivity.this.sorts);
            if (SelectDishMenuTogetherActivity.this.currentSort == -2147483648)
            {
              localDPObject = SelectDishMenuTogetherActivity.this.sorts[0];
              localListFilterDialog.setSelectedItem(localDPObject);
              localListFilterDialog.setOnFilterListener(new FilterDialog.OnFilterListener(paramObject)
              {
                public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
                {
                  paramObject = (DPObject)paramObject;
                  SelectDishMenuTogetherActivity.access$502(SelectDishMenuTogetherActivity.this, paramObject.getInt("ID"));
                  SelectDishMenuTogetherActivity.this.mMenuAdapter.setSort(SelectDishMenuTogetherActivity.this.currentSort);
                  SelectDishMenuTogetherActivity.this.updateFilter(this.val$tag, SelectDishMenuTogetherActivity.this.currentSort);
                  SelectDishMenuTogetherActivity.this.mMenuAdapter.reset();
                  SelectDishMenuTogetherActivity.this.gaUserInfo.title = String.valueOf(SelectDishMenuTogetherActivity.this.currentSort);
                  GAHelper.instance().contextStatisticsEvent(SelectDishMenuTogetherActivity.this, "sort", SelectDishMenuTogetherActivity.this.gaUserInfo, "tap");
                  paramFilterDialog.dismiss();
                }
              });
              localListFilterDialog.show(paramView);
            }
          }
          do
          {
            return;
            localDPObject = SelectDishMenuTogetherActivity.this.getSelectFilterItem(paramObject, SelectDishMenuTogetherActivity.this.currentSort);
            break;
          }
          while (!"category".equals(paramObject));
          ListFilterDialog localListFilterDialog = new ListFilterDialog(SelectDishMenuTogetherActivity.this);
          localListFilterDialog.setItems(SelectDishMenuTogetherActivity.this.categories);
          if (SelectDishMenuTogetherActivity.this.currentCategory == -2147483648);
          for (DPObject localDPObject = SelectDishMenuTogetherActivity.this.categories[0]; ; localDPObject = SelectDishMenuTogetherActivity.this.getSelectFilterItem(paramObject, SelectDishMenuTogetherActivity.this.currentCategory))
          {
            localListFilterDialog.setSelectedItem(localDPObject);
            localListFilterDialog.setOnFilterListener(new FilterDialog.OnFilterListener(paramObject)
            {
              public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
              {
                paramObject = (DPObject)paramObject;
                SelectDishMenuTogetherActivity.access$1002(SelectDishMenuTogetherActivity.this, paramObject.getInt("ID"));
                SelectDishMenuTogetherActivity.this.mMenuAdapter.setCategory(SelectDishMenuTogetherActivity.this.currentCategory);
                SelectDishMenuTogetherActivity.this.updateFilter(this.val$tag, SelectDishMenuTogetherActivity.this.currentCategory);
                SelectDishMenuTogetherActivity.this.mMenuAdapter.reset();
                SelectDishMenuTogetherActivity.this.gaUserInfo.title = String.valueOf(SelectDishMenuTogetherActivity.this.currentCategory);
                GAHelper.instance().contextStatisticsEvent(SelectDishMenuTogetherActivity.this, "category", SelectDishMenuTogetherActivity.this.gaUserInfo, "tap");
                paramFilterDialog.dismiss();
              }
            });
            localListFilterDialog.show(paramView);
            return;
          }
        }
      });
      this.cartMenuInfoView = ((SelectDishMenuCartView)findViewById(R.id.sd_menu_bottom_layout));
      this.cartMenuInfoView.setIsTogetherMenu(true);
      this.cartMenuInfoView.setCheckCartListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishMenuTogetherActivity.this.gaUserInfo.title = "";
          GAHelper.instance().contextStatisticsEvent(SelectDishMenuTogetherActivity.this, "cart", SelectDishMenuTogetherActivity.this.gaUserInfo, "tap");
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishtogethercart"));
          SelectDishMenuTogetherActivity.this.startActivity(paramView);
        }
      });
      updateCartInfoViews();
      return;
      if (this.listStyle != 1)
        continue;
      this.mMenuView.setNumColumns(2);
      this.mMenuView.setPadding(ViewUtils.dip2px(this, 11.0F), ViewUtils.dip2px(this, 17.0F), ViewUtils.dip2px(this, 11.0F), 0);
    }
  }

  private boolean isFirstTimeRequestDishMenu()
  {
    return (this.sorts == null) || (this.categories == null);
  }

  private void onRequestQuitOrderDishRoomFailed()
  {
    showToast("已退出");
    finishWithResult();
  }

  private void onRequestQuitOrderDishRoomFinish(MApiResponse paramMApiResponse)
  {
    String str1 = "已退出";
    Object localObject = str1;
    if (paramMApiResponse.result() != null)
    {
      localObject = str1;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiResponse = (DPObject)paramMApiResponse.result();
        String str2 = paramMApiResponse.getString("Key");
        paramMApiResponse = paramMApiResponse.getString("Value");
        localObject = str1;
        if ("1".equals(str2))
          localObject = paramMApiResponse;
      }
    }
    showToast((String)localObject);
    finishWithResult();
  }

  private void onRequestShopInfoFinish(MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if ((paramMApiResponse == null) && (!(paramMApiResponse instanceof DPObject)))
    {
      setLayoutsVisibleWithDifferentState(1);
      return;
    }
    paramMApiResponse = (DPObject)paramMApiResponse;
    if (paramMApiResponse.getBoolean("IsOrderDish"))
    {
      this.mCartManager.isEstimate = paramMApiResponse.getBoolean("IsEstimate");
      this.mCartManager.setSupportPrePay(paramMApiResponse.getBoolean("IsPrePaySupport"));
      this.mCartManager.setSupportTable(paramMApiResponse.getBoolean("TableIdSupport"));
      this.mCartManager.orderButtonSubTitle = paramMApiResponse.getString("OrderButtonSubTitle");
      setLayoutsVisibleWithDifferentState(2);
      this.mMenuView.setAdapter(this.mMenuAdapter);
      this.mMenuAdapter.reset();
      return;
    }
    setLayoutsVisibleWithDifferentState(3);
  }

  private void registerReceivers()
  {
    IntentFilter localIntentFilter = new IntentFilter("com.dianping.selectdish.resetmenu");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.menuReceiver, localIntentFilter);
    localIntentFilter = new IntentFilter("com.dianping.selectdish.updatedishrecommend");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.menuReceiver, localIntentFilter);
  }

  private void requestQuitOrderDishRoom()
  {
    showProgressDialog("请稍候...");
    if (this.requestQuitRoom != null)
      mapiService().abort(this.requestQuitRoom, this.requestHandler, true);
    this.requestQuitRoom = BasicMApiRequest.mapiPost("http://m.api.dianping.com/orderdish/quitorderdishroom.hbt", new String[] { "roomid", String.valueOf(this.roomId) });
    mapiService().exec(this.requestQuitRoom, this.requestHandler);
  }

  private void requestShopInfo()
  {
    setLayoutsVisibleWithDifferentState(2);
    if (this.mRequestShopInfo != null)
    {
      mapiService().abort(this.mRequestShopInfo, this.requestHandler, true);
      this.mRequestShopInfo = null;
    }
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/menuextrainfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    this.mRequestShopInfo = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mRequestShopInfo, this.requestHandler);
  }

  private void setLayoutsVisibleWithDifferentState(int paramInt)
  {
    int k = 8;
    int j = 0;
    Object localObject = this.mLoadingErrorView;
    int i;
    if (paramInt == 1)
    {
      i = 0;
      ((LoadingErrorView)localObject).setVisibility(i);
      localObject = this.mLoadingView;
      i = k;
      if (paramInt == 2)
        i = 0;
      ((View)localObject).setVisibility(i);
      localObject = this.mMenuContentView;
      if (paramInt != 4)
        break label69;
    }
    label69: for (paramInt = j; ; paramInt = 4)
    {
      ((View)localObject).setVisibility(paramInt);
      return;
      i = 8;
      break;
    }
  }

  private void showQuitOrderDishRoomConfirmAlert()
  {
    SelectDishAlertDialog localSelectDishAlertDialog = new SelectDishAlertDialog(this, "确定退出一起点吗？", "退出后您的购物车将被清空，\n是否确认退出？", "取消", "确定");
    localSelectDishAlertDialog.setCanceledOnTouchOutside(true);
    localSelectDishAlertDialog.setDialogClickListener(new SelectDishAlertDialog.OnDialogClickListener()
    {
      public void onCancelClick(Dialog paramDialog)
      {
        paramDialog.dismiss();
      }

      public void onConfirmClick(Dialog paramDialog)
      {
        SelectDishMenuTogetherActivity.this.gaUserInfo.title = "";
        GAHelper.instance().contextStatisticsEvent(SelectDishMenuTogetherActivity.this, "quit_confirm", SelectDishMenuTogetherActivity.this.gaUserInfo, "tap");
        paramDialog.dismiss();
        SelectDishMenuTogetherActivity.this.requestQuitOrderDishRoom();
        SelectDishMenuTogetherActivity.this.clearNormalDishCart();
      }
    });
    localSelectDishAlertDialog.show();
  }

  private void updateCartInfoViews()
  {
    int m = 1;
    int k = 0;
    int j = this.mCartManager.getTotalDishCount() + this.mCartManager.getTotalSelectFreeDishCount();
    int i = j;
    if (this.mCartManager.isOwner != 1)
      i = j + this.mCartManager.getOtherTotalCount();
    j = m;
    SelectDishMenuCartView localSelectDishMenuCartView;
    if (i <= 0)
    {
      if (this.mCartManager.hasHistoryFreeDish())
        j = m;
    }
    else
    {
      localSelectDishMenuCartView = this.cartMenuInfoView;
      if (j == 0)
        break label95;
    }
    label95: for (i = k; ; i = 8)
    {
      localSelectDishMenuCartView.setVisibility(i);
      this.cartMenuInfoView.refresh();
      return;
      j = 0;
      break;
    }
  }

  private void updateFilter(Object paramObject, int paramInt)
  {
    DPObject localDPObject = getSelectFilterItem(paramObject, paramInt);
    if (localDPObject != null)
      this.filterBar.setItem(paramObject, localDPObject.getString("Name"));
  }

  private void updateMenuSortsAndCategoriesViews(DPObject paramDPObject)
  {
    this.sorts = paramDPObject.getArray("MenuSortNavList");
    this.categories = paramDPObject.getArray("MenuCategoryNavList");
    this.currentSort = paramDPObject.getInt("CurrentSort");
    this.currentCategory = paramDPObject.getInt("CurrentCategory");
    this.mMenuAdapter.setSort(this.currentSort);
    this.mMenuAdapter.setCategory(this.currentCategory);
    if ((this.sorts == null) || (this.categories == null))
      return;
    this.filterBar.addItem("category", "category");
    this.filterBar.addItem("sort", "sort");
    int i = this.filterBar.getHeight();
    paramDPObject = new LinearLayout(this);
    paramDPObject.setOrientation(0);
    paramDPObject.setLayoutParams(new LinearLayout.LayoutParams(-2, i));
    View localView = new View(this);
    localView.setBackgroundColor(getResources().getColor(R.color.inner_divider));
    NovaImageView localNovaImageView = new NovaImageView(this);
    localNovaImageView.setGAString("showlist");
    localNovaImageView.setScaleType(ImageView.ScaleType.CENTER);
    localNovaImageView.setImageResource(R.drawable.sd_menustyle_small);
    localNovaImageView.setPadding(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 15.0F), 0);
    localNovaImageView.setOnClickListener(new View.OnClickListener(localNovaImageView)
    {
      public void onClick(View paramView)
      {
        SelectDishMenuTogetherActivity.this.gaUserInfo.title = "";
        GAHelper.instance().contextStatisticsEvent(SelectDishMenuTogetherActivity.this, "showlist", SelectDishMenuTogetherActivity.this.gaUserInfo, "tap");
        if (SelectDishMenuTogetherActivity.this.mMenuAdapter.isCurrentStyleSmall())
        {
          SelectDishMenuTogetherActivity.this.mMenuAdapter.setBigStyle();
          SelectDishMenuTogetherActivity.this.mMenuView.setNumColumns(2);
          SelectDishMenuTogetherActivity.this.mMenuView.setPadding(ViewUtils.dip2px(SelectDishMenuTogetherActivity.this, 11.0F), ViewUtils.dip2px(SelectDishMenuTogetherActivity.this, 17.0F), ViewUtils.dip2px(SelectDishMenuTogetherActivity.this, 11.0F), 0);
          this.val$styleView.setImageResource(R.drawable.sd_menustyle_small);
          return;
        }
        SelectDishMenuTogetherActivity.this.mMenuAdapter.setSmallStyle();
        SelectDishMenuTogetherActivity.this.mMenuView.setNumColumns(1);
        SelectDishMenuTogetherActivity.this.mMenuView.setPadding(0, 0, 0, 0);
        this.val$styleView.setImageResource(R.drawable.sd_menustyle_big);
      }
    });
    paramDPObject.addView(localView, new LinearLayout.LayoutParams(1, i));
    paramDPObject.addView(localNovaImageView, new LinearLayout.LayoutParams(-2, i));
    this.filterBar.addView(paramDPObject, new LinearLayout.LayoutParams(-2, i));
    updateFilter("sort", this.currentSort);
    updateFilter("category", this.currentCategory);
  }

  private void updateSingleRow(int paramInt)
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

  public void dealData(boolean paramBoolean1, Object paramObject, boolean paramBoolean2)
  {
    if (!paramBoolean1)
    {
      if (paramBoolean2)
      {
        setLayoutsVisibleWithDifferentState(1);
        return;
      }
      setLayoutsVisibleWithDifferentState(4);
      return;
    }
    if ((paramObject == null) || (!(paramObject instanceof DPObject)))
    {
      if (paramBoolean2)
      {
        setLayoutsVisibleWithDifferentState(1);
        return;
      }
      setLayoutsVisibleWithDifferentState(4);
      return;
    }
    paramObject = (DPObject)paramObject;
    if (isFirstTimeRequestDishMenu())
    {
      updateMenuSortsAndCategoriesViews(paramObject);
      if (DishLikeManager.getInstance().recommendInfos != null)
      {
        DishLikeManager.getInstance().recommendInfos.clear();
        DishLikeManager.getInstance().addRecommendInfos(paramObject.getArray("List"));
      }
    }
    if (this.mCartManager.average == 0.0D)
      this.mCartManager.average = paramObject.getInt("AvgPrice");
    if (this.shopName == null)
    {
      this.shopName = paramObject.getString("ShopName");
      this.mCartManager.setShopName(this.shopName);
    }
    setLayoutsVisibleWithDifferentState(4);
  }

  public String getPageName()
  {
    return "menuorder_grouporder";
  }

  public void gotoDishDetail(DishInfo paramDishInfo)
  {
    if (paramDishInfo != null)
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishdetail"));
      localIntent.putExtra("detail", paramDishInfo);
      localIntent.putExtra("istogethermenu", true);
      startActivity(localIntent);
      this.gaUserInfo.title = String.valueOf(paramDishInfo.dishId);
      GAHelper.instance().contextStatisticsEvent(this, "detail", this.gaUserInfo, "tap");
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onBackPressed()
  {
    showQuitOrderDishRoomConfirmAlert();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.selectdish_menu_together_layout);
    GAHelper.instance().setGAPageName(getPageName());
    initDatas(paramBundle);
    initViews();
    registerReceivers();
    requestShopInfo();
    if ((accountService().token() != null) && (!this.mCartManager.hasHistoryFreeDish()) && (this.mCartManager.isOwner == 1))
      getUserHistory();
    this.mCartManager.startPollingRequeset();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.menuReceiver);
    this.mCartManager.removeCartChangedListener(this.cartChangedListener);
    this.mCartManager.stopPollingRequeset();
    if (this.mRequestShopInfo != null)
    {
      mapiService().abort(this.mRequestShopInfo, this.requestHandler, true);
      this.mRequestShopInfo = null;
    }
    if (this.requestQuitRoom != null)
    {
      mapiService().abort(this.requestQuitRoom, this.requestHandler, true);
      this.requestQuitRoom = null;
    }
    if (this.getHistoryUtil != null)
    {
      this.getHistoryUtil.releaseHistoryRequest();
      this.getHistoryUtil.removeGetHistoryListener();
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("orderdishroominfo", this.orderDishRoomInfo);
    paramBundle.putInt("sort", this.currentSort);
    paramBundle.putInt("category", this.currentCategory);
    paramBundle.putInt("shopid", this.shopId);
    paramBundle.putInt("liststyle", this.listStyle);
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
          SelectDishMenuTogetherActivity.this.startAddDishAnimation(this.val$view, this.val$duration, this.val$dishInfo);
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
        SelectDishMenuTogetherActivity.this.gaUserInfo.title = String.valueOf(this.val$dishInfo.dishId);
        GAHelper.instance().contextStatisticsEvent(SelectDishMenuTogetherActivity.this, "addcart", SelectDishMenuTogetherActivity.this.gaUserInfo, "tap");
        this.val$frameLayout.removeView(this.val$movingView);
        SelectDishMenuTogetherActivity.this.mCartManager.addDish(this.val$dishInfo);
      }
    });
    localSelectDishCartAnimationManager.startAddDishAnimation(localImageView, paramView, localView, paramInt, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishMenuTogetherActivity
 * JD-Core Version:    0.6.0
 */