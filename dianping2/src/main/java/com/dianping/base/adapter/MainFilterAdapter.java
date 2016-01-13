package com.dianping.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class MainFilterAdapter extends SubFilterAdapter
{
  public MainFilterAdapter(ArrayList<DPObject> paramArrayList, Context paramContext)
  {
    super(paramArrayList, paramContext);
  }

  protected int getMainItemLayout()
  {
    return R.layout.filter_main_item;
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
      localView = this.layoutInflater.inflate(getMainItemLayout(), paramViewGroup, false);
      localView.setTag(this);
    }
    paramView = (ImageView)localView.findViewById(16908294);
    if (paramView != null)
    {
      paramViewGroup = getItem(paramInt);
      if (((paramViewGroup instanceof DPObject)) && (((DPObject)paramViewGroup).isClass("Category")))
      {
        paramView.setImageResource(NovaConfigUtils.getCategoryIconId(((DPObject)paramViewGroup).getInt("ID")));
        paramView.setVisibility(0);
      }
    }
    else
    {
      ((TextView)localView.findViewById(16908308)).setText(getName(paramInt));
      if (this.selectedItem != paramInt)
        break label145;
    }
    label145: for (paramInt = R.color.white; ; paramInt = R.drawable.filter_main_list_item)
    {
      localView.setBackgroundResource(paramInt);
      return localView;
      paramView.setVisibility(8);
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.adapter.MainFilterAdapter
 * JD-Core Version:    0.6.0
 */