package com.dianping.selectdish.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.DPObject;
import java.io.Serializable;

public class GiftInfo
  implements Parcelable, Serializable
{
  public static final Parcelable.Creator<GiftInfo> CREATOR = new Parcelable.Creator()
  {
    public GiftInfo createFromParcel(Parcel paramParcel)
    {
      return new GiftInfo(paramParcel);
    }

    public GiftInfo[] newArray(int paramInt)
    {
      return new GiftInfo[paramInt];
    }
  };
  public final int activityId;
  public final int dishId;
  public int genusType = -1;
  public final int giftId;
  public String name;
  public String validTime;

  public GiftInfo(Parcel paramParcel)
  {
    this.validTime = paramParcel.readString();
    this.dishId = paramParcel.readInt();
    this.activityId = paramParcel.readInt();
    this.giftId = paramParcel.readInt();
    this.name = paramParcel.readString();
    this.genusType = paramParcel.readInt();
  }

  public GiftInfo(DPObject paramDPObject)
  {
    this.validTime = paramDPObject.getString("ActivityTime");
    this.dishId = paramDPObject.getInt("DishId");
    this.giftId = paramDPObject.getInt("GiftId");
    this.activityId = paramDPObject.getInt("ActId");
    this.name = paramDPObject.getString("Name");
    this.genusType = paramDPObject.getInt("GenusType");
    if ((this.genusType != 2) && (this.genusType != 3))
      this.genusType = -1;
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof GiftInfo))
      paramObject = (GiftInfo)paramObject;
    return ((this.validTime == null) || (this.validTime.equals(paramObject.validTime))) && (this.dishId == paramObject.dishId) && (this.activityId == paramObject.activityId) && (this.giftId == paramObject.giftId) && ((this.name == null) || (this.name.equals(paramObject.name)));
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.validTime);
    paramParcel.writeInt(this.dishId);
    paramParcel.writeInt(this.activityId);
    paramParcel.writeInt(this.giftId);
    paramParcel.writeString(this.name);
    paramParcel.writeInt(this.genusType);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.GiftInfo
 * JD-Core Version:    0.6.0
 */