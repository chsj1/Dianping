package com.dianping.search.util;

import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.WeddingShopListAgentConfig;

public class WedShopListUtils
{
  private static ShopListAgentFragment sLastFragment;
  private static WeddingShopListAgentConfig sWeddingShopListAgentConfig;

  public static WeddingShopListAgentConfig getWedConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    if ((sWeddingShopListAgentConfig == null) || (sLastFragment != paramShopListAgentFragment))
    {
      sLastFragment = paramShopListAgentFragment;
      sWeddingShopListAgentConfig = (WeddingShopListAgentConfig)paramShopListAgentFragment.getCurrentAgentListConfig();
    }
    return sWeddingShopListAgentConfig;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.util.WedShopListUtils
 * JD-Core Version:    0.6.0
 */