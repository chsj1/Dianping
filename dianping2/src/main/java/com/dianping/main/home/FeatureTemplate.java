package com.dianping.main.home;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.array;
import com.dianping.v1.R.id;
import org.json.JSONArray;
import org.json.JSONObject;

public class FeatureTemplate extends LinearLayout
{
  private static final int MAX_COUNT = 4;
  private JSONObject mFeatureObject;
  private TextView subTitleText;
  private TextView titleText;

  public FeatureTemplate(Context paramContext)
  {
    this(paramContext, null);
  }

  public FeatureTemplate(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
  }

  private void inflateTemplate(JSONObject paramJSONObject)
  {
    if (((paramJSONObject == null) && (this.mFeatureObject == null)) || ((paramJSONObject != null) && (this.mFeatureObject != null) && (paramJSONObject.optJSONArray("bodyUnits") != null) && (this.mFeatureObject.optJSONArray("bodyUnits") != null) && (paramJSONObject.optJSONArray("bodyUnits").length() == this.mFeatureObject.optJSONArray("bodyUnits").length())))
      return;
    removeAllViewsInLayout();
    TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.feature_template);
    switch (paramJSONObject.optJSONArray("bodyUnits").length())
    {
    default:
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(2, 0), this, true);
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      localTypedArray.recycle();
      this.titleText = ((TextView)findViewById(R.id.feature_title));
      this.subTitleText = ((TextView)findViewById(R.id.feature_sub_title));
      return;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(0, 0), this, true);
      continue;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(1, 0), this, true);
      continue;
      LayoutInflater.from(getContext()).inflate(localTypedArray.getResourceId(2, 0), this, true);
    }
  }

  public void setFeatureObject(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null)
      return;
    inflateTemplate(paramJSONObject);
    this.mFeatureObject = paramJSONObject;
    Object localObject = paramJSONObject.optJSONObject("headerUnit");
    if (localObject != null)
    {
      this.titleText.setText(TextUtils.jsonParseText(((JSONObject)localObject).optString("title")));
      this.subTitleText.setText(TextUtils.jsonParseText(((JSONObject)localObject).optString("subTitle")));
    }
    paramJSONObject = paramJSONObject.optJSONArray("bodyUnits");
    localObject = getContext().getResources().obtainTypedArray(R.array.feature_template_item);
    int i = 0;
    while (i < Math.min(paramJSONObject.length(), 4))
    {
      ((HomeFeatureItem)findViewById(((TypedArray)localObject).getResourceId(i, 0))).setFeatureData(paramJSONObject.optJSONObject(i), i);
      i += 1;
    }
    ((TypedArray)localObject).recycle();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.FeatureTemplate
 * JD-Core Version:    0.6.0
 */