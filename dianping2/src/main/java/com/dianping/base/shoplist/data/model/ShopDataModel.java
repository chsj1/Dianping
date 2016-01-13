package com.dianping.base.shoplist.data.model;

import android.text.TextUtils;
import com.dianping.archive.DPObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

public class ShopDataModel
{
  public static final int BANK_VISIBILITY = 4096;
  public static final int BANQUET_VISIBILITY = 524288;
  public static final int BOOK_VISIBILITY = 8;
  public static final int CHECKIN_VISIBILITY = 4;
  public static final int EDU_CLASS_TOGETHER_VISIBILITY = 2097152;
  public static final int GUAHAO_VISIBILITY = 4194304;
  public static final int HUI_VISIBILITY = 32768;
  public static final int LANDMARK_VISIBILITY = 128;
  public static final int MAX_ICON_COUNT = 5;
  public static final int MAX_TAG_LIST_COUNT = 4;
  public static final int MEMBER_CARD_VISIBILITY = 16;
  public static final int ORDER_VISIBILITY = 131072;
  public static final int PAY_VISIBILITY = 65536;
  public static final int PROMO_VISIBILITY = 2;
  public static final int QUEUE_VISIBILITY = 512;
  public static final int RENT_VISIBILITY = 8192;
  public static final String RMB_CHINESE = "￥";
  public static final String RMB_ENGLISH = "¥";
  public static final int SCENERY_ORDER_VISIBILITY = 32;
  public static final int SEAT_VISIBILITY = 1024;
  public static final int SELL_VISIBILITY = 16384;
  public static final int SHOP_CLOSE_VISIBILITY = 64;
  public static final int SHOP_PAUSE_VISIBILITY = 262144;
  public static final int TAG_LIST_TYPE_TAG_BLUE = 2;
  public static final int TAG_LIST_TYPE_TAG_GRAY = 3;
  public static final int TAG_LIST_TYPE_TAG_ORANGE = 1;
  public static final int TAG_LIST_TYPE_TEXT = 0;
  public static final int TAKEAWAY_VISIBILITY = 256;
  public static final int TOHOME_VISIBILITY = 2048;
  public static final int TUAN_VISIBILITY = 1;
  public String adReason;
  public int adType = 0;
  public String altName;
  public String authorityLabel;
  public int authorityLabelType;
  public String categoryName;
  public String certifiedHairDresserInfo;
  public String defaultPic;
  public String distanceText;
  public String eventText;
  public String extraJsonString;
  public String friendVisitInfo;
  public String fullName;
  private List<ShopDataModel.IconConfig> iconConfig = new ArrayList();
  public int iconVisibility;
  public int index;
  public boolean isAdShop;
  public boolean isArrived;
  public boolean isGlobal = false;
  public boolean isMall = false;
  public boolean isNewShop;
  public boolean isWedSelectiveShop;
  public boolean isWished;
  public double lat;
  public double lng;
  public String matchText;
  public String naviTitle;
  public String naviUrl;
  public String placeAddress;
  public String priceText;
  public String promoInfoInMall;
  public String regionName;
  public String scoreText;
  public String searchReason;
  public String searchReasonContent;
  public DPObject[] shopDealInfos;
  public int shopId;
  public String shopInfoInMall;
  public DPObject shopObj;
  public int shopPower;
  public String shopQueryId;
  public String shopStatusIcon;
  public String shopStatusText;
  public int shopStatusType;
  public String shopType;
  public Object tag;
  public String[] tagTextList;
  public int[] tagTypeList;
  public String title;
  public int viewType;

