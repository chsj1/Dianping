package com.dianping.selectdish;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.content.LocalBroadcastManager;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.RecommendInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class DishLikeManager
{
  public final ArrayList<RecommendInfo> recommendInfos = new ArrayList();

  public static void addDishLike(NovaActivity paramNovaActivity, int paramInt, RecommendInfo paramRecommendInfo, RequestHandler paramRequestHandler)
  {
    paramRecommendInfo.isRecommended = true;
    paramRecommendInfo.recommendNum += 1;
    sendAddDishLikeRequest(paramNovaActivity, paramInt, paramRecommendInfo.dishId, paramRequestHandler);
  }

  public static void deleteDishLike(NovaActivity paramNovaActivity, int paramInt, RecommendInfo paramRecommendInfo, RequestHandler paramRequestHandler)
  {
    paramRecommendInfo.isRecommended = false;
    paramRecommendInfo.recommendNum -= 1;
    sendDelDishLikeRequest(paramNovaActivity, paramInt, paramRecommendInfo.dishId, paramRequestHandler);
  }

  public static DishLikeManager getInstance()
  {
    return DishLikeManagerInner.INSTANCE;
  }

  public static void sendAddDishLikeRequest(NovaActivity paramNovaActivity, int paramInt1, int paramInt2, RequestHandler paramRequestHandler)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/adddishlike.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(paramInt1));
    localBuilder.appendQueryParameter("dishid", String.valueOf(paramInt2));
    paramNovaActivity.mapiService().exec(BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED), paramRequestHandler);
  }

  public static void sendBroadCast(DishInfo paramDishInfo, NovaActivity paramNovaActivity)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("dishid", paramDishInfo.dishId);
    localIntent.putExtra("dishrecommend", paramDishInfo.recommend);
    localIntent.setAction("com.dianping.selectdish.updatedishrecommend");
    LocalBroadcastManager.getInstance(paramNovaActivity).sendBroadcast(localIntent);
  }

  public static void sendDelDishLikeRequest(NovaActivity paramNovaActivity, int paramInt1, int paramInt2, RequestHandler paramRequestHandler)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/deldishlike.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(paramInt1));
    localBuilder.appendQueryParameter("dishid", String.valueOf(paramInt2));
    paramNovaActivity.mapiService().exec(BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED), paramRequestHandler);
  }

  public void addRecommendInfos(DPObject[] paramArrayOfDPObject)
  {
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      RecommendInfo localRecommendInfo = new RecommendInfo();
      localRecommendInfo.dishId = localDPObject.getInt("Id");
      localRecommendInfo.recommendNum = localDPObject.getInt("RecomCount");
      localRecommendInfo.isRecommended = localDPObject.getBoolean("IsDishLiked");
      this.recommendInfos.add(localRecommendInfo);
      i += 1;
    }
  }

  public RecommendInfo getRecommendInfo(int paramInt)
  {
    Iterator localIterator = this.recommendInfos.iterator();
    while (localIterator.hasNext())
    {
      RecommendInfo localRecommendInfo = (RecommendInfo)localIterator.next();
      if (paramInt == localRecommendInfo.dishId)
        return localRecommendInfo;
    }
    return null;
  }

  private static class DishLikeManagerInner
  {
    private static DishLikeManager INSTANCE = new DishLikeManager(null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.DishLikeManager
 * JD-Core Version:    0.6.0
 */