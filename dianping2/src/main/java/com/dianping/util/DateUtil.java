package com.dianping.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{
  private static final DateFormat FMT_DATE = new SimpleDateFormat("MM-dd");
  private static final DateFormat FMT_DATE2 = new SimpleDateFormat("yy-MM-dd");
  private static final DateFormat FMT_DATE_TIME;
  private static final DateFormat FMT_DATE_TIME2;
  private static final DateFormat FMT_TIME = new SimpleDateFormat("HH:mm");
  public static final long RELEASE_ZERO_TIMESTAMP = 1298908800000L;
  private static long TIME_CALIBRATOR = 0L;
  public static final long ZERO_TIMESTAMP = 1044028800000L;

  static
  {
    FMT_DATE_TIME = new SimpleDateFormat("MM-dd HH:mm");
    FMT_DATE_TIME2 = new SimpleDateFormat("yy-MM-dd HH:mm");
    TIME_CALIBRATOR = 0L;
  }

  public static long currentTimeMillis()
  {
    return System.currentTimeMillis() + TIME_CALIBRATOR;
  }

  public static String format(Date paramDate)
  {
    if ((paramDate == null) || (paramDate.getTime() < 1044028800000L))
      return "";
    Date localDate = new Date();
    if ((localDate.getYear() == paramDate.getYear()) && (localDate.getMonth() == paramDate.getMonth()) && (localDate.getDate() == paramDate.getDate()))
      return FMT_TIME.format(paramDate);
    if (localDate.getYear() == paramDate.getYear())
      return FMT_DATE.format(paramDate);
    return FMT_DATE2.format(paramDate);
  }

  public static String format2(Date paramDate)
  {
    if ((paramDate == null) || (paramDate.getTime() < 1044028800000L))
      return "";
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(11, 0);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    long l1 = localCalendar.getTimeInMillis();
    long l2 = paramDate.getTime();
    if (l2 > 86400000L + l1)
      return FMT_DATE2.format(paramDate);
    if (l2 > l1)
      return FMT_TIME.format(paramDate);
    if (l2 > l1 - 86400000L)
      return "昨天 " + FMT_TIME.format(paramDate);
    if (l2 > l1 - 172800000L)
      return "前天 " + FMT_TIME.format(paramDate);
    int i = localCalendar.get(1);
    localCalendar.setTimeInMillis(l2);
    if (i == localCalendar.get(1))
      return FMT_DATE.format(paramDate);
    return FMT_DATE2.format(paramDate);
  }

  public static String format2t(Date paramDate)
  {
    if ((paramDate == null) || (paramDate.getTime() < 1044028800000L))
      return "";
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(11, 0);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    long l1 = localCalendar.getTimeInMillis();
    long l2 = paramDate.getTime();
    if (l2 > 86400000L + l1)
      return FMT_DATE_TIME2.format(paramDate);
    if (l2 > l1)
      return FMT_TIME.format(paramDate);
    if (l2 > l1 - 86400000L)
      return "昨天 " + FMT_TIME.format(paramDate);
    if (l2 > l1 - 172800000L)
      return "前天 " + FMT_TIME.format(paramDate);
    int i = localCalendar.get(1);
    localCalendar.setTimeInMillis(l2);
    if (i == localCalendar.get(1))
      return FMT_DATE_TIME.format(paramDate);
    return FMT_DATE_TIME2.format(paramDate);
  }

  public static long getNextDayTimeMillis()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(11, 24);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    return localCalendar.getTimeInMillis();
  }

  public static void setHttpResponseDate(String paramString)
  {
    try
    {
      paramString = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).parse(paramString.trim());
      if (paramString.getTime() < 1298908800000L)
        return;
      TIME_CALIBRATOR = paramString.getTime() - System.currentTimeMillis();
      return;
    }
    catch (java.lang.Exception paramString)
    {
      return;
    }
    catch (java.lang.Error paramString)
    {
    }
  }

  public static long timeCalibrator()
  {
    return TIME_CALIBRATOR;
  }

  public static long today(long paramLong)
  {
    paramLong += 28800000L;
    return paramLong - paramLong % 86400000L - 28800000L;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.DateUtil
 * JD-Core Version:    0.6.0
 */