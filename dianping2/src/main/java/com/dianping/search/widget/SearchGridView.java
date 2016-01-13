package com.dianping.search.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;

public class SearchGridView extends NovaRelativeLayout
  implements CustomGridView.OnItemClickListener, View.OnClickListener
{
  public static final int FOLD_COUNT = 8;
  public static final int ROW_NUMBER = 4;
  private boolean isShowMore = false;
  private Adapter mAdapter;
  private Context mContext;
  private DPObject mDPObject;
  private TextView mFavourView;
  private ImageView mFoldIcon;
  private CustomGridView mGridView;
  private DPObject[] mItems;
  private boolean mMultiSelectable;
  private View mNameCont;
  private TextView mNameView;
  private ArrayList<Integer> mSelPos = new ArrayList();

  public SearchGridView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SearchGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    LayoutInflater.from(paramContext).inflate(R.layout.search_gridlist_filter_item, this, true);
    this.mNameCont = findViewById(R.id.group_title);
    this.mNameView = ((TextView)findViewById(R.id.group_name));
    this.mFavourView = ((TextView)findViewById(R.id.favour_label));
    this.mFoldIcon = ((ImageView)findViewById(R.id.group_down_icon));
    this.mGridView = ((CustomGridView)findViewById(R.id.gridview));
    this.mGridView.setStretchAllColumns(true);
    this.mGridView.setOnItemClickListener(this);
    this.mGridView.setNeedHideDivider(true);
    this.mAdapter = new Adapter(null);
    this.mGridView.setAdapter(this.mAdapter);
    this.mNameCont.setOnClickListener(this);
  }

  public String getFilter()
  {
    StringBuilder localStringBuilder = new StringBuilder("");
    int i = 0;
    while (i < this.mSelPos.size())
    {
      String str = this.mItems[((Integer)this.mSelPos.get(i)).intValue()].getString("ID");
      if (!TextUtils.isEmpty(str))
        localStringBuilder.append(str).append(",");
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public String getFilterName()
  {
    StringBuilder localStringBuilder = new StringBuilder("");
    int i = 0;
    while (i < this.mSelPos.size())
    {
      String str = this.mItems[((Integer)this.mSelPos.get(i)).intValue()].getString("Name");
      if (!TextUtils.isEmpty(str))
        localStringBuilder.append(str).append(";");
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public void hideSep()
  {
    findViewById(R.id.grid_bottom_sep).setVisibility(8);
  }

  public boolean isFilterChanged()
  {
    if (this.mItems != null)
    {
      int i = 0;
      while (i < this.mItems.length)
      {
        boolean bool = this.mItems[i].getBoolean("Selected");
        if (this.mSelPos.contains(Integer.valueOf(i)) != bool)
          return true;
        i += 1;
      }
    }
    return false;
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.group_title)
    {
      if (!this.isShowMore)
      {
        this.isShowMore = true;
        this.mFoldIcon.setImageResource(R.drawable.ic_filter_up);
        this.mAdapter.notifyDataSetChanged();
      }
    }
    else
      return;
    this.isShowMore = false;
    this.mFoldIcon.setImageResource(R.drawable.ic_filter_down);
    this.mAdapter.notifyDataSetChanged();
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    if (this.mSelPos.contains(Integer.valueOf(paramInt)))
      this.mSelPos.remove(Integer.valueOf(paramInt));
    while (true)
    {
      this.mAdapter.notifyDataSetChanged();
      return;
      if (!this.mMultiSelectable)
        this.mSelPos.clear();
      this.mSelPos.add(Integer.valueOf(paramInt));
    }
  }

  public void reset()
  {
    this.mSelPos.clear();
    this.mAdapter.notifyDataSetChanged();
  }

  public void setData(DPObject paramDPObject)
  {
    this.mDPObject = paramDPObject;
    this.mSelPos.clear();
    if (this.mDPObject == null)
      return;
    this.mItems = this.mDPObject.getArray("Items");
    this.mMultiSelectable = this.mDPObject.getBoolean("MultiSelectable");
    this.mNameView.setText(this.mDPObject.getString("Name"));
    this.mAdapter.setData(this.mItems);
    if ((this.mItems != null) && (this.mItems.length > 8))
      this.mFoldIcon.setVisibility(0);
    while (true)
    {
      paramDPObject = this.mDPObject.getString("Label");
      if (TextUtils.isEmpty(paramDPObject))
        break;
      this.mFavourView.setText(paramDPObject);
      this.mFavourView.setVisibility(0);
      return;
      this.mNameCont.setClickable(false);
    }
  }

  public void updateSelFilter()
  {
    if (this.mItems != null)
    {
      int i = 0;
      if (i < this.mItems.length)
      {
        if ((this.mItems[i] != null) && (this.mSelPos.contains(Integer.valueOf(i))))
          this.mItems[i].edit().putBoolean("Selected", true);
        while (true)
        {
          i += 1;
          break;
          this.mItems[i].edit().putBoolean("Selected", false);
        }
      }
    }
  }

  private class Adapter extends BaseAdapter
  {
    private int itemWidth;
    protected DPObject[] tableData = null;

    public Adapter(DPObject[] arg2)
    {
      Object localObject;
      this.tableData = localObject;
      this.itemWidth = ((ViewUtils.getScreenWidthPixels(SearchGridView.this.mContext) - ViewUtils.dip2px(SearchGridView.this.mContext, 62.0F)) / 4);
    }

    public int getCount()
    {
      int i = 8;
      if ((this.tableData == null) || (this.tableData.length == 0))
        i = 0;
      while (true)
      {
        return i;
        if (SearchGridView.this.isShowMore)
          break;
        if (this.tableData.length > 8)
          continue;
        if (this.tableData.length < 4)
          return 4;
        return this.tableData.length;
      }
      return this.tableData.length;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt >= this.tableData.length)
        return null;
      return this.tableData[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public String getItemName(int paramInt)
    {
      if ((paramInt >= this.tableData.length) || (this.tableData[paramInt] == null))
        return " ";
      return this.tableData[paramInt].getString("Name");
    }

    public String getSubName(int paramInt)
    {
      if ((paramInt >= this.tableData.length) || (this.tableData[paramInt] == null))
        return " ";
      return this.tableData[paramInt].getString("Desc");
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      String str1 = getItemName(paramInt);
      String str2 = getSubName(paramInt);
      Object localObject;
      TextView localTextView;
      if (paramInt % 4 == 0)
      {
        paramView = new TableRow(paramViewGroup.getContext());
        localObject = (LinearLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_filter_item, (TableRow)paramView, false);
        paramViewGroup = (TableRow.LayoutParams)((LinearLayout)localObject).getLayoutParams();
        paramViewGroup.width = this.itemWidth;
        ((TableRow)paramView).addView((View)localObject, paramViewGroup);
        paramViewGroup = paramView;
        paramView = (View)localObject;
        if (paramInt >= this.tableData.length)
          paramViewGroup.setVisibility(4);
        localObject = (TextView)paramView.findViewById(R.id.filter_name);
        localTextView = (TextView)paramView.findViewById(R.id.filter_subname);
        ((TextView)localObject).setText(str1);
        if (TextUtils.isEmpty(str2))
          break label247;
        localTextView.setText(str2);
        localTextView.setVisibility(0);
      }
      while (true)
      {
        paramView.setTag(localDPObject);
        if (!SearchGridView.this.mSelPos.contains(Integer.valueOf(paramInt)))
          break label257;
        paramView.setSelected(true);
        return paramViewGroup;
        paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_filter_item, ((CustomGridView)paramViewGroup).getCurRow(), false);
        paramView = (LinearLayout)paramViewGroup;
        localObject = (TableRow.LayoutParams)paramView.getLayoutParams();
        ((TableRow.LayoutParams)localObject).width = this.itemWidth;
        paramView.setLayoutParams((ViewGroup.LayoutParams)localObject);
        break;
        label247: localTextView.setVisibility(8);
      }
      label257: paramView.setSelected(false);
      return (View)paramViewGroup;
    }

    public void setData(DPObject[] paramArrayOfDPObject)
    {
      this.tableData = paramArrayOfDPObject;
      if (this.tableData != null)
      {
        int i = 0;
        while (i < this.tableData.length)
        {
          if ((this.tableData[i] != null) && (this.tableData[i].getBoolean("Selected")))
            SearchGridView.this.mSelPos.add(Integer.valueOf(i));
          i += 1;
        }
      }
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchGridView
 * JD-Core Version:    0.6.0
 */