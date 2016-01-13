package com.dianping.main.home;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView.LayoutParams;
import com.dianping.v1.R.array;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;

public class FamousShopTemplate extends NovaLinearLayout
{
  private HomeClockLayout clockLayout;
  private Boolean hasTimeUnit;
  private int maxAdCount = 2;
  private JSONArray saleItemList;

  public FamousShopTemplate(Context paramContext)
  {
    super(paramContext, null);
    setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    setOrientation(1);
  }

  public FamousShopTemplate(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void inflateTemplate(JSONArray paramJSONArray, boolean paramBoolean)
  {
    if ((paramJSONArray != null) && (this.saleItemList != null) && (paramJSONArray.length() == this.saleItemList.length()) && (this.hasTimeUnit != null) && (this.hasTimeUnit.booleanValue() == paramBoolean))
      return;
    removeAllViewsInLayout();
    TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.famous_template);
    int i = 0;
    if (paramJSONArray != null)
      i = paramJSONArray.length();
    if (paramBoolean)
      if (i == 1)
        LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(0, 0), this, true);
    while (true)
    {
      localTypedArray.recycle();
      return;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(1, 0), this, true);
      continue;
      if (i == 2)
      {
        LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(2, 0), this, true);
        continue;
      }
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(3, 0), this, true);
    }
  }

  public void resetCount()
  {
    if (this.clockLayout != null)
      this.clockLayout.resetCount();
  }

  public void restartCount()
  {
    if (this.clockLayout != null)
      this.clockLayout.restartCount();
  }

  public void setShopTemplate(JSONObject paramJSONObject)
  {
    JSONObject localJSONObject = paramJSONObject.optJSONObject("leftTimerUnit");
    paramJSONObject = paramJSONObject.optJSONArray("homeHotActivities");
    boolean bool;
    if (localJSONObject == null)
      bool = false;
    for (this.maxAdCount = 3; ; this.maxAdCount = 2)
    {
      inflateTemplate(paramJSONObject, bool);
      this.saleItemList = paramJSONObject;
      this.hasTimeUnit = Boolean.valueOf(bool);
      if (this.hasTimeUnit.booleanValue())
      {
        this.clockLayout = ((HomeClockLayout)findViewById(R.id.famous_shop_item));
        this.clockLayout.setFamousShopItem(localJSONObject, 0);
      }
      if ((this.saleItemList != null) && (this.saleItemList.length() >= 1) && ((this.hasTimeUnit.booleanValue()) || (this.saleItemList.length() >= 2)))
        break;
      return;
      bool = true;
    }
    paramJSONObject = getContext().getResources().obtainTypedArray(R.array.famousad_template_item);
    int i = 0;
    while (i < Math.min(this.saleItemList.length(), this.maxAdCount))
    {
      ((FamousAdItem)findViewById(paramJSONObject.getResourceId(i, 0))).setFamousAd(this.saleItemList.optJSONObject(i), i, 0);
      i += 1;
    }
    paramJSONObject.recycle();
  }

  public void stopCount()
  {
    if (this.clockLayout != null)
      this.clockLayout.stopCount();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.FamousShopTemplate
 * JD-Core Version:    0.6.0
 */