package com.dianping.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.search.widget.SearchGridView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;

public class ShopFilterNaviView extends NovaLinearLayout
  implements View.OnClickListener
{
  FilterListener filterListener;
  private Context mContext;
  private LinearLayout mFilterContainer;
  private SearchGridView[] mGridViews;
  DPObject[] mNavList;

  public ShopFilterNaviView(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
  }

  public ShopFilterNaviView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  public String getFilterList()
  {
    if (this.mNavList == null)
      return "";
    StringBuilder localStringBuilder = new StringBuilder("");
    int i = 0;
    while (i < this.mNavList.length)
    {
      localStringBuilder.append(this.mGridViews[i].getFilter());
      i += 1;
    }
    if (localStringBuilder.length() > 0)
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }

  public String getFilterName()
  {
    if (this.mNavList == null)
      return "";
    StringBuilder localStringBuilder = new StringBuilder("");
    int i = 0;
    while (i < this.mNavList.length)
    {
      localStringBuilder.append(this.mGridViews[i].getFilterName());
      i += 1;
    }
    if (localStringBuilder.length() > 0)
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.confirm_btn)
    {
      boolean bool = false;
      if ((this.mNavList != null) || (this.mNavList.length > 0))
      {
        i = 0;
        while (i < this.mNavList.length)
        {
          if (this.mGridViews[i].isFilterChanged())
          {
            this.mGridViews[i].updateSelFilter();
            bool = true;
          }
          i += 1;
        }
        this.filterListener.onfilterList(getFilterList(), bool);
        GAHelper.instance().contextStatisticsEvent(getContext(), "filter_confirm", getFilterName(), 2147483647, "tap");
      }
    }
    while (true)
    {
      return;
      if (i != R.id.reset_btn)
        continue;
      i = 0;
      while (i < this.mNavList.length)
      {
        this.mGridViews[i].reset();
        i += 1;
      }
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mFilterContainer = ((LinearLayout)findViewById(R.id.filter_container));
    findViewById(R.id.reset_btn).setOnClickListener(this);
    findViewById(R.id.confirm_btn).setOnClickListener(this);
    findViewById(R.id.btn_cont).setOnClickListener(this);
  }

  public void setFilterListener(FilterListener paramFilterListener)
  {
    this.filterListener = paramFilterListener;
  }

  public void setNavList(DPObject[] paramArrayOfDPObject)
  {
    this.mNavList = paramArrayOfDPObject;
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0));
    while (true)
    {
      return;
      this.mFilterContainer.removeAllViews();
      this.mGridViews = new SearchGridView[paramArrayOfDPObject.length];
      int i = 0;
      while (i < paramArrayOfDPObject.length)
      {
        this.mGridViews[i] = new SearchGridView(this.mContext);
        this.mGridViews[i].setData(paramArrayOfDPObject[i]);
        if (i == paramArrayOfDPObject.length - 1)
          this.mGridViews[i].hideSep();
        this.mFilterContainer.addView(this.mGridViews[i]);
        i += 1;
      }
    }
  }

  public static abstract interface FilterListener
  {
    public abstract void onfilterList(String paramString, boolean paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.view.ShopFilterNaviView
 * JD-Core Version:    0.6.0
 */