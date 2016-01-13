package com.dianping.main.home;

import com.dianping.archive.ArchiveException;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;
import com.dianping.model.GuessLikeList;

public class HomeGuessLikeList extends GuessLikeList
{
  public static final DecodingFactory<HomeGuessLikeList> DECODER = new DecodingFactory()
  {
    public HomeGuessLikeList[] createArray(int paramInt)
    {
      return new HomeGuessLikeList[paramInt];
    }

    public HomeGuessLikeList createInstance(int paramInt)
    {
      if (paramInt == 11683)
        return new HomeGuessLikeList();
      return null;
    }
  };
  public HomeGuessLikeItem[] homeItemList;

  public void decode(Unarchiver paramUnarchiver)
    throws ArchiveException
  {
    while (true)
    {
      int i = paramUnarchiver.readMemberHash16();
      if (i <= 0)
        break;
      switch (i)
      {
      default:
        paramUnarchiver.skipAnyObject();
        break;
      case 3851:
        this.isEnd = paramUnarchiver.readBoolean();
        break;
      case 22275:
        this.nextStartIndex = paramUnarchiver.readInt();
        break;
      case 50589:
        this.sessionId = paramUnarchiver.readString();
        break;
      case 11655:
        this.queryID = paramUnarchiver.readString();
        break;
      case 2167:
        this.homeItemList = ((HomeGuessLikeItem[])paramUnarchiver.readArray(HomeGuessLikeItem.DECODER));
        break;
      case 63581:
      }
      this.moreUrlSchema = paramUnarchiver.readString();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeGuessLikeList
 * JD-Core Version:    0.6.0
 */