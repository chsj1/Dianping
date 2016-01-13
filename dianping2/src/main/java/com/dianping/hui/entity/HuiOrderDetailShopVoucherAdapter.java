package com.dianping.hui.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HuiOrderDetailShopVoucherAdapter extends BaseAdapter
{
  private static final int PART_COUNT = 3;
  Context context;
  String[] shopVouchers;
  boolean showAll = false;

  public HuiOrderDetailShopVoucherAdapter(Context paramContext, String[] paramArrayOfString)
  {
    this.context = paramContext;
    if (paramArrayOfString == null)
    {
      this.shopVouchers = new String[0];
      return;
    }
    this.shopVouchers = paramArrayOfString;
  }

  public int getCount()
  {
    if (this.shopVouchers.length <= 3)
      return this.shopVouchers.length;
    if (this.showAll)
      return this.shopVouchers.length;
    return 4;
  }

  public Object getItem(int paramInt)
  {
    return this.shopVouchers[paramInt];
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null)
    {
      paramView = LayoutInflater.from(this.context).inflate(R.layout.hui_order_detail_shop_voucher, paramViewGroup, false);
      paramViewGroup = new HuiOrderDetailShopVoucherAdapter.ViewHolder(this);
      paramViewGroup.tvVoucherCode = ((TextView)paramView.findViewById(R.id.tv_voucher_code));
      paramViewGroup.tvShowAll = ((TextView)paramView.findViewById(R.id.tv_show_all));
      paramView.setTag(paramViewGroup);
    }
    while (true)
    {
      paramViewGroup.tvVoucherCode.setText((String)getItem(paramInt));
      paramViewGroup.tvShowAll.setOnClickListener(new HuiOrderDetailShopVoucherAdapter.1(this));
      if ((this.showAll) || (paramInt != 3))
        break;
      paramViewGroup.tvVoucherCode.setVisibility(8);
      paramViewGroup.tvShowAll.setVisibility(0);
      return paramView;
      paramViewGroup = (HuiOrderDetailShopVoucherAdapter.ViewHolder)paramView.getTag();
    }
    paramViewGroup.tvVoucherCode.setVisibility(0);
    paramViewGroup.tvShowAll.setVisibility(8);
    return paramView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiOrderDetailShopVoucherAdapter
 * JD-Core Version:    0.6.0
 */