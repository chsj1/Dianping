package com.dianping.base.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout
  implements View.OnClickListener
{
  Adapter adapter;
  LinearLayout curSubLinearLayout;
  Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what != 1)
        return;
      if ((CustomLinearLayout.this.adapter == null) || (CustomLinearLayout.this.adapter.isEmpty()))
      {
        CustomLinearLayout.this.removeAllViews();
        return;
      }
      CustomLinearLayout.this.removeAllViews();
      int i = 0;
      while (true)
      {
        try
        {
          if (i >= CustomLinearLayout.this.adapter.getCount())
            break;
          paramMessage = CustomLinearLayout.this.adapter.getView(i, null, CustomLinearLayout.this);
          if (!(paramMessage instanceof LinearLayout))
            continue;
          if ((CustomLinearLayout.this.maxLine > 0) && (CustomLinearLayout.this.lineCount >= CustomLinearLayout.this.maxLine))
            break;
          CustomLinearLayout.this.addView(paramMessage);
          CustomLinearLayout.this.curSubLinearLayout = ((LinearLayout)paramMessage);
          CustomLinearLayout.access$104(CustomLinearLayout.this);
          break label180;
          if ((paramMessage != null) && (CustomLinearLayout.this.curSubLinearLayout != null))
            CustomLinearLayout.this.curSubLinearLayout.addView(paramMessage);
        }
        catch (java.lang.Exception paramMessage)
        {
          return;
        }
        label180: i += 1;
      }
    }
  };
  private int lineCount = 0;
  private OnItemClickListener mClickListener;
  private int maxLine = -1;
  private final DataSetObserver observer = new DataSetObserver()
  {
    public void onChanged()
    {
      CustomLinearLayout.this.handler.removeMessages(1);
      CustomLinearLayout.this.handler.sendEmptyMessageDelayed(1, 100L);
    }

    public void onInvalidated()
    {
      onChanged();
    }
  };

  public CustomLinearLayout(Context paramContext)
  {
    super(paramContext);
  }

  public CustomLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setChildOnClickListener(View paramView)
  {
    if ((paramView != null) && (paramView.getVisibility() == 0) && (paramView.isClickable()))
      paramView.setOnClickListener(this);
  }

  public Adapter getAdapter()
  {
    return this.adapter;
  }

  public LinearLayout getCurSubLinearLayout()
  {
    return this.curSubLinearLayout;
  }

  public void init()
  {
    this.lineCount = 0;
  }

  public void onClick(View paramView)
  {
    int n;
    long l1;
    int k;
    if (this.mClickListener != null)
    {
      n = -1;
      l1 = -1L;
      k = 0;
    }
    int j;
    for (int i = 0; ; i = j)
    {
      j = n;
      Object localObject;
      if (k < getChildCount())
      {
        localObject = getChildAt(k);
        if (paramView == localObject)
          j = i;
      }
      else
      {
        if (j >= 0)
          break;
        return;
      }
      if ((localObject instanceof LinearLayout))
      {
        localObject = (LinearLayout)localObject;
        int m = 0;
        while (true)
        {
          j = i;
          if (m >= ((LinearLayout)localObject).getChildCount())
            break label115;
          if (paramView == ((LinearLayout)localObject).getChildAt(m))
          {
            j = i;
            break;
          }
          m += 1;
          i += 1;
        }
      }
      j = i + 1;
      label115: k += 1;
    }
    if (this.adapter != null)
      l1 = this.adapter.getItemId(j);
    long l2 = l1;
    if (l1 == -1L)
      l2 = paramView.getId();
    this.mClickListener.onItemClick(this, paramView, j, l2);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = 0;
    while (paramInt1 < getChildCount())
    {
      Object localObject = getChildAt(paramInt1);
      if ((localObject instanceof LinearLayout))
      {
        localObject = (LinearLayout)localObject;
        paramInt2 = 0;
        while (paramInt2 < ((LinearLayout)localObject).getChildCount())
        {
          setChildOnClickListener(((LinearLayout)localObject).getChildAt(paramInt2));
          paramInt2 += 1;
        }
      }
      setChildOnClickListener((View)localObject);
      paramInt1 += 1;
    }
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

  public void setMaxLine(int paramInt)
  {
    this.maxLine = paramInt;
  }

  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
  {
    this.mClickListener = paramOnItemClickListener;
  }

  public static abstract interface OnItemClickListener
  {
    public abstract void onItemClick(LinearLayout paramLinearLayout, View paramView, int paramInt, long paramLong);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CustomLinearLayout
 * JD-Core Version:    0.6.0
 */