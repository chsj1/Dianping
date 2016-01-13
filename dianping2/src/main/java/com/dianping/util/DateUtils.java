package com.dianping.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils
{
  public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static String aboutHour(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 0)
      return "";
    if (paramInt1 / 60 / 60 >= paramInt2);
    for (String str = "大于" + paramInt2 + "小时"; ; str = secToMinutes(paramInt1))
      return str;
  }

  public static String formatDate(int paramInt1, int paramInt2)
  {
    return String.format("%02d", new Object[] { Integer.valueOf(paramInt1 + 1) }) + "-" + String.format("%02d", new Object[] { Integer.valueOf(paramInt2) });
  }

  public static String formatDate(Calendar paramCalendar)
  {
    return String.format("%02d", new Object[] { Integer.valueOf(paramCalendar.get(2) + 1) }) + "月" + String.format("%02d", new Object[] { Integer.valueOf(paramCalendar.get(5)) }) + "日";
  }

  public static String formatDate2TimeZone(long paramLong, String paramString1, String paramString2)
  {
    paramString1 = new SimpleDateFormat(paramString1, Locale.getDefault());
    paramString1.setTimeZone(TimeZone.getTimeZone(paramString2));
    return paramString1.format(Long.valueOf(paramLong));
  }

  public static String formatDate2TimeZone(Date paramDate, String paramString1, String paramString2)
  {
    paramString1 = new SimpleDateFormat(paramString1, Locale.getDefault());
    paramString1.setTimeZone(TimeZone.getTimeZone(paramString2));
    return paramString1.format(paramDate);
  }

  public static String formatTime(int paramInt1, int paramInt2)
  {
    return String.format("%02d", new Object[] { Integer.valueOf(paramInt1) }) + ":" + String.format("%02d", new Object[] { Integer.valueOf(paramInt2) });
  }

  public static String formatTime(Calendar paramCalendar)
  {
    return formatTime(paramCalendar.get(11), paramCalendar.get(12));
  }

  public static boolean isDuringDate(String paramString1, String paramString2)
  {
    int j = 0;
    try
    {
      paramString1 = formatter.parse(paramString1);
      paramString2 = formatter.parse(paramString2);
      Date localDate = new Date();
      int i = j;
      if (localDate.after(paramString1))
      {
        boolean bool = localDate.before(paramString2);
        i = j;
        if (bool)
          i = 1;
      }
      return i;
    }
    catch (java.text.ParseException paramString1)
    {
    }
    return false;
  }

  public static boolean isSameDay(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    if ((paramCalendar1 == null) || (paramCalendar2 == null))
      throw new IllegalArgumentException("The date must not be null");
    return (paramCalendar1.get(0) == paramCalendar2.get(0)) && (paramCalendar1.get(1) == paramCalendar2.get(1)) && (paramCalendar1.get(6) == paramCalendar2.get(6));
  }

  public static boolean isSameDay(Date paramDate1, Date paramDate2)
  {
    if ((paramDate1 == null) || (paramDate2 == null))
      throw new IllegalArgumentException("The date must not be null");
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate1);
    paramDate1 = Calendar.getInstance();
    paramDate1.setTime(paramDate2);
    return isSameDay(localCalendar, paramDate1);
  }

  public static String secToMinutes(int paramInt)
  {
    String str2 = "";
    String str1 = str2;
    if (paramInt > 0)
    {
      paramInt /= 60;
      str1 = str2;
      if (paramInt > 0)
        str1 = paramInt + "分钟";
    }
    return str1;
  }

  public static String secToTime(int paramInt)
  {
    String str2 = "";
    String str1 = str2;
    if (paramInt > 0)
    {
      int i = paramInt / 60 / 60;
      paramInt = paramInt / 60 % 60;
      if (i > 0)
        str2 = i + "小时";
      str1 = str2;
      if (paramInt > 0)
        str1 = str2 + paramInt + "分钟";
    }
    return str1;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.DateUtils
 * JD-Core Version:    0.6.0
 */