package com.dianping.selectdish.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.DPObject;
import java.io.Serializable;

public class SetItem
  implements Parcelable, Serializable
{
  public static final Parcelable.Creator<SetItem> CREATOR = new Parcelable.Creator()
  {
    public SetItem createFromParcel(Parcel paramParcel)
    {
      return new SetItem(paramParcel);
    }

    public SetItem[] newArray(int paramInt)
    {
      return new SetItem[paramInt];
    }
  };
  public int count;
  public int id;
  public String name;
  public double originPrice;
  public String unit;

  public SetItem(int paramInt, DPObject paramDPObject)
  {
    this.count = paramInt;
    this.id = paramDPObject.getInt("Id");
    this.unit = paramDPObject.getString("Unit");
    this.name = paramDPObject.getString("Name");
    try
    {
      this.originPrice = Double.parseDouble(paramDPObject.getString("OriginPrice"));
      return;
    }
    catch (Exception paramDPObject)
    {
      paramDPObject.printStackTrace();
    }
  }

  protected SetItem(Parcel paramParcel)
  {
    this.count = paramParcel.readInt();
    this.id = paramParcel.readInt();
    this.unit = paramParcel.readString();
    this.name = paramParcel.readString();
    this.originPrice = paramParcel.readDouble();
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(SetItem paramSetItem)
  {
    return (paramSetItem.id == this.id) && (paramSetItem.unit != null) && (paramSetItem.unit.equals(this.unit)) && (paramSetItem.name != null) && (paramSetItem.name.equals(this.name)) && (paramSetItem.count == this.count) && (paramSetItem.originPrice == this.originPrice);
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.count);
    paramParcel.writeInt(this.id);
    paramParcel.writeString(this.unit);
    paramParcel.writeString(this.name);
    paramParcel.writeDouble(this.originPrice);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.SetItem
 * JD-Core Version:    0.6.0
 */