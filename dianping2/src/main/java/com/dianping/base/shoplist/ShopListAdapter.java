package com.dianping.base.shoplist;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.model.ShopListDataSource;
import com.dianping.base.shoplist.util.OTAPriceLoad;
import com.dianping.base.shoplist.widget.HotelShopListItem;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;

public class ShopListAdapter extends BasicAdapter
{
  protected String errorMsg;
  protected boolean isEnd;
  protected boolean isHotelType;
  protected boolean isPullToRefresh;
  protected boolean isRank;
  protected View lastExtraView;
  protected BaseShopListDataSource mDataSource;
  protected double offsetLatitude;
  protected double offsetLongitude;
  protected ShopListReloadHandler reloadHandler;
  protected ArrayList<DPObject> shops = new ArrayList();
  protected boolean shouldShowImage;
  protected boolean showDistance;
  protected int status = 1;

  public ShopListAdapter(ShopListReloadHandler paramShopListReloadHandler)
  {
    this.reloadHandler = paramShopListReloadHandler;
  }

  public int getCount()
  {
    switch (this.status)
    {
    case 2:
    default:
      if (!this.isEnd)
        break label99;
      if (this.lastExtraView == null)
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
    label99: return this.shops.size() + 1;
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
      return BasicAdapter.LOADING;
    case 3:
      if (paramInt < this.shops.size())
        return this.shops.get(paramInt);
      return BasicAdapter.ERROR;
    }
    if (this.shops.size() != 0)
      return BasicAdapter.LAST_EXTRA;
    return BasicAdapter.EMPTY;
    label125: if (paramInt < this.shops.size())
      return this.shops.get(paramInt);
    return BasicAdapter.LOADING;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  protected View getShopEmptyView(ViewGroup paramViewGroup, View paramView)
  {
    if (paramView == null)
      paramView = null;
    while (true)
    {
      View localView = paramView;
      if (paramView == null)
      {
        localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.empty_btn_item, paramViewGroup, false);
        localView.setTag(BasicAdapter.EMPTY);
        paramView = (TextView)localView.findViewById(R.id.text);
        paramView.setText("暂无此类商户哦");
        paramView.setCompoundDrawablesWithIntrinsicBounds(paramViewGroup.getContext().getResources().getDrawable(R.drawable.empty_page_search), null, null, null);
        paramViewGroup = (Button)localView.findViewById(R.id.add_btn);
        paramViewGroup.setText("添加商户");
        paramViewGroup.setOnClickListener(new ShopListAdapter.2(this));
      }
      return localView;
      if (paramView.getTag() == BasicAdapter.EMPTY)
        continue;
      paramView = null;
    }
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    DPObject localDPObject;
    if ((localObject instanceof DPObject))
    {
      localDPObject = (DPObject)localObject;
      if (this.isHotelType)
        if ((paramView instanceof HotelShopListItem))
        {
          localObject = (HotelShopListItem)paramView;
          paramView = (View)localObject;
          if (localObject == null)
            paramView = (HotelShopListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.hotel_shop_item, paramViewGroup, false);
          if (!this.isRank)
            break label258;
          paramInt += 1;
          label80: double d1 = this.offsetLatitude;
          double d2 = this.offsetLongitude;
          boolean bool = this.shouldShowImage;
          if (this.mDataSource != null)
            break label263;
          localObject = null;
          label108: paramView.setShop(localDPObject, paramInt, d1, d2, bool, (OTAPriceLoad)localObject);
          if ((!localDPObject.getBoolean("IsAdShop")) && (!this.showDistance) && (TextUtils.isEmpty(localDPObject.getString("placeAddress"))))
            paramView.showDistanceText(false);
          localObject = paramView;
          if ((paramViewGroup.getContext() instanceof DPActivity))
          {
            localObject = ((DPActivity)paramViewGroup.getContext()).getCloneUserInfo();
            ((GAUserInfo)localObject).shop_id = Integer.valueOf(localDPObject.getInt("ID"));
            ((GAUserInfo)localObject).index = Integer.valueOf(localDPObject.getInt("ListPosition"));
            ((GAUserInfo)localObject).query_id = localDPObject.getString("ShopQueryId");
            if (localDPObject.getInt("ViewType") != 1)
              break label275;
            paramViewGroup = "piece";
            label239: paramView.setGAString(paramViewGroup, (GAUserInfo)localObject);
            localObject = paramView;
          }
        }
    }
    label258: label263: label275: label295: label473: label480: 
    do
    {
      do
      {
        return localObject;
        localObject = null;
        break;
        paramInt = -1;
        break label80;
        localObject = this.mDataSource.hotelPricesLoad;
        break label108;
        paramViewGroup = "item";
        break label239;
        if ((paramView instanceof ShopListItem))
        {
          localObject = (ShopListItem)paramView;
          paramView = (View)localObject;
          if (localObject == null)
            paramView = (ShopListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_item, paramViewGroup, false);
          if ((paramViewGroup.getContext() instanceof DPActivity))
          {
            localObject = ((DPActivity)paramViewGroup.getContext()).getCloneUserInfo();
            ((GAUserInfo)localObject).shop_id = Integer.valueOf(localDPObject.getInt("ID"));
            ((GAUserInfo)localObject).index = Integer.valueOf(localDPObject.getInt("ListPosition"));
            ((GAUserInfo)localObject).query_id = localDPObject.getString("ShopQueryId");
            if (localDPObject.getInt("ViewType") != 1)
              break label473;
            paramViewGroup = "piece";
            paramView.setGAString(paramViewGroup, (GAUserInfo)localObject);
          }
          if (!this.isRank)
            break label480;
          paramInt += 1;
        }
        while (true)
        {
          paramView.setShop(localDPObject, paramInt, this.offsetLatitude, this.offsetLongitude, this.shouldShowImage);
          if (this.showDistance)
          {
            localObject = paramView;
            if (TextUtils.isEmpty(localDPObject.getString("placeAddress")))
              break;
          }
          paramView.showDistanceText(false);
          return paramView;
          localObject = null;
          break label295;
          paramViewGroup = "item";
          break label400;
          paramInt = -1;
        }
        if (localObject == BasicAdapter.LOADING)
        {
          if ((this.mDataSource == null) || (this.mDataSource.nextStartIndex() != 0))
            this.reloadHandler.reload(false);
          return getLoadingView(paramViewGroup, paramView);
        }
        if (localObject == BasicAdapter.LAST_EXTRA)
          return this.lastExtraView;
        if (localObject != BasicAdapter.EMPTY)
          break label605;
        paramView = getShopEmptyView(paramViewGroup, paramView);
        localObject = paramView;
      }
      while (this.mDataSource == null);
      localObject = paramView;
    }
    while (this.mDataSource.targetType != 2);
    label400: paramView.findViewById(R.id.text).setVisibility(8);
    paramView.findViewById(R.id.add_btn).setVisibility(8);
    return paramView;
    label605: return (View)getFailedView(this.errorMsg, new ShopListAdapter.1(this), paramViewGroup, paramView);
  }

