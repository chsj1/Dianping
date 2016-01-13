package com.dianping.shopinfo.common;

import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import java.util.HashMap;
import java.util.Map;

public class DefaultShopinfoAgentListConfig
  implements AgentListConfig
{
  public Map<String, AgentInfo> getAgentInfoList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("shopinfo/common_toolbar", new AgentInfo(ShopInfoToolbarAgent.class, ""));
    localHashMap.put("shopinfo/common_announce", new AgentInfo(AnnounceAgent.class, "00000.00000"));
    localHashMap.put("shopinfo/common_shopstatus", new AgentInfo(ShopStatusAgent.class, "00010.00000"));
    localHashMap.put("shopinfo/common_head", new AgentInfo(HeadAgent.class, "00010.00010"));
    localHashMap.put("shopinfo/common_scorehui", new AgentInfo(ScoreHuiAgent.class, "00010.00020"));
    localHashMap.put("shopinfo/common_address", new AgentInfo(AddressAgent.class, "00010.00030"));
    localHashMap.put("shopinfo/common_phone", new AgentInfo(PhoneAgent.class, "00010.00040"));
    localHashMap.put("shopinfo/common_obqt", new AgentInfo(OrderBookQueueTAEntryAgent.class, "00010.00050"));
    localHashMap.put("shopinfo/common_moviebooking", new AgentInfo(MovieAgent.class, "00020.00000"));
    localHashMap.put("shopinfo/common_hui", new AgentInfo(HuiAgent.class, "00030.00000"));
    localHashMap.put("shopinfo/common_coupon", new AgentInfo(CouponAgent.class, "00030.00010"));
    localHashMap.put("shopinfo/common_tuan", new AgentInfo(ShopTuanAgent.class, "00030.00020"));
    localHashMap.put("shopinfo/common_membercard", new AgentInfo(MemberCardAgent.class, "00040.00000"));
    localHashMap.put("shopinfo/common_promo", new AgentInfo(PromoAgent.class, "00040.00010"));
    localHashMap.put("shopinfo/common_onsale", new AgentInfo(OnsaleAgent.class, "00040.00020"));
    localHashMap.put("shopinfo/common_bank", new AgentInfo(BankAgent.class, "00040.00030"));
    localHashMap.put("shopinfo/common_activity", new AgentInfo(ActivityAgent.class, "00040.00040"));
    localHashMap.put("shopinfo/common_wedbanquet", new AgentInfo(WedBanquetAgent.class, "00050.00000"));
    localHashMap.put("shopinfo/common_recommend", new AgentInfo(RecommendDishAgent.class, "00060.00000"));
    localHashMap.put("shopinfo/common_friendhere", new AgentInfo(FriendHereAgent.class, "00070.00000"));
    localHashMap.put("shopinfo/common_friendreview", new AgentInfo(FriendReviewAgent.class, "00080.00000"));
    localHashMap.put("shopinfo/common_wechatguide", new AgentInfo(FriendRelatedAgent.class, "00090.00000"));
    localHashMap.put("shopinfo/common_review", new AgentInfo(ReviewAgent.class, "00100.00000"));
    localHashMap.put("shopinfo/common_checkin", new AgentInfo(CheckinAgent.class, "00110.00000"));
    localHashMap.put("shopinfo/common_emptytech", new AgentInfo(EmptyTechnicianAgent.class, "00120.00000"));
    localHashMap.put("shopinfo/common_technician", new AgentInfo(CommonTechnicianAgent.class, "00130.00000"));
    localHashMap.put("shopinfo/common_shopinfo", new AgentInfo(CharacteristicAgent.class, "00140.00000"));
    localHashMap.put("shopinfo/common_rank", new AgentInfo(DefaultRankAgent.class, "00140.00010"));
    localHashMap.put("shopinfo/common_branch", new AgentInfo(ShopBranchAgent.class, "00140.00020"));
    localHashMap.put("shopinfo/common_mallinfo", new AgentInfo(MallInfoAgent.class, "00150.00000"));
    localHashMap.put("shopinfo/common_nearby", new AgentInfo(NearByShopAgent.class, "00160.00000"));
    localHashMap.put("shopinfo/common_brandstory", new AgentInfo(CommonBrandStoryAgent.class, "00170.00000"));
    localHashMap.put("shopinfo/common_myshop", new AgentInfo(ShoperEntranceAgent.class, "00180.00000"));
    localHashMap.put("shopinfo/common_favorshop", new AgentInfo(FavorShopAgent.class, "00190.00000"));
    return localHashMap;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return null;
  }

  public boolean shouldShow()
  {
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.DefaultShopinfoAgentListConfig
 * JD-Core Version:    0.6.0
 */