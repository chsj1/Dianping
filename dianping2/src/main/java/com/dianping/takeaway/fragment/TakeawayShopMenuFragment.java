package com.dianping.takeaway.fragment;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.NumOperateButton;
import com.dianping.base.widget.PortableScrollView;
import com.dianping.base.widget.PortableScrollView.OnItemOperationListener;
import com.dianping.model.Location;
import com.dianping.takeaway.activity.TakeawayMenuActivity;
import com.dianping.takeaway.entity.CategoryAdapter;
import com.dianping.takeaway.entity.DishOperation;
import com.dianping.takeaway.entity.TakeawayDishInfo;
import com.dianping.takeaway.entity.TakeawayDishMenuAdapter;
import com.dianping.takeaway.entity.TakeawayDishMenuDataSource;
import com.dianping.takeaway.entity.TakeawayDishMenuDataSource.DataLoadListener;
import com.dianping.takeaway.entity.TakeawayDishMenuDataSource.DataStatus;
import com.dianping.takeaway.entity.TakeawayMenuCategory;
import com.dianping.takeaway.entity.TakeawayNetLoadStatus;
import com.dianping.takeaway.entity.TakeawayShopInfo;
import com.dianping.takeaway.util.TakeawayCarCacheManager;
import com.dianping.takeaway.util.TakeawayGAManager;
import com.dianping.takeaway.view.TAToastView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TakeawayShopMenuFragment extends NovaFragment
  implements PortableScrollView.OnItemOperationListener, TakeawayDishMenuDataSource.DataLoadListener
{
  private LinearLayout actiContentView;
  private View actiDividerView;
  TakeawayMenuActivity activity;
  private View activityView;
  private TextView annoContentView;
  private View announceView;
  private TextView bannerContentView;
  private ImageView bannerIconView;
  private NovaRelativeLayout bannerLayout;
  private TextView compContentView;
  private TextView deliContentView;
  private View deliDividerView;
  private View deliveryView;
  public TakeawayDishMenuDataSource dishMenuDataSource;
  private TextView firTagView;
  LayoutInflater inflater;
  private TakeawayDishMenuAdapter menuAdapter;
  private PortableScrollView menuCategoryView;
  private ListView menuListView;
  private View recomCateDividerView;
  private TextView recomCateNameView;
  private View recomCateView;
  private TakeawayDishMenuAdapter recomMenuAdapter;
  private ListView recomMenuListView;
  private TextView secTagView;
  private Dialog shopInfoDialog;
  private LinearLayout statusLayout;

  private void displayCategoryBar(View paramView, int paramInt)
  {
    paramView = paramView.findViewById(R.id.bar_view);
    if (paramView.getHeight() == 0)
      paramView.setLayoutParams(new RelativeLayout.LayoutParams(ViewUtils.dip2px(this.activity, 4.0F), paramInt));
    paramView.setVisibility(0);
  }

  private void initView(View paramView)
  {
    this.shopInfoDialog = new Dialog(this.activity, R.style.dialog);
    View localView = this.inflater.inflate(R.layout.takeaway_menu_shopinfo_dialog, null, false);
    this.announceView = localView.findViewById(R.id.announce_layout);
    this.annoContentView = ((TextView)localView.findViewById(R.id.announce_content));
    this.actiDividerView = localView.findViewById(R.id.announce_divider);
    this.activityView = localView.findViewById(R.id.activity_layout);
    this.actiContentView = ((LinearLayout)localView.findViewById(R.id.activity_content));
    this.deliDividerView = localView.findViewById(R.id.activity_divider);
    this.deliveryView = localView.findViewById(R.id.delivery_layout);
    this.deliContentView = ((TextView)localView.findViewById(R.id.delivery_content));
    this.compContentView = ((TextView)localView.findViewById(R.id.compensate_content));
    localView.findViewById(R.id.image_close).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayShopMenuFragment.this.shopInfoDialog.dismiss();
      }
    });
    this.shopInfoDialog.setContentView(localView);
    this.shopInfoDialog.setCanceledOnTouchOutside(true);
    this.bannerLayout = ((NovaRelativeLayout)paramView.findViewById(R.id.takeaway_shopmenu_banner));
    this.bannerIconView = ((ImageView)paramView.findViewById(R.id.icon));
    this.firTagView = ((TextView)paramView.findViewById(R.id.fir_tag));
    this.secTagView = ((TextView)paramView.findViewById(R.id.sec_tag));
    this.bannerContentView = ((TextView)paramView.findViewById(R.id.content));
    this.recomCateView = paramView.findViewById(R.id.recommend_category_layout);
    this.recomCateDividerView = paramView.findViewById(R.id.recommend_category_divider);
    this.recomCateNameView = ((TextView)paramView.findViewById(R.id.recommend_category_name));
    this.recomCateView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayShopMenuFragment.this.statisticsEvent("takeaway6", "takeaway6_dish_dpcategoryclk", TakeawayShopMenuFragment.this.dishMenuDataSource.shopID, 0);
        paramView = TakeawayShopMenuFragment.this.dishMenuDataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(TakeawayShopMenuFragment.this.activity, "dpcategory", paramView, "tap");
        if ((TakeawayShopMenuFragment.this.recomMenuListView == null) || (TakeawayShopMenuFragment.this.recomMenuListView.getVisibility() == 0))
          return;
        TakeawayShopMenuFragment.this.recomMenuAdapter.notifyDataSetChanged();
        TakeawayShopMenuFragment.this.recomCateNameView.setTextColor(TakeawayShopMenuFragment.this.getResources().getColor(R.color.deep_gray));
        TakeawayShopMenuFragment.this.displayCategoryBar(TakeawayShopMenuFragment.this.recomCateView, TakeawayShopMenuFragment.this.recomCateView.getMeasuredHeight());
        TakeawayShopMenuFragment.this.recomMenuListView.setSelection(0);
        TakeawayShopMenuFragment.this.recomMenuListView.setVisibility(0);
        TakeawayShopMenuFragment.this.menuCategoryView.setSelectedIndex(-1);
        TakeawayShopMenuFragment.this.menuListView.setVisibility(8);
      }
    });
    this.menuCategoryView = ((PortableScrollView)paramView.findViewById(R.id.takeaway_shopmenu_filter));
    this.menuCategoryView.setAdapter(new CategoryAdapter(this.activity, this.dishMenuDataSource.mCategories));
    this.menuCategoryView.setOnChangeSelectedItemListener(this);
    this.menuCategoryView.setSelectedIndex(0);
    this.recomMenuListView = ((ListView)paramView.findViewById(R.id.recommend_menu_list));
    this.recomMenuAdapter = new TakeawayDishMenuAdapter(this.activity, this.dishMenuDataSource, false, true, this.dishMenuDataSource.recomDishInfoList);
    this.recomMenuListView.setAdapter(this.recomMenuAdapter);
    this.recomMenuListView.setOnItemClickListener(this.recomMenuAdapter);
    this.menuListView = ((ListView)paramView.findViewById(R.id.takeaway_shopmenu_list_list));
    this.menuAdapter = new TakeawayDishMenuAdapter(this.activity, this.dishMenuDataSource, false, false, this.dishMenuDataSource.dishInfoList);
    this.menuListView.setAdapter(this.menuAdapter);
    this.menuListView.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      int menuListScrollState;

      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
        if (this.menuListScrollState == 0);
        do
        {
          do
          {
            do
              return;
            while (TakeawayShopMenuFragment.this.menuAdapter.getItem(paramInt1) == null);
            paramAbsListView = (TakeawayDishInfo)TakeawayShopMenuFragment.this.menuAdapter.getItem(paramInt1);
          }
          while (paramAbsListView.categoryName.equals(TakeawayShopMenuFragment.this.dishMenuDataSource.curCategoryName));
          TakeawayShopMenuFragment.this.dishMenuDataSource.curCategoryName = paramAbsListView.categoryName;
        }
        while (TakeawayShopMenuFragment.this.menuCategoryView == null);
        paramAbsListView = TakeawayShopMenuFragment.this.menuCategoryView.getChildViewByTag(TakeawayShopMenuFragment.this.dishMenuDataSource.curCategoryName);
        TakeawayShopMenuFragment.this.menuCategoryView.setSelectedIndex(((Integer)paramAbsListView.getTag(1073741823)).intValue());
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
        this.menuListScrollState = paramInt;
      }
    });
    this.menuListView.setOnItemClickListener(this.menuAdapter);
    this.statusLayout = ((LinearLayout)paramView.findViewById(R.id.statusLayout));
    this.activity.cartAdapter = new TakeawayDishMenuAdapter(this.activity, this.dishMenuDataSource, true, false, this.activity.cartList);
    this.activity.cartListView.setAdapter(this.activity.cartAdapter);
  }

  public static TakeawayShopMenuFragment newInstance(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    TakeawayShopMenuFragment localTakeawayShopMenuFragment = new TakeawayShopMenuFragment();
    Bundle localBundle = new Bundle();
    localBundle.putInt("source", paramInt);
    localBundle.putString("shopid", paramString1);
    localBundle.putString("queryid", paramString2);
    localBundle.putString("address", paramString3);
    localBundle.putString("lat", paramString4);
    localBundle.putString("lng", paramString5);
    localTakeawayShopMenuFragment.setArguments(localBundle);
    return localTakeawayShopMenuFragment;
  }

  private void refreshData()
  {
    TakeawayGAManager.statistics_takeaway6_dish_normal(this.activity, this.dishMenuDataSource.shopID, String.valueOf(this.dishMenuDataSource.source));
    Object localObject1 = this.dishMenuDataSource.getGAUserInfo();
    GAHelper.instance().contextStatisticsEvent(this.activity, "normal", (GAUserInfo)localObject1, "view");
    Object localObject3 = "";
    localObject1 = "";
    Object localObject2 = "";
    Object localObject7 = new ArrayList();
    int i;
    Object localObject6;
    Object localObject4;
    Object localObject5;
    int j;
    if ((this.dishMenuDataSource.mShopInfo.activity != null) && (this.dishMenuDataSource.mShopInfo.activity.length > 0))
    {
      i = 1;
      localObject6 = localObject3;
      localObject4 = localObject2;
      localObject5 = localObject1;
      if (i == 0)
        break label262;
      DPObject[] arrayOfDPObject = this.dishMenuDataSource.mShopInfo.activity;
      int k = arrayOfDPObject.length;
      j = 0;
      label134: localObject6 = localObject3;
      localObject4 = localObject2;
      localObject5 = localObject1;
      if (j >= k)
        break label262;
      localObject5 = arrayOfDPObject[j];
      localObject6 = localObject5.getObject("ActivityButton");
      localObject4 = localObject5.getString("ActivityInfo");
      switch (((DPObject)localObject6).getInt("Type"))
      {
      default:
      case 10:
      case 11:
      case 12:
      case 13:
      }
    }
    while (true)
    {
      j += 1;
      break label134;
      i = 0;
      break;
      localObject1 = localObject4;
      continue;
      ((List)localObject7).add(localObject5);
      continue;
      localObject3 = localObject4;
      continue;
      localObject2 = localObject4;
    }
    label262: ViewUtils.setVisibilityAndContent(this.annoContentView, (String)localObject6);
    this.announceView.setVisibility(this.annoContentView.getVisibility());
    this.actiContentView.removeAllViews();
    if (!((List)localObject7).isEmpty())
    {
      localObject3 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject3).setMargins(0, 0, 0, ViewUtils.dip2px(this.activity, 8.0F));
      localObject6 = ((List)localObject7).iterator();
      while (((Iterator)localObject6).hasNext())
      {
        localObject2 = (DPObject)((Iterator)localObject6).next();
        if (localObject2 == null)
          continue;
        localObject1 = "";
        localObject7 = ((DPObject)localObject2).getObject("ActivityButton");
        if (localObject7 != null)
        {
          if (((DPObject)localObject7).getString("Message") == null)
            localObject1 = "";
        }
        else
        {
          label398: localObject2 = ((DPObject)localObject2).getString("ActivityInfo");
          if (localObject2 != null)
            break label504;
          localObject2 = "";
        }
        label504: 
        while (true)
        {
          localObject7 = this.inflater.inflate(R.layout.takeaway_shop_item_activity_tag, null, false);
          ((TextView)((View)localObject7).findViewById(R.id.tag_icon)).setText((CharSequence)localObject1);
          localObject1 = (TextView)((View)localObject7).findViewById(R.id.tag_content);
          ((TextView)localObject1).setTextColor(getResources().getColor(R.color.text_gray));
          ((TextView)localObject1).setText((CharSequence)localObject2);
          ((View)localObject7).setLayoutParams((ViewGroup.LayoutParams)localObject3);
          this.actiContentView.addView((View)localObject7);
          break;
          localObject1 = ((DPObject)localObject7).getString("Message");
          break label398;
        }
      }
      this.activityView.setVisibility(0);
      this.actiContentView.setVisibility(0);
      this.actiDividerView.setVisibility(this.announceView.getVisibility());
      ViewUtils.setVisibilityAndContent(this.deliContentView, localObject5);
      ViewUtils.setVisibilityAndContent(this.compContentView, (String)localObject4);
      if ((TextUtils.isEmpty(localObject5)) && (TextUtils.isEmpty((CharSequence)localObject4)))
        break label768;
      j = 1;
      label574: if (j == 0)
        break label781;
      this.deliveryView.setVisibility(0);
      localObject1 = this.deliDividerView;
      if ((this.activityView.getVisibility() != 0) && (this.announceView.getVisibility() != 0))
        break label774;
      j = 0;
      label615: ((View)localObject1).setVisibility(j);
      label621: if ((this.dishMenuDataSource.mShopInfo.notice != null) && (!TextUtils.isEmpty(this.dishMenuDataSource.mShopInfo.notice.getString("Msg"))) && (i != 0))
        break label802;
      this.bannerLayout.setVisibility(8);
      if ((this.dishMenuDataSource.recomCategory != null) && (this.dishMenuDataSource.recomDishInfoList.size() > 1))
        break label1043;
      this.recomCateView.setVisibility(8);
      this.recomCateDividerView.setVisibility(8);
    }
    while (true)
    {
      this.menuCategoryView.notifyDataSetChanged();
      broadcastChange(null);
      this.activity.setupCartBarView(this.dishMenuDataSource);
      return;
      this.activityView.setVisibility(8);
      this.actiContentView.setVisibility(8);
      this.actiDividerView.setVisibility(8);
      break;
      label768: j = 0;
      break label574;
      label774: j = 8;
      break label615;
      label781: this.deliveryView.setVisibility(8);
      this.deliDividerView.setVisibility(8);
      break label621;
      label802: switch (this.dishMenuDataSource.mShopInfo.notice.getInt("Type"))
      {
      case 11:
      default:
        this.bannerIconView.setVisibility(8);
        label857: localObject1 = this.dishMenuDataSource.mShopInfo.notice.getStringArray("ButtonList");
        if ((localObject1 != null) && (localObject1.length > 0))
        {
          ViewUtils.setVisibilityAndContent(this.firTagView, localObject1[0]);
          if (localObject1.length <= 1)
            break;
          ViewUtils.setVisibilityAndContent(this.secTagView, localObject1[1]);
        }
      case 10:
      case 13:
      case 12:
      }
      while (true)
      {
        this.bannerContentView.setText(this.dishMenuDataSource.mShopInfo.notice.getString("Msg"));
        this.bannerLayout.setGAString("detail");
        this.bannerLayout.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            TakeawayShopMenuFragment.this.shopInfoDialog.show();
            TakeawayShopMenuFragment.this.statisticsEvent("takeaway6", "takeaway6_dish_shopdetail_clk", "", 0);
          }
        });
        this.bannerLayout.setVisibility(0);
        break;
        this.bannerIconView.setImageResource(R.drawable.wm_sendicon);
        this.bannerIconView.setVisibility(0);
        break label857;
        this.bannerIconView.setImageResource(R.drawable.wm_tipsicon);
        this.bannerIconView.setVisibility(0);
        break label857;
        this.secTagView.setVisibility(8);
        continue;
        this.firTagView.setVisibility(8);
        this.secTagView.setVisibility(8);
      }
      label1043: this.recomCateView.setVisibility(0);
      this.recomCateNameView.setText(this.dishMenuDataSource.recomCategory.categoryName);
      this.recomCateDividerView.setVisibility(0);
      localObject1 = this.dishMenuDataSource.getGAUserInfo();
      GAHelper.instance().contextStatisticsEvent(this.activity, "dpcategory", (GAUserInfo)localObject1, "view");
    }
  }

  private void showErrorDialog(String paramString)
  {
    new AlertDialog.Builder(getActivity()).setMessage(paramString).setCancelable(false).setPositiveButton("知道了", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
        TakeawayShopMenuFragment.this.getActivity().finish();
      }
    }).show();
  }

  private void submitOrder(DPObject paramDPObject)
  {
    if (this.activity.cartView.isShown())
      this.activity.closeCartBtn.performClick();
    if (paramDPObject == null)
      return;
    DPObject localDPObject = this.menuAdapter.getContents();
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayorder"));
    localIntent.putExtra("shopid", this.dishMenuDataSource.shopID);
    localIntent.putExtra("queryid", this.dishMenuDataSource.queryId);
    localIntent.putExtra("content", localDPObject);
    localIntent.putExtra("useraddress", paramDPObject);
    if (this.dishMenuDataSource.mShopInfo != null)
      localIntent.putExtra("carrier", this.dishMenuDataSource.mShopInfo.carrier);
    if (TextUtils.isEmpty(this.dishMenuDataSource.lat))
    {
      paramDPObject = "0.0";
      localIntent.putExtra("initiallat", paramDPObject);
      if (!TextUtils.isEmpty(this.dishMenuDataSource.lng))
        break label254;
    }
    label254: for (paramDPObject = "0.0"; ; paramDPObject = this.dishMenuDataSource.lng)
    {
      localIntent.putExtra("initiallng", paramDPObject);
      this.dishMenuDataSource.getClass();
      startActivityForResult(localIntent, 3);
      TakeawayGAManager.statistics_takeaway6_dish_next(this.activity, this.dishMenuDataSource.shopID, this.dishMenuDataSource.queryId);
      paramDPObject = this.dishMenuDataSource.getGAUserInfo();
      GAHelper.instance().contextStatisticsEvent(this.activity, "next", paramDPObject, "tap");
      return;
      paramDPObject = this.dishMenuDataSource.lat;
      break;
    }
  }

  private void updateAllMenuCategorySelectedNum()
  {
    if ((this.menuCategoryView != null) && (this.dishMenuDataSource.mCategories != null) && (!this.dishMenuDataSource.mCategories.isEmpty()))
    {
      this.menuCategoryView.setSelectedIndex(0);
      this.menuListView.setSelection(((TakeawayMenuCategory)this.dishMenuDataSource.mCategories.get(0)).startLine + this.menuListView.getHeaderViewsCount());
      int i = 0;
      while (i < this.dishMenuDataSource.mCategories.size())
      {
        updateMenuCategorySelectedNum(((TakeawayMenuCategory)this.dishMenuDataSource.mCategories.get(i)).categoryName);
        i += 1;
      }
    }
  }

  private void updateMenuCategorySelectedNum(String paramString)
  {
    Integer localInteger = (Integer)this.dishMenuDataSource.categorySelectedNumMap.get(paramString);
    paramString = this.menuCategoryView.getChildViewByTag(paramString);
    TextView localTextView;
    if (paramString != null)
    {
      localTextView = (TextView)paramString.findViewById(R.id.selected_num);
      if ((localInteger == null) || (!this.dishMenuDataSource.mShopInfo.isAvailable))
        break label88;
      if (localInteger.intValue() >= 100)
        break label81;
    }
    label81: for (paramString = localInteger; ; paramString = "n+")
    {
      localTextView.setText(String.valueOf(paramString));
      localTextView.setVisibility(0);
      return;
    }
    label88: localTextView.setVisibility(8);
  }

  private void updateSelectedData()
  {
    this.activity.cartList.clear();
    this.dishMenuDataSource.categorySelectedNumMap.clear();
    Iterator localIterator = this.dishMenuDataSource.dishInfoList.iterator();
    while (localIterator.hasNext())
    {
      TakeawayDishInfo localTakeawayDishInfo = (TakeawayDishInfo)localIterator.next();
      if (!localTakeawayDishInfo.hasOrder())
        continue;
      this.activity.cartList.add(localTakeawayDishInfo);
      Integer localInteger2 = (Integer)this.dishMenuDataSource.categorySelectedNumMap.get(localTakeawayDishInfo.categoryName);
      Integer localInteger1 = localInteger2;
      if (localInteger2 == null)
        localInteger1 = Integer.valueOf(0);
      this.dishMenuDataSource.categorySelectedNumMap.put(localTakeawayDishInfo.categoryName, Integer.valueOf(localTakeawayDishInfo.selectedNum + localInteger1.intValue()));
    }
  }

  public void broadcastChange(DishOperation paramDishOperation)
  {
    if (paramDishOperation != null)
    {
      TakeawayDishInfo localTakeawayDishInfo = paramDishOperation.dishItem;
      paramDishOperation.operateButton.setCurrentValue(localTakeawayDishInfo.selectedNum);
    }
    updateSelectedData();
    this.menuAdapter.notifyDataSetChanged();
    if (this.recomMenuListView.getVisibility() == 0)
      this.recomMenuAdapter.notifyDataSetChanged();
    if (paramDishOperation != null)
    {
      updateMenuCategorySelectedNum(paramDishOperation.dishItem.categoryName);
      return;
    }
    updateAllMenuCategorySelectedNum();
  }

  public void clickItem(View paramView, int paramInt)
  {
    int j = 0;
    int i;
    label57: GAUserInfo localGAUserInfo;
    if ((this.dishMenuDataSource.mCategories != null) && (this.dishMenuDataSource.mCategories.size() > paramInt))
    {
      i = 1;
      if (i == 0)
        break label199;
      paramView = ((TakeawayMenuCategory)this.dishMenuDataSource.mCategories.get(paramInt)).categoryName;
      statisticsEvent("takeaway6", "takeaway6_dish_catchose", paramView, 0);
      statisticsEvent("takeaway6", "takeaway6_dish_category_click", String.valueOf(paramInt), 0);
      localGAUserInfo = this.dishMenuDataSource.getGAUserInfo();
      if (i == 0)
        break label204;
    }
    label199: label204: for (paramView = ((TakeawayMenuCategory)this.dishMenuDataSource.mCategories.get(paramInt)).categoryName; ; paramView = "")
    {
      localGAUserInfo.title = paramView;
      localGAUserInfo.index = Integer.valueOf(paramInt);
      GAHelper.instance().contextStatisticsEvent(this.activity, "category", localGAUserInfo, "tap");
      paramView = this.menuListView;
      if (i != 0)
        j = ((TakeawayMenuCategory)this.dishMenuDataSource.mCategories.get(paramInt)).startLine;
      paramView.setSelection(this.menuListView.getHeaderViewsCount() + j);
      return;
      i = 0;
      break;
      paramView = null;
      break label57;
    }
  }

  public void gotoAddressManager(int paramInt)
  {
    double d2 = 0.0D;
    Object localObject = new StringBuilder("dianping://takeawayaddress");
    ((StringBuilder)localObject).append("?source=").append(this.dishMenuDataSource.source);
    ((StringBuilder)localObject).append("&shopid=").append(this.dishMenuDataSource.shopID);
    ((StringBuilder)localObject).append("&queryid=").append(this.dishMenuDataSource.queryId);
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(((StringBuilder)localObject).toString())), paramInt);
    localObject = location();
    double d1;
    if (localObject != null)
      d1 = ((Location)localObject).latitude();
    while (true)
    {
      if (localObject != null)
        d2 = ((Location)localObject).longitude();
      TakeawayGAManager.statistics_takeaway6_dishtips_ood_add(this.activity, this.dishMenuDataSource.shopID, String.valueOf(d1), String.valueOf(d2));
      return;
      d1 = 0.0D;
    }
  }

  public void gotoShoplist()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayshoplist"));
    localIntent.putExtra("address", this.dishMenuDataSource.inputAddress);
    if ((!TextUtils.isEmpty(this.dishMenuDataSource.lat)) && (!TextUtils.isEmpty(this.dishMenuDataSource.lng)))
    {
      localIntent.putExtra("lat", String.valueOf(this.dishMenuDataSource.lat));
      localIntent.putExtra("lng", String.valueOf(this.dishMenuDataSource.lng));
    }
    startActivity(localIntent);
    this.activity.finish();
    TakeawayGAManager.statistics_takeaway6_dishtips_ood_shop(this.activity, this.dishMenuDataSource.shopID, String.valueOf(this.dishMenuDataSource.lat), String.valueOf(this.dishMenuDataSource.lng));
  }

  public void loadDishFinsh(TakeawayDishMenuDataSource.DataStatus paramDataStatus, Object paramObject)
  {
    switch (8.$SwitchMap$com$dianping$takeaway$entity$TakeawayDishMenuDataSource$DataStatus[paramDataStatus.ordinal()])
    {
    default:
      return;
    case 1:
      this.dishMenuDataSource.getClass();
      gotoAddressManager(2);
      return;
    case 2:
      this.statusLayout.removeAllViews();
      paramDataStatus = this.inflater.inflate(R.layout.takeaway_menu_networkerror, this.statusLayout, false);
      ((Button)paramDataStatus.findViewById(R.id.takeaway_shopmenu_networkerror_button_inputaddress)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayShopMenuFragment.this.dishMenuDataSource.reloadData();
        }
      });
      this.statusLayout.addView(paramDataStatus);
      this.statusLayout.setVisibility(0);
      this.activity.overallStatusView.setVisibility(8);
      return;
    case 3:
      this.activity.showOverRangeActivity(this.dishMenuDataSource);
      return;
    case 4:
      this.activity.showUnknownAddressErrorView(this.dishMenuDataSource);
      return;
    case 5:
    }
    showErrorDialog(paramObject.toString());
  }

  public void loadDishMenu(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (8.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    case 2:
    default:
      return;
    case 1:
      refreshData();
      this.statusLayout.setVisibility(8);
      this.activity.overallStatusView.setVisibility(8);
      return;
    case 3:
    }
    this.statusLayout.removeAllViews();
    this.inflater.inflate(R.layout.takeaway_menu_waiting, this.statusLayout, true);
    this.statusLayout.setVisibility(0);
    this.activity.overallStatusView.setVisibility(8);
  }

  public void loadSuitableAddress(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    this.activity.toastView.hideToast();
    switch (8.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
      return;
    case 2:
      showToast("网络不给力");
      return;
    case 1:
    }
    paramTakeawayNetLoadStatus = (DPObject)paramObject;
    switch (paramTakeawayNetLoadStatus.getInt("Status"))
    {
    default:
      return;
    case 1:
      submitOrder(paramTakeawayNetLoadStatus.getObject("SuitableAddress"));
      return;
    case 2:
      paramTakeawayNetLoadStatus = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayaddressmodify?shopid=" + this.dishMenuDataSource.shopID));
      this.dishMenuDataSource.getClass();
      startActivityForResult(paramTakeawayNetLoadStatus, 4);
      statisticsEvent("takeaway6", "takeaway6_dish_noadd", "", 0);
      return;
    case 3:
    }
    paramTakeawayNetLoadStatus = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayaddresslist?shopid=" + this.dishMenuDataSource.shopID));
    this.dishMenuDataSource.getClass();
    startActivityForResult(paramTakeawayNetLoadStatus, 4);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.dishMenuDataSource.loadData();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    this.dishMenuDataSource.getClass();
    if (paramInt1 == 3)
    {
      this.dishMenuDataSource.getClass();
      if (paramInt2 == 6)
        this.activity.finish();
    }
    do
      while (true)
      {
        return;
        this.dishMenuDataSource.getClass();
        TakeawayDishMenuDataSource localTakeawayDishMenuDataSource;
        if (paramInt2 == 3)
        {
          localTakeawayDishMenuDataSource = this.dishMenuDataSource;
          if (paramIntent != null);
          for (paramIntent = paramIntent.getStringExtra("address"); ; paramIntent = this.dishMenuDataSource.inputAddress)
          {
            localTakeawayDishMenuDataSource.inputAddress = paramIntent;
            if (TextUtils.isEmpty(this.dishMenuDataSource.inputAddress))
              break;
            this.dishMenuDataSource.loadData();
            return;
          }
        }
        this.dishMenuDataSource.getClass();
        if (paramInt2 == 5)
        {
          TakeawayCarCacheManager.clearCacheMenu(this.activity);
          this.dishMenuDataSource.loadData();
          return;
        }
        this.dishMenuDataSource.getClass();
        if (paramInt2 != 9)
          continue;
        this.dishMenuDataSource.loadData();
        return;
        this.dishMenuDataSource.getClass();
        if (paramInt1 == 2)
        {
          if (paramIntent == null)
            continue;
          localTakeawayDishMenuDataSource = this.dishMenuDataSource;
          this.dishMenuDataSource.getClass();
          localTakeawayDishMenuDataSource.source = 2;
          this.dishMenuDataSource.inputAddress = paramIntent.getStringExtra("Address");
          this.dishMenuDataSource.lat = String.valueOf(paramIntent.getDoubleExtra("Lat", 0.0D));
          this.dishMenuDataSource.lng = String.valueOf(paramIntent.getDoubleExtra("Lng", 0.0D));
          this.dishMenuDataSource.loadData();
          return;
        }
        this.dishMenuDataSource.getClass();
        if (paramInt1 == 1)
        {
          gotoShoplist();
          return;
        }
        this.dishMenuDataSource.getClass();
        if (paramInt1 != 4)
          break;
        if (paramIntent == null)
          continue;
        submitOrder((DPObject)paramIntent.getParcelableExtra("address"));
        return;
      }
    while (paramInt2 != 0);
    this.activity.finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.activity = ((TakeawayMenuActivity)getActivity());
    this.inflater = LayoutInflater.from(this.activity);
    this.dishMenuDataSource = new TakeawayDishMenuDataSource(this.activity);
    this.dishMenuDataSource.setDataLoadListener(this);
    if (paramBundle == null)
    {
      paramBundle = getArguments();
      TakeawayDishMenuDataSource localTakeawayDishMenuDataSource = this.dishMenuDataSource;
      this.dishMenuDataSource.getClass();
      localTakeawayDishMenuDataSource.source = paramBundle.getInt("source", -1);
      this.dishMenuDataSource.shopID = paramBundle.getString("shopid");
      this.dishMenuDataSource.queryId = paramBundle.getString("queryid");
      this.dishMenuDataSource.inputAddress = paramBundle.getString("address");
      this.dishMenuDataSource.lat = paramBundle.getString("lat");
      this.dishMenuDataSource.lng = paramBundle.getString("lng");
    }
    while (true)
    {
      paramBundle = this.activity;
      TakeawayMenuActivity.menuFragment = this;
      return;
      this.dishMenuDataSource.onRestoreInstanceState(paramBundle);
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.takeaway_shopmenu_list, null);
    initView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    this.dishMenuDataSource.onDestroy();
    super.onDestroy();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    this.dishMenuDataSource.onSaveInstanceState(paramBundle);
    super.onSaveInstanceState(paramBundle);
  }

  public void selectNewItem(View paramView1, View paramView2)
  {
    int i;
    if (paramView2 != null)
    {
      this.recomCateNameView.setTextColor(getResources().getColor(R.color.light_gray));
      this.recomCateView.findViewById(R.id.bar_view).setVisibility(8);
      this.recomMenuListView.setVisibility(8);
      this.menuListView.setVisibility(0);
      ((TextView)paramView2.findViewById(16908308)).setTextColor(getResources().getColor(R.color.deep_gray));
      i = paramView2.getMeasuredHeight();
      if (i != 0)
        break label141;
      paramView2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(paramView2)
      {
        public boolean onPreDraw()
        {
          this.val$newView.getViewTreeObserver().removeOnPreDrawListener(this);
          int i = this.val$newView.getMeasuredHeight();
          TakeawayShopMenuFragment.this.displayCategoryBar(this.val$newView, i);
          return false;
        }
      });
    }
    while (true)
    {
      if (paramView1 != null)
      {
        ((TextView)paramView1.findViewById(16908308)).setTextColor(getResources().getColor(R.color.light_gray));
        paramView1.findViewById(R.id.bar_view).setVisibility(8);
      }
      return;
      label141: displayCategoryBar(paramView2, i);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.fragment.TakeawayShopMenuFragment
 * JD-Core Version:    0.6.0
 */