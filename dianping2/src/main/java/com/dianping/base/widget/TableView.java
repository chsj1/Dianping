package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;

public class TableView extends LinearLayout
  implements View.OnClickListener
{
  Adapter adapter;
  private Drawable divider = getResources().getDrawable(R.drawable.gray_horizontal_line);
  private Drawable dividerOfGroupEnd;
  private Drawable dividerOfGroupHeader;
  private int dividerPadding;
  Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what != 1);
      while (true)
      {
        return;
        if ((TableView.this.adapter == null) || (TableView.this.adapter.isEmpty()))
        {
          TableView.this.removeAllViews();
          return;
        }
        TableView.this.removeAllViews();
        int i = 0;
        try
        {
          while (i < TableView.this.adapter.getCount())
          {
            paramMessage = TableView.this.adapter.getView(i, null, TableView.this);
            TableView.this.addView(paramMessage);
            i += 1;
          }
        }
        catch (Exception paramMessage)
        {
          paramMessage.printStackTrace();
        }
      }
    }
  };
  private OnItemClickListener mClickListener;
  private final DataSetObserver observer = new DataSetObserver()
  {
    public void onChanged()
    {
      TableView.this.handler.removeMessages(1);
      TableView.this.handler.sendEmptyMessageDelayed(1, 100L);
    }

    public void onInvalidated()
    {
      onChanged();
    }
  };

  public TableView(Context paramContext)
  {
    this(paramContext, null);
  }

  public TableView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TableView);
    this.dividerPadding = paramContext.getDimensionPixelSize(R.styleable.TableView_divider_padding, getResources().getDimensionPixelOffset(R.dimen.table_item_padding));
    paramContext.recycle();
    setOrientation(1);
  }

  private void drawDivider(Canvas paramCanvas, View paramView)
  {
    int i;
    if (this.divider != null)
    {
      localObject = paramView.getDrawableState();
      this.divider.setState(localObject);
      i = this.divider.getCurrent().getIntrinsicHeight();
      if (i > 0);
    }
    else
    {
      return;
    }
    Object localObject = new Rect();
    ((Rect)localObject).left = (getPaddingLeft() + this.dividerPadding);
    ((Rect)localObject).top = (paramView.getBottom() - i);
    ((Rect)localObject).right = (getRight() - getLeft() - getPaddingRight());
    ((Rect)localObject).bottom = paramView.getBottom();
    this.divider.setBounds((Rect)localObject);
    this.divider.draw(paramCanvas);
  }

  private void drawDividerOfGroupEnd(Canvas paramCanvas, View paramView)
  {
    if (this.dividerOfGroupEnd == null);
    int i;
    for (Drawable localDrawable = this.divider; ; localDrawable = this.dividerOfGroupEnd)
    {
      if (localDrawable != null)
      {
        localDrawable.setState(paramView.getDrawableState());
        i = localDrawable.getCurrent().getIntrinsicHeight();
        if (i > 0)
          break;
      }
      return;
    }
    Rect localRect = new Rect();
    localRect.left = getPaddingLeft();
    localRect.top = (paramView.getBottom() - i);
    localRect.right = (getRight() - getLeft() - getPaddingRight());
    localRect.bottom = paramView.getBottom();
    localDrawable.setBounds(localRect);
    localDrawable.draw(paramCanvas);
  }

  private void drawDividerOfGroupheader(Canvas paramCanvas, View paramView)
  {
    if (this.dividerOfGroupHeader == null);
    int i;
    for (Drawable localDrawable = this.divider; ; localDrawable = this.dividerOfGroupHeader)
    {
      if (localDrawable != null)
      {
        localDrawable.setState(paramView.getDrawableState());
        i = localDrawable.getCurrent().getIntrinsicHeight();
        if (i > 0)
          break;
      }
      return;
    }
    Rect localRect = new Rect();
    localRect.left = getPaddingLeft();
    localRect.top = (paramView.getTop() - i);
    localRect.right = (getRight() - getLeft() - getPaddingRight());
    localRect.bottom = paramView.getTop();
    localDrawable.setBounds(localRect);
    localDrawable.draw(paramCanvas);
  }

  private boolean isGroupEnd(int paramInt)
  {
    paramInt += 1;
    while (paramInt < getChildCount())
    {
      View localView = getChildAt(paramInt);
      if ((localView != null) && (localView.getVisibility() == 0))
        return localView instanceof TableHeader;
      paramInt += 1;
    }
    return true;
  }

  private boolean isGroupStart(int paramInt)
  {
    paramInt -= 1;
    while (paramInt >= 0)
    {
      View localView = getChildAt(paramInt);
      if ((localView != null) && (localView.getVisibility() == 0))
        return localView instanceof TableHeader;
      paramInt -= 1;
    }
    return true;
  }

  public void childDrawableStateChanged(View paramView)
  {
    super.childDrawableStateChanged(paramView);
    if (this.divider != null)
      invalidate(paramView.getLeft(), paramView.getTop(), paramView.getRight(), paramView.getBottom());
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if (this.divider != null)
    {
      int i = 0;
      if (i < getChildCount())
      {
        View localView = getChildAt(i);
        if ((localView != null) && (localView.getVisibility() == 0) && (!(localView instanceof TableHeader)) && (localView.getHeight() > 0))
        {
          if (isGroupStart(i))
            drawDividerOfGroupheader(paramCanvas, localView);
          if (!isGroupEnd(i))
            break label88;
          drawDividerOfGroupEnd(paramCanvas, localView);
        }
        while (true)
        {
          i += 1;
          break;
          label88: drawDivider(paramCanvas, localView);
        }
      }
    }
  }

  public Adapter getAdapter()
  {
    return this.adapter;
  }

  public Drawable getDivider()
  {
    return this.divider;
  }

  public void onClick(View paramView)
  {
    int k;
    long l1;
    int i;
    if (this.mClickListener != null)
    {
      k = -1;
      l1 = -1L;
      i = 0;
    }
    int j;
    while (true)
    {
      j = k;
      if (i < getChildCount())
      {
        if (paramView == getChildAt(i))
          j = i;
      }
      else
      {
        if (j >= 0)
          break;
        return;
      }
      i += 1;
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
      View localView = getChildAt(paramInt1);
      if ((localView != null) && (localView.getVisibility() == 0) && (!(localView instanceof AdapterView)) && (this.mClickListener != null))
      {
        paramBoolean = localView.isClickable();
        localView.setOnClickListener(this);
        if (!paramBoolean)
          localView.setClickable(false);
      }
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

  public void setDivider(Drawable paramDrawable)
  {
    if (paramDrawable == this.divider)
      return;
    this.divider = paramDrawable;
    requestLayout();
  }

  public void setDividerOfGroupEnd(int paramInt)
  {
    if (paramInt > 0)
    {
      this.dividerOfGroupEnd = getResources().getDrawable(paramInt);
      requestLayout();
    }
  }

  public void setDividerOfGroupEnd(Drawable paramDrawable)
  {
    this.dividerOfGroupEnd = paramDrawable;
    requestLayout();
  }

  public void setDividerOfGroupHeader(int paramInt)
  {
    if (paramInt > 0)
    {
      this.dividerOfGroupHeader = getResources().getDrawable(paramInt);
      requestLayout();
    }
  }

  public void setDividerOfGroupHeader(Drawable paramDrawable)
  {
    this.dividerOfGroupHeader = paramDrawable;
    requestLayout();
  }

  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
  {
    this.mClickListener = paramOnItemClickListener;
  }

  public static abstract interface OnItemClickListener
  {
    public abstract void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TableView
 * JD-Core Version:    0.6.0
 */