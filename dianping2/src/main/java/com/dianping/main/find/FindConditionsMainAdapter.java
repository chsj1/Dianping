package com.dianping.main.find;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CustomGridView;
import com.dianping.v1.R.layout;

public abstract class FindConditionsMainAdapter extends BasicAdapter
{
  protected int columnNum = 3;
  protected String moreText;
  protected DPObject[] tableData = null;

  public FindConditionsMainAdapter(DPObject[] paramArrayOfDPObject)
  {
    this.tableData = paramArrayOfDPObject;
  }

  public int getCount()
  {
    if (this.tableData == null)
      return 0;
    return this.tableData.length + 1;
  }

  public DPObject getItem(int paramInt)
  {
    if (paramInt < this.tableData.length)
      return this.tableData[paramInt];
    return null;
  }

  public long getItemId(int paramInt)
  {
    if (paramInt < this.tableData.length)
      return this.tableData[paramInt].getInt("ID");
    return -1L;
  }

  public String getItemName(int paramInt)
  {
    if (paramInt < this.tableData.length)
      return this.tableData[paramInt].getString("Name");
    if (paramInt == getCount() - 1)
      return this.moreText;
    return null;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    String str = getItemName(paramInt);
    Object localObject;
    if (paramInt % this.columnNum == 0)
    {
      paramView = new TableRow(paramViewGroup.getContext());
      localObject = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_conditions_item, (TableRow)paramView, false);
      ((TextView)localObject).setText(str);
      ((TableRow)paramView).addView((View)localObject);
      paramViewGroup = paramView;
      paramView = (View)localObject;
    }
    while (true)
    {
      if (paramInt == this.tableData.length)
      {
        if (str == null)
          paramView.setVisibility(4);
        localObject = (TableRow.LayoutParams)paramView.getLayoutParams();
        ((TableRow.LayoutParams)localObject).span = ((this.columnNum - getCount() % this.columnNum) % this.columnNum + 1);
        paramView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      }
      return paramViewGroup;
      paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_conditions_item, ((CustomGridView)paramViewGroup).getCurRow(), false);
      paramView = (TextView)paramViewGroup;
      paramView.setText(str);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.FindConditionsMainAdapter
 * JD-Core Version:    0.6.0
 */