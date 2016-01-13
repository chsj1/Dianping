package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class BaseMulDeletableItem extends NovaLinearLayout
{
  protected CheckBox checkBox;

  public BaseMulDeletableItem(Context paramContext)
  {
    super(paramContext);
  }

  public BaseMulDeletableItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.checkBox = ((CheckBox)findViewById(R.id.check));
  }

  public void setChecked(int paramInt)
  {
    CheckBox localCheckBox = this.checkBox;
    if (paramInt > 0);
    for (boolean bool = true; ; bool = false)
    {
      localCheckBox.setChecked(bool);
      return;
    }
  }

  public void setEditable(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.checkBox.setVisibility(0);
      return;
    }
    this.checkBox.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.BaseMulDeletableItem
 * JD-Core Version:    0.6.0
 */