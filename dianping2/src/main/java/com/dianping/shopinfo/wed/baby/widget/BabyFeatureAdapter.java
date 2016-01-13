package com.dianping.shopinfo.wed.baby.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.base.widget.wed.WedBaseAdapter;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class BabyFeatureAdapter extends WedBaseAdapter<String>
{
  public BabyFeatureAdapter(Context paramContext, String[] paramArrayOfString)
  {
    this.context = paramContext;
    this.adapterData = paramArrayOfString;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    if (paramView == null)
    {
      localView = LayoutInflater.from(this.context).inflate(R.layout.textview_babyprice_item, paramViewGroup, false);
      localView.setPadding(0, 0, 0, 0);
      localView.setMinimumHeight(0);
    }
    paramView = (TextView)localView;
    paramView.setSingleLine();
    paramView.setBackgroundColor(this.context.getResources().getColor(17170445));
    paramView.setTextColor(this.context.getResources().getColor(R.color.deep_gray));
    paramView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wed_icon_gou, 0, 0, 0);
    paramView.setText(((String[])this.adapterData)[paramInt]);
    return localView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.widget.BabyFeatureAdapter
 * JD-Core Version:    0.6.0
 */