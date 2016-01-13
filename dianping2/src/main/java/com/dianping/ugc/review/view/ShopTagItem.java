package com.dianping.ugc.review.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class ShopTagItem extends FrameLayout
{
  private boolean checked;
  private TextView name;
  private View selectIcon;

  public ShopTagItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public ShopTagItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public String getName()
  {
    return this.name.getText().toString();
  }

  public boolean isChecked()
  {
    return this.checked;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.name = ((TextView)findViewById(R.id.name));
    this.selectIcon = findViewById(R.id.icon);
  }

  public void setChecked(boolean paramBoolean)
  {
    this.checked = paramBoolean;
    View localView = this.selectIcon;
    if (paramBoolean);
    for (int i = 0; ; i = 4)
    {
      localView.setVisibility(i);
      return;
    }
  }

  public void setData(String paramString, boolean paramBoolean)
  {
    setName(paramString);
    setChecked(paramBoolean);
  }

  public void setName(String paramString)
  {
    this.name.setText(paramString);
    if (paramString == null)
    {
      setBackgroundResource(R.drawable.shop_tag_item_add);
      this.selectIcon.setVisibility(4);
      return;
    }
    setBackgroundResource(R.drawable.shop_tag_item_bg);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.view.ShopTagItem
 * JD-Core Version:    0.6.0
 */