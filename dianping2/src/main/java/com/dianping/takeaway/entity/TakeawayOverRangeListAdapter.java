package com.dianping.takeaway.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayOverRangeListAdapter extends TakeawayListAdapter
{
  public TakeawayOverRangeListAdapter(Context paramContext, ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler)
  {
    super(paramContext, paramShopListReloadHandler);
  }

  protected View getShopEmptyView(ViewGroup paramViewGroup, View paramView)
  {
    if ((paramView != null) && (paramView.getTag() == EMPTY))
      paramViewGroup = paramView;
    do
    {
      return paramViewGroup;
      paramView = LayoutInflater.from(this.mContext).inflate(R.layout.takeaway_over_range_noshop_layout, paramViewGroup, false);
      paramView.setTag(EMPTY);
      paramViewGroup = paramView;
    }
    while (this.mDataSource == null);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.takeaway_over_range_error_hint), ((TakeawayOverRangeDataSource)this.mDataSource).overRangeErrorMsg);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.takeaway_over_range_error_currentaddress), ((TakeawayOverRangeDataSource)this.mDataSource).overRangeAddress);
    return paramView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayOverRangeListAdapter
 * JD-Core Version:    0.6.0
 */