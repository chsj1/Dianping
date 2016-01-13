package com.dianping.search.deallist.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class WarningTitleTipViewItem extends NovaLinearLayout
  implements ViewItemInterface
{
  TextView textView;

  public WarningTitleTipViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public WarningTitleTipViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ViewItemType getType()
  {
    return ViewItemType.WARNING_TITLE_TIP;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.textView = ((TextView)findViewById(R.id.title));
  }

  public void setData(DPObject paramDPObject)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject));
    do
      return;
    while (TextUtils.isEmpty(paramDPObject.getString("Title")));
    this.textView.setText(paramDPObject.getString("Title"));
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      setData(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.WarningTitleTipViewItem
 * JD-Core Version:    0.6.0
 */