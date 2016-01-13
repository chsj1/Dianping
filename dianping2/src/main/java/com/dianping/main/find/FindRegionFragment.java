package com.dianping.main.find;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.base.widget.ExpandAnimation;
import com.dianping.base.widget.ExpandAnimation.OnExpendActionListener;
import com.dianping.base.widget.ExpandAnimation.OnExpendAnimationListener;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;

public class FindRegionFragment extends Fragment
  implements TableView.OnItemClickListener, ExpandAnimation.OnExpendActionListener, ExpandAnimation.OnExpendAnimationListener
{
  private static final int LIST_STATUS_ANIMATE = 1;
  private static final int LIST_STATUS_NORMAL = 0;
  private static final int MAX_CHILD_COUNT = 3;
  private static final int STATUS_EXPAND = 1;
  private static final int STATUS_NORMAL = 0;
  private CategoryAdapter categoryAdapter = new CategoryAdapter(null);
  private ArrayList<DPObject> childCategory;
  private View expandView;
  private View itemView;
  private int mListStatus = 0;
  private int mPosition = -1;
  ArrayList<DPObject> parentCategory;
  private DPObject[] regionList;

  public ArrayList<DPObject> findChildrenRegionWithLimit(int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.childCategory.iterator();
    do
    {
      if (!localIterator.hasNext())
        break;
      DPObject localDPObject = (DPObject)localIterator.next();
      if (localDPObject.getInt("ParentID") != paramInt1)
        continue;
      localArrayList.add(localDPObject);
    }
    while (localArrayList.size() != paramInt2);
    return localArrayList;
  }

  String formatChildrenText(ArrayList<DPObject> paramArrayList, int paramInt)
  {
    if (paramArrayList.size() == 0)
      return null;
    StringBuilder localStringBuilder = new StringBuilder();
    paramInt = 0;
    while (paramInt < paramArrayList.size())
    {
      localStringBuilder.append(((DPObject)paramArrayList.get(paramInt)).getString("Name"));
      if (paramInt != paramArrayList.size() - 1)
        localStringBuilder.append("、");
      paramInt += 1;
    }
    localStringBuilder.append("等");
    return localStringBuilder.toString();
  }

  public void onAnimationEnd()
  {
    this.mListStatus = 0;
  }

  public void onAnimationStart()
  {
    this.mListStatus = 1;
  }

  public void onCreate(Bundle paramBundle)
  {
    this.parentCategory = new ArrayList();
    this.childCategory = new ArrayList();
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = LayoutInflater.from(getActivity()).inflate(R.layout.region_fragment_layout, paramViewGroup, false);
    setData();
    paramViewGroup = (TableView)paramLayoutInflater.findViewById(R.id.region_list);
    paramViewGroup.setDivider(null);
    paramViewGroup.setVerticalScrollBarEnabled(false);
    paramViewGroup.setAdapter(this.categoryAdapter);
    paramViewGroup.setOnItemClickListener(this);
    paramViewGroup.setFocusable(false);
    return paramLayoutInflater;
  }

  public void onExpendAction(View paramView)
  {
    ScrollView localScrollView = ((RegionAndMetroActivity)getActivity()).getScrollView();
    localScrollView.setSmoothScrollingEnabled(true);
    localScrollView.requestChildFocus(paramView, paramView);
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    if (this.mListStatus == 1)
      return;
    if (this.mPosition == paramInt)
    {
      setArrow(this.itemView, 0);
      paramTableView = new ExpandAnimation(this.expandView, 500);
      paramTableView.setOnAnimationListener(this);
      this.expandView.startAnimation(paramTableView);
      this.expandView = null;
      this.mPosition = -1;
      ((DPActivity)getActivity()).statisticsEvent("area5", "area5_moreregion", "收起", 0);
      return;
    }
    if (this.expandView != null)
    {
      setArrow(this.itemView, 0);
      paramTableView = new ExpandAnimation(this.expandView, 500);
      paramTableView.setOnAnimationListener(this);
      this.expandView.startAnimation(paramTableView);
    }
    this.mPosition = paramInt;
    this.expandView = paramView.findViewById(R.id.sub_category_layout);
    this.itemView = paramView;
    setArrow(this.itemView, 1);
    paramTableView = new ExpandAnimation(this.expandView, 500);
    paramTableView.setOnAnimationListener(this);
    paramTableView.setOnExpendActionListener(this);
    this.expandView.startAnimation(paramTableView);
    ((DPActivity)getActivity()).statisticsEvent("area5", "area5_moreregion", "展开", 0);
  }

  public void setArrow(View paramView, int paramInt)
  {
    if (paramInt == 0);
    for (paramInt = R.drawable.home_more_arrow_down; ; paramInt = R.drawable.home_more_arrow_up)
    {
      ((ImageView)paramView.findViewById(R.id.arrow)).setImageResource(paramInt);
      return;
    }
  }

  public void setData()
  {
    if (getActivity() != null)
      this.regionList = ((RegionAndMetroActivity)getActivity()).getRegionList();
    if (this.regionList != null)
    {
      DPObject[] arrayOfDPObject = this.regionList;
      int j = arrayOfDPObject.length;
      int i = 0;
      if (i < j)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        if (localDPObject.getInt("ParentID") == 0)
          this.parentCategory.add(localDPObject);
        while (true)
        {
          i += 1;
          break;
          this.childCategory.add(localDPObject);
        }
      }
    }
    this.categoryAdapter.notifyDataSetChanged();
  }

  private class CategoryAdapter extends BasicAdapter
  {
    private CategoryAdapter()
    {
    }

    public int getCount()
    {
      return FindRegionFragment.this.parentCategory.size();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)FindRegionFragment.this.parentCategory.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return ((DPObject)FindRegionFragment.this.parentCategory.get(paramInt)).getInt("ID");
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = getItem(paramInt);
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = LayoutInflater.from(FindRegionFragment.this.getActivity()).inflate(R.layout.more_regionmetro_item, null);
      ((TextView)paramViewGroup.findViewById(R.id.parent_category)).setText(((DPObject)localObject1).getString("Name"));
      Object localObject2 = (TextView)paramViewGroup.findViewById(R.id.child_category);
      Object localObject3 = FindRegionFragment.this.findChildrenRegionWithLimit(((DPObject)localObject1).getInt("ID"), 3);
      paramView = (CustomGridView)paramViewGroup.findViewById(R.id.category_grid);
      ArrayList localArrayList = FindRegionFragment.this.findChildrenRegionWithLimit(((DPObject)localObject1).getInt("ID"), 2147483647);
      localObject3 = FindRegionFragment.this.formatChildrenText((ArrayList)localObject3, localArrayList.size());
      if (!TextUtils.isEmpty((CharSequence)localObject3))
        ((TextView)localObject2).setText(Html.fromHtml((String)localObject3));
      while (true)
      {
        localArrayList.add(0, ((DPObject)localObject1).edit().putString("Name", "全部" + ((DPObject)localObject1).getString("Name")).generate());
        localObject1 = new FindRegionFragment.SubCategoryAdapter(FindRegionFragment.this, localArrayList);
        paramView.setAdapter((Adapter)localObject1);
        paramView.setOnItemClickListener(new CustomGridView.OnItemClickListener()
        {
          public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
          {
            paramCustomGridView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://regionshoplist?categoryid=10&regionid=" + paramLong));
            FindRegionFragment.this.startActivity(paramCustomGridView);
            ((DPActivity)FindRegionFragment.this.getActivity()).statisticsEvent("area5", "area5_moreregion_select", null, 0);
          }
        });
        paramView = paramViewGroup.findViewById(R.id.sub_category_layout);
        localObject2 = (LinearLayout.LayoutParams)paramView.getLayoutParams();
        paramInt = (int)Math.ceil(((FindRegionFragment.SubCategoryAdapter)localObject1).getCount() / 3.0D);
        ((LinearLayout.LayoutParams)localObject2).bottomMargin = (-(ViewUtils.dip2px(FindRegionFragment.this.getActivity(), 42.0F) * paramInt + paramInt));
        paramView.setLayoutParams((ViewGroup.LayoutParams)localObject2);
        return paramViewGroup;
        ((TextView)localObject2).setVisibility(8);
      }
    }
  }

  private class SubCategoryAdapter extends BasicAdapter
  {
    private int columnCount = 3;
    private ArrayList<DPObject> subCategory;

    public SubCategoryAdapter()
    {
      Object localObject;
      this.subCategory = localObject;
    }

    public int getCount()
    {
      return this.subCategory.size();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)this.subCategory.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return ((DPObject)this.subCategory.get(paramInt)).getInt("ID");
    }

    public String getItemName(int paramInt)
    {
      return getItem(paramInt).getString("Name");
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      String str = getItemName(paramInt);
      if (paramInt % this.columnCount == 0)
      {
        paramView = new TableRow(paramViewGroup.getContext());
        paramViewGroup = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_conditions_item, (TableRow)paramView, false);
        paramViewGroup.setText(str);
        ((TableRow)paramView).addView(paramViewGroup);
      }
      while (true)
      {
        if (paramInt == getCount() - 1)
        {
          if (str == null)
            paramViewGroup.setVisibility(4);
          paramViewGroup = (TableRow.LayoutParams)paramView.getLayoutParams();
          paramViewGroup.span = ((this.columnCount - getCount() % this.columnCount) % this.columnCount + 1);
          paramView.setLayoutParams(paramViewGroup);
        }
        return paramView;
        paramViewGroup = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_conditions_item, ((CustomGridView)paramViewGroup).getCurRow(), false);
        paramView = paramViewGroup;
        paramViewGroup.setText(str);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.FindRegionFragment
 * JD-Core Version:    0.6.0
 */