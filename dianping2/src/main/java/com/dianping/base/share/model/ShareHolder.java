package com.dianping.base.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ShareHolder
  implements Parcelable
{
  public static final Parcelable.Creator<ShareHolder> CREATOR = new Parcelable.Creator()
  {
    public ShareHolder createFromParcel(Parcel paramParcel)
    {
      return new ShareHolder(paramParcel);
    }

    public ShareHolder[] newArray(int paramInt)
    {
      return new ShareHolder[paramInt];
    }
  };
  public String content = "";
  public String desc = "";
  public String extra = "";
  public String imageUrl = "";
  public String scheme = "";
  public String title = "";
  public String webUrl = "";
  public String weiboContent = "";

  public ShareHolder()
  {
  }

  public ShareHolder(Parcel paramParcel)
  {
    this.title = paramParcel.readString();
    this.desc = paramParcel.readString();
    this.content = paramParcel.readString();
    this.imageUrl = paramParcel.readString();
    this.webUrl = paramParcel.readString();
    this.extra = paramParcel.readString();
    this.weiboContent = paramParcel.readString();
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.title);
    paramParcel.writeString(this.desc);
    paramParcel.writeString(this.content);
    paramParcel.writeString(this.imageUrl);
    paramParcel.writeString(this.webUrl);
    paramParcel.writeString(this.extra);
    paramParcel.writeString(this.weiboContent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.model.ShareHolder
 * JD-Core Version:    0.6.0
 */