package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.Decoding;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class SimpleMsg
  implements Parcelable, Decoding
{
  public static final Parcelable.Creator<SimpleMsg> CREATOR;
  public static final DecodingFactory<SimpleMsg> DECODER = new SimpleMsg.1();
  protected String content;
  protected String data;
  protected int flag;
  protected int icon;
  protected int statusCode;
  protected String title;

  static
  {
    CREATOR = new SimpleMsg.2();
  }

  protected SimpleMsg()
  {
  }

  public SimpleMsg(int paramInt1, String paramString1, String paramString2, int paramInt2, int paramInt3, String paramString3)
  {
    this.statusCode = paramInt1;
    this.title = paramString1;
    this.content = paramString2;
    this.icon = paramInt2;
    this.flag = paramInt3;
    this.data = paramString3;
  }

  protected SimpleMsg(Parcel paramParcel)
  {
    this.statusCode = paramParcel.readInt();
    this.title = paramParcel.readString();
    this.content = paramParcel.readString();
    this.icon = paramParcel.readInt();
    this.flag = paramParcel.readInt();
    this.data = paramParcel.readString();
  }

  public SimpleMsg(DPObject paramDPObject)
  {
    this.statusCode = paramDPObject.getInt(141);
    this.title = paramDPObject.getString(14057);
    this.content = paramDPObject.getString(22454);
    this.icon = paramDPObject.getInt(45243);
    this.flag = paramDPObject.getInt(29613);
    this.data = paramDPObject.getString(25578);
  }

  public SimpleMsg(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    this.title = paramString1;
    this.content = paramString2;
    this.icon = paramInt1;
    this.flag = paramInt2;
  }

  public String content()
  {
    return this.content;
  }

  public String data()
  {
    return this.data;
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
      case 141:
        this.statusCode = paramUnarchiver.readInt();
        break;
      case 14057:
        this.title = paramUnarchiver.readString();
        break;
      case 22454:
        this.content = paramUnarchiver.readString();
        break;
      case 45243:
        this.icon = paramUnarchiver.readInt();
        break;
      case 29613:
        this.flag = paramUnarchiver.readInt();
        break;
      case 25578:
      }
      this.data = paramUnarchiver.readString();
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public int flag()
  {
    return this.flag;
  }

  @Deprecated
  public int icon()
  {
    return this.icon;
  }

  public int statusCode()
  {
    return this.statusCode;
  }

  public String title()
  {
    return this.title;
  }

  public String toString()
  {
    return this.title + " : " + this.content;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.statusCode);
    paramParcel.writeString(this.title);
    paramParcel.writeString(this.content);
    paramParcel.writeInt(this.icon);
    paramParcel.writeInt(this.flag);
    paramParcel.writeString(this.data);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.SimpleMsg
 * JD-Core Version:    0.6.0
 */