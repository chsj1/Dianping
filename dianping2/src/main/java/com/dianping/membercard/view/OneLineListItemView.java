package com.dianping.membercard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.membercard.utils.ProductType;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.HashSet;
import java.util.Set;

public class OneLineListItemView extends NovaLinearLayout
{
  private boolean isStyleChanged = false;
  private ImageView mArrowImage;
  private ImageView mItemIcon;
  private RelativeLayout mRightContainer;
  private Set<ItemStyle> mStyles = new HashSet();
  private TextView mTitleText;

  public OneLineListItemView(Context paramContext)
  {
    super(paramContext);
    setupView(paramContext);
  }

  public OneLineListItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setupView(paramContext);
  }

  private void setupView(Context paramContext)
  {
    LayoutInflater.from(getContext()).inflate(R.layout.card_product_line_one_item, this, true);
    this.mRightContainer = ((RelativeLayout)findViewById(R.id.rightContainer));
    this.mArrowImage = ((ImageView)findViewById(R.id.arrowImage));
    this.mItemIcon = ((ImageView)findViewById(R.id.itemIcon));
    this.mTitleText = ((TextView)findViewById(R.id.titleText));
  }

  public OneLineListItemView configStyles(ItemStyle[] paramArrayOfItemStyle)
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
    switch (1.$SwitchMap$com$dianping$membercard$view$OneLineListItemView$ItemStyle[localItemStyle.ordinal()])
    {
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    }
    while (true)
    {
      i += 1;
      break;
      setIconVisibility(8);
      continue;
      setIconVisibility(0);
      continue;
      setArrowImageVisibility(8);
      continue;
      setArrowImageVisibility(0);
      continue;
      setEnabled(true);
      setArrowImage(R.drawable.mc_arrow_right);
      continue;
      setEnabled(false);
    }
  }

  public RelativeLayout getContainer()
  {
    return this.mRightContainer;
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
      if ((localItemStyle == ItemStyle.ICON_GONE) || (localItemStyle == ItemStyle.ICON_VISIABLE))
      {
        this.mStyles.remove(ItemStyle.ICON_GONE);
        this.mStyles.remove(ItemStyle.ICON_VISIABLE);
      }
      while (true)
      {
        this.mStyles.add(localItemStyle);
        break;
        if ((localItemStyle != ItemStyle.ARROW_GONE) && (localItemStyle != ItemStyle.ARROW_VISIABLE) && (localItemStyle != ItemStyle.ARROW_JUMPABLE))
          continue;
        this.mStyles.remove(ItemStyle.ARROW_GONE);
        this.mStyles.remove(ItemStyle.ARROW_VISIABLE);
        this.mStyles.remove(ItemStyle.ARROW_JUMPABLE);
      }
    }
  }

  public OneLineListItemView setArrowImage(int paramInt)
  {
    setArrowImageVisibility(0);
    this.mArrowImage.setImageResource(paramInt);
    return this;
  }

  public OneLineListItemView setArrowImageVisibility(int paramInt)
  {
    this.mArrowImage.setVisibility(paramInt);
    return this;
  }

  public OneLineListItemView setIconImage(int paramInt)
  {
    this.mItemIcon.setImageResource(paramInt);
    return this;
  }

  public OneLineListItemView setIconImage(ProductType paramProductType)
  {
    setIconVisibility(0);
    switch (1.$SwitchMap$com$dianping$membercard$utils$ProductType[paramProductType.ordinal()])
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
    }
    setIconImage(R.drawable.mc_icon_score);
    return this;
  }

  public OneLineListItemView setIconVisibility(int paramInt)
  {
    this.mItemIcon.setVisibility(paramInt);
    return this;
  }

  public OneLineListItemView setTitleText(CharSequence paramCharSequence)
  {
    this.mTitleText.setText(paramCharSequence);
    return this;
  }

  public static enum ItemStyle
  {
    static
    {
      ARROW_GONE = new ItemStyle("ARROW_GONE", 2);
      ARROW_VISIABLE = new ItemStyle("ARROW_VISIABLE", 3);
      ARROW_JUMPABLE = new ItemStyle("ARROW_JUMPABLE", 4);
      UNCLICKABLE = new ItemStyle("UNCLICKABLE", 5);
      $VALUES = new ItemStyle[] { ICON_GONE, ICON_VISIABLE, ARROW_GONE, ARROW_VISIABLE, ARROW_JUMPABLE, UNCLICKABLE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.OneLineListItemView
 * JD-Core Version:    0.6.0
 */