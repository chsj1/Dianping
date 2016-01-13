package com.dianping.takeaway.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.dianping.base.adapter.PortableScrollViewAdapter;
import com.dianping.v1.R.layout;
import java.util.List;

public class CategoryAdapter
  implements PortableScrollViewAdapter
{
  private Context context;
  private List<TakeawayMenuCategory> data;

  public CategoryAdapter(Context paramContext, List<TakeawayMenuCategory> paramList)
  {
    this.data = paramList;
    this.context = paramContext;
  }

  public int getCount()
  {
    if (this.data == null)
      return 0;
    return this.data.size();
  }

  public Object getItem(int paramInt)
  {
    if (this.data == null)
      return null;
    return (TakeawayMenuCategory)this.data.get(paramInt);
  }

  public View getView(int paramInt)
  {
    View localView = LayoutInflater.from(this.context).inflate(R.layout.takeaway_menu_category_item, null, false);
    if ((this.data != null) && (paramInt < this.data.size()))
    {
      ((TextView)localView.findViewById(16908308)).setText(((TakeawayMenuCategory)this.data.get(paramInt)).categoryName);
      localView.setTag(1073741843, ((TakeawayMenuCategory)this.data.get(paramInt)).categoryName);
    }
    return localView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.CategoryAdapter
 * JD-Core Version:    0.6.0
 */