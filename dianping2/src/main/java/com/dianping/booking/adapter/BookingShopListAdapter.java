package com.dianping.booking.adapter;

import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.booking.view.BookingShopListItem;
import com.dianping.util.DeviceUtils;

public class BookingShopListAdapter extends ShopListAdapter
{
  private static final Object LISTITEM = new Object();
  private NovaActivity activity;

  public BookingShopListAdapter(ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler, NovaActivity paramNovaActivity)
  {
    super(paramShopListReloadHandler);
    this.activity = paramNovaActivity;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
    {
      if (paramView != null)
      {
        paramViewGroup = paramView;
        if (paramView.getTag() == LISTITEM);
      }
      else
      {
        paramViewGroup = new BookingShopListItem(this.activity, null);
        paramViewGroup.setTag(LISTITEM);
      }
      paramView = (BookingShopListItem)paramViewGroup;
      localObject = (DPObject)localObject;
      if (this.isRank)
        paramInt += 1;
      while (true)
      {
        paramView.bindView((DPObject)localObject, paramInt, NovaConfigUtils.isShowImageInMobileNetwork(), DeviceUtils.isCurrentCity());
        return paramViewGroup;
        paramInt = -1;
      }
    }
    if (localObject == LOADING)
    {
      if ((this.mDataSource == null) || (this.mDataSource.nextStartIndex() != 0))
        this.reloadHandler.reload(false);
      return getLoadingView(paramViewGroup, paramView);
    }
    if (localObject == LAST_EXTRA)
      return this.lastExtraView;
    return (View)getFailedView(this.errorMsg, new BookingShopListAdapter.1(this), paramViewGroup, paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.adapter.BookingShopListAdapter
 * JD-Core Version:    0.6.0
 */