package com.dianping.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.model.GPSCoordinate;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.LinkedList;

public class ShopAndDealListItem extends LinearLayout
{
  public static final LinkedList<Integer> records = new LinkedList();
  protected int MAX_DEAL_COUNT = 1;
  protected Adapter adapter;
  protected TableView dealTable;
  protected final TableView.OnItemClickListener defaultDealClickListener = new TableView.OnItemClickListener()
  {
    public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
    {
      paramTableView = ShopAndDealListItem.this.adapter.getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramTableView, "Deal"))
      {
        paramTableView = (DPObject)paramTableView;
        if (ShopAndDealListItem.this.onDealItemClickListener != null)
          ShopAndDealListItem.this.onDealItemClickListener.onDealItemClick(ShopAndDealListItem.this.dpShop, paramTableView, ShopAndDealListItem.this.shopPosition, paramInt);
        return;
      }
      ShopAndDealListItem.this.setRecord();
      ShopAndDealListItem.this.adapter.notifyDataSetChanged();
      ((DPActivity)ShopAndDealListItem.this.getContext()).statisticsEvent("tuan5", ShopAndDealListItem.this.expandGaAction, ShopAndDealListItem.this.shopPosition + "", 0);
    }
  };
  protected final View.OnClickListener defaultShopClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (ShopAndDealListItem.this.onShopItemClickListener != null)
        ShopAndDealListItem.this.onShopItemClickListener.onShopItemClick(ShopAndDealListItem.this.dpShop, ShopAndDealListItem.this.shopPosition);
    }
  };
  protected DPObject dpShop;
  protected String expandGaAction;
  protected boolean isShowImage;
  protected double lat;
  protected double lng;
  protected OnDealItemClickListener onDealItemClickListener;
  protected OnShopItemClickListener onShopItemClickListener;
  protected TextView shopDetailName;
  protected TextView shopDistanceView;
  protected View shopInfoView;
  protected View shopMoreInfoView;
  protected TextView shopNameView;
  protected int shopPosition;
  protected ShopPower shopPower;

  public ShopAndDealListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public ShopAndDealListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected String calculateDistance(double paramDouble1, double paramDouble2)
  {
    double d = ConfigHelper.configDistanceFactor;
    if (d <= 0.0D);
    do
    {
      do
        return null;
      while ((paramDouble1 == 0.0D) || (paramDouble2 == 0.0D) || (this.dpShop.getDouble("Latitude") == 0.0D) || (this.dpShop.getDouble("Longitude") == 0.0D));
      paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(this.dpShop.getDouble("Latitude"), this.dpShop.getDouble("Longitude"))) * d;
    }
    while ((Double.isNaN(paramDouble1)) || (paramDouble1 <= 0.0D));
    return getDistanceText((int)Math.round(paramDouble1 / 10.0D) * 10);
  }

  protected boolean displayAllDeal()
  {
    return records.contains(Integer.valueOf(this.dpShop.getInt("ID")));
  }

  protected String getDistanceText(long paramLong)
  {
    if ((this.dpShop.getDouble("Latitude") == 0.0D) && (this.dpShop.getDouble("Longitude") == 0.0D))
      return "";
    String str;
    if (paramLong <= 100L)
      str = "<100m";
    while (true)
    {
      return str;
      if (paramLong < 1000L)
      {
        str = paramLong + "m";
        continue;
      }
      if (paramLong < 10000L)
      {
        paramLong /= 100L;
        str = paramLong / 10L + "." + paramLong % 10L + "km";
        continue;
      }
      if (paramLong < 100000L)
      {
        str = paramLong / 1000L + "km";
        continue;
      }
      str = "";
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shopInfoView = findViewById(R.id.shop_info);
    this.shopMoreInfoView = findViewById(R.id.shop_more_info);
    this.shopNameView = ((TextView)findViewById(R.id.shop_name));
    this.shopDistanceView = ((TextView)findViewById(R.id.shop_distance));
    this.shopPower = ((ShopPower)findViewById(R.id.shop_power));
    this.shopDetailName = ((TextView)findViewById(R.id.shop_detail_name));
    this.dealTable = ((TableView)findViewById(R.id.deal_table));
    this.shopInfoView.setOnClickListener(this.defaultShopClickListener);
    this.shopMoreInfoView.setOnClickListener(this.defaultShopClickListener);
    this.dealTable.setOnItemClickListener(this.defaultDealClickListener);
  }

  public void setMaxDealCount(int paramInt)
  {
    this.MAX_DEAL_COUNT = paramInt;
  }

  public void setOnDealItemClickListener(OnDealItemClickListener paramOnDealItemClickListener)
  {
    this.onDealItemClickListener = paramOnDealItemClickListener;
  }

  public void setOnShopItemClickListener(OnShopItemClickListener paramOnShopItemClickListener)
  {
    this.onShopItemClickListener = paramOnShopItemClickListener;
  }

  protected void setRecord()
  {
    Integer localInteger = Integer.valueOf(this.dpShop.getInt("ID"));
    if (!records.remove(localInteger))
    {
      if (records.size() > 30)
        records.removeFirst();
      records.addLast(localInteger);
    }
  }

  public void setShopAggDealGroup(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean, int paramInt, String paramString)
  {
    this.dpShop = paramDPObject.getObject("Shop");
    this.lat = paramDouble1;
    this.lng = paramDouble2;
    this.isShowImage = paramBoolean;
    this.shopPosition = paramInt;
    this.expandGaAction = paramString;
    String str1 = this.dpShop.getString("Name");
    String str2 = this.dpShop.getString("BranchName");
    paramString = str1;
    if (!TextUtils.isEmpty(str2))
    {
      paramString = " (" + str2 + ")";
      paramString = str1 + paramString;
    }
    this.shopNameView.setText(paramString);
    paramString = calculateDistance(paramDouble1, paramDouble2);
    this.shopDistanceView.setText(paramString);
    this.shopPower.setPower(this.dpShop.getInt("ShopPower"));
    paramString = this.dpShop.getString("PriceText");
    str1 = this.dpShop.getString("RegionName");
    str2 = this.dpShop.getString("CategoryName");
    StringBuilder localStringBuilder = new StringBuilder();
    if (!TextUtils.isEmpty(paramString))
      localStringBuilder.append("  ").append(paramString);
    if (!TextUtils.isEmpty(str1))
      localStringBuilder.append("  ").append(str1);
    if (!TextUtils.isEmpty(str2))
      localStringBuilder.append("  ").append(str2);
    if (localStringBuilder.length() > 0)
      this.shopDetailName.setText(localStringBuilder.substring(2));
    if (this.adapter == null)
    {
      this.adapter = new Adapter();
      this.dealTable.setAdapter(this.adapter);
    }
    this.adapter.setData(paramDPObject.getArray("Deals"));
  }

  class Adapter extends BasicAdapter
  {
    protected DPObject[] deals;

    Adapter()
    {
    }

    public int getCount()
    {
      if (!DPObjectUtils.isArrayEmpty(this.deals))
      {
        int j = this.deals.length;
        int i = j;
        if (!ShopAndDealListItem.this.displayAllDeal())
        {
          i = j;
          if (j > ShopAndDealListItem.this.MAX_DEAL_COUNT)
            i = ShopAndDealListItem.this.MAX_DEAL_COUNT + 1;
        }
        return i;
      }
      return 0;
    }

    public Object getItem(int paramInt)
    {
      int i = this.deals.length;
      if ((!ShopAndDealListItem.this.displayAllDeal()) && (i > ShopAndDealListItem.this.MAX_DEAL_COUNT) && (paramInt == getCount() - 1))
        return LOADING;
      return this.deals[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramView, "Deal"))
      {
        paramView = (DPObject)paramView;
        paramViewGroup = (AggDealListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.deal_list_item_agg, paramViewGroup, false);
        paramViewGroup.setDeal(paramView, ShopAndDealListItem.this.lat, ShopAndDealListItem.this.lng, ShopAndDealListItem.this.isShowImage);
        paramViewGroup.setClickable(true);
        return paramViewGroup;
      }
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.tuan_shop_display_more, paramViewGroup, false);
      paramView.setClickable(true);
      ((TextView)paramView.findViewById(R.id.display_deal_count)).setText("更多" + (this.deals.length - ShopAndDealListItem.this.MAX_DEAL_COUNT) + "个团购");
      return paramView;
    }

    public void setData(DPObject[] paramArrayOfDPObject)
    {
      this.deals = paramArrayOfDPObject;
      notifyDataSetChanged();
    }
  }

  public static abstract interface OnDealItemClickListener
  {
    public abstract void onDealItemClick(DPObject paramDPObject1, DPObject paramDPObject2, int paramInt1, int paramInt2);
  }

  public static abstract interface OnShopItemClickListener
  {
    public abstract void onShopItemClick(DPObject paramDPObject, int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ShopAndDealListItem
 * JD-Core Version:    0.6.0
 */