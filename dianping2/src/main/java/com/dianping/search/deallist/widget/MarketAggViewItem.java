package com.dianping.search.deallist.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.ViewItemLocation;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.search.widget.ShopViewItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.LinkedList;

public class MarketAggViewItem extends NovaLinearLayout
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
      paramTableView = MarketAggViewItem.this.adapter.getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramTableView, "ViewItem"))
      {
        paramTableView = (DPObject)paramTableView;
        if (MarketAggViewItem.this.onAggItemClickListener != null)
          MarketAggViewItem.this.onAggItemClickListener.onClick(paramTableView, MarketAggViewItem.this.listPosition, paramInt);
        return;
      }
      MarketAggViewItem.this.setRecord();
      MarketAggViewItem.this.adapter.notifyDataSetChanged();
    }
  };
  protected String expandGaAction;
  protected boolean isShowImage;
  protected double lat;
  protected int listPosition = 0;
  protected double lng;
  protected View mainView;
  protected String moreText;
  protected OnAggItemClickListener onAggItemClickListener;
  protected View rootView;
  protected int showCount;

  public MarketAggViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public MarketAggViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
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
    this.aggItemTable.setOnItemClickListener(this.defaultDealClickListener);
  }

  public void setData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    this.lat = paramDouble1;
    this.lng = paramDouble2;
    this.isShowImage = paramBoolean;
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewAggItem"))
    {
      this.aggItems = paramDPObject.getArray("AggItems");
      this.moreText = paramDPObject.getString("MoreText");
      this.showCount = paramDPObject.getInt("ShowCount");
    }
    if (this.adapter == null)
    {
      this.adapter = new Adapter();
      this.aggItemTable.setAdapter(this.adapter);
    }
    this.adapter.setData(this.aggItems);
    if (this.showCount > 0)
      setMaxDealCount(this.showCount);
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
      }
      while (!(paramView instanceof ShopViewItem));
      addShopViewItemInfo((ShopViewItem)paramView, paramDPObject.getObject("Shop"), paramInt, str);
    }

    protected void addShopViewItemInfo(ShopViewItem paramShopViewItem, DPObject paramDPObject, int paramInt, String paramString)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "Shop"));
      do
        return;
      while (paramShopViewItem == null);
      paramShopViewItem.gaUserInfo.query_id = paramString;
      paramShopViewItem.gaUserInfo.index = Integer.valueOf(paramInt);
      paramShopViewItem.gaUserInfo.shop_id = Integer.valueOf(paramDPObject.getInt("ID"));
      paramShopViewItem.setGAString("mall");
      GAHelper.instance().statisticsEvent(paramShopViewItem, "view");
    }

    public int getCount()
    {
      if (!DPObjectUtils.isArrayEmpty(this.data))
      {
        int j = this.data.length;
        int i = j;
        if (!MarketAggViewItem.this.displayAllDeal())
        {
          i = j;
          if (j > MarketAggViewItem.this.MAX_DEAL_COUNT)
            i = MarketAggViewItem.this.MAX_DEAL_COUNT + 1;
        }
        return i;
      }
      return 0;
    }

    public Object getItem(int paramInt)
    {
      int i = this.data.length;
      if ((!MarketAggViewItem.this.displayAllDeal()) && (i > MarketAggViewItem.this.MAX_DEAL_COUNT) && (paramInt == getCount() - 1))
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
        paramView = (DPObject)paramView;
        paramViewGroup = ViewItemFactory.getView(MarketAggViewItem.this.getContext(), paramView, MarketAggViewItem.this.isShowImage, MarketAggViewItem.this.lat, MarketAggViewItem.this.lng, ViewItemLocation.TUAN_DEAL_LIST_MALL_AGG);
        if (paramViewGroup == null)
          return new View(MarketAggViewItem.this.getContext());
        addAggItemInfo(paramViewGroup, paramView, paramInt);
        return paramViewGroup;
      }
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.tuan_shop_display_more, paramViewGroup, false);
      paramView.setClickable(true);
      if (!TextUtils.isEmpty(MarketAggViewItem.this.moreText))
        ((TextView)paramView.findViewById(R.id.display_deal_count)).setText(String.format(MarketAggViewItem.this.moreText, new Object[] { Integer.valueOf(this.data.length - MarketAggViewItem.this.MAX_DEAL_COUNT) }));
      return paramView;
    }

    public void setData(DPObject[] paramArrayOfDPObject)
    {
      this.data = paramArrayOfDPObject;
      notifyDataSetChanged();
    }
  }

  public static abstract interface OnAggItemClickListener
  {
    public abstract void onClick(DPObject paramDPObject, int paramInt1, int paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.MarketAggViewItem
 * JD-Core Version:    0.6.0
 */