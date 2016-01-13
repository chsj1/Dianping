package com.dianping.membercard.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.membercard.utils.ProductType;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.HashSet;
import java.util.Set;

public class TwoLineListItemView extends LinearLayout
{
  protected ImageView arrowImageView = (ImageView)findViewById(R.id.arrow_icon);
  protected ImageView bottomDividerView;
  protected int cardType = 1;
  protected TextView descTextView = (TextView)findViewById(R.id.product_desc);
  protected View dividerView = findViewById(R.id.list_divider);
  protected boolean isExpanded = false;
  protected boolean isStyleChanged = false;
  protected boolean isVisible;
  protected Set<ItemStyle> mStyles = new HashSet();
  protected View mTitleView = findViewById(R.id.first_line);
  protected View mainView = LayoutInflater.from(getContext()).inflate(inflateResource(), this, true);
  protected TextView nameTextView = (TextView)findViewById(R.id.product_name);
  protected int productID;
  protected ImageView typeImageView = (ImageView)findViewById(R.id.product_icon);

  public TwoLineListItemView(Context paramContext)
  {
    this(paramContext, null);
  }

  public TwoLineListItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public TwoLineListItemView configStyles(ItemStyle[] paramArrayOfItemStyle)
  {
    mergeStyles(paramArrayOfItemStyle);
    if (!this.isStyleChanged);
    int j;
    int i;
    do
    {
      return this;
      j = paramArrayOfItemStyle.length;
      i = 0;
    }
    while (i >= j);
    ItemStyle localItemStyle = paramArrayOfItemStyle[i];
    switch (2.$SwitchMap$com$dianping$membercard$view$TwoLineListItemView$ItemStyle[localItemStyle.ordinal()])
    {
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    }
    while (true)
    {
      i += 1;
      break;
      setTitleClickable(true);
      setExpandableState(true);
      setOnClickTitleListener(null);
      continue;
      setTitleClickable(true);
      setExpandableState(false);
      setOnClickTitleListener(null);
      continue;
      setTitleClickable(true);
      setArrowStyle(ItemStyle.JUMPABLE);
      continue;
      setTitleClickable(false);
      setArrowStyle(ItemStyle.ARROW_GONE);
      continue;
      setIconVisibility(localItemStyle);
      continue;
      setArrowStyle(ItemStyle.ARROW_GONE);
    }
  }

  protected int inflateResource()
  {
    return R.layout.card_product_list_two_item;
  }

  public boolean isExpanded()
  {
    return this.isExpanded;
  }

