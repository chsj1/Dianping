package com.dianping.search.shoplist.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.data.model.AdShopDataModel;
import com.dianping.search.shoplist.data.model.DealModel;
import com.dianping.search.shoplist.data.model.ExtSearchModel;
import com.dianping.search.shoplist.data.model.GlobalSearchResult;
import com.dianping.search.shoplist.data.model.KtvShopDataModel;
import com.dianping.search.shoplist.data.model.SearchDirectZoneModel;
import com.dianping.search.shoplist.data.model.SearchRecAdGuideModel;
import com.dianping.search.widget.MallItem;
import com.dianping.search.widget.NearbyMallsItem;
import com.dianping.search.widget.SearchDirectZoneItem;
import com.dianping.search.widget.SearchMovieDirectZoneView;
import com.dianping.search.widget.SearchRecAdGuideItem;
import com.dianping.search.widget.ShopDealInfoItem;
import com.dianping.search.widget.ShopListExtItem;
import com.dianping.search.widget.ShowMoreItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopListDataModelAdapter extends ShopListAdapter
{
  public static final DPObject BRAND_MORE;
  public static final int DEFAULT_BRAND_DISPLAY_COUNT = 10;
  public static final int DEFAULT_DISPLAY_COUNT = 1;
  public static final DPObject GLOBAL_EMPTY;
  public static final DPObject GLOBAL_MORE;
  public static final DPObject GLOBAL_TITLE;
  public static final int INSERT_REC_AD_GUIDE_POSITION = 3;
  public static final DPObject MALL_MORE = new DPObject();
  public static final DPObject NEARBY_DISTANCE;
  public static final DPObject NEARBY_MALLS;
  public static final DPObject PET;
  public static final DPObject REC_TITLE = new DPObject();
  public static final DPObject SEARCH_MOVIE_DIRECTZONE;
  public static final DPObject TOP_REC;
  public static final DPObject TOP_SHOP_BOTTOM;
  public static final DPObject TOP_SHOP_HEAD;
  private static final int TOTAL_TYPE_COUNT = 26;
  private static final int TYPE_BRAND_MORE = 12;
  private static final int TYPE_DIRECT_ZONE = 14;
  private static final int TYPE_DIRECT_ZONE_MORE = 15;
  private static final int TYPE_EXT_SEARCH = 8;
  private static final int TYPE_EXT_SEARCH_MORE = 9;
  private static final int TYPE_GLOBAL_EMPTY = 18;
  private static final int TYPE_GLOBAL_MORE = 17;
  private static final int TYPE_GLOBAL_TITLE = 16;
  private static final int TYPE_KTV_DEAL = 10;
  private static final int TYPE_KTV_DEAL_MORE = 11;
  private static final int TYPE_MALL = 2;
  private static final int TYPE_MALL_MORE = 0;
  private static final int TYPE_NEARBY_DISTANCE = 25;
  private static final int TYPE_NEARBY_MALLS = 19;
  private static final int TYPE_PET = 20;
  private static final int TYPE_REC = 1;
  private static final int TYPE_REC_AD_GUIDE = 22;
  private static final int TYPE_SEARCH_MOVIE_DIRECTZONE = 21;
  private static final int TYPE_SHOP = 3;
  private static final int TYPE_STATE_EMPTY = 6;
  private static final int TYPE_STATE_FAIL = 7;
  private static final int TYPE_STATE_LAST = 5;
  private static final int TYPE_STATE_LOADING = 4;
  private static final int TYPE_TOPSHOP_BOTTOM = 24;
  private static final int TYPE_TOPSHOP_HEAD = 23;
  private static final int TYPE_TOP_REC = 13;
  protected AdapterItemClickListener adapterItemClickListener;
  public ArrayList<Object> brandShops = new ArrayList();
  public ArrayList<ExtSearchModel> extSearchModels = new ArrayList();
  public GlobalSearchResult globalSearchResult;
  public ArrayList<DealModel> ktvDeals = new ArrayList();
  protected NewShopListDataSource mDataSource;
  HashMap<Integer, Integer> mRepeatMap = null;
  public ArrayList<ShopDataModel> malls = new ArrayList();
  public ArrayList<Object> nearShops = new ArrayList();
  public int nextStartIndex = 0;
  public ArrayList<SearchDirectZoneModel> searchDirectZoneModels = new ArrayList();
  public ArrayList<Object> shops = new ArrayList();
  protected int targetType;
  protected int topMallCount;

  static
  {
    BRAND_MORE = new DPObject();
    TOP_REC = new DPObject();
    GLOBAL_TITLE = new DPObject();
    GLOBAL_MORE = new DPObject();
    GLOBAL_EMPTY = new DPObject();
    NEARBY_MALLS = new DPObject();
    SEARCH_MOVIE_DIRECTZONE = new DPObject();
    PET = new DPObject();
    TOP_SHOP_HEAD = new DPObject();
    TOP_SHOP_BOTTOM = new DPObject();
    NEARBY_DISTANCE = new DPObject();
  }

  public ShopListDataModelAdapter(ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler)
  {
    super(paramShopListReloadHandler);
  }

  private View getBrandMore(View paramView, ViewGroup paramViewGroup, int paramInt)
  {
    View localView = paramView;
    if (!(paramView instanceof ShowMoreItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_display_more, paramViewGroup, false);
    paramView = (ShowMoreItem)localView;
    ((TextView)localView.findViewById(R.id.display_more_count)).setText("更多" + (this.mDataSource.recordCount - 10) + "家商户");
    paramView.findViewById(R.id.divider).setVisibility(8);
    paramView.setClickable(true);
    paramView.setOnClickListener(new View.OnClickListener(paramInt)
    {
      public void onClick(View paramView)
      {
        if (ShopListDataModelAdapter.this.mDataSource.isEnd())
        {
          ShopListDataModelAdapter.this.shops.remove(this.val$position);
          ShopListDataModelAdapter.this.shops.addAll(this.val$position, ShopListDataModelAdapter.this.brandShops.subList(10, ShopListDataModelAdapter.this.brandShops.size()));
          ShopListDataModelAdapter.this.notifyDataSetChanged();
          return;
        }
        ShopListDataModelAdapter.this.shops.subList(10, ShopListDataModelAdapter.this.shops.size()).clear();
        ShopListDataModelAdapter.this.shops.addAll(ShopListDataModelAdapter.this.brandShops.subList(10, ShopListDataModelAdapter.this.brandShops.size()));
        ShopListDataModelAdapter.this.mDataSource.adShopModels = new AdShopDataModel[0];
        ShopListDataModelAdapter.this.notifyDataSetChanged();
      }
    });
    return localView;
  }

  private View getDirectZoneMoreView(View paramView, ViewGroup paramViewGroup, Object paramObject)
  {
    View localView = paramView;
    if (!(paramView instanceof ShowMoreItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_display_more, paramViewGroup, false);
    paramViewGroup = (ShowMoreItem)localView;
    paramView = (SearchDirectZoneModel)paramObject;
    paramViewGroup.setGAString("direct_zone_more");
    paramViewGroup = new StringBuffer();
    paramViewGroup.append("更多").append(this.searchDirectZoneModels.size() - 1);
    if (paramView.displayType == 6)
    {
      paramViewGroup.append("部").append("电影");
      ((TextView)localView.findViewById(R.id.display_more_count)).setText(paramViewGroup.toString());
      localView.setOnClickListener(new View.OnClickListener(paramObject)
      {
        public void onClick(View paramView)
        {
          ShopListDataModelAdapter.this.shops.remove(1);
          ShopListDataModelAdapter.this.shops.addAll(1, ShopListDataModelAdapter.this.searchDirectZoneModels.subList(1, ShopListDataModelAdapter.this.searchDirectZoneModels.size()));
          ShopListDataModelAdapter.this.mDataSource.isShowMoreDirectZone = true;
          ShopListDataModelAdapter.this.notifyDataSetChanged();
          if (ShopListDataModelAdapter.this.adapterItemClickListener != null)
            ShopListDataModelAdapter.this.adapterItemClickListener.onClick(paramView, this.val$obj);
        }
      });
      if ((localView.getContext() instanceof NovaActivity))
        ((NovaActivity)localView.getContext()).addGAView(localView, -1, "shoplist", true);
      return localView;
    }
    StringBuffer localStringBuffer = paramViewGroup.append("家");
    if (paramView.picLabel == null);
    for (paramView = ""; ; paramView = paramView.picLabel)
    {
      localStringBuffer.append(paramView);
      break;
    }
  }

  private View getDirectZoneView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    View localView = paramView;
    if (!(paramView instanceof SearchDirectZoneItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shoplist_direct_zone_layout, paramViewGroup, false);
    boolean bool1;
    boolean bool2;
    if ((paramInt < getCount() - 1) && (getItemViewType(paramInt + 1) != 14) && (getItemViewType(paramInt + 1) != 15) && (getItemViewType(paramInt + 1) != 1))
    {
      bool1 = true;
      if ((paramInt >= getCount() - 1) || (getItemViewType(paramInt + 1) != 14))
        break label255;
      bool2 = true;
      label106: paramView = (SearchDirectZoneItem)localView;
      paramViewGroup = (SearchDirectZoneModel)paramObject;
      paramView.setDirectZone(paramViewGroup, bool1, bool2, true);
      paramView.setGAString("direct_zone");
      paramView.gaUserInfo.index = Integer.valueOf(paramViewGroup.index);
      paramView.gaUserInfo.keyword = paramViewGroup.keyword;
      paramView.gaUserInfo.title = paramViewGroup.title;
      if ((localView.getContext() instanceof NovaActivity))
      {
        ((NovaActivity)localView.getContext()).addGAView(localView, paramViewGroup.index, "shoplist", true);
        if ((!android.text.TextUtils.isEmpty(paramViewGroup.mFeedback)) && (this.mRepeatMap != null))
        {
          paramView = (Integer)this.mRepeatMap.get(Integer.valueOf(paramInt));
          if (paramView != null)
            break label261;
          sendAdGA(paramInt, paramObject);
        }
      }
    }
    label255: 
    do
    {
      return localView;
      bool1 = false;
      break;
      bool2 = false;
      break label106;
    }
    while (paramView.intValue() != 0);
    label261: sendAdGA(paramInt, paramObject);
    return localView;
  }

  private View getExtSearchMoreView(View paramView, ViewGroup paramViewGroup, Object paramObject)
  {
    View localView = paramView;
    if (!(paramView instanceof ShowMoreItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_display_more, paramViewGroup, false);
    ((ShowMoreItem)localView).setGAString("direct_zone_more");
    localView.setClickable(true);
    paramView = (ExtSearchModel)paramObject;
    paramViewGroup = (TextView)localView.findViewById(R.id.display_more_count);
    StringBuilder localStringBuilder = new StringBuilder().append("更多").append(this.extSearchModels.size() - 1).append("个");
    if (paramView.type == null);
    for (paramView = ""; ; paramView = paramView.type)
    {
      paramViewGroup.setText(paramView);
      localView.setOnClickListener(new View.OnClickListener(paramObject)
      {
        public void onClick(View paramView)
        {
          ShopListDataModelAdapter.this.shops.remove(1);
          ShopListDataModelAdapter.this.shops.addAll(1, ShopListDataModelAdapter.this.extSearchModels.subList(1, ShopListDataModelAdapter.this.extSearchModels.size()));
          ShopListDataModelAdapter.this.mDataSource.isShowMoreExtSearch = true;
          ShopListDataModelAdapter.this.notifyDataSetChanged();
          if (ShopListDataModelAdapter.this.adapterItemClickListener != null)
            ShopListDataModelAdapter.this.adapterItemClickListener.onClick(paramView, this.val$obj);
        }
      });
      if ((localView.getContext() instanceof NovaActivity))
        ((NovaActivity)localView.getContext()).addGAView(localView, -1, "shoplist", true);
      return localView;
    }
  }

  private View getExtSearchView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    boolean bool2 = false;
    View localView = paramView;
    if (!(paramView instanceof ShopListExtItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shoplist_info_layout, paramViewGroup, false);
    boolean bool1 = bool2;
    if (paramInt < getCount() - 1)
    {
      bool1 = bool2;
      if (getItemViewType(paramInt + 1) != 8)
      {
        bool1 = bool2;
        if (getItemViewType(paramInt + 1) != 9)
        {
          bool1 = bool2;
          if (getItemViewType(paramInt + 1) != 1)
            bool1 = true;
        }
      }
    }
    paramView = (ExtSearchModel)paramObject;
    paramViewGroup = (ShopListExtItem)localView;
    paramViewGroup.setShopExt(paramView, bool1);
    paramViewGroup.setGAString("direct_zone");
    paramViewGroup.gaUserInfo.index = Integer.valueOf(paramView.index);
    paramViewGroup.gaUserInfo.keyword = paramView.keyword;
    paramViewGroup.gaUserInfo.title = paramView.title;
    if ((localView.getContext() instanceof NovaActivity))
    {
      ((NovaActivity)localView.getContext()).addGAView(localView, paramView.index, "shoplist", true);
      if ((!android.text.TextUtils.isEmpty(paramView.feedback)) && (this.mRepeatMap != null))
      {
        paramView = (Integer)this.mRepeatMap.get(Integer.valueOf(paramInt));
        if (paramView != null)
          break label238;
        sendAdGA(paramInt, paramObject);
      }
    }
    label238: 
    do
      return localView;
    while (paramView.intValue() != 0);
    sendAdGA(paramInt, paramObject);
    return localView;
  }

  private View getGlobalEmptyView(ViewGroup paramViewGroup)
  {
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.global_empty_layout, paramViewGroup, false);
    if ((this.mDataSource != null) && (this.mDataSource.globalSearchResult != null))
    {
      TextView localTextView = (TextView)paramViewGroup.findViewById(R.id.global_empty);
      if (!android.text.TextUtils.isEmpty(this.mDataSource.cityName))
      {
        String str = "{" + this.mDataSource.cityName + "站} 没有找到合适的商户哟";
        localTextView.setText(com.dianping.util.TextUtils.highLightShow(paramViewGroup.getContext(), str, R.color.tuan_common_orange));
      }
    }
    paramViewGroup.setClickable(true);
    return paramViewGroup;
  }

  private View getGlobalMoreView(ViewGroup paramViewGroup, int paramInt)
  {
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.global_more_layout, paramViewGroup, false);
    ((NovaLinearLayout)paramViewGroup).setGAString("global_search_more");
    ((NovaLinearLayout)paramViewGroup).gaUserInfo.keyword = this.mDataSource.suggestKeyword();
    View localView;
    if ((paramInt < getCount() - 1) && (getItemViewType(paramInt + 1) != 1))
    {
      paramInt = 1;
      localView = paramViewGroup.findViewById(R.id.divider);
      if (paramInt == 0)
        break label168;
      localView.setVisibility(0);
    }
    while (true)
    {
      if ((this.mDataSource != null) && (this.mDataSource.globalSearchResult != null))
      {
        ((TextView)paramViewGroup.findViewById(R.id.global_more)).setText(this.mDataSource.globalSearchResult.naviTitle);
        paramViewGroup.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(ShopListDataModelAdapter.this.mDataSource.globalSearchResult.naviUrl)));
          }
        });
      }
      if ((paramViewGroup.getContext() instanceof NovaActivity))
        ((NovaActivity)paramViewGroup.getContext()).addGAView(paramViewGroup, -1, "shoplist", true);
      return paramViewGroup;
      paramInt = 0;
      break;
      label168: localView.setVisibility(8);
    }
  }

  private View getGlobalTitleView(ViewGroup paramViewGroup)
  {
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.global_title_layout, paramViewGroup, false);
    TextView localTextView;
    if (this.mDataSource != null)
    {
      localTextView = (TextView)paramViewGroup.findViewById(R.id.global_title);
      if (!"globalshoplist".equals(this.mDataSource.targetPage))
        break label88;
      localTextView.setText("“" + this.mDataSource.suggestKeyword() + "”全球相关商户");
    }
    label88: 
    do
      return paramViewGroup;
    while (this.mDataSource.globalSearchResult == null);
    localTextView.setText(this.mDataSource.globalSearchResult.title);
    return paramViewGroup;
  }

  private View getKtvDealMoreView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    View localView = paramView;
    if (!(paramView instanceof ShowMoreItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_display_more, paramViewGroup, false);
    paramView = (Pair)paramObject;
    paramViewGroup = (ShowMoreItem)localView;
    paramViewGroup.setGAString("shoplist_moredeal");
    paramViewGroup.setLayoutParams(new AbsListView.LayoutParams(-1, localView.getContext().getResources().getDimensionPixelSize(R.dimen.search_deal_more_height)));
    paramObject = (TextView)paramViewGroup.findViewById(R.id.display_more_count);
    paramObject.setTextSize(0, localView.getContext().getResources().getDimensionPixelSize(R.dimen.basesearch_shoplist_info_text_size));
    paramObject.setTextColor(localView.getContext().getResources().getColor(R.color.light_gray));
    paramObject.setText("更多" + (((Integer)paramView.second).intValue() - ((Integer)paramView.first).intValue()) + "个团购");
    paramViewGroup.findViewById(R.id.divider).setVisibility(8);
    paramViewGroup.findViewById(R.id.divider_line_top).setVisibility(8);
    paramViewGroup.findViewById(R.id.divider_line).setVisibility(0);
    paramViewGroup.setClickable(true);
    paramViewGroup.setOnClickListener(new View.OnClickListener(paramInt, paramView)
    {
      public void onClick(View paramView)
      {
        ShopListDataModelAdapter.this.shops.remove(this.val$position);
        ShopListDataModelAdapter.this.shops.addAll(this.val$position, ShopListDataModelAdapter.this.ktvDeals.subList(((Integer)this.val$pair.first).intValue(), ((Integer)this.val$pair.second).intValue()));
        ShopListDataModelAdapter.this.notifyDataSetChanged();
      }
    });
    return localView;
  }

  private View getKtvDealView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    View localView = paramView;
    if (!(paramView instanceof ShopDealInfoItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_deal_info_item, paramViewGroup, false);
    paramView = (ShopDealInfoItem)localView;
    paramView.setShopDealInfo((DealModel)paramObject);
    int i = paramInt;
    while ((i >= 0) && ((this.shops.get(i) instanceof DealModel)))
      i -= 1;
    paramView.setGAString("shoplist_deal" + (paramInt - i - 1));
    return localView;
  }

  private View getMallMoreView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    View localView = paramView;
    if (!(paramView instanceof ShowMoreItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_display_more, paramViewGroup, false);
    ((ShowMoreItem)localView).setGAString("mall_more");
    localView.setClickable(true);
    ((TextView)localView.findViewById(R.id.display_more_count)).setText("更多" + (this.malls.size() - 1) + "家商场");
    localView.setOnClickListener(new View.OnClickListener(paramInt, paramObject)
    {
      public void onClick(View paramView)
      {
        ShopListDataModelAdapter.this.shops.remove(this.val$position);
        ShopListDataModelAdapter.this.shops.addAll(this.val$position, ShopListDataModelAdapter.this.malls.subList(1, ShopListDataModelAdapter.this.malls.size()));
        ShopListDataModelAdapter.this.mDataSource.isShowMoreMall = true;
        ShopListDataModelAdapter.this.notifyDataSetChanged();
        if (ShopListDataModelAdapter.this.adapterItemClickListener != null)
          ShopListDataModelAdapter.this.adapterItemClickListener.onClick(paramView, this.val$obj);
      }
    });
    if ((localView.getContext() instanceof NovaActivity))
      ((NovaActivity)localView.getContext()).addGAView(localView, -1, "shoplist", true);
    return localView;
  }

  private View getMallView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    boolean bool2 = false;
    View localView = paramView;
    if (!(paramView instanceof MallItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.mall_item, paramViewGroup, false);
    paramView = (ShopDataModel)paramObject;
    boolean bool1 = bool2;
    if (paramInt < getCount() - 1)
    {
      bool1 = bool2;
      if (getItemViewType(paramInt + 1) != 2)
      {
        bool1 = bool2;
        if (getItemViewType(paramInt + 1) != 0)
          bool1 = true;
      }
    }
    paramViewGroup = (MallItem)localView;
    paramViewGroup.setMall(paramView, this.offsetLatitude, this.offsetLongitude, this.shouldShowImage, bool1);
    paramViewGroup.setGAString("mall");
    paramViewGroup.gaUserInfo.shop_id = Integer.valueOf(paramView.shopId);
    paramViewGroup.gaUserInfo.index = Integer.valueOf(paramView.index);
    paramViewGroup.gaUserInfo.query_id = paramView.shopQueryId;
    if ((localView.getContext() instanceof NovaActivity))
      ((NovaActivity)localView.getContext()).addGAView(localView, paramView.index, "shoplist", true);
    return localView;
  }

  private View getNearByMallsView(View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    if (!(paramView instanceof NearbyMallsItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.nearby_malls_layout, paramViewGroup, false);
    paramView = (NearbyMallsItem)localView;
    paramView.setModels(this.mDataSource.searchDirectZoneModels);
    paramView.setGAString("nearby_mall_slide");
    return localView;
  }

  private View getNearbyDistanceView(View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView != null) && (paramView.getTag().equals("NEARBY_DISTANCE")))
      return paramView;
    paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_nearby_distance, paramViewGroup, false);
    paramView.setTag("NEARBY_DISTANCE");
    return paramView;
  }

  private View getPetView(ViewGroup paramViewGroup)
  {
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.global_title_layout, paramViewGroup, false);
    ((TextView)paramViewGroup.findViewById(R.id.global_title)).setText(this.mDataSource.targetInfo);
    return paramViewGroup;
  }

  private View getRecAdGuideView(ViewGroup paramViewGroup, int paramInt)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_rec_ad_guide_view, paramViewGroup, false);
    SearchRecAdGuideItem localSearchRecAdGuideItem = (SearchRecAdGuideItem)localNovaLinearLayout.findViewById(R.id.grid_view);
    localSearchRecAdGuideItem.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localSearchRecAdGuideItem.setPadding(ViewUtils.dip2px(paramViewGroup.getContext(), 6.0F), ViewUtils.dip2px(paramViewGroup.getContext(), 9.0F), ViewUtils.dip2px(paramViewGroup.getContext(), 6.0F), ViewUtils.dip2px(paramViewGroup.getContext(), 3.0F));
    localSearchRecAdGuideItem.setData((SearchRecAdGuideModel)getItem(paramInt), this.mDataSource);
    localSearchRecAdGuideItem.setClickable(true);
    localNovaLinearLayout.setGAString("ad_guidewords");
    localNovaLinearLayout.gaUserInfo.index = Integer.valueOf(((SearchRecAdGuideModel)getItem(paramInt)).index);
    if ((localNovaLinearLayout.getContext() instanceof NovaActivity))
      ((NovaActivity)localNovaLinearLayout.getContext()).addGAView(localNovaLinearLayout, localNovaLinearLayout.gaUserInfo.index.intValue(), "shoplist", true);
    return localNovaLinearLayout;
  }

  private View getRecView(ViewGroup paramViewGroup)
  {
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.keyword_suggest_layout, paramViewGroup, false);
    if (getItem(0) == EMPTY)
    {
      paramViewGroup.setBackgroundResource(R.color.white);
      return paramViewGroup;
    }
    paramViewGroup.setBackgroundResource(R.color.gray_background);
    return paramViewGroup;
  }

  private View getSearchMovieDirectZoneView(View paramView, ViewGroup paramViewGroup, int paramInt)
  {
    View localView = paramView;
    if (!(paramView instanceof SearchMovieDirectZoneView))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_movie_directzone_view, paramViewGroup, false);
    paramView = (SearchMovieDirectZoneView)localView;
    paramView.setMovieGuide(this.mDataSource.searchDirectZoneModels);
    paramView.setGAString("direct_zone");
    paramView.gaUserInfo.index = Integer.valueOf(paramInt);
    paramView.gaUserInfo.keyword = this.mDataSource.suggestKeyword();
    if ((localView.getContext() instanceof NovaActivity))
      ((NovaActivity)localView.getContext()).addGAView(localView, paramInt, "shoplist", true);
    return localView;
  }

  private View getShopView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    boolean bool = true;
    View localView = paramView;
    if (!(paramView instanceof ShopListItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_item, paramViewGroup, false);
    paramView = (ShopListItem)localView;
    paramViewGroup = (ShopDataModel)paramObject;
    if ((paramViewGroup instanceof AdShopDataModel))
    {
      paramView.setGAString("supply");
      paramView.gaUserInfo.shop_id = Integer.valueOf(paramViewGroup.shopId);
      paramView.gaUserInfo.index = Integer.valueOf(paramViewGroup.index);
      paramView.gaUserInfo.query_id = paramViewGroup.shopQueryId;
      paramView.gaUserInfo.title = paramViewGroup.fullName;
      if ((localView.getContext() instanceof NovaActivity))
      {
        if ((paramViewGroup.tag == null) || ((paramViewGroup.tag != ShopListItem.SHOP_TITLE_STYLE) && (paramViewGroup.tag != ShopListItem.SHOP_SIMPLE_STYLE)))
          break label358;
        ((NovaActivity)localView.getContext()).addGAView(localView, paramViewGroup.index, "nearby_headlines", true);
      }
      label164: if ((paramInt >= getCount() - 1) || (getItemViewType(paramInt + 1) == 1) || (getItemViewType(paramInt + 1) == 13) || (getItemViewType(paramInt + 1) == 22))
        break label382;
      label213: if (!this.isRank)
        break label388;
      paramInt += 1;
    }
    while (true)
    {
      paramView.setShop(paramViewGroup, paramInt, this.offsetLatitude, this.offsetLongitude, this.shouldShowImage, false, paramViewGroup.tag);
      paramView.setItemDividerLine(bool);
      return localView;
      if (paramViewGroup.isGlobal)
      {
        paramView.setGAString("global_search_item");
        break;
      }
      if ((paramViewGroup.tag != null) && (paramViewGroup.tag == ShopListItem.SHOP_TITLE_STYLE))
      {
        paramView.setGAString("headline");
        break;
      }
      if ((paramViewGroup.tag != null) && (paramViewGroup.tag == ShopListItem.SHOP_SIMPLE_STYLE))
      {
        paramView.setGAString("piece");
        break;
      }
      if (paramViewGroup.viewType == 1)
      {
        paramView.setGAString("piece");
        break;
      }
      paramView.setGAString("item");
      break;
      label358: ((NovaActivity)localView.getContext()).addGAView(localView, paramViewGroup.index, "shoplist", true);
      break label164;
      label382: bool = false;
      break label213;
      label388: paramInt = -1;
    }
  }

  private View getStateEmptyView(View paramView, ViewGroup paramViewGroup)
  {
    paramView = getShopEmptyView(paramViewGroup, paramView);
    if ((this.mDataSource != null) && (this.mDataSource.targetType == 2) && ((paramView instanceof ViewGroup)))
    {
      int i = 0;
      while (i < ((ViewGroup)paramView).getChildCount())
      {
        ((ViewGroup)paramView).getChildAt(i).setVisibility(8);
        i += 1;
      }
    }
    return paramView;
  }

  private View getStateFailView(View paramView, ViewGroup paramViewGroup)
  {
    paramView = getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        ShopListDataModelAdapter.this.reloadHandler.reload(false);
      }
    }
    , paramViewGroup, paramView);
    paramView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ((LoadingErrorView)paramView).onClick(paramView);
      }
    });
    return paramView;
  }

  private View getStateLoadingView(View paramView, ViewGroup paramViewGroup)
  {
    paramView = getLoadingView(paramViewGroup, paramView);
    if ((this.mDataSource == null) || (this.nextStartIndex != 0))
      this.reloadHandler.reload(false);
    return paramView;
  }

  private View getTopRecView(ViewGroup paramViewGroup)
  {
    return LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.top_rec_layout, paramViewGroup, false);
  }

  private View getTopShopBottom(View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView != null) && (paramView.getTag().equals("TOP_SHOP_BOTTOM")))
      return paramView;
    paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_nearby_topshop_bottom, paramViewGroup, false);
    paramView.setTag("TOP_SHOP_BOTTOM");
    return paramView;
  }

  private View getTopShopHead(View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView != null) && (paramView.getTag().equals("TOP_SHOP_HEAD")))
      return paramView;
    paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_nearby_topshop_header, paramViewGroup, false);
    paramView.setTag("TOP_SHOP_HEAD");
    return paramView;
  }

  private void sendAdGA(int paramInt, Object paramObject)
  {
    Object localObject;
    if ((paramObject instanceof ExtSearchModel))
    {
      Log.d("debug_AdGA", "Impression-GA-Ext");
      localObject = (ExtSearchModel)paramObject;
      this.mRepeatMap.put(Integer.valueOf(paramInt), Integer.valueOf(1));
      HashMap localHashMap = new HashMap();
      localHashMap.put("act", "3");
      localHashMap.put("adidx", String.valueOf(((ExtSearchModel)localObject).index + 1));
      AdClientUtils.report(((ExtSearchModel)localObject).feedback, localHashMap);
    }
    if ((paramObject instanceof SearchDirectZoneModel))
    {
      Log.d("debug_AdGA", "Impression-GA-DirectZone");
      paramObject = (SearchDirectZoneModel)paramObject;
      this.mRepeatMap.put(Integer.valueOf(paramInt), Integer.valueOf(1));
      localObject = new HashMap();
      ((Map)localObject).put("act", "3");
      ((Map)localObject).put("adidx", String.valueOf(paramObject.index + 1));
      AdClientUtils.report(paramObject.mFeedback, (Map)localObject);
    }
  }

  protected void fillShops()
  {
    this.topMallCount = this.mDataSource.topMallCount();
    this.shops.clear();
    this.malls.clear();
    int i = 0;
    Object localObject;
    if (i < this.mDataSource.shopModels.size())
    {
      localObject = (ShopDataModel)this.mDataSource.shopModels.get(i);
      if ((i < this.topMallCount) && (((ShopDataModel)localObject).isMall))
        this.malls.add(localObject);
      while (true)
      {
        i += 1;
        break;
        this.shops.add(localObject);
      }
    }
    if ((1 < this.malls.size()) && (!this.mDataSource.isShowMoreMall))
    {
      this.shops.addAll(0, this.malls.subList(0, 1));
      this.shops.add(1, MALL_MORE);
      if ((this.extSearchModels != null) && (!this.extSearchModels.isEmpty()))
      {
        if ((1 >= this.extSearchModels.size()) || (this.mDataSource.isShowMoreExtSearch))
          break label549;
        this.shops.addAll(0, this.extSearchModels.subList(0, 1));
        this.shops.add(1, ExtSearchModel.getMorePlaceHolder(((ExtSearchModel)this.extSearchModels.get(0)).type));
      }
      label230: if ((this.searchDirectZoneModels != null) && (!this.searchDirectZoneModels.isEmpty()))
        switch (this.mDataSource.searchDirectZoneExpandType)
        {
        default:
        case 0:
        case 1:
        case 2:
        }
    }
    int k;
    while (true)
    {
      j = 0;
      int m = 0;
      if (this.mDataSource.recResultCount <= 0)
        break label710;
      j = this.shops.size() - this.mDataSource.recResultCount;
      if (j <= 0)
      {
        i = m;
        k = j;
        if (this.mDataSource.targetType != 2)
        {
          i = m;
          k = j;
          if (j != 0);
        }
      }
      else
      {
        this.shops.add(j, REC_TITLE);
        m = 1;
        i = m;
        k = j;
        if (j == 0)
        {
          this.shops.add(0, EMPTY);
          k = j + 1;
          i = m;
        }
      }
      j = i;
      if (this.mDataSource.recAdGuideKeywords == null)
        break label710;
      localObject = new SearchRecAdGuideModel();
      ((SearchRecAdGuideModel)localObject).guideKeywords = this.mDataSource.recAdGuideKeywords;
      if (this.mDataSource.recResultCount <= 3)
        break label673;
      this.shops.add(k + 1 + 3, localObject);
      ((SearchRecAdGuideModel)localObject).index = ((ShopDataModel)this.shops.get(k + 2 + 3)).index;
      k = k + 2 + 3;
      while (true)
      {
        j = i;
        if (k >= this.shops.size())
          break;
        if ((this.shops.get(k) instanceof ShopDataModel))
        {
          localObject = (ShopDataModel)this.shops.get(k);
          ((ShopDataModel)localObject).index += 1;
        }
        k += 1;
      }
      this.shops.addAll(0, this.malls);
      break;
      label549: this.shops.addAll(0, this.extSearchModels);
      break label230;
      if ((1 < this.searchDirectZoneModels.size()) && (!this.mDataSource.isShowMoreDirectZone))
      {
        this.shops.addAll(0, this.searchDirectZoneModels.subList(0, 1));
        this.shops.add(1, SearchDirectZoneModel.getMorePlaceHolder((SearchDirectZoneModel)this.searchDirectZoneModels.get(0)));
        continue;
      }
      this.shops.addAll(0, this.searchDirectZoneModels);
      continue;
      this.shops.add(0, NEARBY_MALLS);
      continue;
      this.shops.add(0, SEARCH_MOVIE_DIRECTZONE);
    }
    label673: ((SearchRecAdGuideModel)localObject).index = ((ShopDataModel)this.shops.get(this.shops.size() - 1)).index;
    this.shops.add(localObject);
    int j = i;
    label710: if ((this.globalSearchResult != null) && (this.globalSearchResult.shopModels.length > -1))
    {
      k = this.shops.size() - this.mDataSource.recResultCount;
      i = k;
      if (j != 0)
        i = k - 1;
      j = i;
      if (!this.shops.isEmpty())
      {
        j = i;
        if (EMPTY.equals(this.shops.get(0)))
          j = 1;
      }
      this.shops.add(j, GLOBAL_TITLE);
      i = 0;
      while (i < this.globalSearchResult.shopModels.length)
      {
        this.shops.add(j + i + 1, this.globalSearchResult.shopModels[i]);
        i += 1;
      }
      if (this.globalSearchResult.naviTitle != null)
        this.shops.add(this.globalSearchResult.shopModels.length + j + 1, GLOBAL_MORE);
    }
    if (this.targetType == 5)
      this.shops.add(0, PET);
    if (("globalshoplist".equals(this.mDataSource.targetPage)) && (!this.mDataSource.shopModels.isEmpty()))
      this.shops.add(0, GLOBAL_TITLE);
    i = 0;
    if (i < this.shops.size())
    {
      j = i;
      if ((this.shops.get(i) instanceof KtvShopDataModel))
      {
        j = i + 1;
        localObject = ((KtvShopDataModel)this.shops.get(i)).mDealModels;
        this.ktvDeals.addAll((Collection)localObject);
        if (((List)localObject).size() > 1)
          break label1032;
        this.shops.addAll(j, (Collection)localObject);
        i = j + ((List)localObject).size();
      }
      while (true)
      {
        j = i - 1;
        i = j + 1;
        break;
        label1032: this.shops.addAll(j, ((List)localObject).subList(0, 1));
        i = j + 1;
        this.shops.add(i, new Pair(Integer.valueOf(this.ktvDeals.size() - ((List)localObject).size() + 1), Integer.valueOf(this.ktvDeals.size())));
        i += 1;
      }
    }
    if ((this.mDataSource.adShopModels != null) && (this.mDataSource.adShopModels.length > 0))
    {
      if (this.shops.size() > 10)
      {
        this.brandShops = new ArrayList(this.shops);
        this.shops.subList(10, this.shops.size()).clear();
        this.shops.add(BRAND_MORE);
      }
      if (this.shops.isEmpty())
        this.shops.add(EMPTY);
      this.shops.add(REC_TITLE);
      i = this.shops.size() - 1;
      this.shops.addAll(Arrays.asList(this.mDataSource.adShopModels));
      if (this.mDataSource.recAdGuideKeywords != null)
      {
        localObject = new SearchRecAdGuideModel();
        ((SearchRecAdGuideModel)localObject).guideKeywords = this.mDataSource.recAdGuideKeywords;
        if (this.mDataSource.adShopModels.length > 3)
        {
          this.shops.add(i + 1 + 3, localObject);
          ((SearchRecAdGuideModel)localObject).index = ((ShopDataModel)this.shops.get(i + 2 + 3)).index;
          i = i + 2 + 3;
          while (i < this.shops.size())
          {
            if ((this.shops.get(i) instanceof ShopDataModel))
            {
              localObject = (ShopDataModel)this.shops.get(i);
              ((ShopDataModel)localObject).index += 1;
            }
            i += 1;
          }
        }
        ((SearchRecAdGuideModel)localObject).index = ((ShopDataModel)this.shops.get(this.shops.size() - 1)).index;
        this.shops.add(localObject);
      }
    }
    if (this.globalSearchResult != null)
    {
      if ((this.shops.isEmpty()) || (!EMPTY.equals(this.shops.get(0))))
        break label1600;
      this.shops.set(0, GLOBAL_EMPTY);
    }
    while (true)
    {
      if ((this.mDataSource.topWeddingShopCount > 0) && (this.mDataSource.topWeddingShopCount <= this.shops.size()))
        this.shops.add(this.mDataSource.topWeddingShopCount, TOP_REC);
      if ((this.mDataSource.nearbyHeadlineShops != null) && (this.mDataSource.nearbyHeadlineShops.size() > 0))
      {
        this.shops.add(0, TOP_SHOP_HEAD);
        this.shops.addAll(1, this.mDataSource.nearbyHeadlineShops);
        this.shops.add(this.mDataSource.nearbyHeadlineShops.size() + 1, TOP_SHOP_BOTTOM);
        this.shops.add(this.mDataSource.nearbyHeadlineShops.size() + 2, NEARBY_DISTANCE);
      }
      return;
      label1600: if (!this.mDataSource.shopModels.isEmpty())
        continue;
      this.shops.add(0, GLOBAL_EMPTY);
    }
  }

  public int getCount()
  {
    if (this.mDataSource == null)
      return 0;
    switch (this.status)
    {
    case 2:
    default:
      if (!this.isEnd)
        break label141;
      if (((this.lastExtraView == null) || (this.shops.size() <= 0) || (this.shops.get(0) == EMPTY)) && (this.shops.size() != 0))
        break;
      return this.shops.size() + 1;
    case 1:
      if (this.isPullToRefresh)
        return this.shops.size();
      return this.shops.size() + 1;
    case 3:
      return this.shops.size() + 1;
    }
    return this.shops.size();
    label141: if ((this.mDataSource.adShopModels != null) && (this.mDataSource.adShopModels.length > 0))
      return this.shops.size();
    return this.shops.size() + 1;
  }

  public Object getItem(int paramInt)
  {
    switch (this.status)
    {
    case 2:
    default:
      if (!this.isEnd)
        break label125;
      if (paramInt >= this.shops.size())
        break;
      return this.shops.get(paramInt);
    case 1:
      if (paramInt < this.shops.size())
        return this.shops.get(paramInt);
      return LOADING;
    case 3:
      if (paramInt < this.shops.size())
        return this.shops.get(paramInt);
      return ERROR;
    }
    if (this.shops.size() != 0)
      return LAST_EXTRA;
    return EMPTY;
    label125: if (paramInt < this.shops.size())
      return this.shops.get(paramInt);
    return LOADING;
  }

  public int getItemViewType(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if (localObject == MALL_MORE)
      return 0;
    if (localObject == BRAND_MORE)
      return 12;
    if (localObject == REC_TITLE)
      return 1;
    if (localObject == TOP_REC)
      return 13;
    if (localObject == GLOBAL_TITLE)
      return 16;
    if (localObject == GLOBAL_MORE)
      return 17;
    if (localObject == GLOBAL_EMPTY)
      return 18;
    if (localObject == NEARBY_MALLS)
      return 19;
    if (localObject == SEARCH_MOVIE_DIRECTZONE)
      return 21;
    if (localObject == PET)
      return 20;
    if ((localObject instanceof SearchDirectZoneModel))
    {
      if (((SearchDirectZoneModel)localObject).isMorePlaceHolder())
        return 15;
      return 14;
    }
    if ((localObject instanceof SearchRecAdGuideModel))
      return 22;
    if ((localObject instanceof ExtSearchModel))
    {
      if (((ExtSearchModel)localObject).isMorePlaceHolder())
        return 9;
      return 8;
    }
    if ((localObject instanceof ShopDataModel))
    {
      if (((ShopDataModel)localObject).isMall)
        return 2;
      return 3;
    }
    if ((localObject instanceof DealModel))
      return 10;
    if (((localObject instanceof Pair)) && ((((Pair)localObject).first instanceof Integer)) && ((((Pair)localObject).second instanceof Integer)))
      return 11;
    if (localObject == LOADING)
      return 4;
    if (localObject == LAST_EXTRA)
      return 5;
    if (localObject == EMPTY)
      return 6;
    if (localObject == TOP_SHOP_HEAD)
      return 23;
    if (localObject == TOP_SHOP_BOTTOM)
      return 24;
    if (localObject == NEARBY_DISTANCE)
      return 25;
    return 7;
  }

  protected View getShopEmptyView(ViewGroup paramViewGroup, View paramView)
  {
    if ((paramView != null) && (paramView.getTag() == EMPTY))
      return paramView;
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.empty_btn_item_rec, paramViewGroup, false);
    paramViewGroup.setTag(EMPTY);
    paramViewGroup.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopListDataModelAdapter.this.lastExtraView.performClick();
      }
    });
    return paramViewGroup;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    switch (getItemViewType(paramInt))
    {
    default:
      return paramView;
    case 3:
      return getShopView(paramView, paramViewGroup, localObject, paramInt);
    case 2:
      return getMallView(paramView, paramViewGroup, localObject, paramInt);
    case 0:
      return getMallMoreView(paramView, paramViewGroup, localObject, paramInt);
    case 8:
      return getExtSearchView(paramView, paramViewGroup, localObject, paramInt);
    case 9:
      return getExtSearchMoreView(paramView, paramViewGroup, localObject);
    case 14:
      return getDirectZoneView(paramView, paramViewGroup, localObject, paramInt);
    case 15:
      return getDirectZoneMoreView(paramView, paramViewGroup, localObject);
    case 10:
      return getKtvDealView(paramView, paramViewGroup, localObject, paramInt);
    case 11:
      return getKtvDealMoreView(paramView, paramViewGroup, localObject, paramInt);
    case 12:
      return getBrandMore(paramView, paramViewGroup, paramInt);
    case 20:
      return getPetView(paramViewGroup);
    case 16:
      return getGlobalTitleView(paramViewGroup);
    case 17:
      return getGlobalMoreView(paramViewGroup, paramInt);
    case 18:
      return getGlobalEmptyView(paramViewGroup);
    case 19:
      return getNearByMallsView(paramView, paramViewGroup);
    case 21:
      return getSearchMovieDirectZoneView(paramView, paramViewGroup, paramInt);
    case 1:
      return getRecView(paramViewGroup);
    case 13:
      return getTopRecView(paramViewGroup);
    case 22:
      return getRecAdGuideView(paramViewGroup, paramInt);
    case 4:
      return getStateLoadingView(paramView, paramViewGroup);
    case 6:
      return getStateEmptyView(paramView, paramViewGroup);
    case 5:
      return this.lastExtraView;
    case 7:
      return getStateFailView(paramView, paramViewGroup);
    case 23:
      return getTopShopHead(paramView, paramViewGroup);
    case 24:
      return getTopShopBottom(paramView, paramViewGroup);
    case 25:
    }
    return getNearbyDistanceView(paramView, paramViewGroup);
  }

  public int getViewTypeCount()
  {
    return 26;
  }

  public void setAdapterItemClickListener(AdapterItemClickListener paramAdapterItemClickListener)
  {
    this.adapterItemClickListener = paramAdapterItemClickListener;
  }

  public void setShopList(NewShopListDataSource paramNewShopListDataSource)
  {
    if (paramNewShopListDataSource == null)
      return;
    this.mDataSource = paramNewShopListDataSource;
    this.status = paramNewShopListDataSource.status();
    this.isRank = false;
    this.isEnd = paramNewShopListDataSource.isEnd();
    this.lastExtraView = paramNewShopListDataSource.lastExtraView();
    this.offsetLatitude = paramNewShopListDataSource.offsetLatitude();
    this.offsetLongitude = paramNewShopListDataSource.offsetLongitude();
    this.showDistance = paramNewShopListDataSource.showDistance();
    this.errorMsg = paramNewShopListDataSource.errorMsg();
    this.isHotelType = paramNewShopListDataSource.hasSearchDate();
    this.extSearchModels = paramNewShopListDataSource.extSearchModels;
    this.searchDirectZoneModels = paramNewShopListDataSource.searchDirectZoneModels;
    this.globalSearchResult = paramNewShopListDataSource.globalSearchResult;
    this.nextStartIndex = paramNewShopListDataSource.nextStartIndex();
    this.targetType = paramNewShopListDataSource.targetType;
    this.mRepeatMap = new HashMap();
    fillShops();
    notifyDataSetChanged();
  }

  public static abstract interface AdapterItemClickListener
  {
    public abstract void onClick(View paramView, Object paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.ShopListDataModelAdapter
 * JD-Core Version:    0.6.0
 */