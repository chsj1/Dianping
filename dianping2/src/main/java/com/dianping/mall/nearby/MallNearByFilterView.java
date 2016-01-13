package com.dianping.mall.nearby;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ExtendableListView.LayoutParams;
import com.dianping.locationservice.impl286.util.CommonUtil;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class MallNearByFilterView extends LinearLayout
{
  protected View dividerLine;
  private MallNearByFilterBar filterBar;
  private MallNearByDataSource mallNearByDataSource;
  private NovaButton selectedTagView;
  protected View.OnClickListener tagClickListener = new MallNearByFilterView.2(this);
  protected LinearLayout tagFilterBar;
  protected HorizontalScrollView tagFilterBarContainer;

  public MallNearByFilterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    initView();
  }

  protected View createTagNaviItem(DPObject paramDPObject)
  {
    NovaButton localNovaButton = (NovaButton)LayoutInflater.from(getContext()).inflate(R.layout.mall_tag_navi_item, null);
    Object localObject = new LinearLayout.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams)localObject).setMargins(CommonUtil.dip2px(getContext(), 6.0F), 0, CommonUtil.dip2px(getContext(), 6.0F), 0);
    localNovaButton.setLayoutParams((ViewGroup.LayoutParams)localObject);
    int i = paramDPObject.getInt("Count");
    String str = paramDPObject.getString("Name");
    localObject = str;
    if (i > 0)
      localObject = str + String.format("(%d)", new Object[] { Integer.valueOf(i) });
    localNovaButton.setText((CharSequence)localObject);
    localNovaButton.setTag(paramDPObject);
    localNovaButton.setOnClickListener(this.tagClickListener);
    if (paramDPObject.getString("Name").equals(this.mallNearByDataSource.curCategory().getString("Name")))
    {
      localNovaButton.setSelected(true);
      this.selectedTagView = localNovaButton;
    }
    while (true)
    {
      localNovaButton.setGravity(17);
      localNovaButton.setClickable(true);
      localNovaButton.setGAString("cat1", (String)localObject);
      localNovaButton.gaUserInfo.title = paramDPObject.getString("Name");
      localNovaButton.gaUserInfo.category_id = Integer.valueOf(paramDPObject.getInt("ID"));
      return localNovaButton;
      localNovaButton.setSelected(false);
    }
  }

  public MallNearByFilterBar getFilterBar()
  {
    return this.filterBar;
  }

  public void initTagFilter()
  {
    this.tagFilterBarContainer = new HorizontalScrollView(getContext());
    Object localObject = new ViewGroup.LayoutParams(-1, CommonUtil.dip2px(getContext(), 50.0F));
    this.tagFilterBarContainer.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.tagFilterBarContainer.setHorizontalScrollBarEnabled(false);
    this.tagFilterBarContainer.setBackgroundColor(getContext().getResources().getColor(R.color.common_bk_color));
    this.tagFilterBar = new LinearLayout(getContext());
    this.tagFilterBar.setOrientation(0);
    localObject = new ViewGroup.LayoutParams(-2, -1);
    this.tagFilterBar.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.tagFilterBar.setPadding(CommonUtil.dip2px(getContext(), 4.0F), CommonUtil.dip2px(getContext(), 10.0F), 0, CommonUtil.dip2px(getContext(), 10.0F));
    this.tagFilterBarContainer.addView(this.tagFilterBar);
    this.tagFilterBarContainer.setVisibility(8);
    addView(this.tagFilterBarContainer);
    this.dividerLine = new View(getContext());
    localObject = new ExtendableListView.LayoutParams(-1, 1);
    this.dividerLine.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.dividerLine.setBackgroundColor(getContext().getResources().getColor(R.color.line_gray));
    this.dividerLine.setVisibility(8);
    addView(this.dividerLine);
  }

  public void initView()
  {
    this.filterBar = new MallNearByFilterBar(getContext(), null);
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, CommonUtil.dip2px(getContext(), 45.0F));
    this.filterBar.setLayoutParams(localLayoutParams);
    addView(this.filterBar);
    initTagFilter();
  }

  public void setMallNearByDataSource(MallNearByDataSource paramMallNearByDataSource)
  {
    this.mallNearByDataSource = paramMallNearByDataSource;
    this.filterBar.setMallNearByDataSource(paramMallNearByDataSource);
  }

  public void updateFilter()
  {
    this.filterBar.updateFilter();
    updateTagFilter();
  }

  public void updateTagFilter()
  {
    DPObject[] arrayOfDPObject = this.mallNearByDataSource.filterCategories();
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      this.tagFilterBar.removeAllViews();
      int i = 0;
      while (true)
      {
        View localView;
        if (i < arrayOfDPObject.length)
        {
          localView = createTagNaviItem(arrayOfDPObject[i]);
          if (localView != null);
        }
        else
        {
          this.tagFilterBarContainer.requestLayout();
          if ((this.selectedTagView != null) && ((this.selectedTagView instanceof NovaButton)))
            this.tagFilterBarContainer.post(new MallNearByFilterView.1(this));
          this.tagFilterBarContainer.setVisibility(0);
          this.dividerLine.setVisibility(0);
          return;
        }
        this.tagFilterBar.addView(localView);
        i += 1;
      }
    }
    this.tagFilterBarContainer.setVisibility(8);
    this.dividerLine.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.mall.nearby.MallNearByFilterView
 * JD-Core Version:    0.6.0
 */