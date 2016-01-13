package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.Decoding;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class AccountBindResult
  implements Parcelable, Decoding
{
  public static final Parcelable.Creator<AccountBindResult> CREATOR;
  public static final DecodingFactory<AccountBindResult> DECODER = new DecodingFactory()
  {
    public AccountBindResult[] createArray(int paramInt)
    {
      return new AccountBindResult[paramInt];
    }

    public AccountBindResult createInstance(int paramInt)
    {
      if (paramInt == 24173)
        return new AccountBindResult(null);
      return null;
    }
  };
  private AccountBind[] bindList;
  private boolean shouldShow;

  static
  {
    CREATOR = new Parcelable.Creator()
    {
      public AccountBindResult createFromParcel(Parcel paramParcel)
      {
        return new AccountBindResult(paramParcel, null);
      }

      public AccountBindResult[] newArray(int paramInt)
      {
        return new AccountBindResult[paramInt];
      }
    };
  }

  private AccountBindResult()
  {
  }

  private AccountBindResult(Parcel paramParcel)
  {
    if (paramParcel.readInt() == 1);
    while (true)
    {
      this.shouldShow = bool;
      return;
      bool = false;
    }
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
      case 18972:
        this.shouldShow = paramUnarchiver.readBoolean();
        break;
      case 32077:
      }
      this.bindList = ((AccountBind[])paramUnarchiver.readArray(AccountBind.DECODER));
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public AccountBind[] getBindList()
  {
    return this.bindList;
  }

  public Boolean getShouldShow()
  {
    return Boolean.valueOf(this.shouldShow);
  }

  public void setShouldShow(Boolean paramBoolean)
  {
    this.shouldShow = paramBoolean.booleanValue();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.AccountBindResult
 * JD-Core Version:    0.6.0
 */