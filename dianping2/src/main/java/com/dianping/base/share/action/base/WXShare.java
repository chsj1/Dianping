package com.dianping.base.share.action.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.share.util.ShopUtil;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.base.thirdparty.wxapi.WXHelper.IWxApiListener;
import com.dianping.util.DPUrl;
import com.dianping.v1.R.drawable;

public class WXShare extends BaseShare
{
  public static final String LABEL = "微信好友";

  private boolean share(Context paramContext, ShareHolder paramShareHolder, boolean paramBoolean)
  {
    if (WXHelper.getWXAPI(paramContext) == null)
    {
      ShareUtil.showToast(paramContext, "微信服务出错，稍后再试");
      return false;
    }
    WXHelper.registerWxApiListener(new WXHelper.IWxApiListener((Activity)paramContext)
    {
      public void onCancel()
      {
        ShareUtil.showToast(this.val$activity, "取消分享");
        Intent localIntent = new Intent();
        localIntent.putExtra("shareResult", "cancel");
        localIntent.putExtra("shareChannel", "微信好友");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }

      public void onError()
      {
        ShareUtil.showToast(this.val$activity, "分享失败");
        Intent localIntent = new Intent();
        localIntent.putExtra("shareResult", "fail");
        localIntent.putExtra("shareChannel", "微信好友");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }

      public void onSucess()
      {
        ShareUtil.showToast(this.val$activity, "分享成功");
        Intent localIntent = new Intent();
        localIntent.putExtra("shareResult", "success");
        localIntent.putExtra("shareChannel", "微信好友");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }
    });
    return WXHelper.shareWithFriend(paramContext, paramShareHolder.title, paramShareHolder.desc, getThumbnail(paramContext, paramShareHolder.imageUrl), paramShareHolder.webUrl, paramBoolean, true, paramShareHolder.extra);
  }

  private boolean shareShop(Context paramContext, DPObject paramDPObject, boolean paramBoolean)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = ShopUtil.getShopName(paramDPObject);
    localShareHolder.imageUrl = ShopUtil.getShopImageUrl(paramDPObject);
    localShareHolder.webUrl = ("http://m.dianping.com/appshare/shop/" + String.valueOf(paramDPObject.getInt("ID")));
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(ShopUtil.getShopStar(paramDPObject));
    ((StringBuilder)localObject).append("\n");
    ((StringBuilder)localObject).append(ShopUtil.getShopPrice(paramDPObject));
    ((StringBuilder)localObject).append("\n");
    if (!TextUtils.isEmpty(ShopUtil.getShopShareContent(paramDPObject)))
      ((StringBuilder)localObject).append(ShopUtil.getShopShareContent(paramDPObject));
    while (true)
    {
      ((StringBuilder)localObject).append("\n");
      ((StringBuilder)localObject).append(ShopUtil.getShopAddress(paramDPObject));
      localShareHolder.desc = ((StringBuilder)localObject).toString();
      localObject = new DPUrl("dianping://shopinfo");
      ((DPUrl)localObject).putParam("shopid", paramDPObject.getInt("ID"));
      ((DPUrl)localObject).putParam("utm", "wechatraise");
      localShareHolder.scheme = ((DPUrl)localObject).toString();
      return share(paramContext, localShareHolder, paramBoolean);
      ((StringBuilder)localObject).append(ShopUtil.getShopRegion(paramDPObject));
      ((StringBuilder)localObject).append(" ");
      ((StringBuilder)localObject).append(ShopUtil.getShopCategory(paramDPObject));
    }
  }

  public String getElementId()
  {
    return "ShareTypeWXSession";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_wx;
  }

  public String getLabel()
  {
    return "微信好友";
  }

  public boolean share(Context paramContext, ShareHolder paramShareHolder)
  {
    return share(paramContext, paramShareHolder, false);
  }

  public boolean shareApp(Context paramContext, ShareHolder paramShareHolder)
  {
    paramShareHolder.title = "推荐一个找美食的应用！";
    paramShareHolder.desc = "随时随地找美食，享优惠，抢团购，从大众点评手机客户端开始！";
    paramShareHolder.imageUrl = getDefaultLogoUrl();
    paramShareHolder.webUrl = "http://m.api.dianping.com/weixinaudit?shoppage=false";
    return share(paramContext, paramShareHolder);
  }

  public boolean shareLuckyMoney(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = paramDPObject.getString("Title");
    localShareHolder.desc = paramDPObject.getString("ShareMsg");
    localShareHolder.imageUrl = paramDPObject.getString("ShareImg");
    localShareHolder.webUrl = paramDPObject.getString("Url");
    return share(paramContext, localShareHolder);
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    return shareShop(paramContext, paramDPObject, false);
  }

  public boolean shareShopCallback(Context paramContext, DPObject paramDPObject)
  {
    return shareShop(paramContext, paramDPObject, true);
  }

  public boolean shareWeb(Context paramContext, ShareHolder paramShareHolder)
  {
    if (TextUtils.isEmpty(paramShareHolder.imageUrl))
      paramShareHolder.imageUrl = getDefaultLogoUrl();
    return super.shareWeb(paramContext, paramShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.WXShare
 * JD-Core Version:    0.6.0
 */