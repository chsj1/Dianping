package com.dianping.base.widget.dialogfilter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class CinemaListFilterDialog extends FilterDialog
{
  Adapter adapter = new Adapter();
  String countFieldName = "Count";
  String fieldName = "Name";
  final AdapterView.OnItemClickListener handler = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      if (CinemaListFilterDialog.this.listener != null)
      {
        paramAdapterView = CinemaListFilterDialog.this.adapter.getItem(paramInt);
        CinemaListFilterDialog.this.listener.onFilter(CinemaListFilterDialog.this, paramAdapterView);
      }
    }
  };
  DPObject headerItem;
  DPObject[] items;
  ListView list = (ListView)getLayoutInflater().inflate(R.layout.list_filter, getFilterViewParent(), false);
  DPObject selectedItem;
  boolean showCount;

  public CinemaListFilterDialog(Activity paramActivity)
  {
    super(paramActivity);
    this.list.setAdapter(this.adapter);
    this.list.setOnItemClickListener(this.handler);
    setFilterView(this.list);
  }

  public CinemaListFilterDialog(Activity paramActivity, boolean paramBoolean)
  {
    this(paramActivity);
    this.showCount = paramBoolean;
  }

  public DPObject getHeaderItem()
  {
    return this.headerItem;
  }

  public DPObject[] getItems()
  {
    return this.items;
  }

  public DPObject getSelectedItem()
  {
    return this.selectedItem;
  }

  public void setHeaderItem(DPObject paramDPObject)
  {
    this.headerItem = paramDPObject;
    this.adapter.notifyDataSetChanged();
  }

  public void setItems(DPObject[] paramArrayOfDPObject)
  {
    this.items = paramArrayOfDPObject;
    this.adapter.notifyDataSetChanged();
  }

  public void setSelectedItem(DPObject paramDPObject)
  {
    this.selectedItem = paramDPObject;
    this.adapter.notifyDataSetChanged();
  }

  class Adapter extends BaseAdapter
  {
    Adapter()
    {
    }

    public int getCount()
    {
      int j = 0;
      int i;
      if (CinemaListFilterDialog.this.headerItem == null)
      {
        i = 0;
        if (CinemaListFilterDialog.this.items != null)
          break label33;
      }
      while (true)
      {
        return i + j;
        i = 1;
        break;
        label33: j = CinemaListFilterDialog.this.items.length;
      }
    }

    public DPObject getItem(int paramInt)
    {
      if (CinemaListFilterDialog.this.headerItem == null)
        return CinemaListFilterDialog.this.items[paramInt];
      if (paramInt == 0)
        return CinemaListFilterDialog.this.headerItem;
      return CinemaListFilterDialog.this.items[(paramInt - 1)];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int i = -39679;
      View localView = paramView;
      if (paramView == null)
        localView = CinemaListFilterDialog.this.getLayoutInflater().inflate(R.layout.cinema_filter_item, paramViewGroup, false);
      Object localObject = (TextView)localView.findViewById(16908308);
      TextView localTextView = (TextView)localView.findViewById(16908309);
      paramViewGroup = getItem(paramInt);
      ((TextView)localObject).setText(paramViewGroup.getString(CinemaListFilterDialog.this.fieldName));
      paramInt = paramViewGroup.getInt(CinemaListFilterDialog.this.countFieldName);
      if ((paramInt >= 0) && (CinemaListFilterDialog.this.showCount))
      {
        localTextView.setText("" + paramInt);
        if (!paramViewGroup.isClass("Pair"))
          break label251;
        if (CinemaListFilterDialog.this.selectedItem != null)
          break label235;
        paramView = "";
        label146: paramViewGroup = paramViewGroup.getString("ID");
        if (!paramViewGroup.equals(paramView))
          break label296;
        paramInt = -39679;
        label164: ((TextView)localObject).setTextColor(paramInt);
        if (!paramViewGroup.equals(paramView))
          break label302;
        paramInt = i;
        label181: localTextView.setTextColor(paramInt);
        localObject = CinemaListFilterDialog.this.getContext().getResources();
        if (!paramViewGroup.equals(paramView))
          break label308;
      }
      label296: label302: label308: for (paramInt = R.drawable.filter_sub_selected; ; paramInt = R.drawable.filter_sub_list_item)
      {
        localView.setBackgroundDrawable(((Resources)localObject).getDrawable(paramInt));
        return localView;
        localTextView.setText("");
        break;
        label235: paramView = CinemaListFilterDialog.this.selectedItem.getString("ID");
        break label146;
        label251: if (CinemaListFilterDialog.this.selectedItem == null);
        for (paramView = ""; ; paramView = String.valueOf(CinemaListFilterDialog.this.selectedItem.getInt("ID")))
        {
          paramViewGroup = String.valueOf(paramViewGroup.getInt("ID"));
          break;
        }
        paramInt = -16777216;
        break label164;
        paramInt = -16777216;
        break label181;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.dialogfilter.CinemaListFilterDialog
 * JD-Core Version:    0.6.0
 */