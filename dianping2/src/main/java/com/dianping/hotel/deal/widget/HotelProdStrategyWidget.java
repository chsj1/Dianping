package com.dianping.hotel.deal.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HotelProdStrategyWidget extends NovaLinearLayout
{
  protected static final int SCREEN_WIDTH_LIMIT = 480;
  protected CharSequence mCountStr;
  TextView mCountView;
  protected LinearLayout mSaleCountContainer;
  ArrayList<TextAndWeight> mSupportArray = new ArrayList();
  protected LinearLayout mSupportContainer;
  ArrayList<TextView> mSupportViewArray = new ArrayList();

  public HotelProdStrategyWidget(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotelProdStrategyWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void addLinkedTag(List<TextAndWeight> paramList)
  {
    if (paramList == null)
      return;
    this.mSupportArray.addAll(paramList);
  }

  public TextView createTextView(UiType paramUiType, CharSequence paramCharSequence, String paramString)
  {
    TextView localTextView = new TextView(getContext());
    localTextView.setText(paramCharSequence);
    int i;
    int j;
    if (paramUiType == UiType.SALE_COUNT)
    {
      i = R.drawable.tip_tuan_support_count;
      j = R.color.tuan_common_gray;
    }
    for (int k = R.dimen.refund_support_count_text_size; ; k = R.dimen.refund_support_text_size)
    {
      localTextView.setTextColor(getResources().getColorStateList(j));
      localTextView.setCompoundDrawablesWithIntrinsicBounds(i, 0, 0, 0);
      localTextView.setTextSize(0, getContext().getResources().getDimension(k));
      localTextView.setCompoundDrawablePadding((int)getResources().getDimension(R.dimen.refund_support_icon_padding));
      localTextView.setPadding(0, 0, (int)getResources().getDimension(R.dimen.refund_support_text_padding_right), 0);
      localTextView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
      localTextView.setSingleLine();
      localTextView.setGravity(16);
      localTextView.setOnClickListener(new View.OnClickListener(paramString)
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(this.val$url))
            HotelProdStrategyWidget.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.val$url)));
        }
      });
      return localTextView;
      i = R.drawable.tip_tuan_support_on;
      j = R.color.tuan_common_green;
    }
  }

  protected void createView()
  {
    Object localObject1 = this.mSupportArray.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (TextAndWeight)((Iterator)localObject1).next();
      localObject2 = createTextView(UiType.SUPPORT, ((TextAndWeight)localObject2).text, ((TextAndWeight)localObject2).url);
      this.mSupportContainer.addView((View)localObject2);
      this.mSupportViewArray.add(localObject2);
    }
    localObject1 = new View(getContext());
    Object localObject2 = new LinearLayout.LayoutParams(0, 0);
    ((LinearLayout.LayoutParams)localObject2).weight = 1.0F;
    ((View)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
    this.mSupportContainer.addView((View)localObject1);
    if (!TextUtils.isEmpty(this.mCountStr))
    {
      this.mCountView = createTextView(UiType.SALE_COUNT, this.mCountStr, null);
      this.mSaleCountContainer.addView(this.mCountView);
    }
  }

  protected ScreenType getScreenType()
  {
    if (getResources().getDisplayMetrics().widthPixels > 480)
      return ScreenType.BIG;
    return ScreenType.SMALL;
  }

  protected void initContainer()
  {
    if (getScreenType() == ScreenType.BIG)
    {
      this.mSupportContainer = this;
      this.mSaleCountContainer = this;
      setOrientation(0);
      removeAllViews();
      return;
    }
    setOrientation(1);
    removeAllViews();
    this.mSupportContainer = new LinearLayout(getContext());
    this.mSupportContainer.setOrientation(0);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams.setMargins(0, 0, 0, ViewUtils.dip2px(getContext(), 5.0F));
    this.mSupportContainer.setLayoutParams(localLayoutParams);
    addView(this.mSupportContainer);
    this.mSaleCountContainer = new LinearLayout(getContext());
    this.mSaleCountContainer.setOrientation(0);
    localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    this.mSaleCountContainer.setLayoutParams(localLayoutParams);
    addView(this.mSaleCountContainer);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt3 = this.mSupportContainer.getWidth() - this.mSupportContainer.getPaddingLeft() - this.mSupportContainer.getPaddingRight();
    paramInt1 = 0;
    if (this.mCountView != null)
      paramInt1 = ViewUtils.getViewWidth(this.mCountView);
    paramInt2 = paramInt3;
    if (getScreenType() == ScreenType.BIG)
      paramInt2 = paramInt3 - paramInt1;
    paramInt1 = 0;
    Object localObject = this.mSupportViewArray.iterator();
    while (((Iterator)localObject).hasNext())
    {
      TextView localTextView = (TextView)((Iterator)localObject).next();
      if (localTextView == null)
        continue;
      paramInt1 += ViewUtils.getViewWidth(localTextView);
      localTextView.setVisibility(0);
    }
    paramInt4 = this.mSupportViewArray.size();
    paramInt3 = paramInt1;
    paramInt1 = paramInt4;
    while (paramInt1 > 0)
    {
      localObject = (TextView)this.mSupportViewArray.get(paramInt1 - 1);
      paramInt4 = paramInt3;
      if (paramInt3 > paramInt2)
      {
        ((TextView)localObject).setVisibility(8);
        paramInt4 = paramInt3 - ViewUtils.getViewWidth((View)localObject);
      }
      paramInt1 -= 1;
      paramInt3 = paramInt4;
    }
  }

  public void setupTags(List<TextAndWeight> paramList, CharSequence paramCharSequence)
  {
    this.mCountStr = paramCharSequence;
    this.mSupportArray.clear();
    this.mSupportViewArray.clear();
    initContainer();
    addLinkedTag(paramList);
    createView();
  }

  protected static enum ScreenType
  {
    static
    {
      $VALUES = new ScreenType[] { BIG, SMALL };
    }
  }

  public static class TextAndWeight
  {
    public CharSequence text;
    public String url;
    public int weight;

    public TextAndWeight(String paramString1, String paramString2, int paramInt)
    {
      this.text = paramString1;
      this.url = paramString2;
      this.weight = paramInt;
    }
  }

  public static enum UiType
  {
    static
    {
      $VALUES = new UiType[] { SALE_COUNT, SUPPORT };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.widget.HotelProdStrategyWidget
 * JD-Core Version:    0.6.0
 */