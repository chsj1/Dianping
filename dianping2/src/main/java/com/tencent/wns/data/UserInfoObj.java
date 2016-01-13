package com.tencent.wns.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UserInfoObj
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new g();
  private String a;
  private String b;
  private String c;
  private String d;
  private String e;
  private String f;
  private boolean g;

  public void a(Parcel paramParcel)
  {
    boolean bool = true;
    this.e = paramParcel.readString();
    this.c = paramParcel.readString();
    this.b = paramParcel.readString();
    this.f = paramParcel.readString();
    this.a = paramParcel.readString();
    this.d = paramParcel.readString();
    if (paramParcel.readByte() == 1);
    while (true)
    {
      this.g = bool;
      return;
      bool = false;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  @Deprecated
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder().append("").append(this.a).append("#").append(this.b).append("#").append(this.c).append("#").append(this.d).append("#").append(this.e).append("#").append(this.f).append("#");
    if (this.g);
    for (String str = "1"; ; str = "0")
      return str;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.e);
    paramParcel.writeString(this.c);
    paramParcel.writeString(this.b);
    paramParcel.writeString(this.f);
    paramParcel.writeString(this.a);
    paramParcel.writeString(this.d);
    if (this.g);
    for (byte b1 = 1; ; b1 = 0)
    {
      paramParcel.writeByte(b1);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.data.UserInfoObj
 * JD-Core Version:    0.6.0
 */