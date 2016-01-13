package com.dianping.search.deallist.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.widget.view.NovaTextView;

public class TitleTipViewItem extends NovaTextView
  implements ViewItemInterface
{
  public TitleTipViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public TitleTipViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ViewItemType getType()
  {
    return ViewItemType.TITLE_TIP;
  }

  protected void setData(DPObject paramDPObject)
  {
    setText(paramDPObject.getString("Title"));
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
      setData(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.TitleTipViewItem
 * JD-Core Version:    0.6.0
 */