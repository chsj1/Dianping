package com.dianping.model;

import com.dianping.archive.ArchiveException;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class GuessLikeItem extends BasicModel
{
  public static final DecodingFactory<GuessLikeItem> DECODER = new DecodingFactory()
  {
    public GuessLikeItem[] createArray(int paramInt)
    {
      return new GuessLikeItem[paramInt];
    }

    public GuessLikeItem createInstance(int paramInt)
    {
      if (paramInt == 61484)
        return new GuessLikeItem();
      return null;
    }
  };
  public String detailInfoSchema;
  public String extContent;
  public boolean hasExtension;
  public int id;
  public double lat;
  public double lng;
  public String mainTitle;
  public boolean needOrder;
  public String oriPrice;
  public String picUrl;
  public String price;
  public int shopPower;
  public boolean showCu;
  public boolean showDing;
  public boolean showPiao;
  public boolean showTuan;
  public String subTitle;
  public int tagType;
  public String[] tags;

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
      case 43649:
        this.needOrder = paramUnarchiver.readBoolean();
        break;
      case 41711:
        this.extContent = paramUnarchiver.readString();
        break;
      case 61038:
        this.hasExtension = paramUnarchiver.readBoolean();
        break;
      case 33538:
        this.showPiao = paramUnarchiver.readBoolean();
        break;
      case 38911:
        this.showCu = paramUnarchiver.readBoolean();
        break;
      case 33923:
        this.showTuan = paramUnarchiver.readBoolean();
        break;
      case 49335:
        this.detailInfoSchema = paramUnarchiver.readString();
        break;
      case 5659:
        this.showDing = paramUnarchiver.readBoolean();
        break;
      case 48823:
        this.shopPower = paramUnarchiver.readInt();
        break;
      case 10622:
        this.lat = paramUnarchiver.readDouble();
        break;
      case 11012:
        this.lng = paramUnarchiver.readDouble();
        break;
      case 33519:
        this.oriPrice = paramUnarchiver.readString();
        break;
      case 2363:
        this.id = paramUnarchiver.readInt();
        break;
      case 50613:
        this.price = paramUnarchiver.readString();
        break;
      case 43038:
        this.tags = paramUnarchiver.readStringArray();
        break;
      case 11740:
        this.picUrl = paramUnarchiver.readString();
        break;
      case 18270:
        this.subTitle = paramUnarchiver.readString();
        break;
      case 36900:
        this.mainTitle = paramUnarchiver.readString();
        break;
      case 64304:
      }
      this.tagType = paramUnarchiver.readInt();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.GuessLikeItem
 * JD-Core Version:    0.6.0
 */