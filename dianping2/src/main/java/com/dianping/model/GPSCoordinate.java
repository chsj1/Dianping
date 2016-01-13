package com.dianping.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.text.DecimalFormat;

public class GPSCoordinate
  implements Parcelable
{
  public static final Parcelable.Creator<GPSCoordinate> CREATOR;
  private static final DecimalFormat FMT;
  public static final GPSCoordinate NULL = new GPSCoordinate((0.0D / 0.0D), (0.0D / 0.0D), 0, 0L, "null");
  private static final double RADIUS = 6371000.0D;
  private final int accuracy;
  private final double latitude;
  private final double longitude;
  private final String source;
  private final long timeOffset;

  static
  {
    CREATOR = new GPSCoordinate.1();
    FMT = new DecimalFormat("0.#####");
  }

  public GPSCoordinate(double paramDouble1, double paramDouble2)
  {
    this(paramDouble1, paramDouble2, 0, 0L, "");
  }

  public GPSCoordinate(double paramDouble1, double paramDouble2, int paramInt, long paramLong, String paramString)
  {
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.accuracy = paramInt;
    this.timeOffset = paramLong;
    this.source = paramString;
  }

  public GPSCoordinate(Location paramLocation)
  {
    this.latitude = paramLocation.getLatitude();
    this.longitude = paramLocation.getLongitude();
    this.accuracy = (int)paramLocation.getAccuracy();
    this.timeOffset = (paramLocation.getTime() - System.currentTimeMillis());
    this.source = paramLocation.getProvider();
  }

  private GPSCoordinate(Parcel paramParcel)
  {
    this.latitude = paramParcel.readDouble();
    this.longitude = paramParcel.readDouble();
    this.accuracy = paramParcel.readInt();
    this.timeOffset = paramParcel.readLong();
    this.source = paramParcel.readString();
  }

  public int accuracy()
  {
    return this.accuracy;
  }

  protected Object clone()
  {
    return new GPSCoordinate(this.latitude, this.longitude, this.accuracy, this.timeOffset, this.source);
  }

  public int describeContents()
  {
    return 0;
  }

  public double distanceTo(GPSCoordinate paramGPSCoordinate)
  {
    if (paramGPSCoordinate == this)
      return 0.0D;
    double d1 = this.latitude / 180.0D * 3.141592653589793D;
    double d4 = this.longitude / 180.0D;
    double d2 = paramGPSCoordinate.latitude / 180.0D * 3.141592653589793D;
    double d5 = paramGPSCoordinate.longitude / 180.0D;
    double d3 = d2 - d1;
    d4 = d5 * 3.141592653589793D - d4 * 3.141592653589793D;
    d1 = Math.sin(d3 / 2.0D) * Math.sin(d3 / 2.0D) + Math.cos(d1) * Math.cos(d2) * Math.sin(d4 / 2.0D) * Math.sin(d4 / 2.0D);
    return 6371000.0D * (2.0D * Math.atan2(Math.sqrt(d1), Math.sqrt(1.0D - d1)));
  }

  public boolean isFresh(long paramLong)
  {
    return (this.timeOffset <= 0L) && (this.timeOffset >= -paramLong);
  }

  public boolean isValid()
  {
    if (this == NULL);
    do
      return false;
    while (((this.latitude == 0.0D) && (this.longitude == 0.0D)) || (this.latitude < -90.0D) || (this.latitude > 90.0D) || (this.longitude < -180.0D) || (this.longitude > 180.0D));
    return true;
  }

  public double latitude()
  {
    return this.latitude;
  }

  public String latitudeString()
  {
    return FMT.format(this.latitude);
  }

  public double longitude()
  {
    return this.longitude;
  }

  public String longitudeString()
  {
    return FMT.format(this.longitude);
  }

  public String source()
  {
    return this.source;
  }

  public long timeOffset()
  {
    return this.timeOffset;
  }

  public String toString()
  {
    if (this == NULL)
      return "(?,?) [null]";
    return "(" + FMT.format(this.latitude) + "," + FMT.format(this.longitude) + ") [" + this.accuracy + "," + this.source + "]";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeDouble(this.latitude);
    paramParcel.writeDouble(this.longitude);
    paramParcel.writeInt(this.accuracy);
    paramParcel.writeLong(this.timeOffset);
    paramParcel.writeString(this.source);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.GPSCoordinate
 * JD-Core Version:    0.6.0
 */