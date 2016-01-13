package com.dianping.base.share.action.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.share.util.ShopUtil;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;

public class MailShare extends BaseShare
{
  public String getElementId()
  {
    return "ShareTypeMail";
  }

  public int getIconResId()
  {
    return R.drawable.share_to_icon_mail;
  }

  public String getLabel()
  {
    return "电子邮件";
  }

  public boolean share(Context paramContext, ShareHolder paramShareHolder)
  {
    Intent localIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:"));
    localIntent.putExtra("android.intent.extra.SUBJECT", paramShareHolder.title);
    localIntent.putExtra("android.intent.extra.TEXT", paramShareHolder.desc + " " + paramShareHolder.webUrl);
    try
    {
      paramContext.startActivity(localIntent);
      return true;
    }
    catch (Exception paramShareHolder)
    {
      Log.e("Share", paramShareHolder.toString());
      ShareUtil.showToast(paramContext, "您尚未安装邮件客户端");
    }
    return false;
  }

  public boolean shareApp(Context paramContext, ShareHolder paramShareHolder)
  {
    paramShareHolder.title = "您的朋友为您推荐一款手机软件";
    return share(paramContext, paramShareHolder);
  }

  public boolean shareShop(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = "推荐个商户给你";
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(ShopUtil.getShopName(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopStar(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopPrice(paramDPObject));
    localStringBuilder.append(",");
    localStringBuilder.append(ShopUtil.getShopRegion(paramDPObject));
    localStringBuilder.append(" ");
    localStringBuilder.append(ShopUtil.getShopCategory(paramDPObject));
    localStringBuilder.append(",");
    if (!TextUtils.isEmpty(ShopUtil.getShopShareContent(paramDPObject)))
    {
      localStringBuilder.append(ShopUtil.getShopShareContent(paramDPObject));
      localStringBuilder.append(",");
    }
    localStringBuilder.append(ShopUtil.getShopAddress(paramDPObject));
    localShareHolder.desc = localStringBuilder.toString();
    localShareHolder.webUrl = ("\n更多商户信息点评请查看：" + ShopUtil.getShopUrl(paramDPObject));
    return share(paramContext, localShareHolder);
  }

  public boolean shareWeb(Context paramContext, ShareHolder paramShareHolder)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = paramShareHolder.title;
    localShareHolder.desc = ("【" + paramShareHolder.title + "】" + paramShareHolder.desc);
    localShareHolder.webUrl = paramShareHolder.webUrl;
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.action.base.MailShare
 * JD-Core Version:    0.6.0
 */