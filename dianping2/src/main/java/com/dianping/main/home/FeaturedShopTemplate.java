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

public class FeaturedShopTemplate extends NovaLinearLayout
{
  private static final int MAX_AD_COUNT = 3;
  private HomeClockLayout clockLayout;
  private JSONArray saleItemList;

  public FeaturedShopTemplate(Context paramContext)
  {
    super(paramContext, null);
    setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    setOrientation(1);
  }

  public FeaturedShopTemplate(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void inflateTemplate(JSONArray paramJSONArray)
  {
    if ((paramJSONArray != null) && (this.saleItemList != null) && (paramJSONArray.length() == this.saleItemList.length()))
      return;
    removeAllViewsInLayout();
    TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.featureshop_template);
    int i = 0;
    if (paramJSONArray != null)
      i = paramJSONArray.length();
    switch (i)
    {
    default:
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(1, 0), this, true);
    case 2:
    }
    while (true)
    {
      localTypedArray.recycle();
      return;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(0, 0), this, true);
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
    paramJSONObject = paramJSONObject.optJSONArray("richUnits");
    inflateTemplate(paramJSONObject);
    this.saleItemList = paramJSONObject;
    this.clockLayout = ((HomeClockLayout)findViewById(R.id.famous_shop_item));
    this.clockLayout.setFamousShopItem(localJSONObject, 1);
    if (this.saleItemList.length() < 2)
      return;
    paramJSONObject = getContext().getResources().obtainTypedArray(R.array.famousad_template_item);
    int i = 0;
    while (i < Math.min(this.saleItemList.length(), 3))
    {
      ((FamousAdItem)findViewById(paramJSONObject.getResourceId(i, 0))).setFamousAd(this.saleItemList.optJSONObject(i), i, 1);
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
 * Qualified Name:     com.dianping.main.home.FeaturedShopTemplate
 * JD-Core Version:    0.6.0
 */