package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

@Deprecated
public class CustomTabHost extends TabHost
  implements CompoundButton.OnCheckedChangeListener
{
  private RadioGroup group;
  private LayoutInflater inflater = LayoutInflater.from(getContext());

  public CustomTabHost(Context paramContext)
  {
    this(paramContext, null);
  }

  public CustomTabHost(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void addTab(TabHost.TabSpec paramTabSpec)
  {
    if (this.group == null)
      this.group = ((RadioGroup)findViewById(R.id.tab_group));
    RadioButton localRadioButton = (RadioButton)this.inflater.inflate(R.layout.custom_tab_button, this.group, false);
    this.group.addView(localRadioButton);
    super.addTab(paramTabSpec);
    Object localObject = getTabWidget().getChildAt(getTabWidget().getChildCount() - 1);
    paramTabSpec = (TextView)((View)localObject).findViewById(16908310);
    localObject = (ImageView)((View)localObject).findViewById(16908294);
    localRadioButton.setText(paramTabSpec.getText());
    if ((localObject != null) && (((ImageView)localObject).getDrawable() != null))
    {
      localRadioButton.setTextSize(0, getResources().getDimension(R.dimen.text_small));
      localRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, ((ImageView)localObject).getDrawable(), null, null);
    }
    localRadioButton.setOnCheckedChangeListener(this);
  }

  public View getRadioButton(int paramInt)
  {
    if (this.group != null)
      return this.group.getChildAt(paramInt);
    return null;
  }

  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      int i = 0;
      int j = this.group.getChildCount();
      if (i < j)
      {
        if (paramCompoundButton == this.group.getChildAt(i))
          setCurrentTab(i);
        while (true)
        {
          i += 1;
          break;
          ((CompoundButton)this.group.getChildAt(i)).setChecked(false);
        }
      }
    }
  }

  public void setCurrentTab(int paramInt)
  {
    super.setCurrentTab(paramInt);
    if (this.group == null)
      this.group = ((RadioGroup)findViewById(R.id.tab_group));
    if (this.group.getChildAt(paramInt) != null)
      ((RadioButton)this.group.getChildAt(paramInt)).setChecked(true);
  }

  public void setTabVisibility(int paramInt, boolean paramBoolean)
  {
    View localView = this.group.getChildAt(paramInt);
    if (paramBoolean);
    for (paramInt = 0; ; paramInt = 8)
    {
      localView.setVisibility(paramInt);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CustomTabHost
 * JD-Core Version:    0.6.0
 */