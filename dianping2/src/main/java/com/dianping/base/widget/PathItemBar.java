package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.DateUtils;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import java.util.ArrayList;

public class PathItemBar extends FrameLayout
{
  private LinearLayout detailPathView;
  private TextView interchangeInfoView;
  private RelativeLayout morePathView;
  private ArrayList<String> roadNames = new ArrayList();
  private TextView timeAndWalkDistanceView;
  private TextView titleView;

  public PathItemBar(Context paramContext)
  {
    super(paramContext);
  }

  public PathItemBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private String getDistance(int paramInt)
  {
    if (paramInt < 1000)
      return paramInt + "米";
    StringBuilder localStringBuilder = new StringBuilder().append(paramInt / 1000);
    if (paramInt % 1000 > 0);
    for (String str = "." + paramInt % 1000 / 100; ; str = "")
      return str + "公里";
  }

  private String getInterChangeInfo(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return "";
    paramDPObject = paramDPObject.getArray("Steps");
    int i = 0;
    while (i < paramDPObject.length)
    {
      String str = paramDPObject[i].getString("RoadName");
      if ((!TextUtils.isEmpty(str)) && (i != 0) && (i != paramDPObject.length - 1))
        this.roadNames.add(str.trim().substring(0, str.lastIndexOf(" ")));
      i += 1;
    }
    if (this.roadNames.size() == 0)
      return "";
    return this.roadNames.toString().replace(",", " - ").replace("[", "").replace("]", "");
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.titleView = ((TextView)findViewById(R.id.title));
    this.interchangeInfoView = ((TextView)findViewById(R.id.subtitle));
    this.timeAndWalkDistanceView = ((TextView)findViewById(R.id.detail_title));
    this.morePathView = ((RelativeLayout)findViewById(R.id.map_more));
    this.detailPathView = ((LinearLayout)findViewById(R.id.map_route));
    this.morePathView.setVisibility(8);
    this.detailPathView.setVisibility(0);
  }

  public void setPath(DPObject paramDPObject)
  {
    String str = paramDPObject.getString("Title");
    int i = paramDPObject.getInt("Distance");
    int j = paramDPObject.getInt("Duration");
    this.titleView.setText(str);
    this.interchangeInfoView.setText(getInterChangeInfo(paramDPObject));
    this.titleView.post(new Runnable()
    {
      public void run()
      {
        if (PathItemBar.this.titleView.getLineCount() > 1)
        {
          PathItemBar.this.interchangeInfoView.setVisibility(8);
          return;
        }
        PathItemBar.this.interchangeInfoView.setVisibility(0);
      }
    });
    this.timeAndWalkDistanceView.setText(DateUtils.secToTime(j) + " | " + "步行" + getDistance(i));
  }

  public void showMore()
  {
    this.morePathView.setVisibility(0);
    this.detailPathView.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.PathItemBar
 * JD-Core Version:    0.6.0
 */