  public ShopDataModel(DPObject paramDPObject)
  {
    this.shopObj = paramDPObject;
    this.shopType = paramDPObject.getString("__name");
    this.isNewShop = paramDPObject.getBoolean("IsNewShop");
    this.defaultPic = paramDPObject.getString("DefaultPic");
    this.shopPower = paramDPObject.getInt("ShopPower");
    String str = paramDPObject.getString("BranchName");
    Object localObject2 = paramDPObject.getString("Name");
    Object localObject1 = localObject2;
    if (TextUtils.isEmpty((CharSequence)localObject2))
      localObject1 = "";
    localObject2 = new StringBuilder().append((String)localObject1);
    if ((str == null) || (str.length() == 0))
      localObject1 = "";
    while (true)
    {
      this.fullName = ((String)localObject1);
      this.isAdShop = paramDPObject.getBoolean("IsAdShop");
      this.extraJsonString = paramDPObject.getString("ExtraJson");
      if (!TextUtils.isEmpty(this.extraJsonString));
      try
      {
        localObject1 = new JSONObject(this.extraJsonString);
        this.searchReason = ((JSONObject)localObject1).optString("SearchReason");
        this.searchReasonContent = ((JSONObject)localObject1).optString("SearchReasonContent");
        if (this.isAdShop)
        {
          this.adType = ((JSONObject)localObject1).optInt("AdType", 17);
          this.adReason = ((JSONObject)localObject1).optString("AdReason", "");
        }
        this.isMall = ((JSONObject)localObject1).optBoolean("TopMall");
        if (this.isMall)
        {
          this.shopInfoInMall = ((JSONObject)localObject1).optString("ShopInfoInMall", "");
          this.promoInfoInMall = ((JSONObject)localObject1).optString("PromoInfoInMall", "");
          this.shopType = "TopMall";
        }
        this.authorityLabel = paramDPObject.getString("AuthorityLabel");
        this.authorityLabelType = paramDPObject.getInt("AuthorityLabelType");
        this.matchText = paramDPObject.getString("MatchText");
        this.regionName = paramDPObject.getString("RegionName");
        this.categoryName = paramDPObject.getString("CategoryName");
        this.scoreText = paramDPObject.getString("ScoreText");
        this.eventText = paramDPObject.getString("EventText");
        this.priceText = paramDPObject.getString("PriceText");
        if (!TextUtils.isEmpty(this.priceText))
          this.priceText = this.priceText.replace("￥", "¥");
        this.distanceText = paramDPObject.getString("DistanceText");
        this.lat = paramDPObject.getDouble("Latitude");
        this.lng = paramDPObject.getDouble("Longitude");
        this.placeAddress = paramDPObject.getString("placeAddress");
        this.index = paramDPObject.getInt("ListPosition");
        this.shopQueryId = paramDPObject.getString("ShopQueryId");
        this.shopId = paramDPObject.getInt("ID");
        this.friendVisitInfo = paramDPObject.getString("FriendsVisitInfo");
        this.title = paramDPObject.getString("Title");
        this.naviTitle = paramDPObject.getString("NaviTitle");
        this.naviUrl = paramDPObject.getString("NaviUrl");
        this.viewType = paramDPObject.getInt("ViewType");
        this.shopDealInfos = paramDPObject.getArray("ShopDealInfos");
        localObject1 = paramDPObject.getArray("TagList");
        if ((localObject1 != null) && (localObject1.length > 0))
        {
          this.tagTextList = new String[Math.min(4, localObject1.length)];
          this.tagTypeList = new int[Math.min(4, localObject1.length)];
          j = 0;
          i = 0;
          while (true)
          {
            if (i >= this.tagTextList.length)
              break label703;
            if (!TextUtils.isEmpty(localObject1[i].getString("Text")))
              break;
            i += 1;
            continue;
            localObject1 = "(" + str + ")";
          }
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          int j;
          int i;
          localException.printStackTrace();
          continue;
          this.tagTextList[j] = localException[i].getString("Text");
          this.tagTypeList[j] = localException[i].getInt("Type");
          j += 1;
        }
        label703: this.altName = paramDPObject.getString("AltName");
        addIcons(paramDPObject);
        setIconVisibility();
        this.certifiedHairDresserInfo = paramDPObject.getString("CertifiedHairDresserInfo");
        this.isArrived = paramDPObject.getBoolean("Arrived");
        this.isWished = paramDPObject.getBoolean("Wished");
        getShopStatusTagInfo(paramDPObject.getObject("ShopStatusTag"));
        this.isWedSelectiveShop = paramDPObject.getBoolean("IsWedSelectiveShop");
      }
    }
  }

  private void addIcon(int paramInt, boolean paramBoolean)
  {
    this.iconConfig.add(new ShopDataModel.IconConfig(paramInt, paramBoolean));
  }

