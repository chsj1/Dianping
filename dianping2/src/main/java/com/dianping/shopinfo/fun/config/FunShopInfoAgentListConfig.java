package com.dianping.shopinfo.fun.config;

import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import com.dianping.shopinfo.common.ActivityAgent;
import com.dianping.shopinfo.common.AddressAgent;
import com.dianping.shopinfo.common.AnnounceAgent;
import com.dianping.shopinfo.common.BankAgent;
import com.dianping.shopinfo.common.CharacteristicAgent;
import com.dianping.shopinfo.common.CheckinAgent;
import com.dianping.shopinfo.common.CommonBrandStoryAgent;
import com.dianping.shopinfo.common.CommonImageGalleryAgent;
import com.dianping.shopinfo.common.CommonTechnicianAgent;
import com.dianping.shopinfo.common.FavorShopAgent;
import com.dianping.shopinfo.common.FavoriteAgent;
import com.dianping.shopinfo.common.HistoryAgent;
import com.dianping.shopinfo.common.HuiPayAgent;
import com.dianping.shopinfo.common.MallInfoAgent;
import com.dianping.shopinfo.common.MemberCardAgent;
import com.dianping.shopinfo.common.MoreAgent;
import com.dianping.shopinfo.common.MovieAgent;
import com.dianping.shopinfo.common.NearByShopAgent;
import com.dianping.shopinfo.common.NearbyAgent;
import com.dianping.shopinfo.common.OnsaleAgent;
import com.dianping.shopinfo.common.PayBookingTAEntryAgent;
import com.dianping.shopinfo.common.PhoneAgent;
import com.dianping.shopinfo.common.PromoAgent;
import com.dianping.shopinfo.common.RecommendDishAgent;
import com.dianping.shopinfo.common.ReviewAgent;
import com.dianping.shopinfo.common.ShareAgent;
import com.dianping.shopinfo.common.ShopBranchAgent;
import com.dianping.shopinfo.common.ShopStatusAgent;
import com.dianping.shopinfo.common.ShoperEntranceAgent;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.fun.FunTuanAgent;
import com.dianping.shopinfo.fun.KTVBookTableAgent;
import com.dianping.shopinfo.fun.ScenicBookingAgent;
import com.dianping.shopinfo.fun.ScenicIntroAgent;
import com.dianping.shopinfo.oversea.ImpressionAgent;
import java.util.HashMap;
import java.util.Map;

