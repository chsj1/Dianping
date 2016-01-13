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
import com.dianping.v1.R.drawable;

public class WXQShare extends WXShare
{
  public static final String LABEL = "微信朋友圈";

  public String getElementId()
  {
    return "ShareTypeWXTimeline";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_wxq;
  }

  public String getLabel()
  {
    return "微信朋友圈";
  }

  public boolean share(Context paramContext, ShareHolder paramShareHolder)
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
        localIntent.putExtra("shareChannel", "微信朋友圈");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }

      public void onError()
      {
        ShareUtil.showToast(this.val$activity, "分享失败");
        Intent localIntent = new Intent();
        localIntent.putExtra("shareResult", "fail");
        localIntent.putExtra("shareChannel", "微信朋友圈");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }

      public void onSucess()
      {
        ShareUtil.showToast(this.val$activity, "分享成功");
        Intent localIntent = new Intent();
        localIntent.putExtra("shareResult", "success");
        localIntent.putExtra("shareChannel", "微信朋友圈");
        this.val$activity.setResult(-1, localIntent);
        this.val$activity.finish();
      }
    });
    if (TextUtils.isEmpty(paramShareHolder.content))
      return WXHelper.shareWithFriends(paramContext, paramShareHolder.title, paramShareHolder.desc, getThumbnail(paramContext, paramShareHolder.imageUrl), paramShareHolder.webUrl, true, paramShareHolder.extra);
    return WXHelper.shareWithFriends(paramContext, paramShareHolder.content, paramShareHolder.desc, getThumbnail(paramContext, paramShareHolder.imageUrl), paramShareHolder.webUrl, true, paramShareHolder.extra);
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(ShopUtil.getShopName(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopStar(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopPrice(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopCategory(paramDPObject));
    if (!TextUtils.isEmpty(ShopUtil.getShopShareContent(paramDPObject)))
    {
      localStringBuilder.append(",");
      localStringBuilder.append(ShopUtil.getShopShareContent(paramDPObject));
    }
    localShareHolder.title = localStringBuilder.toString();
    localShareHolder.imageUrl = ShopUtil.getShopImageUrl(paramDPObject);
    localShareHolder.webUrl = ("http://m.dianping.com/appshare/shop/" + String.valueOf(paramDPObject.getInt("ID")));
    return share(paramContext, localShareHolder);
  }

  public boolean shareWeb(Context paramContext, ShareHolder paramShareHolder)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = ("【" + paramShareHolder.title + "】" + paramShareHolder.desc);
    localShareHolder.desc = "";
    localShareHolder.imageUrl = paramShareHolder.imageUrl;
    localShareHolder.webUrl = paramShareHolder.webUrl;
    localShareHolder.extra = paramShareHolder.extra;
    localShareHolder.content = paramShareHolder.content;
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.WXQShare
 * JD-Core Version:    0.6.0
 */