  private void addIcons(DPObject paramDPObject)
  {
    boolean bool2 = true;
    boolean bool3 = true;
    label80: label338: label615: label620: if (this.viewType == 1)
    {
      if (dealTypeIconGone(0))
      {
        addIcon(65536, false);
        addIcon(32768, false);
        if ((!dealTypeIconGone(1)) && (!dealTypeIconGone(0)) && (!dealTypeIconGone(2)))
          break label501;
        addIcon(1, false);
        if (!dealTypeIconGone(4))
          break label537;
        addIcon(131072, false);
        addIcon(512, paramDPObject.getBoolean("IsQueueable"));
        if (dealTypeIconGone(3))
          break label558;
        if ((!paramDPObject.getBoolean(2034)) && (!paramDPObject.getBoolean("KtvBookable")) && (!paramDPObject.getBoolean("HotelBooking")) && (!paramDPObject.getBoolean("Bookable")) && (!paramDPObject.getBoolean("VerticalChannelBookable")) && (TextUtils.isEmpty(paramDPObject.getString("BookType"))))
          break label553;
        bool1 = true;
        addIcon(8, bool1);
        addIcon(256, paramDPObject.getBoolean("HasTakeaway"));
        addIcon(32, paramDPObject.getBoolean("TicketBookable"));
        if (!dealTypeIconGone(5))
          break label568;
        addIcon(2, false);
        if ((paramDPObject.getInt("ShopMemberCardID") <= 0) && ((paramDPObject.getArray("StoreCardGroupList") == null) || (paramDPObject.getArray("StoreCardGroupList").length <= 0)))
          break label615;
        bool1 = true;
        label248: addIcon(16, bool1);
        addIcon(4096, paramDPObject.getBoolean("HasBankCard"));
        addIcon(1024, paramDPObject.getBoolean("MovieBookable"));
        addIcon(2048, paramDPObject.getBoolean("IsToHomeShop"));
        addIcon(8192, paramDPObject.getBoolean("Rentable"));
        addIcon(16384, paramDPObject.getBoolean("Saleable"));
        if (paramDPObject.getInt("Status") != 1)
          break label620;
        bool1 = true;
        addIcon(64, bool1);
        if (paramDPObject.getInt("Status") != 3)
          break label625;
      }
      label496: label625: for (bool1 = bool3; ; bool1 = false)
      {
        addIcon(262144, bool1);
        addIcon(128, paramDPObject.getBoolean("IsLandMark"));
        addIcon(524288, paramDPObject.getBoolean("IsBanquetShop"));
        addIcon(2097152, paramDPObject.getBoolean("IsEduClassTogether"));
        addIcon(4194304, paramDPObject.getBoolean("IsHospitalQueueable"));
        return;
        if ((!paramDPObject.getBoolean("HasMOPay")) && (!paramDPObject.getBoolean("HasCarMoPay")) && (paramDPObject.getBoolean("HasPay")))
        {
          bool1 = true;
          addIcon(65536, bool1);
          if ((!paramDPObject.getBoolean("HasCarMoPay")) && (!paramDPObject.getBoolean("HasMOPay")))
            break label496;
        }
        for (bool1 = true; ; bool1 = false)
        {
          addIcon(32768, bool1);
          break;
          bool1 = false;
          break label452;
        }
        label501: if ((paramDPObject.getBoolean("HasDeals")) || (paramDPObject.getBoolean("HasMeiTuanDeal")));
        for (bool1 = true; ; bool1 = false)
        {
          addIcon(1, bool1);
          break;
        }
        addIcon(131072, paramDPObject.getBoolean("IsOrderDish"));
        break label80;
        bool1 = false;
        break label167;
        addIcon(8, false);
        break label174;
        if (((paramDPObject.getArray("Promos") != null) && (paramDPObject.getArray("Promos").length > 0)) || (paramDPObject.getBoolean("HasPromo")));
        for (bool1 = true; ; bool1 = false)
        {
          addIcon(2, bool1);
          break;
        }
        bool1 = false;
        break label248;
        bool1 = false;
        break label338;
      }
    }
    label167: label174: label452: if ((paramDPObject.getBoolean("HasCarMoPay")) || (paramDPObject.getBoolean("HasMOPay")))
    {
      bool1 = true;
      addIcon(32768, bool1);
      if ((paramDPObject.getBoolean("HasMOPay")) || (paramDPObject.getBoolean("HasCarMoPay")) || (!paramDPObject.getBoolean("HasPay")))
        break label1118;
      bool1 = true;
      addIcon(65536, bool1);
      if ((!paramDPObject.getBoolean("HasDeals")) && (!paramDPObject.getBoolean("HasMeiTuanDeal")))
        break label1123;
      bool1 = true;
      label720: addIcon(1, bool1);
      if (((paramDPObject.getArray("Promos") == null) || (paramDPObject.getArray("Promos").length <= 0)) && (!paramDPObject.getBoolean("HasPromo")))
        break label1128;
      bool1 = true;
      label759: addIcon(2, bool1);
      if (paramDPObject.getObject("Campaign") == null)
        break label1133;
      bool1 = true;
      addIcon(4, bool1);
      if ((!paramDPObject.getBoolean(2034)) && (!paramDPObject.getBoolean("KtvBookable")) && (!paramDPObject.getBoolean("HotelBooking")) && (!paramDPObject.getBoolean("Bookable")) && (!paramDPObject.getBoolean("VerticalChannelBookable")) && (TextUtils.isEmpty(paramDPObject.getString("BookType"))))
        break label1138;
      bool1 = true;
      label848: addIcon(8, bool1);
      if ((paramDPObject.getInt("ShopMemberCardID") <= 0) && ((paramDPObject.getArray("StoreCardGroupList") == null) || (paramDPObject.getArray("StoreCardGroupList").length <= 0)))
        break label1143;
      bool1 = true;
      label888: addIcon(16, bool1);
      addIcon(32, paramDPObject.getBoolean("TicketBookable"));
      if (paramDPObject.getInt("Status") != 1)
        break label1148;
      bool1 = true;
      addIcon(64, bool1);
      if (paramDPObject.getInt("Status") != 3)
        break label1153;
    }
    label537: label553: label558: label568: for (boolean bool1 = bool2; ; bool1 = false)
    {
      addIcon(262144, bool1);
      addIcon(128, paramDPObject.getBoolean("IsLandMark"));
      addIcon(256, paramDPObject.getBoolean("HasTakeaway"));
      addIcon(131072, paramDPObject.getBoolean("IsOrderDish"));
      addIcon(512, paramDPObject.getBoolean("IsQueueable"));
      addIcon(1024, paramDPObject.getBoolean("MovieBookable"));
      addIcon(2048, paramDPObject.getBoolean("IsToHomeShop"));
      addIcon(4096, paramDPObject.getBoolean("HasBankCard"));
      addIcon(8192, paramDPObject.getBoolean("Rentable"));
      addIcon(16384, paramDPObject.getBoolean("Saleable"));
      addIcon(524288, paramDPObject.getBoolean("IsBanquetShop"));
      addIcon(2097152, paramDPObject.getBoolean("IsEduClassTogether"));
      addIcon(4194304, paramDPObject.getBoolean("IsHospitalQueueable"));
      return;
      bool1 = false;
      break;
      label1118: bool1 = false;
      break label691;
      label1123: bool1 = false;
      break label720;
      label1128: bool1 = false;
      break label759;
      label1133: bool1 = false;
      break label777;
      label1138: bool1 = false;
      break label848;
      label1143: bool1 = false;
      break label888;
      label1148: bool1 = false;
      break label921;
    }
  }

