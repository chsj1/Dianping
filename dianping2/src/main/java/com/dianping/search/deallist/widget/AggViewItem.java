package com.dianping.search.deallist.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.ViewItemLocation;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.AggDealListItem;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.LinkedList;

public class AggViewItem extends NovaLinearLayout
  implements ViewItemInterface
{
  public static final LinkedList<Integer> records = new LinkedList();
  protected int MAX_DEAL_COUNT = 1;
  protected Adapter adapter;
  protected TableView aggItemTable;
  protected DPObject[] aggItems;
  protected final TableView.OnItemClickListener defaultDealClickListener = new TableView.OnItemClickListener()
  {
    public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
    {
      paramTableView = AggViewItem.this.adapter.getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramTableView, "ViewItem"))
      {
        paramTableView = (DPObject)paramTableView;
        if (AggViewItem.this.onAggItemClickListener != null)
          AggViewItem.this.onAggItemClickListener.onClick(AggViewItem.this.mainItem, paramTableView, AggViewItem.this.listPosition, paramInt);
        return;
      }
      AggViewItem.this.setRecord();
      AggViewItem.this.adapter.notifyDataSetChanged();
    }
  };
  protected final View.OnClickListener defaultShopClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (AggViewItem.this.onMainItemClickListener != null)
        AggViewItem.this.onMainItemClickListener.onClick(AggViewItem.this.mainItem, AggViewItem.this.listPosition);
    }
  };
  protected String expandGaAction;
  protected boolean isShowImage;
  protected double lat;
  protected int listPosition = 0;
  protected double lng;
  protected DPObject mainItem;
  protected View mainView;
  protected String moreText;
  protected OnAggItemClickListener onAggItemClickListener;
  protected OnMainItemClickListener onMainItemClickListener;
  protected View rootView;
  protected int showCount;

  public AggViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public AggViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void addMainViewInfo()
  {
    if (this.mainView == null);
    DPObject localDPObject;
    do
    {
      do
        return;
      while ((!(this.mainView instanceof AggMainShopViewItem)) || (!DPObjectUtils.isDPObjectof(this.mainItem, "ViewItem")));
      localDPObject = this.mainItem.getObject("Shop");
    }
    while (!DPObjectUtils.isDPObjectof(localDPObject, "Shop"));
    AggMainShopViewItem localAggMainShopViewItem = (AggMainShopViewItem)this.mainView;
    localAggMainShopViewItem.setGAString("shop_item");
    localAggMainShopViewItem.gaUserInfo.query_id = this.mainItem.getString("QueryId");
    localAggMainShopViewItem.gaUserInfo.shop_id = Integer.valueOf(localDPObject.getInt("ID"));
    localAggMainShopViewItem.gaUserInfo.category_id = Integer.valueOf(localDPObject.getInt("CategoryID"));
    localAggMainShopViewItem.gaUserInfo.index = Integer.valueOf(0);
  }

  protected View createMainView(int paramInt)
  {
    View localView = ViewItemFactory.getView(getContext(), this.mainItem, this.isShowImage, this.lat, this.lng, ViewItemLocation.TUAN_DEAL_LIST_AGG);
    localView.setId(R.id.main_item);
    localView.setClickable(true);
    localView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.deal_list_item_bg));
    removeViewAt(paramInt);
    addView(localView, paramInt);
    return localView;
  }

  protected boolean displayAllDeal()
  {
    return records.contains(Integer.valueOf(this.listPosition));
  }

  public ViewItemType getType()
  {
    return ViewItemType.AGGRATION_ITEMS;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mainView = findViewById(R.id.main_item);
    this.aggItemTable = ((TableView)findViewById(R.id.agg_item_table));
  }

  public void setData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    this.lat = paramDouble1;
    this.lng = paramDouble2;
    this.isShowImage = paramBoolean;
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewAggItem"))
    {
      this.mainItem = paramDPObject.getObject("MainItem");
      this.aggItems = paramDPObject.getArray("AggItems");
      this.moreText = paramDPObject.getString("MoreText");
      this.showCount = paramDPObject.getInt("ShowCount");
    }
    int i = indexOfChild(this.mainView);
    if ((i < 0) || (this.mainView == null) || (!(this.mainView instanceof ViewItemInterface)))
    {
      this.mainView = createMainView(i);
      addMainViewInfo();
    }
    while (true)
    {
      if (this.adapter == null)
      {
        this.adapter = new Adapter();
        this.aggItemTable.setAdapter(this.adapter);
      }
      this.adapter.setData(this.aggItems);
      if (this.showCount > 0)
        setMaxDealCount(this.showCount);
      this.mainView.setOnClickListener(this.defaultShopClickListener);
      this.aggItemTable.setOnItemClickListener(this.defaultDealClickListener);
      return;
      ((ViewItemInterface)this.mainView).updateData(this.mainItem, paramDouble1, paramDouble2, paramBoolean);
    }
  }

  public void setExpandGaAction(String paramString)
  {
    this.expandGaAction = paramString;
  }

  public void setListPosition(int paramInt)
  {
    this.listPosition = paramInt;
  }

  public void setMaxDealCount(int paramInt)
  {
    this.MAX_DEAL_COUNT = paramInt;
  }

  public void setOnAggItemClickListener(OnAggItemClickListener paramOnAggItemClickListener)
  {
    this.onAggItemClickListener = paramOnAggItemClickListener;
  }

  public void setOnMainItemClickListener(OnMainItemClickListener paramOnMainItemClickListener)
  {
    this.onMainItemClickListener = paramOnMainItemClickListener;
  }

  protected void setRecord()
  {
    Integer localInteger = Integer.valueOf(this.listPosition);
    if (!records.remove(localInteger))
    {
      if (records.size() > 30)
        records.removeFirst();
      records.addLast(localInteger);
    }
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      setData(paramDPObject.getObject("Agg"), paramDouble1, paramDouble2, paramBoolean);
  }

  class Adapter extends BasicAdapter
  {
    protected DPObject[] data;

    Adapter()
    {
    }

    protected void addAggDealListItemInfo(AggDealListItem paramAggDealListItem, DPObject paramDPObject, int paramInt, String paramString)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "Deal"));
      do
        return;
      while (paramAggDealListItem == null);
      paramAggDealListItem.gaUserInfo.dealgroup_id = Integer.valueOf(paramDPObject.getInt("ID"));
      paramAggDealListItem.gaUserInfo.deal_id = Integer.valueOf(paramDPObject.getInt("DealID"));
      paramAggDealListItem.gaUserInfo.category_id = Integer.valueOf(paramDPObject.getInt("CategoryID"));
      paramAggDealListItem.gaUserInfo.query_id = paramString;
      paramAggDealListItem.gaUserInfo.index = Integer.valueOf(paramInt + 1);
      if (DPObjectUtils.isDPObjectof(AggViewItem.this.mainItem, "ViewItem"))
      {
        paramDPObject = AggViewItem.this.mainItem.getObject("Shop");
        if (DPObjectUtils.isDPObjectof(paramDPObject, "Shop"))
          paramAggDealListItem.gaUserInfo.shop_id = Integer.valueOf(paramDPObject.getInt("ID"));
      }
      paramAggDealListItem.gaUserInfo = setSectionIndex(paramAggDealListItem.gaUserInfo);
      paramAggDealListItem.setGAString("item");
    }

    protected void addAggHuiItemInfo(AggHuiViewItem paramAggHuiViewItem, DPObject paramDPObject, int paramInt, String paramString)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "HuiDetail"));
      do
        return;
      while (paramAggHuiViewItem == null);
      paramAggHuiViewItem.gaUserInfo.query_id = paramString;
      paramAggHuiViewItem.gaUserInfo.index = Integer.valueOf(paramInt + 1);
      paramAggHuiViewItem.gaUserInfo.shop_id = Integer.valueOf(paramDPObject.getInt("PayShopId"));
      paramAggHuiViewItem.gaUserInfo.butag = Integer.valueOf(paramDPObject.getInt("HuiId"));
      paramAggHuiViewItem.gaUserInfo = setSectionIndex(paramAggHuiViewItem.gaUserInfo);
      paramAggHuiViewItem.setGAString("shanhui_item");
      GAHelper.instance().statisticsEvent(paramAggHuiViewItem, "view");
    }

    protected void addAggItemInfo(View paramView, DPObject paramDPObject, int paramInt)
    {
      if (paramView == null);
      String str;
      do
      {
        do
          return;
        while (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"));
        str = paramDPObject.getString("QueryId");
        if ((paramView instanceof AggDealListItem))
        {
          addAggDealListItemInfo((AggDealListItem)paramView, paramDPObject.getObject("Deal"), paramInt, str);
          return;
        }
        if (!(paramView instanceof AggHuiViewItem))
          continue;
        addAggHuiItemInfo((AggHuiViewItem)paramView, paramDPObject.getObject("Hui"), paramInt, str);
        return;
      }
      while (!(paramView instanceof HotelZuesViewItem));
      addAggZuesItemInfo((HotelZuesViewItem)paramView, paramDPObject.getObject("HotelZeus"), paramInt, str);
    }

    protected void addAggZuesItemInfo(HotelZuesViewItem paramHotelZuesViewItem, DPObject paramDPObject, int paramInt, String paramString)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "SpuInfoDTO"));
      do
        return;
      while (paramHotelZuesViewItem == null);
      paramHotelZuesViewItem.gaUserInfo.query_id = paramString;
      paramHotelZuesViewItem.gaUserInfo.index = Integer.valueOf(paramInt + 1);
      if (DPObjectUtils.isDPObjectof(AggViewItem.this.mainItem, "ViewItem"))
      {
        paramString = AggViewItem.this.mainItem.getObject("Shop");
        if (DPObjectUtils.isDPObjectof(paramString, "Shop"))
          paramHotelZuesViewItem.gaUserInfo.shop_id = Integer.valueOf(paramString.getInt("ID"));
      }
      paramHotelZuesViewItem.gaUserInfo.butag = Integer.valueOf(paramDPObject.getInt("SpuId"));
      paramHotelZuesViewItem.gaUserInfo = setSectionIndex(paramHotelZuesViewItem.gaUserInfo);
      paramHotelZuesViewItem.setGAString("jiudian_item");
      GAHelper.instance().statisticsEvent(paramHotelZuesViewItem, "view");
    }

    public int getCount()
    {
      if (!DPObjectUtils.isArrayEmpty(this.data))
      {
        int j = this.data.length;
        int i = j;
        if (!AggViewItem.this.displayAllDeal())
        {
          i = j;
          if (j > AggViewItem.this.MAX_DEAL_COUNT)
            i = AggViewItem.this.MAX_DEAL_COUNT + 1;
        }
        return i;
      }
      return 0;
    }

    public Object getItem(int paramInt)
    {
      int i = this.data.length;
      if ((!AggViewItem.this.displayAllDeal()) && (i > AggViewItem.this.MAX_DEAL_COUNT) && (paramInt == getCount() - 1))
        return LOADING;
      return this.data[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramView, "ViewItem"))
      {
        paramViewGroup = (DPObject)paramView;
        paramView = null;
        if (0 == 0)
          paramView = ViewItemFactory.getView(AggViewItem.this.getContext(), paramViewGroup, AggViewItem.this.isShowImage, AggViewItem.this.lat, AggViewItem.this.lng, ViewItemLocation.TUAN_DEAL_LIST_AGG);
        if (paramView == null)
          return new View(AggViewItem.this.getContext());
        addAggItemInfo(paramView, paramViewGroup, paramInt);
        return paramView;
      }
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.tuan_shop_display_more, paramViewGroup, false);
      paramView.setClickable(true);
      if (!TextUtils.isEmpty(AggViewItem.this.moreText))
        ((TextView)paramView.findViewById(R.id.display_deal_count)).setText(String.format(AggViewItem.this.moreText, new Object[] { Integer.valueOf(this.data.length - AggViewItem.this.MAX_DEAL_COUNT) }));
      return paramView;
    }

    public void setData(DPObject[] paramArrayOfDPObject)
    {
      this.data = paramArrayOfDPObject;
      notifyDataSetChanged();
    }

    protected GAUserInfo setSectionIndex(GAUserInfo paramGAUserInfo)
    {
      paramGAUserInfo.sectionIndex = Integer.valueOf(AggViewItem.this.listPosition);
      return paramGAUserInfo;
    }
  }

  public static abstract interface OnAggItemClickListener
  {
    public abstract void onClick(DPObject paramDPObject1, DPObject paramDPObject2, int paramInt1, int paramInt2);
  }

  public static abstract interface OnMainItemClickListener
  {
    public abstract void onClick(DPObject paramDPObject, int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.AggViewItem
 * JD-Core Version:    0.6.0
 */