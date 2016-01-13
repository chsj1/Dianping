package com.dianping.model;

import android.os.Parcel;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class GuessLikeList extends BasicModel
{
  public static final DecodingFactory<GuessLikeList> DECODER = new DecodingFactory()
  {
    public GuessLikeList[] createArray(int paramInt)
    {
      return new GuessLikeList[paramInt];
    }

    public GuessLikeList createInstance(int paramInt)
    {
      if (paramInt == 11683)
        return new GuessLikeList();
      return null;
    }
  };
  public boolean isEnd;
  public GuessLikeItem[] itemList;
  public String moreUrlSchema;
  public int nextStartIndex;
  public String queryID;
  public String sessionId;

  public GuessLikeList()
  {
  }

  protected GuessLikeList(Parcel paramParcel)
  {
    super(paramParcel);
  }

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
        this.itemList = ((GuessLikeItem[])paramUnarchiver.readArray(GuessLikeItem.DECODER));
        break;
      case 63581:
      }
      this.moreUrlSchema = paramUnarchiver.readString();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.GuessLikeList
 * JD-Core Version:    0.6.0
 */