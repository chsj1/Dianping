package com.tencent.tencentmap.mapsdk.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class LatLng
  implements Parcelable
{
  public static final Parcelable.Creator<LatLng> CREATOR = new LatLng.1();
  public double latitude;
  public double longitude;

  public LatLng(double paramDouble1, double paramDouble2)
  {
    if ((-180.0D <= paramDouble2) && (paramDouble2 < 180.0D));
    for (this.longitude = paramDouble2; ; this.longitude = (((paramDouble2 - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D))
    {
      this.latitude = Math.max(-90.0D, Math.min(90.0D, paramDouble1));
      return;
    }
  }

  private LatLng(Parcel paramParcel)
  {
    this.latitude = paramParcel.readDouble();
    this.longitude = paramParcel.readDouble();
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    do
    {
      return true;
      if (!(paramObject instanceof LatLng))
        return false;
      paramObject = (LatLng)paramObject;
    }
    while ((Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(paramObject.latitude)) && (Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(paramObject.longitude)));
    return false;
  }

  public int hashCode()
  {
    long l = Double.doubleToLongBits(this.latitude);
    int i = (int)(l >>> 32 ^ l);
    l = Double.doubleToLongBits(this.longitude);
    return (i + 31) * 31 + (int)(l >>> 32 ^ l);
  }

  public String toString()
  {
    return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeDouble(this.latitude);
    paramParcel.writeDouble(this.longitude);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.model.LatLng
 * JD-Core Version:    0.6.0
 */