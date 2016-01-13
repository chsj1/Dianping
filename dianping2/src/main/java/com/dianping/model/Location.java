package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.Decoding;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Location
  implements Parcelable, Decoding
{
  private static final SingleClassLoader CITY_CL;
  public static final Parcelable.Creator<Location> CREATOR;
  public static final DecodingFactory<Location> DECODER;
  public static final DecimalFormat FMT = new DecimalFormat("#.00000", new DecimalFormatSymbols(Locale.ENGLISH));
  private int accuracy;
  private String address;
  private City city;
  private double latitude;
  private double longitude;
  private double offsetLatitude;
  private double offsetLongitude;

  static
  {
    DECODER = new DecodingFactory()
    {
      public Location[] createArray(int paramInt)
      {
        return new Location[paramInt];
      }

      public Location createInstance(int paramInt)
      {
        if (paramInt == 30463)
          return new Location(null);
        return null;
      }
    };
    CREATOR = new Parcelable.Creator()
    {
      public Location createFromParcel(Parcel paramParcel)
      {
        return new Location(paramParcel, null);
      }

      public Location[] newArray(int paramInt)
      {
        return new Location[paramInt];
      }
    };
    CITY_CL = new SingleClassLoader(City.class);
  }

  private Location()
  {
  }

  public Location(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, String paramString, City paramCity)
  {
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.offsetLatitude = paramDouble3;
    this.offsetLongitude = paramDouble4;
    this.address = paramString;
    this.city = paramCity;
  }

  private Location(Parcel paramParcel)
  {
    this.latitude = paramParcel.readDouble();
    this.longitude = paramParcel.readDouble();
    this.offsetLatitude = paramParcel.readDouble();
    this.offsetLongitude = paramParcel.readDouble();
    this.address = paramParcel.readString();
    this.city = ((City)paramParcel.readParcelable(CITY_CL));
    this.accuracy = paramParcel.readInt();
  }

  public int accuracy()
  {
    return this.accuracy;
  }

  public String address()
  {
    return this.address;
  }

  public City city()
  {
    return this.city;
  }

  public GPSCoordinate coord()
  {
    if ((this.offsetLatitude != 0.0D) && (this.offsetLongitude != 0.0D))
      return new GPSCoordinate(this.offsetLatitude, this.offsetLongitude);
    return new GPSCoordinate(this.latitude, this.longitude);
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
      case 10622:
        this.latitude = paramUnarchiver.readDouble();
        break;
      case 11012:
        this.longitude = paramUnarchiver.readDouble();
        break;
      case 13688:
        this.offsetLatitude = paramUnarchiver.readDouble();
        break;
      case 15334:
        this.offsetLongitude = paramUnarchiver.readDouble();
        break;
      case 11524:
        this.address = paramUnarchiver.readString();
        break;
      case 3499:
        this.city = ((City)paramUnarchiver.readObject(City.DECODER));
        break;
      case 39378:
      }
      this.accuracy = paramUnarchiver.readInt();
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public double latitude()
  {
    return this.latitude;
  }

  public double longitude()
  {
    return this.longitude;
  }

  public double offsetLatitude()
  {
    return this.offsetLatitude;
  }

  public double offsetLongitude()
  {
    return this.offsetLongitude;
  }

  public String toString()
  {
    if (this.address != null)
      return this.address;
    if ((this.offsetLatitude != 0.0D) && (this.offsetLongitude != 0.0D))
      return "(" + FMT.format(this.offsetLatitude) + ", " + FMT.format(this.offsetLongitude) + ")";
    return "(" + FMT.format(this.latitude) + ", " + FMT.format(this.longitude) + ")";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeDouble(this.latitude);
    paramParcel.writeDouble(this.longitude);
    paramParcel.writeDouble(this.offsetLatitude);
    paramParcel.writeDouble(this.offsetLongitude);
    paramParcel.writeString(this.address);
    paramParcel.writeParcelable(this.city, paramInt);
    paramParcel.writeInt(this.accuracy);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.Location
 * JD-Core Version:    0.6.0
 */