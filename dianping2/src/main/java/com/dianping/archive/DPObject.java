package com.dianping.archive;

import I;
import J;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.dianping.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class DPObject
  implements Parcelable, Iterable<Map.Entry<Integer, Object>>
{
  public static final Parcelable.Creator<DPObject> CREATOR = new Parcelable.Creator()
  {
    public DPObject createFromParcel(Parcel paramParcel)
    {
      return new DPObject(paramParcel);
    }

    public DPObject[] newArray(int paramInt)
    {
      return new DPObject[paramInt];
    }
  };
  private static final int ERR_EOF = -1;
  private static final int ERR_MALFORMED = -2;
  public static final byte TYPE_ARRAY = 65;
  public static final byte TYPE_BOOLEAN = 66;
  public static final byte TYPE_DOUBLE = 68;
  public static final byte TYPE_INT = 73;
  public static final byte TYPE_LONG = 76;
  public static final byte TYPE_NULL = 78;
  public static final byte TYPE_OBJECT = 79;
  public static final byte TYPE_STRING = 83;
  public static final byte TYPE_TIME = 85;
  private static final boolean nativeLoaded;
  private byte[] bytes;
  private int length;
  private int start;

  static
  {
    boolean bool1 = false;
    try
    {
      System.loadLibrary("dpobj");
      boolean bool2 = a();
      bool1 = bool2;
      label23: nativeLoaded = bool1;
      return;
    }
    catch (Throwable localThrowable)
    {
      break label23;
    }
  }

  public DPObject()
  {
    this.bytes = new byte[0];
    this.start = 0;
    this.length = 0;
  }

  public DPObject(int paramInt)
  {
    this.bytes = new byte[] { 79, (byte)(paramInt >>> 8), (byte)paramInt, 90 };
    this.start = 0;
    this.length = 4;
  }

  protected DPObject(Parcel paramParcel)
  {
    this.start = paramParcel.readInt();
    this.length = paramParcel.readInt();
    this.bytes = paramParcel.createByteArray();
  }

  public DPObject(String paramString)
  {
    this(hash16(paramString));
  }

  public DPObject(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.bytes = paramArrayOfByte;
    this.start = paramInt1;
    this.length = paramInt2;
  }

  private static native boolean a();

  public static DPObject[] createArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    Object localObject1;
    if (paramInt2 < 3)
      localObject1 = null;
    while (true)
    {
      return localObject1;
      if (paramArrayOfByte[paramInt1] != 65)
        break;
      int j = (paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 8 | paramArrayOfByte[(paramInt1 + 2)] & 0xFF;
      if (j == 0)
        return new DPObject[0];
      Object localObject2;
      DPObject[] arrayOfDPObject;
      if (nativeLoaded)
      {
        paramInt1 += 3;
        localObject2 = new int[j];
        if (!nativeArraySkip(paramArrayOfByte, paramInt1, paramInt2 - 3, j, localObject2))
          break;
        arrayOfDPObject = new DPObject[j];
        int i = 0;
        paramInt2 = paramInt1;
        paramInt1 = i;
        localObject1 = arrayOfDPObject;
        if (paramInt1 >= j)
          continue;
        i = localObject2[paramInt1];
        int k = paramArrayOfByte[paramInt2];
        if (k == 78)
          arrayOfDPObject[paramInt1] = null;
        while (true)
        {
          paramInt2 += i;
          paramInt1 += 1;
          break;
          if (k != 79)
            break label166;
          arrayOfDPObject[paramInt1] = new DPObject(paramArrayOfByte, paramInt2, i);
        }
        label166: return null;
      }
      else
      {
        localObject2 = ByteBuffer.wrap(paramArrayOfByte, paramInt1 + 3, paramInt2 - 3);
        arrayOfDPObject = new DPObject[j];
        paramInt2 = ((ByteBuffer)localObject2).position();
        paramInt1 = 0;
        localObject1 = arrayOfDPObject;
        if (paramInt1 >= j)
          continue;
        if (skipAny((ByteBuffer)localObject2) != 0)
          return null;
        switch (paramArrayOfByte[paramInt2])
        {
        default:
          return null;
        case 79:
          arrayOfDPObject[paramInt1] = new DPObject(paramArrayOfByte, paramInt2, ((ByteBuffer)localObject2).position() - paramInt2);
        case 78:
        }
        while (true)
        {
          paramInt2 = ((ByteBuffer)localObject2).position();
          paramInt1 += 1;
          break;
          arrayOfDPObject[paramInt1] = null;
        }
      }
    }
    return (DPObject)null;
  }

  public static DPObject createObject(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 < 4);
    do
      return null;
    while (paramArrayOfByte[paramInt1] != 79);
    return new DPObject(paramArrayOfByte, paramInt1, paramInt2);
  }

  public static <T> T[] decodeToObjectArray(DPObject[] paramArrayOfDPObject, DecodingFactory<T> paramDecodingFactory)
    throws ArchiveException
  {
    Object[] arrayOfObject = paramDecodingFactory.createArray(paramArrayOfDPObject.length);
    int i = 0;
    while (i < paramArrayOfDPObject.length)
    {
      arrayOfObject[i] = paramArrayOfDPObject[i].decodeToObject(paramDecodingFactory);
      i += 1;
    }
    return arrayOfObject;
  }

  public static int hash16(String paramString)
  {
    int i = paramString.hashCode();
    return 0xFFFF & i ^ i >>> 16;
  }

  private static native boolean nativeArraySkip(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

  private static native int nativeSeekMember(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);

  private static native int nativeSkipAny(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  private static int seekMember(ByteBuffer paramByteBuffer, int paramInt)
  {
    if (paramByteBuffer.remaining() < 4)
      return -1;
    paramByteBuffer.position(paramByteBuffer.position() + 3);
    while (paramByteBuffer.remaining() != 0)
      switch (paramByteBuffer.get())
      {
      default:
        return -2;
      case 77:
        if (paramByteBuffer.remaining() < 3)
          return -1;
        if ((paramByteBuffer.getShort() & 0xFFFF) == paramInt)
          return 0;
        int i = skipAny(paramByteBuffer);
        if (i == 0)
          continue;
        return i;
      case 90:
        return -1;
      }
    return -1;
  }

  private static int skipAny(ByteBuffer paramByteBuffer)
  {
    int i;
    if (paramByteBuffer.remaining() == 0)
    {
      i = -1;
      return i;
    }
    switch (paramByteBuffer.get())
    {
    case 66:
    case 67:
    case 69:
    case 71:
    case 72:
    case 74:
    case 75:
    case 77:
    case 80:
    case 81:
    case 82:
    default:
      return -2;
    case 73:
      if (paramByteBuffer.remaining() < 4)
        return -1;
      paramByteBuffer.getInt();
      return 0;
    case 83:
      if (paramByteBuffer.remaining() < 2)
        return -1;
      i = paramByteBuffer.getShort() & 0xFFFF;
      if (paramByteBuffer.remaining() < i)
        return -1;
      paramByteBuffer.position(paramByteBuffer.position() + i);
      return 0;
    case 70:
    case 78:
    case 84:
      return 0;
    case 76:
      if (paramByteBuffer.remaining() < 8)
        return -1;
      paramByteBuffer.getLong();
      return 0;
    case 68:
      if (paramByteBuffer.remaining() < 8)
        return -1;
      paramByteBuffer.getDouble();
      return 0;
    case 85:
      if (paramByteBuffer.remaining() < 4)
        return -1;
      paramByteBuffer.getInt();
      return 0;
    case 79:
      if (paramByteBuffer.remaining() < 2)
        return -1;
      paramByteBuffer.getShort();
      while (true)
      {
        if (paramByteBuffer.remaining() <= 0)
          break label298;
        i = paramByteBuffer.get();
        if (i != 77)
          break;
        if (paramByteBuffer.remaining() < 2)
          return -1;
        paramByteBuffer.getShort();
        i = skipAny(paramByteBuffer);
        if (i != 0)
          return i;
      }
      if (i == 90)
        return 0;
      return -2;
      label298: return -1;
    case 65:
    }
    if (paramByteBuffer.remaining() < 2)
      return -1;
    int m = paramByteBuffer.getShort();
    int j = 0;
    while (true)
    {
      if (j >= (m & 0xFFFF))
        break label345;
      int k = skipAny(paramByteBuffer);
      i = k;
      if (k != 0)
        break;
      j += 1;
    }
    label345: return 0;
  }

  private int skipSelf()
  {
    int i;
    if (nativeLoaded)
      i = nativeSkipAny(this.bytes, this.start, this.length);
    ByteBuffer localByteBuffer;
    int j;
    do
    {
      return i;
      localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      j = skipAny(localByteBuffer);
      i = j;
    }
    while (j != 0);
    return localByteBuffer.position() - this.start;
  }

  public boolean contains(int paramInt)
  {
    if (nativeLoaded)
      if (nativeSeekMember(this.bytes, this.start, this.length, paramInt) <= 0);
    ByteBuffer localByteBuffer;
    do
    {
      return true;
      return false;
      localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
    }
    while (seekMember(localByteBuffer, paramInt) == 0);
    return false;
  }

  public boolean contains(String paramString)
  {
    return contains(hash16(paramString));
  }

  public <T> T decodeToObject(DecodingFactory<T> paramDecodingFactory)
    throws ArchiveException
  {
    return new Unarchiver(ByteBuffer.wrap(this.bytes, this.start, this.length)).readObject(paramDecodingFactory);
  }

  public int describeContents()
  {
    return 0;
  }

  public Editor edit()
  {
    return new DefaultEditor(null);
  }

  public DPObject[] getArray(int paramInt)
  {
    int i;
    int j;
    Object localObject1;
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if (paramInt <= 0)
        break label431;
      i = this.start + paramInt;
      j = this.bytes[i];
      if ((j == 65) && (paramInt + 2 < this.length))
      {
        j = (this.bytes[(i + 1)] & 0xFF) << 8 | this.bytes[(i + 2)] & 0xFF;
        if (j == 0)
          localObject1 = new DPObject[0];
      }
    }
    while (true)
    {
      return localObject1;
      i = this.start + paramInt + 3;
      Object localObject2 = new int[j];
      if (!nativeArraySkip(this.bytes, i, this.length - paramInt - 3, j, localObject2))
        break;
      DPObject[] arrayOfDPObject = new DPObject[j];
      paramInt = 0;
      localObject1 = arrayOfDPObject;
      if (paramInt >= j)
        continue;
      int k = localObject2[paramInt];
      int m = this.bytes[i];
      if (m == 78)
        arrayOfDPObject[paramInt] = null;
      while (true)
      {
        i += k;
        paramInt += 1;
        break;
        if (m != 79)
          break label229;
        arrayOfDPObject[paramInt] = new DPObject(this.bytes, i, k);
      }
      label229: return null;
      if (j != 78)
        break;
      return null;
      localObject2 = ByteBuffer.wrap(this.bytes, this.start, this.length);
      ((ByteBuffer)localObject2).order(ByteOrder.BIG_ENDIAN);
      if ((seekMember((ByteBuffer)localObject2, paramInt) != 0) || (((ByteBuffer)localObject2).remaining() <= 0) || (((ByteBuffer)localObject2).get() != 65) || (((ByteBuffer)localObject2).remaining() <= 1))
        break;
      j = ((ByteBuffer)localObject2).getShort() & 0xFFFF;
      if (j == 0)
        return new DPObject[0];
      arrayOfDPObject = new DPObject[j];
      i = ((ByteBuffer)localObject2).position();
      paramInt = 0;
      localObject1 = arrayOfDPObject;
      if (paramInt >= j)
        continue;
      if (skipAny((ByteBuffer)localObject2) != 0)
        return null;
      switch (this.bytes[i])
      {
      default:
        return null;
      case 79:
        arrayOfDPObject[paramInt] = new DPObject(this.bytes, i, ((ByteBuffer)localObject2).position() - i);
      case 78:
      }
      while (true)
      {
        i = ((ByteBuffer)localObject2).position();
        paramInt += 1;
        break;
        arrayOfDPObject[paramInt] = null;
      }
    }
    label431: return (DPObject)(DPObject)null;
  }

  public DPObject[] getArray(String paramString)
  {
    return getArray(hash16(paramString));
  }

  public boolean getBoolean(int paramInt)
  {
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if ((paramInt <= 0) || (paramInt >= this.length))
        break label113;
      paramInt = this.bytes[(this.start + paramInt)];
      if (paramInt != 84);
    }
    while (true)
    {
      return true;
      if (paramInt != 70)
        break;
      return false;
      ByteBuffer localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      if ((seekMember(localByteBuffer, paramInt) != 0) || (localByteBuffer.remaining() <= 0))
        break;
      if (localByteBuffer.get() != 84)
        return false;
    }
    label113: return false;
  }

  public boolean getBoolean(String paramString)
  {
    return getBoolean(hash16(paramString));
  }

  public int getClassHash16()
  {
    if ((this.length > 0) && (this.bytes[this.start] == 79))
      return (this.bytes[(this.start + 1)] & 0xFF) << 8 | this.bytes[(this.start + 2)] & 0xFF;
    return -1;
  }

  public double getDouble(int paramInt)
  {
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if ((paramInt > 0) && (paramInt + 8 < this.length))
      {
        paramInt = this.start + paramInt;
        if (this.bytes[paramInt] == 68)
          return Double.longBitsToDouble((this.bytes[(paramInt + 1)] & 0xFF) << 56 | (this.bytes[(paramInt + 2)] & 0xFF) << 48 | (this.bytes[(paramInt + 3)] & 0xFF) << 40 | (this.bytes[(paramInt + 4)] & 0xFF) << 32 | (this.bytes[(paramInt + 5)] & 0xFF) << 24 | (this.bytes[(paramInt + 6)] & 0xFF) << 16 | (this.bytes[(paramInt + 7)] & 0xFF) << 8 | this.bytes[(paramInt + 8)] & 0xFF);
      }
    }
    else
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      if ((seekMember(localByteBuffer, paramInt) == 0) && (localByteBuffer.remaining() > 8) && (localByteBuffer.get() == 68))
        return localByteBuffer.getDouble();
    }
    return 0.0D;
  }

  public double getDouble(String paramString)
  {
    return getDouble(hash16(paramString));
  }

  public int getInt(int paramInt)
  {
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if ((paramInt > 0) && (paramInt + 4 < this.length))
      {
        paramInt = this.start + paramInt;
        if (this.bytes[paramInt] == 73)
          return (this.bytes[(paramInt + 1)] & 0xFF) << 24 | (this.bytes[(paramInt + 2)] & 0xFF) << 16 | (this.bytes[(paramInt + 3)] & 0xFF) << 8 | this.bytes[(paramInt + 4)] & 0xFF;
      }
    }
    else
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      if ((seekMember(localByteBuffer, paramInt) == 0) && (localByteBuffer.remaining() > 4) && (localByteBuffer.get() == 73))
        return localByteBuffer.getInt();
    }
    return 0;
  }

  public int getInt(String paramString)
  {
    return getInt(hash16(paramString));
  }

  public int[] getIntArray(int paramInt)
  {
    int i;
    Object localObject1;
    Object localObject2;
    int[] arrayOfInt;
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if (paramInt > 0)
      {
        i = this.start + paramInt;
        int j = this.bytes[i];
        if ((j == 65) && (paramInt + 2 < this.length))
        {
          j = (this.bytes[(i + 1)] & 0xFF) << 8 | this.bytes[(i + 2)] & 0xFF;
          if (j == 0)
          {
            localObject1 = new int[0];
            return localObject1;
          }
          i = this.start + paramInt + 3;
          localObject2 = new int[j];
          if (nativeArraySkip(this.bytes, i, this.length - paramInt - 3, j, localObject2))
          {
            arrayOfInt = new int[j];
            paramInt = 0;
            while (true)
            {
              localObject1 = arrayOfInt;
              if (paramInt >= j)
                break;
              int k = localObject2[paramInt];
              if (this.bytes[i] != 73)
                break label258;
              arrayOfInt[paramInt] = ((this.bytes[(i + 1)] & 0xFF) << 24 | (this.bytes[(i + 2)] & 0xFF) << 16 | (this.bytes[(i + 3)] & 0xFF) << 8 | this.bytes[(i + 4)] & 0xFF);
              i += k;
              paramInt += 1;
            }
            label258: return null;
          }
        }
        else if (j == 78)
        {
          return null;
        }
      }
    }
    else
    {
      localObject2 = ByteBuffer.wrap(this.bytes, this.start, this.length);
      ((ByteBuffer)localObject2).order(ByteOrder.BIG_ENDIAN);
      if ((seekMember((ByteBuffer)localObject2, paramInt) == 0) && (((ByteBuffer)localObject2).remaining() > 0) && (((ByteBuffer)localObject2).get() == 65) && (((ByteBuffer)localObject2).remaining() > 1))
      {
        i = ((ByteBuffer)localObject2).getShort() & 0xFFFF;
        if (i == 0)
          return new int[0];
        arrayOfInt = new int[i];
        paramInt = 0;
        while (true)
        {
          localObject1 = arrayOfInt;
          if (paramInt >= i)
            break;
          if ((((ByteBuffer)localObject2).remaining() <= 4) || (((ByteBuffer)localObject2).get() != 73))
            break label399;
          arrayOfInt[paramInt] = ((ByteBuffer)localObject2).getInt();
          paramInt += 1;
        }
        label399: return null;
      }
    }
    return (I)(I)null;
  }

  public int[] getIntArray(String paramString)
  {
    return getIntArray(hash16(paramString));
  }

  public long getLong(int paramInt)
  {
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if ((paramInt > 0) && (paramInt + 8 < this.length))
      {
        paramInt = this.start + paramInt;
        if (this.bytes[paramInt] == 76)
          return (this.bytes[(paramInt + 1)] & 0xFF) << 56 | (this.bytes[(paramInt + 2)] & 0xFF) << 48 | (this.bytes[(paramInt + 3)] & 0xFF) << 40 | (this.bytes[(paramInt + 4)] & 0xFF) << 32 | (this.bytes[(paramInt + 5)] & 0xFF) << 24 | (this.bytes[(paramInt + 6)] & 0xFF) << 16 | (this.bytes[(paramInt + 7)] & 0xFF) << 8 | this.bytes[(paramInt + 8)] & 0xFF;
      }
    }
    else
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      if ((seekMember(localByteBuffer, paramInt) == 0) && (localByteBuffer.remaining() > 8) && (localByteBuffer.get() == 76))
        return localByteBuffer.getLong();
    }
    return 0L;
  }

  public long getLong(String paramString)
  {
    return getLong(hash16(paramString));
  }

  public DPObject getObject(int paramInt)
  {
    Object localObject2 = null;
    Object localObject1;
    int i;
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      localObject1 = localObject2;
      if (paramInt > 0)
      {
        i = this.start;
        i = this.bytes[(i + paramInt)];
        if ((i != 79) || (paramInt + 2 >= this.length))
          break label91;
        localObject1 = new DPObject(this.bytes, this.start + paramInt, this.length - paramInt);
      }
    }
    label91: 
    do
    {
      ByteBuffer localByteBuffer;
      do
      {
        do
        {
          do
          {
            return localObject1;
            localObject1 = localObject2;
          }
          while (i != 78);
          return null;
          localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
          localByteBuffer.order(ByteOrder.BIG_ENDIAN);
          localObject1 = localObject2;
        }
        while (seekMember(localByteBuffer, paramInt) != 0);
        localObject1 = localObject2;
      }
      while (localByteBuffer.remaining() <= 0);
      paramInt = localByteBuffer.get();
      if ((paramInt == 79) && (localByteBuffer.remaining() > 2))
        return new DPObject(this.bytes, localByteBuffer.position() - 1, this.length - localByteBuffer.position() + this.start + 1);
      localObject1 = localObject2;
    }
    while (paramInt != 78);
    return (DPObject)null;
  }

  public DPObject getObject(String paramString)
  {
    return getObject(hash16(paramString));
  }

  public String getString(int paramInt)
  {
    int j;
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if ((paramInt > 0) && (paramInt < this.length))
      {
        int i = this.start + paramInt;
        j = this.bytes[i];
        if ((j != 83) || (paramInt + 2 >= this.length))
          break label144;
        j = (this.bytes[(i + 1)] & 0xFF) << 8 | this.bytes[(i + 2)] & 0xFF;
        if (j == 0)
          return "";
        if (paramInt + 2 + j < this.length)
          try
          {
            String str = new String(this.bytes, i + 3, j, "UTF-8");
            return str;
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException1)
          {
          }
      }
    }
    label144: 
    do
      while (true)
      {
        return null;
        if (j == 78)
        {
          return null;
          Object localObject = ByteBuffer.wrap(this.bytes, this.start, this.length);
          ((ByteBuffer)localObject).order(ByteOrder.BIG_ENDIAN);
          if ((seekMember((ByteBuffer)localObject, paramInt) != 0) || (((ByteBuffer)localObject).remaining() <= 0))
            continue;
          paramInt = ((ByteBuffer)localObject).get();
          if ((paramInt != 83) || (((ByteBuffer)localObject).remaining() <= 1))
            break;
          paramInt = ((ByteBuffer)localObject).getShort() & 0xFFFF;
          if (paramInt == 0)
            return "";
          if (((ByteBuffer)localObject).remaining() < paramInt)
            continue;
          try
          {
            localObject = new String(this.bytes, ((ByteBuffer)localObject).position(), paramInt, "UTF-8");
            return localObject;
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException2)
          {
            Log.e("dpobj", "unable to decode string", localUnsupportedEncodingException2);
          }
        }
      }
    while (paramInt != 78);
    return (String)null;
  }

  public String getString(String paramString)
  {
    return getString(hash16(paramString));
  }

  public String[] getStringArray(int paramInt)
  {
    int i;
    int j;
    Object localObject1;
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if (paramInt <= 0)
        break label536;
      i = this.start + paramInt;
      j = this.bytes[i];
      if ((j == 65) && (paramInt + 2 < this.length))
      {
        j = (this.bytes[(i + 1)] & 0xFF) << 8 | this.bytes[(i + 2)] & 0xFF;
        if (j == 0)
          localObject1 = new String[0];
      }
    }
    while (true)
    {
      return localObject1;
      i = this.start + paramInt + 3;
      Object localObject2 = new int[j];
      if (!nativeArraySkip(this.bytes, i, this.length - paramInt - 3, j, localObject2))
        break;
      String[] arrayOfString = new String[j];
      paramInt = 0;
      localObject1 = arrayOfString;
      if (paramInt >= j)
        continue;
      int k = localObject2[paramInt];
      int m = this.bytes[i];
      if (m == 78)
        arrayOfString[paramInt] = null;
      while (true)
      {
        i += k;
        paramInt += 1;
        break;
        if (m != 83)
          break label267;
        m = this.bytes[(i + 1)];
        int n = this.bytes[(i + 2)];
        arrayOfString[paramInt] = new String(this.bytes, i + 3, (m & 0xFF) << 8 | n & 0xFF);
      }
      label267: return null;
      if (j != 78)
        break;
      return null;
      localObject2 = ByteBuffer.wrap(this.bytes, this.start, this.length);
      ((ByteBuffer)localObject2).order(ByteOrder.BIG_ENDIAN);
      if ((seekMember((ByteBuffer)localObject2, paramInt) != 0) || (((ByteBuffer)localObject2).remaining() <= 0))
        break;
      i = ((ByteBuffer)localObject2).get();
      if ((i != 65) || (((ByteBuffer)localObject2).remaining() <= 1))
        break;
      j = ((ByteBuffer)localObject2).getShort() & 0xFFFF;
      if (j == 0)
        return new String[0];
      arrayOfString = new String[j];
      paramInt = 0;
      localObject1 = arrayOfString;
      if (paramInt >= j)
        continue;
      if (((ByteBuffer)localObject2).remaining() == 0)
        return null;
      switch (((ByteBuffer)localObject2).get())
      {
      default:
        return null;
      case 83:
        if (((ByteBuffer)localObject2).remaining() > 1)
        {
          k = ((ByteBuffer)localObject2).getShort() & 0xFFFF;
          if (k == 0)
            arrayOfString[paramInt] = "";
          if (((ByteBuffer)localObject2).remaining() < k)
            break;
        }
      case 78:
      }
      while (true)
      {
        try
        {
          arrayOfString[paramInt] = new String(this.bytes, ((ByteBuffer)localObject2).position(), k, "UTF-8");
          ((ByteBuffer)localObject2).position(((ByteBuffer)localObject2).position() + k);
          paramInt += 1;
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          arrayOfString[paramInt] = null;
          continue;
        }
        if (i != 78)
          continue;
        arrayOfString[paramInt] = null;
        continue;
        arrayOfString[paramInt] = null;
      }
    }
    label536: return (String)(String)null;
  }

  public String[] getStringArray(String paramString)
  {
    return getStringArray(hash16(paramString));
  }

  public long getTime(int paramInt)
  {
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if ((paramInt > 0) && (paramInt + 4 < this.length))
      {
        paramInt = this.start + paramInt;
        if (this.bytes[paramInt] == 85)
          return ((this.bytes[(paramInt + 1)] & 0xFF) << 24 | (this.bytes[(paramInt + 2)] & 0xFF) << 16 | (this.bytes[(paramInt + 3)] & 0xFF) << 8 | this.bytes[(paramInt + 4)] & 0xFF) * 1000L;
      }
    }
    else
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      if ((seekMember(localByteBuffer, paramInt) == 0) && (localByteBuffer.remaining() > 4) && (localByteBuffer.get() == 85))
        return localByteBuffer.getInt() * 1000L;
    }
    return 0L;
  }

  public long getTime(String paramString)
  {
    return getTime(hash16(paramString));
  }

  public long[] getTimeArray(int paramInt)
  {
    int i;
    Object localObject1;
    Object localObject2;
    long[] arrayOfLong;
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      if (paramInt > 0)
      {
        i = this.start + paramInt;
        int j = this.bytes[i];
        if ((j == 65) && (paramInt + 2 < this.length))
        {
          j = (this.bytes[(i + 1)] & 0xFF) << 8 | this.bytes[(i + 2)] & 0xFF;
          if (j == 0)
          {
            localObject1 = new long[0];
            return localObject1;
          }
          i = this.start + paramInt + 3;
          localObject2 = new int[j];
          if (nativeArraySkip(this.bytes, i, this.length - paramInt - 3, j, localObject2))
          {
            arrayOfLong = new long[j];
            paramInt = 0;
            while (true)
            {
              localObject1 = arrayOfLong;
              if (paramInt >= j)
                break;
              int k = localObject2[paramInt];
              if (this.bytes[i] != 85)
                break label263;
              arrayOfLong[paramInt] = (1000L * ((this.bytes[(i + 1)] & 0xFF) << 24 | (this.bytes[(i + 2)] & 0xFF) << 16 | (this.bytes[(i + 3)] & 0xFF) << 8 | this.bytes[(i + 4)] & 0xFF));
              i += k;
              paramInt += 1;
            }
            label263: return null;
          }
        }
        else if (j == 78)
        {
          return null;
        }
      }
    }
    else
    {
      localObject2 = ByteBuffer.wrap(this.bytes, this.start, this.length);
      ((ByteBuffer)localObject2).order(ByteOrder.BIG_ENDIAN);
      if ((seekMember((ByteBuffer)localObject2, paramInt) == 0) && (((ByteBuffer)localObject2).remaining() > 0) && (((ByteBuffer)localObject2).get() == 65) && (((ByteBuffer)localObject2).remaining() > 1))
      {
        i = ((ByteBuffer)localObject2).getShort() & 0xFFFF;
        if (i == 0)
          return new long[0];
        arrayOfLong = new long[i];
        paramInt = 0;
        while (true)
        {
          localObject1 = arrayOfLong;
          if (paramInt >= i)
            break;
          if ((((ByteBuffer)localObject2).remaining() <= 4) || (((ByteBuffer)localObject2).get() != 85))
            break label409;
          arrayOfLong[paramInt] = (1000L * ((ByteBuffer)localObject2).getInt());
          paramInt += 1;
        }
        label409: return null;
      }
    }
    return (J)(J)null;
  }

  public long[] getTimeArray(String paramString)
  {
    return getTimeArray(hash16(paramString));
  }

  public byte getType(int paramInt)
  {
    int j = 0;
    int i;
    if (nativeLoaded)
    {
      paramInt = nativeSeekMember(this.bytes, this.start, this.length, paramInt);
      i = j;
      if (paramInt > 0)
      {
        i = j;
        if (paramInt < this.length)
          i = this.bytes[(this.start + paramInt)];
      }
    }
    while (i == 73)
    {
      return 73;
      ByteBuffer localByteBuffer = ByteBuffer.wrap(this.bytes, this.start, this.length);
      localByteBuffer.order(ByteOrder.BIG_ENDIAN);
      i = j;
      if (seekMember(localByteBuffer, paramInt) != 0)
        continue;
      i = j;
      if (localByteBuffer.remaining() <= 0)
        continue;
      i = localByteBuffer.get();
    }
    if (i == 83)
      return 83;
    if ((i == 84) || (i == 70))
      return 66;
    if (i == 68)
      return 68;
    if (i == 76)
      return 76;
    if (i == 85)
      return 85;
    if (i == 78)
      return 78;
    if (i == 79)
      return 79;
    if (i == 65)
      return 65;
    return 0;
  }

  public byte getType(String paramString)
  {
    return getType(hash16(paramString));
  }

  public boolean isClass(int paramInt)
  {
    int j = 0;
    int i = j;
    if (this.length > 0)
    {
      i = j;
      if (this.bytes[this.start] == 79)
      {
        i = j;
        if (((this.bytes[(this.start + 1)] & 0xFF) << 8 | this.bytes[(this.start + 2)] & 0xFF) == paramInt)
          i = 1;
      }
    }
    return i;
  }

  public boolean isClass(String paramString)
  {
    return isClass(hash16(paramString));
  }

  public Iterator<Map.Entry<Integer, Object>> iterator()
  {
    return new DPIterator();
  }

  public byte[] toByteArray()
  {
    int i = skipSelf();
    if (i > 0)
    {
      arrayOfByte = new byte[i];
      System.arraycopy(this.bytes, this.start, arrayOfByte, 0, i);
      return arrayOfByte;
    }
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = 79;
    arrayOfByte[3] = 90;
    return arrayOfByte;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramInt = skipSelf();
    if (paramInt > 0);
    while (true)
    {
      paramParcel.writeInt(0);
      paramParcel.writeInt(paramInt);
      if (this.start != 0)
        break;
      paramParcel.writeByteArray(this.bytes, 0, paramInt);
      return;
      paramInt = this.length;
    }
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(this.bytes, this.start, arrayOfByte, 0, paramInt);
    paramParcel.writeByteArray(arrayOfByte);
  }

  private class DPEntry
    implements Map.Entry<Integer, Object>
  {
    private int key;

    DPEntry(int arg2)
    {
      int i;
      this.key = i;
    }

    public Integer getKey()
    {
      return Integer.valueOf(this.key);
    }

    public Object getValue()
    {
      switch (DPObject.this.getType(this.key))
      {
      default:
        return null;
      case 66:
        return Boolean.valueOf(DPObject.this.getBoolean(this.key));
      case 73:
        return Integer.valueOf(DPObject.this.getInt(this.key));
      case 83:
        return DPObject.this.getString(this.key);
      case 76:
        return Long.valueOf(DPObject.this.getLong(this.key));
      case 68:
        return Double.valueOf(DPObject.this.getDouble(this.key));
      case 85:
        return new Date(DPObject.this.getTime(this.key));
      case 79:
        return DPObject.this.getObject(this.key);
      case 65:
      }
      return DPObject.this.getArray(this.key);
    }

    public Object setValue(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public String toString()
    {
      return "0x" + Integer.toHexString(this.key) + ": " + getValue();
    }
  }

  private class DPIterator
    implements Iterator<Map.Entry<Integer, Object>>
  {
    ByteBuffer bb = ByteBuffer.wrap(DPObject.this.bytes, DPObject.this.start, DPObject.this.length);
    DPObject.DPEntry next;

    public DPIterator()
    {
      this.bb.order(ByteOrder.BIG_ENDIAN);
      if (this.bb.remaining() > 3)
      {
        this.bb.get();
        this.bb.getShort();
        read();
      }
    }

    public boolean hasNext()
    {
      return this.next != null;
    }

    public DPObject.DPEntry next()
    {
      if (this.next == null)
        throw new NoSuchElementException();
      DPObject.DPEntry localDPEntry = this.next;
      read();
      return localDPEntry;
    }

    void read()
    {
      if (this.bb.remaining() > 0)
      {
        int i = this.bb.get();
        if (i == 77)
        {
          if (this.bb.remaining() > 1)
          {
            i = this.bb.getShort() & 0xFFFF;
            if (!DPObject.nativeLoaded)
              break label137;
            int j = DPObject.access$600(DPObject.this.bytes, this.bb.position(), DPObject.this.start + DPObject.this.length - this.bb.position());
            if (j > 0)
            {
              this.bb.position(this.bb.position() + j);
              this.next = new DPObject.DPEntry(DPObject.this, i);
            }
          }
          else
          {
            return;
          }
          this.next = null;
          return;
          label137: if (DPObject.access$700(this.bb) == 0)
          {
            this.next = new DPObject.DPEntry(DPObject.this, i);
            return;
          }
          this.next = null;
          return;
        }
        if (i == 90)
        {
          this.next = null;
          return;
        }
        this.next = null;
        return;
      }
      this.next = null;
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  private class DefaultEditor
    implements DPObject.Editor
  {
    private final ArrayList<DPObject.EditCont> couts = new ArrayList();

    private DefaultEditor()
    {
    }

    public DPObject generate()
    {
      Object localObject1 = new ByteArrayOutputStream();
      HashMap localHashMap = new HashMap(this.couts.size());
      Object localObject2 = this.couts.iterator();
      Object localObject3;
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (DPObject.EditCont)((Iterator)localObject2).next();
        localHashMap.put(Integer.valueOf(((DPObject.EditCont)localObject3).hash16), localObject3);
      }
      ((ByteArrayOutputStream)localObject1).write(79);
      int i;
      int j;
      if (DPObject.this.length > 2)
      {
        ((ByteArrayOutputStream)localObject1).write(DPObject.this.bytes[(DPObject.this.start + 1)]);
        ((ByteArrayOutputStream)localObject1).write(DPObject.this.bytes[(DPObject.this.start + 2)]);
        i = 3;
        if (i < DPObject.this.length)
        {
          j = DPObject.this.bytes[(DPObject.this.start + i)];
          i += 1;
          if ((j == 77) && (i + 2 < DPObject.this.length))
            break label297;
        }
      }
      label297: label554: label592: 
      while (true)
      {
        localObject2 = this.couts.iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject3 = (DPObject.EditCont)localHashMap.remove(Integer.valueOf(((DPObject.EditCont)((Iterator)localObject2).next()).hash16));
          if ((localObject3 == null) || (((DPObject.EditCont)localObject3).type == 82))
            continue;
          ((ByteArrayOutputStream)localObject1).write(77);
          ((ByteArrayOutputStream)localObject1).write((byte)(((DPObject.EditCont)localObject3).hash16 >>> 8));
          ((ByteArrayOutputStream)localObject1).write((byte)((DPObject.EditCont)localObject3).hash16);
          DPObject.EditCont.access$900((DPObject.EditCont)localObject3, (ByteArrayOutputStream)localObject1);
        }
        ((ByteArrayOutputStream)localObject1).write(0);
        ((ByteArrayOutputStream)localObject1).write(0);
        break;
        int k = (DPObject.this.bytes[(DPObject.this.start + i)] & 0xFF) << 8 | DPObject.this.bytes[(DPObject.this.start + i + 1)] & 0xFF;
        int m = i + 2;
        localObject2 = (DPObject.EditCont)localHashMap.remove(Integer.valueOf(k));
        if (DPObject.nativeLoaded)
        {
          i = DPObject.access$600(DPObject.this.bytes, DPObject.this.start + m, DPObject.this.length - m);
          if (localObject2 != null)
            break label554;
          if (i <= 0)
            continue;
          ((ByteArrayOutputStream)localObject1).write(77);
          ((ByteArrayOutputStream)localObject1).write((byte)(k >>> 8));
          ((ByteArrayOutputStream)localObject1).write((byte)k);
          ((ByteArrayOutputStream)localObject1).write(DPObject.this.bytes, DPObject.this.start + m, i);
        }
        while (true)
        {
          if (i <= 0)
            break label592;
          i = m + i;
          break;
          localObject3 = ByteBuffer.wrap(DPObject.this.bytes, DPObject.this.start + m, DPObject.this.length - m);
          ((ByteBuffer)localObject3).order(ByteOrder.BIG_ENDIAN);
          j = DPObject.access$700((ByteBuffer)localObject3);
          i = j;
          if (j != 0)
            break label406;
          i = ((ByteBuffer)localObject3).position() - DPObject.this.start - m;
          break label406;
          if (((DPObject.EditCont)localObject2).type == 82)
            continue;
          ((ByteArrayOutputStream)localObject1).write(77);
          ((ByteArrayOutputStream)localObject1).write((byte)(k >>> 8));
          ((ByteArrayOutputStream)localObject1).write((byte)k);
          DPObject.EditCont.access$900((DPObject.EditCont)localObject2, (ByteArrayOutputStream)localObject1);
        }
      }
      label406: ((ByteArrayOutputStream)localObject1).write(90);
      localObject1 = ((ByteArrayOutputStream)localObject1).toByteArray();
      return (DPObject)(DPObject)(DPObject)new DPObject(localObject1, 0, localObject1.length);
    }

    public DefaultEditor putArray(int paramInt, DPObject[] paramArrayOfDPObject)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 65;
      localEditCont.A = paramArrayOfDPObject;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor putArray(String paramString, DPObject[] paramArrayOfDPObject)
    {
      putArray(DPObject.hash16(paramString), paramArrayOfDPObject);
      return this;
    }

    public DefaultEditor putBoolean(int paramInt, boolean paramBoolean)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      if (paramBoolean);
      for (paramInt = 84; ; paramInt = 70)
      {
        localEditCont.type = (byte)paramInt;
        this.couts.add(localEditCont);
        return this;
      }
    }

    public DefaultEditor putBoolean(String paramString, boolean paramBoolean)
    {
      putBoolean(DPObject.hash16(paramString), paramBoolean);
      return this;
    }

    public DefaultEditor putDouble(int paramInt, double paramDouble)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 68;
      localEditCont.D = paramDouble;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor putDouble(String paramString, double paramDouble)
    {
      putDouble(DPObject.hash16(paramString), paramDouble);
      return this;
    }

    public DefaultEditor putInt(int paramInt1, int paramInt2)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt1;
      localEditCont.type = 73;
      localEditCont.I = paramInt2;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor putInt(String paramString, int paramInt)
    {
      putInt(DPObject.hash16(paramString), paramInt);
      return this;
    }

    public DPObject.Editor putIntArray(int paramInt, int[] paramArrayOfInt)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 66;
      localEditCont.B = paramArrayOfInt;
      this.couts.add(localEditCont);
      return this;
    }

    public DPObject.Editor putIntArray(String paramString, int[] paramArrayOfInt)
    {
      putIntArray(DPObject.hash16(paramString), paramArrayOfInt);
      return this;
    }

    public DefaultEditor putLong(int paramInt, long paramLong)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 76;
      localEditCont.L = paramLong;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor putLong(String paramString, long paramLong)
    {
      putLong(DPObject.hash16(paramString), paramLong);
      return this;
    }

    public DefaultEditor putObject(int paramInt, DPObject paramDPObject)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 79;
      localEditCont.O = paramDPObject;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor putObject(String paramString, DPObject paramDPObject)
    {
      putObject(DPObject.hash16(paramString), paramDPObject);
      return this;
    }

    public DefaultEditor putString(int paramInt, String paramString)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 83;
      localEditCont.S = paramString;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor putString(String paramString1, String paramString2)
    {
      putString(DPObject.hash16(paramString1), paramString2);
      return this;
    }

    public DPObject.Editor putStringArray(int paramInt, String[] paramArrayOfString)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 67;
      localEditCont.C = paramArrayOfString;
      this.couts.add(localEditCont);
      return this;
    }

    public DPObject.Editor putStringArray(String paramString, String[] paramArrayOfString)
    {
      putStringArray(DPObject.hash16(paramString), paramArrayOfString);
      return this;
    }

    public DefaultEditor putTime(int paramInt, long paramLong)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 85;
      localEditCont.U = paramLong;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor putTime(String paramString, long paramLong)
    {
      putTime(DPObject.hash16(paramString), paramLong);
      return this;
    }

    public DefaultEditor remove(int paramInt)
    {
      DPObject.EditCont localEditCont = new DPObject.EditCont(null);
      localEditCont.hash16 = paramInt;
      localEditCont.type = 82;
      this.couts.add(localEditCont);
      return this;
    }

    public DefaultEditor remove(String paramString)
    {
      remove(DPObject.hash16(paramString));
      return this;
    }
  }

  private static class EditCont
  {
    public DPObject[] A;
    public int[] B;
    public String[] C;
    public double D;
    public int I;
    public long L;
    public DPObject O;
    public String S;
    public long U;
    public int hash16;
    public byte type;

    private void writeObject(ByteArrayOutputStream paramByteArrayOutputStream, DPObject paramDPObject)
    {
      if (paramDPObject == null)
      {
        paramByteArrayOutputStream.write(78);
        return;
      }
      int i = paramDPObject.skipSelf();
      if (i > 0)
      {
        paramByteArrayOutputStream.write(paramDPObject.bytes, paramDPObject.start, i);
        return;
      }
      paramByteArrayOutputStream.write(79);
      paramByteArrayOutputStream.write(0);
      paramByteArrayOutputStream.write(0);
      paramByteArrayOutputStream.write(90);
    }

    private void writeString(ByteArrayOutputStream paramByteArrayOutputStream, String paramString)
    {
      if (paramString == null)
      {
        paramByteArrayOutputStream.write(78);
        return;
      }
      paramByteArrayOutputStream.write(83);
      try
      {
        paramString = paramString.getBytes("UTF-8");
        int j = paramString.length;
        int i = j;
        if (j > 65535)
          i = 65535;
        paramByteArrayOutputStream.write((byte)(i >>> 8));
        paramByteArrayOutputStream.write((byte)i);
        paramByteArrayOutputStream.write(paramString, 0, i);
        return;
      }
      catch (java.lang.Exception paramString)
      {
        while (true)
          paramString = new byte[0];
      }
    }

    private void writeTo(ByteArrayOutputStream paramByteArrayOutputStream)
    {
      switch (this.type)
      {
      case 69:
      case 71:
      case 72:
      case 74:
      case 75:
      case 77:
      case 78:
      case 80:
      case 81:
      case 82:
      default:
      case 73:
      case 76:
      case 68:
      case 84:
      case 70:
      case 85:
      case 83:
      case 79:
      case 65:
      case 66:
      case 67:
      }
      while (true)
      {
        return;
        paramByteArrayOutputStream.write(73);
        paramByteArrayOutputStream.write((byte)(this.I >>> 24));
        paramByteArrayOutputStream.write((byte)(this.I >>> 16));
        paramByteArrayOutputStream.write((byte)(this.I >>> 8));
        paramByteArrayOutputStream.write((byte)this.I);
        return;
        paramByteArrayOutputStream.write(76);
        paramByteArrayOutputStream.write((byte)(int)(this.L >>> 56));
        paramByteArrayOutputStream.write((byte)(int)(this.L >>> 48));
        paramByteArrayOutputStream.write((byte)(int)(this.L >>> 40));
        paramByteArrayOutputStream.write((byte)(int)(this.L >>> 32));
        paramByteArrayOutputStream.write((byte)(int)(this.L >>> 24));
        paramByteArrayOutputStream.write((byte)(int)(this.L >>> 16));
        paramByteArrayOutputStream.write((byte)(int)(this.L >>> 8));
        paramByteArrayOutputStream.write((byte)(int)this.L);
        return;
        paramByteArrayOutputStream.write(68);
        long l = Double.doubleToRawLongBits(this.D);
        paramByteArrayOutputStream.write((byte)(int)(l >>> 56));
        paramByteArrayOutputStream.write((byte)(int)(l >>> 48));
        paramByteArrayOutputStream.write((byte)(int)(l >>> 40));
        paramByteArrayOutputStream.write((byte)(int)(l >>> 32));
        paramByteArrayOutputStream.write((byte)(int)(l >>> 24));
        paramByteArrayOutputStream.write((byte)(int)(l >>> 16));
        paramByteArrayOutputStream.write((byte)(int)(l >>> 8));
        paramByteArrayOutputStream.write((byte)(int)l);
        return;
        paramByteArrayOutputStream.write(84);
        return;
        paramByteArrayOutputStream.write(70);
        return;
        paramByteArrayOutputStream.write(85);
        int i = (int)(this.U / 1000L);
        paramByteArrayOutputStream.write((byte)(i >>> 24));
        paramByteArrayOutputStream.write((byte)(i >>> 16));
        paramByteArrayOutputStream.write((byte)(i >>> 8));
        paramByteArrayOutputStream.write((byte)i);
        return;
        writeString(paramByteArrayOutputStream, this.S);
        return;
        writeObject(paramByteArrayOutputStream, this.O);
        return;
        if (this.A == null)
        {
          paramByteArrayOutputStream.write(78);
          return;
        }
        int j = this.A.length;
        i = j;
        if (j > 65535)
          i = 65535;
        paramByteArrayOutputStream.write(65);
        paramByteArrayOutputStream.write((byte)(i >>> 8));
        paramByteArrayOutputStream.write((byte)i);
        j = 0;
        while (j < i)
        {
          writeObject(paramByteArrayOutputStream, this.A[j]);
          j += 1;
        }
        continue;
        if (this.B == null)
        {
          paramByteArrayOutputStream.write(78);
          return;
        }
        j = this.B.length;
        i = j;
        if (j > 65535)
          i = 65535;
        paramByteArrayOutputStream.write(65);
        paramByteArrayOutputStream.write((byte)(i >>> 8));
        paramByteArrayOutputStream.write((byte)i);
        j = 0;
        while (j < i)
        {
          int k = this.B[j];
          paramByteArrayOutputStream.write(73);
          paramByteArrayOutputStream.write((byte)(k >>> 24));
          paramByteArrayOutputStream.write((byte)(k >>> 16));
          paramByteArrayOutputStream.write((byte)(k >>> 8));
          paramByteArrayOutputStream.write((byte)k);
          j += 1;
        }
        continue;
        if (this.C == null)
        {
          paramByteArrayOutputStream.write(78);
          return;
        }
        j = this.C.length;
        i = j;
        if (j > 65535)
          i = 65535;
        paramByteArrayOutputStream.write(65);
        paramByteArrayOutputStream.write((byte)(i >>> 8));
        paramByteArrayOutputStream.write((byte)i);
        j = 0;
        while (j < i)
        {
          writeString(paramByteArrayOutputStream, this.C[j]);
          j += 1;
        }
      }
    }
  }

  public static abstract interface Editor
  {
    public abstract DPObject generate();

    public abstract Editor putArray(int paramInt, DPObject[] paramArrayOfDPObject);

    public abstract Editor putArray(String paramString, DPObject[] paramArrayOfDPObject);

    public abstract Editor putBoolean(int paramInt, boolean paramBoolean);

    public abstract Editor putBoolean(String paramString, boolean paramBoolean);

    public abstract Editor putDouble(int paramInt, double paramDouble);

    public abstract Editor putDouble(String paramString, double paramDouble);

    public abstract Editor putInt(int paramInt1, int paramInt2);

    public abstract Editor putInt(String paramString, int paramInt);

    public abstract Editor putIntArray(int paramInt, int[] paramArrayOfInt);

    public abstract Editor putIntArray(String paramString, int[] paramArrayOfInt);

    public abstract Editor putLong(int paramInt, long paramLong);

    public abstract Editor putLong(String paramString, long paramLong);

    public abstract Editor putObject(int paramInt, DPObject paramDPObject);

    public abstract Editor putObject(String paramString, DPObject paramDPObject);

    public abstract Editor putString(int paramInt, String paramString);

    public abstract Editor putString(String paramString1, String paramString2);

    public abstract Editor putStringArray(int paramInt, String[] paramArrayOfString);

    public abstract Editor putStringArray(String paramString, String[] paramArrayOfString);

    public abstract Editor putTime(int paramInt, long paramLong);

    public abstract Editor putTime(String paramString, long paramLong);

    public abstract Editor remove(int paramInt);

    public abstract Editor remove(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.archive.DPObject
 * JD-Core Version:    0.6.0
 */