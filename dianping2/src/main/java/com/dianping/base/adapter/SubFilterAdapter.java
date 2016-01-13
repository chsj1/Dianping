package com.dianping.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;

public class SubFilterAdapter extends BaseAdapter
  implements IFilterAdapter
{
  protected LayoutInflater layoutInflater;
  protected List<DPObject> list = new ArrayList();
  protected int selectedItem = -1;
  protected int textColor_n;

  public SubFilterAdapter(ArrayList<DPObject> paramArrayList, Context paramContext)
  {
    if (paramArrayList != null)
    {
      this.list.clear();
      this.list.addAll(paramArrayList);
    }
    this.layoutInflater = LayoutInflater.from(paramContext);
  }

  public int getCount()
  {
    if (this.list == null)
      return 0;
    return this.list.size();
  }

  protected int getId(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return 0;
    if ((paramDPObject.isClass("Category")) || (paramDPObject.isClass("Region")) || (paramDPObject.isClass("City")) || (paramDPObject.isClass("Metro")))
      return paramDPObject.getInt("ID");
    if (paramDPObject.isClass("Pair"))
      return paramDPObject.getString("ID").hashCode();
    return paramDPObject.getInt("ID");
  }

  public Object getItem(int paramInt)
  {
    if (this.list == null)
      return null;
    return this.list.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  protected String getName(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if (localObject == null)
      return "";
    if ((localObject instanceof DPObject))
    {
      DPObject localDPObject = (DPObject)localObject;
      if ((localDPObject.isClass("Category")) || (localDPObject.isClass("Region")) || (localDPObject.isClass("City")) || (localDPObject.isClass("Metro")))
        return localDPObject.getString("Name");
      if ((localDPObject.isClass("Pair")) || (localDPObject.isClass("NotificationSubType")))
        return localDPObject.getString("Name");
    }
    return localObject.toString();
  }

  public int getSelectItem()
  {
    return this.selectedItem;
  }

  protected int getSubFilterItemLayout()
  {
    return R.layout.filter_sub_item;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView;
    if (paramView != null)
    {
      localView = paramView;
      if (paramView.getTag() == this);
    }
    else
    {
      localView = this.layoutInflater.inflate(getSubFilterItemLayout(), paramViewGroup, false);
      localView.setTag(this);
    }
    paramView = (TextView)localView.findViewById(16908308);
    paramView.setText(getName(paramInt));
    if (this.selectedItem == paramInt)
    {
      paramView.setTextColor(-39679);
      if (this.selectedItem != paramInt)
        break label118;
    }
    label118: for (paramInt = R.drawable.filter_sub_selected; ; paramInt = R.drawable.filter_sub_list_item)
    {
      localView.setBackgroundResource(paramInt);
      return localView;
      if (this.textColor_n != 0)
      {
        paramView.setTextColor(this.textColor_n);
        break;
      }
      paramView.setTextColor(-10066330);
      break;
    }
  }

  protected int indexOf(List<DPObject> paramList, DPObject paramDPObject)
  {
    int j;
    if ((paramList == null) || (paramDPObject == null))
    {
      j = -1;
      return j;
    }
    int k = getId(paramDPObject);
    int i = 0;
    while (true)
    {
      if (i >= paramList.size())
        break label62;
      j = i;
      if (getId((DPObject)paramList.get(i)) == k)
        break;
      i += 1;
    }
    label62: return -1;
  }

  public void setDataSet(ArrayList<DPObject> paramArrayList)
  {
    this.list.clear();
    if (paramArrayList != null)
      this.list.addAll(paramArrayList);
    notifyDataSetChanged();
  }

  public void setDataSet(List<DPObject> paramList, DPObject paramDPObject)
  {
    this.list.clear();
    if (paramList != null)
    {
      this.list.addAll(paramList);
      if (paramDPObject == null)
        break label43;
    }
    label43: for (this.selectedItem = indexOf(paramList, paramDPObject); ; this.selectedItem = 0)
    {
      notifyDataSetChanged();
      return;
    }
  }

  public void setNormalTextColor(int paramInt)
  {
    this.textColor_n = paramInt;
  }

  public void setSelectItem(int paramInt)
  {
    this.selectedItem = paramInt;
    notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.adapter.SubFilterAdapter
 * JD-Core Version:    0.6.0
 */