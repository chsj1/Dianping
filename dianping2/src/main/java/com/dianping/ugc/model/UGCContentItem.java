package com.dianping.ugc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.UUID;

public class UGCContentItem
  implements Parcelable, Comparable<UGCContentItem>
{
  public static final Parcelable.Creator<UGCContentItem> CREATOR = new Parcelable.Creator()
  {
    public UGCContentItem createFromParcel(Parcel paramParcel)
    {
      return new UGCContentItem(paramParcel);
    }

    public UGCContentItem[] newArray(int paramInt)
    {
      return new UGCContentItem[paramInt];
    }
  };
  public String draftId;
  public boolean editable = true;
  public String id = UUID.randomUUID().toString();
  public String shopId;
  public String shopName;
  public long time = System.currentTimeMillis();

  public UGCContentItem()
  {
  }

  public UGCContentItem(Parcel paramParcel)
  {
    this.id = paramParcel.readString();
    this.draftId = paramParcel.readString();
    this.shopId = paramParcel.readString();
    this.shopName = paramParcel.readString();
    this.time = paramParcel.readLong();
    if (paramParcel.readInt() == 1);
    while (true)
    {
      this.editable = bool;
      return;
      bool = false;
    }
  }

  public int compareTo(UGCContentItem paramUGCContentItem)
  {
    if (this.time < paramUGCContentItem.time)
      return 1;
    if (this.time > paramUGCContentItem.time)
      return -1;
    return 0;
  }

  public int describeContents()
  {
    return 0;
  }

  public String getType()
  {
    return "unknown";
  }

  public boolean isEditable()
  {
    return this.editable;
  }

  public void setEditable(boolean paramBoolean)
  {
    this.editable = paramBoolean;
  }

  public void updateEditTime()
  {
    this.time = System.currentTimeMillis();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.id);
    paramParcel.writeString(this.draftId);
    paramParcel.writeString(this.shopId);
    paramParcel.writeString(this.shopName);
    paramParcel.writeLong(this.time);
    if (this.editable);
    for (paramInt = 1; ; paramInt = 0)
    {
      paramParcel.writeInt(paramInt);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.model.UGCContentItem
 * JD-Core Version:    0.6.0
 */