  private boolean dealTypeIconGone(int paramInt)
  {
    if (this.shopDealInfos == null);
    while (true)
    {
      return false;
      DPObject[] arrayOfDPObject = this.shopDealInfos;
      int j = arrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        if ((localDPObject != null) && (localDPObject.getInt("DealType") == paramInt))
          return true;
        i += 1;
      }
    }
  }

  private void getShopStatusTagInfo(DPObject paramDPObject)
  {
    if (paramDPObject != null)
    {
      this.shopStatusIcon = paramDPObject.getString("Icon");
      this.shopStatusType = paramDPObject.getInt("Type");
      this.shopStatusText = paramDPObject.getString("Text");
    }
  }

  private void setIconVisibility()
  {
    this.iconVisibility = 0;
    int i = 0;
    Iterator localIterator = this.iconConfig.iterator();
    while (localIterator.hasNext())
    {
      ShopDataModel.IconConfig localIconConfig = (ShopDataModel.IconConfig)localIterator.next();
      if ((i >= 5) || (!ShopDataModel.IconConfig.access$000(localIconConfig)))
        continue;
      i += 1;
      this.iconVisibility += ShopDataModel.IconConfig.access$100(localIconConfig);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.model.ShopDataModel
 * JD-Core Version:    0.6.0
 */