package com.dianping.locationservice.realtime;

import com.dianping.locationservice.impl286.model.CellModel;
import com.dianping.locationservice.impl286.model.WifiModel;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationIdentifier
{
  private static final String SPLITTER = "|:|";
  private static final Pattern WIFI_PATTERN = Pattern.compile("(?i)(?:\\|?)(.*?),((?:[0-9a-f]{2}:){5}[0-9a-f]{2}(?:,-?\\d+)?|null(?:,-200)?)");
  public static final Comparator<? super LocationIdentifier> createDateComparator = new Comparator()
  {
    public int compare(LocationIdentifier paramLocationIdentifier1, LocationIdentifier paramLocationIdentifier2)
    {
      return paramLocationIdentifier2.createDate - paramLocationIdentifier1.createDate;
    }
  };
  private Set<String> cellSet = new HashSet();
  private String cellType = "";
  private int createDate;
  private Set<String> wifiSet = new HashSet();

  public LocationIdentifier()
  {
    this.createDate = unixTimestamp();
  }

  public LocationIdentifier(String paramString)
  {
    int i = paramString.indexOf("|:|");
    if (i != -1);
    try
    {
      this.createDate = Integer.parseInt(paramString.substring(0, i));
      label57: int j = paramString.indexOf("|:|", "|:|".length() + i);
      init(paramString.substring("|:|".length() + i, j), paramString.substring("|:|".length() + j));
      return;
    }
    catch (Throwable localThrowable)
    {
      break label57;
    }
  }

  public LocationIdentifier(List<CellModel> paramList, List<WifiModel> paramList1)
  {
    this();
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      this.cellType = (((CellModel)paramList.get(0)).getMcc() + "," + ((CellModel)paramList.get(0)).getMncSid());
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        CellModel localCellModel = (CellModel)paramList.next();
        this.cellSet.add(localCellModel.getCidBid() + "," + localCellModel.getLacNid());
      }
    }
    if ((paramList1 != null) && (!paramList1.isEmpty()))
    {
      paramList = paramList1.iterator();
      while (paramList.hasNext())
      {
        paramList1 = (WifiModel)paramList.next();
        this.wifiSet.add(filterSpecialChar(paramList1.getSsid()) + "," + paramList1.getBssid());
      }
    }
  }

  public LocationIdentifier(Map<String, String> paramMap)
  {
    this();
    String str2 = (String)paramMap.get("gsmInfo");
    String str1;
    if (str2 != null)
    {
      str1 = str2;
      if (str2.length() != 0);
    }
    else
    {
      str2 = (String)paramMap.get("cdmaInfo");
      if (str2 != null)
      {
        str1 = str2;
        if (str2.length() != 0);
      }
      else
      {
        str2 = (String)paramMap.get("wcdmaInfo");
        if (str2 != null)
        {
          str1 = str2;
          if (str2.length() != 0);
        }
        else
        {
          str1 = (String)paramMap.get("lteInfo");
        }
      }
    }
    init(str1, (String)paramMap.get("wifiInfo"));
  }

  private String filterSpecialChar(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    paramString = paramString.toCharArray();
    int j = paramString.length;
    int i = 0;
    while (i < j)
    {
      char c = paramString[i];
      if (c > '\037')
        localStringBuilder.append(c);
      i += 1;
    }
    return new String(localStringBuilder);
  }

  private void init(String paramString1, String paramString2)
  {
    label61: Object localObject;
    if ((paramString1 != null) && (paramString1.length() > 0))
    {
      int i = paramString1.indexOf(":");
      if (i != -1)
      {
        this.cellType = paramString1.substring(0, i);
        i += 1;
        paramString1 = paramString1.substring(i).split("\\|");
        int m = paramString1.length;
        i = 0;
        if (i >= m)
          break label155;
        localObject = paramString1[i];
        int k = ((String)localObject).indexOf(",");
        int j = k;
        if (k != -1)
          j = ((String)localObject).indexOf(",", k + 1);
        if (j == -1)
          break label141;
        this.cellSet.add(((String)localObject).substring(0, j));
      }
      while (true)
      {
        i += 1;
        break label61;
        i = 0;
        break;
        label141: this.cellSet.add(localObject);
      }
    }
    label155: if ((paramString2 != null) && (paramString2.length() > 0))
    {
      paramString1 = WIFI_PATTERN.matcher(paramString2);
      while (paramString1.find())
      {
        paramString2 = paramString1.group(1);
        localObject = paramString1.group(2).split(",")[0];
        this.wifiSet.add(filterSpecialChar(paramString2) + "," + (String)localObject);
      }
    }
  }

  private int unixTimestamp()
  {
    return (int)(System.currentTimeMillis() / 1000L);
  }

  public int cellDifferenceFrom(LocationIdentifier paramLocationIdentifier, RealTimeLocateSettings paramRealTimeLocateSettings)
  {
    int k = 2147483647;
    int i = k;
    if (this.cellType.length() > 0)
    {
      i = k;
      if (!this.cellSet.isEmpty())
      {
        i = k;
        if (this.cellType.equals(paramLocationIdentifier.cellType))
        {
          i = 0;
          int j = paramRealTimeLocateSettings.cellLackDifference / this.cellSet.size();
          Object localObject = this.cellSet.iterator();
          while (((Iterator)localObject).hasNext())
          {
            String str = (String)((Iterator)localObject).next();
            if (paramLocationIdentifier.cellSet.contains(str))
              continue;
            i += j;
          }
          j = i;
          if (!paramLocationIdentifier.cellSet.isEmpty())
          {
            int m = paramRealTimeLocateSettings.cellOverDifference / paramLocationIdentifier.cellSet.size();
            paramLocationIdentifier = paramLocationIdentifier.cellSet.iterator();
            while (true)
            {
              j = i;
              if (!paramLocationIdentifier.hasNext())
                break;
              localObject = (String)paramLocationIdentifier.next();
              if (this.cellSet.contains(localObject))
                continue;
              i += m;
            }
          }
          i = k;
          if (j < 2147483647)
          {
            i = k;
            if (j < paramRealTimeLocateSettings.cellInvalidDifferenceLimit)
              i = j;
          }
        }
      }
    }
    return i;
  }

  public boolean cellEquals(LocationIdentifier paramLocationIdentifier)
  {
    return (this.cellType.equals(paramLocationIdentifier.cellType)) && (this.cellSet.size() == paramLocationIdentifier.cellSet.size()) && (this.cellSet.containsAll(paramLocationIdentifier.cellSet));
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof LocationIdentifier))
    {
      paramObject = (LocationIdentifier)paramObject;
      if (!cellEquals(paramObject));
      do
        return false;
      while (!wifiEquals(paramObject));
    }
    return true;
  }

  public boolean expired(RealTimeLocateSettings paramRealTimeLocateSettings)
  {
    return this.createDate + paramRealTimeLocateSettings.expireSeconds <= unixTimestamp();
  }

  public boolean hasCell()
  {
    return !this.cellSet.isEmpty();
  }

  public boolean hasWifi()
  {
    return !this.wifiSet.isEmpty();
  }

  public int hashCode()
  {
    return this.cellType.hashCode() ^ this.cellSet.hashCode() ^ this.wifiSet.hashCode();
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.createDate).append("|:|");
    Iterator localIterator;
    if ((this.cellType.length() > 0) && (this.cellSet.size() > 0))
    {
      localStringBuilder.append(this.cellType).append(":");
      localIterator = this.cellSet.iterator();
      while (localIterator.hasNext())
        localStringBuilder.append((String)localIterator.next()).append("|");
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    }
    localStringBuilder.append("|:|");
    if (this.wifiSet.size() > 0)
    {
      localIterator = this.wifiSet.iterator();
      while (localIterator.hasNext())
        localStringBuilder.append((String)localIterator.next()).append("|");
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    }
    return localStringBuilder.toString();
  }

  public int wifiDifferenceFrom(LocationIdentifier paramLocationIdentifier, RealTimeLocateSettings paramRealTimeLocateSettings)
  {
    int k = 2147483647;
    int i = k;
    if (paramRealTimeLocateSettings.hasAnyFlag(1))
    {
      i = k;
      if (!this.wifiSet.isEmpty())
      {
        i = 0;
        int j = paramRealTimeLocateSettings.wifiLackDifference / this.wifiSet.size();
        Object localObject = this.wifiSet.iterator();
        while (((Iterator)localObject).hasNext())
        {
          String str = (String)((Iterator)localObject).next();
          if (paramLocationIdentifier.wifiSet.contains(str))
            continue;
          i += j;
        }
        j = i;
        if (!paramLocationIdentifier.wifiSet.isEmpty())
        {
          int m = paramRealTimeLocateSettings.wifiOverDifference / paramLocationIdentifier.wifiSet.size();
          paramLocationIdentifier = paramLocationIdentifier.wifiSet.iterator();
          while (true)
          {
            j = i;
            if (!paramLocationIdentifier.hasNext())
              break;
            localObject = (String)paramLocationIdentifier.next();
            if (this.wifiSet.contains(localObject))
              continue;
            i += m;
          }
        }
        i = k;
        if (j < 2147483647)
        {
          i = k;
          if (j < paramRealTimeLocateSettings.wifiInvalidDifferenceLimit)
            i = j;
        }
      }
    }
    return i;
  }

  public boolean wifiEquals(LocationIdentifier paramLocationIdentifier)
  {
    return (this.wifiSet.size() == paramLocationIdentifier.wifiSet.size()) && (this.wifiSet.containsAll(paramLocationIdentifier.wifiSet));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.realtime.LocationIdentifier
 * JD-Core Version:    0.6.0
 */