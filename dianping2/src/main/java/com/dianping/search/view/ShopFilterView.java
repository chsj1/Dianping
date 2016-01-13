package com.dianping.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.RangeSeekBar;
import com.dianping.base.widget.RangeSeekBar.RangeSeekListener;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaButton;

public class ShopFilterView extends LinearLayout
{
  String action;
  ShopFilterList filterList;
  FilterListener filterListener;
  RangeSeekBar rangeBar;
  String riceRangeHigh = "不限";
  String riceRangeLow = "0";

  public ShopFilterView(Context paramContext)
  {
    super(paramContext);
  }

  public ShopFilterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.filterList = ((ShopFilterList)findViewById(R.id.list));
    this.rangeBar = ((RangeSeekBar)findViewById(R.id.find_conditions_pricerange_rsb));
    this.rangeBar.setRangeSeekListener(new RangeSeekBar.RangeSeekListener()
    {
      public void onLeftValueChanged(int paramInt1, int paramInt2)
      {
        ShopFilterView.this.riceRangeLow = PriceFormatUtils.transformValueToPrice(paramInt1, paramInt2);
        ShopFilterView.this.rangeBar.setLowString(ShopFilterView.this.riceRangeLow);
        ShopFilterView.this.rangeBar.invalidate();
      }

      public void onRangeChanged(int paramInt1, int paramInt2)
      {
      }

      public void onRightValueChanged(int paramInt1, int paramInt2)
      {
        ShopFilterView.this.riceRangeHigh = PriceFormatUtils.transformValueToPrice(paramInt1, paramInt2);
        ShopFilterView.this.rangeBar.setHighString(ShopFilterView.this.riceRangeHigh);
        ShopFilterView.this.rangeBar.invalidate();
      }

      public void onSelectedMaxFiled()
      {
        ShopFilterView.this.riceRangeLow = "800";
        ShopFilterView.this.riceRangeHigh = "不限";
        ShopFilterView.this.rangeBar.setLowString(ShopFilterView.this.riceRangeLow);
        ShopFilterView.this.rangeBar.setHighString(ShopFilterView.this.riceRangeHigh);
        ShopFilterView.this.rangeBar.invalidate();
      }

      public void onSelectedMinFiled()
      {
        ShopFilterView.this.riceRangeLow = "0";
        ShopFilterView.this.riceRangeHigh = "20";
        ShopFilterView.this.rangeBar.setLowString(ShopFilterView.this.riceRangeLow);
        ShopFilterView.this.rangeBar.setHighString(ShopFilterView.this.riceRangeHigh);
        ShopFilterView.this.rangeBar.invalidate();
      }
    });
    this.rangeBar.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        return false;
      }
    });
    NovaButton localNovaButton = (NovaButton)findViewById(R.id.okButton);
    localNovaButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        int j = Integer.parseInt(ShopFilterView.this.riceRangeLow);
        if ("不限".equals(ShopFilterView.this.riceRangeHigh));
        for (int i = 99999; ; i = Integer.parseInt(ShopFilterView.this.riceRangeHigh))
        {
          ShopFilterView.this.filterListener.onfilterList(ShopFilterView.this.filterList.getCurrentFilter(), j, i);
          return;
        }
      }
    });
    localNovaButton.setGAString("filter_confirm");
  }

  public void setFilterListener(FilterListener paramFilterListener)
  {
    this.filterListener = paramFilterListener;
  }

  public void setListFilter(DPObject[] paramArrayOfDPObject, DPObject paramDPObject)
  {
    this.filterList.setFilter(paramArrayOfDPObject, paramDPObject);
  }

  public void setPriceHigh(int paramInt)
  {
    label20: label46: RangeSeekBar localRangeSeekBar;
    if (paramInt == -1)
    {
      i = 99999;
      if (i != 99999)
        break label91;
      this.riceRangeHigh = "不限";
      this.rangeBar.setHighString(this.riceRangeHigh);
      if (!this.riceRangeHigh.equals("不限"))
        break label102;
      paramInt = 100;
      localRangeSeekBar = this.rangeBar;
      if ((!this.riceRangeHigh.equals("不限")) && (i < 800))
        break label111;
    }
    label91: label102: label111: for (int i = 2; ; i = 1)
    {
      localRangeSeekBar.setHighValue(paramInt, i);
      this.rangeBar.invalidate();
      return;
      i = paramInt;
      break;
      this.riceRangeHigh = String.valueOf(i);
      break label20;
      paramInt = PriceFormatUtils.transformPriceToValue(i, false);
      break label46;
    }
  }

  public void setPriceLow(int paramInt)
  {
    int i = 0;
    int j;
    RangeSeekBar localRangeSeekBar;
    if (paramInt == -1)
    {
      paramInt = 0;
      this.riceRangeLow = String.valueOf(paramInt);
      this.rangeBar.setLowString(this.riceRangeLow);
      j = PriceFormatUtils.transformPriceToValue(paramInt, true);
      localRangeSeekBar = this.rangeBar;
      if (paramInt > 20)
        break label66;
    }
    label66: for (paramInt = i; ; paramInt = 1)
    {
      localRangeSeekBar.setLowValue(j, paramInt);
      this.rangeBar.invalidate();
      return;
      break;
    }
  }

  public static abstract interface FilterListener
  {
    public abstract void onfilterList(DPObject paramDPObject, int paramInt1, int paramInt2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.view.ShopFilterView
 * JD-Core Version:    0.6.0
 */