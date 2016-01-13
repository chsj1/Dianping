package com.dianping.main.home;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.dianping.v1.R.array;
import org.json.JSONArray;

public class HotAdTemplate extends LinearLayout
{
  private static final int MAX_AD_COUNT = 4;
  private JSONArray hotAdArray;

  public HotAdTemplate(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotAdTemplate(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void inflateTemplate(JSONArray paramJSONArray)
  {
    TypedArray localTypedArray;
    if ((this.hotAdArray == null) || (paramJSONArray.length() != this.hotAdArray.length()))
    {
      removeAllViewsInLayout();
      localTypedArray = getContext().getResources().obtainTypedArray(R.array.hotad_template);
      switch (paramJSONArray.length())
      {
      default:
        LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(3, 0), this, true);
      case 1:
      case 2:
      case 3:
      case 4:
      }
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
    }
  }

  public void setHotAds(JSONArray paramJSONArray)
  {
    if (paramJSONArray == null)
      return;
    inflateTemplate(paramJSONArray);
    this.hotAdArray = paramJSONArray;
    TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.hotad_template_item);
    int i = 0;
    while (i < Math.min(paramJSONArray.length(), 4))
    {
      ((HotAdItem)findViewById(localTypedArray.getResourceId(i, 0))).setHotAd(paramJSONArray.optJSONObject(i), i);
      i += 1;
    }
    localTypedArray.recycle();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HotAdTemplate
 * JD-Core Version:    0.6.0
 */