public class FunShopInfoAgentListConfig
  implements AgentListConfig
{
  private DPObject shop;
  String shopView;

  public FunShopInfoAgentListConfig(DPObject paramDPObject)
  {
    this.shop = paramDPObject;
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    HashMap localHashMap = new HashMap();
    if ("common_funscenic".equals(this.shopView))
      localHashMap.put("shopinfo/intro", new AgentInfo(ScenicIntroAgent.class, "0200Basic.10Intro"));
    localHashMap.put("shopinfo/common_toolbar", new AgentInfo(ShopInfoToolbarAgent.class, ""));
    localHashMap.put("shopinfo/common_address", new AgentInfo(AddressAgent.class, "0200Basic.20Address"));
    localHashMap.put("shopinfo/common_phone", new AgentInfo(PhoneAgent.class, "0200Basic.30Phone"));
    localHashMap.put("shopinfo/default_favorite", new AgentInfo(FavoriteAgent.class, "5Fav."));
    localHashMap.put("shopinfo/default_history", new AgentInfo(HistoryAgent.class, ""));
    localHashMap.put("shopinfo/common_shopstatus", new AgentInfo(ShopStatusAgent.class, "0200Basic.00ShopStatus"));
    localHashMap.put("shopinfo/common_oldhead", new AgentInfo(TopAgent.class, "0200Basic.05Info"));
    localHashMap.put("shopinfo/commonimagegallery", new AgentInfo(CommonImageGalleryAgent.class, "0200Basic.06ImageGallery"));
    localHashMap.put("shopinfo/common_oldobqt", new AgentInfo(PayBookingTAEntryAgent.class, "0200Basic.50Takeaway"));
    localHashMap.put("shopinfo/ktvbooking", new AgentInfo(KTVBookTableAgent.class, "0450KTV.0100KTVBooking"));
    localHashMap.put("shopinfo/common_checkin", new AgentInfo(CheckinAgent.class, "4000Checkin."));
    if ("common_funall".equals(this.shopView))
      localHashMap.put("shopinfo/common_technician", new AgentInfo(CommonTechnicianAgent.class, "4500Technician."));
    localHashMap.put("shopinfo/common_announce", new AgentInfo(AnnounceAgent.class, "0000.10Announce"));
    localHashMap.put("shopinfo/common_recommend", new AgentInfo(RecommendDishAgent.class, "2000Dish.100"));
    localHashMap.put("shopinfo/nearby", new AgentInfo(NearbyAgent.class, "8000Nearby.70Nearby"));
    localHashMap.put("shopinfo/default_report", new AgentInfo(MoreAgent.class, ""));
    localHashMap.put("shopinfo/common_review", new AgentInfo(ReviewAgent.class, "3000Reivew."));
    localHashMap.put("shopinfo/default_share", new AgentInfo(ShareAgent.class, "2Share."));
    localHashMap.put("shopinfo/common_membercard", new AgentInfo(MemberCardAgent.class, "0500Cash.70MemberCard"));
    localHashMap.put("shopinfo/common_promo", new AgentInfo(PromoAgent.class, "0500Cash.50Promo."));
    localHashMap.put("shopinfo/common_onsale", new AgentInfo(OnsaleAgent.class, "0500Cash.60Onsale"));
    localHashMap.put("shopinfo/common_bank", new AgentInfo(BankAgent.class, "0500Cash.70Bank"));
    localHashMap.put("shopinfo/book", new AgentInfo(ScenicBookingAgent.class, "0470fun.10book"));
    localHashMap.put("shopinfo/tuan", new AgentInfo(FunTuanAgent.class, "0480Tuan.50Tuan"));
    localHashMap.put("shopinfo/common_shopinfo", new AgentInfo(CharacteristicAgent.class, "7000ShopInfo.10detail"));
    localHashMap.put("shopinfo/common_nearby", new AgentInfo(NearByShopAgent.class, "8500ShopInfo.20nearbyshop"));
    localHashMap.put("shopinfo/common_moviebooking", new AgentInfo(MovieAgent.class, "0400Movie."));
    localHashMap.put("shopinfo/common_branch", new AgentInfo(ShopBranchAgent.class, "7000ShopInfo.20branch"));
    localHashMap.put("shopinfo/common_impression", new AgentInfo(ImpressionAgent.class, "0500Impression."));
    localHashMap.put("shopinfo/common_mallinfo", new AgentInfo(MallInfoAgent.class, "7700shopinfo."));
    if (!"common_funscenic".equals(this.shopView))
      localHashMap.put("shopinfo/common_myshop", new AgentInfo(ShoperEntranceAgent.class, "8700ShopClaimEntrance."));
    if ("common_funall".equals(this.shopView))
      localHashMap.put("shopinfo/common_brandstory", new AgentInfo(CommonBrandStoryAgent.class, "8600BrandStory"));
    localHashMap.put("shopinfo/common_oldhui", new AgentInfo(HuiPayAgent.class, "0475HuiPay.10Hui"));
    localHashMap.put("shopinfo/common_activity", new AgentInfo(ActivityAgent.class, "0500Cash.75Activity"));
    localHashMap.put("shopinfo/common_favorshop", new AgentInfo(FavorShopAgent.class, "8900Favor.10shop"));
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
    this.shopView = this.shop.getObject("ClientShopStyle").getString("ShopView");
    return "common_funall".equals(this.shopView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fun.config.FunShopInfoAgentListConfig
 * JD-Core Version:    0.6.0
 */