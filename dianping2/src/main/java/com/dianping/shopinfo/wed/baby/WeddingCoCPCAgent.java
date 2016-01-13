package com.dianping.shopinfo.wed.baby;

import android.os.Bundle;

public class WeddingCoCPCAgent extends WeddingBaseCPCAgent
{
  private static final String CELL_WEDDING_SUGGESTION = "9999Basic.05WeddingShopSuggestion";

  public WeddingCoCPCAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.shopList != null)
    {
      if (this.shopList.length != 0)
        break label25;
      removeAllCells();
    }
    label25: 
    do
    {
      return;
      removeAllCells();
      paramBundle = createSuggestionView();
    }
    while (this.cooperateType != 1);
    addCell("9999Basic.05WeddingShopSuggestion", paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingCoCPCAgent
 * JD-Core Version:    0.6.0
 */