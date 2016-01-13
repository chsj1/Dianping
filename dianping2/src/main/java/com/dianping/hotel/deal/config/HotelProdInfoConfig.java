package com.dianping.hotel.deal.config;

import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.hotel.deal.agent.HotelProdBestShopAgent;
import com.dianping.hotel.deal.agent.HotelProdBookingAgent;
import com.dianping.hotel.deal.agent.HotelProdBookingNoticeAgent;
import com.dianping.hotel.deal.agent.HotelProdBuyerAgent;
import com.dianping.hotel.deal.agent.HotelProdEventAgent;
import com.dianping.hotel.deal.agent.HotelProdFlipperAgent;
import com.dianping.hotel.deal.agent.HotelProdFriendlyReminderAgent;
import com.dianping.hotel.deal.agent.HotelProdModificationNoticeAgent;
import com.dianping.hotel.deal.agent.HotelProdMoreDealsAgent;
import com.dianping.hotel.deal.agent.HotelProdOtherDealsAgent;
import com.dianping.hotel.deal.agent.HotelProdPackageDetailsAgent;
import com.dianping.hotel.deal.agent.HotelProdPurchasingNoticeAgent;
import com.dianping.hotel.deal.agent.HotelProdRecommendAgent;
import com.dianping.hotel.deal.agent.HotelProdRefundAgent;
import com.dianping.hotel.deal.agent.HotelProdRefundNoticeAgent;
import com.dianping.hotel.deal.agent.HotelProdReviewsAgent;
import com.dianping.hotel.deal.agent.HotelProdRuleReminderAgent;
import com.dianping.hotel.deal.agent.HotelProdShareAgent;
import com.dianping.hotel.deal.agent.HotelProdUsageAgent;
import com.dianping.hotel.deal.agent.HotelProdUserReviewsAgent;
import com.dianping.hotel.deal.agent.HotelProdValidityAgent;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;
import java.util.HashMap;
import java.util.Map;

public class HotelProdInfoConfig extends BaseHotelProdInfoConfig
{
  private Map<String, Class<? extends CellAgent>> agentMap = new HashMap();

  public HotelProdInfoConfig(HotelProdInfoAgentFragment paramHotelProdInfoAgentFragment)
  {
    super(paramHotelProdInfoAgentFragment);
    this.agentMap.put("1", HotelProdFlipperAgent.class);
    this.agentMap.put("2", HotelProdBuyerAgent.class);
    this.agentMap.put("3", HotelProdRefundAgent.class);
    this.agentMap.put("4", HotelProdEventAgent.class);
    this.agentMap.put("5", HotelProdRecommendAgent.class);
    this.agentMap.put("6", HotelProdReviewsAgent.class);
    this.agentMap.put("7", HotelProdBookingAgent.class);
    this.agentMap.put("8", HotelProdBestShopAgent.class);
    this.agentMap.put("9", HotelProdBestShopAgent.class);
    this.agentMap.put("10", HotelProdUserReviewsAgent.class);
    this.agentMap.put("11", HotelProdMoreDealsAgent.class);
    this.agentMap.put("12", HotelProdOtherDealsAgent.class);
    this.agentMap.put("21", HotelProdBookingNoticeAgent.class);
    this.agentMap.put("22", HotelProdModificationNoticeAgent.class);
    this.agentMap.put("23", HotelProdValidityAgent.class);
    this.agentMap.put("24", HotelProdPackageDetailsAgent.class);
    this.agentMap.put("25", HotelProdPurchasingNoticeAgent.class);
    this.agentMap.put("26", HotelProdRuleReminderAgent.class);
    this.agentMap.put("27", HotelProdUsageAgent.class);
    this.agentMap.put("28", HotelProdRefundNoticeAgent.class);
    this.agentMap.put("29", HotelProdFriendlyReminderAgent.class);
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("hotelprod/share", new AgentInfo(HotelProdShareAgent.class, ""));
    setModuleSort(localHashMap);
    return localHashMap;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return null;
  }

  public void setModuleSort(Map<String, AgentInfo> paramMap)
  {
    int k = 100;
    int i = 300;
    if (this.mFragment.moduleSort == null);
    while (true)
    {
      return;
      String[] arrayOfString1 = this.mFragment.moduleSort;
      int i1 = arrayOfString1.length;
      int j = 0;
      while (j < i1)
      {
        String[] arrayOfString2 = arrayOfString1[j].split(",");
        int i2 = arrayOfString2.length;
        int m = 0;
        while (m < i2)
        {
          Object localObject = arrayOfString2[m];
          localObject = (Class)this.agentMap.get(localObject);
          int n = k;
          if (localObject != null)
          {
            String str = ((Class)localObject).getSimpleName().replace("Agent", "");
            paramMap.put("hotelprod/" + str.toLowerCase(), new AgentInfo((Class)localObject, String.format("%5d.%5d", new Object[] { Integer.valueOf(i), Integer.valueOf(k) })));
            n = k + 50;
          }
          m += 1;
          k = n;
        }
        i += 50;
        j += 1;
      }
    }
  }

  public boolean shouldShow()
  {
    return (this.mFragment != null) && ((this.mFragment.bizType == 3) || (this.mFragment.bizType == 1));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.config.HotelProdInfoConfig
 * JD-Core Version:    0.6.0
 */