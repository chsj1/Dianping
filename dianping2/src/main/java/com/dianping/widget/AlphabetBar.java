package com.dianping.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;

public class AlphabetBar extends View
{
  private ListView list;
  private int mCurIdx;
  private OnSelectedListener mOnSelectedListener;
  private String[] mSections;
  private int m_nItemHeight = 25;
  private SectionIndexer sectionIndexter = null;

  public AlphabetBar(Context paramContext)
  {
    super(paramContext);
    setBackgroundColor(1157627903);
  }

  public AlphabetBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBackgroundColor(1157627903);
  }

  public AlphabetBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setBackgroundColor(1157627903);
  }

  public int getCurIndex()
  {
    return this.mCurIdx;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if (this.mSections == null)
      return;
    Paint localPaint = new Paint();
    localPaint.setColor(getContext().getResources().getColor(R.color.deep_gray));
    localPaint.setAntiAlias(true);
    localPaint.setTextSize(ViewUtils.sp2px(getContext(), 12.0F));
    localPaint.setTextAlign(Paint.Align.CENTER);
    float f = getMeasuredWidth() / 2.0F;
    int i = 0;
    while (i < this.mSections.length)
    {
      String str2 = String.valueOf(this.mSections[i]);
      String str1 = str2;
      if (str2.length() > 2)
        str1 = str2.substring(0, 1);
      paramCanvas.drawText(str1, f, this.m_nItemHeight + this.m_nItemHeight * i, localPaint);
      i += 1;
    }
    super.onDraw(paramCanvas);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if ((this.mSections != null) && (this.mSections.length > 0))
      this.m_nItemHeight = ((paramInt4 - paramInt2 - 10) / this.mSections.length);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onTouchEvent(paramMotionEvent);
    int j = (int)paramMotionEvent.getY() / this.m_nItemHeight;
    int i;
    if (j >= this.mSections.length)
    {
      i = this.mSections.length - 1;
      this.mCurIdx = i;
      if ((paramMotionEvent.getAction() != 0) && (paramMotionEvent.getAction() != 2))
        break label137;
      if (this.sectionIndexter == null)
        this.sectionIndexter = ((SectionIndexer)this.list.getAdapter());
      i = this.sectionIndexter.getPositionForSection(i);
      if (i != -1)
        break label104;
    }
    label104: 
    do
    {
      return true;
      i = j;
      if (j >= 0)
        break;
      i = 0;
      break;
      this.list.setSelection(i);
      if (this.mOnSelectedListener != null)
        this.mOnSelectedListener.onSelected(i);
      setBackgroundColor(-3355444);
      return true;
    }
    while (paramMotionEvent.getAction() != 1);
    label137: if (this.mOnSelectedListener != null)
      this.mOnSelectedListener.onUnselected();
    setBackgroundColor(16777215);
    return true;
  }

  public void setListView(ListView paramListView)
  {
    this.list = paramListView;
  }

  public void setOnSelectedListener(OnSelectedListener paramOnSelectedListener)
  {
    this.mOnSelectedListener = paramOnSelectedListener;
  }

  public void setSectionIndexter(SectionIndexer paramSectionIndexer)
  {
    this.sectionIndexter = paramSectionIndexer;
    this.mSections = ((String[])(String[])paramSectionIndexer.getSections());
    requestLayout();
  }

  public void setSections(String[] paramArrayOfString)
  {
    this.mSections = paramArrayOfString;
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      setVisibility(8);
  }

  public static abstract interface OnSelectedListener
  {
    public abstract void onSelected(int paramInt);

    public abstract void onUnselected();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.AlphabetBar
 * JD-Core Version:    0.6.0
 */