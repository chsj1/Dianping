package com.dianping.base.tuan.widget;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.styleable;

public class RMBLabelItem extends LinearLayout
  implements OverFlowedDetectableTextView.OnTextViewOverFlowed
{
  public static final boolean DEFAULT_EXIST_CENTERLINE = false;
  public static final int DEFAULT_PRICE_STYLE = 6;
  private static final int DEFAULT_TEXT_COLOR = -13421773;
  public static final int DEFAULT_USAGE = 2;
  private static final int DEFAULT_USAGE_COMMON_COLOR = -13421773;
  public static final int DEFAULT_USAGE_NOW_COLOR = -39373;
  private static final int DEFAULT_USAGE_ORIGIN_COLOR = -6710887;
  public static final double INVISIBLE_VALUE = 1.7976931348623157E+308D;
  public static final int STYLE_PRICE_1 = 1;
  public static final int STYLE_PRICE_2 = 2;
  public static final int STYLE_PRICE_3 = 3;
  public static final int STYLE_PRICE_4 = 4;
  public static final int STYLE_PRICE_5 = 5;
  public static final int STYLE_PRICE_6 = 6;
  public static final int USAGE_BOTH = 3;
  public static final int USAGE_NOW = 2;
  public static final int USAGE_ORIGIN = 1;
  RMBLabelItem.RMBLabel nowPriceLabel;
  RMBLabelItem.RMBLabel originPriceLabel;
  int usageValue = 2;

  public RMBLabelItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public RMBLabelItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setupView(paramAttributeSet);
  }

  private float getFullTextWidth(boolean paramBoolean)
  {
    float f2 = 0.0F;
    float f1 = f2;
    LinearLayout.LayoutParams localLayoutParams;
    if (this.nowPriceLabel != null)
    {
      f1 = f2;
      if (this.nowPriceLabel.getVisibility() != 8)
      {
        localLayoutParams = (LinearLayout.LayoutParams)this.nowPriceLabel.getLayoutParams();
        f1 = this.nowPriceLabel.measureFullTextWidth() + localLayoutParams.leftMargin + localLayoutParams.rightMargin;
      }
    }
    f2 = f1;
    float f3;
    if (this.originPriceLabel != null)
    {
      f2 = f1;
      if (this.originPriceLabel.getVisibility() != 8)
      {
        localLayoutParams = (LinearLayout.LayoutParams)this.originPriceLabel.getLayoutParams();
        f3 = this.originPriceLabel.measureFullTextWidth() + localLayoutParams.leftMargin + localLayoutParams.rightMargin;
        if (!paramBoolean)
          break label128;
        f2 = f1 + f3;
      }
    }
    return f2;
    label128: f2 = f1;
    if (f3 > f1)
      f2 = f3;
    return f2;
  }

  private ViewGroup.MarginLayoutParams getMargins(int paramInt1, int paramInt2)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = new ViewGroup.MarginLayoutParams(-2, -2);
    localMarginLayoutParams.setMargins(0, 0, 0, 0);
    paramInt1 = RMBLabelItem.RMBLabel.access$000(paramInt1, paramInt2);
    TypedArray localTypedArray = getContext().obtainStyledAttributes(paramInt1, R.styleable.RMBLabelStyle);
    if ((localTypedArray == null) || (localTypedArray.length() <= 0))
    {
      if (localTypedArray != null)
        localTypedArray.recycle();
      return localMarginLayoutParams;
    }
    int j = ViewUtils.dip2px(getContext(), 0.0F);
    paramInt1 = localTypedArray.getDimensionPixelSize(R.styleable.RMBLabelStyle_label_margin_left, j);
    paramInt2 = localTypedArray.getDimensionPixelSize(R.styleable.RMBLabelStyle_label_margin_right, j);
    int i = localTypedArray.getDimensionPixelSize(R.styleable.RMBLabelStyle_label_margin_bottom, j);
    j = localTypedArray.getDimensionPixelSize(R.styleable.RMBLabelStyle_label_margin_top, j);
    if (localTypedArray != null)
      localTypedArray.recycle();
    localMarginLayoutParams.setMargins(paramInt1, j, paramInt2, i);
    return localMarginLayoutParams;
  }

  private void setLayoutParams()
  {
    LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams1.gravity = 80;
    setLayoutParams(localLayoutParams1);
    setOrientation(0);
    this.nowPriceLabel = new RMBLabelItem.RMBLabel(getContext());
    this.originPriceLabel = new RMBLabelItem.RMBLabel(getContext());
    localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams1.gravity = 80;
    LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams2.gravity = 80;
    addView(this.nowPriceLabel, localLayoutParams1);
    addView(this.originPriceLabel, localLayoutParams2);
  }

  private void setRMBLabelStyle(int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2)
  {
    if (paramInt1 == 6)
      paramInt2 = 2;
    this.usageValue = paramInt2;
    if (paramInt2 == 3)
    {
      this.nowPriceLabel.setStyle(paramInt1, 2, paramBoolean1, paramInt3, paramBoolean2);
      this.originPriceLabel.setStyle(paramInt1, 1, paramBoolean1, paramInt3, paramBoolean2);
    }
    while (true)
    {
      if (paramInt2 == 3)
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams = getMargins(paramInt1, 2);
        ((LinearLayout.LayoutParams)this.nowPriceLabel.getLayoutParams()).setMargins(localMarginLayoutParams.leftMargin, localMarginLayoutParams.topMargin, localMarginLayoutParams.rightMargin, localMarginLayoutParams.bottomMargin);
        localMarginLayoutParams = getMargins(paramInt1, 1);
        ((LinearLayout.LayoutParams)this.originPriceLabel.getLayoutParams()).setMargins(localMarginLayoutParams.leftMargin, localMarginLayoutParams.topMargin, localMarginLayoutParams.rightMargin, localMarginLayoutParams.bottomMargin);
      }
      return;
      if (paramInt2 == 1)
      {
        this.originPriceLabel.setStyle(paramInt1, 1, paramBoolean1, paramInt3, paramBoolean2);
        continue;
      }
      this.nowPriceLabel.setStyle(paramInt1, 2, paramBoolean1, paramInt3, paramBoolean2);
    }
  }

  private void setupView(AttributeSet paramAttributeSet)
  {
    setLayoutParams();
    this.nowPriceLabel.setVisibility(8);
    this.originPriceLabel.setVisibility(8);
    this.originPriceLabel.setOnTextViewOverFlowed(this);
    if (paramAttributeSet == null);
    while (true)
    {
      return;
      paramAttributeSet = getContext().getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.RMBLabelItem, -1, 0);
      if (paramAttributeSet == null)
        continue;
      try
      {
        if (paramAttributeSet.length() <= 0)
          continue;
        int i = paramAttributeSet.getInt(R.styleable.RMBLabelItem_usage, 2);
        setRMBLabelStyle(paramAttributeSet.getInt(R.styleable.RMBLabelItem_style_price, 6), i, paramAttributeSet.getBoolean(R.styleable.RMBLabelItem_rmb_usage_now_centerline, false), paramAttributeSet.getInt(R.styleable.RMBLabelItem_rmb_usage_now_color, -39373));
        if (paramAttributeSet == null)
          continue;
        paramAttributeSet.recycle();
        return;
      }
      catch (Exception localException)
      {
        while (true)
          Log.e("RMBLabelItem", localException.getMessage());
      }
    }
  }

  public int getBaseline()
  {
    RMBLabelItem.RMBLabel localRMBLabel2 = this.nowPriceLabel;
    RMBLabelItem.RMBLabel localRMBLabel1 = localRMBLabel2;
    if (this.originPriceLabel.getBaseline() > localRMBLabel2.getBaseline())
      localRMBLabel1 = this.originPriceLabel;
    return ((LinearLayout.LayoutParams)localRMBLabel1.getLayoutParams()).topMargin + localRMBLabel1.getBaseline();
  }

  public float getFullTextWidth()
  {
    if (getOrientation() == 0);
    for (boolean bool = true; ; bool = false)
      return getFullTextWidth(bool);
  }

  public void onTextViewOverFlowed(OverFlowedDetectableTextView paramOverFlowedDetectableTextView, boolean paramBoolean)
  {
  }

  public void setRMBLabelStyle(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    setRMBLabelStyle(paramInt1, paramInt2, paramBoolean, paramInt3, false);
  }

  public void setRMBLabelStyle6(boolean paramBoolean, int paramInt)
  {
    setRMBLabelStyle(6, 2, paramBoolean, paramInt, true);
  }

  public void setRMBLabelValue(double paramDouble)
  {
    if (this.usageValue == 1)
    {
      setRMBLabelValue(1.7976931348623157E+308D, paramDouble);
      return;
    }
    setRMBLabelValue(paramDouble, 1.7976931348623157E+308D);
  }

  public void setRMBLabelValue(double paramDouble1, double paramDouble2)
  {
    this.nowPriceLabel.setVisibility(8);
    this.originPriceLabel.setVisibility(8);
    if (((this.usageValue == 3) || (this.usageValue == 2)) && (paramDouble1 != 1.7976931348623157E+308D))
    {
      this.nowPriceLabel.setVisibility(0);
      this.nowPriceLabel.setMoney(paramDouble1);
    }
    if (((this.usageValue == 3) || (this.usageValue == 1)) && (paramDouble2 != 1.7976931348623157E+308D))
    {
      this.originPriceLabel.setVisibility(0);
      this.originPriceLabel.setMoney(paramDouble2);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.widget.RMBLabelItem
 * JD-Core Version:    0.6.0
 */