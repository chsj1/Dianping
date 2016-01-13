package com.dianping.takeaway.util;

import com.dianping.takeaway.entity.TakeawayAddress;
import java.util.List;

public class TakeawayHistoryAddressManager
{
  private static final String ADDRESS_DIVIDER = "#-#";
  private String fileName;

  public TakeawayHistoryAddressManager(String paramString)
  {
    this.fileName = paramString;
  }

  public List<TakeawayAddress> parseAddressFile()
  {
    return new TakeawayFileRWer(this.fileName).readFileToList(new TakeawayFileRWer.FileLineParser()
    {
      public TakeawayAddress parse(String paramString)
      {
        paramString = paramString.split("#-#");
        return new TakeawayAddress(paramString[0], Double.parseDouble(paramString[1]), Double.parseDouble(paramString[2]));
      }
    });
  }

  public void writeAddressFile(List<TakeawayAddress> paramList)
  {
    new TakeawayFileRWer(this.fileName).writeListToFile(paramList);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.util.TakeawayHistoryAddressManager
 * JD-Core Version:    0.6.0
 */