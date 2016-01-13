package com.dianping.shopinfo.hospital.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CustomLinearLayout;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;

public class DepartmentAdpter extends BasicAdapter
{
  private int availableWith;
  private Context context;
  private DPObject[] departmentItems;
  private int originalWith;

  public DepartmentAdpter(Context paramContext, DPObject[] paramArrayOfDPObject, int paramInt)
  {
    this.departmentItems = paramArrayOfDPObject;
    this.context = paramContext;
    this.originalWith = (paramInt - ViewUtils.dip2px(paramContext, 15.0F) * 2);
    this.availableWith = this.originalWith;
  }

  public int getCount()
  {
    return this.departmentItems.length;
  }

  public Object getItem(int paramInt)
  {
    return this.departmentItems[paramInt];
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramView = ((DPObject)getItem(paramInt)).getString("Name");
    int i = ViewUtils.getTextViewWidth((TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.review_short_tag, null, false), paramView, 14) + ViewUtils.dip2px(this.context, 12.0F);
    if ((paramInt == 0) || (i > this.availableWith))
    {
      this.availableWith = (this.originalWith - i);
      LinearLayout localLinearLayout = new LinearLayout(this.context);
      ((LinearLayout)localLinearLayout).setOrientation(0);
      paramViewGroup = (NovaTextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.review_short_tag, (LinearLayout)localLinearLayout, false);
      paramViewGroup.setText(paramView);
      ((LinearLayout)localLinearLayout).addView(paramViewGroup);
      return localLinearLayout;
    }
    this.availableWith -= i;
    paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.review_short_tag, ((CustomLinearLayout)paramViewGroup).getCurSubLinearLayout(), false);
    ((NovaTextView)paramViewGroup).setText(paramView);
    return paramViewGroup;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.widget.DepartmentAdpter
 * JD-Core Version:    0.6.0
 */