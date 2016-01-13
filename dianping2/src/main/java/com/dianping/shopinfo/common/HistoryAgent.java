package com.dianping.shopinfo.common;

import android.os.AsyncTask;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.util.HistoryHelper;
import com.dianping.shopinfo.base.ShopCellAgent;

public class HistoryAgent extends ShopCellAgent
{
  public HistoryAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    if (paramBundle == null)
      return;
    new RecordHistoryTask(null).execute(new DPObject[] { paramBundle });
  }

  private class RecordHistoryTask extends AsyncTask<DPObject, Void, Boolean>
  {
    private RecordHistoryTask()
    {
    }

    public Boolean doInBackground(DPObject[] arg1)
    {
      DPObject localDPObject = ???[0];
      synchronized (HistoryHelper.getInstance())
      {
        int i = HistoryAgent.this.shopId();
        if (???.containId(i))
          ???.removeId(i);
        ???.addId(i);
        ???.flushIds();
        ???.addShop(i, localDPObject);
        return Boolean.valueOf(true);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.HistoryAgent
 * JD-Core Version:    0.6.0
 */