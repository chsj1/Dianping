package com.dianping.base.share.action.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.share.util.ShopUtil;
import com.dianping.base.sso.QQSSOLogin;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.tencent.open.utils.TemporaryStorage;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQShare extends BaseShare
{
  public static final String LABEL = "QQ";
  protected static final String QQ_APP_ID = "200002";

  public static boolean shareToQQ(Activity paramActivity, Bundle paramBundle)
  {
    return shareToQQ(paramActivity, paramBundle, new IUiListener(paramActivity)
    {
      public void onCancel()
      {
        ShareUtil.showToast(this.val$activity, "取消分享");
        Intent localIntent = new Intent();
        localIntent.putExtra("shareResult", "cancel");
        localIntent.putExtra("shareChannel", "QQ");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }

      public void onComplete(Object paramObject)
      {
        ShareUtil.showToast(this.val$activity, "分享成功");
        paramObject = new Intent();
        paramObject.putExtra("shareResult", "success");
        paramObject.putExtra("shareChannel", "QQ");
        this.val$activity.setResult(-1, paramObject);
        this.val$activity.finish();
      }

      public void onError(UiError paramUiError)
      {
        ShareUtil.showToast(this.val$activity, "分享失败");
        paramUiError = new Intent();
        paramUiError.putExtra("shareResult", "fail");
        paramUiError.putExtra("shareChannel", "QQ");
        this.val$activity.setResult(-1, paramUiError);
        this.val$activity.finish();
      }
    });
  }

  public static boolean shareToQQ(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    try
    {
      TemporaryStorage.remove("shareToQQ");
      Tencent.createInstance("200002", paramActivity).shareToQQ(paramActivity, paramBundle, paramIUiListener);
      return true;
    }
    catch (Exception paramBundle)
    {
      Log.e("Share", paramBundle.toString());
      ShareUtil.showToast(paramActivity, "分享失败");
    }
    return false;
  }

  protected boolean doShare(Activity paramActivity, ShareHolder paramShareHolder)
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("req_type", 1);
    localBundle.putString("title", paramShareHolder.title);
    localBundle.putString("summary", paramShareHolder.desc);
    localBundle.putString("imageUrl", paramShareHolder.imageUrl);
    localBundle.putString("targetUrl", paramShareHolder.webUrl);
    localBundle.putString("appName", "大众点评");
    localBundle.putInt("cflag", 2);
    return shareToQQ(paramActivity, localBundle);
  }

  public String getElementId()
  {
    return "ShareTypeQQ";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_qq;
  }

  public String getLabel()
  {
    return "QQ";
  }

  public boolean share(Context paramContext, ShareHolder paramShareHolder)
  {
    if (!QQSSOLogin.isSupportQQ(paramContext))
    {
      ShareUtil.showToast(paramContext, "您尚未安装QQ");
      return false;
    }
    try
    {
      boolean bool = doShare((Activity)paramContext, paramShareHolder);
      return bool;
    }
    catch (ClassCastException paramContext)
    {
      Log.e("Share", paramContext.toString());
    }
    return false;
  }

  public boolean shareApp(Context paramContext, ShareHolder paramShareHolder)
  {
    paramShareHolder.title = "推荐一个找美食的应用！";
    paramShareHolder.desc = "随时随地找美食，享优惠，抢团购，从大众点评手机客户端开始！";
    paramShareHolder.imageUrl = getDefaultLogoUrl();
    paramShareHolder.webUrl = "http://m.api.dianping.com/weixinaudit?shoppage=false";
    return share(paramContext, paramShareHolder);
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.webUrl = ("http://m.dianping.com/appshare/shop/" + String.valueOf(paramDPObject.getInt("ID")));
    localShareHolder.title = ShopUtil.getShopName(paramDPObject);
    localShareHolder.imageUrl = ShopUtil.getShopImageUrl(paramDPObject);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(ShopUtil.getShopStar(paramDPObject));
    localStringBuilder.append("\n");
    localStringBuilder.append(ShopUtil.getShopPrice(paramDPObject));
    localStringBuilder.append("\n");
    if (!TextUtils.isEmpty(ShopUtil.getShopShareContent(paramDPObject)))
      localStringBuilder.append(ShopUtil.getShopShareContent(paramDPObject));
    while (true)
    {
      localStringBuilder.append("\n");
      localStringBuilder.append(ShopUtil.getShopAddress(paramDPObject));
      localShareHolder.desc = localStringBuilder.toString();
      return share(paramContext, localShareHolder);
      localStringBuilder.append(ShopUtil.getShopRegion(paramDPObject));
      localStringBuilder.append(" ");
      localStringBuilder.append(ShopUtil.getShopCategory(paramDPObject));
    }
  }

  public boolean shareWeb(Context paramContext, ShareHolder paramShareHolder)
  {
    if (TextUtils.isEmpty(paramShareHolder.imageUrl))
      paramShareHolder.imageUrl = getDefaultLogoUrl();
    return super.shareWeb(paramContext, paramShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.QQShare
 * JD-Core Version:    0.6.0
 */