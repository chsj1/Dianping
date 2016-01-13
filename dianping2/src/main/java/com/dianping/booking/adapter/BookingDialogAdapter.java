package com.dianping.booking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class BookingDialogAdapter extends BaseAdapter
{
  private LayoutInflater inflater;
  private ArrayList<String> list;
  private TextView textView = null;

  public BookingDialogAdapter(Context paramContext, ArrayList<String> paramArrayList)
  {
    this.list = paramArrayList;
    this.inflater = LayoutInflater.from(paramContext);
  }

  public int getCount()
  {
    return this.list.size();
  }

  public Object getItem(int paramInt)
  {
    return this.list.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null)
    {
      paramView = this.inflater.inflate(R.layout.dialog_items, null);
      this.textView = ((TextView)paramView.findViewById(R.id.text1));
      paramView.setTag(this.textView);
    }
    while (true)
    {
      this.textView.setText((CharSequence)this.list.get(paramInt));
      return paramView;
      this.textView = ((TextView)paramView.getTag());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.adapter.BookingDialogAdapter
 * JD-Core Version:    0.6.0
 */