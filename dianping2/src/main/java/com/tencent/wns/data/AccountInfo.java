package com.tencent.wns.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class AccountInfo
  implements Parcelable, Comparable
{
  public static final Parcelable.Creator CREATOR = new b();
  private String a;

  @Deprecated
  private long b;
  private long c;
  private int d;
  private int e = -1;
  private int f;
  private String g;
  private int h;
  private boolean i;
  private UserId j;
  private int k = 0;
  private String l;
  private String m;
  private String n;
  private String o;
  private boolean p;
  private String q;

  public int a(AccountInfo paramAccountInfo)
  {
    long l1 = c();
    long l2 = paramAccountInfo.c();
    if (l1 > l2)
      return 1;
    if (l1 < l2)
      return -1;
    return 0;
  }

  public String a()
  {
    return this.a;
  }

  public void a(int paramInt)
  {
    this.d = paramInt;
  }

  @Deprecated
  public void a(long paramLong)
  {
    this.b = paramLong;
  }

  public void a(Parcel paramParcel)
  {
    boolean bool2 = true;
    a(paramParcel.readString());
    a(paramParcel.readLong());
    b(paramParcel.readLong());
    a(paramParcel.readInt());
    b(paramParcel.readInt());
    c(paramParcel.readInt());
    b(paramParcel.readString());
    a((UserId)paramParcel.readParcelable(UserId.class.getClassLoader()));
    d(paramParcel.readInt());
    if (paramParcel.readInt() == 0)
    {
      bool1 = false;
      a(bool1);
      this.n = paramParcel.readString();
      this.l = paramParcel.readString();
      this.o = paramParcel.readString();
      this.m = paramParcel.readString();
      if (paramParcel.readByte() != 1)
        break label157;
    }
    label157: for (boolean bool1 = bool2; ; bool1 = false)
    {
      this.p = bool1;
      this.q = paramParcel.readString();
      return;
      bool1 = true;
      break;
    }
  }

  public void a(UserId paramUserId)
  {
    this.j = paramUserId;
  }

  public void a(String paramString)
  {
    this.a = paramString;
  }

  public void a(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }

  @Deprecated
  public long b()
  {
    return this.b;
  }

  public void b(int paramInt)
  {
    this.e = paramInt;
  }

  public void b(long paramLong)
  {
    this.c = paramLong;
  }

  public void b(String paramString)
  {
    this.g = paramString;
  }

  public long c()
  {
    return this.c;
  }

  public void c(int paramInt)
  {
    this.f = paramInt;
  }

  public int d()
  {
    return this.d;
  }

  public void d(int paramInt)
  {
    this.k = paramInt;
  }

  public int describeContents()
  {
    return 0;
  }

  public int e()
  {
    return this.e;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    do
    {
      return true;
      if (paramObject == null)
        return false;
      if (getClass() != paramObject.getClass())
        return false;
      paramObject = (AccountInfo)paramObject;
    }
    while (this.b == paramObject.b);
    return false;
  }

  public int f()
  {
    return this.f;
  }

  public String g()
  {
    return this.g;
  }

  public boolean h()
  {
    return this.i;
  }

  public int hashCode()
  {
    return (int)(this.b ^ this.b >>> 32) + 31;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder().append("AccountInfo [nameAccount=").append(this.a).append(", uin=").append(this.b).append(", uid=");
    if (this.j != null);
    for (String str = this.j.b; ; str = null)
      return str + ", localLoginType=" + this.k + ", loginTime=" + this.c + ", age=" + this.d + ", gender=" + this.e + ", faceId=" + this.f + ", nickName=" + this.g + ", loginType=" + this.h + " , isRegister=" + this.i + ",country=" + this.l + ",province=" + this.m + ",city=" + this.n + ",logo=" + this.o + ",isClosed=" + this.p + ",openId=" + this.q + "]";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    byte b1 = 1;
    paramParcel.writeString(a());
    paramParcel.writeLong(b());
    paramParcel.writeLong(c());
    paramParcel.writeInt(d());
    paramParcel.writeInt(e());
    paramParcel.writeInt(f());
    paramParcel.writeString(g());
    paramParcel.writeParcelable(this.j, paramInt);
    paramParcel.writeInt(this.k);
    if (h())
    {
      paramInt = 1;
      paramParcel.writeInt(paramInt);
      paramParcel.writeString(this.n);
      paramParcel.writeString(this.l);
      paramParcel.writeString(this.o);
      paramParcel.writeString(this.m);
      if (!this.p)
        break label147;
    }
    while (true)
    {
      paramParcel.writeByte(b1);
      paramParcel.writeString(this.q);
      return;
      paramInt = 0;
      break;
      label147: b1 = 0;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.data.AccountInfo
 * JD-Core Version:    0.6.0
 */