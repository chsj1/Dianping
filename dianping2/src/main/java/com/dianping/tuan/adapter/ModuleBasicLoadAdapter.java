package com.dianping.tuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.tuan.adapter.decorator.ModuleAdapter;
import com.dianping.tuan.utils.ViewItemUtils;
import com.dianping.tuan.widget.ViewItemData;
import com.dianping.tuan.widget.viewitem.TuanItemDisplayType;
import com.dianping.v1.R.id;
import java.util.ArrayList;
import java.util.Iterator;

public class ModuleBasicLoadAdapter extends TuanBasicLoadAdapter
  implements ModuleAdapter
{
  public ModuleBasicLoadAdapter(Context paramContext)
  {
    super(paramContext);
  }

  public ModuleBasicLoadAdapter(Context paramContext, double paramDouble1, double paramDouble2)
  {
    super(paramContext, paramDouble1, paramDouble2, 0);
  }

  public boolean attachToPrevious()
  {
    return false;
  }

  public int getModuleIndex(int paramInt)
  {
    if (paramInt < this.displayData.size())
      return ((ViewItemData)this.displayData.get(paramInt)).moduleIndex;
    return 0;
  }

  public int getSectionIndex(int paramInt)
  {
    if (paramInt < this.displayData.size())
      return ((ViewItemData)this.displayData.get(paramInt)).sectionIndex;
    return 0;
  }

  protected View itemViewWithData(ViewItemData paramViewItemData, int paramInt, View paramView, ViewGroup paramViewGroup, double paramDouble1, double paramDouble2)
  {
    paramView = super.itemViewWithData(paramViewItemData, paramInt, paramView, paramViewGroup, paramDouble1, paramDouble2);
    if ((paramView != null) && (paramViewItemData != null) && (paramViewItemData.displayType == TuanItemDisplayType.AGG_MORE))
    {
      paramViewItemData = paramView.findViewById(R.id.top_line);
      paramViewGroup = paramView.findViewById(R.id.bottom_line);
      paramViewItemData.setVisibility(8);
      paramViewGroup.setVisibility(8);
    }
    return paramView;
  }

  public void parseDisplayData()
  {
    this.displayData.clear();
    int j = 0;
    int i = 0;
    Object localObject1 = null;
    int k = 0;
    int n = 0;
    while (n < this.mData.size())
    {
      boolean bool = false;
      if (this.speardIndexList.contains(Integer.valueOf(n)))
        bool = true;
      ArrayList localArrayList = ViewItemUtils.unfoldViewItem((DPObject)this.mData.get(n), n, bool, false);
      int m;
      Object localObject2;
      int i1;
      if (localArrayList.size() > 1)
      {
        Iterator localIterator = localArrayList.iterator();
        m = j;
        localObject2 = localObject1;
        i1 = i;
        if (localIterator.hasNext())
        {
          localObject2 = (ViewItemData)localIterator.next();
          if (((ViewItemData)localObject2).displayType == TuanItemDisplayType.AGG_SHOP_MAIN)
          {
            j += 1;
            i += 1;
            ((ViewItemData)localObject2).moduleIndex = j;
            ((ViewItemData)localObject2).sectionIndex = i;
          }
          while (true)
          {
            localObject1 = localObject2;
            k = 1;
            break;
            if (((ViewItemData)localObject2).displayType == TuanItemDisplayType.AGG_MORE)
            {
              i += 1;
              ((ViewItemData)localObject2).sectionIndex = i;
              ((ViewItemData)localObject2).moduleIndex = j;
              continue;
            }
            k = i;
            if (localObject1 != null)
            {
              k = i;
              if (((ViewItemData)localObject1).displayType == TuanItemDisplayType.AGG_SHOP_MAIN)
                k = i + 1;
            }
            ((ViewItemData)localObject2).sectionIndex = k;
            ((ViewItemData)localObject2).moduleIndex = j;
            i = k;
          }
        }
      }
      else
      {
        localObject2 = (ViewItemData)localArrayList.get(0);
        m = j;
        if (localObject1 != null)
          if (((ViewItemData)localObject1).displayType != TuanItemDisplayType.BANNER)
          {
            m = j;
            if (k == 0);
          }
          else
          {
            m = j + 1;
          }
        ((ViewItemData)localObject2).moduleIndex = m;
        ((ViewItemData)localObject2).sectionIndex = i;
        k = 0;
        i1 = i;
      }
      this.displayData.addAll(localArrayList);
      n += 1;
      j = m;
      localObject1 = localObject2;
      i = i1;
    }
    localObject1 = this.displayData.iterator();
    while (((Iterator)localObject1).hasNext())
      ((ViewItemData)((Iterator)localObject1).next()).bottomLine = false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.adapter.ModuleBasicLoadAdapter
 * JD-Core Version:    0.6.0
 */