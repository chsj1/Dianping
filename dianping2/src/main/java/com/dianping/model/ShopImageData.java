package com.dianping.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ShopImageData
  implements Parcelable
{
  private static final SingleClassLoader BITMAP_CL;
  public static final Parcelable.Creator<ShopImageData> CREATOR = new Parcelable.Creator()
  {
    public ShopImageData createFromParcel(Parcel paramParcel)
    {
      return new ShopImageData(paramParcel, null);
    }

    public ShopImageData[] newArray(int paramInt)
    {
      return new ShopImageData[paramInt];
    }
  };
  public String cateName;
  public int direction;
  public int isSeleted;
  public String newPath;
  public String oriPath;
  public int photoId;
  public String photoName;
  public String price;
  public int star;
  public String uploadPath;

  static
  {
    BITMAP_CL = new SingleClassLoader(Bitmap.class);
  }

  public ShopImageData()
  {
  }

  private ShopImageData(Parcel paramParcel)
  {
    this.photoId = paramParcel.readInt();
    this.cateName = paramParcel.readString();
    this.photoName = paramParcel.readString();
    this.price = paramParcel.readString();
    this.star = paramParcel.readInt();
    this.oriPath = paramParcel.readString();
    this.uploadPath = paramParcel.readString();
    this.newPath = paramParcel.readString();
    this.direction = paramParcel.readInt();
    this.isSeleted = paramParcel.readInt();
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.photoId);
    paramParcel.writeString(this.cateName);
    paramParcel.writeString(this.photoName);
    paramParcel.writeString(this.price);
    paramParcel.writeInt(this.star);
    paramParcel.writeString(this.oriPath);
    paramParcel.writeString(this.uploadPath);
    paramParcel.writeString(this.newPath);
    paramParcel.writeInt(this.direction);
    paramParcel.writeInt(this.isSeleted);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.ShopImageData
 * JD-Core Version:    0.6.0
 */