  protected void mergeStyles(ItemStyle[] paramArrayOfItemStyle)
  {
    this.isStyleChanged = false;
    int j = paramArrayOfItemStyle.length;
    int i = 0;
    while (i < j)
    {
      ItemStyle localItemStyle = paramArrayOfItemStyle[i];
      if (this.mStyles.contains(localItemStyle))
      {
        i += 1;
        continue;
      }
      this.isStyleChanged = true;
      if ((localItemStyle == ItemStyle.EXPANDABLE_OPENED) || (localItemStyle == ItemStyle.EXPANDABLE_CLOSED) || (localItemStyle == ItemStyle.UNCLICKABLE) || (localItemStyle == ItemStyle.JUMPABLE) || (localItemStyle == ItemStyle.ARROW_GONE))
      {
        this.mStyles.remove(ItemStyle.EXPANDABLE_OPENED);
        this.mStyles.remove(ItemStyle.EXPANDABLE_CLOSED);
        this.mStyles.remove(ItemStyle.UNCLICKABLE);
        this.mStyles.remove(ItemStyle.JUMPABLE);
        this.mStyles.remove(ItemStyle.ARROW_GONE);
      }
      while (true)
      {
        this.mStyles.add(localItemStyle);
        break;
        if ((localItemStyle != ItemStyle.ICON_VISIABLE) && (localItemStyle != ItemStyle.ICON_GONE))
          continue;
        this.mStyles.remove(ItemStyle.ICON_VISIABLE);
        this.mStyles.remove(ItemStyle.ICON_GONE);
      }
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
  }

  protected void setArrowStyle(ItemStyle paramItemStyle)
  {
    switch (2.$SwitchMap$com$dianping$membercard$view$TwoLineListItemView$ItemStyle[paramItemStyle.ordinal()])
    {
    case 4:
    case 5:
    case 6:
    default:
      return;
    case 2:
      this.arrowImageView.setVisibility(0);
      this.arrowImageView.setBackgroundResource(R.drawable.mc_arrow_up);
      return;
    case 1:
      this.arrowImageView.setVisibility(0);
      this.arrowImageView.setBackgroundResource(R.drawable.mc_arrow_down);
      return;
    case 3:
      this.arrowImageView.setVisibility(0);
      this.arrowImageView.setImageResource(R.drawable.mc_arrow_right);
      return;
    case 7:
    }
    this.arrowImageView.setVisibility(8);
  }

  public TwoLineListItemView setDescription(CharSequence paramCharSequence)
  {
    Object localObject = paramCharSequence;
    if (TextUtils.isEmpty(paramCharSequence))
      localObject = "";
    this.descTextView.setText((CharSequence)localObject);
    return (TwoLineListItemView)this;
  }

  public TwoLineListItemView setExpandableState(boolean paramBoolean)
  {
    if ((!this.mStyles.contains(ItemStyle.EXPANDABLE_CLOSED)) && (!this.mStyles.contains(ItemStyle.EXPANDABLE_OPENED)))
      return this;
    this.isExpanded = paramBoolean;
    if (paramBoolean)
    {
      this.descTextView.setVisibility(0);
      this.dividerView.setVisibility(0);
      setArrowStyle(ItemStyle.EXPANDABLE_CLOSED);
      return this;
    }
    this.descTextView.setVisibility(8);
    this.dividerView.setVisibility(8);
    setArrowStyle(ItemStyle.EXPANDABLE_OPENED);
    return this;
  }

  public TwoLineListItemView setIconImage(int paramInt)
  {
    this.typeImageView.setImageResource(paramInt);
    return this;
  }

  public TwoLineListItemView setIconImage(ProductType paramProductType)
  {
    setIconVisibility(ItemStyle.ICON_VISIABLE);
    switch (2.$SwitchMap$com$dianping$membercard$utils$ProductType[paramProductType.ordinal()])
    {
    default:
      return this;
    case 1:
      setIconImage(R.drawable.mc_icon_welife_gifts);
      return this;
    case 2:
      setIconImage(R.drawable.mc_icon_welife_diamond);
      return this;
    case 3:
      setIconImage(R.drawable.mc_icon_suspend);
      return this;
    case 4:
      setIconImage(R.drawable.mc_icon_point);
      return this;
    case 5:
      setIconImage(R.drawable.mc_icon_times);
      return this;
    case 6:
      setIconImage(R.drawable.mc_icon_times);
      return this;
    case 7:
      setIconImage(R.drawable.mc_icon_feed);
      return this;
    case 8:
      setIconImage(R.drawable.mc_icon_score);
      return this;
    case 9:
      setIconImage(R.drawable.mc_icon_welife_gifts);
      return this;
    case 10:
    }
    setIconImage(R.drawable.mc_icon_welife_diamond);
    return this;
  }

  protected void setIconVisibility(ItemStyle paramItemStyle)
  {
    switch (2.$SwitchMap$com$dianping$membercard$view$TwoLineListItemView$ItemStyle[paramItemStyle.ordinal()])
    {
    default:
      return;
    case 5:
      this.typeImageView.setVisibility(0);
      return;
    case 6:
    }
    this.typeImageView.setVisibility(8);
  }

  public TwoLineListItemView setOnClickTitleListener(View.OnClickListener paramOnClickListener)
  {
    setOnClickListener(new View.OnClickListener(paramOnClickListener)
    {
      public void onClick(View paramView)
      {
        if ((TwoLineListItemView.this.mStyles.contains(TwoLineListItemView.ItemStyle.EXPANDABLE_CLOSED)) || (TwoLineListItemView.this.mStyles.contains(TwoLineListItemView.ItemStyle.EXPANDABLE_OPENED)))
          TwoLineListItemView.this.switchExpandState();
        if (this.val$listener != null)
          this.val$listener.onClick(paramView);
      }
    });
    return this;
  }

  public TwoLineListItemView setTitle(CharSequence paramCharSequence)
  {
    Object localObject = paramCharSequence;
    if (TextUtils.isEmpty(paramCharSequence))
      localObject = "";
    this.nameTextView.setText((CharSequence)localObject);
    return (TwoLineListItemView)this;
  }

  protected void setTitleClickable(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setEnabled(true);
      setClickable(false);
      this.mainView.setBackgroundResource(R.drawable.mc_listview_item_background_rectangle_single);
      return;
    }
    setEnabled(false);
    setClickable(true);
    this.mainView.setBackgroundColor(0);
  }

  public void switchExpandState()
  {
    if (!this.isExpanded);
    for (boolean bool = true; ; bool = false)
    {
      this.isExpanded = bool;
      setExpandableState(this.isExpanded);
      return;
    }
  }

  public static enum ItemStyle
  {
    static
    {
      EXPANDABLE_CLOSED = new ItemStyle("EXPANDABLE_CLOSED", 1);
      UNCLICKABLE = new ItemStyle("UNCLICKABLE", 2);
      JUMPABLE = new ItemStyle("JUMPABLE", 3);
      ICON_VISIABLE = new ItemStyle("ICON_VISIABLE", 4);
      ICON_GONE = new ItemStyle("ICON_GONE", 5);
      ARROW_GONE = new ItemStyle("ARROW_GONE", 6);
      $VALUES = new ItemStyle[] { EXPANDABLE_OPENED, EXPANDABLE_CLOSED, UNCLICKABLE, JUMPABLE, ICON_VISIABLE, ICON_GONE, ARROW_GONE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.TwoLineListItemView
 * JD-Core Version:    0.6.0
 */