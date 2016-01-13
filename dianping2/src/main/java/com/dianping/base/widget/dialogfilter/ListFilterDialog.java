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
import com.dianping.widget.view.NovaLinearLayout;

public class ListFilterDialog extends FilterDialog
{
  private static final String countFieldName = "Count";
  private static final String fieldName = "Name";
  Adapter adapter;
  private final AdapterView.OnItemClickListener handler = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      if (ListFilterDialog.this.listener != null)
      {
        paramAdapterView = ListFilterDialog.this.adapter.getItem(paramInt);
        ListFilterDialog.this.listener.onFilter(ListFilterDialog.this, paramAdapterView);
      }
    }
  };
  DPObject headerItem;
  DPObject[] items;
  String mElementid;
  DPObject selectedItem;
  boolean showCount;

  public ListFilterDialog(Activity paramActivity)
  {
    this(paramActivity, null);
  }

  public ListFilterDialog(Activity paramActivity, String paramString)
  {
    super(paramActivity);
    paramActivity = (ListView)getLayoutInflater().inflate(R.layout.list_filter, getFilterViewParent(), false);
    this.adapter = new Adapter(null);
    paramActivity.setAdapter(this.adapter);
    paramActivity.setOnItemClickListener(this.handler);
    setFilterView(paramActivity);
    this.mElementid = paramString;
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

  private class Adapter extends BaseAdapter
  {
    private Adapter()
    {
    }

    public int getCount()
    {
      int j = 0;
      int i;
      if (ListFilterDialog.this.headerItem == null)
      {
        i = 0;
        if (ListFilterDialog.this.items != null)
          break label33;
      }
      while (true)
      {
        return i + j;
        i = 1;
        break;
        label33: j = ListFilterDialog.this.items.length;
      }
    }

    public DPObject getItem(int paramInt)
    {
      if (ListFilterDialog.this.headerItem == null)
        return ListFilterDialog.this.items[paramInt];
      if (paramInt == 0)
        return ListFilterDialog.this.headerItem;
      return ListFilterDialog.this.items[(paramInt - 1)];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int i = -39679;
      Object localObject2;
      TextView localTextView;
      Object localObject1;
      if (paramView == null)
      {
        paramViewGroup = (NovaLinearLayout)ListFilterDialog.this.getLayoutInflater().inflate(R.layout.filter_item, paramViewGroup, false);
        localObject2 = (TextView)paramViewGroup.findViewById(16908308);
        localTextView = (TextView)paramViewGroup.findViewById(16908309);
        localObject1 = getItem(paramInt);
        ((TextView)localObject2).setText(((DPObject)localObject1).getString("Name"));
        paramInt = ((DPObject)localObject1).getInt("Count");
        if ((paramInt < 0) || (!ListFilterDialog.this.showCount))
          break label245;
        localTextView.setText("" + paramInt);
        label114: paramViewGroup.setGAString(ListFilterDialog.this.mElementid, ((DPObject)localObject1).getString("Name"));
        if (!((DPObject)localObject1).isClass("Pair"))
          break label271;
        if (ListFilterDialog.this.selectedItem != null)
          break label255;
        paramView = "";
        label155: localObject1 = ((DPObject)localObject1).getString("ID");
        if (!((String)localObject1).equals(paramView))
          break label318;
        paramInt = -39679;
        label176: ((TextView)localObject2).setTextColor(paramInt);
        if (!((String)localObject1).equals(paramView))
          break label324;
        paramInt = i;
        label194: localTextView.setTextColor(paramInt);
        localObject2 = ListFilterDialog.this.getContext().getResources();
        if (!((String)localObject1).equals(paramView))
          break label330;
      }
      label271: label318: label324: label330: for (paramInt = R.drawable.filter_sub_selected; ; paramInt = R.drawable.filter_sub_list_item)
      {
        paramViewGroup.setBackgroundDrawable(((Resources)localObject2).getDrawable(paramInt));
        return paramViewGroup;
        paramViewGroup = (NovaLinearLayout)paramView;
        break;
        label245: localTextView.setText("");
        break label114;
        label255: paramView = ListFilterDialog.this.selectedItem.getString("ID");
        break label155;
        if (ListFilterDialog.this.selectedItem == null);
        for (paramView = ""; ; paramView = String.valueOf(ListFilterDialog.this.selectedItem.getInt("ID")))
        {
          localObject1 = String.valueOf(((DPObject)localObject1).getInt("ID"));
          break;
        }
        paramInt = -16777216;
        break label176;
        paramInt = -16777216;
        break label194;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.dialogfilter.ListFilterDialog
 * JD-Core Version:    0.6.0
 */