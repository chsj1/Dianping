package com.dianping.shopinfo.community;

import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import com.dianping.shopinfo.common.AddressAgent;
import com.dianping.shopinfo.common.FavoriteAgent;
import com.dianping.shopinfo.common.MoreAgent;
import com.dianping.shopinfo.common.PhoneAgent;
import com.dianping.shopinfo.common.ReviewAgent;
import com.dianping.shopinfo.district.DistrictEnjoyAgent;
import java.util.HashMap;
import java.util.Map;

public class CommunityShopinfoAgentListConfig
  implements AgentListConfig
{
  private DPObject shop;

  public CommunityShopinfoAgentListConfig(DPObject paramDPObject)
  {
    this.shop = paramDPObject;
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("shopinfo/common_toolbar", new AgentInfo(ShopInfoToolbarAgent.class, ""));
    localHashMap.put("shopinfo/communityhead", new AgentInfo(CommunityHeadAgent.class, "0200Basic.01Head"));
    localHashMap.put("shopinfo/common_address", new AgentInfo(AddressAgent.class, "0200Basic.10Address"));
    localHashMap.put("shopinfo/common_phone", new AgentInfo(PhoneAgent.class, "0200Basic.30Phone"));
    localHashMap.put("shopinfo/communityinfo", new AgentInfo(CommunityInfoAgent.class, "0200Basic.40info"));
    localHashMap.put("shopinfo/sevice", new AgentInfo(SurroundingSeviceAgent.class, "0200Basic.50Sevice"));
    localHashMap.put("shopinfo/facility", new AgentInfo(SurroundingFacilitiesAgent.class, "0300facility.01surround"));
    localHashMap.put("shopinfo/common_review", new AgentInfo(ReviewAgent.class, "0400Reivew."));
    localHashMap.put("shopinfo/districtshop", new AgentInfo(DistrictEnjoyAgent.class, "0500District.01shop"));
    localHashMap.put("shopinfo/default_favorite", new AgentInfo(FavoriteAgent.class, "5Fav"));
    localHashMap.put("shopinfo/report", new AgentInfo(MoreAgent.class, "6More"));
    return localHashMap;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return null;
  }

  public boolean shouldShow()
  {
    return (this.shop != null) && (this.shop.getObject("ClientShopStyle") != null) && ("community_common".equalsIgnoreCase(this.shop.getObject("ClientShopStyle").getString("ShopView")));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.community.CommunityShopinfoAgentListConfig
 * JD-Core Version:    0.6.0
 */