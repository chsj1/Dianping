package com.dianping.base.widget.dialogfilter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Map;

public class TwinListFilterDialog extends FilterDialog
{
  static final String FILE_NAME = "Name";
  Drawable defaultIcon;
  boolean displayIcon = true;
  boolean hasAll = true;
  protected DPObject headerItem;
  protected int headerItemCount;
  Map<Integer, Drawable> icons;
  private DPObject[] items;
  protected LeftAdapter leftAdapter;
  final AdapterView.OnItemClickListener leftHandler = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      TwinListFilterDialog.this.onLeftClick(paramInt);
    }
  };
  protected ListView leftList;
  protected String mElementid;
  protected RightAdapter rightAdapter;
  final AdapterView.OnItemClickListener rightHandler = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      TwinListFilterDialog.this.onRightClick(paramInt);
    }
  };
  protected ListView rightList;
  protected DPObject selectedItem;

  public TwinListFilterDialog(Activity paramActivity)
  {
    this(paramActivity, null);
  }

  public TwinListFilterDialog(Activity paramActivity, String paramString)
  {
    this(paramActivity, paramString, R.layout.twin_list_filter);
  }

  public TwinListFilterDialog(Activity paramActivity, String paramString, int paramInt)
  {
    super(paramActivity);
    paramActivity = getLayoutInflater().inflate(paramInt, getFilterViewParent(), false);
    this.leftList = ((ListView)paramActivity.findViewById(R.id.left));
    this.rightList = ((ListView)paramActivity.findViewById(R.id.right));
    this.leftAdapter = new LeftAdapter();
    this.rightAdapter = new RightAdapter();
    this.leftList.setAdapter(this.leftAdapter);
    this.rightList.setAdapter(this.rightAdapter);
    this.leftList.setOnItemClickListener(this.leftHandler);
    this.rightList.setOnItemClickListener(this.rightHandler);
    setFilterView(paramActivity);
    this.mElementid = paramString;
  }

  protected boolean checkEquals(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    return (paramDPObject1 != null) && (paramDPObject2 != null) && (paramDPObject1.getInt("ID") == paramDPObject2.getInt("ID"));
  }

  public DPObject[] getItems()
  {
    return this.items;
  }

  protected String getNums(Object paramObject)
  {
    String str = "";
    if (DPObjectUtils.isDPObjectof(paramObject, "Category"))
      str = "" + ((DPObject)paramObject).getInt("Count");
    while (true)
    {
      paramObject = str;
      if ("0".equalsIgnoreCase(str))
        paramObject = "";
      return paramObject;
      if (!DPObjectUtils.isDPObjectof(paramObject, "Region"))
        continue;
      str = "" + ((DPObject)paramObject).getInt("Count");
    }
  }

  public View getRightView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    TextView localTextView;
    DPObject localDPObject;
    if (paramView == null)
    {
      paramView = (NovaLinearLayout)getLayoutInflater().inflate(R.layout.filter_item, paramViewGroup, false);
      localTextView = (TextView)paramView.findViewById(16908308);
      localDPObject = this.rightAdapter.getItem(paramInt);
      String str = localDPObject.getString("Name");
      paramViewGroup = str;
      if (localDPObject == this.leftAdapter.selected)
      {
        paramViewGroup = str;
        if (!str.startsWith("全部"))
          paramViewGroup = "全部" + str;
      }
      localTextView.setText(paramViewGroup);
      paramView.setGAString(this.mElementid, paramViewGroup);
      if (!checkEquals(this.selectedItem, localDPObject))
        break label180;
      localTextView.setTextColor(getActivity().getResources().getColor(R.color.yellow_1));
      paramView.setBackgroundResource(R.drawable.filter_sub_selected);
    }
    while (true)
    {
      ((TextView)paramView.findViewById(16908309)).setText(getNums(localDPObject));
      return paramView;
      paramView = (NovaLinearLayout)paramView;
      break;
      label180: localTextView.setTextColor(getActivity().getResources().getColor(R.color.black));
      paramView.setBackgroundResource(R.drawable.filter_sub_list_item);
    }
  }

  public DPObject getSelectedItem()
  {
    return this.selectedItem;
  }

  public void onLeftClick(int paramInt)
  {
    DPObject localDPObject = this.leftAdapter.getItem(paramInt);
    setParentID(localDPObject.getInt("ID"));
    if (this.rightAdapter.getCount() == 0)
    {
      if (this.listener != null)
        this.listener.onFilter(this, localDPObject);
      return;
    }
    this.leftAdapter.notifyDataSetChanged();
    this.rightAdapter.notifyDataSetChanged();
  }

  public void onRightClick(int paramInt)
  {
    if (this.listener != null)
    {
      DPObject localDPObject = this.rightAdapter.getItem(paramInt);
      this.listener.onFilter(this, localDPObject);
    }
  }

  public void setDisplayIcon(boolean paramBoolean)
  {
    this.displayIcon = paramBoolean;
  }

  public void setHasAll(boolean paramBoolean)
  {
    this.hasAll = paramBoolean;
  }

  public void setHeaderItem(DPObject paramDPObject)
  {
    this.headerItem = paramDPObject;
    if (DPObjectUtils.isDPObjectof(this.headerItem, "Category"))
      this.headerItem = this.headerItem.edit().putInt("Count", this.headerItemCount).generate();
    this.leftAdapter.notifyDataSetChanged();
    this.rightAdapter.notifyDataSetChanged();
  }

  public void setItems(DPObject[] paramArrayOfDPObject)
  {
    this.items = paramArrayOfDPObject;
    this.headerItemCount = 0;
    ArrayList localArrayList = new ArrayList();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      if (localDPObject.getInt("ParentID") == 0)
      {
        localArrayList.add(localDPObject);
        this.headerItemCount += localDPObject.getInt("Count");
      }
      i += 1;
    }
    if (DPObjectUtils.isDPObjectof(this.headerItem, "Category"))
      this.headerItem = this.headerItem.edit().putInt("Count", this.headerItemCount).generate();
    this.leftAdapter.parents = ((DPObject[])localArrayList.toArray(new DPObject[0]));
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
    ArrayList localArrayList;
    Object localObject1;
    do
    {
      do
        return;
      while (this.items == null);
      localArrayList = new ArrayList();
      localObject1 = null;
      DPObject[] arrayOfDPObject = this.items;
      int j = arrayOfDPObject.length;
      int i = 0;
      if (i >= j)
        continue;
      DPObject localDPObject = arrayOfDPObject[i];
      int k = localDPObject.getInt("ParentID");
      Object localObject2;
      if (k != 0)
      {
        localObject2 = localObject1;
        if (k == paramInt)
        {
          localArrayList.add(localDPObject);
          localObject2 = localObject1;
        }
      }
      while (true)
      {
        i += 1;
        localObject1 = localObject2;
        break;
        localObject2 = localObject1;
        if (localDPObject.getInt("ID") != paramInt)
          continue;
        localObject2 = localObject1;
        if (this.selectedItem.getInt("ProductCategoryID") != localDPObject.getInt("ProductCategoryID"))
          continue;
        localObject2 = localDPObject;
      }
    }
    while (localObject1 == null);
    this.leftAdapter.selected = localObject1;
    this.rightAdapter.childs = ((DPObject[])localArrayList.toArray(new DPObject[0]));
  }

  public void setSelectedItem(DPObject paramDPObject)
  {
    this.selectedItem = paramDPObject;
    int i;
    if (paramDPObject != null)
    {
      i = paramDPObject.getInt("ParentID");
      if (i != 0)
        break label46;
      setParentID(paramDPObject.getInt("ID"));
    }
    while (true)
    {
      this.leftAdapter.notifyDataSetChanged();
      this.rightAdapter.notifyDataSetChanged();
      return;
      label46: setParentID(i);
    }
  }

  public class LeftAdapter extends BaseAdapter
  {
    public DPObject[] parents;
    public DPObject selected;

    public LeftAdapter()
    {
    }

    public int getCount()
    {
      int j = 0;
      int i;
      if (TwinListFilterDialog.this.headerItem == null)
      {
        i = 0;
        if (this.parents != null)
          break label30;
      }
      while (true)
      {
        return i + j;
        i = 1;
        break;
        label30: j = this.parents.length;
      }
    }

    public DPObject getItem(int paramInt)
    {
      if (TwinListFilterDialog.this.headerItem == null)
        return this.parents[paramInt];
      if (paramInt == 0)
        return TwinListFilterDialog.this.headerItem;
      return this.parents[(paramInt - 1)];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = null;
      DPObject localDPObject;
      ImageView localImageView;
      if (paramView == null)
      {
        paramViewGroup = (LinearLayout)TwinListFilterDialog.this.getLayoutInflater().inflate(R.layout.filter_main_item, paramViewGroup, false);
        localDPObject = getItem(paramInt);
        ((TextView)paramViewGroup.findViewById(16908308)).setText(localDPObject.getString("Name"));
        ((TextView)paramViewGroup.findViewById(16908309)).setText(TwinListFilterDialog.this.getNums(localDPObject));
        localImageView = (ImageView)paramViewGroup.findViewById(16908294);
        if ((!localDPObject.isClass("Category")) || (!TwinListFilterDialog.this.displayIcon))
          break label153;
        localImageView.setImageResource(NovaConfigUtils.getCategoryIconId(localDPObject.getInt("ID")));
        localImageView.setVisibility(0);
        label125: if (localDPObject != this.selected)
          break label263;
      }
      label263: for (paramInt = R.color.white; ; paramInt = R.drawable.filter_main_list_item)
      {
        paramViewGroup.setBackgroundResource(paramInt);
        return paramViewGroup;
        paramViewGroup = (LinearLayout)paramView;
        break;
        label153: if ((TwinListFilterDialog.this.icons == null) && (TwinListFilterDialog.this.defaultIcon == null))
        {
          localImageView.setVisibility(8);
          localImageView.setImageDrawable(null);
          break label125;
        }
        localImageView.setVisibility(0);
        if (TwinListFilterDialog.this.icons == null);
        for (paramView = (View)localObject; ; paramView = (Drawable)TwinListFilterDialog.this.icons.get(Integer.valueOf(localDPObject.getInt("ID"))))
        {
          localObject = paramView;
          if (paramView == null)
            localObject = TwinListFilterDialog.this.defaultIcon;
          localImageView.setImageDrawable((Drawable)localObject);
          break;
        }
      }
    }
  }

  public class RightAdapter extends BaseAdapter
  {
    public DPObject[] childs;

    public RightAdapter()
    {
    }

    public int getCount()
    {
      int j = 0;
      int i;
      if ((TwinListFilterDialog.this.leftAdapter.selected == null) || (!TwinListFilterDialog.this.hasAll))
      {
        i = 0;
        if (this.childs != null)
          break label43;
      }
      while (true)
      {
        return i + j;
        i = 1;
        break;
        label43: j = this.childs.length;
      }
    }

    public DPObject getItem(int paramInt)
    {
      if ((TwinListFilterDialog.this.leftAdapter.selected == null) || (!TwinListFilterDialog.this.hasAll))
        return this.childs[paramInt];
      if (paramInt == 0)
        return TwinListFilterDialog.this.leftAdapter.selected;
      return this.childs[(paramInt - 1)];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return TwinListFilterDialog.this.getRightView(paramInt, paramView, paramViewGroup);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.dialogfilter.TwinListFilterDialog
 * JD-Core Version:    0.6.0
 */