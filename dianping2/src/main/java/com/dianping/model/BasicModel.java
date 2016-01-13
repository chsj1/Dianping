package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.Decoding;
import com.dianping.archive.Unarchiver;

public class BasicModel
  implements Parcelable, Decoding
{
  public static final Parcelable.Creator<BasicModel> CREATOR = new Parcelable.Creator()
  {
    public BasicModel createFromParcel(Parcel paramParcel)
    {
      return new BasicModel(paramParcel);
    }

    public BasicModel[] newArray(int paramInt)
    {
      return new BasicModel[paramInt];
    }
  };

  protected BasicModel()
  {
  }

  protected BasicModel(Parcel paramParcel)
  {
  }

  public void decode(Unarchiver paramUnarchiver)
    throws ArchiveException
  {
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.BasicModel
 * JD-Core Version:    0.6.0
 */