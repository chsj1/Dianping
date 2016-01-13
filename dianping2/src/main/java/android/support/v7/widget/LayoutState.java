package android.support.v7.widget;

import android.view.View;

class LayoutState
{
  static final int INVALID_LAYOUT = -2147483648;
  static final int ITEM_DIRECTION_HEAD = -1;
  static final int ITEM_DIRECTION_TAIL = 1;
  static final int LAYOUT_END = 1;
  static final int LAYOUT_START = -1;
  static final int SCOLLING_OFFSET_NaN = -2147483648;
  static final String TAG = "LayoutState";
  int mAvailable;
  int mCurrentPosition;
  int mEndLine = 0;
  int mItemDirection;
  int mLayoutDirection;
  int mStartLine = 0;

  boolean hasMore(RecyclerView.State paramState)
  {
    return (this.mCurrentPosition >= 0) && (this.mCurrentPosition < paramState.getItemCount());
  }

  View next(RecyclerView.Recycler paramRecycler)
  {
    paramRecycler = paramRecycler.getViewForPosition(this.mCurrentPosition);
    this.mCurrentPosition += this.mItemDirection;
    return paramRecycler;
  }

  public String toString()
  {
    return "LayoutState{mAvailable=" + this.mAvailable + ", mCurrentPosition=" + this.mCurrentPosition + ", mItemDirection=" + this.mItemDirection + ", mLayoutDirection=" + this.mLayoutDirection + ", mStartLine=" + this.mStartLine + ", mEndLine=" + this.mEndLine + '}';
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.v7.widget.LayoutState
 * JD-Core Version:    0.6.0
 */