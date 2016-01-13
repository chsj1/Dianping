package com.dianping.shopinfo.pet;

import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.shopinfo.base.BaseFacilityAgent;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import com.dianping.shopinfo.common.AddressAgent;
import com.dianping.shopinfo.common.BankAgent;
import com.dianping.shopinfo.common.CharacteristicAgent;
import com.dianping.shopinfo.common.CheckinAgent;
import com.dianping.shopinfo.common.CommonBrandStoryAgent;
import com.dianping.shopinfo.common.CouponAgent;
import com.dianping.shopinfo.common.DefaultRankAgent;
import com.dianping.shopinfo.common.FavorShopAgent;
import com.dianping.shopinfo.common.FavoriteAgent;
import com.dianping.shopinfo.common.FriendHereAgent;
import com.dianping.shopinfo.common.FriendRelatedAgent;
import com.dianping.shopinfo.common.FriendReviewAgent;
import com.dianping.shopinfo.common.HistoryAgent;
import com.dianping.shopinfo.common.HuiPayAgent;
import com.dianping.shopinfo.common.MallInfoAgent;
import com.dianping.shopinfo.common.MemberCardAgent;
import com.dianping.shopinfo.common.MoreAgent;
import com.dianping.shopinfo.common.NearByShopAgent;
import com.dianping.shopinfo.common.OnsaleAgent;
import com.dianping.shopinfo.common.OrderBookQueueTAEntryAgent;
import com.dianping.shopinfo.common.PhoneAgent;
import com.dianping.shopinfo.common.PromoAgent;
import com.dianping.shopinfo.common.ReviewAgent;
import com.dianping.shopinfo.common.ShareAgent;
import com.dianping.shopinfo.common.ShopBranchAgent;
import com.dianping.shopinfo.common.ShopInfoTuanAgent;
import com.dianping.shopinfo.common.ShopStatusAgent;
import com.dianping.shopinfo.common.ShoperEntranceAgent;
import com.dianping.shopinfo.common.TopAgent;
import java.util.HashMap;
import java.util.Map;

public class PetShopinfoAgentListConfig
  implements AgentListConfig
{
  private DPObject shop;

  public PetShopinfoAgentListConfig(DPObject paramDPObject)
  {
    this.shop = paramDPObject;
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("shopinfo/common_toolbar", new AgentInfo(ShopInfoToolbarAgent.class, ""));
    localHashMap.put("shopinfo/common_shopstatus", new AgentInfo(ShopStatusAgent.class, "0200Basic.00ShopStatus"));
    localHashMap.put("shopinfo/common_oldhead", new AgentInfo(TopAgent.class, "0200Basic.05Info"));
    localHashMap.put("shopinfo/common_address", new AgentInfo(AddressAgent.class, "0200Basic.10Address"));
    localHashMap.put("shopinfo/common_phone", new AgentInfo(PhoneAgent.class, "0200Basic.30Phone"));
    localHashMap.put("shopinfo/common_pet_service", new AgentInfo(BaseFacilityAgent.class, "0300Service.10PetService"));
    localHashMap.put("shopinfo/common_oldhui", new AgentInfo(HuiPayAgent.class, "0475HuiPay.10Hui"));
    localHashMap.put("shopinfo/common_oldtuan", new AgentInfo(ShopInfoTuanAgent.class, "0480Tuan.50Tuan"));
    localHashMap.put("shopinfo/common_promo", new AgentInfo(PromoAgent.class, "0500Cash.50Promo."));
    localHashMap.put("shopinfo/common_onsale", new AgentInfo(OnsaleAgent.class, "0500Cash.60Onsale"));
    localHashMap.put("shopinfo/common_bank", new AgentInfo(BankAgent.class, "0500Cash.70Bank"));
    localHashMap.put("shopinfo/common_membercard", new AgentInfo(MemberCardAgent.class, "0500Cash.70MemberCard"));
    localHashMap.put("shopinfo/common_friendhere", new AgentInfo(FriendHereAgent.class, "2800friend.review"));
    localHashMap.put("shopinfo/common_friendreview", new AgentInfo(FriendReviewAgent.class, "2900Reivew."));
    localHashMap.put("shopinfo/common_friendrelate", new AgentInfo(FriendRelatedAgent.class, "2950Reivew.relate"));
    localHashMap.put("shopinfo/common_review", new AgentInfo(ReviewAgent.class, "3000Reivew."));
    localHashMap.put("shopinfo/common_checkin", new AgentInfo(CheckinAgent.class, ""));
    localHashMap.put("shopinfo/common_shopinfo", new AgentInfo(CharacteristicAgent.class, "7000ShopInfo.10Basic"));
    localHashMap.put("shopinfo/common_rank", new AgentInfo(DefaultRankAgent.class, "7000ShopInfo.10defaultrank"));
    localHashMap.put("shopinfo/common_branch", new AgentInfo(ShopBranchAgent.class, "7000ShopInfo.20RelatedShops"));
    localHashMap.put("shopinfo/common_mallinfo", new AgentInfo(MallInfoAgent.class, "7700shopinfo."));
    localHashMap.put("shopinfo/common_nearby", new AgentInfo(NearByShopAgent.class, "8500ShopInfo.20nearbyshop"));
    localHashMap.put("shopinfo/common_brandstory", new AgentInfo(CommonBrandStoryAgent.class, "8600BrandStory."));
    localHashMap.put("shopinfo/common_myshop", new AgentInfo(ShoperEntranceAgent.class, "8700ShopClaimEntrance."));
    localHashMap.put("shopinfo/common_favorshop", new AgentInfo(FavorShopAgent.class, "8900Favor.10shop"));
    localHashMap.put("shopinfo/default_report", new AgentInfo(MoreAgent.class, ""));
    localHashMap.put("shopinfo/default_share", new AgentInfo(ShareAgent.class, ""));
    localHashMap.put("shopinfo/default_favorite", new AgentInfo(FavoriteAgent.class, ""));
    localHashMap.put("shopinfo/default_history", new AgentInfo(HistoryAgent.class, ""));
    localHashMap.put("shopinfo/common_obqt", new AgentInfo(OrderBookQueueTAEntryAgent.class, "0200Basic.40Takeaway"));
    localHashMap.put("shopinfo/common_coupon", new AgentInfo(CouponAgent.class, "0475HuiPay.20Hui"));
    return localHashMap;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return null;
  }

  public boolean shouldShow()
  {
    if ((this.shop == null) || (this.shop.getObject("ClientShopStyle") == null))
      return false;
    return "pet_common".equals(this.shop.getObject("ClientShopStyle").getString("ShopView"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.pet.PetShopinfoAgentListConfig
 * JD-Core Version:    0.6.0
 */