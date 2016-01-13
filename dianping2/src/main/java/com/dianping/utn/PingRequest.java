package com.dianping.utn;

import java.net.DatagramPacket;

public class PingRequest extends BaseRequest
{
  public PingRequest()
  {
    this.type = 1;
  }

  public DatagramPacket pack()
    throws Exception
  {
    byte[] arrayOfByte = packRaw(null);
    return new DatagramPacket(arrayOfByte, arrayOfByte.length);
  }

  public void parse(DatagramPacket paramDatagramPacket)
    throws Exception
  {
    super.parseRaw(paramDatagramPacket.getData(), paramDatagramPacket.getOffset(), paramDatagramPacket.getLength());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.PingRequest
 * JD-Core Version:    0.6.0
 */