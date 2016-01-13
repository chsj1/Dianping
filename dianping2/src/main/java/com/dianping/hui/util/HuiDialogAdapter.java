package com.dianping.hui.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.v1.R.layout;
import java.util.List;

public class HuiDialogAdapter extends BaseAdapter
{
  private Context context;
  private List<String> dataList;

  public HuiDialogAdapter(Context paramContext, List<String> paramList)
  {
    this.context = paramContext;
    this.dataList = paramList;
  }

  public int getCount()
  {
    return this.dataList.size();
  }

  public Object getItem(int paramInt)
  {
    return this.dataList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramViewGroup = paramView;
    if (paramView == null)
      paramViewGroup = LayoutInflater.from(this.context).inflate(R.layout.dialog_items, null);
    ((TextView)paramViewGroup).setText((CharSequence)this.dataList.get(paramInt));
    return paramViewGroup;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.util.HuiDialogAdapter
 * JD-Core Version:    0.6.0
 */