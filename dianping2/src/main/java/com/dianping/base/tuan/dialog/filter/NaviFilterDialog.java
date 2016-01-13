package com.dianping.base.tuan.dialog.filter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;

public class NaviFilterDialog extends FilterDialog
{
  public NaviFilterDialog(Activity paramActivity)
  {
    this(paramActivity, null, null);
  }

  public NaviFilterDialog(Activity paramActivity, DPObject paramDPObject, FilterDialog.OnFilterListener paramOnFilterListener)
  {
    super(paramActivity);
    setOnFilterListener(paramOnFilterListener);
    setFilterView(setupView(paramDPObject));
  }

  private View setupView(DPObject paramDPObject)
  {
    paramDPObject = new NaviContainer(getContext(), null, paramDPObject);
    paramDPObject.setDialog(this);
    paramDPObject.setOutListener(this.listener);
    paramDPObject.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    return paramDPObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.dialog.filter.NaviFilterDialog
 * JD-Core Version:    0.6.0
 */