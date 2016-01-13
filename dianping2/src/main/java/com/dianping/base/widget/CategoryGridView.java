package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.dianping.v1.R.drawable;

public class CategoryGridView extends TableLayout
  implements View.OnClickListener
{
  Adapter adapter;
  TableRow curRow;
  private Drawable endHorizontalDivider = getResources().getDrawable(R.drawable.grid_horizontal_unselected);
  Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what != 1)
        return;
      if ((CategoryGridView.this.adapter == null) || (CategoryGridView.this.adapter.isEmpty()))
      {
        CategoryGridView.this.removeAllViews();
        return;
      }
      CategoryGridView.this.removeAllViews();
      int i = 0;
      while (true)
      {
        try
        {
          if (i >= CategoryGridView.this.adapter.getCount())
            break;
          paramMessage = CategoryGridView.this.adapter.getView(i, null, CategoryGridView.this);
          if (!(paramMessage instanceof TableRow))
            continue;
          ((TableRow)paramMessage).setBaselineAligned(false);
          CategoryGridView.this.addView(paramMessage);
          CategoryGridView.this.curRow = ((TableRow)paramMessage);
          break label153;
          if ((paramMessage != null) && (CategoryGridView.this.curRow != null))
            CategoryGridView.this.curRow.addView(paramMessage);
        }
        catch (java.lang.Exception paramMessage)
        {
          return;
        }
        label153: i += 1;
      }
    }
  };
  private Drawable horizontalDivider = getResources().getDrawable(R.drawable.grid_horizontal_unselected);
  private OnItemClickListener mClickListener;
  private boolean needHideDivider = false;
  private final DataSetObserver observer = new DataSetObserver()
  {
    public void onChanged()
    {
      CategoryGridView.this.handler.removeMessages(1);
      CategoryGridView.this.handler.sendEmptyMessageDelayed(1, 100L);
    }

    public void onInvalidated()
    {
      onChanged();
    }
  };
  private Drawable verticalDivider = getResources().getDrawable(R.drawable.grid_vertical_line);

  public CategoryGridView(Context paramContext)
  {
    super(paramContext);
  }

  public CategoryGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void drawDivider(Canvas paramCanvas, Drawable paramDrawable, Rect paramRect)
  {
    if (paramDrawable != null)
    {
      paramDrawable.setBounds(paramRect);
      paramDrawable.draw(paramCanvas);
    }
  }

  private void setChildOnClickListener(View paramView)
  {
    if ((paramView != null) && (paramView.getVisibility() == 0) && (paramView.isClickable()))
      paramView.setOnClickListener(this);
  }

  private void showDivider(Canvas paramCanvas, View paramView1, View paramView2, boolean paramBoolean)
  {
    Rect localRect;
    int i;
    if ((paramView2 != null) && (paramView2.getVisibility() == 0))
    {
      localRect = new Rect();
      if (!paramBoolean)
        break label258;
      if (this.endHorizontalDivider != null)
      {
        i = this.endHorizontalDivider.getIntrinsicHeight();
        if (i > 0)
        {
          if (paramView1 == null)
            break label216;
          localRect.left = (paramView1.getLeft() + paramView2.getLeft());
          localRect.top = (paramView1.getTop() + paramView2.getBottom() - i);
          localRect.right = (paramView1.getLeft() + paramView2.getRight());
          localRect.bottom = (paramView1.getTop() + paramView2.getBottom());
          drawDivider(paramCanvas, this.endHorizontalDivider, localRect);
        }
      }
      label120: if (this.verticalDivider != null)
      {
        i = this.verticalDivider.getIntrinsicWidth();
        if (i > 0)
        {
          if (paramView1 == null)
            break label398;
          localRect.left = (paramView1.getLeft() + paramView2.getRight());
          localRect.top = (paramView1.getTop() + paramView2.getTop());
          localRect.right = (paramView1.getLeft() + paramView2.getRight() + i);
        }
      }
    }
    for (localRect.bottom = (paramView1.getTop() + paramView2.getBottom()); ; localRect.bottom = paramView2.getBottom())
    {
      drawDivider(paramCanvas, this.verticalDivider, localRect);
      return;
      label216: localRect.left = paramView2.getLeft();
      localRect.top = (paramView2.getBottom() - i);
      localRect.right = paramView2.getRight();
      localRect.bottom = paramView2.getBottom();
      break;
      label258: if (this.horizontalDivider == null)
        break label120;
      i = this.horizontalDivider.getIntrinsicHeight();
      if (i <= 0)
        break label120;
      if (paramView1 != null)
      {
        localRect.left = (paramView1.getLeft() + paramView2.getLeft());
        localRect.top = (paramView1.getTop() + paramView2.getBottom() - i);
        localRect.right = (paramView1.getLeft() + paramView2.getRight());
      }
      for (localRect.bottom = (paramView1.getTop() + paramView2.getBottom()); ; localRect.bottom = paramView2.getBottom())
      {
        drawDivider(paramCanvas, this.horizontalDivider, localRect);
        break;
        localRect.left = paramView2.getLeft();
        localRect.top = (paramView2.getBottom() - i);
        localRect.right = paramView2.getRight();
      }
      label398: localRect.left = paramView2.getRight();
      localRect.top = paramView2.getTop();
      localRect.right = (paramView2.getRight() + i);
    }
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if ((!this.needHideDivider) && ((this.horizontalDivider != null) || (this.verticalDivider != null)))
    {
      int i = 0;
      if (i < getChildCount())
      {
        Object localObject = getChildAt(i);
        if ((localObject instanceof TableRow))
        {
          localObject = (TableRow)localObject;
          int j = 0;
          if (j < ((TableRow)localObject).getChildCount())
          {
            View localView = ((TableRow)localObject).getChildAt(j);
            if (i == getChildCount() - 1);
            for (bool = true; ; bool = false)
            {
              showDivider(paramCanvas, (View)localObject, localView, bool);
              j += 1;
              break;
            }
          }
        }
        else
        {
          if (i != getChildCount() - 1)
            break label146;
        }
        label146: for (boolean bool = true; ; bool = false)
        {
          showDivider(paramCanvas, null, (View)localObject, bool);
          i += 1;
          break;
        }
      }
    }
  }

  public Adapter getAdapter()
  {
    return this.adapter;
  }

  public TableRow getCurRow()
  {
    return this.curRow;
  }

  public Drawable getEndHorizontalDivider()
  {
    return this.endHorizontalDivider;
  }

  public Drawable getHorizontalDivider()
  {
    return this.horizontalDivider;
  }

  public Drawable getVerticalDivider()
  {
    return this.verticalDivider;
  }

  public boolean isNeedHideDivider()
  {
    return this.needHideDivider;
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
      if ((localObject instanceof TableRow))
      {
        localObject = (TableRow)localObject;
        int m = 0;
        while (true)
        {
          j = i;
          if (m >= ((TableRow)localObject).getChildCount())
            break label115;
          if (paramView == ((TableRow)localObject).getChildAt(m))
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
      if ((localObject instanceof TableRow))
      {
        localObject = (TableRow)localObject;
        paramInt2 = 0;
        while (paramInt2 < ((TableRow)localObject).getChildCount())
        {
          setChildOnClickListener(((TableRow)localObject).getChildAt(paramInt2));
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

  public void setCurRow(TableRow paramTableRow)
  {
    this.curRow = paramTableRow;
  }

  public void setEndHorizontalDivider(Drawable paramDrawable)
  {
    this.endHorizontalDivider = paramDrawable;
  }

  public void setHorizontalDivider(Drawable paramDrawable)
  {
    if (paramDrawable == this.horizontalDivider)
      return;
    this.horizontalDivider = paramDrawable;
    requestLayout();
  }

  public void setNeedHideDivider(boolean paramBoolean)
  {
    this.needHideDivider = paramBoolean;
  }

  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
  {
    this.mClickListener = paramOnItemClickListener;
  }

  public void setVerticalDivider(Drawable paramDrawable)
  {
    if (paramDrawable == this.verticalDivider)
      return;
    this.verticalDivider = paramDrawable;
    requestLayout();
  }

  public static abstract interface OnItemClickListener
  {
    public abstract void onItemClick(CategoryGridView paramCategoryGridView, View paramView, int paramInt, long paramLong);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CategoryGridView
 * JD-Core Version:    0.6.0
 */