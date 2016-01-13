package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import java.util.ArrayList;

public class WordGroupLayout extends LinearLayout
{
  static final int BUTTON_MARGIN = 5;
  private static final int MAX_LINE = 2;
  private int mButtonBackgroudResId = R.drawable.tab_button_background;
  private int mButtonFirstLevelTextColorResId = R.color.text_color_orange;
  private View.OnClickListener mButtonListener;
  private int mButtonMargin = -1;
  private LinearLayout.LayoutParams mButtonParams;
  private int mButtonSecondLevelTextColorResId = R.color.light_gray;
  private int mButtonTextSize = 16;
  private int mButtonThirdLevelTextColorResId = R.color.text_color_black;
  private boolean mIsDifferentColorEnabled = false;
  private int mLine = 0;
  private int mMarginLeft = ViewUtils.dip2px(getContext(), 13.0F);
  private int mMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
  String mStatisticsAction;
  String mStatisticsCategory;
  private LinearLayout.LayoutParams mSubLayouParams;
  private ArrayList<DPObject> mTabList = new ArrayList();
  private int maxCount = -1;

  public WordGroupLayout(Context paramContext)
  {
    super(paramContext);
    setOrientation(1);
  }

  public WordGroupLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
  }

  private LinearLayout.LayoutParams getButtonParams()
  {
    if (this.mButtonParams == null)
    {
      this.mButtonParams = new LinearLayout.LayoutParams(-2, -2);
      this.mButtonParams.gravity = 1;
      if (this.mButtonMargin != -1)
        break label54;
      this.mButtonParams.setMargins(5, 5, 5, 5);
    }
    while (true)
    {
      return this.mButtonParams;
      label54: this.mButtonParams.setMargins(this.mButtonMargin, this.mButtonMargin, this.mButtonMargin, this.mButtonMargin);
    }
  }

  private int getLayoutWidth()
  {
    return ViewUtils.getScreenWidthPixels(getContext()) - this.mMarginLeft;
  }

  private LinearLayout getSubLayout()
  {
    this.mLine += 1;
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setLayoutParams(getSubLayoutParams());
    localLinearLayout.setOrientation(0);
    addView(localLinearLayout);
    return localLinearLayout;
  }

  private LinearLayout.LayoutParams getSubLayoutParams()
  {
    if (this.mSubLayouParams == null)
    {
      this.mSubLayouParams = new LinearLayout.LayoutParams(-2, -2);
      this.mSubLayouParams.gravity = 3;
      this.mSubLayouParams.leftMargin = this.mMarginLeft;
    }
    return this.mSubLayouParams;
  }

  private int getViewWidth(View paramView)
  {
    paramView.measure(this.mMeasureSpec, this.mMeasureSpec);
    return paramView.getMeasuredWidth();
  }

  private Button setButton(DPObject paramDPObject, int paramInt)
  {
    String str = paramDPObject.getString("Url");
    Button localButton = new Button(getContext());
    localButton.setBackgroundResource(this.mButtonBackgroudResId);
    localButton.setTag(Integer.valueOf(paramInt));
    localButton.setText(paramDPObject.getString("Name"));
    localButton.setTextColor(getResources().getColor(this.mButtonSecondLevelTextColorResId));
    localButton.setTextSize(2, this.mButtonTextSize);
    localButton.setLayoutParams(getButtonParams());
    localButton.setSingleLine(true);
    if (this.mButtonListener != null)
    {
      localButton.setOnClickListener(this.mButtonListener);
      return localButton;
    }
    if (TextUtils.isEmpty(str))
    {
      localButton.setClickable(false);
      return localButton;
    }
    localButton.setOnClickListener(new View.OnClickListener(str, paramDPObject)
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$url));
        try
        {
          WordGroupLayout.this.getContext().startActivity(paramView);
          label28: ((DPActivity)WordGroupLayout.this.getContext()).statisticsEvent(WordGroupLayout.this.mStatisticsCategory, WordGroupLayout.this.mStatisticsAction, this.val$tabDate.getString("Name"), 0);
          return;
        }
        catch (java.lang.Exception paramView)
        {
          break label28;
        }
      }
    });
    return localButton;
  }

  private Button setButton(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    paramDPObject = setButton(paramDPObject, paramInt1);
    if (paramDPObject != null)
      paramDPObject.setTextColor(getResources().getColor(paramInt2));
    return paramDPObject;
  }

  private Button setButton(DPObject paramDPObject, int paramInt1, int paramInt2, int paramInt3)
  {
    paramDPObject = setButton(paramDPObject, paramInt1, paramInt2);
    if (paramDPObject != null)
      paramDPObject.setTextSize(2, paramInt3);
    return paramDPObject;
  }

  private void setView(ArrayList<DPObject> paramArrayList)
  {
    LinearLayout localLinearLayout = getSubLayout();
    int i = getLayoutWidth();
    int j = 0;
    int k;
    label38: Button localButton;
    if ((this.maxCount > 0) && (this.maxCount < paramArrayList.size()))
    {
      k = this.maxCount;
      if (j >= k)
        break label218;
      if (!this.mIsDifferentColorEnabled)
        break label184;
      if (j % 3 != 0)
        break label126;
      localButton = setButton((DPObject)paramArrayList.get(j), j, this.mButtonFirstLevelTextColorResId, 16);
      label81: k = getViewWidth(localButton);
      i -= k + 10;
      if (i < 0)
        break label203;
      localLinearLayout.addView(localButton);
    }
    while (true)
    {
      j += 1;
      break;
      k = paramArrayList.size();
      break label38;
      label126: if (j % 3 == 1)
      {
        localButton = setButton((DPObject)paramArrayList.get(j), j, this.mButtonSecondLevelTextColorResId, 14);
        break label81;
      }
      localButton = setButton((DPObject)paramArrayList.get(j), j, this.mButtonThirdLevelTextColorResId, 12);
      break label81;
      label184: localButton = setButton((DPObject)paramArrayList.get(j), j);
      break label81;
      label203: if ((this.maxCount == 0) && (this.mLine >= 2))
        label218: return;
      localLinearLayout = getSubLayout();
      i = getLayoutWidth() - (k + 10);
      localLinearLayout.addView(localButton);
    }
  }

  public void enableWordCloudEffect()
  {
    this.mIsDifferentColorEnabled = true;
  }

  public void setButtonBackgroudResId(int paramInt)
  {
    this.mButtonBackgroudResId = paramInt;
  }

  public void setButtonMargin(int paramInt)
  {
    this.mButtonMargin = paramInt;
  }

  public void setTabCountLimit(int paramInt)
  {
    this.maxCount = paramInt;
  }

  public void setTabList(ArrayList<DPObject> paramArrayList)
  {
    this.mTabList.clear();
    this.mTabList.addAll(paramArrayList);
    removeAllViews();
    this.mLine = 0;
    setView(paramArrayList);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.WordGroupLayout
 * JD-Core Version:    0.6.0
 */