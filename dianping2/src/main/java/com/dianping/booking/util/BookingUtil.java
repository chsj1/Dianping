package com.dianping.booking.util;

import com.dianping.archive.DPObject;
import com.dianping.util.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class BookingUtil
{
  public static final int BOOKING_TYPE_HALL = 10;
  public static final int BOOKING_TYPE_ROOM = 20;
  public static final int BOOKING_TYPE_ROOM_ONLY = 30;
  public static final int MASK_ALL_HALL_AND_ROOM_BUSY = 1048575;
  public static final int MASK_ALL_HALL_BUSY = 1023;
  public static final int MASK_ALL_ROOM_BUSY = 1047552;
  public static final int MASK_HALL_AND_ROOM_BOOKABLE = 3145728;
  public static final int MASK_HALL_BOOKABLE = 1048576;
  public static final int MASK_HAS_REBATE = 4194304;
  public static final int MASK_IS_HOT = 16777216;
  public static final int MASK_ROOM_BOOKABLE = 2097152;

  private static String formatInteger(int paramInt)
  {
    if (paramInt < 10)
      return "0" + paramInt;
    return "" + paramInt;
  }

  public static int getBitmap(BookingConfig paramBookingConfig, Calendar paramCalendar)
  {
    BookingConfig.BookingBitMap localBookingBitMap = getBitmapObj(paramBookingConfig, paramCalendar);
    if (localBookingBitMap != null)
      return localBookingBitMap.bitmap[getBitmapIndex(paramBookingConfig, paramCalendar)];
    return 0;
  }

  public static int getBitmapIndex(BookingConfig paramBookingConfig, Calendar paramCalendar)
  {
    return (paramCalendar.get(11) * 60 + paramCalendar.get(12)) / paramBookingConfig.timeInterval;
  }

  private static BookingConfig.BookingBitMap getBitmapObj(BookingConfig paramBookingConfig, Calendar paramCalendar)
  {
    paramBookingConfig = paramBookingConfig.getBitmap().entrySet().iterator();
    while (paramBookingConfig.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramBookingConfig.next();
      if (DateUtils.isSameDay((Calendar)localEntry.getKey(), paramCalendar))
        return (BookingConfig.BookingBitMap)localEntry.getValue();
    }
    return null;
  }

  public static List<Calendar> getBookableDate(BookingConfig paramBookingConfig)
  {
    int i = paramBookingConfig.dayAfter;
    int j = paramBookingConfig.dayIn;
    ArrayList localArrayList = new ArrayList();
    while (i < j)
    {
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.add(6, i);
      Iterator localIterator = paramBookingConfig.getBitmap().keySet().iterator();
      while (localIterator.hasNext())
      {
        if (!DateUtils.isSameDay(localCalendar, (Calendar)localIterator.next()))
          continue;
        localArrayList.add(localCalendar);
      }
      i += 1;
    }
    return localArrayList;
  }

  private static BookableTime getBookableTime(BookingConfig.BookingBitMap paramBookingBitMap, int paramInt1, int paramInt2, int paramInt3)
  {
    int k = 0;
    int i = 0;
    BookableTime localBookableTime = new BookableTime();
    paramBookingBitMap = paramBookingBitMap.bitmap;
    int i1 = paramBookingBitMap.length;
    int j = 0;
    while (j < i1)
    {
      int m = paramBookingBitMap[j];
      if (((m & paramInt1) != 0) && ((m & paramInt2) != 0))
        localBookableTime.add(k, i);
      int n = i + paramInt3;
      m = k;
      i = n;
      if (n >= 60)
      {
        i = 0;
        m = k + 1;
      }
      j += 1;
      k = m;
    }
    return localBookableTime;
  }

  public static BookableTime getBookableTime(BookingConfig paramBookingConfig, int paramInt1, int paramInt2, Calendar paramCalendar)
  {
    paramCalendar = getBitmapObj(paramBookingConfig, paramCalendar);
    if (paramCalendar == null)
      return null;
    int i = paramBookingConfig.timeInterval;
    int j = paramBookingConfig.minPeopleCountForRoom;
    switch (paramInt2)
    {
    default:
      throw new RuntimeException("invalid param: type can only be 10(hall), 20(room only) or 30(room and hall)");
    case 10:
      return getHallBookableTime(paramCalendar, paramInt1, i);
    case 30:
      return getRoomOnlyBookableTime(paramCalendar, j, paramInt1, i);
    case 20:
    }
    return getRoomAndHallBookableTime(paramCalendar, j, paramInt1, i);
  }

  private static BookableTime getHallBookableTime(BookingConfig.BookingBitMap paramBookingBitMap, int paramInt1, int paramInt2)
  {
    return getBookableTime(paramBookingBitMap, 1048576, getHallMask(paramInt1), paramInt2);
  }

  private static int getHallMask(int paramInt)
  {
    int i = paramInt;
    paramInt = i;
    if (i <= 2)
      paramInt = 2;
    i = paramInt;
    if (paramInt > 10)
      i = 11;
    return 1 << i - 2;
  }

  public static Set<String> getOneDayHot(BookingConfig paramBookingConfig, Calendar paramCalendar)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramBookingConfig.getBitmap().entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (!DateUtils.isSameDay((Calendar)localEntry.getKey(), paramCalendar))
        continue;
      int k = 0;
      int i = 0;
      int i1 = paramBookingConfig.timeInterval;
      paramCalendar = ((BookingConfig.BookingBitMap)localEntry.getValue()).bitmap;
      int i2 = paramCalendar.length;
      int j = 0;
      while (true)
      {
        paramBookingConfig = localHashSet;
        if (j >= i2)
          break;
        if ((0x1000000 & paramCalendar[j]) != 0)
          localHashSet.add(formatInteger(k) + ":" + formatInteger(i));
        int n = i + i1;
        int m = k;
        i = n;
        if (n >= 60)
        {
          i = 0;
          m = k + 1;
        }
        j += 1;
        k = m;
      }
    }
    paramBookingConfig = Collections.emptySet();
    return paramBookingConfig;
  }

  public static Set<String> getOneDayRebate(BookingConfig paramBookingConfig, Calendar paramCalendar)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramBookingConfig.getBitmap().entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (!DateUtils.isSameDay((Calendar)localEntry.getKey(), paramCalendar))
        continue;
      int k = 0;
      int i = 0;
      int i1 = paramBookingConfig.timeInterval;
      paramCalendar = ((BookingConfig.BookingBitMap)localEntry.getValue()).bitmap;
      int i2 = paramCalendar.length;
      int j = 0;
      while (true)
      {
        paramBookingConfig = localHashSet;
        if (j >= i2)
          break;
        if ((0x400000 & paramCalendar[j]) != 0)
          localHashSet.add(formatInteger(k) + ":" + formatInteger(i));
        int n = i + i1;
        int m = k;
        i = n;
        if (n >= 60)
        {
          i = 0;
          m = k + 1;
        }
        j += 1;
        k = m;
      }
    }
    paramBookingConfig = Collections.emptySet();
    return paramBookingConfig;
  }

  private static BookableTime getRoomAndHallBookableTime(BookingConfig.BookingBitMap paramBookingBitMap, int paramInt1, int paramInt2, int paramInt3)
  {
    return getRoomOnlyBookableTime(paramBookingBitMap, paramInt1, paramInt2, paramInt3).union(getHallBookableTime(paramBookingBitMap, paramInt2, paramInt3));
  }

  public static String getRoomMinChargeInfo(BookingConfig paramBookingConfig, int paramInt)
  {
    String str2 = "";
    String str1 = str2;
    int j;
    int i;
    if (paramBookingConfig.roomMinChargeInfo != null)
    {
      paramBookingConfig = paramBookingConfig.roomMinChargeInfo;
      j = paramBookingConfig.length;
      i = 0;
    }
    while (true)
    {
      str1 = str2;
      if (i < j)
      {
        str1 = paramBookingConfig[i];
        if ((paramInt >= str1.getInt("MinPeople")) && (paramInt <= str1.getInt("MaxPeople")))
          str1 = str1.getString("TextDesc");
      }
      else
      {
        return str1;
      }
      i += 1;
    }
  }

  private static BookableTime getRoomOnlyBookableTime(BookingConfig.BookingBitMap paramBookingBitMap, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 < paramInt1)
      return new BookableTime();
    return getBookableTime(paramBookingBitMap, 2097152, 2097152, paramInt3);
  }

  public static String getStr(String paramString, boolean paramBoolean)
  {
    paramString = paramString.split("_");
    if (paramBoolean)
      return paramString[(paramString.length - 1)];
    return paramString[0];
  }

  public static boolean isEnglish(String paramString)
  {
    int n = 0;
    int j = paramString.length();
    int i = 0;
    while (true)
    {
      int m = n;
      if (i < j)
      {
        int k = paramString.charAt(i);
        if (((k >= 97) && (k <= 122)) || ((k >= 65) && (k <= 90)))
          m = 1;
      }
      else
      {
        return m;
      }
      i += 1;
    }
  }

  public static ValidateResult validate(BookingConfig paramBookingConfig, int paramInt1, int paramInt2, Calendar paramCalendar)
  {
    if ((paramInt1 > paramBookingConfig.peopleMaxCount) || (paramInt1 < paramBookingConfig.peopleMinCount))
      return ValidateResult.PeopleNumberNotSatisfied;
    int i = getBitmap(paramBookingConfig, paramCalendar);
    if (((i & 0x100000) == 0) && ((i & 0x200000) == 0))
      return ValidateResult.UnBookable;
    if (paramBookingConfig.isRoomOnly())
    {
      if (paramBookingConfig.roomInfo == null)
        return ValidateResult.UnBookable;
      if (paramBookingConfig.minPeopleCountForRoom <= paramInt1)
        return ValidateResult.Success;
      return ValidateResult.RoomConditionNotSatisfied;
    }
    int j = getHallMask(paramInt1);
    if (paramInt2 == 10)
    {
      if (((i & 0x100000) != 0) && ((i & j) != 0))
        return ValidateResult.Success;
      if ((i & 0x200000) != 0)
        return ValidateResult.HallFull;
      return ValidateResult.AllFull;
    }
    if ((i & 0x200000) != 0)
    {
      if (paramBookingConfig.minPeopleCountForRoom > paramInt1)
        return ValidateResult.RoomConditionNotSatisfied;
      return ValidateResult.Success;
    }
    if (((i & 0x100000) != 0) && ((i & j) != 0))
      return ValidateResult.RoomFull;
    return ValidateResult.AllFull;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.util.BookingUtil
 * JD-Core Version:    0.6.0
 */