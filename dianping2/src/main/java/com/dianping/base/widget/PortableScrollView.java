package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.dianping.base.adapter.PortableScrollViewAdapter;
import com.dianping.v1.R.layout;
import java.util.HashMap;

public class PortableScrollView extends ScrollView
{
  public static final int INDEX = 1073741823;
  public static final int TAG = 1073741843;
  private final int HEIGHT = 1073741833;
  private PortableScrollViewAdapter adapter;
  private LinearLayout containLayout;
  private int currentIndex = -1;
  private HashMap<Object, View> hashMap = new HashMap();
  private OnItemOperationListener onItemOperationListener;

  public PortableScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void calculateHeight()
  {
    int j = 0;
    int i = 0;
    while (i < getAllChildCount())
    {
      View localView = getChildViewByIndex(i);
      localView.setTag(1073741833, Integer.valueOf(j));
      j += localView.getMeasuredHeight();
      i += 1;
    }
  }

  private void init()
  {
    this.containLayout = ((LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.portable_linearlayout, null));
    addView(this.containLayout);
  }

  private void initView()
  {
    int i = 0;
    while (i < this.adapter.getCount())
    {
      View localView = this.adapter.getView(i);
      addChildView(localView, localView.getTag(1073741843));
      i += 1;
    }
    postDelayed(new Runnable()
    {
      public void run()
      {
        PortableScrollView.this.calculateHeight();
      }
    }
    , 300L);
  }

  private void scrollView(View paramView)
  {
    if ((paramView == null) || (paramView.getTag(1073741833) == null));
    Integer localInteger;
    do
    {
      return;
      localInteger = (Integer)paramView.getTag(1073741833);
      if (getScrollY() <= localInteger.intValue())
        continue;
      scrollTo(0, localInteger.intValue());
    }
    while (localInteger.intValue() + paramView.getMeasuredHeight() <= getScrollY() + getMeasuredHeight());
    scrollTo(0, localInteger.intValue() - (getMeasuredHeight() - paramView.getMeasuredHeight()));
  }

  public void addChildView(View paramView, Object paramObject)
  {
    paramView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        int i = ((Integer)paramView.getTag(1073741823)).intValue();
        if ((PortableScrollView.this.setSelectedIndex(i)) && (PortableScrollView.this.onItemOperationListener != null))
          PortableScrollView.this.onItemOperationListener.clickItem(paramView, i);
      }
    });
    if (paramObject != null)
      this.hashMap.put(paramObject, paramView);
    paramView.setTag(1073741823, Integer.valueOf(getAllChildCount()));
    this.containLayout.addView(paramView);
  }

  public PortableScrollViewAdapter getAdapter()
  {
    return this.adapter;
  }

  public int getAllChildCount()
  {
    if (this.containLayout == null)
      return 0;
    return this.containLayout.getChildCount();
  }

  public View getChildViewByIndex(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > getAllChildCount() - 1))
      return null;
    return this.containLayout.getChildAt(paramInt);
  }

  public View getChildViewByTag(Object paramObject)
  {
    return (View)this.hashMap.get(paramObject);
  }

  public void notifyDataSetChanged()
  {
    resetView();
    initView();
  }

  public void resetView()
  {
    this.currentIndex = -1;
    this.hashMap.clear();
    this.containLayout.removeAllViews();
  }

  public void setAdapter(PortableScrollViewAdapter paramPortableScrollViewAdapter)
  {
    this.adapter = paramPortableScrollViewAdapter;
    if (paramPortableScrollViewAdapter != null)
    {
      initView();
      return;
    }
    resetView();
  }

  public void setOnChangeSelectedItemListener(OnItemOperationListener paramOnItemOperationListener)
  {
    this.onItemOperationListener = paramOnItemOperationListener;
  }

  public boolean setSelectedIndex(int paramInt)
  {
    if (getAllChildCount() == 0);
    do
      return false;
    while (this.currentIndex == paramInt);
    View localView2;
    if (this.onItemOperationListener != null)
    {
      localView2 = getChildViewByIndex(this.currentIndex);
      if (paramInt >= 0)
        break label62;
    }
    label62: for (View localView1 = null; ; localView1 = getChildViewByIndex(paramInt))
    {
      scrollView(localView1);
      this.onItemOperationListener.selectNewItem(localView2, localView1);
      this.currentIndex = paramInt;
      return true;
    }
  }

  public static abstract interface OnItemOperationListener
  {
    public abstract void clickItem(View paramView, int paramInt);

    public abstract void selectNewItem(View paramView1, View paramView2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.PortableScrollView
 * JD-Core Version:    0.6.0
 */