package com.dianping.booking.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.List;

public class BookingChannelAdapter extends BaseAdapter
{
  private int columns;
  private Context context;
  private List<DPObject> dataList;

  public BookingChannelAdapter(Context paramContext, List<DPObject> paramList, int paramInt)
  {
    this.context = paramContext;
    this.dataList = paramList;
    this.columns = paramInt;
  }

  public int getCount()
  {
    return this.dataList.size();
  }

  public Object getItem(int paramInt)
  {
    return this.dataList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = (DPObject)this.dataList.get(paramInt);
    paramView = null;
    if (this.columns == 2)
    {
      paramViewGroup = LayoutInflater.from(this.context).inflate(R.layout.booking_channel_activity_item, null);
      paramView = paramViewGroup;
      if (localObject != null)
      {
        paramView = paramViewGroup;
        if (!TextUtils.isEmpty(((DPObject)localObject).getString("IconUrl")))
        {
          ((NetworkImageView)paramViewGroup.findViewById(R.id.activity_item)).setImage(((DPObject)localObject).getString("IconUrl"));
          paramView = paramViewGroup;
        }
      }
    }
    paramViewGroup = (ImageView)paramView.findViewById(R.id.horizontal_divider);
    localObject = new LinearLayout.LayoutParams(-1, 1);
    if (paramInt >= this.columns)
    {
      if (paramInt % this.columns != 0)
        break label191;
      ((LinearLayout.LayoutParams)localObject).setMargins(ViewUtils.dip2px(this.context, 5.0F), 0, 0, 0);
    }
    while (true)
    {
      paramViewGroup.setLayoutParams((ViewGroup.LayoutParams)localObject);
      paramViewGroup = (ImageView)paramView.findViewById(R.id.vertical_divider);
      localObject = new LinearLayout.LayoutParams(1, -1);
      if (paramInt % this.columns != 0)
        break;
      paramViewGroup.setVisibility(8);
      paramViewGroup.setLayoutParams((ViewGroup.LayoutParams)localObject);
      return paramView;
      label191: if ((paramInt + 1) % this.columns != 0)
        continue;
      ((LinearLayout.LayoutParams)localObject).setMargins(0, 0, ViewUtils.dip2px(this.context, 5.0F), 0);
    }
    if ((paramInt < this.columns) && (paramInt >= this.dataList.size() - this.columns))
      ((LinearLayout.LayoutParams)localObject).setMargins(0, ViewUtils.dip2px(this.context, 5.0F), 0, ViewUtils.dip2px(this.context, 5.0F));
    while (true)
    {
      paramViewGroup.setVisibility(0);
      break;
      if (paramInt < this.columns)
      {
        ((LinearLayout.LayoutParams)localObject).setMargins(0, ViewUtils.dip2px(this.context, 5.0F), 0, 0);
        continue;
      }
      if (paramInt < this.dataList.size() - this.columns)
        continue;
      ((LinearLayout.LayoutParams)localObject).setMargins(0, 0, 0, ViewUtils.dip2px(this.context, 5.0F));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.adapter.BookingChannelAdapter
 * JD-Core Version:    0.6.0
 */