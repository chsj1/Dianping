package com.dianping.shopinfo.dish;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.GroupCellAgent;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.DealListItem;
import com.dianping.base.widget.TableView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaFrameLayout;
import com.dianping.widget.view.NovaLinearLayout;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DishDetailInfoListAgent extends GroupCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, TabHost.TabContentFactory, TabHost.OnTabChangeListener
{
  static final String CELL_DISH_SUGGEST = "0300Dish.0300List";
  private double latitude = 1.0D;
  private double longitude = 1.0D;
  private MApiRequest mDishTuanListRequest;
  DPObject mDishTuanResult;
  private Map<String, Integer> mIndexHash = new HashMap();
  private DPObject[] mRankingList;
  private ViewGroup[] mRankingListView;
  private MApiRequest mRankingReq;
  private TabHost mTabHost;
  private Map<Integer, TableItem> mTagHash = new HashMap();
  ArrayList<DPObject> mTempRankingList = new ArrayList();
  DPObject[] mTuanDeals;
  View mTuanListView;
  String moreUrl;
  boolean rankingApiHasRequested = false;
  String[] rankingTabTitle;
  boolean tuanApiHasRequested = false;
  String tuanTabTitle;

  public DishDetailInfoListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initTabView()
  {
    Object localObject;
    if ((this.mTuanDeals != null) && (this.mTuanDeals.length > 0))
    {
      this.mTuanListView = this.res.inflate(getContext(), R.layout.dish_detail_tuan_list_layout, getParentView(), false);
      localObject = (TableView)this.mTuanListView.findViewById(R.id.district_enjoy_table);
      ((TableView)localObject).setAdapter(new TuanListAdapter(this.mTuanDeals, this.moreUrl));
      if (Build.VERSION.SDK_INT <= 10)
        ((TableView)localObject).setBackgroundColor(-1);
    }
    int i;
    if ((this.mTempRankingList != null) && (this.mTempRankingList.size() > 0))
    {
      int j = this.mTempRankingList.size();
      this.mRankingListView = new ViewGroup[j];
      this.rankingTabTitle = new String[j];
      i = 0;
      while (i < j)
      {
        if ((((DPObject)this.mTempRankingList.get(i)).getArray("List") != null) && (((DPObject)this.mTempRankingList.get(i)).getArray("List").length > 0))
        {
          this.mRankingListView[i] = ((ViewGroup)this.res.inflate(getContext(), R.layout.dish_detail_shop_list_layout, getParentView(), false));
          this.rankingTabTitle[i] = ((DPObject)this.mTempRankingList.get(i)).getString("TabTitle");
          ((TableView)this.mRankingListView[i].findViewById(R.id.district_enjoy_table)).setAdapter(new RankingAdapter(((DPObject)this.mTempRankingList.get(i)).getArray("List"), this.rankingTabTitle[i], i));
        }
        i += 1;
      }
    }
    View localView = this.res.inflate(getContext(), R.layout.dish_detail_tabhost, getParentView(), false);
    TabWidget localTabWidget = (TabWidget)localView.findViewById(16908307);
    this.mTabHost = ((TabHost)localView.findViewById(16908306));
    this.mTabHost.setFocusable(false);
    this.mTabHost.setup();
    StringBuffer localStringBuffer;
    if ((this.mTagHash != null) && (this.mTagHash.size() > 0))
    {
      localStringBuffer = new StringBuffer();
      i = 0;
      Iterator localIterator = this.mTagHash.values().iterator();
      if (localIterator.hasNext())
      {
        TableItem localTableItem = (TableItem)localIterator.next();
        if (this.mTagHash.size() == 1);
        for (localObject = (RelativeLayout)this.res.inflate(getContext(), R.layout.dish_detail_tab_indicator_single, null, false); ; localObject = (RelativeLayout)this.res.inflate(getContext(), R.layout.dish_detail_tab_indicator, null, false))
        {
          TextView localTextView1 = (TextView)((RelativeLayout)localObject).findViewById(R.id.title);
          TextView localTextView2 = (TextView)((RelativeLayout)localObject).findViewById(R.id.subtitle);
          localTextView1.setText(localTableItem.title);
          if (!android.text.TextUtils.isEmpty(localTableItem.subtitle))
          {
            i = 1;
            localTextView2.setText(com.dianping.util.TextUtils.jsonParseText(localTableItem.subtitle));
            localTextView2.setVisibility(0);
          }
          this.mTabHost.addTab(this.mTabHost.newTabSpec(localTableItem.title).setIndicator((View)localObject).setContent(this));
          localStringBuffer.append(localTableItem.title);
          break;
        }
      }
      if (i != 0)
        break label677;
      localTabWidget.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 45.0F)));
    }
    while (true)
    {
      this.mTabHost.setCurrentTabByTag(((TableItem)this.mTagHash.get(Integer.valueOf(0))).title);
      this.mTabHost.setOnTabChangedListener(this);
      addCell("0300Dish.0300List", localView);
      GAHelper.instance().contextStatisticsEvent(getContext(), "shortcut", localStringBuffer.toString(), 0, "view");
      return;
      label677: localTabWidget.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 58.0F)));
    }
  }

  private void sendRankingRequest()
  {
    if (this.mRankingReq != null)
      getFragment().mapiService().abort(this.mRankingReq, this, true);
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/multidishshop.bin");
    localStringBuffer.append("?dishid=").append(((DishDetailInfoFragment)getFragment()).dishshopid).append("&cityid=").append(cityId());
    DPObject localDPObject = getFragment().locationService().location();
    if (localDPObject != null)
    {
      this.latitude = localDPObject.getDouble("Lat");
      this.longitude = localDPObject.getDouble("Lng");
      localStringBuffer.append("&lat=").append(Location.FMT.format(this.latitude)).append("&lng=").append(Location.FMT.format(this.longitude)).append("&locatecityid=").append(localDPObject.getObject("City").getInt("ID"));
    }
    this.mRankingReq = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRankingReq, this);
  }

  private void sendTuanRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mshop/dishdealgroup.bin").buildUpon();
    Object localObject = getFragment().locationService().location();
    String str2 = "";
    String str3 = "";
    String str1;
    if (localObject != null)
    {
      this.latitude = ((DPObject)localObject).getDouble("Lat");
      this.longitude = ((DPObject)localObject).getDouble("Lng");
      localObject = str2;
      str1 = str3;
      if (this.latitude != 0.0D)
      {
        localObject = str2;
        str1 = str3;
        if (this.longitude != 0.0D)
        {
          localObject = str2;
          str1 = str3;
          if (this.latitude != (-1.0D / 0.0D))
          {
            localObject = str2;
            str1 = str3;
            if (this.latitude != (1.0D / 0.0D))
            {
              localObject = str2;
              str1 = str3;
              if (this.longitude != (-1.0D / 0.0D))
              {
                localObject = str2;
                str1 = str3;
                if (this.longitude != (1.0D / 0.0D))
                {
                  localObject = Location.FMT.format(this.latitude);
                  str1 = Location.FMT.format(this.longitude);
                }
              }
            }
          }
        }
      }
    }
    try
    {
      while (true)
      {
        i = getFragment().locationService().city().getInt("ID");
        localBuilder.appendQueryParameter("cityid", "" + cityId());
        localBuilder.appendQueryParameter("locatedcityid", "" + i);
        localBuilder.appendQueryParameter("dishshopid", ((DishDetailInfoFragment)getFragment()).dishshopid);
        localBuilder.appendQueryParameter("lat", (String)localObject);
        localBuilder.appendQueryParameter("lng", str1);
        this.mDishTuanListRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
        getFragment().mapiService().exec(this.mDishTuanListRequest, this);
        return;
        localObject = Location.FMT.format(0L);
        str1 = Location.FMT.format(0L);
      }
    }
    catch (Exception localException)
    {
      while (true)
        int i = 1;
    }
  }

  public View createTabContent(String paramString)
  {
    if ((!android.text.TextUtils.isEmpty(this.tuanTabTitle)) && (this.tuanTabTitle.equals(paramString)))
      return this.mTuanListView;
    if ((!android.text.TextUtils.isEmpty(this.rankingTabTitle[0])) && (this.rankingTabTitle[0].equals(paramString)))
      return this.mRankingListView[0];
    if ((!android.text.TextUtils.isEmpty(this.rankingTabTitle[1])) && (this.rankingTabTitle[1].equals(paramString)))
      return this.mRankingListView[1];
    return new View(getContext());
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.mDishTuanResult == null) && ((this.mRankingList == null) || (this.mRankingList.length == 0)))
      removeAllCells();
    do
      return;
    while ((this.tuanApiHasRequested != true) || (this.rankingApiHasRequested != true));
    int k = 0;
    int i = 0;
    Object localObject;
    if (this.mRankingList != null)
    {
      int j = 0;
      k = i;
      if (j < this.mRankingList.length)
      {
        paramBundle = new TableItem();
        paramBundle.title = this.mRankingList[j].getString("TabTitle");
        paramBundle.subtitle = this.mRankingList[j].getString("SubTabTitle");
        localObject = this.mRankingList[j].getArray("List");
        if (localObject != null);
        for (paramBundle.listCount = localObject.length; ; paramBundle.listCount = 0)
        {
          k = i;
          if (paramBundle.listCount > 0)
          {
            this.mTempRankingList.add(this.mRankingList[j]);
            this.mTagHash.put(Integer.valueOf(i), paramBundle);
            this.mIndexHash.put(this.mRankingList[j].getString("TabTitle"), Integer.valueOf(i));
            k = i + 1;
          }
          j += 1;
          i = k;
          break;
        }
      }
    }
    if (this.mDishTuanResult != null)
    {
      this.mTuanDeals = this.mDishTuanResult.getArray("Deals");
      if ((this.mTuanDeals != null) && (this.mTuanDeals.length > 0))
      {
        this.tuanTabTitle = this.mDishTuanResult.getString("Title");
        paramBundle = this.mDishTuanResult.getString("SubTitle");
        this.moreUrl = this.mDishTuanResult.getString("MoreUrl");
        localObject = new TableItem();
        ((TableItem)localObject).title = this.tuanTabTitle;
        ((TableItem)localObject).subtitle = paramBundle;
        ((TableItem)localObject).listCount = this.mTuanDeals.length;
        this.mTagHash.put(Integer.valueOf(k), localObject);
        this.mIndexHash.put(this.tuanTabTitle, Integer.valueOf(k));
      }
    }
    initTabView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRankingRequest();
    sendTuanRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mRankingReq != null)
    {
      getFragment().mapiService().abort(this.mRankingReq, this, true);
      this.mRankingReq = null;
    }
    do
      return;
    while (this.mDishTuanListRequest == null);
    getFragment().mapiService().abort(this.mDishTuanListRequest, this, true);
    this.mDishTuanListRequest = null;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mDishTuanListRequest == paramMApiRequest)
    {
      this.mDishTuanResult = null;
      this.tuanApiHasRequested = true;
      dispatchAgentChanged(false);
    }
    do
      return;
    while (this.mRankingReq != paramMApiRequest);
    this.mRankingList = null;
    this.rankingApiHasRequested = true;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mDishTuanListRequest == paramMApiRequest)
    {
      this.mDishTuanResult = ((DPObject)paramMApiResponse.result());
      this.tuanApiHasRequested = true;
      dispatchAgentChanged(false);
    }
    do
    {
      do
        return;
      while ((paramMApiRequest == null) || (this.mRankingReq != paramMApiRequest));
      this.mRankingReq = null;
    }
    while ((paramMApiResponse.result() == null) || (!(paramMApiResponse.result() instanceof DPObject[])));
    this.mRankingList = ((DPObject[])(DPObject[])paramMApiResponse.result());
    this.rankingApiHasRequested = true;
    dispatchAgentChanged(false);
  }

  public void onTabChanged(String paramString)
  {
    this.mTabHost.setCurrentTabByTag(paramString);
    if (this.mIndexHash == null);
  }

  class RankingAdapter extends BasicAdapter
  {
    DPObject[] dataList;
    String mGaTable;
    int mSortID;

    public RankingAdapter(DPObject[] paramString, String paramInt, int arg4)
    {
      this.dataList = paramString;
      this.mGaTable = paramInt;
      int i;
      this.mSortID = i;
    }

    public int getCount()
    {
      return this.dataList.length;
    }

    public Object getItem(int paramInt)
    {
      return this.dataList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
      {
        paramViewGroup = DishDetailInfoListAgent.this.res.inflate(DishDetailInfoListAgent.this.getContext(), R.layout.shop_item, DishDetailInfoListAgent.this.getParentView(), false);
        paramViewGroup.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
      }
      paramView = (ShopListItem)paramViewGroup;
      paramViewGroup = this.dataList[paramInt];
      paramView.setShop(paramViewGroup, -1, DishDetailInfoListAgent.this.latitude, DishDetailInfoListAgent.this.longitude, true);
      GAUserInfo localGAUserInfo = DishDetailInfoListAgent.this.getGAExtra();
      localGAUserInfo.sort_id = Integer.valueOf(this.mSortID);
      localGAUserInfo.title = this.mGaTable;
      localGAUserInfo.index = Integer.valueOf(paramInt);
      paramView.setGAString("best_list", localGAUserInfo);
      paramView.setOnClickListener(new View.OnClickListener(paramViewGroup)
      {
        public void onClick(View paramView)
        {
          DishDetailInfoListAgent.this.startActivity("dianping://shopinfo?shopid=" + this.val$shop.getInt("ID"));
        }
      });
      paramViewGroup = (TextView)paramView.findViewById(R.id.paiming);
      paramViewGroup.setText(Integer.toString(paramInt + 1));
      paramViewGroup.setVisibility(0);
      return paramView;
    }
  }

  static class TableItem
  {
    int listCount;
    String subtitle;
    String title;
  }

  class TuanListAdapter extends BasicAdapter
  {
    DPObject[] dataList;
    private ArrayList<Object> mItemData = new ArrayList();
    String mMoreUrl;

    public TuanListAdapter(DPObject[] paramString, String arg3)
    {
      this.dataList = paramString;
      CharSequence localCharSequence;
      this.mMoreUrl = localCharSequence;
      if ((paramString != null) && (paramString.length != 0))
      {
        this.mItemData.addAll(Arrays.asList(paramString));
        if (!android.text.TextUtils.isEmpty(localCharSequence))
          this.mItemData.add(LAST_EXTRA);
      }
    }

    public int getCount()
    {
      return this.mItemData.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.mItemData.size())
        return this.mItemData.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = paramView;
      Object localObject2 = getItem(paramInt);
      if (localObject2 != LAST_EXTRA)
      {
        localObject2 = (DPObject)getItem(paramInt);
        if (localObject1 != null)
        {
          paramView = (View)localObject1;
          if ((localObject1 instanceof NovaFrameLayout));
        }
        else
        {
          paramView = DishDetailInfoListAgent.this.res.inflate(DishDetailInfoListAgent.this.getContext(), R.layout.deal_list_item, paramViewGroup, false);
        }
        paramView.setTag(localObject2);
        paramViewGroup = (DealListItem)paramView;
        if (DishDetailInfoListAgent.this.location() != null)
        {
          paramViewGroup.setDeal((DPObject)localObject2, DishDetailInfoListAgent.this.location().latitude(), DishDetailInfoListAgent.this.location().longitude(), NovaConfigUtils.isShowImageInMobileNetwork(), 1);
          paramViewGroup.setGAString("tuan_list", "团购闪惠", paramInt);
          paramView.setClickable(true);
          paramView.setOnClickListener(new View.OnClickListener((DPObject)localObject2)
          {
            public void onClick(View paramView)
            {
              if (this.val$deal.getInt("DealType") == 5)
              {
                paramView = this.val$deal.getString("Link");
                paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode(paramView)));
                DishDetailInfoListAgent.this.startActivity(paramView);
                return;
              }
              paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
              paramView.putExtra("deal", this.val$deal);
              DishDetailInfoListAgent.this.startActivity(paramView);
            }
          });
        }
      }
      do
      {
        try
        {
          paramViewGroup = (FrameLayout)paramViewGroup.findViewById(R.id.deal_item_icon).getParent();
          localObject1 = paramViewGroup.getLayoutParams();
          ((ViewGroup.LayoutParams)localObject1).width = ViewUtils.dip2px(DishDetailInfoListAgent.this.getContext(), 102.0F);
          ((ViewGroup.LayoutParams)localObject1).height = ViewUtils.dip2px(DishDetailInfoListAgent.this.getContext(), 74.0F);
          paramViewGroup.setLayoutParams((ViewGroup.LayoutParams)localObject1);
          return paramView;
          paramViewGroup.setDeal((DPObject)localObject2, 0.0D, 0.0D, NovaConfigUtils.isShowImageInMobileNetwork(), 1);
        }
        catch (Exception paramViewGroup)
        {
          Log.e(paramViewGroup.toString());
          return paramView;
        }
        paramView = (View)localObject1;
      }
      while (localObject2 != LAST_EXTRA);
      paramView = LayoutInflater.from(DishDetailInfoListAgent.this.getContext()).inflate(R.layout.dish_detail_more_deal, paramViewGroup, false);
      paramView.setTag(this.mMoreUrl);
      paramView.setClickable(true);
      ((NovaLinearLayout)paramView).setGAString("tuan_more", "点击团购更多");
      paramView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(DishDetailInfoListAgent.TuanListAdapter.this.mMoreUrl));
          DishDetailInfoListAgent.this.startActivity(paramView);
        }
      });
      return (View)(View)paramView;
    }

    public void setDataList(DPObject[] paramArrayOfDPObject)
    {
      this.dataList = paramArrayOfDPObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.dish.DishDetailInfoListAgent
 * JD-Core Version:    0.6.0
 */