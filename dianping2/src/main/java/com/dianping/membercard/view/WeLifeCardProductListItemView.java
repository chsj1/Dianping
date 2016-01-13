package com.dianping.membercard.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.v1.R.layout;
import java.util.Set;

public class WeLifeCardProductListItemView extends TwoLineListItemView
{
  public WeLifeCardProductListItemView(Context paramContext)
  {
    super(paramContext);
  }

  public WeLifeCardProductListItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected int inflateResource()
  {
    return R.layout.card_product_list_two_item_for_welife;
  }

  public WeLifeCardProductListItemView setExpandableState(boolean paramBoolean)
  {
    if ((!this.mStyles.contains(TwoLineListItemView.ItemStyle.EXPANDABLE_CLOSED)) && (!this.mStyles.contains(TwoLineListItemView.ItemStyle.EXPANDABLE_OPENED)))
      return this;
    this.isExpanded = paramBoolean;
    int i;
    if (paramBoolean)
    {
      TextView localTextView = this.descTextView;
      if (TextUtils.isEmpty(this.descTextView.getText()))
      {
        i = 8;
        localTextView.setVisibility(i);
        setArrowStyle(TwoLineListItemView.ItemStyle.EXPANDABLE_CLOSED);
      }
    }
    while (true)
    {
      this.dividerView.setVisibility(8);
      return this;
      i = 0;
      break;
      this.descTextView.setVisibility(8);
      setArrowStyle(TwoLineListItemView.ItemStyle.EXPANDABLE_OPENED);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.WeLifeCardProductListItemView
 * JD-Core Version:    0.6.0
 */