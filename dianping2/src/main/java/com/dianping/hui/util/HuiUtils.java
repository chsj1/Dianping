package com.dianping.hui.util;

import android.text.TextUtils;
import android.view.View;
import java.math.BigDecimal;

public class HuiUtils
{
  public static String addSpaceToCnPunc(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramString;
    paramString = paramString.toCharArray();
    StringBuilder localStringBuilder = new StringBuilder();
    int j = paramString.length;
    int i = 0;
    while (i < j)
    {
      char c = paramString[i];
      localStringBuilder.append(c);
      if (isCnPunc(c))
        localStringBuilder.append(' ');
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public static String bigDecimalToString(BigDecimal paramBigDecimal)
  {
    return bigDecimalToString(paramBigDecimal, 2);
  }

  public static String bigDecimalToString(BigDecimal paramBigDecimal, int paramInt)
  {
    return new BigDecimal(paramBigDecimal.toPlainString()).setScale(paramInt, 4).toPlainString();
  }

  public static String bigDecimalTrailingZerosToString(BigDecimal paramBigDecimal, int paramInt)
  {
    return new BigDecimal(paramBigDecimal.toPlainString()).setScale(paramInt, 4).stripTrailingZeros().toPlainString();
  }

  private static boolean isCnPunc(char paramChar)
  {
    char[] arrayOfChar = new char[8];
    char[] tmp6_5 = arrayOfChar;
    tmp6_5[0] = -244;
    char[] tmp11_6 = tmp6_5;
    tmp11_6[1] = 12290;
    char[] tmp16_11 = tmp11_6;
    tmp16_11[2] = -230;
    char[] tmp21_16 = tmp16_11;
    tmp21_16[3] = -229;
    char[] tmp26_21 = tmp21_16;
    tmp26_21[4] = -230;
    char[] tmp31_26 = tmp26_21;
    tmp31_26[5] = 12289;
    char[] tmp36_31 = tmp31_26;
    tmp36_31[6] = -248;
    char[] tmp42_36 = tmp36_31;
    tmp42_36[7] = -247;
    tmp42_36;
    int j = arrayOfChar.length;
    int i = 0;
    while (i < j)
    {
      if (arrayOfChar[i] == paramChar)
        return true;
      i += 1;
    }
    return false;
  }

  public static String separateStringBySpace(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString))
      return "生成支付号码出错";
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramString.length())
    {
      localStringBuilder.append(paramString.charAt(i));
      if ((i + 1) % paramInt == 0)
        localStringBuilder.append(" ");
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public static String splitPhoneNumber(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramString.length())
    {
      if ((i > 0) && (i < paramString.length() - 1) && ((i == 3) || (i == 7)))
        localStringBuilder.append(" ");
      localStringBuilder.append(paramString.charAt(i));
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public static String splitSerialNumber(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramString.length())
    {
      if ((i > 0) && (i < paramString.length() - 1) && (i % 4 == 0))
        localStringBuilder.append(" ");
      localStringBuilder.append(paramString.charAt(i));
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public static void updateViewVisibility(View paramView, int paramInt)
  {
    if (paramView == null)
      return;
    paramView.setVisibility(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.util.HuiUtils
 * JD-Core Version:    0.6.0
 */