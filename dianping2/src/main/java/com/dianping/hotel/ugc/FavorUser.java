package com.dianping.hotel.ugc;

import com.dianping.archive.DPObject;

public class FavorUser
{
  public String username;

  public FavorUser()
  {
  }

  public FavorUser(DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getObject("User");
    if (paramDPObject != null)
      this.username = paramDPObject.getString("Nick");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.FavorUser
 * JD-Core Version:    0.6.0
 */