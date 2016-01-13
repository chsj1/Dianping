package com.dianping.shopinfo.wed.baby;

import android.os.Bundle;
import com.dianping.shopinfo.common.CheckinAgent;

public class BabyCheckinAgent extends CheckinAgent
{
  public BabyCheckinAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    removeAllCells();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.BabyCheckinAgent
 * JD-Core Version:    0.6.0
 */