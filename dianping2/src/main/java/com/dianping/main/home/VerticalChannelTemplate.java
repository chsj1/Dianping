package com.dianping.main.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.dianping.archive.DPObject;
import com.dianping.main.find.VerticalChannelFixedLayout;
import com.dianping.v1.R;
import com.dianping.widget.view.NovaLinearLayout;

import java.util.ArrayList;

public class VerticalChannelTemplate extends NovaLinearLayout
{
  private static final int MAX_AD_COUNT = 7;
  ArrayList<DPObject> hotList = new ArrayList();

  public VerticalChannelTemplate(Context paramContext)
  {
    this(paramContext, null);
  }

  public VerticalChannelTemplate(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void inflateTemplate(ArrayList<DPObject> paramArrayList)
  {
    setGAString("channel");
    if (paramArrayList.size() == this.hotList.size())
      return;
    removeAllViewsInLayout();
    TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.vertical_template);
    switch (paramArrayList.size())
    {
    default:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    }
    while (true)
    {
      localTypedArray.recycle();
      return;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(0, 0), this, true);
      continue;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(1, 0), this, true);
      continue;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(2, 0), this, true);
      continue;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(3, 0), this, true);
      continue;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(4, 0), this, true);
    }
  }

  public void setData(ArrayList<DPObject> paramArrayList)
  {
    if (paramArrayList == null)
      return;
    inflateTemplate(paramArrayList);
    this.hotList.clear();
    this.hotList.addAll(paramArrayList);
    TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.vertical_template_item);
    int j = Math.min(paramArrayList.size(), 7);
    int i = 0;
    while (i < j)
    {
      VerticalChannelFixedLayout localVerticalChannelFixedLayout = (VerticalChannelFixedLayout)findViewById(localTypedArray.getResourceId(i, 0));
      if (localVerticalChannelFixedLayout != null)
        localVerticalChannelFixedLayout.setData((DPObject)paramArrayList.get(i));
      i += 1;
    }
    localTypedArray.recycle();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.VerticalChannelTemplate
 * JD-Core Version:    0.6.0
 */