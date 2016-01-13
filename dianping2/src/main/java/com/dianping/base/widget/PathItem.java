package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class PathItem extends LinearLayout
{
  TextView distance;
  View dividerView;
  View listDividerLine;
  TextView time;
  ViewGroup time_distance;
  TextView title;
  ImageView totalDistanceImg;
  ImageView type;
  TextView wDistance;
  ImageView walkImg;

  public PathItem(Context paramContext)
  {
    super(paramContext);
  }

  public PathItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private String getDistenceString(int paramInt)
  {
    if (paramInt < 1000)
      return paramInt + "米";
    StringBuilder localStringBuilder = new StringBuilder().append(paramInt / 1000);
    if (paramInt % 1000 > 0);
    for (String str = "." + paramInt % 1000 / 100; ; str = "")
      return str + "公里";
  }

  private int getTypeImage(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 0;
    case 0:
      return R.drawable.map_icon_bus;
    case 1:
      return R.drawable.map_icon_car;
    case 2:
    }
    return R.drawable.map_icon_walk;
  }

  private void updateDivider(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2 - 1)
    {
      this.dividerView.setVisibility(8);
      this.listDividerLine.setBackgroundColor(getResources().getColor(R.color.inner_divider));
      return;
    }
    this.dividerView.setVisibility(0);
    this.listDividerLine.setBackgroundColor(getResources().getColor(R.color.line_gray));
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(R.id.title));
    this.distance = ((TextView)findViewById(R.id.total_distance));
    this.wDistance = ((TextView)findViewById(R.id.walk_distance));
    this.time = ((TextView)findViewById(R.id.time));
    this.time_distance = ((ViewGroup)findViewById(R.id.time_and_distance));
    this.type = ((ImageView)findViewById(R.id.type));
    this.walkImg = ((ImageView)findViewById(R.id.walk_img));
    this.totalDistanceImg = ((ImageView)findViewById(R.id.total_distance_img));
    this.dividerView = findViewById(R.id.list_divider);
    this.listDividerLine = findViewById(R.id.list_divider_line);
  }

  public void setPath(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.title.setText(paramString);
    this.time.setText("");
    this.distance.setText("");
    this.wDistance.setText("");
    if (paramInt3 > 0)
    {
      this.time.setText(DateUtils.secToTime(paramInt3));
      if (paramInt2 <= 0)
        break label188;
      this.distance.setText(getDistenceString(paramInt2));
      label68: if (paramInt2 <= 0)
        break label200;
      this.wDistance.setText("步行" + getDistenceString(paramInt2));
    }
    while (true)
    {
      if (paramInt4 == 0)
      {
        this.distance.setVisibility(8);
        this.totalDistanceImg.setVisibility(8);
      }
      if ((paramInt4 == 1) || (paramInt4 == 2))
      {
        this.wDistance.setVisibility(8);
        this.walkImg.setVisibility(8);
      }
      this.type.setImageResource(getTypeImage(paramInt4));
      updateDivider(paramInt1, paramInt5);
      return;
      this.time_distance.setVisibility(8);
      break;
      label188: this.time_distance.setVisibility(8);
      break label68;
      label200: this.wDistance.setVisibility(8);
      this.walkImg.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.PathItem
 * JD-Core Version:    0.6.0
 */