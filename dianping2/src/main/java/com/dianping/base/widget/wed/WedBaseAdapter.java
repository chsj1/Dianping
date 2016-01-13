package com.dianping.base.widget.wed;

import TV;;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class WedBaseAdapter<T> extends BaseAdapter
{
  protected final String RMB = "¥";
  protected T[] adapterData;
  protected int albumFrameHeight;
  protected int albumFrameWidth;
  protected Context context;
  protected int coverStyleType = 0;
  protected int verticalAlbumFrameHeight;
  protected int verticalAlbumFrameWidth;

  protected <V extends View> V getAdapterView(View paramView, int paramInt)
  {
    Object localObject2 = (SparseArray)paramView.getTag();
    Object localObject1 = localObject2;
    if (localObject2 == null)
    {
      localObject1 = new SparseArray();
      paramView.setTag(localObject1);
    }
    View localView = (View)((SparseArray)localObject1).get(paramInt);
    localObject2 = localView;
    if (localView == null)
    {
      localObject2 = paramView.findViewById(paramInt);
      ((SparseArray)localObject1).put(paramInt, localObject2);
    }
    return (TV)(TV)localObject2;
  }

  public int getCount()
  {
    if (this.adapterData == null)
      return 0;
    return this.adapterData.length;
  }

  public Object getItem(int paramInt)
  {
    if (this.adapterData == null)
      return null;
    return this.adapterData[paramInt];
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.wed.WedBaseAdapter
 * JD-Core Version:    0.6.0
 */