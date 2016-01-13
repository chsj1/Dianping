package com.dianping.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class ViewUtils
{
  private static int screenHeightPixels;
  private static int screenWidthPixels;

  public static int dip2px(Context paramContext, float paramFloat)
  {
    if (paramContext == null)
      return (int)paramFloat;
    return (int)(paramFloat * paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  public static void disableView(View paramView)
  {
    if (paramView == null)
      return;
    paramView.setEnabled(false);
  }

  public static void enableView(View paramView)
  {
    if (paramView == null)
      return;
    paramView.setEnabled(true);
  }

  public static int getScreenHeightPixels(Context paramContext)
  {
    if (paramContext == null)
    {
      Log.e("Can't get screen size while the activity is null!");
      return 0;
    }
    if (screenHeightPixels > 0)
      return screenHeightPixels;
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getMetrics(localDisplayMetrics);
    screenHeightPixels = localDisplayMetrics.heightPixels;
    return screenHeightPixels;
  }

  public static int getScreenWidthPixels(Context paramContext)
  {
    if (paramContext == null)
    {
      Log.e("Can't get screen size while the activity is null!");
      return 0;
    }
    if (screenWidthPixels > 0)
      return screenWidthPixels;
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getMetrics(localDisplayMetrics);
    screenWidthPixels = localDisplayMetrics.widthPixels;
    return screenWidthPixels;
  }

  public static int getTextViewWidth(TextView paramTextView, String paramString)
  {
    paramTextView.setText(paramString);
    paramTextView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    return paramTextView.getMeasuredWidth();
  }

  public static int getTextViewWidth(TextView paramTextView, String paramString, int paramInt)
  {
    paramTextView.setText(paramString);
    paramTextView.setTextSize(2, paramInt);
    paramTextView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    return paramTextView.getMeasuredWidth();
  }

  public static int getViewHeight(View paramView)
  {
    if (paramView == null)
      return 0;
    paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    return paramView.getMeasuredHeight();
  }

  public static int getViewWidth(View paramView)
  {
    if (paramView == null)
      return 0;
    paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    return paramView.getMeasuredWidth();
  }

  public static void hideView(View paramView)
  {
    hideView(paramView, false);
  }

  public static void hideView(View paramView, boolean paramBoolean)
  {
    if (paramView == null)
      return;
    if (paramBoolean)
    {
      paramView.setVisibility(8);
      return;
    }
    paramView.setVisibility(4);
  }

  public static boolean isPointInsideView(float paramFloat1, float paramFloat2, View paramView)
  {
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    int i = arrayOfInt[0];
    int j = arrayOfInt[1];
    return (paramFloat1 > i) && (paramFloat1 < paramView.getWidth() + i) && (paramFloat2 > j) && (paramFloat2 < paramView.getHeight() + j);
  }

  public static boolean isShow(View paramView)
  {
    if (paramView == null);
    do
      return false;
    while (paramView.getVisibility() != 0);
    return true;
  }

  public static int measureTextView(TextView paramTextView)
  {
    if (paramTextView == null)
      return -1;
    return (int)paramTextView.getPaint().measureText(paramTextView.getText().toString());
  }

  public static int px2dip(Context paramContext, float paramFloat)
  {
    if (paramContext == null)
      return (int)paramFloat;
    return (int)(paramFloat / paramContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  public static void readOnlyView(EditText paramEditText)
  {
    if (paramEditText == null)
      return;
    paramEditText.setKeyListener(null);
  }

  public static void setVisibilityAndContent(TextView paramTextView, String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      paramTextView.setText(paramString);
      paramTextView.setVisibility(0);
      return;
    }
    paramTextView.setVisibility(8);
  }

  public static void showView(View paramView)
  {
    if (paramView == null)
      return;
    paramView.setVisibility(0);
  }

  public static void showView(View paramView, boolean paramBoolean)
  {
    showView(paramView, paramBoolean, false);
  }

  public static void showView(View paramView, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      showView(paramView);
      return;
    }
    hideView(paramView, paramBoolean2);
  }

  public static float sp2px(Context paramContext, float paramFloat)
  {
    if (paramContext == null)
      return paramFloat;
    return TypedValue.applyDimension(2, paramFloat, paramContext.getResources().getDisplayMetrics());
  }

  public static void updateViewVisibility(View paramView, int paramInt)
  {
    if (paramView == null)
      return;
    paramView.setVisibility(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.ViewUtils
 * JD-Core Version:    0.6.0
 */