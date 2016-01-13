package com.dianping.tuan.utils;

import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.tuan.widget.ViewItemData;
import com.dianping.tuan.widget.viewitem.TuanItemDisplayType;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.tuan.widget.ViewItemData;>;

public class ViewItemUtils
{
  protected static ViewItemData getAggViewItemData(int paramInt1, int paramInt2, DPObject paramDPObject, boolean paramBoolean)
  {
    ViewItemData localViewItemData = new ViewItemData();
    localViewItemData.index = paramInt1;
    localViewItemData.innerIndex = paramInt2;
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
    {
      ViewItemType localViewItemType = ViewItemType.parseFromInteger(paramDPObject.getInt("Type"));
      switch (ViewItemUtils.1.$SwitchMap$com$dianping$tuan$utils$ViewItemType[localViewItemType.ordinal()])
      {
      default:
      case 2:
      case 3:
      case 11:
      case 12:
      }
    }
    while (true)
    {
      localViewItemData.viewItem = paramDPObject;
      localViewItemData.bottomLine = paramBoolean;
      return localViewItemData;
      localViewItemData.displayType = TuanItemDisplayType.AGG_DEAL;
      continue;
      localViewItemData.displayType = TuanItemDisplayType.AGG_HUI;
      continue;
      localViewItemData.displayType = TuanItemDisplayType.AGG_MALL;
      continue;
      localViewItemData.displayType = TuanItemDisplayType.AGG_ZEUS;
    }
  }

  public static ArrayList<ViewItemData> unfoldAggViewItem(DPObject paramDPObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    ArrayList localArrayList = new ArrayList();
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"));
    Object localObject;
    int j;
    int i;
    label170: 
    do
    {
      do
      {
        do
          return localArrayList;
        while (ViewItemType.parseFromInteger(paramDPObject.getInt("Type")) != ViewItemType.AGG_VIEW_ITEM);
        paramDPObject = paramDPObject.getObject("Agg");
      }
      while (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewAggItem"));
      if (paramBoolean2)
      {
        localObject = new ViewItemData();
        ((ViewItemData)localObject).displayType = TuanItemDisplayType.AGG_DIVIDER;
        localArrayList.add(localObject);
      }
      localObject = paramDPObject.getObject("MainItem");
      localViewItemType = ViewItemType.parseFromInteger(((DPObject)localObject).getInt("Type"));
      localViewItemData = new ViewItemData();
      localViewItemData.index = paramInt;
      switch (ViewItemUtils.1.$SwitchMap$com$dianping$tuan$utils$ViewItemType[localViewItemType.ordinal()])
      {
      default:
        j = paramDPObject.getInt("ShowCount");
        localObject = paramDPObject.getString("MoreText");
        paramDPObject = paramDPObject.getArray("AggItems");
        i = 0;
      case 5:
      }
    }
    while (i >= paramDPObject.length);
    ViewItemType localViewItemType = paramDPObject[i];
    ViewItemData localViewItemData = new ViewItemData();
    localViewItemData.index = paramInt;
    boolean bool = true;
    if (i == paramDPObject.length - 1)
      paramBoolean2 = false;
    while (true)
    {
      if ((!paramBoolean1) && (i >= j))
        break label303;
      localArrayList.add(getAggViewItemData(paramInt, i, localViewItemType, paramBoolean2));
      i += 1;
      break label170;
      localViewItemData.displayType = TuanItemDisplayType.AGG_SHOP_MAIN;
      localViewItemData.viewItem = ((DPObject)localObject);
      localViewItemData.bottomLine = true;
      localArrayList.add(localViewItemData);
      break;
      paramBoolean2 = bool;
      if (paramBoolean1)
        continue;
      paramBoolean2 = bool;
      if (i != j - 1)
        continue;
      paramBoolean2 = false;
    }
    label303: localViewItemData.displayType = TuanItemDisplayType.AGG_MORE;
    localViewItemData.text = ((String)localObject);
    localArrayList.add(localViewItemData);
    return (ArrayList<ViewItemData>)localArrayList;
  }

  public static ArrayList<ViewItemData> unfoldViewItem(DPObject paramDPObject, int paramInt)
  {
    return unfoldViewItem(paramDPObject, paramInt, false, true);
  }

  public static ArrayList<ViewItemData> unfoldViewItem(DPObject paramDPObject, int paramInt, boolean paramBoolean)
  {
    return unfoldViewItem(paramDPObject, paramInt, paramBoolean, true);
  }

  public static ArrayList<ViewItemData> unfoldViewItem(DPObject paramDPObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    ArrayList localArrayList = new ArrayList();
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      return localArrayList;
    ViewItemData localViewItemData = new ViewItemData();
    ViewItemType localViewItemType = ViewItemType.parseFromInteger(paramDPObject.getInt("Type"));
    localViewItemData.index = paramInt;
    switch (ViewItemUtils.1.$SwitchMap$com$dianping$tuan$utils$ViewItemType[localViewItemType.ordinal()])
    {
    case 1:
    default:
      return localArrayList;
    case 2:
      localViewItemData.displayType = TuanItemDisplayType.DEAL;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 3:
      localViewItemData.displayType = TuanItemDisplayType.HUI;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 4:
      localArrayList.addAll(unfoldAggViewItem(paramDPObject, paramInt, paramBoolean1, paramBoolean2));
      return localArrayList;
    case 5:
      localViewItemData.displayType = TuanItemDisplayType.SHOP;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 6:
      localViewItemData.displayType = TuanItemDisplayType.TAG_RIGHT;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 7:
      localViewItemData.displayType = TuanItemDisplayType.TIP_TITLE;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 8:
      localViewItemData.displayType = TuanItemDisplayType.BANNER;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 9:
      localViewItemData.displayType = TuanItemDisplayType.DEAL;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 10:
      localViewItemData.displayType = TuanItemDisplayType.WARNING;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 11:
      localViewItemData.displayType = TuanItemDisplayType.MALL;
      localViewItemData.viewItem = paramDPObject;
      localArrayList.add(localViewItemData);
      return localArrayList;
    case 12:
    }
    localViewItemData.displayType = TuanItemDisplayType.ZEUS;
    localViewItemData.viewItem = paramDPObject;
    localArrayList.add(localViewItemData);
    return localArrayList;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.utils.ViewItemUtils
 * JD-Core Version:    0.6.0
 */