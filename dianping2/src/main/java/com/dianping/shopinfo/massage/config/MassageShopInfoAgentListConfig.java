package com.dianping.shopinfo.massage.config;

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
import com.dianping.shopinfo.common.CommonTechnicianAgent;
import com.dianping.shopinfo.common.FavorShopAgent;
import com.dianping.shopinfo.common.FavoriteAgent;
import com.dianping.shopinfo.common.HistoryAgent;
import com.dianping.shopinfo.common.HuiPayAgent;
import com.dianping.shopinfo.common.MemberCardAgent;
import com.dianping.shopinfo.common.MoreAgent;
import com.dianping.shopinfo.common.NearByShopAgent;
import com.dianping.shopinfo.common.OnsaleAgent;
import com.dianping.shopinfo.common.PayBookingTAEntryAgent;
import com.dianping.shopinfo.common.PhoneAgent;
import com.dianping.shopinfo.common.PromoAgent;
import com.dianping.shopinfo.common.ReviewAgent;
import com.dianping.shopinfo.common.ShareAgent;
import com.dianping.shopinfo.common.ShopBranchAgent;
import com.dianping.shopinfo.common.ShopInfoTuanAgent;
import com.dianping.shopinfo.common.ShopStatusAgent;
import com.dianping.shopinfo.common.ShoperEntranceAgent;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.tohome.TohomeBookingAgent;
import com.dianping.shopinfo.verticalchannel.book.BookingAgent;
import com.dianping.shopinfo.verticalchannel.book.FreeBookingAgent;
import com.dianping.shopinfo.verticalchannel.book.ProductAgent;
import java.util.HashMap;
import java.util.Map;

public class MassageShopInfoAgentListConfig
  implements AgentListConfig
{
  private DPObject shop;

  public MassageShopInfoAgentListConfig(DPObject paramDPObject)
  {
    this.shop = paramDPObject;
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("shopinfo/common_toolbar", new AgentInfo(ShopInfoToolbarAgent.class, ""));
    localHashMap.put("shopinfo/common_announce", new AgentInfo(AnnounceAgent.class, "0000.10Announce"));
    localHashMap.put("shopinfo/common_shopstatus", new AgentInfo(ShopStatusAgent.class, "0200Basic.00ShopStatus"));
    localHashMap.put("shopinfo/common_oldhead", new AgentInfo(TopAgent.class, "0200Basic.05Info"));
    localHashMap.put("shopinfo/massagebooking", new AgentInfo(BookingAgent.class, "0200Basic.07Book"));
    localHashMap.put("shopinfo/common_address", new AgentInfo(AddressAgent.class, "0200Basic.10Address"));
    localHashMap.put("shopinfo/common_phone", new AgentInfo(PhoneAgent.class, "0200Basic.30Phone"));
    localHashMap.put("shopinfo/massagefreebooking", new AgentInfo(FreeBookingAgent.class, "0200Basic.40FreeBooking"));
    localHashMap.put("shopinfo/tohomebooking", new AgentInfo(TohomeBookingAgent.class, "0200Basic.45ToHomeBook"));
    localHashMap.put("shopinfo/common_oldobqt", new AgentInfo(PayBookingTAEntryAgent.class, "0200Basic.50Takeaway"));
    localHashMap.put("shopinfo/common_oldhui", new AgentInfo(HuiPayAgent.class, "0475HuiPay.10Hui"));
    localHashMap.put("shopinfo/common_oldtuan", new AgentInfo(ShopInfoTuanAgent.class, "0480Tuan.50Tuan"));
    localHashMap.put("shopinfo/common_promo", new AgentInfo(PromoAgent.class, "0500Cash.50Promo."));
    localHashMap.put("shopinfo/common_onsale", new AgentInfo(OnsaleAgent.class, "0500Cash.60Onsale"));
    localHashMap.put("shopinfo/common_bank", new AgentInfo(BankAgent.class, "0500Cash.70Bank"));
    localHashMap.put("shopinfo/common_membercard", new AgentInfo(MemberCardAgent.class, "0500Cash.70MemberCard"));
    localHashMap.put("shopinfo/common_activity", new AgentInfo(ActivityAgent.class, "0500Cash.75Activity"));
    localHashMap.put("shopinfo/massageproduct", new AgentInfo(ProductAgent.class, "1100Product.10ProductList"));
    localHashMap.put("shopinfo/common_shopinfo", new AgentInfo(CharacteristicAgent.class, "7000ShopInfo.10Basic"));
    localHashMap.put("shopinfo/common_branch", new AgentInfo(ShopBranchAgent.class, "7000ShopInfo.20RelatedShops"));
    localHashMap.put("shopinfo/common_brandstory", new AgentInfo(CommonBrandStoryAgent.class, "8600BrandStory."));
    localHashMap.put("shopinfo/common_myshop", new AgentInfo(ShoperEntranceAgent.class, "8700ShopClaimEntrance."));
    localHashMap.put("shopinfo/technician", new AgentInfo(CommonTechnicianAgent.class, "1000.technician"));
    localHashMap.put("shopinfo/common_nearby", new AgentInfo(NearByShopAgent.class, "8500ShopInfo.20nearbyshop"));
    localHashMap.put("shopinfo/common_favorshop", new AgentInfo(FavorShopAgent.class, "8900Favor.10shop"));
    localHashMap.put("shopinfo/default_history", new AgentInfo(HistoryAgent.class, ""));
    localHashMap.put("shopinfo/default_share", new AgentInfo(ShareAgent.class, ""));
    localHashMap.put("shopinfo/default_favorite", new AgentInfo(FavoriteAgent.class, ""));
    localHashMap.put("shopinfo/common_checkin", new AgentInfo(CheckinAgent.class, ""));
    localHashMap.put("shopinfo/common_review", new AgentInfo(ReviewAgent.class, ""));
    localHashMap.put("shopinfo/default_report", new AgentInfo(MoreAgent.class, ""));
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
    return "entertainment_massage".equals(this.shop.getObject("ClientShopStyle").getString("ShopView"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.massage.config.MassageShopInfoAgentListConfig
 * JD-Core Version:    0.6.0
 */