package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.DistanceUtils;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.base.widget.ShopPower;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.tuan.utils.ViewItemUtils;
import com.dianping.tuan.widget.NaviTagFilterBar;
import com.dianping.tuan.widget.NaviTagFilterBar.OnSelectionChangedListener;
import com.dianping.tuan.widget.ViewItemData;
import com.dianping.tuan.widget.viewitem.TuanItemDisplayType;
import com.dianping.util.ViewUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class TuanNewShopActivity extends BaseTuanActivity
  implements NaviTagFilterBar.OnSelectionChangedListener, AbsListView.OnScrollListener
{
  public static final int FILTER_BAR_HEIGHT_DIP = 45;
  public static final String MAPI_TUAN_REC_SHOP = "findnewshopgn.bin";
  protected String categoryId;
  protected HashMap<String, String> currentFilterData = new HashMap();
  protected String currentScreening;
  protected View.OnClickListener dealAggClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if ((paramView == null) || (!(paramView instanceof ViewItemData)));
      do
      {
        do
        {
          return;
          localObject = (ViewItemData)paramView;
        }
        while ((localObject == null) || (((ViewItemData)localObject).displayType != TuanItemDisplayType.AGG_DEAL) || (!DPObjectUtils.isDPObjectof(((ViewItemData)localObject).viewItem, "ViewItem")));
        paramView = ((ViewItemData)localObject).viewItem.getObject("Deal");
      }
      while (!DPObjectUtils.isDPObjectof(paramView, "Deal"));
      Object localObject = ((ViewItemData)localObject).viewItem.getString("Link");
      if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject))
        try
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
          TuanNewShopActivity.this.startActivity(paramView);
          return;
        }
        catch (Exception paramView)
        {
          paramView.printStackTrace();
          return;
        }
      try
      {
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
        ((Intent)localObject).putExtra("deal", paramView);
        TuanNewShopActivity.this.startActivity((Intent)localObject);
        return;
      }
      catch (Exception paramView)
      {
        paramView.printStackTrace();
      }
    }
  };
  protected NaviTagFilterBar filterBar;
  protected String imageUrl;
  protected NetworkImageView imageView;
  protected HashMap<String, String> intentParamMap = new HashMap();
  protected boolean intentParsed = false;
  protected boolean isFirstPage = true;
  protected String keyword;
  protected ListAdapter listAdapter = new ListAdapter(getBaseContext());
  protected PullToRefreshListView listView;
  protected View.OnClickListener moreDealsOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if ((paramView == null) || (!(paramView instanceof ViewItemData)))
        return;
      paramView = (ViewItemData)paramView;
      TuanNewShopActivity.this.listAdapter.spreadData(paramView.index);
      TuanNewShopActivity.this.listAdapter.notifyDataSetChanged();
    }
  };
  protected DPObject[] naviFilterData;
  protected NovaFrameLayout rootView;
  protected DPObject[] screeningData;
  protected boolean screeningTaped;
  protected View.OnClickListener shopMainClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if ((paramView == null) || (!(paramView instanceof ViewItemData)));
      do
      {
        do
        {
          return;
          localObject = (ViewItemData)paramView;
        }
        while ((localObject == null) || (((ViewItemData)localObject).displayType != TuanItemDisplayType.AGG_SHOP_MAIN) || (!DPObjectUtils.isDPObjectof(((ViewItemData)localObject).viewItem, "ViewItem")));
        paramView = ((ViewItemData)localObject).viewItem.getObject("Shop");
      }
      while (!DPObjectUtils.isDPObjectof(paramView, "Shop"));
      Object localObject = ((ViewItemData)localObject).viewItem.getString("Link");
      if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject))
        try
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
          TuanNewShopActivity.this.startActivity(paramView);
          return;
        }
        catch (Exception paramView)
        {
          paramView.printStackTrace();
          return;
        }
      try
      {
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramView.getInt("ID")));
        ((Intent)localObject).putExtra("shopId", paramView.getInt("ID"));
        ((Intent)localObject).putExtra("shop", paramView);
        TuanNewShopActivity.this.startActivity((Intent)localObject);
        return;
      }
      catch (Exception paramView)
      {
        paramView.printStackTrace();
      }
    }
  };
  protected DPObject[] tagCellData;
  protected NovaFrameLayout topCellContainer;
  protected NaviTagFilterBar topFilterBar;

  protected View createAggDealItem(ViewItemData paramViewItemData, View paramView)
  {
    Object localObject1 = new View(getBaseContext());
    if (paramViewItemData == null)
      return localObject1;
    if (paramViewItemData.displayType != TuanItemDisplayType.AGG_DEAL)
      return localObject1;
    if (!DPObjectUtils.isDPObjectof(paramViewItemData.viewItem, "ViewItem"))
      return localObject1;
    Object localObject2 = paramViewItemData.viewItem.getObject("Deal");
    if (!DPObjectUtils.isDPObjectof(localObject2, "Deal"))
      return localObject1;
    int j;
    Object localObject5;
    Object localObject6;
    Object localObject3;
    TextView localTextView3;
    LinearLayout localLinearLayout;
    TextView localTextView4;
    TextView localTextView1;
    TextView localTextView2;
    Object localObject4;
    int k;
    int i;
    label349: int m;
    if ((paramView == null) || (!(paramView instanceof NovaRelativeLayout)))
    {
      localObject1 = (NovaRelativeLayout)getLayoutInflater().inflate(R.layout.tuan_agg_view_item_deal, null);
      j = DPApplication.instance().getApplicationContext().getResources().getDisplayMetrics().widthPixels - ((NovaRelativeLayout)localObject1).getPaddingLeft() - ((NovaRelativeLayout)localObject1).getPaddingRight();
      paramView = (DPNetworkImageView)((NovaRelativeLayout)localObject1).findViewById(R.id.icon);
      localObject5 = (ImageView)((NovaRelativeLayout)localObject1).findViewById(R.id.status);
      localObject6 = (ImageView)((NovaRelativeLayout)localObject1).findViewById(R.id.status_nopic);
      RelativeLayout localRelativeLayout = (RelativeLayout)((NovaRelativeLayout)localObject1).findViewById(R.id.info_panel);
      TextView localTextView5 = (TextView)((NovaRelativeLayout)localObject1).findViewById(R.id.title);
      localObject3 = (TextView)((NovaRelativeLayout)localObject1).findViewById(R.id.price);
      localTextView3 = (TextView)((NovaRelativeLayout)localObject1).findViewById(R.id.origin_price);
      localLinearLayout = (LinearLayout)((NovaRelativeLayout)localObject1).findViewById(R.id.tags);
      localTextView4 = (TextView)((NovaRelativeLayout)localObject1).findViewById(R.id.rec_text);
      localTextView1 = (TextView)((NovaRelativeLayout)localObject1).findViewById(R.id.recommend_reason);
      localTextView2 = (TextView)((NovaRelativeLayout)localObject1).findViewById(R.id.extra_recommend_reason);
      localObject4 = (TextView)((NovaRelativeLayout)localObject1).findViewById(R.id.status_bottom);
      ((NovaRelativeLayout)localObject1).setGAString("menu");
      ((NovaRelativeLayout)localObject1).getGAUserInfo().index = Integer.valueOf(paramViewItemData.innerIndex + 1);
      ((NovaRelativeLayout)localObject1).getGAUserInfo().sectionIndex = Integer.valueOf(paramViewItemData.index);
      ((NovaRelativeLayout)localObject1).getGAUserInfo().query_id = paramViewItemData.viewItem.getString("QueryId");
      k = ((DPObject)localObject2).getInt("DealType");
      i = 0;
      if ((((DPObject)localObject2).getInt("Tag") & 0x200) == 0)
        break label563;
      i = R.drawable.deal_list_item_status_free;
      ((ImageView)localObject5).setImageResource(i);
      ((ImageView)localObject6).setImageResource(i);
      if (!this.listAdapter.isImageOn())
        break label673;
      paramView.setVisibility(0);
      ((ImageView)localObject6).setVisibility(8);
      ((ImageView)localObject5).setVisibility(0);
      paramView.setImage(((DPObject)localObject2).getString("Photo"));
      i = j - getResources().getDimensionPixelSize(R.dimen.deal_list_image_width);
      label419: j = localRelativeLayout.getPaddingLeft();
      k = localRelativeLayout.getPaddingRight();
      localTextView5.setText(((DPObject)localObject2).getString("DealTitle"));
      paramView = "";
      m = ((DPObject)localObject2).getInt("Status");
      if ((m & 0x4) == 0)
        break label699;
      paramView = "已结束";
      label471: if ("".equals(paramView))
        break label713;
      ((TextView)localObject4).setText(paramView);
      ((TextView)localObject4).setVisibility(0);
      ((TextView)localObject3).setVisibility(8);
      localTextView3.setVisibility(8);
      localLinearLayout.setVisibility(8);
      localTextView4.setVisibility(8);
      localTextView1.setVisibility(8);
      localTextView2.setVisibility(8);
    }
    while (true)
    {
      ((View)localObject1).setClickable(true);
      ((View)localObject1).setOnClickListener(this.dealAggClickListener);
      ((View)localObject1).setTag(paramViewItemData);
      return localObject1;
      localObject1 = (NovaRelativeLayout)paramView;
      break;
      label563: if ((((DPObject)localObject2).getInt("Tag") & 0x400) != 0)
      {
        i = R.drawable.deal_list_item_status_dianping_chosen;
        break label349;
      }
      if ((((DPObject)localObject2).getInt("Tag") & 0x100) != 0)
      {
        i = R.drawable.deal_list_item_status_booking;
        break label349;
      }
      if (k == 3)
      {
        i = R.drawable.deal_list_item_status_lottery;
        break label349;
      }
      if (k == 5)
      {
        i = R.drawable.deal_list_item_status_online;
        break label349;
      }
      if (k == 6)
      {
        i = R.drawable.deal_list_item_status_hui;
        break label349;
      }
      if ((((DPObject)localObject2).getInt("Tag") & 0x1) == 0)
        break label349;
      i = R.drawable.deal_list_item_status_reservation;
      break label349;
      label673: paramView.setVisibility(8);
      ((ImageView)localObject6).setVisibility(0);
      ((ImageView)localObject5).setVisibility(8);
      i = j;
      break label419;
      label699: if ((m & 0x2) == 0)
        break label471;
      paramView = "已售完";
      break label471;
      label713: ((TextView)localObject4).setVisibility(8);
      ((TextView)localObject3).setVisibility(0);
      ((TextView)localObject3).setText("¥" + PriceFormatUtils.formatPrice(((DPObject)localObject2).getDouble("Price")));
      paramView = new SpannableString("¥" + PriceFormatUtils.formatPrice(((DPObject)localObject2).getDouble("OriginalPrice")));
      paramView.setSpan(new StrikethroughSpan(), 0, paramView.length(), 33);
      localTextView3.setText(paramView);
      localTextView3.setVisibility(0);
      j = i - j - k - ViewUtils.measureTextView((TextView)localObject3) - ((TextView)localObject3).getPaddingLeft() - ((TextView)localObject3).getPaddingRight() - ViewUtils.measureTextView(localTextView3) - localTextView3.getPaddingLeft() - localTextView3.getPaddingRight();
      paramView = ((DPObject)localObject2).getString("SalesDesc");
      localObject3 = ((DPObject)localObject2).getString("SalesTag");
      if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject3))
      {
        paramView = com.dianping.util.TextUtils.jsonParseText((String)localObject3);
        i = j;
        if (!com.dianping.util.TextUtils.isEmpty(paramView))
        {
          localTextView4.setText(paramView);
          localTextView4.setVisibility(0);
          i = j - ViewUtils.measureTextView(localTextView4) - localTextView4.getPaddingLeft() - localTextView4.getPaddingRight();
        }
      }
      while (true)
      {
        paramView = ((DPObject)localObject2).getArray("EventList");
        if (DPObjectUtils.isArrayEmpty(paramView))
          break;
        localLinearLayout.removeAllViews();
        localLinearLayout.setVisibility(0);
        int n = Math.min(2, paramView.length);
        localObject3 = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject3).setMargins(10, 0, 0, 0);
        j = localLinearLayout.getPaddingLeft() + localLinearLayout.getPaddingRight();
        k = 0;
        while (true)
          if (k < n)
          {
            localObject4 = paramView[k].getString("ShortTitle");
            m = j;
            if (!android.text.TextUtils.isEmpty((CharSequence)localObject4))
            {
              localObject5 = new ColorBorderTextView(getBaseContext());
              localObject6 = paramView[k].getString("Color");
              ((ColorBorderTextView)localObject5).setTextColor((String)localObject6);
              ((ColorBorderTextView)localObject5).setBorderColor("#C8" + ((String)localObject6).substring(1));
              ((ColorBorderTextView)localObject5).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
              ((ColorBorderTextView)localObject5).setSingleLine();
              ((ColorBorderTextView)localObject5).setEllipsize(TextUtils.TruncateAt.END);
              ((ColorBorderTextView)localObject5).setPadding(ViewUtils.dip2px(getBaseContext(), 4.0F), 0, ViewUtils.dip2px(getBaseContext(), 4.0F), 0);
              ((ColorBorderTextView)localObject5).setText((CharSequence)localObject4);
              localLinearLayout.addView((View)localObject5, (ViewGroup.LayoutParams)localObject3);
              m = ViewUtils.measureTextView((TextView)localObject5) + j + ViewUtils.dip2px(getBaseContext(), 8.0F) + 10;
            }
            k += 1;
            j = m;
            continue;
            i = j;
            if (com.dianping.util.TextUtils.isEmpty(paramView))
              break;
            paramView = com.dianping.util.TextUtils.jsonParseText(paramView);
            i = j;
            if (com.dianping.util.TextUtils.isEmpty(paramView))
              break;
            localTextView4.setText(paramView);
            localTextView4.setVisibility(0);
            i = j - ViewUtils.measureTextView(localTextView4) - localTextView4.getPaddingLeft() - localTextView4.getPaddingRight();
            break;
          }
        if (j > i)
        {
          k = i;
          if (localTextView4.getVisibility() != 8)
          {
            k = ViewUtils.measureTextView(localTextView4) + i + localTextView4.getPaddingLeft() + localTextView4.getPaddingRight();
            localTextView4.setVisibility(8);
          }
          if (j > k)
            localTextView3.setVisibility(8);
        }
        label1358: paramView = ((DPObject)localObject2).getString("RecommendReason");
        if (com.dianping.util.TextUtils.isEmpty(paramView))
          break label1483;
        localTextView1.setText(paramView);
        localTextView1.setVisibility(0);
      }
      while (true)
      {
        localObject2 = ((DPObject)localObject2).getString("ExtraRecommendReason");
        localTextView2.setVisibility(8);
        if ((localTextView1.getVisibility() != 0) && (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2)))
        {
          localTextView2.setText(com.dianping.util.TextUtils.jsonParseText((String)localObject2));
          localTextView2.setVisibility(0);
        }
        if ((!com.dianping.util.TextUtils.isEmpty(paramView)) || (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject2)))
          break label1493;
        ((NovaRelativeLayout)localObject1).getGAUserInfo().title = null;
        break;
        localLinearLayout.setVisibility(8);
        localTextView4.setVisibility(0);
        localTextView3.setVisibility(0);
        break label1358;
        label1483: localTextView1.setVisibility(8);
      }
      label1493: ((NovaRelativeLayout)localObject1).getGAUserInfo().title = "best";
    }
  }

  public View createDividerLine(View paramView)
  {
    if ((paramView == null) || (!(paramView instanceof NovaRelativeLayout)))
      return getLayoutInflater().inflate(R.layout.tuan_agg_view_item_divider, null);
    return paramView;
  }

  public NaviTagFilterBar createFilterBar()
  {
    NaviTagFilterBar localNaviTagFilterBar = new NaviTagFilterBar(this);
    localNaviTagFilterBar.setHasScreening(true);
    return localNaviTagFilterBar;
  }

  public View createMoreDealsBtn(ViewItemData paramViewItemData, View paramView)
  {
    View localView = new View(getBaseContext());
    if (paramViewItemData == null)
      return localView;
    if (paramViewItemData.displayType != TuanItemDisplayType.AGG_MORE)
      return localView;
    if ((paramView != null) && ((paramView instanceof NovaRelativeLayout)));
    for (paramView = (NovaRelativeLayout)paramView; ; paramView = (NovaRelativeLayout)getLayoutInflater().inflate(R.layout.tuan_agg_view_item_more, null))
    {
      ((TextView)paramView.findViewById(R.id.text)).setText(paramViewItemData.text);
      paramView.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(getBaseContext(), 40.0F)));
      paramView.setClickable(true);
      paramView.setOnClickListener(this.moreDealsOnClickListener);
      paramView.setTag(paramViewItemData);
      return paramView;
    }
  }

  protected View createShopMainItem(ViewItemData paramViewItemData, View paramView)
  {
    Object localObject1 = new View(getBaseContext());
    if (paramViewItemData == null)
      return localObject1;
    if (paramViewItemData.displayType != TuanItemDisplayType.AGG_SHOP_MAIN)
      return localObject1;
    if (!DPObjectUtils.isDPObjectof(paramViewItemData.viewItem, "ViewItem"))
      return localObject1;
    DPObject localDPObject = paramViewItemData.viewItem.getObject("Shop");
    if (!DPObjectUtils.isDPObjectof(localDPObject, "Shop"))
      return localObject1;
    Object localObject2;
    if ((paramView == null) || (!(paramView instanceof NovaLinearLayout)))
    {
      paramView = (NovaLinearLayout)getLayoutInflater().inflate(R.layout.tuan_agg_view_item_shop_main, null);
      paramView.setGAString("shopmenu");
      paramView.getGAUserInfo().index = Integer.valueOf(paramViewItemData.index);
      paramView.getGAUserInfo().query_id = paramViewItemData.viewItem.getString("QueryId");
      TextView localTextView = (TextView)paramView.findViewById(R.id.title);
      localObject2 = localDPObject.getString("Name");
      String str = localDPObject.getString("BranchName");
      localObject1 = localObject2;
      if (!com.dianping.util.TextUtils.isEmpty(str))
        localObject1 = String.format("%s (%s)", new Object[] { localObject2, str });
      if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject1))
        localTextView.setText((CharSequence)localObject1);
      ((ShopPower)paramView.findViewById(R.id.shop_info_power)).setPower(localDPObject.getInt("ShopPower"));
      ((TextView)paramView.findViewById(R.id.shop_info_region)).setText(localDPObject.getString("RegionName"));
      ((TextView)paramView.findViewById(R.id.shop_info_category)).setText(localDPObject.getString("CategoryName"));
      localObject2 = (TextView)paramView.findViewById(R.id.shop_info_distance);
      localObject1 = null;
      if (location() != null)
        localObject1 = DistanceUtils.calculateDistance(location().latitude(), location().longitude(), localDPObject.getDouble("Latitude"), localDPObject.getDouble("Longitude"));
      if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject1))
        break label370;
      ((TextView)localObject2).setVisibility(8);
    }
    while (true)
    {
      paramView.setClickable(true);
      paramView.setOnClickListener(this.shopMainClickListener);
      paramView.setTag(paramViewItemData);
      return paramView;
      paramView = (NovaLinearLayout)paramView;
      break;
      label370: ((TextView)localObject2).setText((CharSequence)localObject1);
      ((TextView)localObject2).setVisibility(0);
    }
  }

  public View createWarningItem(ViewItemData paramViewItemData, View paramView)
  {
    Object localObject = new View(getBaseContext());
    if (paramViewItemData == null)
      return localObject;
    if (paramViewItemData.displayType != TuanItemDisplayType.WARNING)
      return localObject;
    if (!DPObjectUtils.isDPObjectof(paramViewItemData.viewItem, "ViewItem"))
      return localObject;
    if ((paramView == null) || (!(paramView instanceof NovaRelativeLayout)))
      paramView = getLayoutInflater().inflate(R.layout.tuan_view_item_warning, null);
    while (true)
    {
      localObject = (TextView)paramView.findViewById(R.id.title);
      paramViewItemData = paramViewItemData.viewItem.getString("Title");
      if (!com.dianping.util.TextUtils.isEmpty(paramViewItemData))
        ((TextView)localObject).setText(paramViewItemData);
      return paramView;
    }
  }

  public String filterType2ParamEnNameKey(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 1:
      return "categoryenname";
    case 2:
    }
    return "regionenname";
  }

  public String filterType2ParamKey(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return null;
    case 1:
      return "categoryid";
    case 2:
      return "regionid";
    case 3:
      return "distance";
    case 4:
    }
    return "filter";
  }

  public String getPageName()
  {
    return "newshop";
  }

  protected HashMap<String, String> mergeParameters()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.putAll(this.intentParamMap);
    localHashMap.putAll(this.currentFilterData);
    return localHashMap;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (this.rootView == null)
      setupView();
    setContentView(this.rootView);
    parseIntent();
    updateView();
    this.listAdapter.reset();
  }

  public void onFilterSelectionChanged(HashMap<Integer, DPObject> paramHashMap, DPObject paramDPObject)
  {
    Log.v("debug_test", "tuan rec shop activity filter changed");
    refreshSelectedFilterData(paramHashMap);
    if (DPObjectUtils.isDPObjectof(paramDPObject, "Navi"))
    {
      paramHashMap = filterType2ParamKey(paramDPObject.getInt("Type"));
      if ((!"regionid".equals(paramHashMap)) && (!"range".equals(paramHashMap)))
        break label83;
      GAHelper.instance().contextStatisticsEvent(getBaseContext(), "district", null, 0, "tap");
    }
    while (true)
    {
      this.listAdapter.reset();
      return;
      label83: if (!"filter".equals(paramHashMap))
        continue;
      GAHelper.instance().contextStatisticsEvent(getBaseContext(), "sort", null, 0, "tap");
    }
  }

  public void onScreeningSelectionChanged(HashMap<Integer, DPObject> paramHashMap, String paramString)
  {
    Log.v("debug_test", "tuan rec shop activity screening changed");
    refreshSelectedFilterData(paramHashMap);
    this.currentScreening = paramString;
    GAHelper.instance().contextStatisticsEvent(getBaseContext(), "select", null, 0, "tap");
    this.screeningTaped = true;
    this.listAdapter.reset();
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 <= this.listView.getHeaderViewsCount() - 2)
      if (this.topFilterBar.isShown())
      {
        this.topFilterBar.setVisibility(8);
        this.filterBar.setVisibility(0);
        this.filterBar.setTagCellsScrollX(this.topFilterBar.getTagCellsScrollX());
      }
    do
      return;
    while (this.topFilterBar.isShown());
    this.topFilterBar.setVisibility(0);
    this.filterBar.setVisibility(4);
    this.topFilterBar.setTagCellsScrollX(this.filterBar.getTagCellsScrollX());
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
  }

  public void onTagCellSelectionChanged(int paramInt, HashMap<Integer, DPObject> paramHashMap, DPObject paramDPObject)
  {
    refreshSelectedFilterData(paramHashMap);
    if ((DPObjectUtils.isDPObjectof(paramDPObject, "Navi")) && ("categoryid".equals(filterType2ParamKey(paramDPObject.getInt("Type")))))
      GAHelper.instance().contextStatisticsEvent(getBaseContext(), "selectcategory", paramDPObject.getString("Title"), paramInt, "tap");
    this.listAdapter.reset();
  }

  protected void parseIntent()
  {
    if (this.intentParsed)
      return;
    String str2 = getStringParam("category");
    String str1 = str2;
    if (com.dianping.util.TextUtils.isEmpty(str2))
      str1 = getStringParam("categoryid");
    if (!com.dianping.util.TextUtils.isEmpty(str1))
    {
      this.intentParamMap.put("categoryid", str1);
      this.categoryId = str1;
    }
    this.intentParamMap.put("categoryenname", getStringParam("categoryenname"));
    str2 = getStringParam("region");
    str1 = str2;
    if (com.dianping.util.TextUtils.isEmpty(str2))
      str1 = getStringParam("regionid");
    if (!com.dianping.util.TextUtils.isEmpty(str1))
      this.intentParamMap.put("regionid", str1);
    this.intentParamMap.put("regionenname", getStringParam("regionenname"));
    str1 = getStringParam("sort");
    if (!com.dianping.util.TextUtils.isEmpty(str1))
      this.intentParamMap.put("filter", str1);
    this.intentParamMap.put("keyword", getStringParam("keyword"));
    this.intentParsed = true;
  }

  protected void refreshSelectedFilterData(HashMap<Integer, DPObject> paramHashMap)
  {
    this.currentFilterData.clear();
    if ((paramHashMap != null) && (paramHashMap.size() > 0))
    {
      paramHashMap = paramHashMap.entrySet().iterator();
      while (paramHashMap.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)paramHashMap.next();
        String str1 = filterType2ParamKey(((Integer)localEntry.getKey()).intValue());
        String str2 = filterType2ParamEnNameKey(((Integer)localEntry.getKey()).intValue());
        if (com.dianping.util.TextUtils.isEmpty(str1))
          continue;
        if (DPObjectUtils.isDPObjectof(localEntry.getValue(), "Navi"))
          this.currentFilterData.put(str1, "" + ((DPObject)localEntry.getValue()).getInt("ID"));
        if ((com.dianping.util.TextUtils.isEmpty(str2)) || (!DPObjectUtils.isDPObjectof(localEntry.getValue(), "Navi")))
          continue;
        this.currentFilterData.put(str2, "" + ((DPObject)localEntry.getValue()).getString("EnName"));
      }
    }
  }

  protected void setHeight()
  {
    this.filterBar.setLineHegiht(ViewUtils.dip2px(getBaseContext(), 45.0F));
    this.topFilterBar.setLineHegiht(ViewUtils.dip2px(getBaseContext(), 45.0F));
  }

  protected void setupView()
  {
    this.rootView = new NovaFrameLayout(getBaseContext());
    Object localObject = new ViewGroup.LayoutParams(-1, -1);
    this.rootView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.topCellContainer = new NovaFrameLayout(getBaseContext());
    localObject = new ViewGroup.LayoutParams(-1, -2);
    this.rootView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.listView = new PullToRefreshListView(getBaseContext());
    this.listView.setDivider(null);
    localObject = new ViewGroup.LayoutParams(-1, -1);
    this.listView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.listView.setPullLoadEnable(0);
    this.listView.setPullRefreshEnable(1);
    this.listView.setOnScrollListener(this);
    this.rootView.addView(this.listView);
    this.rootView.addView(this.topCellContainer);
    this.filterBar = createFilterBar();
    this.filterBar.setVisibility(0);
    this.filterBar.setOnSelectionChangedListener(this);
    this.filterBar.setNeedCount(true);
    this.topFilterBar = createFilterBar();
    this.topFilterBar.setVisibility(8);
    this.topFilterBar.setOnSelectionChangedListener(this);
    this.topFilterBar.setNeedCount(true);
    this.imageView = new NetworkImageView(getBaseContext());
    localObject = new AbsListView.LayoutParams(-1, -2);
    this.imageView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.imageView.setAdjustViewBounds(true);
    this.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    this.listView.addHeaderView(this.imageView);
    this.listView.addHeaderView(this.filterBar);
    this.listView.setAdapter(this.listAdapter);
    this.topCellContainer.addView(this.topFilterBar);
    localObject = new AbsListView.LayoutParams(-1, -2);
    this.filterBar.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localObject = new FrameLayout.LayoutParams(-1, -2);
    this.topFilterBar.setLayoutParams((ViewGroup.LayoutParams)localObject);
  }

  protected void updateFilterBar()
  {
    setHeight();
    this.filterBar.setFilterData(this.naviFilterData, false);
    this.filterBar.setScreeningData(this.screeningData, false);
    this.filterBar.setTagCellData(this.tagCellData, false);
    this.filterBar.setScreening(this.currentScreening, false);
    this.filterBar.updateView();
    this.topFilterBar.setFilterData(this.naviFilterData, false);
    this.topFilterBar.setScreeningData(this.screeningData, false);
    this.topFilterBar.setTagCellData(this.tagCellData, false);
    this.topFilterBar.setScreening(this.currentScreening, false);
    this.topFilterBar.updateView();
    setHeight();
  }

  protected void updateView()
  {
    updateFilterBar();
    this.imageView.setImage(this.imageUrl);
  }

  class ListAdapter extends BasicLoadAdapter
  {
    protected ArrayList<ViewItemData> displayData = new ArrayList();
    private boolean shouldShowImage = DPActivity.preferences().getBoolean("isShowListImage", true);
    protected ArrayList<Integer> speardIndexList = new ArrayList();

    public ListAdapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      Object localObject1 = TuanNewShopActivity.this;
      if (paramInt == 0);
      for (boolean bool = true; ; bool = false)
      {
        ((TuanNewShopActivity)localObject1).isFirstPage = bool;
        localObject1 = UrlBuilder.createBuilder("http://app.t.dianping.com/");
        ((UrlBuilder)localObject1).appendPath("findnewshopgn.bin");
        ((UrlBuilder)localObject1).addParam("cityid", Integer.valueOf(TuanNewShopActivity.this.cityId()));
        if (TuanNewShopActivity.this.getAccount() != null)
          ((UrlBuilder)localObject1).addParam("token", TuanNewShopActivity.this.getAccount().token());
        if (TuanNewShopActivity.this.location() != null)
        {
          ((UrlBuilder)localObject1).addParam("lat", Double.valueOf(TuanNewShopActivity.this.location().latitude()));
          ((UrlBuilder)localObject1).addParam("lng", Double.valueOf(TuanNewShopActivity.this.location().longitude()));
        }
        ((UrlBuilder)localObject1).addParam("start", Integer.valueOf(paramInt));
        Object localObject2 = TuanNewShopActivity.this.mergeParameters();
        if (((HashMap)localObject2).size() <= 0)
          break;
        localObject2 = ((HashMap)localObject2).entrySet().iterator();
        while (((Iterator)localObject2).hasNext())
        {
          Map.Entry localEntry = (Map.Entry)((Iterator)localObject2).next();
          ((UrlBuilder)localObject1).addParam((String)localEntry.getKey(), localEntry.getValue());
        }
      }
      ((UrlBuilder)localObject1).addParam("screening", TuanNewShopActivity.this.currentScreening);
      return (MApiRequest)(MApiRequest)TuanNewShopActivity.this.mapiGet(TuanNewShopActivity.this.listAdapter, ((UrlBuilder)localObject1).buildUrl(), CacheType.DISABLED);
    }

    public int getCount()
    {
      if (this.mIsEnd)
      {
        if (this.displayData.size() == 0)
          return 1;
        return this.displayData.size();
      }
      return this.displayData.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.displayData.size())
        return this.displayData.get(paramInt);
      if ((this.mEmptyMsg != null) || ((this.mIsEnd) && (this.mData.size() == 0)))
        return EMPTY;
      if (this.mErrorMsg == null)
        return LOADING;
      return ERROR;
    }

    public int getItemViewType(int paramInt)
    {
      int i = super.getItemViewType(paramInt);
      if (i == 3);
      switch (TuanNewShopActivity.4.$SwitchMap$com$dianping$tuan$widget$viewitem$TuanItemDisplayType[((ViewItemData)this.displayData.get(paramInt)).displayType.ordinal()])
      {
      default:
        return i;
      case 1:
        return 3;
      case 2:
        return 4;
      case 3:
        return 5;
      case 4:
        return 6;
      case 5:
      }
      return 7;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof ViewItemData))
        return itemViewWithData((ViewItemData)localObject, paramInt, paramView, paramViewGroup);
      if (localObject == LOADING)
      {
        if (this.mErrorMsg == null)
          loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == EMPTY)
        return getEmptyView(emptyMessage(), "暂时没有你要找的哦，看看别的吧", paramViewGroup, paramView);
      return getFailedView(this.mErrorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          TuanNewShopActivity.ListAdapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 8;
    }

    protected boolean isImageOn()
    {
      return (this.shouldShowImage) || (NetworkUtils.isWIFIConnection(DPApplication.instance()));
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return null;
    }

    protected View itemViewWithData(ViewItemData paramViewItemData, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      switch (TuanNewShopActivity.4.$SwitchMap$com$dianping$tuan$widget$viewitem$TuanItemDisplayType[paramViewItemData.displayType.ordinal()])
      {
      default:
        return null;
      case 1:
        return TuanNewShopActivity.this.createShopMainItem(paramViewItemData, paramView);
      case 2:
        return TuanNewShopActivity.this.createAggDealItem(paramViewItemData, paramView);
      case 3:
        return TuanNewShopActivity.this.createMoreDealsBtn(paramViewItemData, paramView);
      case 4:
        return TuanNewShopActivity.this.createDividerLine(paramView);
      case 5:
      }
      return TuanNewShopActivity.this.createWarningItem(paramViewItemData, paramView);
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      TuanNewShopActivity.this.listView.onRefreshComplete();
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        Object localObject = (DPObject)paramMApiResponse.result();
        if (TuanNewShopActivity.this.isFirstPage)
        {
          TuanNewShopActivity.this.imageUrl = ((DPObject)localObject).getString("HeadPicURL");
          TuanNewShopActivity.this.naviFilterData = ((DPObject)localObject).getArray("NaviBars");
          TuanNewShopActivity.this.tagCellData = ((DPObject)localObject).getArray("TagNavis");
          if (!TuanNewShopActivity.this.screeningTaped)
          {
            TuanNewShopActivity.this.screeningData = ((DPObject)localObject).getArray("Screenings");
            TuanNewShopActivity.this.currentScreening = "";
          }
          TuanNewShopActivity.this.screeningTaped = false;
          TuanNewShopActivity.this.updateView();
        }
        if (this.mIsPullToRefresh)
        {
          this.mIsPullToRefresh = false;
          this.mData.clear();
          this.displayData.clear();
          this.speardIndexList.clear();
        }
        this.mEmptyMsg = ((DPObject)localObject).getString("EmptyMsg");
        this.mIsEnd = ((DPObject)localObject).getBoolean("IsEnd");
        this.mNextStartIndex = ((DPObject)localObject).getInt("NextStartIndex");
        this.mRecordCount = ((DPObject)localObject).getInt("RecordCount");
        this.mQueryId = ((DPObject)localObject).getString("QueryID");
        localObject = ((DPObject)localObject).getArray("List");
        if (localObject == null)
          break label283;
        appendDataToList(localObject);
        if ((this.mData.size() == 0) && (this.mEmptyMsg == null))
          this.mEmptyMsg = "数据为空";
        if (localObject.length != 0);
      }
      label283: for (this.mIsEnd = true; ; this.mIsEnd = true)
      {
        parseDisplayData();
        notifyDataSetChanged();
        this.mReq = null;
        onRequestComplete(true, paramMApiRequest, paramMApiResponse);
        return;
      }
    }

    public void parseDisplayData()
    {
      this.displayData.clear();
      int i = 0;
      while (i < this.mData.size())
      {
        boolean bool = false;
        if (this.speardIndexList.contains(Integer.valueOf(i)))
          bool = true;
        this.displayData.addAll(ViewItemUtils.unfoldViewItem((DPObject)this.mData.get(i), i, bool));
        i += 1;
      }
    }

    public void reset()
    {
      this.displayData.clear();
      this.speardIndexList.clear();
      super.reset();
    }

    public void spreadData(int paramInt)
    {
      if (!this.speardIndexList.contains(Integer.valueOf(paramInt)))
      {
        this.speardIndexList.add(Integer.valueOf(paramInt));
        parseDisplayData();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanNewShopActivity
 * JD-Core Version:    0.6.0
 */