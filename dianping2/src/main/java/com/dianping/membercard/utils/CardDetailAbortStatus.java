package com.dianping.membercard.utils;

import com.dianping.util.Log;

public class CardDetailAbortStatus
{
  private boolean hasAborted = false;
  private int openingMemberCardId = -1;

  public CardDetailAbortStatus()
  {
    clearAbortStatus();
  }

  public void clearAbortStatus()
  {
    this.hasAborted = false;
    this.openingMemberCardId = -1;
  }

  public void initAbortStatus(int paramInt)
  {
    this.openingMemberCardId = paramInt;
    this.hasAborted = false;
  }

  public boolean isCardDetailAborted(int paramInt)
  {
    Log.v("CardRequest", "openingMemberCardId:" + this.openingMemberCardId + ", hasAborted:" + this.hasAborted);
    return (this.hasAborted) || ((this.openingMemberCardId > 0) && (this.openingMemberCardId != paramInt));
  }

  public boolean isOpenningListEnable()
  {
    return this.openingMemberCardId <= 0;
  }

  public void openAbortStatus()
  {
    this.hasAborted = true;
    this.openingMemberCardId = -1;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.CardDetailAbortStatus
 * JD-Core Version:    0.6.0
 */