  public void setIsPullToRefresh(boolean paramBoolean)
  {
    this.isPullToRefresh = paramBoolean;
  }

  public void setShopList(BaseShopListDataSource paramBaseShopListDataSource)
  {
    if (paramBaseShopListDataSource == null)
      return;
    this.mDataSource = paramBaseShopListDataSource;
    this.status = paramBaseShopListDataSource.status();
    this.isRank = false;
    this.isEnd = paramBaseShopListDataSource.isEnd();
    this.lastExtraView = paramBaseShopListDataSource.lastExtraView();
    this.offsetLatitude = paramBaseShopListDataSource.offsetLatitude();
    this.offsetLongitude = paramBaseShopListDataSource.offsetLongitude();
    this.showDistance = paramBaseShopListDataSource.showDistance();
    this.errorMsg = paramBaseShopListDataSource.errorMsg();
    this.isHotelType = paramBaseShopListDataSource.hasSearchDate();
    this.shops.clear();
    this.shops.addAll(paramBaseShopListDataSource.shops());
    notifyDataSetChanged();
  }

  public void setShopList(ShopListDataSource paramShopListDataSource)
  {
    if (paramShopListDataSource == null)
      return;
    this.status = paramShopListDataSource.status();
    this.shops.clear();
    this.shops.addAll(paramShopListDataSource.shops());
    this.isRank = paramShopListDataSource.isRank();
    this.isEnd = paramShopListDataSource.isEnd();
    this.lastExtraView = paramShopListDataSource.lastExtraView();
    this.offsetLatitude = paramShopListDataSource.offsetLatitude();
    this.offsetLongitude = paramShopListDataSource.offsetLongitude();
    this.showDistance = paramShopListDataSource.showDistance();
    this.errorMsg = paramShopListDataSource.errorMsg();
    this.isHotelType = paramShopListDataSource.hasSearchDate();
    notifyDataSetChanged();
  }

  public void setShouldShowImage(boolean paramBoolean)
  {
    this.shouldShowImage = paramBoolean;
  }

  public void setShowDistance(boolean paramBoolean)
  {
    this.showDistance = paramBoolean;
  }

  public static abstract interface ShopListReloadHandler
  {
    public abstract void reload(boolean paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.ShopListAdapter
 * JD-Core Version:    0.6.0
 */