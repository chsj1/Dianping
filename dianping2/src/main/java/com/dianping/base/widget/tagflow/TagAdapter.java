package com.dianping.base.widget.tagflow;

import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TagAdapter<T>
{
  private OnDataChangedListener mOnDataChangedListener;
  private List<T> mTagDatas;

  public TagAdapter(List<T> paramList)
  {
    this.mTagDatas = paramList;
  }

  public TagAdapter(T[] paramArrayOfT)
  {
    this.mTagDatas = new ArrayList(Arrays.asList(paramArrayOfT));
  }

  public int getCount()
  {
    if (this.mTagDatas == null)
      return 0;
    return this.mTagDatas.size();
  }

  public T getItem(int paramInt)
  {
    return this.mTagDatas.get(paramInt);
  }

  public abstract View getView(FlowLayout paramFlowLayout, int paramInt, T paramT);

  public void notifyDataChanged()
  {
    this.mOnDataChangedListener.onChanged();
  }

  public void setOnDataChangedListener(OnDataChangedListener paramOnDataChangedListener)
  {
    this.mOnDataChangedListener = paramOnDataChangedListener;
  }

  static abstract interface OnDataChangedListener
  {
    public abstract void onChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.tagflow.TagAdapter
 * JD-Core Version:    0.6.0
 */