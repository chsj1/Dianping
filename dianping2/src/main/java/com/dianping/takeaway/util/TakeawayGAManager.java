package com.dianping.takeaway.util;

import android.content.Context;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.statistics.StatisticsService;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class TakeawayGAManager
{
  private static void statisticsEvent(Context paramContext, GAInfo paramGAInfo)
  {
    if ((paramContext instanceof DPActivity))
    {
      ((DPActivity)paramContext).statisticsEvent(paramGAInfo.category, paramGAInfo.action, paramGAInfo.lable, paramGAInfo.value, paramGAInfo.extras);
      return;
    }
    ((StatisticsService)DPApplication.instance().getService("statistics")).event(paramGAInfo.category, paramGAInfo.action, paramGAInfo.lable, paramGAInfo.value, paramGAInfo.extras);
  }

  public static void statistics_takeaway6_dish_next(Context paramContext, String paramString1, String paramString2)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dish_next");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("queryid", paramString2);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_dish_normal(Context paramContext, String paramString1, String paramString2)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dish_normal");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("takeawaysource", paramString2);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_dish_quantity(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dish_quantity");
    localGAInfo.addNameValuePair("index", paramString1);
    localGAInfo.addNameValuePair("dishid", paramString2);
    localGAInfo.addNameValuePair("shopid", paramString3);
    localGAInfo.addNameValuePair("count", paramString4);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_dishtips_ood(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dishtips_ood");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("lat", paramString2);
    localGAInfo.addNameValuePair("lng", paramString3);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_dishtips_ood_add(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dishtips_ood_add");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("lat", paramString2);
    localGAInfo.addNameValuePair("lng", paramString3);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_dishtips_ood_shop(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dishtips_ood_shop");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("lat", paramString2);
    localGAInfo.addNameValuePair("lng", paramString3);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_dishtips_retry(Context paramContext, String paramString1, String paramString2)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dishtips_retry");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("network", paramString2);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_dishtips_retryclk(Context paramContext, String paramString1, String paramString2)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_dishtips_retryclk");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("network", paramString2);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takeaway6_order_confirmclk(Context paramContext, String paramString1, int paramInt, String paramString2)
  {
    paramString1 = new GAInfo("takeaway6", "takeaway6_order_confirmclk", paramString1, paramInt);
    paramString1.addNameValuePair("queryid", paramString2);
    statisticsEvent(paramContext, paramString1);
  }

  public static void statistics_takeaway6_success_share(Context paramContext, String paramString1, String paramString2)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_success_share");
    localGAInfo.addNameValuePair("channel", paramString1);
    localGAInfo.addNameValuePair("title", paramString2);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takwaway6_brand_shopclk(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_brand_shopclk");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("index", paramString2);
    localGAInfo.addNameValuePair("lng", paramString3);
    localGAInfo.addNameValuePair("lat", paramString4);
    statisticsEvent(paramContext, localGAInfo);
  }

  public static void statistics_takwaway6_hot_shopclk(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    GAInfo localGAInfo = new GAInfo("takeaway6", "takeaway6_hotshops_shopclk");
    localGAInfo.addNameValuePair("shopid", paramString1);
    localGAInfo.addNameValuePair("index", paramString2);
    localGAInfo.addNameValuePair("lng", paramString3);
    localGAInfo.addNameValuePair("lat", paramString4);
    statisticsEvent(paramContext, localGAInfo);
  }

  private static class GAInfo
  {
    public final String action;
    public final String category;
    public List<NameValuePair> extras;
    public final String lable;
    public final int value;

    GAInfo(String paramString1, String paramString2)
    {
      this(paramString1, paramString2, null, 0);
    }

    GAInfo(String paramString1, String paramString2, String paramString3, int paramInt)
    {
      this.category = paramString1;
      this.action = paramString2;
      this.lable = paramString3;
      this.value = paramInt;
      this.extras = new ArrayList();
    }

    public void addNameValuePair(String paramString1, String paramString2)
    {
      paramString1 = new BasicNameValuePair(paramString1, paramString2);
      this.extras.add(paramString1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.util.TakeawayGAManager
 * JD-Core Version:    0.6.0
 */