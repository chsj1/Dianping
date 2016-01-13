package com.dianping.main.home;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.dianping.v1.R.array;
import com.dianping.v1.R.drawable;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewUserAdTemplate extends LinearLayout
{
  public static final int MAX_AD_COUNT = 1;

  public NewUserAdTemplate(Context paramContext)
  {
    this(paramContext, null);
  }

  public NewUserAdTemplate(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    setBackgroundResource(R.drawable.home_listview_bg);
  }

  public void setNewUserAd(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null);
    while (true)
    {
      return;
      TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.newuser_template_item);
      JSONArray localJSONArray = paramJSONObject.optJSONArray("bodyUnits");
      paramJSONObject = paramJSONObject.optString("tags");
      int i = 0;
      while (i < Math.min(localJSONArray.length(), 1))
      {
        ((HomeNewUserAdItem)findViewById(localTypedArray.getResourceId(i, 0))).setNewUserAd(localJSONArray.optJSONObject(i), i, paramJSONObject);
        i += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.NewUserAdTemplate
 * JD-Core Version:    0.6.0
 */