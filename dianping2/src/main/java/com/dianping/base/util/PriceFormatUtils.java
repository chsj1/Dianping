package com.dianping.base.util;

import java.text.DecimalFormat;

public class PriceFormatUtils
{
  public static final DecimalFormat PRICE_DF = new DecimalFormat("#.##");

  public static String formatPrice(double paramDouble)
  {
    try
    {
      String str = PRICE_DF.format(paramDouble);
      return str;
    }
    catch (Exception localException)
    {
    }
    return "#.##";
  }

  public static String formatPrice(String paramString)
  {
    try
    {
      paramString = formatPrice(Double.valueOf(paramString).doubleValue());
      return paramString;
    }
    catch (Exception paramString)
    {
    }
    return "#.##";
  }

  public static int transformPriceToValue(int paramInt, boolean paramBoolean)
  {
    int i = 0;
    if (paramInt < 0)
      i = -1;
    int[] arrayOfInt;
    while (true)
    {
      return i;
      arrayOfInt = new int[8];
      int[] tmp16_15 = arrayOfInt;
      tmp16_15[0] = 0;
      int[] tmp20_16 = tmp16_15;
      tmp20_16[1] = 20;
      int[] tmp25_20 = tmp20_16;
      tmp25_20[2] = 50;
      int[] tmp30_25 = tmp25_20;
      tmp30_25[3] = 100;
      int[] tmp35_30 = tmp30_25;
      tmp35_30[4] = 'È';
      int[] tmp41_35 = tmp35_30;
      tmp41_35[5] = 400;
      int[] tmp47_41 = tmp41_35;
      tmp47_41[6] = 800;
      int[] tmp54_47 = tmp47_41;
      tmp54_47[7] = 1400;
      tmp54_47;
      if (paramInt > arrayOfInt[1])
        break;
      if (paramBoolean)
        return paramInt * 100 / (arrayOfInt[1] - arrayOfInt[0]);
    }
    if (paramInt >= arrayOfInt[(arrayOfInt.length - 2)])
    {
      if (paramBoolean)
        return 100;
      return (paramInt - arrayOfInt[(arrayOfInt.length - 2)]) * 100 / (arrayOfInt[(arrayOfInt.length - 1)] - arrayOfInt[(arrayOfInt.length - 2)]);
    }
    i = 1;
    while (true)
    {
      j = i;
      if (i >= arrayOfInt.length - 1)
        break;
      if (paramInt > arrayOfInt[i])
      {
        i += 1;
        continue;
      }
      if (paramInt != arrayOfInt[i])
        break label178;
    }
    for (int j = i; ; j = i - 1)
    {
      if (j != arrayOfInt.length - 2)
        break label193;
      return 100;
      label178: if (paramInt >= arrayOfInt[i])
        break;
    }
    label193: i = 100 / (arrayOfInt.length - 3);
    return (paramInt - arrayOfInt[j] + (j - 1) * (arrayOfInt[(j + 1)] - arrayOfInt[j])) * i / (arrayOfInt[(j + 1)] - arrayOfInt[j]);
  }

  public static String transformValueToPrice(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    if (paramInt1 < 0)
      i = 0;
    paramInt1 = i;
    if (i > 100)
      paramInt1 = 100;
    int[] arrayOfInt = new int[8];
    int[] tmp25_24 = arrayOfInt;
    tmp25_24[0] = 0;
    int[] tmp29_25 = tmp25_24;
    tmp29_25[1] = 20;
    int[] tmp34_29 = tmp29_25;
    tmp34_29[2] = 50;
    int[] tmp39_34 = tmp34_29;
    tmp39_34[3] = 100;
    int[] tmp44_39 = tmp39_34;
    tmp44_39[4] = 'È';
    int[] tmp50_44 = tmp44_39;
    tmp50_44[5] = 400;
    int[] tmp56_50 = tmp50_44;
    tmp56_50[6] = 800;
    int[] tmp63_56 = tmp56_50;
    tmp63_56[7] = 1400;
    tmp63_56;
    if (paramInt2 == 0)
      return String.valueOf((arrayOfInt[1] - arrayOfInt[0]) * paramInt1 / 100);
    if (paramInt2 == 2)
    {
      if (paramInt1 == 100)
        return "不限";
      return String.valueOf((arrayOfInt[(arrayOfInt.length - 1)] - arrayOfInt[(arrayOfInt.length - 2)]) * paramInt1 / 100 + arrayOfInt[(arrayOfInt.length - 2)]);
    }
    paramInt2 = 100 / (arrayOfInt.length - 3);
    i = paramInt1 / paramInt2;
    return String.valueOf((arrayOfInt[(i + 2)] - arrayOfInt[(i + 1)]) * (paramInt1 % paramInt2) / paramInt2 + arrayOfInt[(i + 1)]);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.PriceFormatUtils
 * JD-Core Version:    0.6.0
 */