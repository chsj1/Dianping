package com.dianping.takeaway.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.List;

public class TakeawaySuggestAddressAdapter extends BasicAdapter
{
  private Context context;
  private List<TakeawayAddress> suggestList;

  public TakeawaySuggestAddressAdapter(Context paramContext, List<TakeawayAddress> paramList)
  {
    this.suggestList = paramList;
    this.context = paramContext;
  }

  public int getCount()
  {
    return this.suggestList.size();
  }

  public Object getItem(int paramInt)
  {
    return this.suggestList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramViewGroup = paramView;
    if (paramView == null)
      paramViewGroup = LayoutInflater.from(this.context).inflate(R.layout.takeaway_suggest_address_item, null, true);
    paramView = (TakeawayAddress)getItem(paramInt);
    ViewUtils.setVisibilityAndContent((TextView)(TextView)paramViewGroup.findViewById(R.id.addressAbstract), paramView.address);
    ViewUtils.setVisibilityAndContent((TextView)(TextView)paramViewGroup.findViewById(R.id.addressDetail), paramView.addressDetail);
    return paramViewGroup;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawaySuggestAddressAdapter
 * JD-Core Version:    0.6.0
 */