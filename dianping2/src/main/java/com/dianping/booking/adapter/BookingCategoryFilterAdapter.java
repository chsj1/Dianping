package com.dianping.booking.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.RangeSeekBar;
import com.dianping.booking.util.BookingShoplistDataSource;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookingCategoryFilterAdapter extends BasicAdapter
{
  private static final String INFINITE = "不限";
  private static final String ZERO = "0";
  private int barLeftField;
  private int barRightField;
  private List<BookingCategoryFilterAdapter.BookingSwitcher> bookingSwitchers;
  private Context context;
  private BookingShoplistDataSource datasource;
  private View selectView;

  public BookingCategoryFilterAdapter(Context paramContext, BookingShoplistDataSource paramBookingShoplistDataSource)
  {
    this.context = paramContext;
    this.datasource = paramBookingShoplistDataSource;
    this.bookingSwitchers = new ArrayList();
    if (this.datasource.curSwitches != null)
    {
      paramContext = this.datasource.curSwitches;
      int j = paramContext.length;
      int i = 0;
      while (i < j)
      {
        paramBookingShoplistDataSource = paramContext[i];
        BookingCategoryFilterAdapter.BookingSwitcher localBookingSwitcher = new BookingCategoryFilterAdapter.BookingSwitcher(null);
        localBookingSwitcher.key = paramBookingShoplistDataSource.getString("ID");
        localBookingSwitcher.on = paramBookingShoplistDataSource.getBoolean("On");
        this.bookingSwitchers.add(localBookingSwitcher);
        i += 1;
      }
    }
  }

  private void setNormalState(View paramView)
  {
    ((TextView)paramView.findViewById(R.id.booking_category_filter_name)).setTextColor(this.context.getResources().getColor(R.color.deep_gray));
    paramView.findViewById(R.id.icon_select).setVisibility(8);
  }

  private void setSelectState(View paramView)
  {
    ((TextView)paramView.findViewById(R.id.booking_category_filter_name)).setTextColor(this.context.getResources().getColor(R.color.light_red));
    paramView.findViewById(R.id.icon_select).setVisibility(0);
    this.selectView = paramView;
  }

  private void updateSwitchInfo(BookingCategoryFilterAdapter.BookingSwitcher paramBookingSwitcher)
  {
    if (paramBookingSwitcher.key == null);
    while (true)
    {
      return;
      int i = 0;
      while (i < this.bookingSwitchers.size())
      {
        BookingCategoryFilterAdapter.BookingSwitcher localBookingSwitcher = (BookingCategoryFilterAdapter.BookingSwitcher)this.bookingSwitchers.get(i);
        if ((localBookingSwitcher.key != null) && (localBookingSwitcher.key.equals(paramBookingSwitcher.key)))
        {
          localBookingSwitcher.on = paramBookingSwitcher.on;
          this.datasource.curSwitches[i] = new DPObject("Switch").edit().putString("ID", paramBookingSwitcher.key).putBoolean("On", paramBookingSwitcher.on).putString("Name", this.datasource.curSwitches[i].getString("Name")).generate();
          return;
        }
        i += 1;
      }
    }
  }

  public int getCount()
  {
    int j = 0;
    int i;
    if (this.datasource.curTags == null)
    {
      i = 0;
      if (this.datasource.curSwitches != null)
        break label42;
    }
    while (true)
    {
      return i + 1 + j;
      i = this.datasource.curTags.length;
      break;
      label42: j = this.datasource.curSwitches.length;
    }
  }

  public String getCurTagId()
  {
    if (this.datasource.curTag == null)
      return "-1";
    return this.datasource.curTag.getString("ID");
  }

  public DPObject getItem(int paramInt)
  {
    if (paramInt < this.datasource.curTags.length)
      return this.datasource.curTags[paramInt];
    if (paramInt == this.datasource.curTags.length)
      return null;
    return this.datasource.curSwitches[(paramInt - this.datasource.curTags.length - 1)];
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public String getMaxPrice()
  {
    if ("不限".equals(this.datasource.curMaxPrice))
      return null;
    return this.datasource.curMaxPrice;
  }

  public String getMinPrice()
  {
    if ("0".equals(this.datasource.curMinPrice))
      return null;
    return this.datasource.curMinPrice;
  }

  public String getSwitchInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = this.bookingSwitchers.iterator();
    if (localIterator.hasNext())
    {
      BookingCategoryFilterAdapter.BookingSwitcher localBookingSwitcher = (BookingCategoryFilterAdapter.BookingSwitcher)localIterator.next();
      String str = localBookingSwitcher.key;
      if (localBookingSwitcher.on);
      for (int i = 1; ; i = 0)
      {
        localStringBuilder.append(String.format("%s=%d", new Object[] { str, Integer.valueOf(i) })).append("&");
        break;
      }
    }
    return localStringBuilder.toString().substring(0, localStringBuilder.length() - 1);
  }

  @TargetApi(16)
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramView = getItem(paramInt);
    paramViewGroup = LayoutInflater.from(this.context).inflate(R.layout.booking_filter_item, paramViewGroup, false);
    TextView localTextView = (TextView)paramViewGroup.findViewById(R.id.booking_category_filter_name);
    CompoundButton localCompoundButton = (CompoundButton)paramViewGroup.findViewById(R.id.filter_switcher);
    RangeSeekBar localRangeSeekBar = (RangeSeekBar)paramViewGroup.findViewById(R.id.price_range);
    if (paramInt < this.datasource.curTags.length)
    {
      localCompoundButton.setVisibility(8);
      localRangeSeekBar.setVisibility(8);
      localTextView.setText(paramView.getString("Name"));
      localTextView.setVisibility(0);
      if ((this.datasource.curTag != null) && (paramView.getString("ID").equals(this.datasource.curTag.getString("ID"))))
        setSelectState(paramViewGroup);
    }
    while (true)
    {
      paramViewGroup.setOnClickListener(new BookingCategoryFilterAdapter.3(this, paramView, paramInt));
      return paramViewGroup;
      setNormalState(paramViewGroup);
      continue;
      if (paramInt == this.datasource.curTags.length)
      {
        localTextView.setVisibility(8);
        localCompoundButton.setVisibility(8);
        localRangeSeekBar.setVisibility(0);
        if ((this.datasource.curMinPrice != null) && (!"0".equals(this.datasource.curMinPrice)))
        {
          localRangeSeekBar.setLowValue(PriceFormatUtils.transformPriceToValue(Integer.parseInt(this.datasource.curMinPrice), true), this.barLeftField);
          localRangeSeekBar.setLowString(this.datasource.curMinPrice);
        }
        if ((this.datasource.curMaxPrice != null) && (!"不限".equals(this.datasource.curMaxPrice)))
        {
          localRangeSeekBar.setHighValue(PriceFormatUtils.transformPriceToValue(Integer.parseInt(this.datasource.curMaxPrice), false), this.barRightField);
          localRangeSeekBar.setHighString(this.datasource.curMaxPrice);
        }
        while (true)
        {
          localRangeSeekBar.setRangeSeekListener(new BookingCategoryFilterAdapter.1(this, localRangeSeekBar));
          break;
          localRangeSeekBar.setHighValue(100, 2);
        }
      }
      localRangeSeekBar.setVisibility(8);
      localTextView.setText(paramView.getString("Name"));
      localTextView.setVisibility(0);
      localCompoundButton.setChecked(paramView.getBoolean("On"));
      localCompoundButton.setVisibility(0);
      if (Build.VERSION.SDK_INT >= 16)
        ((Switch)localCompoundButton).setSwitchMinWidth(((Switch)localCompoundButton).getTrackDrawable().getIntrinsicWidth());
      localCompoundButton.setOnCheckedChangeListener(new BookingCategoryFilterAdapter.2(this, paramView));
    }
  }

  public Boolean hasFilterInfo()
  {
    if ((this.datasource.curTag != null) && (!"-1".equals(this.datasource.curTag.getString("ID"))))
      return Boolean.valueOf(true);
    if (((this.datasource.curMinPrice != null) && (!"0".equals(this.datasource.curMinPrice))) || ((this.datasource.curMaxPrice != null) && (!"不限".equals(this.datasource.curMaxPrice))))
      return Boolean.valueOf(true);
    Iterator localIterator = this.bookingSwitchers.iterator();
    while (localIterator.hasNext())
      if (((BookingCategoryFilterAdapter.BookingSwitcher)localIterator.next()).on)
        return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  public void setData(BookingShoplistDataSource paramBookingShoplistDataSource)
  {
    this.datasource = paramBookingShoplistDataSource;
    this.bookingSwitchers.clear();
    if (this.datasource.curSwitches != null)
    {
      paramBookingShoplistDataSource = this.datasource.curSwitches;
      int j = paramBookingShoplistDataSource.length;
      int i = 0;
      while (i < j)
      {
        Object localObject = paramBookingShoplistDataSource[i];
        BookingCategoryFilterAdapter.BookingSwitcher localBookingSwitcher = new BookingCategoryFilterAdapter.BookingSwitcher(null);
        localBookingSwitcher.key = localObject.getString("ID");
        localBookingSwitcher.on = localObject.getBoolean("On");
        this.bookingSwitchers.add(localBookingSwitcher);
        i += 1;
      }
    }
    notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.adapter.BookingCategoryFilterAdapter
 * JD-Core Version:    0.6.0
 */