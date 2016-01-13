package com.tencent.qphone.base.remote;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.HashMap;

public class FromServiceMsg
  implements Parcelable, Cloneable
{
  public static final Parcelable.Creator CREATOR = new a();
  public HashMap a = new HashMap();

  @Deprecated
  public Bundle b = new Bundle();
  private int c;
  private String d = "";
  private String e;
  private String f;
  private int g;
  private int h = -1;
  private int i = -1;
  private byte[] j = new byte[0];
  private byte k = 1;
  private cloudwns.e.a l = cloudwns.e.a.a;

  public FromServiceMsg()
  {
    this.b.putByte("version", this.k);
  }

  public FromServiceMsg(Parcel paramParcel)
  {
    a(paramParcel);
  }

  public int a()
  {
    return this.h;
  }

  public void a(Parcel paramParcel)
  {
    try
    {
      this.g = paramParcel.readInt();
      this.i = paramParcel.readInt();
      this.c = paramParcel.readInt();
      this.e = paramParcel.readString();
      this.f = paramParcel.readString();
      this.b.clear();
      this.b = paramParcel.readBundle();
      this.a.clear();
      paramParcel.readMap(this.a, getClass().getClassLoader());
      if (this.b.getByte("version") > 0)
      {
        this.l = ((cloudwns.e.a)paramParcel.readSerializable());
        this.h = paramParcel.readInt();
        this.d = paramParcel.readString();
        this.j = new byte[paramParcel.readInt()];
        paramParcel.readByteArray(this.j);
      }
      return;
    }
    catch (java.lang.RuntimeException paramParcel)
    {
      Log.d("FromServiceMsg", "readFromParcel RuntimeException", paramParcel);
    }
    throw paramParcel;
  }

  public int describeContents()
  {
    return 0;
  }

  public String toString()
  {
    return "FromServiceMsg mCmd:" + this.l + " seq:" + a() + " failCode:" + this.c + " errorMsg:" + this.d + " uin:" + this.e + " serviceCmd:" + this.f + " appId:" + this.g + " appSeq:" + this.i;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    try
    {
      paramParcel.writeInt(this.g);
      paramParcel.writeInt(this.i);
      paramParcel.writeInt(this.c);
      paramParcel.writeString(this.e);
      paramParcel.writeString(this.f);
      paramParcel.writeBundle(this.b);
      paramParcel.writeMap(this.a);
      if (this.k > 0)
      {
        paramParcel.writeSerializable(this.l);
        paramParcel.writeInt(this.h);
        paramParcel.writeString(this.d);
        paramParcel.writeInt(this.j.length);
        paramParcel.writeByteArray(this.j);
      }
      return;
    }
    catch (java.lang.RuntimeException paramParcel)
    {
      Log.d("FromServiceMsg", "writeToParcel RuntimeException", paramParcel);
    }
    throw paramParcel;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.qphone.base.remote.FromServiceMsg
 * JD-Core Version:    0.6.0
 */