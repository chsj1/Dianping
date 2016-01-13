package com.dianping.search.view;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class ShopFilterList extends LinearLayout
{
  Adapter adapter;
  DPObject currentFilter;
  DPObject[] filters;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what != 1);
      while (true)
      {
        return;
        if ((ShopFilterList.this.adapter == null) || (ShopFilterList.this.adapter.isEmpty()))
        {
          ShopFilterList.this.removeAllViews();
          return;
        }
        ShopFilterList.this.removeAllViews();
        int i = 0;
        while (i < ShopFilterList.this.adapter.getCount())
        {
          paramMessage = ShopFilterList.this.adapter.getView(i, null, ShopFilterList.this);
          ShopFilterList.this.addView(paramMessage);
          i += 1;
        }
      }
    }
  };
  private final DataSetObserver observer = new DataSetObserver()
  {
    public void onChanged()
    {
      ShopFilterList.this.handler.removeMessages(1);
      ShopFilterList.this.handler.sendEmptyMessageDelayed(1, 100L);
    }

    public void onInvalidated()
    {
      onChanged();
    }
  };
  private View selectView;

  public ShopFilterList(Context paramContext)
  {
    this(paramContext, null);
  }

  public ShopFilterList(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    setAdapter(new Adapter());
  }

  public DPObject getCurrentFilter()
  {
    return this.currentFilter;
  }

  public void setAdapter(Adapter paramAdapter)
  {
    if (this.adapter != null)
      this.adapter.unregisterDataSetObserver(this.observer);
    this.adapter = paramAdapter;
    if (this.adapter != null)
      this.adapter.registerDataSetObserver(this.observer);
    removeAllViews();
    this.observer.onChanged();
  }

  public void setFilter(DPObject[] paramArrayOfDPObject, DPObject paramDPObject)
  {
    this.filters = paramArrayOfDPObject;
    this.currentFilter = paramDPObject;
    this.adapter.notifyDataSetChanged();
  }

  public void setNormalState(View paramView)
  {
    if (paramView == null)
      return;
    ((TextView)paramView.findViewById(16908308)).setTextColor(getResources().getColorStateList(R.drawable.filter_color));
    paramView.findViewById(R.id.line).setBackgroundDrawable(getResources().getDrawable(R.drawable.filter_drawable));
    paramView.findViewById(R.id.icon_select).setVisibility(8);
  }

  public void setSelectState(View paramView)
  {
    ((TextView)paramView.findViewById(16908308)).setTextColor(getResources().getColor(R.color.light_orange));
    paramView.findViewById(R.id.line).setBackgroundColor(getResources().getColor(R.color.light_orange));
    paramView.findViewById(R.id.icon_select).setVisibility(0);
    this.selectView = paramView;
  }

  class Adapter extends BaseAdapter
  {
    Adapter()
    {
    }

    public int getCount()
    {
      if (ShopFilterList.this.filters == null)
        return 0;
      return ShopFilterList.this.filters.length;
    }

    public DPObject getItem(int paramInt)
    {
      return ShopFilterList.this.filters[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItem(paramInt);
      paramViewGroup = LayoutInflater.from(ShopFilterList.this.getContext()).inflate(R.layout.filter_item_icon, paramViewGroup, false);
      String str1 = paramView.getString("Name");
      ((TextView)paramViewGroup.findViewById(16908308)).setText(str1);
      String[] arrayOfString = paramView.getStringArray("Imgs");
      if ((arrayOfString != null) && (arrayOfString.length > 0))
      {
        LinearLayout localLinearLayout = (LinearLayout)paramViewGroup.findViewById(R.id.layout);
        int j = arrayOfString.length;
        int i = 0;
        if (i < j)
        {
          String str2 = arrayOfString[i];
          NetworkThumbView localNetworkThumbView = (NetworkThumbView)LayoutInflater.from(ShopFilterList.this.getContext()).inflate(R.layout.filter_icon, localLinearLayout, false);
          int k = View.MeasureSpec.makeMeasureSpec(0, 0);
          int m = View.MeasureSpec.makeMeasureSpec(0, 0);
          Object localObject = (TextView)paramViewGroup.findViewById(16908308);
          ((TextView)localObject).measure(k, m);
          k = ((TextView)localObject).getMeasuredHeight();
          localObject = localNetworkThumbView.getLayoutParams();
          ((ViewGroup.LayoutParams)localObject).height = (k - ViewUtils.dip2px(ShopFilterList.this.getContext(), 4.0F));
          ((ViewGroup.LayoutParams)localObject).width = ((ViewGroup.LayoutParams)localObject).height;
          if (paramView.getInt("FilterId") == 13)
          {
            ((ViewGroup.LayoutParams)localObject).height = (int)(((ViewGroup.LayoutParams)localObject).height * 52.0D / 40.0D);
            ((ViewGroup.LayoutParams)localObject).width = ((ViewGroup.LayoutParams)localObject).height;
          }
          localObject = ShopListUtils.getIconResIdFromUrl(str2);
          if (localObject == null)
            localNetworkThumbView.setImage(str2);
          while (true)
          {
            localLinearLayout.addView(localNetworkThumbView);
            i += 1;
            break;
            localNetworkThumbView.setImageDrawable(ShopFilterList.this.getResources().getDrawable(((Integer)localObject).intValue()));
          }
        }
      }
      if ((ShopFilterList.this.currentFilter != null) && (paramView.getInt("FilterId") == ShopFilterList.this.currentFilter.getInt("FilterId")))
        ShopFilterList.this.setSelectState(paramViewGroup);
      while (true)
      {
        paramViewGroup.setOnClickListener(new View.OnClickListener(paramView)
        {
          public void onClick(View paramView)
          {
            if ((this.val$item == null) || (ShopFilterList.this.selectView == paramView))
              return;
            ShopFilterList.this.currentFilter = this.val$item;
            ShopFilterList.this.setNormalState(ShopFilterList.this.selectView);
            ShopFilterList.this.setSelectState(paramView);
          }
        });
        ((NovaRelativeLayout)paramViewGroup).setGAString("filter");
        ((NovaRelativeLayout)paramViewGroup).gaUserInfo.title = str1;
        ((NovaRelativeLayout)paramViewGroup).gaUserInfo.index = Integer.valueOf(paramInt);
        return paramViewGroup;
        ShopFilterList.this.setNormalState(paramViewGroup);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.view.ShopFilterList
 * JD-Core Version:    0.6.0
 */