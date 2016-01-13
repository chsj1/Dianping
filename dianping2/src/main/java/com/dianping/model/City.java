package com.dianping.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.archive.Decoding;
import com.dianping.archive.DecodingFactory;
import com.dianping.archive.Unarchiver;

public class City
  implements Parcelable, Decoding
{
  private static final int BOOKING_MASK = 8192;
  private static final int BRAND_PROMO = 512;
  private static final int CHECKIN_TOPIC_MASK = 4096;
  private static final int CITY_METRO_MASK = 8388608;
  public static final Parcelable.Creator<City> CREATOR;
  public static final DecodingFactory<City> DECODER;
  public static final City DEFAULT = new City(0, "上海", "021", true, true, 31.230708D, 121.472916D, 83, true, true, true, true, 895);
  private static final int FOREIGN_MASK = 65536;
  private static final int FOREIGN_MASK_NEW = 1048576;
  private static final int HOTEL_MASK = 32768;
  private static final int HOT_TUAN_MASK = 524288;
  private static final int LOCAL_DISH_MASK = 64;
  private static final int LOCAL_PROMO_MASK = 16;
  private static final int LOCAL_TUAN_MASK = 256;
  private static final int MEMBERCARD_MASK = 1024;
  private static final int MULTI_CAT_MASK = 8;
  private static final int PREPAIDCARD_MASK = 16384;
  private static final int PROMOLIST_MASK = 131072;
  private static final int PROMO_MASK = 1;
  private static final int QRCODE_MASK = 128;
  private static final int RANK_INDEX_MASK = 32;
  private static final int TOP_MASK = 4;
  private static final int TRIP_GUIDE_MASK = 262144;
  private static final int TUAN_MASK = 2;
  private String areaCode;
  private int firstChar;
  private int flag;
  private int id;
  private boolean isLocalDish;
  private boolean isLocalPromoCity;
  private boolean isPromo;
  private boolean isRankIndexCity;
  private boolean isTop;
  private boolean isTuan;
  private double latitude;
  private double longitude;
  private String name;
  private String url;

  static
  {
    DECODER = new DecodingFactory()
    {
      public City[] createArray(int paramInt)
      {
        return new City[paramInt];
      }

      public City createInstance(int paramInt)
      {
        if (paramInt == 3499)
          return new City(null);
        return null;
      }
    };
    CREATOR = new Parcelable.Creator()
    {
      public City createFromParcel(Parcel paramParcel)
      {
        return new City(paramParcel, null);
      }

      public City[] newArray(int paramInt)
      {
        return new City[paramInt];
      }
    };
  }

  private City()
  {
  }

  public City(int paramInt1, String paramString1, String paramString2, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isTuan = false;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.flag = paramInt3;
    this.isRankIndexCity = false;
  }

  public City(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean2)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isPromo = paramBoolean1;
    this.isTuan = false;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.isTop = paramBoolean2;
    this.isLocalPromoCity = false;
    this.isRankIndexCity = false;
    this.isLocalDish = false;
  }

  public City(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isPromo = paramBoolean1;
    this.isTuan = false;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.isTop = paramBoolean2;
    this.isLocalPromoCity = paramBoolean3;
    this.isRankIndexCity = false;
    this.isLocalDish = false;
  }

  public City(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean3, boolean paramBoolean4)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isPromo = paramBoolean1;
    this.isTuan = paramBoolean2;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.isTop = paramBoolean3;
    this.isLocalPromoCity = paramBoolean4;
    this.isRankIndexCity = false;
    this.isLocalDish = false;
  }

  public City(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isPromo = paramBoolean1;
    this.isTuan = paramBoolean2;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.isTop = paramBoolean3;
    this.isLocalPromoCity = paramBoolean4;
    this.isRankIndexCity = paramBoolean5;
    this.isLocalDish = false;
  }

  public City(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isPromo = paramBoolean1;
    this.isTuan = paramBoolean2;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.firstChar = paramInt2;
    this.isTop = paramBoolean3;
    this.isLocalPromoCity = paramBoolean4;
    this.isRankIndexCity = paramBoolean5;
    this.isLocalDish = paramBoolean6;
  }

  public City(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, int paramInt3)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isPromo = paramBoolean1;
    this.isTuan = paramBoolean2;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.firstChar = paramInt2;
    this.isTop = paramBoolean3;
    this.isLocalPromoCity = paramBoolean4;
    this.isRankIndexCity = paramBoolean5;
    this.isLocalDish = paramBoolean6;
    this.flag = paramInt3;
  }

  public City(int paramInt1, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, int paramInt3, String paramString3)
  {
    this.id = paramInt1;
    this.name = paramString1;
    this.areaCode = paramString2;
    this.isPromo = paramBoolean1;
    this.isTuan = paramBoolean2;
    this.firstChar = paramInt2;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.firstChar = paramInt2;
    this.isTop = paramBoolean3;
    this.isLocalPromoCity = paramBoolean4;
    this.isRankIndexCity = paramBoolean5;
    this.isLocalDish = paramBoolean6;
    this.flag = paramInt3;
    this.url = paramString3;
  }

  private City(Parcel paramParcel)
  {
    this.id = paramParcel.readInt();
    this.name = paramParcel.readString();
    this.areaCode = paramParcel.readString();
    if (paramParcel.readInt() != 0)
    {
      bool1 = true;
      this.isPromo = bool1;
      if (paramParcel.readInt() == 0)
        break label156;
      bool1 = true;
      label53: this.isTuan = bool1;
      this.latitude = paramParcel.readDouble();
      this.longitude = paramParcel.readDouble();
      this.firstChar = paramParcel.readInt();
      if (paramParcel.readInt() != 1)
        break label161;
      bool1 = true;
      label92: this.isTop = bool1;
      if (paramParcel.readInt() != 1)
        break label166;
      bool1 = true;
      label107: this.isLocalPromoCity = bool1;
      if (paramParcel.readInt() != 1)
        break label171;
      bool1 = true;
      label122: this.isRankIndexCity = bool1;
      if (paramParcel.readInt() != 1)
        break label176;
    }
    label156: label161: label166: label171: label176: for (boolean bool1 = bool2; ; bool1 = false)
    {
      this.isLocalDish = bool1;
      this.flag = paramParcel.readInt();
      return;
      bool1 = false;
      break;
      bool1 = false;
      break label53;
      bool1 = false;
      break label92;
      bool1 = false;
      break label107;
      bool1 = false;
      break label122;
    }
  }

  public static City fromDPObject(DPObject paramDPObject)
  {
    return new City(paramDPObject.getInt("ID"), paramDPObject.getString("Name"), paramDPObject.getString("AreaCode"), paramDPObject.getBoolean("IsPromo"), paramDPObject.getBoolean("IsTuan"), paramDPObject.getDouble("Lat"), paramDPObject.getDouble("Lng"), paramDPObject.getInt("FirstChar"), paramDPObject.getBoolean("IsTop"), paramDPObject.getBoolean("IsLocalPromoCity"), paramDPObject.getBoolean("IsRankIndexCity"), paramDPObject.getBoolean("IsLocalDish"), paramDPObject.getInt("Flag"));
  }

  public static City fromDPObjectWithUrl(DPObject paramDPObject)
  {
    return new City(paramDPObject.getInt("ID"), paramDPObject.getString("Name"), paramDPObject.getString("AreaCode"), paramDPObject.getBoolean("IsPromo"), paramDPObject.getBoolean("IsTuan"), paramDPObject.getDouble("Lat"), paramDPObject.getDouble("Lng"), paramDPObject.getInt("FirstChar"), paramDPObject.getBoolean("IsTop"), paramDPObject.getBoolean("IsLocalPromoCity"), paramDPObject.getBoolean("IsRankIndexCity"), paramDPObject.getBoolean("IsLocalDish"), paramDPObject.getInt("Flag"), paramDPObject.getString("Url"));
  }

  public String areaCode()
  {
    return this.areaCode;
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
      case 2331:
        this.id = paramUnarchiver.readInt();
        break;
      case 61071:
        this.name = paramUnarchiver.readString();
        break;
      case 59577:
        this.areaCode = paramUnarchiver.readString();
        break;
      case 54116:
        this.isPromo = paramUnarchiver.readBoolean();
        break;
      case 50646:
        this.isTuan = paramUnarchiver.readBoolean();
        break;
      case 10622:
        this.latitude = paramUnarchiver.readDouble();
        break;
      case 11012:
        this.longitude = paramUnarchiver.readDouble();
        break;
      case 23902:
        this.firstChar = paramUnarchiver.readInt();
        break;
      case 18321:
        this.isTop = paramUnarchiver.readBoolean();
        break;
      case 2054:
        this.isLocalPromoCity = paramUnarchiver.readBoolean();
        break;
      case 37853:
        this.isRankIndexCity = paramUnarchiver.readBoolean();
        break;
      case 64690:
        this.isLocalDish = paramUnarchiver.readBoolean();
        break;
      case 29613:
      }
      this.flag = paramUnarchiver.readInt();
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(City paramCity)
  {
    if (paramCity == null);
    do
      return false;
    while ((this.id != paramCity.id()) || (!this.name.equals(paramCity.name())) || (this.flag != paramCity.flag()));
    return true;
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject == this) || (((paramObject instanceof City)) && (((City)paramObject).id == this.id));
  }

  public String firstChar()
  {
    if (this.firstChar == 0)
      return "";
    return String.valueOf((char)this.firstChar);
  }

  public int flag()
  {
    return this.flag;
  }

  public int hashCode()
  {
    return this.id;
  }

  public int id()
  {
    return this.id;
  }

  public boolean isBookingCity()
  {
    return (this.flag & 0x2000) > 0;
  }

  public boolean isBrandPromo()
  {
    return (this.flag & 0x200) > 0;
  }

  public boolean isForeign()
  {
    return (this.flag & 0x10000) > 0;
  }

  public boolean isForeignCity()
  {
    return (this.flag & 0x100000) > 0;
  }

  public boolean isLocalDish()
  {
    if ((this.flag & 0x40) > 0)
      return true;
    return this.isLocalDish;
  }

  public boolean isLocalPromo()
  {
    if ((this.flag & 0x10) > 0)
      return true;
    return this.isLocalPromoCity;
  }

  public boolean isLocalTuan()
  {
    return (this.flag & 0x100) > 0;
  }

  public boolean isMetroCity()
  {
    return (this.flag & 0x800000) != 0;
  }

  public boolean isPrepaidCardCity()
  {
    return (this.flag & 0x4000) > 0;
  }

  public boolean isPromo()
  {
    if ((this.flag & 0x1) > 0)
      return true;
    return this.isPromo;
  }

  public boolean isQRCode()
  {
    return (this.flag & 0x80) > 0;
  }

  public boolean isRankIndexCity()
  {
    if ((this.flag & 0x20) > 0)
      return true;
    return this.isRankIndexCity;
  }

  public boolean isTripGuide()
  {
    return (this.flag & 0x40000) > 0;
  }

  public boolean isTuan()
  {
    return (this.flag & 0x2) > 0;
  }

  public double latitude()
  {
    return this.latitude;
  }

  public double longitude()
  {
    return this.longitude;
  }

  public String name()
  {
    return this.name;
  }

  public DPObject toDPObject()
  {
    return new DPObject("City").edit().putInt("ID", this.id).putString("Name", this.name).putString("AreaCode", this.areaCode).putBoolean("IsPromo", this.isPromo).putBoolean("IsTuan", this.isTuan).putDouble("Lat", this.latitude).putDouble("Lng", this.longitude).putInt("FirstChar", this.firstChar).putBoolean("IsTop", this.isTop).putBoolean("IsLocalPromoCity", this.isLocalPromoCity).putBoolean("IsRankIndexCity", this.isRankIndexCity).putBoolean("IsLocalDish", this.isLocalDish).putBoolean("IsLocalDish", this.isLocalDish).putInt("Flag", this.flag).generate();
  }

  public String toString()
  {
    return this.id + " : " + this.name;
  }

  public String url()
  {
    return this.url;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeInt(this.id);
    paramParcel.writeString(this.name);
    paramParcel.writeString(this.areaCode);
    if (this.isPromo)
    {
      paramInt = 1;
      paramParcel.writeInt(paramInt);
      if (!this.isTuan)
        break label148;
      paramInt = 1;
      label49: paramParcel.writeInt(paramInt);
      paramParcel.writeDouble(this.latitude);
      paramParcel.writeDouble(this.longitude);
      paramParcel.writeInt(this.firstChar);
      if (!this.isTop)
        break label153;
      paramInt = 1;
      label87: paramParcel.writeInt(paramInt);
      if (!this.isLocalPromoCity)
        break label158;
      paramInt = 1;
      label101: paramParcel.writeInt(paramInt);
      if (!this.isRankIndexCity)
        break label163;
      paramInt = 1;
      label115: paramParcel.writeInt(paramInt);
      if (!this.isLocalDish)
        break label168;
    }
    label148: label153: label158: label163: label168: for (paramInt = i; ; paramInt = 0)
    {
      paramParcel.writeInt(paramInt);
      paramParcel.writeInt(this.flag);
      return;
      paramInt = 0;
      break;
      paramInt = 0;
      break label49;
      paramInt = 0;
      break label87;
      paramInt = 0;
      break label101;
      paramInt = 0;
      break label115;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.model.City
 * JD-Core Version:    0.6.0
 */