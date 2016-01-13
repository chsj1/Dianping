package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.Decoding;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class AccountBind
  implements Parcelable, Decoding
{
  public static final Parcelable.Creator<AccountBind> CREATOR;
  public static final DecodingFactory<AccountBind> DECODER = new DecodingFactory()
  {
    public AccountBind[] createArray(int paramInt)
    {
      return new AccountBind[paramInt];
    }

    public AccountBind createInstance(int paramInt)
    {
      if (paramInt == 31979)
        return new AccountBind(null);
      return null;
    }
  };
  private String name;
  private int state;

  static
  {
    CREATOR = new Parcelable.Creator()
    {
      public AccountBind createFromParcel(Parcel paramParcel)
      {
        return new AccountBind(paramParcel, null);
      }

      public AccountBind[] newArray(int paramInt)
      {
        return new AccountBind[paramInt];
      }
    };
  }

  private AccountBind()
  {
  }

  public AccountBind(int paramInt, String paramString)
  {
    this.state = paramInt;
    this.name = paramString;
  }

  private AccountBind(Parcel paramParcel)
  {
    this.state = paramParcel.readInt();
    this.name = paramParcel.readString();
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
      case 53430:
        this.state = paramUnarchiver.readInt();
        break;
      case 61071:
      }
      this.name = paramUnarchiver.readString();
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public String getName()
  {
    return this.name;
  }

  public int getState()
  {
    return this.state;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setState(int paramInt)
  {
    this.state = paramInt;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.name);
    paramParcel.writeInt(this.state);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.AccountBind
 * JD-Core Version:    0.6.0
 */