package com.dianping.base.thirdparty.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.util.Log;
import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage.Req;
import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage.Resp;
import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage.WXCardItem;
import com.tencent.mm.sdk.openapi.IWXAPI;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeiXinCard
{
  public static final int CARD_TYPE_COUPON = 1;
  public static final int CARD_TYPE_DISCOUNT = 2;
  public static final int CARD_TYPE_UNKNOWN = -1;
  private static final String TAG = "WeiXinCard";
  public static final int WEIXINCARD_ERRORCODE_OK = 0;
  public static final int WEIXINCARD_ERRORCODE_PARAM_ERROR = 1;
  public static final int WEIXINCARD_ERRORCODE_THIRDPARTY_EXECUTE_ERROR = 4;
  public static final int WEIXINCARD_ERRORCODE_THIRDPARTY_NOTINSTALLED = 2;
  public static final int WEIXINCARD_ERRORCODE_THIRDPARTY_NOTSUPPORTED = 3;
  public static final int WEIXINCARD_ERRORCODE_UNKNOWN = -1;
  protected static WeiXinCard weixinCard;
  public final int[] SUPPORTED_CARD_TYPE_LIST = { 1, 2 };
  protected WeiXinCardItem weixinCardItem = new WeiXinCardItem();

  public static WeiXinCard instance()
  {
    if (weixinCard == null)
      weixinCard = new WeiXinCard();
    return weixinCard;
  }

  public void addToWeiXinCard(int paramInt, List<DPObject> paramList, Activity paramActivity, IAddToWeiXinCardResult paramIAddToWeiXinCardResult)
  {
    Log.v("WeiXinCard", "addToWeiXinCard: weixinCardInfo=" + paramList + ",activity=" + paramActivity.toString());
    this.weixinCardItem.set(paramIAddToWeiXinCardResult);
    paramIAddToWeiXinCardResult = checkArgs(paramInt, paramList);
    if (!TextUtils.isEmpty(paramIAddToWeiXinCardResult))
    {
      if (this.weixinCardItem.checkArgs())
        this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(1, paramIAddToWeiXinCardResult);
      this.weixinCardItem.clear();
    }
    do
    {
      return;
      if (!WXHelper.isWXAppInstalled(paramActivity, false))
      {
        if (this.weixinCardItem.checkArgs())
          this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(2, "您尚未安装微信");
        this.weixinCardItem.clear();
        return;
      }
      paramIAddToWeiXinCardResult = new ArrayList();
      paramInt = 0;
      while (paramInt < paramList.size())
      {
        DPObject localDPObject = (DPObject)paramList.get(paramInt);
        AddCardToWXCardPackage.WXCardItem localWXCardItem = new AddCardToWXCardPackage.WXCardItem();
        localWXCardItem.cardId = localDPObject.getString("CardId");
        localWXCardItem.cardExtMsg = localDPObject.getString("CardExt");
        localWXCardItem.cardState = 1;
        Log.v("WeiXinCard", "cardId=" + localWXCardItem.cardId + ",cardExtMsg=" + localWXCardItem.cardExtMsg + ",cardState=" + localWXCardItem.cardState);
        paramIAddToWeiXinCardResult.add(localWXCardItem);
        paramInt += 1;
      }
      paramList = new AddCardToWXCardPackage.Req();
      paramList.cardArrary = paramIAddToWeiXinCardResult;
    }
    while (WXHelper.getWXAPI(paramActivity).sendReq(paramList));
    if (this.weixinCardItem.checkArgs())
      this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(4, "添加失败");
    this.weixinCardItem.clear();
  }

  public void addToWeiXinCard(int paramInt, JSONArray paramJSONArray, Activity paramActivity, IAddToWeiXinCardResult paramIAddToWeiXinCardResult)
  {
    Log.v("WeiXinCard", "addToWeiXinCard: weixinCardInfo=" + paramJSONArray + ",activity=" + paramActivity.toString());
    this.weixinCardItem.set(paramIAddToWeiXinCardResult);
    paramIAddToWeiXinCardResult = checkArgs(paramInt, paramJSONArray);
    if (!TextUtils.isEmpty(paramIAddToWeiXinCardResult))
    {
      if (this.weixinCardItem.checkArgs())
        this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(1, paramIAddToWeiXinCardResult);
      this.weixinCardItem.clear();
    }
    do
    {
      return;
      if (!WXHelper.isWXAppInstalled(paramActivity, false))
      {
        if (this.weixinCardItem.checkArgs())
          this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(2, "您尚未安装微信");
        this.weixinCardItem.clear();
        return;
      }
      paramIAddToWeiXinCardResult = new ArrayList();
      paramInt = 0;
      while (true)
        if (paramInt < paramJSONArray.length())
          try
          {
            JSONObject localJSONObject = (JSONObject)paramJSONArray.get(paramInt);
            AddCardToWXCardPackage.WXCardItem localWXCardItem = new AddCardToWXCardPackage.WXCardItem();
            localWXCardItem.cardId = localJSONObject.getString("cardId");
            localWXCardItem.cardExtMsg = localJSONObject.getString("cardExt");
            localWXCardItem.cardState = 1;
            Log.v("WeiXinCard", "cardId=" + localWXCardItem.cardId + ",cardExtMsg=" + localWXCardItem.cardExtMsg + ",cardState=" + localWXCardItem.cardState);
            paramIAddToWeiXinCardResult.add(localWXCardItem);
            paramInt += 1;
          }
          catch (JSONException localJSONException)
          {
            while (true)
              localJSONException.printStackTrace();
          }
      paramJSONArray = new AddCardToWXCardPackage.Req();
      paramJSONArray.cardArrary = paramIAddToWeiXinCardResult;
    }
    while (WXHelper.getWXAPI(paramActivity).sendReq(paramJSONArray));
    if (this.weixinCardItem.checkArgs())
      this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(4, "添加失败");
    this.weixinCardItem.clear();
  }

  public void addToWeiXinCardResult(Bundle paramBundle)
  {
    paramBundle = new AddCardToWXCardPackage.Resp(paramBundle);
    if (this.weixinCardItem.checkArgs())
    {
      if (paramBundle.errCode != 0)
        break label201;
      paramBundle = paramBundle.cardArrary;
      Log.v("WeiXinCard", "onReceive()");
      if (paramBundle.size() > 0)
      {
        int i = 0;
        while (i < paramBundle.size())
        {
          AddCardToWXCardPackage.WXCardItem localWXCardItem = (AddCardToWXCardPackage.WXCardItem)paramBundle.get(i);
          String str1 = localWXCardItem.cardId;
          String str2 = localWXCardItem.cardExtMsg;
          int j = localWXCardItem.cardState;
          Log.v("WeiXinCard", "cardId=" + str1 + ",cardExtMsg=" + str2 + ",cardState=" + j);
          i += 1;
        }
        if (((AddCardToWXCardPackage.WXCardItem)paramBundle.get(0)).cardState != 1)
          break label183;
        this.weixinCardItem.addToWeiXinCardResult.onAddCardSuccess(0, "添加成功");
      }
    }
    while (true)
    {
      this.weixinCardItem.clear();
      return;
      label183: this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(4, "添加失败");
      continue;
      label201: this.weixinCardItem.addToWeiXinCardResult.onAddCardFailed(4, paramBundle.errStr);
    }
  }

  protected String checkArgs(int paramInt, List<DPObject> paramList)
  {
    int k = 0;
    int i = 0;
    while (true)
    {
      int j = k;
      if (i < this.SUPPORTED_CARD_TYPE_LIST.length)
      {
        if (paramInt == this.SUPPORTED_CARD_TYPE_LIST[i])
          j = 1;
      }
      else
      {
        if (j != 0)
          break;
        return "卡类型错误";
      }
      i += 1;
    }
    if ((paramList == null) || (paramList.size() == 0))
      return "卡包列表为空";
    return "";
  }

  protected String checkArgs(int paramInt, JSONArray paramJSONArray)
  {
    int k = 0;
    int i = 0;
    while (true)
    {
      int j = k;
      if (i < this.SUPPORTED_CARD_TYPE_LIST.length)
      {
        if (paramInt == this.SUPPORTED_CARD_TYPE_LIST[i])
          j = 1;
      }
      else
      {
        if (j != 0)
          break;
        return "卡类型错误";
      }
      i += 1;
    }
    if ((paramJSONArray == null) || (paramJSONArray.length() == 0))
      return "卡包列表为空";
    return "";
  }

  public void release()
  {
    this.weixinCardItem.clear();
  }

  public void uninit()
  {
    Log.v("WeiXinCard", "uninit()");
    release();
  }

  public static abstract interface IAddToWeiXinCardResult
  {
    public abstract void onAddCardFailed(int paramInt, String paramString);

    public abstract void onAddCardSuccess(int paramInt, String paramString);
  }

  private class WeiXinCardItem
  {
    protected WeiXinCard.IAddToWeiXinCardResult addToWeiXinCardResult;

    public WeiXinCardItem()
    {
      clear();
    }

    public boolean checkArgs()
    {
      return this.addToWeiXinCardResult != null;
    }

    public void clear()
    {
      this.addToWeiXinCardResult = null;
    }

    public void set(WeiXinCard.IAddToWeiXinCardResult paramIAddToWeiXinCardResult)
    {
      this.addToWeiXinCardResult = paramIAddToWeiXinCardResult;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.thirdparty.wxapi.WeiXinCard
 * JD-Core Version:    0.6.0
 */