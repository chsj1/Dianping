package com.qq.taf.jce.dynamic;

import com.qq.taf.jce.JceDecodeException;
import com.qq.taf.jce.JceInputStream;
import com.qq.taf.jce.JceInputStream.HeadData;
import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class DynamicInputStream
{
  private ByteBuffer bs;
  private String sServerEncoding = "GBK";

  public DynamicInputStream(ByteBuffer paramByteBuffer)
  {
    this.bs = paramByteBuffer;
  }

  public DynamicInputStream(byte[] paramArrayOfByte)
  {
    this.bs = ByteBuffer.wrap(paramArrayOfByte);
  }

  private JceField readString(JceInputStream.HeadData paramHeadData, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    this.bs.get(arrayOfByte);
    try
    {
      String str1 = new String(arrayOfByte, this.sServerEncoding);
      return JceField.create(str1, paramHeadData.tag);
    }
    catch (UnsupportedEncodingException str2)
    {
      while (true)
        String str2 = new String(arrayOfByte);
    }
  }

  public JceField read()
  {
    try
    {
      Object localObject1 = new JceInputStream.HeadData();
      JceInputStream.readHead((JceInputStream.HeadData)localObject1, this.bs);
      int j;
      int i;
      Object localObject2;
      Object localObject3;
      switch (((JceInputStream.HeadData)localObject1).type)
      {
      case 0:
        return JceField.create(this.bs.get(), ((JceInputStream.HeadData)localObject1).tag);
      case 1:
        return JceField.create(this.bs.getShort(), ((JceInputStream.HeadData)localObject1).tag);
      case 2:
        return JceField.create(this.bs.getInt(), ((JceInputStream.HeadData)localObject1).tag);
      case 3:
        return JceField.create(this.bs.getLong(), ((JceInputStream.HeadData)localObject1).tag);
      case 4:
        return JceField.create(this.bs.getFloat(), ((JceInputStream.HeadData)localObject1).tag);
      case 5:
        return JceField.create(this.bs.getDouble(), ((JceInputStream.HeadData)localObject1).tag);
      case 6:
        j = this.bs.get();
        i = j;
        if (j < 0)
          i = j + 256;
        return readString((JceInputStream.HeadData)localObject1, i);
      case 7:
        return readString((JceInputStream.HeadData)localObject1, this.bs.getInt());
      case 9:
        j = ((NumberField)read()).intValue();
        localObject2 = new JceField[j];
        i = 0;
        while (i < j)
        {
          localObject2[i] = read();
          i += 1;
        }
        return JceField.createList(localObject2, ((JceInputStream.HeadData)localObject1).tag);
      case 8:
        j = ((NumberField)read()).intValue();
        localObject2 = new JceField[j];
        localObject3 = new JceField[j];
        i = 0;
        while (i < j)
        {
          localObject2[i] = read();
          localObject3[i] = read();
          i += 1;
        }
        return JceField.createMap(localObject2, localObject3, ((JceInputStream.HeadData)localObject1).tag);
      case 10:
        localObject2 = new ArrayList();
        while (true)
        {
          localObject3 = read();
          if (localObject3 == null)
            return JceField.createStruct((JceField[])((List)localObject2).toArray(new JceField[0]), ((JceInputStream.HeadData)localObject1).tag);
          ((List)localObject2).add(localObject3);
        }
      case 12:
        return JceField.createZero(((JceInputStream.HeadData)localObject1).tag);
      case 13:
        i = ((JceInputStream.HeadData)localObject1).tag;
        JceInputStream.readHead((JceInputStream.HeadData)localObject1, this.bs);
        if (((JceInputStream.HeadData)localObject1).type != 0)
          throw new JceDecodeException("type mismatch, simple_list only support byte, tag: " + i + ", type: " + ((JceInputStream.HeadData)localObject1).type);
        localObject1 = new byte[((NumberField)read()).intValue()];
        this.bs.get(localObject1);
        localObject1 = JceField.create(localObject1, i);
        return localObject1;
      default:
        return null;
      case 11:
      }
    }
    catch (BufferUnderflowException localBufferUnderflowException)
    {
      return null;
    }
    return (JceField)(JceField)(JceField)null;
  }

  public int setServerEncoding(String paramString)
  {
    this.sServerEncoding = paramString;
    return 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.qq.taf.jce.dynamic.DynamicInputStream
 * JD-Core Version:    0.6.0
 */