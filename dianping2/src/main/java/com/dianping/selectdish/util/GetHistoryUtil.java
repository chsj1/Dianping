package com.dianping.selectdish.util;

import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.TogetherCartManager;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class GetHistoryUtil
{
  private DPActivity activity;
  private boolean isTogetherMenu;
  private GetHistoryListener mGetHistoryListener;
  private MApiRequest mRequestHistory;
  private FullRequestHandle<MApiRequest, MApiResponse> mapiHandler = new FullRequestHandle()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (GetHistoryUtil.this.mGetHistoryListener != null)
        GetHistoryUtil.this.mGetHistoryListener.onFail();
      GetHistoryUtil.access$302(GetHistoryUtil.this, null);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (GetHistoryUtil.this.mGetHistoryListener != null)
        GetHistoryUtil.this.mGetHistoryListener.onFinish();
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest instanceof DPObject))
      {
        paramMApiResponse = (DPObject)paramMApiRequest;
        paramMApiRequest = paramMApiResponse.getArray("UserHistoryGiftList");
        paramMApiResponse = paramMApiResponse.getArray("UserHistoryDishList");
        int i;
        if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
        {
          GiftInfo[] arrayOfGiftInfo = new GiftInfo[paramMApiRequest.length];
          i = 0;
          while (i < paramMApiRequest.length)
          {
            arrayOfGiftInfo[i] = new GiftInfo(paramMApiRequest[i]);
            i += 1;
          }
          GetHistoryUtil.this.setUserHistoryGifts(arrayOfGiftInfo);
        }
        if (paramMApiResponse != null)
        {
          i = 0;
          while (i < paramMApiResponse.length)
          {
            GetHistoryUtil.this.setDishHistoryCount(paramMApiResponse[i].getInt("DishId"), paramMApiResponse[i].getInt("DishCount"));
            i += 1;
          }
        }
      }
    }

    public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
    {
      if (GetHistoryUtil.this.mGetHistoryListener != null)
        GetHistoryUtil.this.mGetHistoryListener.onProgress();
    }

    public void onRequestStart(MApiRequest paramMApiRequest)
    {
      if (GetHistoryUtil.this.mGetHistoryListener != null)
        GetHistoryUtil.this.mGetHistoryListener.onStart();
    }
  };

  public GetHistoryUtil(DPActivity paramDPActivity, boolean paramBoolean)
  {
    this.activity = paramDPActivity;
    this.isTogetherMenu = paramBoolean;
  }

  private ArrayList<CartItem> getAllDishes()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().getAllDishesinTotalDish();
    return NewCartManager.getInstance().getAllDishes();
  }

  private int getShopId()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().getShopId();
    return NewCartManager.getInstance().getShopId();
  }

  private void setDishHistoryCount(int paramInt1, int paramInt2)
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager.getInstance().setDishHistoryCount(paramInt1, paramInt2);
      return;
    }
    NewCartManager.getInstance().setDishHistoryCount(paramInt1, paramInt2);
  }

  private void setUserHistoryGifts(GiftInfo[] paramArrayOfGiftInfo)
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager.getInstance().setUserHistoryGifts(paramArrayOfGiftInfo);
      return;
    }
    NewCartManager.getInstance().setUserHistoryGifts(paramArrayOfGiftInfo);
  }

  public void getUserHistory()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/menuhistory.bin").buildUpon();
    ArrayList localArrayList = new ArrayList();
    Object localObject = getAllDishes();
    StringBuilder localStringBuilder = new StringBuilder();
    localObject = ((ArrayList)localObject).iterator();
    while (((Iterator)localObject).hasNext())
      localStringBuilder.append(((CartItem)((Iterator)localObject).next()).dishInfo.dishId).append(",");
    if (localStringBuilder.length() > 0)
    {
      localArrayList.add("dishids");
      localArrayList.add(localStringBuilder.substring(0, localStringBuilder.length() - 1));
    }
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(getShopId()));
    this.mRequestHistory = BasicMApiRequest.mapiPost(localBuilder.toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.mRequestHistory, this.mapiHandler);
  }

  public void releaseHistoryRequest()
  {
    if (this.mRequestHistory != null)
      this.activity.mapiService().abort(this.mRequestHistory, this.mapiHandler, true);
  }

  public void removeGetHistoryListener()
  {
    this.mGetHistoryListener = null;
  }

  public void setGetHistoryListener(GetHistoryListener paramGetHistoryListener)
  {
    this.mGetHistoryListener = paramGetHistoryListener;
  }

  public static abstract interface GetHistoryListener
  {
    public abstract void onFail();

    public abstract void onFinish();

    public abstract void onProgress();

    public abstract void onStart();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.util.GetHistoryUtil
 * JD-Core Version:    0.6.0
 */