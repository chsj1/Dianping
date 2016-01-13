package com.dianping.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;
import java.util.LinkedList;

public class LiteListView extends LinearLayout
  implements View.OnFocusChangeListener, View.OnClickListener, View.OnLongClickListener
{
  static final Object DIVIDER_TAG = new Object();
  Adapter adapter;
  Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what != 1)
        return;
      if ((LiteListView.this.adapter == null) || (LiteListView.this.adapter.isEmpty()))
      {
        LiteListView.this.removeAllViews();
        return;
      }
      LinkedList localLinkedList = new LinkedList();
      paramMessage = new LinkedList();
      int i = 0;
      int j = Math.min(LiteListView.this.getChildCount(), LiteListView.this.adapter.getCount() * 2 - 1);
      View localView;
      if (i < j)
      {
        localView = LiteListView.this.getChildAt(i);
        if (localView.getTag() == LiteListView.DIVIDER_TAG)
          paramMessage.addLast(localView);
        while (true)
        {
          i += 1;
          break;
          localLinkedList.addLast(localView);
        }
      }
      LiteListView.this.removeAllViews();
      if ((LiteListView.this.adapter instanceof BaseAdapter))
      {
        paramMessage = (BaseAdapter)LiteListView.this.adapter;
        label169: i = 0;
        int k = LiteListView.this.adapter.getCount();
        label186: if (i < k)
        {
          if ((paramMessage != null) && (!paramMessage.isEnabled(i)))
            break label320;
          j = 1;
          label209: if (!localLinkedList.isEmpty())
            break label326;
          localView = null;
          label218: localView = LiteListView.this.adapter.getView(i, localView, LiteListView.this);
          if (j == 0)
            break label348;
          localView.setOnFocusChangeListener(LiteListView.this);
          localView.setFocusable(true);
          localView.setClickable(true);
          LiteListView.this.setBackground(localView, true);
          localView.setOnClickListener(LiteListView.this);
          if (LiteListView.this.onLongClickListener != null)
            break label337;
          localView.setOnLongClickListener(null);
          localView.setLongClickable(false);
        }
      }
      while (true)
      {
        LiteListView.this.addView(localView);
        i += 1;
        break label186;
        break;
        paramMessage = null;
        break label169;
        label320: j = 0;
        break label209;
        label326: localView = (View)localLinkedList.removeFirst();
        break label218;
        label337: localView.setOnLongClickListener(LiteListView.this);
        continue;
        label348: localView.setOnFocusChangeListener(null);
        localView.setOnClickListener(null);
        localView.setOnLongClickListener(null);
        localView.setSelected(false);
        localView.setFocusable(false);
        localView.setClickable(false);
        LiteListView.this.setBackground(localView, false);
        localView.setLongClickable(false);
      }
    }
  };
  private final DataSetObserver observer = new DataSetObserver()
  {
    public void onChanged()
    {
      LiteListView.this.handler.removeMessages(1);
      LiteListView.this.handler.sendEmptyMessageDelayed(1, 100L);
    }

    public void onInvalidated()
    {
      onChanged();
    }
  };
  private AdapterView.OnItemClickListener onClickListener;
  AdapterView.OnItemLongClickListener onLongClickListener;
  private boolean showDefaultItemBg = true;

  public LiteListView(Context paramContext)
  {
    this(paramContext, null);
  }

  public LiteListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.LiteListView).recycle();
    setOrientation(1);
  }

  public Adapter getAdapter()
  {
    return this.adapter;
  }

  public void onClick(View paramView)
  {
    long l;
    int j;
    int i;
    int k;
    View localView;
    if (this.onClickListener != null)
    {
      int m = -1;
      l = -1L;
      j = 0;
      i = 0;
      int n = getChildCount();
      k = m;
      if (j < n)
      {
        localView = getChildAt(j);
        if (localView.getTag() != DIVIDER_TAG);
      }
    }
    while (true)
    {
      j += 1;
      break;
      if (localView == paramView)
      {
        k = i;
        if (k >= 0)
          break label84;
        return;
      }
      i += 1;
    }
    label84: if (this.adapter != null)
      l = this.adapter.getItemId(k);
    this.onClickListener.onItemClick(null, paramView, k, l);
  }

  public void onFocusChange(View paramView, boolean paramBoolean)
  {
    paramView.setSelected(paramBoolean);
  }

  public boolean onLongClick(View paramView)
  {
    long l;
    int j;
    int i;
    int k;
    View localView;
    if (this.onLongClickListener != null)
    {
      int m = -1;
      l = -1L;
      j = 0;
      i = 0;
      int n = getChildCount();
      k = m;
      if (j < n)
      {
        localView = getChildAt(j);
        if (localView.getTag() != DIVIDER_TAG);
      }
    }
    while (true)
    {
      j += 1;
      break;
      if (localView == paramView)
      {
        k = i;
        if (k >= 0)
          break label85;
        return false;
      }
      i += 1;
    }
    label85: if (this.adapter != null)
      l = this.adapter.getItemId(k);
    return this.onLongClickListener.onItemLongClick(null, paramView, k, l);
  }

  public void setAdapter(Adapter paramAdapter)
  {
    if (this.adapter != null)
      this.adapter.unregisterDataSetObserver(this.observer);
    this.adapter = paramAdapter;
    if (this.adapter != null)
      this.adapter.registerDataSetObserver(this.observer);
    removeAllViews();
    this.observer.onChanged();
  }

  protected void setBackground(View paramView, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      paramView.setBackgroundDrawable(null);
      return;
    }
    int i = paramView.getPaddingLeft();
    int j = paramView.getPaddingRight();
    int k = paramView.getPaddingTop();
    int m = paramView.getPaddingBottom();
    if (this.showDefaultItemBg)
      paramView.setBackgroundResource(R.drawable.list_item_no_gradual);
    paramView.setPadding(i, k, j, m);
  }

  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    this.onClickListener = paramOnItemClickListener;
    this.handler.sendEmptyMessage(1);
  }

  public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener paramOnItemLongClickListener)
  {
    this.onLongClickListener = paramOnItemLongClickListener;
    this.handler.sendEmptyMessage(1);
  }

  public void setShowDefaultBg(boolean paramBoolean)
  {
    this.showDefaultItemBg = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.LiteListView
 * JD-Core Version:    0.6.0
 */