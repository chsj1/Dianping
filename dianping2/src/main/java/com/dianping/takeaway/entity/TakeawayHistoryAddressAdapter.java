package com.dianping.takeaway.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.List;

public class TakeawayHistoryAddressAdapter extends BaseAdapter
{
  private Context context;
  private List<TakeawayAddress> historyList;

  public TakeawayHistoryAddressAdapter(Context paramContext, List<TakeawayAddress> paramList)
  {
    this.historyList = paramList;
    this.context = paramContext;
  }

  private void deleteAddressByIndex(int paramInt)
  {
    String str = getItem(paramInt);
    paramInt = 0;
    while (true)
    {
      if (paramInt < this.historyList.size())
      {
        if (((TakeawayAddress)this.historyList.get(paramInt)).address.equals(str))
          this.historyList.remove(paramInt);
      }
      else
      {
        notifyDataSetChanged();
        return;
      }
      paramInt += 1;
    }
  }

  public int getCount()
  {
    return this.historyList.size();
  }

  public String getItem(int paramInt)
  {
    return ((TakeawayAddress)this.historyList.get(this.historyList.size() - 1 - paramInt)).address;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramViewGroup = paramView;
    if (paramView == null)
      paramViewGroup = LayoutInflater.from(this.context).inflate(R.layout.takeaway_address_item, null, true);
    ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.historyText), getItem(paramInt));
    paramView = (ImageView)paramViewGroup.findViewById(R.id.deleteButton);
    paramView.setTag(Integer.valueOf(paramInt));
    paramView.setOnClickListener(new View.OnClickListener(paramView)
    {
      public void onClick(View paramView)
      {
        int i = ((Integer)this.val$deleteButton.getTag()).intValue();
        TakeawayHistoryAddressAdapter.this.deleteAddressByIndex(i);
      }
    });
    return paramViewGroup;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayHistoryAddressAdapter
 * JD-Core Version:    0.6.0
 */