package com.tencent.wns.ipc;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.tencent.wns.data.A2Ticket;
import com.tencent.wns.data.B2Ticket;
import java.util.Map;

public abstract interface b extends IInterface
{
  public abstract int a(int paramInt, Bundle paramBundle, a parama);

  public abstract int a(Bundle paramBundle);

  public abstract int a(String paramString, long paramLong1, long paramLong2, boolean paramBoolean);

  public abstract A2Ticket a(String paramString);

  public abstract B2Ticket a(long paramLong);

  public abstract Map a(String[] paramArrayOfString);

  public abstract void a(long paramLong, boolean paramBoolean);

  public abstract void a(long paramLong, boolean paramBoolean, int paramInt);

  public abstract void a(String paramString1, String paramString2);

  public abstract boolean a();

  public abstract int b(String paramString);

  public abstract Map b();

  public abstract int c();

  public abstract boolean c(String paramString);

  public abstract Map d();

  public abstract long e();

  public abstract String f();

  public static abstract class a extends Binder
    implements b
  {
    public a()
    {
      attachInterface(this, "com.tencent.wns.ipc.IWnsService");
    }

    public static b a(IBinder paramIBinder)
    {
      if (paramIBinder == null)
        return null;
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.tencent.wns.ipc.IWnsService");
      if ((localIInterface != null) && ((localIInterface instanceof b)))
        return (b)localIInterface;
      return new b.a.a(paramIBinder);
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
    {
      boolean bool1 = false;
      boolean bool2 = false;
      int j = 0;
      int i = 0;
      Object localObject;
      long l1;
      switch (paramInt1)
      {
      default:
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
        paramParcel2.writeString("com.tencent.wns.ipc.IWnsService");
        return true;
      case 1:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        bool1 = a();
        paramParcel2.writeNoException();
        paramInt1 = i;
        if (bool1)
          paramInt1 = 1;
        paramParcel2.writeInt(paramInt1);
        return true;
      case 2:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0);
        for (localObject = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1); ; localObject = null)
        {
          paramInt1 = a(paramInt1, (Bundle)localObject, a.a.a(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
      case 3:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        if (paramParcel1.readInt() != 0);
        for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1); ; paramParcel1 = null)
        {
          paramInt1 = a(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
      case 4:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        l1 = paramParcel1.readLong();
        if (paramParcel1.readInt() != 0)
          bool1 = true;
        a(l1, bool1, paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      case 5:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        l1 = paramParcel1.readLong();
        bool1 = bool2;
        if (paramParcel1.readInt() != 0)
          bool1 = true;
        a(l1, bool1);
        paramParcel2.writeNoException();
        return true;
      case 6:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        a(paramParcel1.readString(), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      case 7:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramParcel1 = a(paramParcel1.readString());
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 8:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramParcel1 = a(paramParcel1.createStringArray());
        paramParcel2.writeNoException();
        paramParcel2.writeMap(paramParcel1);
        return true;
      case 9:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramParcel1 = b();
        paramParcel2.writeNoException();
        paramParcel2.writeMap(paramParcel1);
        return true;
      case 10:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramParcel1 = a(paramParcel1.readLong());
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        }
        paramParcel2.writeInt(0);
        return true;
      case 11:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramInt1 = c();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      case 12:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramParcel1 = d();
        paramParcel2.writeNoException();
        paramParcel2.writeMap(paramParcel1);
        return true;
      case 13:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        localObject = paramParcel1.readString();
        l1 = paramParcel1.readLong();
        long l2 = paramParcel1.readLong();
        if (paramParcel1.readInt() != 0);
        for (bool1 = true; ; bool1 = false)
        {
          paramInt1 = a((String)localObject, l1, l2, bool1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
      case 14:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        paramInt1 = b(paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      case 15:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        bool1 = c(paramParcel1.readString());
        paramParcel2.writeNoException();
        paramInt1 = j;
        if (bool1)
          paramInt1 = 1;
        paramParcel2.writeInt(paramInt1);
        return true;
      case 16:
        paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
        l1 = e();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l1);
        return true;
      case 17:
      }
      paramParcel1.enforceInterface("com.tencent.wns.ipc.IWnsService");
      paramParcel1 = f();
      paramParcel2.writeNoException();
      paramParcel2.writeString(paramParcel1);
      return true;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.ipc.b
 * JD-Core Version:    0.6.0
 */