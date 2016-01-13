package com.dianping.search.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.shoplist.data.model.NavTree;
import com.dianping.base.shoplist.data.model.NavTreeNode;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog.LeftAdapter;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog.RightAdapter;
import com.dianping.util.ViewUtils;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchTwinListFilterDialog extends TwinListFilterDialog
{
  NavTree navTree;

  public SearchTwinListFilterDialog(Activity paramActivity, String paramString)
  {
    super(paramActivity, paramString);
  }

  public SearchTwinListFilterDialog(Activity paramActivity, String paramString, int paramInt)
  {
    super(paramActivity, paramString, paramInt);
  }

  protected boolean checkEquals(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    return (paramDPObject1 != null) && (paramDPObject2 != null) && (paramDPObject1.getInt("ID") == paramDPObject2.getInt("ID")) && (paramDPObject1.getInt("RegionType") == paramDPObject2.getInt("RegionType"));
  }

  public View getRightView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramView = (NovaLinearLayout)super.getRightView(paramInt, paramView, paramViewGroup);
    paramView.setGAString(this.mElementid + "_right");
    return paramView;
  }

  public void onLeftClick(int paramInt)
  {
    super.onLeftClick(paramInt);
    GAHelper.instance().contextStatisticsEvent(getContext(), this.mElementid + "_left", this.leftAdapter.getItem(paramInt).getString("Name"), paramInt, "tap");
  }

  public void setDialogHeightAdaptive(boolean paramBoolean)
  {
    setDialogHeightAdaptive(true, paramBoolean);
  }

  public void setDialogHeightAdaptive(boolean paramBoolean1, boolean paramBoolean2)
  {
    ViewGroup localViewGroup = getFilterViewParent();
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)localViewGroup.getLayoutParams();
    int j;
    if (paramBoolean1)
    {
      int n = ViewUtils.dip2px(getContext(), 45.0F);
      localLayoutParams.height = (this.navTree.getParent(0).children.size() * n);
      int i = ViewUtils.dip2px(getContext(), 45.0F);
      Object localObject = this.navTree.getParent(0).children.iterator();
      while (((Iterator)localObject).hasNext())
      {
        j = i * ((NavTreeNode)((Iterator)localObject).next()).children.size();
        if (j <= localLayoutParams.height)
          continue;
        localLayoutParams.height = j;
      }
      localObject = new Rect();
      getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame((Rect)localObject);
      i = ((Rect)localObject).top;
      int k = (int)(ViewUtils.getScreenHeightPixels(getContext()) * 0.6D) - i - ViewUtils.dip2px(getContext(), 95.0F);
      int m = (int)(ViewUtils.getScreenHeightPixels(getContext()) * 0.9D) - i - ViewUtils.dip2px(getContext(), 95.0F);
      j = m;
      i = k;
      if (paramBoolean2)
      {
        i = k - ViewUtils.dip2px(getContext(), 45.0F);
        j = m - ViewUtils.dip2px(getContext(), 45.0F);
      }
      if (localLayoutParams.height < i)
      {
        localLayoutParams.height = i;
        localLayoutParams.height = (localLayoutParams.height / n * n);
      }
    }
    for (localLayoutParams.bottomMargin = 0; ; localLayoutParams.bottomMargin = ViewUtils.dip2px(getContext(), 80.0F))
    {
      localViewGroup.setLayoutParams(localLayoutParams);
      return;
      localLayoutParams.height = j;
      break;
      localLayoutParams.height = -1;
    }
  }

  public void setNavTree(NavTree paramNavTree)
  {
    this.navTree = paramNavTree;
    this.headerItemCount = 0;
    if (paramNavTree != null)
      paramNavTree = paramNavTree.getParent(0);
    DPObject[] arrayOfDPObject;
    while (true)
    {
      arrayOfDPObject = new DPObject[paramNavTree.children.size()];
      int i = 0;
      paramNavTree = paramNavTree.children.iterator();
      while (paramNavTree.hasNext())
      {
        arrayOfDPObject[i] = ((NavTreeNode)paramNavTree.next()).node;
        this.headerItemCount += arrayOfDPObject[i].getInt("Count");
        i += 1;
      }
      paramNavTree = new NavTreeNode();
      paramNavTree.children = new ArrayList();
      this.rightAdapter.childs = null;
    }
    if (DPObjectUtils.isDPObjectof(this.headerItem, "Category"))
      this.headerItem = this.headerItem.edit().putInt("Count", this.headerItemCount).generate();
    this.leftAdapter.parents = arrayOfDPObject;
    this.leftAdapter.notifyDataSetChanged();
    this.rightAdapter.notifyDataSetChanged();
  }

  protected void setParentID(int paramInt)
  {
    if ((this.headerItem != null) && (this.headerItem.getInt("ID") == paramInt))
    {
      this.leftAdapter.selected = this.headerItem;
      this.rightAdapter.childs = null;
    }
    NavTreeNode localNavTreeNode;
    do
    {
      do
        return;
      while (this.navTree == null);
      localNavTreeNode = this.navTree.getParent(paramInt);
    }
    while (localNavTreeNode == null);
    DPObject[] arrayOfDPObject = new DPObject[localNavTreeNode.children.size()];
    paramInt = 0;
    Iterator localIterator = localNavTreeNode.children.iterator();
    while (localIterator.hasNext())
    {
      arrayOfDPObject[paramInt] = ((NavTreeNode)localIterator.next()).node;
      paramInt += 1;
    }
    this.leftAdapter.selected = localNavTreeNode.node;
    this.rightAdapter.childs = arrayOfDPObject;
  }

  public void setSelectedItem(DPObject paramDPObject)
  {
    int j = 0;
    super.setSelectedItem(paramDPObject);
    int k = this.leftAdapter.getCount();
    int i = 0;
    if (i < k)
    {
      if (!checkEquals(this.leftAdapter.selected, this.leftAdapter.getItem(i)))
        break label128;
      paramDPObject = this.leftList;
      if (i - 3 > 0)
      {
        i -= 3;
        label61: paramDPObject.setSelection(i);
      }
    }
    else
    {
      k = this.rightAdapter.getCount();
      i = 0;
    }
    while (true)
    {
      if (i < k)
      {
        if (checkEquals(this.selectedItem, this.rightAdapter.getItem(i)))
        {
          paramDPObject = this.rightList;
          if (i - 3 > 0)
            j = i - 3;
          paramDPObject.setSelection(j);
        }
      }
      else
      {
        return;
        i = 0;
        break label61;
        label128: i += 1;
        break;
      }
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchTwinListFilterDialog
 * JD-Core Version:    0.6.0
 */