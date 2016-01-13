package com.tencent.qphone.base.remote;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import cloudwns.e.a;
import java.util.HashMap;

public class ToServiceMsg
  implements Parcelable
{
  public static final Parcelable.Creator CREATOR = new c();
  public b a;

  @Deprecated
  public Bundle b = new Bundle();
  private int c;
  private String d;
  private String e;
  private String f;
  private long g = -1L;
  private long h = -1L;
  private int i = -1;
  private byte[] j = new byte[0];
  private boolean k = true;
  private int l = -1;
  private HashMap m = new HashMap();
  private byte n = 1;
  private a o = a.a;
  private byte p = 0;

  public ToServiceMsg(Parcel paramParcel)
  {
    a(paramParcel);
  }

  private void a(Parcel paramParcel)
  {
    try
    {
      this.c = paramParcel.readInt();
      this.i = paramParcel.readInt();
      this.d = paramParcel.readString();
      this.e = paramParcel.readString();
      this.p = paramParcel.readByte();
      this.f = paramParcel.readString();
      this.h = paramParcel.readLong();
      this.b.clear();
      this.b.putAll(paramParcel.readBundle(Thread.currentThread().getContextClassLoader()));
      this.a = b.a.a(paramParcel.readStrongBinder());
      this.n = this.b.getByte("version");
      if (this.n > 0)
      {
        this.o = ((a)paramParcel.readSerializable());
        this.g = paramParcel.readLong();
        if (paramParcel.readByte() != 0)
          break label191;
      }
      label191: for (boolean bool = false; ; bool = true)
      {
        this.k = bool;
        this.j = new byte[paramParcel.readInt()];
        paramParcel.readByteArray(this.j);
        this.l = paramParcel.readInt();
        this.m.clear();
        paramParcel.readMap(this.m, ToServiceMsg.class.getClassLoader());
        return;
      }
    }
    catch (java.lang.RuntimeException paramParcel)
    {
      Log.d("ToServiceMsg", "readFromParcel RuntimeException", paramParcel);
    }
    throw paramParcel;
  }

  public int a()
  {
    return this.l;
  }

  public int describeContents()
  {
    return 0;
  }

  public String toString()
  {
    return "ToServiceMsg mCmd:" + this.o + " seq:" + a() + " appId:" + this.c + " appSeq:" + this.i + " sName:" + this.d + " uin:" + this.e + " sCmd:" + this.f + " t:" + this.h + " needResp:" + this.k;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    try
    {
      paramParcel.writeInt(this.c);
      paramParcel.writeInt(this.i);
      paramParcel.writeString(this.d);
      paramParcel.writeString(this.e);
      paramParcel.writeByte(this.p);
      paramParcel.writeString(this.f);
      paramParcel.writeLong(this.h);
      paramParcel.writeBundle(this.b);
      paramParcel.writeStrongInterface(this.a);
      if (this.n > 0)
      {
        paramParcel.writeSerializable(this.o);
        paramParcel.writeLong(this.g);
        if (!this.k)
          break label143;
      }
      label143: for (byte b1 = 1; ; b1 = 0)
      {
        paramParcel.writeByte(b1);
        paramParcel.writeInt(this.j.length);
        paramParcel.writeByteArray(this.j);
        paramParcel.writeInt(this.l);
        paramParcel.writeMap(this.m);
        return;
      }
    }
    catch (java.lang.RuntimeException paramParcel)
    {
      Log.d("ToServiceMsg", "writeToParcel RuntimeException", paramParcel);
    }
    throw paramParcel;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.qphone.base.remote.ToServiceMsg
 * JD-Core Version:    0.6.0
 */