package com.dianping.search.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.base.shoplist.data.model.NavTree;
import com.dianping.base.shoplist.data.model.NavTreeNode;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog.LeftAdapter;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Iterator;
import java.util.List;

public class SearchTwinListFilterDialogWithRegionAndSubway extends SearchTwinListFilterDialog
{
  private int currentTabId = 0;
  private NewShopListDataSource ds;
  private View regionTabBottomLine;
  private View subwayTabBottomLine;

  public SearchTwinListFilterDialogWithRegionAndSubway(Activity paramActivity, String paramString, NewShopListDataSource paramNewShopListDataSource)
  {
    super(paramActivity, paramString, R.layout.shoplist_twin_filter_with_two_tabs);
    this.ds = paramNewShopListDataSource;
    paramActivity = (NovaLinearLayout)findViewById(R.id.tab_region);
    paramString = (NovaLinearLayout)findViewById(R.id.tab_subway);
    this.regionTabBottomLine = findViewById(R.id.tab_region_bottom_line);
    this.subwayTabBottomLine = findViewById(R.id.tab_subway_bottom_line);
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.subwayTabBottomLine.getLayoutParams();
    localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 2.0F);
    localLayoutParams.height = ViewUtils.dip2px(getContext(), 1.0F);
    this.subwayTabBottomLine.setLayoutParams(localLayoutParams);
    paramActivity.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SearchTwinListFilterDialogWithRegionAndSubway.this.onRegionClick();
      }
    });
    paramString.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SearchTwinListFilterDialogWithRegionAndSubway.this.onSubwayClick();
      }
    });
    if (paramNewShopListDataSource.isMetro())
    {
      onSubwayClick();
      return;
    }
    onRegionClick();
  }

  public int currentTadId()
  {
    return this.currentTabId;
  }

  public void onRegionClick()
  {
    if (this.currentTabId == 0);
    do
    {
      return;
      this.currentTabId = 0;
      this.mElementid = "distance";
      this.regionTabBottomLine.setBackgroundColor(getContext().getResources().getColor(R.color.light_line_red));
      LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.regionTabBottomLine.getLayoutParams();
      localLayoutParams.topMargin = 0;
      localLayoutParams.height = ViewUtils.dip2px(getContext(), 3.0F);
      this.regionTabBottomLine.setLayoutParams(localLayoutParams);
      this.subwayTabBottomLine.setBackgroundColor(getContext().getResources().getColor(R.color.line_gray));
      localLayoutParams = (LinearLayout.LayoutParams)this.subwayTabBottomLine.getLayoutParams();
      localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 2.0F);
      localLayoutParams.height = ViewUtils.dip2px(getContext(), 1.0F);
      this.subwayTabBottomLine.setLayoutParams(localLayoutParams);
      setNavTree(this.ds.regionNavTree);
      if (this.ds.isMetro())
        continue;
      setSelectedItem(this.ds.curRegion());
      return;
    }
    while (this.leftAdapter.getCount() <= 0);
    onLeftClick(0);
  }

  public void onSubwayClick()
  {
    if (this.currentTabId == 1);
    do
    {
      return;
      this.currentTabId = 1;
      this.mElementid = "subway";
      this.regionTabBottomLine.setBackgroundColor(getContext().getResources().getColor(R.color.line_gray));
      LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.regionTabBottomLine.getLayoutParams();
      localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 2.0F);
      localLayoutParams.height = ViewUtils.dip2px(getContext(), 1.0F);
      this.subwayTabBottomLine.setBackgroundColor(getContext().getResources().getColor(R.color.light_line_red));
      localLayoutParams = (LinearLayout.LayoutParams)this.subwayTabBottomLine.getLayoutParams();
      localLayoutParams.topMargin = 0;
      localLayoutParams.height = ViewUtils.dip2px(getContext(), 3.0F);
      setNavTree(this.ds.metroNavTree);
      if (this.ds.isMetro())
      {
        setSelectedItem(this.ds.curMetro());
        return;
      }
      Log.d("debug metro", this.leftAdapter.getCount() + "");
    }
    while (this.leftAdapter.getCount() <= 0);
    onLeftClick(0);
  }

  public void setDialogHeightAdaptive(boolean paramBoolean1, boolean paramBoolean2)
  {
    ViewGroup localViewGroup = getFilterViewParent();
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)localViewGroup.getLayoutParams();
    int j;
    if (paramBoolean1)
    {
      localLayoutParams.height = 0;
      int n = ViewUtils.dip2px(getContext(), 45.0F);
      int i = ViewUtils.dip2px(getContext(), 45.0F);
      if ((this.ds.regionNavTree != null) && (this.ds.regionNavTree.getParent(0) != null))
      {
        localLayoutParams.height = (this.ds.regionNavTree.getParent(0).children.size() * n);
        localObject = this.ds.regionNavTree.getParent(0).children.iterator();
        while (((Iterator)localObject).hasNext())
        {
          j = i * ((NavTreeNode)((Iterator)localObject).next()).children.size();
          if (j <= localLayoutParams.height)
            continue;
          localLayoutParams.height = j;
        }
      }
      if ((this.ds.metroNavTree != null) && (this.ds.metroNavTree.getParent(0) != null))
      {
        if (this.ds.metroNavTree.getParent(0).children.size() * n > localLayoutParams.height)
          localLayoutParams.height = (this.ds.metroNavTree.getParent(0).children.size() * n);
        localObject = this.ds.metroNavTree.getParent(0).children.iterator();
        while (((Iterator)localObject).hasNext())
        {
          j = i * ((NavTreeNode)((Iterator)localObject).next()).children.size();
          if (j <= localLayoutParams.height)
            continue;
          localLayoutParams.height = j;
        }
      }
      localLayoutParams.height += ViewUtils.getViewWidth(findViewById(R.id.tab_region)) + ViewUtils.dip2px(getContext(), 3.0F);
      Object localObject = new Rect();
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchTwinListFilterDialogWithRegionAndSubway
 * JD-Core Version:    0.6.0
 */