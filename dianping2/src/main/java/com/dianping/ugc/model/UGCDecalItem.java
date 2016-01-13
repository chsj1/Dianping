package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UGCDecalItem
  implements Parcelable
{
  public static final Parcelable.Creator<UGCDecalItem> CREATOR = new UGCDecalItem.1();
  public String category;
  public String fullUrl;
  public String id;
  public String name;
  public String thumbUrl;

  public UGCDecalItem(Parcel paramParcel)
  {
    this.id = paramParcel.readString();
    this.name = paramParcel.readString();
    this.thumbUrl = paramParcel.readString();
    this.fullUrl = paramParcel.readString();
    this.category = paramParcel.readString();
  }

  public UGCDecalItem(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.id = paramString1;
    this.name = paramString2;
    this.thumbUrl = paramString3;
    this.fullUrl = paramString4;
  }

  public int describeContents()
  {
    return 0;
  }

  public String toString()
  {
    return "[decal: id=" + this.id + " name=" + this.name + " category=" + this.category + " thumb url=" + this.thumbUrl + " full url=" + this.fullUrl + "]";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.id);
    paramParcel.writeString(this.name);
    paramParcel.writeString(this.thumbUrl);
    paramParcel.writeString(this.fullUrl);
    paramParcel.writeString(this.category);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.UGCDecalItem
 * JD-Core Version:    0.6.0
 */