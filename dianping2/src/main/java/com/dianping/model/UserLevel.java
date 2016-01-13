package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.Decoding;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class UserLevel extends DPObject
  implements Parcelable, Decoding
{
  public static final Parcelable.Creator<UserLevel> CREATOR;
  public static final DecodingFactory<UserLevel> DECODER = new DecodingFactory()
  {
    public UserLevel[] createArray(int paramInt)
    {
      return new UserLevel[paramInt];
    }

    public UserLevel createInstance(int paramInt)
    {
      if (paramInt == 3269)
        return new UserLevel(null);
      return null;
    }
  };
  private String pic;
  private int pow;
  private String title;
  private String url;

  static
  {
    CREATOR = new Parcelable.Creator()
    {
      public UserLevel createFromParcel(Parcel paramParcel)
      {
        return new UserLevel(paramParcel);
      }

      public UserLevel[] newArray(int paramInt)
      {
        return new UserLevel[paramInt];
      }
    };
  }

  private UserLevel()
  {
  }

  protected UserLevel(Parcel paramParcel)
  {
    super(paramParcel);
    this.title = paramParcel.readString();
    this.pic = paramParcel.readString();
    this.url = paramParcel.readString();
    this.pow = paramParcel.readInt();
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
      case 14699:
        this.pic = paramUnarchiver.readString();
        break;
      case 14057:
        this.title = paramUnarchiver.readString();
        break;
      case 19790:
        this.url = paramUnarchiver.readString();
        break;
      case 14905:
      }
      this.pow = paramUnarchiver.readInt();
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public String getPic()
  {
    return this.pic;
  }

  public int getPow()
  {
    return this.pow;
  }

  public String getTitle()
  {
    return this.title;
  }

  public String getUrl()
  {
    return this.url;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.title);
    paramParcel.writeString(this.pic);
    paramParcel.writeString(this.url);
    paramParcel.writeInt(this.pow);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.UserLevel
 * JD-Core Version:    0.6.0
 */