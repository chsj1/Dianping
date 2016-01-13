package com.dianping.travel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.ThreeFilterFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class FlavourDishShopFilterFragment extends ThreeFilterFragment
{
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.filter_layout, paramViewGroup, false);
    this.filterBar = ((FilterBar)paramLayoutInflater.findViewById(R.id.filterBar));
    this.filterBar.addItem("region", "全部商区");
    this.filterBar.addItem("rank", "默认排序");
    this.filterBar.setOnItemClickListener(this);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.fragment.FlavourDishShopFilterFragment
 * JD-Core Version:    0.6.0
 */