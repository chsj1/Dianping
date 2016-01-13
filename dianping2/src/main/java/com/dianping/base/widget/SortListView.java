package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.AlphabetBar;

public class SortListView extends RelativeLayout
{
  int lastFirstVisibleItem = -1;
  SortListAdapterWrapper mAdapter;
  private AlphabetBar mIndexBar;
  TextView mKeyHeader;
  private ListView mListView;

  public SortListView(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }

  public SortListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }

  private void init(Context paramContext)
  {
    View.inflate(paramContext, R.layout.sort_list_view, this);
    this.mListView = ((ListView)findViewById(R.id.list));
    this.mIndexBar = ((AlphabetBar)findViewById(R.id.sidebar));
    this.mKeyHeader = ((TextView)findViewById(R.id.header));
    this.mIndexBar.setListView(this.mListView);
    this.mListView.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
        if (SortListView.this.mAdapter == null)
          return;
        paramInt2 = SortListView.this.mAdapter.getSectionForPosition(paramInt1);
        paramInt3 = SortListView.this.mAdapter.getPositionForSection(paramInt2 + 1);
        if (paramInt1 != SortListView.this.lastFirstVisibleItem)
        {
          ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)SortListView.this.mKeyHeader.getLayoutParams();
          localMarginLayoutParams.topMargin = 0;
          SortListView.this.mKeyHeader.setLayoutParams(localMarginLayoutParams);
          SortListView.this.mKeyHeader.setText((String)SortListView.this.mAdapter.getSections()[paramInt2]);
        }
        if (paramInt3 == paramInt1 + 1)
        {
          paramAbsListView = paramAbsListView.getChildAt(0);
          if (paramAbsListView != null)
          {
            paramInt2 = SortListView.this.mKeyHeader.getHeight();
            paramInt3 = paramAbsListView.getBottom();
            paramAbsListView = (ViewGroup.MarginLayoutParams)SortListView.this.mKeyHeader.getLayoutParams();
            if (paramInt3 >= paramInt2)
              break label192;
            paramAbsListView.topMargin = (int)(paramInt3 - paramInt2);
            SortListView.this.mKeyHeader.setLayoutParams(paramAbsListView);
          }
        }
        while (true)
        {
          SortListView.this.lastFirstVisibleItem = paramInt1;
          return;
          label192: if (paramAbsListView.topMargin == 0)
            continue;
          paramAbsListView.topMargin = 0;
          SortListView.this.mKeyHeader.setLayoutParams(paramAbsListView);
        }
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
      }
    });
  }

  public void setAdapter(SortListAdapter paramSortListAdapter)
  {
    this.mAdapter = new SortListAdapterWrapper(paramSortListAdapter);
    this.mListView.setAdapter(this.mAdapter);
    this.mIndexBar.setSectionIndexter(this.mAdapter);
    paramSortListAdapter = this.mIndexBar;
    if (this.mAdapter.needSideBar());
    for (int i = 0; ; i = 4)
    {
      paramSortListAdapter.setVisibility(i);
      return;
    }
  }

  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    this.mListView.setOnItemClickListener(paramOnItemClickListener);
  }

  public static abstract class SortListAdapter extends BaseAdapter
    implements SectionIndexer
  {
    private AlphabetIndexer mIndexer;

    public SortListAdapter(String[] paramArrayOfString, int[] paramArrayOfInt)
    {
      this.mIndexer = new AlphabetIndexer(paramArrayOfString, paramArrayOfInt);
    }

    public int getPositionForSection(int paramInt)
    {
      return this.mIndexer.getPositionForSection(paramInt);
    }

    public int getSectionForPosition(int paramInt)
    {
      return this.mIndexer.getSectionForPosition(paramInt);
    }

    public Object[] getSections()
    {
      return this.mIndexer.getSections();
    }

    public boolean needSideBar()
    {
      return true;
    }
  }

  class SortListAdapterWrapper extends BaseAdapter
    implements SectionIndexer
  {
    SortListView.SortListAdapter adapter;

    public SortListAdapterWrapper(SortListView.SortListAdapter arg2)
    {
      Object localObject;
      this.adapter = localObject;
    }

    public int getCount()
    {
      return this.adapter.getCount();
    }

    public Object getItem(int paramInt)
    {
      return this.adapter.getItem(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return this.adapter.getItemId(paramInt);
    }

    public int getPositionForSection(int paramInt)
    {
      return this.adapter.getPositionForSection(paramInt);
    }

    public int getSectionForPosition(int paramInt)
    {
      return this.adapter.getSectionForPosition(paramInt);
    }

    public Object[] getSections()
    {
      return this.adapter.getSections();
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      paramView = paramViewGroup;
      if (paramViewGroup == null)
        paramView = View.inflate(SortListView.this.getContext(), R.layout.sort_list_item, null);
      paramViewGroup = (TextView)paramView.findViewById(R.id.key);
      FrameLayout localFrameLayout = (FrameLayout)paramView.findViewById(R.id.content);
      if (localFrameLayout.getChildAt(0) != null)
        this.adapter.getView(paramInt, localFrameLayout.getChildAt(0), localFrameLayout);
      while (true)
      {
        int i = getSectionForPosition(paramInt);
        if (paramInt != getPositionForSection(i))
          break;
        paramViewGroup.setText((String)getSections()[i]);
        paramViewGroup.setVisibility(0);
        return paramView;
        localFrameLayout.addView(this.adapter.getView(paramInt, null, localFrameLayout));
      }
      paramViewGroup.setVisibility(8);
      return paramView;
    }

    public boolean needSideBar()
    {
      return this.adapter.needSideBar();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.SortListView
 * JD-Core Version:    0.6.0
 */