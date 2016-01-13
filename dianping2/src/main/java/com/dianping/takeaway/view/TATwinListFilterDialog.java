package com.dianping.takeaway.view;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog.LeftAdapter;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog.RightAdapter;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class TATwinListFilterDialog extends TwinListFilterDialog
{
  public TATwinListFilterDialog(Activity paramActivity)
  {
    super(paramActivity);
  }

  protected String getNums(Object paramObject)
  {
    String str = "";
    if ((DPObjectUtils.isDPObjectof(paramObject, "Category")) || (DPObjectUtils.isDPObjectof(paramObject, "Region")))
      str = "" + ((DPObject)paramObject).getInt("Count");
    return str;
  }

  public View getRightView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    TextView localTextView1;
    TextView localTextView2;
    DPObject localDPObject;
    if (paramView == null)
    {
      paramView = (NovaLinearLayout)getLayoutInflater().inflate(R.layout.filter_item, paramViewGroup, false);
      localTextView1 = (TextView)paramView.findViewById(16908308);
      localTextView2 = (TextView)paramView.findViewById(16908309);
      localDPObject = this.rightAdapter.getItem(paramInt);
      String str = localDPObject.getString("Name");
      paramViewGroup = str;
      if (localDPObject == this.leftAdapter.selected)
      {
        paramViewGroup = str;
        if (!str.startsWith("全部"))
          paramViewGroup = "全部" + str;
      }
      localTextView1.setText(paramViewGroup);
      paramView.setGAString(this.mElementid, paramViewGroup);
      if (!checkEquals(this.selectedItem, localDPObject))
        break label202;
      localTextView1.setTextColor(getActivity().getResources().getColor(R.color.yellow_1));
      localTextView2.setTextColor(getActivity().getResources().getColor(R.color.yellow_1));
      paramView.setBackgroundResource(R.drawable.filter_sub_selected);
    }
    while (true)
    {
      localTextView2.setText(getNums(localDPObject));
      return paramView;
      paramView = (NovaLinearLayout)paramView;
      break;
      label202: localTextView1.setTextColor(getActivity().getResources().getColor(R.color.black));
      localTextView2.setTextColor(getActivity().getResources().getColor(R.color.black));
      paramView.setBackgroundResource(R.drawable.filter_sub_list_item);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TATwinListFilterDialog
 * JD-Core Version:    0.6.0
 */