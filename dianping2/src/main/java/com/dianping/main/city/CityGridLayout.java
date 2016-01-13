package com.dianping.main.city;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.model.City;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.List;

public class CityGridLayout extends LinearLayout
{
  private static final int COLUMN_COUNT = 3;
  private LinearLayout categoryContainer;
  private LinearLayout layout;
  private View.OnClickListener listener;
  private TextView titleView;

  public CityGridLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public CityGridLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(paramContext).inflate(R.layout.city_category_layout, this, true);
    this.layout = ((LinearLayout)findViewById(R.id.layout));
    this.titleView = ((TextView)findViewById(R.id.city_title));
    this.categoryContainer = ((LinearLayout)findViewById(R.id.recommend_category_container));
    this.categoryContainer.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
  }

  private NovaTextView createRecommendItem(City paramCity, String paramString, int paramInt)
  {
    NovaTextView localNovaTextView = new NovaTextView(getContext());
    localNovaTextView.setGAString(paramString, paramCity.name());
    localNovaTextView.gaUserInfo.category_id = Integer.valueOf(paramInt);
    localNovaTextView.setClickable(true);
    paramString = new LinearLayout.LayoutParams(0, -2);
    paramString.weight = 1.0F;
    paramString.leftMargin = ViewUtils.dip2px(getContext(), 4.0F);
    paramString.rightMargin = ViewUtils.dip2px(getContext(), 4.0F);
    paramString.topMargin = ViewUtils.dip2px(getContext(), 4.0F);
    paramString.bottomMargin = ViewUtils.dip2px(getContext(), 4.0F);
    paramString.gravity = 17;
    localNovaTextView.setLayoutParams(paramString);
    localNovaTextView.setPadding(0, ViewUtils.dip2px(getContext(), 10.0F), 0, ViewUtils.dip2px(getContext(), 8.0F));
    localNovaTextView.setBackgroundResource(R.drawable.recommend_categroy_bkg);
    localNovaTextView.setGravity(17);
    localNovaTextView.setTextSize(2, 16.0F);
    localNovaTextView.setText(paramCity.name());
    localNovaTextView.setSingleLine(true);
    localNovaTextView.setEllipsize(TextUtils.TruncateAt.END);
    if (paramCity.id() == -1)
      localNovaTextView.setTextColor(getResources().getColor(R.color.light_red));
    while (true)
    {
      localNovaTextView.setTag(paramCity);
      localNovaTextView.setOnClickListener(this.listener);
      return localNovaTextView;
      localNovaTextView.setTextColor(getResources().getColor(R.color.deep_gray));
    }
  }

  private LinearLayout createRecommendRow(List<City> paramList, String paramString, int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setOrientation(0);
    localLinearLayout.setBackgroundResource(R.color.transparent);
    int i = 0;
    if ((i < paramList.size()) || (i < 3))
    {
      if (i < paramList.size())
        localLinearLayout.addView(createRecommendItem((City)paramList.get(i), paramString, paramInt));
      while (true)
      {
        i += 1;
        break;
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -2);
        localLayoutParams.weight = 1.0F;
        View localView = new View(getContext());
        localView.setLayoutParams(localLayoutParams);
        localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 4.0F);
        localLayoutParams.rightMargin = ViewUtils.dip2px(getContext(), 4.0F);
        localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 4.0F);
        localLayoutParams.bottomMargin = ViewUtils.dip2px(getContext(), 4.0F);
        localLinearLayout.addView(localView);
      }
    }
    return localLinearLayout;
  }

  public void setItems(String paramString1, ArrayList<City> paramArrayList, View.OnClickListener paramOnClickListener, int paramInt1, String paramString2, int paramInt2)
  {
    if ((paramArrayList == null) || (paramArrayList.size() == 0) || (paramInt1 == 0))
    {
      this.layout.setVisibility(8);
      return;
    }
    this.layout.setVisibility(0);
    this.titleView.setText(paramString1);
    this.categoryContainer.removeAllViews();
    this.listener = paramOnClickListener;
    int i = paramInt1;
    if (paramInt1 == -1)
      i = paramArrayList.size();
    paramInt1 = 0;
    label73: if ((paramInt1 < paramArrayList.size()) && (paramInt1 < i))
      if (paramInt1 + 3 <= paramArrayList.size())
        break label140;
    label140: for (int j = paramArrayList.size(); ; j = paramInt1 + 3)
    {
      paramString1 = paramArrayList.subList(paramInt1, j);
      this.categoryContainer.addView(createRecommendRow(paramString1, paramString2, paramInt2));
      paramInt1 += 3;
      break label73;
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.city.CityGridLayout
 * JD-Core Version:    0.6.0
 */