package com.dianping.hotel.shoplist.adapt;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.util.OTAPriceLoad;
import com.dianping.base.shoplist.widget.HotelShopListItem;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class HotelShopListAdapter extends ShopListAdapter
{
  private static final String SHOP_TAG_KEY = "__HotelShopListShopType";
  private static final int TYPE_AD = 2;
  private static final int TYPE_HOTEL = 0;
  private static final int TYPE_SEP = 1;
  protected double customLatitude;
  protected double customLongitude;
  protected int searchFilterType;

  public HotelShopListAdapter(ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler)
  {
    super(paramShopListReloadHandler);
  }

  private View getShopView(View paramView, ViewGroup paramViewGroup, Object paramObject, int paramInt)
  {
    boolean bool = true;
    View localView = paramView;
    if (!(paramView instanceof ShopListItem))
      localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_item, paramViewGroup, false);
    paramViewGroup = new ShopDataModel((DPObject)paramObject);
    paramObject = (ShopListItem)localView;
    label73: int i;
    if (paramViewGroup.viewType == 1)
    {
      paramObject.setGAString("piece");
      if (paramViewGroup.viewType != 1)
        break label274;
      paramView = "piece";
      paramObject.setGAString(paramView);
      paramObject.gaUserInfo.shop_id = Integer.valueOf(paramViewGroup.shopId);
      paramObject.gaUserInfo.index = Integer.valueOf(paramViewGroup.index);
      paramObject.gaUserInfo.query_id = paramViewGroup.shopQueryId;
      paramObject.gaUserInfo.title = paramViewGroup.fullName;
      if ((localView.getContext() instanceof NovaActivity))
        ((NovaActivity)localView.getContext()).addGAView(localView, paramViewGroup.index, "shoplist", true);
      if ((paramInt + 1 >= getCount()) || (!(getItem(paramInt + 1) instanceof DPObject)))
        break label280;
      i = ((DPObject)getItem(paramInt + 1)).getInt("__HotelShopListShopType");
      label202: if ((paramInt >= getCount() - 1) || (i == 1))
        break label286;
      label219: paramObject.setItemDividerLine(bool, paramViewGroup.viewType);
      if (!this.isRank)
        break label292;
      paramInt += 1;
    }
    while (true)
    {
      paramObject.setShop(paramViewGroup, paramInt, selectedLatitude(), selectedLongitude(), this.shouldShowImage, false);
      return localView;
      paramObject.setGAString("item");
      break;
      label274: paramView = "item";
      break label73;
      label280: i = 0;
      break label202;
      label286: bool = false;
      break label219;
      label292: paramInt = -1;
    }
  }

  private double selectedLatitude()
  {
    if ((this.searchFilterType == 3) || (this.searchFilterType == 4) || (this.searchFilterType == -1))
      return this.customLatitude;
    return this.offsetLatitude;
  }

  private double selectedLongitude()
  {
    if ((this.searchFilterType == 3) || (this.searchFilterType == 4) || (this.searchFilterType == -1))
      return this.customLongitude;
    return this.offsetLongitude;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject1 = getItem(paramInt);
    Object localObject2;
    int i;
    if ((localObject1 instanceof DPObject))
    {
      localObject2 = (DPObject)localObject1;
      if (this.isHotelType)
        if (localObject2 == null)
        {
          i = 0;
          switch (i)
          {
          default:
          case 0:
          case 1:
          case 2:
          }
        }
    }
    while (true)
    {
      localObject1 = paramView;
      while (true)
      {
        return localObject1;
        i = ((DPObject)localObject2).getInt("__HotelShopListShopType");
        break;
        label95: double d1;
        label139: double d2;
        boolean bool;
        if ((paramView instanceof HotelShopListItem))
        {
          localObject1 = (HotelShopListItem)paramView;
          paramView = (View)localObject1;
          if (localObject1 == null)
            paramView = (HotelShopListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.hotel_shop_item, paramViewGroup, false);
          i = this.searchFilterType;
          if (!this.isRank)
            break label297;
          paramInt += 1;
          d1 = selectedLatitude();
          d2 = selectedLongitude();
          bool = this.shouldShowImage;
          if (this.mDataSource != null)
            break label302;
        }
        label297: label302: for (localObject1 = null; ; localObject1 = this.mDataSource.hotelPricesLoad)
        {
          paramView.setShop((DPObject)localObject2, i, paramInt, d1, d2, bool, (OTAPriceLoad)localObject1);
          if ((!((DPObject)localObject2).getBoolean("IsAdShop")) && (!this.showDistance) && (TextUtils.isEmpty(((DPObject)localObject2).getString("placeAddress"))))
            paramView.showDistanceText(false);
          localObject1 = paramView;
          if (!(paramViewGroup.getContext() instanceof DPActivity))
            break;
          paramViewGroup = ((DPActivity)paramViewGroup.getContext()).getCloneUserInfo();
          paramViewGroup.shop_id = Integer.valueOf(((DPObject)localObject2).getInt("ID"));
          paramViewGroup.index = Integer.valueOf(((DPObject)localObject2).getInt("ListPosition"));
          paramViewGroup.query_id = ((DPObject)localObject2).getString("ShopQueryId");
          paramView.setGAString("item", paramViewGroup);
          return paramView;
          localObject1 = null;
          break label95;
          paramInt = -1;
          break label139;
        }
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.hotel_promotion_shop_layout, paramViewGroup, false);
        localObject1 = (TextView)paramView.findViewById(R.id.keyword_suggest_tv);
        ((TextView)localObject1).setText("你可能会喜欢");
        ((TextView)localObject1).setTextColor(paramViewGroup.getContext().getResources().getColor(R.color.light_gray));
        localObject2 = (TextView)paramView.findViewById(R.id.keyword_popularize_tv);
        ((TextView)localObject2).setText("推广");
        ((TextView)localObject2).setTextColor(paramViewGroup.getContext().getResources().getColor(R.color.text_hint_light_gray));
        paramView.setVisibility(0);
        paramView.setClickable(false);
        ((TextView)localObject1).setVisibility(0);
        ((TextView)localObject1).setClickable(false);
        ((TextView)localObject2).setVisibility(0);
        ((TextView)localObject2).setClickable(false);
        if (getItem(0) == EMPTY)
        {
          paramView.setBackgroundResource(R.color.white);
          return paramView;
        }
        paramView.setBackgroundResource(R.color.gray_background);
        return paramView;
        try
        {
          paramViewGroup = getShopView(paramView, paramViewGroup, localObject2, paramInt);
          return paramViewGroup;
          try
          {
            paramViewGroup = getShopView(paramView, paramViewGroup, localObject2, paramInt);
            return paramViewGroup;
          }
          catch (java.lang.Exception paramViewGroup)
          {
            return paramView;
          }
          if (localObject1 == LOADING)
          {
            if ((this.mDataSource == null) || (this.mDataSource.nextStartIndex() != 0))
              this.reloadHandler.reload(false);
            return getLoadingView(paramViewGroup, paramView);
          }
          if (localObject1 == LAST_EXTRA)
            return this.lastExtraView;
          if (localObject1 == EMPTY)
          {
            paramView = getShopEmptyView(paramViewGroup, paramView);
            localObject1 = paramView;
            if (this.mDataSource == null)
              continue;
            localObject1 = paramView;
            if (this.mDataSource.targetType != 2)
              continue;
            paramView.findViewById(R.id.text).setVisibility(8);
            paramView.findViewById(R.id.add_btn).setVisibility(8);
            return paramView;
          }
          return getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
          {
            public void loadRetry(View paramView)
            {
              HotelShopListAdapter.this.reloadHandler.reload(false);
            }
          }
          , paramViewGroup, paramView);
        }
        catch (java.lang.Exception paramViewGroup)
        {
        }
      }
    }
  }

  public void setShopList(BaseShopListDataSource paramBaseShopListDataSource)
  {
    super.setShopList(paramBaseShopListDataSource);
    this.searchFilterType = paramBaseShopListDataSource.getHotelFilterType();
    this.customLatitude = paramBaseShopListDataSource.customLatitude();
    this.customLongitude = paramBaseShopListDataSource.customLongitude();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.adapt.HotelShopListAdapter
 * JD-Core Version:    0.6.0
 */